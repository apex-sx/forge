/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author ApexSX
 */
package io.github.apex_sx.forge.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BaseContainer} 单元测试。
 * <p>
 * 通过测试专用子类 {@link Probe} 暴露 protected 能力，分别验证哈希扰动、容量规范化、
 * 掩码回绕、扩容搬迁、阈值判断、修改计数快速失败，以及内置原始类型迭代器的两种遍历模式。
 *
 * @author ApexSX
 */
@SuppressWarnings("all")
@DisplayName("BaseContainer 集合基类")
class BaseContainerTest {
    @Nested
    @DisplayName("哈希扰动")
    class HashTest {
        /**
         * 验证各基础类型的 hash 重载最终都落到 hashcode(int)，且结果确定。
         */
        @Test
        @DisplayName("各类型 hash 确定且等价于包装后调用 hashcode")
        void hashIsDeterministic() {
            assertEquals(BaseContainer.hash(1), BaseContainer.hash(1));
            assertEquals(BaseContainer.hash(1L), BaseContainer.hash(1L));
            assertEquals(BaseContainer.hash(1.0f), BaseContainer.hash(1.0f));
            assertEquals(BaseContainer.hash(1.0d), BaseContainer.hash(1.0d));
            assertEquals(BaseContainer.hash(1), BaseContainer.hashcode(Integer.hashCode(1)));
            assertEquals(BaseContainer.hash(1L), BaseContainer.hashcode(Long.hashCode(1L)));
            assertEquals(BaseContainer.hash(1.0f), BaseContainer.hashcode(Float.hashCode(1.0f)));
            assertEquals(BaseContainer.hash(1.0d), BaseContainer.hashcode(Double.hashCode(1.0d)));
        }

        /**
         * 逐位复现 MurmurHash3 末轮混淆公式，锁定扰动算法行为。
         */
        @Test
        @DisplayName("hashcode 使用 MurmurHash3 末轮混淆")
        void hashcodeMatchesMurmurFinalizer() {
            int input = 0x12345678;
            int h = input;
            h ^= h >>> 16;
            h *= 0x85ebca6b;
            h ^= h >>> 13;
            h *= 0xc2b2ae35;
            h ^= h >>> 16;
            assertEquals(h, BaseContainer.hashcode(input));
            assertNotEquals(0x12345678, BaseContainer.hashcode(0x12345678));
        }
    }

    @Nested
    @DisplayName("容量与阈值")
    class CapacityTest {
        /**
         * init 使用 highestOneBit(n-1)<<1 把容量向上取整到 2 的幂，且最小为 4。
         */
        @Test
        @DisplayName("初始容量向上取整为 2 的幂且不小于 4")
        void capacityRoundedUpToPowerOfTwo() {
            assertEquals(4, new Probe(0).capacity());
            assertEquals(4, new Probe(-10).capacity());
            assertEquals(4, new Probe(4).capacity());
            assertEquals(8, new Probe(5).capacity());
            assertEquals(8, new Probe(7).capacity());
            assertEquals(8, new Probe(8).capacity());
            assertEquals(16, new Probe(9).capacity());
        }

        /**
         * 装载因子分子分母会被校正到 [1, +∞) 后取 min/max，保证因子不超过 1。
         */
        @Test
        @DisplayName("装载因子被归一化且不超过 1")
        void loadFactorNormalized() {
            Probe half = new Probe(16, 4, 2);
            half.setSize(7);
            assertFalse(half.over());
            half.setSize(8);
            assertTrue(half.over());
            Probe full = new Probe(16, 4, 4);
            full.setSize(15);
            assertFalse(full.over());
            full.setSize(16);
            assertTrue(full.over());
            Probe invalid = new Probe(16, -1, 0);
            invalid.setSize(15);
            assertFalse(invalid.over());
            invalid.setSize(16);
            assertTrue(invalid.over());
        }

        /**
         * ensureCapacity 只在达到阈值时触发一次扩容，且保持 size 与修改计数不变。
         */
        @Test
        @DisplayName("扩容翻倍且保持元素数量与修改计数")
        void growDoublesCapacityAndKeepsState() {
            Probe probe = new Probe(4);
            probe.setSize(3);
            probe.mark();
            int expectedMark = probe.markValue();
            probe.incSize();
            probe.ensure();
            assertEquals(8, probe.capacity());
            assertEquals(4, probe.size());
            assertEquals(expectedMark, probe.markValue());
            assertEquals(1, probe.resizeCount);
            assertEquals(8, probe.lastNewCapacity);
        }

        /**
         * 各类型数组工厂方法创建的数组长度始终等于当前容量。
         */
        @Test
        @DisplayName("扩容后数组工厂返回新容量长度的数组")
        void arrayFactoriesUseCurrentCapacity() {
            Probe probe = new Probe(4);
            probe.setSize(4);
            probe.ensure();
            assertEquals(8, probe.ints().length);
            assertEquals(8, probe.longs().length);
            assertEquals(8, probe.floats().length);
            assertEquals(8, probe.doubles().length);
            assertEquals(8, probe.objects().length);
        }

        /**
         * 批量预检允许写入后恰好等于阈值；非正数计数直接返回；不足时一次扩容到位。
         */
        @Test
        @DisplayName("批量预扩容一次到位且空批量不扩容")
        void ensureCapacityForBulk() {
            Probe probe = new Probe(4);
            probe.ensureFor(4);
            assertEquals(4, probe.capacity());
            assertEquals(0, probe.resizeCount);
            probe.ensureFor(5);
            assertEquals(8, probe.capacity());
            assertEquals(1, probe.resizeCount);
            probe.ensureFor(0);
            probe.ensureFor(-1);
            assertEquals(8, probe.capacity());
            assertEquals(1, probe.resizeCount);
        }
    }

    @Nested
    @DisplayName("下标与掩码")
    class IndexTest {
        /**
         * 容量恒为 2 的幂，value & mask 对负数与超容量值同样回绕到 [0, capacity)。
         */
        @Test
        @DisplayName("maskIndex 对负数与超容量值回绕")
        void maskWrapsNegativeAndOverflow() {
            Probe probe = new Probe(8);
            assertEquals(0, probe.masked(0));
            assertEquals(7, probe.masked(7));
            assertEquals(0, probe.masked(8));
            assertEquals(1, probe.masked(9));
            assertEquals(7, probe.masked(-1));
            assertEquals(6, probe.masked(-2));
        }

        /**
         * 线性探测的前后驱在表头表尾互相回绕。
         */
        @Test
        @DisplayName("nextIndex 与 prevIndex 在边界回绕")
        void nextAndPrevWrap() {
            Probe probe = new Probe(8);
            assertEquals(1, probe.next(0));
            assertEquals(0, probe.next(7));
            assertEquals(7, probe.prev(0));
            assertEquals(6, probe.prev(7));
        }

        /**
         * 三种基础类型的起始槽位一定落在容量范围内。
         */
        @Test
        @DisplayName("slotOf 结果始终落在容量范围内")
        void slotsStayInRange() {
            Probe probe = new Probe(16);
            for (int i = -100; i < 100; i++) {
                int slot = probe.keySlot(i);
                assertTrue(slot >= 0 && slot < 16);
                slot = probe.longSlot((long) i * 1_000_000L);
                assertTrue(slot >= 0 && slot < 16);
                slot = probe.doubleSlot(i / 3.0);
                assertTrue(slot >= 0 && slot < 16);
            }
        }
    }

    @Nested
    @DisplayName("状态校验")
    class StateTest {
        /**
         * size(int) 只允许 [0, capacity]。
         */
        @Test
        @DisplayName("size 设置越界时抛出异常")
        void sizeRangeCheck() {
            Probe probe = new Probe(8);
            assertDoesNotThrow(() -> probe.setSize(0));
            assertDoesNotThrow(() -> probe.setSize(8));
            assertThrows(IndexOutOfBoundsException.class, () -> probe.setSize(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> probe.setSize(9));
        }

        /**
         * 元素下标校验只接受 [0, size())。
         */
        @Test
        @DisplayName("verifyElementIndex 只接受有效元素下标")
        void elementIndexCheck() {
            Probe probe = new Probe(8);
            probe.setSize(2);
            assertDoesNotThrow(() -> probe.checkIndex(0));
            assertDoesNotThrow(() -> probe.checkIndex(1));
            assertThrows(IndexOutOfBoundsException.class, () -> probe.checkIndex(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> probe.checkIndex(2));
        }

        /**
         * 修改计数快照不一致时必须快速失败。
         */
        @Test
        @DisplayName("修改计数不一致触发快速失败")
        void modificationCheck() {
            Probe probe = new Probe(8);
            int expected = probe.markValue();
            assertDoesNotThrow(() -> probe.verify(expected));
            probe.mark();
            assertThrows(ConcurrentModificationException.class, () -> probe.verify(expected));
        }

        /**
         * 空槽元素标记可独立读写，不影响 size。
         */
        @Test
        @DisplayName("空槽元素标记可独立读写")
        void emptyElementFlag() {
            Probe probe = new Probe(8);
            assertFalse(probe.emptyElement());
            probe.emptyElement(true);
            assertTrue(probe.emptyElement());
            assertEquals(0, probe.size());
            probe.emptyElement(false);
            assertFalse(probe.emptyElement());
        }

        /**
         * isEmpty / isNotEmpty / clear 的语义：清空只重置数量而保留容量。
         */
        @Test
        @DisplayName("empty 判断与 clear 保留容量")
        void clearKeepsCapacity() {
            Probe probe = new Probe(8);
            probe.setSize(3);
            assertTrue(probe.isNotEmpty());
            assertFalse(probe.isEmpty());
            probe.clear();
            assertTrue(probe.isEmpty());
            assertEquals(8, probe.capacity());
            assertEquals(0, probe.size());
        }
    }

    @Nested
    @DisplayName("原始类型迭代器")
    class IteratorTest {
        /**
         * 定长模式按下标顺序遍历，不跳过任何值（包括 0）。
         */
        @Test
        @DisplayName("定长模式不跳过任何值")
        void fixedLengthMode() {
            Probe probe = new Probe(8);
            int[] source = {5, 0, 7, 0, 9};
            PrimitiveIterator.OfInt iterator = new BaseContainer.OfInt(probe, source, 5, false);
            assertEquals(5, iterator.nextInt());
            assertEquals(0, iterator.nextInt());
            assertEquals(7, iterator.nextInt());
            assertEquals(0, iterator.nextInt());
            assertEquals(9, iterator.nextInt());
            assertFalse(iterator.hasNext());
            assertThrows(NoSuchElementException.class, iterator::nextInt);
        }

        /**
         * 空槽标记模式跳过所有等于标记值的槽位。
         */
        @Test
        @DisplayName("空槽标记模式跳过标记值")
        void markerModeSkipsEmptySlots() {
            Probe probe = new Probe(8);
            int[] source = {5, 0, 7, 0, 9};
            PrimitiveIterator.OfInt iterator = new BaseContainer.OfInt(probe, source, 0, true);
            assertEquals(5, iterator.nextInt());
            assertEquals(7, iterator.nextInt());
            assertEquals(9, iterator.nextInt());
            assertFalse(iterator.hasNext());
        }

        /**
         * 单独存储的空槽元素优先返回，且可通过重写 emptyValue 定制返回值。
         */
        @Test
        @DisplayName("空槽元素优先返回且支持定制值")
        void emptyElementReturnedFirst() {
            Probe probe = new Probe(8);
            probe.emptyElement(true);
            int[] source = {1, 2, 0};
            PrimitiveIterator.OfInt iterator = new BaseContainer.OfInt(probe, source, 0, true) {
                @Override
                protected int emptyValue() {
                    return -1;
                }
            };
            assertTrue(iterator.hasNext());
            assertEquals(-1, iterator.nextInt());
            assertEquals(1, iterator.nextInt());
            assertEquals(2, iterator.nextInt());
            assertFalse(iterator.hasNext());
        }

        /**
         * valueAt 可重写为从另一个数组取值，用于映射的值迭代器。
         */
        @Test
        @DisplayName("valueAt 可重写为读取其他数组")
        void valueAtCanBeOverridden() {
            Probe probe = new Probe(8);
            int[] source = {1, 2, 3};
            int[] values = {10, 0, 30};
            PrimitiveIterator.OfInt iterator = new BaseContainer.OfInt(probe, source, 0, true) {
                @Override
                protected int valueAt(int index) {
                    return values[index];
                }
            };
            assertEquals(10, iterator.nextInt());
            assertEquals(0, iterator.nextInt());
            assertEquals(30, iterator.nextInt());
            assertFalse(iterator.hasNext());
        }

        /**
         * 遍历期间容器结构变化会让 hasNext / nextX 快速失败。
         */
        @Test
        @DisplayName("遍历期间结构修改触发快速失败")
        void iteratorFailFast() {
            Probe probe = new Probe(8);
            int[] source = {1, 2, 3};
            PrimitiveIterator.OfInt iterator = new BaseContainer.OfInt(probe, source, 3, false);
            assertEquals(1, iterator.nextInt());
            probe.mark();
            assertThrows(ConcurrentModificationException.class, iterator::hasNext);
            assertThrows(ConcurrentModificationException.class, iterator::nextInt);
        }

        /**
         * 定长模式拒绝负元素数量；空槽标记模式把同一个入参解释为标记值，故允许负数。
         */
        @Test
        @DisplayName("定长模式拒绝负数量，标记模式允许负数标记")
        void negativeInputSemantics() {
            Probe probe = new Probe(8);
            assertThrows(IllegalArgumentException.class,
                    () -> new BaseContainer.OfInt(probe, new int[0], -1, false));
            assertDoesNotThrow(
                    () -> new BaseContainer.OfInt(probe, new int[0], -1, true));
        }
    }

    /**
     * 测试专用子类：把 protected 方法包装为公开方法，并统计扩容回调次数。
     * <p>
     * onResize 只记录参数，不迁移真实数据，足以验证基类的容量/阈值/计数逻辑。
     */
    private static final class Probe extends BaseContainer {
        /**
         * 扩容回调次数。
         */
        int resizeCount;
        /**
         * 最近一次扩容回调传入的新容量。
         */
        int lastNewCapacity;

        Probe(int initialCapacity) {
            this(initialCapacity, 1, 1);
        }

        Probe(int initialCapacity, int numeratorFactor, int denominatorFactor) {
            super(initialCapacity, numeratorFactor, denominatorFactor);
        }

        @Override
        protected void onResize(int newCapacity) {
            resizeCount++;
            lastNewCapacity = newCapacity;
        }

        int masked(int value) {
            return maskIndex(value);
        }

        int next(int index) {
            return nextIndex(index);
        }

        int prev(int index) {
            return prevIndex(index);
        }

        int keySlot(int key) {
            return slotOf(key);
        }

        int longSlot(long key) {
            return slotOf(key);
        }

        int doubleSlot(double key) {
            return slotOf(key);
        }

        void setSize(int value) {
            size(value);
        }

        void incSize() {
            increaseSize();
        }

        void decSize() {
            decreaseSize();
        }

        boolean over() {
            return isOverThreshold();
        }

        boolean over(int count) {
            return isOverThreshold(count);
        }

        void ensure() {
            ensureCapacity();
        }

        void ensureFor(int count) {
            ensureCapacityFor(count);
        }

        boolean emptyElement() {
            return isEmptyElementPresent();
        }

        void emptyElement(boolean value) {
            setEmptyElementPresent(value);
        }

        void checkIndex(int index) {
            verifyElementIndex(index);
        }

        void mark() {
            markModification();
        }

        int markValue() {
            return modificationMark();
        }

        void verify(int expected) {
            verifyModification(expected);
        }

        int[] ints() {
            return newIntArray();
        }

        long[] longs() {
            return newLongArray();
        }

        float[] floats() {
            return newFloatArray();
        }

        double[] doubles() {
            return newDoubleArray();
        }

        Object[] objects() {
            return newObjectArray();
        }
    }
}

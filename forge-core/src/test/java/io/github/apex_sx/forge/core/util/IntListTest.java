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
import java.util.PrimitiveIterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link IntList} 单元测试。
 * <p>
 * 覆盖尾部/指定下标插入、批量追加（含自追加）、查找与各类删除、排序反转、
 * 数组导出、遍历与快速失败，以及扩容后元素顺序与内容保持。
 *
 * @author ApexSX
 */
@SuppressWarnings("all")
@DisplayName("IntList 无装箱 int 列表")
class IntListTest {
    @Nested
    @DisplayName("添加与访问")
    class AddTest {
        /**
         * 默认容量为 16，构造后为空。
         */
        @Test
        @DisplayName("默认构造容量为 16 且为空")
        void defaultConstruction() {
            IntList list = new IntList();
            assertEquals(16, list.capacity());
            assertTrue(list.isEmpty());
            assertEquals(0, list.size());
        }

        /**
         * add 追加到末尾，get 只接受 [0, size())。
         */
        @Test
        @DisplayName("尾部追加与下标访问")
        void addAndGet() {
            IntList list = new IntList(4);
            list.add(10);
            list.add(20);
            list.add(30);
            assertEquals(3, list.size());
            assertEquals(10, list.get(0));
            assertEquals(20, list.get(1));
            assertEquals(30, list.get(2));
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
        }

        /**
         * 指定下标插入时，原位置及其后元素整体后移一位；允许在末尾插入。
         */
        @Test
        @DisplayName("指定下标插入使后续元素后移")
        void insertAt() {
            IntList list = new IntList();
            list.add(1);
            list.add(3);
            list.add(1, 2);
            assertArrayEquals(new int[]{1, 2, 3}, list.toArray());
            list.add(0, 0);
            assertArrayEquals(new int[]{0, 1, 2, 3}, list.toArray());
            list.add(list.size(), 4);
            assertArrayEquals(new int[]{0, 1, 2, 3, 4}, list.toArray());
            assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 9));
            assertThrows(IndexOutOfBoundsException.class, () -> list.add(list.size() + 1, 9));
        }

        /**
         * 单次插入超过容量会扩容，原有元素顺序不变。
         */
        @Test
        @DisplayName("追加超过容量时自动扩容")
        void growOnAdd() {
            IntList list = new IntList(4);
            for (int i = 0; i < 20; i++) {
                list.add(i);
            }
            assertEquals(20, list.size());
            assertTrue(list.capacity() >= 20);
            for (int i = 0; i < 20; i++) {
                assertEquals(i, list.get(i));
            }
        }

        /**
         * addAll 支持数组与另一个列表，空输入不改变结构，null 输入被拒绝。
         */
        @Test
        @DisplayName("批量追加数组与另一个列表")
        void addAll() {
            IntList list = new IntList(4);
            list.addAll(new int[]{1, 2, 3});
            list.addAll(new int[]{4, 5});
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, list.toArray());
            IntList other = new IntList();
            other.addAll(new int[]{6, 7});
            list.addAll(other);
            assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7}, list.toArray());
            list.addAll(new int[0]);
            list.addAll(new IntList());
            assertEquals(7, list.size());
            assertThrows(NullPointerException.class, () -> list.addAll((int[]) null));
            assertThrows(NullPointerException.class, () -> list.addAll((IntList) null));
        }

        /**
         * 把列表自身追加到末尾：源区间 [0, size) 与目标区间 [size, 2*size) 不重叠，结果正确。
         */
        @Test
        @DisplayName("自追加复制原有元素")
        void addSelf() {
            IntList list = new IntList();
            list.addAll(new int[]{1, 2});
            list.addAll(list);
            assertArrayEquals(new int[]{1, 2, 1, 2}, list.toArray());
            assertEquals(4, list.size());
        }

        /**
         * set 只改值不改结构，不会使已创建的迭代器快速失败。
         */
        @Test
        @DisplayName("set 修改元素不触发快速失败")
        void setDoesNotInvalidateIterator() {
            IntList list = new IntList();
            list.addAll(new int[]{1, 2, 3});
            PrimitiveIterator.OfInt iterator = list.iterator();
            list.set(1, 99);
            assertEquals(1, iterator.nextInt());
            assertEquals(99, iterator.nextInt());
            assertEquals(3, iterator.nextInt());
            assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, 0));
            assertThrows(IndexOutOfBoundsException.class, () -> list.set(3, 0));
        }
    }

    @Nested
    @DisplayName("查找与删除")
    class RemoveTest {
        /**
         * indexOf / lastIndexOf 返回首次/末次出现位置，不存在返回 -1。
         */
        @Test
        @DisplayName("indexOf、lastIndexOf 与 contains")
        void search() {
            IntList list = new IntList();
            list.addAll(new int[]{1, 2, 3, 2, 1});
            assertEquals(1, list.indexOf(2));
            assertEquals(3, list.lastIndexOf(2));
            assertEquals(-1, list.indexOf(99));
            assertEquals(-1, list.lastIndexOf(99));
            assertTrue(list.contains(3));
            assertFalse(list.contains(99));
        }

        /**
         * remove 只删除第一个匹配元素，返回是否删除成功。
         */
        @Test
        @DisplayName("remove 删除首个匹配元素")
        void removeFirstMatch() {
            IntList list = new IntList();
            list.addAll(new int[]{1, 2, 3, 2});
            assertTrue(list.remove(2));
            assertArrayEquals(new int[]{1, 3, 2}, list.toArray());
            assertFalse(list.remove(99));
            assertEquals(3, list.size());
        }

        /**
         * removeAll 单次遍历就地压缩，返回实际删除数量并保持剩余元素顺序。
         */
        @Test
        @DisplayName("removeAll 删除全部匹配元素")
        void removeAllMatches() {
            IntList list = new IntList();
            list.addAll(new int[]{7, 1, 7, 2, 7, 3});
            assertEquals(3, list.removeAll(7));
            assertArrayEquals(new int[]{1, 2, 3}, list.toArray());
            assertEquals(0, list.removeAll(99));
            assertEquals(3, list.size());
        }

        /**
         * removeAt 返回被删除元素，后续元素整体前移；下标越界时抛出异常。
         */
        @Test
        @DisplayName("removeAt 删除指定下标")
        void removeAt() {
            IntList list = new IntList();
            list.addAll(new int[]{1, 2, 3, 4});
            assertEquals(2, list.removeAt(1));
            assertArrayEquals(new int[]{1, 3, 4}, list.toArray());
            assertEquals(4, list.removeAt(2));
            assertArrayEquals(new int[]{1, 3}, list.toArray());
            assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(2));
        }
    }

    @Nested
    @DisplayName("排序、反转与导出")
    class OrderTest {
        /**
         * sort 按自然升序原地排序，reverse 原地反转。
         */
        @Test
        @DisplayName("sort 与 reverse")
        void sortAndReverse() {
            IntList list = new IntList();
            list.addAll(new int[]{5, 1, 4, 2, 3});
            list.sort();
            assertArrayEquals(new int[]{1, 2, 3, 4, 5}, list.toArray());
            list.reverse();
            assertArrayEquals(new int[]{5, 4, 3, 2, 1}, list.toArray());
        }

        /**
         * toArray 返回新数组，修改返回数组不影响列表内部存储。
         */
        @Test
        @DisplayName("toArray 返回独立副本")
        void toArrayIsCopy() {
            IntList list = new IntList();
            list.add(1);
            int[] array = list.toArray();
            array[0] = 99;
            assertEquals(1, list.get(0));
            assertEquals(1, list.toArray().length);
        }

        /**
         * clear 只重置元素数量并保留容量，清空后可以继续添加。
         */
        @Test
        @DisplayName("clear 保留容量并可继续添加")
        void clearKeepsCapacity() {
            IntList list = new IntList();
            list.addAll(new int[]{1, 2, 3});
            int capacity = list.capacity();
            list.clear();
            assertTrue(list.isEmpty());
            assertEquals(capacity, list.capacity());
            list.add(9);
            assertEquals(9, list.get(0));
            assertEquals(1, list.size());
        }
    }

    @Nested
    @DisplayName("遍历与快速失败")
    class TraverseTest {
        /**
         * forEach 按下标顺序回调，拒绝 null 消费者。
         */
        @Test
        @DisplayName("forEach 按下标顺序遍历")
        void forEachInOrder() {
            IntList list = new IntList();
            list.addAll(new int[]{1, 2, 3});
            int[] result = new int[3];
            int[] cursor = {0};
            list.forEach(value -> result[cursor[0]++] = value);
            assertArrayEquals(new int[]{1, 2, 3}, result);
            assertThrows(NullPointerException.class, () -> list.forEach(null));
        }

        /**
         * 迭代器遍历期间发生结构修改（add）时快速失败。
         */
        @Test
        @DisplayName("迭代器在结构修改后快速失败")
        void iteratorFailFast() {
            IntList list = new IntList();
            list.addAll(new int[]{1, 2, 3});
            PrimitiveIterator.OfInt iterator = list.iterator();
            list.add(4);
            assertThrows(ConcurrentModificationException.class, iterator::hasNext);
            assertThrows(ConcurrentModificationException.class, iterator::nextInt);
        }

        /**
         * forEach 回调中修改结构同样触发快速失败。
         */
        @Test
        @DisplayName("forEach 回调中修改结构快速失败")
        void forEachFailFast() {
            IntList list = new IntList();
            list.addAll(new int[]{1, 2, 3});
            assertThrows(ConcurrentModificationException.class,
                    () -> list.forEach(value -> {
                        if (value == 1) {
                            list.add(4);
                        }
                    }));
        }

        /**
         * 扩容后迭代器仍按新数组顺序遍历全部元素。
         */
        @Test
        @DisplayName("扩容后迭代器顺序正确")
        void iteratorAfterGrow() {
            IntList list = new IntList(4);
            for (int i = 0; i < 10; i++) {
                list.add(i);
            }
            PrimitiveIterator.OfInt iterator = list.iterator();
            for (int i = 0; i < 10; i++) {
                assertTrue(iterator.hasNext());
                assertEquals(i, iterator.nextInt());
            }
            assertFalse(iterator.hasNext());
        }
    }
}

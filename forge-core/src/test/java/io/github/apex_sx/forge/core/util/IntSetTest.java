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
import java.util.HashSet;
import java.util.PrimitiveIterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link IntSet} 单元测试。
 * <p>
 * 覆盖去重添加、emptyElement 作为普通元素、删除后的探测链回填、扩容后元素保持、
 * 迭代器/forEach/数组导出的一致性，以及快速失败与清空复用。
 *
 * @author ApexSX
 */
@SuppressWarnings("all")
@DisplayName("IntSet 无装箱 int 集合")
class IntSetTest {
    @Nested
    @DisplayName("基本集合操作")
    class BasicTest {
        /**
         * add 对新元素返回 true，重复元素返回 false；remove 语义相同。
         */
        @Test
        @DisplayName("add、contains 与 remove")
        void addContainsRemove() {
            IntSet set = new IntSet(0);
            assertTrue(set.isEmpty());
            assertFalse(set.contains(1));
            assertTrue(set.add(1));
            assertFalse(set.add(1));
            assertTrue(set.contains(1));
            assertEquals(1, set.size());
            assertTrue(set.isNotEmpty());
            assertTrue(set.remove(1));
            assertFalse(set.remove(1));
            assertFalse(set.contains(1));
            assertTrue(set.isEmpty());
        }

        /**
         * emptyElement 非 0 时仍可作为普通元素加入集合，并出现在迭代与导出结果中。
         */
        @Test
        @DisplayName("非 0 emptyElement 可作为普通元素")
        void nonZeroEmptyElementIsUsable() {
            IntSet set = new IntSet(-1);
            assertFalse(set.contains(-1));
            assertTrue(set.add(-1));
            assertFalse(set.add(-1));
            assertTrue(set.contains(-1));
            assertEquals(1, set.size());
            assertArrayEquals(new int[]{-1}, set.toArray());
            assertTrue(set.remove(-1));
            assertFalse(set.contains(-1));
            assertEquals(0, set.size());
        }

        /**
         * emptyElement 为 0 时，0 通过独立字段存储，数组中的 0 仍表示空槽。
         */
        @Test
        @DisplayName("0 作为 emptyElement 时仍可作为普通元素")
        void zeroEmptyElementIsUsable() {
            IntSet set = new IntSet(0);
            assertTrue(set.add(0));
            assertTrue(set.contains(0));
            assertArrayEquals(new int[]{0}, set.toArray());
        }

        /**
         * 大量写入触发多次扩容，所有元素仍可正确查找且 size 准确。
         */
        @Test
        @DisplayName("多次扩容后元素保持")
        void growKeepsAllElements() {
            IntSet set = new IntSet(4, 0);
            for (int i = 0; i < 100; i++) {
                assertTrue(set.add(i));
            }
            assertEquals(100, set.size());
            assertTrue(set.capacity() >= 100);
            for (int i = 0; i < 100; i++) {
                assertTrue(set.contains(i));
            }
        }

        /**
         * 选一个非标记元素作为 homeElement，再找与其起始槽位相同的另一元素；
         * 后插入的元素需线性探测到后继槽位。删除 homeElement 后应回填后继元素，
         * 保证仍处于探测链上的元素可被找到。
         */
        @Test
        @DisplayName("删除冲突元素后探测链保持完整")
        void removePreservesProbeChain() {
            IntSet set = new IntSet(4, 0);
            int homeElement = 1;
            int sameSlot = -1;
            int mask = set.capacity() - 1;
            for (int candidate = 2; candidate < 1000; candidate++) {
                if ((BaseContainer.hash(candidate) & mask)
                        == (BaseContainer.hash(homeElement) & mask)) {
                    sameSlot = candidate;
                    break;
                }
            }
            assertTrue(sameSlot > homeElement, "测试前置条件：应存在哈希同槽的非标记元素");
            set.add(homeElement);
            set.add(sameSlot);
            assertTrue(set.contains(homeElement));
            assertTrue(set.contains(sameSlot));
            assertTrue(set.remove(homeElement));
            assertFalse(set.contains(homeElement));
            assertTrue(set.contains(sameSlot));
            assertEquals(1, set.size());
        }

        /**
         * clear 重置标记与元素数量但保留容量，清空后可复用。
         */
        @Test
        @DisplayName("clear 保留容量并可复用")
        void clearKeepsCapacity() {
            IntSet set = new IntSet(-1);
            set.add(-1);
            set.add(5);
            int capacity = set.capacity();
            set.clear();
            assertTrue(set.isEmpty());
            assertEquals(capacity, set.capacity());
            assertFalse(set.contains(-1));
            assertFalse(set.contains(5));
            assertTrue(set.add(-1));
            assertTrue(set.add(5));
            assertEquals(2, set.size());
        }
    }

    @Nested
    @DisplayName("迭代与导出")
    class TraverseTest {
        /**
         * toArray 导出长度等于 size 的新数组，包含全部元素且不重复。
         */
        @Test
        @DisplayName("toArray 导出全部元素")
        void toArrayExportsAllElements() {
            IntSet set = new IntSet(4, -1);
            set.add(-1);
            for (int i = 0; i < 10; i++) {
                set.add(i);
            }
            int[] array = set.toArray();
            assertEquals(11, array.length);
            Set<Integer> seen = new HashSet<>();
            for (int value : array) {
                assertTrue(seen.add(value), "导出数组不应包含重复元素");
                assertTrue(set.contains(value));
            }
            assertEquals(11, seen.size());
        }

        /**
         * 迭代器与 forEach 遍历到的元素集合一致，且拒绝 null 消费者。
         */
        @Test
        @DisplayName("迭代器与 forEach 覆盖全部元素")
        void iteratorAndForEachCoverAllElements() {
            IntSet set = new IntSet(4, -1);
            set.add(-1);
            for (int i = 0; i < 8; i++) {
                set.add(i);
            }
            Set<Integer> viaIterator = new HashSet<>();
            PrimitiveIterator.OfInt iterator = set.iterator();
            while (iterator.hasNext()) {
                assertTrue(viaIterator.add(iterator.nextInt()));
            }
            Set<Integer> viaForEach = new HashSet<>();
            set.forEach(value -> viaForEach.add(value));
            assertEquals(viaIterator, viaForEach);
            assertEquals(9, viaIterator.size());
            assertThrows(NullPointerException.class, () -> set.forEach(null));
        }

        /**
         * 迭代器遍历期间 add 修改结构导致快速失败。
         */
        @Test
        @DisplayName("迭代器在结构修改后快速失败")
        void iteratorFailFast() {
            IntSet set = new IntSet(0);
            set.add(1);
            set.add(2);
            PrimitiveIterator.OfInt iterator = set.iterator();
            set.add(3);
            assertThrows(ConcurrentModificationException.class, iterator::hasNext);
            assertThrows(ConcurrentModificationException.class, iterator::nextInt);
        }

        /**
         * forEach 回调中修改结构同样触发快速失败。
         */
        @Test
        @DisplayName("forEach 回调中修改结构快速失败")
        void forEachFailFast() {
            IntSet set = new IntSet(0);
            set.add(1);
            set.add(2);
            assertThrows(ConcurrentModificationException.class,
                    () -> set.forEach(value -> set.add(3)));
        }
    }
}

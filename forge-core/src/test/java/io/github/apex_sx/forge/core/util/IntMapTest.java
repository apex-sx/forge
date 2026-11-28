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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link IntMap} 单元测试。
 * <p>
 * 覆盖覆盖写、默认值查询、emptyKey 作为普通键存储、删除后的探测链回填、
 * 扩容后键值保持、键/值迭代器配对、数组导出与快速失败。
 *
 * @author ApexSX
 */
@SuppressWarnings("all")
@DisplayName("IntMap 无装箱 int→int 映射")
class IntMapTest {
    @Nested
    @DisplayName("基本映射操作")
    class BasicTest {
        /**
         * put 覆盖同键值；get 对不存在的键返回 0；containsKey 区分存在性。
         */
        @Test
        @DisplayName("put、get、containsKey 与覆盖写")
        void putGetAndOverwrite() {
            IntMap map = new IntMap(0);
            assertTrue(map.isEmpty());
            assertEquals(0, map.get(1));
            assertEquals(-1, map.getOrDefault(1, -1));
            assertFalse(map.containsKey(1));
            map.put(1, 10);
            map.put(2, 20);
            assertEquals(10, map.get(1));
            assertEquals(20, map.get(2));
            assertEquals(2, map.size());
            assertTrue(map.containsKey(1));
            map.put(1, 11);
            assertEquals(11, map.get(1));
            assertEquals(2, map.size());
            assertEquals(11, map.remove(1));
            assertFalse(map.containsKey(1));
            assertEquals(0, map.get(1));
            assertEquals(0, map.remove(1));
        }

        /**
         * 当映射可能真实存储 0 值时，getOrDefault 可区分“不存在”与“值为 0”。
         */
        @Test
        @DisplayName("getOrDefault 区分缺失与值为 0")
        void zeroValueVsMissing() {
            IntMap map = new IntMap(0);
            map.put(1, 0);
            assertEquals(0, map.get(1));
            assertEquals(0, map.getOrDefault(1, -1));
            assertEquals(-1, map.getOrDefault(2, -1));
        }

        /**
         * emptyKey 非 0 时仍作为普通键存储，并出现在键迭代器与导出数组中。
         */
        @Test
        @DisplayName("非 0 emptyKey 可作为普通键存储")
        void nonZeroEmptyKeyIsUsable() {
            IntMap map = new IntMap(-1);
            assertFalse(map.containsKey(-1));
            map.put(-1, 100);
            assertTrue(map.containsKey(-1));
            assertEquals(100, map.get(-1));
            assertEquals(100, map.getOrDefault(-1, 0));
            assertEquals(1, map.size());
            assertArrayEquals(new int[]{-1}, map.toKeyArray());
            assertArrayEquals(new int[]{100}, map.toValueArray());
            assertEquals(100, map.remove(-1));
            assertFalse(map.containsKey(-1));
            assertEquals(0, map.get(-1));
            assertEquals(0, map.size());
        }

        /**
         * emptyKey 为 0 时，标记键通过独立字段存储，数组中的 0 仍表示空槽。
         */
        @Test
        @DisplayName("0 作为 emptyKey 时仍可作为普通键")
        void zeroEmptyKeyIsUsable() {
            IntMap map = new IntMap(0);
            map.put(0, 7);
            assertTrue(map.containsKey(0));
            assertEquals(7, map.get(0));
            assertArrayEquals(new int[]{0}, map.toKeyArray());
            assertArrayEquals(new int[]{7}, map.toValueArray());
        }

        /**
         * 大量写入触发多次扩容，所有键值在扩容后仍可正确读取。
         */
        @Test
        @DisplayName("多次扩容后键值保持")
        void growKeepsAllMappings() {
            IntMap map = new IntMap(4, 0);
            for (int i = 0; i < 100; i++) {
                map.put(i, i * 10);
            }
            assertEquals(100, map.size());
            assertTrue(map.capacity() >= 100);
            for (int i = 0; i < 100; i++) {
                assertEquals(i * 10, map.get(i));
            }
        }

        /**
         * 选一个非 emptyKey 的键作为 homeKey，再找与其起始槽位相同的另一键；
         * 后插入的键需线性探测到后继槽位。删除 homeKey 后应回填后继键，
         * 保证仍处于探测链上的键可被找到。
         */
        @Test
        @DisplayName("删除冲突键后探测链保持完整")
        void removePreservesProbeChain() {
            IntMap map = new IntMap(4, 0);
            int homeKey = 1;
            int sameSlot = -1;
            int mask = map.capacity() - 1;
            for (int candidate = 2; candidate < 1000; candidate++) {
                if ((BaseContainer.hash(candidate) & mask)
                        == (BaseContainer.hash(homeKey) & mask)) {
                    sameSlot = candidate;
                    break;
                }
            }
            assertTrue(sameSlot > homeKey, "测试前置条件：应存在哈希同槽的非标记键");
            map.put(homeKey, 1);
            map.put(sameSlot, 2);
            assertEquals(1, map.get(homeKey));
            assertEquals(2, map.get(sameSlot));
            assertEquals(1, map.remove(homeKey));
            assertFalse(map.containsKey(homeKey));
            assertTrue(map.containsKey(sameSlot));
            assertEquals(2, map.get(sameSlot));
            assertEquals(1, map.size());
        }

        /**
         * clear 保留容量并清空独立存储的 emptyKey 键及全部键值，清空后可复用。
         */
        @Test
        @DisplayName("clear 保留容量并可复用")
        void clearKeepsCapacity() {
            IntMap map = new IntMap(-1);
            map.put(-1, 1);
            map.put(5, 2);
            int capacity = map.capacity();
            map.clear();
            assertTrue(map.isEmpty());
            assertEquals(capacity, map.capacity());
            assertFalse(map.containsKey(-1));
            assertFalse(map.containsKey(5));
            assertEquals(0, map.get(-1));
            map.put(-1, 3);
            map.put(5, 4);
            assertEquals(3, map.get(-1));
            assertEquals(4, map.get(5));
        }
    }

    @Nested
    @DisplayName("迭代与导出")
    class TraverseTest {
        /**
         * keyIterator 与 valueIterator 顺序一一对应，emptyKey 元素优先返回。
         */
        @Test
        @DisplayName("键迭代器与值迭代器顺序对应")
        void keyAndValueIteratorsMatch() {
            IntMap map = new IntMap(4, -1);
            Map<Integer, Integer> expected = new HashMap<>();
            expected.put(-1, 99);
            map.put(-1, 99);
            for (int i = 0; i < 6; i++) {
                map.put(i, i * 10);
                expected.put(i, i * 10);
            }
            PrimitiveIterator.OfInt keys = map.keyIterator();
            PrimitiveIterator.OfInt values = map.valueIterator();
            int count = 0;
            while (keys.hasNext()) {
                assertTrue(values.hasNext());
                int key = keys.nextInt();
                int value = values.nextInt();
                assertEquals(expected.get(key), value);
                expected.remove(key);
                count++;
            }
            assertFalse(values.hasNext());
            assertEquals(7, count);
            assertTrue(expected.isEmpty());
        }

        /**
         * toKeyArray 返回长度等于 size 的新数组，包含全部键且无重复；导出顺序不保证稳定。
         */
        @Test
        @DisplayName("toKeyArray 导出全部键")
        void toKeyArrayExportsAllKeys() {
            IntMap map = new IntMap(4, -1);
            assertArrayEquals(new int[0], map.toKeyArray());
            Set<Integer> expected = new HashSet<>();
            expected.add(-1);
            map.put(-1, 99);
            for (int i = 0; i < 10; i++) {
                map.put(i, i * i);
                expected.add(i);
            }
            int[] array = map.toKeyArray();
            assertEquals(map.size(), array.length);
            assertEquals(expected.size(), array.length);
            Set<Integer> seen = new HashSet<>();
            for (int key : array) {
                assertTrue(expected.contains(key), "导出数组包含未知键: " + key);
                assertTrue(seen.add(key), "导出数组不应包含重复键: " + key);
                assertTrue(map.containsKey(key));
            }
            assertEquals(expected, seen);
            assertNotSame(array, map.toKeyArray(), "每次导出应返回新数组");
        }

        /**
         * toValueArray 返回长度等于 size 的新数组，包含全部值，且与 toKeyArray 按相同顺序一一对应。
         */
        @Test
        @DisplayName("toValueArray 导出全部值")
        void toValueArrayExportsAllValues() {
            IntMap map = new IntMap(4, -1);
            Map<Integer, Integer> expected = new HashMap<>();
            expected.put(-1, 7);
            map.put(-1, 7);
            for (int i = 0; i < 10; i++) {
                int value = i == 4 ? 7 : i * i;
                map.put(i, value);
                expected.put(i, value);
            }
            int[] keys = map.toKeyArray();
            int[] values = map.toValueArray();
            assertEquals(map.size(), values.length);
            assertEquals(keys.length, values.length);
            Set<Integer> seen = new HashSet<>();
            for (int i = 0; i < keys.length; i++) {
                assertTrue(expected.containsKey(keys[i]), "导出键数组包含未知键: " + keys[i]);
                assertTrue(seen.add(keys[i]), "导出键数组不应包含重复键: " + keys[i]);
                assertEquals(expected.get(keys[i]), values[i], "值应与同下标的键对应");
            }
            assertEquals(expected.keySet(), seen);
            assertNotSame(values, map.toValueArray(), "每次导出应返回新数组");
        }

        /**
         * 迭代器创建后发生 put 修改，后续访问快速失败。
         */
        @Test
        @DisplayName("迭代器在结构修改后快速失败")
        void iteratorFailFast() {
            IntMap map = new IntMap(0);
            map.put(1, 1);
            map.put(2, 2);
            PrimitiveIterator.OfInt keys = map.keyIterator();
            map.put(3, 3);
            assertThrows(ConcurrentModificationException.class, keys::hasNext);
            assertThrows(ConcurrentModificationException.class, keys::nextInt);
        }
    }
}

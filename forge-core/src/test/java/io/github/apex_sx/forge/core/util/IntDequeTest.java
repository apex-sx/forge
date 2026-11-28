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
 * {@link IntDeque} 单元测试。
 * <p>
 * 覆盖双端增删改查、空队列异常、循环数组回绕、回绕状态下的扩容搬迁、
 * forEach/迭代器顺序与快速失败，以及清空后复用。
 *
 * @author ApexSX
 */
@SuppressWarnings("all")
@DisplayName("IntDeque 无装箱 int 双端队列")
class IntDequeTest {
    /**
     * 借助迭代器把队列按队首到队尾顺序导出为数组，便于断言顺序。
     */
    private static int[] toArray(IntDeque deque) {
        int[] result = new int[deque.size()];
        PrimitiveIterator.OfInt iterator = deque.iterator();
        for (int i = 0; i < result.length; i++) {
            result[i] = iterator.nextInt();
        }
        return result;
    }

    @Nested
    @DisplayName("双端增删改查")
    class OperationTest {
        /**
         * 默认容量为 16，构造后为空。
         */
        @Test
        @DisplayName("默认构造容量为 16 且为空")
        void defaultConstruction() {
            IntDeque deque = new IntDeque();
            assertEquals(16, deque.capacity());
            assertTrue(deque.isEmpty());
            assertEquals(0, deque.size());
        }

        /**
         * addFirst 插到队首，addLast 插到队尾，peek 只查看不移除。
         */
        @Test
        @DisplayName("队首队尾插入与查看")
        void addAndPeek() {
            IntDeque deque = new IntDeque(4);
            deque.addLast(2);
            deque.addLast(3);
            deque.addFirst(1);
            deque.addFirst(0);
            assertEquals(4, deque.size());
            assertEquals(0, deque.peekFirst());
            assertEquals(3, deque.peekLast());
            assertArrayEquals(new int[]{0, 1, 2, 3}, toArray(deque));
        }

        /**
         * removeFirst / removeLast 返回并移除对应端元素，size 同步递减。
         */
        @Test
        @DisplayName("队首队尾移除")
        void removeBothEnds() {
            IntDeque deque = new IntDeque();
            deque.addLast(1);
            deque.addLast(2);
            deque.addLast(3);
            assertEquals(1, deque.removeFirst());
            assertEquals(3, deque.removeLast());
            assertEquals(2, deque.peekFirst());
            assertEquals(2, deque.peekLast());
            assertEquals(1, deque.size());
            assertEquals(2, deque.removeFirst());
            assertTrue(deque.isEmpty());
        }

        /**
         * 空队列上 peek / remove 均抛出 NoSuchElementException。
         */
        @Test
        @DisplayName("空队列操作抛出异常")
        void emptyQueueThrows() {
            IntDeque deque = new IntDeque();
            assertThrows(NoSuchElementException.class, deque::peekFirst);
            assertThrows(NoSuchElementException.class, deque::peekLast);
            assertThrows(NoSuchElementException.class, deque::removeFirst);
            assertThrows(NoSuchElementException.class, deque::removeLast);
        }

        /**
         * clear 清空所有元素、复位 head/tail，并保留容量以便复用。
         */
        @Test
        @DisplayName("clear 保留容量并可继续添加")
        void clearKeepsCapacity() {
            IntDeque deque = new IntDeque(4);
            deque.addFirst(1);
            deque.addLast(2);
            int capacity = deque.capacity();
            deque.clear();
            assertTrue(deque.isEmpty());
            assertEquals(capacity, deque.capacity());
            assertEquals(0, deque.size());
            deque.addLast(9);
            assertEquals(9, deque.peekFirst());
            assertEquals(9, deque.peekLast());
        }
    }

    @Nested
    @DisplayName("循环数组与扩容")
    class CircularTest {
        /**
         * 交替队首/队尾增删使逻辑队列跨越数组末尾，验证掩码回绕后顺序仍正确。
         */
        @Test
        @DisplayName("循环回绕后仍保持队首到队尾顺序")
        void wrapAroundOrder() {
            IntDeque deque = new IntDeque(4);
            for (int i = 0; i < 4; i++) {
                deque.addLast(i);
            }
            deque.removeFirst();
            deque.removeFirst();
            deque.addLast(5);
            deque.addLast(6);
            deque.addFirst(9);
            assertEquals(5, deque.size());
            assertEquals(9, deque.peekFirst());
            assertEquals(6, deque.peekLast());
            assertArrayEquals(new int[]{9, 2, 3, 5, 6}, toArray(deque));
        }

        /**
         * 在队列已满、head 与 tail 相等且 head 不为 0 的状态下触发扩容，onResize 必须分两段复制。
         * <p>
         * 构造过程：满队列 [1,2,3,4]，删两个头得 [3,4]，追加 5、6 得 [3,4,5,6]，
         * 此时 head=2、tail=2（队列满且逻辑回绕）；再追加 7 触发翻倍，新数组应按 [3,4,5,6,7] 排列。
         */
        @Test
        @DisplayName("回绕状态下扩容按逻辑顺序搬迁")
        void resizeWhenWrapped() {
            IntDeque deque = new IntDeque(4);
            deque.addLast(1);
            deque.addLast(2);
            deque.addLast(3);
            deque.addLast(4);
            deque.removeFirst();
            deque.removeFirst();
            deque.addLast(5);
            deque.addLast(6);
            deque.addLast(7);
            assertEquals(8, deque.capacity());
            assertEquals(5, deque.size());
            assertEquals(3, deque.peekFirst());
            assertEquals(7, deque.peekLast());
            assertArrayEquals(new int[]{3, 4, 5, 6, 7}, toArray(deque));
        }

        /**
         * 连续 addFirst 使 head 从 0 向前回绕，扩容后仍保持插入的逆序顺序。
         */
        @Test
        @DisplayName("连续队首插入触发扩容后顺序正确")
        void resizeAfterAddFirst() {
            IntDeque deque = new IntDeque(4);
            deque.addFirst(1);
            deque.addFirst(2);
            deque.addFirst(3);
            deque.addFirst(4);
            deque.addFirst(5);
            assertEquals(8, deque.capacity());
            assertEquals(5, deque.size());
            assertArrayEquals(new int[]{5, 4, 3, 2, 1}, toArray(deque));
        }

        /**
         * 交替从两端删除直到队列为空，验证 head/tail 收敛正确。
         */
        @Test
        @DisplayName("两端交替删除至空")
        void drainBothEnds() {
            IntDeque deque = new IntDeque(4);
            for (int i = 0; i < 6; i++) {
                deque.addLast(i);
            }
            assertEquals(0, deque.removeFirst());
            assertEquals(5, deque.removeLast());
            assertEquals(1, deque.removeFirst());
            assertEquals(4, deque.removeLast());
            assertEquals(2, deque.removeFirst());
            assertEquals(3, deque.removeLast());
            assertTrue(deque.isEmpty());
        }
    }

    @Nested
    @DisplayName("遍历与快速失败")
    class TraverseTest {
        /**
         * forEach 与迭代器都按队首到队尾顺序，且拒绝 null 消费者。
         */
        @Test
        @DisplayName("forEach 与迭代器顺序一致")
        void forEachAndIterator() {
            IntDeque deque = new IntDeque();
            deque.addLast(1);
            deque.addFirst(0);
            deque.addLast(2);
            int[] result = new int[3];
            int[] cursor = {0};
            deque.forEach(value -> result[cursor[0]++] = value);
            assertArrayEquals(new int[]{0, 1, 2}, result);
            assertArrayEquals(new int[]{0, 1, 2}, toArray(deque));
            assertThrows(NullPointerException.class, () -> deque.forEach(null));
        }

        /**
         * 迭代器创建后结构修改（addLast）导致快速失败。
         */
        @Test
        @DisplayName("迭代器在结构修改后快速失败")
        void iteratorFailFast() {
            IntDeque deque = new IntDeque();
            deque.addLast(1);
            deque.addLast(2);
            PrimitiveIterator.OfInt iterator = deque.iterator();
            deque.addLast(3);
            assertThrows(ConcurrentModificationException.class, iterator::hasNext);
            assertThrows(ConcurrentModificationException.class, iterator::nextInt);
        }

        /**
         * forEach 回调中修改结构同样触发快速失败。
         */
        @Test
        @DisplayName("forEach 回调中修改结构快速失败")
        void forEachFailFast() {
            IntDeque deque = new IntDeque();
            deque.addLast(1);
            deque.addLast(2);
            assertThrows(ConcurrentModificationException.class,
                    () -> deque.forEach(value -> deque.removeFirst()));
        }
    }
}

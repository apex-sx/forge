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

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

/**
 * 基于循环数组实现的无装箱 int 双端队列。
 * <p>
 * 队首与队尾的插入、删除、查看均为 O(1)，容量不足时扩容均摊 O(n)。
 * 容量恒为 2 的幂，扩容时按逻辑顺序一次性复制元素；迭代顺序为队首到队尾，
 * 遍历期间的结构修改会触发快速失败。
 *
 * @author ApexSX
 */
public final class IntDeque extends BaseContainer {
    /**
     * 存储元素的循环数组。
     */
    private int[] elements;
    /**
     * 队首元素所在下标。
     */
    private int head;
    /**
     * 队尾后一个可写入位置的下标。
     */
    private int tail;

    /**
     * 创建默认容量为 16 的空队列。
     */
    public IntDeque() {
        this(16);
    }

    /**
     * 创建指定初始容量的空队列。
     *
     * @param initialCapacity 初始容量，实际容量会向上取整为 2 的幂
     */
    public IntDeque(int initialCapacity) {
        super(initialCapacity, 1, 1);
        this.elements = new int[capacity()];
    }

    /**
     * 在队首插入元素，容量不足时自动扩容。
     *
     * @param value 待插入元素
     */
    public void addFirst(int value) {
        ensureCapacity();
        head = prevIndex(head);
        elements[head] = value;
        increaseSize();
        markModification();
    }

    /**
     * 在队尾插入元素，容量不足时自动扩容。
     *
     * @param value 待插入元素
     */
    public void addLast(int value) {
        ensureCapacity();
        elements[tail] = value;
        tail = nextIndex(tail);
        increaseSize();
        markModification();
    }

    /**
     * 移除并返回队首元素。
     *
     * @return 原队首元素
     * @throws NoSuchElementException 队列为空时抛出
     */
    public int removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        int value = elements[head];
        elements[head] = 0;
        head = nextIndex(head);
        decreaseSize();
        markModification();
        return value;
    }

    /**
     * 移除并返回队尾元素。
     *
     * @return 原队尾元素
     * @throws NoSuchElementException 队列为空时抛出
     */
    public int removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        tail = prevIndex(tail);
        int value = elements[tail];
        elements[tail] = 0;
        decreaseSize();
        markModification();
        return value;
    }

    /**
     * 返回队首元素但不移除。
     *
     * @return 队首元素
     * @throws NoSuchElementException 队列为空时抛出
     */
    public int peekFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[head];
    }

    /**
     * 返回队尾元素但不移除。
     *
     * @return 队尾元素
     * @throws NoSuchElementException 队列为空时抛出
     */
    public int peekLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
        return elements[prevIndex(tail)];
    }

    /**
     * 以回调方式按队首到队尾顺序遍历所有元素，无装箱、无迭代器分配。
     *
     * @param action 元素消费者，不允许为 {@code null}
     * @throws java.util.ConcurrentModificationException 遍历期间队列发生结构修改
     */
    public void forEach(IntConsumer action) {
        Objects.requireNonNull(action, "action");
        int expected = modificationMark();
        int[] source = elements;
        int start = head;
        for (int i = 0, n = size(); i < n; i++) {
            verifyModification(expected);
            action.accept(source[maskIndex(start + i)]);
        }
        verifyModification(expected);
    }

    /**
     * 返回按队首到队尾顺序遍历的迭代器（无装箱）。
     * 迭代器不支持 {@code remove}，结构修改会导致后续访问快速失败。
     *
     * @return int 元素迭代器
     */
    public PrimitiveIterator.OfInt iterator() {
        final int[] source = elements;
        final int start = head;
        return new OfInt(this, source, size(), false) {
            @Override
            protected int valueAt(int index) {
                return source[maskIndex(start + index)];
            }
        };
    }

    /**
     * 在底层数组容量发生变化后，将原有元素按逻辑顺序迁移到新数组中。
     * 若循环数组已经发生回绕，则分两段复制：先复制从 head 到数组末尾的部分，再复制从数组开头到 tail 的部分，
     * 迁移完成后将 head 重置为 0、tail 设为元素个数。
     *
     * @param newCapacity 调整后的新容量
     */
    @Override
    protected void onResize(int newCapacity) {
        int[] oldElements = elements;
        int[] newElements = newIntArray();
        int count = size();
        // 循环数组可能回绕：先复制从 head 到数组末尾的一段，再复制开头到 tail 的一段。
        int firstPart = Math.min(count, oldElements.length - head);
        System.arraycopy(oldElements, head, newElements, 0, firstPart);
        if (count > firstPart) {
            System.arraycopy(oldElements, 0, newElements, firstPart, count - firstPart);
        }
        elements = newElements;
        head = 0;
        tail = count;
    }

    /**
     * 清空集合，仅重置元素数量而保留容量。
     */
    @Override
    public void clear() {
        if (isNotEmpty()) {
            markModification();
        }
        super.clear();
        Arrays.fill(elements, 0);
        head = 0;
        tail = 0;
    }
}

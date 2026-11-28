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
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

/**
 * 基于动态数组实现的无装箱 int 列表。
 * <p>
 * 尾部追加均摊 O(1)，按下标访问 O(1)，中间插入/删除 O(n)。容量恒为 2 的幂，
 * 批量追加时一次扩容到位；遍历期间的结构修改会触发快速失败。
 *
 * @author ApexSX
 */
public final class IntList extends BaseContainer {
    /**
     * 存储元素的数组，仅前 {@link #size()} 个位置有效。
     */
    private int[] elements;

    /**
     * 创建默认容量为 16 的空列表。
     */
    public IntList() {
        this(16);
    }

    /**
     * 创建指定初始容量的空列表。
     *
     * @param initialCapacity 初始容量，实际容量会向上取整为 2 的幂
     */
    public IntList(int initialCapacity) {
        super(initialCapacity, 1, 1);
        this.elements = new int[capacity()];
    }

    /**
     * 将元素追加到列表末尾。
     *
     * @param value 待追加元素
     */
    public void add(int value) {
        ensureCapacity();
        elements[size()] = value;
        increaseSize();
        markModification();
    }

    /**
     * 在指定下标插入元素，原位置及其后的元素整体后移一位。
     *
     * @param index 插入位置，允许范围为 {@code [0, size()]}
     * @param value 待插入元素
     * @throws IndexOutOfBoundsException 下标越界时抛出
     */
    public void add(int index, int value) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size());
        }
        ensureCapacity();
        int numMoved = size() - index;
        if (numMoved > 0) {
            System.arraycopy(elements, index, elements, index + 1, numMoved);
        }
        elements[index] = value;
        increaseSize();
        markModification();
    }

    /**
     * 将数组中的全部元素按给定顺序追加到末尾。
     *
     * @param values 待追加数组，不允许为 {@code null}
     */
    public void addAll(int[] values) {
        Objects.requireNonNull(values, "values");
        int count = values.length;
        if (count == 0) {
            return;
        }
        ensureCapacityFor(count);
        System.arraycopy(values, 0, elements, size(), count);
        size(size() + count);
        markModification();
    }

    /**
     * 将另一个列表中的全部元素按原顺序追加到末尾。
     *
     * @param other 待追加列表，不允许为 {@code null}
     */
    public void addAll(IntList other) {
        Objects.requireNonNull(other, "other");
        int count = other.size();
        if (count == 0) {
            return;
        }
        ensureCapacityFor(count);
        System.arraycopy(other.elements, 0, elements, size(), count);
        size(size() + count);
        markModification();
    }

    /**
     * 返回指定下标的元素。
     *
     * @param index 下标，范围 {@code [0, size())}
     * @return 该下标处的元素
     * @throws IndexOutOfBoundsException 下标越界时抛出
     */
    public int get(int index) {
        verifyElementIndex(index);
        return elements[index];
    }

    /**
     * 修改指定下标的元素，不改变结构，因此不会触发快速失败。
     *
     * @param index 下标，范围 {@code [0, size())}
     * @param value 新值
     * @throws IndexOutOfBoundsException 下标越界时抛出
     */
    public void set(int index, int value) {
        verifyElementIndex(index);
        elements[index] = value;
    }

    /**
     * 返回指定元素第一次出现的位置。
     *
     * @param value 待查找元素
     * @return 首次出现的下标，不存在时返回 {@code -1}
     */
    public int indexOf(int value) {
        int n = size();
        for (int i = 0; i < n; i++) {
            if (elements[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回指定元素最后一次出现的位置。
     *
     * @param value 待查找元素
     * @return 最后一次出现的下标，不存在时返回 {@code -1}
     */
    public int lastIndexOf(int value) {
        for (int i = size() - 1; i >= 0; i--) {
            if (elements[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 判断是否包含指定元素。
     *
     * @param value 待查找元素
     * @return 包含时返回 {@code true}
     */
    public boolean contains(int value) {
        return indexOf(value) >= 0;
    }

    /**
     * 删除指定元素第一次出现的位置，其余元素保持相对顺序。
     *
     * @param value 待删除元素
     * @return 删除成功返回 {@code true}，元素不存在返回 {@code false}
     */
    public boolean remove(int value) {
        int index = indexOf(value);
        if (index < 0) {
            return false;
        }
        removeAt(index);
        return true;
    }

    /**
     * 删除所有等于 {@code value} 的元素，保持其余元素的相对顺序。
     * <p>
     * 单次遍历就地压缩，时间复杂度 O(n)，空间复杂度 O(1)。
     *
     * @param value 要删除的值
     * @return 实际删除的元素个数
     */
    public int removeAll(int value) {
        int n = size();
        int write = 0;
        for (int read = 0; read < n; read++) {
            int element = elements[read];
            if (element != value) {
                elements[write++] = element;
            }
        }
        int removed = n - write;
        if (removed > 0) {
            Arrays.fill(elements, write, n, 0);
            size(write);
            markModification();
        }
        return removed;
    }

    /**
     * 删除指定下标的元素，后续元素整体前移一位。
     *
     * @param index 下标，范围 {@code [0, size())}
     * @return 被删除的元素
     * @throws IndexOutOfBoundsException 下标越界时抛出
     */
    public int removeAt(int index) {
        verifyElementIndex(index);
        int oldValue = elements[index];
        int numMoved = size() - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[decreaseSize()] = 0;
        markModification();
        return oldValue;
    }

    /**
     * 以回调方式按下标顺序遍历所有元素，无装箱、无迭代器分配。
     *
     * @param action 元素消费者，不允许为 {@code null}
     * @throws java.util.ConcurrentModificationException 遍历期间列表发生结构修改
     */
    public void forEach(IntConsumer action) {
        Objects.requireNonNull(action, "action");
        int expected = modificationMark();
        int n = size();
        for (int i = 0; i < n; i++) {
            verifyModification(expected);
            action.accept(elements[i]);
        }
        verifyModification(expected);
    }

    /**
     * 按自然升序原地排序。
     */
    public void sort() {
        int n = size();
        if (n < 2) {
            return;
        }
        Arrays.sort(elements, 0, n);
        markModification();
    }

    /**
     * 原地反转元素顺序。
     */
    public void reverse() {
        int n = size();
        for (int i = 0, j = n - 1; i < j; i++, j--) {
            int temp = elements[i];
            elements[i] = elements[j];
            elements[j] = temp;
        }
        if (n > 1) {
            markModification();
        }
    }

    /**
     * 导出当前元素的新数组，顺序与列表一致。
     *
     * @return 长度为 {@link #size()} 的新数组
     */
    public int[] toArray() {
        return Arrays.copyOf(elements, size());
    }

    /**
     * 返回按下标顺序遍历的迭代器（无装箱）。
     * 迭代器不支持 {@code remove}，结构修改会导致后续访问快速失败。
     *
     * @return int 元素迭代器
     */
    public PrimitiveIterator.OfInt iterator() {
        return new OfInt(this, elements, size(), false);
    }

    /**
     * 扩容后的回调处理：在底层存储容量调整后重新分配底层数组，并将原数组中的有效元素复制到新数组中。
     *
     * @param newCapacity 扩容后的新容量
     */
    @Override
    protected void onResize(int newCapacity) {
        int[] newElements = newIntArray();
        System.arraycopy(elements, 0, newElements, 0, size());
        elements = newElements;
    }

    /**
     * 清空集合，仅重置元素数量而保留容量。
     */
    @Override
    public void clear() {
        if (isNotEmpty()) {
            markModification();
        }
        Arrays.fill(elements, 0, size(), 0);
        super.clear();
    }
}

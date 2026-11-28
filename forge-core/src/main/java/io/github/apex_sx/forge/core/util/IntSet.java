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
import java.util.ConcurrentModificationException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.IntConsumer;

/**
 * 零依赖、无装箱的 int 集合，采用开放寻址与线性探测。
 * <p>
 * 用调用方指定的 {@code emptyElement} 标记空槽，该值单独存储，因此全部 int 值均可加入集合。
 * 查找、插入与删除均摊 O(1)，删除时回填后继元素以保持探测链完整。
 * 遍历顺序不保证稳定，遍历期间的结构修改会触发快速失败。
 *
 * @author ApexSX
 */
public final class IntSet extends BaseContainer {
    /**
     * 调用方指定的空槽标记值。
     */
    private final int emptyElement;
    /**
     * 元素哈希表，值为 {@code emptyElement} 的位置表示空槽。
     */
    private int[] elements;

    /**
     * 创建默认容量为 16 的空集合。
     *
     * @param emptyElement 空槽标记值，该值仍可作为元素存入
     */
    public IntSet(int emptyElement) {
        this(16, emptyElement);
    }

    /**
     * 创建指定初始容量的空集合。
     *
     * @param initialCapacity 初始容量，实际容量会向上取整为 2 的幂
     * @param emptyElement    空槽标记值，该值仍可作为元素存入
     */
    public IntSet(int initialCapacity, int emptyElement) {
        super(initialCapacity, 3, 4);
        this.emptyElement = emptyElement;
        this.elements = newIntArray();
        if (emptyElement != 0) {
            Arrays.fill(this.elements, emptyElement);
        }
    }

    /**
     * 向集合添加元素。
     *
     * @param element 待添加元素
     * @return 添加成功返回 {@code true}，元素已存在返回 {@code false}
     */
    public boolean add(int element) {
        if (element == emptyElement) {
            if (isEmptyElementPresent()) {
                return false;
            }
            setEmptyElementPresent(true);
            increaseSize();
            markModification();
            return true;
        }
        int idx = slotOf(element);
        while (elements[idx] != emptyElement) {
            if (elements[idx] == element) {
                return false;
            }
            idx = nextIndex(idx);
        }
        elements[idx] = element;
        increaseSize();
        markModification();
        ensureCapacity();
        return true;
    }

    /**
     * 判断集合是否包含指定元素。
     *
     * @param element 待查找元素
     * @return 包含时返回 {@code true}
     */
    public boolean contains(int element) {
        if (element == emptyElement) {
            return isEmptyElementPresent();
        }
        int idx = slotOf(element);
        while (elements[idx] != emptyElement) {
            if (elements[idx] == element) {
                return true;
            }
            idx = nextIndex(idx);
        }
        return false;
    }

    /**
     * 从集合删除指定元素，并通过回填后继元素保持探测链可用。
     *
     * @param element 待删除元素
     * @return 删除成功返回 {@code true}，元素不存在返回 {@code false}
     */
    public boolean remove(int element) {
        if (element == emptyElement) {
            if (!isEmptyElementPresent()) {
                return false;
            }
            setEmptyElementPresent(false);
            decreaseSize();
            markModification();
            return true;
        }
        int idx = slotOf(element);
        while (elements[idx] != emptyElement) {
            if (elements[idx] == element) {
                int current = nextIndex(idx);
                while (elements[current] != emptyElement) {
                    int home = slotOf(elements[current]);
                    // 仅当后继元素离自己的原始槽位更远时，把它回填到当前空槽才不会破坏查找链。
                    if (maskIndex(current - home) > maskIndex(idx - home)) {
                        elements[idx] = elements[current];
                        idx = current;
                    }
                    current = nextIndex(current);
                }
                elements[idx] = emptyElement;
                decreaseSize();
                markModification();
                return true;
            }
            idx = nextIndex(idx);
        }
        return false;
    }

    /**
     * 将全部元素导出为数组，顺序不保证稳定。
     *
     * @return 包含所有元素的新数组
     */
    public int[] toArray() {
        int expected = modificationMark();
        int[] result = new int[size()];
        int count = 0;
        if (isEmptyElementPresent()) {
            result[count++] = emptyElement;
        }
        for (int element : elements) {
            if (element != emptyElement) {
                result[count++] = element;
            }
        }
        verifyModification(expected);
        return result;
    }

    /**
     * 以回调方式遍历所有元素（无装箱）。
     *
     * @param action 元素消费者，不允许为 {@code null}
     * @throws ConcurrentModificationException 遍历期间集合发生结构修改
     */
    public void forEach(IntConsumer action) {
        Objects.requireNonNull(action, "action");
        int expected = modificationMark();
        if (isEmptyElementPresent()) {
            verifyModification(expected);
            action.accept(emptyElement);
        }
        for (int element : elements) {
            verifyModification(expected);
            if (element != emptyElement) {
                action.accept(element);
            }
        }
        verifyModification(expected);
    }

    /**
     * 返回按内部顺序遍历所有元素的迭代器（无装箱）。
     * 迭代器不支持 {@code remove}，结构修改会导致后续访问快速失败。
     *
     * @return int 元素迭代器
     */
    public PrimitiveIterator.OfInt iterator() {
        return new OfInt(this, elements, emptyElement, true);
    }

    /**
     * 在容量调整后重建哈希表，将原有元素按照新的容量重新散列分布。
     * <p>
     * 该方法会用新容量重新分配元素数组，以 {@code emptyElement} 填充空位，
     * 然后遍历原数组，将其中所有非标记元素重新插入新表。
     * 单独存储的空槽元素不放在数组中，无需迁移。
     *
     * @param newCapacity 调整后的新容量，用于确定重建后哈希表的规模
     */
    @Override
    protected void onResize(int newCapacity) {
        int[] oldElements = elements;
        elements = newIntArray();
        if (emptyElement != 0) {
            Arrays.fill(elements, emptyElement);
        }
        for (int oldElement : oldElements) {
            if (oldElement != emptyElement) {
                place(oldElement);
            }
        }
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
        setEmptyElementPresent(false);
        Arrays.fill(elements, emptyElement);
    }

    /**
     * 把元素放入当前哈希表，仅供扩容重建使用。
     */
    private void place(int element) {
        int idx = slotOf(element);
        while (elements[idx] != emptyElement) {
            idx = nextIndex(idx);
        }
        elements[idx] = element;
    }
}

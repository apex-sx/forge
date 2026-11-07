/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.standard.iterator;

import java.util.Iterator;

/**
 * 循环迭代器
 *
 * @author Terraqo
 */
public class CycleIterator<T extends CycleLinker<T>> implements Iterator<T> {
    /**
     * 哨兵节点，用于标记循环的结束点
     */
    private T sentinel;

    /**
     * 当前节点，表示迭代过程中的当前位置
     */
    private T current;

    /**
     * 构造一个循环迭代器
     *
     * @param sentinel 哨兵节点，作为循环的起始/结束标志
     * @param include  是否包含哨兵节点开始迭代
     */
    public CycleIterator(T sentinel, boolean include) {
        this.current = include ? sentinel : sentinel.last();
        this.sentinel = include ? null : sentinel;
    }

    /**
     * 判断是否存在下一个元素
     *
     * @return 如果存在下一个元素则返回true，否则返回false
     */
    @Override
    public boolean hasNext() {
        boolean hasNext = current != null && current != sentinel;
        if (sentinel == null) sentinel = current; // 动态设置哨兵节点为当前节点
        return hasNext;
    }

    /**
     * 获取下一个元素
     *
     * @return 当前元素
     */
    @Override
    public T next() {
        T temp = current;
        current = current.next(); // 移动到下一个节点
        return temp;
    }
}
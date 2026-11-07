/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;


import java.util.ListIterator;
import java.util.Objects;

/**
 * 不可修改的 ListIterator 包装器。
 * <p>
 * 该类通过委托给底层的 {@link ListIterator} 来实现只读访问，
 * 并禁止所有修改操作（{@code remove}, {@code set}, {@code add}）。
 *
 * @param <E> 元素类型
 * @author Terraqo
 */
public class UnmodifiableListIterator<E> implements ListIterator<E> {

    /**
     * 被包装的基础迭代器。
     */
    private final ListIterator<E> iterator;

    /**
     * 构造一个不可修改的列表迭代器。
     *
     * @param iterator 要包装的基础迭代器，不能为 null
     * @throws NullPointerException 如果 iterator 为 null
     */
    public UnmodifiableListIterator(ListIterator<E> iterator) {
        this.iterator = Objects.requireNonNull(iterator);
    }

    /**
     * 判断是否存在下一个元素。
     *
     * @return 如果迭代器还有下一个元素，则返回 {@code true}；否则返回 {@code false}
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * 获取下一个元素。
     *
     * @return 下一个元素
     * @throws java.util.NoSuchElementException 如果没有下一个元素
     */
    @Override
    public E next() {
        return iterator.next();
    }

    /**
     * 不支持移除操作。
     *
     * @throws UnsupportedOperationException 总是抛出此异常
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

    /**
     * 判断是否存在前一个元素。
     *
     * @return 如果迭代器还有前一个元素，则返回 {@code true}；否则返回 {@code false}
     */
    @Override
    public boolean hasPrevious() {
        return iterator.hasPrevious();
    }

    /**
     * 获取前一个元素。
     *
     * @return 前一个元素
     * @throws java.util.NoSuchElementException 如果没有前一个元素
     */
    @Override
    public E previous() {
        return iterator.previous();
    }

    /**
     * 获取下一次调用 {@link #next()} 将返回的元素的索引。
     *
     * @return 下一个元素的索引；如果已到达列表末尾，则返回列表大小
     */
    @Override
    public int nextIndex() {
        return iterator.nextIndex();
    }

    /**
     * 获取下一次调用 {@link #previous()} 将返回的元素的索引。
     *
     * @return 前一个元素的索引；如果已到达列表开头，则返回 -1
     */
    @Override
    public int previousIndex() {
        return iterator.previousIndex();
    }

    /**
     * 不支持设置元素操作。
     *
     * @param e 要设置的元素（未被使用）
     * @throws UnsupportedOperationException 总是抛出此异常
     */
    @Override
    public void set(E e) {
        throw new UnsupportedOperationException("set");
    }

    /**
     * 不支持添加元素操作。
     *
     * @param e 要添加的元素（未被使用）
     * @throws UnsupportedOperationException 总是抛出此异常
     */
    @Override
    public void add(E e) {
        throw new UnsupportedOperationException("add");
    }
}

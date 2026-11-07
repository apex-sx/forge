/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Iterator;
import java.util.Objects;

/**
 * 一个不可修改的迭代器包装类。
 * <p>
 * 该类包装了一个现有的 {@link Iterator}，并委托其 {@link #hasNext()} 和 {@link #next()} 方法，
 * 但不支持 {@link #remove()} 操作。调用 {@link #remove()} 将抛出 {@link UnsupportedOperationException}。
 * </p>
 *
 * @param <E> 迭代元素的类型
 * @author Terraqo
 */
public class UnmodifiableIterator<E> implements Iterator<E> {

    /**
     * 被包装的基础迭代器。
     */
    private final Iterator<E> iterator;

    /**
     * 构造一个不可修改的迭代器。
     *
     * @param iterator 要包装的基础迭代器，不能为 null
     * @throws NullPointerException 如果 iterator 为 null
     */
    public UnmodifiableIterator(Iterator<E> iterator) {
        this.iterator = Objects.requireNonNull(iterator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * {@inheritDoc}
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
}

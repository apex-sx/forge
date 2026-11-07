/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import java.util.Iterator;

/**
 * 序列列表
 *
 * @author potatoxf
 */
@SuppressWarnings("unchecked")
public abstract class Sequential<T> implements Iterable<T> {
    private static final long serialVersionUID = 1L;
    private final Object[] values;

    @SafeVarargs
    public Sequential(T first, T... others) {
        values = new Object[others.length + 1];
        values[0] = first;
        System.arraycopy(others, 0, values, 1, others.length);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < values.length;
            }

            @Override
            public T next() {
                return (T) values[i++];
            }
        };
    }
}

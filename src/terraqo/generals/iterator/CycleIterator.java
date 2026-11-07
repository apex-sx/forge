/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.iterator;

import java.util.Iterator;

/**
 * 循环迭代器
 *
 * @author potatoxf
 */
public class CycleIterator<T extends CycleLinker<T>> implements Iterator<T> {
    private T sentinel, current;

    public CycleIterator(T sentinel, boolean include) {
        this.current = include ? sentinel : sentinel.last();
        this.sentinel = include ? null : sentinel;
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = current != null && current != sentinel;
        if (sentinel == null) sentinel = current;
        return hasNext;
    }

    @Override
    public T next() {
        T temp = current;
        current = current.next();
        return temp;
    }
}
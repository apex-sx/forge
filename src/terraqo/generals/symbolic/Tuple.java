/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.symbolic;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * 元组
 *
 * @author potatoxf
 */
public class Tuple<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private final T[] values;

    public Tuple(T[] values) {
        this.values = Objects.requireNonNull(values);
    }

    public T[] values() {
        return values;
    }

    public final int length() {
        return values.length;
    }

    public final T[] toArray() {
        return Arrays.copyOf(values, values.length);
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Tuple)) return false;
        Tuple<?> tuple = (Tuple<?>) object;
        return Arrays.deepEquals(values(), tuple.values());
    }

    @Override
    public final int hashCode() {
        return Arrays.hashCode(values());
    }

    @Override
    public final String toString() {
        return Arrays.toString(values);
    }
}

/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;


import io.github.terraqo.foundation.util.iteration.ArrayIterator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * @author Terraqo
 */
public class ObjectTuple<T> implements Serializable, Iterable<T> {
    @Serial
    private static final long serialVersionUID = 1L;
    private final T[] values;

    public ObjectTuple(T[] values) {
        this.values = Objects.requireNonNull(values);
    }

    public final int size() {
        return values.length;
    }

    public final T[] toArray() {
        return Arrays.copyOf(values, size());
    }

    public final T at(int index) {
        return values[index];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(values);
    }

    @Override
    public final int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public final String toString() {
        return Arrays.toString(values);
    }
}

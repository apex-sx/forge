package com.github.zhitron.primitive_extension;

import java.util.Iterator;

/**
 * @author zhitron
 */
public final class IntArrayIterator implements Iterator<Integer>, IntRawIterator {
    private final int[] values;
    private int i = 0;

    public IntArrayIterator(int[] values) {
        this.values = values != null ? values : new int[0];
    }

    @Override
    public boolean hasNext() {
        return i < values.length;
    }

    @Override
    public Integer next() {
        return values[i++];
    }

    @Override
    public boolean hasNextIntRawValue() {
        return i < values.length;
    }

    @Override
    public int nextIntRawValue() {
        return values[i++];
    }
}
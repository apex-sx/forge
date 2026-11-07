package com.github.zhitron.primitive_extension;

import java.util.Iterator;

/**
 * @author zhitron
 */
public final class LongArrayIterator implements Iterator<Long>, LongRawIterator {
    private final long[] values;
    private int i = 0;

    public LongArrayIterator(long[] values) {
        this.values = values != null ? values : new long[0];
    }

    @Override
    public boolean hasNext() {
        return i < values.length;
    }

    @Override
    public Long next() {
        return values[i++];
    }

    @Override
    public boolean hasNextLongRawValue() {
        return i < values.length;
    }

    @Override
    public long nextLongRawValue() {
        return values[i++];
    }
}
package com.github.zhitron.primitive_extension;

import java.util.Iterator;

/**
 * @author zhitron
 */
public final class ShortArrayIterator implements Iterator<Short>, ShortRawIterator {
    private final short[] values;
    private int i = 0;

    public ShortArrayIterator(short[] values) {
        this.values = values != null ? values : new short[0];
    }

    @Override
    public boolean hasNext() {
        return i < values.length;
    }

    @Override
    public Short next() {
        return values[i++];
    }

    @Override
    public boolean hasNextShortRawValue() {
        return i < values.length;
    }

    @Override
    public short nextShortRawValue() {
        return values[i++];
    }
}
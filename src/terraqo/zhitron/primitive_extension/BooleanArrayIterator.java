package com.github.zhitron.primitive_extension;

import com.github.zhitron.effortless.func.BooleanRawIterator;

import java.util.Iterator;

/**
 * @author zhitron
 */
public final class BooleanArrayIterator implements Iterator<Boolean>, BooleanRawIterator {
    private final boolean[] values;
    private int i = 0;

    public BooleanArrayIterator(boolean[] values) {
        this.values = values != null ? values : new boolean[0];
    }

    @Override
    public boolean hasNext() {
        return i < values.length;
    }

    @Override
    public Boolean next() {
        return values[i++];
    }

    @Override
    public boolean hasNextBooleanRawValue() {
        return i < values.length;
    }

    @Override
    public boolean nextBooleanRawValue() {
        return values[i++];
    }
}
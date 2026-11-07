package com.github.zhitron.primitive_extension;

import java.util.Iterator;

/**
 * @author zhitron
 */
public final class FloatArrayIterator implements Iterator<Float>, FloatRawIterator {
    private final float[] values;
    private int i = 0;

    public FloatArrayIterator(float[] values) {
        this.values = values != null ? values : new float[0];
    }

    @Override
    public boolean hasNext() {
        return i < values.length;
    }

    @Override
    public Float next() {
        return values[i++];
    }

    @Override
    public boolean hasNextFloatRawValue() {
        return i < values.length;
    }

    @Override
    public float nextFloatRawValue() {
        return values[i++];
    }
}
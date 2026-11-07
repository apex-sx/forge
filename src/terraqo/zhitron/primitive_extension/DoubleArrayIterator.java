package com.github.zhitron.primitive_extension;

import com.github.zhitron.effortless.func.DoubleRawIterator;

import java.util.Iterator;

/**
 * @author zhitron
 */
public final class DoubleArrayIterator implements Iterator<Double>, DoubleRawIterator {
    private final double[] values;
    private int i = 0;

    public DoubleArrayIterator(double[] values) {
        this.values = values != null ? values : new double[0];
    }

    @Override
    public boolean hasNext() {
        return i < values.length;
    }

    @Override
    public Double next() {
        return values[i++];
    }

    @Override
    public boolean hasNextDoubleRawValue() {
        return i < values.length;
    }

    @Override
    public double nextDoubleRawValue() {
        return values[i++];
    }
}
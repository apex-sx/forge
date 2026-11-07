package com.github.zhitron.primitive_extension;

import com.github.zhitron.effortless.func.ByteRawIterator;

import java.util.Iterator;

/**
 * @author zhitron
 */
public final class ByteArrayIterator implements Iterator<Byte>, ByteRawIterator {
    private final byte[] values;
    private int i = 0;

    public ByteArrayIterator(byte[] values) {
        this.values = values != null ? values : new byte[0];
    }

    @Override
    public boolean hasNext() {
        return i < values.length;
    }

    @Override
    public Byte next() {
        return values[i++];
    }

    @Override
    public boolean hasNextByteRawValue() {
        return i < values.length;
    }

    @Override
    public byte nextByteRawValue() {
        return values[i++];
    }
}
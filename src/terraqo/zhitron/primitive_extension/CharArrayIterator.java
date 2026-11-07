package com.github.zhitron.primitive_extension;

import com.github.zhitron.effortless.func.CharRawIterator;

import java.util.Iterator;

/**
 * @author zhitron
 */
public final class CharArrayIterator implements Iterator<Character>, CharRawIterator {
    private final char[] values;
    private int i = 0;

    public CharArrayIterator(char[] values) {
        this.values = values != null ? values : new char[0];
    }

    @Override
    public boolean hasNext() {
        return i < values.length;
    }

    @Override
    public Character next() {
        return values[i++];
    }

    @Override
    public boolean hasNextCharRawValue() {
        return i < values.length;
    }

    @Override
    public char nextCharRawValue() {
        return values[i++];
    }
}
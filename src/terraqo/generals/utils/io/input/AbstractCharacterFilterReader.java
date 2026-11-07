/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import static com.github.potatoxf.generals.utils.io.IOUtils.EOF;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.function.IntPredicate;

/**
 * A filter reader that filters out characters where subclasses decide which characters to filter out.
 */
public abstract class AbstractCharacterFilterReader extends FilterReader {

    /**
     * Skips nothing.
     *
     * @author potatoxf
     */
    protected static final IntPredicate SKIP_NONE = ch -> false;

    private final IntPredicate skip;

    /**
     * Constructs a new reader.
     *
     * @param reader the reader to filter
     */
    protected AbstractCharacterFilterReader(final Reader reader) {
        this(reader, SKIP_NONE);
    }

    /**
     * Constructs a new reader.
     *
     * @param reader the reader to filter.
     * @param skip Skip test.
     * @author potatoxf
     */
    protected AbstractCharacterFilterReader(final Reader reader, final IntPredicate skip) {
        super(reader);
        this.skip = skip == null ? SKIP_NONE : skip;
    }

    /**
     * Returns true if the given character should be filtered out, false to keep the character.
     *
     * @param ch the character to test.
     * @return true if the given character should be filtered out, false to keep the character.
     */
    protected boolean filter(final int ch) {
        return skip.test(ch);
    }

    @Override
    public int read() throws IOException {
        int ch;
        do {
            ch = in.read();
        } while (ch != EOF && filter(ch));
        return ch;
    }

    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        final int read = super.read(cbuf, off, len);
        if (read == EOF) {
            return EOF;
        }
        int pos = off - 1;
        for (int readPos = off; readPos < off + read; readPos++) {
            if (filter(cbuf[readPos])) {
                continue;
            }
            pos++;
            if (pos < readPos) {
                cbuf[pos] = cbuf[readPos];
            }
        }
        return pos - off + 1;
    }
}

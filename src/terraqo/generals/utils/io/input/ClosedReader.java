/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import static com.github.potatoxf.generals.utils.io.IOUtils.EOF;

import java.io.IOException;
import java.io.Reader;

import com.github.potatoxf.generals.utils.io.IOUtils;

/**
 * Always returns {@link IOUtils#EOF} to all attempts to read something from it.
 * <p>
 * Typically uses of this class include testing for corner cases in methods that accept readers and acting as a sentinel
 * value instead of a {@code null} reader.
 * </p>
 *
 * @author potatoxf
 */
public class ClosedReader extends Reader {

    /**
     * The singleton instance.
     *
     * @author potatoxf
     */
    public static final ClosedReader INSTANCE = new ClosedReader();

    /**
     * The singleton instance.
     *
     * @deprecated {@link #INSTANCE}.
     */
    @Deprecated
    public static final ClosedReader CLOSED_READER = INSTANCE;

    @Override
    public void close() throws IOException {
        // noop
    }

    /**
     * Returns -1 to indicate that the stream is closed.
     *
     * @param cbuf ignored
     * @param off ignored
     * @param len ignored
     * @return always -1
     */
    @Override
    public int read(final char[] cbuf, final int off, final int len) {
        return EOF;
    }

}

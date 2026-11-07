/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.IOException;
import java.io.Writer;

/**
 * Throws an IOException on all attempts to write with {@link #close()} implemented as a noop.
 * <p>
 * Typically uses of this class include testing for corner cases in methods that accept a writer and acting as a
 * sentinel value instead of a {@code null} writer.
 * </p>
 *
 * @author potatoxf
 */
public class ClosedWriter extends Writer {

    /**
     * The singleton instance.
     *
     * @author potatoxf
     */
    public static final ClosedWriter INSTANCE = new ClosedWriter();

    /**
     * The singleton instance.
     *
     * @deprecated Use {@link #INSTANCE}.
     */
    @Deprecated
    public static final ClosedWriter CLOSED_WRITER = INSTANCE;

    @Override
    public void close() throws IOException {
        // noop
    }

    /**
     * Throws an {@link IOException} to indicate that the stream is closed.
     *
     * @throws IOException always thrown
     */
    @Override
    public void flush() throws IOException {
        throw new IOException("flush() failed: stream is closed");
    }

    /**
     * Throws an {@link IOException} to indicate that the writer is closed.
     *
     * @param cbuf ignored
     * @param off ignored
     * @param len ignored
     * @throws IOException always thrown
     */
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        throw new IOException("write(" + new String(cbuf) + ", " + off + ", " + len + ") failed: stream is closed");
    }
}

/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Throws an IOException on all attempts to write to the stream.
 * <p>
 * Typically uses of this class include testing for corner cases in methods that accept an output stream and acting as a sentinel value instead of a
 * {@code null} output stream.
 * </p>
 *
 * @author potatoxf
 */
public class ClosedOutputStream extends OutputStream {

    /**
     * The singleton instance.
     *
     * @author potatoxf
     */
    public static final ClosedOutputStream INSTANCE = new ClosedOutputStream();

    /**
     * The singleton instance.
     *
     * @deprecated Use {@link #INSTANCE}.
     */
    @Deprecated
    public static final ClosedOutputStream CLOSED_OUTPUT_STREAM = INSTANCE;

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
     * Throws an {@link IOException} to indicate that the stream is closed.
     *
     * @param b   ignored
     * @param off ignored
     * @param len ignored
     * @throws IOException always thrown
     */
    @Override
    public void write(final byte b[], final int off, final int len) throws IOException {
        throw new IOException("write(byte[], int, int) failed: stream is closed");
    }

    /**
     * Throws an {@link IOException} to indicate that the stream is closed.
     *
     * @param b ignored
     * @throws IOException always thrown
     */
    @Override
    public void write(final int b) throws IOException {
        throw new IOException("write(int) failed: stream is closed");
    }
}

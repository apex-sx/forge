/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.IOException;
import java.io.OutputStream;

import com.github.potatoxf.generals.utils.GeneralIO;

/**
 * Forwards data to a stream that has been associated with this thread.
 */
public class DemuxOutputStream extends OutputStream {
    private final InheritableThreadLocal<OutputStream> outputStreamThreadLocal = new InheritableThreadLocal<>();

    /**
     * Binds the specified stream to the current thread.
     *
     * @param output
     *            the stream to bind
     * @return the OutputStream that was previously active
     */
    public OutputStream bindStream(final OutputStream output) {
        final OutputStream stream = outputStreamThreadLocal.get();
        outputStreamThreadLocal.set(output);
        return stream;
    }

    /**
     * Closes stream associated with current thread.
     *
     * @throws IOException
     *             if an error occurs
     */
    @SuppressWarnings("resource") // we actually close the stream here
    @Override
    public void close() throws IOException {
        GeneralIO.close(outputStreamThreadLocal.get());
    }

    /**
     * Flushes stream associated with current thread.
     *
     * @throws IOException
     *             if an error occurs
     */
    @Override
    public void flush() throws IOException {
        @SuppressWarnings("resource")
        final OutputStream output = outputStreamThreadLocal.get();
        if (null != output) {
            output.flush();
        }
    }

    /**
     * Writes byte to stream associated with current thread.
     *
     * @param ch
     *            the byte to write to stream
     * @throws IOException
     *             if an error occurs
     */
    @Override
    public void write(final int ch) throws IOException {
        @SuppressWarnings("resource")
        final OutputStream output = outputStreamThreadLocal.get();
        if (null != output) {
            output.write(ch);
        }
    }
}

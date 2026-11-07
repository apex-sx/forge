/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import static com.github.potatoxf.generals.utils.io.IOUtils.EOF;

import java.io.IOException;
import java.io.InputStream;

import com.github.potatoxf.generals.utils.GeneralIO;

/**
 * Data written to this stream is forwarded to a stream that has been associated with this thread.
 */
public class DemuxInputStream extends InputStream {

    private final InheritableThreadLocal<InputStream> inputStreamLocal = new InheritableThreadLocal<>();

    /**
     * Binds the specified stream to the current thread.
     *
     * @param input the stream to bind
     * @return the InputStream that was previously active
     */
    public InputStream bindStream(final InputStream input) {
        final InputStream oldValue = inputStreamLocal.get();
        inputStreamLocal.set(input);
        return oldValue;
    }

    /**
     * Closes stream associated with current thread.
     *
     * @throws IOException if an error occurs
     */
    @SuppressWarnings("resource") // we actually close the stream here
    @Override
    public void close() throws IOException {
        GeneralIO.close(inputStreamLocal.get());
    }

    /**
     * Reads byte from stream associated with current thread.
     *
     * @return the byte read from stream
     * @throws IOException if an error occurs
     */
    @Override
    public int read() throws IOException {
        final InputStream inputStream = inputStreamLocal.get();
        if (null != inputStream) {
            return inputStream.read();
        }
        return EOF;
    }
}

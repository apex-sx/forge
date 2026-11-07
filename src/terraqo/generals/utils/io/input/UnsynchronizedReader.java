/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.input;

import static com.github.potatoxf.generals.utils.io.IOUtils.EOF;

import java.io.IOException;
import java.io.Reader;

import com.github.potatoxf.generals.utils.io.IOUtils;

/**
 * A {@link Reader} without any of the superclass' synchronization.
 *
 * @author potatoxf
 */
public abstract class UnsynchronizedReader extends Reader {

    /**
     * The maximum skip-buffer size.
     */
    private static final int MAX_SKIP_BUFFER_SIZE = IOUtils.DEFAULT_BUFFER_SIZE;

    /**
     * Whether {@link #close()} completed successfully.
     */
    private boolean closed;

    /**
     * The skip buffer, defaults to null until allocated in {@link UnsynchronizedReader#skip(long)}.
     */
    private char skipBuffer[];

    /**
     * Checks if this instance is closed and throws an IOException if so.
     *
     * @throws IOException if this instance is closed.
     */
    void checkOpen() throws IOException {
        Input.checkOpen(!isClosed());
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    /**
     * Tests whether this instance is closed; if {@link #close()} completed successfully.
     *
     * @return whether this instance is closed.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Sets whether this instance is closed.
     *
     * @param closed whether this instance is closed.
     */
    public void setClosed(final boolean closed) {
        this.closed = closed;
    }

    /**
     * Skips characters by reading from this instance.
     *
     * This method will <em>block</em> until:
     * <ul>
     * <li>some characters are available,</li>
     * <li>an I/O error occurs, or</li>
     * <li>the end of the stream is reached.</li>
     * </ul>
     *
     * @param n The number of characters to skip.
     * @return The number of characters actually skipped.
     * @throws IllegalArgumentException If {@code n} is negative.
     * @throws IOException              If an I/O error occurs.
     */
    @Override
    public long skip(final long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("skip value < 0");
        }
        final int bufSize = (int) Math.min(n, MAX_SKIP_BUFFER_SIZE);
        if (skipBuffer == null || skipBuffer.length < bufSize) {
            skipBuffer = new char[bufSize];
        }
        long remaining = n;
        while (remaining > 0) {
            final int countOrEof = read(skipBuffer, 0, (int) Math.min(remaining, bufSize));
            if (countOrEof == EOF) {
                break;
            }
            remaining -= countOrEof;
        }
        return n - remaining;
    }
}

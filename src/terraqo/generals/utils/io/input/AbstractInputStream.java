/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.input;

import java.io.IOException;
import java.io.InputStream;

/**
 * Abstracts some InputStream operations for implementations in this package.
 *
 * @author potatoxf
 */
public abstract class AbstractInputStream extends InputStream {

    /**
     * Whether {@link #close()} completed successfully.
     */
    private boolean closed;

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
        super.close();
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

}

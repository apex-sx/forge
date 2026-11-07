/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This is an alternative to {@link ByteArrayInputStream}
 * which removes the synchronization overhead for non-concurrent
 * access; as such this class is not thread-safe.
 *
 * Proxy stream that prevents the underlying input stream from being marked/reset.
 * <p>
 * This class is typically used in cases where an input stream that supports
 * marking needs to be passed to a component that wants to explicitly mark
 * the stream, but it is not desirable to allow marking of the stream.
 * </p>
 *
 * @author potatoxf
 */
public class MarkShieldInputStream extends ProxyInputStream {

    /**
     * Constructs a proxy that shields the given input stream from being
     * marked or rest.
     *
     * @param in underlying input stream
     */
    public MarkShieldInputStream(final InputStream in) {
        super(in);
    }

    @SuppressWarnings("sync-override")
    @Override
    public void mark(final int readLimit) {
        // no-op
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @SuppressWarnings("sync-override")
    @Override
    public void reset() throws IOException {
        throw UnsupportedOperationExceptions.reset();
    }
}

/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.OutputStream;

/**
 * Proxy stream that prevents the underlying output stream from being closed.
 * <p>
 * This class is typically used in cases where an output stream needs to be
 * passed to a component that wants to explicitly close the stream even if other
 * components would still use the stream for output.
 * </p>
 *
 * @author potatoxf
 */
public class CloseShieldOutputStream extends ProxyOutputStream {

    /**
     * Constructs a proxy that shields the given output stream from being closed.
     *
     * @param outputStream the output stream to wrap
     * @return the created proxy
     * @author potatoxf
     */
    public static CloseShieldOutputStream wrap(final OutputStream outputStream) {
        return new CloseShieldOutputStream(outputStream);
    }

    /**
     * Constructs a proxy that shields the given output stream from being closed.
     *
     * @param outputStream underlying output stream
     * @deprecated Using this constructor prevents IDEs from warning if the
     *             underlying output stream is never closed. Use
     *             {@link #wrap(OutputStream)} instead.
     */
    @Deprecated
    public CloseShieldOutputStream(final OutputStream outputStream) {
        super(outputStream);
    }

    /**
     * Replaces the underlying output stream with a {@link ClosedOutputStream}
     * sentinel. The original output stream will remain open, but this proxy will
     * appear closed.
     */
    @Override
    public void close() {
        out = ClosedOutputStream.INSTANCE;
    }

}

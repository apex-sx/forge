/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import java.io.InputStream;

/**
 * Proxy stream that prevents the underlying input stream from being closed.
 * <p>
 * This class is typically used in cases where an input stream needs to be
 * passed to a component that wants to explicitly close the stream even if more
 * input would still be available to other components.
 * </p>
 *
 * @author potatoxf
 */
public class CloseShieldInputStream extends ProxyInputStream {

    /**
     * Constructs a proxy that only shields {@link System#in} from closing.
     *
     * @param inputStream the candidate input stream.
     * @return the given stream or a proxy on {@link System#in}.
     * @author potatoxf
     */
    public static InputStream systemIn(final InputStream inputStream) {
        return inputStream == System.in ? wrap(inputStream) : inputStream;
    }

    /**
     * Constructs a proxy that shields the given input stream from being closed.
     *
     * @param inputStream the input stream to wrap
     * @return the created proxy
     * @author potatoxf
     */
    public static CloseShieldInputStream wrap(final InputStream inputStream) {
        return new CloseShieldInputStream(inputStream);
    }

    /**
     * Constructs a proxy that shields the given input stream from being closed.
     *
     * @param inputStream underlying input stream
     * @deprecated Using this constructor prevents IDEs from warning if the
     *             underlying input stream is never closed. Use
     *             {@link #wrap(InputStream)} instead.
     */
    @Deprecated
    public CloseShieldInputStream(final InputStream inputStream) {
        super(inputStream);
    }

    /**
     * Replaces the underlying input stream with a {@link ClosedInputStream}
     * sentinel. The original input stream will remain open, but this proxy will
     * appear closed.
     */
    @Override
    public void close() {
        in = ClosedInputStream.INSTANCE;
    }

}

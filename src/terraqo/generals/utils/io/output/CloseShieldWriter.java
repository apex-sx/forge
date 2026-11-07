/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.Writer;

/**
 * Proxy writer that prevents the underlying writer from being closed.
 * <p>
 * This class is typically used in cases where a writer needs to be passed to a
 * component that wants to explicitly close the writer even if other components
 * would still use the writer for output.
 * </p>
 *
 * @author potatoxf
 */
public class CloseShieldWriter extends ProxyWriter {

    /**
     * Constructs a proxy that shields the given writer from being closed.
     *
     * @param writer the writer to wrap
     * @return the created proxy
     * @author potatoxf
     */
    public static CloseShieldWriter wrap(final Writer writer) {
        return new CloseShieldWriter(writer);
    }

    /**
     * Constructs a proxy that shields the given writer from being closed.
     *
     * @param writer underlying writer
     * @deprecated Using this constructor prevents IDEs from warning if the
     *             underlying writer is never closed. Use {@link #wrap(Writer)}
     *             instead.
     */
    @Deprecated
    public CloseShieldWriter(final Writer writer) {
        super(writer);
    }

    /**
     * Replaces the underlying writer with a {@link ClosedWriter} sentinel. The
     * original writer will remain open, but this proxy will appear closed.
     */
    @Override
    public void close() {
        out = ClosedWriter.INSTANCE;
    }

}

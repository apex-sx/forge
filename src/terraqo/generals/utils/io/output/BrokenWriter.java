/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Supplier;

import com.github.potatoxf.generals.utils.io.function.Erase;

/**
 * Always throws an exception from all {@link Writer} methods where {@link IOException} is declared.
 * <p>
 * This class is mostly useful for testing error handling.
 * </p>
 *
 * @author potatoxf
 */
public class BrokenWriter extends Writer {

    /**
     * The singleton instance using a default IOException.
     *
     * @author potatoxf
     */
    public static final BrokenWriter INSTANCE = new BrokenWriter();

    /**
     * Supplies the exception that is thrown by all methods of this class.
     */
    private final Supplier<Throwable> exceptionSupplier;

    /**
     * Constructs a new writer that always throws an {@link IOException}.
     */
    public BrokenWriter() {
        this(() -> new IOException("Broken writer"));
    }

    /**
     * Constructs a new writer that always throws the given exception.
     *
     * @param exception the exception to be thrown.
     * @deprecated Use {@link #BrokenWriter(Throwable)}.
     */
    @Deprecated
    public BrokenWriter(final IOException exception) {
        this(() -> exception);
    }

    /**
     * Constructs a new writer that always throws the supplied exception.
     *
     * @param exceptionSupplier a supplier for the IOException or RuntimeException to be thrown.
     * @author potatoxf
     */
    public BrokenWriter(final Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    /**
     * Constructs a new writer that always throws the given exception.
     *
     * @param exception the exception to be thrown.
     * @author potatoxf
     */
    public BrokenWriter(final Throwable exception) {
        this(() -> exception);
    }

    /**
     * Throws the configured exception.
     *
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public void close() throws IOException {
        throw rethrow();
    }

    /**
     * Throws the configured exception.
     *
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public void flush() throws IOException {
        throw rethrow();
    }

    /**
     * Throws the configured exception from its supplier.
     *
     * @return Throws the configured exception from its supplier.
     */
    private RuntimeException rethrow() {
        return Erase.rethrow(exceptionSupplier.get());
    }

    /**
     * Throws the configured exception.
     *
     * @param cbuf ignored.
     * @param off  ignored.
     * @param len  ignored.
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public void write(final char[] cbuf, final int off, final int len) throws IOException {
        throw rethrow();
    }

}

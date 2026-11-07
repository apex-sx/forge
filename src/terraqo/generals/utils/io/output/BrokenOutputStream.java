/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import com.github.potatoxf.generals.utils.io.function.Erase;

/**
 * Always throws an exception from all {@link OutputStream} methods where {@link IOException} is declared.
 * <p>
 * This class is mostly useful for testing error handling.
 * </p>
 *
 * @author potatoxf
 */
public class BrokenOutputStream extends OutputStream {

    /**
     * The singleton instance using a default IOException.
     *
     * @author potatoxf
     */
    public static final BrokenOutputStream INSTANCE = new BrokenOutputStream();

    /**
     * Supplies the exception that is thrown by all methods of this class.
     */
    private final Supplier<Throwable> exceptionSupplier;

    /**
     * Constructs a new stream that always throws an {@link IOException}.
     */
    public BrokenOutputStream() {
        this(() -> new IOException("Broken output stream"));
    }

    /**
     * Constructs a new stream that always throws the given exception.
     *
     * @param exception the exception to be thrown.
     * @deprecated Use {@link #BrokenOutputStream(Throwable)}.
     */
    @Deprecated
    public BrokenOutputStream(final IOException exception) {
        this(() -> exception);
    }

    /**
     * Constructs a new stream that always throws the supplied exception.
     *
     * @param exceptionSupplier a supplier for the IOException or RuntimeException to be thrown.
     * @author potatoxf
     */
    public BrokenOutputStream(final Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    /**
     * Constructs a new stream that always throws the given exception.
     *
     * @param exception the exception to be thrown.
     * @author potatoxf
     */
    public BrokenOutputStream(final Throwable exception) {
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
     * @param b ignored.
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public void write(final int b) throws IOException {
        throw rethrow();
    }

}

/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import com.github.potatoxf.generals.utils.io.function.Erase;

/**
 * Always throws an exception from all {@link InputStream} methods where {@link IOException} is declared.
 * <p>
 * This class is mostly useful for testing error handling.
 * </p>
 *
 * @author potatoxf
 */
public class BrokenInputStream extends InputStream {

    /**
     * The singleton instance using a default IOException.
     *
     * @author potatoxf
     */
    public static final BrokenInputStream INSTANCE = new BrokenInputStream();

    /**
     * A supplier for the exception that is thrown by all methods of this class.
     */
    private final Supplier<Throwable> exceptionSupplier;

    /**
     * Constructs a new stream that always throws an {@link IOException}.
     */
    public BrokenInputStream() {
        this(() -> new IOException("Broken input stream"));
    }

    /**
     * Constructs a new stream that always throws the given exception.
     *
     * @param exception the exception to be thrown.
     * @deprecated Use {@link #BrokenInputStream(Throwable)}.
     */
    @Deprecated
    public BrokenInputStream(final IOException exception) {
        this(() -> exception);
    }

    /**
     * Constructs a new stream that always throws the supplied exception.
     *
     * @param exceptionSupplier a supplier for the IOException or RuntimeException to be thrown.
     * @author potatoxf
     */
    public BrokenInputStream(final Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    /**
     * Constructs a new stream that always throws the given exception.
     *
     * @param exception the exception to be thrown.
     * @author potatoxf
     */
    public BrokenInputStream(final Throwable exception) {
        this(() -> exception);
    }

    /**
     * Throws the configured exception.
     *
     * @return nothing.
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public int available() throws IOException {
        throw rethrow();
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
     * Gets the Throwable to throw. Package-private for testing.
     *
     * @return  the Throwable to throw.
     */
    Throwable getThrowable() {
        return exceptionSupplier.get();
    }

    /**
     * Throws the configured exception.
     *
     * @return nothing.
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public int read() throws IOException {
        throw rethrow();
    }

    /**
     * Throws the configured exception.
     *
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public synchronized void reset() throws IOException {
        throw rethrow();
    }

    /**
     * Throws the configured exception from its supplier.
     *
     * @return Throws the configured exception from its supplier.
     */
    private RuntimeException rethrow() {
        return Erase.rethrow(getThrowable());
    }

    /**
     * Throws the configured exception.
     *
     * @param n ignored.
     * @return nothing.
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public long skip(final long n) throws IOException {
        throw rethrow();
    }

}

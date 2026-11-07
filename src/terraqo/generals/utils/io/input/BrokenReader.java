/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Supplier;

import com.github.potatoxf.generals.utils.io.function.Erase;

/**
 * Always throws an exception from all {@link Reader} methods where {@link IOException} is declared.
 * <p>
 * This class is mostly useful for testing error handling.
 * </p>
 *
 * @author potatoxf
 */
public class BrokenReader extends Reader {

    /**
     * A singleton instance using a default IOException.
     *
     * @author potatoxf
     */
    public static final BrokenReader INSTANCE = new BrokenReader();

    /**
     * A supplier for the exception that is thrown by all methods of this class.
     */
    private final Supplier<Throwable> exceptionSupplier;

    /**
     * Constructs a new reader that always throws an {@link IOException}.
     */
    public BrokenReader() {
        this(() -> new IOException("Broken reader"));
    }

    /**
     * Constructs a new reader that always throws the given exception.
     *
     * @param exception the exception to be thrown.
     * @deprecated Use {@link #BrokenReader(Throwable)}.
     */
    @Deprecated
    public BrokenReader(final IOException exception) {
        this(() -> exception);
    }

    /**
     * Constructs a new reader that always throws the supplied exception.
     *
     * @param exceptionSupplier a supplier for the IOException or RuntimeException to be thrown.
     * @author potatoxf
     */
    public BrokenReader(final Supplier<Throwable> exceptionSupplier) {
        this.exceptionSupplier = exceptionSupplier;
    }

    /**
     * Constructs a new reader that always throws the given exception.
     *
     * @param exception the exception to be thrown.
     * @author potatoxf
     */
    public BrokenReader(final Throwable exception) {
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
     * @param readAheadLimit ignored.
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public void mark(final int readAheadLimit) throws IOException {
        throw rethrow();
    }

    /**
     * Throws the configured exception.
     *
     * @param cbuf ignored.
     * @param off  ignored.
     * @param len  ignored.
     * @return nothing.
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public int read(final char[] cbuf, final int off, final int len) throws IOException {
        throw rethrow();
    }

    /**
     * Throws the configured exception.
     *
     * @return nothing.
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public boolean ready() throws IOException {
        throw rethrow();
    }

    /**
     * Throws the configured exception.
     *
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public void reset() throws IOException {
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
     * @param n ignored.
     * @return nothing.
     * @throws IOException always throws the exception configured in a constructor.
     */
    @Override
    public long skip(final long n) throws IOException {
        throw rethrow();
    }

}

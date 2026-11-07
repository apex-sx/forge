/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

/**
 * Helps use lambdas that throw {@link IOException} rethrow as {@link UncheckedIOException}.
 *
 * @author potatoxf
 */
final class UncheckedIOExceptions {

    /**
     * Constructs a new UncheckedIOException for the given detail message.
     * <p>
     * This method exists because there is no String constructor in {@link UncheckedIOException}.
     * </p>
     *
     * @param message the detail message.
     * @return a new {@link UncheckedIOException}.
     */
    public static UncheckedIOException create(final Object message) {
        final String string = Objects.toString(message);
        return new UncheckedIOException(string, new IOException(string));
    }

    /**
     * Constructs a new UncheckedIOException for the given detail message.
     * <p>
     * This method exists because there is no String constructor in {@link UncheckedIOException}.
     * </p>
     * @param e cause the {@link IOException}.
     * @param message the detail message.
     *
     * @return a new {@link UncheckedIOException}.
     */
    public static UncheckedIOException wrap(final IOException e, final Object message) {
        return new UncheckedIOException(Objects.toString(message), e);
    }

    private UncheckedIOExceptions() {
        // no instance
    }
}

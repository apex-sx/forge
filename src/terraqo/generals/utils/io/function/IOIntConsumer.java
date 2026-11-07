/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * Like {@link IntConsumer} but throws {@link IOException}.
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface IOIntConsumer {

    /**
     * The constant no-op consumer.
     */
    IOIntConsumer NOOP = i -> {/* noop */};

    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     * @throws IOException if an I/O error occurs.
     */
    void accept(int value) throws IOException;

    /**
     * Returns a composed {@code IOIntConsumer} that performs, in sequence, this operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the composed operation. If performing this operation throws an exception, the {@code after}
     * operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code IOIntConsumer} that performs in sequence this operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default IOIntConsumer andThen(final IOIntConsumer after) {
        Objects.requireNonNull(after);
        return (final int i) -> {
            accept(i);
            after.accept(i);
        };
    }

    /**
     * Creates a {@link Consumer} for this instance that throws {@link UncheckedIOException} instead of {@link IOException}.
     *
     * @return an UncheckedIOException IntConsumer.
     */
    default Consumer<Integer> asConsumer() {
        return i -> Uncheck.accept(this, i);
    }

    /**
     * Creates an {@link IntConsumer} for this instance that throws {@link UncheckedIOException} instead of {@link IOException}.
     *
     * @return an UncheckedIOException IntConsumer.
     */
    default IntConsumer asIntConsumer() {
        return i -> Uncheck.accept(this, i);
    }

}

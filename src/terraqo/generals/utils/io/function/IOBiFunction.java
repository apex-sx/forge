/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Like {@link BiFunction} but throws {@link IOException}.
 *
 * <p>
 * This is a <a href="package-summary.html">functional interface</a> whose functional method is
 * {@link #apply(Object, Object)}.
 * </p>
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @author potatoxf
 */
@FunctionalInterface
public interface IOBiFunction<T, U, R> {

    /**
     * Creates a composed function that first applies this function to its input, and then applies the {@code after}
     * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
     * composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <V> IOBiFunction<T, U, V> andThen(final IOFunction<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (final T t, final U u) -> after.apply(apply(t, u));
    }

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     * @throws IOException if an I/O error occurs.
     */
    R apply(T t, U u) throws IOException;

    /**
     * Creates a {@link BiFunction} for this instance that throws {@link UncheckedIOException} instead of
     * {@link IOException}.
     *
     * @return an UncheckedIOException BiFunction.
     */
    default BiFunction<T, U, R> asBiFunction() {
        return (t, u) -> Uncheck.apply(this, t, u);
    }
}

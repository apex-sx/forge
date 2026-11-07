/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * Like {@link BinaryOperator} but throws {@link IOException}.
 *
 * @param <T> the type of the operands and result of the operator.
 *
 * @see IOBiFunction
 * @see BinaryOperator
 * @author potatoxf
 */
@FunctionalInterface
public interface IOBinaryOperator<T> extends IOBiFunction<T, T, T> {

    /**
     * Creates a {@link IOBinaryOperator} which returns the greater of two elements according to the specified
     * {@code Comparator}.
     *
     * @param <T> the type of the input arguments of the comparator
     * @param comparator a {@code Comparator} for comparing the two values
     * @return a {@code BinaryOperator} which returns the greater of its operands, according to the supplied
     *         {@code Comparator}
     * @throws NullPointerException if the argument is null
     */
    static <T> IOBinaryOperator<T> maxBy(final IOComparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) >= 0 ? a : b;
    }

    /**
     * Creates a {@link IOBinaryOperator} which returns the lesser of two elements according to the specified
     * {@code Comparator}.
     *
     * @param <T> the type of the input arguments of the comparator
     * @param comparator a {@code Comparator} for comparing the two values
     * @return a {@code BinaryOperator} which returns the lesser of its operands, according to the supplied
     *         {@code Comparator}
     * @throws NullPointerException if the argument is null
     */
    static <T> IOBinaryOperator<T> minBy(final IOComparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (a, b) -> comparator.compare(a, b) <= 0 ? a : b;
    }

    /**
     * Creates a {@link BinaryOperator} for this instance that throws {@link UncheckedIOException} instead of
     * {@link IOException}.
     *
     * @return an unchecked BiFunction.
     */
    default BinaryOperator<T> asBinaryOperator() {
        return (t, u) -> Uncheck.apply(this, t, u);
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.UnaryOperator;

/**
 * Like {@link UnaryOperator} but throws {@link IOException}.
 *
 * @param <T> the type of the operand and result of the operator.
 *
 * @see UnaryOperator
 * @see IOFunction
 * @author potatoxf
 */
@FunctionalInterface
public interface IOUnaryOperator<T> extends IOFunction<T, T> {

    /**
     * Creates a unary operator that always returns its input argument.
     *
     * @param <T> the type of the input and output of the operator.
     * @return a unary operator that always returns its input argument.
     */
    static <T> IOUnaryOperator<T> identity() {
        return t -> t;
    }

    /**
     * Creates a {@link UnaryOperator} for this instance that throws {@link UncheckedIOException} instead of
     * {@link IOException}.
     *
     * @return an unchecked BiFunction.
     */
    default UnaryOperator<T> asUnaryOperator() {
        return t -> Uncheck.apply(this, t);
    }

}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.function;

/**
 * lambda functional interface
 *
 * @param <F> the type of the first argument to the operation
 * @param <E> the type of the exception
 * @author potatoxf
 */
@FunctionalInterface
public interface Function1Throw<F, R, E extends Throwable> {
    /**
     * handle method
     *
     * @param f the first input argument
     * @return value
     * @throws E throwing an exception
     */
    R apply(F f) throws E;
}

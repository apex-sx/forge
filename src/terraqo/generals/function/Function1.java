/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.function;

/**
 * lambda functional interface
 *
 * @param <F> the type of the first argument to the operation
 * @author potatoxf
 */
@FunctionalInterface
public interface Function1<F, R> {
    /**
     * handle method
     *
     * @param f the first input argument
     * @return value
     */
    R apply(F f);
}

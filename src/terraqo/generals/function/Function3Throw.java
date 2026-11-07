/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.function;

/**
 * lambda functional interface
 *
 * @param <F> the type of the first argument to the operation
 * @param <S> the type of the second argument to the operation
 * @param <T> the type of the third argument to the operation
 * @param <E> the type of the exception
 * @author potatoxf
 */
@FunctionalInterface
public interface Function3Throw<F, S, T, R, E extends Throwable> {
    /**
     * handle method
     *
     * @param f the first input argument
     * @param s the second input argument
     * @param t the third input argument
     * @return value
     * @throws E throwing an exception
     */
    R apply(F f, S s, T t) throws E;
}

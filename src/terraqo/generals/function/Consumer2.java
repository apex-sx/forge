/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.function;
/**
 * lambda functional interface
 *
 * @param <F> the type of the first argument to the operation
 * @param <S> the type of the second argument to the operation
 * @author potatoxf
 */
@FunctionalInterface
public interface Consumer2<F, S> {
    /**
     * handle method
     *
     * @param f the first input argument
     * @param s the second input argument
     */
    void accept(F f, S s);
}

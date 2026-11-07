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
public interface Consumer1<F> {
    /**
     * handle method
     *
     * @param f the first input argument
     */
    void accept(F f);
}

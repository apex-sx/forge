/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.function;

/**
 * lambda functional interface
 *
 * @param <T> the input object type
 * @author potatoxf
 */
@FunctionalInterface
public interface RangeFunction<T, R> {
    /**
     * handle method
     *
     * @param input the first input argument
     * @param v1 the second input argument
     * @param v2 the third input argument
     * @return v2
     */
    R apply(T input, int v1, int v2);
}

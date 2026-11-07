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
public interface RangeFunctionThrow<T, R, E extends Throwable> extends RangeFunction<T, R> {
    /**
     * handle method
     *
     * @param input the first input argument
     * @param v1    the second input argument
     * @param v2    the third input argument
     * @return v2
     * @throws E throw a exception
     */
    R applyThrow(T input, int v1, int v2) throws E;

    /**
     * handle method
     *
     * @param input the first input argument
     * @param v1 the second input argument
     * @param v2 the third input argument
     * @return v2
     */
    @Override
    default R apply(T input, int v1, int v2) {
        try {
            return applyThrow(input, v1, v2);
        } catch (Throwable e) {
            throw new FunctionException(e);
        }
    }
}

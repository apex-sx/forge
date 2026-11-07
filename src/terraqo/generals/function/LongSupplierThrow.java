/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.function;

import java.util.function.LongSupplier;

/**
 * Like {@link LongSupplier} but throws {@link Throwable}.
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface LongSupplierThrow<E extends Throwable> extends LongSupplier {
    /**
     * Gets a result.
     *
     * @return a result
     * @throws E throw a exception
     */
    long getAsLongThrow() throws E;

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    default long getAsLong() {
        try {
            return getAsLongThrow();
        } catch (Throwable e) {
            throw new FunctionException(e);
        }
    }
}

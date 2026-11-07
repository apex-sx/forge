/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.function;

import java.util.function.IntSupplier;

/**
 * Like {@link IntSupplier} but throws {@link Throwable}.
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface IntSupplierThrow<E extends Throwable> extends IntSupplier {
    /**
     * Gets a result.
     *
     * @return a result
     * @throws E throw a exception
     */
    int getAsIntThrow() throws E;

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    default int getAsInt() {
        try {
            return getAsIntThrow();
        } catch (Throwable e) {
            throw new FunctionException(e);
        }
    }
}

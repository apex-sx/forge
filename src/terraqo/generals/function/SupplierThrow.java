/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.function;

import java.util.function.Supplier;

/**
 * SupplierThrow
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface SupplierThrow<T, E extends Throwable> extends Supplier<T> {

    static <T, E extends Throwable> T get(SupplierThrow<T, E> supplier, T defaultValue) {
        T result = null;
        if (supplier != null) {
            try {
                result = supplier.getThrow();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }


    static <T> T get(Supplier<T> supplier, T defaultValue) {
        T result = null;
        if (supplier != null) {
            result = supplier.get();
        }
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Gets a result.
     *
     * @return a result
     * @throws E throw a exception
     */
    T getThrow() throws E;

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    default T get() {
        try {
            return getThrow();
        } catch (Throwable e) {
            throw new FunctionException(e);
        }
    }
}

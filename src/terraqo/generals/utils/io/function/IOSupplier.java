/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * Like {@link Supplier} but throws {@link IOException}.
 * <p>
 * Using an IOSupplier allows you to compose usage of checked and unchecked exceptions as you best see fit.
 * </p>
 *
 * @param <T> the return type of the operations.
 * @author potatoxf
 */
@FunctionalInterface
public interface IOSupplier<T> {
    /**
     * Gets a result.
     *
     * @return a result.
     * @throws IOException if an I/O error occurs.
     */
    T get() throws IOException;
}

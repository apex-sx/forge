/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Like {@link Runnable} but throws {@link IOException}.
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface IORunnable {

    /**
     * Returns the constant no-op runnable.
     *
     * @return a constant no-op runnable.
     * @author potatoxf
     */
    static IORunnable noop() {
        return Constants.IO_RUNNABLE;
    }

    /**
     * Creates a {@link Runnable} for this instance that throws {@link UncheckedIOException} instead of {@link IOException}.
     *
     * @return an UncheckedIOException Predicate.
     */
    default Runnable asRunnable() {
        return () -> Uncheck.run(this);
    }

    /**
     * Like {@link Runnable#run()} but throws {@link IOException}.
     *
     * @throws IOException if an I/O error occurs.
     */
    void run() throws IOException;
}

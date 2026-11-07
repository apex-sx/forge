/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

/**
 * Defines package-private constants.
 */
final class Constants {

    /**
     * No-op singleton.
     */
    @SuppressWarnings("rawtypes")
    static final IOBiConsumer IO_BI_CONSUMER = (t, u) -> {/* No-op */};

    /**
     * No-op singleton.
     */
    static final IORunnable IO_RUNNABLE = () -> {/* No-op */};

    /**
     * No-op singleton.
     */
    @SuppressWarnings("rawtypes")
    static final IOBiFunction IO_BI_FUNCTION = (t, u) -> null;

    /**
     * No-op singleton.
     */
    @SuppressWarnings("rawtypes")
    static final IOFunction IO_FUNCTION_ID = t -> t;

    /**
     * Always false.
     */
    static final IOPredicate<Object> IO_PREDICATE_FALSE = t -> false;

    /**
     * Always true.
     */
    static final IOPredicate<Object> IO_PREDICATE_TRUE = t -> true;


    private Constants() {
        // We don't want instances
    }

}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.input;

/**
 * Package-private factory for {@link UnsupportedOperationException} to provide messages with consistent formatting.
 *
 * <p>
 * TODO Consider making this public and use from LineIterator but this feels like it belongs in LANG rather than IO.
 * </p>
 */
final class UnsupportedOperationExceptions {

    private static final String MARK_RESET = "mark/reset";

    /**
     * Constructs a new instance of UnsupportedOperationException for a {@code mark} method.
     *
     * @return a new instance of UnsupportedOperationException
     */
    static UnsupportedOperationException mark() {
        // Use the same message as in java.io.InputStream.reset() in OpenJDK 8.0.275-1.
        return method(MARK_RESET);
    }

    /**
     * Constructs a new instance of UnsupportedOperationException for the given unsupported a {@code method} name.
     *
     * @param method A method name
     * @return a new instance of UnsupportedOperationException
     */
    static UnsupportedOperationException method(final String method) {
        return new UnsupportedOperationException(method + " not supported");
    }

    /**
     * Constructs a new instance of UnsupportedOperationException for a {@code reset} method.
     *
     * @return a new instance of UnsupportedOperationException
     */
    static UnsupportedOperationException reset() {
        // Use the same message as in java.io.InputStream.reset() in OpenJDK 8.0.275-1.
        return method(MARK_RESET);
    }
}

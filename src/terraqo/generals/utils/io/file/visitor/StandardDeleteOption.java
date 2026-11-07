/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.visitor;

import java.util.stream.Stream;

import com.github.potatoxf.generals.utils.Safe;

/**
 * Defines the standard delete options.
 *
 * @author potatoxf
 */
public enum StandardDeleteOption implements DeleteOption {

    /**
     * Overrides the read-only attribute to allow deletion, on POSIX, this means Write and Execute on the parent.
     */
    OVERRIDE_READ_ONLY;

    /**
     * Returns true if the given options contain {@link StandardDeleteOption#OVERRIDE_READ_ONLY}.
     *
     * For now, assume the array is not sorted.
     *
     * @param options the array to test
     * @return true if the given options contain {@link StandardDeleteOption#OVERRIDE_READ_ONLY}.
     */
    public static boolean overrideReadOnly(final DeleteOption[] options) {
        if (Safe.length(options) == 0) {
            return false;
        }
        return Stream.of(options).anyMatch(e -> OVERRIDE_READ_ONLY == e);
    }

}

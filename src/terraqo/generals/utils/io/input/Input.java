/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.input;

import java.io.IOException;

/**
 * Package-wide internals for input.
 */
class Input {

    /**
     * Throws an IOException on false input.
     *
     * @param isOpen whether an input is open or not.
     * @throws IOException if {@code isOpen} is false indicating an input is closed.
     */
    static void checkOpen(final boolean isOpen) throws IOException {
        if (!isOpen) {
            throw new IOException("Closed");
        }
    }

}

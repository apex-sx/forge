/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io;

import java.io.File;
import java.io.IOException;

/**
 * Indicates that a file already exists.
 *
 * @author potatoxf
 */
public class FileExistsException extends IOException {

    /**
     * Defines the serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance.
     */
    public FileExistsException() {
    }

    /**
     * Constructs an instance with the specified file.
     *
     * @param file The file that exists
     */
    public FileExistsException(final File file) {
        super("File " + file + " exists");
    }

    /**
     * Constructs an instance with the specified message.
     *
     * @param message The error message
     */
    public FileExistsException(final String message) {
        super(message);
    }

}

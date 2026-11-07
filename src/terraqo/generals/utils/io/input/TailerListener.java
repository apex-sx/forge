/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

/**
 * Listener for events from a {@link Tailer}.
 *
 * @author potatoxf
 */
public interface TailerListener {

    /**
     * This method is called if the tailed file is not found.
     * <p>
     * <strong>Note:</strong> this is called from the tailer thread.
     * </p>
     */
    void fileNotFound();

    /**
     * Called if a file rotation is detected.
     *
     * This method is called before the file is reopened, and fileNotFound may
     * be called if the new file has not yet been created.
     * <p>
     * <strong>Note:</strong> this is called from the tailer thread.
     * </p>
     */
    void fileRotated();

    /**
     * Handles an Exception.
     * <p>
     * <strong>Note:</strong> this is called from the tailer thread.
     * </p>
     * @param ex the exception.
     */
    void handle(Exception ex);

    /**
     * Handles a line from a Tailer.
     * <p>
     * <strong>Note:</strong> this is called from the tailer thread.
     * </p>
     * @param line the line.
     */
    void handle(String line);

    /**
     * The tailer will call this method during construction,
     * giving the listener a method of stopping the tailer.
     * @param tailer the tailer.
     */
    void init(Tailer tailer);

}

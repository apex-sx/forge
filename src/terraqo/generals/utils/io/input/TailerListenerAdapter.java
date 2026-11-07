/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

/**
 * {@link TailerListener} Adapter.
 *
 * @author potatoxf
 */
public class TailerListenerAdapter implements TailerListener {

    /**
     * Called each time the Tailer reaches the end of the file.
     *
     * <strong>Note:</strong> this is called from the tailer thread.
     *
     * Note: a future version of commons-io will pull this method up to the TailerListener interface,
     * for now clients must subclass this class to use this feature.
     *
     * @author potatoxf
     */
    public void endOfFileReached() {
        // noop
    }

    /**
     * This method is called if the tailed file is not found.
     */
    @Override
    public void fileNotFound() {
        // noop
    }

    /**
     * Called if a file rotation is detected.
     *
     * This method is called before the file is reopened, and fileNotFound may
     * be called if the new file has not yet been created.
     */
    @Override
    public void fileRotated() {
        // noop
    }

    /**
     * Handles an Exception .
     * @param ex the exception.
     */
    @Override
    public void handle(final Exception ex) {
        // noop
    }

    /**
     * Handles a line from a Tailer.
     * @param line the line.
     */
    @Override
    public void handle(final String line) {
        // noop
    }

    /**
     * The tailer will call this method during construction,
     * giving the listener a method of stopping the tailer.
     * @param tailer the tailer.
     */
    @Override
    public void init(final Tailer tailer) {
        // noop
    }
}

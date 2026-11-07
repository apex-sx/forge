/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.monitor;

import java.io.File;

/**
 * Convenience {@link FileAlterationListener} implementation that does nothing.
 *
 * @see FileAlterationObserver
 *
 * @author potatoxf
 */
public class FileAlterationListenerAdaptor implements FileAlterationListener {

    /**
     * Directory changed Event.
     *
     * @param directory The directory changed (ignored)
     */
    @Override
    public void onDirectoryChange(final File directory) {
        // noop
    }

    /**
     * Directory created Event.
     *
     * @param directory The directory created (ignored)
     */
    @Override
    public void onDirectoryCreate(final File directory) {
        // noop
    }

    /**
     * Directory deleted Event.
     *
     * @param directory The directory deleted (ignored)
     */
    @Override
    public void onDirectoryDelete(final File directory) {
        // noop
    }

    /**
     * File changed Event.
     *
     * @param file The file changed (ignored)
     */
    @Override
    public void onFileChange(final File file) {
        // noop
    }

    /**
     * File created Event.
     *
     * @param file The file created (ignored)
     */
    @Override
    public void onFileCreate(final File file) {
        // noop
    }

    /**
     * File deleted Event.
     *
     * @param file The file deleted (ignored)
     */
    @Override
    public void onFileDelete(final File file) {
        // noop
    }

    /**
     * File system observer started checking event.
     *
     * @param observer The file system observer (ignored)
     */
    @Override
    public void onStart(final FileAlterationObserver observer) {
        // noop
    }

    /**
     * File system observer finished checking event.
     *
     * @param observer The file system observer (ignored)
     */
    @Override
    public void onStop(final FileAlterationObserver observer) {
        // noop
    }

}

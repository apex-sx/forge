/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.Objects;

/**
 * This class turns a Java FileFilter or FilenameFilter into an IO FileFilter.
 * <h2>Deprecating Serialization</h2>
 * <p>
 * <em>Serialization is deprecated and will be removed in 3.0.</em>
 * </p>
 *
 * @author potatoxf
 * @see FileFilterUtils#asFileFilter(FileFilter)
 * @see FileFilterUtils#asFileFilter(FilenameFilter)
 */
public class DelegateFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -8723373124984771318L;

    /** The File filter */
    private transient final FileFilter fileFilter;
    /** The Filename filter */
    private transient final FilenameFilter fileNameFilter;

    /**
     * Constructs a delegate file filter around an existing FileFilter.
     *
     * @param fileFilter  the filter to decorate
     */
    public DelegateFileFilter(final FileFilter fileFilter) {
        Objects.requireNonNull(fileFilter, "filter");
        this.fileFilter = fileFilter;
        this.fileNameFilter = null;
    }

    /**
     * Constructs a delegate file filter around an existing FilenameFilter.
     *
     * @param fileNameFilter  the filter to decorate
     */
    public DelegateFileFilter(final FilenameFilter fileNameFilter) {
        Objects.requireNonNull(fileNameFilter, "filter");
        this.fileNameFilter = fileNameFilter;
        this.fileFilter = null;
    }

    /**
     * Checks the filter.
     *
     * @param file  the file to check
     * @return true if the filter matches
     */
    @Override
    public boolean accept(final File file) {
        if (fileFilter != null) {
            return fileFilter.accept(file);
        }
        return super.accept(file);
    }

    /**
     * Checks the filter.
     *
     * @param dir  the directory
     * @param name  the file name in the directory
     * @return true if the filter matches
     */
    @Override
    public boolean accept(final File dir, final String name) {
        if (fileNameFilter != null) {
            return fileNameFilter.accept(dir, name);
        }
        return super.accept(dir, name);
    }

    /**
     * Provide a String representation of this file filter.
     *
     * @return a String representation
     */
    @Override
    public String toString() {
        final String delegate = Objects.toString(fileFilter, Objects.toString(fileNameFilter, null));
        return super.toString() + "(" + delegate + ")";
    }

}

/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.file.filter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.attribute.BasicFileAttributes;

import com.github.potatoxf.generals.utils.io.file.visitor.PathFilter;

/**
 * An interface which brings the {@link FileFilter}, {@link FilenameFilter}, {@link PathFilter}, and {@link PathMatcher} interfaces together.
 *
 * @author potatoxf
 */
public interface IOFileFilter extends FileFilter, FilenameFilter, PathFilter, PathMatcher {

    /**
     * An empty String array.
     */
    String[] EMPTY_STRING_ARRAY = {};

    /**
     * Tests if a File should be accepted by this filter.
     * <p>
     * Defined in {@link FileFilter}.
     * </p>
     *
     * @param file the File to check.
     * @return true if this file matches the test.
     */
    @Override
    boolean accept(File file);

    /**
     * Tests if a File should be accepted by this filter.
     * <p>
     * Defined in {@link FilenameFilter}.
     * </p>
     *
     * @param dir  the directory File to check.
     * @param name the file name within the directory to check.
     * @return true if this file matches the test.
     */
    @Override
    boolean accept(File dir, String name);

    /**
     * Checks to see if a Path should be accepted by this filter.
     *
     * @param path the Path to check.
     * @param attributes the path's basic attributes (may be null).
     * @return true if this path matches the test.
     * @author potatoxf
     */
    @Override
    default FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return AbstractFileFilter.toDefaultFileVisitResult(path != null && accept(path.toFile()));
    }

    /**
     * Constructs a new "and" filter with this filter.
     *
     * @param fileFilter the filter to "and".
     * @return a new filter.
     * @author potatoxf
     */
    default IOFileFilter and(final IOFileFilter fileFilter) {
        return new AndFileFilter(this, fileFilter);
    }

    /**
     * Tests if a Path should be accepted by this filter.
     *
     * @param path the Path to check.
     * @return true if this path matches the test.
     * @author potatoxf
     */
    @Override
    default boolean matches(final Path path) {
        return accept(path, null) != FileVisitResult.TERMINATE;
    }

    /**
     * Constructs a new "not" filter with this filter.
     *
     * @return a new filter.
     * @author potatoxf
     */
    default IOFileFilter negate() {
        return new NotFileFilter(this);
    }

    /**
     * Constructs a new "or" filter with this filter.
     *
     * @param fileFilter the filter to "or".
     * @return a new filter.
     * @author potatoxf
     */
    default IOFileFilter or(final IOFileFilter fileFilter) {
        return new OrFileFilter(this, fileFilter);
    }

}

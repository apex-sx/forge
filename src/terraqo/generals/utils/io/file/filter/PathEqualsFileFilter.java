/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.filter;

import java.io.File;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/**
 * Accepts only an exact {@link Path} object match. You can use this filter to visit the start directory when walking a
 * file tree with
 * {@link java.nio.file.Files#walkFileTree(java.nio.file.Path, java.util.Set, int, java.nio.file.FileVisitor)}.
 *
 * @author potatoxf
 */
public class PathEqualsFileFilter extends AbstractFileFilter {

    private final Path path;

    /**
     * Constructs a new instance for the given {@link Path}.
     *
     * @param file The file to match.
     */
    public PathEqualsFileFilter(final Path file) {
        this.path = file;
    }

    @Override
    public boolean accept(final File file) {
        return Objects.equals(this.path, file.toPath());
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return toFileVisitResult(Objects.equals(this.path, path));
    }
}

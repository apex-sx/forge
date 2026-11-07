/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.filter;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import com.github.potatoxf.generals.utils.io.file.visitor.NoopPathVisitor;
import com.github.potatoxf.generals.utils.io.file.PathUtils;
import com.github.potatoxf.generals.utils.io.file.visitor.PathVisitor;

/**
 * A file filter backed by a path visitor.
 *
 * @author potatoxf
 */
public class PathVisitorFileFilter extends AbstractFileFilter {

    private final PathVisitor pathVisitor;

    /**
     * Constructs a new instance that will forward calls to the given visitor.
     *
     * @param pathVisitor visit me.
     */
    public PathVisitorFileFilter(final PathVisitor pathVisitor) {
        this.pathVisitor = pathVisitor == null ? NoopPathVisitor.INSTANCE : pathVisitor;
    }

    @Override
    public boolean accept(final File file) {
        try {
            final Path path = file.toPath();
            return visitFile(path, file.exists() ? PathUtils.readBasicFileAttributes(path) : null) == FileVisitResult.CONTINUE;
        } catch (final IOException e) {
            return handle(e) == FileVisitResult.CONTINUE;
        }
    }

    @Override
    public boolean accept(final File dir, final String name) {
        try {
            final Path path = dir.toPath().resolve(name);
            return accept(path, PathUtils.readBasicFileAttributes(path)) == FileVisitResult.CONTINUE;
        } catch (final IOException e) {
            return handle(e) == FileVisitResult.CONTINUE;
        }
    }

    @Override
    public FileVisitResult accept(final Path path, final BasicFileAttributes attributes) {
        return get(() -> Files.isDirectory(path) ? pathVisitor.postVisitDirectory(path, null) : visitFile(path, attributes));
    }

    @Override
    public FileVisitResult visitFile(final Path path, final BasicFileAttributes attributes) throws IOException {
        return pathVisitor.visitFile(path, attributes);
    }

}

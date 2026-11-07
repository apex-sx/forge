/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.visitor;

import com.github.potatoxf.generals.utils.io.file.PathUtils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.util.Objects;

/**
 * A {@link java.nio.file.DirectoryStream.Filter DirectoryStream.Filter} that delegates to a {@link PathFilter}.
 * <p>
 * You pass this filter to {@link java.nio.file.Files#newDirectoryStream(Path, DirectoryStream.Filter)
 * Files#newDirectoryStream(Path, DirectoryStream.Filter)}.
 * </p>
 *
 * @author potatoxf
 */
public class DirectoryStreamFilter implements DirectoryStream.Filter<Path> {

    private final PathFilter pathFilter;

    /**
     * Constructs a new instance for the given path filter.
     *
     * @param pathFilter How to filter paths.
     */
    public DirectoryStreamFilter(final PathFilter pathFilter) {
        // TODO Instead of NPE, we could map null to FalseFileFilter.
        this.pathFilter = Objects.requireNonNull(pathFilter, "pathFilter");
    }

    @Override
    public boolean accept(final Path path) throws IOException {
        return pathFilter.accept(path, PathUtils.readBasicFileAttributes(path, PathUtils.EMPTY_LINK_OPTION_ARRAY)) == FileVisitResult.CONTINUE;
    }

    /**
     * Gets the path filter.
     *
     * @return the path filter.
     */
    public PathFilter getPathFilter() {
        return pathFilter;
    }

}

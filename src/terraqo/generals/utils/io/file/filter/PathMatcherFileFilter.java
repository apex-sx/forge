/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.filter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.Objects;

/**
 * Delegates matching to a {@link PathMatcher}.
 *
 * @author potatoxf
 */
public class PathMatcherFileFilter extends AbstractFileFilter {

    private final PathMatcher pathMatcher;

    /**
     * Constructs a new instance to perform matching with a PathMatcher.
     *
     * @param pathMatcher The PathMatcher delegate.
     */
    public PathMatcherFileFilter(final PathMatcher pathMatcher) {
        this.pathMatcher = Objects.requireNonNull(pathMatcher, "pathMatcher");
    }

    @Override
    public boolean accept(final File file) {
        return file != null && matches(file.toPath());
    }

    @Override
    public boolean matches(final Path path) {
        return pathMatcher.matches(path);
    }
}

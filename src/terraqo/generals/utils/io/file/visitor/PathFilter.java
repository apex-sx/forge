/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.visitor;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A filter for {@link Path}s.
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface PathFilter {

    /**
     * Tests whether or not to include the specified Path in a result.
     *
     * @param path The Path to test.
     * @param attributes the path's basic attributes (may be null).
     * @return a FileVisitResult
     */
    FileVisitResult accept(Path path, BasicFileAttributes attributes);
}

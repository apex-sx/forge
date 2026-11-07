/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.visitor;

import java.nio.file.FileVisitor;
import java.nio.file.Path;

/**
 * A {@link FileVisitor} typed to a {@link Path}.
 *
 * @author potatoxf
 */
public interface PathVisitor extends FileVisitor<Path> {
    // empty
}

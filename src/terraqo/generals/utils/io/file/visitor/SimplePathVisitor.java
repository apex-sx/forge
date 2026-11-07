/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.util.Objects;

import com.github.potatoxf.generals.utils.io.function.IOBiFunction;

/**
 * A {@link SimpleFileVisitor} typed to a {@link Path}.
 *
 * @author potatoxf
 */
public abstract class SimplePathVisitor extends SimpleFileVisitor<Path> implements PathVisitor {

    private final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailedFunction;

    /**
     * Constructs a new instance.
     */
    protected SimplePathVisitor() {
        this.visitFileFailedFunction = super::visitFileFailed;
    }

    /**
     * Constructs a new instance.
     *
     * @param visitFileFailed Called on {@link #visitFileFailed(Path, IOException)}.
     */
    protected SimplePathVisitor(final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed) {
        this.visitFileFailedFunction = Objects.requireNonNull(visitFileFailed, "visitFileFailed");
    }

    @Override
    public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException {
        return visitFileFailedFunction.apply(file, exc);
    }
}

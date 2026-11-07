/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.visitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;

import com.github.potatoxf.generals.utils.io.function.IOBiFunction;

/**
 * A noop path visitor.
 *
 * @author potatoxf
 */
public class NoopPathVisitor extends SimplePathVisitor {

    /**
     * The singleton instance.
     */
    public static final NoopPathVisitor INSTANCE = new NoopPathVisitor();

    /**
     * Constructs a new instance.
     */
    public NoopPathVisitor() {
    }

    /**
     * Constructs a new instance.
     *
     * @param visitFileFailed Called on {@link #visitFileFailed(Path, IOException)}.
     * @author potatoxf
     */
    public NoopPathVisitor(final IOBiFunction<Path, IOException, FileVisitResult> visitFileFailed) {
        super(visitFileFailed);
    }
}

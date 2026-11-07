/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.file.filter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * This filter accepts {@link File}s that are hidden.
 * <p>
 * Example, showing how to print out a list of the
 * current directory's <em>hidden</em> files:
 * </p>
 * <h2>Using Classic IO</h2>
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(HiddenFileFilter.HIDDEN);
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 *
 * <p>
 * Example, showing how to print out a list of the
 * current directory's <em>visible</em> (not hidden) files:
 * </p>
 *
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(HiddenFileFilter.VISIBLE);
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 *
 * <h2>Using NIO</h2>
 * <pre>
 * final Path dir = PathUtils.current();
 * final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(HiddenFileFilter.HIDDEN);
 * //
 * // Walk one directory
 * Files.<strong>walkFileTree</strong>(dir, Collections.emptySet(), 1, visitor);
 * System.out.println(visitor.getPathCounters());
 * System.out.println(visitor.getFileList());
 * //
 * visitor.getPathCounters().reset();
 * //
 * // Walk directory tree
 * Files.<strong>walkFileTree</strong>(dir, visitor);
 * System.out.println(visitor.getPathCounters());
 * System.out.println(visitor.getDirList());
 * System.out.println(visitor.getFileList());
 * </pre>
 * <h2>Deprecating Serialization</h2>
 * <p>
 * <em>Serialization is deprecated and will be removed in 3.0.</em>
 * </p>
 *
 * @author potatoxf
 */
public class HiddenFileFilter extends AbstractFileFilter implements Serializable {

    /** Singleton instance of <em>hidden</em> filter */
    public static final IOFileFilter HIDDEN  = new HiddenFileFilter();

    private static final long serialVersionUID = 8930842316112759062L;

    /** Singleton instance of <em>visible</em> filter */
    public static final IOFileFilter VISIBLE = HIDDEN.negate();

    /**
     * Restrictive constructor.
     */
    protected HiddenFileFilter() {
    }

    /**
     * Checks to see if the file is hidden.
     *
     * @param file  the File to check
     * @return {@code true} if the file is
     *  <em>hidden</em>, otherwise {@code false}.
     */
    @Override
    public boolean accept(final File file) {
        return file == null || file.isHidden();
    }

    /**
     * Checks to see if the file is hidden.
     *
     * @param file       the File to check
     * @param attributes the path's basic attributes (may be null).
     * @return {@code true} if the file is <em>hidden</em>, otherwise {@code false}.
     * @author potatoxf
     */
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return get(() -> toFileVisitResult(file == null || Files.isHidden(file)));
    }

}

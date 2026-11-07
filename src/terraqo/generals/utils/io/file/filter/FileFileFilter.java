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
 * This filter accepts {@link File}s that are files (not directories).
 * <p>
 * For example, here is how to print out a list of the real files
 * within the current directory:
 * </p>
 * <h2>Using Classic IO</h2>
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(FileFileFilter.INSTANCE);
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 *
 * <h2>Using NIO</h2>
 * <pre>
 * final Path dir = PathUtils.current();
 * final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(FileFileFilter.INSTANCE);
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
 * @see FileFilterUtils#fileFileFilter()
 */
public class FileFileFilter extends AbstractFileFilter implements Serializable {

    /**
     * Singleton instance of file filter.
     *
     * @author potatoxf
     */
    public static final IOFileFilter INSTANCE = new FileFileFilter();

    private static final long serialVersionUID = 5345244090827540862L;

    /**
     * Restrictive constructor.
     */
    protected FileFileFilter() {
    }

    /**
     * Checks to see if the file is a file.
     *
     * @param file  the File to check
     * @return true if the file is a file
     */
    @Override
    public boolean accept(final File file) {
        return file != null && file.isFile();
    }

    /**
     * Checks to see if the file is a file.
     *
     * @param file  the File to check
     * @param attributes the path's basic attributes (may be null).
     * @return true if the file is a file
     * @author potatoxf
     */
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return toFileVisitResult(file != null && Files.isRegularFile(file));
    }

}

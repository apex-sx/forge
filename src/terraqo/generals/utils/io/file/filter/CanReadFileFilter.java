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
 * This filter accepts {@link File}s that can be read.
 * <p>
 * Example, showing how to print out a list of the current directory's <em>readable</em> files:
 * </p>
 * <h2>Using Classic IO</h2>
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(CanReadFileFilter.CAN_READ);
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 *
 * <p>
 * Example, showing how to print out a list of the current directory's <em>un-readable</em> files:
 *
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(CanReadFileFilter.CANNOT_READ);
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 *
 * <p>
 * Example, showing how to print out a list of the current directory's <em>read-only</em> files:
 *
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(CanReadFileFilter.READ_ONLY);
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 * <h2>Deprecating Serialization</h2>
 * <p>
 * <em>Serialization is deprecated and will be removed in 3.0.</em>
 * </p>
 *
 * @author potatoxf
 */
public class CanReadFileFilter extends AbstractFileFilter implements Serializable {

    /** Singleton instance of <em>readable</em> filter */
    public static final IOFileFilter CAN_READ = new CanReadFileFilter();

    /** Singleton instance of not <em>readable</em> filter */
    public static final IOFileFilter CANNOT_READ = CAN_READ.negate();

    /** Singleton instance of <em>read-only</em> filter */
    public static final IOFileFilter READ_ONLY = CAN_READ.and(CanWriteFileFilter.CANNOT_WRITE);

    private static final long serialVersionUID = 3179904805251622989L;

    /**
     * Restrictive constructor.
     */
    protected CanReadFileFilter() {
    }

    /**
     * Checks to see if the file can be read.
     *
     * @param file the File to check.
     * @return {@code true} if the file can be read, otherwise {@code false}.
     */
    @Override
    public boolean accept(final File file) {
        return file != null && file.canRead();
    }

    /**
     * Checks to see if the file can be read.
     *
     * @param file the File to check.
     * @param attributes the path's basic attributes (may be null).
     * @return {@code true} if the file can be read, otherwise {@code false}.
     * @author potatoxf
     */
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return toFileVisitResult(file != null && Files.isReadable(file));
    }

}

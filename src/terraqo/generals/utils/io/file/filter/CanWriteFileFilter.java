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
 * This filter accepts {@link File}s that can be written to.
 * <p>
 * Example, showing how to print out a list of the current directory's <em>writable</em> files:
 * </p>
 * <h2>Using Classic IO</h2>
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(CanWriteFileFilter.CAN_WRITE);
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 * <p>
 * Example, showing how to print out a list of the current directory's <em>un-writable</em> files:
 * </p>
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(CanWriteFileFilter.CANNOT_WRITE);
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 * <p>
 * <strong>N.B.</strong> For read-only files, use {@code CanReadFileFilter.READ_ONLY}.
 * </p>
 * <h2>Deprecating Serialization</h2>
 * <p>
 * <em>Serialization is deprecated and will be removed in 3.0.</em>
 * </p>
 *
 * @author potatoxf
 */
public class CanWriteFileFilter extends AbstractFileFilter implements Serializable {

    /** Singleton instance of <em>writable</em> filter */
    public static final IOFileFilter CAN_WRITE = new CanWriteFileFilter();

    /** Singleton instance of not <em>writable</em> filter */
    public static final IOFileFilter CANNOT_WRITE = CAN_WRITE.negate();

    private static final long serialVersionUID = 5132005214688990379L;

    /**
     * Restrictive constructor.
     */
    protected CanWriteFileFilter() {
    }

    /**
     * Checks to see if the file can be written to.
     *
     * @param file the File to check
     * @return {@code true} if the file can be written to, otherwise {@code false}.
     */
    @Override
    public boolean accept(final File file) {
        return file != null && file.canWrite();
    }

    /**
     * Checks to see if the file can be written to.
     *
     * @param file the File to check
     * @param attributes the path's basic attributes (may be null).
     * @return {@code true} if the file can be written to, otherwise {@code false}.
     * @author potatoxf
     */
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return toFileVisitResult(file != null && Files.isWritable(file));
    }

}

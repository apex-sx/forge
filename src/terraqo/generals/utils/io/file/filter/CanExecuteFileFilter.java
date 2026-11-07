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
 * This filter accepts {@link File}s that can be executed.
 * <p>
 * Example, showing how to print out a list of the
 * current directory's <em>executable</em> files:
 * </p>
 * <h2>Using Classic IO</h2>
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(CanExecuteFileFilter.CAN_EXECUTE);
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 *
 * <p>
 * Example, showing how to print out a list of the
 * current directory's <em>non-executable</em> files:
 * </p>
 *
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(CanExecuteFileFilter.CANNOT_EXECUTE);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * <h2>Deprecating Serialization</h2>
 * <p>
 * <em>Serialization is deprecated and will be removed in 3.0.</em>
 * </p>
 *
 * @author potatoxf
 */
public class CanExecuteFileFilter extends AbstractFileFilter implements Serializable {

    /** Singleton instance of <em>executable</em> filter */
    public static final IOFileFilter CAN_EXECUTE = new CanExecuteFileFilter();

    /** Singleton instance of not <em>executable</em> filter */
    public static final IOFileFilter CANNOT_EXECUTE = CAN_EXECUTE.negate();

    private static final long serialVersionUID = 3179904805251622989L;

    /**
     * Restrictive constructor.
     */
    protected CanExecuteFileFilter() {
        // empty.
    }

    /**
     * Checks to see if the file can be executed.
     *
     * @param file  the File to check.
     * @return {@code true} if the file can be executed, otherwise {@code false}.
     */
    @Override
    public boolean accept(final File file) {
        return file != null && file.canExecute();
    }

    /**
     * Checks to see if the file can be executed.
     *
     * @param file  the File to check.
     * @param attributes the path's basic attributes (may be null).
     * @return {@code true} if the file can be executed, otherwise {@code false}.
     * @author potatoxf
     */
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return toFileVisitResult(file != null && Files.isExecutable(file));
    }

}

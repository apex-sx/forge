/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.file.filter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import com.github.potatoxf.generals.utils.io.IOCase;
import com.github.potatoxf.generals.utils.io.file.PathUtils;

/**
 * Filters file names for a certain prefix.
 * <p>
 * For example, to print all files and directories in the
 * current directory whose name starts with {@code Test}:
 * </p>
 * <h2>Using Classic IO</h2>
 * <pre>
 * File dir = FileUtils.current();
 * String[] files = dir.list(new PrefixFileFilter("Test"));
 * for (String file : files) {
 *     System.out.println(file);
 * }
 * </pre>
 *
 * <h2>Using NIO</h2>
 * <pre>
 * final Path dir = PathUtils.current();
 * final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters(new PrefixFileFilter("Test"));
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
 * @see FileFilterUtils#prefixFileFilter(String)
 * @see FileFilterUtils#prefixFileFilter(String, IOCase)
 */
public class PrefixFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = 8533897440809599867L;

    /** The file name prefixes to search for */
    private final String[] prefixes;

    /** Whether the comparison is case-sensitive. */
    private final IOCase isCase;

    /**
     * Constructs a new Prefix file filter for a list of prefixes.
     *
     * @param prefixes  the prefixes to allow, must not be null
     * @throws NullPointerException if the prefix list is null
     * @throws ClassCastException if the list does not contain Strings
     */
    public PrefixFileFilter(final List<String> prefixes) {
        this(prefixes, IOCase.SENSITIVE);
    }

    /**
     * Constructs a new Prefix file filter for a list of prefixes
     * specifying case-sensitivity.
     *
     * @param prefixes  the prefixes to allow, must not be null
     * @param ioCase  how to handle case sensitivity, null means case-sensitive
     * @throws NullPointerException if the prefix list is null
     * @throws ClassCastException if the list does not contain Strings
     * @author potatoxf
     */
    public PrefixFileFilter(final List<String> prefixes, final IOCase ioCase) {
        Objects.requireNonNull(prefixes, "prefixes");
        this.prefixes = prefixes.toArray(EMPTY_STRING_ARRAY);
        this.isCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    /**
     * Constructs a new Prefix file filter for a single prefix.
     *
     * @param prefix  the prefix to allow, must not be null
     * @throws IllegalArgumentException if the prefix is null
     */
    public PrefixFileFilter(final String prefix) {
        this(prefix, IOCase.SENSITIVE);
    }

    /**
     * Constructs a new Prefix file filter for any of an array of prefixes.
     * <p>
     * The array is not cloned, so could be changed after constructing the
     * instance. This would be inadvisable however.
     * </p>
     *
     * @param prefixes  the prefixes to allow, must not be null
     * @throws IllegalArgumentException if the prefix array is null
     */
    public PrefixFileFilter(final String... prefixes) {
        this(prefixes, IOCase.SENSITIVE);
    }

    /**
     * Constructs a new Prefix file filter for a single prefix
     * specifying case-sensitivity.
     *
     * @param prefix  the prefix to allow, must not be null
     * @param ioCase  how to handle case sensitivity, null means case-sensitive
     * @throws IllegalArgumentException if the prefix is null
     * @author potatoxf
     */
    public PrefixFileFilter(final String prefix, final IOCase ioCase) {
        Objects.requireNonNull(prefix, "prefix");
        this.prefixes = new String[] {prefix};
        this.isCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    /**
     * Constructs a new Prefix file filter for any of an array of prefixes
     * specifying case-sensitivity.
     *
     * @param prefixes  the prefixes to allow, must not be null
     * @param ioCase  how to handle case sensitivity, null means case-sensitive
     * @throws IllegalArgumentException if the prefix is null
     * @author potatoxf
     */
    public PrefixFileFilter(final String[] prefixes, final IOCase ioCase) {
        Objects.requireNonNull(prefixes, "prefixes");
        this.prefixes = prefixes.clone();
        this.isCase = IOCase.value(ioCase, IOCase.SENSITIVE);
    }

    /**
     * Checks to see if the file name starts with the prefix.
     *
     * @param file  the File to check
     * @return true if the file name starts with one of our prefixes
     */
    @Override
    public boolean accept(final File file) {
        return accept(file == null ? null : file.getName());
    }

    /**
     * Checks to see if the file name starts with the prefix.
     *
     * @param file  the File directory
     * @param name  the file name
     * @return true if the file name starts with one of our prefixes
     */
    @Override
    public boolean accept(final File file, final String name) {
        return accept(name);
    }

    /**
     * Checks to see if the file name starts with the prefix.
     *
     * @param file  the File to check
     * @param attributes the path's basic attributes (may be null).
     * @return true if the file name starts with one of our prefixes
     * @author potatoxf
     */
    @Override
    public FileVisitResult accept(final Path file, final BasicFileAttributes attributes) {
        return toFileVisitResult(accept(PathUtils.getFileName(file, Path::toFile)));
    }

    private boolean accept(final String name) {
        return Stream.of(prefixes).anyMatch(prefix -> isCase.checkStartsWith(name, prefix));
    }

    /**
     * Provide a String representation of this file filter.
     *
     * @return a String representation
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        append(prefixes, buffer);
        buffer.append(")");
        return buffer.toString();
    }

}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.file.visitor;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;

import com.github.potatoxf.generals.utils.io.file.PathUtils;
import com.github.potatoxf.generals.utils.io.file.visitor.Counters.PathCounters;

/**
 * Copies a source directory to a target directory.
 *
 * @author potatoxf
 */
public class CopyDirectoryVisitor extends CountingPathVisitor {

    private static CopyOption[] toCopyOption(final CopyOption... copyOptions) {
        return copyOptions == null ? PathUtils.EMPTY_COPY_OPTIONS : copyOptions.clone();
    }

    private final CopyOption[] copyOptions;
    private final Path sourceDirectory;
    private final Path targetDirectory;

    /**
     * Constructs a instance that deletes files except for the files and directories explicitly given.
     *
     * @param pathCounter How to count visits.
     * @param sourceDirectory The source directory
     * @param targetDirectory The target directory
     * @param copyOptions Specifies how the copying should be done.
     */
    public CopyDirectoryVisitor(final PathCounters pathCounter, final Path sourceDirectory, final Path targetDirectory, final CopyOption... copyOptions) {
        super(pathCounter);
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.copyOptions = toCopyOption(copyOptions);
    }

    /**
     * Constructs a instance that deletes files except for the files and directories explicitly given.
     *
     * @param pathCounter How to count visits.
     * @param fileFilter How to filter file paths.
     * @param dirFilter How to filter directory paths.
     * @param sourceDirectory The source directory
     * @param targetDirectory The target directory
     * @param copyOptions Specifies how the copying should be done.
     * @author potatoxf
     */
    public CopyDirectoryVisitor(final PathCounters pathCounter, final PathFilter fileFilter, final PathFilter dirFilter, final Path sourceDirectory,
        final Path targetDirectory, final CopyOption... copyOptions) {
        super(pathCounter, fileFilter, dirFilter);
        this.sourceDirectory = sourceDirectory;
        this.targetDirectory = targetDirectory;
        this.copyOptions = toCopyOption(copyOptions);
    }

    /**
     * Copies the sourceFile to the targetFile.
     *
     * @param sourceFile the source file.
     * @param targetFile the target file.
     * @throws IOException if an I/O error occurs.
     * @author potatoxf
     */
    protected void copy(final Path sourceFile, final Path targetFile) throws IOException {
        Files.copy(sourceFile, targetFile, copyOptions);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CopyDirectoryVisitor other = (CopyDirectoryVisitor) obj;
        return Arrays.equals(copyOptions, other.copyOptions) && Objects.equals(sourceDirectory, other.sourceDirectory)
            && Objects.equals(targetDirectory, other.targetDirectory);
    }

    /**
     * Gets the copy options.
     *
     * @return the copy options.
     * @author potatoxf
     */
    public CopyOption[] getCopyOptions() {
        return copyOptions.clone();
    }

    /**
     * Gets the source directory.
     *
     * @return the source directory.
     * @author potatoxf
     */
    public Path getSourceDirectory() {
        return sourceDirectory;
    }

    /**
     * Gets the target directory.
     *
     * @return the target directory.
     * @author potatoxf
     */
    public Path getTargetDirectory() {
        return targetDirectory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(copyOptions);
        result = prime * result + Objects.hash(sourceDirectory, targetDirectory);
        return result;
    }

    @Override
    public FileVisitResult preVisitDirectory(final Path directory, final BasicFileAttributes attributes)
        throws IOException {
        final Path newTargetDir = resolveRelativeAsString(directory);
        if (Files.notExists(newTargetDir)) {
            Files.createDirectory(newTargetDir);
        }
        return super.preVisitDirectory(directory, attributes);
    }

    /**
     * Relativizes against {@code sourceDirectory}, then resolves against {@code targetDirectory}.
     *
     * We have to call {@link Path#toString()} relative value because we cannot use paths belonging to different
     * FileSystems in the Path methods, usually this leads to {@link ProviderMismatchException}.
     *
     * @param directory the directory to relativize.
     * @return a new path, relativized against sourceDirectory, then resolved against targetDirectory.
     */
    private Path resolveRelativeAsString(final Path directory) {
        return targetDirectory.resolve(sourceDirectory.relativize(directory).toString());
    }

    @Override
    public FileVisitResult visitFile(final Path sourceFile, final BasicFileAttributes attributes) throws IOException {
        final Path targetFile = resolveRelativeAsString(sourceFile);
        copy(sourceFile, targetFile);
        return super.visitFile(targetFile, attributes);
    }

}

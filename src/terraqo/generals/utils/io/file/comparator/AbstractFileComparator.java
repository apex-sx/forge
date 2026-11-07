/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.file.comparator;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Abstract file {@link Comparator} which provides sorting for file arrays and lists.
 *
 * @author potatoxf
 */
abstract class AbstractFileComparator implements Comparator<File> {

    /**
     * Sorts an array of files.
     * <p>
     * This method uses {@link Arrays#sort(Object[], Comparator)} and returns the original array.
     * </p>
     *
     * @param files The files to sort, may be null.
     * @return The sorted array.
     * @author potatoxf
     */
    public File[] sort(final File... files) {
        if (files != null) {
            Arrays.sort(files, this);
        }
        return files;
    }

    /**
     * Sorts a List of files.
     * <p>
     * This method uses {@link List#sort(Comparator)} and returns the original list.
     * </p>
     *
     * @param files The files to sort, may be null.
     * @return The sorted list.
     * @author potatoxf
     */
    public List<File> sort(final List<File> files) {
        if (files != null) {
            files.sort(this);
        }
        return files;
    }

    /**
     * String representation of this file comparator.
     *
     * @return String representation of this file comparator.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

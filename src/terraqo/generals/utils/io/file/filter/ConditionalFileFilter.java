/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.file.filter;

import java.util.List;

/**
 * Defines operations for conditional file filters.
 *
 * @author potatoxf
 */
public interface ConditionalFileFilter {

    /**
     * Adds the specified file filter to the list of file filters at the end of
     * the list.
     *
     * @param ioFileFilter the filter to be added
     * @author potatoxf
     */
    void addFileFilter(IOFileFilter ioFileFilter);

    /**
     * Gets this conditional file filter's list of file filters.
     *
     * @return the file filter list
     * @author potatoxf
     */
    List<IOFileFilter> getFileFilters();

    /**
     * Removes the specified file filter.
     *
     * @param ioFileFilter filter to be removed
     * @return {@code true} if the filter was found in the list,
     * {@code false} otherwise
     * @author potatoxf
     */
    boolean removeFileFilter(IOFileFilter ioFileFilter);

    /**
     * Sets the list of file filters, replacing any previously configured
     * file filters on this filter.
     *
     * @param fileFilters the list of filters
     * @author potatoxf
     */
    void setFileFilters(List<IOFileFilter> fileFilters);

}

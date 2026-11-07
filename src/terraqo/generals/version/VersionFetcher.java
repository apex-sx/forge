/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.version;

/**
 * interface that gets string and returns extracted version
 *
 * @author potatoxf
 */
public interface VersionFetcher {
    /**
     * extracted version
     *
     * @param input the input string
     * @return {@link Version}
     */
    Version fetch(String input);
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.version;

import com.github.potatoxf.generals.lang.Sequential;

/**
 * Implementation of {@link VersionFetcher} that holds a list of other {@link VersionFetcher}
 * and calls them sequentially until any of them manages to find version.
 *
 * @author potatoxf
 */
public class VersionFetcherFromSequential extends Sequential<VersionFetcher> implements VersionFetcher {

    public VersionFetcherFromSequential(VersionFetcher first, VersionFetcher... others) {
        super(first, others);
    }

    @Override
    public Version fetch(String input) {
        for (VersionFetcher fetcher : this) {
            Version version = fetcher.fetch(input);
            if (version != null) {
                return version;
            }
        }
        return null;
    }

}

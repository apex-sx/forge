/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.version;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sometimes version of something is not written clearly in User-Agent string.
 * However it is possible to extract version of other component that can be
 * mapped to needed version. Concrete example is discovery of version of Safari
 * browser by version of Webkit.
 *
 * @author potatoxf
 */
public class VersionFetcherFromMap extends VersionFetcherFromPattern {
    private final Map<String, Version> versions;

    public VersionFetcherFromMap(Pattern pattern, Map<String, Version> versions) {
        super(pattern);
        this.versions = Collections.unmodifiableMap(versions);
    }

    protected Version createVersion(Matcher matcher) {
        return versions.get(matcher.group(1));
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Extracts version from provided UserAgent string using pattern.
 *
 * @author potatoxf
 */
public class VersionFetcherFromPattern implements VersionFetcher {
    private final Pattern pattern;

    public VersionFetcherFromPattern(String regex) {
        this(Pattern.compile(regex, CASE_INSENSITIVE));
    }

    public VersionFetcherFromPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public final Version fetch(String input) {
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) {
            return null;
        }
        return createVersion(matcher);
    }

    protected Version createVersion(Matcher matcher) {
        String fullVersionString = matcher.group(1);
        String majorVersion = matcher.group(2);
        String minorVersion = "0";
        if (matcher.groupCount() > 2) // usually but not always there is a minor version
            minorVersion = matcher.group(3);
        return new Version(fullVersionString, majorVersion, minorVersion);
    }

}

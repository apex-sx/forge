/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.serialization;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A {@link ClassNameMatcher} that uses regular expressions.
 * <p>
 * This object is immutable and thread-safe.
 * </p>
 */
final class RegexpClassNameMatcher implements ClassNameMatcher {

    private final Pattern pattern; // Class is thread-safe

    /**
     * Constructs an object based on the specified pattern.
     *
     * @param pattern a pattern for evaluating acceptable class names
     * @throws NullPointerException if {@code pattern} is null
     */
    public RegexpClassNameMatcher(final Pattern pattern) {
        this.pattern = Objects.requireNonNull(pattern, "pattern");
    }

    /**
     * Constructs an object based on the specified regular expression.
     *
     * @param regex a regular expression for evaluating acceptable class names
     */
    public RegexpClassNameMatcher(final String regex) {
        this(Pattern.compile(regex));
    }

    @Override
    public boolean matches(final String className) {
        return pattern.matcher(className).matches();
    }
}
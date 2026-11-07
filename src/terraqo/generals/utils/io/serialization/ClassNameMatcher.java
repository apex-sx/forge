/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.serialization;

/**
 * An object that matches a Class name to a condition.
 */
@FunctionalInterface
public interface ClassNameMatcher {

    /**
     * Returns {@code true} if the supplied class name matches this object's condition.
     *
     * @param className fully qualified class name
     * @return {@code true} if the class name matches this object's condition
     */
    boolean matches(String className);
}
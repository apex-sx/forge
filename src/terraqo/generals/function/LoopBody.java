/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.function;
/**
 * @author potatoxf
 */
@FunctionalInterface
public interface LoopBody<T> {
    void accept(T value, int index, int length);
}

/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.function;
/**
 * 一位Float功能函数
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface FloatFunction1 {
    /**
     * handle method
     *
     * @param f the first input argument
     * @return value
     */
    float apply(float f);
}
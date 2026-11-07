/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.function;
/**
 * 二位Float功能函数
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface FloatFunction2 {
    /**
     * handle method
     *
     * @param f the first input argument
     * @param s the second input argument
     * @return value
     */
    float apply(float f, float s);
}
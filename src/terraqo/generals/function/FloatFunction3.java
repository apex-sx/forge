/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.function;
/**
 * 三位Float功能函数
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface FloatFunction3 {
    /**
     * handle method
     *
     * @param f the first input argument
     * @param s the second input argument
     * @param t the third input argument
     * @return value
     */
    float apply(float f, float s, float t);
}
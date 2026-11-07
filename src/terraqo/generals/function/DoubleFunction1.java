/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.function;
/**
 * 一位Double功能函数
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface DoubleFunction1 {
    /**
     * handle method
     *
     * @param f the first input argument
     * @return value
     */
    double apply(double f);
}
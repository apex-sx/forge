/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.function;
/**
 * 二位Double功能函数
 *
 * @author potatoxf
 */
@FunctionalInterface
public interface DoubleFunction2 {
    /**
     * handle method
     *
     * @param f the first input argument
     * @param s the second input argument
     * @return value
     */
    double apply(double f, double s);
}
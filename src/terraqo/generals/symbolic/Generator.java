/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.symbolic;

/**
 * 生成器
 *
 * @author potatoxf
 */
public interface Generator<T, E extends Throwable> {
    /**
     * 生成实例
     *
     * @return {@link  T}
     * @throws E 抛出异常
     */
    T generate() throws E;
}

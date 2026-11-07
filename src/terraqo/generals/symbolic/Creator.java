/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.symbolic;

/**
 * 创建器
 *
 * @author potatoxf
 */
public interface Creator<T, E extends Throwable> {
    /**
     * 创建实例
     *
     * @return {@link  T}
     * @throws E 抛出异常
     */
    T create() throws E;
}

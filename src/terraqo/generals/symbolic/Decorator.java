/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.symbolic;

/**
 * 装饰器
 *
 * @author potatoxf
 */
public interface Decorator<D extends Decorated> {

    /**
     * 被装饰实例
     *
     * @return 返回被装饰实例
     */
    D decorated();
}

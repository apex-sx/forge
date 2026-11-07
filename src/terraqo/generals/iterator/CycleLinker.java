/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.iterator;


/**
 * 循环链接器
 *
 * @author potatoxf
 */
public interface CycleLinker<T> {
    /**
     * 下一个结点
     *
     * @return 返回下一个结点
     */

    T next();

    /**
     * 上一个结点
     *
     * @return 返回上一个结点
     */
    T last();
}

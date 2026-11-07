/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.symbolic;

import java.io.Serializable;

/**
 * 用于标记是引用数据模型，该模型拥有主键
 *
 * @author potatoxf
 */
public interface DataModelReferenced<K, E extends DataModelReferenced<K, E>> extends This<E>, Serializable, Cloneable {
    long serialVersionUID = 1L;

    /**
     * 获取当前主键
     *
     * @return {@code Key}主键
     */
    K getCurrentKey();

    /**
     * 获取当前主键
     *
     * @param key {@code Key}主键
     * @return this
     */
    E setCurrentKey(K key);

    /**
     * 获取当前名称
     * <p>
     * 默认不实现
     *
     * @return 返回当前名称
     */
    default String getCurrentName() {
        return null;
    }

    /**
     * 设置当前名称
     * <p>
     * 默认不实现
     *
     * @param name 名称
     * @return this
     */
    default E setCurrentName(String name) {
        return this$();
    }
}

/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.symbolic;

import com.github.potatoxf.generals.utils.GeneralTree;

/**
 * 用于标记是层级数据模型，该模型拥有主键与父级主键
 *
 * @author potatoxf
 */
public interface DataModelHierarchical<K, E extends DataModelHierarchical<K, E>> extends DataModelReferenced<K, E>, GeneralTree.TreeData<K, E> {
    long serialVersionUID = 1L;

    /**
     * 获取当前主键
     *
     * @return {@code Key}主键
     */
    @Override
    K getCurrentKey();

    /**
     * 获取当前主键
     *
     * @param key {@code Key}主键
     * @return this
     */
    @Override
    E setCurrentKey(K key);

    /**
     * 获取当前父级主键
     *
     * @return {@code Key}父级主键
     */
    K getParentKey();

    /**
     * 设置当前父级主键
     *
     * @param key {@code Key}父级主键
     * @return this
     */
    E setParentKey(K key);

    /**
     * 获取当前名称
     * <p>
     * 默认不实现
     *
     * @return 返回当前名称
     */
    @Override
    default String getCurrentName() {
        return DataModelReferenced.super.getCurrentName();
    }

    /**
     * 设置当前名称
     * <p>
     * 默认不实现
     *
     * @param name 名称
     * @return this
     */
    @Override
    default E setCurrentName(String name) {
        return DataModelReferenced.super.setCurrentName(name);
    }

    /**
     * 获取当前父级名称
     * <p>
     * 默认不实现
     *
     * @return 返回当前父级名称
     */
    default String getParentName() {
        return null;
    }

    /**
     * 设置当前父级名称
     * <p>
     * 默认不实现
     *
     * @param name 名称
     * @return this
     */
    default E setParentName(String name) {
        return this$();
    }
}

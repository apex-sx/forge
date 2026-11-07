/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.attributes;

import java.util.Set;

/**
 * 定义属性存在性检查的相关操作接口。
 * <p>
 * 该接口提供了一系列用于查询属性状态的方法，包括判断属性是否存在、获取属性数量以及遍历属性键等。
 *
 * @author Terraqo
 */
public interface AttributeExisting {

    /**
     * 获取当前容器中属性的总数量。
     *
     * @return 属性的数量
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    int size() throws AttributeException;

    /**
     * 判断当前容器是否为空，即不包含任何属性。
     *
     * @return 如果属性数量为 0 则返回 {@code true}，否则返回 {@code false}
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    boolean isEmpty() throws AttributeException;

    /**
     * 判断当前容器是否非空，即至少包含一个属性。
     *
     * @return 如果属性数量大于 0 则返回 {@code true}，否则返回 {@code false}
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    boolean isNotEmpty() throws AttributeException;

    /**
     * 检查指定键对应的属性是否存在。
     *
     * @param key 要检查的属性键，通常不为 null
     * @return 如果存在指定键的属性则返回 {@code true}，否则返回 {@code false}
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    boolean hasAttribute(Object key) throws AttributeException;

    /**
     * 获取所有属性键的集合视图。
     * <p>
     * 返回的集合反映了当前属性的实时状态，对集合的修改可能会影响底层数据（取决于具体实现）。
     *
     * @return 包含所有属性键的 {@link Set} 集合
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    Set<Object> keys() throws AttributeException;
}

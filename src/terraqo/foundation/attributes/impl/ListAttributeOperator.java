/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.attributes.impl;

import io.github.terraqo.foundation.attributes.AttributeException;
import io.github.terraqo.foundation.attributes.AttributeGetter;
import io.github.terraqo.foundation.attributes.AttributeSetter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 基于 {@link List} 实现的属性操作器。
 * <p>
 * 该类通过委托给一个底层的 {@link List} 实例来实现 {@link AttributeGetter} 和 {@link AttributeSetter} 接口，
 * 提供基于整数索引的属性获取、设置及查询功能。
 *
 * @author Terraqo
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class ListAttributeOperator implements AttributeGetter, AttributeSetter {

    /**
     * 底层委托的 List 实例，用于存储和管理属性。
     */
    private final List delegate;

    /**
     * 构造一个新的 ListAttributeOperator 实例。
     *
     * @param delegate 底层用于存储属性的 List 对象，不能为 null
     */
    public ListAttributeOperator(List delegate) {
        this.delegate = delegate;
    }

    /**
     * 获取当前容器中属性的总数量。
     *
     * @return 属性的数量
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    @Override
    public int size() throws AttributeException {
        return delegate.size();
    }

    /**
     * 判断当前容器是否为空，即不包含任何属性。
     *
     * @return 如果属性数量为 0 则返回 {@code true}，否则返回 {@code false}
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    @Override
    public boolean isEmpty() throws AttributeException {
        return delegate.isEmpty();
    }

    /**
     * 判断当前容器是否非空，即至少包含一个属性。
     *
     * @return 如果属性数量大于 0 则返回 {@code true}，否则返回 {@code false}
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    @Override
    public boolean isNotEmpty() throws AttributeException {
        return !delegate.isEmpty();
    }

    /**
     * 检查指定键对应的属性是否存在。
     *
     * @param key 要检查的属性键，通常不为 null
     * @return 如果存在指定键的属性则返回 {@code true}，否则返回 {@code false}
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    @Override
    public boolean hasAttribute(Object key) throws AttributeException {
        if (key instanceof Integer) {
            int index = (int) key;
            return index >= 0 && index < delegate.size();
        }
        return delegate.contains(key);
    }

    /**
     * 获取所有属性键的集合视图。
     * <p>
     * 返回的集合反映了当前属性的实时状态，对集合的修改可能会影响底层数据（取决于具体实现）。
     *
     * @return 包含所有属性键的 {@link Set} 集合
     * @throws AttributeException 当访问属性发生错误时抛出
     */
    @Override
    public Set<Object> keys() throws AttributeException {
        LinkedHashSet<Object> keys = new LinkedHashSet<>();
        for (int i = 0; i < delegate.size(); i++) {
            keys.add(i);
        }
        return keys;
    }

    /**
     * 尝试获取指定键对应的属性值。
     * <p>
     * 如果属性不存在、类型不匹配或在获取过程中发生其他错误，将抛出 {@link AttributeException}。
     * 具体的行为（如是否返回 null 或直接抛异常）取决于实现类的具体逻辑。
     *
     * @param key 属性的键，不能为 null
     * @return 属性值
     * @throws AttributeException 当属性不存在、获取失败或发生其他错误时抛出
     */
    @Override
    public Object tryGetAttribute(Object key) throws AttributeException {
        if (!(key instanceof Integer)) {
            throw new AttributeException("The key must be Integer");
        }
        return delegate.get((Integer) key);
    }

    /**
     * 尝试设置指定键的属性值。
     * <p>
     * 如果属性已存在，则更新其值并返回旧值；
     * 如果属性不存在，则创建新属性并返回 {@code null}。
     *
     * @param key   属性键，不能为 {@code null}
     * @param value 要设置的属性值，可以为 {@code null}
     * @return 设置前的旧值；如果该属性之前不存在，则返回 {@code null}
     * @throws AttributeException 如果属性类型不匹配、权限不足或其他原因导致设置失败
     */
    @Override
    public Object trySetAttribute(Object key, Object value) throws AttributeException {
        if (!(key instanceof Integer)) {
            throw new AttributeException("The key must be Integer");
        }
        int index = (Integer) key;
        // 如果索引超出当前列表大小，先填充 null 值直到索引位置可达
        while (delegate.size() <= index) {
            delegate.add(null);
        }
        return delegate.set(index, value);
    }
}

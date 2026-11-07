/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.cache;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * 基本缓存接口
 *
 * @author potatoxf
 */
public interface Caching<K, V> {
    /**
     * 缓存名称
     *
     * @return 返回缓存名称
     */
    String name();

    /**
     * 是否为空
     *
     * @return 返回是否为空
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 是否不为空
     *
     * @return 返回是否不为空
     */
    default boolean isNotEmpty() {
        return size() != 0;
    }

    /**
     * 获取缓存数量
     *
     * @return 返回缓存数量
     */
    int size();

    /**
     * 是否包括缓存
     *
     * @param key 主键
     * @return 如果存在返回true，否则返回false
     */
    boolean has(K key);

    /**
     * 获取缓存值
     *
     * @param key 主键
     * @return 返回值
     */
    V get(K key);

    /**
     * 获取缓存值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 返回值
     */
    default V get(K key, V defaultValue) {
        Objects.requireNonNull(defaultValue);
        V oldValue = get(key);
        if (oldValue == null) {
            oldValue = defaultValue;
            put(key, oldValue);
        }
        return oldValue;
    }

    /**
     * 获取缓存值
     *
     * @param key               键
     * @param replace           是否替换原来值
     * @param remappingFunction 计算新值函数
     * @return 返回值
     */
    default V get(K key, boolean replace, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        V oldValue = get(key);
        if (oldValue == null) {
            V newValue = remappingFunction.apply(key, null);
            if (newValue != null) {
                put(key, newValue);
                return newValue;
            }
            return null;
        } else {
            //替换，处理存在值
            if (replace) {
                V newValue = remappingFunction.apply(key, oldValue);
                if (newValue != null) {
                    put(key, newValue);
                } else {
                    del(key);
                }
                return newValue;
            }
            //非替换，处理不存在值
            else {
                return oldValue;
            }
        }
    }

    /**
     * 设置缓存值
     *
     * @param key   键
     * @param value 值
     * @return 返回原来值
     */
    V put(K key, V value);

    /**
     * 删除缓存值
     *
     * @param key 主键
     * @return 返回原来值
     */
    V del(K key);

    /**
     * 清空缓存
     */
    void clear();

    /**
     * 获取所有键
     *
     * @return 返回所有值
     */
    Set<K> keys();

    /**
     * 获取所有值
     *
     * @return 返回所有值
     */
    Collection<V> values();

}

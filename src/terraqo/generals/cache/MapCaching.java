/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map实现的缓存
 *
 * @author potatoxf
 */
public class MapCaching<K, V> extends BasicNameCaching<K, V> implements Caching<K, V> {

    /**
     * Backing instance.
     */
    private final Map<K, V> map;

    public MapCaching(String name, Map<K, V> map) {
        super(name);
        this.map = map;
    }

    /**
     * 获取缓存数量
     *
     * @return 返回缓存数量
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * 是否包括缓存
     *
     * @param key 主键
     * @return 如果存在返回true，否则返回false
     */
    @Override
    public boolean has(K key) {
        return map.containsKey(key);
    }

    /**
     * 获取缓存值
     *
     * @param key 主键
     * @return 返回值
     */
    @Override
    public V get(K key) {
        return map.get(key);
    }

    /**
     * 设置缓存值
     *
     * @param key   键
     * @param value 值
     * @return 返回原来值
     */
    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    /**
     * 删除缓存值
     *
     * @param key 主键
     * @return 返回原来值
     */
    @Override
    public V del(K key) {
        return map.remove(key);
    }

    /**
     * 清空缓存
     */
    @Override
    public void clear() {
        map.clear();
    }

    /**
     * 获取所有键
     *
     * @return 返回所有值
     */
    @Override
    public Set<K> keys() {
        return map.keySet();
    }

    /**
     * 获取所有值
     *
     * @return 返回所有值
     */
    @Override
    public Collection<V> values() {
        return map.values();
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.cache;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认基本缓存管理
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public class CachingManagerImpl implements CachingManager, CachingManagerAware {
    private volatile CachingManager cachingManager;

    /**
     * 是否同步缓存
     */
    private boolean concurrent = false;

    /**
     * 获取具有指定名称的缓存。
     * 如果尚不存在具有该名称的缓存，则创建一个新缓存将使用该名称创建并返回。
     *
     * @param name 要获取的缓存的名称
     * @return 具有给定名称的 {@link Caching}
     * @throws CachingException 如果获取 {@link Caching}实例时出错
     */
    @Override
    public <K, V> Caching<K, V> getCaching(String name) throws CachingException {
        if (cachingManager != null) {
            return cachingManager.getCaching(name);
        }
        if (concurrent) {
            return new MapCaching<>(name, new HashMap<>());
        } else {
            return new MapCaching<>(name, new ConcurrentHashMap<>());
        }
    }

    /**
     * 设置{@link CachingManager}
     *
     * @param cachingManager 设置的{@link CachingManager}实例
     */
    @Override
    public void setCacheManager(CachingManager cachingManager) {
        this.cachingManager = cachingManager;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public CachingManagerImpl setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
        return this;
    }
}

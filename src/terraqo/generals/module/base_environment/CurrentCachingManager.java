/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.module.base_environment;

import com.github.potatoxf.generals.cache.Caching;
import com.github.potatoxf.generals.cache.CachingException;
import com.github.potatoxf.generals.cache.CachingManager;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 当前全局缓存
 *
 * @author potatoxf
 */
public interface CurrentCachingManager extends CachingManager {
    AtomicReference<CurrentCachingManager> GLOBAL = new AtomicReference<>();

    static CurrentCachingManager global() {
        CurrentCachingManager value = GLOBAL.get();
        if (value == null) {
            throw new RuntimeException("No set global '" + CurrentCachingManager.class.getSimpleName() + "' instance");
        }
        return value;
    }

    /**
     * 获取具有指定名称的缓存。
     * 如果尚不存在具有该名称的缓存，则创建一个新缓存将使用该名称创建并返回。
     *
     * @param name 要获取的缓存的名称
     * @return 具有给定名称的 {@link Caching}
     * @throws CachingException 如果获取 {@link Caching}实例时出错
     */
    static <K, V> Caching<K, V> getsCaching(String name) throws CachingException {
        return global().getCaching(name);
    }
}

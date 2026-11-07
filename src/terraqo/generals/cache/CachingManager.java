/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.cache;

/**
 * 基本缓存管理器
 *
 * @author potatoxf
 */
public interface CachingManager {

    /**
     * 获取具有指定名称的缓存。
     * 如果尚不存在具有该名称的缓存，则创建一个新缓存将使用该名称创建并返回。
     *
     * @param name 要获取的缓存的名称
     * @return 具有给定名称的 {@link Caching}
     * @throws CachingException 如果获取 {@link Caching}实例时出错
     */
    <K, V> Caching<K, V> getCaching(String name) throws CachingException;
}

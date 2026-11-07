/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.cache;

/**
 * 带有名称的缓存接口基础实现
 *
 * @author potatoxf
 */
public abstract class BasicNameCaching<K, V> implements Caching<K, V> {
    /**
     * 缓存名称
     */
    private final String name;

    public BasicNameCaching(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The Cache name cannot be null.");
        }
        this.name = name;
    }

    /**
     * 缓存名称
     *
     * @return 返回缓存名称
     */
    @Override
    public final String name() {
        return name;
    }
}

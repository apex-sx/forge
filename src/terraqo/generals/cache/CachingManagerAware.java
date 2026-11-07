/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.cache;

/**
 * 由使用 {@link CachingManager} 并希望提供 {@link CachingManager} 的组件实现的接口。
 * <p>
 * 这样做是为了让使用 {@link CachingManager} 的内部安全组件可以注入它，而不是使用自行创建一个。
 *
 * @author potatoxf
 */
public interface CachingManagerAware {

    /**
     * 设置{@link CachingManager}
     *
     * @param cachingManager 设置的{@link CachingManager}实例
     */
    void setCacheManager(CachingManager cachingManager);
}
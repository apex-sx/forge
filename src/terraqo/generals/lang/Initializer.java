/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 初始化器
 *
 * @author potatoxf
 */
public abstract class Initializer {

    private static final Map<Object, Boolean> CACHE = new ConcurrentHashMap<>();

    private final Object key;

    protected Initializer(Object key) {
        this.key = key;
    }

    /**
     * 是否成功初始化
     *
     * @param key 初始化键
     * @return 如果成功初始化过返回true，否则返回false
     */
    public static boolean isSuccess(Object key) {
        return Boolean.TRUE.equals(CACHE.get(key));
    }

    /**
     * 是否处理过
     *
     * @param key 初始化键
     * @return 如果初始化过返回true，否则返回false
     */
    public static boolean isHandled(Object key) {
        return CACHE.get(key) != null;
    }

    /**
     * 开始初始化功能
     *
     * @param initializer 初始化器
     */
    public static void enable(Initializer initializer) {
        Boolean v = CACHE.get(initializer.key);
        if (v == null) {
            try {
                initializer.init();
            } catch (Throwable e) {
                if (Log.isEnabledError()) {
                    Log.error("Error to init", e);
                }
            }
        }
    }

    /**
     * 初始化，如果不能初始化或者已经成功初始化则不进行初始化
     *
     * @throws Throwable 初始化抛出的异常
     */
    public final void init() throws Throwable {
        if (isSuccess()) return;
        try {
            initialize();
            CACHE.put(getClass(), true);
        } catch (Throwable e) {
            CACHE.put(getClass(), false);
        }
    }

    /**
     * 是否成功
     *
     * @return 如果成功返回true，否则返回false
     */
    public final boolean isSuccess() {
        return Boolean.TRUE.equals(CACHE.get(key));
    }

    /**
     * 初始化，如果抛出异常则失败
     *
     * @throws Throwable 初始化抛出的异常
     */
    protected abstract void initialize() throws Throwable;
}

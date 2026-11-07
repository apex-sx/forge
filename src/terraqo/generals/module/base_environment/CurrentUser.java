/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.module.base_environment;

import com.github.potatoxf.generals.Log;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 当前全局用户
 *
 * @author potatoxf
 */
public interface CurrentUser {
    AtomicReference<CurrentUser> GLOBAL = new AtomicReference<>();

    static CurrentUser global() {
        CurrentUser value = GLOBAL.get();
        if (value == null) {
            throw new RuntimeException("No set global '" + CurrentUser.class.getSimpleName() + "' instance");
        }
        return value;
    }

    /**
     * 获取当前用户对象
     *
     * @return 返回当前用户对象
     */
    static <T> T getsObject() {
        try {
            return global().getObject();
        } catch (Throwable e) {
            Log.error("Failed to get the current user object", e);
            return null;
        }
    }

    /**
     * 设置当前用户对象
     */
    static void setsObject(Object object) {
        try {
            global().setObject(object);
        } catch (Throwable e) {
            Log.error("Failed to get the current user object", e);
        }
    }

    /**
     * 获取当前用户唯一id
     *
     * @return 返回当前用户唯一id
     */
    static String getsId() {
        try {
            return global().getId();
        } catch (Throwable ignored) {
            return "System";
        }
    }

    /**
     * 获取当前用户登录名
     *
     * @return 返回当前用户名
     */
    static String getsAccount() {
        return CurrentUser.getsAccount("System");
    }

    /**
     * 获取当前用户登录名
     *
     * @return 返回当前用户名
     */
    static String getsAccount(String defaultValue) {
        try {
        return global().getAccount();
        } catch (Throwable ignored) {
            return defaultValue;
        }
    }

    /**
     * 获取当前用户名
     *
     * @return 返回当前用户名
     */
    static String getsName() {
        return CurrentUser.getsName("System");
    }

    /**
     * 获取当前用户名
     *
     * @return 返回当前用户名
     */
    static String getsName(String defaultValue) {
        try {
        return global().getName();
        } catch (Throwable ignored) {
            return defaultValue;
        }
    }

    /**
     * 获取当前用户登录ip
     *
     * @return 返回当前用户ip
     */
    static String getsIp() {
        try {
        return global().getIp();
        } catch (Throwable ignored) {
            return "localhost";
        }
    }

    /**
     * 获取当前用户对象
     *
     * @return 返回当前用户对象
     */
    <T> T getObject();

    /**
     * 设置当前用户对象
     *
     * @param object 用户对象
     */
    void setObject(Object object);

    /**
     * 获取当前用户唯一id
     *
     * @return 返回当前用户唯一id
     */
    String getId();

    /**
     * 获取当前用户登录名
     *
     * @return 返回当前用户名
     */
    String getAccount();

    /**
     * 获取当前用户名
     *
     * @return 返回当前用户名
     */
    String getName();

    /**
     * 获取当前用户登录ip
     *
     * @return 返回当前用户ip
     */
    String getIp();
}

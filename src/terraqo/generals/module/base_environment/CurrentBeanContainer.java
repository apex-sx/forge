/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.module.base_environment;


import com.github.potatoxf.generals.Log;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 当前Bean容器，方便在非Bean容器获取Bean
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public interface CurrentBeanContainer {
    AtomicReference<CurrentBeanContainer> GLOBAL = new AtomicReference<>();

    static CurrentBeanContainer global() {
        CurrentBeanContainer value = GLOBAL.get();
        if (value == null) {
            throw new RuntimeException("No set global '" + CurrentBeanContainer.class.getSimpleName() + "' instance");
        }
        return value;
    }

    /**
     * 是否包含Bean
     *
     * @param input 输入Bean名称
     * @return 如果包含返回true，否则返回false
     */
    static boolean containBean(String input) {
        return global().hasBean(input);
    }

    /**
     * 是否包含Bean
     *
     * @param input 输入Bean类型
     * @return 如果包含返回true，否则返回false
     */
    static boolean containBean(Class<?> input) {
        return global().hasBean(input);
    }

    /**
     * 判断指定Bean是否是单例
     *
     * @param input 输入Bean类型
     * @return 如果是返回true，否则返回false
     */
    static boolean whetherSingleton(String input) {
        return global().isSingleton(input);
    }

    /**
     * 判断指定Bean是否是单例
     *
     * @param input 输入Bean类型
     * @return 如果是返回true，否则返回false
     */
    static boolean whetherSingleton(Class<?> input) {
        return global().isSingleton(input);
    }

    /**
     * 获取Bean类型{@link Class}
     *
     * @param input 输入Bean名称
     * @return {@link Class} 注册对象的类型
     */
    static Class<?> getsType(String input) {
        try {
            return global().getBean(input);
        } catch (Throwable e) {
            Log.error("Error to get bean type.", e);
            return null;
        }
    }

    /**
     * 获取Bean实例
     *
     * @param input 输入Bean名称
     * @return 返回Bean实例
     * @throws RuntimeException 如果发生异常
     */
    static <T> T getsBean(String input) {
        try {
            return global().getBean(input);
        } catch (Throwable e) {
            Log.error("Error to get bean.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Bean实例
     *
     * @param input 输入Bean类型
     * @return 返回Bean实例
     * @throws RuntimeException 如果发生异常
     */
    static <T> T getsBean(Class<T> input) {
        try {
            return global().getBean(input);
        } catch (Throwable e) {
            Log.error("Error to get bean.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Bean别名
     *
     * @param input 输入Bean名称
     * @return 返回Bean实例
     * @throws RuntimeException 如果发生异常
     */
    static String[] getsAliases(String input) {
        try {
            return global().getAliases(input);
        } catch (Throwable e) {
            Log.error("Error to get bean aliases.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取Bean别名
     *
     * @param input 输入Bean类型
     * @return 返回Bean实例
     */
    static String[] getsAliases(Class<?> input) {
        try {
            return global().getAliases(input);
        } catch (Throwable e) {
            Log.error("Error to get bean aliases.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否包含Bean
     *
     * @param input 输入Bean名称
     * @return 如果包含返回true，否则返回false
     */
    boolean hasBean(String input);

    /**
     * 是否包含Bean
     *
     * @param input 输入Bean类型
     * @return 如果包含返回true，否则返回false
     */
    boolean hasBean(Class<?> input);

    /**
     * 判断指定Bean是否是单例
     *
     * @param input 输入Bean类型
     * @return 如果是返回true，否则返回false
     */
    boolean isSingleton(String input);

    /**
     * 判断指定Bean是否是单例
     *
     * @param input 输入Bean类型
     * @return 如果是返回true，否则返回false
     */
    boolean isSingleton(Class<?> input);

    /**
     * 获取Bean类型{@link Class}
     *
     * @param input 输入Bean名称
     * @return {@link Class} 注册对象的类型
     * @throws Throwable 如果发生异常
     */
    Class<?> getType(String input) throws Throwable;

    /**
     * 获取Bean实例
     *
     * @param input 输入Bean名称
     * @return 返回Bean实例
     * @throws Throwable 如果发生异常
     */
    <T> T getBean(String input) throws Throwable;

    /**
     * 获取Bean实例
     *
     * @param input 输入Bean类型
     * @return 返回Bean实例
     * @throws Throwable 如果发生异常
     */
    <T> T getBean(Class<T> input) throws Throwable;

    /**
     * 获取Bean别名
     *
     * @param input 输入Bean名称
     * @return 返回Bean实例
     * @throws Throwable 如果发生异常
     */
    String[] getAliases(String input) throws Throwable;

    /**
     * 获取Bean别名
     *
     * @param input 输入Bean类型
     * @return 返回Bean实例
     * @throws Throwable 如果发生异常
     */
    String[] getAliases(Class<?> input) throws Throwable;
}

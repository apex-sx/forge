package com.github.zhitron.effortless.lang;

import java.util.function.Supplier;

/**
 * 线程安全的延迟初始化持有者类
 * 使用双重检查锁定实现高效、安全的单例对象创建模式
 *
 * @param <T> 持有的对象类型
 * @author zhitron
 */
public final class LazyHolder<T> {

    /**
     * 使用volatile确保多线程环境下的可见性
     * 双重检查锁定模式保证线程安全的同时提升并发性能
     */
    private volatile T instance;

    /**
     * 实例供应器，用于在需要时创建新实例
     * 使用final保证多线程环境下初始化的安全发布
     */
    private final Supplier<T> instanceDefaultSupplier;

    /**
     * 默认构造函数，使用null作为默认实例供应器
     */
    public LazyHolder() {
        this(null);
    }

    /**
     * 构造函数，指定实例创建逻辑的供应器
     *
     * @param instanceDefaultSupplier 提供实例创建逻辑的供应器
     */
    public LazyHolder(Supplier<T> instanceDefaultSupplier) {
        this.instanceDefaultSupplier = instanceDefaultSupplier;
    }

    /**
     * 检查实例是否尚未初始化
     *
     * @return 如果实例为null则返回true，否则返回false
     */
    public boolean isAbsent() {
        return instance == null;
    }

    /**
     * 获取实例，如果实例尚未初始化则使用默认供应器创建
     *
     * @return 初始化后的实例
     */
    public T get() {
        if (this.isAbsent()) {
            return this.getOrComputeIfAbsent(instanceDefaultSupplier);
        } else {
            return this.getOrComputeIfAbsent(null);
        }
    }

    /**
     * 获取已存在的实例或使用给定的创建器生成新实例
     * <p>
     * 采用双重检查锁定机制来确保多线程环境下初始化的安全性和效率。
     * 如果当前实例已经存在，则直接返回该实例；
     * 否则进入同步代码块进行初始化操作，并通过二次检查防止重复初始化。
     *
     * @param creator 提供实例创建逻辑的供应器，若为null且实例未初始化则抛出异常
     * @return 初始化后的实例
     */
    public T getOrComputeIfAbsent(Supplier<T> creator) {
        // 第一次检查 - 无锁快速路径
        T value = instance;
        if (value == null) {
            synchronized (this) {
                // 第二次检查 - 加锁后再次确认
                value = instance;
                if (value == null) {
                    if (creator == null) {
                        throw new NullPointerException("Creator cannot be null");
                    }
                    // 执行实际创建操作
                    value = creator.get();
                    instance = value;
                }
            }
        }
        return value;
    }
}


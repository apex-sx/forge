/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * 一个基于委托模式的 {@link Set} 实现。
 * <p>
 * 此类将所有 {@link Set} 操作委托给内部持有的另一个 {@link Set} 实例（称为 delegate）。
 * 它支持可选的同步控制和不可修改视图包装，适用于需要自定义集合行为或增强线程安全性的场景。
 * </p>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 */
public class DelegateSet<E> extends DelegateCollection<E> implements Set<E> {
    public DelegateSet(Set<E> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        super(delegate, isUnmodifiable, isSynchronized);
    }

    /**
     * 将给定的 {@code delegate} 包装为不可修改的 {@link Set}。
     * <p>
     * 此方法使用 {@link Collections#unmodifiableSet(Set)} 创建原始集合的不可修改视图。
     * </p>
     *
     * @param delegate 要包装的原始 {@link Collection} 实例
     * @return 不可修改的 {@link Set} 视图
     */
    @Override
    protected Set<E> doUnmodifiable(Collection<E> delegate) {
        return Collections.unmodifiableSet((Set<E>) delegate);
    }

    /**
     * 返回底层的委托 {@link Set} 实例。
     * <p>
     * 此方法主要用于子类，以便在不进行额外强制转换的情况下访问具体 delegate 类型的特有方法。
     * </p>
     *
     * @return 底层的 {@link Set} 实例
     */
    protected Set<E> delegate() {
        return (Set<E>) super.delegate();
    }
}
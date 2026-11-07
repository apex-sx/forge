/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Set;

/**
 * 一个线程安全的 Set 包装器。
 * <p>
 * 该类继承自 {@link SynchronizedCollection}，并提供 {@link Set} 接口的同步实现。
 * </p>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 */
public class SynchronizedSet<E> extends SynchronizedCollection<E> implements Set<E> {

    /**
     * 构造一个同步集合包装器。
     *
     * @param delegate   要包装的底层集合，不能为 null
     * @param mutex 用于同步的锁对象。如果为 null，则不进行同步
     * @throws NullPointerException 如果 delegate 为 null
     */
    public SynchronizedSet(Set<E> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 返回底层的委托集合。
     *
     * @return 底层的 {@link Set} 实例
     */
    @Override
    protected Set<E> delegate() {
        return (Set<E>) super.delegate();
    }
}

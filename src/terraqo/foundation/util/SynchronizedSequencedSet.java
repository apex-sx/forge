/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.SequencedSet;

/**
 * 一个线程安全的 {@link SequencedSet} 包装器。
 * <p>
 * 该类继承自 {@link SynchronizedSequencedCollection}，
 * 并提供 Java 21 引入的 {@link SequencedSet} 接口的同步实现。
 * 所有可选的集合操作都通过互斥锁（mutex）进行同步，以确保线程安全。
 * </p>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 */
public class SynchronizedSequencedSet<E> extends SynchronizedSequencedCollection<E> implements SequencedSet<E> {

    /**
     * 构造一个同步有序集合包装器。
     *
     * @param delegate 要包装的底层有序集合，不能为 null
     * @param mutex    用于同步的锁对象。如果为 null，则调用方需自行保证线程安全
     * @throws NullPointerException 如果 delegate 为 null
     */
    public SynchronizedSequencedSet(SequencedSet<E> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 返回底层的 {@link SequencedSet} 代理对象。
     *
     * @return 底层的有序集合代理
     */
    @Override
    protected SequencedSet<E> delegate() {
        return (SequencedSet<E>) super.delegate();
    }

    /**
     * 返回此集合的逆序视图。
     * <p>
     * 如果设置了互斥锁（mutex），则在同步块中执行反转操作；
     * 否则直接返回代理集合的逆序视图。
     * </p>
     *
     * @return 此集合的逆序视图
     */
    @Override
    public SequencedSet<E> reversed() {
        if (mutex == null) {
            return delegate().reversed();
        }
        synchronized (mutex) {
            return delegate().reversed();
        }
    }

}

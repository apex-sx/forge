/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Comparator;
import java.util.SortedSet;

/**
 * 一个线程安全的 SortedSet 包装器。
 * <p>
 * 该类继承自 {@link SynchronizedSequencedSet}，并扩展支持 {@link SortedSet} 接口特性，
 * 如获取比较器、首尾元素、子集、头集和尾集等操作。所有方法均通过互斥锁保证线程安全。
 * </p>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 */
public class SynchronizedSortedSet<E> extends SynchronizedSequencedSet<E> implements SortedSet<E> {

    /**
     * 构造一个同步排序集合包装器。
     *
     * @param delegate 要包装的底层排序集合，不能为 null
     * @param mutex    用于同步的锁对象。如果为 null，则不进行同步
     * @throws NullPointerException 如果 delegate 为 null
     */
    public SynchronizedSortedSet(SortedSet<E> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 返回底层代理集合，并将其强制转换为 SortedSet。
     *
     * @return 底层的 SortedSet 实例
     */
    @Override
    protected SortedSet<E> delegate() {
        return (SortedSet<E>) super.delegate();
    }

    /**
     * 返回此集合中元素的逆序视图。
     *
     * @return 此集合的逆序视图
     */
    @Override
    public SortedSet<E> reversed() {
        if (mutex == null) {
            return delegate().reversed();
        }
        synchronized (mutex) {
            return delegate().reversed();
        }
    }

    /**
     * 返回用于对此集合中的元素进行排序的比较器，如果此集合使用其元素的自然排序，则返回 null。
     *
     * @return 比较器，或者 null（如果使用自然排序）
     */
    @Override
    public final Comparator<? super E> comparator() {
        if (mutex == null) {
            return delegate().comparator();
        }
        synchronized (mutex) {
            return delegate().comparator();
        }
    }

    /**
     * 返回此集合的部分视图，其元素范围从 fromElement（包含）到 toElement（不包含）。
     *
     * @param fromElement 返回集合的低端点（包含）
     * @param toElement   返回集合的高端点（不包含）
     * @return 此集合指定范围的视图
     */
    @Override
    public final SortedSet<E> subSet(E fromElement, E toElement) {
        if (mutex == null) {
            return delegate().subSet(fromElement, toElement);
        }
        synchronized (mutex) {
            return delegate().subSet(fromElement, toElement);
        }
    }

    /**
     * 返回此集合的部分视图，其元素严格小于 toElement。
     *
     * @param toElement 返回集合的高端点（不包含）
     * @return 此集合指定范围的视图
     */
    @Override
    public final SortedSet<E> headSet(E toElement) {
        if (mutex == null) {
            return delegate().headSet(toElement);
        }
        synchronized (mutex) {
            return delegate().headSet(toElement);
        }
    }

    /**
     * 返回此集合的部分视图，其元素大于或等于 fromElement。
     *
     * @param fromElement 返回集合的低端点（包含）
     * @return 此集合指定范围的视图
     */
    @Override
    public final SortedSet<E> tailSet(E fromElement) {
        if (mutex == null) {
            return delegate().tailSet(fromElement);
        }
        synchronized (mutex) {
            return delegate().tailSet(fromElement);
        }
    }

    /**
     * 返回此集合中当前的第一个（最低）元素。
     *
     * @return 此集合中当前的第一个元素
     * @throws java.util.NoSuchElementException 如果此集合为空
     */
    @Override
    public final E first() {
        if (mutex == null) {
            return delegate().first();
        }
        synchronized (mutex) {
            return delegate().first();
        }
    }

    /**
     * 返回此集合中当前的最后一个（最高）元素。
     *
     * @return 此集合中当前的最后一个元素
     * @throws java.util.NoSuchElementException 如果此集合为空
     */
    @Override
    public final E last() {
        if (mutex == null) {
            return delegate().last();
        }
        synchronized (mutex) {
            return delegate().last();
        }
    }
}

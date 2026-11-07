/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.SortedMap;

/**
 * 一个线程安全的 {@link SortedMap} 包装器。
 * <p>
 * 该类继承自 {@link SynchronizedSequencedMap}，并实现了 {@link SortedMap} 接口，
 * 提供了对排序映射的完整支持，包括范围视图、首尾键获取以及比较器访问等功能。
 * </p>
 * <p>
 * 所有方法调用都会通过锁对象进行同步，以确保在多线程环境下的线程安全性。
 * </p>
 *
 * @param <K> 键的类型
 * @param <V> 值的类型
 * @author Terraqo
 */
public class SynchronizedSortedMap<K, V> extends SynchronizedSequencedMap<K, V> implements SortedMap<K, V> {

    /**
     * 构造一个同步排序映射包装器。
     *
     * @param delegate 要包装的底层排序映射，不能为 {@code null}
     * @param mutex    用于同步的锁对象。如果为 {@code null}，则不进行同步
     * @throws NullPointerException 如果 {@code delegate} 为 {@code null}
     */
    public SynchronizedSortedMap(SortedMap<K, V> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 返回底层的 {@link SortedMap} 代理对象。
     *
     * @return 底层的排序映射实例
     */
    @Override
    protected SortedMap<K, V> delegate() {
        return (SortedMap<K, V>) super.delegate();
    }

    /**
     * 返回用于对此映射中的键进行排序的比较器，如果此映射使用其键的自然排序，则返回 {@code null}。
     *
     * @return 用于对此映射中的键进行排序的比较器，如果使用自然排序则返回 {@code null}
     */
    @Override
    public final Comparator<? super K> comparator() {
        if (mutex == null) {
            return delegate().comparator();
        }
        synchronized (mutex) {
            return delegate().comparator();
        }
    }

    /**
     * 返回此映射的部分视图，其键的范围从 {@code fromKey}（包含）到 {@code toKey}（不包含）。
     *
     * @param fromKey 返回映射中键的低端点（包含）
     * @param toKey   返回映射中键的高端点（不包含）
     * @return 此映射指定键范围内的部分视图
     */
    @Override
    public final SortedMap<K, V> subMap(K fromKey, K toKey) {
        if (mutex == null) {
            return delegate().subMap(fromKey, toKey);
        }
        synchronized (mutex) {
            return new SynchronizedSortedMap<>(delegate().subMap(fromKey, toKey), mutex);
        }
    }

    /**
     * 返回此映射的部分视图，其键严格小于 {@code toKey}。
     *
     * @param toKey 返回映射中键的高端点（不包含）
     * @return 此映射指定键范围内的部分视图
     */
    @Override
    public final SortedMap<K, V> headMap(K toKey) {
        if (mutex == null) {
            return delegate().headMap(toKey);
        }
        synchronized (mutex) {
            return new SynchronizedSortedMap<>(delegate().headMap(toKey), mutex);
        }
    }

    /**
     * 返回此映射的部分视图，其键大于或等于 {@code fromKey}。
     *
     * @param fromKey 返回映射中键的低端点（包含）
     * @return 此映射指定键范围内的部分视图
     */
    @Override
    public final SortedMap<K, V> tailMap(K fromKey) {
        if (mutex == null) {
            return delegate().tailMap(fromKey);
        }
        synchronized (mutex) {
            return new SynchronizedSortedMap<>(delegate().tailMap(fromKey), mutex);
        }
    }

    /**
     * 返回此映射中当前第一个（最低）键。
     *
     * @return 此映射中当前第一个键
     * @throws NoSuchElementException 如果此映射为空
     */
    @Override
    public final K firstKey() {
        if (mutex == null) {
            return delegate().firstKey();
        }
        synchronized (mutex) {
            return delegate().firstKey();
        }
    }

    /**
     * 返回此映射中当前最后一个（最高）键。
     *
     * @return 此映射中当前最后一个键
     * @throws NoSuchElementException 如果此映射为空
     */
    @Override
    public final K lastKey() {
        if (mutex == null) {
            return delegate().lastKey();
        }
        synchronized (mutex) {
            return delegate().lastKey();
        }
    }

    /**
     * 返回此映射的逆序视图。
     * <p>
     * 如果未设置锁对象（mutex 为 null），则直接返回底层映射的逆序视图；
     * 否则，在同步块中创建并返回一个新的同步排序映射包装器，该包装器包裹底层映射的逆序视图。
     *
     * @return 此映射的逆序视图
     */
    @Override
    public SortedMap<K, V> reversed() {
        if (mutex == null) {
            return delegate().reversed();
        }
        synchronized (mutex) {
            return new SynchronizedSortedMap<>(delegate().reversed(), mutex);
        }
    }
}

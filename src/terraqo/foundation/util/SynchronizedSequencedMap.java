/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;


import java.util.SequencedCollection;
import java.util.SequencedMap;
import java.util.SequencedSet;

/**
 * 一个线程安全的 {@link SequencedMap} 包装器。
 * <p>
 * 该类继承自 {@link SynchronizedMap}，并扩展支持 Java 21 引入的
 * {@link SequencedMap} 接口特性。它通过同步机制确保对底层映射的所有访问都是线程安全的。
 * </p>
 * <p>
 * 主要功能包括：
 * <ul>
 *     <li>获取首尾条目 ({@link #firstEntry()}, {@link #lastEntry()})</li>
 *     <li>在首尾插入条目 ({@link #putFirst(Object, Object)}, {@link #putLast(Object, Object)})</li>
 *     <li>移除首尾条目 ({@link #pollFirstEntry()}, {@link #pollLastEntry()})</li>
 *     <li>获取反向视图 ({@link #reversed()})，返回的视图也是线程安全的</li>
 *     <li>提供有序的键集、值集合和条目集视图 ({@link #sequencedKeySet()}, {@link #sequencedValues()}, {@link #sequencedEntrySet()})</li>
 * </ul>
 * </p>
 *
 * @param <K> 映射中键的类型
 * @param <V> 映射中值的类型
 * @author Terraqo
 */
public class SynchronizedSequencedMap<K, V> extends SynchronizedMap<K, V> implements SequencedMap<K, V> {

    /**
     * 构造一个同步有序映射包装器。
     *
     * @param delegate 要包装的底层有序映射，不能为 {@code null}
     * @param mutex    用于同步的锁对象。如果为 {@code null}，则不进行同步
     * @throws NullPointerException 如果 {@code delegate} 为 {@code null}
     */
    public SynchronizedSequencedMap(SequencedMap<K, V> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 返回底层的 {@link SequencedMap} 代理对象。
     *
     * @return 底层的有序映射实例
     */
    protected SequencedMap<K, V> delegate() {
        return (SequencedMap<K, V>) super.delegate();
    }

    /**
     * 返回此映射的反向视图。
     * <p>
     * 如果提供了锁对象，则在同步块中创建一个新的同步包装器来包装反向视图，以确保反向视图的操作也是线程安全的。
     *
     * @return 此映射的反向视图
     */
    @Override
    public SequencedMap<K, V> reversed() {
        if (mutex == null) {
            return delegate().reversed();
        }
        synchronized (mutex) {
            return new SynchronizedSequencedMap<>(delegate().reversed(), mutex);
        }
    }

    /**
     * 返回与此映射中第一个（最小索引或最小键）关联的键值对 entry，如果映射为空则返回 {@code null}。
     *
     * @return 具有第一个位置的 entry，如果映射为空则返回 {@code null}
     */
    @Override
    public final Entry<K, V> firstEntry() {
        if (mutex == null) {
            return delegate().firstEntry();
        }
        synchronized (mutex) {
            return delegate().firstEntry();
        }
    }

    /**
     * 返回与此映射中最后一个（最大索引或最大键）关联的键值对 entry，如果映射为空则返回 {@code null}。
     *
     * @return 具有最后一个位置的 entry，如果映射为空则返回 {@code null}
     */
    @Override
    public final Entry<K, V> lastEntry() {
        if (mutex == null) {
            return delegate().lastEntry();
        }
        synchronized (mutex) {
            return delegate().lastEntry();
        }
    }

    /**
     * 移除并返回与此映射中第一个（最小索引或最小键）关联的键值对 entry，如果映射为空则返回 {@code null}。
     *
     * @return 已移除的具有第一个位置的 entry，如果映射为空则返回 {@code null}
     */
    @Override
    public final Entry<K, V> pollFirstEntry() {
        if (mutex == null) {
            return delegate().pollFirstEntry();
        }
        synchronized (mutex) {
            return delegate().pollFirstEntry();
        }
    }

    /**
     * 移除并返回与此映射中最后一个（最大索引或最大键）关联的键值对 entry，如果映射为空则返回 {@code null}。
     *
     * @return 已移除的具有最后一个位置的 entry，如果映射为空则返回 {@code null}
     */
    @Override
    public final Entry<K, V> pollLastEntry() {
        if (mutex == null) {
            return delegate().pollLastEntry();
        }
        synchronized (mutex) {
            return delegate().pollLastEntry();
        }
    }

    /**
     * 将指定的键值对插入到此映射的开头。
     *
     * @param k 要关联指定值的键
     * @param v 要与指定键关联的值
     * @return 与指定键关联的前一个值，如果没有映射关系则返回 {@code null}
     */
    @Override
    public final V putFirst(K k, V v) {
        if (mutex == null) {
            return delegate().putFirst(k, v);
        }
        synchronized (mutex) {
            return delegate().putFirst(k, v);
        }
    }

    /**
     * 将指定的键值对插入到此映射的末尾。
     *
     * @param k 要关联指定值的键
     * @param v 要与指定键关联的值
     * @return 与指定键关联的前一个值，如果没有映射关系则返回 {@code null}
     */
    @Override
    public final V putLast(K k, V v) {
        if (mutex == null) {
            return delegate().putLast(k, v);
        }
        synchronized (mutex) {
            return delegate().putLast(k, v);
        }
    }

    /**
     * 返回此映射中包含的键的 {@link java.util.SequencedSet} 视图。
     * <p>
     * 如果提供了锁对象，则返回一个同步的有序集合视图，以确保对键集的迭代和访问是线程安全的。
     *
     * @return 此映射中键的有序集合视图
     */
    @Override
    public final SequencedSet<K> sequencedKeySet() {
        if (mutex == null) {
            return delegate().sequencedKeySet();
        }
        synchronized (mutex) {
            return new SynchronizedSequencedSet<>(delegate().sequencedKeySet(), mutex);
        }
    }

    /**
     * 返回此映射中包含的值的 {@link java.util.SequencedCollection} 视图。
     * <p>
     * 如果提供了锁对象，则返回一个同步的有序集合视图，以确保对值集合的迭代和访问是线程安全的。
     *
     * @return 此映射中值的有序集合视图
     */
    @Override
    public final SequencedCollection<V> sequencedValues() {
        if (mutex == null) {
            return delegate().sequencedValues();
        }
        synchronized (mutex) {
            return new SynchronizedSequencedCollection<>(delegate().sequencedValues(), mutex);
        }
    }

    /**
     * 返回此映射中包含的映射关系的 {@link java.util.SequencedSet} 视图。
     * <p>
     * 如果提供了锁对象，则返回一个同步的有序集合视图，以确保对条目集的迭代和访问是线程安全的。
     *
     * @return 此映射中映射关系的有序集合视图
     */
    @Override
    public final SequencedSet<Entry<K, V>> sequencedEntrySet() {
        if (mutex == null) {
            return delegate().sequencedEntrySet();
        }
        synchronized (mutex) {
            return new SynchronizedSequencedSet<>(delegate().sequencedEntrySet(), mutex);
        }
    }
}

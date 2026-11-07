/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;


import java.util.NavigableMap;
import java.util.NavigableSet;

/**
 * 一个线程安全的 {@link NavigableMap} 包装器。
 * <p>
 * 该类继承自 {@link SynchronizedSortedMap}，并实现了 {@link NavigableMap} 接口中定义的所有导航方法。
 * 它通过同步机制确保对底层映射的所有访问都是线程安全的。
 * </p>
 * <p>
 * 主要功能包括：
 * <ul>
 *     <li>导航方法：{@link #lowerEntry}, {@link #floorEntry}, {@link #ceilingEntry}, {@link #higherEntry}</li>
 *     <li>导航键方法：{@link #lowerKey}, {@link #floorKey}, {@link #ceilingKey}, {@link #higherKey}</li>
 *     <li>首尾条目：{@link #firstEntry}, {@link #lastEntry}</li>
 *     <li>轮询方法：{@link #pollFirstEntry}, {@link #pollLastEntry}</li>
 *     <li>降序视图：{@link #descendingMap}, {@link #descendingKeySet}, {@link #navigableKeySet}</li>
 *     <li>范围视图：{@link #subMap}, {@link #headMap}, {@link #tailMap}（带边界参数）</li>
 * </ul>
 * </p>
 *
 * @param <K> 映射中键的类型
 * @param <V> 映射中值的类型
 * @author Terraqo
 */
public class SynchronizedNavigableMap<K, V> extends SynchronizedSortedMap<K, V> implements NavigableMap<K, V> {

    /**
     * 构造一个同步可导航映射包装器。
     *
     * @param delegate 要包装的底层可导航映射，不能为 {@code null}
     * @param mutex    用于同步的锁对象。如果为 {@code null}，则不进行同步
     * @throws NullPointerException 如果 {@code delegate} 为 {@code null}
     */
    public SynchronizedNavigableMap(NavigableMap<K, V> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 返回底层的 {@link NavigableMap} 代理对象。
     *
     * @return 底层的可导航映射实例
     */
    @Override
    protected NavigableMap<K, V> delegate() {
        return (NavigableMap<K, V>) super.delegate();
    }

    /**
     * 返回严格小于给定键的最大键值对 entry。
     *
     * @param key 要搜索的键
     * @return 严格小于 key 的最大 entry，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public final Entry<K, V> lowerEntry(K key) {
        if (mutex == null) {
            return delegate().lowerEntry(key);
        }
        synchronized (mutex) {
            return delegate().lowerEntry(key);
        }
    }

    /**
     * 返回严格小于给定键的最大键。
     *
     * @param key 要搜索的键
     * @return 严格小于 key 的最大键，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public final K lowerKey(K key) {
        if (mutex == null) {
            return delegate().lowerKey(key);
        }
        synchronized (mutex) {
            return delegate().lowerKey(key);
        }
    }

    /**
     * 返回小于或等于给定键的最大键值对 entry。
     *
     * @param key 要搜索的键
     * @return 小于或等于 key 的最大 entry，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public final Entry<K, V> floorEntry(K key) {
        if (mutex == null) {
            return delegate().floorEntry(key);
        }
        synchronized (mutex) {
            return delegate().floorEntry(key);
        }
    }

    /**
     * 返回小于或等于给定键的最大键。
     *
     * @param key 要搜索的键
     * @return 小于或等于 key 的最大键，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public final K floorKey(K key) {
        if (mutex == null) {
            return delegate().floorKey(key);
        }
        synchronized (mutex) {
            return delegate().floorKey(key);
        }
    }

    /**
     * 返回大于或等于给定键的最小键值对 entry。
     *
     * @param key 要搜索的键
     * @return 大于或等于 key 的最小 entry，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public final Entry<K, V> ceilingEntry(K key) {
        if (mutex == null) {
            return delegate().ceilingEntry(key);
        }
        synchronized (mutex) {
            return delegate().ceilingEntry(key);
        }
    }

    /**
     * 返回大于或等于给定键的最小键。
     *
     * @param key 要搜索的键
     * @return 大于或等于 key 的最小键，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public final K ceilingKey(K key) {
        if (mutex == null) {
            return delegate().ceilingKey(key);
        }
        synchronized (mutex) {
            return delegate().ceilingKey(key);
        }
    }

    /**
     * 返回严格大于给定键的最小键值对 entry。
     *
     * @param key 要搜索的键
     * @return 严格大于 key 的最小 entry，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public final Entry<K, V> higherEntry(K key) {
        if (mutex == null) {
            return delegate().higherEntry(key);
        }
        synchronized (mutex) {
            return delegate().higherEntry(key);
        }
    }

    /**
     * 返回严格大于给定键的最小键。
     *
     * @param key 要搜索的键
     * @return 严格大于 key 的最小键，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public final K higherKey(K key) {
        if (mutex == null) {
            return delegate().higherKey(key);
        }
        synchronized (mutex) {
            return delegate().higherKey(key);
        }
    }

    /**
     * 返回此映射的逆序视图。
     * <p>
     * 如果提供了锁对象（mutex），则返回的降序映射也将是线程安全的，
     * 并与当前映射共享同一个锁。
     * </p>
     *
     * @return 此映射的逆序视图
     */
    @Override
    public final NavigableMap<K, V> descendingMap() {
        if (mutex == null) {
            return delegate().descendingMap();
        }
        synchronized (mutex) {
            return new SynchronizedNavigableMap<>(delegate().descendingMap(), mutex);
        }
    }

    /**
     * 返回此映射中包含的键的 {@link NavigableSet} 视图。
     * <p>
     * 如果提供了锁对象（mutex），则返回的集合视图也将是线程安全的，
     * 并与当前映射共享同一个锁。
     * </p>
     *
     * @return 此映射中键的可导航集合视图
     */
    @Override
    public final NavigableSet<K> navigableKeySet() {
        if (mutex == null) {
            return delegate().navigableKeySet();
        }
        synchronized (mutex) {
            return new SynchronizedNavigableSet<>(delegate().navigableKeySet(), mutex);
        }
    }

    /**
     * 返回此映射中包含的键的逆序 {@link NavigableSet} 视图。
     * <p>
     * 如果提供了锁对象（mutex），则返回的集合视图也将是线程安全的，
     * 并与当前映射共享同一个锁。
     * </p>
     *
     * @return 此映射中键的逆序可导航集合视图
     */
    @Override
    public final NavigableSet<K> descendingKeySet() {
        if (mutex == null) {
            return delegate().descendingKeySet();
        }
        synchronized (mutex) {
            return new SynchronizedNavigableSet<>(delegate().descendingKeySet(), mutex);
        }
    }

    /**
     * 返回此映射的部分视图，其键范围从 {@code fromKey} 到 {@code toKey}。
     * <p>
     * 如果提供了锁对象（mutex），则返回的子映射也将是线程安全的，
     * 并与当前映射共享同一个锁。
     * </p>
     *
     * @param fromKey       返回映射中键的低端点（包含与否由 fromInclusive 决定）
     * @param fromInclusive 如果为 true，则低端点包含在返回的视图中
     * @param toKey         返回映射中键的高端点（包含与否由 toInclusive 决定）
     * @param toInclusive   如果为 true，则高端点包含在返回的视图中
     * @return 此映射指定范围内的部分视图
     */
    @Override
    public final NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        if (mutex == null) {
            return delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
        }
        synchronized (mutex) {
            return new SynchronizedNavigableMap<>(delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), mutex);
        }
    }

    /**
     * 返回此映射的部分视图，其键严格小于（或等于，如果 inclusive 为 true）toKey。
     * <p>
     * 如果提供了锁对象（mutex），则返回的头映射也将是线程安全的，
     * 并与当前映射共享同一个锁。
     * </p>
     *
     * @param toKey     返回映射中键的高端点
     * @param inclusive 如果为 true，则高端点包含在返回的视图中
     * @return 此映射头部部分视图
     */
    @Override
    public final NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        if (mutex == null) {
            return delegate().headMap(toKey, inclusive);
        }
        synchronized (mutex) {
            return new SynchronizedNavigableMap<>(delegate().headMap(toKey, inclusive), mutex);
        }
    }

    /**
     * 返回此映射的部分视图，其键大于或等于（或严格大于，如果 inclusive 为 false）fromKey。
     * <p>
     * 如果提供了锁对象（mutex），则返回的尾映射也将是线程安全的，
     * 并与当前映射共享同一个锁。
     * </p>
     *
     * @param fromKey   返回映射中键的低端点
     * @param inclusive 如果为 true，则低端点包含在返回的视图中
     * @return 此映射尾部部分视图
     */
    @Override
    public final NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        if (mutex == null) {
            return delegate().tailMap(fromKey, inclusive);
        }
        synchronized (mutex) {
            return new SynchronizedNavigableMap<>(delegate().tailMap(fromKey, inclusive), mutex);
        }
    }

    /**
     * 返回此映射的逆序视图。
     * <p>
     * 如果提供了锁对象（mutex），则返回的逆序视图也将是线程安全的，
     * 并与当前映射共享同一个锁。
     * </p>
     *
     * @return 此映射的逆序视图
     */
    @Override
    public NavigableMap<K, V> reversed() {
        if (mutex == null) {
            return delegate().reversed();
        }
        synchronized (mutex) {
            return new SynchronizedNavigableMap<>(delegate().reversed(), mutex);
        }
    }

}

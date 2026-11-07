package io.github.terraqo.foundation.util;

import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;

/**
 * 一个基于委托模式的 {@link NavigableMap} 实现。
 * <p>
 * 该类继承自 {@link DelegateSortedMap}，并实现了 {@link NavigableMap} 接口中定义的所有导航方法。
 * 它通过将操作委托给内部持有的 {@link NavigableMap} 实例来工作，同时支持可选的不可变性和同步控制。
 *
 * @param <K> 映射中键的类型
 * @param <V> 映射中值的类型
 * @author Terraqo
 */
public class DelegateNavigableMap<K, V> extends DelegateSortedMap<K, V> implements NavigableMap<K, V> {

    /**
     * 构造一个新的 DelegateNavigableMap。
     *
     * @param delegate       被委托的基础 {@link NavigableMap} 实例
     * @param isUnmodifiable 如果为 true，则将 delegate 包装为不可修改的 Map。
     * @param isSynchronized 如果为 true 且 isUnmodifiable 为 false，则将 delegate 包装为同步的 Map。
     */
    public DelegateNavigableMap(NavigableMap<K, V> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        super(delegate, isUnmodifiable, isSynchronized);
    }

    /**
     * 将给定的 delegate 包装为不可修改的 NavigableMap。
     * <p>
     * 此方法使用 {@link Collections#unmodifiableNavigableMap(NavigableMap)} 创建原始 map 的不可修改视图。
     * </p>
     *
     * @param delegate 要包装的原始 Map 实例
     * @return 不可修改的 NavigableMap 视图
     */
    @Override
    protected NavigableMap<K, V> doUnmodifiable(Map<K, V> delegate) {
        return Collections.unmodifiableNavigableMap((NavigableMap<K, V>) delegate);
    }

    /**
     * 将给定的 delegate 包装为同步的 NavigableMap。
     * <p>
     * 此方法使用 {@link SynchronizedNavigableMap} 创建原始 map 的同步视图。
     * </p>
     *
     * @param delegate 要包装的原始 Map 实例
     * @return 同步的 NavigableMap 视图
     */
    @Override
    protected NavigableMap<K, V> doSynchronized(Map<K, V> delegate) {
        return new SynchronizedNavigableMap<>((NavigableMap<K, V>) delegate, delegate);
    }

    /**
     * 返回强类型的委托 NavigableMap 实例。
     * <p>
     * 此方法主要用于子类，以便在不进行额外强制转换的情况下访问具体 delegate 类型的特有方法。
     * </p>
     *
     * @return 强类型的 delegate NavigableMap 实例
     */
    @Override
    protected NavigableMap<K, V> delegate() {
        return (NavigableMap<K, V>) super.delegate();
    }

    /**
     * 返回一个键值对映射关系，其键是此映射中严格小于给定键的最大键；
     * 如果不存在这样的键，则返回 {@code null}。
     *
     * @param key 要匹配的键
     * @return 严格小于给定键的最大键的关联条目，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public Entry<K, V> lowerEntry(K key) {
        return delegate().lowerEntry(key);
    }

    /**
     * 返回此映射中严格小于给定键的最大键；如果不存在这样的键，则返回 {@code null}。
     *
     * @param key 要匹配的键
     * @return 严格小于给定键的最大键，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public K lowerKey(K key) {
        return delegate().lowerKey(key);
    }

    /**
     * 返回一个键值对映射关系，其键是此映射中小于或等于给定键的最大键；
     * 如果不存在这样的键，则返回 {@code null}。
     *
     * @param key 要匹配的键
     * @return 小于或等于给定键的最大键的关联条目，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public Entry<K, V> floorEntry(K key) {
        return delegate().floorEntry(key);
    }

    /**
     * 返回此映射中小于或等于给定键的最大键；如果不存在这样的键，则返回 {@code null}。
     *
     * @param key 要匹配的键
     * @return 小于或等于给定键的最大键，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public K floorKey(K key) {
        return delegate().floorKey(key);
    }

    /**
     * 返回一个键值对映射关系，其键是此映射中大于或等于给定键的最小键；
     * 如果不存在这样的键，则返回 {@code null}。
     *
     * @param key 要匹配的键
     * @return 大于或等于给定键的最小键的关联条目，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public Entry<K, V> ceilingEntry(K key) {
        return delegate().ceilingEntry(key);
    }

    /**
     * 返回此映射中大于或等于给定键的最小键；如果不存在这样的键，则返回 {@code null}。
     *
     * @param key 要匹配的键
     * @return 大于或等于给定键的最小键，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public K ceilingKey(K key) {
        return delegate().ceilingKey(key);
    }

    /**
     * 返回一个键值对映射关系，其键是此映射中严格大于给定键的最小键；
     * 如果不存在这样的键，则返回 {@code null}。
     *
     * @param key 要匹配的键
     * @return 严格大于给定键的最小键的关联条目，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public Entry<K, V> higherEntry(K key) {
        return delegate().higherEntry(key);
    }

    /**
     * 返回此映射中严格大于给定键的最小键；如果不存在这样的键，则返回 {@code null}。
     *
     * @param key 要匹配的键
     * @return 严格大于给定键的最小键，如果不存在这样的键则返回 {@code null}
     */
    @Override
    public K higherKey(K key) {
        return delegate().higherKey(key);
    }

    /**
     * 返回此映射中所包含映射关系的逆序视图。
     * 降序映射由此映射支持，对原映射的更改会反映在降序映射中，反之亦然。
     *
     * @return 此映射的逆序视图
     */
    @Override
    public NavigableMap<K, V> descendingMap() {
        return delegate().descendingMap();
    }

    /**
     * 返回此映射中包含的键的 {@link NavigableSet} 视图。
     * 键集的顺序与映射的顺序相同。对键集的更改会反映在映射中，反之亦然。
     *
     * @return 此映射中键的导航集视图
     */
    @Override
    public NavigableSet<K> navigableKeySet() {
        return delegate().navigableKeySet();
    }

    /**
     * 返回此映射中包含的键的逆序 {@link NavigableSet} 视图。
     * 键集的顺序与映射的逆序相同。对键集的更改会反映在映射中，反之亦然。
     *
     * @return 此映射中键的逆序导航集视图
     */
    @Override
    public NavigableSet<K> descendingKeySet() {
        return delegate().descendingKeySet();
    }

    /**
     * 返回此映射的部分视图，其键的范围从 {@code fromKey} 到 {@code toKey}。
     *
     * @param fromKey       返回映射中键的下限
     * @param fromInclusive 如果为 {@code true}，则在返回的视图中包含下限键
     * @param toKey         返回映射中键的上限
     * @param toInclusive   如果为 {@code true}，则在返回的视图中包含上限键
     * @return 此映射的部分视图，其键的范围从 {@code fromKey} 到 {@code toKey}
     */
    @Override
    public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
    }

    /**
     * 返回此映射的部分视图，其键严格小于（或等于，如果 {@code inclusive} 为 true）{@code toKey}。
     *
     * @param toKey     返回映射中键的上限
     * @param inclusive 如果为 {@code true}，则在返回的视图中包含上限键
     * @return 此映射的部分视图，其键严格小于（或等于）{@code toKey}
     */
    @Override
    public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
        return delegate().headMap(toKey, inclusive);
    }

    /**
     * 返回此映射的部分视图，其键大于等于（或严格大于，如果 {@code inclusive} 为 false）{@code fromKey}。
     *
     * @param fromKey   返回映射中键的下限
     * @param inclusive 如果为 {@code true}，则在返回的视图中包含下限键
     * @return 此映射的部分视图，其键大于等于（或严格大于）{@code fromKey}
     */
    @Override
    public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
        return delegate().tailMap(fromKey, inclusive);
    }

    /**
     * 返回此映射的逆序视图。如果此映射的键顺序是自然顺序，则返回按相反自然顺序排列的映射。
     *
     * @return 此映射的逆序视图
     */
    @Override
    public NavigableMap<K, V> reversed() {
        return delegate().reversed();
    }
}
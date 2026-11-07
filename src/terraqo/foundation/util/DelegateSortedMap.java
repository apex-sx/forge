package io.github.terraqo.foundation.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

/**
 * 一个委托实现的 {@link SortedMap}，将所有操作委托给底层的 {@link SortedMap} 实例。
 * <p>
 * 此类继承自 {@link DelegateSequencedMap}，并实现了 {@link SortedMap} 接口，
 * 提供了对排序映射的完整支持，包括范围视图、首尾键获取以及比较器访问等功能。
 * <p>
 * 支持通过构造函数配置是否不可修改（unmodifiable）以及是否同步（sync）。
 *
 * @param <K> 键的类型
 * @param <V> 值的类型
 * @author Terraqo
 */
public class DelegateSortedMap<K, V> extends DelegateSequencedMap<K, V> implements SortedMap<K, V> {

    /**
     * 构造一个委托 SortedMap。
     *
     * @param delegate       底层委托的 SortedMap 实例
     * @param isUnmodifiable 如果为 true，则将 delegate 包装为不可修改的 Map。
     *                       注意：如果此参数为 true，则不会进行同步包装，因为不可修改映射通常是线程安全的
     *                       （只要底层 map 在发布后不被修改）。
     * @param isSynchronized 如果为 true 且 isUnmodifiable 为 false，则将 delegate 包装为同步的 Map。
     */
    public DelegateSortedMap(SortedMap<K, V> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        super(delegate, isUnmodifiable, isSynchronized);
    }

    /**
     * 将给定的 delegate 包装为不可修改的 SortedMap。
     * <p>
     * 此方法使用 {@link Collections#unmodifiableSortedMap(SortedMap)} 创建原始 map 的不可修改视图。
     * </p>
     *
     * @param delegate 要包装的原始 Map 实例
     * @return 不可修改的 SortedMap 视图
     */
    @Override
    protected SortedMap<K, V> doUnmodifiable(Map<K, V> delegate) {
        return Collections.unmodifiableSortedMap((SortedMap<K, V>) delegate);
    }

    /**
     * 返回强类型的委托 SortedMap 实例。
     * <p>
     * 此方法主要用于子类，以便在不进行额外强制转换的情况下访问具体 delegate 类型的特有方法。
     * </p>
     *
     * @return 强类型的 delegate SortedMap 实例
     */
    @Override
    protected SortedMap<K, V> delegate() {
        return (SortedMap<K, V>) super.delegate();
    }

    /**
     * 返回用于对此映射中的键进行排序的比较器，如果映射使用键的自然顺序则返回 {@code null}。
     *
     * @return 用于对此映射中的键进行排序的比较器，如果映射使用键的自然顺序则返回 {@code null}
     */
    @Override
    public Comparator<? super K> comparator() {
        return delegate().comparator();
    }

    /**
     * 返回此映射的部分视图，其键范围从 {@code fromKey}（包含）到 {@code toKey}（不包含）。
     *
     * @param fromKey 返回的映射中允许的键的最小值（包含）
     * @param toKey   返回的映射中允许的键的最大值（不包含）
     * @return 此映射的部分视图，其键范围从 {@code fromKey}（包含）到 {@code toKey}（不包含）
     */
    @Override
    public SortedMap<K, V> subMap(K fromKey, K toKey) {
        return delegate().subMap(fromKey, toKey);
    }

    /**
     * 返回此映射的部分视图，其键严格小于 {@code toKey}。
     *
     * @param toKey 返回的映射中允许的键的最大值（不包含）
     * @return 此映射的部分视图，其键严格小于 {@code toKey}
     */
    @Override
    public SortedMap<K, V> headMap(K toKey) {
        return delegate().headMap(toKey);
    }

    /**
     * 返回此映射的部分视图，其键大于或等于 {@code fromKey}。
     *
     * @param fromKey 返回的映射中允许的键的最小值（包含）
     * @return 此映射的部分视图，其键大于或等于 {@code fromKey}
     */
    @Override
    public SortedMap<K, V> tailMap(K fromKey) {
        return delegate().tailMap(fromKey);
    }

    /**
     * 返回此映射中当前最小的键。
     *
     * @return 此映射中当前最小的键
     */
    @Override
    public K firstKey() {
        return delegate().firstKey();
    }

    /**
     * 返回此映射中当前最大的键。
     *
     * @return 此映射中当前最大的键
     */
    @Override
    public K lastKey() {
        return delegate().lastKey();
    }

    /**
     * 返回此映射的逆序视图，即键的排序顺序与此映射相反的视图。
     *
     * @return 此映射的逆序视图
     */
    @Override
    public SortedMap<K, V> reversed() {
        return delegate().reversed();
    }
}
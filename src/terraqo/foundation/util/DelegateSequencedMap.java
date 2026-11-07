package io.github.terraqo.foundation.util;

import java.util.*;

/**
 * 一个基于委托模式的 {@link SequencedMap} 实现。
 * <p>
 * 该类继承自 {@link DelegateMap}，并实现了 Java 21 引入的 {@link SequencedMap} 接口，
 * 允许对具有确定 encounter order（相遇顺序）的 Map 进行首尾元素的操作以及反向视图的获取。
 * 所有操作均委托给内部持有的 {@code delegate} 实例。
 *
 * @param <K> 键的类型
 * @param <V> 值的类型
 * @author Terraqo
 */
public class DelegateSequencedMap<K, V> extends DelegateMap<K, V> implements SequencedMap<K, V> {

    /**
     * 构造一个 DelegateSequencedMap 实例。
     *
     * @param delegate       被委托的基础 {@link SequencedMap} 实例，不能为 null
     * @param isUnmodifiable 如果为 true，则将 delegate 包装为不可修改的 Map。
     *                       注意：如果此参数为 true，则不会进行同步包装，
     *                       因为不可修改映射在发布后通常是线程安全的。
     * @param isSynchronized 如果为 true 且 isUnmodifiable 为 false，则将 delegate 包装为同步的 Map。
     */
    public DelegateSequencedMap(SequencedMap<K, V> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        super(delegate, isUnmodifiable, isSynchronized);
    }

    /**
     * 将给定的 delegate 包装为不可修改的 SequencedMap。
     * <p>
     * 此方法使用 {@link Collections#unmodifiableSequencedMap(SequencedMap)} 创建原始 map 的不可修改视图。
     * </p>
     *
     * @param delegate 要包装的原始 Map 实例
     * @return 不可修改的 SequencedMap 视图
     */
    @Override
    protected SequencedMap<K, V> doUnmodifiable(Map<K, V> delegate) {
        return Collections.unmodifiableSequencedMap((SequencedMap<K, V>) delegate);
    }

    /**
     * 将给定的 delegate 包装为线程安全的同步 SequencedMap。
     * <p>
     * 此方法使用自定义的 {@link SynchronizedSequencedMap} 来提供同步访问控制。
     * </p>
     *
     * @param delegate 要包装的原始 Map 实例
     * @return 同步的 SequencedMap 视图
     */
    @Override
    protected SequencedMap<K, V> doSynchronized(Map<K, V> delegate) {
        return new SynchronizedSequencedMap<>((SequencedMap<K, V>) delegate, delegate);
    }

    /**
     * 返回强类型的委托 SequencedMap 实例。
     * <p>
     * 此方法主要用于子类，以便在不进行额外强制转换的情况下访问具体 delegate 类型的特有方法。
     * </p>
     *
     * @return 强类型的 delegate SequencedMap 实例
     */
    @Override
    protected SequencedMap<K, V> delegate() {
        return (SequencedMap<K, V>) super.delegate();
    }

    /**
     * 返回此映射的反向视图。
     * <p>
     * 该视图由底层委托对象支持，因此对反向视图的更改会反映在此映射中，反之亦然。
     *
     * @return 此映射的反向视图
     */
    @Override
    public SequencedMap<K, V> reversed() {
        return delegate().reversed();
    }

    /**
     * 返回此映射中第一个（最老的）键值对的映射关系，如果映射为空则返回 {@code null}。
     *
     * @return 此映射的第一个条目，如果映射为空则返回 {@code null}
     */
    @Override
    public Entry<K, V> firstEntry() {
        return delegate().firstEntry();
    }

    /**
     * 返回此映射中最后一个（最新的）键值对的映射关系，如果映射为空则返回 {@code null}。
     *
     * @return 此映射的最后一个条目，如果映射为空则返回 {@code null}
     */
    @Override
    public Entry<K, V> lastEntry() {
        return delegate().lastEntry();
    }

    /**
     * 移除并返回此映射中第一个（最老的）键值对的映射关系，如果映射为空则返回 {@code null}。
     *
     * @return 移除的此映射的第一个条目，如果映射为空则返回 {@code null}
     */
    @Override
    public Entry<K, V> pollFirstEntry() {
        return delegate().pollFirstEntry();
    }

    /**
     * 移除并返回此映射中最后一个（最新的）键值对的映射关系，如果映射为空则返回 {@code null}。
     *
     * @return 移除的此映射的最后一个条目，如果映射为空则返回 {@code null}
     */
    @Override
    public Entry<K, V> pollLastEntry() {
        return delegate().pollLastEntry();
    }

    /**
     * 将指定的键值对插入到此映射的开头。
     * <p>
     * 如果映射中已存在该键，则替换旧值并返回旧值；否则返回 null。
     *
     * @param k 要关联指定值的键
     * @param v 要与指定键关联的值
     * @return 与指定键关联的前一个值，如果没有映射关系则返回 null
     */
    @Override
    public V putFirst(K k, V v) {
        return delegate().putFirst(k, v);
    }

    /**
     * 将指定的键值对插入到此映射的末尾。
     * <p>
     * 如果映射中已存在该键，则替换旧值并返回旧值；否则返回 null。
     *
     * @param k 要关联指定值的键
     * @param v 要与指定键关联的值
     * @return 与指定键关联的前一个值，如果没有映射关系则返回 null
     */
    @Override
    public V putLast(K k, V v) {
        return delegate().putLast(k, v);
    }

    /**
     * 返回此映射中包含的键的 {@link SequencedSet} 视图。
     * <p>
     * 该集合的迭代器将按照键的相遇顺序（encounter order）返回键。
     * 对该映射的更改会反映在该集合中，反之亦然。
     * </p>
     *
     * @return 此映射中键的有序集合视图
     */
    @Override
    public SequencedSet<K> sequencedKeySet() {
        return delegate().sequencedKeySet();
    }

    /**
     * 返回此映射中包含的值的 {@link SequencedCollection} 视图。
     * <p>
     * 该集合的迭代器将按照对应键的相遇顺序（encounter order）返回值。
     * 对该映射的更改会反映在该集合中，反之亦然。
     * </p>
     *
     * @return 此映射中值的有序集合视图
     */
    @Override
    public SequencedCollection<V> sequencedValues() {
        return delegate().sequencedValues();
    }

    /**
     * 返回此映射中包含的映射关系的 {@link SequencedSet} 视图。
     * <p>
     * 该集合的迭代器将按照映射关系的相遇顺序（encounter order）返回条目。
     * 对该映射的更改会反映在该集合中，反之亦然。
     * </p>
     *
     * @return 此映射中映射关系的有序集合视图
     */
    @Override
    public SequencedSet<Entry<K, V>> sequencedEntrySet() {
        return delegate().sequencedEntrySet();
    }
}
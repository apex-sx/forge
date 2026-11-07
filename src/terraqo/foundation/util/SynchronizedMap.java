/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 一个线程安全的 {@link Map} 包装器。
 * <p>
 * 该类通过委托模式（Delegate Pattern）包装现有的 {@link Map} 实例，
 * 并在每次调用映射的修改或查询方法时，使用指定的锁对象（mutex）进行同步，
 * 以确保在多线程并发环境下的数据一致性和安全性。
 * </p>
 * <p>
 * <strong>注意：</strong>
 * <ul>
 *   <li>如果构造时传入的 {@code mutex} 为 {@code null}，则不进行同步操作，直接委托给底层映射。</li>
 *   <li>键集、值集合和条目集的视图也是线程安全的。</li>
 *   <li>用户必须确保所有对该映射的访问都通过此包装器进行，直接访问底层映射将破坏线程安全性。</li>
 * </ul>
 *
 * @param <K> 映射中键的类型
 * @param <V> 映射中值的类型
 * @author Terraqo
 * @see java.util.Collections#synchronizedMap(Map)
 */
public class SynchronizedMap<K, V> implements Map<K, V> {

    /**
     * 被包装的底层映射委托对象。
     */
    private final Map<K, V> delegate;

    /**
     * 用于同步访问的锁对象。
     * 如果为 {@code null}，则表示不进行同步。
     */
    protected final Object mutex;

    /**
     * 构造一个线程安全的映射包装器。
     *
     * @param delegate 要包装的底层映射，不能为 {@code null}
     * @param mutex    用于同步的锁对象。如果为 {@code null}，则方法调用不同步
     * @throws NullPointerException 如果 {@code delegate} 为 {@code null}
     */
    public SynchronizedMap(Map<K, V> delegate, Object mutex) {
        this.delegate = Objects.requireNonNull(delegate);
        this.mutex = mutex;
    }

    /**
     * 获取底层委托的映射对象。
     *
     * @return 底层映射
     */
    protected Map<K, V> delegate() {
        return delegate;
    }

    /**
     * 比较指定对象与此映射是否相等。
     * <p>
     * 如果提供了锁对象，则在同步块中执行比较操作。
     *
     * @param object 要比较的对象
     * @return 如果指定对象等于此映射，则返回 {@code true}
     */
    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Map<?, ?> other)) {
            return false;
        }
        if (mutex == null) {
            return delegate().equals(other);
        }
        synchronized (mutex) {
            return delegate().equals(other);
        }
    }

    /**
     * 返回此映射的哈希码值。
     * <p>
     * 如果提供了锁对象，则在同步块中计算哈希码。
     *
     * @return 此映射的哈希码值
     */
    @Override
    public final int hashCode() {
        if (mutex == null) {
            return delegate().hashCode();
        }
        synchronized (mutex) {
            return delegate().hashCode();
        }
    }

    /**
     * 返回此映射的字符串表示形式。
     * <p>
     * 如果提供了锁对象，则在同步块中生成字符串。
     *
     * @return 此映射的字符串表示形式
     */
    @Override
    public final String toString() {
        if (mutex == null) {
            return delegate().toString();
        }
        synchronized (mutex) {
            return delegate().toString();
        }
    }

    /**
     * 返回此映射中的键值映射数量。
     *
     * @return 此映射中的键值映射数量
     */
    @Override
    public final int size() {
        if (mutex == null) {
            return delegate().size();
        }
        synchronized (mutex) {
            return delegate().size();
        }
    }

    /**
     * 如果此映射不包含键值映射，则返回 {@code true}。
     *
     * @return 如果此映射不包含键值映射，则返回 {@code true}
     */
    @Override
    public final boolean isEmpty() {
        if (mutex == null) {
            return delegate().isEmpty();
        }
        synchronized (mutex) {
            return delegate().isEmpty();
        }
    }

    /**
     * 如果此映射包含指定键的映射，则返回 {@code true}。
     *
     * @param key 测试其在此映射中是否存在性的键
     * @return 如果此映射包含指定键的映射，则返回 {@code true}
     */
    @Override
    public final boolean containsKey(Object key) {
        if (mutex == null) {
            return delegate().containsKey(key);
        }
        synchronized (mutex) {
            return delegate().containsKey(key);
        }
    }

    /**
     * 如果此映射将一个或多个键映射到指定值，则返回 {@code true}。
     *
     * @param value 测试其在此映射中是否存在性的值
     * @return 如果此映射将一个或多个键映射到指定值，则返回 {@code true}
     */
    @Override
    public final boolean containsValue(Object value) {
        if (mutex == null) {
            return delegate().containsValue(value);
        }
        synchronized (mutex) {
            return delegate().containsValue(value);
        }
    }

    /**
     * 返回指定键所映射的值；如果此映射不包含该键的映射，则返回 {@code null}。
     *
     * @param key 要返回其关联值的键
     * @return 指定键所映射的值；如果此映射不包含该键的映射，则返回 {@code null}
     */
    @Override
    public final V get(Object key) {
        if (mutex == null) {
            return delegate().get(key);
        }
        synchronized (mutex) {
            return delegate().get(key);
        }
    }

    /**
     * 将指定值与此映射中的指定键关联。
     *
     * @param key   与指定值关联的键
     * @param value 与指定键关联的值
     * @return 与 key 关联的先前值；如果 key 没有映射，则返回 {@code null}
     */
    @Override
    public final V put(K key, V value) {
        if (mutex == null) {
            return delegate().put(key, value);
        }
        synchronized (mutex) {
            return delegate().put(key, value);
        }
    }

    /**
     * 如果存在键的映射，则从该映射中移除它。
     *
     * @param key 要从映射中移除其映射的键
     * @return 与 key 关联的先前值；如果 key 没有映射，则返回 {@code null}
     */
    @Override
    public final V remove(Object key) {
        if (mutex == null) {
            return delegate().remove(key);
        }
        synchronized (mutex) {
            return delegate().remove(key);
        }
    }

    /**
     * 将指定映射中的所有映射复制到此映射。
     *
     * @param m 要存储在此映射中的映射
     */
    @Override
    public final void putAll(Map<? extends K, ? extends V> m) {
        if (mutex == null) {
            delegate().putAll(m);
        } else {
            synchronized (mutex) {
                delegate().putAll(m);
            }
        }
    }

    /**
     * 从此映射中移除所有映射。
     */
    @Override
    public final void clear() {
        if (mutex == null) {
            delegate().clear();
        } else {
            synchronized (mutex) {
                delegate().clear();
            }
        }
    }

    /**
     * 返回此映射中包含的键的 {@link Set} 视图。
     * <p>
     * 该集合由映射支持，因此对映射的更改会反映在集合中，反之亦然。
     *
     * @return 此映射中包含的键的 set 视图
     */
    @Override
    public final Set<K> keySet() {
        if (mutex == null) {
            return delegate().keySet();
        }
        synchronized (mutex) {
            return new SynchronizedSet<>(delegate().keySet(), mutex);
        }
    }

    /**
     * 返回此映射中包含的值的 {@link Collection} 视图。
     * <p>
     * 该集合由映射支持，因此对映射的更改会反映在集合中，反之亦然。
     *
     * @return 此映射中包含的值的 collection 视图
     */
    @Override
    public final Collection<V> values() {
        if (mutex == null) {
            return delegate().values();
        }
        synchronized (mutex) {
            return new SynchronizedCollection<>(delegate().values(), mutex);
        }
    }

    /**
     * 返回此映射中包含的映射关系的 {@link Set} 视图。
     * <p>
     * 该集合由映射支持，因此对映射的更改会反映在集合中，反之亦然。
     *
     * @return 此映射中包含的映射关系的 set 视图
     */
    @Override
    public final Set<Entry<K, V>> entrySet() {
        if (mutex == null) {
            return delegate().entrySet();
        }
        synchronized (mutex) {
            return new SynchronizedSet<>(delegate().entrySet(), mutex);
        }
    }

    /**
     * 返回指定键所映射的值；如果此映射不包含该键的映射，则返回默认值。
     *
     * @param key          要返回其关联值的键
     * @param defaultValue 当映射不包含键的映射时要返回的默认值
     * @return 指定键所映射的值；如果此映射不包含该键的映射，则返回默认值
     */
    @Override
    public final V getOrDefault(Object key, V defaultValue) {
        if (mutex == null) {
            return delegate().getOrDefault(key, defaultValue);
        }
        synchronized (mutex) {
            return delegate().getOrDefault(key, defaultValue);
        }
    }

    /**
     * 对此映射中的每个条目执行给定的操作，直到所有条目都被处理或操作抛出异常。
     *
     * @param action 要对每个条目执行的操作
     */
    @Override
    public final void forEach(BiConsumer<? super K, ? super V> action) {
        if (mutex == null) {
            delegate().forEach(action);
        } else {
            synchronized (mutex) {
                delegate().forEach(action);
            }
        }
    }

    /**
     * 将此映射中的每个条目的值替换为对该条目调用给定函数的结果。
     *
     * @param function 要对每个条目应用的函数
     */
    @Override
    public final void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        if (mutex == null) {
            delegate().replaceAll(function);
        } else {
            synchronized (mutex) {
                delegate().replaceAll(function);
            }
        }
    }

    /**
     * 如果指定的键尚未与值关联（或映射到 {@code null}），则将其与给定值关联并返回 {@code null}，否则返回当前值。
     *
     * @param key   与指定值关联的键
     * @param value 与指定键关联的值
     * @return 与指定键关联的先前值；如果没有映射，则返回 {@code null}
     */
    @Override
    public final V putIfAbsent(K key, V value) {
        if (mutex == null) {
            return delegate().putIfAbsent(key, value);
        }
        synchronized (mutex) {
            return delegate().putIfAbsent(key, value);
        }
    }

    /**
     * 仅当指定键当前映射到指定值时才移除该键的条目。
     *
     * @param key   要与指定值关联的键
     * @param value 期望与指定键关联的值
     * @return 如果值被移除，则返回 {@code true}
     */
    @Override
    public final boolean remove(Object key, Object value) {
        if (mutex == null) {
            return delegate().remove(key, value);
        }
        synchronized (mutex) {
            return delegate().remove(key, value);
        }
    }

    /**
     * 仅当当前映射到预期旧值时，才替换指定键的条目。
     *
     * @param key      要与指定值关联的键
     * @param oldValue 期望与指定键关联的值
     * @param newValue 要与指定键关联的值
     * @return 如果值被替换，则返回 {@code true}
     */
    @Override
    public final boolean replace(K key, V oldValue, V newValue) {
        if (mutex == null) {
            return delegate().replace(key, oldValue, newValue);
        }
        synchronized (mutex) {
            return delegate().replace(key, oldValue, newValue);
        }
    }

    /**
     * 仅当指定键当前映射到某个值时，才替换该条目。
     *
     * @param key   要与指定值关联的键
     * @param value 要与指定键关联的值
     * @return 与指定键关联的先前值；如果之前没有映射，则返回 {@code null}
     */
    @Override
    public final V replace(K key, V value) {
        if (mutex == null) {
            return delegate().replace(key, value);
        }
        synchronized (mutex) {
            return delegate().replace(key, value);
        }
    }

    /**
     * 如果指定的键尚未与值关联（或映射到 {@code null}），则尝试使用给定的映射函数计算其值并将其输入到此映射中，除非值为 {@code null}。
     *
     * @param key             要与结果值关联的键
     * @param mappingFunction 用于计算值的函数
     * @return 与指定键关联的当前（现有或计算出的）值；如果计算出的值为 {@code null}，则返回 {@code null}
     */
    @Override
    public final V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        if (mutex == null) {
            return delegate().computeIfAbsent(key, mappingFunction);
        }
        synchronized (mutex) {
            return delegate().computeIfAbsent(key, mappingFunction);
        }
    }

    /**
     * 如果指定键的值存在且非空，则尝试在给定键及其当前映射值的情况下计算新映射。
     *
     * @param key               要与结果值关联的键
     * @param remappingFunction 用于计算值的函数
     * @return 与指定键关联的新值；如果没有新值，则返回 {@code null}
     */
    @Override
    public final V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (mutex == null) {
            return delegate().computeIfPresent(key, remappingFunction);
        }
        synchronized (mutex) {
            return delegate().computeIfPresent(key, remappingFunction);
        }
    }

    /**
     * 尝试在给定指定键及其当前映射值（如果没有当前映射，则为 {@code null}）的情况下计算新映射。
     *
     * @param key               要与结果值关联的键
     * @param remappingFunction 用于计算值的函数
     * @return 与指定键关联的新值；如果没有新值，则返回 {@code null}
     */
    @Override
    public final V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        if (mutex == null) {
            return delegate().compute(key, remappingFunction);
        }
        synchronized (mutex) {
            return delegate().compute(key, remappingFunction);
        }
    }

    /**
     * 如果指定的键尚未与值关联或与 {@code null} 关联，则将其与给定的非空值关联。
     * 否则，将关联值替换为给定重映射函数的结果，如果结果为 {@code null} 则移除。
     *
     * @param key               要与结果值关联的键
     * @param value             要与键关联的非空值，如果不存在映射则使用
     * @param remappingFunction 用于重新计算值的函数（如果存在）
     * @return 与指定键关联的新值；如果没有新值，则返回 {@code null}
     */
    @Override
    public final V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        if (mutex == null) {
            return delegate().merge(key, value, remappingFunction);
        }
        synchronized (mutex) {
            return delegate().merge(key, value, remappingFunction);
        }
    }
}

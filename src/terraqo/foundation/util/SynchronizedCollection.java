/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Collection;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 一个线程安全的 {@link Collection} 包装器。
 * <p>
 * 该类通过委托模式（Delegate Pattern）包装现有的 {@link Collection} 实例，
 * 并在每次调用集合的修改或查询方法时，使用指定的锁对象（mutex）进行同步，
 * 以确保在多线程并发环境下的数据一致性和安全性。
 * </p>
 * <p>
 * <strong>注意：</strong>
 * <ul>
 *   <li>如果构造时传入的 {@code mutex} 为 {@code null}，则不进行同步操作，直接委托给底层集合。</li>
 *   <li>迭代器（Iterator）、Spliterator 和 Stream 也是线程安全的，它们内部同样持有锁引用。</li>
 *   <li>用户必须确保所有对该集合的访问都通过此包装器进行，直接访问底层集合将破坏线程安全性。</li>
 * </ul>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 * @see java.util.Collections#synchronizedCollection(Collection)
 */
public class SynchronizedCollection<E> extends SynchronizedIterable<E> implements Collection<E> {

    /**
     * 构造一个线程安全的集合包装器。
     *
     * @param delegate 要包装的底层集合，不能为 {@code null}
     * @param mutex    用于同步的锁对象。如果为 {@code null}，则方法调用不同步
     * @throws NullPointerException 如果 {@code delegate} 为 {@code null}
     */
    public SynchronizedCollection(Collection<E> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 获取底层委托的集合对象。
     *
     * @return 底层集合
     */
    protected Collection<E> delegate() {
        return (Collection<E>) super.delegate();
    }

    /**
     * 返回此集合中的元素数。
     *
     * @return 此集合中的元素数
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
     * 如果此集合不包含元素，则返回 {@code true}。
     *
     * @return 如果此集合不包含元素，则返回 {@code true}
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
     * 如果此集合包含指定的元素，则返回 {@code true}。
     *
     * @param o 测试其在此集合中是否存在的元素
     * @return 如果此集合包含指定的元素，则返回 {@code true}
     */
    @Override
    public final boolean contains(Object o) {
        if (mutex == null) {
            return delegate().contains(o);
        }
        synchronized (mutex) {
            return delegate().contains(o);
        }
    }

    /**
     * 返回一个包含此集合中所有元素的数组。
     *
     * @return 包含此集合中所有元素的数组
     */
    @Override
    public final Object[] toArray() {
        if (mutex == null) {
            return delegate().toArray();
        }
        synchronized (mutex) {
            return delegate().toArray();
        }
    }

    /**
     * 返回一个包含此集合中所有元素的数组；返回数组的运行时类型是指定数组的运行时类型。
     *
     * @param a   如果足够大，则存储此集合元素的数组；否则，将为相同运行时类型分配一个新数组
     * @param <T> 数组中包含元素的类型
     * @return 包含此集合中所有元素的数组
     * @throws ArrayStoreException  如果指定数组的运行时类型不是此集合中每个元素的运行时类型的超类型
     * @throws NullPointerException 如果指定的数组为 null
     */
    @Override
    public final <T> T[] toArray(T[] a) {
        if (mutex == null) {
            return delegate().toArray(a);
        }
        synchronized (mutex) {
            return delegate().toArray(a);
        }
    }

    /**
     * 使用提供的 {@link IntFunction} 生成器函数分配返回的数组，
     * 返回一个包含此集合中所有元素的数组。
     *
     * @param generator 生成所需类型和提供长度的新数组的函数
     * @param <T>       结果数组元素的类型
     * @return 包含此集合中所有元素的数组
     * @throws NullPointerException 如果指定的生成器为 null
     * @since 11
     */
    @Override
    public final <T> T[] toArray(IntFunction<T[]> generator) {
        if (mutex == null) {
            return delegate().toArray(generator);
        }
        synchronized (mutex) {
            return delegate().toArray(generator);
        }
    }

    /**
     * 将指定的元素追加到此集合的末尾（如果尚未存在）。
     *
     * @param e 要添加到此集合的元素
     * @return 如果此集合因调用而更改，则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 add 操作
     * @throws ClassCastException            如果指定元素的类阻止将其添加到此集合
     * @throws NullPointerException          如果指定元素为 null 且此集合不允许 null 元素
     * @throws IllegalArgumentException      如果指定元素的某些属性阻止将其添加到此集合
     */
    @Override
    public final boolean add(E e) {
        if (mutex == null) {
            return delegate().add(e);
        }
        synchronized (mutex) {
            return delegate().add(e);
        }
    }

    /**
     * 从此集合中移除指定元素的单个实例（如果存在）。
     *
     * @param o 要从此集合中移除的元素（如果存在）
     * @return 如果此集合包含指定的元素（或因此调用而发生更改），则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 remove 操作
     * @throws ClassCastException            如果指定元素的类型与此集合不兼容
     * @throws NullPointerException          如果指定元素为 null 且此集合不允许 null 元素
     */
    @Override
    public final boolean remove(Object o) {
        if (mutex == null) {
            return delegate().remove(o);
        }
        synchronized (mutex) {
            return delegate().remove(o);
        }
    }

    /**
     * 如果此集合包含指定集合中的所有元素，则返回 {@code true}。
     *
     * @param c 要检查在此集合中是否包含的集合
     * @return 如果此集合包含指定集合中的所有元素，则返回 {@code true}
     * @throws NullPointerException 如果指定的集合为 null
     */
    @Override
    public final boolean containsAll(Collection<?> c) {
        if (mutex == null) {
            return delegate().containsAll(c);
        }
        synchronized (mutex) {
            return delegate().containsAll(c);
        }
    }

    /**
     * 将指定集合中的所有元素添加到此集合中。
     *
     * @param c 包含要添加到此集合的元素的集合
     * @return 如果此集合因调用而更改，则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 addAll 操作
     * @throws ClassCastException            如果指定集合中元素的类阻止将其添加到此集合
     * @throws NullPointerException          如果指定的集合包含一个或多个 null 元素且此集合不允许 null 元素，
     *                                       或者指定的集合为 null
     * @throws IllegalArgumentException      如果指定集合中元素的某些属性阻止将其添加到此集合
     */
    @Override
    public final boolean addAll(Collection<? extends E> c) {
        if (mutex == null) {
            return delegate().addAll(c);
        }
        synchronized (mutex) {
            return delegate().addAll(c);
        }
    }

    /**
     * 移除此集合中也包含在指定集合中的所有元素（可选操作）。
     *
     * @param c 包含要从此集合中移除的元素的集合
     * @return 如果此集合因调用而更改，则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 removeAll 方法
     * @throws ClassCastException            如果此集合中元素的类与指定集合不兼容
     * @throws NullPointerException          如果此集合包含一个或多个 null 元素且指定集合不支持 null 元素，
     *                                       或者指定的集合为 null
     */
    @Override
    public final boolean removeAll(Collection<?> c) {
        if (mutex == null) {
            return delegate().removeAll(c);
        }
        synchronized (mutex) {
            return delegate().removeAll(c);
        }
    }

    /**
     * 移除此集合中满足给定谓词的所有元素。
     *
     * @param filter 一个谓词，应用于每个元素以确定是否应将其移除
     * @return 如果任何元素被移除，则返回 {@code true}
     * @throws NullPointerException          如果指定的过滤器为 null
     * @throws UnsupportedOperationException 如果此集合不支持 removeIf 方法
     */
    @Override
    public final boolean removeIf(Predicate<? super E> filter) {
        if (mutex == null) {
            return delegate().removeIf(filter);
        }
        synchronized (mutex) {
            return delegate().removeIf(filter);
        }
    }

    /**
     * 仅保留此集合中包含在指定集合中的元素（可选操作）。
     *
     * @param c 包含要在此集合中保留的元素的集合
     * @return 如果此集合因调用而更改，则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 retainAll 方法
     * @throws ClassCastException            如果此集合中元素的类与指定集合不兼容
     * @throws NullPointerException          如果此集合包含一个或多个 null 元素且指定集合不支持 null 元素，
     *                                       或者指定的集合为 null
     */
    @Override
    public final boolean retainAll(Collection<?> c) {
        if (mutex == null) {
            return delegate().retainAll(c);
        }
        synchronized (mutex) {
            return delegate().retainAll(c);
        }
    }

    /**
     * 从此集合中移除所有元素（可选操作）。
     * 此调用返回后，集合将为空。
     *
     * @throws UnsupportedOperationException 如果此集合不支持 clear 方法
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
     * 返回以此集合为源的顺序 {@link Stream}。
     * <p>
     * 返回的流是线程安全的 {@link SynchronizedStream}。
     *
     * @return 顺序流
     */
    @Override
    public final Stream<E> stream() {
        return new SynchronizedStream<>(delegate().stream(), mutex);
    }

    /**
     * 返回以此集合为源的并行 {@link Stream}。
     * <p>
     * 返回的流是线程安全的 {@link SynchronizedStream}。
     *
     * @return 并行流
     */
    @Override
    public final Stream<E> parallelStream() {
        return new SynchronizedStream<>(delegate().parallelStream(), mutex);
    }
}

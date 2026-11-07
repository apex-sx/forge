/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

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
public class SynchronizedIterable<E> implements Iterable<E> {

    /**
     * 被包装的底层集合委托对象。
     */
    private final Iterable<E> delegate;

    /**
     * 用于同步访问的锁对象。
     * 如果为 {@code null}，则表示不进行同步。
     */
    protected final Object mutex;

    /**
     * 构造一个线程安全的集合包装器。
     *
     * @param delegate 要包装的底层集合，不能为 {@code null}
     * @param mutex    用于同步的锁对象。如果为 {@code null}，则方法调用不同步
     * @throws NullPointerException 如果 {@code delegate} 为 {@code null}
     */
    public SynchronizedIterable(Iterable<E> delegate, Object mutex) {
        this.delegate = Objects.requireNonNull(delegate);
        this.mutex = mutex;
    }

    /**
     * 获取底层委托的集合对象。
     *
     * @return 底层集合
     */
    protected Iterable<E> delegate() {
        return delegate;
    }

    /**
     * 比较指定对象与此集合是否相等。
     * <p>
     * 如果提供了锁对象，则在同步块中执行比较操作。
     *
     * @param object 要比较的对象
     * @return 如果指定对象等于此集合，则返回 {@code true}
     */
    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Iterable<?> other)) {
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
     * 返回此集合的哈希码值。
     * <p>
     * 如果提供了锁对象，则在同步块中计算哈希码。
     *
     * @return 此集合的哈希码值
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
     * 返回在此集合中的元素上进行迭代的迭代器。
     * <p>
     * 返回的迭代器是线程安全的 {@link SynchronizedIterator}。
     *
     * @return 集合元素的迭代器
     */
    @Override
    public final Iterator<E> iterator() {
        if (mutex == null) {
            return delegate().iterator();
        }
        synchronized (mutex) {
            return new SynchronizedIterator<>(delegate().iterator(), mutex);
        }
    }

    /**
     * 在此集合的元素上创建 {@link Spliterator}。
     * <p>
     * 返回的 Spliterator 是线程安全的 {@link SynchronizedSpliterator}。
     *
     * @return 集合元素的 Spliterator
     */
    @Override
    public final Spliterator<E> spliterator() {
        if (mutex == null) {
            return delegate().spliterator();
        }
        synchronized (mutex) {
            return new SynchronizedSpliterator<>(delegate().spliterator(), mutex);
        }
    }

    /**
     * 对集合中的每个元素执行给定的操作，直到处理完所有元素或操作抛出异常。
     * <p>
     * 如果提供了锁对象，整个遍历过程将在同步块中进行。
     *
     * @param action 要对每个元素执行的操作
     * @throws NullPointerException 如果指定的操作为 null
     */
    @Override
    public final void forEach(Consumer<? super E> action) {
        if (mutex == null) {
            delegate().forEach(action);
        } else {
            synchronized (mutex) {
                delegate().forEach(action);
            }
        }
    }

}

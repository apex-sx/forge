/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.NoSuchElementException;
import java.util.SequencedCollection;

/**
 * 一个线程安全的 {@link SequencedCollection} 包装器。
 * <p>
 * 该类继承自 {@link SynchronizedCollection}，并扩展支持 Java 21 引入的
 * {@link SequencedCollection} 接口特性。它通过同步机制确保对底层集合的所有访问都是线程安全的。
 * </p>
 * <p>
 * 主要功能包括：
 * <ul>
 *     <li>获取首尾元素 ({@link #getFirst()}, {@link #getLast()})</li>
 *     <li>在首尾添加元素 ({@link #addFirst(Object)}, {@link #addLast(Object)})</li>
 *     <li>移除首尾元素 ({@link #removeFirst()}, {@link #removeLast()})</li>
 *     <li>获取反向视图 ({@link #reversed()})，返回的视图也是线程安全的</li>
 * </ul>
 * </p>
 * <p>
 * 注意：迭代器需要由用户手动同步，或者使用集合本身作为锁对象进行同步，以保证遍历期间的线程安全。
 * </p>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 */
public class SynchronizedSequencedCollection<E> extends SynchronizedCollection<E> implements SequencedCollection<E> {

    /**
     * 构造一个同步有序集合包装器。
     *
     * @param delegate 要包装的底层有序集合，不能为 null
     * @param mutex    用于同步的锁对象。如果为 null，则不进行同步（通常用于内部已同步的集合或单线程环境）
     * @throws NullPointerException 如果 delegate 为 null
     */
    public SynchronizedSequencedCollection(SequencedCollection<E> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 返回底层的 {@link SequencedCollection} 代理对象。
     * <p>
     * 此方法重写父类方法以提供更具体的返回类型，避免每次调用时都需要强制转换。
     * </p>
     *
     * @return 底层的有序集合实例
     */
    @Override
    protected SequencedCollection<E> delegate() {
        return (SequencedCollection<E>) super.delegate();
    }

    /**
     * 返回此序列的反向视图。
     * <p>
     * 如果提供了锁对象（mutex），则在同步块中创建一个新的同步包装器来包装反向视图，
     * 以确保反向视图的操作也是线程安全的。否则直接返回底层集合的反向视图。
     * </p>
     *
     * @return 此序列的反向视图
     */
    @Override
    public SequencedCollection<E> reversed() {
        if (mutex == null) {
            return delegate().reversed();
        } else {
            synchronized (mutex) {
                return new SynchronizedSequencedCollection<>(delegate().reversed(), mutex);
            }
        }
    }

    /**
     * 将指定元素插入到此序列的开头。
     * <p>
     * 如果提供了锁对象（mutex），则在同步块中执行操作；否则直接调用底层集合的方法。
     * </p>
     *
     * @param e 要添加的元素
     * @throws UnsupportedOperationException 如果底层集合不支持此操作
     * @throws ClassCastException            如果指定元素的类阻止将其添加到此集合
     * @throws NullPointerException          如果指定元素为 null 且底层集合不允许 null 元素
     * @throws IllegalArgumentException      如果指定元素的某些属性阻止将其添加到此集合
     */
    @Override
    public final void addFirst(E e) {
        if (mutex == null) {
            delegate().addFirst(e);
        } else {
            synchronized (mutex) {
                delegate().addFirst(e);
            }
        }
    }

    /**
     * 将指定元素追加到此序列的末尾。
     * <p>
     * 如果提供了锁对象（mutex），则在同步块中执行操作；否则直接调用底层集合的方法。
     * </p>
     *
     * @param e 要添加的元素
     * @throws UnsupportedOperationException 如果底层集合不支持此操作
     * @throws ClassCastException            如果指定元素的类阻止将其添加到此集合
     * @throws NullPointerException          如果指定元素为 null 且底层集合不允许 null 元素
     * @throws IllegalArgumentException      如果指定元素的某些属性阻止将其添加到此集合
     */
    @Override
    public final void addLast(E e) {
        if (mutex == null) {
            delegate().addLast(e);
        } else {
            synchronized (mutex) {
                delegate().addLast(e);
            }
        }
    }

    /**
     * 返回此序列中的第一个元素。
     * <p>
     * 如果提供了锁对象（mutex），则在同步块中执行操作；否则直接调用底层集合的方法。
     * </p>
     *
     * @return 此序列中的第一个元素
     * @throws NoSuchElementException 如果此序列为空
     */
    @Override
    public final E getFirst() {
        if (mutex == null) {
            return delegate().getFirst();
        } else {
            synchronized (mutex) {
                return delegate().getFirst();
            }
        }
    }

    /**
     * 返回此序列中的最后一个元素。
     * <p>
     * 如果提供了锁对象（mutex），则在同步块中执行操作；否则直接调用底层集合的方法。
     * </p>
     *
     * @return 此序列中的最后一个元素
     * @throws NoSuchElementException 如果此序列为空
     */
    @Override
    public final E getLast() {
        if (mutex == null) {
            return delegate().getLast();
        } else {
            synchronized (mutex) {
                return delegate().getLast();
            }
        }
    }

    /**
     * 移除并返回此序列中的第一个元素。
     * <p>
     * 如果提供了锁对象（mutex），则在同步块中执行操作；否则直接调用底层集合的方法。
     * </p>
     *
     * @return 被移除的第一个元素
     * @throws NoSuchElementException        如果此序列为空
     * @throws UnsupportedOperationException 如果底层集合不支持此操作
     */
    @Override
    public final E removeFirst() {
        if (mutex == null) {
            return delegate().removeFirst();
        } else {
            synchronized (mutex) {
                return delegate().removeFirst();
            }
        }
    }

    /**
     * 移除并返回此序列中的最后一个元素。
     * <p>
     * 如果提供了锁对象（mutex），则在同步块中执行操作；否则直接调用底层集合的方法。
     * </p>
     *
     * @return 被移除的最后一个元素
     * @throws NoSuchElementException        如果此序列为空
     * @throws UnsupportedOperationException 如果底层集合不支持此操作
     */
    @Override
    public final E removeLast() {
        if (mutex == null) {
            return delegate().removeLast();
        } else {
            synchronized (mutex) {
                return delegate().removeLast();
            }
        }
    }
}

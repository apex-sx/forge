/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Iterator;
import java.util.NavigableSet;

/**
 * 一个线程安全的 NavigableSet 包装器。
 * <p>
 * 该类继承自 {@link SynchronizedSortedSet}，并扩展支持 {@link NavigableSet} 接口特性，
 * 如导航方法（lower, floor, ceiling, higher）、降序视图、子集操作等。
 * </p>
 * <p>
 * 所有访问底层集合的操作都通过同步块保护，以确保线程安全。
 * 如果构造时提供的 mutex 为 null，则不执行同步操作。
 * </p>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 */
public class SynchronizedNavigableSet<E> extends SynchronizedSortedSet<E> implements NavigableSet<E> {

    /**
     * 构造一个同步可导航集合包装器。
     *
     * @param delegate 要包装的底层可导航集合，不能为 null
     * @param mutex    用于同步的锁对象。如果为 null，则不进行同步
     * @throws NullPointerException 如果 delegate 为 null
     */
    public SynchronizedNavigableSet(NavigableSet<E> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 返回底层委托的可导航集合。
     *
     * @return 底层委托的 NavigableSet 实例
     */
    @Override
    protected NavigableSet<E> delegate() {
        return (NavigableSet<E>) super.delegate();
    }

    /**
     * 返回此集合中元素的逆序视图。
     * <p>
     * 等同于 {@link #descendingSet()}。
     * </p>
     *
     * @return 此集合的逆序视图
     */
    @Override
    public NavigableSet<E> reversed() {
        if (mutex == null) {
            return delegate().reversed();
        }
        synchronized (mutex) {
            return new SynchronizedNavigableSet<>(delegate().reversed(), mutex);
        }
    }

    /**
     * 返回严格小于给定元素的最大元素。
     *
     * @param e 要比较的目标元素
     * @return 严格小于 e 的最大元素，如果不存在则返回 null
     */
    @Override
    public E lower(E e) {
        if (mutex == null) {
            return delegate().lower(e);
        }
        synchronized (mutex) {
            return delegate().lower(e);
        }
    }

    /**
     * 返回小于或等于给定元素的最大元素。
     *
     * @param e 要比较的目标元素
     * @return 小于或等于 e 的最大元素，如果不存在则返回 null
     */
    @Override
    public E floor(E e) {
        if (mutex == null) {
            return delegate().floor(e);
        }
        synchronized (mutex) {
            return delegate().floor(e);
        }
    }

    /**
     * 返回大于或等于给定元素的最小元素。
     *
     * @param e 要比较的目标元素
     * @return 大于或等于 e 的最小元素，如果不存在则返回 null
     */
    @Override
    public E ceiling(E e) {
        if (mutex == null) {
            return delegate().ceiling(e);
        }
        synchronized (mutex) {
            return delegate().ceiling(e);
        }
    }

    /**
     * 返回严格大于给定元素的最小元素。
     *
     * @param e 要比较的目标元素
     * @return 严格大于 e 的最小元素，如果不存在则返回 null
     */
    @Override
    public E higher(E e) {
        if (mutex == null) {
            return delegate().higher(e);
        }
        synchronized (mutex) {
            return delegate().higher(e);
        }
    }

    /**
     * 检索并移除第一个（最低）元素，如果此集合为空则返回 null。
     *
     * @return 第一个元素，如果集合为空则返回 null
     */
    @Override
    public E pollFirst() {
        if (mutex == null) {
            return delegate().pollFirst();
        }
        synchronized (mutex) {
            return delegate().pollFirst();
        }
    }

    /**
     * 检索并移除最后一个（最高）元素，如果此集合为空则返回 null。
     *
     * @return 最后一个元素，如果集合为空则返回 null
     */
    @Override
    public E pollLast() {
        if (mutex == null) {
            return delegate().pollLast();
        }
        synchronized (mutex) {
            return delegate().pollLast();
        }
    }

    /**
     * 返回此集合中元素的逆序视图。
     * <p>
     * 返回的集合由此集合支持，因此对返回集合的更改会反映在此集合中，反之亦然。
     * </p>
     *
     * @return 此集合的逆序视图
     */
    @Override
    public NavigableSet<E> descendingSet() {
        if (mutex == null) {
            return delegate().descendingSet();
        }
        synchronized (mutex) {
            return new SynchronizedNavigableSet<>(delegate().descendingSet(), mutex);
        }
    }

    /**
     * 返回以此集合中元素相反顺序进行迭代的迭代器。
     *
     * @return 逆序迭代器
     */
    @Override
    public Iterator<E> descendingIterator() {
        if (mutex == null) {
            return delegate().descendingIterator();
        }
        synchronized (mutex) {
            return new SynchronizedIterator<>(delegate().descendingIterator(), mutex);
        }
    }

    /**
     * 返回此集合部分元素的视图，其元素范围从 fromElement 到 toElement。
     * <p>
     * 如果 fromInclusive 为 true，则返回视图包含 fromElement；否则不包含。
     * 如果 toInclusive 为 true，则返回视图包含 toElement；否则不包含。
     * </p>
     *
     * @param fromElement   返回视图中最低端点的值
     * @param fromInclusive 如果为 true，则低断点包含在返回的视图中
     * @param toElement     返回视图中最高端点的值
     * @param toInclusive   如果为 true，则高断点包含在返回的视图中
     * @return 指定范围的子集视图
     */
    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        if (mutex == null) {
            return delegate().subSet(fromElement, fromInclusive, toElement, toInclusive);
        }
        synchronized (mutex) {
            return new SynchronizedNavigableSet<>(delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), mutex);
        }
    }

    /**
     * 返回此集合部分元素的视图，其元素严格小于（或等于，如果 inclusive 为 true）toElement。
     *
     * @param toElement 返回视图中最高端点的值
     * @param inclusive 如果为 true，则高断点包含在返回的视图中
     * @return 头部子集视图
     */
    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        if (mutex == null) {
            return delegate().headSet(toElement, inclusive);
        }
        synchronized (mutex) {
            return new SynchronizedNavigableSet<>(delegate().headSet(toElement, inclusive), mutex);
        }
    }

    /**
     * 返回此集合部分元素的视图，其元素大于或等于（或严格大于，如果 inclusive 为 false）fromElement。
     *
     * @param fromElement 返回视图中最低端点的值
     * @param inclusive   如果为 true，则低断点包含在返回的视图中
     * @return 尾部子集视图
     */
    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        if (mutex == null) {
            return delegate().tailSet(fromElement, inclusive);
        }
        synchronized (mutex) {
            return new SynchronizedNavigableSet<>(delegate().tailSet(fromElement, inclusive), mutex);
        }
    }

}

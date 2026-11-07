/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.*;

public class DelegateSortedSet<E> extends DelegateSequencedSet<E> implements SortedSet<E> {
    public DelegateSortedSet(SortedSet<E> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        super(delegate, isUnmodifiable, isSynchronized);
    }

    /**
     * 将给定的 {@code delegate} 包装为不可修改的 {@link Collection}。
     * <p>
     * 此方法使用 {@link Collections#unmodifiableCollection(Collection)} 创建原始集合的不可修改视图。
     * </p>
     *
     * @param delegate 要包装的原始 {@link Collection} 实例
     * @return 不可修改的 {@link Collection} 视图
     */
    @Override
    protected SortedSet<E> doUnmodifiable(Collection<E> delegate) {
        return Collections.unmodifiableSortedSet((SortedSet<E>) delegate);
    }

    @Override
    protected SortedSet<E> doSynchronized(Collection<E> delegate) {
        return new SynchronizedSortedSet<>((SortedSet<E>) delegate, delegate);
    }

    /**
     * 返回底层的委托 {@link Collection} 实例。
     * <p>
     * 此方法主要用于子类，以便在不进行额外强制转换的情况下访问具体 delegate 类型的特有方法。
     * </p>
     *
     * @return 底层的 {@link Collection} 实例
     */
    @Override
    protected SortedSet<E> delegate() {
        return (SortedSet<E>) super.delegate();
    }

    @Override
    public Comparator<? super E> comparator() {
        return delegate().comparator();
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        return delegate().subSet(fromElement, toElement);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        return delegate().headSet(toElement);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        return delegate().tailSet(fromElement);
    }

    @Override
    public E first() {
        return delegate().first();
    }

    @Override
    public E last() {
        return delegate().last();
    }

    @Override
    public Spliterator<E> spliterator() {
        return delegate().spliterator();
    }

    @Override
    public void addFirst(E e) {
        delegate().addFirst(e);
    }

    @Override
    public void addLast(E e) {
        delegate().addLast(e);
    }

    @Override
    public E getFirst() {
        return delegate().getFirst();
    }

    @Override
    public E getLast() {
        return delegate().getLast();
    }

    @Override
    public E removeFirst() {
        return delegate().removeFirst();
    }

    @Override
    public E removeLast() {
        return delegate().removeLast();
    }

    @Override
    public SortedSet<E> reversed() {
        return delegate().reversed();
    }
}
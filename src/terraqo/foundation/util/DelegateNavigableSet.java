/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NavigableSet;

public class DelegateNavigableSet<E> extends DelegateSortedSet<E> implements NavigableSet<E> {
    public DelegateNavigableSet(NavigableSet<E> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        super(delegate, isUnmodifiable, isSynchronized);
    }

    @Override
    protected NavigableSet<E> doUnmodifiable(Collection<E> delegate) {
        return Collections.unmodifiableNavigableSet((NavigableSet<E>) delegate);
    }

    @Override
    protected NavigableSet<E> doSynchronized(Collection<E> delegate) {
        return new SynchronizedNavigableSet<>((NavigableSet<E>) delegate, delegate);
    }

    @Override
    protected NavigableSet<E> delegate() {
        return (NavigableSet<E>) super.delegate();
    }

    // --- NavigableSet 特有方法 ---
    @Override
    public E lower(E e) {
        return delegate().lower(e);
    }

    @Override
    public E floor(E e) {
        return delegate().floor(e);
    }

    @Override
    public E ceiling(E e) {
        return delegate().ceiling(e);
    }

    @Override
    public E higher(E e) {
        return delegate().higher(e);
    }

    @Override
    public E pollFirst() {
        return delegate().pollFirst();
    }

    @Override
    public E pollLast() {
        return delegate().pollLast();
    }

    @Override
    public NavigableSet<E> descendingSet() {
        return delegate().descendingSet();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return delegate().descendingIterator();
    }

    @Override
    public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
        return delegate().subSet(fromElement, fromInclusive, toElement, toInclusive);
    }

    @Override
    public NavigableSet<E> headSet(E toElement, boolean inclusive) {
        return delegate().headSet(toElement, inclusive);
    }

    @Override
    public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
        return delegate().tailSet(fromElement, inclusive);
    }

    @Override
    public NavigableSet<E> reversed() {
        return delegate().reversed();
    }
}
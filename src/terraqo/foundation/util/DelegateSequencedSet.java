/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Collection;
import java.util.Collections;
import java.util.SequencedSet;

/**
 * 一个基于委托模式的 {@link SequencedSet} 实现。
 * <p>
 * 此类将所有 {@link SequencedSet} 操作委托给内部持有的另一个 {@link SequencedSet} 实例（称为 delegate）。
 * 它支持可选的同步控制和不可修改视图包装，适用于需要自定义有序集合行为或增强线程安全性的场景。
 * </p>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 */
public class DelegateSequencedSet<E> extends DelegateSet<E> implements SequencedSet<E> {
    public DelegateSequencedSet(SequencedSet<E> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        super(delegate, isUnmodifiable, isSynchronized);
    }

    /**
     * 将给定的 {@code delegate} 包装为不可修改的 {@link SequencedSet}。
     * <p>
     * 此方法使用 {@link Collections#unmodifiableSequencedSet(SequencedSet)} 创建原始集合的不可修改视图。
     * </p>
     *
     * @param delegate 要包装的原始 {@link Collection} 实例
     * @return 不可修改的 {@link SequencedSet} 视图
     */
    @Override
    protected SequencedSet<E> doUnmodifiable(Collection<E> delegate) {
        return Collections.unmodifiableSequencedSet((SequencedSet<E>) delegate);
    }

    @Override
    protected SequencedSet<E> doSynchronized(Collection<E> delegate) {
        return new SynchronizedSequencedSet<>((SequencedSet<E>) delegate, delegate);
    }

    /**
     * 返回底层的委托 {@link SequencedSet} 实例。
     * <p>
     * 此方法主要用于子类，以便在不进行额外强制转换的情况下访问具体 delegate 类型的特有方法。
     * </p>
     *
     * @return 底层的 {@link SequencedSet} 实例
     */
    @Override
    protected SequencedSet<E> delegate() {
        return (SequencedSet<E>) super.delegate();
    }

    /**
     * 返回此集合的反序视图。
     *
     * @return 此集合的反序视图
     */
    @Override
    public SequencedSet<E> reversed() {
        return delegate().reversed();
    }

    /**
     * 在此序列的开头插入指定元素。
     *
     * @param e 要添加的元素
     */
    @Override
    public void addFirst(E e) {
        delegate().addFirst(e);
    }

    /**
     * 在此序列的末尾追加指定元素。
     *
     * @param e 要添加的元素
     */
    @Override
    public void addLast(E e) {
        delegate().addLast(e);
    }

    /**
     * 检索但不移除此序列的第一个元素。
     *
     * @return 此序列的第一个元素
     */
    @Override
    public E getFirst() {
        return delegate().getFirst();
    }

    /**
     * 检索但不移除此序列的最后一个元素。
     *
     * @return 此序列的最后一个元素
     */
    @Override
    public E getLast() {
        return delegate().getLast();
    }

    /**
     * 检索并移除此序列的第一个元素。
     *
     * @return 此前被移除的第一个元素
     */
    @Override
    public E removeFirst() {
        return delegate().removeFirst();
    }

    /**
     * 检索并移除此序列的最后一个元素。
     *
     * @return 此前被移除的最后一个元素
     */
    @Override
    public E removeLast() {
        return delegate().removeLast();
    }
}
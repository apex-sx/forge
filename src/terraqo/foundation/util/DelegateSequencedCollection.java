/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.SequencedCollection;

/**
 * 一个基于委托模式的 {@link SequencedCollection} 实现。
 * <p>
 * 此类将所有 {@link SequencedCollection} 操作委托给内部持有的另一个 {@link SequencedCollection} 实例（称为 delegate）。
 * 它继承自 {@link DelegateCollection}，并扩展支持了 Java 21 引入的有序集合接口特性，如获取首尾元素、在首尾添加/移除元素以及获取反向视图。
 * </p>
 * <p>
 * 该类支持可选的同步控制（通过 mutex）和不可修改视图包装，适用于需要自定义集合行为、增强线程安全性或提供特定集合语义的场景。
 * </p>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 * @see SequencedCollection
 * @see DelegateCollection
 */
public class DelegateSequencedCollection<E> extends DelegateCollection<E> implements SequencedCollection<E> {
    public DelegateSequencedCollection(Collection<E> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        super(delegate, isUnmodifiable, isSynchronized);
    }

    /**
     * 将给定的 {@code delegate} 包装为不可修改的 {@link SequencedCollection}。
     * <p>
     * 此方法重写父类方法，使用 {@link Collections#unmodifiableSequencedCollection(SequencedCollection)}
     * 创建原始有序集合的不可修改视图，确保返回类型符合 {@link SequencedCollection} 接口契约。
     * </p>
     *
     * @param delegate 要包装的原始 {@link Collection} 实例（预期为 {@link SequencedCollection}）
     * @return 不可修改的 {@link SequencedCollection} 视图
     * @throws ClassCastException 如果 delegate 不是 {@link SequencedCollection} 的实例
     */
    @Override
    protected SequencedCollection<E> doUnmodifiable(Collection<E> delegate) {
        return Collections.unmodifiableSequencedCollection((SequencedCollection<E>) delegate);
    }

    /**
     * 返回底层的委托 {@link SequencedCollection} 实例。
     * <p>
     * 此方法重写父类方法，提供类型安全的访问器，避免在子类中进行额外的强制转换。
     * 主要用于内部方法调用，以便直接访问 {@link SequencedCollection} 特有的方法。
     * </p>
     *
     * @return 底层的 {@link SequencedCollection} 实例
     */
    @Override
    protected SequencedCollection<E> delegate() {
        return (SequencedCollection<E>) super.delegate();
    }

    /**
     * 获取集合中的第一个元素。
     *
     * @return 集合中的第一个元素
     * @throws NoSuchElementException 如果集合为空
     */
    @Override
    public E getFirst() {
        return delegate().getFirst();
    }

    /**
     * 获取集合中的最后一个元素。
     *
     * @return 集合中的最后一个元素
     * @throws NoSuchElementException 如果集合为空
     */
    @Override
    public E getLast() {
        return delegate().getLast();
    }

    /**
     * 在集合的开头插入指定元素。
     *
     * @param e 要插入的元素
     * @throws UnsupportedOperationException 如果此集合不支持 addFirst 操作
     * @throws ClassCastException            如果指定元素的类阻止其添加到此集合
     * @throws NullPointerException          如果指定元素为 null 且此集合不允许 null 元素
     * @throws IllegalArgumentException      如果指定元素的某些属性阻止其添加到此集合
     */
    @Override
    public void addFirst(E e) {
        delegate().addFirst(e);
    }

    /**
     * 在集合的末尾追加指定元素。
     *
     * @param e 要追加的元素
     * @throws UnsupportedOperationException 如果此集合不支持 addLast 操作
     * @throws ClassCastException            如果指定元素的类阻止其添加到此集合
     * @throws NullPointerException          如果指定元素为 null 且此集合不允许 null 元素
     * @throws IllegalArgumentException      如果指定元素的某些属性阻止其添加到此集合
     */
    @Override
    public void addLast(E e) {
        delegate().addLast(e);
    }

    /**
     * 检索并移除集合中的第一个元素。
     *
     * @return 被移除的第一个元素
     * @throws NoSuchElementException        如果集合为空
     * @throws UnsupportedOperationException 如果此集合不支持 removeFirst 操作
     */
    @Override
    public E removeFirst() {
        return delegate().removeFirst();
    }

    /**
     * 检索并移除集合中的最后一个元素。
     *
     * @return 被移除的最后一个元素
     * @throws NoSuchElementException        如果集合为空
     * @throws UnsupportedOperationException 如果此集合不支持 removeLast 操作
     */
    @Override
    public E removeLast() {
        return delegate().removeLast();
    }

    /**
     * 返回此集合的反向视图。
     * <p>
     * 反向视图是此集合的一个视图，其中元素的顺序相反。
     * </p>
     *
     * @return 此集合的反向视图
     */
    @Override
    public SequencedCollection<E> reversed() {
        return delegate().reversed();
    }
}
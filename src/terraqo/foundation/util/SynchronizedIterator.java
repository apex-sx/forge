/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 一个线程安全的 Iterator 包装器。
 * <p>
 * 该类通过委托模式包装现有的 {@link Iterator}，并在每次调用迭代器方法时同步访问，
 * 以确保在多线程环境下的安全性。
 * </p>
 * <p>
 * 如果提供了非空的互斥锁对象（mutex），则所有操作将在该锁的保护下执行；
 * 如果 mutex 为 null，则直接委托给底层迭代器，不进行额外的同步处理。
 * </p>
 *
 * @param <E> 迭代元素的类型
 * @author Terraqo
 */
public class SynchronizedIterator<E> implements Iterator<E> {

    /**
     * 被包装的底层迭代器。
     */
    private final Iterator<E> delegate;

    /**
     * 用于同步控制的互斥锁对象。
     * 如果为 null，则不执行同步操作。
     */
    private final Object mutex;

    /**
     * 构造一个同步迭代器。
     *
     * @param delegate 要包装的底层迭代器，不能为 null
     * @param mutex    用于同步的锁对象。如果为 null，则不进行同步
     * @throws NullPointerException 如果 iterator 为 null
     */
    public SynchronizedIterator(Iterator<E> delegate, Object mutex) {
        this.delegate = Objects.requireNonNull(delegate);
        this.mutex = mutex;
    }

    /**
     * 判断是否还有下一个元素。
     * <p>
     * 如果 mutex 为 null，则直接调用底层迭代器的 hasNext() 方法；
     * 否则在同步块中调用。
     * </p>
     *
     * @return 如果迭代还有更多元素，则返回 true
     */
    @Override
    public boolean hasNext() {
        if (mutex == null) {
            return delegate.hasNext();
        }
        synchronized (mutex) {
            return delegate.hasNext();
        }

    }

    /**
     * 返回迭代中的下一个元素。
     * <p>
     * 如果 mutex 为 null，则直接调用底层迭代器的 next() 方法；
     * 否则在同步块中调用。
     * </p>
     *
     * @return 迭代中的下一个元素
     * @throws java.util.NoSuchElementException 如果迭代没有更多元素
     */
    @Override
    public E next() {
        if (mutex == null) {
            return delegate.next();
        }
        synchronized (mutex) {
            return delegate.next();
        }
    }

    /**
     * 从底层集合中移除由 next() 返回的最后一个元素（可选操作）。
     * <p>
     * 如果 mutex 为 null，则直接调用底层迭代器的 remove() 方法；
     * 否则在同步块中调用。
     * </p>
     *
     * @throws UnsupportedOperationException 如果底层迭代器不支持 remove 操作
     * @throws IllegalStateException         如果 next() 尚未被调用，或者 remove() 已经在上次 next() 调用后被调用
     */
    @Override
    public void remove() {
        if (mutex == null) {
            delegate.remove();
        } else {
            synchronized (mutex) {
                delegate.remove();
            }
        }
    }

    /**
     * 对每个剩余元素执行给定的操作，直到所有元素都被处理或动作抛出异常。
     * <p>
     * 如果 mutex 为 null，则直接调用底层迭代器的 forEachRemaining() 方法；
     * 否则在同步块中调用。
     * </p>
     *
     * @param action 要对每个元素执行的操作
     * @throws NullPointerException 如果 action 为 null
     * @since 1.8
     */
    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        if (mutex == null) {
            delegate.forEachRemaining(action);
        } else {
            synchronized (mutex) {
                delegate.forEachRemaining(action);
            }
        }
    }
}
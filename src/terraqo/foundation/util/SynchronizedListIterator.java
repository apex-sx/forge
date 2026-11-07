/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.ListIterator;
import java.util.function.Consumer;

/**
 * 同步的 ListIterator 包装器。
 * <p>
 * 该类通过委托模式包装一个 {@link ListIterator}，并在所有方法调用上使用互斥锁（mutex）进行同步，
 * 以确保在多线程环境下的线程安全性。如果提供的 mutex 为 null，则不进行同步操作，直接委托给底层迭代器。
 *
 * @param <E> 迭代元素的类型
 * @author Terraqo
 */
public class SynchronizedListIterator<E> implements ListIterator<E> {
    /**
     * 被包装的底層 ListIterator 实例。
     */
    private final ListIterator<E> delegate;

    /**
     * 用于同步的互斥锁对象。如果为 null，则不执行同步。
     */
    private final Object mutex;

    /**
     * 构造一个同步的 ListIterator 包装器。
     *
     * @param delegate 底层的 ListIterator 实例，不能为 null
     * @param mutex    用于同步的互斥锁对象。如果为 null，则方法调用不会同步
     */
    public SynchronizedListIterator(ListIterator<E> delegate, Object mutex) {
        this.delegate = delegate;
        this.mutex = mutex;
    }

    /**
     * 返回被包装的底层 ListIterator 实例。
     *
     * @return 底层的 ListIterator
     */
    protected ListIterator<E> delegate() {
        return delegate;
    }

    /**
     * 判断是否还有下一个元素。
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
     *
     * @return 迭代中的下一个元素
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
     * 判断是否还有前一个元素。
     *
     * @return 如果反向遍历迭代还有更多元素，则返回 true
     */
    @Override
    public boolean hasPrevious() {
        if (mutex == null) {
            return delegate.hasPrevious();
        }
        synchronized (mutex) {
            return delegate.hasPrevious();
        }
    }

    /**
     * 返回迭代中的前一个元素。
     *
     * @return 迭代中的前一个元素
     */
    @Override
    public E previous() {
        if (mutex == null) {
            return delegate.previous();
        }
        synchronized (mutex) {
            return delegate.previous();
        }
    }

    /**
     * 返回对 next 的后续调用将返回的元素的索引。
     *
     * @return 下一个元素的索引，如果当前位于列表末尾，则返回列表大小
     */
    @Override
    public int nextIndex() {
        if (mutex == null) {
            return delegate.nextIndex();
        }
        synchronized (mutex) {
            return delegate.nextIndex();
        }
    }

    /**
     * 返回对 previous 的后续调用将返回的元素的索引。
     *
     * @return 前一个元素的索引，如果当前位于列表开头，则返回 -1
     */
    @Override
    public int previousIndex() {
        if (mutex == null) {
            return delegate.previousIndex();
        }
        synchronized (mutex) {
            return delegate.previousIndex();
        }
    }

    /**
     * 从底层集合中移除由 next 或 previous 返回的最后一个元素。
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
     * 用指定元素替换由 next 或 previous 返回的最后一个元素。
     *
     * @param e 要存储在列表中的元素
     */
    @Override
    public void set(E e) {
        if (mutex == null) {
            delegate.set(e);
        } else {
            synchronized (mutex) {
                delegate.set(e);
            }
        }
    }

    /**
     * 将指定元素插入列表。
     *
     * @param e 要插入的元素
     */
    @Override
    public void add(E e) {
        if (mutex == null) {
            delegate.add(e);
        } else {
            synchronized (mutex) {
                delegate.add(e);
            }
        }
    }

    /**
     * 对每个剩余元素执行给定操作，直到处理完所有元素或操作抛出异常。
     *
     * @param action 要对每个元素执行的操作
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

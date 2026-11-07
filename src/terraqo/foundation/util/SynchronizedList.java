/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.UnaryOperator;

/**
 * 一个线程安全的 List 包装器。
 * <p>
 * 该类继承自 {@link SynchronizedSequencedCollection}，并扩展支持 {@link List} 接口特性，
 * 如按索引访问、搜索、子列表等操作。所有修改和读取操作均通过同步锁保证线程安全。
 * </p>
 *
 * @param <E> 列表中元素的类型
 * @author Terraqo
 */
public class SynchronizedList<E> extends SynchronizedSequencedCollection<E> implements List<E> {

    /**
     * 构造一个同步列表包装器。
     *
     * @param delegate 要包装的底层列表，不能为 null
     * @param mutex    用于同步的锁对象。如果为 null，则不进行同步
     * @throws NullPointerException 如果 delegate 为 null
     */
    public SynchronizedList(List<E> delegate, Object mutex) {
        super(delegate, mutex);
    }

    /**
     * 返回底层的代理列表。
     *
     * @return 底层列表实例
     */
    @Override
    protected List<E> delegate() {
        return (List<E>) super.delegate();
    }

    /**
     * 以相反的顺序返回此列表的视图。
     *
     * @return 此列表的反向视图
     */
    @Override
    public final List<E> reversed() {
        if (mutex == null) {
            return delegate().reversed();
        }
        synchronized (mutex) {
            return delegate().reversed();
        }
    }

    /**
     * 获取指定位置的元素。
     *
     * @param index 元素索引
     * @return 指定位置的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    @Override
    public final E get(int index) {
        if (mutex == null) {
            return delegate().get(index);
        }
        synchronized (mutex) {
            return delegate().get(index);
        }
    }

    /**
     * 替换指定位置的元素。
     *
     * @param index   要替换元素的索引
     * @param element 要存储在该位置的新元素
     * @return 以前在该位置的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    @Override
    public final E set(int index, E element) {
        if (mutex == null) {
            return delegate().set(index, element);
        }
        synchronized (mutex) {
            return delegate().set(index, element);
        }
    }

    /**
     * 在指定位置插入元素。
     *
     * @param index   要插入元素的索引
     * @param element 要插入的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    @Override
    public final void add(int index, E element) {
        if (mutex == null) {
            delegate().add(index, element);
        } else {
            synchronized (mutex) {
                delegate().add(index, element);
            }
        }
    }

    /**
     * 删除指定位置的元素。
     *
     * @param index 要删除元素的索引
     * @return 以前在该位置的元素
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    @Override
    public final E remove(int index) {
        if (mutex == null) {
            return delegate().remove(index);
        }
        synchronized (mutex) {
            return delegate().remove(index);
        }
    }

    /**
     * 返回此列表中第一次出现的指定元素的索引；如果此列表不包含该元素，则返回 -1。
     *
     * @param o 要搜索的元素
     * @return 第一次出现该元素的索引，如果未找到则返回 -1
     */
    @Override
    public final int indexOf(Object o) {
        if (mutex == null) {
            return delegate().indexOf(o);
        }
        synchronized (mutex) {
            return delegate().indexOf(o);
        }
    }

    /**
     * 返回此列表中最后一次出现的指定元素的索引；如果此列表不包含该元素，则返回 -1。
     *
     * @param o 要搜索的元素
     * @return 最后一次出现该元素的索引，如果未找到则返回 -1
     */
    @Override
    public final int lastIndexOf(Object o) {
        if (mutex == null) {
            return delegate().lastIndexOf(o);
        }
        synchronized (mutex) {
            return delegate().lastIndexOf(o);
        }
    }

    /**
     * 返回此列表元素的列表迭代器（按适当顺序）。
     *
     * @return 列表迭代器
     */
    @Override
    public final ListIterator<E> listIterator() {
        if (mutex == null) {
            return delegate().listIterator();
        }
        synchronized (mutex) {
            return new SynchronizedListIterator<>(delegate().listIterator(), mutex);
        }
    }

    /**
     * 从指定位置开始，返回此列表元素的列表迭代器（按适当顺序）。
     *
     * @param index 要从其返回列表迭代器的第一个元素的索引
     * @return 列表迭代器
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    @Override
    public final ListIterator<E> listIterator(int index) {
        if (mutex == null) {
            return delegate().listIterator(index);
        }
        synchronized (mutex) {
            return new SynchronizedListIterator<>(delegate().listIterator(index), mutex);
        }
    }

    /**
     * 返回此列表中指定的 fromIndex（包括）和 toIndex（不包括）之间的部分视图。
     *
     * @param fromIndex subList 的低端点（包括）
     * @param toIndex   subList 的高端点（不包括）
     * @return 指定范围的视图
     * @throws IndexOutOfBoundsException 如果端点值超出范围
     * @throws IllegalArgumentException  如果 fromIndex > toIndex
     */
    @Override
    public final List<E> subList(int fromIndex, int toIndex) {
        if (mutex == null) {
            return delegate().subList(fromIndex, toIndex);
        }
        synchronized (mutex) {
            return new SynchronizedList<>(delegate().subList(fromIndex, toIndex), mutex);
        }
    }

    /**
     * 将指定集合中的所有元素插入到此列表中的指定位置。
     *
     * @param index 要插入指定集合中第一个元素的索引
     * @param c     包含要添加到此列表的元素的集合
     * @return 如果此列表因调用而更改，则返回 true
     * @throws IndexOutOfBoundsException 如果索引超出范围
     */
    @Override
    public final boolean addAll(int index, Collection<? extends E> c) {
        if (mutex == null) {
            return delegate().addAll(index, c);
        }
        synchronized (mutex) {
            return delegate().addAll(index, c);
        }
    }

    /**
     * 将该列表的每个元素替换为对该元素应用运算符的结果。
     *
     * @param operator 应用于每个元素的操作
     */
    @Override
    public final void replaceAll(UnaryOperator<E> operator) {
        if (mutex == null) {
            delegate().replaceAll(operator);
        } else {
            synchronized (mutex) {
                delegate().replaceAll(operator);
            }
        }
    }

    /**
     * 根据指定的比较器引发的顺序对此列表进行排序。
     *
     * @param c 用于比较列表元素的 {@code Comparator}，如果为 {@code null} 则使用自然顺序
     */
    @Override
    public final void sort(Comparator<? super E> c) {
        if (mutex == null) {
            delegate().sort(c);
        } else {
            synchronized (mutex) {
                delegate().sort(c);
            }
        }
    }

}

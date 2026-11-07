/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */
package io.github.terraqo.foundation.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 一个基于委托模式的 {@link Collection} 实现。
 * <p>
 * 此类将所有 {@link Collection} 操作委托给内部持有的另一个 {@link Collection} 实例（称为 delegate）。
 * 它支持可选的同步控制和不可修改视图包装，适用于需要自定义集合行为或增强线程安全性的场景。
 * </p>
 *
 * @param <E> 集合中元素的类型
 * @author Terraqo
 */
public class DelegateCollection<E> implements Collection<E> {
    /**
     * 底层实际存储数据的集合实例。所有操作最终都委托给此对象。
     */
    private final Collection<E> delegate;

    /**
     * 构造一个新的 {@code DelegateCollection}。
     *
     * @param delegate 作为底层存储的实际 {@link Collection} 实例，不能为 null。
     * @throws NullPointerException 如果 {@code delegate} 为 null
     */
    public DelegateCollection(Collection<E> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        Objects.requireNonNull(delegate, "The delegate cannot be null");
        if (isUnmodifiable) {
            // 优先处理不可修改需求，此时同步标志被忽略
            delegate = this.doUnmodifiable(delegate);
        } else {
            if (isSynchronized) {
                delegate = this.doSynchronized(delegate);
            }
        }
        this.delegate = delegate;
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
    protected Collection<E> doUnmodifiable(Collection<E> delegate) {
        return Collections.unmodifiableCollection(delegate);
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
    protected Collection<E> doSynchronized(Collection<E> delegate) {
        return new SynchronizedCollection<>(delegate, delegate);
    }

    /**
     * 返回底层的委托 {@link Collection} 实例。
     * <p>
     * 此方法主要用于子类，以便在不进行额外强制转换的情况下访问具体 delegate 类型的特有方法。
     * </p>
     *
     * @return 底层的 {@link Collection} 实例
     */
    protected Collection<E> delegate() {
        return delegate;
    }

    /**
     * 比较此 {@code DelegateCollection} 与指定对象是否相等。
     * <p>
     * 两个集合相等当且仅当它们包含相同的元素（顺序可能不同，取决于底层集合的实现）。
     * </p>
     *
     * @param object 要比较的对象
     * @return 如果指定对象与此集合相等，则返回 {@code true}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Collection<?> other)) {
            return false;
        }
        return delegate().equals(other);
    }

    /**
     * 返回此 {@code DelegateCollection} 的哈希码。
     * <p>
     * 哈希码基于底层集合中所有元素的哈希码之和计算。
     * </p>
     *
     * @return 此集合的哈希码值
     */
    @Override
    public int hashCode() {
        return delegate().hashCode();
    }

    /**
     * 返回此集合中的元素数量。
     *
     * @return 集合中的元素数量
     */
    @Override
    public int size() {
        return delegate().size();
    }

    /**
     * 判断此集合是否为空。
     *
     * @return 如果此集合不包含任何元素，则返回 {@code true}
     */
    @Override
    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    /**
     * 判断此集合是否包含指定元素。
     *
     * @param o 要检查是否存在于集合中的元素
     * @return 如果此集合包含指定元素，则返回 {@code true}
     */
    @Override
    public boolean contains(Object o) {
        return delegate().contains(o);
    }

    /**
     * 返回一个包含此集合中所有元素的数组。
     *
     * @return 包含此集合所有元素的数组
     */
    @Override
    public Object[] toArray() {
        return delegate().toArray();
    }

    /**
     * 返回一个包含此集合中所有元素的数组；返回数组的运行时类型是指定数组的类型。
     *
     * @param a   用于存储集合元素的数组（如果足够大）；否则，将分配一个具有相同运行时类型的新数组
     * @param <T> 数组组件的类型
     * @return 包含此集合所有元素的数组
     * @throws ArrayStoreException  如果指定数组的运行时类型不是此集合中每个元素的运行时类型的超类型
     * @throws NullPointerException 如果指定数组为 null
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return delegate().toArray(a);
    }

    /**
     * 将指定元素添加到此集合中（如果尚未存在）。
     *
     * @param e 要添加到集合中的元素
     * @return 如果此集合因调用而更改，则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 add 操作
     * @throws ClassCastException            如果指定元素的类阻止将其添加到此集合
     * @throws NullPointerException          如果指定元素为 null 且此集合不允许 null 元素
     * @throws IllegalArgumentException      如果指定元素的某些属性阻止将其添加到此集合
     */
    @Override
    public boolean add(E e) {
        return delegate().add(e);
    }

    /**
     * 从此集合中移除指定元素的单个实例（如果存在）。
     *
     * @param o 要从此集合中移除的元素（如果存在）
     * @return 如果此集合包含指定元素，则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 remove 操作
     * @throws ClassCastException            如果指定元素的类型与此集合不兼容
     * @throws NullPointerException          如果指定元素为 null 且此集合不允许 null 元素
     */
    @Override
    public boolean remove(Object o) {
        return delegate().remove(o);
    }

    /**
     * 判断此集合是否包含指定集合中的所有元素。
     *
     * @param c 要检查是否包含在此集合中的集合
     * @return 如果此集合包含指定集合中的所有元素，则返回 {@code true}
     * @throws NullPointerException 如果指定集合为 null
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return delegate().containsAll(c);
    }

    /**
     * 将指定集合中的所有元素添加到此集合中。
     *
     * @param c 包含要添加到此集合中的元素的集合
     * @return 如果此集合因调用而更改，则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 addAll 操作
     * @throws ClassCastException            如果指定集合中元素的类阻止将其添加到此集合
     * @throws NullPointerException          如果指定集合为 null，或其中包含 null 元素且此集合不允许 null 元素
     * @throws IllegalArgumentException      如果指定集合中元素的某些属性阻止将其添加到此集合
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return delegate().addAll(c);
    }

    /**
     * 移除此集合中也包含在指定集合中的所有元素。
     *
     * @param c 包含要从该集合中移除的元素的集合
     * @return 如果此集合因调用而更改，则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 removeAll 操作
     * @throws ClassCastException            如果此集合中元素的类与指定集合不兼容
     * @throws NullPointerException          如果此集合包含 null 元素且指定集合不允许 null 元素，或者指定集合为 null
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return delegate().removeAll(c);
    }

    /**
     * 仅保留此集合中也包含在指定集合中的元素。
     *
     * @param c 包含要保留在此集合中的元素的集合
     * @return 如果此集合因调用而更改，则返回 {@code true}
     * @throws UnsupportedOperationException 如果此集合不支持 retainAll 操作
     * @throws ClassCastException            如果此集合中元素的类与指定集合不兼容
     * @throws NullPointerException          如果此集合包含 null 元素且指定集合不允许 null 元素，或者指定集合为 null
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return delegate().retainAll(c);
    }

    /**
     * 从此集合中移除所有元素。
     *
     * @throws UnsupportedOperationException 如果此集合不支持 clear 操作
     */
    @Override
    public void clear() {
        delegate().clear();
    }

    /**
     * 返回一个包含此集合中所有元素的数组，使用提供的生成器函数分配返回的数组。
     *
     * @param generator 一个函数，接受一个整数（数组的大小）并生成一个所需类型和大小的数组
     * @param <T>       数组组件的类型
     * @return 包含此集合所有元素的数组
     * @throws NullPointerException 如果生成器为 null
     * @since 11
     */
    @Override
    public <T> T[] toArray(IntFunction<T[]> generator) {
        return delegate().toArray(generator);
    }

    /**
     * 移除此集合中满足给定谓词的所有元素。
     *
     * @param filter 一个谓词，应用于每个元素以确定是否应将其移除
     * @return {@code true} 如果任何元素被移除
     * @throws NullPointerException          如果过滤器为 null
     * @throws UnsupportedOperationException 如果此集合不支持 removeIf 操作
     * @since 1.8
     */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        return delegate().removeIf(filter);
    }

    /**
     * 返回由此集合中的元素组成的顺序 {@link Stream}。
     *
     * @return 一个顺序 {@link Stream}
     * @since 1.8
     */
    @Override
    public Stream<E> stream() {
        return delegate().stream();
    }

    /**
     * 返回由此集合中的元素组成的可能并行 {@link Stream}。
     *
     * @return 一个可能并行的 {@link Stream}
     * @since 1.8
     */
    @Override
    public Stream<E> parallelStream() {
        return delegate().parallelStream();
    }

    /**
     * 返回在此集合中的元素上迭代的迭代器。
     *
     * @return 集合元素的迭代器
     */
    @Override
    public Iterator<E> iterator() {
        return delegate().iterator();
    }

    /**
     * 在此集合中的元素上创建一个 {@link Spliterator}。
     *
     * @return 一个 {@link Spliterator} over the elements in this collection
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return delegate().spliterator();
    }

    /**
     * 对此集合中的每个元素执行给定的操作，直到所有元素都被处理或操作抛出异常。
     *
     * @param action 要为每个元素执行的操作
     * @throws NullPointerException 如果指定的操作为 null
     * @since 1.8
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        delegate().forEach(action);
    }
}
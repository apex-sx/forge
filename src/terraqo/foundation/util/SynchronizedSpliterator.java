/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * 一个线程安全的 {@link Spliterator} 包装器。
 * <p>
 * 此类通过委托模式将所有的 {@link Spliterator} 操作转发到底层的 spliterator 实例。
 * 如果在构造时提供了非空的 {@code mutex} 对象，则所有方法调用都会在同步块中执行，
 * 以确保在多线程环境下的线程安全性。如果 {@code mutex} 为 {@code null}，
 * 则不进行任何同步操作，直接委托给底层 spliterator，适用于单线程场景或底层已保证线程安全的情况。
 * </p>
 * <p>
 * 注意：当调用 {@link #trySplit()} 时，返回的新 Spliterator 也会使用相同的 mutex 进行同步，
 * 从而保证分割后的子迭代器同样具备线程安全性。
 * </p>
 *
 * @param <E> 元素类型
 * @author Terraqo
 * @see Spliterator
 */
public class SynchronizedSpliterator<E> implements Spliterator<E> {

    /**
     * 被包装的底层 Spliterator 实例。
     */
    private final Spliterator<E> spliterator;

    /**
     * 用于同步控制的锁对象。如果为 null，则不执行同步操作。
     */
    private final Object mutex;

    /**
     * 构造一个同步的 Spliterator 包装器。
     *
     * @param spliterator 要包装的底层 Spliterator，不能为 null
     * @param mutex       用于同步的锁对象。如果为 null，则不进行同步
     * @throws NullPointerException 如果 spliterator 为 null
     */
    public SynchronizedSpliterator(Spliterator<E> spliterator, Object mutex) {
        this.spliterator = Objects.requireNonNull(spliterator);
        this.mutex = mutex;
    }

    /**
     * 如果存在下一个元素，则对其执行给定的操作。
     * <p>
     * 如果提供了 mutex，则在调用底层 spliterator 之前获取锁。
     *
     * @param action 对下一个元素执行的操作
     * @return 如果存在下一个元素并执行了操作，则返回 true；否则返回 false
     * @throws NullPointerException 如果 action 为 null
     */
    @Override
    public boolean tryAdvance(Consumer<? super E> action) {
        if (mutex == null) {
            return spliterator.tryAdvance(action);
        }
        synchronized (mutex) {
            return spliterator.tryAdvance(action);
        }
    }

    /**
     * 对剩余的所有元素依次执行给定的操作，直到所有元素都被处理或操作抛出异常。
     * <p>
     * 如果提供了 mutex，则在调用底层 spliterator 之前获取锁。
     *
     * @param action 对剩余元素执行的操作
     * @throws NullPointerException 如果 action 为 null
     */
    @Override
    public void forEachRemaining(Consumer<? super E> action) {
        if (mutex == null) {
            spliterator.forEachRemaining(action);
            return;
        }
        synchronized (mutex) {
            spliterator.forEachRemaining(action);
        }
    }

    /**
     * 将此 Spliterator 分割成两部分。
     * <p>
     * 如果提供了 mutex，则在调用底层 spliterator 之前获取锁。
     * 如果分割成功，返回的新 Spliterator 也将使用相同的 mutex 进行同步，
     * 以确保线程安全性。
     *
     * @return 一个新的 {@code SynchronizedSpliterator}，覆盖此对象未覆盖的部分元素；
     * 如果无法分割，则返回 {@code null}
     */
    @Override
    public Spliterator<E> trySplit() {
        if (mutex == null) {
            return spliterator.trySplit();
        }
        synchronized (mutex) {
            Spliterator<E> split = spliterator.trySplit();
            // 确保分割出的新 Spliterator 也具备相同的同步机制
            return split == null ? null : new SynchronizedSpliterator<>(split, mutex);
        }
    }

    /**
     * 估计剩余元素的数量。
     * <p>
     * 如果提供了 mutex，则在调用底层 spliterator 之前获取锁。
     *
     * @return 剩余元素的估计数量，如果无限、未知或计算代价过高，则返回 {@link Long#MAX_VALUE}
     */
    @Override
    public long estimateSize() {
        if (mutex == null) {
            return spliterator.estimateSize();
        }
        synchronized (mutex) {
            return spliterator.estimateSize();
        }
    }

    /**
     * 如果已知确切大小，则返回剩余元素的精确数量；否则返回 -1。
     * <p>
     * 如果提供了 mutex，则在调用底层 spliterator 之前获取锁。
     *
     * @return 剩余元素的精确数量，如果未知则返回 -1
     */
    @Override
    public long getExactSizeIfKnown() {
        if (mutex == null) {
            return spliterator.getExactSizeIfKnown();
        }
        synchronized (mutex) {
            return spliterator.getExactSizeIfKnown();
        }
    }

    /**
     * 返回此 Spliterator 的特征值集合。
     * <p>
     * 如果提供了 mutex，则在调用底层 spliterator 之前获取锁。
     *
     * @return 特征值的位掩码，包含 {@link #ORDERED}, {@link #DISTINCT}, {@link #SORTED},
     * {@link #SIZED}, {@link #NONNULL}, {@link #IMMUTABLE}, {@link #CONCURRENT},
     * {@link #SUBSIZED} 等
     */
    @Override
    public int characteristics() {
        if (mutex == null) {
            return spliterator.characteristics();
        }
        synchronized (mutex) {
            return spliterator.characteristics();
        }
    }

    /**
     * 测试此 Spliterator 是否具有给定的特征值。
     * <p>
     * 如果提供了 mutex，则在调用底层 spliterator 之前获取锁。
     *
     * @param characteristics 要测试的特征值
     * @return 如果此 Spliterator 具有所有给定的特征值，则返回 true
     */
    @Override
    public boolean hasCharacteristics(int characteristics) {
        if (mutex == null) {
            return spliterator.hasCharacteristics(characteristics);
        }
        synchronized (mutex) {
            return spliterator.hasCharacteristics(characteristics);
        }
    }

    /**
     * 如果此 Spliterator 的来源是 {@link #SORTED} 的，则返回用于排序的 {@link Comparator}；
     * 否则抛出 IllegalStateException。
     * <p>
     * 如果提供了 mutex，则在调用底层 spliterator 之前获取锁。
     *
     * @return 元素排序的比较器
     * @throws IllegalStateException 如果此 Spliterator 不是 SORTED 的
     */
    @Override
    public Comparator<? super E> getComparator() {
        if (mutex == null) {
            return spliterator.getComparator();
        }
        synchronized (mutex) {
            return spliterator.getComparator();
        }
    }
}

/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 一个线程安全的 {@link IntStream} 包装器。
 * <p>
 * 此类通过委托模式将所有的 {@link IntStream} 操作转发到底层的 stream 实例。
 * 如果在构造时提供了非空的 {@code mutex} 对象，则所有方法调用都会在同步块中执行，
 * 以确保在多线程环境下的线程安全性。如果 {@code mutex} 为 {@code null}，
 * 则不进行任何同步操作，直接委托给底层 stream。
 * </p>
 * <p>
 * 注意：所有返回新 Stream 的中间操作都会使用相同的 mutex 进行同步包装，
 * 从而保证链式调用的线程安全性。
 * </p>
 *
 * @author Terraqo
 */
public class SynchronizedIntStream implements IntStream {

    /**
     * 被包装的底层 IntStream 实例。
     */
    private final IntStream delegate;

    /**
     * 用于同步控制的锁对象。如果为 null，则不执行同步操作。
     */
    private final Object mutex;

    /**
     * 构造一个同步的 IntStream 包装器。
     *
     * @param delegate 要包装的底层 IntStream，不能为 null
     * @param mutex  用于同步的锁对象。如果为 null，则不进行同步
     * @throws NullPointerException 如果 stream 为 null
     */
    public SynchronizedIntStream(IntStream delegate, Object mutex) {
        this.delegate = Objects.requireNonNull(delegate);
        this.mutex = mutex;
    }

    /**
     * 返回由匹配给定谓词的元素组成的流。
     */
    @Override
    public IntStream filter(IntPredicate predicate) {
        if (mutex == null) {
            return delegate.filter(predicate);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.filter(predicate), mutex);
        }
    }

    /**
     * 返回由将给定函数应用于元素的结果组成的流。
     */
    @Override
    public IntStream map(IntUnaryOperator mapper) {
        if (mutex == null) {
            return delegate.map(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.map(mapper), mutex);
        }
    }

    /**
     * 返回由将给定函数应用于元素的结果组成的对象流。
     */
    @Override
    public <U> Stream<U> mapToObj(IntFunction<? extends U> mapper) {
        if (mutex == null) {
            return delegate.mapToObj(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(delegate.mapToObj(mapper), mutex);
        }
    }

    /**
     * 返回由将给定函数应用于元素的结果组成的 LongStream。
     */
    @Override
    public LongStream mapToLong(IntToLongFunction mapper) {
        if (mutex == null) {
            return delegate.mapToLong(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedLongStream(delegate.mapToLong(mapper), mutex);
        }
    }

    /**
     * 返回由将给定函数应用于元素的结果组成的 DoubleStream。
     */
    @Override
    public DoubleStream mapToDouble(IntToDoubleFunction mapper) {
        if (mutex == null) {
            return delegate.mapToDouble(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedDoubleStream(delegate.mapToDouble(mapper), mutex);
        }
    }

    /**
     * 返回由将给定函数应用于每个元素并连接结果而获得的流。
     */
    @Override
    public IntStream flatMap(IntFunction<? extends IntStream> mapper) {
        if (mutex == null) {
            return delegate.flatMap(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.flatMap(mapper), mutex);
        }
    }

    /**
     * 返回由将给定多对多映射函数应用于每个元素并连接结果而获得的流。
     */
    @Override
    public IntStream mapMulti(IntMapMultiConsumer mapper) {
        if (mutex == null) {
            return delegate.mapMulti(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.mapMulti(mapper), mutex);
        }
    }

    /**
     * 返回由不同元素组成的流。
     */
    @Override
    public IntStream distinct() {
        if (mutex == null) {
            return delegate.distinct();
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.distinct(), mutex);
        }
    }

    /**
     * 返回由自然顺序排序的元素组成的流。
     */
    @Override
    public IntStream sorted() {
        if (mutex == null) {
            return delegate.sorted();
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.sorted(), mutex);
        }
    }

    /**
     * 返回在每次迭代时 peek 给定操作的流。
     */
    @Override
    public IntStream peek(IntConsumer action) {
        if (mutex == null) {
            return delegate.peek(action);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.peek(action), mutex);
        }
    }

    /**
     * 返回由此流的元素组成的流，截断长度不超过 maxSize。
     */
    @Override
    public IntStream limit(long maxSize) {
        if (mutex == null) {
            return delegate.limit(maxSize);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.limit(maxSize), mutex);
        }
    }

    /**
     * 返回在丢弃前 n 个元素后由此流的剩余元素组成的流。
     */
    @Override
    public IntStream skip(long n) {
        if (mutex == null) {
            return delegate.skip(n);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.skip(n), mutex);
        }
    }

    /**
     * 返回包含匹配给定谓词的前缀元素的流。
     */
    @Override
    public IntStream takeWhile(IntPredicate predicate) {
        if (mutex == null) {
            return delegate.takeWhile(predicate);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.takeWhile(predicate), mutex);
        }
    }

    /**
     * 返回在丢弃匹配给定谓词的前缀元素后由此流的剩余元素组成的流。
     */
    @Override
    public IntStream dropWhile(IntPredicate predicate) {
        if (mutex == null) {
            return delegate.dropWhile(predicate);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.dropWhile(predicate), mutex);
        }
    }

    /**
     * 对此流的每个元素执行给定操作。
     */
    @Override
    public void forEach(IntConsumer action) {
        if (mutex == null) {
            delegate.forEach(action);
        } else {
            synchronized (mutex) {
                delegate.forEach(action);
            }
        }
    }

    /**
     * 按遇到顺序对此流的每个元素执行给定操作。
     */
    @Override
    public void forEachOrdered(IntConsumer action) {
        if (mutex == null) {
            delegate.forEachOrdered(action);
        } else {
            synchronized (mutex) {
                delegate.forEachOrdered(action);
            }
        }
    }

    /**
     * 返回包含此流元素的数组。
     */
    @Override
    public int[] toArray() {
        if (mutex == null) {
            return delegate.toArray();
        }
        synchronized (mutex) {
            return delegate.toArray();
        }
    }

    /**
     * 对此流的元素执行归约操作。
     */
    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        if (mutex == null) {
            return delegate.reduce(identity, op);
        }
        synchronized (mutex) {
            return delegate.reduce(identity, op);
        }
    }

    /**
     * 对此流的元素执行归约操作。
     */
    @Override
    public OptionalInt reduce(IntBinaryOperator op) {
        if (mutex == null) {
            return delegate.reduce(op);
        }
        synchronized (mutex) {
            return delegate.reduce(op);
        }
    }

    /**
     * 对此流的元素执行可变归约操作。
     */
    @Override
    public <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> accumulator, BiConsumer<R, R> combiner) {
        if (mutex == null) {
            return delegate.collect(supplier, accumulator, combiner);
        }
        synchronized (mutex) {
            return delegate.collect(supplier, accumulator, combiner);
        }
    }

    /**
     * 返回此流中元素的总和。
     */
    @Override
    public int sum() {
        if (mutex == null) {
            return delegate.sum();
        }
        synchronized (mutex) {
            return delegate.sum();
        }
    }

    /**
     * 返回此流的最小元素（根据自然顺序）。
     */
    @Override
    public OptionalInt min() {
        if (mutex == null) {
            return delegate.min();
        }
        synchronized (mutex) {
            return delegate.min();
        }
    }

    /**
     * 返回此流的最大元素（根据自然顺序）。
     */
    @Override
    public OptionalInt max() {
        if (mutex == null) {
            return delegate.max();
        }
        synchronized (mutex) {
            return delegate.max();
        }
    }

    /**
     * 返回此流中的元素数量。
     */
    @Override
    public long count() {
        if (mutex == null) {
            return delegate.count();
        }
        synchronized (mutex) {
            return delegate.count();
        }
    }

    /**
     * 返回此流中元素的算术平均值。
     */
    @Override
    public OptionalDouble average() {
        if (mutex == null) {
            return delegate.average();
        }
        synchronized (mutex) {
            return delegate.average();
        }
    }

    /**
     * 返回此流元素的汇总统计信息。
     */
    @Override
    public IntSummaryStatistics summaryStatistics() {
        if (mutex == null) {
            return delegate.summaryStatistics();
        }
        synchronized (mutex) {
            return delegate.summaryStatistics();
        }
    }

    /**
     * 返回此流的任何元素是否匹配给定谓词。
     */
    @Override
    public boolean anyMatch(IntPredicate predicate) {
        if (mutex == null) {
            return delegate.anyMatch(predicate);
        }
        synchronized (mutex) {
            return delegate.anyMatch(predicate);
        }
    }

    /**
     * 返回此流的所有元素是否匹配给定谓词。
     */
    @Override
    public boolean allMatch(IntPredicate predicate) {
        if (mutex == null) {
            return delegate.allMatch(predicate);
        }
        synchronized (mutex) {
            return delegate.allMatch(predicate);
        }
    }

    /**
     * 返回此流的元素是否都不匹配给定谓词。
     */
    @Override
    public boolean noneMatch(IntPredicate predicate) {
        if (mutex == null) {
            return delegate.noneMatch(predicate);
        }
        synchronized (mutex) {
            return delegate.noneMatch(predicate);
        }
    }

    /**
     * 返回此流的第一个元素（如果遇到顺序有定义）。
     */
    @Override
    public OptionalInt findFirst() {
        if (mutex == null) {
            return delegate.findFirst();
        }
        synchronized (mutex) {
            return delegate.findFirst();
        }
    }

    /**
     * 返回此流的任何元素。
     */
    @Override
    public OptionalInt findAny() {
        if (mutex == null) {
            return delegate.findAny();
        }
        synchronized (mutex) {
            return delegate.findAny();
        }
    }

    /**
     * 返回等效于此流的 LongStream。
     */
    @Override
    public LongStream asLongStream() {
        if (mutex == null) {
            return delegate.asLongStream();
        }
        synchronized (mutex) {
            return new SynchronizedLongStream(delegate.asLongStream(), mutex);
        }
    }

    /**
     * 返回等效于此流的 DoubleStream。
     */
    @Override
    public DoubleStream asDoubleStream() {
        if (mutex == null) {
            return delegate.asDoubleStream();
        }
        synchronized (mutex) {
            return new SynchronizedDoubleStream(delegate.asDoubleStream(), mutex);
        }
    }

    /**
     * 返回由装箱后的元素组成的 Stream<Integer>。
     */
    @Override
    public Stream<Integer> boxed() {
        if (mutex == null) {
            return delegate.boxed();
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(delegate.boxed(), mutex);
        }
    }

    /**
     * 返回顺序流。
     */
    @Override
    public IntStream sequential() {
        if (mutex == null) {
            return delegate.sequential();
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.sequential(), mutex);
        }
    }

    /**
     * 返回并行流。
     */
    @Override
    public IntStream parallel() {
        if (mutex == null) {
            return delegate.parallel();
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.parallel(), mutex);
        }
    }

    /**
     * 返回无序流。
     */
    @Override
    public IntStream unordered() {
        if (mutex == null) {
            return delegate.unordered();
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.unordered(), mutex);
        }
    }

    /**
     * 返回当流关闭时执行给定操作的流。
     */
    @Override
    public IntStream onClose(Runnable closeHandler) {
        if (mutex == null) {
            return delegate.onClose(closeHandler);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(delegate.onClose(closeHandler), mutex);
        }
    }

    /**
     * 关闭此流。
     */
    @Override
    public void close() {
        if (mutex == null) {
            delegate.close();
        } else {
            synchronized (mutex) {
                delegate.close();
            }
        }
    }

    /**
     * 返回此流是否为并行流。
     */
    @Override
    public boolean isParallel() {
        if (mutex == null) {
            return delegate.isParallel();
        }
        synchronized (mutex) {
            return delegate.isParallel();
        }
    }

    /**
     * 返回此流的迭代器。
     */
    @Override
    public PrimitiveIterator.OfInt iterator() {
        if (mutex == null) {
            return delegate.iterator();
        }
        synchronized (mutex) {
            return delegate.iterator();
        }
    }

    /**
     * 返回此流的 spliterator。
     */
    @Override
    public Spliterator.OfInt spliterator() {
        if (mutex == null) {
            return delegate.spliterator();
        }
        synchronized (mutex) {
            return delegate.spliterator();
        }
    }
}

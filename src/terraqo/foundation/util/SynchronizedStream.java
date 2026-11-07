/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;


/**
 * 线程安全的 Stream 实现。
 * <p>
 * 该类通过委托模式包装一个标准的 {@link Stream}，并在每次方法调用时根据提供的锁对象（mutex）进行同步控制。
 * 如果构造函数中传入的 mutex 为 null，则所有操作将直接委托给底层 Stream，不进行任何同步开销，适用于单线程环境或外部已保证线程安全的场景。
 * </p>
 * <p>
 * <strong>中间操作（Intermediate Operations）：</strong>
 * 对于返回新 Stream 的中间操作（如 filter, map, sorted 等），在获取锁后执行底层操作，并将结果包装在新的 {@code SynchronizedStream} 实例中返回。
 * 新生成的 Stream 实例共享同一个 mutex 锁，从而确保整个流处理链路的线程安全性。
 * 对于转换为基本类型流的操作（如 mapToInt），会返回对应的同步包装类（如 {@link SynchronizedIntStream}）。
 * </p>
 * <p>
 * <strong>终端操作（Terminal Operations）：</strong>
 * 对于触发计算的终端操作（如 forEach, collect, reduce 等），整个计算过程将在锁的保护下原子性地执行，防止与其他线程对流状态的并发访问冲突。
 * </p>
 * <p>
 * <strong>迭代器与 Spliterator：</strong>
 * {@link #iterator()} 和 {@link #spliterator()} 方法返回经过同步包装的迭代器或分割迭代器，确保在遍历过程中的线程安全。
 * </p>
 *
 * @param <E> 流中元素的类型
 * @author Terraqo
 * @see SynchronizedIntStream
 * @see SynchronizedLongStream
 * @see SynchronizedDoubleStream
 */
public class SynchronizedStream<E> implements Stream<E> {
    /**
     * 被包装的底层 Stream 实例。
     */
    private final Stream<E> stream;

    /**
     * 用于同步控制的锁对象。如果为 null，则不执行同步操作。
     */
    private final Object mutex;

    /**
     * 构造一个同步 Stream 包装器。
     *
     * @param stream 要包装的底层 Stream，不能为 null
     * @param mutex  同步锁对象。如果为 null，则此类退化为非同步的代理模式，直接委托所有调用。
     */
    public SynchronizedStream(Stream<E> stream, Object mutex) {
        this.stream = stream;
        this.mutex = mutex;
    }

    /**
     * 返回由与此给定谓词匹配的此流的元素组成的流。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行过滤操作，并返回一个新的同步 Stream。
     *
     * @param predicate 用于测试元素的非干扰、无状态谓词
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> filter(Predicate<? super E> predicate) {
        if (mutex == null) {
            return stream.filter(predicate);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.filter(predicate), mutex);
        }
    }

    /**
     * 返回由将此流的每个元素映射到结果而产生的流。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行映射操作，并返回一个新的同步 Stream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数
     * @param <R>    新流中元素的类型
     * @return 新的同步 Stream
     */
    @Override
    public <R> Stream<R> map(Function<? super E, ? extends R> mapper) {
        if (mutex == null) {
            return stream.map(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.map(mapper), mutex);
        }
    }

    /**
     * 返回一个 {@link IntStream}，其中包含将此流的每个元素映射到 {@code int} 的结果。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 IntStream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数
     * @return 新的同步 IntStream
     */
    @Override
    public IntStream mapToInt(ToIntFunction<? super E> mapper) {
        if (mutex == null) {
            return stream.mapToInt(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(stream.mapToInt(mapper), mutex);
        }
    }

    /**
     * 返回一个 {@link LongStream}，其中包含将此流的每个元素映射到 {@code long} 的结果。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 LongStream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数
     * @return 新的同步 LongStream
     */
    @Override
    public LongStream mapToLong(ToLongFunction<? super E> mapper) {
        if (mutex == null) {
            return stream.mapToLong(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedLongStream(stream.mapToLong(mapper), mutex);
        }
    }

    /**
     * 返回一个 {@link DoubleStream}，其中包含将此流的每个元素映射到 {@code double} 的结果。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 DoubleStream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数
     * @return 新的同步 DoubleStream
     */
    @Override
    public DoubleStream mapToDouble(ToDoubleFunction<? super E> mapper) {
        if (mutex == null) {
            return stream.mapToDouble(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedDoubleStream(stream.mapToDouble(mapper), mutex);
        }
    }

    /**
     * 返回由将此流的每个元素替换为通过将提供的映射函数应用于每个元素而产生的映射流的内容而创建的流。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数，该函数产生一个新流
     * @param <R>    新流中元素的类型
     * @return 新的同步 Stream
     */
    @Override
    public <R> Stream<R> flatMap(Function<? super E, ? extends Stream<? extends R>> mapper) {
        if (mutex == null) {
            return stream.flatMap(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.flatMap(mapper), mutex);
        }
    }

    /**
     * 返回一个 {@link IntStream}，其中包含将此流的每个元素替换为通过将提供的映射函数应用于每个元素而产生的映射流的内容而创建的值。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 IntStream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数，该函数产生一个新流
     * @return 新的同步 IntStream
     */
    @Override
    public IntStream flatMapToInt(Function<? super E, ? extends IntStream> mapper) {
        if (mutex == null) {
            return stream.flatMapToInt(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(stream.flatMapToInt(mapper), mutex);
        }
    }

    /**
     * 返回一个 {@link LongStream}，其中包含将此流的每个元素替换为通过将提供的映射函数应用于每个元素而产生的映射流的内容而创建的值。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 LongStream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数，该函数产生一个新流
     * @return 新的同步 LongStream
     */
    @Override
    public LongStream flatMapToLong(Function<? super E, ? extends LongStream> mapper) {
        if (mutex == null) {
            return stream.flatMapToLong(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedLongStream(stream.flatMapToLong(mapper), mutex);
        }
    }

    /**
     * 返回一个 {@link DoubleStream}，其中包含将此流的每个元素替换为通过将提供的映射函数应用于每个元素而产生的映射流的内容而创建的值。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 DoubleStream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数，该函数产生一个新流
     * @return 新的同步 DoubleStream
     */
    @Override
    public DoubleStream flatMapToDouble(Function<? super E, ? extends DoubleStream> mapper) {
        if (mutex == null) {
            return stream.flatMapToDouble(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedDoubleStream(stream.flatMapToDouble(mapper), mutex);
        }
    }

    /**
     * 返回由该流的不同元素（根据 {@link Object#equals(Object)}）组成的流。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> distinct() {
        if (mutex == null) {
            return stream.distinct();
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.distinct(), mutex);
        }
    }

    /**
     * 返回由此流的元素组成的流，并根据自然顺序排序。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> sorted() {
        if (mutex == null) {
            return stream.sorted();
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.sorted(), mutex);
        }
    }

    /**
     * 返回由此流的元素组成的流，并根据提供的 {@link Comparator} 排序。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @param comparator 用于比较流元素的非干扰、无状态 {@link Comparator}
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> sorted(Comparator<? super E> comparator) {
        if (mutex == null) {
            return stream.sorted(comparator);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.sorted(comparator), mutex);
        }
    }

    /**
     * 返回由该流的元素组成的流，另外在从生成的流中使用元素时对每个元素执行提供的操作。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @param action 对元素执行的非干扰、无状态操作
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> peek(Consumer<? super E> action) {
        if (mutex == null) {
            return stream.peek(action);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.peek(action), mutex);
        }
    }

    /**
     * 返回由该流的截断版本组成的流，其长度不超过 {@code maxSize}。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @param maxSize 流应限制的元素数量
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> limit(long maxSize) {
        if (mutex == null) {
            return stream.limit(maxSize);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.limit(maxSize), mutex);
        }
    }

    /**
     * 在丢弃流的前 {@code n} 个元素后，返回由该流的剩余元素组成的流。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @param n 要跳过的元素数量
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> skip(long n) {
        if (mutex == null) {
            return stream.skip(n);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.skip(n), mutex);
        }
    }

    /**
     * 对此流的每个元素执行操作。
     * <p>
     * 如果 mutex 不为 null，则整个遍历过程在同步块中执行。
     *
     * @param action 对元素执行的非干扰、无状态操作
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        if (mutex == null) {
            stream.forEach(action);
        } else {
            synchronized (mutex) {
                stream.forEach(action);
            }
        }
    }

    /**
     * 如果流具有定义的遇到顺序，则以遇到顺序对此流的每个元素执行操作。
     * <p>
     * 如果 mutex 不为 null，则整个遍历过程在同步块中执行。
     *
     * @param action 对元素执行的非干扰、无状态操作
     */
    @Override
    public void forEachOrdered(Consumer<? super E> action) {
        if (mutex == null) {
            stream.forEachOrdered(action);
        } else {
            synchronized (mutex) {
                stream.forEachOrdered(action);
            }
        }
    }

    /**
     * 将流的元素收集到一个数组中。
     * <p>
     * 如果 mutex 不为 null，则整个收集过程在同步块中执行。
     *
     * @return 包含流元素的数组
     */
    @Override
    public Object[] toArray() {
        if (mutex == null) {
            return stream.toArray();
        }
        synchronized (mutex) {
            return stream.toArray();
        }
    }

    /**
     * 使用提供的 {@code generator} 函数将流的元素收集到一个数组中，以分配返回的数组，以及分区执行或调整大小可能需要的任何其他数组。
     * <p>
     * 如果 mutex 不为 null，则整个收集过程在同步块中执行。
     *
     * @param generator 生成所需类型的新数组的函数
     * @param <A>       结果数组的组件类型
     * @return 包含流元素的数组
     */
    @Override
    public <A> A[] toArray(IntFunction<A[]> generator) {
        if (mutex == null) {
            return stream.toArray(generator);
        }
        synchronized (mutex) {
            return stream.toArray(generator);
        }
    }

    /**
     * 使用提供的身份值和累加函数对此流的元素执行缩减操作。
     * <p>
     * 如果 mutex 不为 null，则整个归约过程在同步块中执行。
     *
     * @param identity    身份值
     * @param accumulator 用于组合两个值的关联、非干扰、无状态函数
     * @return 归约结果
     */
    @Override
    public E reduce(E identity, BinaryOperator<E> accumulator) {
        if (mutex == null) {
            return stream.reduce(identity, accumulator);
        }
        synchronized (mutex) {
            return stream.reduce(identity, accumulator);
        }
    }

    /**
     * 使用提供的累加函数对此流的元素执行缩减操作。
     * <p>
     * 如果 mutex 不为 null，则整个归约过程在同步块中执行。
     *
     * @param accumulator 用于组合两个值的关联、非干扰、无状态函数
     * @return 描述归约结果的 {@link Optional}
     */
    @Override
    public Optional<E> reduce(BinaryOperator<E> accumulator) {
        if (mutex == null) {
            return stream.reduce(accumulator);
        }
        synchronized (mutex) {
            return stream.reduce(accumulator);
        }
    }

    /**
     * 使用提供的身份值、累加函数和组合函数对此流的元素执行缩减操作。
     * <p>
     * 如果 mutex 不为 null，则整个归约过程在同步块中执行。
     *
     * @param identity    身份值
     * @param accumulator 用于将额外输入值合并到部分结果中的关联、非干扰、无状态函数
     * @param combiner    用于组合两个值的关联、非干扰、无状态函数，当并行执行时必须兼容累加器函数
     * @param <U>         结果类型
     * @return 归约结果
     */
    @Override
    public <U> U reduce(U identity, BiFunction<U, ? super E, U> accumulator, BinaryOperator<U> combiner) {
        if (mutex == null) {
            return stream.reduce(identity, accumulator, combiner);
        }
        synchronized (mutex) {
            return stream.reduce(identity, accumulator, combiner);
        }
    }

    /**
     * 使用提供的 supplier、accumulator 和 combiner 函数对此流的元素执行可变归约操作。
     * <p>
     * 如果 mutex 不为 null，则整个收集过程在同步块中执行。
     *
     * @param supplier    提供新的结果容器的函数
     * @param accumulator 将值合并到结果容器中的关联、非干扰、无状态函数
     * @param combiner    用于组合两个值容器的关联、非干扰、无状态函数，当并行执行时必须兼容累加器函数
     * @param <R>         结果类型
     * @return 归约结果
     */
    @Override
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super E> accumulator, BiConsumer<R, R> combiner) {
        if (mutex == null) {
            return stream.collect(supplier, accumulator, combiner);
        }
        synchronized (mutex) {
            return stream.collect(supplier, accumulator, combiner);
        }
    }

    /**
     * 使用提供的 {@link Collector} 对此流的元素执行可变归约操作。
     * <p>
     * 如果 mutex 不为 null，则整个收集过程在同步块中执行。
     *
     * @param collector 描述归约的 {@link Collector}
     * @param <R>       结果类型
     * @param <A>       归约操作的中间累积类型
     * @return 归约结果
     */
    @Override
    public <R, A> R collect(Collector<? super E, A, R> collector) {
        if (mutex == null) {
            return stream.collect(collector);
        }
        synchronized (mutex) {
            return stream.collect(collector);
        }
    }

    /**
     * 根据提供的 {@link Comparator} 返回此流的最小元素。
     * <p>
     * 如果 mutex 不为 null，则整个查找过程在同步块中执行。
     *
     * @param comparator 用于比较流元素的非干扰、无状态 {@link Comparator}
     * @return 描述最小元素的 {@link Optional}
     */
    @Override
    public Optional<E> min(Comparator<? super E> comparator) {
        if (mutex == null) {
            return stream.min(comparator);
        }
        synchronized (mutex) {
            return stream.min(comparator);
        }
    }

    /**
     * 根据提供的 {@link Comparator} 返回此流的最大元素。
     * <p>
     * 如果 mutex 不为 null，则整个查找过程在同步块中执行。
     *
     * @param comparator 用于比较流元素的非干扰、无状态 {@link Comparator}
     * @return 描述最大元素的 {@link Optional}
     */
    @Override
    public Optional<E> max(Comparator<? super E> comparator) {
        if (mutex == null) {
            return stream.max(comparator);
        }
        synchronized (mutex) {
            return stream.max(comparator);
        }
    }

    /**
     * 返回此流中的元素计数。
     * <p>
     * 如果 mutex 不为 null，则整个计数过程在同步块中执行。
     *
     * @return 流中的元素计数
     */
    @Override
    public long count() {
        if (mutex == null) {
            return stream.count();
        }
        synchronized (mutex) {
            return stream.count();
        }
    }

    /**
     * 返回此流的任何元素是否匹配提供的谓词。
     * <p>
     * 如果 mutex 不为 null，则整个匹配检查过程在同步块中执行。
     *
     * @param predicate 用于测试元素的非干扰、无状态谓词
     * @return 如果任何元素匹配提供的谓词，则为 {@code true}，否则为 {@code false}
     */
    @Override
    public boolean anyMatch(Predicate<? super E> predicate) {
        if (mutex == null) {
            return stream.anyMatch(predicate);
        }
        synchronized (mutex) {
            return stream.anyMatch(predicate);
        }
    }

    /**
     * 返回此流的所有元素是否匹配提供的谓词。
     * <p>
     * 如果 mutex 不为 null，则整个匹配检查过程在同步块中执行。
     *
     * @param predicate 用于测试元素的非干扰、无状态谓词
     * @return 如果所有元素匹配提供的谓词，或者流为空，则为 {@code true}，否则为 {@code false}
     */
    @Override
    public boolean allMatch(Predicate<? super E> predicate) {
        if (mutex == null) {
            return stream.allMatch(predicate);
        }
        synchronized (mutex) {
            return stream.allMatch(predicate);
        }
    }

    /**
     * 返回此流的元素是否都不匹配提供的谓词。
     * <p>
     * 如果 mutex 不为 null，则整个匹配检查过程在同步块中执行。
     *
     * @param predicate 用于测试元素的非干扰、无状态谓词
     * @return 如果没有元素匹配提供的谓词，或者流为空，则为 {@code true}，否则为 {@code false}
     */
    @Override
    public boolean noneMatch(Predicate<? super E> predicate) {
        if (mutex == null) {
            return stream.noneMatch(predicate);
        }
        synchronized (mutex) {
            return stream.noneMatch(predicate);
        }
    }

    /**
     * 返回描述此流的第一个元素的 {@link Optional}，如果流为空，则返回空的 {@link Optional}。
     * <p>
     * 如果 mutex 不为 null，则整个查找过程在同步块中执行。
     *
     * @return 描述第一个元素的 {@link Optional}
     */
    @Override
    public Optional<E> findFirst() {
        if (mutex == null) {
            return stream.findFirst();
        }
        synchronized (mutex) {
            return stream.findFirst();
        }
    }

    /**
     * 返回描述此流的某些元素的 {@link Optional}，如果流为空，则返回空的 {@link Optional}。
     * <p>
     * 如果 mutex 不为 null，则整个查找过程在同步块中执行。
     *
     * @return 描述某些元素的 {@link Optional}
     */
    @Override
    public Optional<E> findAny() {
        if (mutex == null) {
            return stream.findAny();
        }
        synchronized (mutex) {
            return stream.findAny();
        }
    }

    /**
     * 返回此流元素的迭代器。
     * <p>
     * 如果 mutex 不为 null，则返回一个同步包装的迭代器。
     *
     * @return 元素迭代器
     */
    @Override
    public Iterator<E> iterator() {
        if (mutex == null) {
            return stream.iterator();
        }
        synchronized (mutex) {
            return new SynchronizedIterator<>(stream.iterator(), mutex);
        }
    }

    /**
     * 返回此流元素的 Spliterator。
     * <p>
     * 如果 mutex 不为 null，则返回一个同步包装的 Spliterator。
     *
     * @return 元素 Spliterator
     */
    @Override
    public Spliterator<E> spliterator() {
        if (mutex == null) {
            return stream.spliterator();
        }
        synchronized (mutex) {
            return new SynchronizedSpliterator<>(stream.spliterator(), mutex);
        }
    }

    /**
     * 返回此流是否并行执行。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行检查。
     *
     * @return 如果此流并行执行，则为 {@code true}，否则为 {@code false}
     */
    @Override
    public boolean isParallel() {
        if (mutex == null) {
            return stream.isParallel();
        }
        synchronized (mutex) {
            return stream.isParallel();
        }
    }

    /**
     * 返回等效的顺序流。如果流已经是顺序的，可能会返回自身。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @return 顺序流
     */
    @Override
    public Stream<E> sequential() {
        if (mutex == null) {
            return stream.sequential();
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.sequential(), mutex);
        }
    }

    /**
     * 返回等效的并行流。如果流已经是并行的，可能会返回自身。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @return 并行流
     */
    @Override
    public Stream<E> parallel() {
        if (mutex == null) {
            return stream.parallel();
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.parallel(), mutex);
        }
    }

    /**
     * 返回无序的等效流。如果流已经无序，可能会返回自身。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @return 无序流
     */
    @Override
    public Stream<E> unordered() {
        if (mutex == null) {
            return stream.unordered();
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.unordered(), mutex);
        }
    }

    /**
     * 返回具有附加关闭处理程序的等效流。当返回的流关闭时，将执行关闭处理程序。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @param closeHandler 当流关闭时要运行的 Runnable
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> onClose(Runnable closeHandler) {
        if (mutex == null) {
            return stream.onClose(closeHandler);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.onClose(closeHandler), mutex);
        }
    }

    /**
     * 关闭此流，导致调用所有附加的关闭处理程序。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行关闭操作。
     */
    @Override
    public void close() {
        if (mutex == null) {
            stream.close();
        } else {
            synchronized (mutex) {
                stream.close();
            }
        }
    }

    /**
     * 返回由将此流的每个元素映射到零个或多个元素而产生的流。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数
     * @param <R>    新流中元素的类型
     * @return 新的同步 Stream
     */
    @Override
    public <R> Stream<R> mapMulti(BiConsumer<? super E, ? super Consumer<R>> mapper) {
        if (mutex == null) {
            return stream.mapMulti(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.mapMulti(mapper), mutex);
        }
    }

    /**
     * 返回一个 {@link IntStream}，其中包含将此流的每个元素映射到零个或多个 {@code int} 值而产生的值。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 IntStream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数
     * @return 新的同步 IntStream
     */
    @Override
    public IntStream mapMultiToInt(BiConsumer<? super E, ? super IntConsumer> mapper) {
        if (mutex == null) {
            return stream.mapMultiToInt(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedIntStream(stream.mapMultiToInt(mapper), mutex);
        }
    }

    /**
     * 返回一个 {@link LongStream}，其中包含将此流的每个元素映射到零个或多个 {@code long} 值而产生的值。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 LongStream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数
     * @return 新的同步 LongStream
     */
    @Override
    public LongStream mapMultiToLong(BiConsumer<? super E, ? super LongConsumer> mapper) {
        if (mutex == null) {
            return stream.mapMultiToLong(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedLongStream(stream.mapMultiToLong(mapper), mutex);
        }
    }

    /**
     * 返回一个 {@link DoubleStream}，其中包含将此流的每个元素映射到零个或多个 {@code double} 值而产生的值。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 DoubleStream。
     *
     * @param mapper 用于映射元素的非干扰、无状态函数
     * @return 新的同步 DoubleStream
     */
    @Override
    public DoubleStream mapMultiToDouble(BiConsumer<? super E, ? super DoubleConsumer> mapper) {
        if (mutex == null) {
            return stream.mapMultiToDouble(mapper);
        }
        synchronized (mutex) {
            return new SynchronizedDoubleStream(stream.mapMultiToDouble(mapper), mutex);
        }
    }

    /**
     * 在满足给定谓词时截取流的元素。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @param predicate 用于测试元素的非干扰、无状态谓词
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> takeWhile(Predicate<? super E> predicate) {
        if (mutex == null) {
            return stream.takeWhile(predicate);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.takeWhile(predicate), mutex);
        }
    }

    /**
     * 在满足给定谓词时丢弃流的元素。
     * <p>
     * 如果 mutex 不为 null，则在同步块中执行操作，并返回一个新的同步 Stream。
     *
     * @param predicate 用于测试元素的非干扰、无状态谓词
     * @return 新的同步 Stream
     */
    @Override
    public Stream<E> dropWhile(Predicate<? super E> predicate) {
        if (mutex == null) {
            return stream.dropWhile(predicate);
        }
        synchronized (mutex) {
            return new SynchronizedStream<>(stream.dropWhile(predicate), mutex);
        }
    }

    /**
     * 将流的元素收集到不可修改的 {@link List} 中。
     * <p>
     * 如果 mutex 不为 null，则整个收集过程在同步块中执行。
     *
     * @return 包含流元素的列表
     */
    @Override
    public List<E> toList() {
        if (mutex == null) {
            return stream.toList();
        }
        synchronized (mutex) {
            return stream.toList();
        }
    }
}
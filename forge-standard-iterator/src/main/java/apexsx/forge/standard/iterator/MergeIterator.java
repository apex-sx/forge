/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util.iteration;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * 一个合并迭代器，它同时遍历两个独立的迭代器（键迭代器和值迭代器），
 * 并通过提供的 {@link BiFunction} 将每一对元素合并为一个结果元素。
 * <p>
 * 该迭代器仅在两个底层迭代器都有下一个元素时才认为有下一个元素。
 * 调用 {@link #next()} 时会同时推进两个底层迭代器。
 * <p>
 * 注意：如果两个底层迭代器的长度不一致，较短的迭代器耗尽后，此迭代器将停止。
 *
 * @param <K> 键元素的类型
 * @param <V> 值元素的类型
 * @param <E> 合并后输出元素的类型
 * @author Terraqo
 */
public class MergeIterator<K, V, E> implements Iterator<E> {

    /**
     * 键元素的源迭代器
     */
    private final Iterator<K> keyIterator;

    /**
     * 值元素的源迭代器
     */
    private final Iterator<V> valeIterator;

    /**
     * 用于将键和值合并为输出元素的函数
     */
    private final BiFunction<K, V, E> entryFunction;

    /**
     * 构造一个新的 MergeIterator。
     *
     * @param keyIterator   键元素的迭代器，不能为 null
     * @param valeIterator  值元素的迭代器，不能为 null
     * @param entryFunction 用于合并键和值的函数，不能为 null
     * @throws NullPointerException 如果任何参数为 null
     */
    public MergeIterator(Iterator<K> keyIterator, Iterator<V> valeIterator, BiFunction<K, V, E> entryFunction) {
        this.keyIterator = Objects.requireNonNull(keyIterator);
        this.valeIterator = Objects.requireNonNull(valeIterator);
        this.entryFunction = Objects.requireNonNull(entryFunction);
    }

    /**
     * 检查是否还有下一个元素。
     * 只有当键迭代器和值迭代器都有下一个元素时，才返回 true。
     *
     * @return 如果两个底层迭代器都有下一个元素，则返回 true；否则返回 false
     */
    @Override
    public boolean hasNext() {
        return keyIterator.hasNext() && valeIterator.hasNext();
    }

    /**
     * 返回下一个合并后的元素。
     * 此方法会同时从键迭代器和值迭代器中获取下一个元素，
     * 并使用构造函数中提供的 {@link BiFunction} 进行合并。
     *
     * @return 合并后的下一个元素
     * @throws java.util.NoSuchElementException 如果没有更多元素（即任一底层迭代器已耗尽）
     */
    @Override
    public E next() {
        return entryFunction.apply(keyIterator.next(), valeIterator.next());
    }

    /**
     * 从底层迭代器中移除最后一个返回的元素。
     * 此操作会同时在键迭代器和值迭代器上调用 {@link Iterator#remove()}。
     *
     * @throws UnsupportedOperationException 如果底层迭代器不支持 remove 操作
     * @throws IllegalStateException         如果尚未调用 next()，或在上次调用 next() 后已经调用了 remove()
     */
    @Override
    public void remove() {
        keyIterator.remove();
        valeIterator.remove();
    }
}

/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.standard.iterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * 转换迭代器类，用于将一种类型的可迭代对象转换为另一种类型的可迭代对象
 *
 * @param <E> 源可迭代对象的元素类型
 * @param <T> 目标可迭代对象的元素类型
 */
public class ConvertingIterable<E, T> implements Iterable<T> {
    /**
     * 源可迭代对象，用于遍历原始类型的元素
     */
    private final Iterable<E> iterator;
    /**
     * 转换函数，用于将源类型E的元素转换为目标类型T的元素
     */
    private final Function<E, T> mapper;

    /**
     * 构造方法，接受数组作为输入
     *
     * @param input     输入数组，不能为空
     * @param converter 转换函数，用于将数组元素转换为新类型，不能为空
     */
    public ConvertingIterable(E[] input, Function<E, T> converter) {
        this(new ArrayIterable<>(input), converter);
    }

    /**
     * 构造方法，接受可迭代对象作为输入
     *
     * @param input     输入的可迭代对象，不能为空
     * @param converter 转换函数，用于将迭代元素转换为新类型，不能为空
     */
    public ConvertingIterable(Iterable<E> input, Function<E, T> converter) {
        this.iterator = Objects.requireNonNull(input, "Input must not be null");
        this.mapper = Objects.requireNonNull(converter, "Converter must not be null");
    }

    /**
     * 返回一个类型为T的元素迭代器
     *
     * @return 一个新的转换后的迭代器
     */
    @Override
    public Iterator<T> iterator() {
        return new ConvertingIterator<>(this.iterator.iterator(), this.mapper);
    }
}

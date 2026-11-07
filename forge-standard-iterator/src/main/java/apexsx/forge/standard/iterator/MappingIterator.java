/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.util.iteration;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

/**
 * 转换迭代器类，用于将一种类型的迭代器转换为另一种类型的迭代器
 *
 * @param <E> 源迭代器的元素类型
 * @param <T> 目标迭代器的元素类型
 * @author Terraqo
 */
public class MappingIterator<E, T> implements Iterator<T> {
    /**
     * 源迭代器，用于遍历原始类型的元素
     */
    private final Iterator<E> iterator;
    /**
     * 转换函数，用于将源类型E的元素转换为目标类型T的元素
     */
    private final Function<E, T> mapper;

    /**
     * 构造方法
     *
     * @param iterator 源迭代器，不能为空
     * @param mapper   转换函数，不能为空
     */
    public MappingIterator(Iterator<E> iterator, Function<E, T> mapper) {
        this.iterator = Objects.requireNonNull(iterator, "Iterator must not be null");
        this.mapper = Objects.requireNonNull(mapper, "Mapper must not be null");
    }

    /**
     * 检查是否还有下一个元素
     *
     * @return 如果还有下一个元素则返回true，否则返回false
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * 获取下一个转换后的元素
     *
     * @return 转换后的下一个元素
     */
    @Override
    public T next() {
        return mapper.apply(iterator.next());
    }
}

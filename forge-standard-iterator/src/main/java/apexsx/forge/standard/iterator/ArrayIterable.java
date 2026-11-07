/*
 * Copyright (c) 2025-2026.
 */
package apexsx.forge.standard.iterator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * ArrayIterable 是一个泛型可迭代类，用于包装数组并提供迭代功能
 * 实现了 Iterable 接口，允许使用增强for循环遍历数组
 *
 * @param <E> 可迭代元素的类型
 * @author Terraqo
 */
public class ArrayIterable<E> implements Iterable<E> {
    private final E[] elements;

    /**
     * 构造函数，使用给定的数组创建可迭代对象
     *
     * @param elements 要迭代的数组，不能为null
     * @throws NullPointerException 如果elements参数为null则抛出异常
     */
    public ArrayIterable(E[] elements) {
        this.elements = Arrays.copyOf(Objects.requireNonNull(elements, "The elements array must not be null"), elements.length);
    }

    /**
     * 返回一个用于遍历数组元素的迭代器
     *
     * @return 一个新的ArrayIterator实例
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<>(elements);
    }
}
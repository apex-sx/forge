/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

import java.util.NoSuchElementException;

/**
 * {@code byte} 迭代器接口，用于遍历 {@code byte} 类型的数据。
 * 该接口提供了检查是否有下一个 {@code byte} 值以及获取下一个 {@code byte} 值的方法。
 *
 * @author Terraqo
 * @see ByteRawIterator#hasNextByteRawValue() 判断是否有下一个 {@code byte} 值
 * @see ByteRawIterator#nextByteRawValue() 获取下一个 {@code byte} 值
 */
public interface ByteRawIterator {

    /**
     * 是否有下一个{@code byte}值
     *
     * @return 如果有返回true，否则返回false
     */
    boolean hasNextByteRawValue();

    /**
     * 获取下一个值
     *
     * @return 返回下一个值
     * @throws NoSuchElementException 如果没有下一个值返回该异常
     */
    byte nextByteRawValue() throws NoSuchElementException;
}

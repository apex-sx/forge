/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

/**
 * {@code byte}可迭代器
 *
 * @author Terraqo
 */
@FunctionalInterface
public interface ByteRawIterable {

    /**
     * 获取{@code ByteRawIterator}
     *
     * @return 返回{@code ByteRawIterator}
     */
    ByteRawIterator byteRawIterator();
}

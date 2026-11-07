/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.standard.iterator;

import java.util.Iterator;

/**
 * 空迭代器实现类，该迭代器不包含任何元素
 *
 * @param <T> 迭代器元素类型
 * @author Terraqo
 */
public final class EmptyIterator<T> implements Iterator<T> {

    /**
     * 始终返回false，因为空迭代器没有下一个元素
     */
    @Override
    public boolean hasNext() {
        return false;
    }

    /**
     * 返回null，因为该迭代器不包含任何元素
     * 注意：根据Iterator接口规范，不应该调用此方法，因为hasNext()始终返回false
     */
    @Override
    public T next() {
        return null;
    }
}
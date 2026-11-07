/*
 * Copyright (c) 2026.
 */

package apexsx.forge.standard.iterator;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * 将 Iterator 适配为 Enumeration 的适配器类
 * 该类实现了 Enumeration 接口，包装一个 Iterator 实例
 * 使得可以将 Iterator 作为 Enumeration 使用
 *
 * @param <E> 元素类型
 * @author Terraqo
 */
public final class IteratorEnumeration<E> implements Enumeration<E> {
    private final Iterator<E> iterator;

    public IteratorEnumeration(Iterator<E> iterator) {
        this.iterator = iterator == null ? Collections.emptyIterator() : iterator;
    }

    /**
     * 检查是否还有更多元素
     *
     * @return 如果还有更多元素则返回true，否则返回false
     */
    @Override
    public boolean hasMoreElements() {
        return iterator.hasNext();
    }

    /**
     * 获取下一个元素
     *
     * @return 下一个元素
     * @throws java.util.NoSuchElementException 如果没有更多元素
     */
    @Override
    public E nextElement() {
        return iterator.next();
    }
}

/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.loader;

import java.util.Collections;
import java.util.Iterator;

/**
 * 基于迭代器的预读加载器实现
 * 该类允许通过迭代器预读元素，支持在不移动当前位置的情况下查看下一个元素
 *
 * @param <T> 元素类型
 * @author Terraqo
 */
public class ElementLookaheadLoaderByIterator<T> extends ElementLookaheadLoaderBase<T> {

    /**
     * 底层迭代器，用于实际的数据遍历
     * 如果构造函数传入的迭代器为null，则会被设置为{@link Collections#emptyIterator()}空迭代器
     */
    private final Iterator<T> iterator;

    /**
     * 使用可迭代对象构造预读加载器
     *
     * @param iterable 可迭代对象，可以为null
     */
    public ElementLookaheadLoaderByIterator(Iterable<T> iterable) {
        this(iterable == null ? null : iterable.iterator());
    }

    /**
     * 使用迭代器构造预读加载器
     * 如果传入的迭代器为null，则使用空迭代器
     *
     * @param iterator 迭代器，可以为null
     */
    public ElementLookaheadLoaderByIterator(Iterator<T> iterator) {
        this.iterator = iterator == null ? Collections.emptyIterator() : iterator;
    }

    /**
     * 从底层数据源获取下一个元素
     * 此方法由子类实现以提供实际的数据源逻辑
     *
     * @return 从数据源获取的下一个元素，若已到达数据源末尾则返回 {@link #endIndicator()}
     */
    @Override
    protected T fetchElement() {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return endIndicator();
    }

}

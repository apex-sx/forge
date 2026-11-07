/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.loader;

import java.util.LinkedList;

/**
 * 具有前瞻功能的数据加载器核心抽象实现
 *
 * <p>此抽象类为实现支持向前查看元素和向后回溯的数据流提供了基础框架。
 * 子类必须实现 {@link #fetchElement()} 方法以提供实际的数据源，
 * 并可通过 {@link #retrieveElement(int, boolean)} 方法在不消耗元素的情况下查看未来元素，
 * 或者消费指定偏移量处的元素。
 *
 * @param <T> 流中元素的泛型类型
 * @author Terraqo
 */
public abstract class ElementLookaheadLoaderBase<T> implements ElementLookaheadLoader<T> {
    /**
     * 存储已预取但尚未消费的元素的缓存队列
     * 此队列用于实现前瞻功能，保存从数据源提前加载的元素
     */
    private final LinkedList<T> cache = new LinkedList<>();
    /**
     * 标识是否已经到达数据源的末尾
     * 当 {@link #fetchElement()} 返回 null 时设置为 true
     */
    protected boolean endReached = false;

    /**
     * 结束指示符缓存，用于标识数据流的结束
     * 使用缓存避免每次调用 endIndicator() 时重复创建对象
     */
    private T endIndicator;

    /**
     * 检索指定偏移量处的元素，并根据需要决定是否消费该元素及之前的元素
     *
     * <p>此方法允许向前查看流中的元素而不消费它们（当 consume=false 时），
     * 或者检索并消费指定偏移量处的元素以及所有在其之前的元素（当 consume=true 时）。
     * 如果缓存中没有足够的元素满足请求的偏移量，会尝试从数据源中获取更多元素。
     *
     * @param offset  要检索的元素相对于当前位置的偏移量（非负整数）
     * @param consume 如果为 true，则消费从索引 0 到 offset 的所有元素（包括 offset 处的元素）；
     *                如果为 false，则仅返回指定偏移量处的元素而不消费任何元素
     * @return 在指定偏移量处的元素，如果已达到数据源末尾且该位置没有元素，则返回 endIndicator()
     * @throws IllegalArgumentException 如果偏移量为负数
     */
    @Override
    public T retrieveElement(int offset, boolean consume) {
        if (offset < 0) {
            throw new IllegalArgumentException("The item offset must not be negative, but was: " + offset);
        }

        // 填充缓存直到有足够的元素满足请求的偏移量
        while (offset >= cache.size() && !endReached) {
            T item = fetchElement();
            if (item == endIndicator()) {
                endReached = true;
            } else if (item != null) {
                cache.add(item);
            }
        }

        T result;
        int elementsToConsume;

        if (offset >= cache.size()) {
            result = endIndicator();
            // 当缓存中没有足够元素时，只能消费已有的元素
            elementsToConsume = cache.size();
        } else {
            result = cache.get(offset);
            elementsToConsume = offset + 1; // 包括offset位置的元素
        }

        if (consume) {
            // 消费从索引0到需要消费数量的元素
            for (int i = 0; i < elementsToConsume; i++) {
                if (!cache.isEmpty()) { // 确保缓存不为空再删除
                    T removed = cache.removeFirst();
                    removedElement(removed);
                }
            }
        }

        return result;
    }

    /**
     * 返回结束标识符，用于表示序列的结束
     * 当到达序列末尾时，peek方法将返回此值
     * 默认实现返回null，子类可以根据需要重写此方法
     *
     * @return 序列结束标识符
     */
    @Override
    public final T endIndicator() {
        if (endIndicator == null) {
            endIndicator = eof();
        }
        return endIndicator;
    }

    /**
     * 获取表示序列结束的对象
     * 子类可以重写此方法以自定义结束标识符
     *
     * @return 表示序列结束的对象，默认为null
     */
    protected T eof() {
        return null;
    }

    /**
     * 从底层数据源获取下一个元素
     * 此方法由子类实现以提供实际的数据源逻辑
     *
     * @return 从数据源获取的下一个元素，若已到达数据源末尾则返回 null
     */
    protected T fetchElement() {
        return null;
    }

    /**
     * 处理被移除的元素
     * 当元素从缓存中被消费时调用此方法，可用于资源清理等操作
     *
     * @param removedElement 被移除的元素
     */
    protected void removedElement(T removedElement) {
    }
}

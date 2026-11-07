/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.loader;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 一个支持向前预览和弹出元素的迭代器接口
 * 提供了向前查看元素的能力，同时支持消费指定偏移位置的元素
 *
 * @param <T> 元素类型
 * @author Terraqo
 */
public interface ElementLookaheadLoader<T> extends Iterator<T> {

    /**
     * 根据偏移量检索元素，并根据参数决定是否消费该元素
     *
     * @param offset  偏移量，从当前位置开始计算（0表示当前位置）
     * @param consume 如果为true，则元素将被消费（从序列中移除）；如果为false，则仅预览
     * @return 指定偏移位置的元素
     */
    T retrieveElement(int offset, boolean consume);

    /**
     * 检查是否还有下一个元素
     * 通过检查前瞻缓存是否为空来判断
     *
     * @return 如果还有下一个元素则返回true，否则返回false
     */
    @Override
    default boolean hasNext() {
        return !this.isEmpty();
    }

    /**
     * 获取下一个元素并将其从序列中移除
     * 如果没有下一个元素可用，则抛出NoSuchElementException异常
     *
     * @return 下一个元素
     * @throws NoSuchElementException 如果没有下一个元素可用
     */
    @Override
    default T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException("No more elements available");
        }
        return this.popElement();
    }

    /**
     * 检查前瞻缓存是否为空
     * 当前元素与结束标识符相同时认为缓存为空
     *
     * @return 如果前瞻缓存为空则返回true，否则返回false
     */
    default boolean isEmpty() {
        return this.peekElement() == this.endIndicator();
    }

    /**
     * 检查是否已到达输入流的末尾
     * 当前元素与结束标识符相同时认为已到末尾
     *
     * @return 当已到达输入流末尾时返回true，否则返回false
     */
    default boolean isEOF() {
        return this.peekElement() == this.endIndicator();
    }

    /**
     * 预览当前元素（不消耗）
     * 等效于调用{@link #peekElementAt(int)}(0)
     *
     * @return 当前元素，如果已到达末尾则返回结束标识符
     */
    default T peekElement() {
        return this.peekElementAt(0);
    }

    /**
     * 预览下一个元素（不消耗）
     * 等效于调用{@link #peekElementAt(int)}(1)
     *
     * @return 下一个元素，如果已到达末尾则返回结束标识符
     */
    default T peekElementNext() {
        return this.peekElementAt(1);
    }

    /**
     * 预览指定偏移量处的元素（不消耗）
     *
     * @param offset 偏移量，必须大于等于0（0表示当前位置）
     * @return 指定偏移量处的元素
     * @throws IllegalArgumentException 如果offset小于0
     */
    default T peekElementAt(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must be greater than or equal to 0");
        }
        T element = this.retrieveElement(offset, false);
        if (element == this.endIndicator()) {
            return element;
        }
        if (element == null) {
            throw new NoSuchElementException("No more elements available");
        }
        return element;
    }

    /**
     * 弹出（消费）当前元素
     * 等效于调用{@link #popElementAt(int)}(0)
     *
     * @return 当前元素
     */
    default T popElement() {
        return this.popElementAt(0);
    }

    /**
     * 弹出（消费）下一个元素
     * 等效于调用{@link #popElementAt(int)}(1)
     *
     * @return 下一个元素
     */
    default T popElementNext() {
        return this.popElementAt(1);
    }

    /**
     * 弹出（消费）指定偏移量处的元素
     *
     * @param offset 偏移量，必须大于等于0（0表示当前位置）
     * @return 指定偏移量处的元素
     * @throws IllegalArgumentException 如果offset小于0
     */
    default T popElementAt(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must be greater than or equal to 0");
        }
        T element = this.retrieveElement(offset, true);
        if (element == this.endIndicator()) {
            return element;
        }
        if (element == null) {
            throw new NoSuchElementException("No more elements available");
        }
        return element;
    }

    /**
     * 返回结束标识符，用于表示序列的结束
     * 当到达序列末尾时，peek方法将返回此值
     * 默认实现返回null，子类可以根据需要重写此方法
     *
     * @return 序列结束标识符
     */
    default T endIndicator() {
        return null;
    }

}

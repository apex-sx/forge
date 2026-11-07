package apexsx.forge.standard.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 循环迭代器
 *
 * @author zhitron
 */
public class CircularIterator<T extends BidirectionalNode<T>> implements Iterator<T> {
    /**
     * 迭代方向，true表示正向（next方向），false表示反向（previous方向）
     */
    private final boolean direction;
    /**
     * 是否包含哨兵节点，true表示从哨兵节点开始迭代，false表示从哨兵节点的下一个节点开始迭代
     */
    private final boolean include;
    /**
     * 哨兵节点，用于标记循环的结束点
     */
    private final T sentinel;
    /**
     * 标识是否为第一次访问，用于处理包含哨兵节点的情况
     */
    private boolean first;
    /**
     * 当前节点，表示迭代过程中的当前位置
     */
    private T current;

    /**
     * 构造一个循环迭代器
     *
     * @param sentinel  哨兵节点，作为循环的起始/结束标志
     * @param include   是否包含哨兵节点开始迭代
     * @param direction 迭代方向，true表示正向，false表示反向
     */
    public CircularIterator(T sentinel, boolean include, boolean direction) {
        this.direction = direction;
        this.include = include;
        this.sentinel = sentinel;
        this.first = true;
        if (include) {
            this.current = sentinel;
        } else {
            if (sentinel == null) {
                this.current = null;
            } else {
                if (direction) {
                    this.current = sentinel.next();
                } else {
                    this.current = sentinel.previous();
                }
            }
        }
    }

    /**
     * 判断是否存在下一个元素
     *
     * @return 如果存在下一个元素则返回true，否则返回false
     */
    @Override
    public boolean hasNext() {
        // 如果current为null，肯定没有下一个元素
        if (current == null) {
            return false;
        }
        // 如果include为true，需要特殊处理第一次访问sentinel的情况
        if (include) {
            // 第一次访问或者当前节点不等于哨兵节点时，表示还有下一个元素
            return first || current != sentinel;
        } else {
            // 如果include为false，则当current等于sentinel时停止
            return current != sentinel;
        }
    }

    /**
     * 获取下一个元素
     *
     * @return 当前元素
     */
    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        T temp = current;
        // 根据方向移动到下一个节点
        current = (direction ? current.next() : current.previous());
        // 设置first为false，表示已经不是第一次访问
        first = false;
        return temp;
    }
}
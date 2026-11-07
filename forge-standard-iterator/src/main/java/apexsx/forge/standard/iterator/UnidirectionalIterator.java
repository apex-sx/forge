package apexsx.forge.standard.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 单向迭代器实现类
 * 用于遍历单向链表结构，支持从哨兵节点或其下一个节点开始迭代
 *
 * @param <T> 继承自UnidirectionalNode的节点类型
 * @author zhitron
 */
public class UnidirectionalIterator<T extends UnidirectionalNode<T>> implements Iterator<T> {
    /**
     * 是否包含哨兵节点标志
     * true表示从哨兵节点开始迭代，false表示从哨兵节点的下一个节点开始迭代
     */
    private final boolean include;
    /**
     * 哨兵节点引用，作为迭代终止条件判断依据
     */
    private final T sentinel;
    /**
     * 首次访问标志，用于处理包含哨兵节点的迭代逻辑
     */
    private boolean first;
    /**
     * 当前迭代位置的节点引用
     */
    private T current;

    /**
     * 构造函数，创建一个包含哨兵节点的单向迭代器
     *
     * @param sentinel 哨兵节点引用
     */
    public UnidirectionalIterator(T sentinel) {
        this(sentinel, true);
    }

    /**
     * 构造函数，创建一个单向迭代器
     *
     * @param sentinel 哨兵节点引用
     * @param include  是否从哨兵节点开始迭代
     */
    public UnidirectionalIterator(T sentinel, boolean include) {
        this.sentinel = sentinel;
        this.include = include;
        this.current = include ? sentinel : sentinel.next();
        this.first = true;
    }

    /**
     * 判断是否还有下一个可迭代元素
     *
     * @return 如果还有下一个元素返回true，否则返回false
     */
    @Override
    public boolean hasNext() {
        // 当前节点为空时，无下一元素
        if (current == null) {
            return false;
        }
        // 包含哨兵节点的特殊处理逻辑
        if (include) {
            // 首次访问或当前节点不为哨兵节点时，说明还有下一元素
            return first || current != sentinel;
        } else {
            // 不包含哨兵节点时，当前节点不为哨兵节点即表示还有下一元素
            return current != sentinel;
        }
    }

    /**
     * 获取下一个元素并移动游标到下一位置
     *
     * @return 下一个元素的引用
     * @throws NoSuchElementException 当没有更多元素可供迭代时抛出异常
     */
    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more elements to iterate");
        }
        first = false;
        T node = current;
        current = current.next();
        return node;
    }
}

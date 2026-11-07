package apexsx.forge.standard.iterator;

/**
 * 双向节点接口，用于定义循环链表中节点的基本操作
 *
 * @param <T> 节点存储的数据类型
 * @author zhitron
 */
public interface UnidirectionalNode<T extends UnidirectionalNode<T>> {
    /**
     * 获取下一个节点
     *
     * @return 返回下一个节点的引用
     */
    T next();

    /**
     * 设置下一个节点
     *
     * @param node 要设置为下一个节点的引用
     */
    default void next(T node) {
        throw new UnsupportedOperationException("Setting next node of UnidirectionalNode is not supported by default");
    }
}

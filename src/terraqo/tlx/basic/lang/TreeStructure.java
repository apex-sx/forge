package pxf.toolkit.basic.lang;

import java.util.List;

/**
 * 树结构
 *
 * @author potatoxf
 * @date 2021/4/19
 */
public interface TreeStructure<T extends Comparable<T>, E extends TreeStructure<T, E>>
    extends Comparable<E> {

  /**
   * 当前节点引用
   *
   * @return {@code T}
   */
  T currentReference();

  /**
   * 父节点引用
   *
   * @return {@code T}
   */
  T parentReference();

  /**
   * 获取父节点
   *
   * @return {@code E}
   */
  E getParent();

  /**
   * 设置父节点
   *
   * @param parent 父节点
   */
  void setParent(E parent);

  /**
   * 获取孩子节点
   *
   * @return {@code List<E>}
   */
  List<E> getChildren();

  /**
   * 设置孩子节点
   *
   * @param children 改节点的孩子节点
   */
  void setChildren(List<E> children);
}

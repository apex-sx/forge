package pxf.toolkit.basic.lang;

/**
 * 树结构
 *
 * @author potatoxf
 * @date 2021/4/19
 */
public interface TreeLevelStructure<T extends Comparable<T>, E extends TreeLevelStructure<T, E>>
    extends TreeStructure<T, E> {

  /**
   * 当前所在层级
   *
   * @return 返回当前层级
   */
  int level();
}

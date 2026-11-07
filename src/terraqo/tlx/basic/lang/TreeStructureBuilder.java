package pxf.toolkit.basic.lang;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 树结构构建器
 *
 * @author potatoxf
 * @date 2021/4/19
 */
public class TreeStructureBuilder<T extends Comparable<T>, E extends TreeStructure<T, E>> {

  /**
   * 构建单个树
   *
   * @param allElement 所有元素
   * @param rootValue 指定根节点值
   * @return {@code E}
   */
  public List<E> buildTree(List<E> allElement, T rootValue) {
    allElement = clearElement(allElement);
    List<E> parentElement =
        allElement.stream()
            .filter(e -> e.currentReference().equals(rootValue))
            .collect(Collectors.toUnmodifiableList());
    if (parentElement.isEmpty()) {
      return Collections.emptyList();
    }
    for (E e : parentElement) {
      e.setParent(null);
      buildTreeRecursion(e, allElement);
    }
    return parentElement;
  }

  /**
   * 构建单个树
   *
   * @param allElement 所有元素
   * @return {@code E}
   */
  public E buildSingleTree(List<E> allElement, T parent) {
    List<E> root = buildTree(allElement, parent);
    if (root.isEmpty()) {
      return null;
    }
    return root.get(0);
  }

  /**
   * 清理元素
   *
   * @param allElement 元素列表
   * @return {@code List<E>}
   */
  protected List<E> clearElement(List<E> allElement) {
    allElement =
        allElement.stream()
            .filter(Objects::nonNull)
            .filter(e -> e.parentReference() != null)
            .filter(e -> e.currentReference() != null)
            .sorted()
            .collect(Collectors.toCollection(LinkedList::new));
    return allElement;
  }

  /**
   * 构建单个树
   *
   * @param parent 所有元素
   * @param allElement 所有元素
   */
  private void buildTreeRecursion(E parent, List<E> allElement) {
    List<E> children = new LinkedList<>();
    Iterator<E> iterator = allElement.iterator();
    while (iterator.hasNext()) {
      E next = iterator.next();
      if (next.parentReference().equals(parent.currentReference())) {
        next.setParent(parent);
        children.add(next);
        iterator.remove();
      }
    }
    parent.setChildren(children.isEmpty() ? null : children);
    for (E child : children) {
      buildTreeRecursion(child, allElement);
    }
  }
}

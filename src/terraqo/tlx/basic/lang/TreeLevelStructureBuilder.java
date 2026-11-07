package pxf.toolkit.basic.lang;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 树结构构建器
 *
 * @author potatoxf
 * @date 2021/4/19
 */
public class TreeLevelStructureBuilder<T extends Comparable<T>, E extends TreeLevelStructure<T, E>>
    extends TreeStructureBuilder<T, E> {

  /**
   * 构建单个树
   *
   * @param allElement 所有元素
   * @param rootValue 指定根节点值
   * @return {@code E}
   */
  public List<E> buildTreeByLevel(List<E> allElement, T rootValue) {
    allElement = clearElement(allElement);
    Optional<E> first =
        allElement.stream().filter(e -> e.currentReference().equals(rootValue)).findFirst();
    if (first.isEmpty()) {
      return Collections.emptyList();
    }
    int startLevel = first.get().level();
    Multimap<Integer, E> levelMap = LinkedListMultimap.create();
    allElement.forEach(e -> levelMap.put(e.level(), e));
    return buildTreeByLevel(startLevel, levelMap);
  }

  /**
   * 构建单个树
   *
   * @param allElement 所有元素
   * @param startLevel 起始层级
   * @return {@code E}
   */
  public List<E> buildTreeByLevel(List<E> allElement, int startLevel) {
    allElement = clearElement(allElement);
    Multimap<Integer, E> levelMap = LinkedListMultimap.create();
    allElement.forEach(e -> levelMap.put(e.level(), e));
    return buildTreeByLevel(startLevel, levelMap);
  }

  /**
   * 构建单个树
   *
   * @param allElement 所有元素
   * @return {@code E}
   */
  public E buildSingleTreeByLevel(List<E> allElement, int startLevel) {
    List<E> root = buildTreeByLevel(allElement, startLevel);
    if (root.isEmpty()) {
      return null;
    }
    return root.get(0);
  }

  private List<E> buildTreeByLevel(int startLevel, Multimap<Integer, E> levelMap) {
    for (int currentLevel = startLevel; levelMap.containsKey(currentLevel); currentLevel++) {
      Collection<E> currentLevelElements = levelMap.get(currentLevel);
      int nextLevel = currentLevel + 1;
      if (!levelMap.containsKey(nextLevel)) {
        for (E currentElement : currentLevelElements) {
          currentElement.setParent(null);
        }
        break;
      }
      Collection<E> nextLevelElements = levelMap.get(nextLevel);
      for (E parent : currentLevelElements) {
        List<E> children = new LinkedList<>();
        for (E child : nextLevelElements) {
          if (child.parentReference().equals(parent.currentReference())) {
            child.setParent(parent);
            children.add(child);
          }
        }
        parent.setChildren(children.isEmpty() ? null : children);
      }
    }
    return levelMap.get(startLevel).stream()
        .peek(e -> e.setParent(null))
        .collect(Collectors.toUnmodifiableList());
  }
}

package pxf.toolkit.basic.lang.collection;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 基本Map表
 *
 * @author potatoxf
 * @date 2021/3/6
 */
public abstract class BaseMap<K, V> implements Map<K, V>, Cloneable, Serializable {

  /**
   * 计算hashcode
   *
   * @param key 键
   * @return 返回hashcode
   */
  protected int hash(Object key) {
    if (key == null) {
      return 0;
    }
    int h = 0;
    if (key instanceof String) {
      for (int i = 0; i < ((String) key).length(); i++) {
        h = 31 * h + Character.toLowerCase(((String) key).charAt(i));
      }
    }
    if (h == 0) {
      h = key.hashCode();
    }
    return (h) ^ (h >>> 16);
  }

  /**
   * 红黑平衡树结点
   *
   * @param <K> 键
   * @param <V> 值
   */
  protected static class BalanceTreeNode<K, V> extends BaseMap.TreeNode<K, V> {

    /** 是否为红色节点 */
    private boolean color;

    public BalanceTreeNode(int hashcode, K key, V value) {
      super(hashcode, key, value);
    }

    public boolean isBlack() {
      return !color;
    }

    public boolean isRed() {
      return color;
    }

    public void setColor(boolean color) {
      this.color = color;
    }
  }

  /**
   * 树结点
   *
   * @param <K> 键
   * @param <V> 值
   */
  protected static class TreeNode<K, V> extends BaseMap.Node<K, V> {

    private BaseMap.TreeNode<K, V> parent;
    private BaseMap.TreeNode<K, V> left;
    private BaseMap.TreeNode<K, V> right;

    public TreeNode(int hashcode, K key, V value) {
      super(hashcode, key, value);
    }

    public TreeNode<K, V> getParent() {
      return parent;
    }

    public void setParent(TreeNode<K, V> parent) {
      this.parent = parent;
    }

    public TreeNode<K, V> getLeft() {
      return left;
    }

    public void setLeft(TreeNode<K, V> left) {
      this.left = left;
    }

    public TreeNode<K, V> getRight() {
      return right;
    }

    public void setRight(TreeNode<K, V> right) {
      this.right = right;
    }
  }

  /**
   * 链表结点
   *
   * @param <K> 键
   * @param <V> 值
   */
  protected static class LinkNode<K, V> extends BaseMap.Node<K, V> {

    private BaseMap.LinkNode<K, V> next;

    public LinkNode(int hashcode, K key, V value, BaseMap.LinkNode<K, V> next) {
      super(hashcode, key, value);
      this.next = next;
    }

    public LinkNode<K, V> getNext() {
      return next;
    }

    public void setNext(LinkNode<K, V> next) {
      this.next = next;
    }
  }

  /**
   * 结点
   *
   * @param <K> 键
   * @param <V> 值
   */
  protected static class Node<K, V> implements Map.Entry<K, V> {

    /** 该结点存储的hashcode */
    private final int hashcode;
    /** 键 */
    private final K key;
    /** 值 */
    private V value;

    public Node(int hashcode, K key, V value) {
      this.hashcode = hashcode;
      this.key = key;
      this.value = value;
    }

    @Override
    public final K getKey() {
      return key;
    }

    @Override
    public final V getValue() {
      return value;
    }

    @Override
    public final V setValue(V newValue) {
      V oldValue = value;
      value = newValue;
      return oldValue;
    }

    public int getHashcode() {
      return hashcode;
    }

    @Override
    public final String toString() {
      return key + "=" + value;
    }

    @Override
    public final int hashCode() {
      return Objects.hashCode(key) ^ Objects.hashCode(value);
    }

    @Override
    public final boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o instanceof Map.Entry) {
        Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
        return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
      }
      return false;
    }
  }
}

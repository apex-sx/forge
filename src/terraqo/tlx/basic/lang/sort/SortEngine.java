package pxf.toolkit.basic.lang.sort;

import javax.annotation.Nonnull;

/**
 * 排序引擎
 *
 * @author potatoxf
 * @date 2020/12/15
 */
public interface SortEngine<T> {

  /**
   * 排序给定集合
   *
   * @param t 被排序对象
   */
  void sort(@Nonnull T t);
}

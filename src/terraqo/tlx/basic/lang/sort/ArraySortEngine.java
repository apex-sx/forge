package pxf.toolkit.basic.lang.sort;

import javax.annotation.Nonnull;

/**
 * 排序引擎
 *
 * @author potatoxf
 * @date 2020/12/15
 */
public interface ArraySortEngine<T extends Comparable<T>> extends SortEngine<T[]> {

  /**
   * 排序数组
   *
   * @param ts 数组
   */
  @Override
  default void sort(@Nonnull T[] ts) {
    if (ts == null) {
      return;
    }
    sort(ts, 0, ts.length);
  }

  /**
   * 排序数组
   *
   * @param ts 数组
   * @param start 起始索引
   * @param end 结束索引（不包括）
   */
  void sort(T[] ts, int start, int end);
}

package pxf.toolkit.basic.lang.sort;

import javax.annotation.Nonnull;

/**
 * 选择排序
 *
 * @author potatoxf
 * @date 2020/12/15
 */
public class SelectionArraySortEngine<T extends Comparable<T>> extends AbstractArraySortEngine<T> {

  @Override
  protected void sort(@Nonnull final T[] ts, final int start, final int end, final int length) {
    // 选择次数
    for (int i = 0; i < length - 1; i++) {
      int mini = i;
      // 选择最小
      for (int j = i + 1; j < length; j++) {
        if (SortHelper.gt(ts, mini, j)) {
          mini = j;
        }
      }
      if (mini != i) {
        SortHelper.exchange(ts, mini, i);
      }
    }
  }
}

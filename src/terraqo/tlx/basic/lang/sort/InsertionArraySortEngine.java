package pxf.toolkit.basic.lang.sort;

import javax.annotation.Nonnull;

/**
 * 插入排序
 *
 * @author potatoxf
 * @date 2020/12/15
 */
public class InsertionArraySortEngine<T extends Comparable<T>> extends AbstractArraySortEngine<T> {

  @Override
  protected void sort(@Nonnull final T[] ts, final int start, final int end, final int length) {
    for (int i = start + 1; i < end; i++) {
      T key = ts[i];
      int j = i - 1;
      while (j >= start && SortHelper.lt(key, ts[j])) {
        j--;
      }
      int t = j + 1;
      if (t != i) {
        System.arraycopy(ts, t, ts, t + 1, i - t);
        ts[t] = key;
      }
    }
  }
}

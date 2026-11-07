package pxf.toolkit.basic.lang.sort;

import javax.annotation.Nonnull;

/**
 * 堆排序
 *
 * @author potatoxf
 * @date 2020/12/15
 */
public class HeapArraySortEngine<T extends Comparable<T>> extends AbstractArraySortEngine<T> {

  @Override
  protected void sort(@Nonnull final T[] ts, final int start, final int end, final int length) {
    if (start != 0 && start != 1) {
      throw new IllegalArgumentException("The start index must be equal 0 or 1");
    }
    if (end != ts.length) {
      throw new IllegalArgumentException("The end index must be equal " + ts.length);
    }
    boolean isIncludeZero = start == 0;
    int n = end - 1;
    for (int i = n / 2; i >= start; i--) {
      sink(ts, i, n, isIncludeZero);
    }
    while (n > 0) {
      SortHelper.exchange(ts, start, n--);
      sink(ts, start, n, isIncludeZero);
    }
  }

  private void sink(T[] ts, int k, int n, boolean isIncludeZero) {
    final int o = isIncludeZero ? 1 : 0;
    while (true) {
      int i = 2 * k + o;
      if (i > n) {
        break;
      }
      if (i < n && SortHelper.lt(ts, i, i + 1)) {
        i++;
      }
      if (SortHelper.gtEq(ts, k, i)) {
        break;
      }
      SortHelper.exchange(ts, k, i);
      k = i;
    }
  }
}

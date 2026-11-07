package pxf.toolkit.basic.lang.sort;

import javax.annotation.Nonnull;

/**
 * 希尔排序
 *
 * @author potatoxf
 * @date 2020/12/15
 */
public class ShellArraySortEngine<T extends Comparable<T>> extends AbstractArraySortEngine<T> {

  private final int factor;

  public ShellArraySortEngine() {
    this(3);
  }

  public ShellArraySortEngine(int factor) {
    this.factor = factor;
  }

  @Override
  protected void sort(@Nonnull final T[] ts, final int start, final int end, final int length) {
    int gap = 1;
    while (gap < length / factor) {
      gap = factor * gap + 1;
    }
    while (gap > 0) {
      for (int i = start + gap; i < end; i++) {
        T key = ts[i];
        int j = i - gap;
        while (j >= start && SortHelper.lt(key, ts[j])) {
          ts[j + gap] = ts[j];
          j -= gap;
        }
        ts[j + gap] = key;
      }
      gap /= factor;
    }
  }
}

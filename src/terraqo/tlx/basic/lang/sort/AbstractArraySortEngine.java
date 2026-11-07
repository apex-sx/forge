package pxf.toolkit.basic.lang.sort;

import javax.annotation.Nonnull;

/**
 * @author potatoxf
 * @date 2020/12/15
 */
public abstract class AbstractArraySortEngine<T extends Comparable<T>>
    implements ArraySortEngine<T> {

  /**
   * 排序数组
   *
   * @param ts 数组
   */
  @Override
  public final void sort(@Nonnull T[] ts) {
    check(ts);
    sort(ts, 0, ts.length);
  }

  /**
   * 排序数组
   *
   * @param ts 数组
   * @param start 起始索引
   * @param end 结束索引（不包括）
   * @throws java.lang.IllegalArgumentException 索引必须 {@code
   *     start>=0&&start<=end&&end<length}，否则抛出该异常
   */
  @Override
  public final void sort(T[] ts, int start, int end) {
    check(ts, start, end);
    sort(ts, start, end, end - start);
  }

  /**
   * 排序数组
   *
   * @param ts 数组
   * @param start 起始索引
   * @param end 结束索引（不包括）
   * @param length 排序总长度
   */
  protected abstract void sort(
      @Nonnull final T[] ts, final int start, final int end, final int length);

  private void check(T[] ts, int start, int end) {
    check(ts);
    if (start < 0 || start > end || end > ts.length) {
      throw new IllegalArgumentException(
          "The start index must be smaller than the end index, and must be a legal index");
    }
  }

  private void check(T[] ts) {
    if (ts == null) {
      throw new IllegalArgumentException("The array must not be null");
    }
  }
}

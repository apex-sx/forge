package pxf.toolkit.basic.lang.sort;

import javax.annotation.Nonnull;

/**
 * 冒泡排序
 *
 * @author potatoxf
 * @date 2020/12/15
 */
public class BubbleArraySortEngine<T extends Comparable<T>> extends AbstractArraySortEngine<T> {

  @Override
  protected void sort(@Nonnull final T[] ts, final int start, final int end, final int length) {
    // 需要交换次数
    for (int i = 0; i < length - 1; i++) {
      boolean swaped = false;
      // 相邻依次比较
      for (int j = start; j < length - 1 - i; j++) {
        if (SortHelper.gt(ts, j, j + 1)) {
          SortHelper.exchange(ts, j, j + 1);
          swaped = true;
        }
      }
      if (!swaped) {
        break;
      }
    }
  }
}

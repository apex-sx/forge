package pxf.toolkit.basic.lang.sort;

import javax.annotation.Nonnull;
import pxf.toolkit.basic.util.New;

/**
 * 归并排序
 *
 * @author potatoxf
 * @date 2020/12/15
 */
public class MergeArraySortEngine<T extends Comparable<T>> extends AbstractArraySortEngine<T> {

  private final ThreadLocal<Info<T>> infoThreadLocal = new ThreadLocal<>();

  @Override
  protected void sort(@Nonnull final T[] ts, final int start, final int end, final int length) {
    Info<T> info = new Info<T>(ts, New.array(ts, end - start));
    infoThreadLocal.set(info);
    sort(start, end - 1);
    infoThreadLocal.remove();
  }

  private void sort(int l, int r) {
    if (l >= r) {
      return;
    }
    int mid = l + (r - l) / 2;
    sort(l, mid);
    sort(mid + 1, r);
    merge(l, mid, r);
  }

  private void merge(final int l, final int mid, final int r) {
    Info<T> info = infoThreadLocal.get();
    int length = r - l + 1;
    int i = l;
    int j = mid + 1;
    int k = 0;
    while (i <= mid && j <= r) {
      if (SortHelper.lt(info.ts, i, j)) {
        info.tempArr[k + l] = info.ts[i];
        k++;
        i++;
      } else {
        info.tempArr[k + l] = info.ts[j];
        k++;
        j++;
      }
    }
    while (i <= mid) {
      info.tempArr[k + l] = info.ts[i];
      k++;
      i++;
    }
    while (j <= r) {
      info.tempArr[k + l] = info.ts[j];
      k++;
      j++;
    }
    System.arraycopy(info.tempArr, l, info.ts, l, length);
  }

  private static class Info<T> {

    final T[] ts;
    final T[] tempArr;

    private Info(T[] ts, T[] tempArr) {
      this.ts = ts;
      this.tempArr = tempArr;
    }
  }
}

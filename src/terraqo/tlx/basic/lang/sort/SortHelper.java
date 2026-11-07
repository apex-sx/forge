package pxf.toolkit.basic.lang.sort;

/**
 * 排序助手
 *
 * @author potatoxf
 * @date 2020/12/15
 */
final class SortHelper {

  private SortHelper() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  public static <T> void exchange(T[] arr, int i1, int i2) {
    T temp = arr[i1];
    arr[i1] = arr[i2];
    arr[i2] = temp;
  }

  public static <T extends Comparable<T>> boolean lt(T[] arr, int i1, int i2) {
    return lt(arr[i1], arr[i2]);
  }

  public static <T extends Comparable<T>> boolean lt(T a1, T a2) {
    return a1.compareTo(a2) < 0;
  }

  public static <T extends Comparable<T>> boolean ltEq(T[] arr, int i1, int i2) {
    return ltEq(arr[i1], arr[i2]);
  }

  public static <T extends Comparable<T>> boolean ltEq(T a1, T a2) {
    return a1.compareTo(a2) <= 0;
  }

  public static <T extends Comparable<T>> boolean gt(T[] arr, int i1, int i2) {
    return gt(arr[i1], arr[i2]);
  }

  public static <T extends Comparable<T>> boolean gt(T a1, T a2) {
    return a1.compareTo(a2) > 0;
  }

  public static <T extends Comparable<T>> boolean gtEq(T[] arr, int i1, int i2) {
    return gtEq(arr[i1], arr[i2]);
  }

  public static <T extends Comparable<T>> boolean gtEq(T a1, T a2) {
    return a1.compareTo(a2) >= 0;
  }
}

package pxf.toolkit.basic.util;

/**
 * 比较操作
 *
 * @author potatoxf
 * @date 2021/3/26
 */
public class Compare {

  private Compare() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Character#compare(char, char)
   */
  public static int value(char x, char y) {
    return x - y;
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Double#compare(double, double)
   */
  public static int value(double x, double y) {
    return Double.compare(x, y);
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Integer#compare(int, int)
   */
  public static int value(int x, int y) {
    return Integer.compare(x, y);
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Long#compare(long, long)
   */
  public static int value(long x, long y) {
    return Long.compare(x, y);
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Short#compare(short, short)
   */
  public static int value(short x, short y) {
    return Short.compare(x, y);
  }

  /**
   * 比较两个值的大小
   *
   * @param x 第一个值
   * @param y 第二个值
   * @return x==y返回0，x&lt;y返回-1，x&gt;y返回1
   * @see Byte#compare(byte, byte)
   */
  public static int value(byte x, byte y) {
    return Byte.compare(x, y);
  }

  /**
   * 优先比较空
   *
   * @param main 主对象
   * @param other 另一个对象
   * @return 当主对象和另一个都为 {@code null}，返回0，
   *     <p>主对象为 {@code null}返回 {@code -1}，
   *     <p>另一个对象为 {@code null}返回 {@code 1}，
   *     <p>否则返回 {@code null}
   */
  public static Integer objectOrComparableForPriorityNull(Object main, Object other) {
    return objectOrComparable(main, other, false);
  }

  /**
   * 对象比较
   *
   * @param main 对象1，可以为{@code null}
   * @param other 对象2，可以为{@code null}
   * @param isNullGreater 当被比较对象为null时是否排在前面
   * @return 比较结果，如果c1小于c2，返回数小于0，c1==c2返回0，c1大于c2 大于0，
   * @see java.util.Comparator#compare(Object, Object)
   */
  public static Integer objectOrComparable(Object main, Object other, boolean isNullGreater) {
    if (main == null && other == null) {
      return 0;
    } else if (main == null) {
      return isNullGreater ? 1 : -1;
    } else if (other == null) {
      return isNullGreater ? -1 : 1;
    } else if (main == other) {
      return 0;
    } else if (main instanceof Comparable && other instanceof Comparable) {
      return ((Comparable) main).compareTo(other);
    } else if (main.equals(other)) {
      return 0;
    }
    return null;
  }
}

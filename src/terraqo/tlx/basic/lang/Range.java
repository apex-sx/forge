package pxf.toolkit.basic.lang;

import java.util.Iterator;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 范围
 *
 * <p>描述了范围，它的最小值为0，最大值为{@code Integer.MAX_VALUE}。
 *
 * <p>这个范围只是描述了上下限，但这里的上下限是否可以取值由使用者来保证。
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public class Range implements Comparable<Range>, Cloneable, Iterable<Integer> {

  private static final int MIN = Integer.MIN_VALUE;
  private static final int MAX = Integer.MAX_VALUE;
  private final int lower;
  private final int upper;

  protected Range(@Nullable Integer min, @Nullable Integer max) {
    int lower = min == null ? MIN : min;
    int upper = max == null ? MAX : max;
    if (lower > upper) {
      throw new IllegalArgumentException("The max must greater than min");
    }
    this.lower = lower;
    this.upper = upper;
  }

  /**
   * 构造大于某个值的范围
   *
   * <p>最大值是无限的，但是由于计算机限制最大值是{@code StringRange.MAX}
   *
   * @param value 最大的值
   * @return {@code StringRange}
   * @throws IllegalArgumentException 如果lo小于{@code StringRange.MIN}
   */
  @Nonnull
  public static Range gt(int value) {
    return new Range(value, null);
  }

  /**
   * 构造小于某个值的范围
   *
   * <p>最小值是{@code StringRange.MIN}
   *
   * @param value 最大的值
   * @return {@code StringRange}
   * @throws IllegalArgumentException 如果lo小于{@code StringRange.MIN}
   */
  @Nonnull
  public static Range lt(int value) {
    return new Range(null, value);
  }

  /**
   * 构造上下限范围
   *
   * @param lo 下限
   * @param hi 上限
   * @return {@code StringRange}
   * @throws IllegalArgumentException 如果lo小于{@code StringRange.MIN}或者lo大于hi
   */
  @Nonnull
  public static Range of(int lo, int hi) {
    return new Range(lo, hi);
  }

  /**
   * 是否在范围内
   *
   * @param value 判断的值
   * @return true在范围内，否则false
   */
  public boolean isInRange(int value) {
    return value >= lower && value < upper;
  }

  /**
   * 求返回交集
   *
   * @param other 其它范围
   * @return {@code Range}
   */
  @Nonnull
  public Range and(Range other) {
    if (other.lower >= upper) {
      return new Range(-1, -1);
    }
    return new Range(Math.max(lower, other.lower), Math.min(upper, other.upper));
  }

  /**
   * 求返回并集
   *
   * @param other 其它范围
   * @return {@code Range}
   */
  @Nonnull
  public Range or(Range other) {
    return new Range(Math.min(lower, other.lower), Math.max(upper, other.upper));
  }

  @Override
  public int hashCode() {
    return Objects.hash(lower, upper);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Range that = (Range) o;
    return lower == that.lower && upper == that.upper;
  }

  @Override
  public int compareTo(final Range o) {
    // 这个范围完全在另一个范围前面
    if (upper <= o.lower) {
      return -1;
    }
    // 这个范围完全在另一个范围后面
    if (lower >= o.upper) {
      return 1;
    }
    // 相等
    if (lower == o.lower && upper == o.upper) {
      return 0;
    }
    int thisRange = upper - lower;
    int otherRange = o.upper - o.lower;
    int u, ou;
    // 确定小范围上限
    if (thisRange < otherRange) {
      u = upper;
      ou = o.upper;
    } else {
      u = o.upper;
      ou = upper;
    }
    // 当一个范围小的上限大于另一个范围大的上限时则这个小范围优先级靠后
    return u >= ou ? 1 : -1;
  }

  /**
   * Returns an iterator over elements of type {@code T}.
   *
   * @return an Iterator.
   */
  @Nonnull
  @Override
  public Iterator<Integer> iterator() {
    return new NumberIterator(lower, upper);
  }

  private static class NumberIterator implements Iterator<Integer> {

    private final int hi;
    private int lo;

    private NumberIterator(int lo, int hi) {
      this.lo = lo;
      this.hi = hi;
    }

    @Override
    public boolean hasNext() {
      return lo < hi;
    }

    @Override
    public Integer next() {
      return lo++;
    }
  }
}

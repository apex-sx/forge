package pxf.toolkit.basic.unit.impl;

import java.util.Iterator;
import pxf.toolkit.basic.lang.iterators.EnumIterator;

/**
 * 非等距单位接口
 *
 * @author potatoxf
 * @date 2021/3/13
 */
@SuppressWarnings("unchecked")
public interface NonIsometric<E extends Enum<E> & NonIsometric<E>>
    extends Isometric<E>, Iterable<E> {

  /**
   * 该单位与下一个大单位的间隔，等级是越来越高
   *
   * @return 与下一个大单位间隔数值
   */
  int nextInterval();

  /**
   * 获取枚举迭代器
   *
   * @return {@code Iterator<E>}
   */
  @Override
  default Iterator<E> iterator() {
    try {
      return new EnumIterator<>((Class<E>) getClass());
    } catch (Exception e) {
      throw new RuntimeException(
          "The enumeration interface does not use itself as a parent generic parameter", e);
    }
  }

  /**
   * 与目标的差值
   *
   * @param target {@code UnitEquallySpaced}
   * @return 返回数字等级
   */
  @Override
  default int exactFactor(final E target) {
    final int diff = poorGrade(target);
    int value = 1;
    if (diff == 0) {
      return value;
    }
    int bigUnit;
    int smallUnit;
    if (diff > 0) {
      bigUnit = grade();
      smallUnit = target.grade();
    } else {
      bigUnit = target.grade();
      smallUnit = grade();
    }
    for (E e : this) {
      boolean ltBigUnit = e.grade() < bigUnit;
      boolean gtSmallUnit = e.grade() >= smallUnit;
      if (gtSmallUnit && ltBigUnit) {
        value *= e.nextInterval();
      }
    }
    return value;
  }

  /**
   * 精确间隔默认是与下一位的间隔
   *
   * @return 间隔数值
   * @see #nextInterval()
   */
  @Override
  default int exactInterval() {
    return nextInterval();
  }
}

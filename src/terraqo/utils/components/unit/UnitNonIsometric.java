/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.unit;

/**
 * 枚举差值
 *
 * @author : Alfred X Fu
 * @date : 2020/2/23 17:55
 */
public interface UnitNonIsometric<E extends UnitNonIsometric<E>> extends UnitIsometric<E>,
    Iterable<E> {

  /**
   * 精确间隔默认是与下一位的间隔
   *
   * @return 间隔数值
   */
  @Override
  default int exactInterval() {
    return this.next();
  }

  /**
   * 与目标的差值
   *
   * @param target {@code UnitEquallySpaced}
   * @return 返回数字等级
   */
  @Override
  default int exactIntervalDifference(final E target) {
    final int diff = levelDifference(target);
    int value = 1;
    if (diff == 0) {
      return value;
    }
    int bigUnit;
    int smallUnit;
    if (diff > 0) {
      bigUnit = this.level();
      smallUnit = target.level();
    } else {
      bigUnit = target.level();
      smallUnit = this.level();
    }
    for (final E e : this) {
      boolean ltBigUnit = e.level() < bigUnit;
      boolean gtSmallUnit = e.level() >= smallUnit;
      if (gtSmallUnit && ltBigUnit) {
        value *= e.next();
      }
    }
    return value;
  }

  /**
   * 与目标的差值
   *
   * @param target {@code UnitEquallySpaced}
   * @return 返回数字等级
   */
  @Override
  default int humanIntervalDifference(final E target) {
    return this.exactIntervalDifference(target);
  }

  /**
   * 该单位与下一个单位的间隔，等级越来越高
   *
   * @return 间隔数值
   */
  int next();
}

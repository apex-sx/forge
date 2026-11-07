/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.unit;

/**
 * 等距间隔
 *
 * @author : Alfred X Fu
 * @date : 2020/2/23 17:55
 */
public interface UnitIsometric<E extends UnitIsometric<E>> {

  /**
   * 获取等级，从小到大依次增加
   *
   * @return 返回数字等级
   */
  int level();

  /**
   * 精确间隔
   *
   * @return 返回相邻从小单位到大单位的倍数
   */
  int exactInterval();

  /**
   * 与目标的差值
   *
   * @param target {@code UnitEquallySpaced}
   * @return 返回数字等级
   */
  default int levelDifference(final E target) {
    return this.level() - target.level();
  }

  /**
   * 间隔，默认情况等于精确间隔
   *
   * @return 返回相邻从小单位到大单位的倍数
   */
  default int humanInterval() {
    return this.exactInterval();
  }

  /**
   * 与目标的差值
   *
   * @param target {@code UnitEquallySpaced}
   * @return 返回数字等级
   */
  default int exactIntervalDifference(final E target) {
    final int diff = this.levelDifference(target);
    final int interval = this.exactInterval();
    if (diff > 0) {
      return (int) Math.pow(interval, diff);
    }
    if (diff < 0) {
      return (int) Math.pow(interval, -diff);
    }
    return 1;
  }

  /**
   * 与目标的差值
   *
   * @param target {@code UnitEquallySpaced}
   * @return 返回数字等级
   */
  default int humanIntervalDifference(final E target) {
    final int diff = this.levelDifference(target);
    final int interval = this.humanInterval();
    if (diff > 0) {
      return (int) Math.pow(interval, diff);
    }
    if (diff < 0) {
      return (int) Math.pow(interval, -diff);
    }
    return 1;
  }
}

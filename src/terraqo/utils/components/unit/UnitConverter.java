/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.unit;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 单位转换接口
 *
 * @author : Alfred X Fu
 * @date : 2020/2/23 17:53
 */
public interface UnitConverter<E extends UnitIsometric<E>> {

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default int humanConvert(final int value, final E targetUnit) {
    return this.exactConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  int exactConvert(int value, E targetUnit);

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default long humanConvert(final long value, final E targetUnit) {
    return this.exactConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  long exactConvert(long value, E targetUnit);

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default double humanConvert(final double value, final E targetUnit) {
    return this.exactConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  double exactConvert(double value, E targetUnit);

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default BigInteger humanConvert(final BigInteger value, final E targetUnit) {
    return this.exactConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  BigInteger exactConvert(BigInteger value, E targetUnit);

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default BigDecimal humanConvert(final BigDecimal value, final E targetUnit) {
    return this.exactConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  BigDecimal exactConvert(BigDecimal value, E targetUnit);
}

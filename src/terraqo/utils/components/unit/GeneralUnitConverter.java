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
import java.math.RoundingMode;

/**
 * 通用单位转换器
 *
 * @author : Alfred X Fu
 * @date : 2020/2/24 12:11
 */
public class GeneralUnitConverter<E extends UnitIsometric<E>> implements UnitConverter<E> {

  private final E originUnit;

  public GeneralUnitConverter(final E originUnit) {
    this.originUnit = originUnit;
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public int humanConvert(final int value, final E targetUnit) {
    return doIntConvert(value, targetUnit, this.originUnit.humanIntervalDifference(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public int exactConvert(final int value, final E targetUnit) {
    return doIntConvert(value, targetUnit, this.originUnit.exactIntervalDifference(targetUnit));
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public long humanConvert(final long value, final E targetUnit) {
    return doLongConvert(value, targetUnit, this.originUnit.humanIntervalDifference(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public long exactConvert(final long value, final E targetUnit) {
    return doLongConvert(value, targetUnit, this.originUnit.exactIntervalDifference(targetUnit));
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public double humanConvert(final double value, final E targetUnit) {
    return doDoubleConvert(value, targetUnit, this.originUnit.humanIntervalDifference(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public double exactConvert(final double value, final E targetUnit) {
    return doDoubleConvert(value, targetUnit, this.originUnit.exactIntervalDifference(targetUnit));
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigInteger humanConvert(final BigInteger value, final E targetUnit) {
    return doBigIntegerConvert(value, targetUnit, this.originUnit.humanIntervalDifference(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigInteger exactConvert(final BigInteger value, final E targetUnit) {
    return doBigIntegerConvert(value, targetUnit, this.originUnit.exactIntervalDifference(targetUnit));
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigDecimal humanConvert(final BigDecimal value, final E targetUnit) {
    return doBigDecimalConvert(value, targetUnit, this.originUnit.humanIntervalDifference(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigDecimal exactConvert(final BigDecimal value, final E targetUnit) {
    return doBigDecimalConvert(value, targetUnit, this.originUnit.exactIntervalDifference(targetUnit));
  }

  private int doIntConvert(int value, E targetUnit, int interval) {
    final int diff = this.originUnit.levelDifference(targetUnit);
    if (diff < 0) {
      return Math.floorDiv(value, interval);
    } else if (diff > 0) {
      return Math.multiplyExact(value, interval);
    }
    return value;
  }

  private long doLongConvert(long value, E targetUnit, int interval) {
    final int diff = this.originUnit.levelDifference(targetUnit);
    if (diff < 0) {
      return Math.floorDiv(value, interval);
    } else if (diff > 0) {
      return Math.multiplyExact(value, interval);
    }
    return value;
  }

  private double doDoubleConvert(double value, E targetUnit, int interval) {
    final int diff = this.originUnit.levelDifference(targetUnit);
    if (diff < 0) {
      return value / interval;
    } else if (diff > 0) {
      return value * interval;
    }
    return value;
  }

  private BigInteger doBigIntegerConvert(BigInteger value, E targetUnit, int interval) {
    final int diff = this.originUnit.levelDifference(targetUnit);
    if (diff < 0) {
      return value.divide(new BigInteger(String.valueOf(interval)));
    } else if (diff > 0) {
      return value.multiply(new BigInteger(String.valueOf(interval)));
    }
    return value;
  }

  private BigDecimal doBigDecimalConvert(BigDecimal value, E targetUnit, int interval) {
    final int diff = this.originUnit.levelDifference(targetUnit);
    if (diff < 0) {
      return value.divide(new BigDecimal(interval), 2, RoundingMode.HALF_UP);
    } else if (diff > 0) {
      return value.multiply(new BigDecimal(interval));
    }
    return value;
  }
}

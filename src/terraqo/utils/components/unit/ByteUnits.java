/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.unit;

import axf.java.common.utils.function.Constants;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 字节单位
 *
 * @author : Alfred X Fu
 * @date : 2020/2/21 15:03
 */
public enum ByteUnits implements UnitIsometric<ByteUnits>, UnitConverter<ByteUnits>, Constants<ByteUnits> {
  /**
   * 字节
   */
  B("byte", "bytes"),
  /**
   * 千字节
   */
  KB(),
  /**
   * 兆字节
   */
  MB(),
  /**
   * GB
   */
  GB(),
  /**
   * TB
   */
  TB(),
  /**
   * PB
   */
  PB();
  private final GeneralUnitConverter<ByteUnits> converter = new GeneralUnitConverter<>(this);
  private final String[] aliases;

  ByteUnits(String... aliases) {
    this.aliases = aliases;
  }

  /**
   * 获取等级
   *
   * @return 返回数字等级
   */
  @Override
  public int level() {
    return this.ordinal();
  }

  /**
   * 精确间隔
   *
   * @return 返回相邻从小单位到大单位的倍数
   */
  @Override
  public int exactInterval() {
    return 1024;
  }

  /**
   * 间隔，默认情况等于精确间隔
   *
   * @return 返回相邻从小单位到大单位的倍数
   */
  @Override
  public int humanInterval() {
    return 1000;
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public int humanConvert(final int value, final ByteUnits targetUnit) {
    return this.converter.humanConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public int exactConvert(final int value, final ByteUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public long humanConvert(final long value, final ByteUnits targetUnit) {
    return this.converter.humanConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public long exactConvert(final long value, final ByteUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public double humanConvert(final double value, final ByteUnits targetUnit) {
    return this.converter.humanConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public double exactConvert(final double value, final ByteUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigInteger humanConvert(final BigInteger value, final ByteUnits targetUnit) {
    return this.converter.humanConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigInteger exactConvert(final BigInteger value, final ByteUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  /**
   * 人类易懂转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigDecimal humanConvert(final BigDecimal value, final ByteUnits targetUnit) {
    return this.converter.humanConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value      值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigDecimal exactConvert(final BigDecimal value, final ByteUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  /**
   * 自己
   *
   * @return {@code <E extends Enum<E> & Constants<E>>}
   */
  @Override
  public ByteUnits self() {
    return this;
  }

  /**
   * 常量名称别名
   *
   * @return 返回该常量名称别名
   */
  @Override
  public String[] aliases() {
    return aliases;
  }
}

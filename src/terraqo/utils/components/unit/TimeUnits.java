/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.unit;

import com.utils.UtilsOfFunction;
import com.utils.UtilsOfJudge;
import axf.java.common.utils.function.Constants;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * 时间单位
 *
 * @author : Alfred X Fu
 * @date : 2020/2/29 12:40
 */
public enum TimeUnits implements UnitNonIsometric<TimeUnits>, UnitConverter<TimeUnits>, Constants<TimeUnits> {
  /**
   * 纳秒
   */
  NS(TimeUnit.NANOSECONDS, ChronoUnit.NANOS, 1000),
  /**
   * 微妙
   */
  MMS(TimeUnit.MICROSECONDS, ChronoUnit.MICROS, 1000),
  /**
   * 毫秒
   */
  MS(TimeUnit.MILLISECONDS, ChronoUnit.MILLENNIA, 1000),
  /**
   * 秒
   */
  SEC(TimeUnit.SECONDS, ChronoUnit.SECONDS, 60),
  /**
   * 分钟
   */
  MIN(TimeUnit.MINUTES, ChronoUnit.MINUTES, 60),
  /**
   * 小时
   */
  HOUR(TimeUnit.HOURS, ChronoUnit.HOURS, 24),
  /**
   * 天
   */
  DAY(TimeUnit.DAYS, ChronoUnit.DAYS, -1);

  private final GeneralUnitConverter<TimeUnits> converter = new GeneralUnitConverter<>(this);
  private final TimeUnit timeUnit;
  private final ChronoUnit chronoUnit;
  private final int next;

  TimeUnits(final TimeUnit timeUnit, ChronoUnit chronoUnit, final int next) {
    this.timeUnit = timeUnit;
    this.chronoUnit = chronoUnit;
    this.next = next;
  }

  public static TimeUnits from(final TimeUnit timeUnit) {
    TimeUnits found = UtilsOfFunction
        .findFirstValue(TimeUnits.values(), o -> ((TimeUnits) o).timeUnit.equals(timeUnit));
    if (UtilsOfJudge.isNull(found)) {
      throw new IllegalArgumentException(
          "No TimeUnit equivalent for " + timeUnit);
    }
    return found;
  }

  public static TimeUnits from(ChronoUnit chronoUnit) {
    TimeUnits found = UtilsOfFunction
        .findFirstValue(TimeUnits.values(), o -> ((TimeUnits) o).chronoUnit.equals(chronoUnit));
    if (UtilsOfJudge.isNull(found)) {
      throw new IllegalArgumentException(
          "No TimeUnit equivalent for " + chronoUnit);
    }
    return found;
  }

  @Override
  public int humanConvert(final int value, final TimeUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  @Override
  public int exactConvert(final int value, final TimeUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  @Override
  public long humanConvert(final long value, final TimeUnits targetUnit) {
    return this.converter.humanConvert(value, targetUnit);
  }

  @Override
  public long exactConvert(final long value, final TimeUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  @Override
  public double humanConvert(final double value, final TimeUnits targetUnit) {
    return this.converter.humanConvert(value, targetUnit);
  }

  @Override
  public double exactConvert(final double value, final TimeUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  @Override
  public BigInteger humanConvert(final BigInteger value, final TimeUnits targetUnit) {
    return this.converter.humanConvert(value, targetUnit);
  }

  @Override
  public BigInteger exactConvert(final BigInteger value, final TimeUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  @Override
  public BigDecimal humanConvert(final BigDecimal value, final TimeUnits targetUnit) {
    return this.converter.humanConvert(value, targetUnit);
  }

  @Override
  public BigDecimal exactConvert(final BigDecimal value, final TimeUnits targetUnit) {
    return this.converter.exactConvert(value, targetUnit);
  }

  public final TimeUnit toTimeUnit() {
    return this.timeUnit;
  }

  public final ChronoUnit toChronoUnit() {
    return this.chronoUnit;
  }

  @Override
  public Iterator<TimeUnits> iterator() {
    return new OfIterator();
  }

  @Override
  public int level() {
    return this.ordinal();
  }

  @Override
  public int next() {
    return this.next;
  }

  @Override
  public TimeUnits self() {
    return this;
  }

  private static class OfIterator implements Iterator<TimeUnits> {

    private final TimeUnits[] timeUnits = TimeUnits.values();
    private int i = 0;

    @Override
    public boolean hasNext() {
      return this.i < this.timeUnits.length;
    }

    @Override
    public TimeUnits next() {
      return this.timeUnits[this.i++];
    }
  }
}

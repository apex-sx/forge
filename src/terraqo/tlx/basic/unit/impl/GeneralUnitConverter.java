package pxf.toolkit.basic.unit.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * 通用单位转换器
 *
 * @author potatoxf
 * @date 2021/3/13
 */
class GeneralUnitConverter<E extends Enum<E> & Isometric<E>> implements UnitConverter<E> {

  private final E originUnit;

  GeneralUnitConverter(E originUnit) {
    this.originUnit = originUnit;
  }

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public int humanConvert(int value, E targetUnit) {
    return doIntConvert(value, targetUnit, originUnit.humanFactor(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public int exactConvert(int value, E targetUnit) {
    return doIntConvert(value, targetUnit, originUnit.exactFactor(targetUnit));
  }

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public long humanConvert(long value, E targetUnit) {
    return doLongConvert(value, targetUnit, originUnit.humanFactor(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public long exactConvert(long value, E targetUnit) {
    return doLongConvert(value, targetUnit, originUnit.exactFactor(targetUnit));
  }

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public double humanConvert(double value, E targetUnit) {
    return doDoubleConvert(value, targetUnit, originUnit.humanFactor(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public double exactConvert(double value, E targetUnit) {
    return doDoubleConvert(value, targetUnit, originUnit.exactFactor(targetUnit));
  }

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigInteger humanConvert(BigInteger value, E targetUnit) {
    return doBigIntegerConvert(value, targetUnit, originUnit.humanFactor(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigInteger exactConvert(BigInteger value, E targetUnit) {
    return doBigIntegerConvert(value, targetUnit, originUnit.exactFactor(targetUnit));
  }

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigDecimal humanConvert(BigDecimal value, E targetUnit) {
    return doBigDecimalConvert(value, targetUnit, originUnit.humanFactor(targetUnit));
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  public BigDecimal exactConvert(BigDecimal value, E targetUnit) {
    return doBigDecimalConvert(value, targetUnit, originUnit.exactFactor(targetUnit));
  }

  /**
   * 处理转换
   *
   * @param value 原来值
   * @param targetUnit 目标值
   * @param interval 转换间隔
   * @return 返回转换之后的值
   */
  private int doIntConvert(int value, E targetUnit, int interval) {
    int diff = originUnit.poorGrade(targetUnit);
    if (diff < 0) {
      return Math.floorDiv(value, interval);
    } else if (diff > 0) {
      return Math.multiplyExact(value, interval);
    }
    return value;
  }

  /**
   * 处理转换
   *
   * @param value 原来值
   * @param targetUnit 目标值
   * @param interval 转换间隔
   * @return 返回转换之后的值
   */
  private long doLongConvert(long value, E targetUnit, int interval) {
    int diff = originUnit.poorGrade(targetUnit);
    if (diff < 0) {
      return Math.floorDiv(value, interval);
    } else if (diff > 0) {
      return Math.multiplyExact(value, interval);
    }
    return value;
  }

  /**
   * 处理转换
   *
   * @param value 原来值
   * @param targetUnit 目标值
   * @param interval 转换间隔
   * @return 返回转换之后的值
   */
  private double doDoubleConvert(double value, E targetUnit, int interval) {
    int diff = originUnit.poorGrade(targetUnit);
    if (diff < 0) {
      return value / interval;
    } else if (diff > 0) {
      return value * interval;
    }
    return value;
  }

  /**
   * 处理转换
   *
   * @param value 原来值
   * @param targetUnit 目标值
   * @param interval 转换间隔
   * @return 返回转换之后的值
   */
  private BigInteger doBigIntegerConvert(BigInteger value, E targetUnit, int interval) {
    int diff = originUnit.poorGrade(targetUnit);
    if (diff < 0) {
      return value.divide(new BigInteger(String.valueOf(interval)));
    } else if (diff > 0) {
      return value.multiply(new BigInteger(String.valueOf(interval)));
    }
    return value;
  }

  /**
   * 处理转换
   *
   * @param value 原来值
   * @param targetUnit 目标值
   * @param interval 转换间隔
   * @return 返回转换之后的值
   */
  private BigDecimal doBigDecimalConvert(BigDecimal value, E targetUnit, int interval) {
    int diff = originUnit.poorGrade(targetUnit);
    if (diff < 0) {
      return value.divide(new BigDecimal(interval), 2, RoundingMode.HALF_UP);
    } else if (diff > 0) {
      return value.multiply(new BigDecimal(interval));
    }
    return value;
  }
}

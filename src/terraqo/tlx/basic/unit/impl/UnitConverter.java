package pxf.toolkit.basic.unit.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 单位转换器
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public interface UnitConverter<E extends Enum<E> & Isometric<E>> {

  /**
   * 构建默认单位转换器
   *
   * @param unitEnum 单位枚举
   * @param <E> 枚举类型
   * @return {@code <E extends Enum<E> & Isometric<E>> UnitConverter<E>}
   */
  static <E extends Enum<E> & Isometric<E>> UnitConverter<E> of(E unitEnum) {
    return new GeneralUnitConverter<>(unitEnum);
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  int exactConvert(int value, E targetUnit);

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  long exactConvert(long value, E targetUnit);

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  double exactConvert(double value, E targetUnit);

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  BigInteger exactConvert(BigInteger value, E targetUnit);

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  BigDecimal exactConvert(BigDecimal value, E targetUnit);

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default int humanConvert(final int value, final E targetUnit) {
    return exactConvert(value, targetUnit);
  }

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default long humanConvert(final long value, final E targetUnit) {
    return exactConvert(value, targetUnit);
  }

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default double humanConvert(final double value, final E targetUnit) {
    return exactConvert(value, targetUnit);
  }

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default BigInteger humanConvert(final BigInteger value, final E targetUnit) {
    return exactConvert(value, targetUnit);
  }

  /**
   * 人类易懂转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  default BigDecimal humanConvert(final BigDecimal value, final E targetUnit) {
    return exactConvert(value, targetUnit);
  }
}

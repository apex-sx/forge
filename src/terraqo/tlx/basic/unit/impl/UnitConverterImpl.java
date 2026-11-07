package pxf.toolkit.basic.unit.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 单位转换器接口实现类
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public interface UnitConverterImpl<E extends Enum<E> & Isometric<E>> extends UnitConverter<E> {

  /**
   * 返回持有单位转换器
   *
   * @return {@code UnitConverter<E>}
   */
  UnitConverter<E> holdingUnitConverter();

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  default int exactConvert(int value, E targetUnit) {
    return holdingUnitConverter().exactConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  default long exactConvert(long value, E targetUnit) {
    return holdingUnitConverter().exactConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  default double exactConvert(double value, E targetUnit) {
    return holdingUnitConverter().exactConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  default BigInteger exactConvert(BigInteger value, E targetUnit) {
    return holdingUnitConverter().exactConvert(value, targetUnit);
  }

  /**
   * 精确转换
   *
   * @param value 值
   * @param targetUnit 目标单位
   * @return 返回目标单位的值
   */
  @Override
  default BigDecimal exactConvert(BigDecimal value, E targetUnit) {
    return holdingUnitConverter().exactConvert(value, targetUnit);
  }
}

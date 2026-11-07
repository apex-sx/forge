package pxf.toolkit.basic.unit;

import pxf.toolkit.basic.unit.impl.Isometric;
import pxf.toolkit.basic.unit.impl.UnitConverter;
import pxf.toolkit.basic.unit.impl.UnitConverterImpl;

/**
 * 字节单位
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public enum ByteUnits implements Isometric<ByteUnits>, UnitConverterImpl<ByteUnits> {
  /** 字节 */
  B,
  /** 千字节 */
  KB,
  /** 兆字节 */
  MB,
  /** GB */
  GB,
  /** TB */
  TB,
  /** PB */
  PB;
  private final UnitConverter<ByteUnits> converter = UnitConverter.of(this);

  public int getValue(int value) {
    return exactConvert(value, B);
  }

  public long getValue(long value) {
    return exactConvert(value, B);
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
   * 返回持有单位转换器
   *
   * @return {@code UnitConverter<E>}
   */
  @Override
  public UnitConverter<ByteUnits> holdingUnitConverter() {
    return converter;
  }
}

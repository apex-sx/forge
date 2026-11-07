package pxf.toolkit.basic.unit;

import pxf.toolkit.basic.unit.impl.NonIsometric;
import pxf.toolkit.basic.unit.impl.UnitConverter;
import pxf.toolkit.basic.unit.impl.UnitConverterImpl;

/**
 * 数字进位
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public enum NumberUnit implements NonIsometric<NumberUnit>, UnitConverterImpl<NumberUnit> {
  /** 万分比 */
  TEN_THOUSANDTHS(10),
  /** 千分比 */
  THOUSANDS(10),
  /** 百分比 */
  PERCENTAGE(100),
  /** 正常 */
  NORMAL(-1);
  private final UnitConverter<NumberUnit> converter = UnitConverter.of(this);
  private final int next;

  NumberUnit(int next) {
    this.next = next;
  }

  @Override
  public int nextInterval() {
    return next;
  }

  /**
   * 返回持有单位转换器
   *
   * @return {@code UnitConverter<E>}
   */
  @Override
  public UnitConverter<NumberUnit> holdingUnitConverter() {
    return converter;
  }
}

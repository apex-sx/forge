package pxf.toolkit.basic.unit;

import pxf.toolkit.basic.unit.impl.NonIsometric;
import pxf.toolkit.basic.unit.impl.UnitConverter;
import pxf.toolkit.basic.unit.impl.UnitConverterImpl;

/**
 * 时间周期转换
 *
 * @author potatoxf
 * @date 2021/3/13
 */
public enum TimeCycleUnit implements NonIsometric<TimeCycleUnit>, UnitConverterImpl<TimeCycleUnit> {
  /** 月 */
  MONTH(12),
  /** 年 */
  YEAR(-1);
  private final UnitConverter<TimeCycleUnit> converter = UnitConverter.of(this);
  private final int next;

  TimeCycleUnit(int next) {
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
  public UnitConverter<TimeCycleUnit> holdingUnitConverter() {
    return this;
  }
}

package pxf.toolkit.basic.constants.impl;

import pxf.toolkit.basic.constants.FindableEnumConstant;

/**
 * 布尔值
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public enum BooleanValue implements FindableEnumConstant<BooleanValue> {
  /** false */
  FALSE(0, "f", "no"),
  /** true */
  TRUE(1, "t", "yes");
  private final int value;
  private final String[] alias;

  BooleanValue(int value, String... alias) {
    this.value = value;
    this.alias = alias;
  }

  @Override
  public int identity() {
    return value;
  }

  @Override
  public String[] alias() {
    return alias;
  }
}

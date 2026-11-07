/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math;

import com.utils.UtilsOfNumber;
import java.math.RoundingMode;

/**
 * 精度
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public abstract class AbstractAccuracy {

  protected int accuracy = 2;

  public void setAccuracy(final int accuracy) {
    if (accuracy < 0) {
      throw new IllegalArgumentException("Don't allow less than 0!");
    }
    this.accuracy = accuracy;
  }

  /**
   * 格式化小数位
   *
   * @param f 数字
   * @return 返回格式化后的数字
   */
  protected float format(final float f) {
    return UtilsOfNumber.format(f, accuracy, RoundingMode.HALF_UP);
  }

  /**
   * 格式化小数位
   *
   * @param d 数字
   * @return 返回格式化后的数字
   */
  protected double format(final double d) {
    return UtilsOfNumber.format(d, accuracy, RoundingMode.HALF_UP);
  }

  /**
   * floatPlaceholder
   * <p>
   * 浮点数占位符
   *
   * @return 占位符符号
   */
  protected String fp() {
    return this.accuracy == 0 ? "%.f" : "%." + this.accuracy + "f";
  }
}

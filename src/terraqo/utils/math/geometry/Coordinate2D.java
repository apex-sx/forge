/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import axf.java.common.utils.math.AbstractAccuracy;
import java.util.Objects;

/**
 * 坐标
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public class Coordinate2D extends AbstractAccuracy implements Comparable<Coordinate2D> {

  /**
   * 原点
   */
  public static final Coordinate2D ORIGIN_POINT = new Coordinate2D(0f, 0f);
  /**
   * 默认以X,和Y轴为比较方式
   */
  public static Arrangement ARRANGEMENT = Arrangement.X_Y;
  /**
   * x轴坐标
   */
  private final float pX;
  /**
   * y轴坐标
   */
  private final float pY;

  public Coordinate2D(final float x, final float y) {
    this.pX = this.format(x);
    this.pY = this.format(y);
  }

  /**
   * 原点对称对称
   *
   * @return {@code Coordinate2D}对称后的坐标位置
   */
  public final Coordinate2D originSymmetry() {
    return new Coordinate2D(0.0F - this.pX, 0.0F - this.pY);
  }

  /**
   * x轴坐标
   *
   * @return x轴坐标位置
   */
  public final float x() {
    return this.pX;
  }

  /**
   * y轴坐标
   *
   * @return y轴坐标位置
   */
  public final float y() {
    return this.pY;
  }

  /**
   * x轴平移
   *
   * @param offset 平移距离
   * @return {@code Coordinate2D}平移后的坐标
   */
  public Coordinate2D xAxisTranslation(final float offset) {
    return new Coordinate2D(this.pX + offset, this.pY);
  }

  /**
   * x轴对称
   *
   * @return {@code Coordinate2D}对称后的坐标位置
   */
  public final Coordinate2D xAxisSymmetry() {
    return new Coordinate2D(0.0F - this.pX, -this.pY);
  }

  /**
   * y轴平移
   *
   * @param offset 平移距离
   * @return {@code Coordinate2D}平移后的坐标
   */
  public final Coordinate2D yAxisTranslation(final float offset) {
    return new Coordinate2D(this.pX, this.pY + offset);
  }

  /**
   * y轴对称
   *
   * @return {@code Coordinate2D}对称后的坐标位置
   */
  public final Coordinate2D yAxisSymmetry() {
    return new Coordinate2D(0.0F - this.pX, this.pY);
  }

  /**
   * 平移
   *
   * @param xOffset x轴平移距离
   * @param yOffset y轴平移距离
   * @return {@code Coordinate2D}平移后的坐标
   */
  public final Coordinate2D xyTranslation(final float xOffset, final float yOffset) {
    return new Coordinate2D(this.pX + xOffset, this.pY + yOffset);
  }

  /**
   * 计算与另一个点的终点
   *
   * @param other 另一个点
   * @return {@code Coordinate2D}新的坐标
   */
  public Coordinate2D midPointWithOther(Coordinate2D other) {
    float x = (this.pX + other.pX) / 2;
    float y = (this.pY + other.pY) / 2;
    return new Coordinate2D(x, y);
  }

  /**
   * 比较两个点
   *
   * @param o 另一个点
   * @return 返回负整数，零或正整数，因为此对象小于，等于或大于指定的对象
   */
  @Override
  public int compareTo(final Coordinate2D o) {
    switch (Coordinate2D.ARRANGEMENT) {
      case X:
        return this.xCompareTo(o);
      case Y:
        return this.yCompareTo(o);
      default: {
        break;
      }
    }
    return this.xyCompareTo(o);
  }

  private int xCompareTo(final Coordinate2D o) {
    return (int) (this.pX - o.pX);
  }

  private int yCompareTo(final Coordinate2D o) {
    return (int) (this.pY - o.pY);
  }

  /**
   * 比较两个点到原点的距离
   *
   * @param o 另一个点
   * @return 如果大于0则this>o，如果小于0则this<o，如果等于0则this=o
   */
  private int xyCompareTo(final Coordinate2D o) {
    final float distance1 = this.distance(Coordinate2D.ORIGIN_POINT);
    final float distance2 = o.distance(Coordinate2D.ORIGIN_POINT);
    return (int) (distance1 - distance2);
  }

  /**
   * 计算两点之间的距离
   *
   * @param other 另一个点的坐标
   * @return 距离值，该值一定会是正的
   * @throws ArithmeticException 当超出float范围时会抛出此异常
   */
  public float distance(final Coordinate2D other) {
    if (this.pX == other.pX) {
      final float result = Math.abs(this.pY - other.pY);
      if (!Float.isInfinite(result)) {
        return result;
      }
    } else if (this.pY == other.pY) {
      final float result = Math.abs(this.pX - other.pX);
      if (!Float.isInfinite(result)) {
        return result;
      }
    } else {
      final float lx = this.pX - other.pX;
      final float ly = this.pY - other.pY;
      final double dx = Math.pow(lx, 2);
      final double dy = Math.pow(ly, 2);
      final double dResult = Math.sqrt(dx + dy);
      final float fResult = (float) dResult;
      if (fResult == dResult) {
        return fResult;
      }
    }
    throw new ArithmeticException("Out of max key of float!");
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.pX, this.pY);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    final Coordinate2D that = (Coordinate2D) o;
    return Float.compare(that.pX, this.pX) == 0 && Float.compare(that.pY, this.pY) == 0;
  }

  @Override
  public String toString() {
    return String.format("(" + this.fp() + "," + this.fp() + ")", this.pX, this.pY);
  }

  /**
   * 排序方式
   */
  public enum Arrangement {
    X,
    Y,
    X_Y
  }
}

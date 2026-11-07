/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import axf.java.common.utils.UtilsOfAssert;
import axf.java.common.utils.UtilsOfString;
import axf.java.common.utils.math.AbstractAccuracy;
import java.util.Objects;

/**
 * 矩形结构
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public abstract class BaseRectangle extends AbstractAccuracy implements Rectangular {

  /**
   * 起点坐标
   */
  private final Coordinate2D startPoint;
  /**
   * 宽度
   */
  private final float totalWidth;
  /**
   * 高度
   */
  private final float totalHeight;
  /**
   * 间隙
   */
  private float gap = 0f;

  /**
   * @param startX      起点x轴坐标
   * @param startY      起点y轴坐标
   * @param totalWidth  宽度
   * @param totalHeight 高度
   * @throws ArithmeticException 当{@code totalWidth},{@code totalHeight}小于等于0时抛出该异常
   */
  public BaseRectangle(final float startX, final float startY, final float totalWidth,
      final float totalHeight) {
    this(new Coordinate2D(startX, startY), totalWidth, totalHeight);
  }

  /**
   * @param startPoint  起点坐标
   * @param totalWidth  宽度
   * @param totalHeight 高度
   * @throws ArithmeticException 当{@code totalWidth},{@code totalHeight}小于等于0时抛出该异常
   */
  public BaseRectangle(final Coordinate2D startPoint, final float totalWidth,
      final float totalHeight) {
    this.startPoint = Objects.requireNonNull(startPoint);
    UtilsOfAssert.checkPositive(totalWidth, "Weight must greater than 0");
    this.totalWidth = totalWidth;
    UtilsOfAssert.checkPositive(totalHeight, "Height must greater than 0");
    this.totalHeight = totalHeight;
  }

  /**
   * 设置间隙
   *
   * @param gap 间隙值
   */
  public void setGap(float gap) {
    this.gap = gap;
  }

  @Override
  public float width() {
    return this.totalWidth;
  }

  @Override
  public float height() {
    return this.totalHeight;
  }

  @Override
  public final Coordinate2D startPoint() {
    return this.startPoint;
  }

  /**
   * 单行文本书写起始点
   * <p>
   * 以xy轴正方向为书写方向
   *
   * @param placeholderDirection 占位方向
   * @param contentWidth         内容所暂用的宽度
   * @param contentHeight        内存所占用的高度
   * @return {@code Coordinate2D}返回书写起始点
   */
  @Override
  public final Coordinate2D singleLine(PlaceholderDirection placeholderDirection,
      float contentWidth,
      float contentHeight) {
    UtilsOfAssert.checkGreaterEqual(contentWidth, 0, "Width must greater equal 0");
    UtilsOfAssert.checkGreaterEqual(contentHeight, 0, "Height must greater equal 0");
    final float limitWidth = width();
    final float limitHeight = height();
    UtilsOfAssert.checkLesserEqual(contentWidth, limitWidth,
        "Width " + contentWidth + " of content out of limit width +" + limitWidth);
    UtilsOfAssert.checkLesserEqual(contentHeight, limitHeight,
        "Height " + contentHeight + " of content out of limit height +" + limitHeight);
    switch (placeholderDirection) {
      case TOP: {
        return computeTopLocation(contentWidth, contentHeight);
      }
      case BOTTOM: {
        return computeBottomLocation(contentWidth, contentHeight);
      }
      case LEFT: {
        return computeLeftLocation(contentWidth, contentHeight);
      }
      case RIGHT: {
        return computeRightLocation(contentWidth, contentHeight);
      }
      case LEFT_TOP: {
        return computeLeftTopLocation(contentWidth, contentHeight);
      }
      case RIGHT_TOP: {
        return computeRightTopLocation(contentWidth, contentHeight);
      }
      case LEFT_BOTTOM: {
        return computeLeftBottomLocation(contentWidth, contentHeight);
      }
      case RIGHT_BOTTOM: {
        return computeRightBottomLocation(contentWidth, contentHeight);
      }
      default: {
        return centerMidpoint().xAxisTranslation(-contentWidth / 2)
            .yAxisTranslation(-contentHeight / 2);
      }
    }
  }

  /**
   * 多行文本书写起始点
   * <p>
   * 以xy轴正方向为书写方向
   *
   * @param contentWidth  内容所暂用的宽度
   * @param contentHeight 内存所占用的高度
   * @return {@code Coordinate2D}返回每一行的书写起始点
   */
  @Override
  public final Coordinate2D[] multiplyLine(PlaceholderDirection placeholderDirection,
      float contentWidth, float contentHeight, float interval, int rows) {
    if (rows < 1) {
      rows = 1;
    }
    final float rowHeight = contentHeight + interval;
    final float totalHeight = rowHeight * rows;
    Coordinate2D coordinate2D = singleLine(placeholderDirection, contentWidth, totalHeight);
    Coordinate2D[] results = new Coordinate2D[rows];
    for (int i = 0; i < rows; i++) {
      results[i] = coordinate2D.yAxisTranslation((rows - i - 1) * rowHeight);
    }
    return results;
  }

  protected Coordinate2D computeLeftLocation(float contentWidth, float contentHeight) {
    float xOffset = computeLeftXOffset(contentWidth);
    float yOffset = computeCenterYOffset(contentHeight);
    return centerMidpoint().xyTranslation(xOffset, yOffset);
  }

  protected Coordinate2D computeBottomLocation(float contentWidth, float contentHeight) {
    float xOffset = computeCenterXOffset(contentWidth);
    float yOffset = computeBottomYOffset(contentHeight);
    return centerMidpoint().xyTranslation(xOffset, yOffset);
  }

  protected Coordinate2D computeRightLocation(float contentWidth, float contentHeight) {
    float xOffset = computeRightXOffset(contentWidth);
    float yOffset = computeCenterYOffset(contentHeight);
    return centerMidpoint().xyTranslation(xOffset, yOffset);
  }

  protected Coordinate2D computeTopLocation(float contentWidth, float contentHeight) {
    float xOffset = computeCenterXOffset(contentWidth);
    float yOffset = computeTopYOffset(contentHeight);
    return centerMidpoint().xyTranslation(xOffset, yOffset);
  }

  protected Coordinate2D computeLeftTopLocation(float contentWidth, float contentHeight) {
    float xOffset = computeLeftXOffset(contentWidth);
    float yOffset = computeTopYOffset(contentHeight);
    return centerMidpoint().xyTranslation(xOffset, yOffset);
  }

  protected Coordinate2D computeLeftBottomLocation(float contentWidth, float contentHeight) {
    float xOffset = computeLeftXOffset(contentWidth);
    float yOffset = computeBottomYOffset(contentHeight);
    return centerMidpoint().xyTranslation(xOffset, yOffset);
  }

  protected Coordinate2D computeRightTopLocation(float contentWidth, float contentHeight) {
    float xOffset = computeRightXOffset(contentWidth);
    float yOffset = computeTopYOffset(contentHeight);
    return centerMidpoint().xyTranslation(xOffset, yOffset);
  }

  protected Coordinate2D computeRightBottomLocation(float contentWidth, float contentHeight) {
    float xOffset = computeRightXOffset(contentWidth);
    float yOffset = computeBottomYOffset(contentHeight);
    return centerMidpoint().xyTranslation(xOffset, yOffset);
  }

  protected float computeCenterXOffset(float contentWidth) {
    return -contentWidth / 2;
  }

  protected float computeCenterYOffset(float contentHeight) {
    return -contentHeight / 2;
  }

  protected float computeLeftXOffset(float contentWidth) {
    return -width() / 2 + gap;
  }

  protected float computeBottomYOffset(float contentHeight) {
    return -height() / 2 + gap;
  }

  protected float computeRightXOffset(float contentWidth) {
    return width() / 2 - contentWidth - gap;
  }

  protected float computeTopYOffset(float contentHeight) {
    return height() / 2 - contentHeight - gap;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.startPoint, this.totalWidth, this.totalHeight);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    final BaseRectangle baseRectangle = (BaseRectangle) o;
    return Float.compare(baseRectangle.totalWidth, this.totalWidth) == 0
        && Float.compare(baseRectangle.totalHeight,
        this.totalHeight
    ) == 0 && this.startPoint.equals(baseRectangle.startPoint);
  }

  @Override
  public String toString() {
    String segment = UtilsOfString.repeat('=', 20);
    String split = UtilsOfString.repeat('-', 20);
    String s1 = String
        .format("start point:%s totalWidth:" + this.fp() + " totalHeight:" + this.fp(),
            this.startPoint, this.totalWidth, this.totalHeight);
    String s2 = String.format("%s %s%n%s %s", this.getLeftTop(), this.getRightTop(),
        this.getLeftBottom(), this.getRightBottom());
    return String.format("%s\n%s\n%s\n%s\n%s\n", segment, s1, split, s2, segment);
  }
}

/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import axf.java.common.utils.math.constants.CoordinateSystem2D;
import java.util.Iterator;

/**
 * 矩形结构
 * <p>
 * 以人类熟悉的上下左右的方式为标准
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public interface Rectangular {

  /**
   * 坐标系类型
   * <p>
   * 只有两种，标准笛卡尔坐标系和笛卡尔坐标系Y轴反向
   *
   * @return {@code CoordinateSystem2D}
   */
  CoordinateSystem2D coordinateSystem();

  /**
   * 矩形面积
   *
   * @return 返回矩形面积
   */
  default float area() {
    return this.width() * this.height();
  }

  /**
   * 获取宽度
   *
   * @return 宽度
   */
  float width();

  /**
   * 获取高度
   *
   * @return 高度
   */
  float height();

  /**
   * 迭代矩形所有线
   *
   * @return {@code Iterator<Linear>}
   */
  default Iterator<Linear> iteratorLine() {
    return new Iterator<>() {
      private final Linear[] LINES = new Linear[]{
          new Line(Rectangular.this.getLeftTop(), Rectangular.this.getRightTop()),
          new Line(Rectangular.this.getLeftTop(), Rectangular.this.getLeftBottom()),
          new Line(Rectangular.this.getRightBottom(), Rectangular.this.getRightTop()),
          new Line(Rectangular.this.getRightBottom(), Rectangular.this.getLeftBottom())
      };
      private int INDEX = 0;

      @Override
      public boolean hasNext() {
        return this.INDEX < this.LINES.length;
      }

      @Override
      public Linear next() {
        return this.LINES[this.INDEX++];
      }
    };
  }

  /**
   * 获取左上角点的坐标
   * <p>
   * 子类必须重写 {@code this#getLeftTop()} 或 {@code this#getLeftBottom()} 根据不同的坐标系
   *
   * @return {@code Coordinate2D}坐标
   * @see CoordinateSystem2D
   */
  default Coordinate2D getLeftTop() {
    return startPoint();
  }

  /**
   * 矩形起点
   * <p>
   * 根据坐标系的不同起点也会不同 但是总是在四个点的其中一个位置
   *
   * @return {@code Coordinate2D}起点坐标
   */
  Coordinate2D startPoint();

  /**
   * 获取左下角点的坐标
   * <p>
   * 子类必须重写 {@code this#getLeftTop()} 或 {@code this#getLeftBottom()} 根据不同的坐标系
   *
   * @return {@code Coordinate2D}坐标
   * @see CoordinateSystem2D
   */
  default Coordinate2D getLeftBottom() {
    return startPoint();
  }

  /**
   * 获取右上角点的坐标
   *
   * @return {@code Coordinate2D}坐标
   */
  default Coordinate2D getRightTop() {
    return getLeftTop().xAxisTranslation(width());
  }

  /**
   * 获取右下角点的坐标
   *
   * @return {@code Coordinate2D}坐标
   */
  default Coordinate2D getRightBottom() {
    return getLeftBottom().xAxisTranslation(width());
  }

  /**
   * 正中心的点
   *
   * @return {@code Coordinate2D}
   */
  default Coordinate2D centerMidpoint() {
    return getLeftTop().midPointWithOther(getRightBottom());
  }

  /**
   * 单行文本书写起始点，默认居中
   * <p>
   * 以xy轴正方向为书写方向
   *
   * @param contentWidth  内容所暂用的宽度
   * @param contentHeight 内存所占用的高度
   * @return {@code Coordinate2D}返回书写起始点
   */
  default Coordinate2D singleLine(float contentWidth, float contentHeight) {
    return singleLine(PlaceholderDirection.CENTER, contentWidth, contentHeight);
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
  Coordinate2D singleLine(PlaceholderDirection placeholderDirection,
      float contentWidth, float contentHeight);

  /**
   * 多行文本书写起始点，默认居中
   * <p>
   * 以xy轴正方向为书写方向
   *
   * @param contentWidth  单行内容所暂用的宽度
   * @param contentHeight 单行内存所占用的高度
   * @param interval      每行的间隔
   * @param rows          行数
   * @return {@code Coordinate2D}返回书写起始点
   */
  default Coordinate2D[] multiplyLine(
      float contentWidth, float contentHeight, float interval, int rows) {
    return multiplyLine(PlaceholderDirection.CENTER, contentWidth, contentHeight, interval,
        rows);
  }

  /**
   * 多行文本书写起始点
   * <p>
   * 以xy轴正方向为书写方向
   *
   * @param placeholderDirection 占位方向
   * @param contentWidth         单行内容所暂用的宽度
   * @param contentHeight        单行内存所占用的高度
   * @param interval             每行的间隔
   * @param rows                 行数
   * @return {@code Coordinate2D}返回书写起始点
   */
  Coordinate2D[] multiplyLine(PlaceholderDirection placeholderDirection,
      float contentWidth, float contentHeight, float interval, int rows);

  /**
   * 水平中点坐标
   *
   * @return {@code Coordinate2D}
   */
  default float horizontalMidpoint() {
    return getLeftTop().midPointWithOther(getRightTop()).x();
  }

  /**
   * 垂直中点坐标
   *
   * @return {@code Coordinate2D}
   */
  default float verticalMidpoint() {
    return getRightTop().midPointWithOther(getRightBottom()).y();
  }
}

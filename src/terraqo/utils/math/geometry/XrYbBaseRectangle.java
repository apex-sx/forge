/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import axf.java.common.utils.math.constants.CoordinateSystem2D;

/**
 * 矩形结构
 * <p>
 * 坐标系： 以左上点为原点 向右为x轴正方向 向下为y轴正方向
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public class XrYbBaseRectangle extends BaseRectangle {

  public XrYbBaseRectangle(final float startX, final float startY, final float width,
      final float height) {
    super(startX, startY, width, height);
  }

  public XrYbBaseRectangle(final Coordinate2D startPoint, final float width, final float height) {
    super(startPoint, width, height);
  }

  @Override
  public final CoordinateSystem2D coordinateSystem() {
    return CoordinateSystem2D.XR_YB;
  }

  @Override
  public Coordinate2D getLeftBottom() {
    return getLeftTop().yAxisTranslation(height());
  }
}

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
 * 可以合并的等距网格
 * <p>
 * 坐标系： 以左上点为原点 向右为x轴正方向 向下为y轴正方向
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public class XrYbIrregularEquidistantLattice extends BaseIrregularEquidistantLattice {

  public XrYbIrregularEquidistantLattice(final float startX,
      final float startY,
      final float width,
      final float height,
      final int cols,
      final int rows) {
    super(startX, startY, width, height, cols, rows);
  }

  public XrYbIrregularEquidistantLattice(final Coordinate2D startPoint,
      final float width,
      final float height,
      final int rows,
      final int cols) {
    super(startPoint, width, height, rows, cols);
  }

  @Override
  public final CoordinateSystem2D coordinateSystem() {
    return CoordinateSystem2D.XR_YB;
  }

  @Override
  public Coordinate2D getLeftBottom() {
    return getLeftTop().yAxisTranslation(height());
  }

  @Override
  protected BaseNamedGrid newGrid(String keyName, Coordinate2D startPoint,
      float width, float height, Range range) {
    return new XrYbNamedGrid(startPoint, width, height, range, keyName);
  }

  @Override
  protected BaseGrid newGrid(Coordinate2D startPoint, float width, float height, Range range) {
    return new XrYbGrid(startPoint, width, height, range);
  }

  private class XrYbNamedGrid extends BaseNamedGrid {

    protected XrYbNamedGrid(Coordinate2D startPoint, float width, float height,
        Range range, String keyName) {
      super(startPoint, width, height, range, keyName);
    }

    @Override
    public Coordinate2D getLeftBottom() {
      return getLeftTop().yAxisTranslation(height());
    }
  }

  private class XrYbGrid extends BaseGrid {

    protected XrYbGrid(Coordinate2D startPoint, float width, float height,
        Range range) {
      super(startPoint, width, height, range);
    }

    @Override
    public Coordinate2D getLeftBottom() {
      return getLeftTop().yAxisTranslation(height());
    }
  }
}

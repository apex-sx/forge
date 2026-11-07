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
 * 坐标系： 以左下点为原点 向右为x轴正方向 向上为y轴正方向
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public class XrYtIrregularEquidistantLattice extends BaseIrregularEquidistantLattice {

  public XrYtIrregularEquidistantLattice(final float startX,
      final float startY,
      final float width,
      final float height,
      final int cols,
      final int rows) {
    super(startX, startY, width, height, cols, rows);
  }

  public XrYtIrregularEquidistantLattice(final Coordinate2D startPoint,
      final float width,
      final float height,
      final int rows,
      final int cols) {
    super(startPoint, width, height, rows, cols);
  }

  @Override
  public final CoordinateSystem2D coordinateSystem() {
    return CoordinateSystem2D.XR_YT;
  }

  @Override
  public Coordinate2D getLeftTop() {
    return this.getLeftBottom().yAxisTranslation(this.height());
  }

  @Override
  protected BaseGrid newGrid(Coordinate2D startPoint, float width, float height, Range range) {
    return new XrYtGrid(startPoint, width, height, range);
  }

  @Override
  protected BaseNamedGrid newGrid(String keyName, Coordinate2D startPoint,
      float width, float height, Range range) {
    return new XrYtNamedGrid(startPoint, width, height, range, keyName);
  }

  private class XrYtNamedGrid extends BaseNamedGrid {

    protected XrYtNamedGrid(Coordinate2D startPoint, float width, float height,
        Range range, String keyName) {
      super(startPoint, width, height, range, keyName);
    }

    @Override
    public Coordinate2D getLeftTop() {
      return this.getLeftBottom().yAxisTranslation(this.height());
    }
  }

  private class XrYtGrid extends BaseGrid {

    /**
     * @param startPoint 起点坐标
     * @param width      宽度
     * @param height     高度
     * @throws ArithmeticException 当{@code row},{@code col}小于0时或{@code totalWidth},{@code totalHeight}小于等于0抛出该异常
     */
    protected XrYtGrid(Coordinate2D startPoint, float width, float height,
        Range range) {
      super(startPoint, width, height, range);
    }

    @Override
    public Coordinate2D getLeftTop() {
      return this.getLeftBottom().yAxisTranslation(this.height());
    }
  }
}

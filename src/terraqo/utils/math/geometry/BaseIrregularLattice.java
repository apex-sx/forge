/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import com.utils.UtilsOfArray;
import com.utils.UtilsOfAssert;
import com.utils.UtilsOfJudge;
import java.util.Arrays;
import java.util.Collection;

/**
 * 不规则网格
 * <p>
 * 该网格是在的网格的基础上合并网格
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public abstract class BaseIrregularLattice extends BaseIrregularEquidistantLattice {

  /**
   * 列宽
   */
  private final float[] colWidths;
  /**
   * 行高
   */
  private float[] rowHeights;

  public BaseIrregularLattice(float startX, float startY, float width,
      float height, int cols, int rows) {
    this(new Coordinate2D(startX, startY), width, height, cols, rows);
  }

  public BaseIrregularLattice(Coordinate2D startPoint, float width,
      float height, int rows, int cols) {
    super(startPoint, width, height, rows, cols);
    colWidths = new float[cols];
    rowHeights = new float[rows];
    float cw = width / cols;
    float rh = height / rows;
    Arrays.fill(colWidths, cw);
    Arrays.fill(rowHeights, rh);
  }

  public void setColWidth(int col, float width) {
    UtilsOfAssert.checkGreater(width, 0, "Width must greater 0");
    checkRowCol(null, col);
    updateMergeGridColWidth(col, width);
  }

  public void setRowHeight(int row, float height) {
    UtilsOfAssert.checkGreater(height, 0, "Height must greater 0");
    checkRowCol(row, null);
    updateMergeGridRowHeight(row, height);
  }

  public void setMergeGrid(String keyName, float width, float height) {
    //获取原来合并格子
    BaseNamedGrid ng = getMergeGrid(keyName);
    Range range = ng.getRange();
    //合并格子高度平摊
    int rowAmount = ng.getRowAmount();
    final float newHeight = height / rowAmount;
    range.iterateRow(r -> setRowHeight(r, newHeight));
    //合并格子宽度平摊
    int colAmount = ng.getColAmount();
    final float newWidth = width / colAmount;
    range.iterateCol(c -> setColWidth(c, newWidth));
    Coordinate2D startPoint = ng.startPoint();
    //替换原来
    replaceMergeGrid(keyName, newGrid(keyName, startPoint, width, height, range));
    range.iterateRow(r -> updateMergeGridRowHeight(r, newHeight));
    range.iterateRow(c -> updateMergeGridColWidth(c, newWidth));
  }

  @Override
  public float width() {
    if (hasMergeGrid()) {
      return (float) UtilsOfArray.sum(colWidths);
    }
    return super.width();
  }

  @Override
  public float height() {
    if (hasMergeGrid()) {
      return (float) UtilsOfArray.sum(rowHeights);
    }
    return super.height();
  }

  @Override
  public float rowHeight(int row) {
    checkRowCol(row, null);
    return rowHeights[row];
  }

  @Override
  public float colWidth(int col) {
    checkRowCol(null, col);
    return colWidths[col];
  }

  @Override
  protected float computeOffsetX(int col) {
    double with = UtilsOfArray.sum(colWidths, 0, col);
    return (float) with;
  }

  @Override
  protected float computeOffsetY(int row) {
    double height = UtilsOfArray.sum(rowHeights, 0, row);
    return (float) height;
  }

  private void updateMergeGridColWidth(int col, float width) {
    float originColWidth = colWidths[col];
    colWidths[col] = width;
    //更新宽度
    for (int i = 0; i < rowHeights.length; i++) {
      BaseNamedGrid ng = getMergeGrid(i, col);
      if (UtilsOfJudge.isNoNull(ng)) {
        float newWidth = ng.width() - originColWidth + width;
        replaceMergeGrid(i, col,
            newGrid(ng.keyName(), ng.startPoint(), newWidth, ng.height(),
                ng.getRange()));
      }
    }
    Collection<BaseNamedGrid> namedGridCollection = getMergeGrid(
        new Range(0, col, rows() - 1, cols() - 1));
    //更新起始点
    for (BaseNamedGrid namedGrid : namedGridCollection) {
      Coordinate2D startPoint = this.computeStartPoint(namedGrid.getRow(), namedGrid.getCol());
      BaseNamedGrid newGrid = newGrid(namedGrid.keyName(), startPoint, namedGrid.width(),
          namedGrid.height(),
          namedGrid.getRange());
      replaceMergeGrid(namedGrid.keyName(), newGrid);
    }
  }

  private void updateMergeGridRowHeight(int row, float height) {
    float originHeight = UtilsOfArray.getSet(rowHeights, row, height);
    for (int i = 0; i < colWidths.length; i++) {
      BaseNamedGrid ng = getMergeGrid(row, i);
      if (UtilsOfJudge.isNoNull(ng)) {
        float newHeight = ng.height() - originHeight + height;
        float yOffset = this.computeOffsetY(row);
        Coordinate2D newStartPoint = this.startPoint().yAxisTranslation(yOffset);
        replaceMergeGrid(row, i,
            newGrid(ng.keyName(), newStartPoint, ng.width(), newHeight,
                ng.getRange()));
      }
    }
    //更新起始点
    Collection<BaseNamedGrid> namedGridCollection = getMergeGrid(
        new Range(row, 0, rows() - 1, cols() - 1));
    for (BaseNamedGrid namedGrid : namedGridCollection) {
      Coordinate2D startPoint = this.computeStartPoint(namedGrid.getRow(), namedGrid.getCol());
      BaseNamedGrid newGrid = newGrid(namedGrid.keyName(), startPoint, namedGrid.width(),
          namedGrid.height(),
          namedGrid.getRange());
      replaceMergeGrid(namedGrid.keyName(), newGrid);
    }
  }
}

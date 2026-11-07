/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import axf.java.common.utils.UtilsOfAssert;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

/**
 * 网格
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public interface Latticed<E extends Rectangular> extends Rectangular, Iterable<E> {

  /**
   * 行数量
   *
   * @return 行数
   */
  int rows();

  /**
   * 列数量
   *
   * @return 列数
   */
  int cols();

  /**
   * 第几行的高度
   *
   * @param row 第几行
   * @return 列宽
   */
  float rowHeight(int row);

  /**
   * 第几列的宽度
   *
   * @param col 第几列
   * @return 列宽
   */
  float colWidth(int col);

  /**
   * 获取格子
   *
   * @param row 第几行
   * @param col 第几列
   * @return @{code <E extends Rectangular>}
   */
  E getGrid(int row, int col);

  class Range {

    private final int row;
    private final int col;
    private final int endRow;
    private final int endCol;

    public Range(int row, int col) {
      this(row, col, row, col);
    }

    public Range(int row, int col, int endRow, int endCol) {
      UtilsOfAssert.checkGreaterEqual(endRow, row, "Don't allow start row more than equal end row!");
      UtilsOfAssert.checkGreaterEqual(endCol, col, "Don't allow start col more than equal end col!");
      UtilsOfAssert.checkNoNegative(row, "Row must greater than 0");
      UtilsOfAssert.checkNoNegative(endRow, "End Row must greater than 0");
      UtilsOfAssert.checkNoNegative(col, "Col must greater than 0");
      UtilsOfAssert.checkNoNegative(endCol, "End Col must greater than 0");
      this.row = row;
      this.col = col;
      this.endRow = endRow;
      this.endCol = endCol;
    }

    /**
     * 判断与另一个范围是否冲突
     *
     * @param range 范围
     * @return true冲突，否则为false
     */
    public boolean isConflict(final Range range) {
      if (this.isOneGrid() && range.isOneGrid()) {
        if (this.row == range.row) {
          return this.col == range.col;
        }
        return false;
      } else if (this.isOneGrid()) {
        return isInRange(this.row, this.col, range);
      } else if (range.isOneGrid()) {
        return isInRange(range.row, range.col, this);
      }
      Range min, max;
      if (this.placeholderCount() > range.placeholderCount()) {
        max = this;
        min = range;
      } else {
        max = range;
        min = this;
      }
      if (isInRange(min.row, min.col, max) || isInRange(min.endRow, min.endCol, max)) {
        return true;
      }
      return isInRange(min.row, min.endCol, max) || isInRange(min.endRow, min.col, max);
    }

    public final boolean isOneGrid() {
      return row == endRow && col == endCol;
    }

    public int placeholderCount() {
      return rowDiff() * colDiff();
    }

    public int colDiff() {
      return endCol - col + 1;
    }

    public int rowDiff() {
      return endRow - row + 1;
    }

    private boolean isInRange(int row, int col, Range range) {
      if (row >= range.row && row <= range.endRow) {
        return col >= range.col && col <= range.endCol;
      }
      return false;
    }

    public int getRow() {
      return row;
    }

    public int getCol() {
      return col;
    }

    public int getEndRow() {
      return endRow;
    }

    public int getEndCol() {
      return endCol;
    }

    public void iterateRow(IntConsumer indexConsumer) {
      IntStream.rangeClosed(row, endRow).forEach(indexConsumer);
    }

    public void iterateCol(IntConsumer indexConsumer) {
      IntStream.rangeClosed(col, endCol).forEach(indexConsumer);
    }

    public void iterate(BiConsumer<Integer, Integer> indexConsumer) {
      IntStream.rangeClosed(row, endRow)
          .forEach(r -> IntStream.rangeClosed(col, endCol)
              .forEach(c -> indexConsumer.accept(r, c)));
    }

    @Override
    public int hashCode() {
      return Objects.hash(row, col, endRow, endCol);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Range)) {
        return false;
      }
      Range range = (Range) o;
      return row == range.row &&
          col == range.col &&
          endRow == range.endRow &&
          endCol == range.endCol;
    }

    @Override
    public String toString() {
      if (isOneGrid()) {
        return "[" + row + "-" + col + "]";
      }
      return "[" + row + "-" + col + " : " + endRow + "-" + endCol + "]";
    }
  }
}

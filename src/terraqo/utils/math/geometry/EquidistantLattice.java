/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import axf.java.common.utils.UtilsOfAssert;
import axf.java.common.utils.UtilsOfJudge;
import axf.java.common.utils.math.constants.CoordinateSystem2D;
import axf.java.common.utils.math.geometry.EquidistantLattice.BaseGrid;
import java.util.Iterator;
import java.util.Objects;

/**
 * 等距网格
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public abstract class EquidistantLattice extends BaseRectangle implements
    Latticed<BaseGrid> {

  /**
   * 行数
   */
  private final int totalRows;
  /**
   * 列数
   */
  private final int totalCols;
  /**
   * 行高
   */
  private final float rowHeight;
  /**
   * 列宽
   */
  private final float colWidth;
  /**
   * 总计网格
   */
  private final int totalGrids;

  /**
   * @param startX    起点x轴坐标
   * @param startY    起点y轴坐标
   * @param width     宽度
   * @param height    高度
   * @param totalRows 总行数
   * @param totalCols 总列数
   * @throws ArithmeticException 当{@code totalWidth},{@code totalHeight},{@code totalCols},{@code totalRows}小于等于0时抛出该异常
   */
  public EquidistantLattice(final float startX,
      final float startY,
      final float width,
      final float height,
      final int totalRows,
      final int totalCols) {
    this(new Coordinate2D(startX, startY), width, height, totalRows, totalCols);
  }

  /**
   * @param startPoint 起点坐标
   * @param width      宽度
   * @param height     高度
   * @param totalRows  总行数
   * @param totalCols  总列数
   * @throws ArithmeticException 当{@code totalWidth},{@code totalHeight},{@code totalCols},{@code totalRows}小于等于0时抛出该异常
   */
  public EquidistantLattice(final Coordinate2D startPoint,
      final float width,
      final float height,
      final int totalRows,
      final int totalCols) {
    super(startPoint, width, height);
    UtilsOfAssert.checkPositive(totalCols, "Row must greater than 0");
    this.totalRows = totalRows;
    UtilsOfAssert.checkPositive(totalCols, "Row must greater than 0");
    this.totalCols = totalCols;
    this.rowHeight = this.format(height / totalRows);
    this.colWidth = this.format(width / totalCols);
    this.totalGrids = totalRows * totalCols;
  }

  /**
   * 行数量
   *
   * @return 行数
   */
  @Override
  public final int rows() {
    return this.totalRows;
  }

  /**
   * 列数量
   *
   * @return 列数
   */
  @Override
  public final int cols() {
    return this.totalCols;
  }

  /**
   * 返回行高
   *
   * @param row 该参数无效，因为是等距的，因此任何值都会等到相同结果
   * @return 行高
   */
  @Override
  public float rowHeight(final int row) {
    return this.rowHeight;
  }

  /**
   * 返回列宽
   *
   * @param col 该参数无效，因为是等距的，因此任何值都会等到相同结果
   * @return 列宽
   */
  @Override
  public float colWidth(final int col) {
    return this.colWidth;
  }

  /**
   * 获取格子
   *
   * @param row 第几行
   * @param col 第几列
   * @return {@code Grid}
   */
  @Override
  public final BaseGrid getGrid(final int row, final int col) {
    checkRowCol(row, col);
    return this.createGridsDynamically(row, col);
  }

  /**
   * 动态创建格子
   *
   * @param row 第几行
   * @param col 第几列
   * @return {@code Grid}
   */
  protected BaseGrid createGridsDynamically(final int row, final int col) {
    final Coordinate2D newCoordinate = this.computeStartPoint(row, col);
    return this.newGrid(newCoordinate, colWidth(col), rowHeight(row), new Range(row, col));
  }

  protected Coordinate2D computeStartPoint(final int row, final int col) {
    final float ox = this.computeOffsetX(col);
    final float oy = this.computeOffsetY(row);
    final Coordinate2D startPoint = this.startPoint();
    return startPoint.xyTranslation(ox, oy);
  }

  /**
   * 计算start point的X轴偏移量
   *
   * @param col 第几列
   * @return 偏移量距离
   */
  protected float computeOffsetX(final int col) {
    return this.format(col * this.colWidth);
  }

  /**
   * 计算start point的Y轴偏移量
   *
   * @param row 第几列
   * @return 偏移量距离
   */
  protected float computeOffsetY(final int row) {
    return this.format(row * this.rowHeight);
  }

  /**
   * 创建一个格子 决定它的坐标轴方式
   *
   * @param startPoint 起点
   * @param width      宽度
   * @param height     高度
   * @param range      范围，第几行几列
   * @return {@code Grid}
   */
  protected abstract BaseGrid newGrid(Coordinate2D startPoint, float width, float height, Range range);

  /**
   * 检测行列是否符合要求
   *
   * @param row 行索引
   * @param col 列索引
   */
  protected void checkRowCol(Integer row, Integer col) {
    if (UtilsOfJudge.isNoNull(row)) {
      UtilsOfAssert.checkGreaterEqual(row, 0, "Row must greater equal 0");
      UtilsOfAssert.checkLesser(row, totalRows, "Row must lesser" + totalRows);
    }
    if (UtilsOfJudge.isNoNull(col)) {
      UtilsOfAssert.checkGreaterEqual(col, 0, "Col must greater equal 0");
      UtilsOfAssert.checkLesser(col, totalCols, "Col must lesser" + totalCols);
    }
  }

  protected void checkRange(Range range) {
    UtilsOfAssert.checkIsNoNull(range, "Range is null");
    UtilsOfAssert.checkGreaterEqual(range.getRow(), 0, "Start row of range must greater than 0 or equal");
    UtilsOfAssert.checkGreaterEqual(range.getCol(), 0, "Start col of range must greater than 0 or equal");
    if (!range.isOneGrid()) {
      UtilsOfAssert.checkLesser(range.getEndRow(), totalRows, "End row of range must lesser than " + totalRows);
      UtilsOfAssert.checkLesser(range.getEndCol(), totalRows, "End col of range must lesser than " + totalRows);
    }
  }

  /**
   * 动态创建格子
   *
   * @param index 格子索引
   * @return {@code Grid}
   */
  private BaseGrid createGridsDynamically(final int index) {
    return this.createGridsDynamically(index / this.totalCols, index % this.totalCols);
  }

  /**
   * 格子迭代器
   * <p>
   * 总是朝着x轴y轴正方向
   * <p>
   * 先以x轴正方向遍历顺序 再以y轴正方向遍历顺序
   *
   * @return {@code Iterator<EquidistantLattice.Grid>}
   */
  @Override
  public Iterator<BaseGrid> iterator() {
    return new GridIterator();
  }

  /**
   * 格子对象
   */
  public abstract class BaseGrid extends BaseRectangle {

    /**
     * 第几行
     */
    private final Range range;

    /**
     * @param startPoint 起点坐标
     * @param width      宽度
     * @param height     高度
     * @param range      第几行
     * @throws ArithmeticException 当{@code row},{@code col}小于0时或{@code totalWidth},{@code totalHeight}小于等于0抛出该异常
     */
    protected BaseGrid(final Coordinate2D startPoint,
        final float width,
        final float height,
        final Range range) {
      super(startPoint, width, height);
      this.range = range;
    }

    public Range getRange() {
      return range;
    }

    /**
     * 是否是第一列
     *
     * @return true为第一列否则为false
     */
    public final boolean isFirstCol() {
      return getCol() == 0;
    }

    //get方法
    public int getCol() {
      return this.range.getCol();
    }

    /**
     * 是否是第一行
     *
     * @return true为第一行否则为false
     */
    public final boolean isFirstRow() {
      return getRow() == 0;
    }

    public int getRow() {
      return this.range.getRow();
    }

    /**
     * 是否是最后一列
     *
     * @return true为最后一列否则为false
     */
    public final boolean isLastCol() {
      return getCol() == EquidistantLattice.this.totalCols - 1;
    }

    /**
     * 是否是最后一行
     *
     * @return true为最后一行否则为false
     */
    public final boolean isLastRow() {
      return getRow() == EquidistantLattice.this.totalRows - 1;
    }

    /**
     * 是否是标准的格子
     * <p>
     * 标准格子是未经过合并的格子
     *
     * @return true为标准格子
     */
    public boolean isStandardGrid() {
      return range.isOneGrid();
    }

    @Override
    public final CoordinateSystem2D coordinateSystem() {
      return EquidistantLattice.this.coordinateSystem();
    }

    @Override
    public int hashCode() {
      return Objects.hash(super.hashCode(), range);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof BaseGrid)) {
        return false;
      }
      if (!super.equals(o)) {
        return false;
      }
      BaseGrid grid = (BaseGrid) o;
      return range.equals(grid.range);
    }

    @Override
    public String toString() {
      return String.format("[%s]:%n%s", this.keyName(), super.toString());
    }

    /**
     * 格子的键名
     * <p>
     * 键名格式：行号-列号 如： 1-1 1-2 1-3 2-1 2-2 2-3
     *
     * @return 键名
     */
    public String keyName() {
      return range.toString();
    }
  }

  /**
   * 格子迭代器
   * <p>
   * 总是朝着x轴y轴正方向
   * <p>
   * 先以x轴正方向遍历顺序 再以y轴正方向遍历顺序
   */
  private class GridIterator implements Iterator<BaseGrid> {

    private int index;

    public GridIterator() {
      this.index = 0;
    }

    @Override
    public boolean hasNext() {
      return this.index < EquidistantLattice.this.totalGrids;
    }

    @Override
    public BaseGrid next() {
      final BaseGrid grid = EquidistantLattice.this.createGridsDynamically(this.index);
      this.index++;
      return grid;
    }
  }
}

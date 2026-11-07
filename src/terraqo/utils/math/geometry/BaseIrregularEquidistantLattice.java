/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import com.utils.UtilsOfAssert;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * 不规则等距网格
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public abstract class BaseIrregularEquidistantLattice extends EquidistantLattice {

  /**
   * 合并格子的最高位的标记位
   */
  private static final int MERGE_GRID_HIGHEST_BIT = 0x80000000;
  /**
   * 正常格子标识
   */
  private static final int SIGN_NORMAL_GRID = 0;
  /**
   * 标记
   */
  private final int[] signGrids;
  /**
   * 标记格子映射
   */
  private final Map<Integer, BaseNamedGrid> markGridMap = new HashMap<>(3);
  /**
   * 键名索引映射
   */
  private final Map<String, Integer> keyNameIndexMap = new HashMap<>(3);
  /**
   * 标记数量
   */
  private int signCount;

  /**
   * @param startX 起点x轴坐标
   * @param startY 起点y轴坐标
   * @param width  宽度
   * @param height 高度
   * @param rows   总行数
   * @param cols   总列数
   * @throws ArithmeticException 当{@code totalWidth},{@code totalHeight},{@code totalCols},{@code totalRows}小于等于0时抛出该异常
   */
  public BaseIrregularEquidistantLattice(final float startX,
      final float startY,
      final float width,
      final float height,
      final int cols,
      final int rows) {
    this(new Coordinate2D(startX, startY), width, height, cols, rows);
  }

  /**
   * @param startPoint 起点坐标
   * @param width      宽度
   * @param height     高度
   * @param rows       总行数
   * @param cols       总列数
   * @throws ArithmeticException 当{@code totalWidth},{@code totalHeight},{@code totalCols},{@code totalRows}小于等于0时抛出该异常
   */
  public BaseIrregularEquidistantLattice(final Coordinate2D startPoint,
      final float width,
      final float height,
      final int rows,
      final int cols) {
    super(startPoint, width, height, rows, cols);
    this.signGrids = new int[rows * cols];
  }

  public boolean hasMergeGrid() {
    return signCount != 0;
  }

  /**
   * 获取合并格子
   *
   * @param keyName 合并格子的键名
   * @return {@code NamedGrid}
   */
  public BaseNamedGrid getMergeGrid(final String keyName) {
    if (!this.keyNameIndexMap.containsKey(keyName)) {
      throw new RuntimeException("Don't exist grid of [" + keyName + "]");
    }
    return this.markGridMap.get(this.keyNameIndexMap.get(keyName));
  }

  /**
   * 获取范围中的格子
   *
   * @param range 范围
   * @return {@code Collection<NamedGrid>}
   */
  public Collection<BaseNamedGrid> getMergeGrid(final Range range) {
    checkRange(range);
    Map<Integer, BaseNamedGrid> map = new HashMap<>(signCount);
    range.iterate((r, c) -> {
      if (isMarkGrids(r, c)) {
        int mark = getMarkNumber(r, c);
        if (!map.containsKey(mark)) {
          map.put(mark, markGridMap.get(mark));
        }
      }
    });
    return map.values();
  }

  /**
   * 是否标记了格子 如果标记了格子就说明是合并格子 如果没有标记了格子就说明不是合并格子
   *
   * @param row 第几行
   * @param col 第几列
   * @return true为标记了，否则为false
   */
  private boolean isMarkGrids(final int row, final int col) {
    return this.signGrids[row * cols() + col] != 0;
  }

  /**
   * 获取指定位置的标记
   *
   * @param row 第几行
   * @param col 第几列
   * @return 标记数字
   */
  private int getMarkNumber(final int row, final int col) {
    final int cols = cols();
    if (this.isStartMergeGrid(row, col)) {
      return this.signGrids[row * cols + col]
          ^ BaseIrregularEquidistantLattice.MERGE_GRID_HIGHEST_BIT;
    }
    return this.signGrids[row * cols + col];
  }

  /**
   * 判断当前点是否在合并格子的起始点
   *
   * @param row 行号
   * @param col 列号
   * @return true为是在合并格子的起始点，否则为false
   */
  private boolean isStartMergeGrid(final int row, final int col) {
    final int cols = cols();
    return (this.signGrids[row * cols + col]
        & BaseIrregularEquidistantLattice.MERGE_GRID_HIGHEST_BIT)
        == BaseIrregularEquidistantLattice.MERGE_GRID_HIGHEST_BIT;
  }

  /**
   * 获取合并格子
   *
   * @param row 第几行
   * @param col 第几列
   * @return {@code NamedGrid}
   */
  public BaseNamedGrid getMergeGrid(final int row, final int col) {
    checkRowCol(row, col);
    if (isMarkGrids(row, col)) {
      int mark = getMarkNumber(row, col);
      return this.markGridMap.get(mark);
    }
    return null;
  }

  /**
   * 添加合并格子
   *
   * @param keyName 合并盒子的键名
   * @param range   开始行
   * @throws IllegalArgumentException 当参数大小错误抛出该异常，end必须大于start
   * @throws ArithmeticException      当参数负数时抛出该异常
   */
  public void addMergeGrid(final String keyName, final Range range) {
    UtilsOfAssert.checkValid(keyName, "Don't allow key name invalid!");
    if (range.isOneGrid()) {
      return;
    }
    checkRange(range);
    checkRangeConflict(keyName, range);
    //合并格子标记
    final int sign = this.markGrids(range);
    //x y轴起点偏移
    final float xOffset = cw() * range.getCol();
    final float yOffset = rh() * range.getRow();
    final Coordinate2D targetPoint = this.startPoint().xyTranslation(xOffset, yOffset);
    //合并格子宽高
    final float width = range.colDiff() * cw();
    final float height = range.rowDiff() * rh();
    this.keyNameIndexMap.put(keyName, sign);
    this.markGridMap.put(sign, this.newGrid(keyName, targetPoint, width, height, range));
  }

  /**
   * 创建无规则网格的格子
   *
   * @param startPoint 起始点
   * @param keyName    键名
   * @param width      宽度
   * @param height     高度
   * @param range      第几行几列范围
   * @return {@code IrregularGrid}
   */
  protected abstract BaseNamedGrid newGrid(String keyName, Coordinate2D startPoint,
      float width, float height, Range range);

  /**
   * 获取列宽
   *
   * @return 列宽
   */
  private float cw() {
    return super.colWidth(0);
  }

  /**
   * 获取行高
   *
   * @return 行高
   */
  private float rh() {
    return super.rowHeight(0);
  }

  /**
   * 检查范围内有冲突的合并格子，是否键名有冲突
   *
   * @param keyName 键名
   */
  private void checkRangeConflict(final String keyName, final Range range) {
    if (this.keyNameIndexMap.containsKey(keyName)) {
      throw new IllegalArgumentException("Already exist key name of " + keyName + "!");
    }
    if (this.isMarkGrids(range)) {
      throw new IllegalArgumentException(
          "Already exist irregular lattice in the range of " + range);
    }
  }

  /**
   * 是否标记了格子 如果标记了格子就说明是合并格子 如果没有标记了格子就说明不是合并格子
   *
   * @param range 第几行几列范围
   * @return true为标记了，否则为false
   */
  private boolean isMarkGrids(final Range range) {
    for (int i = range.getRow(); i <= range.getEndRow(); i++) {
      for (int j = range.getCol(); j <= range.getEndCol(); j++) {
        if (isMarkGrids(i, j)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * 标记合并的格子
   *
   * @param range 第几行几列范围
   * @return 当前标记的数字
   */
  private int markGrids(final Range range) {
    final int sign = ++this.signCount;
    final int row = range.getRow();
    final int col = range.getCol();
    final int cols = cols();
    IntStream.rangeClosed(row, range.getEndRow())
        .forEach(r -> IntStream.rangeClosed(col, range.getEndCol())
            .forEach(c -> this.signGrids[r * cols + c] = sign));
    //起始点最高位标记
    this.signGrids[row * cols + col] =
        sign ^ BaseIrregularEquidistantLattice.MERGE_GRID_HIGHEST_BIT;
    return sign;
  }

  /**
   * 替换归并格子
   *
   * @param keyName   以存在的键名
   * @param namedGrid 格子
   */
  protected final void replaceMergeGrid(final String keyName,
      final BaseNamedGrid namedGrid) {
    int index = this.keyNameIndexMap.get(keyName);
    BaseNamedGrid origin = this.markGridMap.get(index);
    this.markGridMap.put(index, namedGrid);
  }

  /**
   * 替换归并格子
   *
   * @param row       第几行
   * @param col       第几列
   * @param namedGrid 格子
   */
  protected final void replaceMergeGrid(final int row, int col,
      final BaseNamedGrid namedGrid) {
    int mark = getMarkNumber(row, col);
    this.markGridMap.put(mark, namedGrid);
  }

  /**
   * 动态创建格子
   *
   * @param row 第几行
   * @param col 第几列
   * @return {@code Grid}
   */
  @Override
  protected BaseGrid createGridsDynamically(final int row, final int col) {
    final int signNumber = this.getMarkNumber(row, col);
    if (signNumber != BaseIrregularEquidistantLattice.SIGN_NORMAL_GRID) {
      return this.markGridMap.get(signNumber);
    }
    return super.createGridsDynamically(row, col);
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
   * 格子迭代器
   * <p>
   * 总是朝着x轴y轴正方向
   * <p>
   * 先以x轴正方向遍历顺序 再以y轴正方向遍历顺序
   */
  private class GridIterator implements Iterator<BaseGrid> {

    private final int gridAmount;
    private int realIndex;
    private int index;

    public GridIterator() {
      this.gridAmount = BaseIrregularEquidistantLattice.this.signCount + (int) Arrays.stream(
          BaseIrregularEquidistantLattice.this.signGrids).filter(i -> i == 0).count();
      this.realIndex = 0;
      this.index = 0;
    }

    @Override
    public boolean hasNext() {
      return this.index < this.gridAmount;
    }

    @Override
    public BaseGrid next() {
      final int cols = cols();
      int r = this.realIndex / cols;
      int c = this.realIndex % cols;
      while (BaseIrregularEquidistantLattice.this.isMarkGrids(r, c)) {
        this.realIndex++;
        if (BaseIrregularEquidistantLattice.this.isStartMergeGrid(r, c)) {
          this.index++;
          return BaseIrregularEquidistantLattice.this.createGridsDynamically(r, c);
        }
        r = this.realIndex / cols;
        c = this.realIndex % cols;
      }
      this.realIndex++;
      this.index++;
      return BaseIrregularEquidistantLattice.this.createGridsDynamically(r, c);
    }
  }

  /**
   * 无规则网格的格子
   */
  protected abstract class BaseNamedGrid extends BaseGrid {

    private final String keyName;

    protected BaseNamedGrid(Coordinate2D startPoint, float width, float height,
        Range range, String keyName) {
      super(startPoint, width, height, range);
      this.keyName = keyName;
    }

    public int getRowAmount() {
      Range range = getRange();
      return range.getEndRow() - range.getRow() + 1;
    }

    public int getColAmount() {
      Range range = getRange();
      return range.getEndCol() - range.getCol() + 1;
    }

    @Override
    public String keyName() {
      return this.keyName;
    }

    public boolean isConflict(final BaseNamedGrid namedGrid) {
      return this.getRange().isConflict(namedGrid.getRange());
    }
  }
}

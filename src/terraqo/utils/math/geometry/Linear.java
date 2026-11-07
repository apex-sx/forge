/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import java.util.Iterator;

/**
 * 线条的
 */
public interface Linear extends Iterable<Coordinate2D> {

  /**
   * 返回默认迭代器
   * <p>
   * 默认迭代是从第一个点到第二个点
   *
   * @return {@code Iterator<Coordinate2D>}
   */
  @Override
  default Iterator<Coordinate2D> iterator() {
    return new Iterator<>() {
      private int count = 0;

      @Override
      public boolean hasNext() {
        return this.count < 2;
      }

      @Override
      public Coordinate2D next() {
        this.count++;
        if (this.count == 1) {
          return Linear.this.first();
        }
        if (this.count == 2) {
          return Linear.this.second();
        }
        return null;
      }
    };
  }

  /**
   * 第一个点
   *
   * @return @{code Coordinate2D}坐标点
   */
  Coordinate2D first();

  /**
   * 第二个点
   *
   * @return @{code Coordinate2D}坐标点
   */
  Coordinate2D second();

  /**
   * 长度
   *
   * @return 返回线的长度
   */
  float length();
}

/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.geometry;

import axf.java.common.utils.math.AbstractAccuracy;
import java.util.Objects;

/**
 * 线
 */
public class Line extends AbstractAccuracy implements Linear {

  protected final Coordinate2D first;
  protected final Coordinate2D second;

  public Line(final Coordinate2D first, final Coordinate2D second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public Coordinate2D first() {
    return this.first;
  }

  @Override
  public Coordinate2D second() {
    return this.second;
  }

  @Override
  public float length() {
    return this.first.distance(this.second);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.first, this.second);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    final Line that = (Line) o;
    return this.first.equals(that.first) && this.second.equals(that.second);
  }

  @Override
  public String toString() {
    return String
        .format("%s-%s totalWidth:" + this.fp() + "%n", this.first, this.second, this.length());
  }
}

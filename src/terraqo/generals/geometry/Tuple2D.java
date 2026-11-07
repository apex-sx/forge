/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")
public class Tuple2D<E extends Tuple2D<E>> extends Tuple1D<E> {

    public Tuple2D() {
        this(true, 0, 0);
    }

    public Tuple2D(double x, double y) {
        this(true, x, y);
    }

    public Tuple2D(double[] x) {
        this(true, x[0], x[1]);
    }
    public Tuple2D(E input) {
        super(input);
    }

    protected Tuple2D(boolean ignored, double... input) {
        super(ignored, input);
    }

    public final double y() {
        return this.points()[1];
    }

    public final void y(double y) {
         this.points()[1] = y;
    }

    public final void set(double x, double y) {
        this.points(new double[]{x, y});
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")

public class Tuple4D<E extends Tuple4D<E>> extends Tuple3D<E> {

    public Tuple4D() {
        this(true, 0, 0, 0, 0);
    }

    public Tuple4D(double x, double y, double z, double w) {
        this(true, x, y, z);
    }

    public Tuple4D(double[] x) {
        this(true, x[0], x[1], x[2], x[3]);
    }

    public Tuple4D(E input) {
        super(input);
    }

    protected Tuple4D(boolean ignored, double... input) {
        super(ignored, input);
    }

    public final double w() {
        return this.points()[3];
    }

    public final void w(double w) {
        this.points()[3] = w;
    }

    public final void set(double x, double y, double z, double w) {
        this.points(new double[]{x, y, z, w});
    }
}

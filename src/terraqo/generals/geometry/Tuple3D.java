/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")

public class Tuple3D<E extends Tuple3D<E>> extends Tuple2D<E> {
    public Tuple3D() {
        this(true, 0, 0, 0);
    }

    public Tuple3D(double x, double y, double z) {
        this(true, x, y, z);
    }

    public Tuple3D(double[] x) {
        this(true, x[0], x[1], x[2]);
    }

    public Tuple3D(E input) {
        super(input);
    }

    protected Tuple3D(boolean ignored, double... input) {
        super(ignored, input);
    }

    public final double z() {
        return this.points()[2];
    }

    public final void z(double z) {
        this.points()[2] = z;
    }


    public final void set(double x, double y, double z) {
        this.points(new double[]{x, y, z});
    }

    public void cross(E et, E eo) {
        double x = et.y() * eo.z() - et.z() * eo.y();
        double y = et.z() * eo.x() - et.x() * eo.z();
        double z = et.x() * eo.y() - et.y() * eo.x();
        set(x, y, z);
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")

public class Tuple1D<E extends Tuple1D<E>> extends DoubleTuple<E> {

    public Tuple1D() {
        this(true, 0);
    }

    public Tuple1D(double x) {
        this(true, x);
    }

    public Tuple1D(double[] x) {
        this(true, x[0]);
    }

    public Tuple1D(E input) {
        super(input);
    }

    protected Tuple1D(boolean ignored, double... input) {
        super(input);
    }


    public final double x() {
        return this.points()[0];
    }

    public final void x(double x) {
         this.points()[1] = x;
    }

    public final void set(double x) {
        this.points(new double[]{x});
    }
}

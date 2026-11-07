/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")

public class Tuple4F<E extends Tuple4F<E>> extends Tuple3F<E> {

    public Tuple4F() {
        this(true, 0, 0, 0, 0);
    }

    public Tuple4F(float x, float y, float z, float w) {
        this(true, x, y, z);
    }

    public Tuple4F(float[] x) {
        this(true, x[0], x[1], x[2], x[3]);
    }

    public Tuple4F(E input) {
        super(input);
    }

    protected Tuple4F(boolean ignored, float... input) {
        super(ignored, input);
    }

    public final float w() {
        return this.points()[3];
    }

    public final void w(float w) {
        this.points()[3] = w;
    }

    public final void set(float x, float y, float z, float w) {
        this.points(new float[]{x, y, z, w});
    }
}

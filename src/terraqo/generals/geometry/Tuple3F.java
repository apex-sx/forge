/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")

public class Tuple3F<E extends Tuple3F<E>> extends Tuple2F<E> {
    public Tuple3F() {
        this(true, 0, 0, 0);
    }

    public Tuple3F(float x, float y, float z) {
        this(true, x, y, z);
    }

    public Tuple3F(float[] x) {
        this(true, x[0], x[1], x[2]);
    }

    public Tuple3F(E input) {
        super(input);
    }

    protected Tuple3F(boolean ignored, float... input) {
        super(ignored, input);
    }

    public final float z() {
        return this.points()[2];
    }

    public final void z(float z) {
        this.points()[2] = z;
    }
    

    public final void set(float x, float y, float z) {
        this.points(new float[]{x, y, z});
    }

    public void cross(E et, E eo) {
        float x = et.y() * eo.z() - et.z() * eo.y();
        float y = et.z() * eo.x() - et.x() * eo.z();
        float z = et.x() * eo.y() - et.y() * eo.x();
        set(x, y, z);
    }
}

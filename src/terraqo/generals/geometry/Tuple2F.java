/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")
public class Tuple2F<E extends Tuple2F<E>> extends Tuple1F<E> {

    public Tuple2F() {
        this(true, 0, 0);
    }

    public Tuple2F(float x, float y) {
        this(true, x, y);
    }

    public Tuple2F(float[] x) {
        this(true, x[0], x[1]);
    }

    public Tuple2F(E input) {
        super(input);
    }

    protected Tuple2F(boolean ignored, float... input) {
        super(ignored, input);
    }

    public final float y() {
        return this.points()[1];
    }

    public final void y(float y) {
         this.points()[1] = y;
    }

    public final void set(float x, float y) {
        this.points(new float[]{x, y});
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")

public class Tuple1F<E extends Tuple1F<E>> extends FloatTuple<E> {

    public Tuple1F() {
        this(true, 0);
    }

    public Tuple1F(float x) {
        this(true, x);
    }

    public Tuple1F(float[] x) {
        this(true, x[0]);
    }

    public Tuple1F(E input) {
        super(input);
    }

    protected Tuple1F(boolean ignored, float... input) {
        super(input);
    }

    public final float x() {
        return this.points()[0];
    }

    public final void x(float x) {
        this.points()[1] = x;
    }

    public final void set(float x) {
        this.points(new float[]{x});
    }
}

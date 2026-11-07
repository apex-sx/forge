/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

/**
 * Vector math package, converted to look similar to javax.vecmath.
 */
public class AxisAngle4f extends Tuple3F<AxisAngle4f> {
    public float angle;

    public AxisAngle4f() {
        this(0, 0, 0, 0);
    }

    public AxisAngle4f(float x, float y, float z, float angle) {
        super(x, y, z);
        this.angle = angle;
    }

    public AxisAngle4f(float[] input) {
        super(input);
        this.angle = input[3];
    }

    public AxisAngle4f(AxisAngle4f input) {
        super(input);
        this.angle = input.angle;
    }
}

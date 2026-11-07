/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

import java.awt.*;

/**
 * Vector math package, converted to look similar to javax.vecmath.
 */
public class Color4f extends Tuple4F<Color4f> {

    public Color4f() {
    }

    public Color4f(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    public Color4f(float[] x) {
        super(x);
    }

    public Color4f(Color4f input) {
        super(input);
    }

    public Color4f(Color c) {
        set(c);
    }

    public void set(Color c) {
        set(c.getRGBComponents(null));
    }

    public Color get() {
        return new Color(x(), y(), z(), w());
    }
}

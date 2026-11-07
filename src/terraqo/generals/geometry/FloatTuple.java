/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

import com.github.potatoxf.generals.function.FloatFunction1;
import com.github.potatoxf.generals.function.FloatFunction2;
import com.github.potatoxf.generals.function.FloatFunction3;
import com.github.potatoxf.generals.symbolic.This;

import java.util.Objects;

/**
 * @author potatoxf
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "unchecked", "ForLoopReplaceableByForEach", "DuplicatedCode"})
public abstract class FloatTuple<E extends FloatTuple<E>> implements This<E> {
    private final float[] points;

    protected FloatTuple(float[] points) {
        this.points = Objects.requireNonNull(points);
    }

    protected FloatTuple(E input) {
        this.points = input.points();
    }

    public final void set(float[] input) {
        if (points.length != input.length) {
            throw new IllegalArgumentException();
        }
        System.arraycopy(input, 0, points, 0, input.length);
    }

    public final void set(E e) {
        eachPoint(e, ((v, t) -> t));
    }

    public final void get(float[] input) {
        if (points.length != input.length) {
            throw new IllegalArgumentException();
        }
        System.arraycopy(points, 0, input, 0, input.length);
    }

    public final void get(E e) {
        e.eachPoint((E) this, ((v, t) -> t));
    }

    public final void absolute() {
        eachPoint(Math::abs);
    }

    public final void absolute(E et) {
        eachPoint(et, (v, t) -> Math.abs(t));
    }

    public final void negate() {
        eachPoint(v -> -v);
    }

    public final void negate(E et) {
        eachPoint(et, (v, t) -> -t);
    }

    public final void scale(float s) {
        eachPoint((v) -> s * v);
    }

    public void add(float vs) {
        eachPoint((v) -> v + vs);
    }

    public final void add(E et) {
        eachPoint(et, ((v, t) -> v + t));
    }

    public final void add(E et, E eo) {
        eachPoint(et, eo, (v, t, o) -> t + o);
    }

    public final void addScale(E et, float ets) {
        eachPoint(et, (v, t) -> v + ets * t);
    }

    public final void addScale(float vs, E et, float ets) {
        eachPoint(et, (v, t) -> v * vs + t * ets);
    }

    public final void addScale(E et, float ets, E eo, float eos) {
        eachPoint(et, eo, (v, t, o) -> t * ets + o * eos);
    }

    public void mul(float vs) {
        eachPoint((v) -> v * vs);
    }

    public final void mul(E et) {
        eachPoint(et, (v, t) -> v * t);
    }

    public final void mul(E et, E eo) {
        eachPoint(et, eo, (v, t, o) -> t * o);
    }

    public final void mulScale(E et, float ets) {
        eachPoint(et, (v, t) -> v * t * ets);
    }

    public final void mulScale(float vs, E et, float ets) {
        eachPoint(et, (v, t) -> v * vs * t * ets);
    }

    public final void mulScale(E et, float ets, E eo, float eos) {
        eachPoint(et, eo, (v, t, o) -> t * ets * o * eos);
    }

    public void sub(float vs) {
        eachPoint((v) -> v - vs);
    }

    public final void sub(E et) {
        eachPoint(et, (v, t) -> v - t);
    }

    public final void sub(E et, E eo) {
        eachPoint(et, eo, (v, t, o) -> t - o);
    }

    public final void subScale(E et, float ets) {
        eachPoint(et, (v, t) -> v - t * ets);
    }

    public final void subScale(float vs, E et, float ets) {
        eachPoint(et, (v, t) -> v * vs - t * ets);
    }

    public final void subScale(E et, float ets, E eo, float eos) {
        eachPoint(et, eo, (v, t, o) -> t * ets - o * eos);
    }

    public void div(float vs) {
        eachPoint((v) -> v / vs);
    }

    public final void interpolate(E et, float alpha) {
        eachPoint(et, (v, t) -> (1 - alpha) * v + alpha * t);
    }

    public final void clamp(float min, float max) {
        eachPoint((v) -> Math.min(Math.max(v, min), max));
    }

    public final float distance(E et) {
        return distance(et, false);
    }

    public final float distance(E et, boolean squared) {
        float value = sumEachPoint(et, (v, t) -> (v - t) * (v - t));
        return squared ? value : ((Number) Math.sqrt(value)).floatValue();
    }

    public final float pointLength(E et) {
        return sumEachPoint(et, (v, t) -> Math.abs(v - t));
    }

    public final float angle(E et) {
        float value = dotProduct(et) / (modulo() * et.modulo());
        return ((Number) Math.acos(value)).floatValue();
    }

    public final float modulo() {
        float value = sumEachPoint(v -> v * v);
        return ((Number) Math.sqrt(value)).floatValue();
    }

    public final float dotProduct(E et) {
        return sumEachPoint(et, (v, t) -> v * t);
    }

    public final void toUnitVector() {
        float modulo = modulo();
        eachPoint(v -> v / modulo);
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (points.length > 0) {
            sb.append(points[0]);
        }
        for (int i = 1; i < points.length; i++) {
            sb.append(", ").append(points[0]);

        }
        sb.append("]");
        return sb.toString();
    }

    protected final float[] points() {
        return this.points;
    }

    protected final E points(float[] points) {
        System.arraycopy(this.points, 0, points, 0, this.points.length);
        return this$();
    }

    protected final void eachPoint(FloatFunction1 function) {
        float[] vs = points();
        float[] array = new float[vs.length];
        for (int i = 0; i < vs.length; i++) {
            array[i] = function.apply(vs[i]);
        }
        points(array);
    }

    protected final void eachPoint(E et, FloatFunction2 function) {
        float[] vs = points();
        float[] ts = et.points();
        int length = vs.length;
        if (length != ts.length) {
            throw new IllegalArgumentException();
        }
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = function.apply(vs[i], ts[i]);
        }
        points(array);
    }

    protected final void eachPoint(E et, E eo, FloatFunction3 function) {
        float[] vs = points();
        float[] ts = et.points();
        float[] os = eo.points();
        int length = vs.length;
        if (length != ts.length || length != os.length) {
            throw new IllegalArgumentException();
        }
        float[] array = new float[length];
        for (int i = 0; i < length; i++) {
            array[i] = function.apply(vs[i], ts[i], os[i]);
        }
        points(array);
    }

    protected final float sumEachPoint(FloatFunction1 function) {
        float[] vs = points();
        float value = 0;
        for (int i = 0; i < vs.length; i++) {
            value += function.apply(vs[i]);
        }
        return value;
    }

    protected final float sumEachPoint(E e, FloatFunction2 function) {
        float[] vs = points();
        float[] ts = e.points();
        int length = vs.length;
        if (length != ts.length) {
            throw new IllegalArgumentException();
        }
        float value = 0;
        for (int i = 0; i < length; i++) {
            value += function.apply(vs[i], ts[i]);
        }
        return value;
    }

    protected final float sumEachPoint(E e, E o, FloatFunction3 function) {
        float[] vs = points();
        float[] ts = e.points();
        float[] os = o.points();
        int length = vs.length;
        if (length != ts.length || length != os.length) {
            throw new IllegalArgumentException();
        }
        float value = 0;
        for (int i = 0; i < length; i++) {
            value += function.apply(vs[i], ts[i], os[i]);
        }
        return value;
    }
}

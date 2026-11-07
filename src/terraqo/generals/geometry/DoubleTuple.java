/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.geometry;

import com.github.potatoxf.generals.function.DoubleFunction1;
import com.github.potatoxf.generals.function.DoubleFunction2;
import com.github.potatoxf.generals.function.DoubleFunction3;
import com.github.potatoxf.generals.symbolic.This;

import java.util.Objects;

/**
 * @author potatoxf
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "unchecked", "ForLoopReplaceableByForEach", "DuplicatedCode"})
public abstract class DoubleTuple<E extends DoubleTuple<E>> implements This<E> {
    private final double[] points;

    protected DoubleTuple(double[] points) {
        this.points = Objects.requireNonNull(points);
    }

    protected DoubleTuple(E input) {
        this.points = input.points();
    }

    public final void set(double[] input) {
        if (points.length != input.length) {
            throw new IllegalArgumentException();
        }
        System.arraycopy(input, 0, points, 0, input.length);
    }

    public final void set(E e) {
        eachPoint(e, ((v, t) -> t));
    }

    public final void get(double[] input) {
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

    public final void scale(double s) {
        eachPoint((v) -> s * v);
    }

    public void add(double vs) {
        eachPoint((v) -> v + vs);
    }

    public final void add(E et) {
        eachPoint(et, ((v, t) -> v+t));
    }

    public final void add(E et, E eo) {
        eachPoint(et, eo, (v, t, o) -> t + o);
    }

    public final void addScale(E et, double ets) {
        eachPoint(et, (v, t) -> v + ets * t);
    }

    public final void addScale(double vs, E et, double ets) {
        eachPoint(et, (v, t) -> v * vs + t * ets);
    }

    public final void addScale(E et, double ets, E eo, double eos) {
        eachPoint(et, eo, (v, t, o) -> t * ets + o * eos);
    }

    public void mul(double vs) {
        eachPoint((v) -> v * vs);
    }

    public final void mul(E et) {
        eachPoint(et, (v, t) -> v * t);
    }

    public final void mul(E et, E eo) {
        eachPoint(et, eo, (v, t, o) -> t * o);
    }

    public final void mulScale(E et, double ets) {
        eachPoint(et, (v, t) -> v * t * ets);
    }

    public final void mulScale(double vs, E et, double ets) {
        eachPoint(et, (v, t) -> v * vs * t * ets);
    }

    public final void mulScale(E et, double ets, E eo, double eos) {
        eachPoint(et, eo, (v, t, o) -> t * ets * o * eos);
    }

    public void sub(double vs) {
        eachPoint((v) -> v - vs);
    }

    public final void sub(E et) {
        eachPoint(et, (v, t) -> v - t);
    }

    public final void sub(E et, E eo) {
        eachPoint(et, eo, (v, t, o) -> t - o);
    }

    public final void subScale(E et, double ets) {
        eachPoint(et, (v, t) -> v - t * ets);
    }

    public final void subScale(double vs, E et, double ets) {
        eachPoint(et, (v, t) -> v * vs - t * ets);
    }

    public final void subScale(E et, double ets, E eo, double eos) {
        eachPoint(et, eo, (v, t, o) -> t * ets - o * eos);
    }

    public void div(double vs) {
        eachPoint((v) -> v / vs);
    }

    public final void interpolate(E et, double alpha) {
        eachPoint(et, (v, t) -> (1 - alpha) * v + alpha * t);
    }

    public final void clamp(double min, double max) {
        eachPoint((v) -> Math.min(Math.max(v, min), max));
    }

    public final double distance(E et) {
        return distance(et, false);
    }

    public final double distance(E et, boolean squared) {
        double value = sumEachPoint(et, (v, t) -> (v - t) * (v - t));
        return squared ? value : Math.sqrt(value);
    }

    public final double pointLength(E et) {
        return sumEachPoint(et, (v, t) -> Math.abs(v - t));
    }

    public final double angle(E et) {
        return Math.acos(dotProduct(et) / (modulo() * et.modulo()));
    }

    public final double modulo() {
        return Math.sqrt(sumEachPoint(v -> v * v));
    }

    public final double dotProduct(E et) {
        return sumEachPoint(et, (v, t) -> v * t);
    }

    public final void toUnitVector() {
        double modulo = modulo();
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

    protected final double[] points() {
        return this.points;
    }

    protected final E points(double[] points) {
        System.arraycopy(this.points, 0, points, 0, this.points.length);
        return this$();
    }

    protected final void eachPoint(DoubleFunction1 function) {
        double[] vs = points();
        double[] array = new double[vs.length];
        for (int i = 0; i < vs.length; i++) {
            array[i] = function.apply(vs[i]);
        }
        points(array);
    }

    protected final void eachPoint(E et, DoubleFunction2 function) {
        double[] vs = points();
        double[] ts = et.points();
        int length = vs.length;
        if (length != ts.length) {
            throw new IllegalArgumentException();
        }
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            array[i] = function.apply(vs[i], ts[i]);
        }
        points(array);
    }

    protected final void eachPoint(E et, E eo, DoubleFunction3 function) {
        double[] vs = points();
        double[] ts = et.points();
        double[] os = eo.points();
        int length = vs.length;
        if (length != ts.length || length != os.length) {
            throw new IllegalArgumentException();
        }
        double[] array = new double[length];
        for (int i = 0; i < length; i++) {
            array[i] = function.apply(vs[i], ts[i], os[i]);
        }
        points(array);
    }

    protected final double sumEachPoint(DoubleFunction1 function) {
        double[] vs = points();
        double value = 0;
        for (int i = 0; i < vs.length; i++) {
            value += function.apply(vs[i]);
        }
        return value;
    }

    protected final double sumEachPoint(E e, DoubleFunction2 function) {
        double[] vs = points();
        double[] ts = e.points();
        int length = vs.length;
        if (length != ts.length) {
            throw new IllegalArgumentException();
        }
        double value = 0;
        for (int i = 0; i < length; i++) {
            value += function.apply(vs[i], ts[i]);
        }
        return value;
    }

    protected final double sumEachPoint(E e, E o, DoubleFunction3 function) {
        double[] vs = points();
        double[] ts = e.points();
        double[] os = o.points();
        int length = vs.length;
        if (length != ts.length || length != os.length) {
            throw new IllegalArgumentException();
        }
        double value = 0;
        for (int i = 0; i < length; i++) {
            value += function.apply(vs[i], ts[i], os[i]);
        }
        return value;
    }
}

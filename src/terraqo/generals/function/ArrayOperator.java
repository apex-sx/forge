/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.function;

import com.github.potatoxf.generals.Kit;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.function.Function;
/**
 * 数组操作
 *
 * @param <A> 数组
 * @param <E> 元素类型
 * @author potatoxf
 */
@SuppressWarnings({"unchecked", "unused"})
public final class ArrayOperator<A, E> {
    public static final ArrayOperator<boolean[], Boolean> AO_BOOLEAN = of(a -> a.length, boolean[]::new, (a, i) -> a[i], (a, i, v) -> a[i] = v);
    public static final ArrayOperator<char[], Character> AO_CHAR = of(a -> a.length, char[]::new, (a, i) -> a[i], (a, i, v) -> a[i] = v);
    public static final ArrayOperator<byte[], Byte> AO_BYTE = of(a -> a.length, byte[]::new, (a, i) -> a[i], (a, i, v) -> a[i] = v);
    public static final ArrayOperator<short[], Short> AO_SHORT = of(a -> a.length, short[]::new, (a, i) -> a[i], (a, i, v) -> a[i] = v);
    public static final ArrayOperator<int[], Integer> AO_INT = of(a -> a.length, int[]::new, (a, i) -> a[i], (a, i, v) -> a[i] = v);
    public static final ArrayOperator<long[], Long> AO_LONG = of(a -> a.length, long[]::new, (a, i) -> a[i], (a, i, v) -> a[i] = v);
    public static final ArrayOperator<float[], Float> AO_FLOAT = of(a -> a.length, float[]::new, (a, i) -> a[i], (a, i, v) -> a[i] = v);
    public static final ArrayOperator<double[], Double> AO_DOUBLE = of(a -> a.length, double[]::new, (a, i) -> a[i], (a, i, v) -> a[i] = v);
    private static final Map<Class<?>, ArrayOperator<?, ?>> CATEGORY = Kit.unmodifiableMap(
            new Class<?>[]{Kit.T_AZ, Kit.T_AC, Kit.T_AB, Kit.T_AS, Kit.T_AI, Kit.T_AJ, Kit.T_AF, Kit.T_AD},
            new ArrayOperator<?, ?>[]{AO_BOOLEAN, AO_CHAR, AO_BYTE, AO_SHORT, AO_INT, AO_LONG, AO_FLOAT, AO_DOUBLE}
    );
    public static final ArrayOperator<?, ?> AO_OBJECT = of(null);
    private final Protractor<A> p;
    private final Creator<A> c;
    private final Setter<A, E> s;
    private final Getter<A, E> g;
    private final Class<?> mt, ot;
    private ArrayOperator(Protractor<A> p, Creator<A> c, Getter<A, E> g, Setter<A, E> s, Class<?> mt, Class<?> ot) {
        this.p = p;
        this.c = c;
        this.g = g;
        this.s = s;
        this.mt = mt;
        this.ot = ot;
    }
    public static <E> ArrayOperator<E[], E> of() {
        return (ArrayOperator<E[], E>) AO_OBJECT;
    }
    public static <E> ArrayOperator<E[], E> of(Class<?> type) {
        Creator<E[]> creator;
        if (type != null && type.isArray()) {
            ArrayOperator<?, ?> arrayOperator = CATEGORY.get(type);
            if (arrayOperator != null) return (ArrayOperator<E[], E>) arrayOperator;
            creator = l -> (E[]) Array.newInstance(type.getComponentType(), l);
        } else if (type == null) {
            creator = (Creator<E[]>) Creator.NOT_SUPPORT;
        } else {
            creator = l -> (E[]) Array.newInstance(type, l);
        }
        return of(a -> a.length, creator, (a, i) -> a[i], (a, i, v) -> a[i] = v);
    }
    private static <A, E> ArrayOperator<A, E> of(Protractor<A> p, Creator<A> c, Getter<A, E> g, Setter<A, E> s) {
        Class<?> mt;
        try {
            mt = c.apply(0).getClass().getComponentType();
        } catch (Throwable e) {
            mt = Object.class;
        }
        if (Kit.PRIMITIVE_MAPPING.containsKey(mt)) {
            Class<?> ot = Kit.PRIMITIVE_MAPPING.get(mt);
            return new ArrayOperator<>(p, c, g, s, mt, ot);
        } else {
            return new ArrayOperator<>(p, c, g, s, mt, mt);
        }
    }
    public static <A, E> Boolean[] copyToObjectArray(A a, ArrayOperator<A, E> ao, ToPrimitive.Z<E> z) {
        return ArrayOperator.doCopy(a, ao, ArrayOperator.of(Kit.T_WZ), z.toFunction());
    }
    public static <A, E> Character[] copyToObjectArray(A a, ArrayOperator<A, E> ao, ToPrimitive.C<E> c) {
        return ArrayOperator.doCopy(a, ao, ArrayOperator.of(Kit.T_WC), c.toFunction());
    }
    public static <A, E> Byte[] copyToObjectArray(A a, ArrayOperator<A, E> ao, ToPrimitive.B<E> b) {
        return ArrayOperator.doCopy(a, ao, ArrayOperator.of(Kit.T_WB), b.toFunction());
    }
    public static <A, E> Short[] copyToObjectArray(A a, ArrayOperator<A, E> ao, ToPrimitive.S<E> s) {
        return ArrayOperator.doCopy(a, ao, ArrayOperator.of(Kit.T_WS), s.toFunction());
    }
    public static <A, E> Integer[] copyToObjectArray(A a, ArrayOperator<A, E> ao, ToPrimitive.I<E> i) {
        return ArrayOperator.doCopy(a, ao, ArrayOperator.of(Kit.T_WI), i.toFunction());
    }
    public static <A, E> Long[] copyToObjectArray(A a, ArrayOperator<A, E> ao, ToPrimitive.J<E> j) {
        return ArrayOperator.doCopy(a, ao, ArrayOperator.of(Kit.T_WJ), j.toFunction());
    }
    public static <A, E> Float[] copyToObjectArray(A a, ArrayOperator<A, E> ao, ToPrimitive.F<E> f) {
        return ArrayOperator.doCopy(a, ao, ArrayOperator.of(Kit.T_WF), f.toFunction());
    }
    public static <A, E> Double[] copyToObjectArray(A a, ArrayOperator<A, E> ao, ToPrimitive.D<E> d) {
        return ArrayOperator.doCopy(a, ao, ArrayOperator.of(Kit.T_WD), d.toFunction());
    }
    public static <A, E> boolean[] copyToPrimitiveArray(A a, ArrayOperator<A, E> ao, ToPrimitive.Z<E> z) {
        return ArrayOperator.doCopy(a, ao, AO_BOOLEAN, z.toFunction());
    }
    public static <A, E> char[] copyToPrimitiveArray(A a, ArrayOperator<A, E> ao, ToPrimitive.C<E> c) {
        return ArrayOperator.doCopy(a, ao, AO_CHAR, c.toFunction());
    }
    public static <A, E> byte[] copyToPrimitiveArray(A a, ArrayOperator<A, E> ao, ToPrimitive.B<E> b) {
        return ArrayOperator.doCopy(a, ao, AO_BYTE, b.toFunction());
    }
    public static <A, E> short[] copyToPrimitiveArray(A a, ArrayOperator<A, E> ao, ToPrimitive.S<E> s) {
        return ArrayOperator.doCopy(a, ao, AO_SHORT, s.toFunction());
    }
    public static <A, E> int[] copyToPrimitiveArray(A a, ArrayOperator<A, E> ao, ToPrimitive.I<E> i) {
        return ArrayOperator.doCopy(a, ao, AO_INT, i.toFunction());
    }
    public static <A, E> long[] copyToPrimitiveArray(A a, ArrayOperator<A, E> ao, ToPrimitive.J<E> j) {
        return ArrayOperator.doCopy(a, ao, AO_LONG, j.toFunction());
    }
    public static <A, E> float[] copyToPrimitiveArray(A a, ArrayOperator<A, E> ao, ToPrimitive.F<E> f) {
        return ArrayOperator.doCopy(a, ao, AO_FLOAT, f.toFunction());
    }
    public static <A, E> double[] copyToPrimitiveArray(A a, ArrayOperator<A, E> ao, ToPrimitive.D<E> d) {
        return ArrayOperator.doCopy(a, ao, AO_DOUBLE, d.toFunction());
    }
    public static <A, E> A copy(Object a, ArrayOperator<Object, Object> o, ArrayOperator<A, ? super E> n, Function<Object, ? extends E> c) {
        return doCopy(a, o, n, v -> (E) c.apply(v));
    }
    private static <A, E, NA, NE> NA doCopy(A a, ArrayOperator<A, ? extends E> o, ArrayOperator<NA, ? super NE> n, Function<? super E, ? extends NE> c) {
        int l = o.p.apply(a);
        NA r = n.c.apply(l);
        for (int i = 0; i < l; i++) {
            E apply = o.g.apply(a, i);
            n.s.apply(r, i, c.apply(apply));
        }
        return r;
    }
    public Protractor<A> protractor() {
        return p;
    }
    public Creator<A> creator() {
        return c;
    }
    public Getter<A, E> getter() {
        return g;
    }
    public Setter<A, E> setter() {
        return s;
    }
    public Class<?> mainType() {
        return mt;
    }
    public Class<?> otherType() {
        return ot;
    }
    public <NA> A createMainArray(int size) {
        if (size < 0) size = 0;
        if (mainType() != null) {
            return creator().apply(size);
        }
        throw new IllegalArgumentException();
    }
    public <NA> NA createOtherArray(int size) {
        if (size < 0) size = 0;
        if (otherType() != null) {
            return (NA) ArrayOperator.of(otherType()).creator().apply(size);
        }
        throw new IllegalArgumentException();
    }
    public <NA> A toMainArray(NA a) {
        if (mt == ot) return (A) a;
        return ArrayOperator.doCopy(a, (ArrayOperator<NA, E>) ArrayOperator.of(ot), this, e -> e);
    }
    public <NA> NA toOtherArray(A a) {
        if (mt == ot) return (NA) a;
        return (NA) ArrayOperator.doCopy(a, this, ArrayOperator.of(ot), e -> e);
    }
    public A resize(A array, int capacity, int size) {
        final int oldLength = p.apply(array);
        if (size < oldLength) size = oldLength;
        final int min = 4, max = 1 << 29;
        if (capacity > max / 3) capacity = max;
        if (capacity <= 2 * min / 3) capacity = min;
        capacity = Integer.highestOneBit(capacity + (capacity << 1));
        if (array == null) c.apply(capacity);
        int newLength = capacity * 2;
        if (oldLength == 2 * max) { // can't expand any further
            if (size >= max - 1) {
                throw new IllegalStateException("Capacity exhausted.");
            }
        }
        return oldLength >= newLength ? array : c.apply(newLength);
    }
    @FunctionalInterface
    public interface Protractor<A> {
        int apply(A array);
    }
    @FunctionalInterface
    public interface Getter<A, E> {
        E apply(A array, int index);
    }
    @FunctionalInterface
    public interface Setter<A, E> {
        void apply(A array, int index, E value);
    }
    @FunctionalInterface
    public interface Creator<A> {
        Creator<?> NOT_SUPPORT = length -> {
            throw new UnsupportedOperationException();
        };
        A apply(int length);
    }
}

/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.function;
import java.util.function.Function;
/**
 * 转原生类型
 *
 * @author potatoxf
 */
public interface ToPrimitive {
    @FunctionalInterface
    interface Z<T> extends ToPrimitive {
        boolean apply(T value);
        default Function<T, Boolean> toFunction() {
            return this::apply;
        }
    }
    @FunctionalInterface
    interface C<T> extends ToPrimitive {
        char apply(T value);
        default Function<T, Character> toFunction() {
            return this::apply;
        }
    }
    @FunctionalInterface
    interface B<T> extends ToPrimitive {
        byte apply(T value);
        default Function<T, Byte> toFunction() {
            return this::apply;
        }
    }
    @FunctionalInterface
    interface S<T> extends ToPrimitive {
        short apply(T value);
        default Function<T, Short> toFunction() {
            return this::apply;
        }
    }
    @FunctionalInterface
    interface I<T> extends ToPrimitive {
        int apply(T value);
        default Function<T, Integer> toFunction() {
            return this::apply;
        }
    }
    @FunctionalInterface
    interface J<T> extends ToPrimitive {
        long apply(T value);
        default Function<T, Long> toFunction() {
            return this::apply;
        }
    }
    @FunctionalInterface
    interface F<T> extends ToPrimitive {
        float apply(T value);
        default Function<T, Float> toFunction() {
            return this::apply;
        }
    }
    @FunctionalInterface
    interface D<T> extends ToPrimitive {
        double apply(T value);
        default Function<T, Double> toFunction() {
            return this::apply;
        }
    }
}

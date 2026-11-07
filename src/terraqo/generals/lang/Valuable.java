/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 保存值的容器
 *
 * @author potatoxf
 */
@SuppressWarnings("unchecked")
public final class Valuable<V> {
    private static final Valuable<?> NULL = new Valuable<>(null);
    private final Supplier<V> value;

    public static <V> Valuable<V> of(CompletableFuture<V> value, boolean waiting) {
        if (value == null) return (Valuable<V>) NULL;
        return new Valuable<>(() -> {
            try {
                if (waiting) {
                    return value.join();
                } else {
                    return value.isDone() && !value.isCompletedExceptionally() ? value.get() : null;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            } catch (ExecutionException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static <V> Valuable<V> of(Future<V> value) {
        if (value == null) return (Valuable<V>) NULL;
        return new Valuable<>(() -> {
            try {
                return value.isDone() ? value.get() : null;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            } catch (ExecutionException e) {
                throw new CompletionException(e);
            }
        });
    }

    public static <V> Valuable<V> of(Optional<V> value) {
        return value == null || !value.isPresent() ? (Valuable<V>) NULL : new Valuable<>(value::get);
    }

    public static <V> Valuable<V> of(Callable<V> value) {
        if (value == null) return (Valuable<V>) NULL;
        return new Valuable<>(() -> {
            try {
                return value.call();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static <V> Valuable<V> of(Supplier<V> value) {
        if (value == null) return (Valuable<V>) NULL;
        return new Valuable<>(value);
    }

    public static <V> Valuable<V> of(V value) {
        if (value == null) return (Valuable<V>) NULL;
        return new Valuable<>(() -> value);
    }

    private Valuable(Supplier<V> value) {
        this.value = value;
    }

    public V getValue() {
        return value.get();
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.util.Objects;
import java.util.Spliterator;

/**
 * Adapts an {@link Spliterator} as an {@link IOSpliterator}.
 *
 * @param <T> the type of the stream elements.
 */
final class IOSpliteratorAdapter<T> implements IOSpliterator<T> {

    static <E> IOSpliteratorAdapter<E> adapt(final Spliterator<E> delegate) {
        return new IOSpliteratorAdapter<>(delegate);
    }

    private final Spliterator<T> delegate;

    IOSpliteratorAdapter(final Spliterator<T> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public Spliterator<T> unwrap() {
        return delegate;
    }

}

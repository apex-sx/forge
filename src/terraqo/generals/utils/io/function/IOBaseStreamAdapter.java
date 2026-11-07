/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.util.Objects;
import java.util.stream.BaseStream;

/**
 * Abstracts an {@link IOBaseStream} implementation.
 *
 * Keep package-private for now.
 *
 * @param <T> the type of the stream elements.
 * @param <S> the type of the stream extending {@code IOBaseStream}.
 */
abstract class IOBaseStreamAdapter<T, S extends IOBaseStream<T, S, B>, B extends BaseStream<T, B>> implements IOBaseStream<T, S, B> {

    /**
     * The underlying base stream.
     */
    private final B delegate;

    /**
     * Constructs an instance.
     *
     * @param delegate the delegate.
     */
    IOBaseStreamAdapter(final B delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public B unwrap() {
        return delegate;
    }

}

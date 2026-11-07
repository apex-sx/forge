/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.util.stream.Stream;

/**
 * Adapts an {@link Stream} as an {@link IOStream}.
 *
 * Keep package-private for now.
 *
 * @param <T> the type of the stream elements.
 */
final class IOStreamAdapter<T> extends IOBaseStreamAdapter<T, IOStream<T>, Stream<T>> implements IOStream<T> {

    static <T> IOStream<T> adapt(final Stream<T> delegate) {
        return delegate != null ? new IOStreamAdapter<>(delegate) : IOStream.empty();
    }

    private IOStreamAdapter(final Stream<T> delegate) {
        super(delegate);
    }

    @Override
    public IOStream<T> wrap(final Stream<T> delegate) {
        return unwrap() == delegate ? this : adapt(delegate);
    }

}

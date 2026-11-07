/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.Objects;

/**
 * An {@link Iterator} for a {@link IOIterator} that throws {@link UncheckedIOException} instead of {@link IOException}.
 *
 * Keep package-private for now.
 *
 * @param <E> the type of elements returned by this iterator.
 */
final class UncheckedIOIterator<E> implements Iterator<E> {

    private final IOIterator<E> delegate;

    /**
     * Constructs a new instance.
     *
     * @param delegate The delegate
     */
    UncheckedIOIterator(final IOIterator<E> delegate) {
        this.delegate = Objects.requireNonNull(delegate, "delegate");
    }

    @Override
    public boolean hasNext() {
        return Uncheck.get(delegate::hasNext);
    }

    @Override
    public E next() {
        return Uncheck.get(delegate::next);
    }

    @Override
    public void remove() {
        Uncheck.run(delegate::remove);
    }
}

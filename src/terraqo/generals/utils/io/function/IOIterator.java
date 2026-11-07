/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Like {@link Iterator} but throws {@link IOException}.
 *
 * @param <E> the type of elements returned by this iterator.
 * @author potatoxf
 */
public interface IOIterator<E> {

    /**
     * Adapts the given Iterable as an IOIterator.
     *
     * @param <E> the type of the stream elements.
     * @param iterable The iterable to adapt
     * @return A new IOIterator
     * @author potatoxf
     */
    static <E> IOIterator<E> adapt(final Iterable<E> iterable) {
        return IOIteratorAdapter.adapt(iterable.iterator());
    }

    /**
     * Adapts the given Iterator as an IOIterator.
     *
     * @param <E> the type of the stream elements.
     * @param iterator The iterator to adapt
     * @return A new IOIterator
     */
    static <E> IOIterator<E> adapt(final Iterator<E> iterator) {
        return IOIteratorAdapter.adapt(iterator);
    }

    /**
     * Creates an {@link Iterator} for this instance that throws {@link UncheckedIOException} instead of
     * {@link IOException}.
     *
     * @return an {@link UncheckedIOException} {@link Iterator}.
     */
    default Iterator<E> asIterator() {
        return new UncheckedIOIterator<>(this);
    }

    /**
     * Like {@link Iterator#forEachRemaining(Consumer)}.
     *
     * @param action See delegate.
     * @throws IOException if an I/O error occurs.
     */
    default void forEachRemaining(final IOConsumer<? super E> action) throws IOException {
        Objects.requireNonNull(action);
        while (hasNext()) {
            action.accept(next());
        }
    }

    /**
     * Like {@link Iterator#hasNext()}.
     *
     * @return See delegate.
     * @throws IOException if an I/O error occurs.
     */
    boolean hasNext() throws IOException;

    /**
     * Like {@link Iterator#next()}.
     *
     * @return See delegate.
     * @throws IOException if an I/O error occurs.
     * @throws NoSuchElementException if the iteration has no more elements
     */
    E next() throws IOException;

    /**
     * Like {@link Iterator#remove()}.
     *
     * @throws IOException if an I/O error occurs.
     */
    @SuppressWarnings("unused")
    default void remove() throws IOException {
        unwrap().remove();
    }

    /**
     * Unwraps this instance and returns the underlying {@link Iterator}.
     * <p>
     * Implementations may not have anything to unwrap and that behavior is undefined for now.
     * </p>
     * @return the underlying Iterator.
     */
    Iterator<E> unwrap();

}

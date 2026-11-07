/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * OutputStream implementation that writes the data to an {@link Appendable}
 * Object.
 * <p>
 * For example, can be used with any {@link Writer} or a {@link StringBuilder}
 * or {@link StringBuffer}.
 * </p>
 *
 * @author potatoxf
 * @see Appendable
 *
 * @param <T> The type of the {@link Appendable} wrapped by this AppendableOutputStream.
 */
public class AppendableOutputStream <T extends Appendable> extends OutputStream {

    private final T appendable;

    /**
     * Constructs a new instance with the specified appendable.
     *
     * @param appendable the appendable to write to
     */
    public AppendableOutputStream(final T appendable) {
        this.appendable = appendable;
    }

    /**
     * Gets the target appendable.
     *
     * @return the target appendable
     */
    public T getAppendable() {
        return appendable;
    }

    /**
     * Write a character to the underlying appendable.
     *
     * @param b the character to write
     * @throws IOException upon error
     */
    @Override
    public void write(final int b) throws IOException {
        appendable.append((char) b);
    }

}

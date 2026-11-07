/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.iterator;

import com.github.potatoxf.generals.utils.GeneralIO;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An Iterator over the lines in a {@link Reader}.
 * <p>
 * {@link LineIterator} holds a reference to an open {@link Reader}.
 * When you have finished with the iterator you should close the reader
 * to free internal resources. This can be done by closing the reader directly,
 * or by calling the {@link #close()}
 * method on the iterator.
 * <p>
 * The recommended usage pattern is:
 * <pre>
 * LineIterator it = FileUtils.lineIterator(file, StandardCharsets.UTF_8.name());
 * try {
 *   while (it.hasNext()) {
 *     String line = it.nextLine();
 *     // do something with line
 *   }
 * } finally {
 *   it.close();
 * }
 * </pre>
 *
 * @author potatoxf
 */
public class LineIterator implements Iterator<String>, Closeable {

    /** The reader that is being read. */
    private final BufferedReader bufferedReader;

    /** The current line. */
    private String cachedLine;

    /** A flag indicating if the iterator has been fully read. */
    private boolean finished;

    /**
     * Constructs an iterator of the lines for a {@link Reader}.
     *
     * @param reader the {@link Reader} to read from, not null
     * @throws NullPointerException if the reader is null
     */
    @SuppressWarnings("resource") // Caller closes Reader
    public LineIterator(final Reader reader) {
        Objects.requireNonNull(reader, "reader");
        if (reader instanceof BufferedReader) {
            bufferedReader = (BufferedReader) reader;
        } else {
            bufferedReader = new BufferedReader(reader);
        }
    }

    /**
     * Closes the underlying {@link Reader}.
     * This method is useful if you only want to process the first few
     * lines of a larger file. If you do not close the iterator
     * then the {@link Reader} remains open.
     * This method can safely be called multiple times.
     *
     * @throws IOException if closing the underlying {@link Reader} fails.
     */
    @Override
    public void close() throws IOException {
        finished = true;
        cachedLine = null;
        GeneralIO.close(bufferedReader);
    }

    /**
     * Indicates whether the {@link Reader} has more lines.
     * If there is an {@link IOException} then {@link #close()} will
     * be called on this instance.
     *
     * @return {@code true} if the Reader has more lines
     * @throws IllegalStateException if an IO exception occurs
     */
    @Override
    public boolean hasNext() {
        if (cachedLine != null) {
            return true;
        }
        if (finished) {
            return false;
        }
        try {
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    finished = true;
                    return false;
                }
                if (isValidLine(line)) {
                    cachedLine = line;
                    return true;
                }
            }
        } catch (final IOException ioe) {
            GeneralIO.close(this);
            throw new IllegalStateException(ioe);
        }
    }

    /**
     * Overridable method to validate each line that is returned.
     * This implementation always returns true.
     * @param line  the line that is to be validated
     * @return true if valid, false to remove from the iterator
     */
    protected boolean isValidLine(final String line) {
        return true;
    }

    /**
     * Returns the next line in the wrapped {@link Reader}.
     *
     * @return the next line from the input
     * @throws NoSuchElementException if there is no line to return
     */
    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        final String currentLine = cachedLine;
        cachedLine = null;
        return currentLine;
    }

    /**
     * Unsupported.
     *
     * @throws UnsupportedOperationException always
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }

}

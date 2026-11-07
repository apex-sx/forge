/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io.output;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import com.github.potatoxf.generals.utils.io.build.AbstractStreamBuilder;

/**
 * An {@link OutputStream} that writes to a {@link RandomAccessFile}.
 *
 * @author potatoxf
 */
public final class RandomAccessFileOutputStream extends OutputStream {

    // @formatter:off
    /**
     * Builds a new {@link RandomAccessFileOutputStream}.
     * <p>
     * For example:
     * </p>
     * <pre>{@code
     * RandomAccessFileOutputStream s = RandomAccessFileOutputStream.builder()
     *   .setFile("myfile.txt")
     *   .setOpenOptions(StandardOpenOption.SYNC)
     *   .get();}
     * </pre>
     * <p>
     * The only super's aspect used is buffer size.
     * </p>
     *
     * @see #get()
     */
    // @formatter:on
    public final static class Builder extends AbstractStreamBuilder<RandomAccessFileOutputStream, Builder> {

        /**
         * Use {@link RandomAccessFileOutputStream#builder()}.
         */
        private Builder() {
            setOpenOptions(StandardOpenOption.WRITE);
        }

        @SuppressWarnings("resource")
        @Override
        public RandomAccessFileOutputStream get() throws IOException {
            return new RandomAccessFileOutputStream(getRandomAccessFile());
        }

    }

    /**
     * Constructs a new {@link Builder}.
     *
     * @return a new {@link Builder}.
     */
    public static Builder builder() {
        return new Builder();
    }

    private final RandomAccessFile randomAccessFile;

    private RandomAccessFileOutputStream(final RandomAccessFile randomAccessFile) {
        this.randomAccessFile = Objects.requireNonNull(randomAccessFile);
    }

    @Override
    public void close() throws IOException {
        this.randomAccessFile.close();
        super.close();
    }

    @SuppressWarnings("resource")
    @Override
    public void flush() throws IOException {
        randomAccessFile.getChannel().force(true);
        super.flush();
    }

    @Override
    public void write(final int b) throws IOException {
        randomAccessFile.write(b);
    }

}

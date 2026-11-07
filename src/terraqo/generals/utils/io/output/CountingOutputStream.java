/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.OutputStream;

/**
 * A decorating output stream that counts the number of bytes that have passed
 * through the stream so far.
 * <p>
 * A typical use case would be during debugging, to ensure that data is being
 * written as expected.
 * </p>
 */
public class CountingOutputStream extends ProxyOutputStream {

    /** The count of bytes that have passed. */
    private long count;

    /**
     * Constructs a new CountingOutputStream.
     *
     * @param out  the OutputStream to write to
     */
    public CountingOutputStream(final OutputStream out) {
        super(out);
    }

    /**
     * Updates the count with the number of bytes that are being written.
     *
     * @param n number of bytes to be written to the stream
     * @author potatoxf
     */
    @Override
    protected synchronized void beforeWrite(final int n) {
        count += n;
    }

    /**
     * The number of bytes that have passed through this stream.
     * <p>
     * NOTE: This method is an alternative for {@code getCount()}.
     * It was added because that method returns an integer which will
     * result in incorrect count for files over 2GB.
     * </p>
     *
     * @return the number of bytes accumulated
     * @author potatoxf
     */
    public synchronized long getByteCount() {
        return this.count;
    }

    /**
     * Gets the number of bytes that have passed through this stream.
     * <p>
     * NOTE: From v1.3 this method throws an ArithmeticException if the
     * count is greater than can be expressed by an {@code int}.
     * See {@link #getByteCount()} for a method using a {@code long}.
     * </p>
     *
     * @return the number of bytes accumulated
     * @throws ArithmeticException if the byte count is too large
     */
    public int getCount() {
        final long result = getByteCount();
        if (result > Integer.MAX_VALUE) {
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int) result;
    }

    /**
     * Sets the byte count back to 0.
     * <p>
     * NOTE: This method is an alternative for {@code resetCount()}.
     * It was added because that method returns an integer which will
     * result in incorrect count for files over 2GB.
     * </p>
     *
     * @return the count previous to resetting
     * @author potatoxf
     */
    public synchronized long resetByteCount() {
        final long tmp = this.count;
        this.count = 0;
        return tmp;
    }

    /**
     * Sets the byte count back to 0.
     * <p>
     * NOTE: From v1.3 this method throws an ArithmeticException if the
     * count is greater than can be expressed by an {@code int}.
     * See {@link #resetByteCount()} for a method using a {@code long}.
     * </p>
     *
     * @return the count previous to resetting
     * @throws ArithmeticException if the byte count is too large
     */
    public int resetCount() {
        final long result = resetByteCount();
        if (result > Integer.MAX_VALUE) {
            throw new ArithmeticException("The byte count " + result + " is too large to be converted to an int");
        }
        return (int) result;
    }

}

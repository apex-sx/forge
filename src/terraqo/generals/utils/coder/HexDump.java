/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.coder;

import com.github.potatoxf.generals.Kit;
import com.github.potatoxf.generals.utils.io.output.CloseShieldOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Dumps data in hexadecimal format.
 * <p>
 * Provides a single function to take an array of bytes and display it
 * in hexadecimal form.
 * </p>
 * <p>
 * Provenance: POI.
 * </p>
 */
public class HexDump {

    private static final int[] SHIFTS = {28, 24, 20, 16, 12, 8, 4, 0};

    /**
     * Dumps an array of bytes to an Appendable. The output is formatted
     * for human inspection, with a hexadecimal offset followed by the
     * hexadecimal values of the next 16 bytes of data and the printable ASCII
     * characters (if any) that those bytes represent printed per each line
     * of output.
     *
     * @param data       the byte array to be dumped
     * @param appendable the Appendable to which the data is to be written
     * @throws IOException          is thrown if anything goes wrong writing
     *                              the data to appendable
     * @throws NullPointerException if the output appendable is null
     * @author potatoxf
     */
    public static void dump(final byte[] data, final Appendable appendable)
            throws IOException {
        dump(data, 0, appendable, 0, data.length);
    }

    /**
     * Dumps an array of bytes to an Appendable. The output is formatted
     * for human inspection, with a hexadecimal offset followed by the
     * hexadecimal values of the next 16 bytes of data and the printable ASCII
     * characters (if any) that those bytes represent printed per each line
     * of output.
     * <p>
     * The offset argument specifies the start offset of the data array
     * within a larger entity like a file or an incoming stream. For example,
     * if the data array contains the third kibibyte of a file, then the
     * offset argument should be set to 2048. The offset value printed
     * at the beginning of each line indicates where in that larger entity
     * the first byte on that line is located.
     * </p>
     *
     * @param data       the byte array to be dumped
     * @param offset     offset of the byte array within a larger entity
     * @param appendable the Appendable to which the data is to be written
     * @param index      initial index into the byte array
     * @param length     number of bytes to dump from the array
     * @throws IOException                    is thrown if anything goes wrong writing
     *                                        the data to appendable
     * @throws ArrayIndexOutOfBoundsException if the index or length is
     *                                        outside the data array's bounds
     * @throws NullPointerException           if the output appendable is null
     * @author potatoxf
     */
    public static void dump(final byte[] data, final long offset,
                            final Appendable appendable, final int index,
                            final int length)
            throws IOException, ArrayIndexOutOfBoundsException {
        Objects.requireNonNull(appendable, "appendable");
        if (index < 0 || index >= data.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "illegal index: " + index + " into array of length "
                            + data.length);
        }
        long display_offset = offset + index;
        final StringBuilder buffer = new StringBuilder(74);

        // TODO Use Objects.checkFromIndexSize(index, length, data.length) when upgrading to JDK9
        if (length < 0 || index + length > data.length) {
            throw new ArrayIndexOutOfBoundsException(String.format("Range [%s, %<s + %s) out of bounds for length %s", index, length, data.length));
        }

        final int endIndex = index + length;

        for (int j = index; j < endIndex; j += 16) {
            int chars_read = endIndex - j;

            if (chars_read > 16) {
                chars_read = 16;
            }
            dump(buffer, display_offset);
            buffer.append(' ');
            for (int k = 0; k < 16; k++) {
                if (k < chars_read) {
                    dump(buffer, data[k + j]);
                } else {
                    buffer.append("  ");
                }
                buffer.append(' ');
            }
            for (int k = 0; k < chars_read; k++) {
                if (data[k + j] >= ' ' && data[k + j] < 127) {
                    buffer.append((char) data[k + j]);
                } else {
                    buffer.append('.');
                }
            }
            buffer.append(System.lineSeparator());
            appendable.append(buffer);
            buffer.setLength(0);
            display_offset += chars_read;
        }
    }

    /**
     * Dumps an array of bytes to an OutputStream. The output is formatted
     * for human inspection, with a hexadecimal offset followed by the
     * hexadecimal values of the next 16 bytes of data and the printable ASCII
     * characters (if any) that those bytes represent printed per each line
     * of output.
     * <p>
     * The offset argument specifies the start offset of the data array
     * within a larger entity like a file or an incoming stream. For example,
     * if the data array contains the third kibibyte of a file, then the
     * offset argument should be set to 2048. The offset value printed
     * at the beginning of each line indicates where in that larger entity
     * the first byte on that line is located.
     * </p>
     * <p>
     * All bytes between the given index (inclusive) and the end of the
     * data array are dumped.
     * </p>
     *
     * @param data   the byte array to be dumped
     * @param offset offset of the byte array within a larger entity
     * @param stream the OutputStream to which the data is to be
     *               written
     * @param index  initial index into the byte array
     * @throws IOException                    is thrown if anything goes wrong writing
     *                                        the data to stream
     * @throws ArrayIndexOutOfBoundsException if the index is
     *                                        outside the data array's bounds
     * @throws NullPointerException           if the output stream is null
     */
    @SuppressWarnings("resource") // Caller closes stream
    public static void dump(final byte[] data, final long offset,
                            final OutputStream stream, final int index)
            throws IOException, ArrayIndexOutOfBoundsException {
        Objects.requireNonNull(stream, "stream");

        try (OutputStreamWriter out = new OutputStreamWriter(CloseShieldOutputStream.wrap(stream), Charset.defaultCharset())) {
            dump(data, offset, out, index, data.length - index);
        }
    }

    /**
     * Dumps a byte value into a StringBuilder.
     *
     * @param builder the StringBuilder to dump the value in
     * @param value   the byte value to be dumped
     */
    private static void dump(final StringBuilder builder, final byte value) {
        for (int j = 0; j < 2; j++) {
            builder.append(Kit.S_UPPER_HEX.charAt(value >> SHIFTS[j + 6] & 15));
        }
    }

    /**
     * Dumps a long value into a StringBuilder.
     *
     * @param builder the StringBuilder to dump the value in
     * @param value   the long value to be dumped
     */
    private static void dump(final StringBuilder builder, final long value) {
        for (int j = 0; j < 8; j++) {
            builder.append(Kit.S_UPPER_HEX.charAt((int) (value >> SHIFTS[j]) & 15));
        }
    }

    /**
     * Instances should NOT be constructed in standard programming.
     */
    public HexDump() {
    }

}

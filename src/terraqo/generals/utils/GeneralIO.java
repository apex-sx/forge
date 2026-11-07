/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Kit;
import com.github.potatoxf.generals.Log;
import com.github.potatoxf.generals.utils.io.IOUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author potatoxf
 */
public interface GeneralIO {

    static void close(Object... closeables) {
        if (closeables == null) return;
        for (Object closeable : closeables) {
            if (closeable == null) continue;
            else if (closeable instanceof Flushable) {
                try {
                    ((Flushable) closeable).flush();
                } catch (IOException e) {
                    if (Log.isEnabledError()) {
                        Log.error("", e);
                    }
                }
            }
            try {
                if (closeable instanceof Closeable) {
                    ((Closeable) closeable).close();
                } else if (closeable instanceof HttpURLConnection) {
                    ((HttpURLConnection) closeable).disconnect();
                } else if (closeable instanceof Iterable) {
                    for (Object object : (Iterable<?>) closeable) {
                        GeneralIO.close(object);
                    }
                } else if (closeable instanceof Stream) {
                    ((Stream<?>) closeable).forEach(GeneralIO::close);
                }
            } catch (Throwable e) {
                if (Log.isEnabledError()) {
                    Log.error("", e);
                }
            }
        }
    }

    static byte[] read(Object root, Object... paths) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Safe.newPath(root, paths))) {
            return GeneralIO.copy(inputStream, new ByteArrayOutputStream()).toByteArray();
        }
    }

    static byte[] read(InputStream input) throws IOException {
        try (InputStream inputStream = input) {
            return GeneralIO.copy(inputStream, new ByteArrayOutputStream()).toByteArray();
        }
    }

    static <Output extends OutputStream> Output write(Output output, long length, boolean random, byte value) throws IOException {
        final byte[] bytes = new byte[Kit.BUFFER_SIZE];
        final Consumer<byte[]> filler;
        if (random) {
            filler = arr -> ThreadLocalRandom.current().nextBytes(arr);
        } else {
            filler = arr -> Arrays.fill(arr, 0, arr.length, value);
        }
        long l = 0;
        try (OutputStream outputStream = output) {
            while (l < length) {
                filler.accept(bytes);
                if (l + bytes.length < length) {
                    outputStream.write(bytes, 0, bytes.length);
                    l += bytes.length;
                } else {
                    outputStream.write(bytes, 0, (int) (length - l));
                    l = length;
                }
            }
        }
        return output;
    }

    static <Output extends OutputStream> Output copy(InputStream input, Output output) throws IOException {
        byte[] bytes = new byte[Kit.BUFFER_SIZE];
        int read;
        while ((read = input.read(bytes)) >= 0) {
            output.write(bytes, 0, read);
        }
        return output;
    }

    /**
     * Tests if two RandomAccessFiles contents are equal.
     *
     * @param channel1       A FileChannel.
     * @param channel2       Another FileChannel.
     * @param byteBufferSize The two internal buffer capacities, in bytes.
     * @return true if the contents of both RandomAccessFiles are equal, false otherwise.
     * @throws IOException if an I/O error occurs.
     */
    static boolean contentEquals(final FileChannel channel1, final FileChannel channel2, final int byteBufferSize) throws IOException {
        // Short-circuit test
        if (Objects.equals(channel1, channel2)) {
            return true;
        }
        // Short-circuit test
        final long size1 = channel1 != null ? channel1.size() : 0;
        final long size2 = channel2 != null ? channel2.size() : 0;
        if (size1 != size2) {
            return false;
        }
        if (size1 == 0 && size2 == 0) {
            return true;
        }
        // Dig in and do the work
        final ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(byteBufferSize);
        final ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(byteBufferSize);
        while (true) {
            final int read1 = channel1.read(byteBuffer1);
            final int read2 = channel2.read(byteBuffer2);
            byteBuffer1.clear();
            byteBuffer2.clear();
            if (read1 == IOUtils.EOF && read2 == IOUtils.EOF) {
                return byteBuffer1.equals(byteBuffer2);
            }
            if (read1 != read2) {
                return false;
            }
            if (!byteBuffer1.equals(byteBuffer2)) {
                return false;
            }
        }
    }

    /**
     * Parses the String argument as a {@link ByteOrder}.
     * <p>
     * Returns {@code ByteOrder.LITTLE_ENDIAN} if the given value is {@code "LITTLE_ENDIAN"}.
     * </p>
     * <p>
     * Returns {@code ByteOrder.BIG_ENDIAN} if the given value is {@code "BIG_ENDIAN"}.
     * </p>
     * Examples:
     * <ul>
     * <li>{@code ByteOrderParser.parseByteOrder("LITTLE_ENDIAN")} returns {@code ByteOrder.LITTLE_ENDIAN}</li>
     * <li>{@code ByteOrderParser.parseByteOrder("BIG_ENDIAN")} returns {@code ByteOrder.BIG_ENDIAN}</li>
     * </ul>
     *
     * @param value
     *            the {@link String} containing the ByteOrder representation to be parsed
     * @return the ByteOrder represented by the string argument
     * @throws IllegalArgumentException
     *             if the {@link String} containing the ByteOrder representation to be parsed is unknown.
     */
    static ByteOrder parseByteOrder(final String value) {
        if (ByteOrder.BIG_ENDIAN.toString().equals(value)) {
            return ByteOrder.BIG_ENDIAN;
        }
        if (ByteOrder.LITTLE_ENDIAN.toString().equals(value)) {
            return ByteOrder.LITTLE_ENDIAN;
        }
        throw new IllegalArgumentException("Unsupported byte order setting: " + value + ", expected one of " + ByteOrder.LITTLE_ENDIAN +
                 ", " + ByteOrder.BIG_ENDIAN);
    }

    /**
     * Tests if two RandomAccessFile contents are equal.
     *
     * @param raf1 A RandomAccessFile.
     * @param raf2 Another RandomAccessFile.
     * @return true if the contents of both RandomAccessFiles are equal, false otherwise.
     * @throws IOException if an I/O error occurs.
     * @author potatoxf
     */
    @SuppressWarnings("resource") // See comments
    static boolean contentEquals(final RandomAccessFile raf1, final RandomAccessFile raf2) throws IOException {
        // Short-circuit test
        if (Objects.equals(raf1, raf2)) {
            return true;
        }
        // Short-circuit test
        final long length1 = raf1 != null ? raf1.length() : 0;
        final long length2 = raf2 != null ? raf2.length() : 0;
        if (length1 != length2) {
            return false;
        }
        if (length1 == 0 && length2 == 0) {
            return true;
        }
        // Dig in and to the work
        // We do not close FileChannels because that closes the owning RandomAccessFile.
        // Instead, the caller is assumed to manage the given RandomAccessFile objects.
        final FileChannel channel1 = raf1.getChannel();
        final FileChannel channel2 = raf2.getChannel();
        return contentEquals(channel1, channel2, IOUtils.DEFAULT_BUFFER_SIZE);
    }

    /**
     * Reads a byte array starting at "position" for "length" bytes.
     *
     * @param input    The source RandomAccessFile.
     * @param position The offset position, measured in bytes from the beginning of the file, at which to set the file pointer.
     * @param length   How many bytes to read.
     * @return a new byte array.
     * @throws IOException If the first byte cannot be read for any reason other than end of file, or if the random access file has been closed, or if some
     *                     other I/O error occurs.
     */
    static byte[] read(final RandomAccessFile input, final long position, final int length) throws IOException {
        input.seek(position);
        return IOUtils.toByteArray(input::read, length);
    }

    /**
     * Resets the given file to position 0.
     *
     * @param raf The RandomAccessFile to reset.
     * @return The given RandomAccessFile.
     * @throws IOException If {@code pos} is less than {@code 0} or if an I/O error occurs.
     * @author potatoxf
     */
    static RandomAccessFile reset(final RandomAccessFile raf) throws IOException {
        raf.seek(0);
        return raf;
    }

    /**
     * Skips the requested number or fail if there are not enough left.
     *
     * @param input              byte stream to toSkip
     * @param toSkip             number of bytes to toSkip.
     * @param fully Supplies the buffer to use for reading.
     * @return number of bytes actually skipped.
     * @throws IOException              if there is a problem reading the ReadableByteChannel
     * @throws IllegalArgumentException if toSkip is negative
     * @throws EOFException             if the number of bytes skipped was incorrect
     * @author potatoxf
     */
    static long skip(final Object input, final long toSkip, boolean fully) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        //
        // No need to synchronize access to SCRATCH_BYTE_BUFFER_WO: We don't care if the buffer is written multiple
        // times or in parallel since the data is ignored. We reuse the same buffer, if the buffer size were variable or read-write,
        // we would need to synch or use a thread local to ensure some other thread safety.
        //
        long remain = toSkip;
        byte[] byteArray = null;
        char[] charArray = null;
        ByteBuffer byteBuffer = null;
        // See https://issues.apache.org/jira/browse/IO-203 for why we use read() rather than delegating to toSkip()
        while (remain > 0) {
            long n = -1;
            if (input instanceof InputStream) {
                if (byteArray == null) byteArray = new byte[Kit.BUFFER_SIZE];
                n = ((InputStream) input).read(byteArray, 0, (int) Math.min(remain, byteArray.length));
            } else if (input instanceof Reader) {
                if (charArray == null) charArray = new char[Kit.BUFFER_SIZE];
                n = ((Reader) input).read(charArray, 0, (int) Math.min(remain, charArray.length));
            } else if (input instanceof ReadableByteChannel) {
                if (byteBuffer == null) byteBuffer = ByteBuffer.allocate(Kit.BUFFER_SIZE);
                byteBuffer.position(0);
                byteBuffer.limit((int) Math.min(remain, byteBuffer.capacity()));
                n = ((ReadableByteChannel) input).read(byteBuffer);
            }
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        long skipped = toSkip - remain;
        if (fully && skipped != toSkip) {
            throw new EOFException("To toSkip: " + toSkip + " actual: " + skipped);
        }
        return skipped;
    }
}

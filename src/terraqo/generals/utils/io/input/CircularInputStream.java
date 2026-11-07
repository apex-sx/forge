/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.github.potatoxf.generals.utils.io.IOUtils;

/**
 * An {@link InputStream} that repeats provided bytes for given target byte count.
 * <p>
 * Closing this input stream has no effect. The methods in this class can be called after the stream has been closed
 * without generating an {@link IOException}.
 * </p>
 *
 * @see InfiniteCircularInputStream
 * @author potatoxf
 */
public class CircularInputStream extends AbstractInputStream {

    /**
     * Throws an {@link IllegalArgumentException} if the input contains -1.
     *
     * @param repeatContent input to validate.
     * @return the input.
     */
    private static byte[] validate(final byte[] repeatContent) {
        Objects.requireNonNull(repeatContent, "repeatContent");
        for (final byte b : repeatContent) {
            if (b == IOUtils.EOF) {
                throw new IllegalArgumentException("repeatContent contains the end-of-stream marker " + IOUtils.EOF);
            }
        }
        return repeatContent;
    }

    private long byteCount;
    private int position = IOUtils.EOF;
    private final byte[] repeatedContent;
    private final long targetByteCount;

    /**
     * Constructs an instance from the specified array of bytes.
     *
     * @param repeatContent Input buffer to be repeated this buffer is not copied.
     * @param targetByteCount How many bytes the read. A negative number means an infinite target count.
     */
    public CircularInputStream(final byte[] repeatContent, final long targetByteCount) {
        this.repeatedContent = validate(repeatContent);
        if (repeatContent.length == 0) {
            throw new IllegalArgumentException("repeatContent is empty.");
        }
        this.targetByteCount = targetByteCount;
    }

    @Override
    public int available() throws IOException {
        // A negative targetByteCount means an infinite target count.
        return isClosed() ? 0 : targetByteCount <= Integer.MAX_VALUE ? Math.max(Integer.MAX_VALUE, (int) targetByteCount) : Integer.MAX_VALUE;
    }

    @Override
    public void close() throws IOException {
        super.close();
        byteCount = targetByteCount;
    }

    @Override
    public int read() {
        if (targetByteCount >= 0 || isClosed()) {
            if (byteCount == targetByteCount) {
                return IOUtils.EOF;
            }
            byteCount++;
        }
        position = (position + 1) % repeatedContent.length;
        return repeatedContent[position] & 0xff;
    }

}

/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.io.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Objects;

import com.github.potatoxf.generals.utils.GeneralCharset;
import com.github.potatoxf.generals.utils.GeneralIO;
import com.github.potatoxf.generals.utils.io.FileUtils;
import com.github.potatoxf.generals.utils.io.build.AbstractOrigin;
import com.github.potatoxf.generals.utils.io.build.AbstractStreamBuilder;

/**
 * Writer of files that allows the encoding to be set.
 * <p>
 * This class provides a simple alternative to {@link FileWriter} that allows an encoding to be set. Unfortunately, it cannot subclass {@link FileWriter}.
 * </p>
 * <p>
 * By default, the file will be overwritten, but this may be changed to append.
 * </p>
 * <p>
 * The encoding must be specified using either the name of the {@link Charset}, the {@link Charset}, or a {@link CharsetEncoder}. If the default encoding is
 * required then use the {@link FileWriter} directly, rather than this implementation.
 * </p>
 * <p>
 * To build an instance, use {@link Builder}.
 * </p>
 *
 * @see Builder
 * @author potatoxf
 */
public class FileWriterWithEncoding extends ProxyWriter {

    // @formatter:off
    /**
     * Builds a new {@link FileWriterWithEncoding}.
     *
     * <p>
     * Using a CharsetEncoder:
     * </p>
     * <pre>{@code
     * FileWriterWithEncoding w = FileWriterWithEncoding.builder()
     *   .setPath(path)
     *   .setAppend(false)
     *   .setCharsetEncoder(StandardCharsets.UTF_8.newEncoder())
     *   .get();}
     * </pre>
     * <p>
     * Using a Charset:
     * </p>
     * <pre>{@code
     * FileWriterWithEncoding w = FileWriterWithEncoding.builder()
     *   .setPath(path)
     *   .setAppend(false)
     *   .setCharsetEncoder(StandardCharsets.UTF_8)
     *   .get();}
     * </pre>
     *
     * @see #get()
     * @author potatoxf
     */
    // @formatter:on
    public static class Builder extends AbstractStreamBuilder<FileWriterWithEncoding, Builder> {

        private boolean append;

        private CharsetEncoder charsetEncoder = super.getCharset().newEncoder();

        /**
         * Builds a new {@link FileWriterWithEncoding}.
         * <p>
         * You must set input that supports {@link File} on this builder, otherwise, this method throws an exception.
         * </p>
         * <p>
         * This builder use the following aspects:
         * </p>
         * <ul>
         * <li>{@link File}</li>
         * <li>{@link CharsetEncoder}</li>
         * <li>append</li>
         * </ul>
         *
         * @return a new instance.
         * @throws UnsupportedOperationException if the origin cannot provide a File.
         * @throws IllegalStateException if the {@code origin} is {@code null}.
         * @see AbstractOrigin#getFile()
         */
        @SuppressWarnings("resource")
        @Override
        public FileWriterWithEncoding get() throws IOException {
            if (charsetEncoder != null && getCharset() != null && !charsetEncoder.charset().equals(getCharset())) {
                throw new IllegalStateException(String.format("Mismatched Charset(%s) and CharsetEncoder(%s)", getCharset(), charsetEncoder.charset()));
            }
            final Object encoder = charsetEncoder != null ? charsetEncoder : getCharset();
            return new FileWriterWithEncoding(initWriter(checkOrigin().getFile(), encoder, append));
        }

        /**
         * Sets whether or not to append.
         *
         * @param append Whether or not to append.
         * @return {@code this} instance.
         */
        public Builder setAppend(final boolean append) {
            this.append = append;
            return this;
        }

        /**
         * Sets charsetEncoder to use for encoding.
         *
         * @param charsetEncoder The charsetEncoder to use for encoding.
         * @return {@code this} instance.
         */
        public Builder setCharsetEncoder(final CharsetEncoder charsetEncoder) {
            this.charsetEncoder = charsetEncoder;
            return this;
        }

    }

    /**
     * Constructs a new {@link Builder}.
     *
     * @return Creates a new {@link Builder}.
     * @author potatoxf
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Initializes the wrapped file writer. Ensure that a cleanup occurs if the writer creation fails.
     *
     * @param file     the file to be accessed
     * @param encoding the encoding to use - may be Charset, CharsetEncoder or String, null uses the default Charset.
     * @param append   true to append
     * @return a new initialized OutputStreamWriter
     * @throws IOException if an error occurs
     */
    private static OutputStreamWriter initWriter(final File file, final Object encoding, final boolean append) throws IOException {
        Objects.requireNonNull(file, "file");
        OutputStream outputStream = null;
        final boolean fileExistedAlready = file.exists();
        try {
            outputStream = FileUtils.newOutputStream(file, append);
            if (encoding == null || encoding instanceof Charset) {
                return new OutputStreamWriter(outputStream, GeneralCharset.toCharset((Charset) encoding));
            }
            if (encoding instanceof CharsetEncoder) {
                return new OutputStreamWriter(outputStream, (CharsetEncoder) encoding);
            }
            return new OutputStreamWriter(outputStream, (String) encoding);
        } catch (final IOException | RuntimeException ex) {
            GeneralIO.close(outputStream);
            if (!fileExistedAlready) {
                FileUtils.deleteQuietly(file);
            }
            throw ex;
        }
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param file    the file to write to, not null
     * @param charset the encoding to use, not null
     * @throws NullPointerException if the file or encoding is null
     * @throws IOException          in case of an I/O error
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    public FileWriterWithEncoding(final File file, final Charset charset) throws IOException {
        this(file, charset, false);
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param file     the file to write to, not null.
     * @param encoding the name of the requested charset, null uses the default Charset.
     * @param append   true if content should be appended, false to overwrite.
     * @throws NullPointerException if the file is null.
     * @throws IOException          in case of an I/O error.
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    @SuppressWarnings("resource") // Call site is responsible for closing a new instance.
    public FileWriterWithEncoding(final File file, final Charset encoding, final boolean append) throws IOException {
        this(initWriter(file, encoding, append));
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param file           the file to write to, not null
     * @param charsetEncoder the encoding to use, not null
     * @throws NullPointerException if the file or encoding is null
     * @throws IOException          in case of an I/O error
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    public FileWriterWithEncoding(final File file, final CharsetEncoder charsetEncoder) throws IOException {
        this(file, charsetEncoder, false);
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param file           the file to write to, not null.
     * @param charsetEncoder the encoding to use, null uses the default Charset.
     * @param append         true if content should be appended, false to overwrite.
     * @throws NullPointerException if the file is null.
     * @throws IOException          in case of an I/O error.
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    @SuppressWarnings("resource") // Call site is responsible for closing a new instance.
    public FileWriterWithEncoding(final File file, final CharsetEncoder charsetEncoder, final boolean append) throws IOException {
        this(initWriter(file, charsetEncoder, append));
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param file        the file to write to, not null
     * @param charsetName the name of the requested charset, not null
     * @throws NullPointerException if the file or encoding is null
     * @throws IOException          in case of an I/O error
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    public FileWriterWithEncoding(final File file, final String charsetName) throws IOException {
        this(file, charsetName, false);
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param file        the file to write to, not null.
     * @param charsetName the name of the requested charset, null uses the default Charset.
     * @param append      true if content should be appended, false to overwrite.
     * @throws NullPointerException if the file is null.
     * @throws IOException          in case of an I/O error.
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    @SuppressWarnings("resource") // Call site is responsible for closing a new instance.
    public FileWriterWithEncoding(final File file, final String charsetName, final boolean append) throws IOException {
        this(initWriter(file, charsetName, append));
    }

    private FileWriterWithEncoding(final OutputStreamWriter outputStreamWriter) {
        super(outputStreamWriter);
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param fileName the name of the file to write to, not null
     * @param charset  the charset to use, not null
     * @throws NullPointerException if the file name or encoding is null
     * @throws IOException          in case of an I/O error
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    public FileWriterWithEncoding(final String fileName, final Charset charset) throws IOException {
        this(new File(fileName), charset, false);
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param fileName the name of the file to write to, not null
     * @param charset  the encoding to use, not null
     * @param append   true if content should be appended, false to overwrite
     * @throws NullPointerException if the file name or encoding is null
     * @throws IOException          in case of an I/O error
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    public FileWriterWithEncoding(final String fileName, final Charset charset, final boolean append) throws IOException {
        this(new File(fileName), charset, append);
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param fileName the name of the file to write to, not null
     * @param encoding the encoding to use, not null
     * @throws NullPointerException if the file name or encoding is null
     * @throws IOException          in case of an I/O error
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    public FileWriterWithEncoding(final String fileName, final CharsetEncoder encoding) throws IOException {
        this(new File(fileName), encoding, false);
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param fileName       the name of the file to write to, not null
     * @param charsetEncoder the encoding to use, not null
     * @param append         true if content should be appended, false to overwrite
     * @throws NullPointerException if the file name or encoding is null
     * @throws IOException          in case of an I/O error
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    public FileWriterWithEncoding(final String fileName, final CharsetEncoder charsetEncoder, final boolean append) throws IOException {
        this(new File(fileName), charsetEncoder, append);
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param fileName    the name of the file to write to, not null
     * @param charsetName the name of the requested charset, not null
     * @throws NullPointerException if the file name or encoding is null
     * @throws IOException          in case of an I/O error
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    public FileWriterWithEncoding(final String fileName, final String charsetName) throws IOException {
        this(new File(fileName), charsetName, false);
    }

    /**
     * Constructs a FileWriterWithEncoding with a file encoding.
     *
     * @param fileName    the name of the file to write to, not null
     * @param charsetName the name of the requested charset, not null
     * @param append      true if content should be appended, false to overwrite
     * @throws NullPointerException if the file name or encoding is null
     * @throws IOException          in case of an I/O error
     * @deprecated Use {@link #builder()}, {@link Builder}, and {@link Builder#get()}
     */
    @Deprecated
    public FileWriterWithEncoding(final String fileName, final String charsetName, final boolean append) throws IOException {
        this(new File(fileName), charsetName, append);
    }
}

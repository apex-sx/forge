/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.io;

import com.github.potatoxf.generals.Kit;
import com.github.potatoxf.generals.function.RangeFunctionThrow;
import com.github.potatoxf.generals.iterator.LineIterator;
import com.github.potatoxf.generals.utils.GeneralCharset;
import com.github.potatoxf.generals.utils.GeneralIO;
import com.github.potatoxf.generals.utils.io.function.IOSupplier;
import com.github.potatoxf.generals.utils.io.input.CharSequenceReader;
import com.github.potatoxf.generals.utils.io.input.QueueInputStream;
import com.github.potatoxf.generals.utils.io.output.ByteArrayOutputStream;
import com.github.potatoxf.generals.utils.io.output.*;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * General IO stream manipulation utilities.
 * <p>
 * This class provides static utility methods for input/output operations.
 * </p>
 * <ul>
 * <li>closeQuietly - these methods close a stream ignoring nulls and exceptions
 * <li>toXxx/read - these methods read data from a stream
 * <li>write - these methods write data to a stream
 * <li>copy - these methods copy all the data from one stream to another
 * <li>contentEquals - these methods compare the content of two streams
 * </ul>
 * <p>
 * The byte-to-char methods and char-to-byte methods involve a conversion step.
 * Two methods are provided in each case, one that uses the platform default
 * encoding and the other which allows you to specify an encoding. You are
 * encouraged to always specify an encoding because relying on the platform
 * default can lead to unexpected results, for example when moving from
 * development to production.
 * </p>
 * <p>
 * All the methods in this class that read a stream are buffered internally.
 * This means that there is no cause to use a {@link BufferedInputStream}
 * or {@link BufferedReader}. The default buffer size of 4K has been shown
 * to be efficient in tests.
 * </p>
 * <p>
 * The various copy methods all delegate the actual copying to one of the following methods:
 * </p>
 * <ul>
 * <li>{@link #copyLarge(InputStream, OutputStream, byte[])}</li>
 * <li>{@link #copyLarge(InputStream, OutputStream, long, long, byte[])}</li>
 * <li>{@link #copyLarge(Reader, Writer, char[])}</li>
 * <li>{@link #copyLarge(Reader, Writer, long, long, char[])}</li>
 * </ul>
 * For example, {@link #copy(InputStream, OutputStream)} calls {@link #copyLarge(InputStream, OutputStream)}
 * which calls {@link #copy(InputStream, OutputStream, int)} which creates the buffer and calls
 * {@link #copyLarge(InputStream, OutputStream, byte[])}.
 * <p>
 * Applications can re-use buffers by using the underlying methods directly.
 * This may improve performance for applications that need to do a lot of copying.
 * </p>
 * <p>
 * Wherever possible, the methods in this class do <em>not</em> flush or close
 * the stream. This is to avoid making non-portable assumptions about the
 * streams' origin and further use. Thus the caller is still responsible for
 * closing streams after use.
 * </p>
 * <p>
 * Provenance: Excalibur.
 * </p>
 */
public class IOUtils {
    // NOTE: This class is focused on InputStream, OutputStream, Reader and
    // Writer. Each method should take at least one of these as a parameter,
    // or return one of them.

    /**
     * CR char '{@value}'.
     */
    public static final int CR = '\r';

    /**
     * The default buffer size ({@value}) to use in copy methods.
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * The system directory separator character.
     */
    public static final char DIR_SEPARATOR = File.separatorChar;

    /**
     * The UNIX directory separator character '{@value}'.
     */
    public static final char DIR_SEPARATOR_UNIX = '/';

    /**
     * The Windows directory separator character '{@value}'.
     */
    public static final char DIR_SEPARATOR_WINDOWS = '\\';

    /**
     * A singleton empty byte array.
     */
    public static final byte[] EMPTY_BYTE_ARRAY = {};

    /**
     * Represents the end-of-file (or stream) value {@value}.
     */
    public static final int EOF = -1;

    /**
     * LF char '{@value}'.
     */
    public static final int LF = '\n';

    /**
     * Returns the given InputStream if it is already a {@link BufferedInputStream}, otherwise creates a
     * BufferedInputStream from the given InputStream.
     *
     * @param inputStream the InputStream to wrap or return (not null)
     * @return the given InputStream or a new {@link BufferedInputStream} for the given InputStream
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     */
    @SuppressWarnings("resource") // parameter null check
    public static BufferedInputStream buffer(final InputStream inputStream) {
        // reject null early on rather than waiting for IO operation to fail
        // not checked by BufferedInputStream
        Objects.requireNonNull(inputStream, "inputStream");
        return inputStream instanceof BufferedInputStream ?
                (BufferedInputStream) inputStream : new BufferedInputStream(inputStream);
    }

    /**
     * Returns the given InputStream if it is already a {@link BufferedInputStream}, otherwise creates a
     * BufferedInputStream from the given InputStream.
     *
     * @param inputStream the InputStream to wrap or return (not null)
     * @param size        the buffer size, if a new BufferedInputStream is created.
     * @return the given InputStream or a new {@link BufferedInputStream} for the given InputStream
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     */
    @SuppressWarnings("resource") // parameter null check
    public static BufferedInputStream buffer(final InputStream inputStream, final int size) {
        // reject null early on rather than waiting for IO operation to fail
        // not checked by BufferedInputStream
        Objects.requireNonNull(inputStream, "inputStream");
        return inputStream instanceof BufferedInputStream ?
                (BufferedInputStream) inputStream : new BufferedInputStream(inputStream, size);
    }

    /**
     * Returns the given OutputStream if it is already a {@link BufferedOutputStream}, otherwise creates a
     * BufferedOutputStream from the given OutputStream.
     *
     * @param outputStream the OutputStream to wrap or return (not null)
     * @return the given OutputStream or a new {@link BufferedOutputStream} for the given OutputStream
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     */
    @SuppressWarnings("resource") // parameter null check
    public static BufferedOutputStream buffer(final OutputStream outputStream) {
        // reject null early on rather than waiting for IO operation to fail
        // not checked by BufferedInputStream
        Objects.requireNonNull(outputStream, "outputStream");
        return outputStream instanceof BufferedOutputStream ?
                (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream);
    }

    /**
     * Returns the given OutputStream if it is already a {@link BufferedOutputStream}, otherwise creates a
     * BufferedOutputStream from the given OutputStream.
     *
     * @param outputStream the OutputStream to wrap or return (not null)
     * @param size         the buffer size, if a new BufferedOutputStream is created.
     * @return the given OutputStream or a new {@link BufferedOutputStream} for the given OutputStream
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     */
    @SuppressWarnings("resource") // parameter null check
    public static BufferedOutputStream buffer(final OutputStream outputStream, final int size) {
        // reject null early on rather than waiting for IO operation to fail
        // not checked by BufferedInputStream
        Objects.requireNonNull(outputStream, "outputStream");
        return outputStream instanceof BufferedOutputStream ?
                (BufferedOutputStream) outputStream : new BufferedOutputStream(outputStream, size);
    }

    /**
     * Returns the given reader if it is already a {@link BufferedReader}, otherwise creates a BufferedReader from
     * the given reader.
     *
     * @param reader the reader to wrap or return (not null)
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     */
    public static BufferedReader buffer(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    /**
     * Returns the given reader if it is already a {@link BufferedReader}, otherwise creates a BufferedReader from the
     * given reader.
     *
     * @param reader the reader to wrap or return (not null)
     * @param size   the buffer size, if a new BufferedReader is created.
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     */
    public static BufferedReader buffer(final Reader reader, final int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }

    /**
     * Returns the given Writer if it is already a {@link BufferedWriter}, otherwise creates a BufferedWriter from the
     * given Writer.
     *
     * @param writer the Writer to wrap or return (not null)
     * @return the given Writer or a new {@link BufferedWriter} for the given Writer
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     */
    public static BufferedWriter buffer(final Writer writer) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    /**
     * Returns the given Writer if it is already a {@link BufferedWriter}, otherwise creates a BufferedWriter from the
     * given Writer.
     *
     * @param writer the Writer to wrap or return (not null)
     * @param size   the buffer size, if a new BufferedWriter is created.
     * @return the given Writer or a new {@link BufferedWriter} for the given Writer
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     */
    public static BufferedWriter buffer(final Writer writer, final int size) {
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer, size);
    }

    /**
     * Returns a new byte array of size {@link #DEFAULT_BUFFER_SIZE}.
     *
     * @return a new byte array of size {@link #DEFAULT_BUFFER_SIZE}.
     * @author potatoxf
     */
    public static byte[] byteArray() {
        return byteArray(DEFAULT_BUFFER_SIZE);
    }

    /**
     * Returns a new byte array of the given size.
     * <p>
     * TODO Consider guarding or warning against large allocations...
     *
     * @param size array size.
     * @return a new byte array of the given size.
     * @throws NegativeArraySizeException if the size is negative.
     * @author potatoxf
     */
    public static byte[] byteArray(final int size) {
        return new byte[size];
    }

    /**
     * Returns a new char array of size {@link #DEFAULT_BUFFER_SIZE}.
     *
     * @return a new char array of size {@link #DEFAULT_BUFFER_SIZE}.
     * @author potatoxf
     */
    private static char[] charArray() {
        return charArray(DEFAULT_BUFFER_SIZE);
    }

    /**
     * Returns a new char array of the given size.
     * <p>
     * TODO Consider guarding or warning against large allocations...
     *
     * @param size array size.
     * @return a new char array of the given size.
     * @author potatoxf
     */
    private static char[] charArray(final int size) {
        return new char[size];
    }

    /**
     * Consumes bytes from a {@link InputStream} and ignores them.
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     * </p>
     *
     * @param input the {@link InputStream} to read.
     * @return the number of bytes copied. or {@code 0} if {@code input is null}.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @author potatoxf
     */
    public static long consume(final InputStream input) throws IOException {
        return copyLarge(input, Kit.EMPTY_OUTPUT_STREAM);
    }

    /**
     * Consumes characters from a {@link Reader} and ignores them.
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     * </p>
     *
     * @param input the {@link Reader} to read.
     * @return the number of bytes copied. or {@code 0} if {@code input is null}.
     * @throws NullPointerException if the Reader is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @author potatoxf
     */
    public static long consume(final Reader input) throws IOException {
        return copyLarge(input, Kit.EMPTY_WRITER);
    }

    /**
     * Compares the contents of two Streams to determine if they are equal or
     * not.
     * <p>
     * This method buffers the input internally using
     * {@link BufferedInputStream} if they are not already buffered.
     * </p>
     *
     * @param input1 the first stream
     * @param input2 the second stream
     * @return true if the content of the streams are equal or they both don't
     * exist, false otherwise
     * @throws IOException if an I/O error occurs
     */
    public static boolean contentEquals(final InputStream input1, final InputStream input2) throws IOException {
        // Before making any changes, please test with
        // com.github.potatoxf.generals.utils.io.jmh.IOUtilsContentEqualsInputStreamsBenchmark
        if (input1 == input2) {
            return true;
        }
        if (input1 == null || input2 == null) {
            return false;
        }

        // reuse one
        final byte[] array1 = Kit.getScratchByteArray();
        // allocate another
        final byte[] array2 = byteArray();
        int pos1;
        int pos2;
        int count1;
        int count2;
        while (true) {
            pos1 = 0;
            pos2 = 0;
            for (int index = 0; index < DEFAULT_BUFFER_SIZE; index++) {
                if (pos1 == index) {
                    do {
                        count1 = input1.read(array1, pos1, DEFAULT_BUFFER_SIZE - pos1);
                    } while (count1 == 0);
                    if (count1 == EOF) {
                        return pos2 == index && input2.read() == EOF;
                    }
                    pos1 += count1;
                }
                if (pos2 == index) {
                    do {
                        count2 = input2.read(array2, pos2, DEFAULT_BUFFER_SIZE - pos2);
                    } while (count2 == 0);
                    if (count2 == EOF) {
                        return pos1 == index && input1.read() == EOF;
                    }
                    pos2 += count2;
                }
                if (array1[index] != array2[index]) {
                    return false;
                }
            }
        }
    }

    // TODO Consider making public
    private static boolean contentEquals(final Iterator<?> iterator1, final Iterator<?> iterator2) {
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) {
                return false;
            }
            if (!Objects.equals(iterator1.next(), iterator2.next())) {
                return false;
            }
        }
        return !iterator2.hasNext();
    }

    /**
     * Compares the contents of two Readers to determine if they are equal or not.
     * <p>
     * This method buffers the input internally using {@link BufferedReader} if they are not already buffered.
     * </p>
     *
     * @param input1 the first reader
     * @param input2 the second reader
     * @return true if the content of the readers are equal or they both don't exist, false otherwise
     * @throws NullPointerException if either input is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static boolean contentEquals(final Reader input1, final Reader input2) throws IOException {
        if (input1 == input2) {
            return true;
        }
        if (input1 == null || input2 == null) {
            return false;
        }

        // reuse one
        final char[] array1 = Kit.getScratchCharArray();
        // but allocate another
        final char[] array2 = charArray();
        int pos1;
        int pos2;
        int count1;
        int count2;
        while (true) {
            pos1 = 0;
            pos2 = 0;
            for (int index = 0; index < DEFAULT_BUFFER_SIZE; index++) {
                if (pos1 == index) {
                    do {
                        count1 = input1.read(array1, pos1, DEFAULT_BUFFER_SIZE - pos1);
                    } while (count1 == 0);
                    if (count1 == EOF) {
                        return pos2 == index && input2.read() == EOF;
                    }
                    pos1 += count1;
                }
                if (pos2 == index) {
                    do {
                        count2 = input2.read(array2, pos2, DEFAULT_BUFFER_SIZE - pos2);
                    } while (count2 == 0);
                    if (count2 == EOF) {
                        return pos1 == index && input1.read() == EOF;
                    }
                    pos2 += count2;
                }
                if (array1[index] != array2[index]) {
                    return false;
                }
            }
        }
    }

    // TODO Consider making public
    private static boolean contentEquals(final Stream<?> stream1, final Stream<?> stream2) {
        if (stream1 == stream2) {
            return true;
        }
        if (stream1 == null || stream2 == null) {
            return false;
        }
        return contentEquals(stream1.iterator(), stream2.iterator());
    }

    // TODO Consider making public
    private static boolean contentEqualsIgnoreEOL(final BufferedReader reader1, final BufferedReader reader2) {
        if (reader1 == reader2) {
            return true;
        }
        if (reader1 == null || reader2 == null) {
            return false;
        }
        return contentEquals(reader1.lines(), reader2.lines());
    }

    /**
     * Compares the contents of two Readers to determine if they are equal or
     * not, ignoring EOL characters.
     * <p>
     * This method buffers the input internally using
     * {@link BufferedReader} if they are not already buffered.
     * </p>
     *
     * @param reader1 the first reader
     * @param reader2 the second reader
     * @return true if the content of the readers are equal (ignoring EOL differences),  false otherwise
     * @throws NullPointerException if either input is null
     * @throws UncheckedIOException if an I/O error occurs
     * @author potatoxf
     */
    @SuppressWarnings("resource")
    public static boolean contentEqualsIgnoreEOL(final Reader reader1, final Reader reader2) throws UncheckedIOException {
        if (reader1 == reader2) {
            return true;
        }
        if (reader1 == null || reader2 == null) {
            return false;
        }
        return contentEqualsIgnoreEOL(toBufferedReader(reader1), toBufferedReader(reader2));
    }

    /**
     * Copies bytes from an {@link InputStream} to an {@link OutputStream}.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@link BufferedInputStream}.
     * </p>
     * <p>
     * Large streams (over 2GB) will return a bytes copied value of {@code -1} after the copy has completed since
     * the correct number of bytes cannot be returned as an int. For large streams use the
     * {@link #copyLarge(InputStream, OutputStream)} method.
     * </p>
     *
     * @param inputStream  the {@link InputStream} to read.
     * @param outputStream the {@link OutputStream} to write.
     * @return the number of bytes copied, or -1 if greater than {@link Integer#MAX_VALUE}.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @author potatoxf
     */
    public static int copy(final InputStream inputStream, final OutputStream outputStream) throws IOException {
        final long count = copyLarge(inputStream, outputStream);
        return count > Integer.MAX_VALUE ? EOF : (int) count;
    }

    /**
     * Copies bytes from an {@link InputStream} to an {@link OutputStream} using an internal buffer of the
     * given size.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@link BufferedInputStream}.
     * </p>
     *
     * @param inputStream  the {@link InputStream} to read.
     * @param outputStream the {@link OutputStream} to write to
     * @param bufferSize   the bufferSize used to copy from the input to the output
     * @return the number of bytes copied.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @author potatoxf
     */
    public static long copy(final InputStream inputStream, final OutputStream outputStream, final int bufferSize)
            throws IOException {
        return copyLarge(inputStream, outputStream, byteArray(bufferSize));
    }

    /**
     * Copies bytes from an {@link InputStream} to chars on a
     * {@link Writer} using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     * <p>
     * This method uses {@link InputStreamReader}.
     * </p>
     *
     * @param input        the {@link InputStream} to read
     * @param writer       the {@link Writer} to write to
     * @param inputCharset the charset to use for the input stream, null means platform default
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void copy(final InputStream input, final Writer writer, final Charset inputCharset)
            throws IOException {
        copy(new InputStreamReader(input, GeneralCharset.toCharset(inputCharset)), writer);
    }

    /**
     * Copies bytes from an {@link InputStream} to chars on a
     * {@link Writer} using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     * <p>
     * Character encoding names can be found at
     * <a href="https://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * This method uses {@link InputStreamReader}.
     * </p>
     *
     * @param input            the {@link InputStream} to read
     * @param writer           the {@link Writer} to write to
     * @param inputCharsetName the name of the requested charset for the InputStream, null means platform default
     * @throws NullPointerException                         if the input or output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static void copy(final InputStream input, final Writer writer, final String inputCharsetName)
            throws IOException {
        copy(input, writer, GeneralCharset.toCharset(inputCharsetName));
    }

    /**
     * Copies bytes from a {@link ByteArrayOutputStream} to a {@link QueueInputStream}.
     * <p>
     * Unlike using JDK {@link PipedInputStream} and {@link PipedOutputStream} for this, this
     * solution works safely in a single thread environment.
     * </p>
     * <p>
     * Example usage:
     * </p>
     *
     * <pre>
     * ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
     * outputStream.writeBytes("hello world".getBytes(StandardCharsets.UTF_8));
     *
     * InputStream inputStream = IOUtils.copy(outputStream);
     * </pre>
     *
     * @param outputStream the {@link ByteArrayOutputStream} to read.
     * @return the {@link QueueInputStream} filled with the content of the outputStream.
     * @throws NullPointerException if the {@link ByteArrayOutputStream} is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @author potatoxf
     */
    @SuppressWarnings("resource") // streams are closed by the caller.
    public static QueueInputStream copy(final java.io.ByteArrayOutputStream outputStream) throws IOException {
        Objects.requireNonNull(outputStream, "outputStream");
        final QueueInputStream in = new QueueInputStream();
        outputStream.writeTo(in.newQueueOutputStream());
        return in;
    }

    /**
     * Copies chars from a {@link Reader} to a {@link Appendable}.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     * <p>
     * Large streams (over 2GB) will return a chars copied value of
     * {@code -1} after the copy has completed since the correct
     * number of chars cannot be returned as an int. For large streams
     * use the {@link #copyLarge(Reader, Writer)} method.
     * </p>
     *
     * @param reader the {@link Reader} to read
     * @param output the {@link Appendable} to write to
     * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static long copy(final Reader reader, final Appendable output) throws IOException {
        return copy(reader, output, CharBuffer.allocate(DEFAULT_BUFFER_SIZE));
    }

    /**
     * Copies chars from a {@link Reader} to an {@link Appendable}.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     *
     * @param reader the {@link Reader} to read
     * @param output the {@link Appendable} to write to
     * @param buffer the buffer to be used for the copy
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static long copy(final Reader reader, final Appendable output, final CharBuffer buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = reader.read(buffer))) {
            buffer.flip();
            output.append(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Copies chars from a {@link Reader} to bytes on an
     * {@link OutputStream} using the specified character encoding, and
     * calling flush.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     * <p>
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     * </p>
     * <p>
     * This method uses {@link OutputStreamWriter}.
     * </p>
     *
     * @param reader        the {@link Reader} to read
     * @param output        the {@link OutputStream} to write to
     * @param outputCharset the charset to use for the OutputStream, null means platform default
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void copy(final Reader reader, final OutputStream output, final Charset outputCharset)
            throws IOException {
        final OutputStreamWriter writer = new OutputStreamWriter(output, GeneralCharset.toCharset(outputCharset));
        copy(reader, writer);
        // XXX Unless anyone is planning on rewriting OutputStreamWriter,
        // we have to flush here.
        writer.flush();
    }

    /**
     * Copies chars from a {@link Reader} to bytes on an
     * {@link OutputStream} using the specified character encoding, and
     * calling flush.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * Due to the implementation of OutputStreamWriter, this method performs a
     * flush.
     * </p>
     * <p>
     * This method uses {@link OutputStreamWriter}.
     * </p>
     *
     * @param reader            the {@link Reader} to read
     * @param output            the {@link OutputStream} to write to
     * @param outputCharsetName the name of the requested charset for the OutputStream, null means platform default
     * @throws NullPointerException                         if the input or output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static void copy(final Reader reader, final OutputStream output, final String outputCharsetName)
            throws IOException {
        copy(reader, output, GeneralCharset.toCharset(outputCharsetName));
    }

    /**
     * Copies chars from a {@link Reader} to a {@link Writer}.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     * <p>
     * Large streams (over 2GB) will return a chars copied value of
     * {@code -1} after the copy has completed since the correct
     * number of chars cannot be returned as an int. For large streams
     * use the {@link #copyLarge(Reader, Writer)} method.
     * </p>
     *
     * @param reader the {@link Reader} to read.
     * @param writer the {@link Writer} to write.
     * @return the number of characters copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static int copy(final Reader reader, final Writer writer) throws IOException {
        final long count = copyLarge(reader, writer);
        if (count > Integer.MAX_VALUE) {
            return EOF;
        }
        return (int) count;
    }

    /**
     * Copies bytes from a {@link URL} to an {@link OutputStream}.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@link BufferedInputStream}.
     * </p>
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     * </p>
     *
     * @param url  the {@link URL} to read.
     * @param file the {@link OutputStream} to write.
     * @return the number of bytes copied.
     * @throws NullPointerException if the URL is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @author potatoxf
     */
    public static long copy(final URL url, final File file) throws IOException {
        try (OutputStream outputStream = Files.newOutputStream(Objects.requireNonNull(file, "file").toPath())) {
            return copy(url, outputStream);
        }
    }

    /**
     * Copies bytes from a {@link URL} to an {@link OutputStream}.
     * <p>
     * This method buffers the input internally, so there is no need to use a {@link BufferedInputStream}.
     * </p>
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     * </p>
     *
     * @param url          the {@link URL} to read.
     * @param outputStream the {@link OutputStream} to write.
     * @return the number of bytes copied.
     * @throws NullPointerException if the URL is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @author potatoxf
     */
    public static long copy(final URL url, final OutputStream outputStream) throws IOException {
        try (InputStream inputStream = Objects.requireNonNull(url, "url").openStream()) {
            return copyLarge(inputStream, outputStream);
        }
    }

    /**
     * Copies bytes from a large (over 2GB) {@link InputStream} to an
     * {@link OutputStream}.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     * </p>
     *
     * @param inputStream  the {@link InputStream} to read.
     * @param outputStream the {@link OutputStream} to write.
     * @return the number of bytes copied.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @author potatoxf
     */
    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream)
            throws IOException {
        return copy(inputStream, outputStream, DEFAULT_BUFFER_SIZE);
    }

    /**
     * Copies bytes from a large (over 2GB) {@link InputStream} to an
     * {@link OutputStream}.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param inputStream  the {@link InputStream} to read.
     * @param outputStream the {@link OutputStream} to write.
     * @param buffer       the buffer to use for the copy
     * @return the number of bytes copied.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws NullPointerException if the OutputStream is {@code null}.
     * @throws IOException          if an I/O error occurs.
     * @author potatoxf
     */
    @SuppressWarnings("resource") // streams are closed by the caller.
    public static long copyLarge(final InputStream inputStream, final OutputStream outputStream, final byte[] buffer)
            throws IOException {
        Objects.requireNonNull(inputStream, "inputStream");
        Objects.requireNonNull(outputStream, "outputStream");
        long count = 0;
        int n;
        while (EOF != (n = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Copies some or all bytes from a large (over 2GB) {@link InputStream} to an
     * {@link OutputStream}, optionally skipping input bytes.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     * <p>
     * Note that the implementation uses {@link GeneralIO#skip(Object, long, boolean)}.
     * This means that the method may be considerably less efficient than using the actual skip implementation,
     * this is done to guarantee that the correct number of characters are skipped.
     * </p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     *
     * @param input       the {@link InputStream} to read
     * @param output      the {@link OutputStream} to write to
     * @param inputOffset : number of bytes to skip from input before copying
     *                    -ve values are ignored
     * @param length      number of bytes to copy. -ve means all
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static long copyLarge(final InputStream input, final OutputStream output, final long inputOffset,
                                 final long length) throws IOException {
        return copyLarge(input, output, inputOffset, length, Kit.getScratchByteArray());
    }

    /**
     * Copies some or all bytes from a large (over 2GB) {@link InputStream} to an
     * {@link OutputStream}, optionally skipping input bytes.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     * <p>
     * Note that the implementation uses {@link GeneralIO#skip(Object, long, boolean)}.
     * This means that the method may be considerably less efficient than using the actual skip implementation,
     * this is done to guarantee that the correct number of characters are skipped.
     * </p>
     *
     * @param input       the {@link InputStream} to read
     * @param output      the {@link OutputStream} to write to
     * @param inputOffset number of bytes to skip from input before copying
     *                    -ve values are ignored
     * @param length      number of bytes to copy. -ve means all
     * @param buffer      the buffer to use for the copy
     * @return the number of bytes copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static long copyLarge(final InputStream input, final OutputStream output,
                                 final long inputOffset, final long length, final byte[] buffer) throws IOException {
        if (inputOffset > 0) {
            GeneralIO.skip(input, inputOffset,true);
        }
        if (length == 0) {
            return 0;
        }
        final int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < bufferLength) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) { // only adjust length if not reading to the end
                // Note the cast must work because buffer.length is an integer
                bytesToRead = (int) Math.min(length - totalRead, bufferLength);
            }
        }
        return totalRead;
    }

    /**
     * Copies chars from a large (over 2GB) {@link Reader} to a {@link Writer}.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     * </p>
     *
     * @param reader the {@link Reader} to source.
     * @param writer the {@link Writer} to target.
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static long copyLarge(final Reader reader, final Writer writer) throws IOException {
        return copyLarge(reader, writer, Kit.getScratchCharArray());
    }

    /**
     * Copies chars from a large (over 2GB) {@link Reader} to a {@link Writer}.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     *
     * @param reader the {@link Reader} to source.
     * @param writer the {@link Writer} to target.
     * @param buffer the buffer to be used for the copy
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static long copyLarge(final Reader reader, final Writer writer, final char[] buffer) throws IOException {
        long count = 0;
        int n;
        while (EOF != (n = reader.read(buffer))) {
            writer.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Copies some or all chars from a large (over 2GB) {@link InputStream} to an
     * {@link OutputStream}, optionally skipping input chars.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     * <p>
     * The buffer size is given by {@link #DEFAULT_BUFFER_SIZE}.
     * </p>
     *
     * @param reader      the {@link Reader} to read
     * @param writer      the {@link Writer} to write to
     * @param inputOffset number of chars to skip from input before copying
     *                    -ve values are ignored
     * @param length      number of chars to copy. -ve means all
     * @return the number of chars copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static long copyLarge(final Reader reader, final Writer writer, final long inputOffset, final long length)
            throws IOException {
        return copyLarge(reader, writer, inputOffset, length, Kit.getScratchCharArray());
    }

    /**
     * Copies some or all chars from a large (over 2GB) {@link InputStream} to an
     * {@link OutputStream}, optionally skipping input chars.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     *
     * @param reader      the {@link Reader} to read
     * @param writer      the {@link Writer} to write to
     * @param inputOffset number of chars to skip from input before copying
     *                    -ve values are ignored
     * @param length      number of chars to copy. -ve means all
     * @param buffer      the buffer to be used for the copy
     * @return the number of chars copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static long copyLarge(final Reader reader, final Writer writer, final long inputOffset, final long length,
                                 final char[] buffer)
            throws IOException {
        if (inputOffset > 0) {
            GeneralIO.skip(reader, inputOffset,true);
        }
        if (length == 0) {
            return 0;
        }
        int bytesToRead = buffer.length;
        if (length > 0 && length < buffer.length) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = reader.read(buffer, 0, bytesToRead))) {
            writer.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) { // only adjust length if not reading to the end
                // Note the cast must work because buffer.length is an integer
                bytesToRead = (int) Math.min(length - totalRead, buffer.length);
            }
        }
        return totalRead;
    }

    /**
     * Returns an Iterator for the lines in an {@link InputStream}, using
     * the character encoding specified (or default encoding if null).
     * <p>
     * {@link LineIterator} holds a reference to the open
     * {@link InputStream} specified here. When you have finished with
     * the iterator you should close the stream to free internal resources.
     * This can be done by using a try-with-resources block, closing the stream directly, or by calling
     * {@link LineIterator#close()}.
     * </p>
     * <p>
     * The recommended usage pattern is:
     * </p>
     * <pre>
     * try {
     *   LineIterator it = IOUtils.lineIterator(stream, charset);
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   IOUtils.closeQuietly(stream);
     * }
     * </pre>
     *
     * @param input   the {@link InputStream} to read, not null
     * @param charset the charset to use, null means platform default
     * @return an Iterator of the lines in the reader, never null
     * @throws IllegalArgumentException if the input is null
     * @author potatoxf
     */
    public static LineIterator lineIterator(final InputStream input, final Charset charset) {
        return new LineIterator(new InputStreamReader(input, GeneralCharset.toCharset(charset)));
    }

    /**
     * Returns an Iterator for the lines in an {@link InputStream}, using
     * the character encoding specified (or default encoding if null).
     * <p>
     * {@link LineIterator} holds a reference to the open
     * {@link InputStream} specified here. When you have finished with
     * the iterator you should close the stream to free internal resources.
     * This can be done by using a try-with-resources block, closing the stream directly, or by calling
     * {@link LineIterator#close()}.
     * </p>
     * <p>
     * The recommended usage pattern is:
     * </p>
     * <pre>
     * try {
     *   LineIterator it = IOUtils.lineIterator(stream, StandardCharsets.UTF_8.name());
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   IOUtils.closeQuietly(stream);
     * }
     * </pre>
     *
     * @param input       the {@link InputStream} to read, not null
     * @param charsetName the encoding to use, null means platform default
     * @return an Iterator of the lines in the reader, never null
     * @throws IllegalArgumentException                     if the input is null
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static LineIterator lineIterator(final InputStream input, final String charsetName) {
        return lineIterator(input, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Returns an Iterator for the lines in a {@link Reader}.
     * <p>
     * {@link LineIterator} holds a reference to the open
     * {@link Reader} specified here. When you have finished with the
     * iterator you should close the reader to free internal resources.
     * This can be done by using a try-with-resources block, closing the reader directly, or by calling
     * {@link LineIterator#close()}.
     * </p>
     * <p>
     * The recommended usage pattern is:
     * </p>
     * <pre>
     * try {
     *   LineIterator it = IOUtils.lineIterator(reader);
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   IOUtils.closeQuietly(reader);
     * }
     * </pre>
     *
     * @param reader the {@link Reader} to read, not null
     * @return an Iterator of the lines in the reader, never null
     * @throws NullPointerException if the reader is null
     * @author potatoxf
     */
    public static LineIterator lineIterator(final Reader reader) {
        return new LineIterator(reader);
    }

    /**
     * Reads bytes from an input stream.
     * This implementation guarantees that it will read as many bytes
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link InputStream}.
     *
     * @param input  where to read input from
     * @param buffer destination
     * @return actual length read; may be less than requested if EOF was reached
     * @throws IOException if a read error occurs
     * @author potatoxf
     */
    public static int read(final InputStream input, final byte[] buffer) throws IOException {
        return read(input, buffer, 0, buffer.length);
    }

    /**
     * Reads bytes from an input stream.
     * This implementation guarantees that it will read as many bytes
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link InputStream}.
     *
     * @param input  where to read input
     * @param buffer destination
     * @param offset initial offset into buffer
     * @param length length to read, must be &gt;= 0
     * @return actual length read; may be less than requested if EOF was reached
     * @throws IllegalArgumentException if length is negative
     * @throws IOException              if a read error occurs
     * @author potatoxf
     */
    public static int read(final InputStream input, final byte[] buffer, final int offset, final int length)
            throws IOException {
        if (length == 0) {
            return 0;
        }
        return read(input::read, buffer, offset, length);
    }

    /**
     * Reads bytes from an input. This implementation guarantees that it will read as many bytes as possible before giving up; this may not always be the case
     * for subclasses of {@link InputStream}.
     *
     * @param input  How to read input
     * @param buffer destination
     * @param offset initial offset into buffer
     * @param length length to read, must be &gt;= 0
     * @return actual length read; may be less than requested if EOF was reached
     * @throws IllegalArgumentException if length is negative
     * @throws IOException              if a read error occurs
     * @author potatoxf
     */
    static int read(final RangeFunctionThrow<byte[], Integer, IOException> input, final byte[] buffer, final int offset, final int length)
            throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        }
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = input.applyThrow(buffer, offset + location, remaining);
            if (EOF == count) {
                break;
            }
            remaining -= count;
        }
        return length - remaining;
    }

    /**
     * Reads bytes from a ReadableByteChannel.
     * <p>
     * This implementation guarantees that it will read as many bytes
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link ReadableByteChannel}.
     * </p>
     *
     * @param input  the byte channel to read
     * @param buffer byte buffer destination
     * @return the actual length read; may be less than requested if EOF was reached
     * @throws IOException if a read error occurs
     * @author potatoxf
     */
    public static int read(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
        final int length = buffer.remaining();
        while (buffer.remaining() > 0) {
            final int count = input.read(buffer);
            if (EOF == count) { // EOF
                break;
            }
        }
        return length - buffer.remaining();
    }

    /**
     * Reads characters from an input character stream.
     * This implementation guarantees that it will read as many characters
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link Reader}.
     *
     * @param reader where to read input from
     * @param buffer destination
     * @return actual length read; may be less than requested if EOF was reached
     * @throws IOException if a read error occurs
     * @author potatoxf
     */
    public static int read(final Reader reader, final char[] buffer) throws IOException {
        return read(reader, buffer, 0, buffer.length);
    }

    /**
     * Reads characters from an input character stream.
     * This implementation guarantees that it will read as many characters
     * as possible before giving up; this may not always be the case for
     * subclasses of {@link Reader}.
     *
     * @param reader where to read input from
     * @param buffer destination
     * @param offset initial offset into buffer
     * @param length length to read, must be &gt;= 0
     * @return actual length read; may be less than requested if EOF was reached
     * @throws IllegalArgumentException if length is negative
     * @throws IOException              if a read error occurs
     * @author potatoxf
     */
    public static int read(final Reader reader, final char[] buffer, final int offset, final int length)
            throws IOException {
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative: " + length);
        }
        int remaining = length;
        while (remaining > 0) {
            final int location = length - remaining;
            final int count = reader.read(buffer, offset + location, remaining);
            if (EOF == count) { // EOF
                break;
            }
            remaining -= count;
        }
        return length - remaining;
    }

    /**
     * Reads the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link InputStream#read(byte[], int, int)} may
     * not read as many bytes as requested (most likely because of reaching EOF).
     * </p>
     *
     * @param input  where to read input from
     * @param buffer destination
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of bytes read was incorrect
     * @author potatoxf
     */
    public static void readFully(final InputStream input, final byte[] buffer) throws IOException {
        readFully(input, buffer, 0, buffer.length);
    }

    /**
     * Reads the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link InputStream#read(byte[], int, int)} may
     * not read as many bytes as requested (most likely because of reaching EOF).
     * </p>
     *
     * @param input  where to read input from
     * @param buffer destination
     * @param offset initial offset into buffer
     * @param length length to read, must be &gt;= 0
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of bytes read was incorrect
     * @author potatoxf
     */
    public static void readFully(final InputStream input, final byte[] buffer, final int offset, final int length)
            throws IOException {
        final int actual = read(input, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    /**
     * Reads the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link InputStream#read(byte[], int, int)} may
     * not read as many bytes as requested (most likely because of reaching EOF).
     * </p>
     *
     * @param input  where to read input from
     * @param length length to read, must be &gt;= 0
     * @return the bytes read from input
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of bytes read was incorrect
     * @author potatoxf
     */
    public static byte[] readFully(final InputStream input, final int length) throws IOException {
        final byte[] buffer = byteArray(length);
        readFully(input, buffer, 0, buffer.length);
        return buffer;
    }

    /**
     * Reads the requested number of bytes or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link ReadableByteChannel#read(ByteBuffer)} may
     * not read as many bytes as requested (most likely because of reaching EOF).
     * </p>
     *
     * @param input  the byte channel to read
     * @param buffer byte buffer destination
     * @throws IOException  if there is a problem reading the file
     * @throws EOFException if the number of bytes read was incorrect
     * @author potatoxf
     */
    public static void readFully(final ReadableByteChannel input, final ByteBuffer buffer) throws IOException {
        final int expected = buffer.remaining();
        final int actual = read(input, buffer);
        if (actual != expected) {
            throw new EOFException("Length to read: " + expected + " actual: " + actual);
        }
    }

    /**
     * Reads the requested number of characters or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link Reader#read(char[], int, int)} may
     * not read as many characters as requested (most likely because of reaching EOF).
     * </p>
     *
     * @param reader where to read input from
     * @param buffer destination
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of characters read was incorrect
     * @author potatoxf
     */
    public static void readFully(final Reader reader, final char[] buffer) throws IOException {
        readFully(reader, buffer, 0, buffer.length);
    }

    /**
     * Reads the requested number of characters or fail if there are not enough left.
     * <p>
     * This allows for the possibility that {@link Reader#read(char[], int, int)} may
     * not read as many characters as requested (most likely because of reaching EOF).
     * </p>
     *
     * @param reader where to read input from
     * @param buffer destination
     * @param offset initial offset into buffer
     * @param length length to read, must be &gt;= 0
     * @throws IOException              if there is a problem reading the file
     * @throws IllegalArgumentException if length is negative
     * @throws EOFException             if the number of characters read was incorrect
     * @author potatoxf
     */
    public static void readFully(final Reader reader, final char[] buffer, final int offset, final int length)
            throws IOException {
        final int actual = read(reader, buffer, offset, length);
        if (actual != length) {
            throw new EOFException("Length to read: " + length + " actual: " + actual);
        }
    }

    /**
     * Gets the contents of a {@link CharSequence} as a list of Strings, one entry per line.
     *
     * @param csq the {@link CharSequence} to read, not null
     * @return the list of Strings, never null
     * @throws UncheckedIOException if an I/O error occurs
     * @author potatoxf
     */
    public static List<String> readLines(final CharSequence csq) throws UncheckedIOException {
        try (CharSequenceReader reader = new CharSequenceReader(csq)) {
            return readLines(reader);
        }
    }

    /**
     * Gets the contents of an {@link InputStream} as a list of Strings,
     * one entry per line, using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param input   the {@link InputStream} to read, not null
     * @param charset the charset to use, null means platform default
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws UncheckedIOException if an I/O error occurs
     * @author potatoxf
     */
    public static List<String> readLines(final InputStream input, final Charset charset) throws UncheckedIOException {
        return readLines(new InputStreamReader(input, GeneralCharset.toCharset(charset)));
    }

    /**
     * Gets the contents of an {@link InputStream} as a list of Strings,
     * one entry per line, using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param input       the {@link InputStream} to read, not null
     * @param charsetName the name of the requested charset, null means platform default
     * @return the list of Strings, never null
     * @throws NullPointerException                         if the input is null
     * @throws UncheckedIOException                         if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static List<String> readLines(final InputStream input, final String charsetName) throws UncheckedIOException {
        return readLines(input, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Gets the contents of a {@link Reader} as a list of Strings,
     * one entry per line.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     *
     * @param reader the {@link Reader} to read, not null
     * @return the list of Strings, never null
     * @throws NullPointerException if the input is null
     * @throws UncheckedIOException if an I/O error occurs
     * @author potatoxf
     */
    @SuppressWarnings("resource") // reader wraps input and is the responsibility of the caller.
    public static List<String> readLines(final Reader reader) throws UncheckedIOException {
        return toBufferedReader(reader).lines().collect(Collectors.toList());
    }

    /**
     * Gets the contents of a resource as a byte array.
     * <p>
     * Delegates to {@link #resourceToByteArray(String, ClassLoader) resourceToByteArray(String, null)}.
     * </p>
     *
     * @param name The resource name.
     * @return the requested byte array
     * @throws IOException if an I/O error occurs or the resource is not found.
     * @author potatoxf
     * @see #resourceToByteArray(String, ClassLoader)
     */
    public static byte[] resourceToByteArray(final String name) throws IOException {
        return resourceToByteArray(name, null);
    }

    /**
     * Gets the contents of a resource as a byte array.
     * <p>
     * Delegates to {@link #resourceToURL(String, ClassLoader)}.
     * </p>
     *
     * @param name        The resource name.
     * @param classLoader the class loader that the resolution of the resource is delegated to
     * @return the requested byte array
     * @throws IOException if an I/O error occurs or the resource is not found.
     * @author potatoxf
     * @see #resourceToURL(String, ClassLoader)
     */
    public static byte[] resourceToByteArray(final String name, final ClassLoader classLoader) throws IOException {
        return toByteArray(resourceToURL(name, classLoader));
    }

    /**
     * Gets the contents of a resource as a String using the specified character encoding.
     * <p>
     * Delegates to {@link #resourceToString(String, Charset, ClassLoader) resourceToString(String, Charset, null)}.
     * </p>
     *
     * @param name    The resource name.
     * @param charset the charset to use, null means platform default
     * @return the requested String
     * @throws IOException if an I/O error occurs or the resource is not found.
     * @author potatoxf
     * @see #resourceToString(String, Charset, ClassLoader)
     */
    public static String resourceToString(final String name, final Charset charset) throws IOException {
        return resourceToString(name, charset, null);
    }

    /**
     * Gets the contents of a resource as a String using the specified character encoding.
     * <p>
     * Delegates to {@link #resourceToURL(String, ClassLoader)}.
     * </p>
     *
     * @param name        The resource name.
     * @param charset     the Charset to use, null means platform default
     * @param classLoader the class loader that the resolution of the resource is delegated to
     * @return the requested String
     * @throws IOException if an I/O error occurs.
     * @author potatoxf
     * @see #resourceToURL(String, ClassLoader)
     */
    public static String resourceToString(final String name, final Charset charset, final ClassLoader classLoader) throws IOException {
        return toString(resourceToURL(name, classLoader), charset);
    }

    /**
     * Gets a URL pointing to the given resource.
     * <p>
     * Delegates to {@link #resourceToURL(String, ClassLoader) resourceToURL(String, null)}.
     * </p>
     *
     * @param name The resource name.
     * @return A URL object for reading the resource.
     * @throws IOException if the resource is not found.
     * @author potatoxf
     */
    public static URL resourceToURL(final String name) throws IOException {
        return resourceToURL(name, null);
    }

    /**
     * Gets a URL pointing to the given resource.
     * <p>
     * If the {@code classLoader} is not null, call {@link ClassLoader#getResource(String)}, otherwise call
     * {@link Class#getResource(String) IOUtils.class.getResource(name)}.
     * </p>
     *
     * @param name        The resource name.
     * @param classLoader Delegate to this class loader if not null
     * @return A URL object for reading the resource.
     * @throws IOException if the resource is not found.
     * @author potatoxf
     */
    public static URL resourceToURL(final String name, final ClassLoader classLoader) throws IOException {
        // What about the thread context class loader?
        // What about the system class loader?
        final URL resource = classLoader == null ? IOUtils.class.getResource(name) : classLoader.getResource(name);
        if (resource == null) {
            throw new IOException("Resource not found: " + name);
        }
        return resource;
    }

    /**
     * Fetches entire contents of an {@link InputStream} and represent
     * same data as result InputStream.
     * <p>
     * This method is useful where,
     * </p>
     * <ul>
     * <li>Source InputStream is slow.</li>
     * <li>It has network resources associated, so we cannot keep it open for
     * long time.</li>
     * <li>It has network timeout associated.</li>
     * </ul>
     * <p>
     * It can be used in favor of {@link #toByteArray(InputStream)}, since it
     * avoids unnecessary allocation and copy of byte[].<br>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param input Stream to be fully buffered.
     * @return A fully buffered stream.
     * @throws IOException if an I/O error occurs.
     * @author potatoxf
     */
    public static InputStream toBufferedInputStream(final InputStream input) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(input);
    }

    /**
     * Fetches entire contents of an {@link InputStream} and represent
     * same data as result InputStream.
     * <p>
     * This method is useful where,
     * </p>
     * <ul>
     * <li>Source InputStream is slow.</li>
     * <li>It has network resources associated, so we cannot keep it open for
     * long time.</li>
     * <li>It has network timeout associated.</li>
     * </ul>
     * <p>
     * It can be used in favor of {@link #toByteArray(InputStream)}, since it
     * avoids unnecessary allocation and copy of byte[].<br>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param input Stream to be fully buffered.
     * @param size  the initial buffer size
     * @return A fully buffered stream.
     * @throws IOException if an I/O error occurs.
     * @author potatoxf
     */
    public static InputStream toBufferedInputStream(final InputStream input, final int size) throws IOException {
        return ByteArrayOutputStream.toBufferedInputStream(input, size);
    }

    /**
     * Returns the given reader if it is a {@link BufferedReader}, otherwise creates a BufferedReader from the given
     * reader.
     *
     * @param reader the reader to wrap or return (not null)
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     * @see #buffer(Reader)
     */
    public static BufferedReader toBufferedReader(final Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    /**
     * Returns the given reader if it is a {@link BufferedReader}, otherwise creates a BufferedReader from the given
     * reader.
     *
     * @param reader the reader to wrap or return (not null)
     * @param size   the buffer size, if a new BufferedReader is created.
     * @return the given reader or a new {@link BufferedReader} for the given reader
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     * @see #buffer(Reader)
     */
    public static BufferedReader toBufferedReader(final Reader reader, final int size) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader, size);
    }

    /**
     * Gets the contents of an {@link InputStream} as a {@code byte[]}.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param inputStream the {@link InputStream} to read.
     * @return the requested byte array.
     * @throws NullPointerException if the InputStream is {@code null}.
     * @throws IOException          if an I/O error occurs or reading more than {@link Integer#MAX_VALUE} occurs.
     */
    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        // We use a ThresholdingOutputStream to avoid reading AND writing more than Integer.MAX_VALUE.
        try (UnsynchronizedByteArrayOutputStream ubaOutput = UnsynchronizedByteArrayOutputStream.builder().get();
             ThresholdingOutputStream thresholdOutput = new ThresholdingOutputStream(Integer.MAX_VALUE, os -> {
                 throw new IllegalArgumentException(String.format("Cannot read more than %,d into a byte array", Integer.MAX_VALUE));
             }, os -> ubaOutput)) {
            copy(inputStream, thresholdOutput);
            return ubaOutput.toByteArray();
        }
    }

    /**
     * Gets the contents of an {@link InputStream} as a {@code byte[]}. Use this method instead of
     * {@link #toByteArray(InputStream)} when {@link InputStream} size is known.
     *
     * @param input the {@link InputStream} to read.
     * @param size  the size of {@link InputStream} to read, where 0 &lt; {@code size} &lt;= length of input stream.
     * @return byte [] of length {@code size}.
     * @throws IOException              if an I/O error occurs or {@link InputStream} length is smaller than parameter {@code size}.
     * @throws IllegalArgumentException if {@code size} is less than zero.
     * @author potatoxf
     */
    public static byte[] toByteArray(final InputStream input, final int size) throws IOException {
        if (size == 0 || input == null) {
            return EMPTY_BYTE_ARRAY;
        }
        return toByteArray(input::read, size);
    }

    /**
     * Gets contents of an {@link InputStream} as a {@code byte[]}.
     * Use this method instead of {@link #toByteArray(InputStream)}
     * when {@link InputStream} size is known.
     * <strong>NOTE:</strong> the method checks that the length can safely be cast to an int without truncation
     * before using {@link IOUtils#toByteArray(InputStream, int)} to read into the byte array.
     * (Arrays can have no more than Integer.MAX_VALUE entries anyway)
     *
     * @param input the {@link InputStream} to read
     * @param size  the size of {@link InputStream} to read, where 0 &lt; {@code size} &lt;= min(Integer.MAX_VALUE, length of input stream).
     * @return byte [] the requested byte array, of length {@code size}
     * @throws IOException              if an I/O error occurs or {@link InputStream} length is less than {@code size}
     * @throws IllegalArgumentException if size is less than zero or size is greater than Integer.MAX_VALUE
     * @author potatoxf
     * @see IOUtils#toByteArray(InputStream, int)
     */
    public static byte[] toByteArray(final InputStream input, final long size) throws IOException {
        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
        }
        return toByteArray(input, (int) size);
    }

    /**
     * Gets the contents of an input as a {@code byte[]}.
     *
     * @param input the input to read.
     * @param size  the size of the input to read, where 0 &lt; {@code size} &lt;= length of input.
     * @return byte [] of length {@code size}.
     * @throws IOException              if an I/O error occurs or input length is smaller than parameter {@code size}.
     * @throws IllegalArgumentException if {@code size} is less than zero.
     */
    public static byte[] toByteArray(final RangeFunctionThrow<byte[], Integer, IOException> input, final int size) throws IOException {

        if (size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        }

        if (size == 0) {
            return EMPTY_BYTE_ARRAY;
        }

        final byte[] data = new byte[size];
        int offset = 0;
        int read;

        while (offset < size && (read = input.applyThrow(data, offset, size - offset)) != EOF) {
            offset += read;
        }

        if (offset != size) {
            throw new IOException("Unexpected read size, current: " + offset + ", expected: " + size);
        }

        return data;
    }

    /**
     * Gets the contents of a {@link Reader} as a {@code byte[]}
     * using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     *
     * @param reader  the {@link Reader} to read
     * @param charset the charset to use, null means platform default
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static byte[] toByteArray(final Reader reader, final Charset charset) throws IOException {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            copy(reader, output, charset);
            return output.toByteArray();
        }
    }

    /**
     * Gets the contents of a {@link Reader} as a {@code byte[]}
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     *
     * @param reader      the {@link Reader} to read
     * @param charsetName the name of the requested charset, null means platform default
     * @return the requested byte array
     * @throws NullPointerException                         if the input is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static byte[] toByteArray(final Reader reader, final String charsetName) throws IOException {
        return toByteArray(reader, GeneralCharset.toCharset(charsetName));
    }


    /**
     * Gets the contents of a {@link URI} as a {@code byte[]}.
     *
     * @param uri the {@link URI} to read
     * @return the requested byte array
     * @throws NullPointerException if the uri is null
     * @throws IOException          if an I/O exception occurs
     * @author potatoxf
     */
    public static byte[] toByteArray(final URI uri) throws IOException {
        return toByteArray(uri.toURL());
    }

    /**
     * Gets the contents of a {@link URL} as a {@code byte[]}.
     *
     * @param url the {@link URL} to read
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O exception occurs
     * @author potatoxf
     */
    public static byte[] toByteArray(final URL url) throws IOException {
        try (CloseableURLConnection urlConnection = CloseableURLConnection.open(url)) {
            return toByteArray(urlConnection);
        }
    }

    /**
     * Gets the contents of a {@link URLConnection} as a {@code byte[]}.
     *
     * @param urlConnection the {@link URLConnection} to read.
     * @return the requested byte array.
     * @throws NullPointerException if the urlConn is null.
     * @throws IOException          if an I/O exception occurs.
     * @author potatoxf
     */
    public static byte[] toByteArray(final URLConnection urlConnection) throws IOException {
        try (InputStream inputStream = urlConnection.getInputStream()) {
            return toByteArray(inputStream);
        }
    }

    /**
     * Gets the contents of an {@link InputStream} as a character array
     * using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param inputStream the {@link InputStream} to read
     * @param charset     the charset to use, null means platform default
     * @return the requested character array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static char[] toCharArray(final InputStream inputStream, final Charset charset)
            throws IOException {
        final CharArrayWriter writer = new CharArrayWriter();
        copy(inputStream, writer, charset);
        return writer.toCharArray();
    }

    /**
     * Gets the contents of an {@link InputStream} as a character array
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param inputStream the {@link InputStream} to read
     * @param charsetName the name of the requested charset, null means platform default
     * @return the requested character array
     * @throws NullPointerException                         if the input is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static char[] toCharArray(final InputStream inputStream, final String charsetName) throws IOException {
        return toCharArray(inputStream, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Gets the contents of a {@link Reader} as a character array.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     *
     * @param reader the {@link Reader} to read
     * @return the requested character array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static char[] toCharArray(final Reader reader) throws IOException {
        final CharArrayWriter sw = new CharArrayWriter();
        copy(reader, sw);
        return sw.toCharArray();
    }

    /**
     * Converts the specified CharSequence to an input stream, encoded as bytes
     * using the specified character encoding.
     *
     * @param input   the CharSequence to convert
     * @param charset the charset to use, null means platform default
     * @return an input stream
     * @author potatoxf
     */
    public static InputStream toInputStream(final CharSequence input, final Charset charset) {
        return toInputStream(input.toString(), charset);
    }

    /**
     * Converts the specified CharSequence to an input stream, encoded as bytes
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     *
     * @param input       the CharSequence to convert
     * @param charsetName the name of the requested charset, null means platform default
     * @return an input stream
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static InputStream toInputStream(final CharSequence input, final String charsetName) {
        return toInputStream(input, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Converts the specified string to an input stream, encoded as bytes
     * using the specified character encoding.
     *
     * @param input   the string to convert
     * @param charset the charset to use, null means platform default
     * @return an input stream
     * @author potatoxf
     */
    public static InputStream toInputStream(final String input, final Charset charset) {
        return new ByteArrayInputStream(input.getBytes(GeneralCharset.toCharset(charset)));
    }

    /**
     * Converts the specified string to an input stream, encoded as bytes
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     *
     * @param input       the string to convert
     * @param charsetName the name of the requested charset, null means platform default
     * @return an input stream
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static InputStream toInputStream(final String input, final String charsetName) {
        return new ByteArrayInputStream(input.getBytes(GeneralCharset.toCharset(charsetName)));
    }

    /**
     * Gets the contents of a {@code byte[]} as a String
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     *
     * @param input       the byte array to read
     * @param charsetName the name of the requested charset, null means platform default
     * @return the requested String
     * @throws NullPointerException if the input is null
     */
    public static String toString(final byte[] input, final String charsetName) {
        return new String(input, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Gets the contents of an {@link InputStream} as a String
     * using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param input   the {@link InputStream} to read
     * @param charset the charset to use, null means platform default
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static String toString(final InputStream input, final Charset charset) throws IOException {
        try (StringBuilderWriter sw = new StringBuilderWriter()) {
            copy(input, sw, charset);
            return sw.toString();
        }
    }

    /**
     * Gets the contents of an {@link InputStream} as a String
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param input       the {@link InputStream} to read
     * @param charsetName the name of the requested charset, null means platform default
     * @return the requested String
     * @throws NullPointerException                         if the input is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     */
    public static String toString(final InputStream input, final String charsetName)
            throws IOException {
        return toString(input, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Gets the contents of an {@link InputStream} from a supplier as a String
     * using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param input   supplies the {@link InputStream} to read
     * @param charset the charset to use, null means platform default
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static String toString(final IOSupplier<InputStream> input, final Charset charset) throws IOException {
        return toString(input, charset, () -> {
            throw new NullPointerException("input");
        });
    }

    /**
     * Gets the contents of an {@link InputStream} from a supplier as a String
     * using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedInputStream}.
     * </p>
     *
     * @param input         supplies the {@link InputStream} to read
     * @param charset       the charset to use, null means platform default
     * @param defaultString the default return value if the supplier or its value is null.
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static String toString(final IOSupplier<InputStream> input, final Charset charset, final IOSupplier<String> defaultString) throws IOException {
        if (input == null) {
            return defaultString.get();
        }
        try (InputStream inputStream = input.get()) {
            return inputStream != null ? toString(inputStream, charset) : defaultString.get();
        }
    }

    /**
     * Gets the contents of a {@link Reader} as a String.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * {@link BufferedReader}.
     * </p>
     *
     * @param reader the {@link Reader} to read
     * @return the requested String
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static String toString(final Reader reader) throws IOException {
        try (StringBuilderWriter sw = new StringBuilderWriter()) {
            copy(reader, sw);
            return sw.toString();
        }
    }

    /**
     * Gets the contents at the given URI.
     *
     * @param uri      The URI source.
     * @param encoding The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     * @author potatoxf
     */
    public static String toString(final URI uri, final Charset encoding) throws IOException {
        return toString(uri.toURL(), GeneralCharset.toCharset(encoding));
    }

    /**
     * Gets the contents at the given URI.
     *
     * @param uri         The URI source.
     * @param charsetName The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException                                  if an I/O exception occurs.
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static String toString(final URI uri, final String charsetName) throws IOException {
        return toString(uri, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Gets the contents at the given URL.
     *
     * @param url      The URL source.
     * @param encoding The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException if an I/O exception occurs.
     * @author potatoxf
     */
    public static String toString(final URL url, final Charset encoding) throws IOException {
        return toString(url::openStream, encoding);
    }

    /**
     * Gets the contents at the given URL.
     *
     * @param url         The URL source.
     * @param charsetName The encoding name for the URL contents.
     * @return The contents of the URL as a String.
     * @throws IOException                                  if an I/O exception occurs.
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static String toString(final URL url, final String charsetName) throws IOException {
        return toString(url, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Writes bytes from a {@code byte[]} to an {@link OutputStream}.
     *
     * @param data   the byte array to write, do not modify during output,
     *               null ignored
     * @param output the {@link OutputStream} to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void write(final byte[] data, final OutputStream output)
            throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    /**
     * Writes bytes from a {@code byte[]} to chars on a {@link Writer}
     * using the specified character encoding.
     * <p>
     * This method uses {@link String#String(byte[], String)}.
     * </p>
     *
     * @param data    the byte array to write, do not modify during output,
     *                null ignored
     * @param writer  the {@link Writer} to write to
     * @param charset the charset to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void write(final byte[] data, final Writer writer, final Charset charset) throws IOException {
        if (data != null) {
            writer.write(new String(data, GeneralCharset.toCharset(charset)));
        }
    }

    /**
     * Writes bytes from a {@code byte[]} to chars on a {@link Writer}
     * using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * This method uses {@link String#String(byte[], String)}.
     * </p>
     *
     * @param data        the byte array to write, do not modify during output,
     *                    null ignored
     * @param writer      the {@link Writer} to write to
     * @param charsetName the name of the requested charset, null means platform default
     * @throws NullPointerException                         if output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static void write(final byte[] data, final Writer writer, final String charsetName) throws IOException {
        write(data, writer, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Writes chars from a {@code char[]} to bytes on an
     * {@link OutputStream} using the specified character encoding.
     * <p>
     * This method uses {@link String#String(char[])} and
     * {@link String#getBytes(String)}.
     * </p>
     *
     * @param data    the char array to write, do not modify during output,
     *                null ignored
     * @param output  the {@link OutputStream} to write to
     * @param charset the charset to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void write(final char[] data, final OutputStream output, final Charset charset) throws IOException {
        if (data != null) {
            write(new String(data), output, charset);
        }
    }

    /**
     * Writes chars from a {@code char[]} to bytes on an
     * {@link OutputStream} using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * This method uses {@link String#String(char[])} and
     * {@link String#getBytes(String)}.
     * </p>
     *
     * @param data        the char array to write, do not modify during output,
     *                    null ignored
     * @param output      the {@link OutputStream} to write to
     * @param charsetName the name of the requested charset, null means platform default
     * @throws NullPointerException                         if output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static void write(final char[] data, final OutputStream output, final String charsetName)
            throws IOException {
        write(data, output, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Writes chars from a {@code char[]} to a {@link Writer}
     *
     * @param data   the char array to write, do not modify during output,
     *               null ignored
     * @param writer the {@link Writer} to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void write(final char[] data, final Writer writer) throws IOException {
        if (data != null) {
            writer.write(data);
        }
    }

    /**
     * Writes chars from a {@link CharSequence} to bytes on an
     * {@link OutputStream} using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     * </p>
     *
     * @param data    the {@link CharSequence} to write, null ignored
     * @param output  the {@link OutputStream} to write to
     * @param charset the charset to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void write(final CharSequence data, final OutputStream output, final Charset charset)
            throws IOException {
        if (data != null) {
            write(data.toString(), output, charset);
        }
    }

    /**
     * Writes chars from a {@link CharSequence} to bytes on an
     * {@link OutputStream} using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * This method uses {@link String#getBytes(String)}.
     * </p>
     *
     * @param data        the {@link CharSequence} to write, null ignored
     * @param output      the {@link OutputStream} to write to
     * @param charsetName the name of the requested charset, null means platform default
     * @throws NullPointerException                         if output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static void write(final CharSequence data, final OutputStream output, final String charsetName)
            throws IOException {
        write(data, output, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Writes chars from a {@link CharSequence} to a {@link Writer}.
     *
     * @param data   the {@link CharSequence} to write, null ignored
     * @param writer the {@link Writer} to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void write(final CharSequence data, final Writer writer) throws IOException {
        if (data != null) {
            write(data.toString(), writer);
        }
    }

    /**
     * Writes chars from a {@link String} to bytes on an
     * {@link OutputStream} using the specified character encoding.
     * <p>
     * This method uses {@link String#getBytes(String)}.
     * </p>
     *
     * @param data    the {@link String} to write, null ignored
     * @param output  the {@link OutputStream} to write to
     * @param charset the charset to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    @SuppressWarnings("resource")
    public static void write(final String data, final OutputStream output, final Charset charset) throws IOException {
        if (data != null) {
            // Use Charset#encode(String), since calling String#getBytes(Charset) might result in
            // NegativeArraySizeException or OutOfMemoryError.
            // The underlying OutputStream should not be closed, so the channel is not closed.
            Channels.newChannel(output).write(GeneralCharset.toCharset(charset).encode(data));
        }
    }

    /**
     * Writes chars from a {@link String} to bytes on an
     * {@link OutputStream} using the specified character encoding.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     * <p>
     * This method uses {@link String#getBytes(String)}.
     * </p>
     *
     * @param data        the {@link String} to write, null ignored
     * @param output      the {@link OutputStream} to write to
     * @param charsetName the name of the requested charset, null means platform default
     * @throws NullPointerException                         if output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static void write(final String data, final OutputStream output, final String charsetName)
            throws IOException {
        write(data, output, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Writes chars from a {@link String} to a {@link Writer}.
     *
     * @param data   the {@link String} to write, null ignored
     * @param writer the {@link Writer} to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void write(final String data, final Writer writer) throws IOException {
        if (data != null) {
            writer.write(data);
        }
    }

    /**
     * Writes bytes from a {@code byte[]} to an {@link OutputStream} using chunked writes.
     * This is intended for writing very large byte arrays which might otherwise cause excessive
     * memory usage if the native code has to allocate a copy.
     *
     * @param data   the byte array to write, do not modify during output,
     *               null ignored
     * @param output the {@link OutputStream} to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void writeChunked(final byte[] data, final OutputStream output)
            throws IOException {
        if (data != null) {
            int bytes = data.length;
            int offset = 0;
            while (bytes > 0) {
                final int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE);
                output.write(data, offset, chunk);
                bytes -= chunk;
                offset += chunk;
            }
        }
    }

    /**
     * Writes chars from a {@code char[]} to a {@link Writer} using chunked writes.
     * This is intended for writing very large byte arrays which might otherwise cause excessive
     * memory usage if the native code has to allocate a copy.
     *
     * @param data   the char array to write, do not modify during output,
     *               null ignored
     * @param writer the {@link Writer} to write to
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void writeChunked(final char[] data, final Writer writer) throws IOException {
        if (data != null) {
            int bytes = data.length;
            int offset = 0;
            while (bytes > 0) {
                final int chunk = Math.min(bytes, DEFAULT_BUFFER_SIZE);
                writer.write(data, offset, chunk);
                bytes -= chunk;
                offset += chunk;
            }
        }
    }

    /**
     * Writes the {@link #toString()} value of each item in a collection to
     * an {@link OutputStream} line by line, using the specified character
     * encoding and the specified line ending.
     * <p>
     * UTF-16 is written big-endian with no byte order mark.
     * For little-endian, use UTF-16LE. For a BOM, write it to the stream
     * before calling this method.
     * </p>
     *
     * @param lines      the lines to write, null entries produce blank lines
     * @param lineEnding the line separator to use, null is system default
     * @param output     the {@link OutputStream} to write to, not null, not closed
     * @param charset    the charset to use, null means platform default
     * @throws NullPointerException if output is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void writeLines(final Collection<?> lines, String lineEnding, final OutputStream output,
                                  Charset charset) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator();
        }
        if (StandardCharsets.UTF_16.equals(charset)) {
            // don't write a BOM
            charset = StandardCharsets.UTF_16BE;
        }
        final byte[] eolBytes = lineEnding.getBytes(charset);
        for (final Object line : lines) {
            if (line != null) {
                write(line.toString(), output, charset);
            }
            output.write(eolBytes);
        }
    }

    /**
     * Writes the {@link #toString()} value of each item in a collection to
     * an {@link OutputStream} line by line, using the specified character
     * encoding and the specified line ending.
     * <p>
     * Character encoding names can be found at
     * <a href="http://www.iana.org/assignments/character-sets">IANA</a>.
     * </p>
     *
     * @param lines       the lines to write, null entries produce blank lines
     * @param lineEnding  the line separator to use, null is system default
     * @param output      the {@link OutputStream} to write to, not null, not closed
     * @param charsetName the name of the requested charset, null means platform default
     * @throws NullPointerException                         if the output is null
     * @throws IOException                                  if an I/O error occurs
     * @throws java.nio.charset.UnsupportedCharsetException if the encoding is not supported
     * @author potatoxf
     */
    public static void writeLines(final Collection<?> lines, final String lineEnding,
                                  final OutputStream output, final String charsetName) throws IOException {
        writeLines(lines, lineEnding, output, GeneralCharset.toCharset(charsetName));
    }

    /**
     * Writes the {@link #toString()} value of each item in a collection to
     * a {@link Writer} line by line, using the specified line ending.
     *
     * @param lines      the lines to write, null entries produce blank lines
     * @param lineEnding the line separator to use, null is system default
     * @param writer     the {@link Writer} to write to, not null, not closed
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     * @author potatoxf
     */
    public static void writeLines(final Collection<?> lines, String lineEnding,
                                  final Writer writer) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = System.lineSeparator();
        }
        for (final Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }

    /**
     * Returns the given Appendable if it is already a {@link Writer}, otherwise creates a Writer wrapper around the
     * given Appendable.
     *
     * @param appendable the Appendable to wrap or return (not null)
     * @return the given Appendable or a Writer wrapper around the given Appendable
     * @throws NullPointerException if the input parameter is null
     * @author potatoxf
     */
    public static Writer writer(final Appendable appendable) {
        Objects.requireNonNull(appendable, "appendable");
        if (appendable instanceof Writer) {
            return (Writer) appendable;
        }
        if (appendable instanceof StringBuilder) {
            return new StringBuilderWriter((StringBuilder) appendable);
        }
        return new AppendableWriter<>(appendable);
    }

}

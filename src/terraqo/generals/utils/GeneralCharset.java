/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Kit;

import java.nio.charset.*;
import java.util.SortedMap;
import java.util.function.Supplier;

/**
 * 字符集工具类
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public interface GeneralCharset {
    Charset CHARSET_ISO_8859_1 = StandardCharsets.ISO_8859_1, CHARSET_UTF_8 = StandardCharsets.UTF_8, CHARSET_GBK = Charset.forName("GBK");
    SortedMap<String, Charset> STANDARD_CHARSET_MAP = Kit.unmodifiableSortedMap(Charset::name, String.CASE_INSENSITIVE_ORDER,
            StandardCharsets.ISO_8859_1, StandardCharsets.US_ASCII, StandardCharsets.UTF_16, StandardCharsets.UTF_16BE, StandardCharsets.UTF_16LE, StandardCharsets.UTF_8, CHARSET_GBK
    );

    /**
     * Constructs a sorted map from canonical charset names to charset objects required of every implementation of the
     * Java platform.
     * <p>
     * From the Java documentation <a href="https://docs.oracle.com/javase/8/docs/api/java/nio/charset/Charset.html">
     * Standard charsets</a>:
     * </p>
     *
     * @return An immutable, case-insensitive map from canonical charset names to charset objects.
     * @author potatoxf
     * @see Charset#availableCharsets()
     */
    static SortedMap<String, Charset> requiredCharsets() {
        return STANDARD_CHARSET_MAP;
    }

    /**
     * Returns the given Charset or the default Charset if the given Charset is null.
     *
     * @param charset A charset or null.
     * @return the given Charset or the default Charset if the given Charset is null
     */
    static Charset toCharset(final Object charset) {
        return GeneralConverter.toCharset(charset, null);
    }

    /**
     * Returns the given charset if non-null, otherwise return defaultCharset.
     *
     * @param charset        The charset to test, may be null.
     * @param defaultCharset The charset to return if charset is null, may be null.
     * @return a Charset .
     * @author potatoxf
     */
    static Charset toCharset(final Object charset, final Charset defaultCharset) {
        return GeneralConverter.toCharset(charset, defaultCharset);
    }

    /**
     * 转换字符串的字符集编码
     *
     * @param source      字符串
     * @param srcCharset  源字符集，默认ISO-8859-1
     * @param destCharset 目标字符集，默认UTF-8
     * @return 转换后的字符集
     */
    static String convert(String source, String srcCharset, String destCharset) {
        return convert(source, Charset.forName(srcCharset), Charset.forName(destCharset));
    }

    /**
     * 转换字符串的字符集编码
     *
     * @param source      字符串
     * @param srcCharset  源字符集，默认ISO-8859-1
     * @param destCharset 目标字符集，默认UTF-8
     * @return 转换后的字符集
     */
    static String convert(String source, Charset srcCharset, Charset destCharset) {
        srcCharset = GeneralConverter.toCharset(srcCharset);
        destCharset = GeneralConverter.toCharset(destCharset);

        if (GeneralIF.isEmpty(source) || srcCharset.equals(destCharset)) {
            return source;
        }
        return new String(source.getBytes(srcCharset), destCharset);
    }

    /**
     * Returns the given non-null CharsetDecoder or a new default CharsetDecoder.
     *
     * @param charsetDecoder The CharsetDecoder to test.
     * @return the given non-null CharsetDecoder or a new default CharsetDecoder.
     */
    static CharsetDecoder toCharsetDecoder(final CharsetDecoder charsetDecoder) {
        return charsetDecoder != null ? charsetDecoder : Charset.defaultCharset().newDecoder();
    }

    /**
     * Returns the given non-null CharsetEncoder or a new default CharsetEncoder.
     *
     * @param charsetEncoder The CharsetEncoder to test.
     * @return the given non-null CharsetEncoder or a new default CharsetEncoder.
     */
    static CharsetEncoder toCharsetEncoder(final CharsetEncoder charsetEncoder) {
        return toCharsetEncoder(charsetEncoder, () -> Charset.defaultCharset().newEncoder());
    }

    /**
     * Returns the given non-null CharsetEncoder or a new default CharsetEncoder.
     *
     * @param charsetEncoder  The CharsetEncoder to test.
     * @param defaultSupplier The CharsetEncoder supplier to get when charsetEncoder is null.
     * @return the given non-null CharsetEncoder or a new default CharsetEncoder.
     * @author potatoxf
     */
    static CharsetEncoder toCharsetEncoder(final CharsetEncoder charsetEncoder, final Supplier<CharsetEncoder> defaultSupplier) {
        return charsetEncoder != null ? charsetEncoder : defaultSupplier.get();
    }
}

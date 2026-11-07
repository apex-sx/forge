/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Kit;

import java.text.ParseException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * 字符 工具类
 *
 * @author potatoxf
 */
public interface GeneralChar {

    /**
     * 检查并获取unicode的单个Char，如果单个char无法表达则抛出异常
     *
     * @param input unicode
     * @return 返回字符
     */
    static char checkGetSingletonChar(int input) {
        char[] chars = Character.toChars(input);
        if (chars.length != 1) {
            throw new IllegalArgumentException(
                    "Characters are generated incorrectly, unicode ["
                            + input
                            + "] is not in the range of char characters");
        }
        return chars[0];
    }

    /**
     * t <--> \t
     * n <--> \n
     * r <--> \r
     * f <--> \f
     * b <--> \b
     *
     * @param c        转义字符
     * @param unescape 是否解除转义
     * @return 返回转移字符
     */
    static char escapeSpecifyChar(char c, boolean unescape) {
        if (unescape) {
            switch (c) {
                case '\t':
                    return 't';
                case '\n':
                    return 'n';
                case '\r':
                    return 'r';
                case '\f':
                    return 'f';
                case '\b':
                    return 'b';
                default:
                    return c;
            }
        } else {
            switch (c) {
                case 't':
                    return '\t';
                case 'n':
                    return '\n';
                case 'r':
                    return '\r';
                case 'f':
                    return '\f';
                case 'b':
                    return '\b';
                default:
                    return c;
            }
        }
    }

    /**
     * 重复Char符号
     *
     * @param c     符号
     * @param count 个数
     * @return {@code String}
     */
    static String repeatChar(char c, int count) {
        if (count <= 0) return Kit.S_EMPTY;
        int limit = GeneralMath.ceilSqrt(count), len = 10, min = -1;
        for (int i = 2; i <= limit; i++) {
            int v = (count / i) + (count % i) + i;
            if (min < 0 || v < min) {
                min = v;
                len = i;
            }
        }
        if (count > len) {
            char[] chars = new char[len];
            for (int i = 0; i < len; i++) {
                chars[i] = c;
            }
            StringBuilder sb = new StringBuilder(count);
            IntStream.range(0, count / chars.length).forEach(i -> sb.append(chars));
            IntStream.range(0, count % chars.length).forEach(i -> sb.append(chars[0]));
            return sb.toString();
        } else {
            StringBuilder sb = new StringBuilder(count);
            IntStream.range(0, count).forEach(i -> sb.append(c));
            return sb.toString();
        }
    }

    /**
     * 解析Boolean或Null
     *
     * @param chars    字符序列，只处理范围内的数据{@code start<=&&<=end}
     * @param startRef 开始索引引用
     * @param end      结束索引，如果为小于0则为字符数组结尾索引
     * @return 返回解析后的字符串，如果不符号返回null
     */
    static String resolveBooleanOrNull(char[] chars, AtomicInteger startRef, int end) {
        int start = startRef.get();
        if (end < 0) end = chars.length - 1;
        String v = chars[start] == 't' ? "true" : (chars[start] == 'f' ? "false" : "null");
        int length = v.length();
        if (start + length >= end) return null;
        if (v.equalsIgnoreCase(new String(chars, start, length))) {
            startRef.set(start + length - 1);
            return v;
        }
        return null;
    }

    /**
     * 解析引号内的数据，将字符通过{@link #escapeSpecifyChar(char, boolean)}转换，并将unicode编码转换成文字
     *
     * @param chars        字符序列，只处理范围内的数据{@code start<=&&<=end}
     * @param startRef     开始索引引用，该引用位置必须是双引号
     * @param end          结束索引，如果为小于0则为字符数组结尾索引，该引用位置必须是双引号
     * @param doubleQuotes 是否是双引号
     * @return 返回解析后的字符串
     * @throws ParseException 如果解析出现了问题
     */
    static String resolveEscapeQuotes(char[] chars, AtomicInteger startRef, int end, boolean doubleQuotes) throws ParseException {
        char q = doubleQuotes ? '"' : '\'';
        int start = startRef.get();
        if (chars[start] != q) throw new ParseException("", start);
        if (end < 0) end = chars.length - 1;
        else if (chars[end] != q) throw new ParseException("", end);

        StringBuilder sb = new StringBuilder(end - start + 1);
        int current = start;
        for (current += 1; current <= end; current++) {
            char ch = chars[current];
            if (ch == q) break;
            else if (ch == '\\' && current + 1 <= end) {
                ch = chars[++current];
                if (ch == 'u' && current + 4 <= end) {
                    try {
                        sb.append(Character.toChars(Integer.decode("0x" + String.copyValueOf(chars, current + 1, 4))));
                    } catch (Throwable ignored) {
                        sb.append("\\").append(chars, current, 5);
                    }
                    current += 5;
                } else sb.append(GeneralChar.escapeSpecifyChar(ch, false));
            } else sb.append(ch);
        }
        startRef.set(current);
        return sb.toString();
    }

    /**
     * 解析数字
     *
     * @param chars    字符数组
     * @param startRef 开始索引引用
     * @param end      结束索引，如果为小于0则为字符数组结尾索引
     * @return 返回数字字符串
     * @throws ParseException 解析错误
     */
    static String resolveNumber(char[] chars, AtomicInteger startRef, int end) throws ParseException {
        int start = startRef.get();
        if (!("+-".indexOf(chars[start]) >= 0 || '0' <= chars[start] && chars[start] <= '9'))
            throw new ParseException("The start char '" + chars[start] + "' not a number symbol", start);
        if (end < 0) end = chars.length - 1;
        else if (!('0' <= chars[end] && chars[end] <= '9'))
            throw new ParseException("The end char '" + chars[start] + "' not a number symbol", end);
        StringBuilder sb = new StringBuilder(end - start + 1);
        int current = start;
        if ("+-".indexOf(chars[current]) >= 0) {
            current++;
            if ('-' == chars[current]) sb.append('-');
        }
        boolean dot = false, coff = false;
        for (; current <= end; current++) {
            char ch = chars[current];
            if ('0' <= ch && ch <= '9') sb.append(ch);
            else if (ch == '.') {
                if (dot) throw new ParseException("A decimal point already exists", current);
                else dot = true;
                sb.append(ch);
            } else if (ch == 'e' || ch == 'E') {
                if (coff) throw new ParseException("Already contains scientific notation 'e' or 'E'", current);
                if (current + 1 > end) throw new ParseException("The char '" + ch + "' end", current + 1);
                coff = true;
                ch = chars[current + 1];
                if ("+-".indexOf(ch) >= 0) {
                    if (current + 2 > end) throw new ParseException("The char '" + ch + "' end", current + 2);
                    ch = chars[current + 2];
                    if (!('0' <= ch && ch <= '9')) {
                        throw new ParseException("The char " + ch + " not a number symbol", current + 2);
                    }
                } else if (!('0' <= ch && ch <= '9')) {
                    throw new ParseException("The char " + ch + " not a number symbol", current + 1);
                }
                sb.append(chars, current, 3);
                current += 2;
            } else break;
        }
        startRef.set(current - 1);
        return sb.toString();
    }

    /**
     * 解析数据为引号内数据，，并将unicode编码转换成文字
     *
     * @param chars           字符序列，全部字符都进行处理
     * @param unescapeUnicode 是否将字符通过{@link #escapeSpecifyChar(char, boolean)}转换
     * @param unescapeChars   是否解析转义字符
     * @param doubleQuotes    是否是双引号
     * @return 返回解析后的字符串
     */
    static String unescapeQuotes(char[] chars, boolean unescapeUnicode, String unescapeChars, boolean doubleQuotes) {
        char q = doubleQuotes ? '"' : '\'';
        StringBuilder sb = new StringBuilder(chars.length * 2);
        sb.append(q);
        char[] temp = new char[1];
        for (char c : chars) {
            if (unescapeChars != null && !unescapeChars.isEmpty()) {
                if (unescapeChars.indexOf(c) >= 0) sb.append("\\").append(c);
                else sb.append(c);
            } else if (c <= 255 && "\"\\\t\f\b\r\n".indexOf(c) >= 0) {
                sb.append("\\").append(GeneralChar.escapeSpecifyChar(c, true));
            } else if (unescapeUnicode) {
                temp[0] = c;
                int codepoint = Character.codePointAt(temp, 0);
                sb.append("\\u").append(Integer.toHexString(codepoint));
            } else sb.append(c);
        }
        sb.append(q);
        return sb.toString();
    }

    /**
     * Trim all occurrences of the supplied leading character from the given String.
     *
     * @param input            the String to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed String
     */
    static String trimLeadingCharacter(char[] input, String leadingCharacter) {
        return GeneralChar.trim(input, c -> leadingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim all occurrences of the supplied leading character from the given String.
     *
     * @param input            the String to check
     * @param offset           the char[] to offset
     * @param count            the char[] to count
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed String
     */
    static String trimLeadingCharacter(char[] input, int offset, int count, String leadingCharacter) {
        return GeneralChar.trim(input, offset, count, c -> leadingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading leadingPredicate from the given String.
     *
     * @param input            the String to check
     * @param leadingPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trimLeading(char[] input, Predicate<Character> leadingPredicate) {
        return Inner.trimLeading(input, 0, input.length, leadingPredicate);
    }

    /**
     * Trim leading leadingPredicate from the given String.
     *
     * @param input            the String to check
     * @param offset           the char[] to offset
     * @param count            the char[] to count
     * @param leadingPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trimLeading(char[] input, int offset, int count, Predicate<Character> leadingPredicate) {
        return Inner.trimLeading(input, offset, count, leadingPredicate);
    }

    /**
     * Trim all occurrences of the supplied trailing character from the given String.
     *
     * @param input             the String to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed String
     */
    static String trimTrailingCharacter(char[] input, String trailingCharacter) {
        return GeneralChar.trimTrailing(input, c -> trailingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim all occurrences of the supplied trailing character from the given String.
     *
     * @param input             the String to check
     * @param offset            the char[] to offset
     * @param count             the char[] to count
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed String
     */
    static String trimTrailingCharacter(char[] input, int offset, int count, String trailingCharacter) {
        return GeneralChar.trimTrailing(input, offset, count, c -> trailingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim trailing trailingPredicate from the given String.
     *
     * @param input             the String to check
     * @param trailingPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trimTrailing(char[] input, Predicate<Character> trailingPredicate) {
        return Inner.trimTrailing(input, 0, input.length, trailingPredicate);
    }

    /**
     * Trim trailing trailingPredicate from the given String.
     *
     * @param input             the String to check
     * @param offset            the char[] to offset
     * @param count             the char[] to count
     * @param trailingPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trimTrailing(char[] input, int offset, int count, Predicate<Character> trailingPredicate) {
        return Inner.trimTrailing(input, offset, count, trailingPredicate);
    }

    /**
     * Trim all occurrences of the supplied leading and trailing character from the given String.
     *
     * @param input the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static String trimCharacter(char[] input, String trimCharacter) {
        return GeneralChar.trim(input, c -> trimCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim all occurrences of the supplied leading and trailing character from the given String.
     *
     * @param input  the String to check
     * @param offset the char[] to offset
     * @param count  the char[] to count
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static String trimCharacter(char[] input, int offset, int count, String trimCharacter) {
        return GeneralChar.trim(input, offset, count, c -> trimCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading or trailing trimPredicate from the given String.
     *
     * @param input         the String to check
     * @param trimPredicate the trim Predicate
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static String trim(char[] input, Predicate<Character> trimPredicate) {
        return Inner.trim(input, 0, input.length, trimPredicate);
    }

    /**
     * Trim leading or trailing trimPredicate from the given String.
     *
     * @param input         the String to check
     * @param offset        the char[] to offset
     * @param count         the char[] to count
     * @param trimPredicate the trim Predicate
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static String trim(char[] input, int offset, int count, Predicate<Character> trimPredicate) {
        return Inner.trim(input, offset, count, trimPredicate);
    }

    final class Inner {

        /**
         * Trim leading leadingPredicate from the given String.
         *
         * @param input            the String to check
         * @param offset           the char[] to offset
         * @param count            the char[] to count
         * @param leadingPredicate the trim Predicate
         * @return the trimmed String
         */
        private static String trimLeading(char[] input, int offset, int count, Predicate<Character> leadingPredicate) {
            return Inner.trim(input, offset, count, true, false, leadingPredicate);
        }

        /**
         * Trim trailing trailingPredicate from the given String.
         *
         * @param input             the String to check
         * @param offset            the char[] to offset
         * @param count             the char[] to count
         * @param trailingPredicate the trim Predicate
         * @return the trimmed String
         */
        private static String trimTrailing(char[] input, int offset, int count, Predicate<Character> trailingPredicate) {
            return Inner.trim(input, offset, count, false, true, trailingPredicate);
        }

        /**
         * Trim leading or trailing trimPredicate from the given String.
         *
         * @param input         the String to check
         * @param offset        the char[] to offset
         * @param count         the char[] to count
         * @param trimPredicate the trim Predicate
         * @return the trimmed String
         * @see Character#isWhitespace
         */
        private static String trim(char[] input, int offset, int count, Predicate<Character> trimPredicate) {
            return Inner.trim(input, offset, count, true, true, trimPredicate);
        }

        /**
         * Trim leading or trailing trimPredicate from the given String.
         *
         * @param input          the char[] to check
         * @param offset         the char[] to offset
         * @param count          the char[] to count
         * @param isTrimLeading  is trim leading
         * @param isTrimTrailing is trim trailing
         * @param trimPredicate  the trim Predicate
         * @return the trimmed String
         */
        private static String trim(char[] input, int offset, int count,
                                   boolean isTrimLeading, boolean isTrimTrailing,
                                   Predicate<Character> trimPredicate) {
            int si, ei;
            if (offset >= input.length) {
                si = input.length - 1;
                ei = si - 1;
            } else {
                si = Math.max(offset, 0);
                if (si + count >= input.length) ei = input.length - 1;
                else ei = si + count - 1;
            }
            int c = (isTrimTrailing ? 0b10 : 0) | (isTrimLeading ? 0b01 : 0);
            while (c != 0 && si < ei) {
                if ((c & 0b01) == 0b01 && trimPredicate.test(input[si])) {
                    si++;
                } else {
                    c = c & 0b10;
                }
                if ((c & 0b10) == 0b10 && trimPredicate.test(input[ei])) {
                    ei--;
                } else {
                    c = c & 0b01;
                }
            }
            return new String(input, si, ei - si + 1);
        }

        private Inner() {
        }
    }
}

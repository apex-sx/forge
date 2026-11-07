/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;


import com.github.potatoxf.generals.Kit;
import com.github.potatoxf.generals.function.RangeOperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static com.github.potatoxf.generals.Kit.P_LINE;

/**
 * 字符序列 工具类
 *
 * @author potatoxf
 */
public interface GeneralCharSequence extends GeneralChar {

    /**
     * 连接字符序列
     *
     * @param inputs 对象序列
     * @return 返回连接后的字符串
     */
    static String concat(Object... inputs) {
        return GeneralCharSequence.concat(null, inputs);
    }

    /**
     * 连接字符序列
     *
     * @param split  分割字符序列
     * @param inputs 对象序列
     * @return 返回连接后的字符串
     */
    static String concat(CharSequence split, Object... inputs) {
        if (split == null) split = ",";
        StringBuilder sb = new StringBuilder();
        for (Object object : inputs) {
            if (object == null || (object instanceof CharSequence && ((CharSequence) object).length() == 0)) continue;
            sb.append(object).append(',');
        }
        sb.setLength(sb.length() - split.length());
        return sb.toString();
    }

    /**
     * 转换首字符大小写
     *
     * @param input      字符序列
     * @param capitalize 如果为true则首字母大写，否则小写
     * @return {@code CharSequence}
     */
    static String capitalize(CharSequence input, boolean capitalize) {
        if (input == null) return "";
        int length = input.length();
        if (length == 0) return "";
        String str = input.toString();
        if (length == 1) return capitalize ? str.toLowerCase() : str.toUpperCase();
        String h = str.substring(0, 1);
        return (!capitalize ? h.toLowerCase() : h.toUpperCase()) + str.substring(1);
    }

    /**
     * Check whether the given CharSequence contains any whitespace characters.
     *
     * @param input the CharSequence to check (may be <code>null</code>)
     * @return <code>true</code> if the CharSequence is not empty and
     * contains at least 1 whitespace character
     * @see Character#isWhitespace
     */
    static boolean containsWhitespace(CharSequence input) {
        if (GeneralIF.isEmpty(input)) return false;
        int strLen = input.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(input.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 清理字符串构建器
     *
     * @param input {@code StringBuilder}
     * @return 返回清理之前的字符串
     */
    static String getStringAndClean(StringBuilder input) {
        String result = input.toString();
        input.setLength(0);
        return result;
    }

    /**
     * 清理字符串构建器
     *
     * @param input {@code StringBuffer}
     * @return 返回清理之前的字符串
     */
    static String getStringAndClean(StringBuffer input) {
        String result = input.toString();
        input.setLength(0);
        return result;
    }

    /**
     * 获取字符
     *
     * @param input          {@code StringBuffer}
     * @param trimTailLength 清除尾部长度，如果超出字符长度则全部删除
     * @return 字符串
     */
    static String getTrimTailStringAndClean(StringBuffer input, int trimTailLength) {
        int i = input.length() - trimTailLength;
        if (i < 0) return Kit.S_EMPTY;
        String result = input.delete(i, Integer.MAX_VALUE).toString();
        input.setLength(0);
        return result;
    }

    /**
     * 获取字符
     *
     * @param input          {@code StringBuffer}
     * @param trimTailLength 清除尾部长度，如果超出字符长度则全部删除
     * @return 字符串
     */
    static String getTrimTailStringAndClean(StringBuilder input, int trimTailLength) {
        int i = input.length() - trimTailLength;
        if (i < 0) return Kit.S_EMPTY;
        String result = input.delete(i, Integer.MAX_VALUE).toString();
        input.setLength(0);
        return result;
    }

    /**
     * String --> 'String'
     * Object --> 'Object'
     * null --> ''
     */
    static String quote(CharSequence input, boolean doubleQuotes) {
        final char q = doubleQuotes ? '"' : '\'';
        if (input != null) {
            input = GeneralCharSequence.trimCharacter(input, CharSequence::subSequence, " \t\r\n\f'\"");
            int length = input.length();
            StringBuilder sb = new StringBuilder(length).append(q);
            char c;
            for (int i = 0; i < length; i++) {
                c = input.charAt(i);
                if (c == q) {
                    sb.append("\\").append(q);
                } else {
                    sb.append(c);
                }
            }
            sb.append(q);
            return sb.toString();
        } else {
            return q + "" + q;
        }
    }

    /**
     * String --> 'String'
     * Object --> Object
     * null -->
     */
    static String quoteIfString(Object input, boolean doubleQuotes) {
        return (input instanceof CharSequence ? GeneralCharSequence.quote((CharSequence) input, doubleQuotes) : "");
    }

    /**
     * 分割行
     *
     * @param input 输入字符序列
     * @return 返回序列
     */
    static List<String> splitLine(CharSequence input) {
        String[] lines = P_LINE.split(input);
        List<String> lineList = new ArrayList<>(lines.length);
        for (String line : lines) {
            if (line == null || line.isEmpty()) lineList.add("");
            else lineList.add(line);
        }
        return lineList;
    }

    /**
     * 将字符串构建器内容添加至容器里并清除字符串串构建器里的内容
     *
     * @param container 字符串容器
     * @param input     字符串串构建器
     */
    static void addStringAndClear(Collection<String> container, StringBuilder input) {
        String string = input.toString();
        if (string.length() != 0) {
            container.add(string);
        }
        input.setLength(0);
    }

    /**
     * 将字符串构建器内容添加至容器里并清除字符串串构建器里的内容
     *
     * @param container 字符串容器
     * @param input     字符串串构建器
     */
    static void addStringAndClear(Collection<String> container, StringBuffer input) {
        String string = input.toString();
        if (string.length() != 0) {
            container.add(string);
        }
        input.setLength(0);
    }

    /**
     * Trim leading whitespace from the given String.
     *
     * @param input the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static <T extends CharSequence> T trimLeadingWhitespace(T input, RangeOperator<T> sub) {
        return GeneralCharSequence.trimLeading(input, sub, Character::isWhitespace);
    }

    /**
     * Trim leading whitespace from the given String.
     *
     * @param input the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static <T extends CharSequence> T trimLeadingWhitespace(T input, int offset, int count, RangeOperator<T> sub) {
        return GeneralCharSequence.trimLeading(input, offset, count, sub, Character::isWhitespace);
    }

    /**
     * Trim leading character from the given String.
     *
     * @param input            the CharSequence to check
     * @param sub              the CharSequence to sub
     * @param leadingCharacter the leading character to be trimmed
     * @param <T>              input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimLeadingCharacter(T input, RangeOperator<T> sub, String leadingCharacter) {
        return GeneralCharSequence.trimLeading(input, 0, input.length(), sub, c -> leadingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading character from the given String.
     *
     * @param input            the CharSequence to check
     * @param offset           the CharSequence to offset
     * @param count            the CharSequence to count
     * @param sub              the CharSequence to sub
     * @param leadingCharacter the leading character to be trimmed
     * @param <T>              input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimLeadingCharacter(T input, int offset, int count, RangeOperator<T> sub, String leadingCharacter) {
        return GeneralCharSequence.trimLeading(input, offset, count, sub, c -> leadingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param sub           the CharSequence to sub
     * @param trimPredicate the trim Predicate
     * @param <T>           input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimLeading(T input, RangeOperator<T> sub, Predicate<Character> trimPredicate) {
        return Inner.trimLeading(input, 0, input.length(), sub, trimPredicate);
    }

    /**
     * Trim leading predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param offset        the CharSequence to offset
     * @param count         the CharSequence to count
     * @param sub           the CharSequence to sub
     * @param trimPredicate the trim Predicate
     * @param <T>           input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimLeading(T input, int offset, int count, RangeOperator<T> sub, Predicate<Character> trimPredicate) {
        return Inner.trimLeading(input, offset, count, sub, trimPredicate);
    }

    /**
     * Trim trailing whitespace from the given String.
     *
     * @param input the CharSequence to check
     * @param sub   the CharSequence to sub
     * @param <T>   input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimTrailingWhitespace(T input, RangeOperator<T> sub) {
        return GeneralCharSequence.trimTrailing(input, sub, Character::isWhitespace);
    }

    /**
     * Trim trailing whitespace from the given String.
     *
     * @param input  the CharSequence to check
     * @param offset the CharSequence to offset
     * @param count  the CharSequence to count
     * @param sub    the CharSequence to sub
     * @param <T>    input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimTrailingWhitespace(T input, int offset, int count, RangeOperator<T> sub) {
        return GeneralCharSequence.trimTrailing(input, offset, count, sub, Character::isWhitespace);
    }

    /**
     * Trim trailing character from the given String.
     *
     * @param input             the CharSequence to check
     * @param sub               the CharSequence to sub
     * @param trailingCharacter the trailing character to be trimmed
     * @param <T>               input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimTrailingCharacter(T input, RangeOperator<T> sub, String trailingCharacter) {
        return GeneralCharSequence.trimTrailing(input, 0, input.length(), sub, c -> trailingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim trailing character from the given String.
     *
     * @param input             the CharSequence to check
     * @param offset            the CharSequence to offset
     * @param count             the CharSequence to count
     * @param sub               the CharSequence to sub
     * @param trailingCharacter the trailing character to be trimmed
     * @param <T>               input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimTrailingCharacter(T input, int offset, int count, RangeOperator<T> sub, String trailingCharacter) {
        return GeneralCharSequence.trimTrailing(input, offset, count, sub, c -> trailingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim trailing predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param sub           the CharSequence to sub
     * @param trimPredicate the trim Predicate
     * @param <T>           input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimTrailing(T input, RangeOperator<T> sub, Predicate<Character> trimPredicate) {
        return Inner.trimTrailing(input, 0, input.length(), sub, trimPredicate);
    }

    /**
     * Trim trailing predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param offset        the CharSequence to offset
     * @param count         the CharSequence to count
     * @param sub           the CharSequence to sub
     * @param trimPredicate the trim Predicate
     * @param <T>           input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimTrailing(T input, int offset, int count, RangeOperator<T> sub, Predicate<Character> trimPredicate) {
        return Inner.trimTrailing(input, offset, count, sub, trimPredicate);
    }

    /**
     * Trim leading or trailing whitespace from the given String.
     *
     * @param input the CharSequence to check
     * @param sub   the CharSequence to sub
     * @param <T>   input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimWhitespace(T input, RangeOperator<T> sub) {
        return GeneralCharSequence.trim(input, 0, input.length(), sub, Character::isWhitespace);
    }

    /**
     * Trim leading or trailing whitespace from the given String.
     *
     * @param input  the CharSequence to check
     * @param offset the CharSequence to offset
     * @param count  the CharSequence to count
     * @param sub    the CharSequence to sub
     * @param <T>    input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimWhitespace(T input, int offset, int count, RangeOperator<T> sub) {
        return GeneralCharSequence.trim(input, offset, count, sub, Character::isWhitespace);
    }

    /**
     * Trim leading or trailing character from the given String.
     *
     * @param input         the CharSequence to check
     * @param sub           the CharSequence to sub
     * @param trimCharacter the trim Predicate
     * @param <T>           input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimCharacter(T input, RangeOperator<T> sub, String trimCharacter) {
        return GeneralCharSequence.trim(input, sub, c -> trimCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading or trailing character from the given String.
     *
     * @param input         the CharSequence to check
     * @param offset        the CharSequence to offset
     * @param count         the CharSequence to count
     * @param sub           the CharSequence to sub
     * @param trimCharacter the trim Predicate
     * @param <T>           input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trimCharacter(T input, int offset, int count, RangeOperator<T> sub, String trimCharacter) {
        return GeneralCharSequence.trim(input, offset, count, sub, c -> trimCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading or trailing predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param sub           the CharSequence to sub
     * @param trimPredicate the trim Predicate
     * @param <T>           input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trim(T input, RangeOperator<T> sub, Predicate<Character> trimPredicate) {
        return Inner.trim(input, 0, input.length(), sub, trimPredicate);
    }

    /**
     * Trim leading or trailing predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param offset        the CharSequence to offset
     * @param count         the CharSequence to count
     * @param sub           the CharSequence to sub
     * @param trimPredicate the trim Predicate
     * @param <T>           input type
     * @return the trimmed String
     */
    static <T extends CharSequence> T trim(T input, int offset, int count, RangeOperator<T> sub, Predicate<Character> trimPredicate) {
        return Inner.trim(input, offset, count, sub, trimPredicate);
    }

    final class Inner {

        /**
         * Trim leading predicate from the given String.
         *
         * @param input         the CharSequence to check
         * @param offset        the CharSequence to offset
         * @param count         the CharSequence to count
         * @param sub           the CharSequence to sub
         * @param trimPredicate the trim Predicate
         * @param <T>           input type
         * @return the trimmed String
         */
        private static <T extends CharSequence> T trimLeading(T input, int offset, int count, RangeOperator<T> sub, Predicate<Character> trimPredicate) {
            return Inner.doTrim(input, offset, count, sub, trimPredicate, true, false);
        }

        /**
         * Trim trailing predicate from the given String.
         *
         * @param input         the CharSequence to check
         * @param offset        the CharSequence to offset
         * @param count         the CharSequence to count
         * @param sub           the CharSequence to sub
         * @param trimPredicate the trim Predicate
         * @param <T>           input type
         * @return the trimmed String
         */
        private static <T extends CharSequence> T trimTrailing(T input, int offset, int count, RangeOperator<T> sub, Predicate<Character> trimPredicate) {
            return Inner.doTrim(input, offset, count, sub, trimPredicate, false, true);
        }

        /**
         * Trim leading or trailing predicate from the given String.
         *
         * @param input         the CharSequence to check
         * @param offset        the CharSequence to offset
         * @param count         the CharSequence to count
         * @param sub           the CharSequence to sub
         * @param trimPredicate the trim Predicate
         * @param <T>           input type
         * @return the trimmed String
         */
        private static <T extends CharSequence> T trim(T input, int offset, int count, RangeOperator<T> sub, Predicate<Character> trimPredicate) {
            return Inner.doTrim(input, offset, count, sub, trimPredicate, true, true);
        }

        /**
         * Trim leading or trailing predicate from the given String.
         *
         * @param input          the CharSequence to check
         * @param offset         the CharSequence to offset
         * @param count          the CharSequence to count
         * @param sub            the CharSequence to sub
         * @param isTrimLeading  is trim leading
         * @param isTrimTrailing is trim trailing
         * @param trimPredicate  the trim Predicate
         * @param <T>            input type
         * @return the trimmed String
         */
        private static <T extends CharSequence> T doTrim(T input, int offset, int count, RangeOperator<T> sub, Predicate<Character> trimPredicate, boolean isTrimLeading, boolean isTrimTrailing) {
            if (input == null) return input;
            final int length = input.length();
            if (length <= 0) return input;
            int si, ei, c = (isTrimTrailing ? 0b10 : 0) | (isTrimLeading ? 0b01 : 0);
            if (offset >= length) {
                si = length - 1;
                ei = si - 1;
            } else {
                si = Math.max(offset, 0);
                if (si + count >= length) ei = length - 1;
                else ei = si + count - 1;
            }
            while (c != 0 && si < ei) {
                if ((c & 0b01) == 0b01 && trimPredicate.test(input.charAt(si))) {
                    si++;
                } else {
                    c = c & 0b10;
                }
                if ((c & 0b10) == 0b10 && trimPredicate.test(input.charAt(ei))) {
                    ei--;
                } else {
                    c = c & 0b01;
                }
            }
            return ((si > 0) || (ei < length - 1)) ? ((input = sub.apply(input, si, ei + 1)).length() != 0 ? input : null) : input;
        }

        private Inner() {
        }
    }
}

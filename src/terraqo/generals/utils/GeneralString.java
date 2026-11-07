/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Kit;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 字符串格式化
 *
 * @author potatoxf
 */
public interface GeneralString extends GeneralCharSequence {

    /**
     * 替换文本中的内容
     *
     * @param text     文本
     * @param mappings 替换映射
     * @return 返回处理后的字符串
     */
    static String replace(String text, String... mappings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mappings.length; i += 2) {
            sb.setLength(0);
            String oldString = mappings[i];
            if (oldString.isEmpty()) continue;
            String newString = mappings[i + 1];
            int c, s = 0;
            while ((c = text.indexOf(oldString, s)) >= 0) {
                sb.append(text, s, c);
                sb.append(newString);
                s = c + oldString.length();
            }
            if (s != 0) {
                if (s != text.length()) {
                    sb.append(text, s, text.length());
                }
                text = sb.toString();
            }
        }
        return text;
    }

    /**
     * 格式化文本, {} 表示占位符<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param template 字符串模板
     * @param args     参数列表
     * @return 结果
     */
    static String format(String template, Object... args) {
        if (GeneralIF.isEmpty(template) || GeneralIF.isEmpty(args)) {
            return template;
        }
        final int strPatternLength = template.length();

        // 初始化定义好的长度以获得更好的性能
        StringBuilder sb = new StringBuilder(strPatternLength + 50);

        int handledPosition = 0;
        int delimIndex;// 占位符所在位置
        for (int argIndex = 0; argIndex < args.length; argIndex++) {
            delimIndex = template.indexOf("{}", handledPosition);
            if (delimIndex == -1) {
                if (handledPosition == 0) {
                    return template;
                } else { // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
                    sb.append(template, handledPosition, strPatternLength);
                    return sb.toString();
                }
            } else {
                if (delimIndex > 0 && template.charAt(delimIndex - 1) == '\\') {
                    if (delimIndex > 1 && template.charAt(delimIndex - 2) == '\\') {
                        // 转义符之前还有一个转义符，占位符依旧有效
                        sb.append(template, handledPosition, delimIndex - 1);
                        sb.append(args[argIndex]);
                        handledPosition = delimIndex + 2;
                    } else {
                        // 占位符被转义
                        argIndex--;
                        sb.append(template, handledPosition, delimIndex - 1);
                        sb.append('{');
                        handledPosition = delimIndex + 1;
                    }
                } else {
                    // 正常占位符
                    sb.append(template, handledPosition, delimIndex);
                    sb.append(args[argIndex]);
                    handledPosition = delimIndex + 2;
                }
            }
        }
        // 加入最后一个占位符后所有的字符
        sb.append(template, handledPosition, template.length());

        return sb.toString();
    }

    /**
     * 用[]包裹字符串
     *
     * @param input 输入对象
     * @return 返回字符串
     */
    static String wrap(Object input) {
        return input == null ? "" : "[" + input + "]";
    }

    static String[] handleStringArray(Function<String, String> input, String... strings) {
        if (strings == null) return Kit.EMPTY_ARRAY_STRING;
        String[] results = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            results[i] = input.apply(strings[0]);
        }
        return results;
    }

    static String[] toLowerCase(String... strings) {
        return GeneralString.handleStringArray(String::toLowerCase, strings);
    }

    static String[] toUpperCase(String... strings) {
        return GeneralString.handleStringArray(String::toUpperCase, strings);
    }

    static String clean(String input, String string) {
        Set<Integer> set = string.codePoints().boxed().collect(Collectors.toSet());
        StringBuilder sb = new StringBuilder(input.length());
        input.codePoints().forEach(i -> {
            if (!set.contains(i)) sb.appendCodePoint(i);
        });
        return sb.toString();
    }

    /**
     * 检查并获取unicode的单个Char，如果单个char无法表达则抛出异常
     *
     * @param input unicode字符
     * @return 返回字符
     */
    static char[] checkGetSingletonChars(String input) {
        int[] codePoints = input.codePoints().toArray();
        char[] result = new char[codePoints.length];
        for (int i = 0; i < codePoints.length; i++) {
            result[i] = GeneralChar.checkGetSingletonChar(codePoints[i]);
        }
        return result;
    }

    /**
     * 判断String是否是整数<br>
     * 支持10、16进制
     *
     * @param input String
     * @return 是否为整数
     */
    static boolean isIntegerNumber(String input) {
        boolean[] numberString = GeneralString.isNumberTypes(input);
        return numberString[0] && !numberString[1];
    }

    /**
     * 判断String是否是整数<br>
     * 支持10、16进制
     *
     * @param input String
     * @return 是否为整数
     */
    static boolean isIntegerNumberHuman(String input) {
        boolean[] numberString = GeneralString.isNumberTypes(input);
        return numberString[0] && !numberString[1] && !numberString[2];
    }

    /**
     * 判断字符串是否是浮点数
     *
     * @param input String
     * @return 是否为{@link Double}类型
     */
    static boolean isDecimalNumber(String input) {
        boolean[] numberString = GeneralString.isNumberTypes(input);
        return numberString[0] && numberString[1];
    }

    /**
     * 判断字符串是否是浮点数
     *
     * @param input String
     * @return 是否为{@link Double}类型
     */
    static boolean isDecimalNumberHuman(String input) {
        boolean[] numberString = GeneralString.isNumberTypes(input);
        return numberString[0] && !numberString[1] && !numberString[2];
    }

    /**
     * 是否为数字
     *
     * @param input 字符串值
     * @return 是否为数字
     */
    static boolean isNumber(String input) {
        return GeneralString.isNumberTypes(input)[0];
    }

    /**
     * 是否为人类易懂数字，排除16进制
     *
     * @param input 字符串值
     * @return 是否为数字
     */
    static boolean isNumberHuman(String input) {
        boolean[] numberString = GeneralString.isNumberTypes(input);
        return numberString[0] && !numberString[2];
    }

    /**
     * 是否为数字
     *
     * @param input 字符串值
     * @return 是否为数字
     */
    static boolean[] isNumberTypes(String input) {
        // 0 isNumber 1 isDecimal 2 isHex
        boolean[] error = {false, false, false};
        char[] chars = input.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;
                if (i == sz) {
                    // input == "0x"
                    return error;
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return error;
                    }
                }
                return new boolean[]{true, false, true};
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return error;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return error;
                }
                if (!foundDigit) {
                    return error;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return error;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return error;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return new boolean[]{true, hasDecPoint, false};
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return error;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return error;
                }
                // single trailing decimal point after non-exponent is ok
                return new boolean[]{foundDigit, true, false};
            }
            if (!allowSigns
                    && (chars[i] == 'd' || chars[i] == 'D' || chars[i] == 'f' || chars[i] == 'F')) {
                return new boolean[]{foundDigit, hasDecPoint, false};
            }
            if (chars[i] == 'l' || chars[i] == 'L') {
                // not allowing L with an exponent
                return new boolean[]{foundDigit && !hasExp, hasDecPoint, false};
            }
            // last character is illegal
            return error;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return new boolean[]{!allowSigns && foundDigit, hasDecPoint, false};
    }

    /**
     * Delete all occurrences of the given substring.
     *
     * @param input   the original String
     * @param pattern the pattern to delete all occurrences of
     * @return the resulting String
     */
    static String deleteString(String input, String pattern) {
        return GeneralString.replace(input, pattern, "");
    }

    /**
     * Delete any character in a given String.
     *
     * @param input the original String
     * @param chars a set of characters to delete.
     *              E.g. "az\n" will delete 'a's, 'z's and new lines.
     * @return the resulting String
     */
    static String deleteChars(String input, String chars) {
        if (GeneralIF.isEmpty(input)) return input;
        if (GeneralIF.isNoEmpty(chars)) return input;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (chars.indexOf(c) >= 0) continue;
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Returns the number of occurrences the substring {@code sub} appears in string {@code input}.
     *
     * @param input string to search in. Return 0 if this is null.
     * @param sub   string to search for. Return 0 if this is null.
     * @return the number of occurrences the substring {@code sub} appears in string {@code input}.
     */
    static int countOccurrencesOf(String input, String sub) {
        if (input == null || sub == null || input.isEmpty() || sub.isEmpty()) return 0;
        int count = 0, pos = 0, idx;
        while ((idx = input.indexOf(sub, pos)) != -1) {
            ++count;
            pos = idx + sub.length();
        }
        return count;
    }

    /**
     * Replace all occurrences of a substring within a string with
     * another string.
     *
     * @param input      String to examine
     * @param oldPattern String to replace
     * @param newPattern String to insert
     * @return a String with the replacements
     */
    static String replace(String input, String oldPattern, String newPattern) {
        if (GeneralIF.isEmpty(input)) return input;
        if (GeneralIF.isEmpty(oldPattern)) return input;
        if (GeneralIF.isEmpty(newPattern)) return input;
        StringBuilder sb = new StringBuilder();
        int pos = 0; // our position in the old string
        int index = input.indexOf(oldPattern);
        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sb.append(input, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
            index = input.indexOf(oldPattern, pos);
        }
        sb.append(input.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }

    /**
     * 分割字符串成键值对形式
     *
     * @param input       输入字符串
     * @param splitSymbol 分割符号
     * @param greedy      是否贪婪匹配
     * @return {@code Pair<String, String>}
     */
    static String[] splitToPair(String input, String splitSymbol, boolean greedy) {
        if (input == null) return new String[]{"", ""};
        if (splitSymbol == null) return new String[]{input, ""};
        int i = greedy ? input.lastIndexOf(splitSymbol) : input.indexOf(splitSymbol);
        if (i <= 0 || i >= input.length()) return new String[]{input, ""};
        return new String[]{input.substring(0, i), input.substring(i + splitSymbol.length())};
    }

    /**
     * 改进JDK subString<br>
     * index从0开始计算，最后一个字符为-1<br>
     * 如果from和to位置一样，返回 "" <br>
     * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
     *
     * @param input            String
     * @param fromIndexInclude 开始的index（包括）
     * @param toIndexExclude   结束的index（不包括）
     * @return 字串
     */
    static String subString(String input, int fromIndexInclude, int toIndexExclude) {
        if (input == null || input.isEmpty()) return "";
        int[] idx = GeneralIndex.idx(input.length(), fromIndexInclude, toIndexExclude, true);
        if (idx[0] == idx[1]) return "";
        return input.substring(idx[0], idx[1]);
    }

    /**
     * 简单切分字符串，不进行其它操作
     *
     * @param input     输入字符串
     * @param splitChar 切分字符
     * @param trimChar  清除字符
     * @return 返回切分后的字符串数组
     */

    static List<String> splitString(String input, String splitChar, String trimChar) {
        return GeneralString.splitString(input, splitChar, trimChar, true);
    }

    /**
     * 简单切分字符串，不进行其它操作
     *
     * @param input       输入字符串
     * @param splitChar   切分字符
     * @param trimChar    清除字符
     * @param ignoreEmpty 是否忽略为空
     * @return 返回切分后的字符串数组
     */

    static List<String> splitString(String input, String splitChar, String trimChar, boolean ignoreEmpty) {
        return GeneralString.splitString(input, splitChar, trimChar, s -> s, ignoreEmpty);
    }

    /**
     * 简单切分字符串，不进行其它操作
     *
     * @param input     输入字符串
     * @param splitChar 切分字符
     * @param trimChar  清除字符
     * @param converter 字符串转换器
     * @return 返回切分后的字符串数组
     */

    static <T> List<T> splitString(String input, String splitChar, String trimChar, Function<String, T> converter) {
        return GeneralString.splitString(input, splitChar, trimChar, converter, true);
    }

    /**
     * 简单切分字符串，不进行其它操作
     *
     * @param input       输入字符串
     * @param splitChar   切分字符
     * @param trimChar    清除字符
     * @param converter   字符串转换器
     * @param ignoreEmpty 是否忽略为空
     * @return 返回切分后的字符串数组
     */

    static <T> List<T> splitString(String input, String splitChar, String trimChar, Function<String, T> converter, boolean ignoreEmpty) {
        if (input == null || input.isEmpty()) return Collections.emptyList();
        int length = input.length();
        List<T> list = new ArrayList<>();
        Consumer<String> consumer = (string) -> {
            if (trimChar != null) {
                string = GeneralString.trimCharacter(string, trimChar);
            }
            T result = converter.apply(string);
            if (result != null) {
                list.add(result);
            }
        };
        if (splitChar == null || splitChar.isEmpty()) {
            consumer.accept(input);
            return list;
        }
        final int scLength = splitChar.length();
        int si = 0, i;
        while ((i = input.indexOf(splitChar, si)) >= 0) {
            String substring = input.substring(si, i);
            if (!ignoreEmpty || substring.length() > 0) {
                consumer.accept(substring);
            }
            si = i + scLength;
        }
        String substring = input.substring(si);
        if (!ignoreEmpty || substring.length() > 0) {
            consumer.accept(input.substring(si));
        }
        return list;
    }

    /**
     * 清除前导 {@code --}，{@code -}字符串
     *
     * @param string 输入字符串
     * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串
     */
    static String trimLeadingHyphens(String string) {
        return GeneralString.trimLeadingPrefix(string, "--", "-");
    }

    /**
     * 清除前缀字符串
     *
     * @param input          输入字符串
     * @param selectPrefixes 选择要删除的前缀
     * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串，如果没有前缀则原样返回
     */

    static String trimLeadingPrefix(String input, String... selectPrefixes) {
        if (input == null) return "";
        for (String prefix : selectPrefixes) {
            if (input.startsWith(prefix)) {
                return input.substring(prefix.length());
            }
        }
        return input;
    }

    /**
     * 清除后缀字符串
     *
     * @param input          输入字符串
     * @param selectSuffixes 选择要删除的后缀
     * @return {@code String}，如果为输入字符串为 {@code null}返回空字符串，如果没有后缀则原样返回
     */
    static String trimLeadingSuffix(String input, String... selectSuffixes) {
        if (input == null) return "";
        for (String suffix : selectSuffixes) {
            if (input.endsWith(suffix)) {
                return input.substring(0, input.length() - suffix.length());
            }
        }
        return input;
    }

    /**
     * Trim <i>all</i> trimPredicate from the given String:
     * leading, trailing, and intermediate characters.
     *
     * @param input the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static String trimAllWhitespace(CharSequence input) {
        return trimAll(input, Character::isWhitespace);
    }

    /**
     * Trim <i>all</i> whitespace from the given String:
     * leading, trailing, and intermediate characters.
     *
     * @param input the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static String trimAll(CharSequence input, Predicate<Character> trimPredicate) {
        if (input == null || input.length() <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder(input);
        int index = 0;
        while (sb.length() > index) {
            if (trimPredicate.test(sb.charAt(index))) {
                sb.deleteCharAt(index);
            } else {
                index++;
            }
        }
        return sb.toString();
    }

    /**
     * Trim leading whitespace from the given String.
     *
     * @param input the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static String trimLeadingWhitespace(String input) {
        return GeneralCharSequence.trimLeading(input, String::substring, Character::isWhitespace);
    }

    /**
     * Trim leading whitespace from the given String.
     *
     * @param input the String to check
     * @return the trimmed String
     * @see Character#isWhitespace
     */
    static String trimLeadingWhitespace(String input, int offset, int count) {
        return GeneralCharSequence.trimLeading(input, offset, count, String::substring, Character::isWhitespace);
    }

    /**
     * Trim leading character from the given String.
     *
     * @param input            the CharSequence to check
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed String
     */
    static String trimLeadingCharacter(String input, String leadingCharacter) {
        return GeneralCharSequence.trimLeading(input, 0, input.length(), String::substring, c -> leadingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading character from the given String.
     *
     * @param input            the CharSequence to check
     * @param offset           the CharSequence to offset
     * @param count            the CharSequence to count
     * @param leadingCharacter the leading character to be trimmed
     * @return the trimmed String
     */
    static String trimLeadingCharacter(String input, int offset, int count, String leadingCharacter) {
        return GeneralCharSequence.trimLeading(input, offset, count, String::substring, c -> leadingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param trimPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trimLeading(String input, Predicate<Character> trimPredicate) {
        return GeneralCharSequence.trimLeading(input, 0, input.length(), String::substring, trimPredicate);
    }

    /**
     * Trim leading predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param offset        the CharSequence to offset
     * @param count         the CharSequence to count
     * @param trimPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trimLeading(String input, int offset, int count, Predicate<Character> trimPredicate) {
        return GeneralCharSequence.trimLeading(input, offset, count, String::substring, trimPredicate);
    }

    /**
     * Trim trailing whitespace from the given String.
     *
     * @param input the CharSequence to check
     * @return the trimmed String
     */
    static String trimTrailingWhitespace(String input) {
        return GeneralCharSequence.trimTrailing(input, String::substring, Character::isWhitespace);
    }

    /**
     * Trim trailing whitespace from the given String.
     *
     * @param input  the CharSequence to check
     * @param offset the CharSequence to offset
     * @param count  the CharSequence to count
     * @return the trimmed String
     */
    static String trimTrailingWhitespace(String input, int offset, int count) {
        return GeneralCharSequence.trimTrailing(input, offset, count, String::substring, Character::isWhitespace);
    }

    /**
     * Trim trailing character from the given String.
     *
     * @param input             the CharSequence to check
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed String
     */
    static String trimTrailingCharacter(String input, String trailingCharacter) {
        return GeneralCharSequence.trimTrailing(input, 0, input.length(), String::substring, c -> trailingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim trailing character from the given String.
     *
     * @param input             the CharSequence to check
     * @param offset            the CharSequence to offset
     * @param count             the CharSequence to count
     * @param trailingCharacter the trailing character to be trimmed
     * @return the trimmed String
     */
    static String trimTrailingCharacter(String input, int offset, int count, String trailingCharacter) {
        return GeneralCharSequence.trimTrailing(input, offset, count, String::substring, c -> trailingCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim trailing predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param trimPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trimTrailing(String input, Predicate<Character> trimPredicate) {
        return GeneralCharSequence.trimTrailing(input, 0, input.length(), String::substring, trimPredicate);
    }

    /**
     * Trim trailing predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param offset        the CharSequence to offset
     * @param count         the CharSequence to count
     * @param trimPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trimTrailing(String input, int offset, int count, Predicate<Character> trimPredicate) {
        return GeneralCharSequence.trimTrailing(input, offset, count, String::substring, trimPredicate);
    }

    /**
     * Trim leading or trailing whitespace from the given String.
     *
     * @param input the CharSequence to check
     * @return the trimmed String
     */
    static String trimWhitespace(String input) {
        return GeneralCharSequence.trim(input, 0, input.length(), String::substring, Character::isWhitespace);
    }

    /**
     * Trim leading or trailing whitespace from the given String.
     *
     * @param input  the CharSequence to check
     * @param offset the CharSequence to offset
     * @param count  the CharSequence to count
     * @return the trimmed String
     */
    static String trimWhitespace(String input, int offset, int count) {
        return GeneralCharSequence.trim(input, offset, count, String::substring, Character::isWhitespace);
    }

    /**
     * Trim leading or trailing character from the given String.
     *
     * @param input         the CharSequence to check
     * @param trimCharacter the trim Predicate
     * @return the trimmed String
     */
    static String trimCharacter(String input, String trimCharacter) {
        return GeneralCharSequence.trim(input, String::substring, c -> trimCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading or trailing character from the given String.
     *
     * @param input         the CharSequence to check
     * @param offset        the CharSequence to offset
     * @param count         the CharSequence to count
     * @param trimCharacter the trim Predicate
     * @return the trimmed String
     */
    static String trimCharacter(String input, int offset, int count, String trimCharacter) {
        return GeneralCharSequence.trim(input, offset, count, String::substring, c -> trimCharacter.indexOf(c) >= 0);
    }

    /**
     * Trim leading or trailing predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param trimPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trim(String input, Predicate<Character> trimPredicate) {
        return GeneralCharSequence.trim(input, 0, input.length(), String::substring, trimPredicate);
    }

    /**
     * Trim leading or trailing predicate from the given String.
     *
     * @param input         the CharSequence to check
     * @param offset        the CharSequence to offset
     * @param count         the CharSequence to count
     * @param trimPredicate the trim Predicate
     * @return the trimmed String
     */
    static String trim(String input, int offset, int count, Predicate<Character> trimPredicate) {
        return GeneralCharSequence.trim(input, offset, count, String::substring, trimPredicate);
    }

    /**
     * Unqualify a string qualified by a '.' dot character. For example,
     * "this.name.is.qualified", returns "qualified".
     *
     * @param qualifiedName the qualified name
     * @return an unqualified string by stripping all previous text before (and including) the last period character.
     */
    static String unqualify(String qualifiedName) {
        return GeneralString.unqualify(qualifiedName, '.');
    }

    /**
     * Unqualify a string qualified by a separator character. For example,
     * "this:name:is:qualified" returns "qualified" if using a ':' separator.
     *
     * @param qualifiedName the qualified name
     * @param separator     the separator
     * @return an unqualified string by stripping all previous text before and including the last {@code separator} character.
     */
    static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    /**
     * Determine the RFC 3066 compliant language tag,
     * as used for the HTTP "Accept-Language" header.
     *
     * @param locale the Locale to transform to a language tag
     * @return the RFC 3066 compliant language tag as String
     */
    static String formatLocale(Locale locale) {
        return locale.getLanguage() + (GeneralIF.isNoEmpty(locale.getCountry()) ? "-" + locale.getCountry() : "");
    }

    /**
     * 解析路径分段
     *
     * @param input 文件路径
     * @return 返回路径分段，{@code String[]}
     */
    static String[] parsePath(String input) {
        int[] fi = GeneralString.parsePathIndex(input);
        return new String[]{input.substring(0, fi[0]), input.substring(fi[0], fi[1]), input.substring(fi[1])};
    }

    /**
     * 解析文件名索引
     *
     * @param input 文件路径
     * @return 返回前后索引位置，{@code int{start,end}}
     */
    static int[] parsePathIndex(String input) {
        final int length = input.length();
        if (length == 0) return new int[]{0, 0};
        int d = -1, p = -1;
        for (int i = length - 1; i >= 0; i--) {
            char c = input.charAt(i);
            if (d == -1 && c == '.') d = i;
            else if (p == -1 && (c == '/' || c == '\\')) p = i;
        }
        return new int[]{p < 0 ? 0 : p + 1, d < 0 || d < p ? length : d};
    }

    /**
     * 找出指定字符的位置，并且不包含限定字符
     *
     * @param input      字符串
     * @param c          指定字符
     * @param reserve    是否反向
     * @param limitChars 限制分割字符
     * @return 返回
     */
    static int indexOf(String input, char c, boolean reserve, char... limitChars) {
        if (input == null) return -1;
        int l = reserve ? input.lastIndexOf(c) : input.indexOf(c);
        if (l < 0) return -1;
        if (limitChars.length == 0) return l;
        IntStream.Builder builder = IntStream.builder();
        for (char beforeSplitChar : limitChars) {
            int i = reserve ? input.lastIndexOf(c) : input.indexOf(c);
            if (i < 0) continue;
            builder.add(i);
        }
        IntStream intStream = builder.build();
        OptionalInt i = reserve ? intStream.max() : intStream.min();
        if (i.isPresent()) {
            return reserve ? (l > i.getAsInt() ? l : -1) : (l < i.getAsInt() ? l : -1);
        }
        return l;
    }

    /**
     * Imported from
     * https://github.com/apache/commons-lang/blob/690dc3c9c4cf8a1875d882ae09741c2e6342ad6b/src/main/java/org/apache/commons/lang3/StringUtils.java
     * <p>
     * Performs the logic for the {@code split} and
     * {@code splitPreserveAllTokens} methods that return a maximum array
     * length.
     *
     * @param str               the String to parse, may be {@code null}
     * @param separatorChars    the separate character
     * @param max               the maximum number of elements to include in the
     *                          array. A zero or negative value implies no limit.
     * @param preserveAllTokens if {@code true}, adjacent separators are
     *                          treated as empty token separators; if {@code false}, adjacent
     *                          separators are treated as one separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    static String[] splitWorker(String str, final String separatorChars, final int max, final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        //XXX: This never happens in Domain.parseDomain
        //if (str == null) {
        //    return null;
        //}
        final int len = str.length();
        //if (len == 0) {
        //    return EMPTY_STRING_ARRAY;
        //}

        final List<String> list = new ArrayList<>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[0]);
    }
}

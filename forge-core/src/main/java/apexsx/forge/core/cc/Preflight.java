/*
 * Copyright (c) 2025-2026.
 */

package apexsx.forge.core.cc;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.Supplier;

/**
 * 预检工具类 - 提供各种校验和检查功能
 * 包含空值检查、数组检查、字符串匹配、范围验证等实用方法
 *
 * @author Terraqo
 */
@SuppressWarnings("unused")
public final class Preflight {

    /**
     * 判断JDK版本
     *
     * @return 判断是否是1.4之前的JVM
     */
    public static boolean isMatchJVM(int integer, int decimal, boolean before, boolean includeEquals) {
        integer = Math.max(integer, 1);
        decimal = Math.max(decimal, 0);
        final String specVersion = System.getProperty("java.specification.version");
        int i = specVersion.indexOf('.');
        int specVersionInteger, specVersionDecimal = 0;
        if (i >= 0) {
            specVersionInteger = Integer.parseInt(specVersion.substring(0, i));
            specVersionDecimal = Integer.parseInt(specVersion.substring(i + 1));
        } else {
            specVersionInteger = Integer.parseInt(specVersion);
        }
        if (specVersionInteger < integer) return before;
        if (specVersionInteger > integer) return !before;
        if (specVersionDecimal < decimal) return before;
        if (specVersionDecimal > decimal) return !before;
        return includeEquals;
    }

    public static boolean isArray(Object input) {
        return input != null && input.getClass().isArray();
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isNoNull(Object input) {
        return input != null;
    }

    public static boolean isNoNull(Object input, Object other) {
        return input != null && other != null;
    }

    public static boolean isEmpty(Object input) {
        return input instanceof String ? ((String) input).isEmpty() : input == null;
    }

    public static boolean isEmpty(CharSequence input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(Collection<?> input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(Map<?, ?> input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(Object[] input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(boolean[] input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(char[] input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(byte[] input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(short[] input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(int[] input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(long[] input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(float[] input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isEmpty(double[] input) {
        return !Preflight.isNoEmpty(input);
    }

    public static boolean isNoEmpty(Object input) {
        return input instanceof String ? !((String) input).isEmpty() : input != null;
    }

    public static boolean isNoEmpty(CharSequence input) {
        return input != null && !input.isEmpty();
    }

    public static boolean isNoEmpty(Collection<?> input) {
        return input != null && !input.isEmpty();
    }

    public static boolean isNoEmpty(Map<?, ?> input) {
        return input != null && !input.isEmpty();
    }

    public static boolean isNoEmpty(Object[] input) {
        return input != null && input.length != 0;
    }

    public static boolean isNoEmpty(boolean[] input) {
        return input != null && input.length != 0;
    }

    public static boolean isNoEmpty(char[] input) {
        return input != null && input.length != 0;
    }

    public static boolean isNoEmpty(byte[] input) {
        return input != null && input.length != 0;
    }

    public static boolean isNoEmpty(short[] input) {
        return input != null && input.length != 0;
    }

    public static boolean isNoEmpty(int[] input) {
        return input != null && input.length != 0;
    }

    public static boolean isNoEmpty(long[] input) {
        return input != null && input.length != 0;
    }

    public static boolean isNoEmpty(float[] input) {
        return input != null && input.length != 0;
    }

    public static boolean isNoEmpty(double[] input) {
        return input != null && input.length != 0;
    }

    public static boolean isNoNullAll(Collection<?> input) {
        if (!Preflight.isNoEmpty(input)) return false;
        for (Object e : input) if (Preflight.isNull(e)) return false;
        return true;
    }

    public static boolean isNoNullAll(Object[] input) {
        if (!Preflight.isNoEmpty(input)) return false;
        for (Object e : input) if (Preflight.isNull(e)) return false;
        return true;
    }

    public static boolean isNoEmptyAll(Collection<? extends CharSequence> input) {
        if (!Preflight.isNoEmpty(input)) return false;
        for (CharSequence e : input) if (Preflight.isEmpty(e)) return false;
        return true;
    }

    public static boolean isNoEmptyAll(CharSequence[] input) {
        if (!Preflight.isNoEmpty(input)) return false;
        for (CharSequence e : input) if (Preflight.isEmpty(e)) return false;
        return true;
    }

    public static boolean isNatural(long input) {
        return Preflight.isNatural(input, Long.MAX_VALUE);
    }

    public static boolean isNatural(long input, long limit) {
        return Preflight.isInRange(input, 0, limit);
    }

    public static boolean isInRange(long input, long lo, long hi) {
        return input >= lo && input < hi;
    }

    public static boolean isOutRange(long input, long lo, long hi) {
        return input < lo || input >= hi;
    }

    /**
     * 是否为ASCII字符类型
     *
     * @param input 被检查的字符处
     * @param type  ascii类型
     *              <p>
     *              type: 0，是否为为空字符
     *              type: 1，是否为ASCII控制符（不可见字符），控制符位于0~31和127
     *              type: 2，是否为可见ASCII字符，可见字符位于32~126之间
     *              type: 3，数字字符
     *              type: 4，英文字符
     *              type: 5，数字或英文字符
     *              type: 6，大写英文字符
     *              type: 7，小写英文字符
     *              type: 8，数字或大写英文字符
     *              type: 9，数字或小写英文字符
     * @return 返回true符号要求，否则返回false
     */
    public static boolean isAsciiCharWithType(int input, int type) {
        return switch (type) {
            case 0 ->
                    Character.isWhitespace(input) || Character.isSpaceChar(input) || input == '\ufeff' || input == '\u202a';
            case 1 -> input < 32 || input == 127;
            case 2 -> input >= 32 && input < 127;
            case 3 -> Character.isDigit(input);
            case 4 -> Character.isLetter(input);
            case 5 -> Character.isLetterOrDigit(input);
            case 6 -> Character.isUpperCase(input);
            case 7 -> Character.isLowerCase(input);
            case 8 -> Character.isDigit(input) || Character.isUpperCase(input);
            case 9 -> Character.isDigit(input) || Character.isLowerCase(input);
            case 10 -> '/' == input || '\\' == input;
            default -> input < 128;
        };
    }

    public static boolean isNumberChar(int input, boolean hex) {
        return Preflight.isAsciiCharWithType(input, 3) || (hex && ((input >= 'a' && input <= 'f') || (input >= 'A' && input <= 'F')));
    }

    /**
     * 比较两个字符是否相同
     *
     * @param input      字符1
     * @param other      字符2
     * @param ignoreCase 是否忽略大小写
     * @return 是否相同
     */
    public static boolean isEqualsChar(boolean ignoreCase, char input, char other) {
        return input == other || (ignoreCase && Character.toLowerCase(input) == Character.toLowerCase(other));
    }

    public static boolean isCanContain(CharSequence input, CharSequence other) {
        return input != null && other != null && other.length() <= input.length() && !other.isEmpty();
    }

    public static boolean isContain(CharSequence input, CharSequence other) {
        return Preflight.isContain(input, other, true);
    }

    public static boolean isContain(CharSequence input, CharSequence other, boolean ignoreCase) {
        if (!Preflight.isCanContain(input, other)) return false;
        int inputLen = input.length(), subLen = other.length();
        int len = inputLen - subLen;
        for (int i = 0; i < len; i++) {
            if (Preflight.isMatchRegion(false, input, i, other, 0, subLen)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContainAll(CharSequence input, CharSequence... other) {
        return Preflight.isContainAll(true, input, other);
    }

    public static boolean isContainAll(boolean ignoreCase, CharSequence input, CharSequence... other) {
        if (other == null) return false;
        for (CharSequence value : other) {
            if (!Preflight.isContain(input, value, ignoreCase)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isContainAny(CharSequence input, CharSequence... other) {
        return Preflight.isContainAny(true, input, other);
    }

    public static boolean isContainAny(boolean ignoreCase, CharSequence input, CharSequence... other) {
        if (other == null) return false;
        for (CharSequence value : other) {
            if (Preflight.isContain(input, value, ignoreCase)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEquals(boolean ignoreCase, CharSequence input, CharSequence other) {
        if (input == other) return true;
        if (input == null || other == null) return false;
        return input.length() == other.length() && Preflight.isMatchRegion(ignoreCase, input, 0, other, 0, input.length());
    }

    /**
     * 测试两个字符串区域是否相等。
     *
     * @param ignoreCase  是否忽略大小写
     * @param input       输入字符串
     * @param inputOffset 输入字符串偏移量
     * @param other       匹配字符串
     * @param otherOffset 匹配字符串偏移
     * @param length      要比对字符串长度
     * @return 如果此字符串的指定子区域与字符串参数的指定子区域完全匹配返回true，否则返回false
     * @see String#regionMatches(boolean, int, String, int, int)
     */
    public static boolean isMatchRegion(boolean ignoreCase, CharSequence input, int inputOffset, CharSequence other, int otherOffset, int length) {
        if (input instanceof String && other instanceof String) {
            return ((String) input).regionMatches(ignoreCase, inputOffset, (String) other, otherOffset, length);
        }
        int i1 = inputOffset, i2 = otherOffset;
        int tmpLen = length;

        while (tmpLen-- > 0) {
            char c1 = input.charAt(i1++), c2 = other.charAt(i2++);
            if (c1 == c2) continue;
            if (!ignoreCase) return false;

            // The same check as in String.regionMatches():
            if (!Preflight.isEqualsChar(true, c1, c2)) return false;
        }

        return true;
    }

    /**
     * 比较字符数组和字符串是否相等
     *
     * @param isIgnoreCase 忽略字符串
     * @param input        字符数组
     * @param inputOffset  字符起始位置
     * @param other        字符串
     * @param otherOffset  字符串起始位置
     * @param length       长度
     * @return 返回是否相等
     */
    public static boolean isMatchRegion(boolean isIgnoreCase, char[] input, int inputOffset, char[] other, int otherOffset, int length) {
        if (inputOffset < 0 || otherOffset < 0) return false;
        if (inputOffset + length >= input.length) return false;
        if (otherOffset + length >= input.length) return false;
        for (int j = 0; j < length; j++) {
            if (Preflight.isEqualsChar(isIgnoreCase, input[inputOffset + j], other[otherOffset + j])) continue;
            return false;
        }
        return true;
    }

    /**
     * 检查字符串是否与指定的通配符匹配器匹配，
     * <p>
     * 通配符匹配器使用字符 '?' 和 '*' 来表示单个或多个（零个或多个）通配符。
     *
     * @param ignoreCase   是否区分大小写
     * @param input        输入字符串
     * @param patternMatch 匹配通配符模式
     * @return 如果匹配返回true，否则返回false
     */
    public static boolean isMatchWildcard(boolean ignoreCase, CharSequence input, CharSequence patternMatch) {
        if (input == null || patternMatch == null) return false;
        int inputLength = input.length(), patternLength = patternMatch.length();
        if (inputLength == 0 || patternLength == 0) return false;
        StringBuilder patternBuffer = new StringBuilder();
        int questionCount = 0, multiplyCount = 0;
        boolean preMultiply = false, hasNormal = false;
        char pc, ic;
        for (int i = 0; i < patternLength; i++) {
            pc = patternMatch.charAt(i);
            if (pc != '*') {
                patternBuffer.append(pc);
                preMultiply = false;
                if (pc != '?') hasNormal = true;
                else questionCount++;
            } else if (!preMultiply) {
                patternBuffer.append(pc);
                preMultiply = true;
                multiplyCount++;
            }
        }
        if (!hasNormal) {
            return questionCount == 0 || (multiplyCount > 0 ? inputLength >= questionCount : inputLength == questionCount);
        }
        patternLength = patternBuffer.length();
        if (questionCount == 0 && multiplyCount == 0) {
            if (inputLength != patternLength) return false;
            for (int i = 0; i < inputLength; i++) {
                if (Preflight.isEqualsChar(ignoreCase, input.charAt(i), patternBuffer.charAt(i))) continue;
                return false;
            }
            return true;
        } else if (multiplyCount == 0) {
            if (inputLength != patternLength) return false;
            for (int i = 0; i < inputLength; i++) {
                if ((pc = patternBuffer.charAt(i)) != '?' && !Preflight.isEqualsChar(ignoreCase, input.charAt(i), pc))
                    return false;
            }
            return true;
        } else {
            int si = 0, pi = 0, i;
            String[] ps = new String[multiplyCount + 1];
            if (patternBuffer.charAt(0) == '*') {
                ps[pi++] = "";
                si = 1;
            }
            for (i = pi; i < patternLength; i++) {
                if (patternBuffer.charAt(i) != '*') continue;
                ps[pi++] = patternBuffer.substring(si, i);
                si = i + 1;
            }
            if (si >= patternLength) ps[pi] = "";
            else ps[pi] = patternBuffer.substring(si);
            int[] pcs = new int[ps.length];
            pcs[pcs.length - 1] = ps[pcs.length - 1].length();
            for (i = pcs.length - 2; i >= 0; i--) pcs[i] = pcs[i + 1] + ps[i].length();
            if (inputLength < pcs[0]) return false;
            final int s = ps[0].isEmpty() ? 1 : 0, e = ps.length - (ps[ps.length - 1].isEmpty() ? 1 : 0);
            for (si = 0, i = s; i < e; ) {
                int ii = 0;
                for (pi = 0; pi < ps[i].length(); pi++) {
                    if (si + ii >= inputLength) return false;//模式未完全匹配，已到字符串末尾
                    ic = input.charAt(si + ii);
                    pc = ps[i].charAt(pi);
                    if (pc != '?' && !Preflight.isEqualsChar(ignoreCase, ic, pc)) break;
                    ii++;
                }
                if (pi < ps[i].length()) {//模式匹配失败
                    if (inputLength - si - 1 < pcs[i]) return false;//剩余字符串长度，不满足模式匹配长度
                    si++;
                } else {
                    si += ii;
                    i++;
                }
            }
            return e != ps.length || si == inputLength;
        }
    }

    /**
     * 是否有效类名
     *
     * @param className 类名
     * @return 如果有效返回true，否则返回false
     */
    public static boolean isAvailableClassName(String className) {
        try {
            Class.forName(className, false, ClassLoader.getSystemClassLoader());
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 判断 {@code targetType}是{@code inputType}或接口，
     *
     * @param inputType  要判断的类型，不允许为null
     * @param targetType 是否兼容父级类型，不允许为null
     * @return 如果兼容返回true，否则返回false
     */
    public static boolean isImplementsFrom(Class<?> inputType, Class<?> targetType) {
        return inputType != targetType && targetType.isAssignableFrom(inputType);
    }

    //------------------------------------------------------------------------------------------------------------------
    //检查函数
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 检查并获取unicode的单个Char，如果单个char无法表达则抛出异常
     *
     * @param input unicode
     * @return 返回字符
     */
    public static char checkGetSingletonChar(int input) {
        char[] chars = Character.toChars(input);
        if (chars.length != 1) {
            throw new IllegalArgumentException(
                    "Characters are generated incorrectly, unicode ["
                            + input
                            + "] is not in the range of char characters");
        }
        return chars[0];
    }

    public static void check(boolean success) {
        Preflight.checkAndReturn(success, null);
    }

    public static void check(boolean success, String messageTemplate, Object... args) {
        Preflight.checkAndReturn(success, null, messageTemplate, args);
    }

    public static void check(boolean success, Supplier<String> messageSupplier) {
        Preflight.checkAndReturn(success, null, messageSupplier);
    }

    public static <T> T checkAndReturn(boolean success, T input) {
        if (!success) {
            throw new IllegalArgumentException("The check result is error");
        }
        return input;
    }

    public static <T> T checkAndReturn(boolean success, T input, String messageTemplate, Object... args) {
        if (!success) {
            throw new IllegalArgumentException(String.format(messageTemplate, args));
        }
        return input;
    }

    public static <T> T checkAndReturn(boolean success, T input, Supplier<String> messageSupplier) {
        if (!success) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
        return input;
    }

    public static <T> T checkNoNull(T input) {
        return Preflight.checkAndReturn(Preflight.isNoNull(input), input, "The input value must be not null");
    }

    public static <T> T checkNoNull(T input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoNull(input), input, messageTemplate, args);
    }

    public static <T> T checkNoNull(T input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoNull(input), input, messageSupplier);
    }

    public static void checkNoNull(Object input, Object other) {
        Preflight.check(Preflight.isNoNull(input, other), null, "The input value must be not null");
    }

    public static void checkNoNull(Object input, Object other, String messageTemplate, Object... args) {
        Preflight.checkAndReturn(Preflight.isNoNull(input, other), null, messageTemplate, args);
    }

    public static void checkNoNull(Object input, Object other, Supplier<String> messageSupplier) {
        Preflight.checkAndReturn(Preflight.isNoNull(input, other), null, messageSupplier);
    }

    public static <T extends CharSequence> T checkNoEmpty(T input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static <T extends CharSequence> T checkNoEmpty(T input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static <T extends CharSequence> T checkNoEmpty(T input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static <E, T extends Collection<E>> T checkNoEmpty(T input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static <E, T extends Collection<E>> T checkNoEmpty(T input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static <E, T extends Collection<E>> T checkNoEmpty(T input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static <T> T[] checkNoEmpty(T[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static <T> T[] checkNoEmpty(T[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static <T> T[] checkNoEmpty(T[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static boolean[] checkNoEmpty(boolean[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static boolean[] checkNoEmpty(boolean[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static boolean[] checkNoEmpty(boolean[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static char[] checkNoEmpty(char[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static char[] checkNoEmpty(char[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static char[] checkNoEmpty(char[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static byte[] checkNoEmpty(byte[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static byte[] checkNoEmpty(byte[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static byte[] checkNoEmpty(byte[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static short[] checkNoEmpty(short[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static short[] checkNoEmpty(short[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static short[] checkNoEmpty(short[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static int[] checkNoEmpty(int[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static int[] checkNoEmpty(int[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static int[] checkNoEmpty(int[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static long[] checkNoEmpty(long[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static long[] checkNoEmpty(long[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static long[] checkNoEmpty(long[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static float[] checkNoEmpty(float[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static float[] checkNoEmpty(float[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static float[] checkNoEmpty(float[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static double[] checkNoEmpty(double[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, "The input value must be not empty");
    }

    public static double[] checkNoEmpty(double[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageTemplate, args);
    }

    public static double[] checkNoEmpty(double[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmpty(input), input, messageSupplier);
    }

    public static <E, T extends Collection<E>> T checkElementsNoNull(T input) {
        return Preflight.checkAndReturn(Preflight.isNoNullAll(input), input, "The input collection value must be not null");
    }

    public static <E, T extends Collection<E>> T checkElementsNoNull(T input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoNullAll(input), input, messageTemplate, args);
    }

    public static <E, T extends Collection<E>> T checkElementsNoNull(T input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoNullAll(input), input, messageSupplier);
    }

    public static <E> E[] checkElementsNoNull(E[] input) {
        return Preflight.checkAndReturn(Preflight.isNoNullAll(input), input, "The input array value must be not null");
    }

    public static <E> E[] checkElementsNoNull(E[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoNullAll(input), input, messageTemplate, args);
    }

    public static <E> E[] checkElementsNoNull(E[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoNullAll(input), input, messageSupplier);
    }

    public static <T extends Collection<? extends CharSequence>> T checkElementsNoEmpty(T input) {
        return Preflight.checkAndReturn(Preflight.isNoEmptyAll(input), input, "The input array value must be not empty");
    }

    public static <T extends Collection<? extends CharSequence>> T checkElementsNoEmpty(T input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmptyAll(input), input, messageTemplate, args);
    }

    public static <T extends Collection<? extends CharSequence>> T checkElementsNoEmpty(T input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmptyAll(input), input, messageSupplier);
    }

    public static <E extends CharSequence> E[] checkElementsNoEmpty(E[] input) {
        return Preflight.checkAndReturn(Preflight.isNoEmptyAll(input), input, "The input array value must be not empty");
    }

    public static <E extends CharSequence> E[] checkElementsNoEmpty(E[] input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNoEmptyAll(input), input, messageTemplate, args);
    }

    public static <E extends CharSequence> E[] checkElementsNoEmpty(E[] input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNoEmptyAll(input), input, messageSupplier);
    }

    public static void checkContain(String input, String ele) {
        Preflight.check(Preflight.isContain(input, ele), "The substring '%s' is not in the string '%s'", ele, input);
    }

    public static void checkContain(String input, String ele, String messageTemplate, Object... args) {
        Preflight.checkAndReturn(Preflight.isContain(input, ele), null, messageTemplate, args);
    }

    public static void checkContain(String input, String ele, Supplier<String> messageSupplier) {
        Preflight.checkAndReturn(Preflight.isContain(input, ele), null, messageSupplier);
    }

    public static <T extends CharSequence> T checkMatch(T input, IntPredicate charMatch) {
        return Preflight.checkMatch(input, charMatch, value -> "The '" + input + "' string contains invalid characters '" + ((char) value) + "'");
    }

    public static <T extends CharSequence> T checkMatch(T input, IntPredicate charMatch, IntFunction<String> messageSupplier) {
        int[] array = input.codePoints().toArray();
        for (int c : array) {
            if (!charMatch.test(c)) {
                throw new IllegalArgumentException(messageSupplier.apply(c));
            }
        }
        return input;
    }

    public static void checkElementIndex(int index, int size) {
        Preflight.check(index >= 0 && index < size, "The index must be 0<=index(%d)x<size(%d)", index, size);
    }

    public static void checkElementIndex(int start, int end, int size) {
        Preflight.check(start <= end && start >= 0 && end < size, "The index must be 0<=start(%d)<=end(%d)<size(%d)", start, end, size);

    }

    public static Duration checkNoNegative(Duration input) {
        return Preflight.checkAndReturn(input != null && !input.isNegative(), input, "The value '%s', but the duration cannot be negative", input);
    }

    public static long checkNoNegative(long input) {
        return Preflight.checkAndReturn(input >= 0, input, "The value '%s', but it must be no negative", input);
    }

    public static long checkNoNegative(long input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, messageTemplate, args);
    }

    public static long checkNoNegative(long input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, messageSupplier);
    }

    public static int checkNoNegative(int input) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, "The value '%s', but it must be no negative", input);
    }

    public static int checkNoNegative(int input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, messageTemplate, args);
    }

    public static int checkNoNegative(int input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, messageSupplier);
    }

    public static short checkNoNegative(short input) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, "The value '%s', but it must be no negative", input);
    }

    public static short checkNoNegative(short input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, messageTemplate, args);
    }

    public static short checkNoNegative(short input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, messageSupplier);
    }

    public static byte checkNoNegative(byte input) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, "The value '%s', but it must be no negative", input);
    }

    public static byte checkNoNegative(byte input, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, messageTemplate, args);
    }

    public static byte checkNoNegative(byte input, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNatural(input), input, messageSupplier);
    }

    public static long checkNatural(long input, long limit) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, "The value '%s', but it must be natural number with limit '%s'", input, limit);
    }

    public static long checkNatural(long input, long limit, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, messageTemplate, args);
    }

    public static long checkNatural(long input, long limit, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, messageSupplier);
    }

    public static int checkNatural(int input, int limit) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, "The value '%s', but it must be natural number with limit '%s'", input, limit);
    }

    public static int checkNatural(int input, int limit, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, messageTemplate, args);
    }

    public static int checkNatural(int input, int limit, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, messageSupplier);
    }

    public static short checkNatural(short input, short limit) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, "The value '%s', but it must be natural number with limit '%s'", input, limit);
    }

    public static short checkNatural(short input, short limit, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, messageTemplate, args);
    }

    public static short checkNatural(short input, short limit, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, messageSupplier);
    }

    public static byte checkNatural(byte input, byte limit) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, "The value '%s', but it must be natural number with limit '%s'", input, limit);
    }

    public static byte checkNatural(byte input, byte limit, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, messageTemplate, args);
    }

    public static byte checkNatural(byte input, byte limit, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isNatural(input, limit), input, messageSupplier);
    }

    public static long checkRange(long input, long lo, long hi) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, "The value '%s', but it must be '%s<=value<%s'", input, lo, hi);
    }

    public static long checkRange(long input, long lo, long hi, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, messageTemplate, args);
    }

    public static long checkRange(long input, long lo, long hi, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, messageSupplier);
    }

    public static int checkRange(int input, int lo, int hi) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, "The value '%s', but it must be '%s<=value<%s'", input, lo, hi);
    }

    public static int checkRange(int input, int lo, int hi, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, messageTemplate, args);
    }

    public static int checkRange(int input, int lo, int hi, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, messageSupplier);
    }

    public static short checkRange(short input, short lo, short hi) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, "The value '%s', but it must be '%s<=value<%s'", input, lo, hi);
    }

    public static short checkRange(short input, short lo, short hi, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, messageTemplate, args);
    }

    public static short checkRange(short input, short lo, short hi, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, messageSupplier);
    }

    public static byte checkRange(byte input, byte lo, byte hi) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, "The value '%s', but it must be '%s<=value<%s'", input, lo, hi);
    }

    public static byte checkRange(byte input, byte lo, byte hi, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, messageTemplate, args);
    }

    public static byte checkRange(byte input, byte lo, byte hi, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isInRange(input, lo, hi), input, messageSupplier);
    }

    public static long checkExcludeRange(long input, long lo, long hi) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, "The value '%s', but it must be 'value<%s' or 'value>=%s'", input, lo, hi);
    }

    public static long checkExcludeRange(long input, long lo, long hi, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, messageTemplate, args);
    }

    public static long checkExcludeRange(long input, long lo, long hi, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, messageSupplier);
    }

    public static int checkExcludeRange(int input, int lo, int hi) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, "The value '%s', but it must be 'value<%s' or 'value>=%s'", input, lo, hi);
    }

    public static int checkExcludeRange(int input, int lo, int hi, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, messageTemplate, args);
    }

    public static int checkExcludeRange(int input, int lo, int hi, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, messageSupplier);
    }

    public static short checkExcludeRange(short input, short lo, short hi) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, "The value '%s', but it must be 'value<%s' or 'value>=%s'", input, lo, hi);
    }

    public static short checkExcludeRange(short input, short lo, short hi, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, messageTemplate, args);
    }

    public static short checkExcludeRange(short input, short lo, short hi, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, messageSupplier);
    }

    public static byte checkExcludeRange(byte input, byte lo, byte hi) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, "The value '%s', but it must be 'value<%s' or 'value>=%s'", input, lo, hi);
    }

    public static byte checkExcludeRange(byte input, byte lo, byte hi, String messageTemplate, Object... args) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, messageTemplate, args);
    }

    public static byte checkExcludeRange(byte input, byte lo, byte hi, Supplier<String> messageSupplier) {
        return Preflight.checkAndReturn(Preflight.isOutRange(input, lo, hi), input, messageSupplier);
    }
}

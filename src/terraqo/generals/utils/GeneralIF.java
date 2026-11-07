/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Kit;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author potatoxf
 */
@SuppressWarnings("unused")
public interface GeneralIF {

    static <T> T nvl(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    static String nvlString(Object value, String defaultValue) {
        return value != null ? value.toString() : defaultValue;
    }

    /**
     * 判断JDK版本
     *
     * @return 判断是否是1.4之前的JVM
     */
    static boolean isMatchJVM(int integer, int decimal, boolean before, boolean includeEquals) {
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

    static boolean isArray(Object input) {
        return input != null && input.getClass().isArray();
    }

    static boolean isNull(Object object) {
        return object == null;
    }

    static boolean isNoNull(Object input) {
        return input != null;
    }

    static boolean isNoNull(Object input, Object other) {
        return input != null && other != null;
    }

    static boolean isEmpty(Object input) {
        return input instanceof String ? ((String) input).isEmpty() : input == null;
    }

    static boolean isEmpty(CharSequence input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(Collection<?> input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(Map<?, ?> input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(Object[] input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(boolean[] input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(char[] input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(byte[] input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(short[] input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(int[] input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(long[] input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(float[] input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isEmpty(double[] input) {
        return !GeneralIF.isNoEmpty(input);
    }

    static boolean isNoEmpty(Object input) {
        return input instanceof String ? !((String) input).isEmpty() : input != null;
    }

    static boolean isNoEmpty(CharSequence input) {
        return input != null && input.length() != 0;
    }

    static boolean isNoEmpty(Collection<?> input) {
        return input != null && !input.isEmpty();
    }

    static boolean isNoEmpty(Map<?, ?> input) {
        return input != null && !input.isEmpty();
    }

    static boolean isNoEmpty(Object[] input) {
        return input != null && input.length != 0;
    }

    static boolean isNoEmpty(boolean[] input) {
        return input != null && input.length != 0;
    }

    static boolean isNoEmpty(char[] input) {
        return input != null && input.length != 0;
    }

    static boolean isNoEmpty(byte[] input) {
        return input != null && input.length != 0;
    }

    static boolean isNoEmpty(short[] input) {
        return input != null && input.length != 0;
    }

    static boolean isNoEmpty(int[] input) {
        return input != null && input.length != 0;
    }

    static boolean isNoEmpty(long[] input) {
        return input != null && input.length != 0;
    }

    static boolean isNoEmpty(float[] input) {
        return input != null && input.length != 0;
    }

    static boolean isNoEmpty(double[] input) {
        return input != null && input.length != 0;
    }

    static boolean isElementsNoNull(Collection<?> input) {
        if (!GeneralIF.isNoEmpty(input)) return false;
        for (Object e : input) if (!GeneralIF.isNoNull(e)) return false;
        return true;
    }

    static boolean isElementsNoNull(Object[] input) {
        if (!GeneralIF.isNoEmpty(input)) return false;
        for (Object e : input) if (!GeneralIF.isNoNull(e)) return false;
        return true;
    }

    static boolean isElementsNoEmpty(Collection<? extends CharSequence> input) {
        if (!GeneralIF.isNoEmpty(input)) return false;
        for (CharSequence e : input) if (!GeneralIF.isNoEmpty(e)) return false;
        return true;
    }

    static boolean isElementsNoEmpty(CharSequence[] input) {
        if (!GeneralIF.isNoEmpty(input)) return false;
        for (CharSequence e : input) if (!GeneralIF.isNoEmpty(e)) return false;
        return true;
    }

    static boolean isNatural(long input) {
        return GeneralIF.isNatural(input, Long.MAX_VALUE);
    }

    static boolean isNatural(long input, long limit) {
        return GeneralIF.isInRange(input, 0, limit);
    }

    static boolean isInRange(long input, long lo, long hi) {
        return input >= lo && input < hi;
    }

    static boolean isOutRange(long input, long lo, long hi) {
        return input < lo || input >= hi;
    }

    /**
     * 是否为ASCII字符，ASCII字符位于0~127之间
     *
     * <pre>
     *   CharUtil.isAscii('a')  = true
     *   CharUtil.isAscii('A')  = true
     *   CharUtil.isAscii('3')  = true
     *   CharUtil.isAscii('-')  = true
     *   CharUtil.isAscii('\n') = true
     *   CharUtil.isAscii('&copy;') = false
     * </pre>
     *
     * @param input 被检查的字符处
     * @return true表示为ASCII字符，ASCII字符位于0~127之间
     */
    static boolean isAsciiChar(int input) {
        return input < 128;
    }

    /**
     * 是否为可见ASCII字符，可见字符位于32~126之间
     *
     * <pre>
     *   CharUtil.isAsciiPrintable('a')  = true
     *   CharUtil.isAsciiPrintable('A')  = true
     *   CharUtil.isAsciiPrintable('3')  = true
     *   CharUtil.isAsciiPrintable('-')  = true
     *   CharUtil.isAsciiPrintable('\n') = false
     *   CharUtil.isAsciiPrintable('&copy;') = false
     * </pre>
     *
     * @param input 被检查的字符处
     * @return true表示为ASCII可见字符，可见字符位于32~126之间
     */
    static boolean isAsciiPrintableChar(int input) {
        return input >= 32 && input < 127;
    }

    /**
     * 是否为ASCII控制符（不可见字符），控制符位于0~31和127
     *
     * <pre>
     *   CharUtil.isAsciiControl('a')  = false
     *   CharUtil.isAsciiControl('A')  = false
     *   CharUtil.isAsciiControl('3')  = false
     *   CharUtil.isAsciiControl('-')  = false
     *   CharUtil.isAsciiControl('\n') = true
     *   CharUtil.isAsciiControl('&copy;') = false
     * </pre>
     *
     * @param input 被检查的字符
     * @return true表示为控制符，控制符位于0~31和127
     */
    static boolean isAsciiControlChar(int input) {
        return input < 32 || input == 127;
    }

    static boolean isBlankChar(int input) {
        return Character.isWhitespace(input) || Character.isSpaceChar(input) || input == '\ufeff' || input == '\u202a';
    }

    static boolean isLetterChar(int input, Boolean upper) {
        return upper == null ? Character.isLetter(input) : (upper ? Character.isUpperCase(input) : Character.isLowerCase(input));
    }

    static boolean isNumberChar(int input, boolean hex) {
        return input >= '0' && input <= '9' || (hex && ((input >= 'a' && input <= 'f') || (input >= 'A' && input <= 'F')));
    }

    static boolean isLetterOrNumberChar(int input) {
        return GeneralIF.isLetterChar(input, null) || GeneralIF.isNumberChar(input, false);
    }

    static boolean isURLCodePoint(int input) {
        return GeneralIF.isLetterOrNumberChar(input) || "/=?!@$+_-.:*&~'()".indexOf(input) >= 0 ||
                (input >= 0x00A0 && input <= 0xD7FF) || (input >= 0xE000 && input <= 0xFDCF) ||
                (input >= 0xFDF0 && input <= 0xFFEF) || (input >= 0x10000 && input <= 0x1FFFD) ||
                (input >= 0x20000 && input <= 0x2FFFD) || (input >= 0x30000 && input <= 0x3FFFD) ||
                (input >= 0x40000 && input <= 0x4FFFD) || (input >= 0x50000 && input <= 0x5FFFD) ||
                (input >= 0x60000 && input <= 0x6FFFD) || (input >= 0x70000 && input <= 0x7FFFD) ||
                (input >= 0x80000 && input <= 0x8FFFD) || (input >= 0x90000 && input <= 0x9FFFD) ||
                (input >= 0xA0000 && input <= 0xAFFFD) || (input >= 0xB0000 && input <= 0xBFFFD) ||
                (input >= 0xC0000 && input <= 0xCFFFD) || (input >= 0xD0000 && input <= 0xDFFFD) ||
                (input >= 0xE0000 && input <= 0xEFFFD) || (input >= 0xF0000 && input <= 0xFFFFD) ||
                (input >= 0x100000 && input <= 0x10FFFD);
    }

    /**
     * 判断是否是中日韩文字
     */
    static boolean isChinese(int c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 判断是否为emoji表情符<br>
     *
     * @param input 字符
     * @return 是否为emoji
     */
    static boolean isEmojiChar(int input) {
        return !(input == 0x0
                || input == 0x9
                || input == 0xA
                || input == 0xD
                || input >= 0x20 && input <= 0xD7FF
                || input >= 0xE000 && input <= 0xFFFD);
    }

    /**
     * 是否是全角字符串
     *
     * @param codepoint unicode 码点
     * @return 如果是则返回 {@code true}，否则 {@code false}
     */
    static boolean isQuadEm(int codepoint) {
        boolean isEm;
        if (codepoint < 0x9FFF) {
            isEm =
                    (codepoint >= 0x1100 && codepoint <= 0x11FF)
                            || (codepoint >= 0x2600 && codepoint <= 0x27BF)
                            || (codepoint >= 0x2800 && codepoint <= 0x28FF)
                            || (codepoint >= 0x2E80 && codepoint <= 0x2FDF)
                            || (codepoint >= 0x2FF0 && codepoint <= 0x318F)
                            || (codepoint >= 0x31A0 && codepoint <= 0x4DB5)
                            || (codepoint >= 0x4DC0 && codepoint <= 0x9FBB);
        } else if (codepoint < 0xFAFF) {
            isEm =
                    (codepoint >= 0xA000 && codepoint <= 0xA4CF)
                            || (codepoint >= 0xAC00 && codepoint <= 0xD7AF)
                            || (codepoint >= 0xF900 && codepoint <= 0xFA2D)
                            || (codepoint >= 0xFA30 && codepoint <= 0xFA6A)
                            || (codepoint >= 0xFA70 && codepoint <= 0xFAD9);
        } else {
            isEm =
                    (codepoint >= 0xFE10 && codepoint <= 0xFE1F)
                            || (codepoint >= 0xFE30 && codepoint <= 0xFE4F)
                            || (codepoint >= 0xFF00 && codepoint <= 0xFFEF)
                            || (codepoint >= 0x1D300 && codepoint <= 0x1D35F)
                            || (codepoint >= 0x20000 && codepoint <= 0x2A6D6)
                            || (codepoint >= 0x2F800 && codepoint <= 0x2FA1D);
        }
        return isEm;
    }

    /**
     * 比较两个字符是否相同
     *
     * @param c1         字符1
     * @param c2         字符2
     * @param ignoreCase 是否忽略大小写
     * @return 是否相同
     */
    static boolean isEqualChar(char c1, char c2, boolean ignoreCase) {
        return c1 == c2 || (ignoreCase && Character.toLowerCase(c1) == Character.toLowerCase(c2));
    }

    /**
     * 比较字符数组和字符串是否相等
     *
     * @param chars        字符数组
     * @param csi          字符起始位置
     * @param cei          字符结束位置
     * @param isIgnoreCase 忽略字符串
     * @return 返回是否相等
     */
    static boolean isEqualChars(char[] chars, int csi, int cei, char[] other, boolean isIgnoreCase) {
        return GeneralIF.isEqualChars(chars, csi, cei, other, 0, other.length, isIgnoreCase);
    }

    /**
     * 比较字符数组和字符串是否相等
     *
     * @param chars        字符数组
     * @param csi          字符起始位置
     * @param cei          字符结束位置
     * @param other        字符串
     * @param osi          字符串起始位置
     * @param oei          字符串结束位置
     * @param isIgnoreCase 忽略字符串
     * @return 返回是否相等
     */
    static boolean isEqualChars(char[] chars, int csi, int cei, char[] other, int osi, int oei, boolean isIgnoreCase) {
        if (oei - osi != cei - csi) return false;
        int len = oei - osi;
        for (int j = 0; j < len; j++) {
            if (GeneralIF.isEqualChar(chars[csi + j], other[osi + j], isIgnoreCase)) continue;
            return false;
        }
        return true;
    }

    /**
     * 是否为Windows或者Linux（Unix）文件分隔符<br>
     * Windows平台下分隔符为\，Linux（Unix）为/
     *
     * @param input 字符
     * @return 是否为Windows或者Linux（Unix）文件分隔符
     */
    static boolean isPathSeparatorChar(int input) {
        return '/' == input || '\\' == input;
    }

    static boolean isHttpOrHttpsUrl(String string) {
        return Kit.P_HTTP_OR_HTTPS_URL.matcher(string).matches();
    }

    //全部打印true
    public static void main(String[] args) {
        System.out.println(isHttpOrHttpsUrl("http://localhost:8080?abcd=123&adfdsa=2212#abc"));
        System.out.println(isHttpOrHttpsUrl("http://localhost:8080?abcd=123"));
        System.out.println(isHttpOrHttpsUrl("http://localhost:8080"));
        System.out.println(isHttpOrHttpsUrl("http://localhost"));
        System.out.println(isHttpOrHttpsUrl("http://www.sina.com"));
        System.out.println(isHttpOrHttpsUrl("http://www.sina.com/abcd/adbc/?fileName=dasfj+@:;"));
    }

    static boolean isPeriphery(CharSequence input, CharSequence other) {
        return input != null && other != null && other.length() <= input.length() && other.length() != 0;
    }

    static boolean isContain(CharSequence input, CharSequence other) {
        return GeneralIF.isContain(input, other, true);
    }

    static boolean isContain(CharSequence input, CharSequence other, boolean ignoreCase) {
        if (!GeneralIF.isPeriphery(input, other)) return false;
        int inputLen = input.length(), subLen = other.length();
        int len = inputLen - subLen;
        for (int i = 0; i < len; i++) {
            if (GeneralIF.isMatchRegion(false, input, i, other, 0, subLen)) {
                return true;
            }
        }
        return false;
    }

    static boolean isContainAll(CharSequence input, CharSequence... other) {
        return GeneralIF.isContainAll(true, input, other);
    }

    static boolean isContainAll(boolean ignoreCase, CharSequence input, CharSequence... other) {
        if (other == null) return false;
        for (CharSequence value : other) {
            if (!GeneralIF.isContain(input, value, ignoreCase)) {
                return false;
            }
        }
        return true;
    }

    static boolean isContainAny(CharSequence input, CharSequence... other) {
        return GeneralIF.isContainAny(true, input, other);
    }

    static boolean isContainAny(boolean ignoreCase, CharSequence input, CharSequence... other) {
        if (other == null) return false;
        for (CharSequence value : other) {
            if (GeneralIF.isContain(input, value, ignoreCase)) {
                return true;
            }
        }
        return false;
    }

    static boolean isEquals(CharSequence input, CharSequence other) {
        if (input == other) {
            return true;
        }
        if (input == null || other == null) {
            return false;
        }
        if (input instanceof String && other instanceof String) {
            return input.equals(other);
        }
        return input.length() == other.length() && GeneralIF.isMatchRegion(false, input, 0, other, 0, input.length());
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
    static boolean isMatchRegion(boolean ignoreCase, CharSequence input, int inputOffset, CharSequence other, int otherOffset, int length) {
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
            if (!GeneralIF.isEqualChar(c1, c2, true)) return false;
        }

        return true;
    }

    /**
     * 检查字符串是否与指定的通配符匹配器匹配，
     * <p>
     * 通配符匹配器使用字符 '?' 和 '*' 来表示单个或多个（零个或多个）通配符。
     *
     * @param input        输入字符串
     * @param patternMatch 匹配通配符模式
     * @param ignoreCase   是否区分大小写
     * @return 如果匹配返回true，否则返回false
     */
    static boolean isMatchWildcard(CharSequence input, CharSequence patternMatch, boolean ignoreCase) {
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
                if (GeneralIF.isEqualChar(input.charAt(i), patternBuffer.charAt(i), ignoreCase)) continue;
                return false;
            }
            return true;
        } else if (multiplyCount == 0) {
            if (inputLength != patternLength) return false;
            for (int i = 0; i < inputLength; i++) {
                if ((pc = patternBuffer.charAt(i)) != '?' && !GeneralIF.isEqualChar(input.charAt(i), pc, ignoreCase))
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
                    if (pc != '?' && !GeneralIF.isEqualChar(ic, pc, ignoreCase)) break;
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
    static boolean isAvailableClassName(String className) {
        try {
            Class.forName(className, false, ClassLoader.getSystemClassLoader());
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * 判断{@code targetType}是否兼容{@code inputType}，
     * {@code targetType}是{@code inputType}或接口，
     * 或{@code targetType}是{@code inputType}或更大的基本类型
     *
     * @param inputType  要判断的类型，允许为null
     * @param targetType 是否兼容父级类型，不允许为null
     * @return 如果兼容返回true，否则返回false
     */
    static boolean isCompatibilityFrom(Class<?> inputType, Class<?> targetType) {
        if (targetType == null) throw new IllegalArgumentException("The match class must no null");
        if (inputType == targetType || inputType == null) return true;
        boolean inputArray = inputType.isArray();
        boolean matchArray = targetType.isArray();
        if (inputArray && matchArray) {
            return targetType.isAssignableFrom(inputType);
        } else if (inputArray || matchArray) {
            return false;
        } else {
            boolean targetPrimitive = targetType.isPrimitive();
            boolean inputPrimitive = inputType.isPrimitive();
            if (targetPrimitive) {
                int matchCompatibility = GeneralPrimitive.getLevel(targetType);
                int inputCompatibility = GeneralPrimitive.getLevel(inputType);
                if (inputCompatibility < 0) return false;
                if (inputCompatibility > 0 && matchCompatibility > 0) {
                    return matchCompatibility >= inputCompatibility;//匹配类型是原生类型的兼容性
                } else if (inputCompatibility == 0 && matchCompatibility == 0) {
                    if (inputPrimitive) return Objects.equals(targetType, inputType);
                    else if (inputType == Kit.T_WZ && targetType == Kit.T_Z) return true;
                    else return inputType == Kit.T_WC && targetType == Kit.T_C;
                }
                return false;
            } else if (inputPrimitive) {
                return targetType.isAssignableFrom(GeneralPrimitive.getPrimitiveType(inputType));
            } else {
                return targetType.isAssignableFrom(inputType);
            }
        }
    }

    /**
     * 比较判断{@code inputType}是否兼容{@code targetType}，
     * {@code targetType}是{@code inputType}或接口，
     * 或{@code targetType}是{@code inputType}或更大的基本类型
     *
     * @param inputCLasses 要判断的类型，允许内部元素为null
     * @param matchClasses 是否兼容父级类型，不允许内部元素为null
     * @return 如果兼容返回true，否则返回false
     */
    static boolean isCompatibilityFrom(Class<?>[] inputCLasses, Class<?>[] matchClasses) {
        if (matchClasses == inputCLasses) return true;
        if (null == matchClasses || null == inputCLasses) return false;
        if (matchClasses.length != inputCLasses.length) return false;
        for (int i = 0; i < matchClasses.length; i++) {
            if (!isCompatibilityFrom(inputCLasses[i], matchClasses[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断 {@code targetType}是{@code inputType}或接口，
     *
     * @param inputType  要判断的类型，不允许为null
     * @param targetType 是否兼容父级类型，不允许为null
     * @return 如果兼容返回true，否则返回false
     */
    static boolean isImplementsFrom(Class<?> inputType, Class<?> targetType) {
        return inputType != targetType && targetType.isAssignableFrom(inputType);
    }
}

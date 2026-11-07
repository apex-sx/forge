/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.Kit;
import com.github.potatoxf.generals.function.Function1Throw;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.*;

/**
 * 转换器工具类
 *
 * @author potatoxf
 */
public interface GeneralConverter {
    /**
     * 转字符集
     *
     * @param charset 字符集
     * @return 返回字符集
     */
    static Charset toCharset(Object charset) {
        return GeneralConverter.toCharset(charset, null);
    }

    /**
     * 转字符集
     *
     * @param charset 字符集
     * @return 返回字符集
     */
    static Charset toCharset(Object charset, Charset defaultCharset) {
        if (charset == null) return defaultCharset != null ? defaultCharset : Charset.defaultCharset();
        if (charset instanceof Charset) return (Charset) charset;
        if (charset instanceof String) {
            try {
                return Charset.forName(charset.toString());
            } catch (Throwable ignored) {
            }
        }
        return defaultCharset != null ? defaultCharset : Charset.defaultCharset();
    }

    /**
     * 将输入值转换成{@link String}类型
     *
     * @param value 输入值
     * @return 返回 {@link String}
     */
    static String toStr(Object value) {
        return GeneralConverter.toStr(value, null);
    }

    /**
     * 将输入值转换成{@link String}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link String}
     */
    static String toStr(Object value, String defaultValue) {
        return GeneralConverter.toStr(value, defaultValue, null);
    }

    /**
     * 将输入值转换成{@link String}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @param charset      字符集
     * @return 返回 {@link String}
     */
    static String toStr(Object value, String defaultValue, Object charset) {
        if (null == value) return defaultValue == null ? "" : defaultValue;
        if (value instanceof ByteBuffer) {
            return GeneralConverter.toCharset(charset).decode((ByteBuffer) value).toString();
        } else if (value instanceof byte[]) {
            return new String((byte[]) value, GeneralConverter.toCharset(charset));
        } else if (value instanceof char[]) {
            return new String((char[]) value);
        } else if (value instanceof String) {
            return (String) value;
        }
        return value.toString();
    }

    /**
     * 将输入值转换成{@link String}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link String}列表
     */
    static List<String> toStrListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toStr);
    }

    //------------------------------------------------------------------------------------------------------------------
    //全角半角转换
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 半角转全角
     *
     * @param input String.
     * @return 全角字符串.
     */
    static String toSBC(String input) {
        return toSBC(input, null);
    }

    /**
     * 半角转全角
     *
     * @param input         String
     * @param notConvertSet 不替换的字符集合
     * @return 全角字符串.
     */
    static String toSBC(String input, Set<Character> notConvertSet) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (null != notConvertSet && notConvertSet.contains(c[i])) {
                // 跳过不替换的字符
                continue;
            }

            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    /**
     * 全角转半角
     *
     * @param input String.
     * @return 半角字符串
     */
    static String toDBC(String input) {
        return toDBC(input, null);
    }

    /**
     * 替换全角为半角
     *
     * @param text          文本
     * @param notConvertSet 不替换的字符集合
     * @return 替换后的字符
     */
    static String toDBC(String text, Set<Character> notConvertSet) {
        char[] c = text.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (null != notConvertSet && notConvertSet.contains(c[i])) {
                // 跳过不替换的字符
                continue;
            }

            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    //------------------------------------------------------------------------------------------------------------------
    //Enum转换
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 转换为Enum对象<br>
     * 如果给定的值为空，或者转换失败，返回默认值<code>null</code><br>
     *
     * @param clazz Enum的Class
     * @param value 值
     * @return Enum
     */
    static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value) {
        return GeneralConverter.toEnum(clazz, value, null);
    }

    /**
     * 转换为Enum对象<br>
     * 如果给定的值为空，或者转换失败，返回默认值<br>
     *
     * @param clazz        Enum的Class
     * @param value        值
     * @param defaultValue 默认值
     * @return Enum
     */
    @SuppressWarnings("unchecked")
    static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue) {
        if (value == null) return defaultValue;
        if (clazz.isAssignableFrom(value.getClass())) {
            return (E) value;
        }
        final String v = GeneralConverter.toStr(value, null);
        if (GeneralIF.isEmpty(v)) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(clazz, v);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //boolean转换
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 将输入值转换成{@link boolean}类型
     *
     * @param value 输入值
     * @return 返回 {@link boolean}
     */
    static boolean toBoolean(Object value) {
        return GeneralConverter.toBoolean(value, null);
    }

    /**
     * 将输入值转换成{@link boolean}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link boolean}
     */
    static boolean toBoolean(Object value, Boolean defaultValue) {
        if (value == null) return defaultValue != null && defaultValue;
        if (value instanceof Boolean) return (boolean) value;
        if (value instanceof Number) return ((Number) value).intValue() != 0;
        if (value instanceof Character) return " 0fF\r\n\0".indexOf((char) value) < 0;
        String string = value.toString();
        int length = string.length();
        switch (length) {
            case 0: {
                return false;
            }
            case 1: {
                return !" 0fF\r\n\0错".contains((String) value);
            }
            case 2: {
                if ("失败".equalsIgnoreCase(string)) return false;
                if ("no".equalsIgnoreCase(string)) return false;
            }
            case 4: {
                if ("null".equalsIgnoreCase(string)) return false;
                if ("fail".equalsIgnoreCase(string)) return true;
                break;
            }
            case 5: {
                if ("false".equalsIgnoreCase(string)) return false;
                if ("error".equalsIgnoreCase(string)) return false;
                if (string.startsWith("操作失败")) return false;
                break;
            }
            case 9: {
                if ("undefined".equalsIgnoreCase(string)) return false;
                break;
            }
        }
        return true;
    }

    /**
     * 将输入值转换成{@link boolean}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link boolean}列表
     */
    static List<Boolean> toBooleanListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toBoolean);
    }

    //------------------------------------------------------------------------------------------------------------------
    //Character转换
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 将输入值转换成{@link Character}类型
     *
     * @param value 输入值
     * @return 返回 {@link Character}
     */
    static Character toCharacter(Object value) {
        return GeneralConverter.toCharacter(value, null);
    }

    /**
     * 将输入值转换成{@link Character}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link Character}
     */
    static Character toCharacter(Object value, Character defaultValue) {
        if (value == null) return defaultValue != null ? defaultValue : 0;
        if (value instanceof Character) return (Character) value;
        if (value instanceof Number) return Character.toChars(((Number) value).intValue())[0];
        if (value instanceof String) {
            int length = ((String) value).length();
            if (length >= 1) {
                return ((String) value).charAt(0);
            }
        }
        return defaultValue;
    }

    /**
     * 将输入值转换成{@link Character}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link Character}列表
     */
    static List<Character> toCharacterListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toCharacter);
    }

    //------------------------------------------------------------------------------------------------------------------
    //数字转换
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 将输入值转换成{@link Byte}类型
     *
     * @param value 输入值
     * @return 返回 {@link Byte}
     */
    static Byte toByte(Object value) {
        Number number = GeneralConverter.toNumber(value, null);
        return number == null ? null : number.byteValue();
    }

    /**
     * 将输入值转换成{@link byte}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link byte}
     */
    static byte toByte(Object value, Byte defaultValue) {
        return GeneralConverter.toNumber(value, defaultValue).byteValue();
    }

    /**
     * 将输入值转换成{@link byte}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link byte}列表
     */
    static List<Byte> toByteListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toByte);
    }

    /**
     * 将输入值转换成{@link Short}类型
     *
     * @param value 输入值
     * @return 返回 {@link Short}
     */
    static Short toShort(Object value) {
        Number number = GeneralConverter.toNumber(value, null);
        return number == null ? null : number.shortValue();
    }

    /**
     * 将输入值转换成{@link short}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link short}
     */
    static short toShort(Object value, Short defaultValue) {
        return GeneralConverter.toNumber(value, defaultValue).shortValue();
    }

    /**
     * 将输入值转换成{@link short}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link short}列表
     */
    static List<Short> toShortListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toShort);
    }

    /**
     * 将输入值转换成{@link Integer}类型
     *
     * @param value 输入值
     * @return 返回 {@link Integer}
     */
    static Integer toInt(Object value) {
        Number number = GeneralConverter.toNumber(value, null);
        return number == null ? null : number.intValue();
    }

    /**
     * 将输入值转换成{@link int}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link int}
     */
    static int toInt(Object value, Integer defaultValue) {
        return GeneralConverter.toNumber(value, defaultValue).intValue();
    }

    /**
     * 将输入值转换成{@link int}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link int}列表
     */
    static List<Integer> toIntListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toInt);
    }

    /**
     * 将输入值转换成{@link Long}类型
     *
     * @param value 输入值
     * @return 返回 {@link Long}
     */
    static Long toLong(Object value) {
        Number number = GeneralConverter.toNumber(value, null);
        return number == null ? null : number.longValue();
    }

    /**
     * 将输入值转换成{@link long}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link long}
     */
    static long toLong(Object value, Long defaultValue) {
        return GeneralConverter.toNumber(value, defaultValue).longValue();
    }

    /**
     * 将输入值转换成{@link long}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link long}列表
     */
    static List<Long> toLongListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toLong);
    }

    /**
     * 将输入值转换成{@link Float}类型
     *
     * @param value 输入值
     * @return 返回 {@link Float}
     */
    static Float toFloat(Object value) {
        Number number = GeneralConverter.toNumber(value, null);
        return number == null ? null : number.floatValue();
    }

    /**
     * 将输入值转换成{@link float}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link float}
     */
    static float toFloat(Object value, Float defaultValue) {
        return GeneralConverter.toNumber(value, defaultValue).floatValue();
    }

    /**
     * 将输入值转换成{@link float}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link float}列表
     */
    static List<Float> toFloatListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toFloat);
    }

    /**
     * 将输入值转换成{@link Double}类型
     *
     * @param value 输入值
     * @return 返回 {@link Double}
     */
    static Double toDouble(Object value) {
        Number number = GeneralConverter.toNumber(value, null);
        return number == null ? null : number.doubleValue();
    }

    /**
     * 将输入值转换成{@link double}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link double}
     */
    static double toDouble(Object value, Double defaultValue) {
        return GeneralConverter.toNumber(value, defaultValue).doubleValue();
    }

    /**
     * 将输入值转换成{@link double}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link double}列表
     */
    static List<Double> toDoubleListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toDouble);
    }

    /**
     * 将输入值转换成{@link BigInteger}类型
     *
     * @param value 输入值
     * @return 返回 {@link BigInteger}
     */
    static BigInteger toBigInteger(Object value) {
        return GeneralConverter.toBigInteger(value, null);
    }

    /**
     * 将输入值转换成{@link BigInteger}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link BigInteger}
     */
    static BigInteger toBigInteger(Object value, BigInteger defaultValue) {
        Number number = GeneralConverter.toNumber(value, defaultValue);
        if (number instanceof BigInteger) {
            return (BigInteger) number;
        } else {
            return new BigDecimal(number.toString()).toBigInteger();
        }
    }

    /**
     * 将输入值转换成{@link BigInteger}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link BigInteger}列表
     */
    static List<BigInteger> toBigIntegerListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toBigInteger);
    }

    /**
     * 将输入值转换成{@link BigDecimal}类型
     *
     * @param value 输入值
     * @return 返回 {@link BigDecimal}
     */
    static BigDecimal toBigDecimal(Object value) {
        return GeneralConverter.toBigDecimal(value, null);
    }

    /**
     * 将输入值转换成{@link BigDecimal}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link BigDecimal}
     */
    static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
        Number number = GeneralConverter.toNumber(value, defaultValue);
        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        } else {
            return new BigDecimal(number.toString());
        }
    }

    /**
     * 将输入值转换成{@link BigDecimal}列表
     *
     * @param value 输入值
     * @param delim 分隔符
     * @return 返回 {@link BigDecimal}列表
     */
    static List<BigDecimal> toBigDecimalListByDelim(Object value, String delim) {
        return GeneralConverter.toList(value, delim, GeneralConverter::toBigDecimal);
    }

    /**
     * 将输入值转换成{@link Number}类型
     *
     * @param value        输入值
     * @param defaultValue 默认值
     * @return 返回 {@link int}
     */
    static Number toNumber(Object value, Number defaultValue) {
        if (value == null) return defaultValue != null ? defaultValue : 0;
        if (value instanceof Boolean) return (boolean) value ? 1 : 0;
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof Character) {
            int input = (int) (char) value;
            if ('0' <= input && input <= '9') {
                return input - '0';
            } else if ('a' <= input && input <= 'z') {
                return input - 'a' + 10;
            } else if ('A' <= input && input <= 'Z') {
                return input - 'A' + 10;
            } else {
                throw new IllegalArgumentException("The [" + (char) input + "] is not numbers or letters");
            }
        } else if (value instanceof Date) {
            return ((Date) value).getTime();
        } else if (value instanceof Instant) {
            return ((Instant) value).toEpochMilli();
        } else if (value instanceof String) {
            String string = value.toString();
            int length = string.length();
            if (Kit.P_NUMBERS.matcher(string).matches()) {
                string = string.replace(",", "");
                int i = string.lastIndexOf('.');
                if (i == 0) {
                    return new BigDecimal('0' + string);
                } else if (i > 0) {
                    return new BigDecimal(string);
                } else {
                    return new BigInteger(string);
                }
            } else {
                return GeneralConverter.toBoolean(value, false) ? 1 : 0;
            }
        }
        return defaultValue;
    }

    /**
     * 将输入字符串逗号分割转列表
     *
     * @param input     输入字符串
     * @param converter 转换器
     * @param <T>       元素类型
     * @return 返回列表
     */
    static <T> List<T> toListByDelim(Object input, Function1Throw<String, T, Throwable> converter) {
        return GeneralConverter.toList(input, ",", converter);
    }

    /**
     * 将输入字符串转列表
     *
     * @param value     输入字符串
     * @param delim     分隔符
     * @param converter 转换器
     * @param <T>       元素类型
     * @return 返回列表
     */
    static <T> List<T> toList(Object value, String delim, Function1Throw<String, T, Throwable> converter) {
        List<T> list = new ArrayList<>();
        if (value != null) {
            if (delim == null) delim = ",";
            String str = GeneralConverter.toStr(value);
            StringTokenizer st = new StringTokenizer(str, delim, false);
            Set<T> set = new LinkedHashSet<>();
            while (st.hasMoreTokens()) {
                String string = st.nextToken();
                T t;
                try {
                    t = converter.apply(string);
                } catch (Throwable e) {
                    continue;
                }
                if (t != null) {
                    set.add(t);
                }
            }
            list.addAll(set);
        }
        return list;
    }
}

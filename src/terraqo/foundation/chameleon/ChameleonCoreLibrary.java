/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.chameleon;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.*;
import java.util.function.Predicate;

/**
 * Chameleon 核心转换库
 * <p>
 * 提供了各种基础数据类型之间的静态转换器实例。
 * 该类不可实例化，所有成员均为静态常量。
 * </p>
 *
 * @author Terraqo
 */
public final class ChameleonCoreLibrary {

    /**
     * 私有构造函数，防止实例化
     */
    private ChameleonCoreLibrary() {
        throw new UnsupportedOperationException("The class cannot be instantiated");
    }

    // ==================== Boolean 转其他类型 ====================

    /**
     * Boolean 转 Byte 转换器
     * <p>
     * 将布尔值转换为字节数值：
     * <ul>
     *     <li>true -> 1</li>
     *     <li>false -> 0</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Boolean, Byte, ?> BOOLEAN__TO__BYTE = new Chameleon<>(Boolean.class, Byte.class) {
        @Override
        protected Byte handleObject(Boolean source) {
            return source ? ChameleonConstantPool.BYTE_1 : ChameleonConstantPool.BYTE_0;
        }
    };

    /**
     * Boolean 转 Character (0/1) 转换器
     * <p>
     * 将布尔值转换为字符 '0' 或 '1'：
     * <ul>
     *     <li>true -> '1'</li>
     *     <li>false -> '0'</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Boolean, Character, ?> BOOLEAN__TO__CHARACTER_01 = new Chameleon<>(Boolean.class, Character.class, true) {
        @Override
        protected Character handleObject(Boolean source) {
            return source ? ChameleonConstantPool.CHAR_1 : ChameleonConstantPool.CHAR_0;
        }
    };

    /**
     * Boolean 转 Character (T/F) 转换器
     * <p>
     * 将布尔值转换为字符 'T' 或 'F'：
     * <ul>
     *     <li>true -> 'T'</li>
     *     <li>false -> 'F'</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Boolean, Character, ?> BOOLEAN__TO__CHARACTER_FT = new Chameleon<>(Boolean.class, Character.class, false) {
        @Override
        protected Character handleObject(Boolean source) {
            return source ? ChameleonConstantPool.CHAR_T : ChameleonConstantPool.CHAR_F;
        }
    };

    /**
     * Boolean 转 Double 转换器
     * <p>
     * 将布尔值转换为双精度浮点数：
     * <ul>
     *     <li>true -> 1.0</li>
     *     <li>false -> 0.0</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Boolean, Double, ?> BOOLEAN__TO__DOUBLE = new Chameleon<>(Boolean.class, Double.class) {
        @Override
        protected Double handleObject(Boolean source) {
            return source ? ChameleonConstantPool.DOUBLE_1 : ChameleonConstantPool.DOUBLE_0;
        }
    };

    /**
     * Boolean 转 Float 转换器
     * <p>
     * 将布尔值转换为单精度浮点数：
     * <ul>
     *     <li>true -> 1.0f</li>
     *     <li>false -> 0.0f</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Boolean, Float, ?> BOOLEAN__TO__FLOAT = new Chameleon<>(Boolean.class, Float.class) {
        @Override
        protected Float handleObject(Boolean source) {
            return source ? ChameleonConstantPool.FLOAT_1 : ChameleonConstantPool.FLOAT_0;
        }
    };

    /**
     * Boolean 转 Short 转换器
     * <p>
     * 将布尔值转换为短整型数值：
     * <ul>
     *     <li>true -> 1</li>
     *     <li>false -> 0</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Boolean, Short, ?> BOOLEAN__TO__SHORT = new Chameleon<>(Boolean.class, Short.class) {
        @Override
        protected Short handleObject(Boolean source) {
            return source ? ChameleonConstantPool.SHORT_1 : ChameleonConstantPool.SHORT_0;
        }
    };

    /**
     * Boolean 转 Integer 转换器
     * <p>
     * 将布尔值转换为整型数值：
     * <ul>
     *     <li>true -> 1</li>
     *     <li>false -> 0</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Boolean, Integer, ?> BOOLEAN__TO__INTEGER = new Chameleon<>(Boolean.class, Integer.class) {
        @Override
        protected Integer handleObject(Boolean source) {
            return source ? ChameleonConstantPool.INT_1 : ChameleonConstantPool.INT_0;
        }
    };

    /**
     * Boolean 转 Long 转换器
     * <p>
     * 将布尔值转换为长整型数值：
     * <ul>
     *     <li>true -> 1L</li>
     *     <li>false -> 0L</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Boolean, Long, ?> BOOLEAN__TO__LONG = new Chameleon<>(Boolean.class, Long.class) {
        @Override
        protected Long handleObject(Boolean source) {
            return source ? ChameleonConstantPool.LONG_1 : ChameleonConstantPool.LONG_0;
        }
    };

    // ==================== Character 转其他类型 ====================

    /**
     * Character 转 Boolean 转换器
     * <p>
     * 支持多种常见字符表示形式的布尔值解析：
     * <ul>
     *     <li>False: '0', 'F', '×', '对'</li>
     *     <li>True:  '1', 'T', '√', '错'</li>
     * </ul>
     * 如果字符不属于上述集合，则返回 null。
     * </p>
     */
    public static final Chameleon<Character, Boolean, ?> CHARACTER__TO__BOOLEAN = new Chameleon<>(Character.class, Boolean.class) {
        @Override
        protected Boolean handleObject(Character source) {
            switch (source) {
                case '0', 'F', '×', '对' -> {
                    return false;
                }
                case '1', 'T', '√', '错' -> {
                    return true;
                }
                default -> {
                    return null;
                }
            }
        }
    };

    /**
     * Character 转 Byte 转换器
     * <p>
     * 获取字符的 Unicode 码点值，并强制转换为 byte 类型。
     * 注意：如果码点超出 byte 范围 (-128 到 127)，会发生截断。
     * </p>
     */
    public static final Chameleon<Character, Byte, ?> CHARACTER__TO__BYTE = new Chameleon<>(Character.class, Byte.class) {
        @Override
        protected Byte handleObject(Character source) {
            return (byte) source.charValue();
        }
    };

    /**
     * Character 转 Short 转换器
     * <p>
     * 获取字符的 Unicode 码点值，并强制转换为 short 类型。
     * </p>
     */
    public static final Chameleon<Character, Short, ?> CHARACTER__TO__SHORT = new Chameleon<>(Character.class, Short.class) {
        @Override
        protected Short handleObject(Character source) {
            return (short) source.charValue();
        }
    };

    /**
     * Character 转 Integer 转换器
     * <p>
     * 获取字符的 Unicode 码点值，并转换为 int 类型。
     * </p>
     */
    public static final Chameleon<Character, Integer, ?> CHARACTER__TO__INTEGER = new Chameleon<>(Character.class, Integer.class) {
        @Override
        protected Integer handleObject(Character source) {
            return (int) source;
        }
    };

    /**
     * Character 转 Long 转换器
     * <p>
     * 获取字符的 Unicode 码点值，并转换为 long 类型。
     * </p>
     */
    public static final Chameleon<Character, Long, ?> CHARACTER__TO__LONG = new Chameleon<>(Character.class, Long.class) {
        @Override
        protected Long handleObject(Character source) {
            return (long) source;
        }
    };

    /**
     * Character 转 Float 转换器
     * <p>
     * 获取字符的 Unicode 码点值，并转换为 float 类型。
     * </p>
     */
    public static final Chameleon<Character, Float, ?> CHARACTER__TO__FLOAT = new Chameleon<>(Character.class, Float.class) {
        @Override
        protected Float handleObject(Character source) {
            return (float) source;
        }
    };

    /**
     * Character 转 Double 转换器
     * <p>
     * 获取字符的 Unicode 码点值，并转换为 double 类型。
     * </p>
     */
    public static final Chameleon<Character, Double, ?> CHARACTER__TO__DOUBLE = new Chameleon<>(Character.class, Double.class) {
        @Override
        protected Double handleObject(Character source) {
            return (double) source;
        }
    };

    // ==================== Number 转其他类型 ====================

    /**
     * 用于判断对象是否为 Number 类型的谓词
     */
    private static final Predicate<Object> IS_NUMBER = source -> source instanceof Number;

    /**
     * Number 转 Boolean 转换器
     * <p>
     * 根据数值判断布尔值：
     * <ul>
     *     <li>非零值 -> true</li>
     *     <li>零值 -> false</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Number, Boolean, ?> NUMBER__TO__BOOLEAN = new Chameleon<>(IS_NUMBER, Boolean.class) {
        @Override
        protected Boolean handleObject(Number source) {
            return source.longValue() != 0;
        }
    };

    /**
     * Number 转 Character 转换器
     * <p>
     * 将数值作为 Unicode 码点转换为对应的字符。
     * 如果数值对应的码点无法表示为单个 char（例如代理对或无效码点），则抛出 {@link ChameleonException}。
     * </p>
     */
    public static final Chameleon<Number, Character, ?> NUMBER__TO__CHARACTER = new Chameleon<>(IS_NUMBER, Character.class) {
        @Override
        protected Character handleObject(Number source) {
            char[] chars = Character.toChars(Math.toIntExact(source.longValue()));
            if (chars.length != 1) {
                throw new ChameleonException("cannot convert to char");
            }
            return chars[0];
        }
    };

    /**
     * Number 转 Byte 转换器
     * <p>
     * 将数值转换为 byte 类型。
     * 包含溢出检查：如果数值超出 byte 范围 (-128 到 127)，则抛出 {@link ChameleonException}。
     * </p>
     */
    public static final Chameleon<Number, Byte, ?> NUMBER__TO__BYTE = new Chameleon<>(IS_NUMBER, Byte.class) {
        @Override
        protected Byte handleObject(Number source) {
            long value = source.longValue();
            if ((byte) value != value) {
                throw new ChameleonException("byte overflow");
            }
            return (byte) value;
        }
    };

    /**
     * Number 转 Short 转换器
     * <p>
     * 将数值转换为 short 类型。
     * 包含溢出检查：如果数值超出 short 范围 (-32768 到 32767)，则抛出 {@link ChameleonException}。
     * </p>
     */
    public static final Chameleon<Number, Short, ?> NUMBER__TO__SHORT = new Chameleon<>(IS_NUMBER, Short.class) {
        @Override
        protected Short handleObject(Number source) {
            long value = source.longValue();
            if ((short) value != value) {
                throw new ChameleonException("short overflow");
            }
            return (short) value;
        }
    };

    /**
     * Number 转 Integer 转换器
     * <p>
     * 将数值转换为 int 类型。
     * 包含溢出检查：如果数值超出 int 范围，则抛出 {@link ChameleonException}。
     * </p>
     */
    public static final Chameleon<Number, Integer, ?> NUMBER__TO__INTEGER = new Chameleon<>(IS_NUMBER, Integer.class) {
        @Override
        protected Integer handleObject(Number source) {
            long value = source.longValue();
            if ((int) value != value) {
                throw new ChameleonException("integer overflow");
            }
            return (int) value;
        }
    };

    /**
     * Number 转 Long 转换器
     * <p>
     * 直接获取数值的 long 值。
     * 注意：对于浮点数，此操作会丢弃小数部分。
     * </p>
     */
    public static final Chameleon<Number, Long, ?> NUMBER__TO__LONG = new Chameleon<>(IS_NUMBER, Long.class) {
        @Override
        protected Long handleObject(Number source) {
            return source.longValue();
        }
    };

    /**
     * Number 转 Float 转换器
     * <p>
     * 直接获取数值的 float 值。
     * 注意：对于高精度数值可能存在精度损失。
     * </p>
     */
    public static final Chameleon<Number, Float, ?> NUMBER__TO__FLOAT = new Chameleon<>(IS_NUMBER, Float.class) {
        @Override
        protected Float handleObject(Number source) {
            return source.floatValue();
        }
    };

    /**
     * Number 转 Double 转换器
     * <p>
     * 直接获取数值的 double 值。
     * </p>
     */
    public static final Chameleon<Number, Double, ?> NUMBER__TO__DOUBLE = new Chameleon<>(IS_NUMBER, Double.class) {
        @Override
        protected Double handleObject(Number source) {
            return source.doubleValue();
        }
    };

    /**
     * Number 转 BigInteger 转换器
     * <p>
     * 智能处理各种 Number 子类以转换为 BigInteger：
     * <ul>
     *     <li>BigInteger: 直接返回原对象</li>
     *     <li>BigDecimal: 转换为 BigInteger (丢弃小数部分)</li>
     *     <li>Double/Float: 检查 NaN/Infinity 后先转 BigDecimal 再转 BigInteger</li>
     *     <li>其他整数类型: 使用 {@link BigInteger#valueOf(long)}</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Number, BigInteger, ?> NUMBER__TO__BIG_INTEGER = new Chameleon<>(IS_NUMBER, BigInteger.class) {
        @Override
        protected BigInteger handleObject(Number source) {
            switch (source) {
                case null -> {
                    return BigInteger.ZERO;
                }
                case BigInteger value -> {
                    return value;
                }
                case BigDecimal value -> {
                    return value.toBigInteger();
                }
                case Double value -> {
                    if (Double.isNaN(value) || Double.isInfinite(value)) {
                        throw new ChameleonException("Cannot convert NaN or Infinite Double to BigInteger");
                    }
                    return BigDecimal.valueOf(value).toBigInteger();
                }
                case Float value -> {
                    if (Float.isNaN(value) || Float.isInfinite(value)) {
                        throw new ChameleonException("Cannot convert NaN or Infinite Float to BigInteger");
                    }
                    return BigDecimal.valueOf(value).toBigInteger();
                }
                default -> {
                    return BigInteger.valueOf(source.longValue());
                }
            }
        }
    };

    /**
     * Number 转 BigDecimal 转换器
     * <p>
     * 智能处理各种 Number 子类以转换为 BigDecimal：
     * <ul>
     *     <li>BigDecimal: 直接返回原对象</li>
     *     <li>BigInteger: 构造新的 BigDecimal</li>
     *     <li>整数类型 (Integer, Long, Short, Byte):使用 {@link BigDecimal#valueOf(long)} 以保持精度</li>
     *     <li>浮点类型 (Double, Float): 检查 NaN/Infinity 后使用 {@link BigDecimal#valueOf(double)}</li>
     * </ul>
     * </p>
     */
    public static final Chameleon<Number, BigDecimal, ?> NUMBER__TO__BIG_DECIMAL = new Chameleon<>(IS_NUMBER, BigDecimal.class) {
        @Override
        protected BigDecimal handleObject(Number source) {
            if (source instanceof BigDecimal value) {
                return value;
            } else if (source instanceof BigInteger value) {
                return new BigDecimal(value);
            } else if (source instanceof Integer || source instanceof Long ||
                    source instanceof Short || source instanceof Byte) {
                return BigDecimal.valueOf(source.longValue());
            } else {
                // 处理Double/Float及其他类型
                double value = source.doubleValue();
                // 检查特殊值
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    throw new ChameleonException("Cannot convert NaN or Infinite to BigDecimal");
                }
                return BigDecimal.valueOf(value);
            }
        }
    };

    // ==================== String 转其他类型 ====================

    /**
     * String 转 Boolean 转换器
     * <p>
     * 使用 {@link Boolean#parseBoolean(String)} 进行解析。
     * 仅当字符串忽略大小写等于 "true" 时返回 true，其他情况均返回 false。
     * </p>
     */
    public static final Chameleon<String, Boolean, ?> STRING__TO__BOOLEAN = new Chameleon<>(String.class, Boolean.class) {
        @Override
        protected Boolean handleString(String source) throws Throwable {
            return Boolean.parseBoolean(source);
        }
    };

    /**
     * String 转 Character 转换器
     * <p>
     * 将长度为 1 的字符串转换为字符。
     * 如果字符串长度不为 1，则抛出 {@link ChameleonException}。
     * </p>
     */
    public static final Chameleon<String, Character, ?> STRING__TO__CHARACTER = new Chameleon<>(String.class, Character.class) {
        @Override
        protected Character handleString(String source) throws Throwable {
            if (source.length() != 1) {
                throw new ChameleonException("String length must be 1");
            }
            return source.charAt(0);
        }
    };

    /**
     * String 转 Byte 转换器
     * <p>
     * 解析字符串为 byte 类型。
     * 使用常量池缓存解析结果以提高重复转换的性能。
     * </p>
     */
    public static final Chameleon<String, Byte, ?> STRING__TO__BYTE = new Chameleon<>(String.class, Byte.class) {
        @Override
        protected Byte handleString(String source) throws Throwable {
            return ChameleonConstantPool.BYTE.computeIfAbsent(source, Byte::parseByte);
        }
    };

    /**
     * String 转 Short 转换器
     * <p>
     * 解析字符串为 short 类型。
     * 使用常量池缓存解析结果以提高重复转换的性能。
     * </p>
     */
    public static final Chameleon<String, Short, ?> STRING__TO__SHORT = new Chameleon<>(String.class, Short.class) {
        @Override
        protected Short handleString(String source) throws Throwable {
            return ChameleonConstantPool.SHORT.computeIfAbsent(source, Short::parseShort);
        }
    };

    /**
     * String 转 Integer 转换器
     * <p>
     * 解析字符串为 int 类型。
     * 使用常量池缓存解析结果以提高重复转换的性能。
     * </p>
     */
    public static final Chameleon<String, Integer, ?> STRING__TO__INTEGER = new Chameleon<>(String.class, Integer.class) {
        @Override
        protected Integer handleString(String source) throws Throwable {
            return ChameleonConstantPool.INT.computeIfAbsent(source, Integer::parseInt);
        }
    };

    /**
     * String 转 Long 转换器
     * <p>
     * 解析字符串为 long 类型。
     * 使用常量池缓存解析结果以提高重复转换的性能。
     * </p>
     */
    public static final Chameleon<String, Long, ?> STRING__TO__LONG = new Chameleon<>(String.class, Long.class) {
        @Override
        protected Long handleString(String source) throws Throwable {
            return ChameleonConstantPool.LONG.computeIfAbsent(source, Long::parseLong);
        }
    };

    /**
     * String 转 Float 转换器
     * <p>
     * 解析字符串为 float 类型。
     * 使用常量池缓存解析结果以提高重复转换的性能。
     * </p>
     */
    public static final Chameleon<String, Float, ?> STRING__TO__FLOAT = new Chameleon<>(String.class, Float.class) {
        @Override
        protected Float handleString(String source) throws Throwable {
            return ChameleonConstantPool.FLOAT.computeIfAbsent(source, Float::parseFloat);
        }
    };

    /**
     * String 转 Double 转换器
     * <p>
     * 解析字符串为 double 类型。
     * 使用常量池缓存解析结果以提高重复转换的性能。
     * </p>
     */
    public static final Chameleon<String, Double, ?> STRING__TO__DOUBLE = new Chameleon<>(String.class, Double.class) {
        @Override
        protected Double handleString(String source) throws Throwable {
            return ChameleonConstantPool.DOUBLE.computeIfAbsent(source, Double::parseDouble);
        }
    };

    /**
     * String 转 BigInteger 转换器
     * <p>
     * 解析字符串为 BigInteger。
     * 使用常量池缓存解析结果以提高重复转换的性能。
     * </p>
     */
    public static final Chameleon<String, BigInteger, ?> STRING__TO__BIG_INTEGER = new Chameleon<>(String.class, BigInteger.class) {
        @Override
        protected BigInteger handleString(String source) throws Throwable {
            return ChameleonConstantPool.BIG_INTEGER.computeIfAbsent(source, BigInteger::new);
        }
    };

    /**
     * String 转 BigDecimal 转换器
     * <p>
     * 解析字符串为 BigDecimal。
     * 使用常量池缓存解析结果以提高重复转换的性能。
     * </p>
     */
    public static final Chameleon<String, BigDecimal, ?> STRING__TO__BIG_DECIMAL = new Chameleon<>(String.class, BigDecimal.class) {
        @Override
        protected BigDecimal handleString(String source) throws Throwable {
            return ChameleonConstantPool.BIG_DECIMAL.computeIfAbsent(source, BigDecimal::new);
        }
    };

    /**
     * String 转 Number 通用转换器
     * <p>
     * 使用内部类 {@link TextToNumber} 进行智能解析。
     * 支持千分位分隔符、科学计数法格式。
     * 根据输入内容自动决定返回 BigDecimal 或 BigInteger。
     * </p>
     */
    public static final Chameleon<String, Number, ?> STRING__TO__NUMBER = new TextToNumber<>(String.class, null, null);

    // ==================== CharSequence 转其他类型 ====================
    // 这些转换器委托给对应的 String 转换器处理，以复用逻辑和缓存

    /**
     * 用于判断对象是否为 CharSequence 类型的谓词
     */
    private static final Predicate<Object> IS_CHAR_SEQUENCE = source -> source instanceof CharSequence;

    /**
     * CharSequence 转 Boolean 转换器
     * <p>
     * 委托给 {@link #STRING__TO__BOOLEAN} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, Boolean, ?> CHAR_SEQUENCE_TO__BOOLEAN = new Chameleon<>(IS_CHAR_SEQUENCE, Boolean.class) {
        @Override
        protected Boolean handleString(String source) throws Throwable {
            return STRING__TO__BOOLEAN.handleString(source);
        }
    };

    /**
     * CharSequence 转 Character 转换器
     * <p>
     * 委托给 {@link #STRING__TO__CHARACTER} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, Character, ?> CHAR_SEQUENCE_TO__CHARACTER = new Chameleon<>(IS_CHAR_SEQUENCE, Character.class) {
        @Override
        protected Character handleString(String source) throws Throwable {
            return STRING__TO__CHARACTER.handleString(source);
        }
    };

    /**
     * CharSequence 转 Byte 转换器
     * <p>
     * 委托给 {@link #STRING__TO__BYTE} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, Byte, ?> CHAR_SEQUENCE_TO__BYTE = new Chameleon<>(IS_CHAR_SEQUENCE, Byte.class) {
        @Override
        protected Byte handleString(String source) throws Throwable {
            return STRING__TO__BYTE.handleString(source);
        }
    };

    /**
     * CharSequence 转 Short 转换器
     * <p>
     * 委托给 {@link #STRING__TO__SHORT} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, Short, ?> CHAR_SEQUENCE_TO__SHORT = new Chameleon<>(IS_CHAR_SEQUENCE, Short.class) {
        @Override
        protected Short handleString(String source) throws Throwable {
            return STRING__TO__SHORT.handleString(source);
        }
    };

    /**
     * CharSequence 转 Integer 转换器
     * <p>
     * 委托给 {@link #STRING__TO__INTEGER} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, Integer, ?> CHAR_SEQUENCE_TO__INTEGER = new Chameleon<>(IS_CHAR_SEQUENCE, Integer.class) {
        @Override
        protected Integer handleString(String source) throws Throwable {
            return STRING__TO__INTEGER.handleString(source);
        }
    };

    /**
     * CharSequence 转 Long 转换器
     * <p>
     * 委托给 {@link #STRING__TO__LONG} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, Long, ?> CHAR_SEQUENCE_TO__LONG = new Chameleon<>(IS_CHAR_SEQUENCE, Long.class) {
        @Override
        protected Long handleString(String source) throws Throwable {
            return STRING__TO__LONG.handleString(source);
        }
    };

    /**
     * CharSequence 转 Float 转换器
     * <p>
     * 委托给 {@link #STRING__TO__FLOAT} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, Float, ?> CHAR_SEQUENCE_TO__FLOAT = new Chameleon<>(IS_CHAR_SEQUENCE, Float.class) {
        @Override
        protected Float handleString(String source) throws Throwable {
            return STRING__TO__FLOAT.handleString(source);
        }
    };

    /**
     * CharSequence 转 Double 转换器
     * <p>
     * 委托给 {@link #STRING__TO__DOUBLE} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, Double, ?> CHAR_SEQUENCE_TO__DOUBLE = new Chameleon<>(IS_CHAR_SEQUENCE, Double.class) {
        @Override
        protected Double handleString(String source) throws Throwable {
            return STRING__TO__DOUBLE.handleString(source);
        }
    };

    /**
     * CharSequence 转 BigInteger 转换器
     * <p>
     * 委托给 {@link #STRING__TO__BIG_INTEGER} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, BigInteger, ?> CHAR_SEQUENCE_TO__BIG_INTEGER = new Chameleon<>(IS_CHAR_SEQUENCE, BigInteger.class) {
        @Override
        protected BigInteger handleString(String source) throws Throwable {
            return STRING__TO__BIG_INTEGER.handleString(source);
        }
    };

    /**
     * CharSequence 转 BigDecimal 转换器
     * <p>
     * 委托给 {@link #STRING__TO__BIG_DECIMAL} 处理。
     * </p>
     */
    public static final Chameleon<CharSequence, BigDecimal, ?> CHAR_SEQUENCE_TO__BIG_DECIMAL = new Chameleon<>(IS_CHAR_SEQUENCE, BigDecimal.class) {
        @Override
        protected BigDecimal handleString(String source) throws Throwable {
            return STRING__TO__BIG_DECIMAL.handleString(source);
        }
    };

    /**
     * CharSequence 转 Number 通用转换器
     * <p>
     * 使用 {@link TextToNumber} 进行智能解析，支持千分位、科学计数等格式。
     * </p>
     */
    public static final Chameleon<CharSequence, Number, ?> CHAR_SEQUENCE_TO__NUMBER = new TextToNumber<>(null, null, null);

    /**
     * 字符串到 Number 类型的高级转换器
     * <p>
     * 用于将字符串转换为 Number 类型（BigDecimal 或 BigInteger）。
     * 支持以下特性：
     * <ul>
     *     <li>自定义字符映射（例如全角数字转半角）</li>
     *     <li>千分位分隔符处理</li>
     *     <li>科学计数法处理</li>
     *     <li>正负号处理</li>
     * </ul>
     * 如果字符串格式无法解析为有效数字，则返回 null。
     * </p>
     *
     * @param <SourceType> 源类型，必须是 CharSequence 的子类
     * @author Terraqo
     */
    public static class TextToNumber<SourceType extends CharSequence> extends Chameleon<SourceType, Number, Object> {

        /**
         * 字符映射表，Key 为源字符代码点，Value 为目标字符代码点
         */
        private final Map<Integer, Integer> characterMapping;

        /**
         * 构造函数
         *
         * @param sourceType 源类型 Class
         * @param source     源字符序列，用于定义字符映射规则
         * @param target     目标字符序列，用于定义字符映射规则
         *                   如果 source 或 target 为 null，则不进行字符映射
         */
        public TextToNumber(Class<SourceType> sourceType, CharSequence source, CharSequence target) {
            super(sourceType, Number.class);
            if (source == null || target == null) {
                this.characterMapping = Collections.emptyMap();
            } else {
                int sourceLength = source.length(), targetLength = target.length();
                if (sourceLength == 0) {
                    this.characterMapping = Collections.emptyMap();
                } else {
                    Map<Integer, Integer> map = new HashMap<>();
                    // 遍历源和目标字符序列，建立代码点映射
                    for (int i = 0, iv, j = 0, jv; i < sourceLength && j < targetLength; i += Character.charCount(iv), j += Character.charCount(jv)) {
                        iv = Character.codePointAt(source, i);
                        jv = Character.codePointAt(target, j);
                        if (map.containsKey(iv)) {
                            throw new IllegalArgumentException("Duplicate mapping for character: " + iv);
                        }
                        map.put(iv, jv);
                    }
                    this.characterMapping = map;
                }
            }
        }

        /**
         * 执行字符串到 Number 的核心转换逻辑
         *
         * @param source 经过修剪后的原始字符串
         * @return 转换后的 Number 对象 (BigDecimal 或 BigInteger)，如果格式无效则返回 null
         * @throws Throwable 转换过程中的异常
         */
        @Override
        protected Number handleString(String source) throws Throwable {
            // 1. 应用字符映射
            int[] codePoints = source.codePoints()
                    .map(cp -> characterMapping.getOrDefault(cp, cp))
                    .toArray();
            int len = codePoints.length;
            StringBuilder buffer = new StringBuilder(len + 8);
            int index = 0;

            // 2. 处理正负号
            if (len > 0) {
                int ch = codePoints[index];
                if (ch == '+' || ch == '-') {
                    buffer.appendCodePoint(ch);
                    index++;
                    if (index >= len) {
                        // 只有符号没有数字，视为 0
                        return BigInteger.ZERO;
                    }
                }
            }

            boolean hasDigit = false;
            int dotIndex = -1;

            // 预查找小数点位置，用于最终决定返回类型
            for (int i = 0; i < len; i++) {
                if (codePoints[i] == '.') {
                    dotIndex = i;
                    break;
                }
            }

            // 3. 处理整数部分（支持千分位逗号）
            while (index < len) {
                int ch = codePoints[index];
                if (ch == ',') {
                    index++; // 跳过千分位分隔符
                } else if (ch == '.') {
                    break; // 遇到小数点，进入小数处理阶段
                } else if (Character.isDigit(ch)) {
                    buffer.appendCodePoint(ch);
                    hasDigit = true;
                    index++;
                } else {
                    break; // 遇到非数字非逗号非小数点，停止解析整数部分
                }
            }

            // 4. 处理小数部分
            if (index < len && codePoints[index] == '.') {
                if (!hasDigit) {
                    buffer.append('0'); // 处理 ".5" 这种情况
                }
                buffer.append('.');
                hasDigit = true;
                index++;

                boolean decimalHasDigit = false;
                while (index < len && Character.isDigit(codePoints[index])) {
                    buffer.appendCodePoint(codePoints[index]);
                    decimalHasDigit = true;
                    index++;
                }
                if (!decimalHasDigit) {
                    buffer.append('0'); // 处理 "5." 这种情况
                }
            }

            // 如果没有任何数字，返回 null
            if (!hasDigit) {
                return null;
            }

            // 5. 处理指数部分 (e/E)
            if (index < len && (codePoints[index] == 'e' || codePoints[index] == 'E')) {
                buffer.append('E');
                index++;
                if (index >= len) {
                    buffer.setLength(buffer.length() - 1); // 回退 E，因为后面没有指数内容
                } else {
                    int ch = codePoints[index];
                    if (ch == '+' || ch == '-') {
                        buffer.appendCodePoint(ch);
                        index++;
                    } else {
                        buffer.append('+'); // 默认正指数
                    }

                    int expStart = buffer.length();
                    while (index < len && Character.isDigit(codePoints[index])) {
                        buffer.appendCodePoint(codePoints[index]);
                        index++;
                    }

                    // 如果指数部分没有数字，去掉刚才添加的符号
                    if (buffer.length() == expStart) {
                        buffer.setLength(buffer.length() - 1);
                    }
                }
            }

            // 6. 检查是否所有字符都已处理
            if (index != len) {
                return null; // 存在无法识别的尾部字符
            }

            // 7. 尝试构建 Number 对象
            try {
                String resultStr = buffer.toString();
                // 根据原始字符串中是否存在小数点来决定返回 BigDecimal 还是 BigInteger
                // 注意：这里使用 dotIndex 判断，即使处理后变成了 "5.0"，如果原串有小数点也优先用 BigDecimal
                return dotIndex >= 0 ? new BigDecimal(resultStr) : new BigInteger(resultStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    // ==================== Date/Time 转其他类型 ====================

    /**
     * 用于判断对象是否为 java.util.Date 类型的谓词
     */
    private static final Predicate<Object> IS_DATE = source -> source instanceof Date;

    /**
     * 系统默认时区 ID
     */
    private static final ZoneId SYSTEM_DEFAULT_ZONE_ID = ZoneId.systemDefault();

    /**
     * 系统默认时区偏移量
     */
    private static final ZoneOffset SYSTEM_DEFAULT_ZONE_OFFSET = SYSTEM_DEFAULT_ZONE_ID.getRules().getOffset(Instant.now());

    /**
     * Date 转 Long (毫秒时间戳) 转换器
     */
    public static final Chameleon<Date, Long, ?> DATE__TO__LONG = new Chameleon<>(IS_DATE, Long.class) {
        @Override
        protected Long handleObject(Date source) {
            return source.getTime();
        }
    };

    /**
     * Date 转 Instant 转换器
     */
    public static final Chameleon<Date, Instant, ?> DATE__TO__INSTANT = new Chameleon<>(IS_DATE, Instant.class) {
        @Override
        protected Instant handleObject(Date source) {
            return source.toInstant();
        }
    };

    /**
     * Date 转 LocalDateTime 转换器
     * <p>
     * 使用系统默认时区进行转换。
     * </p>
     */
    public static final Chameleon<Date, LocalDateTime, ZoneId> DATE__TO__LOCAL_DATE_TIME = new Chameleon<>(SYSTEM_DEFAULT_ZONE_ID, IS_DATE, LocalDateTime.class) {
        @Override
        protected LocalDateTime handleObject(Date source) {
            return LocalDateTime.ofInstant(source.toInstant(), getContextParameter());
        }
    };

    /**
     * Date 转 OffsetDateTime 转换器
     * <p>
     * 使用系统默认时区进行转换。
     * </p>
     */
    public static final Chameleon<Date, OffsetDateTime, ZoneId> DATE__TO__OFFSET_DATE_TIME = new Chameleon<>(SYSTEM_DEFAULT_ZONE_ID, IS_DATE, OffsetDateTime.class) {
        @Override
        protected OffsetDateTime handleObject(Date source) {
            return OffsetDateTime.ofInstant(source.toInstant(), getContextParameter());
        }
    };

    /**
     * Date 转 ZonedDateTime 转换器
     * <p>
     * 使用系统默认时区进行转换。
     * </p>
     */
    public static final Chameleon<Date, ZonedDateTime, ZoneId> DATE__TO__ZONED_DATE_TIME = new Chameleon<>(SYSTEM_DEFAULT_ZONE_ID, IS_DATE, ZonedDateTime.class) {
        @Override
        protected ZonedDateTime handleObject(Date source) {
            return ZonedDateTime.ofInstant(source.toInstant(), getContextParameter());
        }
    };

    /**
     * Instant 转 Long (毫秒时间戳) 转换器
     */
    public static final Chameleon<Instant, Long, ?> INSTANT__TO__LONG = new Chameleon<>(Instant.class, Long.class) {
        @Override
        protected Long handleObject(Instant source) {
            return source.toEpochMilli();
        }
    };

    /**
     * Instant 转 Date 转换器
     */
    public static final Chameleon<Instant, Date, ?> INSTANT__TO__DATE = new Chameleon<>(Instant.class, Date.class) {
        @Override
        protected Date handleObject(Instant source) {
            return Date.from(source);
        }
    };

    /**
     * Instant 转 LocalDateTime 转换器
     * <p>
     * 使用系统默认时区进行转换。
     * </p>
     */
    public static final Chameleon<Instant, LocalDateTime, ZoneId> INSTANT__TO__LOCAL_DATE_TIME = new Chameleon<>(SYSTEM_DEFAULT_ZONE_ID, Instant.class, LocalDateTime.class) {
        @Override
        protected LocalDateTime handleObject(Instant source) {
            return LocalDateTime.ofInstant(source, getContextParameter());
        }
    };

    /**
     * Instant 转 OffsetDateTime 转换器
     * <p>
     * 使用系统默认时区进行转换。
     * </p>
     */
    public static final Chameleon<Instant, OffsetDateTime, ZoneId> INSTANT__TO__OFFSET_DATE_TIME = new Chameleon<>(SYSTEM_DEFAULT_ZONE_ID, Instant.class, OffsetDateTime.class) {
        @Override
        protected OffsetDateTime handleObject(Instant source) {
            return OffsetDateTime.ofInstant(source, getContextParameter());
        }
    };

    /**
     * Instant 转 ZonedDateTime 转换器
     * <p>
     * 使用系统默认时区进行转换。
     * </p>
     */
    public static final Chameleon<Instant, ZonedDateTime, ZoneId> INSTANT__TO__ZONED_DATE_TIME = new Chameleon<>(SYSTEM_DEFAULT_ZONE_ID, Instant.class, ZonedDateTime.class) {
        @Override
        protected ZonedDateTime handleObject(Instant source) {
            return ZonedDateTime.ofInstant(source, getContextParameter());
        }
    };

    /**
     * LocalDateTime 转 Long (毫秒时间戳) 转换器
     * <p>
     * 使用系统默认时区偏移量进行转换。
     * </p>
     */
    public static final Chameleon<LocalDateTime, Long, ZoneOffset> LOCAL_DATE_TIME__TO__LONG = new Chameleon<>(SYSTEM_DEFAULT_ZONE_OFFSET, LocalDateTime.class, Long.class) {
        @Override
        protected Long handleObject(LocalDateTime source) {
            return source.toEpochSecond(getContextParameter()) * 1000 + source.getNano() / 1_000_000;
        }
    };

    /**
     * LocalDateTime 转 Date 转换器
     * <p>
     * 使用系统默认时区偏移量进行转换。
     * </p>
     */
    public static final Chameleon<LocalDateTime, Date, ZoneOffset> LOCAL_DATE_TIME__TO__DATE = new Chameleon<>(SYSTEM_DEFAULT_ZONE_OFFSET, LocalDateTime.class, Date.class) {
        @Override
        protected Date handleObject(LocalDateTime source) {
            return new Date(source.toEpochSecond(getContextParameter()) * 1000 + source.getNano() / 1_000_000);
        }
    };

    /**
     * LocalDateTime 转 Instant 转换器
     * <p>
     * 使用系统默认时区偏移量进行转换。
     * </p>
     */
    public static final Chameleon<LocalDateTime, Instant, ZoneOffset> LOCAL_DATE_TIME__TO__INSTANT = new Chameleon<>(SYSTEM_DEFAULT_ZONE_OFFSET, LocalDateTime.class, Instant.class) {
        @Override
        protected Instant handleObject(LocalDateTime source) {
            return source.toInstant(getContextParameter());
        }
    };

    /**
     * LocalDateTime 转 OffsetDateTime 转换器
     * <p>
     * 使用系统默认时区偏移量进行转换。
     * </p>
     */
    public static final Chameleon<LocalDateTime, OffsetDateTime, ZoneOffset> LOCAL_DATE_TIME__TO__OFFSET_DATE_TIME = new Chameleon<>(SYSTEM_DEFAULT_ZONE_OFFSET, LocalDateTime.class, OffsetDateTime.class) {
        @Override
        protected OffsetDateTime handleObject(LocalDateTime source) {
            return OffsetDateTime.of(source, getContextParameter());
        }
    };

    /**
     * LocalDateTime 转 ZonedDateTime 转换器
     * <p>
     * 使用系统默认时区偏移量进行转换。
     * </p>
     */
    public static final Chameleon<LocalDateTime, ZonedDateTime, ZoneId> LOCAL_DATE_TIME__TO__ZONED_DATE_TIME = new Chameleon<>(SYSTEM_DEFAULT_ZONE_OFFSET, LocalDateTime.class, ZonedDateTime.class) {
        @Override
        protected ZonedDateTime handleObject(LocalDateTime source) {
            return ZonedDateTime.of(source, getContextParameter());
        }
    };

    /**
     * OffsetDateTime 转 Long (毫秒时间戳) 转换器
     */
    public static final Chameleon<OffsetDateTime, Long, ?> OFFSET_DATE_TIME__TO__LONG = new Chameleon<>(OffsetDateTime.class, Long.class) {
        @Override
        protected Long handleObject(OffsetDateTime source) {
            return source.toEpochSecond() * 1000 + source.getNano() / 1_000_000;
        }
    };

    /**
     * OffsetDateTime 转 Date 转换器
     */
    public static final Chameleon<OffsetDateTime, Date, ?> OFFSET_DATE_TIME__TO__DATE = new Chameleon<>(OffsetDateTime.class, Date.class) {
        @Override
        protected Date handleObject(OffsetDateTime source) {
            return new Date(source.toEpochSecond() * 1000 + source.getNano() / 1_000_000);
        }
    };

    /**
     * OffsetDateTime 转 Instant 转换器
     */
    public static final Chameleon<OffsetDateTime, Instant, ?> OFFSET_DATE_TIME__TO__INSTANT = new Chameleon<>(OffsetDateTime.class, Instant.class) {
        @Override
        protected Instant handleObject(OffsetDateTime source) {
            return source.toInstant();
        }
    };

    /**
     * OffsetDateTime 转 ZonedDateTime 转换器
     */
    public static final Chameleon<OffsetDateTime, ZonedDateTime, ?> OFFSET_DATE_TIME__TO__ZONED_DATE_TIME = new Chameleon<>(OffsetDateTime.class, ZonedDateTime.class) {
        @Override
        protected ZonedDateTime handleObject(OffsetDateTime source) {
            return source.toZonedDateTime();
        }
    };

    /**
     * OffsetDateTime 转 LocalDateTime 转换器
     */
    public static final Chameleon<OffsetDateTime, LocalDateTime, ?> OFFSET_DATE_TIME__TO__LOCAL_DATE_TIME = new Chameleon<>(OffsetDateTime.class, LocalDateTime.class) {
        @Override
        protected LocalDateTime handleObject(OffsetDateTime source) {
            return source.toLocalDateTime();
        }
    };

    /**
     * ZonedDateTime 转 Long (毫秒时间戳) 转换器
     */
    public static final Chameleon<ZonedDateTime, Long, ?> ZONED_DATE_TIME__TO__LONG = new Chameleon<>(ZonedDateTime.class, Long.class) {
        @Override
        protected Long handleObject(ZonedDateTime source) {
            return source.toEpochSecond() * 1000 + source.getNano() / 1_000_000;
        }
    };

    /**
     * ZonedDateTime 转 Date 转换器
     */
    public static final Chameleon<ZonedDateTime, Date, ?> ZONED_DATE_TIME__TO__DATE = new Chameleon<>(ZonedDateTime.class, Date.class) {
        @Override
        protected Date handleObject(ZonedDateTime source) {
            return new Date(source.toEpochSecond() * 1000 + source.getNano() / 1_000_000);
        }
    };

    /**
     * ZonedDateTime 转 Instant 转换器
     */
    public static final Chameleon<ZonedDateTime, Instant, ?> ZONED_DATE_TIME__TO__INSTANT = new Chameleon<>(ZonedDateTime.class, Instant.class) {
        @Override
        protected Instant handleObject(ZonedDateTime source) {
            return source.toInstant();
        }
    };

    /**
     * ZonedDateTime 转 OffsetDateTime 转换器
     */
    public static final Chameleon<ZonedDateTime, OffsetDateTime, ?> ZONED_DATE_TIME__TO__OFFSET_DATE_TIME = new Chameleon<>(ZonedDateTime.class, OffsetDateTime.class) {
        @Override
        protected OffsetDateTime handleObject(ZonedDateTime source) {
            return source.toOffsetDateTime();
        }
    };

    /**
     * ZonedDateTime 转 LocalDateTime 转换器
     */
    public static final Chameleon<ZonedDateTime, LocalDateTime, ?> ZONED_DATE_TIME__TO__LOCAL_DATE_TIME = new Chameleon<>(ZonedDateTime.class, LocalDateTime.class) {
        @Override
        protected LocalDateTime handleObject(ZonedDateTime source) {
            return source.toLocalDateTime();
        }
    };
}

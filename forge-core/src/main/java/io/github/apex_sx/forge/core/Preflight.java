/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author ApexSX
 */
package io.github.apex_sx.forge.core;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 通用预检查工具类
 * <p>
 * 提供三层能力，全部为无状态静态方法：
 * - 判断层：{@link #isEmpty(Object)}、{@link #isNotEmpty(Object)} 等只返回判断结果
 * - 兜底层：{@link #defaultIfEmpty(Object, Object)} 等在不满足要求时返回默认值，不抛异常
 * - 校验层：{@link #requireNotEmpty(Object, Supplier)}、{@link #requireEmpty(Object, Supplier)} 等
 * 不满足要求时抛出{@link IllegalArgumentException}
 * <p>
 * 覆盖的检查项：
 * - 判空：对象、字符串、集合、映射、{@link Optional}与数组
 * - 数字：{@link #isInRange(Number, Number, Number)} 区间判断，
 * {@link #isNatural(Number)} 自然数判断，{@link #isPositive(Number)} 正整数判断
 * - 集合元素：{@link #isContains(Collection, Object)} 单个值，
 * {@link #isContainsAny(Collection, Collection)} 任一值，{@link #isContainsAll(Collection, Collection)} 全部值
 * - 字符序列区间：{@link #isRegionMatches(CharSequence, int, CharSequence, int, int)} 按区间比较，
 * 两侧都是{@link String}时直接调用JDK原生实现，否则逐字符比较
 * - 通配符：{@link #isWildcardMatch(CharSequence, CharSequence)} 支持{@code ?}单字符、
 * {@code *}任意长度，{@code \}用于转义
 * <p>
 * 统一判空规则：支持{@link CharSequence}、{@link Collection}、{@link Map}、{@link Optional}与数组，
 * 其他类型仅判断是否为{@code null}。校验层的错误信息通过{@link Supplier}惰性提供，
 * 仅在对象不满足要求时才会构建错误信息，正常路径不产生字符串拼接开销。
 *
 * @author ApexSX
 */
public final class Preflight {
    /**
     * 返回对象本身，对象为空时返回默认值
     * <p>
     * 判空规则与{@link #isEmpty(Object)}一致。与{@link #requireNotEmpty(Object, Supplier)}不同，
     * 该方法在对象为空时不抛异常，适合为可空入参提供兜底值。
     *
     * @param value        待检查对象
     * @param defaultValue 对象为空时返回的默认值
     * @param <T>          待检查对象类型
     * @return 不为空时返回{@code value}本身，为空时返回{@code defaultValue}
     */
    public static <T> T defaultIfEmpty(T value, T defaultValue) {
        return Preflight.isNotEmpty(value) ? value : defaultValue;
    }

    /**
     * 返回集合本身，集合不包含指定元素时返回默认值
     *
     * @param collection   待检查集合
     * @param value        待查找元素
     * @param defaultValue 不包含元素时返回的默认值
     * @param <C>          集合类型
     * @return 包含元素返回{@code collection}本身，否则返回{@code defaultValue}
     */
    public static <C extends Collection<?>> C defaultIfNotContains(C collection, Object value, C defaultValue) {
        return Preflight.isContains(collection, value) ? collection : defaultValue;
    }

    /**
     * 返回集合本身，集合未包含指定元素中的全部时返回默认值
     *
     * @param collection   待检查集合
     * @param values       待查找元素集合
     * @param defaultValue 未包含全部元素时返回的默认值
     * @param <C>          集合类型
     * @return 包含全部元素返回{@code collection}本身，否则返回{@code defaultValue}
     */
    public static <C extends Collection<?>> C defaultIfNotContainsAll(C collection, Collection<?> values, C defaultValue) {
        return Preflight.isContainsAll(collection, values) ? collection : defaultValue;
    }

    /**
     * 返回集合本身，集合不包含指定元素中的任意一个时返回默认值
     *
     * @param collection   待检查集合
     * @param values       待查找元素集合
     * @param defaultValue 不包含任意一个元素时返回的默认值
     * @param <C>          集合类型
     * @return 包含至少一个元素返回{@code collection}本身，否则返回{@code defaultValue}
     */
    public static <C extends Collection<?>> C defaultIfNotContainsAny(C collection, Collection<?> values, C defaultValue) {
        return Preflight.isContainsAny(collection, values) ? collection : defaultValue;
    }

    /**
     * 返回数字本身，数字不是自然数时返回默认值
     *
     * @param value        待检查数字
     * @param defaultValue 不是自然数时返回的默认值
     * @param <T>          数字类型
     * @return 是自然数返回{@code value}本身，否则返回{@code defaultValue}
     */
    public static <T extends Number> T defaultIfNotNatural(T value, T defaultValue) {
        return Preflight.isNatural(value) ? value : defaultValue;
    }

    /**
     * 返回数字本身，数字不是正整数时返回默认值
     *
     * @param value        待检查数字
     * @param defaultValue 不是正整数时返回的默认值
     * @param <T>          数字类型
     * @return 是正整数返回{@code value}本身，否则返回{@code defaultValue}
     */
    public static <T extends Number> T defaultIfNotPositive(T value, T defaultValue) {
        return Preflight.isPositive(value) ? value : defaultValue;
    }

    /**
     * 返回字符序列本身，两个区间不相等时返回默认值
     *
     * @param value        待检查的字符序列
     * @param valueOffset  待检查字符序列的起始偏移
     * @param other        参照字符序列
     * @param otherOffset  参照字符序列的起始偏移
     * @param length       需要比较的字符数量
     * @param defaultValue 区间不相等时返回的默认值
     * @param <T>          字符序列类型
     * @return 区间相等返回{@code value}本身，否则返回{@code defaultValue}
     */
    public static <T extends CharSequence> T defaultIfNotRegionMatches(T value, int valueOffset, CharSequence other, int otherOffset, int length, T defaultValue) {
        return Preflight.isRegionMatches(value, valueOffset, other, otherOffset, length) ? value : defaultValue;
    }

    /**
     * 返回字符序列本身，两个区间不相等时返回默认值，可选择忽略大小写
     *
     * @param ignoreCase   是否忽略大小写
     * @param value        待检查的字符序列
     * @param valueOffset  待检查字符序列的起始偏移
     * @param other        参照字符序列
     * @param otherOffset  参照字符序列的起始偏移
     * @param length       需要比较的字符数量
     * @param defaultValue 区间不相等时返回的默认值
     * @param <T>          字符序列类型
     * @return 区间相等返回{@code value}本身，否则返回{@code defaultValue}
     */
    public static <T extends CharSequence> T defaultIfNotRegionMatches(boolean ignoreCase, T value, int valueOffset, CharSequence other, int otherOffset, int length, T defaultValue) {
        return Preflight.isRegionMatches(ignoreCase, value, valueOffset, other, otherOffset, length) ? value : defaultValue;
    }

    /**
     * 返回字符序列本身，不匹配通配符表达式时返回默认值
     *
     * @param value        待检查的字符序列
     * @param pattern      通配符表达式
     * @param defaultValue 不匹配时返回的默认值
     * @param <T>          字符序列类型
     * @return 匹配返回{@code value}本身，否则返回{@code defaultValue}
     */
    public static <T extends CharSequence> T defaultIfNotWildcardMatch(T value, CharSequence pattern, T defaultValue) {
        return Preflight.isWildcardMatch(value, pattern) ? value : defaultValue;
    }

    /**
     * 返回数字本身，数字超出区间时返回默认值
     * <p>
     * 区间为闭区间，{@code min}或{@code max}为{@code null}表示该侧不设限，
     * 比较规则与{@link #isInRange(Number, Number, Number)}一致，不抛异常。
     *
     * @param value        待检查数字
     * @param min          下界，{@code null}表示不设下界
     * @param max          上界，{@code null}表示不设上界
     * @param defaultValue 超出区间时返回的默认值
     * @param <T>          数字类型
     * @return 落在区间内返回{@code value}本身，否则返回{@code defaultValue}
     */
    public static <T extends Number> T defaultIfOutOfRange(T value, Number min, Number max, T defaultValue) {
        return Preflight.isInRange(value, min, max) ? value : defaultValue;
    }

    /**
     * 判断集合是否包含指定元素
     *
     * @param collection 待检查集合
     * @param value      待查找元素
     * @return 包含返回true，集合为{@code null}或不包含返回false
     */
    public static boolean isContains(Collection<?> collection, Object value) {
        return collection != null && collection.contains(value);
    }

    /**
     * 判断集合是否包含指定元素中的全部
     * <p>
     * 元素集合为空时不存在待包含的元素，视为平凡成立。
     *
     * @param collection 待检查集合
     * @param values     待查找元素集合
     * @return 全部包含返回true；任一参数为{@code null}返回false，元素集合为空返回true
     */
    public static boolean isContainsAll(Collection<?> collection, Collection<?> values) {
        if (collection == null || values == null) {
            return false;
        }
        for (Object value : values) {
            if (!collection.contains(value)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断集合是否包含指定元素中的任意一个
     *
     * @param collection 待检查集合
     * @param values     待查找元素集合
     * @return 至少包含一个返回true；任一参数为{@code null}或元素集合为空返回false
     */
    public static boolean isContainsAny(Collection<?> collection, Collection<?> values) {
        if (collection == null || values == null) {
            return false;
        }
        for (Object value : values) {
            if (collection.contains(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否为空
     * <p>
     * 支持{@link CharSequence}、{@link Collection}、{@link Map}、{@link Optional}与数组，
     * 其他类型仅判断是否为null，{@code null}视为空。
     *
     * @param value 待检查对象
     * @return 为空返回true，否则返回false
     */
    public static boolean isEmpty(Object value) {
        return switch (value) {
            case null -> true;
            case CharSequence chars -> {
                if (chars.isEmpty()) {
                    yield true;
                }
                if (chars instanceof String) {
                    yield chars.toString().isBlank();
                } else {
                    int i = 0;
                    int length = chars.length();
                    while (i < length) {
                        int codePoint = Character.codePointAt(chars, i);
                        if (!Character.isWhitespace(codePoint)) {
                            yield false;
                        }
                        i += Character.charCount(codePoint);
                    }
                    yield true;
                }
            }
            case Collection<?> collection -> collection.isEmpty();
            case Map<?, ?> map -> map.isEmpty();
            case Optional<?> optional -> optional.isEmpty();
            default -> value.getClass().isArray() && Array.getLength(value) == 0;
        };
    }

    /**
     * 判断数字是否落在闭区间内
     * <p>
     * 区间为闭区间，{@code min}或{@code max}为{@code null}表示该侧不设限，
     * 比较规则与{@link #compare(Number, Number)}一致：常规包装类型之间按原生类型比较，
     * 涉及{@link BigDecimal}、{@link BigInteger}、原子类型或自定义{@link Number}时改用{@link BigDecimal}精确比较，
     * 避免大整数与高精度小数在转换时丢失精度。
     *
     * @param value 待检查数字
     * @param min   下界，{@code null}表示不设下界
     * @param max   上界，{@code null}表示不设上界
     * @return 落在区间内返回true，{@code null}或越界返回false
     */
    public static boolean isInRange(Number value, Number min, Number max) {
        if (value == null) {
            return false;
        }
        if (min != null && Preflight.compare(value, min) < 0) {
            return false;
        }
        return max == null || Preflight.compare(value, max) <= 0;
    }

    /**
     * 判断数字是否为自然数
     * <p>
     * 自然数指大于等于0的整数，{@code 0}与{@code 2.0}是自然数，
     * {@code -1}、{@code 1.5}与{@code NaN}不是自然数。
     *
     * @param value 待检查数字
     * @return 是自然数返回true，{@code null}或不是自然数返回false
     */
    public static boolean isNatural(Number value) {
        return value != null && Preflight.isIntegral(value) && Preflight.compare(value, 0) >= 0;
    }

    /**
     * 判断对象是否不为空
     * <p>
     * 判空规则与{@link #isEmpty(Object)}一致，{@code null}视为空。
     *
     * @param value 待检查对象
     * @return 不为空返回true，否则返回false
     */
    public static boolean isNotEmpty(Object value) {
        return !Preflight.isEmpty(value);
    }

    /**
     * 判断数字是否为正整数
     * <p>
     * 正整数指大于0的整数，{@code 1}与{@code 2.0}是正整数，
     * {@code 0}、{@code -1}与{@code 1.5}不是正整数。
     *
     * @param value 待检查数字
     * @return 是正整数返回true，{@code null}或不是正整数返回false
     */
    public static boolean isPositive(Number value) {
        return value != null && Preflight.isIntegral(value) && Preflight.compare(value, 0) > 0;
    }

    /**
     * 判断字符序列的指定区间是否与另一个字符序列的指定区间相等
     * <p>
     * 语义与{@link String#regionMatches(int, String, int, int)}一致，偏移量以{@code char}为单位。
     *
     * @param value       待检查的字符序列
     * @param valueOffset 待检查字符序列的起始偏移
     * @param other       参照字符序列
     * @param otherOffset 参照字符序列的起始偏移
     * @param length      需要比较的字符数量
     * @return 区间相等返回true，任一参数为{@code null}或偏移越界返回false
     */
    public static boolean isRegionMatches(CharSequence value, int valueOffset, CharSequence other, int otherOffset, int length) {
        return Preflight.isRegionMatches(false, value, valueOffset, other, otherOffset, length);
    }

    /**
     * 判断字符序列的指定区间是否相等，可选择忽略大小写
     * <p>
     * 语义与{@link String#regionMatches(boolean, int, String, int, int)}一致，偏移量以{@code char}为单位，
     * 不作code point对齐。两侧都是{@link String}时直接调用JDK原生实现，否则逐字符比较，
     * 避免为{@link StringBuilder}等非{@link String}实现创建字符串副本。
     *
     * @param ignoreCase  是否忽略大小写
     * @param value       待检查的字符序列
     * @param valueOffset 待检查字符序列的起始偏移
     * @param other       参照字符序列
     * @param otherOffset 参照字符序列的起始偏移
     * @param length      需要比较的字符数量，为0且偏移合法时返回true
     * @return 区间相等返回true，任一参数为{@code null}或偏移越界返回false
     */
    public static boolean isRegionMatches(boolean ignoreCase, CharSequence value, int valueOffset, CharSequence other, int otherOffset, int length) {
        if (Preflight.isEmpty(value) || Preflight.isEmpty(other)) {
            return false;
        }
        if (value instanceof String left && other instanceof String right) {
            return left.regionMatches(ignoreCase, valueOffset, right, otherOffset, length);
        }
        return Preflight.regionMatches(ignoreCase, value, valueOffset, other, otherOffset, length);
    }

    /**
     * 判断字符序列是否匹配通配符表达式
     * <p>
     * 通配符规则：
     * - {@code ?} 匹配任意一个字符，按code point计，增补字符算一个
     * - {@code *} 匹配任意长度的字符，包括0个
     * - {@code \} 使其后的一个字符失去通配含义，可用于转义{@code ?}、{@code *}与{@code \}本身，
     * 位于末尾的{@code \}按字面反斜杠处理
     * <p>
     * 采用双指针加星号回退实现，不创建正则对象与字符串副本，正常路径为O(n)
     *
     * @param value   待检查的字符序列
     * @param pattern 通配符表达式
     * @return 匹配返回true，任一参数为{@code null}或不匹配返回false
     */
    public static boolean isWildcardMatch(CharSequence value, CharSequence pattern) {
        if (value == null || pattern == null) {
            return false;
        }
        int valueLength = value.length();
        int patternLength = pattern.length();
        int valueIndex = 0;
        int patternIndex = 0;
        int starPatternIndex = -1;
        int starValueIndex = -1;
        while (valueIndex < valueLength) {
            if (patternIndex < patternLength) {
                char current = pattern.charAt(patternIndex);
                if (current == '*') {
                    starPatternIndex = ++patternIndex;
                    starValueIndex = valueIndex;
                    continue;
                }
                if (current == '?') {
                    patternIndex++;
                    valueIndex += Character.charCount(Character.codePointAt(value, valueIndex));
                    continue;
                }
                int expected;
                int patternWidth;
                if (current == '\\' && patternIndex + 1 < patternLength) {
                    expected = Character.codePointAt(pattern, patternIndex + 1);
                    patternWidth = 1 + Character.charCount(expected);
                } else {
                    expected = Character.codePointAt(pattern, patternIndex);
                    patternWidth = Character.charCount(expected);
                }
                int actual = Character.codePointAt(value, valueIndex);
                if (expected == actual) {
                    patternIndex += patternWidth;
                    valueIndex += Character.charCount(actual);
                    continue;
                }
            }
            if (starPatternIndex < 0) {
                return false;
            }
            starValueIndex += Character.charCount(Character.codePointAt(value, starValueIndex));
            if (starValueIndex > valueLength) {
                return false;
            }
            valueIndex = starValueIndex;
            patternIndex = starPatternIndex;
        }
        while (patternIndex < patternLength && pattern.charAt(patternIndex) == '*') {
            patternIndex++;
        }
        return patternIndex == patternLength;
    }

    /**
     * 判断数字是否属于基本类型的包装类型
     * <p>
     * 只用{@code instanceof}判定，比{@link PrimitiveType#of(Class)}的查表更轻量；
     * 八种包装类型中只有{@link Byte}、{@link Short}、{@link Integer}、{@link Long}、{@link Float}与
     * {@link Double}是{@link Number}。
     *
     * @param value 待检查数字
     * @return 是基本类型的包装类型返回true，{@code null}或不是时返回false
     */
    public static boolean isWrapperNumber(Number value) {
        return value instanceof Byte || value instanceof Short || value instanceof Integer
                || value instanceof Long || value instanceof Float || value instanceof Double;
    }

    /**
     * 要求集合包含指定元素，否则抛出异常
     *
     * @param collection       待检查集合
     * @param value            待查找元素
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <C>              集合类型
     * @return 校验通过时返回原集合，便于链式调用
     * @throws IllegalArgumentException 当集合为{@code null}或不包含该元素时抛出
     */
    public static <C extends Collection<?>> C requireContains(C collection, Object value, Supplier<String> exceptionMessage) {
        if (!Preflight.isContains(collection, value)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return collection;
    }

    /**
     * 要求集合包含指定元素中的全部，否则抛出异常
     *
     * @param collection       待检查集合
     * @param values           待查找元素集合
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <C>              集合类型
     * @return 校验通过时返回原集合，便于链式调用
     * @throws IllegalArgumentException 当集合未包含全部元素时抛出
     */
    public static <C extends Collection<?>> C requireContainsAll(C collection, Collection<?> values, Supplier<String> exceptionMessage) {
        if (!Preflight.isContainsAll(collection, values)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return collection;
    }

    /**
     * 要求集合包含指定元素中的任意一个，否则抛出异常
     *
     * @param collection       待检查集合
     * @param values           待查找元素集合
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <C>              集合类型
     * @return 校验通过时返回原集合，便于链式调用
     * @throws IllegalArgumentException 当集合不包含任意一个元素时抛出
     */
    public static <C extends Collection<?>> C requireContainsAny(C collection, Collection<?> values, Supplier<String> exceptionMessage) {
        if (!Preflight.isContainsAny(collection, values)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return collection;
    }

    /**
     * 要求对象为空，不为空时抛出异常
     * <p>
     * 判空规则与{@link #isEmpty(Object)}一致，错误信息由{@code exceptionMessage}惰性提供，
     * 仅在对象不为空时才调用其{@link Supplier#get()}。
     *
     * @param value            待检查对象
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <T>              待检查对象类型
     * @return 校验通过时返回原对象，便于链式调用
     * @throws IllegalArgumentException 当{@code value}不为空时抛出
     */
    public static <T> T requireEmpty(T value, Supplier<String> exceptionMessage) {
        if (Preflight.isNotEmpty(value)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return value;
    }

    /**
     * 要求数字落在闭区间内，否则抛出异常
     *
     * @param value            待检查数字
     * @param min              下界，{@code null}表示不设下界
     * @param max              上界，{@code null}表示不设上界
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <T>              数字类型
     * @return 校验通过时返回原数字，便于链式调用
     * @throws IllegalArgumentException 当{@code value}为{@code null}或超出区间时抛出
     */
    public static <T extends Number> T requireInRange(T value, Number min, Number max, Supplier<String> exceptionMessage) {
        if (!Preflight.isInRange(value, min, max)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return value;
    }

    /**
     * 要求数字是自然数，否则抛出异常
     *
     * @param value            待检查数字
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <T>              数字类型
     * @return 校验通过时返回原数字，便于链式调用
     * @throws IllegalArgumentException 当{@code value}不是自然数时抛出
     */
    public static <T extends Number> T requireNatural(T value, Supplier<String> exceptionMessage) {
        if (!Preflight.isNatural(value)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return value;
    }

    /**
     * 要求对象不为空，为空时抛出异常
     * <p>
     * 判空规则与{@link #isEmpty(Object)}一致，错误信息由{@code exceptionMessage}惰性提供，
     * 仅在对象为空时才调用其{@link Supplier#get()}。
     *
     * @param value            待检查对象
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <T>              待检查对象类型
     * @return 校验通过时返回原对象，便于链式调用
     * @throws IllegalArgumentException 当{@code value}为空时抛出
     */
    public static <T> T requireNotEmpty(T value, Supplier<String> exceptionMessage) {
        if (Preflight.isEmpty(value)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return value;
    }

    /**
     * 要求数字是正整数，否则抛出异常
     *
     * @param value            待检查数字
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <T>              数字类型
     * @return 校验通过时返回原数字，便于链式调用
     * @throws IllegalArgumentException 当{@code value}不是正整数时抛出
     */
    public static <T extends Number> T requirePositive(T value, Supplier<String> exceptionMessage) {
        if (!Preflight.isPositive(value)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return value;
    }

    /**
     * 要求字符序列的指定区间相等，否则抛出异常
     *
     * @param value            待检查的字符序列
     * @param valueOffset      待检查字符序列的起始偏移
     * @param other            参照字符序列
     * @param otherOffset      参照字符序列的起始偏移
     * @param length           需要比较的字符数量
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <T>              字符序列类型
     * @return 校验通过时返回原字符序列，便于链式调用
     * @throws IllegalArgumentException 当区间不相等时抛出
     */
    public static <T extends CharSequence> T requireRegionMatches(T value, int valueOffset, CharSequence other, int otherOffset, int length, Supplier<String> exceptionMessage) {
        if (!Preflight.isRegionMatches(value, valueOffset, other, otherOffset, length)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return value;
    }

    /**
     * 要求字符序列的指定区间相等，否则抛出异常，可选择忽略大小写
     *
     * @param ignoreCase       是否忽略大小写
     * @param value            待检查的字符序列
     * @param valueOffset      待检查字符序列的起始偏移
     * @param other            参照字符序列
     * @param otherOffset      参照字符序列的起始偏移
     * @param length           需要比较的字符数量
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <T>              字符序列类型
     * @return 校验通过时返回原字符序列，便于链式调用
     * @throws IllegalArgumentException 当区间不相等时抛出
     */
    public static <T extends CharSequence> T requireRegionMatches(boolean ignoreCase, T value, int valueOffset, CharSequence other, int otherOffset, int length, Supplier<String> exceptionMessage) {
        if (!Preflight.isRegionMatches(ignoreCase, value, valueOffset, other, otherOffset, length)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return value;
    }

    /**
     * 要求字符序列匹配通配符表达式，否则抛出异常
     *
     * @param value            待检查的字符序列
     * @param pattern          通配符表达式
     * @param exceptionMessage 校验失败时的异常信息提供者
     * @param <T>              字符序列类型
     * @return 校验通过时返回原字符序列，便于链式调用
     * @throws IllegalArgumentException 当不匹配时抛出
     */
    public static <T extends CharSequence> T requireWildcardMatch(T value, CharSequence pattern, Supplier<String> exceptionMessage) {
        if (!Preflight.isWildcardMatch(value, pattern)) {
            throw new IllegalArgumentException(exceptionMessage.get());
        }
        return value;
    }

    /**
     * 比较两个数字的大小
     * <p>
     * 两侧都是基本类型的包装类型时直接按原生类型比较，不产生对象分配：
     * 任一侧为{@link Double}或{@link Float}时按{@code double}比较，两侧都是整数时按{@code long}比较。
     * 只要有一侧不是基本类型的包装类型，例如{@link BigDecimal}、{@link BigInteger}、原子类型或
     * 自定义{@link Number}，两侧就统一转成{@link BigDecimal}精确比较，
     * 浮点数按其十进制文本转换，避免大整数与高精度小数在比较时丢失精度。
     *
     * @param left  左侧数字
     * @param right 右侧数字
     * @return 小于返回负数，相等返回0，大于返回正数
     */
    private static int compare(Number left, Number right) {
        if (Preflight.isWrapperNumber(left) && Preflight.isWrapperNumber(right)) {
            if (left instanceof Double || left instanceof Float || right instanceof Double || right instanceof Float) {
                return Double.compare(left.doubleValue(), right.doubleValue());
            }
            return Long.compare(left.longValue(), right.longValue());
        }
        BigDecimal leftValue = left instanceof BigDecimal decimal ? decimal : new BigDecimal(left.toString());
        BigDecimal rightValue = right instanceof BigDecimal decimal ? decimal : new BigDecimal(right.toString());
        return leftValue.compareTo(rightValue);
    }

    /**
     * 判断数字是否不存在小数部分
     * <p>
     * {@link BigDecimal}按小数位判断，{@link BigInteger}与{@code long}范围内的整数恒为true，
     * 其余类型先转为{@code double}再判断是否有小数部分，{@code NaN}与无穷大返回false。
     *
     * @param value 待检查数字
     * @return 不存在小数部分返回true，{@code null}以外的非数类型按{@code double}判断
     */
    private static boolean isIntegral(Number value) {
        if (value instanceof BigDecimal decimal) {
            return decimal.stripTrailingZeros().scale() <= 0;
        }
        if (value instanceof BigInteger) {
            return true;
        }
        double number = value.doubleValue();
        return number == Math.rint(number) && !Double.isInfinite(number);
    }

    /**
     * 逐字符比较两个字符序列的指定区间，供非{@link String}实现的高性能分支使用
     * <p>
     * 调用方需保证两个字符序列非{@code null}。
     *
     * @param ignoreCase  是否忽略大小写
     * @param value       待检查的字符序列
     * @param valueOffset 待检查字符序列的起始偏移
     * @param other       参照字符序列
     * @param otherOffset 参照字符序列的起始偏移
     * @param length      需要比较的字符数量
     * @return 区间相等返回true，偏移越界返回false
     */
    private static boolean regionMatches(boolean ignoreCase, CharSequence value, int valueOffset, CharSequence other, int otherOffset, int length) {
        if (valueOffset < 0 || otherOffset < 0
                || valueOffset > (long) value.length() - length
                || otherOffset > (long) other.length() - length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            char left = value.charAt(valueOffset + i);
            char right = other.charAt(otherOffset + i);
            if (left == right) {
                continue;
            }
            if (!ignoreCase) {
                return false;
            }
            if (Character.toUpperCase(left) != Character.toUpperCase(right)
                    && Character.toLowerCase(left) != Character.toLowerCase(right)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 工具类不允许实例化
     */
    private Preflight() {
    }
}

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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link Preflight} 单元测试
 *
 * @author ApexSX
 */
@SuppressWarnings("all")
@DisplayName("Preflight 预检查工具类")
class PreflightTest {
    /**
     * 统一的异常信息，便于断言错误信息被原样传递
     */
    private static final Supplier<String> MESSAGE = () -> "校验失败";

    @Nested
    @DisplayName("判空判断")
    class EmptyTest {
        @Test
        @DisplayName("null 视为空")
        void nullIsEmpty() {
            assertTrue(Preflight.isEmpty(null));
            assertFalse(Preflight.isNotEmpty(null));
        }

        @Test
        @DisplayName("空字符串与全空白字符串视为空")
        void emptyAndBlankStringIsEmpty() {
            assertTrue(Preflight.isEmpty(""));
            assertTrue(Preflight.isEmpty(" "));
            assertTrue(Preflight.isEmpty("\t\r\n"));
        }

        @Test
        @DisplayName("非空白字符串不为空")
        void nonBlankStringIsNotEmpty() {
            assertTrue(Preflight.isNotEmpty("a"));
            assertTrue(Preflight.isNotEmpty(" a "));
            assertFalse(Preflight.isEmpty("a"));
        }

        @Test
        @DisplayName("非 String 的字符序列按 code point 判断空白")
        void nonStringCharSequenceIsCheckedByCodePoint() {
            assertTrue(Preflight.isEmpty(new StringBuilder()));
            assertTrue(Preflight.isEmpty(new StringBuilder(" \t")));
            assertTrue(Preflight.isNotEmpty(new StringBuilder("a")));
        }

        @Test
        @DisplayName("空集合、映射与 Optional 视为空")
        void emptyContainersAreEmpty() {
            assertTrue(Preflight.isEmpty(List.of()));
            assertTrue(Preflight.isEmpty(new HashMap<>()));
            assertTrue(Preflight.isEmpty(Optional.empty()));
        }

        @Test
        @DisplayName("非空集合、映射与 Optional 不为空")
        void nonEmptyContainersAreNotEmpty() {
            assertTrue(Preflight.isNotEmpty(List.of("a")));
            assertTrue(Preflight.isNotEmpty(Map.of("k", "v")));
            assertTrue(Preflight.isNotEmpty(Optional.of("a")));
        }

        @Test
        @DisplayName("空数组视为空，非空数组不为空")
        void arraysAreCheckedByLength() {
            assertTrue(Preflight.isEmpty(new int[0]));
            assertTrue(Preflight.isEmpty(new String[0]));
            assertTrue(Preflight.isNotEmpty(new int[]{0}));
            assertTrue(Preflight.isNotEmpty(new String[]{null}));
        }

        @Test
        @DisplayName("其他类型仅判断是否为 null")
        void otherTypesOnlyCheckNull() {
            assertTrue(Preflight.isNotEmpty(new Object()));
            assertTrue(Preflight.isNotEmpty(0));
        }
    }

    @Nested
    @DisplayName("包装数字判断")
    class WrapperNumberTest {
        @Test
        @DisplayName("六种数值包装类型返回 true")
        void wrapperNumbersReturnTrue() {
            assertTrue(Preflight.isWrapperNumber((byte) 1));
            assertTrue(Preflight.isWrapperNumber((short) 1));
            assertTrue(Preflight.isWrapperNumber(1));
            assertTrue(Preflight.isWrapperNumber(1L));
            assertTrue(Preflight.isWrapperNumber(1.0F));
            assertTrue(Preflight.isWrapperNumber(1.0D));
        }

        @Test
        @DisplayName("BigDecimal、BigInteger 与原子类型返回 false")
        void nonWrapperNumbersReturnFalse() {
            assertFalse(Preflight.isWrapperNumber(BigDecimal.ONE));
            assertFalse(Preflight.isWrapperNumber(BigInteger.ONE));
            assertFalse(Preflight.isWrapperNumber(new AtomicInteger(1)));
        }
    }

    @Nested
    @DisplayName("集合元素判断")
    class ContainsTest {
        private final List<String> collection = new ArrayList<>(List.of("a", "b"));

        @Test
        @DisplayName("包含单个元素")
        void containsValue() {
            assertTrue(Preflight.isContains(collection, "a"));
            assertFalse(Preflight.isContains(collection, "z"));
            assertFalse(Preflight.isContains(null, "a"));
        }

        @Test
        @DisplayName("包含全部元素")
        void containsAllValues() {
            assertTrue(Preflight.isContainsAll(collection, List.of("a", "b")));
            assertFalse(Preflight.isContainsAll(collection, List.of("a", "z")));
            assertFalse(Preflight.isContainsAll(null, List.of("a")));
            assertFalse(Preflight.isContainsAll(collection, null));
        }

        @Test
        @DisplayName("待查找元素为空时视为全部包含")
        void emptyValuesMeansContainsAll() {
            assertTrue(Preflight.isContainsAll(collection, List.of()));
        }

        @Test
        @DisplayName("包含任意一个元素")
        void containsAnyValue() {
            assertTrue(Preflight.isContainsAny(collection, List.of("z", "b")));
            assertFalse(Preflight.isContainsAny(collection, List.of("y", "z")));
            assertFalse(Preflight.isContainsAny(null, List.of("a")));
            assertFalse(Preflight.isContainsAny(collection, null));
            assertFalse(Preflight.isContainsAny(collection, List.of()));
        }
    }

    @Nested
    @DisplayName("数字区间判断")
    class InRangeTest {
        @Test
        @DisplayName("闭区间内返回 true，边界值包含在内")
        void valueWithinClosedRange() {
            assertTrue(Preflight.isInRange(5, 1, 10));
            assertTrue(Preflight.isInRange(1, 1, 10));
            assertTrue(Preflight.isInRange(10, 1, 10));
        }

        @Test
        @DisplayName("越界返回 false")
        void valueOutOfRange() {
            assertFalse(Preflight.isInRange(0, 1, 10));
            assertFalse(Preflight.isInRange(11, 1, 10));
        }

        @Test
        @DisplayName("null 值返回 false")
        void nullValueIsOutOfRange() {
            assertFalse(Preflight.isInRange(null, 1, 10));
        }

        @Test
        @DisplayName("上下界为 null 表示不设限")
        void nullBoundsMeanUnbounded() {
            assertTrue(Preflight.isInRange(-100, null, 0));
            assertTrue(Preflight.isInRange(100, 0, null));
            assertTrue(Preflight.isInRange(Integer.MIN_VALUE, null, null));
        }

        @Test
        @DisplayName("BigDecimal 与包装类型混合时按精确值比较")
        void bigDecimalPrecisionIsPreserved() {
            assertTrue(Preflight.isInRange(new BigDecimal("0.1"), 0, 1));
            assertFalse(Preflight.isInRange(new BigDecimal("9007199254740993"), null, new BigDecimal("9007199254740992")));
            assertTrue(Preflight.isInRange(new BigDecimal("9007199254740992"), null, new BigDecimal("9007199254740993")));
            assertFalse(Preflight.isInRange(2, new BigDecimal("2.5"), null));
            assertTrue(Preflight.isInRange(2, new BigDecimal("1.5"), null));
        }

        @Test
        @DisplayName("自定义 Number 与包装类型混合时按精确值比较")
        void customNumberIsComparedPrecisely() {
            assertTrue(Preflight.isInRange(new AtomicInteger(5), 1, 10));
            assertFalse(Preflight.isInRange(new AtomicInteger(0), 1, 10));
        }
    }

    @Nested
    @DisplayName("自然数与正整数判断")
    class IntegralNumberTest {
        @Test
        @DisplayName("0 与整数、整值浮点数是自然数")
        void naturalNumbers() {
            assertTrue(Preflight.isNatural(0));
            assertTrue(Preflight.isNatural(2));
            assertTrue(Preflight.isNatural(2.0));
            assertTrue(Preflight.isNatural(new BigInteger("0")));
            assertTrue(Preflight.isNatural(new BigDecimal("2.000")));
        }

        @Test
        @DisplayName("负数、小数、NaN 与无穷大不是自然数")
        void nonNaturalNumbers() {
            assertFalse(Preflight.isNatural(-1));
            assertFalse(Preflight.isNatural(1.5));
            assertFalse(Preflight.isNatural(Double.NaN));
            assertFalse(Preflight.isNatural(Double.POSITIVE_INFINITY));
            assertFalse(Preflight.isNatural(new BigDecimal("2.5")));
            assertFalse(Preflight.isNatural(null));
        }

        @Test
        @DisplayName("正整数判断只接受大于 0 的整数")
        void positiveNumbers() {
            assertTrue(Preflight.isPositive(1));
            assertTrue(Preflight.isPositive(2.0));
            assertTrue(Preflight.isPositive(new AtomicInteger(1)));
            assertFalse(Preflight.isPositive(0));
            assertFalse(Preflight.isPositive(-1));
            assertFalse(Preflight.isPositive(1.5));
            assertFalse(Preflight.isPositive(new BigDecimal("0.0")));
            assertFalse(Preflight.isPositive(null));
        }
    }

    @Nested
    @DisplayName("字符序列区间比较")
    class RegionMatchesTest {
        @Test
        @DisplayName("String 区间相等")
        void stringRegionMatches() {
            assertTrue(Preflight.isRegionMatches("abcdef", 1, "bcdef", 0, 4));
            assertTrue(Preflight.isRegionMatches("abc", 0, "abc", 0, 3));
            assertFalse(Preflight.isRegionMatches("abcdef", 1, "bcdef", 0, 5 + 1));
        }

        @Test
        @DisplayName("String 区间可忽略大小写")
        void stringRegionMatchesIgnoreCase() {
            assertTrue(Preflight.isRegionMatches(true, "HELLO", 1, "hello", 1, 3));
            assertFalse(Preflight.isRegionMatches(false, "HELLO", 1, "hello", 1, 3));
        }

        @Test
        @DisplayName("非 String 字符序列逐字符比较")
        void nonStringRegionMatches() {
            assertTrue(Preflight.isRegionMatches(new StringBuilder("abcdef"), 1, new StringBuilder("bcd"), 0, 3));
            assertTrue(Preflight.isRegionMatches(new StringBuilder("abcdef"), 0, "abc", 0, 3));
            assertFalse(Preflight.isRegionMatches(new StringBuilder("abcdef"), 0, "abd", 0, 3));
        }

        @Test
        @DisplayName("非 String 字符序列可忽略大小写")
        void nonStringRegionMatchesIgnoreCase() {
            assertTrue(Preflight.isRegionMatches(true, new StringBuilder("HELLO"), 1, new StringBuilder("hello"), 1, 3));
            assertFalse(Preflight.isRegionMatches(false, new StringBuilder("HELLO"), 1, new StringBuilder("hello"), 1, 3));
        }

        @Test
        @DisplayName("偏移越界返回 false")
        void offsetOutOfBounds() {
            assertFalse(Preflight.isRegionMatches("abc", 1, "abc", 0, 3));
            assertFalse(Preflight.isRegionMatches("abc", 0, "abc", 1, 3));
            assertFalse(Preflight.isRegionMatches(new StringBuilder("abc"), 1, new StringBuilder("abc"), 0, 3));
            assertFalse(Preflight.isRegionMatches(new StringBuilder("abc"), 0, new StringBuilder("abc"), 1, 3));
        }

        @Test
        @DisplayName("长度为 0 且偏移合法时返回 true")
        void zeroLengthWithLegalOffset() {
            assertTrue(Preflight.isRegionMatches("abc", 3, "xyz", 3, 0));
            assertTrue(Preflight.isRegionMatches(new StringBuilder("abc"), 3, new StringBuilder("xyz"), 3, 0));
        }

        @Test
        @DisplayName("任一参数为 null 或空序列返回 false")
        void nullOrEmptySequence() {
            assertFalse(Preflight.isRegionMatches(null, 0, "abc", 0, 3));
            assertFalse(Preflight.isRegionMatches("abc", 0, null, 0, 3));
            assertFalse(Preflight.isRegionMatches("", 0, "", 0, 0));
        }
    }

    @Nested
    @DisplayName("通配符匹配")
    class WildcardMatchTest {
        @Test
        @DisplayName("字面量完全一致时匹配")
        void exactMatch() {
            assertTrue(Preflight.isWildcardMatch("abc", "abc"));
            assertTrue(Preflight.isWildcardMatch("", ""));
            assertFalse(Preflight.isWildcardMatch("abc", "abd"));
        }

        @Test
        @DisplayName("星号匹配任意长度，包括 0 个字符")
        void starMatchesAnyLength() {
            assertTrue(Preflight.isWildcardMatch("abc", "*"));
            assertTrue(Preflight.isWildcardMatch("abc", "a*"));
            assertTrue(Preflight.isWildcardMatch("abc", "*c"));
            assertTrue(Preflight.isWildcardMatch("abc", "a*c"));
            assertTrue(Preflight.isWildcardMatch("aXbYc", "a*b*c"));
            assertTrue(Preflight.isWildcardMatch("aaab", "a*ab"));
            assertFalse(Preflight.isWildcardMatch("abc", "a*d"));
        }

        @Test
        @DisplayName("星号可匹配空序列")
        void starMatchesEmptySequence() {
            assertTrue(Preflight.isWildcardMatch("", "*"));
            assertTrue(Preflight.isWildcardMatch("abc", "a**c"));
        }

        @Test
        @DisplayName("问号恰好匹配一个字符")
        void questionMatchesSingleCharacter() {
            assertTrue(Preflight.isWildcardMatch("abc", "a?c"));
            assertFalse(Preflight.isWildcardMatch("ac", "a?c"));
            assertFalse(Preflight.isWildcardMatch("abbc", "a?c"));
        }

        @Test
        @DisplayName("问号按 code point 匹配增补字符")
        void questionMatchesSupplementaryCodePoint() {
            assertTrue(Preflight.isWildcardMatch("😀", "?"));
            assertTrue(Preflight.isWildcardMatch("😀x", "?x"));
            assertFalse(Preflight.isWildcardMatch("😀", "??"));
        }

        @Test
        @DisplayName("星号与问号可匹配增补字符")
        void starMatchesSupplementaryCodePoint() {
            assertTrue(Preflight.isWildcardMatch("a😀b", "a*b"));
            assertTrue(Preflight.isWildcardMatch("a😀b", "a?b"));
        }

        @Test
        @DisplayName("反斜杠可转义通配符")
        void backslashEscapesWildcard() {
            assertTrue(Preflight.isWildcardMatch("a*c", "a\\*c"));
            assertFalse(Preflight.isWildcardMatch("abc", "a\\*c"));
            assertTrue(Preflight.isWildcardMatch("a?c", "a\\?c"));
            assertFalse(Preflight.isWildcardMatch("abc", "a\\?c"));
        }

        @Test
        @DisplayName("反斜杠可转义自身，末尾反斜杠按字面处理")
        void backslashEscapesItselfAndTrailingBackslashIsLiteral() {
            assertTrue(Preflight.isWildcardMatch("a\\b", "a\\\\b"));
            assertTrue(Preflight.isWildcardMatch("abc\\", "abc\\"));
            assertFalse(Preflight.isWildcardMatch("abc", "abc\\"));
        }

        @Test
        @DisplayName("任一参数为 null 返回 false")
        void nullArguments() {
            assertFalse(Preflight.isWildcardMatch(null, "*"));
            assertFalse(Preflight.isWildcardMatch("abc", null));
        }
    }

    @Nested
    @DisplayName("兜底默认值")
    class DefaultValueTest {
        @Test
        @DisplayName("为空时返回默认值")
        void defaultIfEmpty() {
            assertEquals("fallback", Preflight.defaultIfEmpty(null, "fallback"));
            assertEquals("fallback", Preflight.defaultIfEmpty("", "fallback"));
            assertEquals("fallback", Preflight.defaultIfEmpty("  ", "fallback"));
            assertEquals("value", Preflight.defaultIfEmpty("value", "fallback"));
        }

        @Test
        @DisplayName("空数组返回默认值")
        void defaultIfEmptyArray() {
            int[] fallback = {1};
            assertSame(fallback, Preflight.defaultIfEmpty(new int[0], fallback));
            int[] value = {1};
            assertSame(value, Preflight.defaultIfEmpty(value, fallback));
        }

        @Test
        @DisplayName("集合不包含元素时返回默认值")
        void defaultIfNotContains() {
            List<String> collection = new ArrayList<>(List.of("a", "b"));
            List<String> fallback = new ArrayList<>(List.of("z"));
            assertSame(collection, Preflight.defaultIfNotContains(collection, "a", fallback));
            assertSame(fallback, Preflight.defaultIfNotContains(collection, "z", fallback));
        }

        @Test
        @DisplayName("集合未包含全部元素时返回默认值")
        void defaultIfNotContainsAll() {
            List<String> collection = new ArrayList<>(List.of("a", "b"));
            List<String> fallback = new ArrayList<>(List.of("z"));
            assertSame(collection, Preflight.defaultIfNotContainsAll(collection, List.of("a", "b"), fallback));
            assertSame(collection, Preflight.defaultIfNotContainsAll(collection, List.of(), fallback));
            assertSame(fallback, Preflight.defaultIfNotContainsAll(collection, List.of("a", "z"), fallback));
        }

        @Test
        @DisplayName("集合未包含任意元素时返回默认值")
        void defaultIfNotContainsAny() {
            List<String> collection = new ArrayList<>(List.of("a", "b"));
            List<String> fallback = new ArrayList<>(List.of("z"));
            assertSame(collection, Preflight.defaultIfNotContainsAny(collection, List.of("z", "b"), fallback));
            assertSame(fallback, Preflight.defaultIfNotContainsAny(collection, List.of("y", "z"), fallback));
        }

        @Test
        @DisplayName("不是自然数或正整数时返回默认值")
        void defaultIfNotNaturalOrPositive() {
            assertEquals(Integer.valueOf(2), Preflight.defaultIfNotNatural(2, -1));
            assertEquals(Integer.valueOf(0), Preflight.defaultIfNotNatural(-1, 0));
            assertEquals(Integer.valueOf(1), Preflight.defaultIfNotPositive(1, 0));
            assertEquals(Integer.valueOf(0), Preflight.defaultIfNotPositive(0, 0));
        }

        @Test
        @DisplayName("区间不相等时返回默认值")
        void defaultIfNotRegionMatches() {
            assertEquals("abc", Preflight.defaultIfNotRegionMatches("abc", 0, "abc", 0, 3, "fallback"));
            assertEquals("fallback", Preflight.defaultIfNotRegionMatches("abc", 0, "abd", 0, 3, "fallback"));
            assertEquals("ABC", Preflight.defaultIfNotRegionMatches(true, "ABC", 0, "abc", 0, 3, "fallback"));
            assertEquals("fallback", Preflight.defaultIfNotRegionMatches(false, "ABC", 0, "abc", 0, 3, "fallback"));
        }

        @Test
        @DisplayName("不匹配通配符时返回默认值")
        void defaultIfNotWildcardMatch() {
            assertEquals("abc", Preflight.defaultIfNotWildcardMatch("abc", "a*c", "fallback"));
            assertEquals("fallback", Preflight.defaultIfNotWildcardMatch("abc", "a*d", "fallback"));
        }

        @Test
        @DisplayName("超出区间时返回默认值")
        void defaultIfOutOfRange() {
            assertEquals(Integer.valueOf(5), Preflight.defaultIfOutOfRange(5, 1, 10, 0));
            assertEquals(Integer.valueOf(0), Preflight.defaultIfOutOfRange(11, 1, 10, 0));
            assertEquals(Integer.valueOf(0), Preflight.defaultIfOutOfRange(0, 1, 10, 0));
        }
    }

    @Nested
    @DisplayName("校验并抛出异常")
    class RequireTest {
        @Test
        @DisplayName("校验通过时返回原对象")
        void requireReturnsOriginalValue() {
            List<String> collection = new ArrayList<>(List.of("a", "b"));
            assertSame(collection, Preflight.requireContains(collection, "a", MESSAGE));
            assertSame(collection, Preflight.requireContainsAll(collection, List.of("a", "b"), MESSAGE));
            assertSame(collection, Preflight.requireContainsAny(collection, List.of("b", "z"), MESSAGE));
            assertEquals("value", Preflight.requireNotEmpty("value", MESSAGE));
            assertEquals("", Preflight.requireEmpty("", MESSAGE));
            assertEquals(Integer.valueOf(5), Preflight.requireInRange(5, 1, 10, MESSAGE));
            assertEquals(Integer.valueOf(0), Preflight.requireNatural(0, MESSAGE));
            assertEquals(Integer.valueOf(1), Preflight.requirePositive(1, MESSAGE));
            assertEquals("abc", Preflight.requireRegionMatches("abc", 0, "abc", 0, 3, MESSAGE));
            assertEquals("ABC", Preflight.requireRegionMatches(true, "ABC", 0, "abc", 0, 3, MESSAGE));
            assertEquals("abc", Preflight.requireWildcardMatch("abc", "a*c", MESSAGE));
        }

        @Test
        @DisplayName("校验失败时抛出 IllegalArgumentException 并携带异常信息")
        void requireThrowsWithMessage() {
            List<String> collection = new ArrayList<>(List.of("a", "b"));
            IllegalArgumentException exception;
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireContains(collection, "z", MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireContainsAll(collection, List.of("a", "z"), MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireContainsAny(collection, List.of("y", "z"), MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireNotEmpty(" ", MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireEmpty("value", MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireInRange(null, 1, 10, MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireInRange(11, 1, 10, MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireNatural(-1, MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requirePositive(0, MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireRegionMatches("abc", 0, "abd", 0, 3, MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireRegionMatches(false, "ABC", 0, "abc", 0, 3, MESSAGE));
            assertEquals("校验失败", exception.getMessage());
            exception = assertThrows(IllegalArgumentException.class, () -> Preflight.requireWildcardMatch("abc", "a*d", MESSAGE));
            assertEquals("校验失败", exception.getMessage());
        }

        @Test
        @DisplayName("校验通过时不调用异常信息提供者")
        void exceptionMessageIsLazy() {
            AtomicBoolean invoked = new AtomicBoolean();
            Supplier<String> message = () -> {
                invoked.set(true);
                return "校验失败";
            };
            Preflight.requireNotEmpty("value", message);
            Preflight.requireInRange(5, 1, 10, message);
            Preflight.requireWildcardMatch("abc", "a*c", message);
            assertFalse(invoked.get());
        }
    }
}

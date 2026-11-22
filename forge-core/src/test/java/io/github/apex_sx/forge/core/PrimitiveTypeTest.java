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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link PrimitiveType} 单元测试
 *
 * @author ApexSX
 */
@SuppressWarnings("all")
@DisplayName("PrimitiveType 基本类型枚举")
class PrimitiveTypeTest {
    @Nested
    @DisplayName("类型映射")
    class OfTest {
        @Test
        @DisplayName("原生类型映射到对应枚举常量")
        void primitiveTypesAreMapped() {
            assertSame(PrimitiveType.BOOLEAN, PrimitiveType.of(boolean.class));
            assertSame(PrimitiveType.BYTE, PrimitiveType.of(byte.class));
            assertSame(PrimitiveType.SHORT, PrimitiveType.of(short.class));
            assertSame(PrimitiveType.CHAR, PrimitiveType.of(char.class));
            assertSame(PrimitiveType.INT, PrimitiveType.of(int.class));
            assertSame(PrimitiveType.LONG, PrimitiveType.of(long.class));
            assertSame(PrimitiveType.FLOAT, PrimitiveType.of(float.class));
            assertSame(PrimitiveType.DOUBLE, PrimitiveType.of(double.class));
        }

        @Test
        @DisplayName("包装类型映射到对应枚举常量")
        void wrapperTypesAreMapped() {
            assertSame(PrimitiveType.BOOLEAN, PrimitiveType.of(Boolean.class));
            assertSame(PrimitiveType.BYTE, PrimitiveType.of(Byte.class));
            assertSame(PrimitiveType.SHORT, PrimitiveType.of(Short.class));
            assertSame(PrimitiveType.CHAR, PrimitiveType.of(Character.class));
            assertSame(PrimitiveType.INT, PrimitiveType.of(Integer.class));
            assertSame(PrimitiveType.LONG, PrimitiveType.of(Long.class));
            assertSame(PrimitiveType.FLOAT, PrimitiveType.of(Float.class));
            assertSame(PrimitiveType.DOUBLE, PrimitiveType.of(Double.class));
        }

        @Test
        @DisplayName("非基本类型与 null 返回 null")
        void otherTypesReturnNull() {
            assertNull(PrimitiveType.of(null));
            assertNull(PrimitiveType.of(String.class));
            assertNull(PrimitiveType.of(Object.class));
            assertNull(PrimitiveType.of(void.class));
            assertNull(PrimitiveType.of(Number.class));
        }

        @Test
        @DisplayName("八种基本类型及其包装类型均可识别")
        void basicTypesAreRecognized() {
            assertEquals(8, PrimitiveType.values().length);
            assertTrue(PrimitiveType.isBasicType(int.class));
            assertTrue(PrimitiveType.isBasicType(Integer.class));
            assertFalse(PrimitiveType.isBasicType(String.class));
            assertFalse(PrimitiveType.isBasicType(null));
        }
    }

    @Nested
    @DisplayName("原生类型与包装类型判断")
    class TypeCategoryTest {
        @Test
        @DisplayName("isPrimitive 只认可原生类型")
        void onlyPrimitiveReturnsTrue() {
            assertTrue(PrimitiveType.isPrimitive(int.class));
            assertFalse(PrimitiveType.isPrimitive(Integer.class));
            assertFalse(PrimitiveType.isPrimitive(String.class));
            assertFalse(PrimitiveType.isPrimitive(null));
        }

        @Test
        @DisplayName("isWrapper 只认可包装类型")
        void onlyWrapperReturnsTrue() {
            assertTrue(PrimitiveType.isWrapper(Integer.class));
            assertFalse(PrimitiveType.isWrapper(int.class));
            assertFalse(PrimitiveType.isWrapper(String.class));
            assertFalse(PrimitiveType.isWrapper(null));
        }

        @Test
        @DisplayName("八组原生类型与包装类型互斥")
        void primitiveAndWrapperAreMutuallyExclusive() {
            for (PrimitiveType type : PrimitiveType.values()) {
                assertTrue(PrimitiveType.isPrimitive(type.primitiveType()));
                assertTrue(PrimitiveType.isWrapper(type.wrapperType()));
                assertFalse(PrimitiveType.isPrimitive(type.wrapperType()));
                assertFalse(PrimitiveType.isWrapper(type.primitiveType()));
            }
        }
    }

    @Nested
    @DisplayName("自动转换")
    class AutoConvertTest {
        @Test
        @DisplayName("同一基本类型视为可赋值")
        void sameBasicTypeIsConvertible() {
            assertTrue(PrimitiveType.canAutoConvert(int.class, int.class));
            assertTrue(PrimitiveType.canAutoConvert(char.class, char.class));
            assertTrue(PrimitiveType.canAutoConvert(boolean.class, boolean.class));
            assertTrue(PrimitiveType.canAutoConvert(Integer.class, Integer.class));
        }

        @Test
        @DisplayName("拓宽转换可以自动赋值")
        void wideningConversionsAreAllowed() {
            assertTrue(PrimitiveType.canAutoConvert(byte.class, short.class));
            assertTrue(PrimitiveType.canAutoConvert(byte.class, int.class));
            assertTrue(PrimitiveType.canAutoConvert(byte.class, long.class));
            assertTrue(PrimitiveType.canAutoConvert(byte.class, float.class));
            assertTrue(PrimitiveType.canAutoConvert(byte.class, double.class));
            assertTrue(PrimitiveType.canAutoConvert(short.class, int.class));
            assertTrue(PrimitiveType.canAutoConvert(int.class, long.class));
            assertTrue(PrimitiveType.canAutoConvert(int.class, float.class));
            assertTrue(PrimitiveType.canAutoConvert(int.class, double.class));
            assertTrue(PrimitiveType.canAutoConvert(long.class, float.class));
            assertTrue(PrimitiveType.canAutoConvert(long.class, double.class));
            assertTrue(PrimitiveType.canAutoConvert(float.class, double.class));
        }

        @Test
        @DisplayName("char 只能自动赋给 int 及更宽的类型")
        void charOnlyWidensToIntOrWider() {
            assertTrue(PrimitiveType.canAutoConvert(char.class, int.class));
            assertTrue(PrimitiveType.canAutoConvert(char.class, long.class));
            assertTrue(PrimitiveType.canAutoConvert(char.class, float.class));
            assertTrue(PrimitiveType.canAutoConvert(char.class, double.class));
            assertFalse(PrimitiveType.canAutoConvert(char.class, short.class));
            assertFalse(PrimitiveType.canAutoConvert(char.class, byte.class));
        }

        @Test
        @DisplayName("窄化转换不能自动赋值")
        void narrowingConversionsAreRejected() {
            assertFalse(PrimitiveType.canAutoConvert(int.class, byte.class));
            assertFalse(PrimitiveType.canAutoConvert(int.class, short.class));
            assertFalse(PrimitiveType.canAutoConvert(long.class, int.class));
            assertFalse(PrimitiveType.canAutoConvert(float.class, long.class));
            assertFalse(PrimitiveType.canAutoConvert(double.class, float.class));
            assertFalse(PrimitiveType.canAutoConvert(short.class, char.class));
            assertFalse(PrimitiveType.canAutoConvert(short.class, byte.class));
        }

        @Test
        @DisplayName("boolean 不参与拓宽转换")
        void booleanDoesNotParticipateInWidening() {
            for (PrimitiveType type : PrimitiveType.values()) {
                if (type == PrimitiveType.BOOLEAN) {
                    continue;
                }
                assertFalse(PrimitiveType.canAutoConvert(boolean.class, type.primitiveType()));
                assertFalse(PrimitiveType.canAutoConvert(type.primitiveType(), boolean.class));
            }
        }

        @Test
        @DisplayName("包装类型不参与自动转换")
        void wrapperTypesAreExcluded() {
            assertFalse(PrimitiveType.canAutoConvert(byte.class, Integer.class));
            assertFalse(PrimitiveType.canAutoConvert(Byte.class, int.class));
            assertFalse(PrimitiveType.canAutoConvert(Byte.class, Integer.class));
            assertFalse(PrimitiveType.canAutoConvert(Integer.class, long.class));
            assertFalse(PrimitiveType.canAutoConvert(int.class, Long.class));
            assertFalse(PrimitiveType.canAutoConvert(Character.class, int.class));
        }

        @Test
        @DisplayName("非基本类型与 null 一律返回 false")
        void nonBasicOrNullIsRejected() {
            assertFalse(PrimitiveType.canAutoConvert(String.class, Object.class));
            assertFalse(PrimitiveType.canAutoConvert(int.class, Number.class));
            assertFalse(PrimitiveType.canAutoConvert(Number.class, int.class));
            assertFalse(PrimitiveType.canAutoConvert(null, int.class));
            assertFalse(PrimitiveType.canAutoConvert(int.class, null));
            assertFalse(PrimitiveType.canAutoConvert(null, null));
        }
    }

    @Nested
    @DisplayName("实例转换判断")
    class InstanceConvertTest {
        @Test
        @DisplayName("canConvertTo 与 canAcceptFrom 方向相反")
        void convertToAndAcceptFromAreOpposite() {
            assertTrue(PrimitiveType.BYTE.canConvertTo(int.class));
            assertFalse(PrimitiveType.INT.canConvertTo(byte.class));
            assertTrue(PrimitiveType.INT.canAcceptFrom(byte.class));
            assertFalse(PrimitiveType.BYTE.canAcceptFrom(int.class));
        }

        @Test
        @DisplayName("自身类型双向可转换")
        void selfTypeIsConvertibleBothWays() {
            for (PrimitiveType type : PrimitiveType.values()) {
                assertTrue(type.canConvertTo(type.primitiveType()));
                assertTrue(type.canAcceptFrom(type.primitiveType()));
            }
        }

        @Test
        @DisplayName("目标或来源为包装类型时返回 false")
        void wrapperArgumentReturnsFalse() {
            assertFalse(PrimitiveType.INT.canConvertTo(Integer.class));
            assertFalse(PrimitiveType.INT.canAcceptFrom(Integer.class));
            assertFalse(PrimitiveType.INT.canConvertTo(null));
            assertFalse(PrimitiveType.INT.canAcceptFrom(null));
        }
    }

    @Nested
    @DisplayName("类型元信息")
    class MetaDataTest {
        @Test
        @DisplayName("原生类型与包装类型互相匹配")
        void primitiveAndWrapperArePaired() {
            assertEquals(int.class, PrimitiveType.INT.primitiveType());
            assertEquals(Integer.class, PrimitiveType.INT.wrapperType());
            assertEquals(char.class, PrimitiveType.CHAR.primitiveType());
            assertEquals(Character.class, PrimitiveType.CHAR.wrapperType());
            assertEquals(boolean.class, PrimitiveType.BOOLEAN.primitiveType());
            assertEquals(Boolean.class, PrimitiveType.BOOLEAN.wrapperType());
        }

        @Test
        @DisplayName("可接受来源集合包含自身且只含原生类型")
        void compatibleTypesContainSelfOnlyPrimitives() {
            Set<Class<?>> compatible = PrimitiveType.INT.compatiblePrimitiveTypes();
            assertEquals(Set.of(byte.class, short.class, char.class, int.class), compatible);
            assertTrue(PrimitiveType.DOUBLE.compatiblePrimitiveTypes().contains(long.class));
            assertTrue(PrimitiveType.DOUBLE.compatiblePrimitiveTypes().contains(double.class));
            for (PrimitiveType type : PrimitiveType.values()) {
                Set<Class<?>> sources = type.compatiblePrimitiveTypes();
                assertTrue(sources.contains(type.primitiveType()));
                for (Class<?> source : sources) {
                    assertTrue(PrimitiveType.isPrimitive(source));
                }
            }
        }

        @Test
        @DisplayName("可接受来源集合不可修改")
        void compatibleTypesAreImmutable() {
            assertThrows(UnsupportedOperationException.class,
                    () -> PrimitiveType.INT.compatiblePrimitiveTypes().add(long.class));
        }

        @Test
        @DisplayName("宽度序由窄到宽递增")
        void rankIncreasesByWidth() {
            assertEquals(0, PrimitiveType.BOOLEAN.rank());
            assertEquals(1, PrimitiveType.BYTE.rank());
            assertEquals(2, PrimitiveType.SHORT.rank());
            assertEquals(2, PrimitiveType.CHAR.rank());
            assertEquals(3, PrimitiveType.INT.rank());
            assertEquals(4, PrimitiveType.LONG.rank());
            assertEquals(5, PrimitiveType.FLOAT.rank());
            assertEquals(6, PrimitiveType.DOUBLE.rank());
            assertTrue(PrimitiveType.BYTE.rank() < PrimitiveType.SHORT.rank());
            assertTrue(PrimitiveType.INT.rank() < PrimitiveType.LONG.rank());
            assertTrue(PrimitiveType.FLOAT.rank() < PrimitiveType.DOUBLE.rank());
        }

        @Test
        @DisplayName("字节数与 JVM 规范一致")
        void byteSizeMatchesSpecification() {
            assertEquals(1, PrimitiveType.BOOLEAN.byteSize());
            assertEquals(1, PrimitiveType.BYTE.byteSize());
            assertEquals(2, PrimitiveType.SHORT.byteSize());
            assertEquals(2, PrimitiveType.CHAR.byteSize());
            assertEquals(4, PrimitiveType.INT.byteSize());
            assertEquals(8, PrimitiveType.LONG.byteSize());
            assertEquals(4, PrimitiveType.FLOAT.byteSize());
            assertEquals(8, PrimitiveType.DOUBLE.byteSize());
        }
    }

    @Nested
    @DisplayName("严格转换器")
    class StrictConverterTest {
        @Test
        @DisplayName("数值类型按字面量解析")
        void numericValuesAreParsed() {
            assertEquals(Byte.valueOf((byte) 1), PrimitiveType.BYTE.strictConverter().apply("1"));
            assertEquals(Short.valueOf((short) 2), PrimitiveType.SHORT.strictConverter().apply("2"));
            assertEquals(Integer.valueOf(3), PrimitiveType.INT.strictConverter().apply("3"));
            assertEquals(Long.valueOf(4L), PrimitiveType.LONG.strictConverter().apply("4"));
            assertEquals(Float.valueOf(5.5F), PrimitiveType.FLOAT.strictConverter().apply("5.5"));
            assertEquals(Double.valueOf(6.5D), PrimitiveType.DOUBLE.strictConverter().apply("6.5"));
        }

        @Test
        @DisplayName("字符类型取首字符，null 与空字符串返回 '\\0'")
        void characterTakesFirstChar() {
            assertEquals(Character.valueOf('a'), PrimitiveType.CHAR.strictConverter().apply("abc"));
            assertEquals(Character.valueOf('\0'), PrimitiveType.CHAR.strictConverter().apply(""));
            assertEquals(Character.valueOf('\0'), PrimitiveType.CHAR.strictConverter().apply(null));
        }

        @Test
        @DisplayName("布尔类型只识别忽略大小写的 true")
        void booleanOnlyRecognizesTrue() {
            assertEquals(Boolean.TRUE, PrimitiveType.BOOLEAN.strictConverter().apply("true"));
            assertEquals(Boolean.TRUE, PrimitiveType.BOOLEAN.strictConverter().apply("TRUE"));
            assertEquals(Boolean.FALSE, PrimitiveType.BOOLEAN.strictConverter().apply("false"));
            assertEquals(Boolean.FALSE, PrimitiveType.BOOLEAN.strictConverter().apply("1"));
            assertEquals(Boolean.FALSE, PrimitiveType.BOOLEAN.strictConverter().apply(" true "));
            assertEquals(Boolean.FALSE, PrimitiveType.BOOLEAN.strictConverter().apply(null));
        }

        @Test
        @DisplayName("数值解析失败时抛出 NumberFormatException")
        void invalidNumberThrows() {
            assertThrows(NumberFormatException.class, () -> PrimitiveType.BYTE.strictConverter().apply("128"));
            assertThrows(NumberFormatException.class, () -> PrimitiveType.SHORT.strictConverter().apply("1.5"));
            assertThrows(NumberFormatException.class, () -> PrimitiveType.INT.strictConverter().apply("abc"));
            assertThrows(NumberFormatException.class, () -> PrimitiveType.LONG.strictConverter().apply(""));
            assertThrows(NumberFormatException.class, () -> PrimitiveType.DOUBLE.strictConverter().apply("abc"));
        }
    }
}

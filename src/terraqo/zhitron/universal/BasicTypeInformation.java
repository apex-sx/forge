package com.github.zhitron.universal;

import terraqo.arbiter.Constant;

import java.util.Set;
import java.util.function.Function;

/**
 * 基本类型信息类
 * 用于存储Java基本类型及其相关信息，包括包装类型、兼容类型、类型级别、字节大小和转换器
 *
 * @author zhitron
 */
public final class BasicTypeInformation implements Comparable<BasicTypeInformation> {
    /**
     * 基本类型，如int.class, long.class等
     */
    public final Class<?> primitiveType;

    /**
     * 基本类型兼容类型集合
     * 包含该基本类型可以兼容的其他类型
     */
    public final Set<Class<?>> primitiveCompatibleTypes;

    /**
     * 包装类型，如Integer.class, Long.class等
     */
    public final Class<?> wrapperType;

    /**
     * 包装类型兼容类型集合
     * 包含该包装类型可以兼容的其他类型，通常包含其对应的基本类型和自身
     */
    public final Set<Class<?>> wrapperCompatibleTypes;

    /**
     * 类型级别，用于表示类型的优先级或范围大小
     * 级别越小表示范围越窄，级别越大表示范围越宽
     */
    public final int level;

    /**
     * 字节数，表示该类型占用的内存大小
     */
    public final int byteSize;

    /**
     * 严格转换器，用于将字符串严格转换为该类型的对象
     */
    public final Function<String, Object> strictConverter;

    /*
     * 基本类型信息常量
     */
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_DOUBLE = new BasicTypeInformation(double.class, Double.class, 1, 8, Double::parseDouble, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class, double.class, Double.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_FLOAT = new BasicTypeInformation(float.class, Float.class, 2, 4, Float::parseFloat, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class, float.class, Float.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_LONG = new BasicTypeInformation(long.class, Long.class, 3, 8, Long::parseLong, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class, long.class, Long.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_INTEGER = new BasicTypeInformation(int.class, Integer.class, 4, 4, Integer::parseInt, byte.class, Byte.class, short.class, Short.class, int.class, Integer.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_SHORT = new BasicTypeInformation(short.class, Short.class, 5, 2, Short::parseShort, byte.class, Byte.class, short.class, Short.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_BYTE = new BasicTypeInformation(byte.class, Byte.class, 6, 1, Byte::parseByte, byte.class, Byte.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_CHARACTER = new BasicTypeInformation(char.class, Character.class, 0, 2, s -> s.isEmpty() ? '\0' : s.charAt(0), char.class, Character.class);
    public static final BasicTypeInformation BASIC_TYPE_INFORMATION_BOOLEAN = new BasicTypeInformation(boolean.class, Boolean.class, 0, 1, Boolean::parseBoolean, boolean.class, Boolean.class);

    /**
     * 构造函数
     *
     * @param primitiveType            基本类型
     * @param wrapperType              包装类型
     * @param level                    类型级别
     * @param byteSize                 字节数
     * @param strictConverter          严格转换器
     * @param primitiveCompatibleTypes 基本类型兼容类型
     */
    BasicTypeInformation(Class<?> primitiveType, Class<?> wrapperType, int level, int byteSize, Function<String, Object> strictConverter, Class<?>... primitiveCompatibleTypes) {
        this.primitiveType = primitiveType;
        this.primitiveCompatibleTypes = Constant.asHashSet(true, primitiveCompatibleTypes);
        this.wrapperType = wrapperType;
        this.wrapperCompatibleTypes = Constant.asHashSet(true, primitiveType, wrapperType);
        this.level = level;
        this.byteSize = byteSize;
        this.strictConverter = strictConverter;
    }

    /**
     * 比较类型级别
     * 根据level字段比较两个BasicTypeInformation对象的优先级
     *
     * @param o 另一个BasicTypeInformation实例
     * @return 比较结果，负数表示当前对象级别更低，0表示级别相同，正数表示当前对象级别更高
     */
    @Override
    public int compareTo(BasicTypeInformation o) {
        return Integer.compare(level, o.level);
    }
}

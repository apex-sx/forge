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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Java基本类型枚举
 * <p>
 * 描述Java八种基本类型及其包装类型，回答两类问题：
 * - 类型归属：{@link #isBasicType(Class)}、{@link #isPrimitive(Class)}、{@link #isWrapper(Class)}
 * - 自动转换：{@link #canAutoConvert(Class, Class)} 判断源类型能否自动赋值给目标类型
 * <p>
 * 自动转换只认可JLS 5.1.2定义的拓宽原生类型转换，即大范围兼容小范围：
 * {@code byte → short → int → long → float → double}，
 * {@code char → int → long → float → double}。
 * 例如{@code byte}、{@code short}可以自动赋给{@code int}，{@code int}不能自动赋给{@code byte}，
 * {@code byte}也不能自动赋给{@code char}。
 * <p>
 * 包装类型不参与自动转换：{@code byte}可以赋给{@code int}，但不能赋给{@code Integer}；
 * {@code Byte}也不能赋给{@code int}或{@code Integer}。装箱、拆箱以及拆箱后再拓宽都不属于自动转换，
 * 只有源类型与目标类型完全相同时才视为可赋值。
 * <p>
 * 不覆盖的场景：显式强制转换（窄化）、常量表达式窄化（如{@code char c = 65}）、
 * 原生类型与包装类型之间的装箱拆箱，以及引用类型之间的转换。
 * {@code int → float}、{@code long → float}与{@code long → double}虽然属于拓宽转换，但可能丢失精度。
 * <p>
 * 枚举常量按{@link #rank()}由窄到宽声明，自然顺序即可用于排序；
 * 但{@code char}与{@code short}宽度相同且互不兼容，能否转换必须依据
 * {@link #canConvertTo(Class)}或{@link #canAutoConvert(Class, Class)}判断。
 *
 * @author ApexSX
 */
public enum PrimitiveType {
    /**
     * 布尔类型，不参与拓宽转换，只能兼容自身
     */
    BOOLEAN(boolean.class, Boolean.class, 0, 1, Boolean::parseBoolean, boolean.class),
    /**
     * 字节类型
     */
    BYTE(byte.class, Byte.class, 1, 1, Byte::parseByte, byte.class),
    /**
     * 短整型
     */
    SHORT(short.class, Short.class, 2, 2, Short::parseShort, byte.class, short.class),
    /**
     * 字符类型，只能自动赋给{@code int}及更宽的类型，不能接收{@code byte}与{@code short}
     */
    CHAR(char.class, Character.class, 2, 2, PrimitiveType::toCharacter, char.class),
    /**
     * 整型
     */
    INT(int.class, Integer.class, 3, 4, Integer::parseInt, byte.class, short.class, char.class, int.class),
    /**
     * 长整型
     */
    LONG(long.class, Long.class, 4, 8, Long::parseLong, byte.class, short.class, char.class, int.class, long.class),
    /**
     * 单精度浮点型，可接收{@code long}，超出2的24次方的整数会丢失精度
     */
    FLOAT(float.class, Float.class, 5, 4, Float::parseFloat, byte.class, short.class, char.class, int.class, long.class, float.class),
    /**
     * 双精度浮点型，可接收{@code long}，超出2的53次方的整数会丢失精度
     */
    DOUBLE(double.class, Double.class, 6, 8, Double::parseDouble, byte.class, short.class, char.class, int.class, long.class, float.class, double.class);
    /**
     * 类型映射表，同时以原生类型和包装类型为键
     */
    private static final Map<Class<?>, PrimitiveType> TYPES = PrimitiveType.buildTypes();

    /**
     * 判断源类型能否自动赋值给目标类型
     * <p>
     * 只有原生类型之间存在自动转换，且仅限拓宽方向；包装类型不参与，源类型与目标类型完全相同时视为可赋值。
     * 因此{@code canAutoConvert(byte.class, int.class)}为true，
     * 而{@code canAutoConvert(byte.class, Integer.class)}、{@code canAutoConvert(Byte.class, int.class)}、
     * {@code canAutoConvert(int.class, byte.class)}均为false。
     *
     * @param source 源类型
     * @param target 目标类型
     * @return 可以自动赋值返回true，任一参数为{@code null}或不存在自动转换返回false
     */
    public static boolean canAutoConvert(Class<?> source, Class<?> target) {
        if (source == target) {
            return PrimitiveType.isBasicType(source);
        }
        PrimitiveType sourceType = PrimitiveType.of(source);
        PrimitiveType targetType = PrimitiveType.of(target);
        if (sourceType == null || targetType == null) {
            return false;
        }
        if (sourceType.primitiveType != source || targetType.primitiveType != target) {
            return false;
        }
        return targetType.compatiblePrimitiveTypes.contains(sourceType.primitiveType);
    }

    /**
     * 判断类型是否为基本类型
     * <p>
     * 基本类型指八种原生类型及其包装类型。
     *
     * @param type 待检查类型
     * @return 是原生类型或包装类型返回true，否则返回false
     */
    public static boolean isBasicType(Class<?> type) {
        return PrimitiveType.of(type) != null;
    }

    /**
     * 判断类型是否为原生类型
     * <p>
     * 只有八种原生类型返回true，包装类型返回false。
     *
     * @param type 待检查类型
     * @return 是原生类型返回true，否则返回false
     */
    public static boolean isPrimitive(Class<?> type) {
        PrimitiveType entry = PrimitiveType.of(type);
        return entry != null && entry.primitiveType == type;
    }

    /**
     * 判断类型是否为包装类型
     * <p>
     * 只有八种包装类型返回true，原生类型返回false。
     *
     * @param type 待检查类型
     * @return 是包装类型返回true，否则返回false
     */
    public static boolean isWrapper(Class<?> type) {
        PrimitiveType entry = PrimitiveType.of(type);
        return entry != null && entry.wrapperType == type;
    }

    /**
     * 获取类型对应的基本类型
     *
     * @param type 原生类型或其包装类型
     * @return 对应的枚举常量；参数为{@code null}或不是基本类型及其包装类型时返回{@code null}
     */
    public static PrimitiveType of(Class<?> type) {
        return type == null ? null : PrimitiveType.TYPES.get(type);
    }

    /**
     * 构建类型映射表，同时以原生类型和包装类型为键
     *
     * @return 以原生类型和包装类型为键的不可变映射表
     */
    private static Map<Class<?>, PrimitiveType> buildTypes() {
        PrimitiveType[] entries = PrimitiveType.values();
        Map<Class<?>, PrimitiveType> types = new HashMap<>(entries.length * 2);
        for (PrimitiveType entry : entries) {
            types.put(entry.primitiveType, entry);
            types.put(entry.wrapperType, entry);
        }
        return Map.copyOf(types);
    }

    /**
     * 严格的字符转换，空字符串无法表示字符，返回{@code '\0'}而不是截取首位
     *
     * @param value 待转换的字符串
     * @return 首个字符，参数为{@code null}或空字符串时返回{@code '\0'}
     */
    private static Character toCharacter(String value) {
        if (value == null || value.isEmpty()) {
            return '\0';
        }
        return value.charAt(0);
    }

    /**
     * 当前枚举常量对应的原生类型，如 {@code int.class}
     */
    private final Class<?> primitiveType;
    /**
     * 当前枚举常量对应的包装类型，如 {@code Integer.class}
     */
    private final Class<?> wrapperType;
    /**
     * 可自动赋值给当前原生类型的来源原生类型集合，包含自身且不包含包装类型
     */
    private final Set<Class<?>> compatiblePrimitiveTypes;
    /**
     * 类型宽度序，数值越大表示范围越宽，{@code char} 与 {@code short} 同序但不互容
     */
    private final int rank;
    /**
     * 当前原生类型占用的字节数，{@code boolean} 按 1 字节记录
     */
    private final int byteSize;
    /**
     * 字符串到包装对象的严格转换器，数值类型解析失败时抛出 {@link NumberFormatException}
     */
    private final Function<String, Object> strictConverter;

    /**
     * 构造函数
     *
     * @param primitiveType            原生类型
     * @param wrapperType              包装类型
     * @param rank                     类型宽度序
     * @param byteSize                 字节数
     * @param strictConverter          严格转换器
     * @param compatiblePrimitiveTypes 可自动赋值给该原生类型的来源原生类型，需包含自身
     */
    PrimitiveType(Class<?> primitiveType, Class<?> wrapperType, int rank, int byteSize, Function<String, Object> strictConverter, Class<?>... compatiblePrimitiveTypes) {
        this.primitiveType = primitiveType;
        this.wrapperType = wrapperType;
        this.compatiblePrimitiveTypes = Set.of(compatiblePrimitiveTypes);
        this.rank = rank;
        this.byteSize = byteSize;
        this.strictConverter = strictConverter;
    }

    /**
     * 获取原生类型
     *
     * @return 原生类型，如{@code int.class}
     */
    public Class<?> primitiveType() {
        return primitiveType;
    }

    /**
     * 获取包装类型
     *
     * @return 包装类型，如{@code Integer.class}
     */
    public Class<?> wrapperType() {
        return wrapperType;
    }

    /**
     * 获取可自动赋值给该原生类型的来源原生类型集合
     * <p>
     * 仅包含原生类型且包含自身，例如{@link #INT}对应{@code byte}、{@code short}、{@code char}与{@code int}。
     * 包装类型不在其中，装箱与拆箱都不属于自动转换。
     *
     * @return 不可变的来源原生类型集合
     */
    public Set<Class<?>> compatiblePrimitiveTypes() {
        return compatiblePrimitiveTypes;
    }

    /**
     * 获取类型宽度序
     * <p>
     * 数值越大表示范围越宽，仅作为同一拓宽链上的排序参考：
     * {@link #CHAR}与{@link #SHORT}宽度相同，但两者互不兼容；
     * {@link #BOOLEAN}不参与拓宽，宽度序取0。
     *
     * @return 类型宽度序
     */
    public int rank() {
        return rank;
    }

    /**
     * 获取字节数
     *
     * @return 该类型占用的内存大小，{@link #BOOLEAN}按1字节记录
     */
    public int byteSize() {
        return byteSize;
    }

    /**
     * 获取严格转换器，用于将字符串严格转换为该类型的对象
     * <p>
     * 数值类型转换失败时抛出{@link NumberFormatException}，{@link #CHAR}对{@code null}
     * 或空字符串返回{@code '\0'}，{@link #BOOLEAN}仅识别忽略大小写的{@code true}，
     * 其余内容一律为{@code false}。
     *
     * @return 严格转换器
     */
    public Function<String, Object> strictConverter() {
        return strictConverter;
    }

    /**
     * 判断当前原生类型能否自动赋值给目标类型
     * <p>
     * 判定规则与{@link #canAutoConvert(Class, Class)}一致，目标类型为包装类型时返回false。
     *
     * @param target 目标类型
     * @return 可以自动赋值返回true，否则返回false
     */
    public boolean canConvertTo(Class<?> target) {
        return PrimitiveType.canAutoConvert(primitiveType, target);
    }

    /**
     * 判断当前原生类型能否自动接收来源类型
     * <p>
     * 适用于为形参选择可接受的实参类型，来源类型为包装类型时返回false。
     *
     * @param source 来源类型
     * @return 可以自动接收返回true，否则返回false
     */
    public boolean canAcceptFrom(Class<?> source) {
        return PrimitiveType.canAutoConvert(source, primitiveType);
    }
}

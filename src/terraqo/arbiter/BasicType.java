
/*
 * Copyright (c) 2025-2026.
 */

package terraqo.arbiter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * 基本类型信息类
 * 用于存储Java基本类型及其相关信息，包括包装类型、兼容类型、类型级别、字节大小和转换器
 *
 * @param primitiveType         基本类型，如int.class, long.class等
 * @param wrapperType           包装类型，如Integer.class, Long.class等
 * @param supportStoreByteData  是否支持存储字节数据
 * @param numberCompatibleOrder 数字兼容顺序，用于表示类型的优先级或范围大小
 *                              级别越小表示范围越窄，级别越大表示范围越宽
 *                              对于非数字类型，该值为-1
 * @param byteSize              字节数，表示该类型占用的内存大小
 * @param defaultValue          该类型的默认值
 * @param strictConverter       严格转换器，用于将字符串严格转换为该类型的对象
 * @author Terraqo
 */
@SuppressWarnings("unchecked")
public record BasicType(Class<?> primitiveType, Class<?> wrapperType, boolean supportStoreByteData, int numberCompatibleOrder, int byteSize, Object defaultValue,
                        Function<String, Object> strictConverter) implements Comparable<BasicType> {

    // 预定义的基本类型常量
    public static final BasicType BASIC_TYPE_DOUBLE = new BasicType(double.class, Double.class, false, 0, 8, 0D, Double::parseDouble);
    public static final BasicType BASIC_TYPE_FLOAT = new BasicType(float.class, Float.class, false, 1, 4, 0F, Float::parseFloat);
    public static final BasicType BASIC_TYPE_LONG = new BasicType(long.class, Long.class, true, 2, 8, 0L, Long::parseLong);
    public static final BasicType BASIC_TYPE_INTEGER = new BasicType(int.class, Integer.class, true, 3, 4, 0, Integer::parseInt);
    public static final BasicType BASIC_TYPE_SHORT = new BasicType(short.class, Short.class, true, 4, 2, (short) 0, Short::parseShort);
    public static final BasicType BASIC_TYPE_BYTE = new BasicType(byte.class, Byte.class, true, 5, 1, (byte) 0, Byte::parseByte);
    public static final BasicType BASIC_TYPE_CHARACTER = new BasicType(char.class, Character.class, false, -1, 2, '\0', s -> s.isEmpty() ? '\0' : s.charAt(0));
    public static final BasicType BASIC_TYPE_BOOLEAN = new BasicType(boolean.class, Boolean.class, false, -1, 1, false, Boolean::parseBoolean);

    /**
     * 基本类型信息映射表
     * 将基本类型和包装类型映射到对应的BasicType实例
     */
    public static final Map<Class<?>, BasicType> BASIC_TYPE_MAP = Map.ofEntries(
            Map.entry(BASIC_TYPE_DOUBLE.primitiveType, BASIC_TYPE_DOUBLE),
            Map.entry(BASIC_TYPE_DOUBLE.wrapperType, BASIC_TYPE_DOUBLE),
            Map.entry(BASIC_TYPE_FLOAT.primitiveType, BASIC_TYPE_FLOAT),
            Map.entry(BASIC_TYPE_FLOAT.wrapperType, BASIC_TYPE_FLOAT),
            Map.entry(BASIC_TYPE_LONG.primitiveType, BASIC_TYPE_LONG),
            Map.entry(BASIC_TYPE_LONG.wrapperType, BASIC_TYPE_LONG),
            Map.entry(BASIC_TYPE_INTEGER.primitiveType, BASIC_TYPE_INTEGER),
            Map.entry(BASIC_TYPE_INTEGER.wrapperType, BASIC_TYPE_INTEGER),
            Map.entry(BASIC_TYPE_SHORT.primitiveType, BASIC_TYPE_SHORT),
            Map.entry(BASIC_TYPE_SHORT.wrapperType, BASIC_TYPE_SHORT),
            Map.entry(BASIC_TYPE_BYTE.primitiveType, BASIC_TYPE_BYTE),
            Map.entry(BASIC_TYPE_BYTE.wrapperType, BASIC_TYPE_BYTE),
            Map.entry(BASIC_TYPE_CHARACTER.primitiveType, BASIC_TYPE_CHARACTER),
            Map.entry(BASIC_TYPE_CHARACTER.wrapperType, BASIC_TYPE_CHARACTER),
            Map.entry(BASIC_TYPE_BOOLEAN.primitiveType, BASIC_TYPE_BOOLEAN),
            Map.entry(BASIC_TYPE_BOOLEAN.wrapperType, BASIC_TYPE_BOOLEAN)
    );

    /**
     * 是否包括基本类型信息
     *
     * @param input 输入类型
     * @return 如果有返回true，否则返回false
     */
    public static boolean hasBasicType(Class<?> input) {
        return BASIC_TYPE_MAP.containsKey(input);
    }

    /**
     * 是否为包装类型
     *
     * @param input 类
     * @return 是否为包装类型
     */
    public static boolean isWrapperType(Class<?> input) {
        BasicType basicType = BASIC_TYPE_MAP.get(input);
        return input != null && !input.isPrimitive() && basicType != null;
    }

    /**
     * 是否为原生类型
     *
     * @param input 类
     * @return 是否为原生类型
     */
    public static boolean isPrimitiveType(Class<?> input) {
        BasicType basicType = BASIC_TYPE_MAP.get(input);
        return input != null && input.isPrimitive() && basicType != null;
    }

    /**
     * 是否可以存储字节数据，如byte,short,int,long
     *
     * @param input 类
     * @return 如果是返回true，否则返回false
     */
    public static boolean isStoreByteData(Class<?> input) {
        BasicType basicType = BASIC_TYPE_MAP.get(input);
        if (basicType != null) {
            return basicType.supportStoreByteData();
        }
        return false;
    }

    /**
     * 基本类型的默认值
     *
     * @param input 输入类型
     * @return 如果是基本类型则返回默认值，否则返回null
     */
    public static <T> T getDefaultValue(Class<T> input) {
        BasicType basicType = BASIC_TYPE_MAP.get(input);
        if (basicType != null) {
            return (T) basicType.defaultValue();
        }
        return null;
    }

    /**
     * 基本类型的数字兼容顺序
     *
     * @param input 输入类型
     * @return 返回基本类型的数字兼容顺序
     */
    public static int getNumberCompatibleOrder(Class<?> input) {
        BasicType basicType = BASIC_TYPE_MAP.get(input);
        if (basicType != null) {
            return basicType.numberCompatibleOrder();
        }
        return -1;
    }

    /**
     * 基本类型的字节数量
     *
     * @param input 输入类型
     * @return 返回基本类型的字节数量
     */
    public static int getByteSize(Class<?> input) {
        BasicType basicType = BASIC_TYPE_MAP.get(input);
        if (basicType != null) {
            return basicType.byteSize();
        }
        return -1;
    }

    /**
     * 基本类型的包装类型与原生类型互相转换
     *
     * @param input 输入类型
     * @return 返回对应的基本类型或包装类型，如果不支持则返回{@code input}
     */
    public static Class<?> getFlipType(Class<?> input) {
        BasicType basicType = BASIC_TYPE_MAP.get(input);
        if (basicType != null) {
            if (input.isPrimitive()) {
                return basicType.wrapperType();
            } else {
                return basicType.primitiveType();
            }
        }
        return input;
    }

    /**
     * 获取对应的包装类型
     *
     * @param input 原生类型或包装类型
     * @return 包装类型，如果输入不是基本类型则返回原类型
     */
    public static Class<?> getWrapperType(Class<?> input) {
        if (input != null && input.isPrimitive()) {
            BasicType basicType = BASIC_TYPE_MAP.get(input);
            return basicType != null ? basicType.wrapperType() : input;
        }
        // 如果已经是包装类型，直接返回
        return input;
    }

    /**
     * 获取对应的原生类型
     *
     * @param input 原生类型或包装类型
     * @return 原生类型，如果输入不是基本类型则返回原类型
     */
    public static Class<?> getPrimitiveType(Class<?> input) {
        if (input == null) return null;
        if (!input.isPrimitive()) {
            BasicType basicType = BASIC_TYPE_MAP.get(input);
            return basicType != null ? basicType.primitiveType() : input;
        }
        // 如果已经是原生类型，直接返回
        return input;
    }

    /**
     * 获取字符串转换器
     *
     * @param input 输入类型
     * @return 返回将字符串转换为指定类型的函数
     */
    public static <T> Function<String, T> getStrictConverter(Class<T> input) {
        BasicType basicType = BASIC_TYPE_MAP.get(input);
        if (basicType != null) {
            return (Function<String, T>) basicType.strictConverter();
        }
        return null;
    }

    /**
     * 获取与指定类型兼容的类型集合
     * 该方法检查基本类型和包装类型的兼容性，返回与输入类型兼容的相关类型集合
     *
     * @param input 输入的Class类型，用于查找兼容的类型集合
     * @return 与输入类型兼容的类型集合，如果输入类型不是基本类型或包装类型则返回空集合
     */
    public static Set<Class<?>> getCompatibleTypes(Class<?> input) {
        // 检查基本类型和包装类型的兼容性
        BasicType basicType = BasicType.BASIC_TYPE_MAP.get(input);
        if (basicType != null) {
            if (input.isPrimitive()) {
                return basicType.getCompatiblePrimitiveTypes();
            } else {
                return basicType.getCompatibleWrapperTypes();
            }
        }
        return Collections.emptySet();
    }


    /**
     * 获取基本类型兼容类型集合
     * 对于数字类型，返回所有兼容的数字类型（包括当前类型及范围更大的类型）
     * 对于非数字类型，返回仅包含自身基本类型和包装类型的集合
     *
     * @return 该基本类型可以兼容的其他类型集合
     */
    public Set<Class<?>> getCompatiblePrimitiveTypes() {
        if (numberCompatibleOrder < 0) {
            // 对于非数字类型（如boolean、char），只兼容自身
            return Set.of(primitiveType, wrapperType);
        }
        Set<Class<?>> compatiblePrimitiveTypes = new HashSet<>();
        for (Map.Entry<Class<?>, BasicType> entry : BASIC_TYPE_MAP.entrySet()) {
            BasicType basicType = entry.getValue();
            // 数字类型兼容性：当前类型的order小于等于其他类型的order时，表示当前类型可以转换为其他类型
            if (numberCompatibleOrder <= basicType.numberCompatibleOrder) {
                compatiblePrimitiveTypes.add(entry.getKey());
            }
        }
        return compatiblePrimitiveTypes;
    }

    /**
     * 获取包装类型兼容类型集合
     * 返回包含该类型的基本类型和包装类型的集合
     *
     * @return 包装类型兼容的类型集合
     */
    public Set<Class<?>> getCompatibleWrapperTypes() {
        return Set.of(primitiveType, wrapperType);
    }

    /**
     * 比较类型级别
     * 根据numberCompatibleOrder字段比较两个BasicType实例的优先级
     *
     * @param o 另一个BasicType实例
     * @return 比较结果，负数表示当前对象级别更低（范围更窄），0表示级别相同，正数表示当前对象级别更高（范围更宽）
     */
    @Override
    public int compareTo(BasicType o) {
        return Integer.compare(numberCompatibleOrder, o.numberCompatibleOrder);
    }
}
package terraqo.arbiter;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author Terraqo
 */
public enum BasicTypeEnum {
    BOOLEAN_TYPE(boolean.class, Boolean.class, 0x00_00_00_10, false, Boolean::parseBoolean, boolean.class, Boolean.class),
    CHARACTER_TYPE(char.class, Character.class, 0x00_00_00_20, '\0', s -> s.isEmpty() ? '\0' : s.charAt(0), char.class, Character.class),
    BYTE_TYPE(byte.class, Byte.class, 0x80_00_00_11, (byte) 0, Byte::parseByte, byte.class, Byte.class),
    SHORT_TYPE(short.class, Short.class, 0x80_00_00_22, (short) 0, Short::parseShort, short.class, Short.class),
    INTEGER_TYPE(int.class, Integer.class, 0x80_00_00_43, 0, Integer::parseInt, int.class, Integer.class),
    LONG_TYPE(long.class, Long.class, 0x80_00_00_84, 0L, Long::parseLong, long.class, Long.class),
    FLOAT_TYPE(float.class, Float.class, 0x80_00_00_45, 0.0f, Float::parseFloat, float.class, Float.class),
    DOUBLE_TYPE(double.class, Double.class, 0x80_00_00_86, 0.0, Double::parseDouble, double.class, Double.class);
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
     * 0-4 bit 用于表示类型的优先级或范围大小,级别越小表示范围越窄，级别越大表示范围越宽
     * 5-8 bit 用于表示字节数，表示该类型占用的内存大小
     * <p>
     * 31 bit 用于表示是否支持存储字节数据，如果支持则该类型可以存储字节数据
     */
    private final int value;

    /**
     * 严格转换器，用于将字符串严格转换为该类型的对象
     */
    public final Function<String, Object> strictConverter;
    /**
     * 默认值，用于创建该类型的对象时，如果未指定值，则使用默认值
     */
    public final Object defaultValue;

    BasicTypeEnum(Class<?> primitiveType, Class<?> wrapperType, int value,
                  Object defaultValue, Function<String, Object> strictConverter, Class<?>... primitiveCompatibleTypes) {
        this.primitiveType = primitiveType;
        this.primitiveCompatibleTypes = Set.of(primitiveCompatibleTypes);
        this.wrapperType = wrapperType;
        this.wrapperCompatibleTypes = Set.of(primitiveType, wrapperType);
        this.value = value;
        this.defaultValue = defaultValue;
        this.strictConverter = strictConverter;
    }

    /**
     * 基本类型信息映射表
     * 将基本类型和包装类型映射到对应的BasicType实例
     */
    public static final Map<Class<?>, BasicTypeEnum> BASIC_TYPE_MAP = Map.ofEntries(
            Map.entry(DOUBLE_TYPE.primitiveType, DOUBLE_TYPE),
            Map.entry(DOUBLE_TYPE.wrapperType, DOUBLE_TYPE),
            Map.entry(FLOAT_TYPE.primitiveType, FLOAT_TYPE),
            Map.entry(FLOAT_TYPE.wrapperType, FLOAT_TYPE),
            Map.entry(LONG_TYPE.primitiveType, LONG_TYPE),
            Map.entry(LONG_TYPE.wrapperType, LONG_TYPE),
            Map.entry(INTEGER_TYPE.primitiveType, INTEGER_TYPE),
            Map.entry(INTEGER_TYPE.wrapperType, INTEGER_TYPE),
            Map.entry(SHORT_TYPE.primitiveType, SHORT_TYPE),
            Map.entry(SHORT_TYPE.wrapperType, SHORT_TYPE),
            Map.entry(BYTE_TYPE.primitiveType, BYTE_TYPE),
            Map.entry(BYTE_TYPE.wrapperType, BYTE_TYPE),
            Map.entry(CHARACTER_TYPE.primitiveType, CHARACTER_TYPE),
            Map.entry(CHARACTER_TYPE.wrapperType, CHARACTER_TYPE),
            Map.entry(BOOLEAN_TYPE.primitiveType, BOOLEAN_TYPE),
            Map.entry(BOOLEAN_TYPE.wrapperType, BOOLEAN_TYPE)
    );

    public Class<?> primitiveType() {
        return primitiveType;
    }

    public Class<?> wrapperType() {
        return wrapperType;
    }

    public boolean supportStoreByteData() {
        return (0x80_00_00_00 & value) == 0x80_00_00_00;
    }

    public int numberCompatibleOrder() {
        return 0xF & value;
    }

    public int byteSize() {
        return (0xF & value) >>> 4;
    }

    public Object defaultValue() {
        return defaultValue;
    }

    public Function<String, Object> strictConverter() {
        return strictConverter;
    }
}

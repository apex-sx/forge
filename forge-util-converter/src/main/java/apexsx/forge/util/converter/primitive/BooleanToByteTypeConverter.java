package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 布尔值到字节类型的转换器
 * <p>
 * 该转换器用于将Boolean类型转换为Byte类型，true转换为1，false转换为0
 * 这是一个单例类，通过INSTANCE字段获取实例
 * </p>
 *
 * @author zhitron
 */
public class BooleanToByteTypeConverter extends TypeConverter<Boolean, Byte> {
    /**
     * 单例实例
     */
    public static final BooleanToByteTypeConverter INSTANCE = new BooleanToByteTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected BooleanToByteTypeConverter() {
        super(Boolean.class, Byte.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 转换规则：true转换为(byte)1，false转换为(byte)0
     *
     * @param source 源对象，Boolean类型
     * @return 转换后的Byte对象
     */
    @Override
    public Byte convertsUnchecked(Boolean source) throws Throwable {
        return source ? (byte) 1 : (byte) 0;
    }
}

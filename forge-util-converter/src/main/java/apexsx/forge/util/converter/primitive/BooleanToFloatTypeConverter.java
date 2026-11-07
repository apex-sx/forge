package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 布尔值到浮点数的类型转换器
 * <p>
 * 该转换器用于将布尔值转换为对应的浮点数值，其中 true 转换为 1.0f，false 转换为 0.0f。
 * 这是一个单例类，通过 {@link #INSTANCE} 获取实例。
 * </p>
 *
 * @author zhitron
 */
public class BooleanToFloatTypeConverter extends TypeConverter<Boolean, Float> {
    /**
     * 单例实例
     */
    public static final BooleanToFloatTypeConverter INSTANCE = new BooleanToFloatTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected BooleanToFloatTypeConverter() {
        super(Boolean.class, Float.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 转换规则：true转换为1.0f，false转换为0.0f
     *
     * @param source 源对象，Boolean类型
     * @return 转换后的Float对象
     */
    @Override
    public Float convertsUnchecked(Boolean source) throws Throwable {
        return source ? (float) 1 : (float) 0;
    }
}

package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 布尔值到双精度浮点数的类型转换器
 * <p>
 * 该转换器用于将布尔值(true/false)转换为对应的双精度浮点数值(1.0/0.0)
 * 这是一个单例类，通过INSTANCE字段获取实例
 * </p>
 *
 * @author zhitron
 */
public class BooleanToDoubleTypeConverter extends TypeConverter<Boolean, Double> {
    /**
     * 单例实例
     */
    public static final BooleanToDoubleTypeConverter INSTANCE = new BooleanToDoubleTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected BooleanToDoubleTypeConverter() {
        super(Boolean.class, Double.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 转换规则：true转换为1.0，false转换为0.0
     *
     * @param source 源对象，Boolean类型
     * @return 转换后的Double对象
     */
    @Override
    public Double convertsUnchecked(Boolean source) throws Throwable {
        return source ? (double) 1 : (double) 0;
    }
}

package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 布尔值到整型的类型转换器
 * <p>
 * 该转换器用于将Boolean类型转换为Integer类型，转换规则是：
 * true转换为1，false转换为0。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class BooleanToIntegerTypeConverter extends TypeConverter<Boolean, Integer> {
    /**
     * 单例实例
     */
    public static final BooleanToIntegerTypeConverter INSTANCE = new BooleanToIntegerTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected BooleanToIntegerTypeConverter() {
        super(Boolean.class, Integer.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 转换规则：true转换为1，false转换为0
     *
     * @param source 源对象，Boolean类型
     * @return 转换后的Integer对象
     */
    @Override
    public Integer convertsUnchecked(Boolean source) throws Throwable {
        return source ? 1 : 0;
    }
}

package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 字符串到浮点数的类型转换器
 * <p>
 * 该转换器用于将String类型转换为Float类型，使用Float.parseFloat()方法进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToFloatTypeConverter extends TypeConverter<String, Float> {
    /**
     * 单例实例
     */
    public static final StringToFloatTypeConverter INSTANCE = new StringToFloatTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected StringToFloatTypeConverter() {
        super(String.class, Float.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 使用Float.parseFloat()方法将字符串转换为浮点数
     *
     * @param source 源对象，必须是有效的数字字符串
     * @return 转换后的Float对象
     * @throws NumberFormatException 当字符串不是有效的数字格式时抛出
     * @throws NullPointerException  当source为null时抛出
     */
    @Override
    public Float convertsUnchecked(String source) throws Throwable {
        return Float.parseFloat(source);
    }
}

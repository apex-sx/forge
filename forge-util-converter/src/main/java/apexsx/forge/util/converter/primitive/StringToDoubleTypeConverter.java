package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 字符串到双精度浮点数的类型转换器
 * <p>
 * 该转换器用于将String类型转换为Double类型，使用Double.parseDouble()方法进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToDoubleTypeConverter extends TypeConverter<String, Double> {
    /**
     * 单例实例
     */
    public static final StringToDoubleTypeConverter INSTANCE = new StringToDoubleTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected StringToDoubleTypeConverter() {
        super(String.class, Double.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 使用Double.parseDouble()方法将字符串转换为双精度浮点数
     *
     * @param source 源对象，必须是有效的数字字符串
     * @return 转换后的Double对象
     * @throws NumberFormatException 当字符串不是有效的数字格式时抛出
     * @throws NullPointerException  当source为null时抛出
     */
    @Override
    public Double convertsUnchecked(String source) throws Throwable {
        return Double.parseDouble(source);
    }
}

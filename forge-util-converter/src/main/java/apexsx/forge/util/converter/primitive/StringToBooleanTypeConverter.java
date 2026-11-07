package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 字符串到布尔值的类型转换器
 * <p>
 * 该转换器用于将String类型转换为Boolean类型，使用Boolean.parseBoolean()方法进行转换。
 * 只有当字符串为"true"（忽略大小写）时返回true，其他所有情况都返回false。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToBooleanTypeConverter extends TypeConverter<String, Boolean> {
    /**
     * 单例实例
     */
    public static final StringToBooleanTypeConverter INSTANCE = new StringToBooleanTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected StringToBooleanTypeConverter() {
        super(String.class, Boolean.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 使用Boolean.parseBoolean()方法将字符串转换为布尔值
     * 只有当字符串为"true"（忽略大小写）时返回true，其他所有情况都返回false
     *
     * @param source 源对象，任意字符串
     * @return 转换后的Boolean对象
     */
    @Override
    public Boolean convertsUnchecked(String source) throws Throwable {
        return Boolean.parseBoolean(source);
    }
}

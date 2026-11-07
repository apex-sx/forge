package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.CharSequenceTypeConverter;

/**
 * 字符串到Short类型转换器
 * <p>
 * 用于将字符串转换为Short类型。
 * 该转换器直接使用Short的构造函数进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToShortTypeConverter extends CharSequenceTypeConverter<String, Short> {
    /**
     * 单例实例
     */
    public static final StringToShortTypeConverter INSTANCE = new StringToShortTypeConverter();

    /**
     * 构造函数，初始化目标类型为Short.meta
     */
    protected StringToShortTypeConverter() {
        super(String.class, Short.class);
    }

    /**
     * 将字符串源对象转换为Short对象
     *
     * @param input 源对象(字符串类型)
     * @return 转换后的Short对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    protected Short convertsUncheckedString(String input) throws Throwable {
        return Short.parseShort(input);
    }
}

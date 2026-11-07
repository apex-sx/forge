package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.CharSequenceTypeConverter;

/**
 * 字符串到Float类型转换器
 * <p>
 * 用于将字符串转换为Float类型。
 * 该转换器直接使用Float的构造函数进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToFloatTypeConverter extends CharSequenceTypeConverter<String, Float> {
    /**
     * 单例实例
     */
    public static final StringToFloatTypeConverter INSTANCE = new StringToFloatTypeConverter();

    /**
     * 构造函数，初始化目标类型为Float.meta
     */
    protected StringToFloatTypeConverter() {
        super(String.class, Float.class);
    }

    /**
     * 将字符串源对象转换为Float对象
     *
     * @param input 源对象(字符串类型)
     * @return 转换后的Float对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    protected Float convertsUncheckedString(String input) throws Throwable {
        return Float.parseFloat(input);
    }
}

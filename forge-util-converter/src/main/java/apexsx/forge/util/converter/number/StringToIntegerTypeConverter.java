package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.CharSequenceTypeConverter;

/**
 * 字符串到Integer类型转换器
 * <p>
 * 用于将字符串转换为Integer类型。
 * 该转换器直接使用Integer的构造函数进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToIntegerTypeConverter extends CharSequenceTypeConverter<String, Integer> {
    /**
     * 单例实例
     */
    public static final StringToIntegerTypeConverter INSTANCE = new StringToIntegerTypeConverter();

    /**
     * 构造函数，初始化目标类型为Integer.meta
     */
    protected StringToIntegerTypeConverter() {
        super(String.class, Integer.class);
    }

    /**
     * 将字符串源对象转换为Integer对象
     *
     * @param input 源对象(字符串类型)
     * @return 转换后的Integer对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    protected Integer convertsUncheckedString(String input) throws Throwable {
        return Integer.parseInt(input);
    }
}

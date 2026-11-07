package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.CharSequenceTypeConverter;

/**
 * 字符串到Double类型转换器
 * <p>
 * 用于将字符串转换为Double类型。
 * 该转换器直接使用Double的构造函数进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class CharSequenceToDoubleTypeConverter extends CharSequenceTypeConverter<CharSequence, Double> {
    /**
     * 单例实例
     */
    public static final CharSequenceToDoubleTypeConverter INSTANCE = new CharSequenceToDoubleTypeConverter();

    /**
     * 构造函数，初始化目标类型为Double.meta
     */
    protected CharSequenceToDoubleTypeConverter() {
        super(Double.class);
    }

    /**
     * 将字符串源对象转换为Double对象
     *
     * @param input 源对象(字符串类型)
     * @return 转换后的Double对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    protected Double convertsUncheckedString(String input) throws Throwable {
        return StringToDoubleTypeConverter.INSTANCE.convertsUnchecked(input);
    }
}

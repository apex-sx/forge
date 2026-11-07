package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.CharSequenceTypeConverter;

/**
 * 字符串到Number类型转换器
 * <p>
 * 用于将字符串转换为Number类型。
 * 该转换器直接使用Number的构造函数进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class CharSequenceToNumberTypeConverter extends CharSequenceTypeConverter<CharSequence, Number> {
    /**
     * 单例实例
     */
    public static final CharSequenceToNumberTypeConverter INSTANCE = new CharSequenceToNumberTypeConverter();

    /**
     * 构造函数，初始化目标类型为Number.meta
     */
    protected CharSequenceToNumberTypeConverter() {
        super(Number.class);
    }

    /**
     * 将字符串源对象转换为Number对象
     *
     * @param input 源对象(字符串类型)
     * @return 转换后的Number对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    protected Number convertsUncheckedString(String input) throws Throwable {
        return StringToNumberTypeConverter.INSTANCE.convertsUnchecked(input);
    }
}

package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.CharSequenceTypeConverter;

import java.math.BigDecimal;

/**
 * 字符串到BigDecimal类型转换器
 * <p>
 * 用于将字符串转换为BigDecimal类型。
 * 该转换器直接使用BigDecimal的构造函数进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class CharSequenceToBigDecimalTypeConverter extends CharSequenceTypeConverter<CharSequence, BigDecimal> {
    /**
     * 单例实例
     */
    public static final CharSequenceToBigDecimalTypeConverter INSTANCE = new CharSequenceToBigDecimalTypeConverter();

    /**
     * 构造函数，初始化目标类型为BigDecimal.meta
     */
    protected CharSequenceToBigDecimalTypeConverter() {
        super(BigDecimal.class);
    }

    /**
     * 将字符串源对象转换为BigDecimal对象
     *
     * @param input 源对象(字符串类型)
     * @return 转换后的BigDecimal对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    protected BigDecimal convertsUncheckedString(String input) throws Throwable {
        return StringToBigDecimalTypeConverter.INSTANCE.convertsUnchecked(input);
    }
}

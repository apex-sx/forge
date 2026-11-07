package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.CharSequenceTypeConverter;

import java.math.BigInteger;

/**
 * 字符串到BigInteger类型转换器
 * <p>
 * 用于将字符串转换为BigInteger表示形式。
 * 该转换器直接使用BigInteger的构造函数进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToBigIntegerTypeConverter extends CharSequenceTypeConverter<String, BigInteger> {
    /**
     * 单例实例
     */
    public static final StringToBigIntegerTypeConverter INSTANCE = new StringToBigIntegerTypeConverter();

    /**
     * 构造函数，初始化目标类型为BigInteger.meta
     */
    protected StringToBigIntegerTypeConverter() {
        super(String.class, BigInteger.class);
    }

    /**
     * 将字符串源对象转换为BigInteger对象
     *
     * @param input 源对象(字符串类型)
     * @return 转换后的BigInteger对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    protected BigInteger convertsUncheckedString(String input) throws Throwable {
        return new BigInteger(input);
    }
}

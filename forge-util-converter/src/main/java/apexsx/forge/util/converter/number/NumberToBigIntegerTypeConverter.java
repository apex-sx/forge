package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.TypeConverter;
import com.github.zhitron.type_converter.TypeConverterException;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数字到BigInteger类型转换器
 * <p>
 * 用于将各种数字类型(Number子类)转换为BigInteger表示形式。
 * 针对不同数字类型采用不同的转换策略以保证精度：
 * - BigInteger: 直接返回
 * - BigDecimal: 使用toBigInteger()方法转换
 * - Double/Float: 检查特殊值(NaN/Infinite)后转为BigDecimal再转换
 * - 其他Number类型: 使用longValue()转换后创建BigInteger
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class NumberToBigIntegerTypeConverter extends TypeConverter<Number, BigInteger> {
    /**
     * 单例实例
     */
    public static final NumberToBigIntegerTypeConverter INSTANCE = new NumberToBigIntegerTypeConverter();

    /**
     * 构造函数，初始化目标类型为BigInteger.meta
     */
    protected NumberToBigIntegerTypeConverter() {
        super(BigInteger.class);
    }

    /**
     * 判断指定的源对象是否支持转换
     * 仅当源对象是Number类型时才支持转换
     *
     * @param source 源对象
     * @return 如果源对象是Number类型返回true，否则返回false
     */
    @Override
    public boolean isSupportsSource(Object source) {
        return source instanceof Number;
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 针对不同数字类型采用不同的转换策略以保证精度：
     * - BigInteger: 直接返回
     * - BigDecimal: 使用toBigInteger()方法转换
     * - Double: 检查特殊值后通过BigDecimal.valueOf()转换
     * - Float: 检查特殊值后通过BigDecimal.valueOf()转换
     * - 其他Number类型: 通过longValue()转换后创建BigInteger
     *
     * @param source 源对象(数字类型)
     * @return 转换后的BigInteger对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    public BigInteger convertsUnchecked(Number source) throws Throwable {
        if (source instanceof BigInteger) {
            return (BigInteger) source;
        } else if (source instanceof BigDecimal) {
            return ((BigDecimal) source).toBigInteger();
        } else if (source instanceof Double) {
            double d = (Double) source;
            if (Double.isNaN(d) || Double.isInfinite(d)) {
                throw new TypeConverterException("Cannot convert NaN or Infinite Double to BigInteger");
            }
            return BigDecimal.valueOf(d).toBigInteger();
        } else if (source instanceof Float) {
            float f = (Float) source;
            if (Float.isNaN(f) || Float.isInfinite(f)) {
                throw new TypeConverterException("Cannot convert NaN or Infinite Float to BigInteger");
            }
            return BigDecimal.valueOf(f).toBigInteger();
        } else {
            return BigInteger.valueOf(source.longValue());
        }
    }
}

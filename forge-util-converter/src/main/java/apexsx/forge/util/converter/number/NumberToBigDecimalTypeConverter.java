package apexsx.forge.util.converter.number;

import com.github.zhitron.type_converter.TypeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数字到BigDecimal类型转换器
 * <p>
 * 用于将各种数字类型(Number子类)转换为BigDecimal表示形式。
 * 针对不同数字类型采用不同的转换策略以保证精度：
 * - BigDecimal: 直接返回原对象
 * - BigInteger: 转换为BigDecimal
 * - 整数类型(Integer,Long,Short,Byte): 使用BigDecimal.valueOf(long)转换
 * - 浮点类型(Double,Float): 使用BigDecimal.valueOf(double)转换，但先检查特殊值
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class NumberToBigDecimalTypeConverter extends TypeConverter<Number, BigDecimal> {
    /**
     * 单例实例
     */
    public static final NumberToBigDecimalTypeConverter INSTANCE = new NumberToBigDecimalTypeConverter();

    /**
     * 构造函数，初始化目标类型为BigDecimal.meta
     */
    protected NumberToBigDecimalTypeConverter() {
        super(BigDecimal.class);
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
     * - BigDecimal: 直接返回原对象
     * - BigInteger: 转换为BigDecimal
     * - 整数类型(Integer,Long,Short,Byte): 使用BigDecimal.valueOf(long)转换
     * - 浮点类型(Double,Float): 使用BigDecimal.valueOf(double)转换，但先检查特殊值
     *
     * @param source 源对象(数字类型)
     * @return 转换后的BigDecimal对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    public BigDecimal convertsUnchecked(Number source) throws Throwable {
        if (source instanceof BigDecimal) {
            return (BigDecimal) source;
        } else if (source instanceof BigInteger) {
            return new BigDecimal((BigInteger) source);
        } else if (source instanceof Integer || source instanceof Long ||
                source instanceof Short || source instanceof Byte) {
            return BigDecimal.valueOf(source.longValue());
        } else {
            // 处理Double/Float及其他类型
            double value = source.doubleValue();
            // 检查特殊值
            if (Double.isNaN(value) || Double.isInfinite(value)) {
                throw new IllegalArgumentException("Cannot convert NaN or Infinite to BigDecimal");
            }
            return BigDecimal.valueOf(value);
        }
    }
}

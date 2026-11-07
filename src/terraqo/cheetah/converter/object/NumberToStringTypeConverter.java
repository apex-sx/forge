/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.converter.object;

import terraqo.cheetah.converter.AbstractTypeConverter;
import terraqo.cheetah.converter.TypeConverterException;

import java.math.BigDecimal;

/**
 * 数字到字符串类型转换器
 * <p>
 * 用于将各种数字类型(Number子类)转换为字符串表示形式。
 * 针对不同数字类型采用不同的转换策略以保证精度：
 * - BigDecimal: 使用toPlainString()避免科学计数法
 * - Double/Float: 先转为BigDecimal再使用toPlainString()
 * - 其他Number类型: 直接使用toString()
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author Terraqo
 */
public class NumberToStringTypeConverter extends AbstractTypeConverter<Number, String> {
    /**
     * 单例实例
     */
    public static final NumberToStringTypeConverter INSTANCE = new NumberToStringTypeConverter();

    /**
     * 构造函数，初始化目标类型为String.class
     * 使用public修饰符（注意：与其他转换器不同）
     */
    protected NumberToStringTypeConverter() {
        super(String.class);
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
     * - BigDecimal: 使用toPlainString()避免科学计数法
     * - Double/Float: 先转为BigDecimal再使用toPlainString()
     * - 其他Number类型: 直接使用toString()
     *
     * @param source 源对象(数字类型)
     * @return 转换后的目标对象(字符串)，可能为null
     * @throws TypeConverterException 转换过程中发生错误时抛出
     */
    @Override
    public String convertsUnchecked(Number source) throws Throwable {
        if (source instanceof BigDecimal) {
            // 对于BigDecimal，使用toPlainString()避免科学计数法表示
            return ((BigDecimal) source).toPlainString();
        } else if (source instanceof Double) {
            // 对于Double，先转换为BigDecimal再输出，确保精度不丢失
            return BigDecimal.valueOf((Double) source).toPlainString();
        } else if (source instanceof Float) {
            // 对于Float，先转换为BigDecimal再输出，确保精度不丢失
            return BigDecimal.valueOf((Float) source).toPlainString();
        }
        // 其他Number类型的直接转换
        return source.toString();
    }
}

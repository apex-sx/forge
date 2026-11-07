/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.converter.object;

import terraqo.cheetah.converter.AbstractTypeConverter;

/**
 * 对象到字符串的类型转换器
 * <p>
 * 支持将任意对象转换为字符串，并可选择转换为大写或小写格式。
 * 提供三个预定义的实例：
 * - DEFAULT: 默认转换器实例，不改变字符串大小写
 * - UPPER_CASE: 大写转换器实例，将结果转换为大写
 * - LOWER_CASE: 小写转换器实例，将结果转换为小写
 * </p>
 *
 * @author Terraqo
 */
public class ObjectToStringTypeConverter extends AbstractTypeConverter<Object, String> {
    /**
     * 默认转换器实例，不改变字符串大小写
     */
    public static final ObjectToStringTypeConverter DEFAULT = new ObjectToStringTypeConverter();
    /**
     * 大写转换器实例，将结果转换为大写
     */
    public static final ObjectToStringTypeConverter UPPER_CASE = new ObjectToStringTypeConverter("upper", "uppercase");
    /**
     * 小写转换器实例，将结果转换为小写
     */
    public static final ObjectToStringTypeConverter LOWER_CASE = new ObjectToStringTypeConverter("lower", "lowercase");

    /**
     * 构造一个新的对象到字符串转换器
     * 使用protected修饰符防止外部直接实例化
     *
     * @param supportedSelectors 转换类型，可为"upper"(大写)、"lower"(小写)或null(默认)
     */
    protected ObjectToStringTypeConverter(Object... supportedSelectors) {
        super(String.class, supportedSelectors);
    }

    /**
     * 判断指定的源对象是否支持转换
     * 仅当源对象不为null时才支持转换
     *
     * @param source 源对象
     * @return 如果源对象不为null返回true，否则返回false
     */
    @Override
    public boolean isSupportsSource(Object source) {
        return source != null;
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 根据构造函数中指定的选择器决定是否转换大小写
     *
     * @param source 源对象，不能为null
     * @return 转换后的String对象
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public String convertsUnchecked(Object source) throws Throwable {
        String result = source.toString();
        if (isContainsSelector("upper", "uppercase")) {
            return result.toUpperCase();
        } else if (isContainsSelector("lower", "lowercase")) {
            return result.toLowerCase();
        }
        return result;
    }
}

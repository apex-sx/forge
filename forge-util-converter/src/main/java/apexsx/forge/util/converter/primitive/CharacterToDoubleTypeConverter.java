package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 字符到双精度浮点数的类型转换器
 * <p>
 * 该转换器用于将Character类型转换为Double类型，通过将字符的数值直接转换为double类型实现。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class CharacterToDoubleTypeConverter extends TypeConverter<Character, Double> {
    /**
     * 单例实例
     */
    public static final CharacterToDoubleTypeConverter INSTANCE = new CharacterToDoubleTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected CharacterToDoubleTypeConverter() {
        super(Character.class, Double.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 直接将字符的数值转换为double类型
     *
     * @param source 源对象，必须是Character类型
     * @return 转换后的Double对象
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public Double convertsUnchecked(Character source) throws Throwable {
        return (double) source;
    }
}

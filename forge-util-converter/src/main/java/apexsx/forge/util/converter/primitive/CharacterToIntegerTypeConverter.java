package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 字符到整型的类型转换器
 * <p>
 * 该转换器用于将Character类型转换为Integer类型，通过将字符的数值直接转换为int类型实现。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class CharacterToIntegerTypeConverter extends TypeConverter<Character, Integer> {
    /**
     * 单例实例
     */
    public static final CharacterToIntegerTypeConverter INSTANCE = new CharacterToIntegerTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected CharacterToIntegerTypeConverter() {
        super(Character.class, Integer.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 直接将字符的数值转换为int类型
     *
     * @param source 源对象，必须是Character类型
     * @return 转换后的Integer对象
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public Integer convertsUnchecked(Character source) throws Throwable {
        return (int) source;
    }
}

package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 字符到布尔值的类型转换器
 * <p>
 * 该转换器用于将Character类型转换为Boolean类型。转换规则是：
 * 当字符为null、空白字符或空字符('\u0000')时返回false，
 * 其他情况下返回true。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class CharacterToBooleanTypeConverter extends TypeConverter<Character, Boolean> {
    /**
     * 单例实例
     */
    public static final CharacterToBooleanTypeConverter INSTANCE = new CharacterToBooleanTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected CharacterToBooleanTypeConverter() {
        super(Character.class, Boolean.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 转换规则：字符为null、空白字符或空字符时返回false，其他情况返回true
     *
     * @param source 源对象，可以为null
     * @return 转换后的Boolean对象
     */
    @Override
    public Boolean convertsUnchecked(Character source) throws Throwable {
        if (source == null) {
            return false;
        }
        return !Character.isWhitespace(source) && source != '\u0000';
    }
}

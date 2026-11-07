package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 字符到字节的类型转换器
 * <p>
 * 该转换器用于将Character类型转换为Byte类型，通过将字符的数值直接转换为byte类型实现。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class CharacterToByteTypeConverter extends TypeConverter<Character, Byte> {
    /**
     * 单例实例
     */
    public static final CharacterToByteTypeConverter INSTANCE = new CharacterToByteTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected CharacterToByteTypeConverter() {
        super(Character.class, Byte.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 直接将字符的数值转换为byte类型
     *
     * @param source 源对象，必须是Character类型
     * @return 转换后的Byte对象
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public Byte convertsUnchecked(Character source) throws Throwable {
        return (byte) source.charValue();
    }
}

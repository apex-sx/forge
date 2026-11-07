package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;
import com.github.zhitron.type_converter.TypeConverterException;

/**
 * 字符串到字符的类型转换器
 * <p>
 * 该转换器用于将String类型转换为Character类型。要求字符串长度必须为1，
 * 否则将抛出TypeConverterException异常。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToCharacterTypeConverter extends TypeConverter<String, Character> {
    /**
     * 单例实例
     */
    public static final StringToCharacterTypeConverter INSTANCE = new StringToCharacterTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected StringToCharacterTypeConverter() {
        super(String.class, Character.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 检查字符串长度必须为1，然后返回第一个字符
     *
     * @param source 源对象，必须是长度为1的字符串
     * @return 转换后的Character对象，即字符串中的第一个字符
     * @throws TypeConverterException 当字符串长度不为1时抛出
     * @throws NullPointerException   当source为null时抛出
     */
    @Override
    public Character convertsUnchecked(String source) throws Throwable {
        if (source.length() != 1) {
            throw new TypeConverterException("String length must be 1");
        }
        return source.charAt(0);
    }
}

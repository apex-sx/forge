package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;
import com.github.zhitron.type_converter.TypeConverterException;

/**
 * 数字到字符的类型转换器
 * <p>
 * 该转换器用于将Number类型转换为Character类型。使用Character.toChars()方法将数字转换为字符，
 * 并检查转换结果是否为单个字符，如果不是则抛出TypeConverterException异常。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class NumberToCharacterTypeConverter extends TypeConverter<Number, Character> {
    /**
     * 单例实例
     */
    public static final NumberToCharacterTypeConverter INSTANCE = new NumberToCharacterTypeConverter();

    /**
     * 构造函数，初始化目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected NumberToCharacterTypeConverter() {
        super(Character.class);
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
     * 使用Character.toChars()方法将数字转换为字符，并检查结果是否为单个字符
     *
     * @param source 源对象，必须是Number类型
     * @return 转换后的Character对象
     * @throws TypeConverterException 当数字无法转换为单个字符时抛出
     * @throws NullPointerException   当source为null时抛出
     */
    @Override
    public Character convertsUnchecked(Number source) throws Throwable {
        char[] chars = Character.toChars(Math.toIntExact(source.intValue()));
        if (chars.length != 1) {
            throw new TypeConverterException("cannot convert to char");
        }
        return chars[0];
    }
}

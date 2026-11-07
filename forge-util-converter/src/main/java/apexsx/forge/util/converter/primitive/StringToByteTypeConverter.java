package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 字符串到字节的类型转换器
 * <p>
 * 该转换器用于将String类型转换为Byte类型，使用Byte.parseByte()方法进行转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class StringToByteTypeConverter extends TypeConverter<String, Byte> {
    /**
     * 单例实例
     */
    public static final StringToByteTypeConverter INSTANCE = new StringToByteTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用public修饰符（注意：与其他转换器不同）
     */
    protected StringToByteTypeConverter() {
        super(String.class, Byte.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 使用Byte.parseByte()方法将字符串转换为字节
     *
     * @param source 源对象，必须是有效的数字字符串（范围-128到127）
     * @return 转换后的Byte对象
     * @throws NumberFormatException 当字符串不是有效的数字格式时抛出
     * @throws NullPointerException  当source为null时抛出
     */
    @Override
    public Byte convertsUnchecked(String source) throws Throwable {
        return Byte.parseByte(source);
    }
}

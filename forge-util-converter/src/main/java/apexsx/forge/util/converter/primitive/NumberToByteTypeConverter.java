package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 数字到字节的类型转换器
 * <p>
 * 该转换器用于将Number类型转换为Byte类型。在转换过程中会检查数值是否溢出，
 * 如果超出byte类型的范围则抛出ArithmeticException异常。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class NumberToByteTypeConverter extends TypeConverter<Number, Byte> {
    /**
     * 单例实例
     */
    public static final NumberToByteTypeConverter INSTANCE = new NumberToByteTypeConverter();

    /**
     * 构造函数，初始化目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected NumberToByteTypeConverter() {
        super(Byte.class);
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
     * 检查转换过程中是否会发生溢出，如果溢出则抛出ArithmeticException
     *
     * @param source 源对象，必须是Number类型
     * @return 转换后的Byte对象
     * @throws ArithmeticException  当数值超出byte类型范围时抛出
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public Byte convertsUnchecked(Number source) throws Throwable {
        long value = source.longValue();
        if ((byte) value != value) {
            throw new ArithmeticException("byte overflow");
        }
        return (byte) value;
    }
}

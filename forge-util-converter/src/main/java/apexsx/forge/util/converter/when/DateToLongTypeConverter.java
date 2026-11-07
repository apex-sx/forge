package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverterException;

import java.util.Date;

/**
 * 日期转长整型类型转换器。
 * <p>
 * 该转换器用于将{@link Date}类型的对象转换为{@link Long}类型。
 * {@link Date}表示自1970年1月1日00:00:00 UTC以来的毫秒数，而{@link Long}表示相同的毫秒数。
 * </p>
 *
 * @author zhitron
 */
public class DateToLongTypeConverter extends AbstractWhenTypeConverter<Date, Long> {
    /**
     * 单例实例。
     * <p>
     * 由于该转换器是无状态的，因此可以通过此单例实例复用转换器对象，
     * 避免重复创建实例以提升性能。
     * </p>
     */
    public static final DateToLongTypeConverter INSTANCE = new DateToLongTypeConverter();

    /**
     * 构造函数，指定目标类型为Long。
     * <p>
     * 设置目标类型为{@link Long}。
     * 该构造函数被声明为protected，意味着主要通过单例实例访问该转换器。
     * </p>
     */
    protected DateToLongTypeConverter() {
        super(Long.class);
    }

    /**
     * 判断指定的源对象是否支持转换。
     * <p>
     * 当{@link #supportsSourceType()}返回null时，会调用此方法进行判断。
     * 该方法检查源对象是否为Date类型实例。
     * </p>
     *
     * @param source 源对象，需要判断是否支持转换的对象。
     * @return 如果源对象是Date类型实例返回true，否则返回false。
     */
    @Override
    public boolean isSupportsSource(Object source) {
        return source instanceof Date;
    }

    /**
     * 将源Date对象转换为Long对象，不进行源对象类型检查。
     * <p>
     * 该方法通过调用Date.getTime()方法获取自1970年1月1日00:00:00 UTC以来的毫秒数。
     * </p>
     *
     * @param source 源Date对象，表示自1970年1月1日00:00:00 UTC以来的毫秒数。
     * @return 转换后的Long对象，表示相同的毫秒数；如果source为null则返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public Long convertsUnchecked(Date source) throws Throwable {
        return source.getTime();
    }
}

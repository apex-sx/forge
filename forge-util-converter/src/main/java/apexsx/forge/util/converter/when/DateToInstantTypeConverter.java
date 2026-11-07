package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverterException;

import java.time.Instant;
import java.util.Date;

/**
 * Date转Instant类型转换器。
 * <p>
 * 该转换器用于将{@link Date}类型的对象转换为{@link Instant}类型。
 * {@link Date}表示自1970年1月1日00:00:00 UTC以来的毫秒数，而{@link Instant}表示时间轴上的一个瞬时点。
 * </p>
 *
 * @author zhitron
 */
public class DateToInstantTypeConverter extends AbstractWhenTypeConverter<Date, Instant> {
    /**
     * 转换器的单例实例
     */
    public static final DateToInstantTypeConverter INSTANCE = new DateToInstantTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型。
     * <p>
     * 设置目标类型为{@link Instant}。
     * 该构造函数被声明为protected，意味着主要通过单例实例访问该转换器。
     * </p>
     */
    protected DateToInstantTypeConverter() {
        super(Instant.class);
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
     * 将源Date对象转换为Instant对象，不进行源对象类型检查。
     * <p>
     * 该方法通过调用Date.toInstant()方法将Date转换为Instant。
     * </p>
     *
     * @param source 源Date对象，表示自1970年1月1日00:00:00 UTC以来的毫秒数。
     * @return 转换后的Instant对象，表示时间轴上的一个瞬时点；如果source为null则返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public Instant convertsUnchecked(Date source) throws Throwable {
        return source.toInstant();
    }
}

package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverterException;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/**
 * ZonedDateTime到OffsetDateTime的类型转换器。
 * <p>
 * 该转换器用于将{@link ZonedDateTime}类型的对象转换为{@link OffsetDateTime}类型。
 * {@link ZonedDateTime}表示带有时区信息的日期时间，而{@link OffsetDateTime}表示带有时区偏移量的日期时间。
 * 转换过程会丢失时区的区域信息，只保留时区偏移量。
 * </p>
 *
 * @author zhitron
 */
public class ZonedDateTimeToOffsetDateTimeTypeConverter extends AbstractWhenTypeConverter<ZonedDateTime, OffsetDateTime> {
    /**
     * 转换器的单例实例
     */
    public static final ZonedDateTimeToOffsetDateTimeTypeConverter INSTANCE = new ZonedDateTimeToOffsetDateTimeTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型。
     * <p>
     * 设置源类型为{@link ZonedDateTime}，目标类型为{@link OffsetDateTime}。
     * 该构造函数被声明为protected，意味着主要通过单例实例访问该转换器。
     * </p>
     */
    protected ZonedDateTimeToOffsetDateTimeTypeConverter() {
        super(ZonedDateTime.class, OffsetDateTime.class);
    }

    /**
     * 将源ZonedDateTime对象转换为OffsetDateTime对象，不进行源对象类型检查。
     * <p>
     * 该方法通过调用ZonedDateTime.toOffsetDateTime()方法提取时区偏移量信息。
     * </p>
     *
     * @param source 源ZonedDateTime对象，表示带有时区信息的日期时间。
     * @return 转换后的OffsetDateTime对象，表示带有时区偏移量的日期时间；如果source为null则返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public OffsetDateTime convertsUnchecked(ZonedDateTime source) throws Throwable {
        return source.toOffsetDateTime();
    }
}

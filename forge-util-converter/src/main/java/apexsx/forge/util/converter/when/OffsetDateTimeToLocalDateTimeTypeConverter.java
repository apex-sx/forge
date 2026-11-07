package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverterException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * OffsetDateTime到LocalDateTime的类型转换器。
 * <p>
 * 该转换器用于将{@link OffsetDateTime}类型的对象转换为{@link LocalDateTime}类型。
 * {@link OffsetDateTime}表示带有时区偏移量的日期时间，而{@link LocalDateTime}表示不带时区的日期时间。
 * 转换过程会丢失时区信息，只保留本地日期时间部分。
 * </p>
 *
 * @author zhitron
 */
public class OffsetDateTimeToLocalDateTimeTypeConverter extends AbstractWhenTypeConverter<OffsetDateTime, LocalDateTime> {
    /**
     * 转换器的单例实例
     */
    public static final OffsetDateTimeToLocalDateTimeTypeConverter INSTANCE = new OffsetDateTimeToLocalDateTimeTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型。
     * <p>
     * 设置源类型为{@link OffsetDateTime}，目标类型为{@link LocalDateTime}。
     * 该构造函数被声明为protected，意味着主要通过单例实例访问该转换器。
     * </p>
     */
    protected OffsetDateTimeToLocalDateTimeTypeConverter() {
        super(OffsetDateTime.class, LocalDateTime.class);
    }

    /**
     * 将源OffsetDateTime对象转换为LocalDateTime对象，不进行源对象类型检查。
     * <p>
     * 该方法通过调用OffsetDateTime.toLocalDateTime()方法提取日期时间部分，
     * 忽略时区偏移量信息。
     * </p>
     *
     * @param source 源OffsetDateTime对象，表示带有时区偏移量的日期时间。
     * @return 转换后的LocalDateTime对象，表示不带时区信息的日期时间；如果source为null则返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public LocalDateTime convertsUnchecked(OffsetDateTime source) throws Throwable {
        return source.toLocalDateTime();
    }
}

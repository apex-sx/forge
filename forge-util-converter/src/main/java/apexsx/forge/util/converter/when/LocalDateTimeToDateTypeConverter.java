package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverterException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

/**
 * LocalDateTime到Date的类型转换器。
 * <p>
 * 该转换器用于将{@link LocalDateTime}类型的对象转换为{@link Date}类型。
 * {@link LocalDateTime}表示不带时区的日期时间，而{@link Date}表示自1970年1月1日00:00:00 UTC以来的毫秒数。
 * 转换时需要提供一个时区偏移量，用于将LocalDateTime转换为Instant后再转换为Date。
 * </p>
 *
 * @author zhitron
 */
public class LocalDateTimeToDateTypeConverter extends AbstractWhenTypeConverter<LocalDateTime, Date> {
    /**
     * 构造函数，使用指定的时区偏移字符串创建转换器。
     * <p>
     * 该构造函数接受一个时区偏移量字符串，并将其解析为时区偏移对象。
     * 字符串格式应符合ISO-8601标准，例如："+8"、"UTC+8" 或 "+08:00"。
     * </p>
     *
     * @param offset 时区偏移量字符串，例如 "+8"、"UTC+8" 或 "+08:00" 等符合ISO-8601标准的格式。
     * @throws NullPointerException 如果offset为null，则抛出空指针异常。
     */
    public LocalDateTimeToDateTypeConverter(String offset) {
        super(LocalDateTime.class, Date.class, Objects.requireNonNull(offset));
    }

    /**
     * 构造函数，使用指定的ZoneOffset创建转换器。
     * <p>
     * 该构造函数接受一个ZoneOffset对象作为时区偏移量。
     * ZoneOffset表示与UTC时间的固定偏移量。
     * </p>
     *
     * @param zoneOffset ZoneOffset对象，指定转换时使用的时区偏移量。
     * @throws NullPointerException 如果zoneOffset为null，则抛出空指针异常。
     */
    public LocalDateTimeToDateTypeConverter(ZoneOffset zoneOffset) {
        super(LocalDateTime.class, Date.class, Objects.requireNonNull(zoneOffset));
    }

    /**
     * 将源LocalDateTime对象转换为Date对象，不进行源对象类型检查。
     * <p>
     * 该方法首先将LocalDateTime结合指定的时区偏移量转换为Instant，
     * 然后通过Date.from()方法将Instant转换为Date。
     * </p>
     *
     * @param source 源LocalDateTime对象，表示不带时区信息的日期时间。
     * @return 转换后的Date对象，表示自1970年1月1日00:00:00 UTC以来的毫秒数；如果source为null则返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public Date convertsUnchecked(LocalDateTime source) throws Throwable {
        return Date.from(source.toInstant(getZoneOffset()));
    }
}

package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverterException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * LocalDateTime到Long类型转换器。
 * <p>
 * 该转换器用于将{@link LocalDateTime}对象转换为{@link Long}类型的毫秒时间戳。
 * LocalDateTime表示不带时区的日期时间，而转换为Long时需要考虑时区因素。
 * 支持指定时区偏移量来进行转换，默认使用系统时区。
 * </p>
 *
 * @author zhitron
 */
public class LocalDateTimeToLongTypeConverter extends AbstractWhenTypeConverter<LocalDateTime, Long> {
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
    public LocalDateTimeToLongTypeConverter(String offset) {
        super(LocalDateTime.class, Long.class, Objects.requireNonNull(offset));
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
    public LocalDateTimeToLongTypeConverter(ZoneOffset zoneOffset) {
        super(LocalDateTime.class, Long.class, Objects.requireNonNull(zoneOffset));
    }

    /**
     * 将LocalDateTime对象转换为Long类型的毫秒时间戳，不进行源对象类型检查。
     * <p>
     * 该方法首先将LocalDateTime结合指定的时区偏移量转换为Instant，
     * 然后获取该Instant对应的毫秒时间戳。
     * </p>
     *
     * @param source LocalDateTime源对象，表示不带时区信息的日期时间。
     * @return 转换后的毫秒时间戳，表示从1970年1月1日00:00:00 UTC开始经过的毫秒数；如果source为null则可能返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public Long convertsUnchecked(LocalDateTime source) throws Throwable {
        return source.toInstant(getZoneOffset()).toEpochMilli();
    }
}

package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverterException;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Instant转ZonedDateTime类型转换器。
 * <p>
 * 该转换器用于将{@link Instant}类型的对象转换为{@link ZonedDateTime}类型。
 * {@link Instant}表示时间轴上的一个瞬时点，而{@link ZonedDateTime}表示带有时区信息的日期时间。
 * 转换时需要提供一个时区偏移量，用于指定ZonedDateTime的时区。
 * </p>
 *
 * @author zhitron
 */
public class InstantToZonedDateTimeTypeConverter extends AbstractWhenTypeConverter<Instant, ZonedDateTime> {
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
    public InstantToZonedDateTimeTypeConverter(String offset) {
        super(Instant.class, ZonedDateTime.class, Objects.requireNonNull(offset));
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
    public InstantToZonedDateTimeTypeConverter(ZoneOffset zoneOffset) {
        super(Instant.class, ZonedDateTime.class, Objects.requireNonNull(zoneOffset));
    }

    /**
     * 将源Instant对象转换为ZonedDateTime对象，不进行源对象类型检查。
     * <p>
     * 该方法通过ZonedDateTime.ofInstant()方法将Instant结合指定的时区偏移量转换为ZonedDateTime。
     * </p>
     *
     * @param source 源Instant对象，表示时间轴上的一个瞬时点。
     * @return 转换后的ZonedDateTime对象，表示带有时区信息的日期时间；如果source为null则返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public ZonedDateTime convertsUnchecked(Instant source) throws Throwable {
        return ZonedDateTime.ofInstant(source, getZoneOffset());
    }
}

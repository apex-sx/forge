package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverterException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * Instant转OffsetDateTime类型转换器。
 * <p>
 * 该转换器用于将{@link Instant}类型的对象转换为{@link OffsetDateTime}类型。
 * {@link Instant}表示时间轴上的一个瞬时点，而{@link OffsetDateTime}表示带有时区偏移量的日期时间。
 * 转换时需要提供一个时区偏移量，用于指定OffsetDateTime的时区偏移。
 * </p>
 *
 * @author zhitron
 */
public class InstantToOffsetDateTimeTypeConverter extends AbstractWhenTypeConverter<Instant, OffsetDateTime> {
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
    public InstantToOffsetDateTimeTypeConverter(String offset) {
        super(Instant.class, OffsetDateTime.class, Objects.requireNonNull(offset));
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
    public InstantToOffsetDateTimeTypeConverter(ZoneOffset zoneOffset) {
        super(Instant.class, OffsetDateTime.class, Objects.requireNonNull(zoneOffset));
    }

    /**
     * 将源Instant对象转换为OffsetDateTime对象，不进行源对象类型检查。
     * <p>
     * 该方法通过OffsetDateTime.ofInstant()方法将Instant结合指定的时区偏移量转换为OffsetDateTime。
     * </p>
     *
     * @param source 源Instant对象，表示时间轴上的一个瞬时点。
     * @return 转换后的OffsetDateTime对象，表示带有时区偏移量的日期时间；如果source为null则返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public OffsetDateTime convertsUnchecked(Instant source) throws Throwable {
        return OffsetDateTime.ofInstant(source, getZoneOffset());
    }
}

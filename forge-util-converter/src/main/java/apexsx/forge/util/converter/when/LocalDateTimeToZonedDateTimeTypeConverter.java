package apexsx.forge.util.converter.when;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * LocalDateTime到ZonedDateTime的类型转换器
 * <p>
 * 该转换器用于将LocalDateTime对象转换为ZonedDateTime对象，
 * 通过ZonedDateTime.of()方法实现转换，需要指定时区偏移量。
 * </p>
 *
 * @author zhitron
 */
public class LocalDateTimeToZonedDateTimeTypeConverter extends AbstractWhenTypeConverter<LocalDateTime, ZonedDateTime> {
    /**
     * 构造函数，使用指定的时区偏移字符串创建转换器
     *
     * @param offset 时区偏移量字符串，例如 "+8"、"UTC+8" 或 "+08:00" 等符合ISO-8601标准的格式
     * @throws NullPointerException 如果offset为null
     */
    public LocalDateTimeToZonedDateTimeTypeConverter(String offset) {
        super(LocalDateTime.class, ZonedDateTime.class, Objects.requireNonNull(offset));
    }

    /**
     * 构造函数，使用指定的ZoneOffset创建转换器
     *
     * @param zoneOffset ZoneOffset对象，指定转换时使用的时区偏移量
     * @throws NullPointerException 如果zoneOffset为null
     */
    public LocalDateTimeToZonedDateTimeTypeConverter(ZoneOffset zoneOffset) {
        super(LocalDateTime.class, ZonedDateTime.class, Objects.requireNonNull(zoneOffset));
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 通过ZonedDateTime.of()方法将LocalDateTime和时区偏移量组合成ZonedDateTime
     *
     * @param source 源LocalDateTime对象
     * @return 转换后的ZonedDateTime对象
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public ZonedDateTime convertsUnchecked(LocalDateTime source) throws Throwable {
        return ZonedDateTime.of(source, getZoneOffset());
    }
}

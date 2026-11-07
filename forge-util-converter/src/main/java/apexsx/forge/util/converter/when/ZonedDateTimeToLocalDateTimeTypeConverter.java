package apexsx.forge.util.converter.when;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * ZonedDateTime到LocalDateTime的类型转换器
 * <p>
 * 该转换器用于将ZonedDateTime对象转换为LocalDateTime对象，
 * 通过ZonedDateTime.toLocalDateTime()方法实现转换，
 * 转换过程会丢失时区信息。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class ZonedDateTimeToLocalDateTimeTypeConverter extends AbstractWhenTypeConverter<ZonedDateTime, LocalDateTime> {
    /**
     * 转换器的单例实例
     */
    public static final ZonedDateTimeToLocalDateTimeTypeConverter INSTANCE = new ZonedDateTimeToLocalDateTimeTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected ZonedDateTimeToLocalDateTimeTypeConverter() {
        super(ZonedDateTime.class, LocalDateTime.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 通过ZonedDateTime.toLocalDateTime()方法将ZonedDateTime转换为LocalDateTime
     * 转换过程会丢失时区信息
     *
     * @param source 源对象，必须是ZonedDateTime类型
     * @return 转换后的LocalDateTime对象
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public LocalDateTime convertsUnchecked(ZonedDateTime source) throws Throwable {
        return source.toLocalDateTime();
    }
}

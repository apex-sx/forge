package apexsx.forge.util.converter.when;

import java.time.ZonedDateTime;

/**
 * ZonedDateTime到Long类型的时间转换器
 * <p>
 * 该转换器用于将ZonedDateTime对象转换为Long类型的毫秒时间戳。
 * 通过ZonedDateTime.toInstant().toEpochMilli()方法实现转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class ZonedDateTimeToLongTypeConverter extends AbstractWhenTypeConverter<ZonedDateTime, Long> {
    /**
     * 转换器的单例实例
     */
    public static final ZonedDateTimeToLongTypeConverter INSTANCE = new ZonedDateTimeToLongTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected ZonedDateTimeToLongTypeConverter() {
        super(ZonedDateTime.class, Long.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 通过ZonedDateTime.toInstant().toEpochMilli()方法将ZonedDateTime转换为毫秒时间戳
     *
     * @param source 源对象，必须是ZonedDateTime类型
     * @return 转换后的Long对象，表示自1970-01-01T00:00:00Z以来的毫秒数
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public Long convertsUnchecked(ZonedDateTime source) throws Throwable {
        return source.toInstant().toEpochMilli();
    }
}

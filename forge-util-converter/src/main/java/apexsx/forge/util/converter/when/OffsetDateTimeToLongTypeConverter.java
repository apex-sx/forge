package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverter;

import java.time.OffsetDateTime;

/**
 * OffsetDateTime到长整型的时间戳转换器
 * <p>
 * 该转换器用于将OffsetDateTime类型转换为Long类型的时间戳（毫秒数）。
 * 通过将OffsetDateTime转换为Instant再获取epoch毫秒数来实现转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class OffsetDateTimeToLongTypeConverter extends TypeConverter<OffsetDateTime, Long> {
    /**
     * 转换器的单例实例
     */
    public static final OffsetDateTimeToLongTypeConverter INSTANCE = new OffsetDateTimeToLongTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected OffsetDateTimeToLongTypeConverter() {
        super(OffsetDateTime.class, Long.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 通过OffsetDateTime.toInstant().toEpochMilli()方法将时间转换为毫秒时间戳
     *
     * @param source 源对象，OffsetDateTime类型的时间值
     * @return 转换后的Long对象，表示自1970-01-01T00:00:00Z以来的毫秒数
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public Long convertsUnchecked(OffsetDateTime source) throws Throwable {
        return source.toInstant().toEpochMilli();
    }
}

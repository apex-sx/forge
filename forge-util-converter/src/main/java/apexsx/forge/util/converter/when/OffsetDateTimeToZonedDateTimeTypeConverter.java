package apexsx.forge.util.converter.when;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/**
 * OffsetDateTime到ZonedDateTime的类型转换器
 * <p>
 * 该转换器用于将OffsetDateTime对象转换为ZonedDateTime对象，
 * 通过OffsetDateTime.toZonedDateTime()方法实现转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class OffsetDateTimeToZonedDateTimeTypeConverter extends AbstractWhenTypeConverter<OffsetDateTime, ZonedDateTime> {
    /**
     * 转换器的单例实例
     */
    public static final OffsetDateTimeToZonedDateTimeTypeConverter INSTANCE = new OffsetDateTimeToZonedDateTimeTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected OffsetDateTimeToZonedDateTimeTypeConverter() {
        super(OffsetDateTime.class, ZonedDateTime.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 通过OffsetDateTime.toZonedDateTime()方法将OffsetDateTime转换为ZonedDateTime
     *
     * @param source 源对象，必须是OffsetDateTime类型
     * @return 转换后的ZonedDateTime对象
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public ZonedDateTime convertsUnchecked(OffsetDateTime source) throws Throwable {
        return source.toZonedDateTime();
    }
}

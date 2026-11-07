package apexsx.forge.util.converter.when;

import java.time.ZonedDateTime;
import java.util.Date;

/**
 * ZonedDateTime到Date的类型转换器
 * <p>
 * 该转换器用于将ZonedDateTime对象转换为Date对象，
 * 通过ZonedDateTime.toInstant()和Date.from()方法实现转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class ZonedDateTimeToDateTypeConverter extends AbstractWhenTypeConverter<ZonedDateTime, Date> {
    /**
     * 转换器的单例实例
     */
    public static final ZonedDateTimeToDateTypeConverter INSTANCE = new ZonedDateTimeToDateTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected ZonedDateTimeToDateTypeConverter() {
        super(ZonedDateTime.class, Date.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 通过ZonedDateTime.toInstant()和Date.from()方法将ZonedDateTime转换为Date
     *
     * @param source 源对象，必须是ZonedDateTime类型
     * @return 转换后的Date对象
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public Date convertsUnchecked(ZonedDateTime source) throws Throwable {
        return Date.from(source.toInstant());
    }
}

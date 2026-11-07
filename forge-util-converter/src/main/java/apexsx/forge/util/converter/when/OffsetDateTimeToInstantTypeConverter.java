package apexsx.forge.util.converter.when;

import java.time.Instant;
import java.time.OffsetDateTime;

/**
 * OffsetDateTime到Instant的类型转换器
 * <p>
 * 该转换器用于将OffsetDateTime对象转换为Instant对象，
 * 通过OffsetDateTime.toInstant()方法实现转换。
 * 这是一个单例类，通过INSTANCE字段获取实例。
 * </p>
 *
 * @author zhitron
 */
public class OffsetDateTimeToInstantTypeConverter extends AbstractWhenTypeConverter<OffsetDateTime, Instant> {
    /**
     * 转换器的单例实例
     */
    public static final OffsetDateTimeToInstantTypeConverter INSTANCE = new OffsetDateTimeToInstantTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected OffsetDateTimeToInstantTypeConverter() {
        super(OffsetDateTime.class, Instant.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 通过OffsetDateTime.toInstant()方法将OffsetDateTime转换为Instant
     *
     * @param source 源对象，必须是OffsetDateTime类型
     * @return 转换后的Instant对象
     * @throws NullPointerException 当source为null时抛出
     */
    @Override
    public Instant convertsUnchecked(OffsetDateTime source) throws Throwable {
        return source.toInstant();
    }
}

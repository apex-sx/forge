package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverter;
import com.github.zhitron.type_converter.TypeConverterException;

import java.time.Instant;

/**
 * Instant到Long类型转换器。
 * <p>
 * 该转换器用于将{@link Instant}类型的对象转换为{@link Long}类型。
 * {@link Instant}表示时间轴上的一个瞬时点，而{@link Long}表示自1970年1月1日00:00:00 UTC以来的毫秒数。
 * </p>
 *
 * @author zhitron
 */
public class InstantToLongTypeConverter extends TypeConverter<Instant, Long> {
    /**
     * 转换器单例实例。
     * <p>
     * 由于该转换器是无状态的，因此可以通过此单例实例复用转换器对象，
     * 避免重复创建实例以提升性能。
     * </p>
     */
    public static final InstantToLongTypeConverter INSTANCE = new InstantToLongTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型。
     * <p>
     * 设置源类型为{@link Instant}，目标类型为{@link Long}。
     * 该构造函数被声明为protected，意味着主要通过单例实例访问该转换器。
     * </p>
     */
    protected InstantToLongTypeConverter() {
        super(Instant.class, Long.class);
    }

    /**
     * 将源Instant对象转换为Long对象，不进行源对象类型检查。
     * <p>
     * 该方法通过调用Instant.toEpochMilli()方法获取自1970年1月1日00:00:00 UTC以来的毫秒数。
     * </p>
     *
     * @param source 源Instant对象，表示时间轴上的一个瞬时点。
     * @return 转换后的Long对象，表示自1970年1月1日00:00:00 UTC以来的毫秒数；如果source为null则返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public Long convertsUnchecked(Instant source) throws Throwable {
        return source.toEpochMilli();
    }
}

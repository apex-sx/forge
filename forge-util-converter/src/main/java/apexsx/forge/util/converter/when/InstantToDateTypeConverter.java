package apexsx.forge.util.converter.when;

import com.github.zhitron.type_converter.TypeConverterException;

import java.time.Instant;
import java.util.Date;

/**
 * Instant到Date的类型转换器。
 * <p>
 * 该转换器用于将{@link Instant}类型的对象转换为{@link Date}类型。
 * {@link Instant}表示时间轴上的一个瞬时点，而{@link Date}表示自1970年1月1日00:00:00 UTC以来的毫秒数。
 * </p>
 *
 * @author zhitron
 */
public class InstantToDateTypeConverter extends AbstractWhenTypeConverter<Instant, Date> {
    /**
     * 转换器的单例实例
     */
    public static final InstantToDateTypeConverter INSTANCE = new InstantToDateTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型。
     * <p>
     * 设置源类型为{@link Instant}，目标类型为{@link Date}。
     * 该构造函数被声明为protected，意味着主要通过单例实例访问该转换器。
     * </p>
     */
    protected InstantToDateTypeConverter() {
        super(Instant.class, Date.class);
    }

    /**
     * 将源Instant对象转换为Date对象，不进行源对象类型检查。
     * <p>
     * 该方法通过调用Date.from()方法将Instant转换为Date。
     * </p>
     *
     * @param source 源Instant对象，表示时间轴上的一个瞬时点。
     * @return 转换后的Date对象，表示自1970年1月1日00:00:00 UTC以来的毫秒数；如果source为null则返回null。
     * @throws TypeConverterException 转换过程中发生错误时抛出类型转换异常。
     */
    @Override
    public Date convertsUnchecked(Instant source) throws Throwable {
        return Date.from(source);
    }
}

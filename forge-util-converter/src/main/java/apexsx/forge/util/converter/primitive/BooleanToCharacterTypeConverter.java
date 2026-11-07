package apexsx.forge.util.converter.primitive;

import com.github.zhitron.type_converter.TypeConverter;

/**
 * 布尔值到字符类型的转换器
 * <p>
 * 该转换器用于将布尔值转换为对应的字符表示：
 * - true 转换为 'T'
 * - false 转换为 'F'
 * </p>
 *
 * @author zhitron
 */
public class BooleanToCharacterTypeConverter extends TypeConverter<Boolean, Character> {
    /**
     * 转换器的单例实例
     */
    public static final BooleanToCharacterTypeConverter INSTANCE = new BooleanToCharacterTypeConverter();

    /**
     * 构造函数，初始化源类型和目标类型
     * 使用protected修饰符防止外部直接实例化
     */
    protected BooleanToCharacterTypeConverter() {
        super(Boolean.class, Character.class);
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     * 转换规则：true转换为'T'，false转换为'F'
     *
     * @param source 源对象，Boolean类型
     * @return 转换后的Character对象
     */
    @Override
    public Character convertsUnchecked(Boolean source) throws Throwable {
        return source ? '1' : '0';
    }
}

package apexsx.forge.util.converter.string;

import com.github.zhitron.type_converter.TypeConverter;

import java.util.Locale;

/**
 * Locale到字符串的类型转换器
 * <p>
 * 支持将Locale对象转换为字符串格式，格式为language-country或仅language（如果没有国家部分）。
 * 转换规则：
 * - 如果Locale为null，返回null
 * - 如果Locale有语言和国家部分，返回"language-country"格式
 * - 如果Locale只有语言部分，返回"language"
 * - 如果Locale的语言部分为null，则视为空字符串
 * </p>
 *
 * @author zhitron
 */
public class LocaleToStringTypeConverter extends TypeConverter<Locale, String> {
    /**
     * 单例实例
     */
    public static final LocaleToStringTypeConverter INSTANCE = new LocaleToStringTypeConverter();

    /**
     * 构造函数，初始化源类型为Locale.meta，目标类型为String.meta
     */
    public LocaleToStringTypeConverter() {
        super(Locale.class, String.class);
    }

    /**
     * 将Locale对象转换为字符串表示形式
     *
     * @param source Locale源对象
     * @return 转换后的字符串，可能为null
     * @throws Throwable 转换过程中发生错误时抛出
     */
    @Override
    public String convertsUnchecked(Locale source) throws Throwable {
        if (source == null) {
            return null;
        }

        String language = source.getLanguage();
        String country = source.getCountry();

        if (language == null) {
            language = "";
        }

        if (country != null && !country.isEmpty()) {
            return language + "-" + country;
        } else {
            return language;
        }
    }

}

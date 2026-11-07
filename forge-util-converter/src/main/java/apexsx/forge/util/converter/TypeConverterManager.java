package apexsx.forge.util.converter;

import com.github.zhitron.type_converter.number.*;
import com.github.zhitron.type_converter.object.CharSequenceToLocaleTypeConverter;
import com.github.zhitron.type_converter.object.StringToLocaleTypeConverter;
import com.github.zhitron.type_converter.primitive.StringToByteTypeConverter;
import com.github.zhitron.type_converter.primitive.StringToDoubleTypeConverter;
import com.github.zhitron.type_converter.primitive.StringToFloatTypeConverter;
import com.github.zhitron.type_converter.primitive.StringToIntegerTypeConverter;
import com.github.zhitron.type_converter.primitive.StringToLongTypeConverter;
import com.github.zhitron.type_converter.primitive.StringToShortTypeConverter;
import com.github.zhitron.type_converter.primitive.*;
import com.github.zhitron.type_converter.string.LocaleToStringTypeConverter;
import com.github.zhitron.type_converter.string.NumberToStringTypeConverter;
import com.github.zhitron.type_converter.string.ObjectToStringTypeConverter;
import com.github.zhitron.type_converter.table.ResultSetToArrayTableTypeConverter;
import com.github.zhitron.type_converter.table.ResultSetToMapTableTypeConverter;
import com.github.zhitron.type_converter.when.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * 类型转换管理器，负责注册和管理各种类型转换器，并提供类型转换功能
 *
 * @author zhitron
 */
public class TypeConverterManager {

    /**
     * 默认的类型转换管理器实例，预注册了所有内置的类型转换器
     * 包括基本数据类型之间的转换、字符串转换、日期时间转换等
     */
    public static final TypeConverterManager DEFAULT = TypeConverterManager.of(typeConverterManager -> {

        typeConverterManager
                // 注册CharSequence到数值类型的转换器
                .register(CharSequenceToBigDecimalTypeConverter.INSTANCE)
                .register(CharSequenceToBigIntegerTypeConverter.INSTANCE)
                .register(CharSequenceToByteTypeConverter.INSTANCE)
                .register(CharSequenceToDoubleTypeConverter.INSTANCE)
                .register(CharSequenceToFloatTypeConverter.INSTANCE)
                .register(CharSequenceToIntegerTypeConverter.INSTANCE)
                .register(CharSequenceToLongTypeConverter.INSTANCE)
                .register(CharSequenceToNumberTypeConverter.INSTANCE)
                .register(CharSequenceToShortTypeConverter.INSTANCE)
                // 注册Number到BigDecimal/BigInteger的转换器
                .register(NumberToBigDecimalTypeConverter.INSTANCE)
                .register(NumberToBigIntegerTypeConverter.INSTANCE)
                // 注册String到数值类型的转换器
                .register(StringToBigDecimalTypeConverter.INSTANCE)
                .register(StringToBigIntegerTypeConverter.INSTANCE)
                .register(StringToByteTypeConverter.INSTANCE)
                .register(StringToDoubleTypeConverter.INSTANCE)
                .register(StringToFloatTypeConverter.INSTANCE)
                .register(StringToIntegerTypeConverter.INSTANCE)
                .register(StringToLongTypeConverter.INSTANCE)
                .register(StringToNumberTypeConverter.INSTANCE)
                .register(StringToShortTypeConverter.INSTANCE);

        // 注册本地化语言环境类型转换器
        typeConverterManager
                .register(CharSequenceToLocaleTypeConverter.INSTANCE)
                .register(StringToLocaleTypeConverter.INSTANCE);

        // 注册布尔型与其他基本数据类型之间的转换器
        typeConverterManager
                .register(BooleanToByteTypeConverter.INSTANCE)
                .register(BooleanToCharacterTypeConverter.INSTANCE)
                .register(BooleanToDoubleTypeConverter.INSTANCE)
                .register(BooleanToFloatTypeConverter.INSTANCE)
                .register(BooleanToIntegerTypeConverter.INSTANCE)
                .register(BooleanToLongTypeConverter.INSTANCE)
                .register(BooleanToShortTypeConverter.INSTANCE)
                // 注册字符型与其他基本数据类型之间的转换器
                .register(CharacterToBooleanTypeConverter.INSTANCE)
                .register(CharacterToByteTypeConverter.INSTANCE)
                .register(CharacterToDoubleTypeConverter.INSTANCE)
                .register(CharacterToFloatTypeConverter.INSTANCE)
                .register(CharacterToIntegerTypeConverter.INSTANCE)
                .register(CharacterToLongTypeConverter.INSTANCE)
                .register(CharacterToShortTypeConverter.INSTANCE)
                // 注册数字型与其他基本数据类型之间的转换器
                .register(NumberToBooleanTypeConverter.INSTANCE)
                .register(NumberToByteTypeConverter.INSTANCE)
                .register(NumberToCharacterTypeConverter.INSTANCE)
                .register(NumberToDoubleTypeConverter.INSTANCE)
                .register(NumberToFloatTypeConverter.INSTANCE)
                .register(NumberToIntegerTypeConverter.INSTANCE)
                .register(NumberToLongTypeConverter.INSTANCE)
                .register(NumberToShortTypeConverter.INSTANCE)
                // 注册字符串型与其他基本数据类型之间的转换器
                .register(StringToBooleanTypeConverter.INSTANCE)
                .register(StringToByteTypeConverter.INSTANCE)
                .register(StringToCharacterTypeConverter.INSTANCE)
                .register(StringToDoubleTypeConverter.INSTANCE)
                .register(StringToFloatTypeConverter.INSTANCE)
                .register(StringToIntegerTypeConverter.INSTANCE)
                .register(StringToLongTypeConverter.INSTANCE)
                .register(StringToShortTypeConverter.INSTANCE)
                // 注意：这里重复注册了一次StringToShortTypeConverter，可能是代码错误
                .register(StringToShortTypeConverter.INSTANCE);

        // 注册数字到字符串以及其他对象到字符串的转换器
        typeConverterManager
                .register(LocaleToStringTypeConverter.INSTANCE)
                .register(NumberToStringTypeConverter.INSTANCE)
                .register(ObjectToStringTypeConverter.DEFAULT)
                .register(ObjectToStringTypeConverter.LOWER_CASE)
                .register(ObjectToStringTypeConverter.UPPER_CASE);

        // 注册结果集到数组和映射表的转换器
        typeConverterManager
                .register(ResultSetToArrayTableTypeConverter.INSTANCE)
                .register(ResultSetToMapTableTypeConverter.INSTANCE);

        // 注册Date类型与其他日期时间类型的转换器，包括各种时区偏移量
        typeConverterManager
                .register(DateToInstantTypeConverter.INSTANCE)
                // 注册Date到LocalDateTime的转换器，支持不同时区偏移量（+0到+16）
                .register(new DateToLocalDateTimeTypeConverter("+0"))
                .register(new DateToLocalDateTimeTypeConverter("+1"))
                .register(new DateToLocalDateTimeTypeConverter("+2"))
                .register(new DateToLocalDateTimeTypeConverter("+3"))
                .register(new DateToLocalDateTimeTypeConverter("+4"))
                .register(new DateToLocalDateTimeTypeConverter("+5"))
                .register(new DateToLocalDateTimeTypeConverter("+6"))
                .register(new DateToLocalDateTimeTypeConverter("+7"))
                .register(new DateToLocalDateTimeTypeConverter("+8"))
                .register(new DateToLocalDateTimeTypeConverter("+9"))
                .register(new DateToLocalDateTimeTypeConverter("+10"))
                .register(new DateToLocalDateTimeTypeConverter("+11"))
                .register(new DateToLocalDateTimeTypeConverter("+12"))
                .register(new DateToLocalDateTimeTypeConverter("+13"))
                .register(new DateToLocalDateTimeTypeConverter("+14"))
                .register(new DateToLocalDateTimeTypeConverter("+15"))
                .register(new DateToLocalDateTimeTypeConverter("+16"))
                // 注册Date到LocalDateTime的转换器，支持不同时区偏移量（-1到-16）
                .register(new DateToLocalDateTimeTypeConverter("-1"))
                .register(new DateToLocalDateTimeTypeConverter("-2"))
                .register(new DateToLocalDateTimeTypeConverter("-3"))
                .register(new DateToLocalDateTimeTypeConverter("-4"))
                .register(new DateToLocalDateTimeTypeConverter("-5"))
                .register(new DateToLocalDateTimeTypeConverter("-6"))
                .register(new DateToLocalDateTimeTypeConverter("-7"))
                .register(new DateToLocalDateTimeTypeConverter("-8"))
                .register(new DateToLocalDateTimeTypeConverter("-9"))
                .register(new DateToLocalDateTimeTypeConverter("-10"))
                .register(new DateToLocalDateTimeTypeConverter("-11"))
                .register(new DateToLocalDateTimeTypeConverter("-12"))
                .register(new DateToLocalDateTimeTypeConverter("-13"))
                .register(new DateToLocalDateTimeTypeConverter("-14"))
                .register(new DateToLocalDateTimeTypeConverter("-15"))
                .register(new DateToLocalDateTimeTypeConverter("-16"))
                // 注册Date到Long的转换器
                .register(DateToLongTypeConverter.INSTANCE)
                // 注册Date到OffsetDateTime的转换器，支持不同时区偏移量（+0到+16）
                .register(new DateToOffsetDateTimeTypeConverter("+0"))
                .register(new DateToOffsetDateTimeTypeConverter("+1"))
                .register(new DateToOffsetDateTimeTypeConverter("+2"))
                .register(new DateToOffsetDateTimeTypeConverter("+3"))
                .register(new DateToOffsetDateTimeTypeConverter("+4"))
                .register(new DateToOffsetDateTimeTypeConverter("+5"))
                .register(new DateToOffsetDateTimeTypeConverter("+6"))
                .register(new DateToOffsetDateTimeTypeConverter("+7"))
                .register(new DateToOffsetDateTimeTypeConverter("+8"))
                .register(new DateToOffsetDateTimeTypeConverter("+9"))
                .register(new DateToOffsetDateTimeTypeConverter("+10"))
                .register(new DateToOffsetDateTimeTypeConverter("+11"))
                .register(new DateToOffsetDateTimeTypeConverter("+12"))
                .register(new DateToOffsetDateTimeTypeConverter("+13"))
                .register(new DateToOffsetDateTimeTypeConverter("+14"))
                .register(new DateToOffsetDateTimeTypeConverter("+15"))
                .register(new DateToOffsetDateTimeTypeConverter("+16"))
                // 注册Date到OffsetDateTime的转换器，支持不同时区偏移量（-1到-16）
                .register(new DateToOffsetDateTimeTypeConverter("-1"))
                .register(new DateToOffsetDateTimeTypeConverter("-2"))
                .register(new DateToOffsetDateTimeTypeConverter("-3"))
                .register(new DateToOffsetDateTimeTypeConverter("-4"))
                .register(new DateToOffsetDateTimeTypeConverter("-5"))
                .register(new DateToOffsetDateTimeTypeConverter("-6"))
                .register(new DateToOffsetDateTimeTypeConverter("-7"))
                .register(new DateToOffsetDateTimeTypeConverter("-8"))
                .register(new DateToOffsetDateTimeTypeConverter("-9"))
                .register(new DateToOffsetDateTimeTypeConverter("-10"))
                .register(new DateToOffsetDateTimeTypeConverter("-11"))
                .register(new DateToOffsetDateTimeTypeConverter("-12"))
                .register(new DateToOffsetDateTimeTypeConverter("-13"))
                .register(new DateToOffsetDateTimeTypeConverter("-14"))
                .register(new DateToOffsetDateTimeTypeConverter("-15"))
                .register(new DateToOffsetDateTimeTypeConverter("-16"))
                // 注册Date到ZonedDateTime的转换器，支持不同时区偏移量（+0到+16）
                .register(new DateToZonedDateTimeTypeConverter("+0"))
                .register(new DateToZonedDateTimeTypeConverter("+1"))
                .register(new DateToZonedDateTimeTypeConverter("+2"))
                .register(new DateToZonedDateTimeTypeConverter("+3"))
                .register(new DateToZonedDateTimeTypeConverter("+4"))
                .register(new DateToZonedDateTimeTypeConverter("+5"))
                .register(new DateToZonedDateTimeTypeConverter("+6"))
                .register(new DateToZonedDateTimeTypeConverter("+7"))
                .register(new DateToZonedDateTimeTypeConverter("+8"))
                .register(new DateToZonedDateTimeTypeConverter("+9"))
                .register(new DateToZonedDateTimeTypeConverter("+10"))
                .register(new DateToZonedDateTimeTypeConverter("+11"))
                .register(new DateToZonedDateTimeTypeConverter("+12"))
                .register(new DateToZonedDateTimeTypeConverter("+13"))
                .register(new DateToZonedDateTimeTypeConverter("+14"))
                .register(new DateToZonedDateTimeTypeConverter("+15"))
                .register(new DateToZonedDateTimeTypeConverter("+16"))
                // 注册Date到ZonedDateTime的转换器，支持不同时区偏移量（-1到-16）
                .register(new DateToZonedDateTimeTypeConverter("-1"))
                .register(new DateToZonedDateTimeTypeConverter("-2"))
                .register(new DateToZonedDateTimeTypeConverter("-3"))
                .register(new DateToZonedDateTimeTypeConverter("-4"))
                .register(new DateToZonedDateTimeTypeConverter("-5"))
                .register(new DateToZonedDateTimeTypeConverter("-6"))
                .register(new DateToZonedDateTimeTypeConverter("-7"))
                .register(new DateToZonedDateTimeTypeConverter("-8"))
                .register(new DateToZonedDateTimeTypeConverter("-9"))
                .register(new DateToZonedDateTimeTypeConverter("-10"))
                .register(new DateToZonedDateTimeTypeConverter("-11"))
                .register(new DateToZonedDateTimeTypeConverter("-12"))
                .register(new DateToZonedDateTimeTypeConverter("-13"))
                .register(new DateToZonedDateTimeTypeConverter("-14"))
                .register(new DateToZonedDateTimeTypeConverter("-15"))
                .register(new DateToZonedDateTimeTypeConverter("-16"));

        // 注册Instant类型与其他日期时间类型的转换器，包括各种时区偏移量
        typeConverterManager
                .register(InstantToDateTypeConverter.INSTANCE)
                // 注册Instant到LocalDateTime的转换器，支持不同时区偏移量（+0到+16）
                .register(new InstantToLocalDateTimeTypeConverter("+0"))
                .register(new InstantToLocalDateTimeTypeConverter("+1"))
                .register(new InstantToLocalDateTimeTypeConverter("+2"))
                .register(new InstantToLocalDateTimeTypeConverter("+3"))
                .register(new InstantToLocalDateTimeTypeConverter("+4"))
                .register(new InstantToLocalDateTimeTypeConverter("+5"))
                .register(new InstantToLocalDateTimeTypeConverter("+6"))
                .register(new InstantToLocalDateTimeTypeConverter("+7"))
                .register(new InstantToLocalDateTimeTypeConverter("+8"))
                .register(new InstantToLocalDateTimeTypeConverter("+9"))
                .register(new InstantToLocalDateTimeTypeConverter("+10"))
                .register(new InstantToLocalDateTimeTypeConverter("+11"))
                .register(new InstantToLocalDateTimeTypeConverter("+12"))
                .register(new InstantToLocalDateTimeTypeConverter("+13"))
                .register(new InstantToLocalDateTimeTypeConverter("+14"))
                .register(new InstantToLocalDateTimeTypeConverter("+15"))
                .register(new InstantToLocalDateTimeTypeConverter("+16"))
                // 注册Instant到LocalDateTime的转换器，支持不同时区偏移量（-1到-16）
                .register(new InstantToLocalDateTimeTypeConverter("-1"))
                .register(new InstantToLocalDateTimeTypeConverter("-2"))
                .register(new InstantToLocalDateTimeTypeConverter("-3"))
                .register(new InstantToLocalDateTimeTypeConverter("-4"))
                .register(new InstantToLocalDateTimeTypeConverter("-5"))
                .register(new InstantToLocalDateTimeTypeConverter("-6"))
                .register(new InstantToLocalDateTimeTypeConverter("-7"))
                .register(new InstantToLocalDateTimeTypeConverter("-8"))
                .register(new InstantToLocalDateTimeTypeConverter("-9"))
                .register(new InstantToLocalDateTimeTypeConverter("-10"))
                .register(new InstantToLocalDateTimeTypeConverter("-11"))
                .register(new InstantToLocalDateTimeTypeConverter("-12"))
                .register(new InstantToLocalDateTimeTypeConverter("-13"))
                .register(new InstantToLocalDateTimeTypeConverter("-14"))
                .register(new InstantToLocalDateTimeTypeConverter("-15"))
                .register(new InstantToLocalDateTimeTypeConverter("-16"))
                // 注册Instant到Long的转换器
                .register(InstantToLongTypeConverter.INSTANCE)
                // 注册Instant到OffsetDateTime的转换器，支持不同时区偏移量（+0到+16）
                .register(new InstantToOffsetDateTimeTypeConverter("+0"))
                .register(new InstantToOffsetDateTimeTypeConverter("+1"))
                .register(new InstantToOffsetDateTimeTypeConverter("+2"))
                .register(new InstantToOffsetDateTimeTypeConverter("+3"))
                .register(new InstantToOffsetDateTimeTypeConverter("+4"))
                .register(new InstantToOffsetDateTimeTypeConverter("+5"))
                .register(new InstantToOffsetDateTimeTypeConverter("+6"))
                .register(new InstantToOffsetDateTimeTypeConverter("+7"))
                .register(new InstantToOffsetDateTimeTypeConverter("+8"))
                .register(new InstantToOffsetDateTimeTypeConverter("+9"))
                .register(new InstantToOffsetDateTimeTypeConverter("+10"))
                .register(new InstantToOffsetDateTimeTypeConverter("+11"))
                .register(new InstantToOffsetDateTimeTypeConverter("+12"))
                .register(new InstantToOffsetDateTimeTypeConverter("+13"))
                .register(new InstantToOffsetDateTimeTypeConverter("+14"))
                .register(new InstantToOffsetDateTimeTypeConverter("+15"))
                .register(new InstantToOffsetDateTimeTypeConverter("+16"))
                // 注册Instant到OffsetDateTime的转换器，支持不同时区偏移量（-1到-16）
                .register(new InstantToOffsetDateTimeTypeConverter("-1"))
                .register(new InstantToOffsetDateTimeTypeConverter("-2"))
                .register(new InstantToOffsetDateTimeTypeConverter("-3"))
                .register(new InstantToOffsetDateTimeTypeConverter("-4"))
                .register(new InstantToOffsetDateTimeTypeConverter("-5"))
                .register(new InstantToOffsetDateTimeTypeConverter("-6"))
                .register(new InstantToOffsetDateTimeTypeConverter("-7"))
                .register(new InstantToOffsetDateTimeTypeConverter("-8"))
                .register(new InstantToOffsetDateTimeTypeConverter("-9"))
                .register(new InstantToOffsetDateTimeTypeConverter("-10"))
                .register(new InstantToOffsetDateTimeTypeConverter("-11"))
                .register(new InstantToOffsetDateTimeTypeConverter("-12"))
                .register(new InstantToOffsetDateTimeTypeConverter("-13"))
                .register(new InstantToOffsetDateTimeTypeConverter("-14"))
                .register(new InstantToOffsetDateTimeTypeConverter("-15"))
                .register(new InstantToOffsetDateTimeTypeConverter("-16"))
                // 注册Instant到ZonedDateTime的转换器，支持不同时区偏移量（+0到+16）
                .register(new InstantToZonedDateTimeTypeConverter("+0"))
                .register(new InstantToZonedDateTimeTypeConverter("+1"))
                .register(new InstantToZonedDateTimeTypeConverter("+2"))
                .register(new InstantToZonedDateTimeTypeConverter("+3"))
                .register(new InstantToZonedDateTimeTypeConverter("+4"))
                .register(new InstantToZonedDateTimeTypeConverter("+5"))
                .register(new InstantToZonedDateTimeTypeConverter("+6"))
                .register(new InstantToZonedDateTimeTypeConverter("+7"))
                .register(new InstantToZonedDateTimeTypeConverter("+8"))
                .register(new InstantToZonedDateTimeTypeConverter("+9"))
                .register(new InstantToZonedDateTimeTypeConverter("+10"))
                .register(new InstantToZonedDateTimeTypeConverter("+11"))
                .register(new InstantToZonedDateTimeTypeConverter("+12"))
                .register(new InstantToZonedDateTimeTypeConverter("+13"))
                .register(new InstantToZonedDateTimeTypeConverter("+14"))
                .register(new InstantToZonedDateTimeTypeConverter("+15"))
                .register(new InstantToZonedDateTimeTypeConverter("+16"))
                // 注册Instant到ZonedDateTime的转换器，支持不同时区偏移量（-1到-16）
                .register(new InstantToZonedDateTimeTypeConverter("-1"))
                .register(new InstantToZonedDateTimeTypeConverter("-2"))
                .register(new InstantToZonedDateTimeTypeConverter("-3"))
                .register(new InstantToZonedDateTimeTypeConverter("-4"))
                .register(new InstantToZonedDateTimeTypeConverter("-5"))
                .register(new InstantToZonedDateTimeTypeConverter("-6"))
                .register(new InstantToZonedDateTimeTypeConverter("-7"))
                .register(new InstantToZonedDateTimeTypeConverter("-8"))
                .register(new InstantToZonedDateTimeTypeConverter("-9"))
                .register(new InstantToZonedDateTimeTypeConverter("-10"))
                .register(new InstantToZonedDateTimeTypeConverter("-11"))
                .register(new InstantToZonedDateTimeTypeConverter("-12"))
                .register(new InstantToZonedDateTimeTypeConverter("-13"))
                .register(new InstantToZonedDateTimeTypeConverter("-14"))
                .register(new InstantToZonedDateTimeTypeConverter("-15"))
                .register(new InstantToZonedDateTimeTypeConverter("-16"));

        // 注册LocalDateTime类型与其他日期时间类型的转换器，包括各种时区偏移量
        typeConverterManager
                // 注册LocalDateTime到Date的转换器，支持不同时区偏移量（+0到+16）
                .register(new LocalDateTimeToDateTypeConverter("+0"))
                .register(new LocalDateTimeToDateTypeConverter("+1"))
                .register(new LocalDateTimeToDateTypeConverter("+2"))
                .register(new LocalDateTimeToDateTypeConverter("+3"))
                .register(new LocalDateTimeToDateTypeConverter("+4"))
                .register(new LocalDateTimeToDateTypeConverter("+5"))
                .register(new LocalDateTimeToDateTypeConverter("+6"))
                .register(new LocalDateTimeToDateTypeConverter("+7"))
                .register(new LocalDateTimeToDateTypeConverter("+8"))
                .register(new LocalDateTimeToDateTypeConverter("+9"))
                .register(new LocalDateTimeToDateTypeConverter("+10"))
                .register(new LocalDateTimeToDateTypeConverter("+11"))
                .register(new LocalDateTimeToDateTypeConverter("+12"))
                .register(new LocalDateTimeToDateTypeConverter("+13"))
                .register(new LocalDateTimeToDateTypeConverter("+14"))
                .register(new LocalDateTimeToDateTypeConverter("+15"))
                .register(new LocalDateTimeToDateTypeConverter("+16"))
                // 注册LocalDateTime到Date的转换器，支持不同时区偏移量（-1到-16）
                .register(new LocalDateTimeToDateTypeConverter("-1"))
                .register(new LocalDateTimeToDateTypeConverter("-2"))
                .register(new LocalDateTimeToDateTypeConverter("-3"))
                .register(new LocalDateTimeToDateTypeConverter("-4"))
                .register(new LocalDateTimeToDateTypeConverter("-5"))
                .register(new LocalDateTimeToDateTypeConverter("-6"))
                .register(new LocalDateTimeToDateTypeConverter("-7"))
                .register(new LocalDateTimeToDateTypeConverter("-8"))
                .register(new LocalDateTimeToDateTypeConverter("-9"))
                .register(new LocalDateTimeToDateTypeConverter("-10"))
                .register(new LocalDateTimeToDateTypeConverter("-11"))
                .register(new LocalDateTimeToDateTypeConverter("-12"))
                .register(new LocalDateTimeToDateTypeConverter("-13"))
                .register(new LocalDateTimeToDateTypeConverter("-14"))
                .register(new LocalDateTimeToDateTypeConverter("-15"))
                .register(new LocalDateTimeToDateTypeConverter("-16"))
                // 注册LocalDateTime到Instant的转换器，支持不同时区偏移量（+0到+16）
                .register(new LocalDateTimeToInstantTypeConverter("+0"))
                .register(new LocalDateTimeToInstantTypeConverter("+1"))
                .register(new LocalDateTimeToInstantTypeConverter("+2"))
                .register(new LocalDateTimeToInstantTypeConverter("+3"))
                .register(new LocalDateTimeToInstantTypeConverter("+4"))
                .register(new LocalDateTimeToInstantTypeConverter("+5"))
                .register(new LocalDateTimeToInstantTypeConverter("+6"))
                .register(new LocalDateTimeToInstantTypeConverter("+7"))
                .register(new LocalDateTimeToInstantTypeConverter("+8"))
                .register(new LocalDateTimeToInstantTypeConverter("+9"))
                .register(new LocalDateTimeToInstantTypeConverter("+10"))
                .register(new LocalDateTimeToInstantTypeConverter("+11"))
                .register(new LocalDateTimeToInstantTypeConverter("+12"))
                .register(new LocalDateTimeToInstantTypeConverter("+13"))
                .register(new LocalDateTimeToInstantTypeConverter("+14"))
                .register(new LocalDateTimeToInstantTypeConverter("+15"))
                .register(new LocalDateTimeToInstantTypeConverter("+16"))
                // 注册LocalDateTime到Instant的转换器，支持不同时区偏移量（-1到-16）
                .register(new LocalDateTimeToInstantTypeConverter("-1"))
                .register(new LocalDateTimeToInstantTypeConverter("-2"))
                .register(new LocalDateTimeToInstantTypeConverter("-3"))
                .register(new LocalDateTimeToInstantTypeConverter("-4"))
                .register(new LocalDateTimeToInstantTypeConverter("-5"))
                .register(new LocalDateTimeToInstantTypeConverter("-6"))
                .register(new LocalDateTimeToInstantTypeConverter("-7"))
                .register(new LocalDateTimeToInstantTypeConverter("-8"))
                .register(new LocalDateTimeToInstantTypeConverter("-9"))
                .register(new LocalDateTimeToInstantTypeConverter("-10"))
                .register(new LocalDateTimeToInstantTypeConverter("-11"))
                .register(new LocalDateTimeToInstantTypeConverter("-12"))
                .register(new LocalDateTimeToInstantTypeConverter("-13"))
                .register(new LocalDateTimeToInstantTypeConverter("-14"))
                .register(new LocalDateTimeToInstantTypeConverter("-15"))
                .register(new LocalDateTimeToInstantTypeConverter("-16"))
                // 注册LocalDateTime到Long的转换器，支持不同时区偏移量（+0到+16）
                .register(new LocalDateTimeToLongTypeConverter("+0"))
                .register(new LocalDateTimeToLongTypeConverter("+1"))
                .register(new LocalDateTimeToLongTypeConverter("+2"))
                .register(new LocalDateTimeToLongTypeConverter("+3"))
                .register(new LocalDateTimeToLongTypeConverter("+4"))
                .register(new LocalDateTimeToLongTypeConverter("+5"))
                .register(new LocalDateTimeToLongTypeConverter("+6"))
                .register(new LocalDateTimeToLongTypeConverter("+7"))
                .register(new LocalDateTimeToLongTypeConverter("+8"))
                .register(new LocalDateTimeToLongTypeConverter("+9"))
                .register(new LocalDateTimeToLongTypeConverter("+10"))
                .register(new LocalDateTimeToLongTypeConverter("+11"))
                .register(new LocalDateTimeToLongTypeConverter("+12"))
                .register(new LocalDateTimeToLongTypeConverter("+13"))
                .register(new LocalDateTimeToLongTypeConverter("+14"))
                .register(new LocalDateTimeToLongTypeConverter("+15"))
                .register(new LocalDateTimeToLongTypeConverter("+16"))
                // 注册LocalDateTime到Long的转换器，支持不同时区偏移量（-1到-16）
                .register(new LocalDateTimeToLongTypeConverter("-1"))
                .register(new LocalDateTimeToLongTypeConverter("-2"))
                .register(new LocalDateTimeToLongTypeConverter("-3"))
                .register(new LocalDateTimeToLongTypeConverter("-4"))
                .register(new LocalDateTimeToLongTypeConverter("-5"))
                .register(new LocalDateTimeToLongTypeConverter("-6"))
                .register(new LocalDateTimeToLongTypeConverter("-7"))
                .register(new LocalDateTimeToLongTypeConverter("-8"))
                .register(new LocalDateTimeToLongTypeConverter("-9"))
                .register(new LocalDateTimeToLongTypeConverter("-10"))
                .register(new LocalDateTimeToLongTypeConverter("-11"))
                .register(new LocalDateTimeToLongTypeConverter("-12"))
                .register(new LocalDateTimeToLongTypeConverter("-13"))
                .register(new LocalDateTimeToLongTypeConverter("-14"))
                .register(new LocalDateTimeToLongTypeConverter("-15"))
                .register(new LocalDateTimeToLongTypeConverter("-16"))
                // 注册LocalDateTime到OffsetDateTime的转换器，支持不同时区偏移量（+0到+16）
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+0"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+1"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+2"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+3"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+4"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+5"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+6"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+7"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+8"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+9"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+10"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+11"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+12"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+13"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+14"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+15"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("+16"))
                // 注册LocalDateTime到OffsetDateTime的转换器，支持不同时区偏移量（-1到-16）
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-1"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-2"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-3"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-4"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-5"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-6"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-7"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-8"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-9"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-10"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-11"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-12"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-13"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-14"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-15"))
                .register(new LocalDateTimeToOffsetDateTimeTypeConverter("-16"))
                // 注册LocalDateTime到ZonedDateTime的转换器，支持不同时区偏移量（+0到+16）
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+0"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+1"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+2"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+3"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+4"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+5"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+6"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+7"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+8"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+9"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+10"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+11"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+12"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+13"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+14"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+15"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("+16"))
                // 注册LocalDateTime到ZonedDateTime的转换器，支持不同时区偏移量（-1到-16）
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-1"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-2"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-3"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-4"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-5"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-6"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-7"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-8"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-9"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-10"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-11"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-12"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-13"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-14"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-15"))
                .register(new LocalDateTimeToZonedDateTimeTypeConverter("-16"));

        // 注册OffsetDateTime和ZonedDateTime之间的相互转换器
        typeConverterManager
                .register(OffsetDateTimeToDateTypeConverter.INSTANCE)
                .register(OffsetDateTimeToInstantTypeConverter.INSTANCE)
                .register(OffsetDateTimeToLocalDateTimeTypeConverter.INSTANCE)
                .register(OffsetDateTimeToLongTypeConverter.INSTANCE)
                .register(OffsetDateTimeToZonedDateTimeTypeConverter.INSTANCE)
                .register(ZonedDateTimeToDateTypeConverter.INSTANCE)
                .register(ZonedDateTimeToInstantTypeConverter.INSTANCE)
                .register(ZonedDateTimeToLocalDateTimeTypeConverter.INSTANCE)
                .register(ZonedDateTimeToLongTypeConverter.INSTANCE)
                .register(ZonedDateTimeToOffsetDateTimeTypeConverter.INSTANCE);
    });

    /**
     * 存储目标类型到类型映射关系的映射表
     * key: 目标类型
     * value: 该目标类型对应的源类型转换器映射关系
     */
    private final Map<Class<?>, TargetTypeConverterRegistry> targetTypeRegistryContainer;

    /**
     * 基于现有TypeConverterManager实例创建新的实例的构造函数
     * 用于复制现有的类型转换器配置
     *
     * @param typeConverterManager 要复制的现有TypeConverterManager实例
     */
    private TypeConverterManager(TypeConverterManager typeConverterManager) {
        this.targetTypeRegistryContainer = new ConcurrentHashMap<>();
        if (typeConverterManager != null) {
            typeConverterManager.targetTypeRegistryContainer.forEach((targetType, targetTypeConverterRegistry) ->
                    this.targetTypeRegistryContainer.put(targetType, new TargetTypeConverterRegistry(targetTypeConverterRegistry)));
        }
    }

    /**
     * 私有构造函数，初始化目标类型注册容器
     */
    private TypeConverterManager() {
        this.targetTypeRegistryContainer = new ConcurrentHashMap<>();
    }

    /**
     * 创建一个新的TypeConverterManager实例，并通过consumer进行配置
     *
     * @param consumer 用于配置TypeConverterManager的消费者函数
     * @return 配置完成的TypeConverterManager实例
     */
    public static TypeConverterManager of(Consumer<TypeConverterManager> consumer) {
        TypeConverterManager typeConverterManager = new TypeConverterManager();
        consumer.accept(typeConverterManager);
        return typeConverterManager;
    }

    /**
     * 基于现有的TypeConverterManager创建一个新的实例
     *
     * @param typeConverterManager 用于复制配置的现有实例
     * @return 新的TypeConverterManager实例，包含原有实例的配置
     */
    public static TypeConverterManager of(TypeConverterManager typeConverterManager) {
        return new TypeConverterManager(typeConverterManager);
    }

    /**
     * 创建一个新的空TypeConverterManager实例
     *
     * @return 新的TypeConverterManager实例
     */
    public static TypeConverterManager of() {
        return new TypeConverterManager();
    }

    /**
     * 注册类型转换器
     *
     * @param converter    类型转换器实例
     * @param <SourceType> 源类型
     * @param <TargetType> 目标类型
     * @return TypeConverterManager 当前实例，支持链式调用
     */
    public <SourceType, TargetType> TypeConverterManager register(TypeConverter<SourceType, TargetType> converter) {
        if (converter != null) {
            // 获取或创建目标类型的映射关系
            targetTypeRegistryContainer.computeIfAbsent(converter.supportsTargetType(), k -> new TargetTypeConverterRegistry()).register(converter);
        }
        return this;
    }

    /**
     * 将源对象转换为目标类型对象
     *
     * @param source        源对象
     * @param targetType    目标类型
     * @param targetDefault 默认值，当转换失败或找不到合适转换器时返回
     * @param selectors     选择器，用于选择合适的转换器
     * @param <SourceType>  源对象类型
     * @param <TargetType>  目标对象类型
     * @return 转换后的目标对象，可能为默认值
     * @throws TypeConverterException 转换过程中发生错误时抛出
     */
    public final <SourceType, TargetType> TargetType converts(SourceType source, Class<TargetType> targetType, TargetType targetDefault, Object... selectors) throws TypeConverterException {
        // 如果源对象为空，直接返回默认值
        if (source == null) {
            return targetDefault;
        }
        // 如果目标类型未指定，则尝试从默认值推断
        if (targetType == null) {
            if (targetDefault == null) {
                throw new TypeConverterException("Please specify the target type or provide a non-null default value");
            }
            //noinspection unchecked
            targetType = (Class<TargetType>) targetDefault.getClass();
        }
        // 获取目标类型的映射关系
        TargetTypeConverterRegistry targetTypeConverterRegistry = targetTypeRegistryContainer.get(targetType);
        if (targetTypeConverterRegistry == null) {
            return targetDefault;
        }
        // 获取源对象的实际类型
        //noinspection unchecked
        Class<SourceType> sourceType = (Class<SourceType>) source.getClass();
        // 首先尝试通过精确类型匹配查找转换器
        //noinspection unchecked
        TypeConverter<SourceType, TargetType> foundTypeConverter = (TypeConverter<SourceType, TargetType>) targetTypeConverterRegistry.specificConverters.get(sourceType);
        if (foundTypeConverter != null) {
            // 验证转换器是否支持目标类型
            return TypeConverter.performConversion(sourceType, source, targetType, targetDefault, foundTypeConverter, false);
        }
        // 如果没有找到精确匹配的转换器，则遍历通用转换器列表
        for (TypeConverter<?, ?> typeConverter : targetTypeConverterRegistry.genericConverters) {
            if (typeConverter.isCanConvert(source, selectors)) {
                //noinspection unchecked
                foundTypeConverter = (TypeConverter<SourceType, TargetType>) typeConverter;
                // 验证转换器是否支持目标类型
                return TypeConverter.performConversion(sourceType, source, targetType, targetDefault, foundTypeConverter, false);
            }
        }
        // 如果没有找到任何适用的转换器，返回默认值
        return targetDefault;
    }

    /**
     * 类型映射内部类，用于存储特定目标类型的所有转换器
     * 包括精确类型匹配的转换器映射表和通用转换器列表
     */
    private static final class TargetTypeConverterRegistry {
        /**
         * 精确类型匹配的转换器映射表
         * key: 源类型
         * value: 对应的类型转换器
         */
        private final Map<Class<?>, TypeConverter<?, ?>> specificConverters;
        /**
         * 通用转换器列表，用于处理源类型不明确的转换器
         */
        private final List<TypeConverter<?, ?>> genericConverters;

        private TargetTypeConverterRegistry(TargetTypeConverterRegistry targetTypeConverterRegistry) {
            this.specificConverters = new ConcurrentHashMap<>(targetTypeConverterRegistry.specificConverters);
            this.genericConverters = new CopyOnWriteArrayList<>(targetTypeConverterRegistry.genericConverters);
        }

        private TargetTypeConverterRegistry() {
            this.specificConverters = new ConcurrentHashMap<>();
            this.genericConverters = new CopyOnWriteArrayList<>();
        }

        /**
         * 将转换器注册到指定的类型映射中
         *
         * @param converter 要注册的转换器
         */
        private void register(TypeConverter<?, ?> converter) {
            // 获取转换器支持的源类型
            Class<?> sourceType = converter.supportsSourceType();
            if (converter.isSpecific()) {
                // 如果源类型明确指定，则使用精确映射
                specificConverters.put(sourceType, converter);
            } else {
                // 如果源类型未明确指定，则添加到通用转换器列表中
                genericConverters.add(converter);
            }
        }
    }
}

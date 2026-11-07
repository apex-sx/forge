/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.converter;

import terraqo.cheetah.converter.basic.*;
import terraqo.cheetah.converter.object.*;
import terraqo.cheetah.converter.when.*;
import terraqo.cheetah.converter.table.ResultSetToArrayTableTypeConverter;
import terraqo.cheetah.converter.table.ResultSetToMapTableTypeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 类型转换管理器，负责注册和管理各种类型转换器，并提供类型转换功能
 *
 * @author Terraqo
 */
public class TypeConverterManager {
    public static final byte CONSTANT_POOL_BYTE_0 = 0, CONSTANT_POOL_BYTE_1 = 1;
    public static final short CONSTANT_POOL_SHORT_0 = 0, CONSTANT_POOL_SHORT_1 = 1;
    public static final int CONSTANT_POOL_INT_0 = 0, CONSTANT_POOL_INT_1 = 1;
    public static final long CONSTANT_POOL_LONG_0 = 0, CONSTANT_POOL_LONG_1 = 1;
    public static final float CONSTANT_POOL_FLOAT_0 = 0, CONSTANT_POOL_FLOAT_1 = 1;
    public static final double CONSTANT_POOL_DOUBLE_0 = 0, CONSTANT_POOL_DOUBLE_1 = 1;
    public static final char CONSTANT_POOL_CHAR_0 = '0', CONSTANT_POOL_CHAR_1 = '1';
    public static final char CONSTANT_POOL_CHAR_T = 'T', CONSTANT_POOL_CHAR_F = 'F';
    // 缓存 -100 到 100 的 byte 值
    public static final Map<String, Byte> CONSTANT_POOL_BYTE = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::byteValue));
    // 缓存 -100 到 100 的 short 值
    public static final Map<String, Short> CONSTANT_POOL_SHORT = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::shortValue));
    // 缓存 -100 到 100 的 int 值
    public static final Map<String, Integer> CONSTANT_POOL_INT = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::intValue));
    // 缓存 -100 到 100 的 long 值
    public static final Map<String, Long> CONSTANT_POOL_LONG = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::longValue));
    // 缓存 -100 到 100 的 float 值
    public static final Map<String, Float> CONSTANT_POOL_FLOAT = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::floatValue));
    // 缓存 -100 到 100 的 double 值
    public static final Map<String, Double> CONSTANT_POOL_DOUBLE = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, Integer::doubleValue));
    // 缓存 -100 到 100 的 BigInteger 值
    public static final Map<String, BigInteger> CONSTANT_POOL_BIG_INTEGER = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, v -> BigInteger.valueOf(v)));
    // 缓存 -100 到 100 的 BigDecimal 值
    public static final Map<String, BigDecimal> CONSTANT_POOL_BIG_DECIMAL = IntStream.rangeClosed(-100, 100)
            .boxed()
            .collect(Collectors.toMap(String::valueOf, BigDecimal::new));

    static {
        for (int i = 0; i <= 1000; i += 100) {
            Number v = i;
            String num = String.valueOf(v);
            CONSTANT_POOL_SHORT.put(num, v.shortValue());
            CONSTANT_POOL_INT.put(num, v.intValue());
            CONSTANT_POOL_LONG.put(num, v.longValue());
            CONSTANT_POOL_FLOAT.put(num, v.floatValue());
            CONSTANT_POOL_DOUBLE.put(num, v.doubleValue());
            CONSTANT_POOL_BIG_INTEGER.put(num, new BigInteger(num));
            CONSTANT_POOL_BIG_DECIMAL.put(num, new BigDecimal(num));
            if (i != 0) {
                num = "+" + num;
                CONSTANT_POOL_SHORT.put(num, v.shortValue());
                CONSTANT_POOL_INT.put(num, v.intValue());
                CONSTANT_POOL_LONG.put(num, v.longValue());
                CONSTANT_POOL_FLOAT.put(num, v.floatValue());
                CONSTANT_POOL_DOUBLE.put(num, v.doubleValue());
                CONSTANT_POOL_BIG_INTEGER.put(num, new BigInteger(num));
                CONSTANT_POOL_BIG_DECIMAL.put(num, new BigDecimal(num));
                v = -i;
                num = String.valueOf(v);
                CONSTANT_POOL_SHORT.put(num, v.shortValue());
                CONSTANT_POOL_INT.put(num, v.intValue());
                CONSTANT_POOL_LONG.put(num, v.longValue());
                CONSTANT_POOL_FLOAT.put(num, v.floatValue());
                CONSTANT_POOL_DOUBLE.put(num, v.doubleValue());
                CONSTANT_POOL_BIG_INTEGER.put(num, new BigInteger(num));
                CONSTANT_POOL_BIG_DECIMAL.put(num, new BigDecimal(num));
            }
        }
        for (int i = 0; i <= 10000; i += 1000) {
            Number v = i;
            String num = String.valueOf(v);
            CONSTANT_POOL_SHORT.put(num, v.shortValue());
            CONSTANT_POOL_INT.put(num, v.intValue());
            CONSTANT_POOL_LONG.put(num, v.longValue());
            CONSTANT_POOL_FLOAT.put(num, v.floatValue());
            CONSTANT_POOL_DOUBLE.put(num, v.doubleValue());
            CONSTANT_POOL_BIG_INTEGER.put(num, new BigInteger(num));
            CONSTANT_POOL_BIG_DECIMAL.put(num, new BigDecimal(num));
            if (i != 0) {
                num = "+" + num;
                CONSTANT_POOL_SHORT.put(num, v.shortValue());
                CONSTANT_POOL_INT.put(num, v.intValue());
                CONSTANT_POOL_LONG.put(num, v.longValue());
                CONSTANT_POOL_FLOAT.put(num, v.floatValue());
                CONSTANT_POOL_DOUBLE.put(num, v.doubleValue());
                CONSTANT_POOL_BIG_INTEGER.put(num, new BigInteger(num));
                CONSTANT_POOL_BIG_DECIMAL.put(num, new BigDecimal(num));
                v = -i;
                num = String.valueOf(v);
                CONSTANT_POOL_SHORT.put(num, v.shortValue());
                CONSTANT_POOL_INT.put(num, v.intValue());
                CONSTANT_POOL_LONG.put(num, v.longValue());
                CONSTANT_POOL_FLOAT.put(num, v.floatValue());
                CONSTANT_POOL_DOUBLE.put(num, v.doubleValue());
                CONSTANT_POOL_BIG_INTEGER.put(num, new BigInteger(num));
                CONSTANT_POOL_BIG_DECIMAL.put(num, new BigDecimal(num));
            }
        }

        // 增加 0.01 至 0.99 的缓存到 FLOAT_CACHE、DOUBLE_CACHE 和 BIG_DECIMAL_CACHE 中
        for (int i = 1; i <= 99; i++) {
            double v = i / 100.0;
            String num = String.format("%.2f", v);
            CONSTANT_POOL_FLOAT.put(num, (float) v);
            CONSTANT_POOL_DOUBLE.put(num, v);
            CONSTANT_POOL_BIG_DECIMAL.put(num, BigDecimal.valueOf(v));

            // 添加带 '+' 符号的正数版本
            num = "+" + num;
            CONSTANT_POOL_FLOAT.put(num, (float) v);
            CONSTANT_POOL_DOUBLE.put(num, v);
            CONSTANT_POOL_BIG_DECIMAL.put(num, BigDecimal.valueOf(v));

            // 添加负数版本
            v = -v;
            num = String.format("%.2f", v);
            CONSTANT_POOL_FLOAT.put(num, (float) v);
            CONSTANT_POOL_DOUBLE.put(num, v);
            CONSTANT_POOL_BIG_DECIMAL.put(num, BigDecimal.valueOf(v));
        }
    }

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
                .register(BooleanToCharacterTypeConverter.INSTANCE_TF)
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
    private final Map<Class<?>, Registry> registryContainer;

    /**
     * 私有构造函数，初始化目标类型注册容器
     */
    private TypeConverterManager(Map<Class<?>, Registry> registryContainer) {
        this.registryContainer = registryContainer;
    }

    /**
     * 创建一个新的TypeConverterManager实例，并通过consumer进行配置
     *
     * @param consumer 用于配置TypeConverterManager的消费者函数
     * @return 配置完成的TypeConverterManager实例
     */
    public static TypeConverterManager of(Consumer<TypeConverterManager> consumer) {
        TypeConverterManager typeConverterManager = new TypeConverterManager(new ConcurrentHashMap<>());
        consumer.accept(typeConverterManager);
        return typeConverterManager;
    }

    /**
     * 基于现有的TypeConverterManager创建一个新的实例
     *
     * @param manager 用于复制配置的现有实例
     * @return 新的TypeConverterManager实例，包含原有实例的配置
     */
    public static TypeConverterManager of(TypeConverterManager manager) {
        Map<Class<?>, Registry> map = new ConcurrentHashMap<>();
        if (manager != null) {
            manager.registryContainer.forEach((targetType, registry) -> map.put(targetType, new Registry(registry)));
        }
        return new TypeConverterManager(map);
    }

    /**
     * 创建一个新的空TypeConverterManager实例
     *
     * @return 新的TypeConverterManager实例
     */
    public static TypeConverterManager of() {
        return new TypeConverterManager(new ConcurrentHashMap<>());
    }

    /**
     * 注册类型转换器
     *
     * @param converter    类型转换器实例
     * @param <SourceType> 源类型
     * @param <TargetType> 目标类型
     * @return TypeConverterManager 当前实例，支持链式调用
     */
    public <SourceType, TargetType> TypeConverterManager register(AbstractTypeConverter<SourceType, TargetType> converter) {
        if (converter != null) {
            // 获取或创建目标类型的映射关系
            registryContainer.computeIfAbsent(converter.supportsTargetType(), Registry::new).register(converter);
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
    @SuppressWarnings("unchecked")
    public final <SourceType, TargetType> TargetType converts(SourceType source, Class<TargetType> targetType, TargetType targetDefault, Object... selectors) throws TypeConverterException {
        // 如果源对象为空，直接返回默认值
        if (source == null) {
            return targetDefault;
        }
        // 如果目标类型未指定，则尝试从默认值推断
        if (targetType == null) {
            if (targetDefault == null) {
                throw new TypeConverterException("Please specify the target targetType or provide a non-null default value");
            }
            targetType = (Class<TargetType>) targetDefault.getClass();
        }
        if (targetType.isInstance(source)) {
            return (TargetType) source;
        }
        // 获取目标类型的映射关系
        Registry registry = registryContainer.get(targetType);
        if (registry == null) {
            return targetDefault;
        }
        // 获取源对象的实际类型
        Class<SourceType> sourceType = (Class<SourceType>) source.getClass();
        // 首先尝试通过精确类型匹配查找转换器
        AbstractTypeConverter<SourceType, TargetType> foundTypeConverter = (AbstractTypeConverter<SourceType, TargetType>) registry.specificConverters.get(sourceType);
        if (foundTypeConverter != null) {
            // 验证转换器是否支持目标类型
            return AbstractTypeConverter.performConversion(sourceType, source, targetType, targetDefault, foundTypeConverter, false);
        }
        // 如果没有找到精确匹配的转换器，则遍历通用转换器列表
        for (AbstractTypeConverter<?, ?> typeConverter : registry.genericConverters) {
            if (typeConverter.isCanConvert(source, selectors)) {
                //noinspection unchecked
                foundTypeConverter = (AbstractTypeConverter<SourceType, TargetType>) typeConverter;
                // 验证转换器是否支持目标类型
                return AbstractTypeConverter.performConversion(sourceType, source, targetType, targetDefault, foundTypeConverter, false);
            }
        }
        // 如果没有找到任何适用的转换器，返回默认值
        return targetDefault;
    }

    /**
     * 类型映射内部类，用于存储特定目标类型的所有转换器
     * 包括精确类型匹配的转换器映射表和通用转换器列表
     *
     * @param specificConverters 精确类型匹配的转换器映射表
     *                           key: 源类型
     *                           value: 对应的类型转换器
     * @param genericConverters  通用转换器列表，用于处理源类型不明确的转换器
     */
    private record Registry(Class<?> targetType, Map<Class<?>, AbstractTypeConverter<?, ?>> specificConverters,
                            List<AbstractTypeConverter<?, ?>> genericConverters) {

        private Registry(Class<?> type) {
            this(type, new ConcurrentHashMap<>(), new CopyOnWriteArrayList<>());
        }

        private Registry(Registry registry) {
            this(registry.targetType, new ConcurrentHashMap<>(registry.specificConverters), new CopyOnWriteArrayList<>(registry.genericConverters));
        }

        /**
         * 将转换器注册到指定的类型映射中
         *
         * @param converter 要注册的转换器
         */
        private void register(AbstractTypeConverter<?, ?> converter) {
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

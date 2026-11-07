
/*
 * Copyright (c) 2026.
 */

package terraqo.cheetah.loader;


import terraqo.cheetah.Logger;
import terraqo.cheetah.converter.TypeConverterManager;
import terraqo.cheetah.iteration.EnumerationIterator;
import terraqo.cheetah.iteration.IteratorEnumeration;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.time.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * 属性加载器接口，提供从配置中获取各种类型属性值的方法
 *
 * @author Terraqo
 */
@SuppressWarnings("unchecked")
public interface PropertyLoader extends Iterable<String> {

    /**
     * 获取所有属性名称的枚举
     *
     * @return 属性名称枚举，包含当前加载器中所有可用的属性名称
     */
    default Enumeration<String> propertyNames() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 检查调用栈，防止无限递归：如果当前方法(propertyNames)被iterator方法调用
        if (stackTrace.length > 3 && stackTrace[1].getMethodName().equals(stackTrace[3].getMethodName())) {
            throw new IllegalStateException("Either 'propertyNames' or 'iterator' methods must be implemented");
        }
        return new IteratorEnumeration<>(iterator());
    }

    /**
     * 返回一个迭代器，用于遍历属性名称集合
     * <p>
     * 该方法提供了一个默认实现，通过调用propertyNames()方法获取属性名称枚举，
     * 然后将其包装为Iterator<String>返回。同时包含防止无限递归的安全检查机制。
     *
     * @return Iterator<String> 返回一个字符串迭代器，用于遍历属性名称
     * @throws IllegalStateException 当propertyNames方法和iterator方法都未被实现时抛出
     */
    @Override
    default Iterator<String> iterator() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 检查调用栈，防止无限递归：如果当前方法(iterator)被propertyNames方法调用
        if (stackTrace.length > 3 && stackTrace[1].getMethodName().equals(stackTrace[3].getMethodName())) {
            throw new IllegalStateException("Either 'propertyNames' or 'iterator' methods must be implemented");
        }
        return new EnumerationIterator<>(propertyNames());
    }

    /**
     * 获取布尔类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的布尔值，如果未找到或转换失败则返回 null
     */
    default Boolean getBoolean(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Boolean.class, null);
    }

    /**
     * 获取布尔类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的布尔值，如果未找到或转换失败则返回默认值
     */
    default boolean getBoolean(String propertyName, boolean defaultValue) {
        Boolean value = this.getBoolean(propertyName);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取字节类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的字节值，如果未找到或转换失败则返回 null
     */
    default Byte getByte(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Byte.class, null);
    }

    /**
     * 获取字节类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的字节值，如果未找到或转换失败则返回默认值
     */
    default Byte getByte(String propertyName, byte defaultValue) {
        Byte value = this.getByte(propertyName);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取字符类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的字符值，如果未找到或转换失败则返回 null
     */
    default Character getChar(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Character.class, null);
    }

    /**
     * 获取字符类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的字符值，如果未找到或转换失败则返回默认值
     */
    default char getChar(String propertyName, char defaultValue) {
        Character value = this.getChar(propertyName);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取短整型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的短整型值，如果未找到或转换失败则返回 null
     */
    default Short getShort(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Short.class, null);
    }

    /**
     * 获取短整型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的短整型值，如果未找到或转换失败则返回默认值
     */
    default short getShort(String propertyName, short defaultValue) {
        Short value = this.getShort(propertyName);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取整型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的整型值，如果未找到或转换失败则返回 null
     */
    default Integer getInt(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Integer.class, null);
    }

    /**
     * 获取整型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的整型值，如果未找到或转换失败则返回默认值
     */
    default int getInt(String propertyName, int defaultValue) {
        Integer value = this.getInt(propertyName);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取长整型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的长整型值，如果未找到或转换失败则返回 null
     */
    default Long getLong(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Long.class, null);
    }

    /**
     * 获取长整型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的长整型值，如果未找到或转换失败则返回默认值
     */
    default long getLong(String propertyName, long defaultValue) {
        Long value = this.getLong(propertyName);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取浮点型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的浮点型值，如果未找到或转换失败则返回 null
     */
    default Float getFloat(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Float.class, null);
    }

    /**
     * 获取浮点型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的浮点型值，如果未找到或转换失败则返回默认值
     */
    default float getFloat(String propertyName, float defaultValue) {
        Float value = this.getFloat(propertyName);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取双精度浮点型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的双精度浮点型值，如果未找到或转换失败则返回 null
     */
    default Double getDouble(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Double.class, null);
    }

    /**
     * 获取双精度浮点型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的双精度浮点型值，如果未找到或转换失败则返回默认值
     */
    default double getDouble(String propertyName, double defaultValue) {
        Double value = this.getDouble(propertyName);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取大整型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的大整型值，如果未找到或转换失败则返回 null
     */
    default BigInteger getBigInteger(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, BigInteger.class, null);
    }

    /**
     * 获取大整型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的大整型值，如果未找到或转换失败则返回默认值
     */
    default BigInteger getBigInteger(String propertyName, Supplier<BigInteger> defaultValue) {
        BigInteger value = this.getBigInteger(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取大整型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的大整型值，如果未找到或转换失败则返回默认值
     */
    default BigInteger getBigInteger(String propertyName, BigInteger defaultValue) {
        return this.getBigInteger(propertyName, () -> defaultValue);
    }

    /**
     * 获取大十进制的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的大十进制值，如果未找到或转换失败则返回 null
     */
    default BigDecimal getBigDecimal(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, BigDecimal.class, null);
    }

    /**
     * 获取大十进制的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的大十进制值，如果未找到或转换失败则返回默认值
     */
    default BigDecimal getBigDecimal(String propertyName, Supplier<BigDecimal> defaultValue) {
        BigDecimal value = this.getBigDecimal(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取大十进制的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的大十进制值，如果未找到或转换失败则返回默认值
     */
    default BigDecimal getBigDecimal(String propertyName, BigDecimal defaultValue) {
        return this.getBigDecimal(propertyName, () -> defaultValue);
    }

    /**
     * 获取数值类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的数值类型值，如果未找到或转换失败则返回 null
     */
    default Number getNumber(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Number.class, null);
    }

    /**
     * 获取数值类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的数值类型值，如果未找到或转换失败则返回默认值
     */
    default Number getNumber(String propertyName, Supplier<Number> defaultValue) {
        Number value = this.getNumber(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取数值类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的数值类型值，如果未找到或转换失败则返回默认值
     */
    default Number getNumber(String propertyName, Number defaultValue) {
        return this.getNumber(propertyName, () -> defaultValue);
    }

    /**
     * 获取字符串类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的字符串值，如果未找到或转换失败则返回 null
     */
    default String getString(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, String.class, null);
    }

    /**
     * 获取字符串类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的字符串值，如果未找到或转换失败则返回默认值
     */
    default String getString(String propertyName, Supplier<String> defaultValue) {
        String value = this.getString(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取字符串类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的字符串值，如果未找到或转换失败则返回默认值
     */
    default String getString(String propertyName, String defaultValue) {
        return this.getString(propertyName, () -> defaultValue);
    }

    /**
     * 获取类类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的类类型值，如果未找到或转换失败则返回 null
     */
    default Class<?> getClass(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Class.class, null);
    }

    /**
     * 获取类类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的类类型值，如果未找到或转换失败则返回默认值
     */
    default Class<?> getClass(String propertyName, Supplier<Class<?>> defaultValue) {
        Class<?> value = this.getClass(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取类类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的类类型值，如果未找到或转换失败则返回默认值
     */
    default Class<?> getClass(String propertyName, Class<?> defaultValue) {
        return this.getClass(propertyName, () -> defaultValue);
    }

    /**
     * 获取持续时间类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的持续时间值，如果未找到或转换失败则返回 null
     */
    default Duration getDuration(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Duration.class, null);
    }

    /**
     * 获取持续时间类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的持续时间值，如果未找到或转换失败则返回默认值
     */
    default Duration getDuration(String propertyName, Supplier<Duration> defaultValue) {
        Duration value = this.getDuration(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取持续时间类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的持续时间值，如果未找到或转换失败则返回默认值
     */
    default Duration getDuration(String propertyName, Duration defaultValue) {
        return this.getDuration(propertyName, () -> defaultValue);
    }

    /**
     * 获取时间段类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的时间段值，如果未找到或转换失败则返回 null
     */
    default Period getPeriod(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Period.class, null);
    }

    /**
     * 获取时间段类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的时间段值，如果未找到或转换失败则返回默认值
     */
    default Period getPeriod(String propertyName, Supplier<Period> defaultValue) {
        Period value = this.getPeriod(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取时间段类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的时间段值，如果未找到或转换失败则返回默认值
     */
    default Period getPeriod(String propertyName, Period defaultValue) {
        return this.getPeriod(propertyName, () -> defaultValue);
    }

    /**
     * 获取日期类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的日期值，如果未找到或转换失败则返回 null
     */
    default Date getDate(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Date.class, null);
    }

    /**
     * 获取日期类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的日期值，如果未找到或转换失败则返回默认值
     */
    default Date getDate(String propertyName, Supplier<Date> defaultValue) {
        Date value = this.getDate(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取日期类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的日期值，如果未找到或转换失败则返回默认值
     */
    default Date getDate(String propertyName, Date defaultValue) {
        return this.getDate(propertyName, () -> defaultValue);
    }

    /**
     * 获取时间戳类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的时间戳值，如果未找到或转换失败则返回 null
     */
    default Instant getInstant(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Instant.class, null);
    }

    /**
     * 获取时间戳类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的时间戳值，如果未找到或转换失败则返回默认值
     */
    default Instant getInstant(String propertyName, Supplier<Instant> defaultValue) {
        Instant value = this.getInstant(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取时间戳类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的时间戳值，如果未找到或转换失败则返回默认值
     */
    default Instant getInstant(String propertyName, Instant defaultValue) {
        return this.getInstant(propertyName, () -> defaultValue);
    }

    /**
     * 获取本地日期类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的本地日期值，如果未找到或转换失败则返回 null
     */
    default LocalDate getLocalDate(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, LocalDate.class, null);
    }

    /**
     * 获取本地日期类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的本地日期值，如果未找到或转换失败则返回默认值
     */
    default LocalDate getLocalDate(String propertyName, Supplier<LocalDate> defaultValue) {
        LocalDate value = this.getLocalDate(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取本地日期类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的本地日期值，如果未找到或转换失败则返回默认值
     */
    default LocalDate getLocalDate(String propertyName, LocalDate defaultValue) {
        return this.getLocalDate(propertyName, () -> defaultValue);
    }

    /**
     * 获取本地时间类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的本地时间值，如果未找到或转换失败则返回 null
     */
    default LocalTime getLocalTime(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, LocalTime.class, null);
    }

    /**
     * 获取本地时间类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的本地时间值，如果未找到或转换失败则返回默认值
     */
    default LocalTime getLocalTime(String propertyName, Supplier<LocalTime> defaultValue) {
        LocalTime value = this.getLocalTime(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取本地时间类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的本地时间值，如果未找到或转换失败则返回默认值
     */
    default LocalTime getLocalTime(String propertyName, LocalTime defaultValue) {
        return this.getLocalTime(propertyName, () -> defaultValue);
    }

    /**
     * 获取本地日期时间类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的本地日期时间值，如果未找到或转换失败则返回 null
     */
    default LocalDateTime getLocalDateTime(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, LocalDateTime.class, null);
    }

    /**
     * 获取本地日期时间类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的本地日期时间值，如果未找到或转换失败则返回默认值
     */
    default LocalDateTime getLocalDateTime(String propertyName, Supplier<LocalDateTime> defaultValue) {
        LocalDateTime value = this.getLocalDateTime(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取本地日期时间类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的本地日期时间值，如果未找到或转换失败则返回默认值
     */
    default LocalDateTime getLocalDateTime(String propertyName, LocalDateTime defaultValue) {
        return this.getLocalDateTime(propertyName, () -> defaultValue);
    }

    /**
     * 获取带时区的日期时间类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的带时区的日期时间值，如果未找到或转换失败则返回 null
     */
    default ZonedDateTime getZonedDateTime(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, ZonedDateTime.class, null);
    }

    /**
     * 获取带时区的日期时间类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的带时区的日期时间值，如果未找到或转换失败则返回默认值
     */
    default ZonedDateTime getZonedDateTime(String propertyName, Supplier<ZonedDateTime> defaultValue) {
        ZonedDateTime value = this.getZonedDateTime(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取带时区的日期时间类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的带时区的日期时间值，如果未找到或转换失败则返回默认值
     */
    default ZonedDateTime getZonedDateTime(String propertyName, ZonedDateTime defaultValue) {
        return this.getZonedDateTime(propertyName, () -> defaultValue);
    }

    /**
     * 获取文件类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的文件值，如果未找到或转换失败则返回 null
     */
    default File getFile(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, File.class, null);
    }

    /**
     * 获取文件类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的文件值，如果未找到或转换失败则返回默认值
     */
    default File getFile(String propertyName, Supplier<File> defaultValue) {
        File value = this.getFile(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取文件类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的文件值，如果未找到或转换失败则返回默认值
     */
    default File getFile(String propertyName, File defaultValue) {
        return this.getFile(propertyName, () -> defaultValue);
    }

    /**
     * 获取路径类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的路径值，如果未找到或转换失败则返回 null
     */
    default Path getPath(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, Path.class, null);
    }

    /**
     * 获取路径类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的路径值，如果未找到或转换失败则返回默认值
     */
    default Path getPath(String propertyName, Supplier<Path> defaultValue) {
        Path value = this.getPath(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取路径类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的路径值，如果未找到或转换失败则返回默认值
     */
    default Path getPath(String propertyName, Path defaultValue) {
        return this.getPath(propertyName, () -> defaultValue);
    }

    /**
     * 获取字符串数组类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的字符串数组值，如果未找到或转换失败则返回 null
     */
    default String[] getStringArray(String propertyName) {
        Object object = this.getObject(propertyName);
        return TypeConverterManager.DEFAULT.converts(object, String[].class, null);
    }

    /**
     * 获取字符串数组类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值提供者
     * @return 对应的字符串数组值，如果未找到或转换失败则返回默认值
     */
    default String[] getStringArray(String propertyName, Supplier<String[]> defaultValue) {
        String[] value = this.getStringArray(propertyName);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取字符串数组类型的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param defaultValue 默认值
     * @return 对应的字符串数组值，如果未找到或转换失败则返回默认值
     */
    default String[] getStringArray(String propertyName, String[] defaultValue) {
        return this.getStringArray(propertyName, () -> defaultValue);
    }

    /**
     * 获取指定类型列表的属性值
     *
     * @param propertyName 属性名称
     * @param type         列表元素类型
     * @param <T>          列表元素泛型类型
     * @return 对应的列表值，如果未找到或转换失败则返回空列表
     */
    default <T> List<T> getList(String propertyName, Class<T> type) {
        return this.getCollection(new ArrayList<>(), propertyName, type);
    }

    /**
     * 获取指定类型列表的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param type         列表元素类型
     * @param defaultValue 默认值提供者
     * @param <T>          列表元素泛型类型
     * @return 对应的列表值，如果未找到或转换失败则返回默认值
     */
    default <T> List<T> getList(String propertyName, Class<T> type, Supplier<List<T>> defaultValue) {
        List<T> value = this.getList(propertyName, type);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取指定类型列表的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param type         列表元素类型
     * @param defaultValue 默认值
     * @param <T>          列表元素泛型类型
     * @return 对应的列表值，如果未找到或转换失败则返回默认值
     */
    default <T> List<T> getList(String propertyName, Class<T> type, List<T> defaultValue) {
        return this.getList(propertyName, type, () -> defaultValue);
    }

    /**
     * 获取指定类型集合的属性值
     *
     * @param propertyName 属性名称
     * @param type         集合元素类型
     * @param <T>          集合元素泛型类型
     * @return 对应的集合值，如果未找到或转换失败则返回空集合
     */
    default <T> Set<T> getSet(String propertyName, Class<T> type) {
        return this.getCollection(new HashSet<>(), propertyName, type);
    }

    /**
     * 获取指定类型集合的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param type         集合元素类型
     * @param defaultValue 默认值提供者
     * @param <T>          集合元素泛型类型
     * @return 对应的集合值，如果未找到或转换失败则返回默认值
     */
    default <T> Set<T> getSet(String propertyName, Class<T> type, Supplier<Set<T>> defaultValue) {
        Set<T> value = this.getSet(propertyName, type);
        return value != null ? value : defaultValue.get();
    }

    /**
     * 获取指定类型集合的属性值，如果未找到则返回默认值
     *
     * @param propertyName 属性名称
     * @param type         集合元素类型
     * @param defaultValue 默认值
     * @param <T>          集合元素泛型类型
     * @return 对应的集合值，如果未找到或转换失败则返回默认值
     */
    default <T> Set<T> getSet(String propertyName, Class<T> type, Set<T> defaultValue) {
        return this.getSet(propertyName, type, () -> defaultValue);
    }

    /**
     * 获取指定类型集合的属性值
     *
     * @param container    集合容器
     * @param propertyName 属性名称
     * @param type         集合元素类型
     * @param <T>          集合元素泛型类型
     * @param <C>          集合泛型类型
     * @return 对应的集合值，如果未找到或转换失败则返回空集合
     */
    default <T, C extends Collection<T>> C getCollection(C container, String propertyName, Class<T> type) {
        if (container == null) {
            throw new IllegalArgumentException("The container must be not null");
        }
        Object object = this.getObject(propertyName);
        if (object != null) {
            if (object instanceof Collection<?> collection) {
                for (Object element : collection) {
                    T value = TypeConverterManager.DEFAULT.converts(element, type, null);
                    if (value != null) {
                        container.add(value);
                    }
                }
            } else if (type.isInstance(object)) {
                container.add((T) object);
            } else {
                for (String s : object.toString().split(",")) {
                    T value = TypeConverterManager.DEFAULT.converts(s, type, null);
                    if (value != null) {
                        container.add(value);
                    }
                }
            }
        }
        return container;
    }

    /**
     * 配置指定值
     * 从属性加载器中获取指定属性的值，如果获取失败则使用默认值，支持设置null值选项
     *
     * @param propertyName 参数键 - 属性名称
     * @param getter       属性获取器 - 用于从PropertyLoader中获取属性值的函数
     * @param setter       属性设置器 - 用于设置属性值的消费者函数
     * @param <T>          值类型 - 属性值的泛型类型
     */
    default <T> void transferProperty(String propertyName, PropertyLoaderGetter<T> getter, PropertyLoaderSetter<T> setter) {
        transferProperty(propertyName, getter, setter, null, true);
    }

    /**
     * 配置指定值
     * 从属性加载器中获取指定属性的值，如果获取失败则使用默认值，支持设置null值选项
     *
     * @param propertyName 参数键 - 属性名称
     * @param defaultValue 默认值 - 当获取属性值失败时使用的默认值
     * @param isSetNull    是否设置null值 - 当属性值和默认值都设置失败时，是否将值设置为null
     * @param getter       属性获取器 - 用于从PropertyLoader中获取属性值的函数
     * @param setter       属性设置器 - 用于设置属性值的消费者函数
     * @param <T>          值类型 - 属性值的泛型类型
     */
    default <T> void transferProperty(String propertyName, PropertyLoaderGetter<T> getter, PropertyLoaderSetter<T> setter, T defaultValue, boolean isSetNull) {
        T value = null;
        try {
            value = getter.get(propertyName);
        } catch (Exception e) {
            if (Logger.isEnabledError()) {
                Logger.error("Error to get value", e);
            }
        }

        // 尝试设置获取到的属性值
        boolean set = false;
        if (value != null) {
            try {
                setter.set(propertyName, value);
                set = true;
            } catch (Exception e) {
                if (Logger.isEnabledError()) {
                    Logger.error("Error to set value [%s]", value, e);
                }
            }
        }

        // 如果属性值设置失败，尝试设置默认值
        if (!set && defaultValue != null) {
            try {
                setter.set(propertyName, value);
                set = true;
            } catch (Exception e) {
                if (Logger.isEnabledError()) {
                    Logger.error("Error to set default value [%s]", defaultValue, e);
                }
            }
        }

        // 如果属性值和默认值都设置失败，且允许设置null值，则设置为null
        if (!set && isSetNull) {
            try {
                setter.set(propertyName, value);
            } catch (Exception e) {
                if (Logger.isEnabledError()) {
                    Logger.error("Error to set null value", e);
                }
            }
        }
    }

    /**
     * 获取原始对象类型的属性值
     *
     * @param propertyName 属性名称
     * @return 对应的原始对象值
     */
    Object getObject(String propertyName);
}

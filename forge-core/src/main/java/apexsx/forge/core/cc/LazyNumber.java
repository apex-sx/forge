/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */
package apexsx.forge.core.cc;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 一个持有数字字符串表示形式的类，支持延迟解析为具体的数值类型。
 * <p>
 * 该类主要用于在不需要立即进行数值计算的场景下，保留原始的数字字符串格式，
 * 以避免浮点数精度丢失或不必要的解析开销。只有在调用 {@link #intValue()},
 * {@link #longValue()}, {@link #floatValue()}, 或 {@link #doubleValue()} 等方法时，
 * 才会将字符串解析为 {@link BigDecimal} 并缓存结果。
 * <p>
 * 此类是不可变的（Immutable），并且线程安全。
 *
 * @author Terraqo
 * @see Number
 * @see BigDecimal
 */
public final class LazyNumber extends Number {

    /**
     * 预编译的正则表达式模式，用于校验数字字符串格式。
     * 匹配可选的正负号，后跟数字部分（包含可选的小数点和指数部分）。
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[+-]?(\\d+\\.?\\d*|\\.\\d+)([eE][+-]?\\d+)?");

    /**
     * 原始的数字字符串表示形式。
     */
    private final String value;

    /**
     * 缓存解析后的 BigDecimal 对象。
     * 使用 volatile 确保在多线程环境下的可见性，配合双重检查锁定实现懒加载。
     */
    private volatile BigDecimal number;

    /**
     * 构造一个懒加载解析的数字对象。
     *
     * @param value 数字的字符串表示形式，不能为 null。
     *              该字符串应符合 {@link BigDecimal#BigDecimal(String)} 的格式要求。
     * @throws NullPointerException 如果 value 为 null
     */
    public LazyNumber(String value) {
        this.value = Objects.requireNonNull(value, "value must not be null");
        // 预先检查是否是数字，使用预编译的正则表达式快速校验格式，避免不必要的 BigDecimal 对象创建开销
        if (!NUMBER_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid number format: " + value);
        }
    }

    /**
     * 将此数字转换为 byte 类型。
     * <p>
     * 如果数值超出 byte 范围（-128 到 127），将发生截断，仅保留低 8 位。
     *
     * @return 转换后的 byte 值
     */
    @Override
    public byte byteValue() {
        return bigDecimalValue().byteValue();
    }

    /**
     * 将此数字转换为 short 类型。
     * <p>
     * 如果数值超出 short 范围（-32,768 到 32,767），将发生截断，仅保留低 16 位。
     *
     * @return 转换后的 short 值
     */
    @Override
    public short shortValue() {
        return bigDecimalValue().shortValue();
    }

    /**
     * 将此数字转换为 int 类型。
     * <p>
     * 如果数值超出 int 范围，将发生截断。
     *
     * @return 转换后的 int 值
     */
    @Override
    public int intValue() {
        return bigDecimalValue().intValue();
    }

    /**
     * 将此数字转换为 long 类型。
     * <p>
     * 如果数值超出 long 范围，将发生截断。
     *
     * @return 转换后的 long 值
     */
    @Override
    public long longValue() {
        return bigDecimalValue().longValue();
    }

    /**
     * 将此数字转换为 float 类型。
     * <p>
     * 可能会损失精度。
     *
     * @return 转换后的 float 值
     */
    @Override
    public float floatValue() {
        return bigDecimalValue().floatValue();
    }

    /**
     * 将此数字转换为 double 类型。
     * <p>
     * 可能会损失精度。
     *
     * @return 转换后的 double 值
     */
    @Override
    public double doubleValue() {
        return bigDecimalValue().doubleValue();
    }

    /**
     * 内部方法：懒加载解析字符串为 BigDecimal。
     * <p>
     * 使用双重检查锁定模式确保线程安全且只解析一次。
     *
     * @return 解析后的 BigDecimal 对象
     */
    public BigInteger bigIntegerValue() {
        return bigDecimalValue().toBigInteger();
    }

    /**
     * 内部方法：懒加载解析字符串为 BigDecimal。
     * <p>
     * 使用双重检查锁定模式确保线程安全且只解析一次。
     *
     * @return 解析后的 BigDecimal 对象
     */
    public BigDecimal bigDecimalValue() {
        BigDecimal localRef = number;
        if (localRef == null) {
            synchronized (this) {
                localRef = number;
                if (localRef == null) {
                    localRef = new BigDecimal(value);
                    number = localRef;
                }
            }
        }
        return localRef;
    }

    /**
     * 返回此数字对象的哈希码。
     * <p>
     * 哈希码基于原始字符串值计算，确保相等的对象具有相同的哈希码。
     *
     * @return 基于字符串值的哈希码
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * 判断此对象是否与指定对象相等。
     * <p>
     * 两个 {@code LazilyParsedNumber} 对象当且仅当它们的原始字符串表示形式完全相同时才被视为相等。
     * 注意：这不同于数值相等（例如 "1.0" 和 "1" 在此类中不相等）。
     *
     * @param obj 要比较的对象
     * @return 如果对象相同则返回 true，否则返回 false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LazyNumber other) {
            return Objects.equals(value, other.value);
        }
        return false;
    }

    /**
     * 返回此数字的原始字符串表示形式。
     *
     * @return 原始字符串值
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * 序列化替换方法。
     * <p>
     * 在序列化过程中，将此对象替换为 {@link BigDecimal} 实例。
     * 这样做的好处是：
     * 1. 确保跨版本序列化兼容性，因为 BigDecimal 是标准库类。
     * 2. 避免序列化内部的 volatile 字段和同步逻辑。
     * 3. 保持数值语义的一致性。
     *
     * @return 用于序列化的替代对象（BigDecimal）
     * @throws ObjectStreamException 如果发生序列化错误
     */
    @Serial
    private Object writeReplace() throws ObjectStreamException {
        return bigDecimalValue();
    }

    /**
     * 禁止直接反序列化此类的实例。
     * <p>
     * 由于使用了 {@link #writeReplace()} 机制，正常的反序列化流程会将数据还原为
     * {@link BigDecimal}，而不会创建 {@code LazilyParsedNumber} 实例，因此不会调用此方法。
     * <p>
     * 如果攻击者尝试通过恶意构造的数据流直接反序列化此类，此方法将抛出异常，
     * 从而防止潜在的安全风险和不一致状态。
     *
     * @param in 对象输入流
     * @throws InvalidObjectException 始终抛出，指示不允许直接反序列化
     */
    @Serial
    private void readObject(ObjectInputStream in) throws InvalidObjectException {
        throw new InvalidObjectException("Unauthorized deserialization attempt");
    }
}

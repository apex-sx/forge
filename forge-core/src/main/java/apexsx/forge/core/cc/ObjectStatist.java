/*
 * Copyright (c) 2026.
 */

package apexsx.forge.core.cc;

import java.io.Serial;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;

/**
 * 实例统计器
 * <p>
 * 该类继承自AtomicLong，并实现了IntSupplier和LongSupplier接口，
 * 提供线程安全的计数功能，用于统计特定对象实例的使用情况。
 * <p>
 * 此类设计用于跟踪和统计特定对象实例的访问次数或使用频率，
 * 同时提供便捷的方法获取关联的对象实例。
 *
 * @author Terraqo
 */
public class ObjectStatist extends AtomicLong implements IntSupplier, LongSupplier {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 被统计的实例对象
     * <p>
     * 存储需要进行统计的目标对象引用，此字段为final类型，
     * 确保在构造后不能被重新赋值，保证对象引用的一致性
     */
    private final Object instance;

    /**
     * 构造函数，初始化实例统计器
     *
     * @param instance 需要被统计的实例对象，不可为null
     */
    public ObjectStatist(Object instance) {
        super(0); // 初始化计数值为0
        this.instance = instance;
    }

    /**
     * 获取被统计的实例对象
     *
     * @return 返回与当前统计器关联的实例对象
     */
    public Object getInstance() {
        return instance;
    }

    /**
     * 获取并强制转换实例对象为指定类型
     * <p>
     * 此方法允许将存储的对象转换为特定类型，调用者需确保类型兼容性
     * 以避免ClassCastException异常的发生。
     *
     * @param <T> 目标类型参数，表示期望的返回类型
     * @return 返回转换为目标类型的实例对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getAndCastInstance() {
        return (T) instance;
    }

    /**
     * 实现IntSupplier接口方法，获取当前计数的int值
     * <p>
     * 此方法将当前的long型计数值转换为int类型，如果数值超出int范围，
     * 将抛出ArithmeticException异常，确保数据的正确性。
     *
     * @return 当前计数的int值，如果原值超出int范围则抛出异常
     * @throws ArithmeticException 如果当前计数值超出int的取值范围
     */
    @Override
    public int getAsInt() {
        return Math.toIntExact(this.get());
    }

    /**
     * 实现LongSupplier接口方法，获取当前计数的long值
     * <p>
     * 此方法返回当前统计器中存储的long型计数值，提供直接访问原子计数器的途径。
     *
     * @return 当前计数的long值，表示对象被统计的次数
     */
    @Override
    public long getAsLong() {
        return this.get();
    }
}

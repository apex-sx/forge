package apexsx.forge.core;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Objects;

/**
 * 临时对象持有者，用于管理线程局部的可复用对象（如 byte[] 或 char[]）。
 * <p>
 * 该类通过 {@link ScratchLifecycle} 管理对象的生命周期，包括创建、重置和回收。
 * 它通常与 {@link ThreadLocal} 配合使用，以减少频繁创建和销毁临时对象带来的 GC 压力。
 * <p>
 * 注意：此类是线程不安全的，预期每个线程拥有独立的实例。
 *
 * @param <T> 持有的对象类型
 * @author Terraqo
 */
public final class ScratchHolder<T> {

    /**
     * 线程局部的 CharBuffer 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#CHAR_BUFFER} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<CharBuffer>> CHAR_BUFFER = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.CHAR_BUFFER));

    /**
     * 线程局部的 ByteBuffer 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#BYTE_BUFFER} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<ByteBuffer>> BYTE_BUFFER = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.BYTE_BUFFER));

    /**
     * 线程局部的 boolean[] 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#BOOLEAN_ARRAY} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<boolean[]>> BOOLEAN_ARRAY = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.BOOLEAN_ARRAY));

    /**
     * 线程局部的 char[] 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#CHAR_ARRAY} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<char[]>> CHAR_ARRAY = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.CHAR_ARRAY));

    /**
     * 线程局部的 byte[] 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#BYTE_ARRAY} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<byte[]>> BYTE_ARRAY = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.BYTE_ARRAY));

    /**
     * 线程局部的 short[] 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#SHORT_ARRAY} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<short[]>> SHORT_ARRAY = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.SHORT_ARRAY));

    /**
     * 线程局部的 int[] 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#INT_ARRAY} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<int[]>> INT_ARRAY = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.INT_ARRAY));

    /**
     * 线程局部的 long[] 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#LONG_ARRAY} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<long[]>> LONG_ARRAY = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.LONG_ARRAY));

    /**
     * 线程局部的 float[] 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#FLOAT_ARRAY} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<float[]>> FLOAT_ARRAY = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.FLOAT_ARRAY));

    /**
     * 线程局部的 double[] 持有者初始化器。
     * 每个线程首次访问时将创建一个基于 {@link ScratchLifecycle#DOUBLE_ARRAY} 的 ScratchHolder。
     */
    public static final ThreadLocal<ScratchHolder<double[]>> DOUBLE_ARRAY = ThreadLocal.withInitial(() -> new ScratchHolder<>(ScratchLifecycle.DOUBLE_ARRAY));


    /**
     * 对象的生命周期管理器，负责对象的创建和重置逻辑。
     */
    private final ScratchLifecycle<T> lifecycle;

    /**
     * 实际持有的临时对象实例。
     */
    private final T object;

    /**
     * 标记当前持有者是否已被使用。
     * 使用 volatile 确保多线程环境下的可见性（尽管通常每个线程只访问自己的实例）。
     */
    private volatile boolean used;

    /**
     * 构造一个新的 ScratchHolder。
     * <p>
     * 如果未提供具体对象，则通过 {@code lifecycle.create()} 创建一个新对象。
     *
     * @param lifecycle 非空的生命周期管理器
     */
    public ScratchHolder(ScratchLifecycle<T> lifecycle) {
        this(lifecycle, null);
    }

    /**
     * 构造一个新的 ScratchHolder。
     *
     * @param lifecycle 非空的生命周期管理器
     * @param object    可选的初始对象。如果为 null，则通过 {@code lifecycle.create()} 创建。
     * @throws IllegalArgumentException 如果 lifecycle 为 null
     */
    public ScratchHolder(ScratchLifecycle<T> lifecycle, T object) {
        if (lifecycle == null) {
            throw new IllegalArgumentException("The 'InstanceLifecycle' must not be null");
        }
        this.used = false;
        // 如果传入对象为 null，则使用 lifecycle 创建新对象；否则使用传入对象
        this.object = Objects.requireNonNullElseGet(object, lifecycle::create);
        this.lifecycle = lifecycle;
    }

    /**
     * 获取关联的生命周期管理器。
     *
     * @return 生命周期管理器实例
     */
    public ScratchLifecycle<T> lifecycle() {
        return lifecycle;
    }

    /**
     * 检查当前持有者是否已被标记为“已使用”。
     *
     * @return 如果已使用返回 true，否则返回 false
     */
    public boolean used() {
        return used;
    }

    /**
     * 将当前持有者标记为“已使用”。
     * <p>
     * 此方法通常在使用完对象后调用，以指示该对象当前处于活跃状态，不应被意外重置或复用，
     * 直到显式调用 {@link #reset()}。
     */
    public void use() {
        this.used = true;
    }

    /**
     * 获取持有的临时对象，并自动将状态标记为“已使用”。
     *
     * @return 持有的临时对象实例
     */
    public T object() {
        this.used = true;
        return object;
    }

    /**
     * 重置当前持有者的状态。
     * <p>
     * 1. 将 {@code used} 标志重置为 false。
     * 2. 调用 {@link ScratchLifecycle#reset(Object)} 清理或重置内部对象的状态，以便下次复用。
     */
    public void reset() {
        this.used = false;
        this.lifecycle.reset(object);
    }

}

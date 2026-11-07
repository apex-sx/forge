package apexsx.forge.core;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 临时对象生命周期管理类。
 * <p>
 * 该类用于管理可复用临时对象（如缓冲区）的创建和重置逻辑，
 * 旨在减少频繁创建和销毁对象带来的垃圾回收压力，提升性能。
 * 通过提供 {@link Supplier} 来创建新实例，并通过 {@link Consumer} 来重置实例状态。
 *
 * @param <T> 临时对象的类型
 * @author Terraqo
 */
public class ScratchLifecycle<T> {

    /**
     * 复制方法中使用的默认缓冲区大小。
     */
    public static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * 字符缓冲区类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<CharBuffer> CHAR_BUFFER = new ScratchLifecycle<>(
            () -> CharBuffer.allocate(DEFAULT_BUFFER_SIZE),
            CharBuffer::clear
    );

    /**
     * 字节缓冲区类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<ByteBuffer> BYTE_BUFFER = new ScratchLifecycle<>(
            () -> ByteBuffer.allocate(DEFAULT_BUFFER_SIZE),
            ByteBuffer::clear
    );

    /**
     * 布尔型数组类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<boolean[]> BOOLEAN_ARRAY = new ScratchLifecycle<>(
            () -> new boolean[DEFAULT_BUFFER_SIZE],
            e -> Arrays.fill(e, false)
    );

    /**
     * 字符数组类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<char[]> CHAR_ARRAY = new ScratchLifecycle<>(
            () -> new char[DEFAULT_BUFFER_SIZE],
            e -> Arrays.fill(e, (char) 0)
    );

    /**
     * 字节数组类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<byte[]> BYTE_ARRAY = new ScratchLifecycle<>(
            () -> new byte[DEFAULT_BUFFER_SIZE],
            e -> Arrays.fill(e, (byte) 0)
    );

    /**
     * 短整型数组类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<short[]> SHORT_ARRAY = new ScratchLifecycle<>(
            () -> new short[DEFAULT_BUFFER_SIZE],
            e -> Arrays.fill(e, (short) 0)
    );

    /**
     * 整型数组类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<int[]> INT_ARRAY = new ScratchLifecycle<>(
            () -> new int[DEFAULT_BUFFER_SIZE],
            e -> Arrays.fill(e, 0)
    );

    /**
     * 长整型数组类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<long[]> LONG_ARRAY = new ScratchLifecycle<>(
            () -> new long[DEFAULT_BUFFER_SIZE],
            e -> Arrays.fill(e, 0L)
    );

    /**
     * 浮点型数组类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<float[]> FLOAT_ARRAY = new ScratchLifecycle<>(
            () -> new float[DEFAULT_BUFFER_SIZE],
            e -> Arrays.fill(e, 0.0f)
    );

    /**
     * 双精度浮点型数组类型的临时对象生命周期实例。
     */
    public static final ScratchLifecycle<double[]> DOUBLE_ARRAY = new ScratchLifecycle<>(
            () -> new double[DEFAULT_BUFFER_SIZE],
            e -> Arrays.fill(e, 0.0d)
    );

    /**
     * 用于创建新临时对象实例的供应者。
     */
    private final Supplier<T> supplier;

    /**
     * 用于重置临时对象状态的消费者。
     */
    private final Consumer<T> resetter;

    /**
     * 构造一个新的 ScratchLifecycle 实例。
     *
     * @param supplier 用于创建新对象的供应者，不能返回 null
     * @param resetter 用于重置对象状态的消费者
     */
    public ScratchLifecycle(Supplier<T> supplier, Consumer<T> resetter) {
        if (supplier == null || resetter == null) {
            throw new IllegalArgumentException("Supplier and Resetter must not be null");
        }
        this.supplier = supplier;
        this.resetter = resetter;
    }

    /**
     * 创建一个新的临时对象实例。
     *
     * @return 新创建的临时对象
     * @throws IllegalStateException 如果供应商返回 null
     */
    public T create() {
        T object = supplier.get();
        if (object == null) {
            throw new IllegalStateException("ScratchLifecycle supplier returned null");
        }
        return object;
    }

    /**
     * 重置给定临时对象的状态。
     *
     * @param object 需要重置的临时对象
     */
    public void reset(T object) {
        if (object != null) {
            resetter.accept(object);
        }
    }
}

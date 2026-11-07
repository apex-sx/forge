package apexsx.forge.core;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.function.IntFunction;

/**
 * 线程内部临时缓冲区的持有者。
 * <p>
 * 缓冲区采用懒加载创建，并在同一线程内复用以减少内存分配开销。在罕见的重入访问情况下，
 * 会分配一个临时缓冲区以避免数据损坏。
 * </p>
 * <p>
 * 典型用法：
 * </p>
 *
 * <pre>{@code
 * try (Scratch<byte[]> scratch = Scratch.get(ScratchHolder.BYTE_ARRAY)) {
 *     // 使用缓冲区
 *     byte[] bytes = scratch.object();
 *     // ...
 * }
 * }</pre>
 *
 * @param <T> 缓冲区对象的类型，例如 byte[] 或 char[]
 * @author Terraqo
 */
public class Scratch<T> implements AutoCloseable {

    /**
     * 线程局部变量持有者。如果此字段为 null，则表示当前实例使用的是临时分配的缓冲区（非线程局部复用）。
     */
    private final ThreadLocal<ScratchHolder<T>> global;

    /**
     * 实际持有的缓冲区对象。
     * <ul>
     *     <li>如果不为 null，表示这是一个临时分配的缓冲区（通常发生在重入场景），关闭时无需重置线程局部状态。</li>
     *     <li>如果为 null，表示使用的是 {@link #global} 中获取的线程局部缓冲区，关闭时需要重置以便复用。</li>
     * </ul>
     */
    private final T object;

    /**
     * 构造函数。
     *
     * @param global 线程局部变量持有者，如果是临时缓冲区则为 null
     * @param object 实际的缓冲区对象，如果是复用线程局部缓冲区则为 null
     */
    private Scratch(ThreadLocal<ScratchHolder<T>> global, final T object) {
        this.global = global;
        this.object = object;
    }

    /**
     * 获取一个 Scratch 实例。
     * <p>
     * 该方法首先尝试从给定的 ThreadLocal 中获取现有的缓冲区持有者。
     * 如果该持有者已被标记为“使用中”（即发生了重入调用），则创建一个新的临时 Scratch 实例，
     * 其中包含一个新分配的缓冲区对象，以避免干扰外层调用的缓冲区状态。
     * 如果未被使用，则标记为使用中，并返回一个指向线程局部缓冲区的 Scratch 实例。
     * </p>
     *
     * @param local 线程局部的 ScratchHolder 引用
     * @param <T>   缓冲区类型
     * @return Scratch 实例
     */
    public static <T> Scratch<T> acquire(ThreadLocal<ScratchHolder<T>> local) {
        final ScratchHolder<T> holder = local.get();
        // 如果当前线程局部缓冲区已被占用（重入情况），则创建一个独立的临时缓冲区
        if (holder.used()) {
            return new Scratch<>(null, holder.lifecycle().create());
        } else {
            // 标记为使用中，并返回复用线程局部缓冲区的实例
            holder.use();
            return new Scratch<>(local, null);
        }
    }

    /**
     * 创建一个指定大小的临时 Scratch 实例。
     * <p>
     * 此方法不利用线程局部缓存，而是直接通过提供的供应商函数分配一个新的缓冲区对象。
     * 适用于需要特定大小且不需要复用的场景，或者作为线程局部缓存未命中时的后备方案。
     * </p>
     *
     * @param size     缓冲区的大小
     * @param supplier 用于创建缓冲区对象的函数
     * @param <T>      缓冲区类型
     * @return Scratch 实例
     */
    public static <T> Scratch<T> acquireArray(ThreadLocal<ScratchHolder<T>> local, int size, IntFunction<T> supplier) {
        // 确保缓冲区大小不小于最小值，避免性能急剧下降
        if (size < ScratchLifecycle.DEFAULT_BUFFER_SIZE) {
            return Scratch.acquire(local);
        } else {
            return new Scratch<>(null, supplier.apply(size));
        }
    }

    /**
     * 获取 char[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 char 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<char [ ]> 实例
     */
    public static Scratch<CharBuffer> getCharBuffer(int size) {
        return Scratch.acquireArray(ScratchHolder.CHAR_BUFFER, size, CharBuffer::allocate);
    }

    /**
     * 获取 byte[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 byte 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<byte [ ]> 实例
     */
    public static Scratch<ByteBuffer> getByteBuffer(int size) {
        return Scratch.acquireArray(ScratchHolder.BYTE_BUFFER, size, ByteBuffer::allocateDirect);
    }

    /**
     * 获取 boolean[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 boolean 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<boolean [ ]> 实例
     */
    public static Scratch<boolean[]> getBooleanArray(int size) {
        return Scratch.acquireArray(ScratchHolder.BOOLEAN_ARRAY, size, boolean[]::new);
    }

    /**
     * 获取 char[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 char 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<char [ ]> 实例
     */
    public static Scratch<char[]> getCharArray(int size) {
        return Scratch.acquireArray(ScratchHolder.CHAR_ARRAY, size, char[]::new);
    }

    /**
     * 获取 byte[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 byte 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<byte [ ]> 实例
     */
    public static Scratch<byte[]> getByteArray(int size) {
        return Scratch.acquireArray(ScratchHolder.BYTE_ARRAY, size, byte[]::new);
    }

    /**
     * 获取 short[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 short 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<short [ ]> 实例
     */
    public static Scratch<short[]> getShortArray(int size) {
        return Scratch.acquireArray(ScratchHolder.SHORT_ARRAY, size, short[]::new);
    }

    /**
     * 获取 int[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 int 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<int [ ]> 实例
     */
    public static Scratch<int[]> getIntArray(int size) {
        return Scratch.acquireArray(ScratchHolder.INT_ARRAY, size, int[]::new);
    }

    /**
     * 获取 long[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 long 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<long [ ]> 实例
     */
    public static Scratch<long[]> getLongArray(int size) {
        return Scratch.acquireArray(ScratchHolder.LONG_ARRAY, size, long[]::new);
    }

    /**
     * 获取 float[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 float 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<float [ ]> 实例
     */
    public static Scratch<float[]> getFloatArray(int size) {
        return Scratch.acquireArray(ScratchHolder.FLOAT_ARRAY, size, float[]::new);
    }

    /**
     * 获取 double[] 类型的 Scratch 实例。
     * <p>
     * 如果请求的缓冲区大小小于默认最小值，将退化为使用线程局部缓存的标准 acquire 逻辑。
     * 否则，将分配一个新的临时 double 数组。
     * </p>
     *
     * @param size 数组大小
     * @return Scratch<double [ ]> 实例
     */
    public static Scratch<double[]> getDoubleArray(int size) {
        return Scratch.acquireArray(ScratchHolder.DOUBLE_ARRAY, size, double[]::new);
    }

    /**
     * 获取底层的缓冲区对象。
     *
     * @return 缓冲区对象（例如 byte[] 或 char[]）
     */
    public final T object() {
        // 如果是临时缓冲区，直接返回对象；否则从线程局部持有者中获取
        return object != null ? object : global.get().object();
    }

    /**
     * 清除当前线程的线程局部变量状态。
     * <p>
     * 此操作会移除当前线程在 {@link #global} 中持有的值。
     * 注意：这通常用于彻底清理线程局部状态，而不是常规的缓冲区释放（常规释放请使用 {@link #close()}）。
     * </p>
     */
    public final void remove() {
        if (global != null) {
            global.remove();
        }
    }

    /**
     * 关闭资源。
     * <p>
     * 如果当前实例使用的是线程局部缓冲区（即 {@link #object} 为 null），
     * 则重置线程局部持有者的状态，使其可以被后续调用复用。
     * 如果使用的是临时缓冲区，则无需执行任何操作，临时对象将随垃圾回收被清理。
     * </p>
     */
    @Override
    public final void close() {
        if (object == null) {
            global.get().reset();
        }
    }

}

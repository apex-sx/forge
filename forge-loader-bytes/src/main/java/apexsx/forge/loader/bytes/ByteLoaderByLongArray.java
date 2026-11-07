package apexsx.forge.loader.bytes;

import com.github.zhitron.byte_loader.ByteLoader;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * ByteLoaderByLongArray 是一个具体的 ByteLoader 实现，用于从 long 数组中加载字节数据。
 * 该类将每个 long 值拆分为 8 个字节，并按大端序（Big Endian）顺序写入 ByteBuffer。
 *
 * @author zhitron
 */
public class ByteLoaderByLongArray extends ByteLoader {
    /**
     * 存储待加载的原始 long 数据数组。
     * 该数组中的每个元素将被转换为 8 字节序列。
     */
    private final long[] input;

    /**
     * 当前处理的偏移量，表示已读取的 long 元素数量。
     * 每次 load 调用后递增，确保按顺序读取数据。
     */
    private int offset = 0;

    /**
     * 构造一个新的 ByteLoaderByLongArray 实例。
     *
     * @param input      包含原始 long 数据的数组
     * @param bufferSize 缓冲区大小，必须大于 8
     */
    public ByteLoaderByLongArray(long[] input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input);
    }

    /**
     * 获取最小缓冲区大小。
     * 由于每个 long 需要 8 字节存储，因此最小缓冲区大小为 8。
     *
     * @return 返回最小缓冲区大小，固定为 8
     */
    @Override
    protected int minBufferSize() {
        return 8;
    }

    /**
     * 从数据源（long 数组）加载字节数据到指定的缓冲区。
     * 将每个 long 值拆分为 8 个字节，并按大端序写入缓冲区。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void load(ByteBuffer buffer) throws Exception {
        int remaining = buffer.remaining();
        int available = input.length - offset;
        if (available <= 0 || remaining <= 0) return;
        int max = Math.min(available, remaining / 8);
        for (int i = 0; i < max; i++) {
            long value = input[offset + i];
            buffer.put((byte) (value >> 56));
            buffer.put((byte) (value >> 48));
            buffer.put((byte) (value >> 40));
            buffer.put((byte) (value >> 32));
            buffer.put((byte) (value >> 24));
            buffer.put((byte) (value >> 16));
            buffer.put((byte) (value >> 8));
            buffer.put((byte) value);
        }
        offset += max;
    }
}

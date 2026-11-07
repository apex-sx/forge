package apexsx.forge.loader.bytes;

import com.github.zhitron.byte_loader.ByteLoader;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * ByteLoaderByShortArray 是一个具体的 ByteLoader 实现，用于从 short 数组中加载字节数据。
 * 该类将每个 short 值拆分为 2 个字节，并按大端序（Big Endian）顺序写入 ByteBuffer。
 *
 * @author zhitron
 */
public class ByteLoaderByShortArray extends ByteLoader {
    /**
     * 存储待处理的 short 类型数组输入数据。
     */
    private final short[] input;

    /**
     * 当前读取位置在 input 中的偏移量。
     */
    private int offset = 0;

    /**
     * 构造一个新的 ByteLoaderByShortArray 实例。
     *
     * @param input      包含原始数据的 short 数组
     * @param bufferSize 缓冲区大小，必须大于 2
     */
    public ByteLoaderByShortArray(short[] input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input);
    }

    /**
     * 获取最小缓冲区大小。由于每个 short 需要两个字节，因此最小缓冲区大小为 2。
     *
     * @return 返回最小缓冲区大小，必须大于等于 0
     */
    @Override
    protected int minBufferSize() {
        return 2;
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     * 将 short 数组中的数据转换为字节序列，并填充到 ByteBuffer 中。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void load(ByteBuffer buffer) throws Exception {
        int remaining = buffer.remaining();
        int available = input.length - offset;
        if (available <= 0 || remaining <= 0) {
            return;
        }
        int max = Math.min(available, remaining >> 1);
        for (int i = 0; i < max; i++) {
            short value = input[offset + i];
            buffer.put((byte) (value >> 8));
            buffer.put((byte) value);
        }
        offset += max;
    }
}

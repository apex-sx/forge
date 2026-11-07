package apexsx.forge.loader.bytes;

import com.github.zhitron.byte_loader.ByteLoader;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * ByteLoaderByCharArray 是一个具体的 ByteLoader 实现，用于从 byte 数组中加载字节数据。
 *
 * @author zhitron
 */
public class ByteLoaderByByteArray extends ByteLoader {
    /**
     * 存储输入数据的字节数组，该数组在构造时提供且不可变。
     */
    private final byte[] input;

    /**
     * 当前读取位置的偏移量，指示下一个要读取的字节位置。
     */
    private int offset = 0;

    /**
     * 构造一个新的 ByteLoaderByByteArray 实例。
     *
     * @param input      提供的字节数据源，不可为 null
     * @param bufferSize 缓冲区大小，必须大于 0
     */
    public ByteLoaderByByteArray(byte[] input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input);
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void load(ByteBuffer buffer) throws Exception {
        int len = Math.min(buffer.remaining(), input.length - offset);
        if (len <= 0) return;
        buffer.put(input, offset, len);
        offset += len;
    }
}

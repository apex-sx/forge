package apexsx.forge.loader.bytes;

import com.github.zhitron.byte_loader.ByteLoader;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Objects;

/**
 * ByteLoaderByCharBuffer 是一个具体的 ByteLoader 实现，用于从 CharBuffer 中加载字节数据。
 * 该类将每个 char 值拆分为 2 个字节，并按大端序（Big Endian）顺序写入 ByteBuffer。
 *
 * @author zhitron
 */
public class ByteLoaderByCharBuffer extends ByteLoader {
    /**
     * 输入的字符缓冲区，包含需要转换为字节数据的字符信息。
     * 该缓冲区是只读的，用于从字符形式的数据源中提取内容并编码为字节。
     */
    private final CharBuffer input;

    /**
     * 构造一个新的 ByteLoaderByCharBuffer 实例。
     *
     * @param input      提供字符数据的 CharBuffer，不能为 null
     * @param bufferSize 缓冲区大小，必须大于 2
     */
    public ByteLoaderByCharBuffer(CharBuffer input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input);
    }

    /**
     * 获取最小缓冲区大小。由于每个 char 需要 2 字节存储，
     * 因此最小缓冲区大小为 2，确保至少可以容纳一个字符的字节表示。
     *
     * @return 返回最小缓冲区大小，固定为 2
     */
    @Override
    protected int minBufferSize() {
        return 2;
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     * 将 CharBuffer 中的字符转换为字节形式，并写入 ByteBuffer。
     * 每个字符使用两个字节进行编码（高位在前，低位在后）。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void load(ByteBuffer buffer) throws Exception {
        int remaining = buffer.remaining();
        int available = input.remaining();
        if (available <= 0 || remaining <= 0) return;
        int max = Math.min(available, remaining >> 1);
        for (int i = 0; i < max; i++) {
            char value = input.get();
            buffer.put((byte) (value >> 8)); // 存储高字节
            buffer.put((byte) value);        // 存储低字节
        }
    }
}

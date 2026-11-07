package apexsx.forge.loader.bytes;

import com.github.zhitron.byte_loader.ByteLoader;

import java.io.Reader;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * ByteLoaderByReader 是一个用于从字符输入流中读取数据并将其转换为字节的类。
 * 该类继承自 ByteLoader，通过 Reader 实现字节数据的加载和处理。
 * 支持自动资源管理，并保证每个字符以两个字节的形式存储在缓冲区中。
 *
 * @author zhitron
 */
public class ByteLoaderByReader extends ByteLoader {
    /**
     * 输入流读取器，用于从字符输入流中读取数据并转换为字节。
     * 该 Reader 是只读的，并且在对象创建时被初始化，不可变。
     */
    private final Reader input;

    /**
     * 缓存数组，用于临时存储从 Reader 中读取的字符数据。
     * 在需要时动态分配内存，以减少频繁的内存分配开销。
     */
    private char[] cache;

    /**
     * 构造一个新的 ByteLoaderByReader 实例。
     *
     * @param input      字符输入流，用于读取字符数据并转换为字节，不可为 null
     * @param bufferSize 缓冲区大小，必须大于 2
     */
    public ByteLoaderByReader(Reader input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input);
    }

    /**
     * 获取最小缓冲区大小。
     *
     * @return 返回最小缓冲区大小，必须大于等于 0
     */
    @Override
    protected int minBufferSize() {
        return 2;
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     * <p>
     * 此方法从 Reader 中读取字符数据，并将其转换为字节对（两个字节表示一个字符），
     * 然后写入 ByteBuffer 中。每个字符占用两个字节，高位字节先写入。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void load(ByteBuffer buffer) throws Exception {
        int remaining = buffer.remaining();
        if (remaining <= 0) return;
        if (cache == null) {
            cache = new char[buffer.capacity() >> 1];
        }
        int read = input.read(cache, 0, remaining >> 1);
        if (read <= 0) return;
        for (int i = 0; i < read; i++) {
            char value = cache[i];
            buffer.put((byte) (value >> 8));
            buffer.put((byte) value);
        }
    }

    /**
     * 关闭资源。此方法应释放与该加载器关联的所有资源。
     * 此实现关闭了底层的 Reader 输入流。
     *
     * @throws Exception 如果关闭过程中发生错误
     */
    @Override
    public void close() throws Exception {
        this.input.close();
    }
}

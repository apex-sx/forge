package apexsx.forge.loader.bytes;

import com.github.zhitron.byte_loader.ByteLoader;

import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Objects;

/**
 * ByteLoaderByReadableByteChannel 是 ByteLoader 的一个实现类，
 * 通过 ReadableByteChannel 读取字节数据。该类负责从指定的通道中加载数据到缓冲区，
 * 并在数据读取完成后正确关闭通道以释放资源。
 *
 * @author zhitron
 */
public class ByteLoaderByReadableByteChannel extends ByteLoader {
    /**
     * 数据输入源，用于从通道读取字节数据。
     * 该通道在构造时提供，并在关闭时释放相关资源。
     */
    private final ReadableByteChannel input;

    /**
     * 构造一个新的 ByteLoaderByReadableByteChannel 实例。
     *
     * @param input      提供字节数据的可读通道，不能为 null
     * @param bufferSize 缓冲区大小，必须大于 0
     */
    public ByteLoaderByReadableByteChannel(ReadableByteChannel input, int bufferSize) {
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
        int remaining = buffer.remaining();
        if (remaining <= 0) return;
        int read = input.read(buffer);
    }

    /**
     * 关闭资源。此方法应释放与该加载器关联的所有资源。
     * 默认实现为空，子类可以根据需要覆盖此方法。
     *
     * @throws Exception 如果关闭过程中发生错误
     */
    @Override
    public void close() throws Exception {
        this.input.close();
    }
}

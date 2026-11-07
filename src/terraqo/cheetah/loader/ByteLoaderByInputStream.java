/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * ByteLoaderByInputStream 是一个具体的 ByteLoader 实现类，用于从 InputStream 中加载字节数据。
 * 它使用缓存机制来提高读取效率，并正确释放与输入流相关的资源。
 * <p>
 *
 * @author Terraqo
 */
public class ByteLoaderByInputStream extends ByteLoader {
    /**
     * input 表示要从中读取字节数据的输入流。
     * 该输入流在对象构造时初始化，且不能为 null。
     */
    private final InputStream input;

    /**
     * cache 是一个临时存储区域，用于在读取数据时暂存从输入流中获取的字节。
     * 它在首次读取时按缓冲区容量初始化，以避免重复分配内存。
     */
    private byte[] cache;

    /**
     * 构造一个新的 ByteLoaderByInputStream 实例。
     *
     * @param input      要读取数据的输入流，不能为 null
     * @param bufferSize 缓冲区大小，必须大于 0
     */
    public ByteLoaderByInputStream(InputStream input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input);
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     * 此方法从输入流中读取数据并将其放入缓冲区中。
     * 如果缓冲区没有剩余空间或输入流无数据可读，则不进行任何操作。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void loadByteBuffer(ByteBuffer buffer) throws Exception {
        int remaining = buffer.remaining();
        if (remaining <= 0) return;
        if (cache == null) {
            cache = new byte[buffer.capacity()];
        }
        int read = input.read(cache, 0, remaining);
        if (read <= 0) return;
        buffer.put(cache, 0, read);
    }

    /**
     * 关闭资源。此方法应释放与该加载器关联的所有资源。
     * 在此类中，它会关闭底层的输入流。
     *
     * @throws Exception 如果关闭过程中发生错误
     */
    @Override
    public void close() throws Exception {
        this.input.close();
    }
}

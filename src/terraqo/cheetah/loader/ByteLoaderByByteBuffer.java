/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * ByteLoaderByByteBuffer 是一个具体的 ByteLoader 实现，用于从 ByteBuffer 中加载字节数据。
 * <p>
 *
 * @author Terraqo
 */
public class ByteLoaderByByteBuffer extends ByteLoader {
    /**
     * 输入的字节缓冲区，存储待加载的数据。
     * 该缓冲区为只读副本，防止原始数据被意外修改。
     */
    private final ByteBuffer input;

    /**
     * 构造一个新的 ByteLoaderByByteBuffer 实例。
     *
     * @param input      提供数据源的 ByteBuffer
     * @param bufferSize 缓冲区大小，必须大于 0
     */
    public ByteLoaderByByteBuffer(ByteBuffer input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input);
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     * 从输入缓冲区中读取尽可能多的数据，并将其放入目标缓冲区中。
     * 如果输入缓冲区或目标缓冲区没有剩余空间，则不进行任何操作。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void loadByteBuffer(ByteBuffer buffer) throws Exception {
        int remaining = buffer.remaining();
        int available = input.remaining();
        if (available <= 0 || remaining <= 0) return;
        int max = Math.min(remaining, input.remaining());
        buffer.put(input.array(), input.position(), max);
        input.position(input.position() + max);
    }
}

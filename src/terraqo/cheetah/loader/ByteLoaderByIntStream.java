/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

/**
 * ByteLoaderByIntStream 是一个具体的 ByteLoader 实现，用于从 int 数组中加载字节数据。
 * 该类将每个 int 值拆分为 4 个字节，并按大端序（Big Endian）顺序写入 ByteBuffer。
 * <p>
 *
 * @author Terraqo
 */
public class ByteLoaderByIntStream extends ByteLoader {
    /**
     * 存储输入的整型数组，每个元素将被转换为 4 个字节。
     * 使用 PrimitiveIterator.OfInt 以高效迭代 int 值。
     */
    private final PrimitiveIterator.OfInt input;

    /**
     * 构造一个新的 ByteLoaderByIntStream 实例。
     *
     * @param input      输入的整型数组，用于提供待转换的 int 值
     * @param bufferSize 缓冲区大小，必须大于 8
     */
    public ByteLoaderByIntStream(IntStream input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input).iterator();
    }

    /**
     * 获取最小缓冲区大小。
     * 对于本实现来说，每次操作会写入 4 个字节，因此最小缓冲区大小为 4。
     * 此方法保证 ByteBuffer 至少能容纳一个完整的 int 数据。
     *
     * @return 返回最小缓冲区大小，必须大于等于 0
     */
    @Override
    protected int minBufferSize() {
        return 4;
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     * 该方法将 int 值转换为大端序（Big Endian）格式的字节顺序并填充到 ByteBuffer 中。
     * 每个 int 值被拆分为 4 个字节，并按高位在前、低位在后的顺序写入缓冲区。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void loadByteBuffer(ByteBuffer buffer) throws Exception {
        while (buffer.hasRemaining()) {
            if (!input.hasNext()) {
                return;
            }
            int value = input.next();
            buffer.put((byte) ((value >> 24) & 0xFF));
            buffer.put((byte) ((value >> 16) & 0xFF));
            buffer.put((byte) ((value >> 8) & 0xFF));
            buffer.put((byte) (value & 0xFF));
        }
    }
}

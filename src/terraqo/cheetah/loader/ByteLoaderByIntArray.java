/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * ByteLoaderByIntArray 是一个具体的 ByteLoader 实现，用于从 int 数组中加载字节数据。
 * 该类将每个 int 值拆分为 4 个字节，并按大端序（Big Endian）顺序写入 ByteBuffer。
 * <p>
 *
 * @author Terraqo
 */
public class ByteLoaderByIntArray extends ByteLoader {
    /**
     * 存储输入的整型数组，每个元素将被转换为 4 个字节。
     */
    private final int[] input;

    /**
     * 当前处理的偏移量，表示已经处理到 input 数组的哪个位置。
     */
    private int offset = 0;

    /**
     * 构造一个新的 ByteLoaderByIntArray 实例。
     *
     * @param input      输入的整型数组
     * @param bufferSize 缓冲区大小，必须大于等于 4
     */
    public ByteLoaderByIntArray(int[] input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input);
    }

    /**
     * 获取最小缓冲区大小。
     * 对于本实现来说，每次操作会写入 4 个字节，因此最小缓冲区大小为 4。
     *
     * @return 返回最小缓冲区大小，必须大于等于 0
     */
    @Override
    protected int minBufferSize() {
        return 4;
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     * 该方法将 int 值转换为字节顺序并填充到 ByteBuffer 中。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void loadByteBuffer(ByteBuffer buffer) throws Exception {
        int remaining = buffer.remaining();
        int available = input.length - offset;
        if (available <= 0 || remaining <= 0) return;
        int max = Math.min(available, remaining / 4);
        for (int i = 0; i < max; i++) {
            int value = input[offset + i];
            buffer.put((byte) (value >> 24));
            buffer.put((byte) (value >> 16));
            buffer.put((byte) (value >> 8));
            buffer.put((byte) value);
        }
        offset += max;
    }
}

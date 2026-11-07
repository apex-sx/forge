/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * ByteLoaderByCharArray 是一个具体的 ByteLoader 实现，用于从 char 数组中加载字节数据。
 * 该类将每个 char 值拆分为 2 个字节，并按大端序（Big Endian）顺序写入 ByteBuffer。
 * <p>
 *
 * @author Terraqo
 */
public class ByteLoaderByCharArray extends ByteLoader {
    /**
     * 存储输入字符的字符数组，用于转换为字节数据
     */
    private final char[] input;

    /**
     * 当前处理的字符数组偏移量，用于跟踪已读取的位置
     */
    private int offset = 0;

    /**
     * 构造一个新的 ByteLoaderByCharArray 实例。
     *
     * @param input      输入的字符数组
     * @param bufferSize 缓冲区大小，必须大于 2
     */
    public ByteLoaderByCharArray(char[] input, int bufferSize) {
        super(bufferSize);
        this.input = Objects.requireNonNull(input);
    }

    /**
     * 获取最小缓冲区大小。该实现确保缓冲区至少可以容纳一个 char 的字节表示（2 字节）。
     *
     * @return 返回最小缓冲区大小，必须大于等于 0
     */
    @Override
    protected int minBufferSize() {
        return 2;
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。该方法将字符数组中的字符转换为字节形式，
     * 并写入给定的 ByteBuffer 中。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    @Override
    protected void loadByteBuffer(ByteBuffer buffer) throws Exception {
        int remaining = buffer.remaining();
        int available = input.length - offset;
        if (available <= 0 || remaining <= 0) return;
        int max = Math.min(available, remaining >> 1);
        for (int i = 0; i < max; i++) {
            char value = input[offset + i];
            buffer.put((byte) (value >> 8));
            buffer.put((byte) value);
        }
        offset += max;
    }
}

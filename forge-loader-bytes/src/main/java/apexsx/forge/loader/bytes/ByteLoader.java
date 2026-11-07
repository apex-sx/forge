package apexsx.forge.loader.bytes;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;

/**
 * ByteLoader 是一个抽象类，用于加载和处理字节数据。
 * 它实现了 AutoCloseable 接口以支持自动资源管理，并实现了 ByteIterator 接口以提供字节迭代功能。
 *
 * @author zhitron
 */
public abstract class ByteLoader implements AutoCloseable, ByteSequenceIterator {
    /**
     * 内部使用的 ByteBuffer，用于存储从数据源加载的字节数据。
     * 使用 direct buffer 以提高 I/O 操作性能。
     */
    private final ByteBuffer buffer;
    /**
     * 缓存上一个读取的字节值，避免重复读取相同位置的数据。
     * 初始值为 -1 表示没有缓存任何有效数据。
     */
    private int cached = -1;

    /**
     * 构造一个新的 ByteLoader 实例，指定缓冲区大小。
     *
     * @param bufferSize 缓冲区大小，必须大于 0
     * @throws IllegalArgumentException 如果 bufferSize 小于等于 0
     */
    public ByteLoader(int bufferSize) {
        int minBufferSize = minBufferSize();
        if (minBufferSize < 0) minBufferSize = 0;
        if (bufferSize < minBufferSize || bufferSize == 0) {
            throw new IllegalArgumentException("bufferSize must be greater than " + bufferSize);
        }
        this.buffer = ByteBuffer.allocateDirect(bufferSize);
        this.buffer.flip(); // 准备读取模式
    }

    /**
     * 关闭资源。此方法应释放与该加载器关联的所有资源。
     * 默认实现为空，子类可以根据需要覆盖此方法。
     *
     * @throws Exception 如果关闭过程中发生错误
     */
    @Override
    public void close() throws Exception {
    }

    /**
     * 检查是否还有下一个字节值可用。
     * 如果有缓存值则直接返回 true，否则调用 peek() 方法检查是否有下一个值。
     *
     * @return 如果有下一个字节值返回 true，否则返回 false
     */
    @Override
    public final boolean hasNextByteValue() {
        if (cached != -1) return true;
        int value = this.peek();
        cached = value;
        return value != -1;
    }

    /**
     * 获取下一个字节值。
     * 如果缓存中没有值，则先调用 hasNextByteValue() 确保有值。
     * 获取后会验证获取的值是否与预期一致，如果不一致抛出异常。
     *
     * @return 下一个字节值
     * @throws NoSuchElementException 如果没有更多字节值可用
     */
    @Override
    public final byte nextByteValue() {
        int result = cached;
        if (result == -1) {
            if (!this.hasNextByteValue()) {
                throw new NoSuchElementException("There is no next element");
            }
            result = cached;
        }
        cached = -1;
        int value = this.pop();
        if (value != result) {
            throw new RuntimeException("Got the wrong value");
        }
        return (byte) result;
    }

    /**
     * 检查数据源是否为空。
     *
     * @return 如果数据源为空返回 true，否则返回 false
     */
    public final boolean isEmpty() {
        return this.peek() == -1;
    }

    /**
     * 查看当前字节值（默认偏移量为 0）。
     *
     * @return 当前偏移量为 0 的字节值
     */
    public final int peek() {
        return this.peek(0);
    }

    /**
     * 查看指定偏移量处的字节值而不移动指针。
     *
     * @param offset 偏移量
     * @return 指定偏移量处的字节值
     */
    public final int peek(int offset) {
        return this.get(offset, false);
    }

    /**
     * 弹出当前字节值（默认偏移量为 0）。
     *
     * @return 当前偏移量为 0 的字节值
     */
    public final int pop() {
        return this.pop(0);
    }

    /**
     * 弹出指定偏移量处的字节值并移动指针。
     *
     * @param offset 偏移量
     * @return 指定偏移量处的字节值
     */
    public final int pop(int offset) {
        return this.get(offset, true);
    }

    /**
     * 将所有剩余的字节值转换为字节数组。
     *
     * @return 包含所有剩余字节值的字节数组
     */
    public final byte[] toContent() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        while (hasNextByteValue()) {
            result.write(nextByteValue());
        }
        return result.toByteArray();
    }

    /**
     * 获取最小缓冲区大小。
     *
     * @return 返回最小缓冲区大小，必须大于等于 0
     */
    protected int minBufferSize() {
        return 0;
    }

    /**
     * 获取指定偏移量处的字节值。
     *
     * @param value   要获取的字节的位置
     * @param consume 是否消费该字节（即是否移动指针）
     * @return 成功获取到的字节值，如果到达流末尾则返回 -1
     */
    protected final int get(final int value, final boolean consume) {
        if (value < 0 || value >= buffer.capacity()) {
            throw new IllegalArgumentException("offset out of range at [0," + buffer.capacity() + ")");
        }
        tag(true, consume);
        int data, count = 0;
        try {
            while (count <= value) {
                // 判断是否有剩余字符可读
                if (!buffer.hasRemaining()) {
                    // 缓冲区无足够空间，重新填充数据
                    tag(false, consume);
                    buffer.compact();
                    load(buffer);
                    buffer.flip();
                    if (!consume) {
                        buffer.mark();
                    }
                    buffer.position(count);
                    if (!buffer.hasRemaining()) {
                        return -1;
                    }
                }
                data = buffer.get();
                if (count == value) {
                    return data;
                }
                count += 1;
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException("Error to load char data", e);
        } finally {
            // 操作完成后恢复位置并根据 consume 决定是否消费字符
            tag(false, consume);
        }
    }

    /**
     * 标记或重置缓冲区的位置。
     *
     * @param mark    是否标记当前位置
     * @param consume 是否消费该字节（即是否移动指针）
     */
    private void tag(boolean mark, boolean consume) {
        try {
            if (!consume) {
                if (mark) {
                    buffer.mark();
                } else {
                    buffer.reset();
                }
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    protected abstract void load(ByteBuffer buffer) throws Exception;
}
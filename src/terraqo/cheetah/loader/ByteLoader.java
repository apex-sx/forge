/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.loader;


import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.NoSuchElementException;

/**
 * ByteLoader 是一个抽象类，用于加载和处理字节数据。
 * 它实现了 AutoCloseable 接口以支持自动资源管理，并实现了 ByteIterator 接口以提供字节迭代功能。
 * <p>
 *
 * @author Terraqo
 */
public abstract class ByteLoader extends ElementLookaheadLoaderBase<Byte> implements AutoCloseable, ByteRawIterator {
    /**
     * 内部使用的 ByteBuffer，用于存储从数据源加载的字节数据。
     * 使用 direct buffer 以提高 I/O 操作性能。
     */
    private final ByteBuffer byteBuffer;

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
        this.byteBuffer = ByteBuffer.allocateDirect(bufferSize);
        this.byteBuffer.flip(); // 准备读取模式
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
    public final boolean hasNextByteRawValue() {
        return hasNext();
    }

    /**
     * 获取下一个字节值。
     * 如果缓存中没有值，则先调用 hasNextByteRawValue() 确保有值。
     * 获取后会验证获取的值是否与预期一致，如果不一致抛出异常。
     *
     * @return 下一个字节值
     * @throws NoSuchElementException 如果没有更多字节值可用
     */
    @Override
    public final byte nextByteRawValue() {
        return next();
    }

    /**
     * 查看当前字节值（默认偏移量为 0）。
     *
     * @return 当前偏移量为 0 的字节值
     */
    public final int peekByte() {
        return this.peekByte(0);
    }

    /**
     * 查看指定偏移量处的字节值而不移动指针。
     *
     * @param offset 偏移量
     * @return 指定偏移量处的字节值
     */
    public final int peekByte(int offset) {
        return this.retrieveElement(offset, false);
    }

    /**
     * 弹出当前字节值（默认偏移量为 0）。
     *
     * @return 当前偏移量为 0 的字节值
     */
    public final int popByte() {
        return this.popByte(0);
    }

    /**
     * 弹出指定偏移量处的字节值并移动指针。
     *
     * @param offset 偏移量
     * @return 指定偏移量处的字节值
     */
    public final int popByte(int offset) {
        return this.retrieveElement(offset, true);
    }

    /**
     * 将所有剩余的字节值转换为字节数组。
     *
     * @return 包含所有剩余字节值的字节数组
     */
    public final byte[] toContent() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        while (hasNextByteRawValue()) {
            result.write(nextByteRawValue());
        }
        return result.toByteArray();
    }

    /**
     * 获取结束指示器的值
     *
     * @return Integer 返回结束指示器的值，固定返回-1表示结束
     */
    @Override
    public Byte eof() {
        return -1;
    }

    /**
     * 检索指定偏移量处的元素，并根据需要决定是否消费该元素及之前的元素
     *
     * <p>此方法允许向前查看流中的元素而不消费它们（当 consume=false 时），
     * 或者检索并消费指定偏移量处的元素以及所有在其之前的元素（当 consume=true 时）。
     * 如果缓存中没有足够的元素满足请求的偏移量，会尝试从数据源中获取更多元素。
     *
     * @param offset  要检索的元素相对于当前位置的偏移量（非负整数）
     * @param consume 如果为 true，则消费从索引 0 到 offset 的所有元素（包括 offset 处的元素）；
     *                如果为 false，则仅返回指定偏移量处的元素而不消费任何元素
     * @return 在指定偏移量处的元素，如果已达到数据源末尾且该位置没有元素，则返回 endIndicator()
     * @throws IllegalArgumentException 如果偏移量为负数
     */
    @Override
    public Byte retrieveElement(int offset, boolean consume) {
        if (offset < 0 || offset >= byteBuffer.capacity()) {
            throw new IllegalArgumentException("offset out of range at [0," + byteBuffer.capacity() + ")");
        }
        if (!consume) {
            byteBuffer.mark();
        }
        int data, count = 0;
        try {
            while (count <= offset) {
                // 判断是否有剩余字符可读
                if (!ensureBufferData(count, consume)) {
                    return endIndicator();
                }
                data = byteBuffer.get();
                if (count == offset) {
                    return (byte) data;
                }
                count += 1;
            }
            return endIndicator();
        } catch (Exception e) {
            throw new RuntimeException("Error to load char data", e);
        } finally {
            // 操作完成后恢复位置并根据 consume 决定是否消费字符
            if (!consume) {
                byteBuffer.reset();
            }
        }
    }

    /**
     * 确保缓冲区中有可用数据
     *
     * @return 如果成功获取到数据返回true，否则返回false
     */
    private boolean ensureBufferData(int count, boolean consume) throws Exception {
        if (!byteBuffer.hasRemaining()) {
            if (!consume) {
                byteBuffer.reset();
            }
            // 缓冲区无足够空间，重新填充数据
            byteBuffer.compact();
            loadByteBuffer(byteBuffer);
            byteBuffer.flip();
            if (!consume) {
                byteBuffer.mark();
            }
            byteBuffer.position(count);
            return byteBuffer.hasRemaining();
        }
        return true;
    }

    /**
     * 从数据源加载字节数据到指定的缓冲区。
     *
     * @param buffer 要填充数据的 Buffer
     * @throws Exception 如果加载过程中发生错误
     */
    protected abstract void loadByteBuffer(ByteBuffer buffer) throws Exception;

    /**
     * 获取最小缓冲区大小。
     *
     * @return 返回最小缓冲区大小，必须大于等于 0
     */
    protected int minBufferSize() {
        return 0;
    }
}
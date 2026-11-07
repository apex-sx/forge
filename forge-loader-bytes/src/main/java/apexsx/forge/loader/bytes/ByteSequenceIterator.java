package apexsx.forge.loader.bytes;

import java.util.NoSuchElementException;

/**
 * {@code byte} 迭代器接口，用于遍历 {@code byte} 类型的数据。
 * 该接口提供了检查是否有下一个 {@code byte} 值以及获取下一个 {@code byte} 值的方法。
 *
 * @author zhitron
 * @see ByteSequenceIterator#hasNextByteValue() 判断是否有下一个 {@code byte} 值
 * @see ByteSequenceIterator#nextByteValue() 获取下一个 {@code byte} 值
 */
public interface ByteSequenceIterator {
    /**
     * 是否有下一个{@code byte}值
     *
     * @return 如果有返回true，否则返回false
     */
    boolean hasNextByteValue();

    /**
     * 获取下一个值
     *
     * @return 返回下一个值
     * @throws NoSuchElementException 如果没有下一个值返回该异常
     */
    byte nextByteValue() throws NoSuchElementException;
}

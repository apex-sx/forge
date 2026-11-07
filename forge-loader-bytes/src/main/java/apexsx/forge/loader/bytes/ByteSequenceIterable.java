package apexsx.forge.loader.bytes;

/**
 * 可迭代的字节序列接口，用于支持遍历字节数据。
 * 提供一个方法来获取用于遍历的 {@link ByteSequenceIterator} 实例。
 *
 * @author zhitron
 */
@FunctionalInterface
public interface ByteSequenceIterable {
    /**
     * 获取一个用于遍历字节数据的迭代器。
     *
     * @return 返回一个实现 {@link ByteSequenceIterator} 的实例
     */
    ByteSequenceIterator byteSequenceIterator();
}

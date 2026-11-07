package apexsx.forge.standard.iterator;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 将 {@link Stream} 包装并呈现为一个 {@link AutoCloseable} 的 {@link Iterator} 资源，
 * 当到达流的末尾时会自动关闭自身。
 *
 * <h2>警告</h2>
 * <p>
 * 为了关闭流，调用方必须要么关闭其分配的流，要么将此迭代器调用到末尾。
 * </p>
 *
 * @param <E> {@link Stream} 和 {@link Iterator} 的类型。
 * @author zhitron
 */
public final class StreamIterator<E> implements Iterator<E>, AutoCloseable {
    /**
     * 给定流的迭代器。
     */
    private final Iterator<E> iterator;
    /**
     * 给定的流。
     */
    private final Stream<E> stream;
    /**
     * 是否已经调用了 {@link #close()} 方法。
     */
    private boolean closed;

    /**
     * 构造一个新的 StreamIterator 实例。
     *
     * @param stream 要包装的流，不能为 null。
     */
    private StreamIterator(final Stream<E> stream) {
        this.stream = Objects.requireNonNull(stream, "stream");
        this.iterator = stream.iterator();
    }

    /**
     * 将流包装并呈现为一个可关闭的资源，当到达流的末尾时会自动关闭自身。
     * <p>
     * <strong>警告</strong>
     * </p>
     * <p>
     * 为了关闭流，调用方必须要么关闭其分配的流，要么将此迭代器调用到末尾。
     * </p>
     *
     * @param <T>    流和迭代器的类型。
     * @param stream 要迭代的流。
     * @return 一个新的迭代器。
     */
    public static <T> StreamIterator<T> iterator(final Stream<T> stream) {
        return new StreamIterator<>(stream);
    }

    public boolean isClosed() {
        return closed;
    }

    /**
     * 关闭底层的流。
     */
    @Override
    public void close() {
        closed = true;
        stream.close();
    }

    /**
     * 检查是否有下一个元素。
     *
     * @return 如果有下一个元素则返回 true，否则返回 false。
     */
    @Override
    public boolean hasNext() {
        if (closed) {
            return false;
        }
        final boolean hasNext = iterator.hasNext();
        if (!hasNext) {
            close();
        }
        return hasNext;
    }

    /**
     * 获取下一个元素。
     *
     * @return 下一个元素。
     */
    @Override
    public E next() {
        final E next = iterator.next();
        if (next == null) {
            close();
        }
        return next;
    }
}

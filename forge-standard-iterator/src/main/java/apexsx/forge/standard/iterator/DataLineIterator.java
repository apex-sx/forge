package apexsx.forge.standard.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * DataLineIterator 是一个迭代器类，用于遍历经过注释过滤后的数据行。
 * 它实现了 {@code Iterator<String>} 接口和 {@code AutoCloseable} 接口，
 * 可以逐行读取非空的数据行，并在使用完毕后自动关闭资源。
 *
 * @author zhitron
 */
public class DataLineIterator implements Iterator<String>, AutoCloseable {
    /**
     * 底层的注释过滤行迭代器，用于提供原始数据行
     */
    private final CommentFilterLineIterator commentFilterLineIterator;

    /**
     * 缓存的当前数据行，用于支持 hasNext() 和 next() 方法的正确实现
     * 当值为 null 时表示尚未加载下一行数据
     * 当值为空字符串时表示没有更多数据
     * 当值为非空字符串时表示下一行可用的数据
     */
    private String data = null;

    /**
     * 构造函数，创建一个新的 DataLineIterator 实例
     *
     * @param commentFilterLineIterator 注释过滤行迭代器，不能为空
     * @throws NullPointerException 如果 commentFilterLineIterator 为 null
     */
    public DataLineIterator(CommentFilterLineIterator commentFilterLineIterator) {
        this.commentFilterLineIterator = Objects.requireNonNull(commentFilterLineIterator);
    }

    /**
     * 关闭此迭代器及其底层的资源
     *
     * @throws Exception 如果关闭过程中发生异常
     */
    @Override
    public void close() throws Exception {
        commentFilterLineIterator.close();
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        if (data != null) {
            return !data.isEmpty();
        }
        while (commentFilterLineIterator.hasNext()) {
            String line = commentFilterLineIterator.next();
            // 去除前缀与后缀空白
            line = line.trim();
            // 过滤空白行
            if (!line.isEmpty()) {
                data = line;
                return true;
            }
        }
        data = "";
        return false;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        String tmp = data;
        data = null;
        return tmp;
    }
}

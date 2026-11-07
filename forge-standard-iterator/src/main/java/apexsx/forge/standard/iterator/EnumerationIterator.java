package apexsx.forge.standard.iterator;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * EnumerationIterator 是一个适配器类，它将 Enumeration 转换为 Iterator。
 * 这允许在需要 Iterator 的地方使用 Enumeration 对象。
 *
 * @param <T> 迭代器返回的元素类型
 * @author zhitron
 */
public final class EnumerationIterator<T> implements Iterator<T> {
    /**
     * 需要被适配成 Iterator 的底层 Enumeration 对象
     */
    private final Enumeration<T> input;

    /**
     * 构造一个 EnumerationIterator，将给定的 Enumeration 适配为 Iterator。
     * 如果输入的 Enumeration 为 null，则使用一个空的 Enumeration 代替。
     *
     * @param input 需要被适配的 Enumeration 对象，可以为 null
     */
    @SuppressWarnings("unchecked")
    public EnumerationIterator(Enumeration<T> input) {
        this.input = input == null ? (Enumeration<T>) Collections.emptyEnumeration() : input;
    }

    /**
     * 如果迭代还有更多元素，返回 {@code true}。
     * （换句话说，如果调用 {@link #next} 方法会返回一个元素而不是抛出异常，则返回 {@code true}。）
     *
     * @return 如果迭代还有更多元素，返回 {@code true}
     */
    @Override
    public boolean hasNext() {
        return input.hasMoreElements();
    }

    /**
     * 返回迭代中的下一个元素。
     *
     * @return 迭代中的下一个元素
     * @throws java.util.NoSuchElementException 如果迭代中没有更多元素
     */
    @Override
    public T next() {
        return input.nextElement();
    }
}

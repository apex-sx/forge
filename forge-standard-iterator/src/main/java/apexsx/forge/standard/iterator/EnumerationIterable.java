package apexsx.forge.standard.iterator;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * EnumerationIterable 是一个适配器类，它将 Enumeration 转换为 Iterable。
 * 这允许在需要 Iterable 的地方使用 Enumeration 对象。
 *
 * @param <T> 迭代器返回的元素类型
 * @author zhitron
 */
public final class EnumerationIterable<T> implements Iterable<T> {
    /**
     * 需要被适配成 Iterable 的底层 Enumeration 对象
     */
    private final Enumeration<T> input;

    /**
     * 构造一个 EnumerationIterable，将给定的 Enumeration 适配为 Iterable。
     *
     * @param input 需要被适配的 Enumeration 对象
     */
    @SuppressWarnings("unchecked")
    public EnumerationIterable(Enumeration<T> input) {
        this.input = input == null ? (Enumeration<T>) Collections.emptyEnumeration() : input;
    }

    /**
     * 返回一个迭代器，该迭代器用于遍历此 Iterable 中的元素。
     *
     * @return 一个迭代器
     */
    @Override
    public Iterator<T> iterator() {
        return new EnumerationIterator<>(input);
    }
}

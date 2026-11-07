package apexsx.forge.standard.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 基于索引访问的迭代器，迭代时自动跳过 {@code null} 元素。
 *
 * @param <T> 元素类型
 * @author ApexSX
 */
public abstract class IndexIterator<T> implements Iterator<T> {

    private int i;

    @Override
    public final boolean hasNext() {
        final int end = size();
        while (i < end) {
            if (get(i) != null) {
                return true;
            }
            i++;
        }
        return false;
    }

    @Override
    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return get(i++);
    }

    @Override
    public final void forEachRemaining(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        int end = size();
        while (i < end) {
            T t = get(i++);
            if (t != null) {
                action.accept(t);
            }
        }
    }

    protected abstract int size();

    protected  abstract T get(int i);
}
package io.github.terraqo.foundation.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class DelegateList<E> extends DelegateSequencedCollection<E> implements List<E> {
    public DelegateList(Collection<E> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        super(delegate, isUnmodifiable, isSynchronized);
    }

    /**
     * 将给定的 {@code delegate} 包装为不可修改的 {@link Collection}。
     * <p>
     * 此方法使用 {@link Collections#unmodifiableCollection(Collection)} 创建原始集合的不可修改视图。
     * </p>
     *
     * @param delegate 要包装的原始 {@link Collection} 实例
     * @return 不可修改的 {@link Collection} 视图
     */
    @Override
    protected List<E> doUnmodifiable(Collection<E> delegate) {
        return Collections.unmodifiableList((List<E>) delegate);
    }

    /**
     * 返回底层的委托 {@link Collection} 实例。
     * <p>
     * 此方法主要用于子类，以便在不进行额外强制转换的情况下访问具体 delegate 类型的特有方法。
     * </p>
     *
     * @return 底层的 {@link Collection} 实例
     */
    @Override
    protected List<E> delegate() {
        return (List<E>) super.delegate();
    }

    @Override
    public E get(int index) {
        return delegate().get(index);
    }

    @Override
    public E set(int index, E element) {
        return delegate().set(index, element);
    }

    @Override
    public void add(int index, E element) {
        delegate().add(index, element);
    }

    @Override
    public E remove(int index) {
        return delegate().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return delegate().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return delegate().lastIndexOf(o);
    }

    @Override
    public ListIterator<E> listIterator() {
        return delegate().listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return delegate().listIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return delegate().subList(fromIndex, toIndex);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return delegate().addAll(index, c);
    }

    @Override
    public List<E> reversed() {
        return delegate().reversed();
    }
}
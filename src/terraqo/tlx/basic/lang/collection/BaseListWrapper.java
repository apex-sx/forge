package pxf.toolkit.basic.lang.collection;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nonnull;

/**
 * {@code List}包裹器
 *
 * @author potatoxf
 * @date 2020/11/11
 */
public abstract class BaseListWrapper<E> extends BaseCollectionWrapper<E, List<E>>
    implements List<E> {

  private static final long serialVersionUID = -7194690810108891445L;

  public BaseListWrapper(List<E> collection) {
    super(collection);
  }

  @Override
  public boolean addAll(int index, @Nonnull Collection<? extends E> c) {
    return collection.addAll(index, c);
  }

  // ---------------------------------------------------------------------------
  @Override
  public E get(int index) {
    return collection.get(index);
  }

  @Override
  public E set(int index, E element) {
    return collection.set(index, element);
  }

  @Override
  public void add(int index, E element) {
    collection.add(index, element);
  }

  @Override
  public E remove(int index) {
    return collection.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return collection.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return collection.lastIndexOf(o);
  }

  @Nonnull
  @Override
  public ListIterator<E> listIterator() {
    return collection.listIterator();
  }

  @Nonnull
  @Override
  public ListIterator<E> listIterator(int index) {
    return collection.listIterator(index);
  }

  @Nonnull
  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return collection.subList(fromIndex, toIndex);
  }
}

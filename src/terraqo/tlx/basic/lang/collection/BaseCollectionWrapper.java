package pxf.toolkit.basic.lang.collection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * {@code Collection}包裹器
 *
 * @author potatoxf
 * @date 2020/11/11
 */
public abstract class BaseCollectionWrapper<E, C extends Collection<E>>
    implements Collection<E>, Serializable, Cloneable {

  private static final long serialVersionUID = -5735864977225130209L;
  protected C collection;

  public BaseCollectionWrapper(C collection) {
    this.collection = Objects.requireNonNull(collection);
  }

  @Override
  public int size() {
    return collection.size();
  }

  @Override
  public boolean isEmpty() {
    return collection.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return collection.contains(o);
  }

  @Nonnull
  @Override
  public Iterator<E> iterator() {
    return collection.iterator();
  }

  @Nonnull
  @Override
  public Object[] toArray() {
    return collection.toArray();
  }

  @Nonnull
  @Override
  public <T> T[] toArray(@Nonnull T[] a) {
    return collection.toArray(a);
  }

  @Override
  public boolean add(E e) {
    return collection.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return collection.remove(o);
  }

  @Override
  public boolean containsAll(@Nonnull Collection<?> c) {
    return collection.containsAll(c);
  }

  @Override
  public boolean addAll(@Nonnull Collection<? extends E> c) {
    return collection.addAll(c);
  }

  @Override
  public boolean removeAll(@Nonnull Collection<?> c) {
    return collection.removeAll(c);
  }

  @Override
  public boolean retainAll(@Nonnull Collection<?> c) {
    return collection.retainAll(c);
  }

  @Override
  public void clear() {
    collection.clear();
  }

  @Override
  public int hashCode() {
    return collection.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return collection.equals(obj);
  }

  @Override
  public String toString() {
    return collection.toString();
  }

  /**
   * Write the list out using a custom routine.
   *
   * @param out the output stream
   */
  private void writeObject(ObjectOutputStream out) throws IOException {
    out.defaultWriteObject();
    out.writeObject(collection);
  }

  /**
   * Read the list in using a custom routine.
   *
   * @param in the input stream
   */
  @SuppressWarnings("unchecked")
  private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
    in.defaultReadObject();
    collection = (C) in.readObject();
  }
}

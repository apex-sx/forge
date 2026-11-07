package potatoxf.helper.api.iterate;

import java.util.Iterator;

/**
 * 抽象迭代器
 *
 * @author potatoxf
 * @date 2021/8/12
 */
public abstract class AbstractIterator<T> implements Iterator<T> {
  /**
   * Removes from the underlying collection the last element returned by this iterator (optional
   * operation). This method can be called only apply per call to {@link #next}.
   *
   * <p>The behavior of an iterator is unspecified if the underlying collection is modified while
   * the iteration is in progress in any way other than by calling this method, unless an overriding
   * class has specified a concurrent modification policy.
   *
   * <p>The behavior of an iterator is unspecified if this method is called after a call to the
   * {@link #forEachRemaining forEachRemaining} method.
   *
   * @throws UnsupportedOperationException if the {@code remove} operation is not supported by this
   *     iterator
   * @throws IllegalStateException if the {@code next} method has not yet been called, or the {@code
   *     remove} method has already been called after the last call to the {@code next} method
   * @implSpec The default implementation throws an instance of {@link
   *     UnsupportedOperationException} and performs no other action.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException("remove");
  }
}

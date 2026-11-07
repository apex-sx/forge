package potatoxf.helper.api.iterate;

import potatoxf.helper.Tools;
import potatoxf.helper.api.Whether;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 对象可迭代
 *
 * @author potatoxf
 * @date 2021/9/26
 * @date 2022/6/21
 */
public class ObjectIter<T> implements Iter<T> {
  private final LinkedList<Object> cached = new LinkedList<>();
  /** */
  private boolean finish = false;
  /** */
  private int size = 0;
  /** 枚举 */
  private Object object;
  /** */
  private Class<T> type;
  /** 枚举 */
  private Enumeration<?> enumeration;
  /** */
  private Iterator<?> iterator;
  /** */
  private Collection<?> collection;
  /** 数组 */
  private Object[] array;
  /** 指针 */
  private int i;

  public ObjectIter(T[] array) {
    this.array = array;
  }

  public ObjectIter(Collection<T> collection) {
    this.collection = collection;
  }

  public ObjectIter(Iterator<T> iterator) {
    this.iterator = iterator;
  }

  public ObjectIter(Enumeration<T> enumeration) {
    this.enumeration = enumeration;
  }

  public ObjectIter(Object object, Class<T> type) {
    if (Whether.arrayObj(object)) {
      this.object = object;
    }
  }

  public static ObjectIter<Integer> of(int... values) {
    return new ObjectIter<Integer>(Tools.toObjectValue(values));
  }

  private boolean addCache(Object object) {
    if (object != null && (type == null || type.isInstance(object))) {
      cached.add(object);
      return true;
    }
    return false;
  }

  /**
   * Returns {@code true} if the iteration has more elements. (In other words, returns {@code true}
   * if {@link #next} would return an element rather than throwing an exception.)
   *
   * @return {@code true} if the iteration has more elements
   */
  @Override
  public boolean hasNext() {
    if (finish) {
      if (iterator == null) {
        iterator = cached.iterator();
      }
      return iterator.hasNext();
    }
    boolean isSuccess = false;
    while (!isSuccess) {
      try {
        if (collection != null) {
          if (iterator == null) {
            iterator = collection.iterator();
          }
          if (iterator.hasNext()) {
            isSuccess = addCache(iterator.next());
          }
        } else if (iterator != null && iterator.hasNext()) {
          isSuccess = addCache(iterator.next());
        } else if (enumeration != null && enumeration.hasMoreElements()) {
          isSuccess = addCache(enumeration.nextElement());
        } else if (array != null && i < array.length) {
          isSuccess = addCache(array[i++]);
        } else if (object != null && i < Array.getLength(object)) {
          isSuccess = addCache(Array.get(object, i++));
        }
      } catch (Throwable ignored) {
        break;
      }
    }
    if (size != cached.size()) {
      size = cached.size();
      return true;
    }
    finish = true;
    enumeration = null;
    iterator = null;
    collection = null;
    array = null;
    return false;
  }

  /**
   * Returns the next element in the iteration.
   *
   * @return the next element in the iteration
   * @throws NoSuchElementException if the iteration has no more elements
   */
  @SuppressWarnings("unchecked")
  @Override
  public T next() {
    return (T) cached.getLast();
  }
}

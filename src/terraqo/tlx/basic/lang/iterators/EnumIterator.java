package pxf.toolkit.basic.lang.iterators;

import java.util.Iterator;
import pxf.toolkit.basic.util.Extract;

/**
 * 枚举迭代器
 *
 * @author potatoxf
 * @date 2021/3/13
 */
@SuppressWarnings("unchecked")
public class EnumIterator<E extends Enum<E>> implements Iterator<E> {

  private final E[] values;
  private int i = 0;

  public EnumIterator(Class<E> type) {
    this((E[]) Extract.genericFromSuperclass(type, 0).getEnumConstants());
  }

  protected EnumIterator(E[] values) {
    this.values = values;
  }

  @Override
  public boolean hasNext() {
    return i < values.length;
  }

  @Override
  public E next() {
    return values[i++];
  }
}

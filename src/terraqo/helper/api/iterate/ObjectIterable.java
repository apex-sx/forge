package potatoxf.helper.api.iterate;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Objects;

/**
 * 对象可迭代
 *
 * @author potatoxf
 * @date 2022/6/21
 */
public class ObjectIterable implements Iterable<Object> {

  private final Object object;

  public ObjectIterable(Object object) {
    this.object = Objects.requireNonNull(object);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Iterator<Object> iterator() {
    if (object instanceof Iterator) {
      return (Iterator<Object>) object;
    } else if (object instanceof Iterable) {
      return (Iterator<Object>) ((Iterable<?>) object).iterator();
    } else if (object.getClass().isArray()) {
      return new ObjectArray(object);
    } else {
      return new ArrayIterator<>(object);
    }
  }

  private static class ObjectArray implements Iterator<Object> {

    private final int length;
    private final Object array;
    private int i = 0;

    ObjectArray(Object array) {
      this.array = array;
      this.length = Array.getLength(array);
    }

    @Override
    public boolean hasNext() {
      return i < length;
    }

    @Override
    public Object next() {
      return Array.get(array, i++);
    }
  }
}

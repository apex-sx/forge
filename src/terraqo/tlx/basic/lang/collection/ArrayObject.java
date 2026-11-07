package pxf.toolkit.basic.lang.collection;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import pxf.toolkit.basic.lang.iterators.ArrayIterator;
import pxf.toolkit.basic.util.Is;
import pxf.toolkit.basic.util.Legalize;

/**
 * 数组对象
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public class ArrayObject<T> implements Serializable, Iterable<T> {

  private static final long serialVersionUID = -3721884918834374839L;
  private final T[] array;

  public ArrayObject(T[] array) {
    this.array = array;
  }

  public ArrayObject(ArrayObject<T> arrayObject) {
    this.array = arrayObject.array;
  }

  public ArrayObject(ArrayObject<T> arrayObject, T[] array) {
    if (Is.empty(array)) {
      this.array = arrayObject.array;
    } else {
      int newLen = arrayObject.array.length + array.length;
      T[] newArr = Arrays.copyOf(arrayObject.array, newLen);
      System.arraycopy(array, 0, newArr, arrayObject.array.length, array.length);
      this.array = newArr;
    }
  }

  /**
   * 是否数组有元素
   *
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public final boolean isPresent() {
    return !isEmpty();
  }

  /**
   * 是否数组为空
   *
   * @return 如果空为 {@code true}，否则 {@code false}
   */
  public final boolean isEmpty() {
    return length() == 0;
  }

  /**
   * 数组长度
   *
   * @return 长度
   */
  public final int length() {
    return array.length;
  }

  /**
   * 获取元素
   *
   * @param index 元素索引，任何数值都可以转化成合法索引 {@link Legalize#index(int, int)}
   * @return 返回元素
   */
  public T get(int index) {
    int idx = Legalize.index(index, array.length);
    return array[idx];
  }

  /**
   * 获取数组{@code Stream<T>}
   *
   * @return {@code Stream<T>}
   */
  public Stream<T> stream() {
    return Arrays.stream(this.array);
  }

  /**
   * 去重
   *
   * @param generator 构造数组
   * @return 新的ArrayObject<T>
   */
  public ArrayObject<T> distinct(IntFunction<T[]> generator) {
    T[] ts = stream().distinct().toArray(generator);
    return new ArrayObject<>(ts);
  }

  /**
   * 返回数组迭代器
   *
   * @return {@code Iterator<T>}
   */
  @Nonnull
  @Override
  public Iterator<T> iterator() {
    return new ArrayIterator<T>(array);
  }

  /**
   * 返回数组拷贝迭代器，这也意味着该迭代器线程安全
   *
   * @return {@code Iterator<T>}
   */
  @Nonnull
  public Iterator<T> copyIterator() {
    return new ArrayIterator<T>(Arrays.copyOf(array, array.length));
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(array);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ArrayObject<?> that = (ArrayObject<?>) o;
    return Arrays.equals(array, that.array);
  }

  @Override
  public String toString() {
    return Arrays.toString(array);
  }
}

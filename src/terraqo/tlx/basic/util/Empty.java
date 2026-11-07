package pxf.toolkit.basic.util;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import pxf.toolkit.basic.lang.iterators.ArrayIterator;

/**
 * 空的实例
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public final class Empty {

  public static final String STRING = "";
  public static final char CHAR = ' ';

  // --------------------------------------------------------------------------- 空的数组

  public static final boolean[] BOOLEAN_ARRAY = {};
  public static final byte[] BYTE_ARRAY = {};
  public static final char[] CHAR_ARRAY = {};
  public static final short[] SHORT_ARRAY = {};
  public static final int[] INT_ARRAY = {};
  public static final long[] LONG_ARRAY = {};
  public static final float[] FLOAT_ARRAY = {};
  public static final double[] DOUBLE_ARRAY = {};
  public static final Object[] OBJECT_ARRAY = {};
  public static final Class[] CLASS_ARRAY = {};
  public static final String[] STRING_ARRAY = {};

  private static final ArrayIterator<?> ARRAY_ITERATOR = new ArrayIterator<>();

  private Empty() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  public static <T> T[] array() {
    return variableArray();
  }

  @SafeVarargs
  private static <T> T[] variableArray(T... array) {
    return array;
  }

  @SuppressWarnings("unchecked")
  public static <T> ArrayIterator<T> arrayIterator() {
    return (ArrayIterator<T>) ARRAY_ITERATOR;
  }

  // --------------------------------------------------------------------------- 空的功能函数

  public static <T> Consumer<T> consumer() {
    return t -> {};
  }

  public static <T, U> BiConsumer<T, U> biConsumer() {
    return (t, u) -> {};
  }

  public static <T, R> Function<T, R> function() {
    return t -> null;
  }

  public static <T, U, R> BiFunction<T, U, R> biFunction() {
    return (t, u) -> null;
  }

  public static <T> Predicate<T> predicate() {
    return t -> false;
  }

  public static <T, U> BiPredicate<T, U> biPredicate() {
    return (t, u) -> false;
  }
}

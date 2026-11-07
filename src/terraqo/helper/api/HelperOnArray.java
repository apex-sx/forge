package potatoxf.helper.api;

import potatoxf.helper.api.function.FactoryThrow;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiPredicate;

/**
 * 数组助手类
 *
 * @author potatoxf
 * @date 2021/7/24
 */
@SuppressWarnings("unchecked")
public final class HelperOnArray {
  private HelperOnArray() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  // Reverse
  // -----------------------------------------------------------------------
  /**
   * Reverses the order of the given array.
   *
   * <p>There is no special handling for multi-dimensional arrays.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   */
  public static void reverse(final Object[] array) {
    if (array == null) {
      return;
    }
    reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the given array.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   */
  public static void reverse(final long[] array) {
    if (array == null) {
      return;
    }
    reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the given array.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   */
  public static void reverse(final int[] array) {
    if (array == null) {
      return;
    }
    reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the given array.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   */
  public static void reverse(final short[] array) {
    if (array == null) {
      return;
    }
    reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the given array.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   */
  public static void reverse(final char[] array) {
    if (array == null) {
      return;
    }
    reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the given array.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   */
  public static void reverse(final byte[] array) {
    if (array == null) {
      return;
    }
    reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the given array.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   */
  public static void reverse(final double[] array) {
    if (array == null) {
      return;
    }
    reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the given array.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   */
  public static void reverse(final float[] array) {
    if (array == null) {
      return;
    }
    reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the given array.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   */
  public static void reverse(final boolean[] array) {
    if (array == null) {
      return;
    }
    reverse(array, 0, array.length);
  }

  /**
   * Reverses the order of the given array in the given range.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
   *     (&gt;array.length) results in no change.
   * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue (&lt;
   *     start index) results in no change. Overvalue (&gt;array.length) is demoted to array length.
   * @since 3.2
   */
  public static void reverse(
      final boolean[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null) {
      return;
    }
    int i = Math.max(startIndexInclusive, 0);
    int j = Math.min(array.length, endIndexExclusive) - 1;
    boolean tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

  /**
   * Reverses the order of the given array in the given range.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
   *     (&gt;array.length) results in no change.
   * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue (&lt;
   *     start index) results in no change. Overvalue (&gt;array.length) is demoted to array length.
   * @since 3.2
   */
  public static void reverse(final byte[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null) {
      return;
    }
    int i = Math.max(startIndexInclusive, 0);
    int j = Math.min(array.length, endIndexExclusive) - 1;
    byte tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

  /**
   * Reverses the order of the given array in the given range.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
   *     (&gt;array.length) results in no change.
   * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue (&lt;
   *     start index) results in no change. Overvalue (&gt;array.length) is demoted to array length.
   * @since 3.2
   */
  public static void reverse(final char[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null) {
      return;
    }
    int i = Math.max(startIndexInclusive, 0);
    int j = Math.min(array.length, endIndexExclusive) - 1;
    char tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

  /**
   * Reverses the order of the given array in the given range.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
   *     (&gt;array.length) results in no change.
   * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue (&lt;
   *     start index) results in no change. Overvalue (&gt;array.length) is demoted to array length.
   * @since 3.2
   */
  public static void reverse(final double[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null) {
      return;
    }
    int i = Math.max(startIndexInclusive, 0);
    int j = Math.min(array.length, endIndexExclusive) - 1;
    double tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

  /**
   * Reverses the order of the given array in the given range.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
   *     (&gt;array.length) results in no change.
   * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue (&lt;
   *     start index) results in no change. Overvalue (&gt;array.length) is demoted to array length.
   * @since 3.2
   */
  public static void reverse(final float[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null) {
      return;
    }
    int i = Math.max(startIndexInclusive, 0);
    int j = Math.min(array.length, endIndexExclusive) - 1;
    float tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

  /**
   * Reverses the order of the given array in the given range.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
   *     (&gt;array.length) results in no change.
   * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue (&lt;
   *     start index) results in no change. Overvalue (&gt;array.length) is demoted to array length.
   * @since 3.2
   */
  public static void reverse(final int[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null) {
      return;
    }
    int i = Math.max(startIndexInclusive, 0);
    int j = Math.min(array.length, endIndexExclusive) - 1;
    int tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

  /**
   * Reverses the order of the given array in the given range.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
   *     (&gt;array.length) results in no change.
   * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue (&lt;
   *     start index) results in no change. Overvalue (&gt;array.length) is demoted to array length.
   * @since 3.2
   */
  public static void reverse(final long[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null) {
      return;
    }
    int i = Math.max(startIndexInclusive, 0);
    int j = Math.min(array.length, endIndexExclusive) - 1;
    long tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

  /**
   * Reverses the order of the given array in the given range.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
   *     (&gt;array.length) results in no change.
   * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue (&lt;
   *     start index) results in no change. Overvalue (&gt;array.length) is demoted to array length.
   * @since 3.2
   */
  public static void reverse(final Object[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null) {
      return;
    }
    int i = Math.max(startIndexInclusive, 0);
    int j = Math.min(array.length, endIndexExclusive) - 1;
    Object tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

  /**
   * Reverses the order of the given array in the given range.
   *
   * <p>This method does nothing for a {@code null} input array.
   *
   * @param array the array to reverse, may be {@code null}
   * @param startIndexInclusive the starting index. Undervalue (&lt;0) is promoted to 0, overvalue
   *     (&gt;array.length) results in no change.
   * @param endIndexExclusive elements up to endIndex-1 are reversed in the array. Undervalue (&lt;
   *     start index) results in no change. Overvalue (&gt;array.length) is demoted to array length.
   * @since 3.2
   */
  public static void reverse(final short[] array, int startIndexInclusive, int endIndexExclusive) {
    if (array == null) {
      return;
    }
    int i = Math.max(startIndexInclusive, 0);
    int j = Math.min(array.length, endIndexExclusive) - 1;
    short tmp;
    while (j > i) {
      tmp = array[j];
      array[j] = array[i];
      array[i] = tmp;
      j--;
      i++;
    }
  }

  /**
   * 求和
   *
   * @param arr 数组
   */
  public static long sum(byte... arr) {
    long result = 0L;
    for (byte e : arr) {
      result += e;
    }
    return result;
  }

  /**
   * 求和
   *
   * @param arr 数组
   */
  public static long sum(short... arr) {
    long result = 0L;
    for (short e : arr) {
      result += e;
    }
    return result;
  }

  /**
   * 求和
   *
   * @param arr 数组
   */
  public static long sum(int... arr) {
    long result = 0L;
    for (int e : arr) {
      result += e;
    }
    return result;
  }

  /**
   * 求和
   *
   * @param arr 数组
   */
  public static long sum(long... arr) {
    long result = 0L;
    for (long e : arr) {
      result += e;
    }
    return result;
  }

  /**
   * 求和
   *
   * @param arr 数组
   */
  public static float sum(float... arr) {
    float result = 0f;
    for (float e : arr) {
      result += e;
    }
    return result;
  }

  /**
   * 求和
   *
   * @param arr 数组
   */
  public static double sum(double... arr) {
    double result = 0d;
    for (double e : arr) {
      result += e;
    }
    return result;
  }

  /**
   * 精确求和
   *
   * @param arr 数组
   */
  public static BigInteger sumExact(byte... arr) {
    BigInteger result = BigInteger.ZERO;
    long r = 0, t = 0;
    for (byte e : arr) {
      r = t + e;
      if (((t ^ r) & (e ^ r)) < 0) {
        result = result.add(BigInteger.valueOf(t)).add(BigInteger.valueOf(e));
        t = 0;
        r = 0;
      } else {
        t = r;
      }
    }
    if (r != 0) {
      result = result.add(BigInteger.valueOf(r));
    }
    return result;
  }

  /**
   * 精确求和
   *
   * @param arr 数组
   */
  public static BigInteger sumExact(short... arr) {
    BigInteger result = BigInteger.ZERO;
    long r = 0, t = 0;
    for (short e : arr) {
      r = t + e;
      if (((t ^ r) & (e ^ r)) < 0) {
        result = result.add(BigInteger.valueOf(t)).add(BigInteger.valueOf(e));
        t = 0;
        r = 0;
      } else {
        t = r;
      }
    }
    if (r != 0) {
      result = result.add(BigInteger.valueOf(r));
    }
    return result;
  }

  /**
   * 精确求和
   *
   * @param arr 数组
   */
  public static BigInteger sumExact(int... arr) {
    BigInteger result = BigInteger.ZERO;
    long r = 0, t = 0;
    for (int e : arr) {
      r = t + e;
      if (((t ^ r) & (e ^ r)) < 0) {
        result = result.add(BigInteger.valueOf(t)).add(BigInteger.valueOf(e));
        t = 0;
        r = 0;
      } else {
        t = r;
      }
    }
    if (r != 0) {
      result = result.add(BigInteger.valueOf(r));
    }
    return result;
  }

  /**
   * 精确求和
   *
   * @param arr 数组
   */
  public static BigInteger sumExact(long... arr) {
    BigInteger result = BigInteger.ZERO;
    long r = 0, t = 0;
    for (long e : arr) {
      r = t + e;
      if (((t ^ r) & (e ^ r)) < 0) {
        result = result.add(BigInteger.valueOf(t)).add(BigInteger.valueOf(e));
        t = 0;
        r = 0;
      } else {
        t = r;
      }
    }
    if (r != 0) {
      result = result.add(BigInteger.valueOf(r));
    }
    return result;
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static boolean[] mergeToArray(boolean[] inputs, boolean... objs) {
    if (inputs == null && objs == null) {
      return Tools.EMPTY_BOOLEAN_ARRAY;
    } else if (inputs == null) {
      return objs;
    } else if (objs == null) {
      return inputs;
    }
    int length = inputs.length + objs.length;
    boolean[] outputs = new boolean[length];
    if (inputs.length != 0) {
      System.arraycopy(inputs, 0, outputs, 0, inputs.length);
    }
    if (objs.length != 0) {
      System.arraycopy(objs, 0, outputs, inputs.length, objs.length);
    }
    return outputs;
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static char[] mergeToArray(char[] inputs, char... objs) {
    if (inputs == null && objs == null) {
      return Tools.EMPTY_CHAR_ARRAY;
    } else if (inputs == null) {
      return objs;
    } else if (objs == null) {
      return inputs;
    }
    int length = inputs.length + objs.length;
    char[] outputs = new char[length];
    if (inputs.length != 0) {
      System.arraycopy(inputs, 0, outputs, 0, inputs.length);
    }
    if (objs.length != 0) {
      System.arraycopy(objs, 0, outputs, inputs.length, objs.length);
    }
    return outputs;
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static byte[] mergeToArray(byte[] inputs, byte... objs) {
    if (inputs == null && objs == null) {
      return Tools.EMPTY_BYTE_ARRAY;
    } else if (inputs == null) {
      return objs;
    } else if (objs == null) {
      return inputs;
    }
    int length = inputs.length + objs.length;
    byte[] outputs = new byte[length];
    if (inputs.length != 0) {
      System.arraycopy(inputs, 0, outputs, 0, inputs.length);
    }
    if (objs.length != 0) {
      System.arraycopy(objs, 0, outputs, inputs.length, objs.length);
    }
    return outputs;
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static short[] mergeToArray(short[] inputs, short... objs) {
    if (inputs == null && objs == null) {
      return Tools.EMPTY_SHORT_ARRAY;
    } else if (inputs == null) {
      return objs;
    } else if (objs == null) {
      return inputs;
    }
    int length = inputs.length + objs.length;
    short[] outputs = new short[length];
    if (inputs.length != 0) {
      System.arraycopy(inputs, 0, outputs, 0, inputs.length);
    }
    if (objs.length != 0) {
      System.arraycopy(objs, 0, outputs, inputs.length, objs.length);
    }
    return outputs;
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static int[] mergeToArray(int[] inputs, int... objs) {
    if (inputs == null && objs == null) {
      return Tools.EMPTY_INT_ARRAY;
    } else if (inputs == null) {
      return objs;
    } else if (objs == null) {
      return inputs;
    }
    int length = inputs.length + objs.length;
    int[] outputs = new int[length];
    if (inputs.length != 0) {
      System.arraycopy(inputs, 0, outputs, 0, inputs.length);
    }
    if (objs.length != 0) {
      System.arraycopy(objs, 0, outputs, inputs.length, objs.length);
    }
    return outputs;
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static long[] mergeToArray(long[] inputs, long... objs) {
    if (inputs == null && objs == null) {
      return Tools.EMPTY_LONG_ARRAY;
    } else if (inputs == null) {
      return objs;
    } else if (objs == null) {
      return inputs;
    }
    int length = inputs.length + objs.length;
    long[] outputs = new long[length];
    if (inputs.length != 0) {
      System.arraycopy(inputs, 0, outputs, 0, inputs.length);
    }
    if (objs.length != 0) {
      System.arraycopy(objs, 0, outputs, inputs.length, objs.length);
    }
    return outputs;
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static float[] mergeToArray(float[] inputs, float... objs) {
    if (inputs == null && objs == null) {
      return Tools.EMPTY_FLOAT_ARRAY;
    } else if (inputs == null) {
      return objs;
    } else if (objs == null) {
      return inputs;
    }
    int length = inputs.length + objs.length;
    float[] outputs = new float[length];
    if (inputs.length != 0) {
      System.arraycopy(inputs, 0, outputs, 0, inputs.length);
    }
    if (objs.length != 0) {
      System.arraycopy(objs, 0, outputs, inputs.length, objs.length);
    }
    return outputs;
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static double[] mergeToArray(double[] inputs, double... objs) {
    if (inputs == null && objs == null) {
      return Tools.EMPTY_DOUBLE_ARRAY;
    } else if (inputs == null) {
      return objs;
    } else if (objs == null) {
      return inputs;
    }
    int length = inputs.length + objs.length;
    double[] outputs = new double[length];
    if (inputs.length != 0) {
      System.arraycopy(inputs, 0, outputs, 0, inputs.length);
    }
    if (objs.length != 0) {
      System.arraycopy(objs, 0, outputs, inputs.length, objs.length);
    }
    return outputs;
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static Class<?>[] mergeToArray(Class<?>[] inputs, Class<?>... objs) {
    return mergeToArray(Class.class, inputs, objs);
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static String[] mergeToArray(String[] inputs, String... objs) {
    return mergeToArray(String.class, inputs, objs);
  }

  /**
   * Append the given object to the given array, returning a new array consisting of the input array
   * contents plus the given object.
   *
   * @param inputs the array to append to (can be {@code null})
   * @param objs the object to append
   * @return the new array (of the same component type; never {@code null})
   */
  public static <A, O extends A> A[] mergeToArray(Class<A> type, A[] inputs, O... objs) {
    if (objs == null || inputs == null) {
      return (A[]) Array.newInstance(type, 0);
    }
    if (objs.length == 0) {
      return inputs;
    }
    int length = inputs.length + objs.length;
    @SuppressWarnings("unchecked")
    A[] outputs = (A[]) Array.newInstance(type, length);
    if (inputs.length != 0) {
      System.arraycopy(inputs, 0, outputs, 0, inputs.length);
    }
    System.arraycopy(objs, 0, outputs, inputs.length, objs.length);
    return outputs;
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static boolean[] mergeArray(boolean[]... inputs) {
    if (inputs == null || inputs.length == 0) {
      return new boolean[0];
    }
    int len = 0;
    for (boolean[] input : inputs) {
      len += input.length;
    }
    boolean[] outputs = new boolean[len];
    int p = 0;
    for (boolean[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static char[] mergeArray(char[]... inputs) {
    if (inputs == null || inputs.length == 0) {
      return new char[0];
    }
    int len = 0;
    for (char[] input : inputs) {
      len += input.length;
    }
    char[] outputs = new char[len];
    int p = 0;
    for (char[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static byte[] mergeArray(byte[]... inputs) {
    if (inputs == null || inputs.length == 0) {
      return new byte[0];
    }
    int len = 0;
    for (byte[] input : inputs) {
      len += input.length;
    }
    byte[] outputs = new byte[len];
    int p = 0;
    for (byte[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static short[] mergeArray(short[]... inputs) {
    if (inputs == null || inputs.length == 0) {
      return new short[0];
    }
    int len = 0;
    for (short[] input : inputs) {
      len += input.length;
    }
    short[] outputs = new short[len];
    int p = 0;
    for (short[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static int[] mergeArray(int[]... inputs) {
    if (inputs == null || inputs.length == 0) {
      return new int[0];
    }
    int len = 0;
    for (int[] input : inputs) {
      len += input.length;
    }
    int[] outputs = new int[len];
    int p = 0;
    for (int[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static long[] mergeArray(long[]... inputs) {
    if (inputs == null || inputs.length == 0) {
      return new long[0];
    }
    int len = 0;
    for (long[] input : inputs) {
      len += input.length;
    }
    long[] outputs = new long[len];
    int p = 0;
    for (long[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static float[] mergeArray(float[]... inputs) {
    if (inputs == null || inputs.length == 0) {
      return new float[0];
    }
    int len = 0;
    for (float[] input : inputs) {
      len += input.length;
    }
    float[] outputs = new float[len];
    int p = 0;
    for (float[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static double[] mergeArray(double[]... inputs) {
    if (inputs == null || inputs.length == 0) {
      return new double[0];
    }
    int len = 0;
    for (double[] input : inputs) {
      len += input.length;
    }
    double[] outputs = new double[len];
    int p = 0;
    for (double[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static String[] mergeArray(String[]... inputs) {
    return mergeArray(String.class, inputs);
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static Class<?>[] mergeArray(Class<?>[]... inputs) {
    return mergeArray(Class.class, inputs);
  }

  /**
   * 合并数组
   *
   * @param inputs 数组
   * @return 返回合并后的数组
   */
  public static <A> A[] mergeArray(Class<?> type, A[]... inputs) {
    if (inputs == null || inputs.length == 0) {
      return (A[]) Array.newInstance(type, 0);
    }
    int len = 0;
    for (Object[] input : inputs) {
      len += input.length;
    }
    if (len == 0) {
      return (A[]) Array.newInstance(type, 0);
    }
    A[] outputs = (A[]) Array.newInstance(type, len);
    int p = 0;
    for (A[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  /**
   * 取最小值
   *
   * @param arr 数字数组
   * @return 最小值
   */
  public static byte min(byte... arr) {
    byte min = arr[0];
    for (byte l : arr) {
      if (min > l) {
        min = l;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param arr 数字数组
   * @return 最小值
   */
  public static char min(char... arr) {
    char min = arr[0];
    for (char l : arr) {
      if (min > l) {
        min = l;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param arr 数字数组
   * @return 最小值
   */
  public static short min(short... arr) {
    short min = arr[0];
    for (short l : arr) {
      if (min > l) {
        min = l;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param arr 数字数组
   * @return 最小值
   */
  public static int min(int... arr) {
    int min = arr[0];
    for (int l : arr) {
      if (min > l) {
        min = l;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param arr 数字数组
   * @return 最小值
   */
  public static long min(long... arr) {
    long min = arr[0];
    for (long l : arr) {
      if (min > l) {
        min = l;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param arr 数字数组
   * @return 最小值
   */
  public static float min(float... arr) {
    float min = arr[0];
    for (float l : arr) {
      if (min > l) {
        min = l;
      }
    }
    return min;
  }

  /**
   * 取最小值
   *
   * @param arr 数字数组
   * @return 最小值
   */
  public static double min(double... arr) {
    double min = arr[0];
    for (double l : arr) {
      if (min > l) {
        min = l;
      }
    }
    return min;
  }

  /**
   * 取最大值
   *
   * @param arr 数字数组
   * @return 最大值
   */
  public static byte max(byte... arr) {
    byte max = arr[0];
    for (byte v : arr) {
      if (max < v) {
        max = v;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param arr 数字数组
   * @return 最大值
   */
  public static char max(char... arr) {
    char max = arr[0];
    for (char v : arr) {
      if (max < v) {
        max = v;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param arr 数字数组
   * @return 最大值
   */
  public static short max(short... arr) {
    short max = arr[0];
    for (short v : arr) {
      if (max < v) {
        max = v;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param arr 数字数组
   * @return 最大值
   */
  public static int max(int... arr) {
    int max = arr[0];
    for (int v : arr) {
      if (max < v) {
        max = v;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param arr 数字数组
   * @return 最大值
   */
  public static long max(long... arr) {
    long max = arr[0];
    for (long v : arr) {
      if (max < v) {
        max = v;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param arr 数字数组
   * @return 最大值
   */
  public static float max(float... arr) {
    float max = arr[0];
    for (float v : arr) {
      if (max < v) {
        max = v;
      }
    }
    return max;
  }

  /**
   * 取最大值
   *
   * @param arr 数字数组
   * @return 最大值
   */
  public static double max(double... arr) {
    double max = arr[0];
    for (double v : arr) {
      if (max < v) {
        max = v;
      }
    }
    return max;
  }

  /**
   * get the value, returns the absolute min value min
   *
   * @param numbers contains elements
   * @return the absolute min value
   */
  public static byte absMin(byte[] numbers) {
    byte absMinValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) < Math.abs(absMinValue)) {
        absMinValue = numbers[i];
      }
    }
    return absMinValue;
  }

  /**
   * get the value, returns the absolute min value min
   *
   * @param numbers contains elements
   * @return the absolute min value
   */
  public static short absMin(short[] numbers) {
    short absMinValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) < Math.abs(absMinValue)) {
        absMinValue = numbers[i];
      }
    }
    return absMinValue;
  }

  /**
   * get the value, returns the absolute min value min
   *
   * @param numbers contains elements
   * @return the absolute min value
   */
  public static int absMin(int[] numbers) {
    int absMinValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) < Math.abs(absMinValue)) {
        absMinValue = numbers[i];
      }
    }
    return absMinValue;
  }

  /**
   * get the value, returns the absolute min value min
   *
   * @param numbers contains elements
   * @return the absolute min value
   */
  public static long absMin(long[] numbers) {
    long absMinValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) < Math.abs(absMinValue)) {
        absMinValue = numbers[i];
      }
    }
    return absMinValue;
  }

  /**
   * get the value, returns the absolute min value min
   *
   * @param numbers contains elements
   * @return the absolute min value
   */
  public static float absMin(float[] numbers) {
    float absMinValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) < Math.abs(absMinValue)) {
        absMinValue = numbers[i];
      }
    }
    return absMinValue;
  }

  /**
   * get the value, returns the absolute min value min
   *
   * @param numbers contains elements
   * @return the absolute min value
   */
  public static double absMin(double[] numbers) {
    double absMinValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) < Math.abs(absMinValue)) {
        absMinValue = numbers[i];
      }
    }
    return absMinValue;
  }

  /**
   * get the value, return the absolute max value
   *
   * @param numbers contains elements
   * @return the absolute max value
   */
  public static byte absMax(byte[] numbers) {
    byte absMaxValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) > Math.abs(absMaxValue)) {
        absMaxValue = numbers[i];
      }
    }
    return absMaxValue;
  }

  /**
   * get the value, return the absolute max value
   *
   * @param numbers contains elements
   * @return the absolute max value
   */
  public static short absMax(short[] numbers) {
    short absMaxValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) > Math.abs(absMaxValue)) {
        absMaxValue = numbers[i];
      }
    }
    return absMaxValue;
  }

  /**
   * get the value, return the absolute max value
   *
   * @param numbers contains elements
   * @return the absolute max value
   */
  public static int absMax(int[] numbers) {
    int absMaxValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) > Math.abs(absMaxValue)) {
        absMaxValue = numbers[i];
      }
    }
    return absMaxValue;
  }

  /**
   * get the value, return the absolute max value
   *
   * @param numbers contains elements
   * @return the absolute max value
   */
  public static long absMax(long[] numbers) {
    long absMaxValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) > Math.abs(absMaxValue)) {
        absMaxValue = numbers[i];
      }
    }
    return absMaxValue;
  }

  /**
   * get the value, return the absolute max value
   *
   * @param numbers contains elements
   * @return the absolute max value
   */
  public static float absMax(float[] numbers) {
    float absMaxValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) > Math.abs(absMaxValue)) {
        absMaxValue = numbers[i];
      }
    }
    return absMaxValue;
  }

  /**
   * get the value, return the absolute max value
   *
   * @param numbers contains elements
   * @return the absolute max value
   */
  public static double absMax(double[] numbers) {
    double absMaxValue = numbers[0];
    for (int i = 1, length = numbers.length; i < length; ++i) {
      if (Math.abs(numbers[i]) > Math.abs(absMaxValue)) {
        absMaxValue = numbers[i];
      }
    }
    return absMaxValue;
  }

  /**
   * 合法化索引
   *
   * @param index 索引
   * @param length 总长度
   * @return 返回合法化索引
   */
  public static int legalizedIndex(int index, int length) {
    while (index < 0) {
      index += length;
    }
    while (index >= length) {
      index -= length;
    }
    return index;
  }

  /**
   * 过滤空
   *
   * @param array 数组
   * @return String[]
   */
  public static String[] filterEmpty(String... array) {
    return filter(array, p -> Whether.empty(p) ? null : p.trim());
  }

  /**
   * 过滤空
   *
   * @param array 数组
   * @return String[]
   */
  public static String[] filterEmpty(Object... array) {
    return filter(
        array,
        p -> {
          if (p == null) {
            return null;
          } else {
            String string = p.toString();
            return Whether.empty(string) ? null : string.trim();
          }
        });
  }

  /**
   * 过滤数组
   *
   * @param array 数组
   * @param process 处理器，返回null时过滤掉
   * @param <T> 类型
   * @return T[]
   */
  public static <T, R> R[] filter(T[] array, FactoryThrow<T, R, Throwable> process) {
    if (Whether.empty(array)) {
      return null;
    }
    List<R> list = new ArrayList<>(array.length);
    for (T ele : array) {
      try {
        R t = process.handle(ele);
        if (t != null) {
          list.add(t);
        }
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    if (list.isEmpty()) {
      return null;
    }
    return (R[]) Arrays.copyOf(list.toArray(), list.size(), array.getClass());
  }

  /**
   * 数组是否包含值
   *
   * @param array 数组
   * @param value 值
   * @return 如果包含返回true，否则返回false
   */
  public static boolean contains(byte[] array, byte value) {
    boolean found = false;
    for (byte v : array) {
      if (v == value) {
        found = true;
        break;
      }
    }
    return found;
  }

  /**
   * 数组是否包含值
   *
   * @param array 数组
   * @param value 值
   * @return 如果包含返回true，否则返回false
   */
  public static boolean contains(short[] array, short value) {
    boolean found = false;
    for (short v : array) {
      if (v == value) {
        found = true;
        break;
      }
    }
    return found;
  }

  /**
   * 数组是否包含值
   *
   * @param array 数组
   * @param value 值
   * @return 如果包含返回true，否则返回false
   */
  public static boolean contains(int[] array, int value) {
    boolean found = false;
    for (int v : array) {
      if (v == value) {
        found = true;
        break;
      }
    }
    return found;
  }

  /**
   * 数组是否包含值
   *
   * @param array 数组
   * @param value 值
   * @return 如果包含返回true，否则返回false
   */
  public static boolean contains(long[] array, long value) {
    boolean found = false;
    for (long v : array) {
      if (v == value) {
        found = true;
        break;
      }
    }
    return found;
  }

  /**
   * 数组是否包含值
   *
   * @param array 数组
   * @param value 值
   * @return 如果包含返回true，否则返回false
   */
  public static boolean contains(float[] array, float value) {
    boolean found = false;
    for (float v : array) {
      if (v == value) {
        found = true;
        break;
      }
    }
    return found;
  }

  /**
   * 数组是否包含值
   *
   * @param array 数组
   * @param value 值
   * @return 如果包含返回true，否则返回false
   */
  public static boolean contains(double[] array, double value) {
    boolean found = false;
    for (double v : array) {
      if (v == value) {
        found = true;
        break;
      }
    }
    return found;
  }

  /**
   * 数组是否包含值
   *
   * @param array 数组
   * @param value 值
   * @return 如果包含返回true，否则返回false
   */
  public static <T> boolean contains(T[] array, T value) {
    boolean found = false;
    for (T v : array) {
      if (v == value || v.equals(value)) {
        found = true;
        break;
      }
    }
    return found;
  }
  /**
   * @param array
   * @param otherArray
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> boolean equals(T[] array, U[] otherArray) {
    return equals(array, otherArray, Object::equals);
  }
  /**
   * @param array
   * @param otherArray
   * @param equalPredicate
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> boolean equals(T[] array, U[] otherArray, BiPredicate<T, U> equalPredicate) {
    if (array == otherArray) return true;
    if (array == null || otherArray == null) return false;

    int length = array.length;
    if (otherArray.length != length) return false;

    for (int i = 0; i < length; i++) {
      if (!(array[i] == null
          ? otherArray[i] == null
          : equalPredicate.test(array[i], otherArray[i]))) return false;
    }

    return true;
  }

  /**
   * @param array
   * @param otherArray
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> boolean isPresent(T[] array, U[] otherArray) {
    return isPresent(array, otherArray, Object::equals);
  }

  /**
   * @param array
   * @param otherArray
   * @param equalPredicate
   * @param <T>
   * @param <U>
   * @return
   */
  public static <T, U> boolean isPresent(
      T[] array, U[] otherArray, BiPredicate<T, U> equalPredicate) {
    if (array == otherArray) return true;
    if (array == null || otherArray == null) return false;

    int length = array.length;
    if (otherArray.length != length) return false;

    Set<T> set = new HashSet<>(length, 1);
    Collections.addAll(set, array);
    Set<U> otherSet = new HashSet<>(length, 1);
    Collections.addAll(otherSet, otherArray);

    if (set.size() != otherSet.size()) {
      return false;
    }
    for (T t : set) {
      for (U u : otherSet) {
        if (equalPredicate.test(t, u)) {
          set.remove(t);
          otherSet.remove(u);
          break;
        } else {
          return false;
        }
      }
    }

    return true;
  }
}

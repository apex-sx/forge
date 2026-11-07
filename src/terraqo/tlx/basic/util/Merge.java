package pxf.toolkit.basic.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import javax.annotation.Nonnull;

/**
 * @author potatoxf
 * @date 2021/4/12
 */
public class Merge {

  /**
   * 将索引合法化
   *
   * <p>如果小于0则会加上len 如果大于len则会剪切len 直到索引在0-len的范围内
   *
   * @param index 索引
   * @param len 目标长度
   * @return 返回合法的索引值
   */
  public static int idx(int index, final int len) {
    if (len == 0) {
      return 0;
    }
    while (index < 0) {
      index += len;
    }
    while (index >= len) {
      index -= len;
    }
    return index;
  }

  /**
   * 过滤NULL值
   *
   * @param inputs 输入参数
   * @return 去除NULL
   */
  @SafeVarargs
  @Nonnull
  public static <T> T[] filterNull(T... inputs) {
    if (Is.nvl(inputs)) {
      throw new NullPointerException();
    }
    if (Is.empty(inputs)) {
      return New.array(inputs, 0);
    }
    int i = Statics.countNoNull(inputs);
    if (i == inputs.length) {
      return inputs;
    }
    T[] result = New.array(inputs, i);
    int srcP = 0, disP = 0;
    i = 0;
    for (Object input : inputs) {
      if (input != null) {
        i++;
      } else {
        if (i != 0) {
          System.arraycopy(inputs, srcP, result, disP, i);
          disP += i;
        }
        srcP += i + 1;
        i = 0;
      }
    }
    if (i != 0) {
      System.arraycopy(inputs, srcP, result, disP, i);
    }
    return result;
  }

  /**
   * 过滤空字符串
   *
   * @param inputs 输入参数
   * @return 去除空字符串
   */
  @Nonnull
  public static String[] filterEmpty(String... inputs) {
    if (Is.empty(inputs)) {
      return Empty.STRING_ARRAY;
    }
    int i = 0;
    for (String input : inputs) {
      if (!"".equals(input)) {
        i++;
      }
    }
    if (i == inputs.length) {
      return inputs;
    }
    String[] result = new String[i];
    int srcP = 0, disP = 0;
    i = 0;
    for (String input : inputs) {
      if (!Is.empty(input)) {
        i++;
      } else {
        if (i != 0) {
          System.arraycopy(inputs, srcP, result, disP, i);
          disP += i;
        }
        srcP += i + 1;
        i = 0;
      }
    }
    if (i != 0) {
      System.arraycopy(inputs, srcP, result, disP, i);
    }
    return result;
  }

  /**
   * 将数组或对象整理成一个一维数组
   *
   * @param any 任何元素
   * @return 目标类型的一维数组
   */
  @Nonnull
  public static Object[] flatArrays(Object... any) {
    return flatArrays((Object) any);
  }

  /**
   * 将数组或对象整理成一个一维数组
   *
   * @param array 任何元素
   * @return 目标类型的一维数组
   */
  @Nonnull
  public static Object[] flatArrays(Object array) {
    int count = Statics.countArrayElement(array);
    if (count == 0) {
      return Empty.OBJECT_ARRAY;
    }
    if (count == 1) {
      return new Object[] {array};
    }
    Object[] results = new Object[count];
    int index = 0;
    Deque<Object> deque = new LinkedList<>();
    deque.push(array);
    while (index < results.length) {
      Object arr = deque.poll();
      int length = Array.getLength(arr);
      for (int i = 0; i < length; i++) {
        Object e = Array.get(arr, i);
        if (Is.arrayObj(e)) {
          deque.push(e);
        } else {
          results[index] = e;
          index++;
        }
      }
    }
    return results;
  }

  // --------------------------------------------------------------------------- 合并数组
  @Nonnull
  public static boolean[] array(boolean[]... inputs) {
    if (Is.empty(inputs)) {
      return Empty.BOOLEAN_ARRAY;
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e.length).sum();
    boolean[] outputs = new boolean[len];
    int p = 0;
    for (boolean[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  @Nonnull
  public static byte[] array(byte[]... inputs) {
    if (Is.empty(inputs)) {
      return Empty.BYTE_ARRAY;
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e.length).sum();
    byte[] outputs = new byte[len];
    int p = 0;
    for (byte[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  @Nonnull
  public static short[] array(short[]... inputs) {
    if (Is.empty(inputs)) {
      return Empty.SHORT_ARRAY;
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e.length).sum();
    short[] outputs = new short[len];
    int p = 0;
    for (short[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  @Nonnull
  public static char[] array(char[]... inputs) {
    if (Is.empty(inputs)) {
      return Empty.CHAR_ARRAY;
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e.length).sum();
    char[] outputs = new char[len];
    int p = 0;
    for (char[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  @Nonnull
  public static int[] array(int[]... inputs) {
    if (Is.empty(inputs)) {
      return Empty.INT_ARRAY;
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e.length).sum();
    int[] outputs = new int[len];
    int p = 0;
    for (int[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  @Nonnull
  public static long[] array(long[]... inputs) {
    if (Is.empty(inputs)) {
      return Empty.LONG_ARRAY;
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e.length).sum();
    long[] outputs = new long[len];
    int p = 0;
    for (long[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  @Nonnull
  public static float[] array(float[]... inputs) {
    if (Is.empty(inputs)) {
      return Empty.FLOAT_ARRAY;
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e.length).sum();
    float[] outputs = new float[len];
    int p = 0;
    for (float[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  @Nonnull
  public static double[] array(double[]... inputs) {
    if (Is.empty(inputs)) {
      return Empty.DOUBLE_ARRAY;
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e.length).sum();
    double[] outputs = new double[len];
    int p = 0;
    for (double[] input : inputs) {
      int copyLen = input.length;
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  @Nonnull
  public static String[] array(@Nonnull final String[]... inputs) {
    if (Is.empty(inputs)) {
      return Empty.STRING_ARRAY;
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e == null ? 0 : e.length).sum();
    String[] outputs = new String[len];
    int p = 0;
    for (String[] input : inputs) {
      if (input == null) {
        continue;
      }
      int copyLen = input.length;
      if (copyLen == 0) {
        continue;
      }
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }

  @SafeVarargs
  @Nonnull
  public static <T> T[] array(@Nonnull final Class<T> outputClass, @Nonnull final T[]... inputs) {
    if (Is.empty(inputs)) {
      return New.array(outputClass, 0);
    }
    final int len = Arrays.stream(inputs).mapToInt(e -> e == null ? 0 : e.length).sum();
    T[] outputs = New.array(outputClass, len);
    int p = 0;
    for (T[] input : inputs) {
      if (input == null) {
        continue;
      }
      int copyLen = input.length;
      if (copyLen == 0) {
        continue;
      }
      System.arraycopy(input, 0, outputs, p, copyLen);
      p += copyLen;
    }
    return outputs;
  }
}

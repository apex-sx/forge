package pxf.toolkit.basic.util;

import java.lang.reflect.Array;
import java.util.Deque;
import java.util.LinkedList;
import javax.annotation.Nullable;

/**
 * 统计工具类
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public final class Statics {

  private Statics() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 统计为 {@code null}
   *
   * @param inputs 输入数组参数
   * @return 返回 {@code null}的个数
   */
  public static int countNull(Object... inputs) {
    if (inputs == null) {
      return -1;
    }
    return inputs.length - countNoNull(inputs);
  }

  /**
   * 统计为不为 {@code null}
   *
   * @param inputs 输入数组参数
   * @param <T> 类型
   * @return 返回不为 {@code null}的个数
   */
  @SafeVarargs
  public static <T> int countNoNull(T... inputs) {
    if (inputs == null) {
      return -1;
    }
    int i = 0;
    for (T input : inputs) {
      if (input != null) {
        i++;
      }
    }
    return i;
  }

  /**
   * 统计包括父类的 {@code Class<?>}
   *
   * @param clz 类
   * @return 返回包括父类的 {@code Class<?>}的个数
   */
  public static int countClass(Class<?> clz) {
    int count = 0;
    for (Class<?> c = clz; c != null && c != Object.class; c = c.getSuperclass()) {
      count++;
    }
    return count;
  }

  /**
   * 统计数组内的不为数组元素的个数
   *
   * @param array 数组
   * @return 返回数组内的不为数组元素的个数
   */
  public static int countArrayElement(Object array) {
    if (Is.nvl(array)) {
      return 0;
    }
    if (!Is.arrayObj(array)) {
      return 1;
    }
    int count = 0;
    Deque<Object> deque = new LinkedList<>();
    deque.push(array);
    while (!deque.isEmpty()) {
      Object arr = deque.poll();
      int length = Array.getLength(arr);
      for (int i = 0; i < length; i++) {
        Object e = Array.get(arr, i);
        if (Is.arrayObj(e)) {
          deque.push(e);
        } else {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * 统计所有条件中为 {@code true}的个数
   *
   * @param conditions 条件
   * @return 返回所有条件中为 {@code true}的个数
   */
  public static int countTrue(boolean... conditions) {
    return countCondition(true, conditions);
  }

  /**
   * 统计所有条件中为 {@code false}的个数
   *
   * @param conditions 条件
   * @return 返回所有条件中为 {@code false}的个数
   */
  public static int countFalse(boolean... conditions) {
    return countCondition(false, conditions);
  }

  private static int countCondition(boolean expected, boolean... conditions) {
    if (Is.empty(conditions)) {
      return 0;
    }
    int count = 0;
    for (boolean condition : conditions) {
      if (condition == expected) {
        count++;
      }
    }
    return count;
  }
}

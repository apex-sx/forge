/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import axf.java.common.utils.exception.NoSupportException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

/**
 * 数组工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 17:00
 */
@SuppressWarnings({"unchecked"})
public final class UtilsOfArray {

  /**
   * 将数组或对象整理成一个一维数组
   *
   * @param eleClz     元素类型，所有元素类型必须一样
   * @param anyElement 元素
   *
   * @return 目标类型的一维数组
   */
  public static <R> R flatArrays(final Class<?> eleClz, final Object... anyElement) {
    return flatArrays(eleClz, (Object) anyElement);
  }

  /**
   * 将数组或对象整理成一个一维数组
   *
   * @param eleClz 元素类型，所有元素类型必须一样
   * @param any    任何元素
   *
   * @return 目标类型的一维数组
   */
  private static <R> R flatArrays(final Class<?> eleClz, final Object any) {
    if (UtilsOfJudge.isNoArray(any)) {
      if (UtilsOfJudge.isEmpty(any)) {
        return null;
      }
      Object result = Array.newInstance(eleClz, 1);
      Array.set(any, 0, any);
      return (R) result;
    }
    final int length = totalElementExceptNull(any);
    final Object result = Array.newInstance(eleClz, length);
    flatArrays(result, any, 0);
    return (R) result;
  }

  private static int flatArrays(final Object result, final Object array, int index) {
    final int length = Array.getLength(array);
    for (int i = 0; i < length; i++) {
      Object value = Array.get(array, i);
      if (UtilsOfJudge.isNoArray(value)) {
        if (UtilsOfJudge.isNoNull(value)) {
          Array.set(result, index, value);
          index++;
        }
      } else {
        index = flatArrays(result, value, index);
      }
    }
    return index;
  }

  private static int totalElementExceptNull(final Object any) {
    if (UtilsOfJudge.isNoArray(any)) {
      return UtilsOfJudge.isNull(any) ? 0 : 1;
    }
    final int length = Array.getLength(any);
    int total = 0;
    for (int i = 0; i < length; i++) {
      Object value = Array.get(any, i);
      if (UtilsOfJudge.isNoArray(value)) {
        if (UtilsOfJudge.isNoNull(value)) {
          total += 1;
        }
      } else {
        int count = totalElementExceptNull(value);
        total += count;
      }
    }
    return total;
  }

  public static <R> R getSet(final Object array, int index, Object newValue) {
    UtilsOfAssert.checkIsArray(array, "No array");
    Object oldValue = Array.get(array, index);
    Array.set(array, index, newValue);
    return (R) oldValue;
  }

  public static <N extends Number> N sum(Object array) {
    return sum(array, 0, Integer.MAX_VALUE);
  }

  public static <N extends Number> N sum(Object array, int start, int end) {
    if (start > end) {
      throw new IllegalArgumentException("Don't allow start greater than end");
    }
    final int length = Array.getLength(array);
    start = UtilsOfJudge.ifElse(start < 0, 0, start);
    end = UtilsOfJudge.ifElse(end > length, length, end);
    double result = 0;
    for (int i = start; i < end; i++) {
      Object value = Array.get(array, i);
      if (value instanceof Number) {
        double v = ((Number) value).doubleValue();
        result += v;
      } else {
        throw new IllegalArgumentException("Don't Number");
      }
    }
    return (N) (Double) result;
  }

  public static <R> R toArray(final Object object, final Class<?> eleClass) {
    Object[] result;
    if (object instanceof Collection) {
      Collection<?> collection = (Collection<?>) object;
      result = collection.toArray((Object[]) Array.newInstance(eleClass, collection.size()));
    } else if (object instanceof Enumeration) {
      Collection<?> collection = Collections.list((Enumeration<?>) object);
      result = collection.toArray((Object[]) Array.newInstance(eleClass, collection.size()));
    } else {
      throw new NoSupportException(object);
    }
    return (R) result;
  }
}

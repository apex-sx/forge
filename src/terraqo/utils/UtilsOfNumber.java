/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils;

import axf.java.common.utils.exception.NoSupportException;
import axf.java.common.utils.function.BiProcessing;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

/**
 * 数字工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 16:59
 */
@SuppressWarnings("unchecked")
public final class UtilsOfNumber {

  private static final Lock NUMBER_LOCK = new ReentrantLock();
  private static final BigDecimal A_NUMBER = new BigDecimal(0);
  private static final BigDecimal B_NUMBER = new BigDecimal(0);
  /**
   * 默认小数的运算精度
   */
  private static int scale = 2;

  /**
   * 设置小数精度
   *
   * @param scale 精度
   */
  public static synchronized void setScale(final int scale) {
    UtilsOfAssert.checkGreaterEqual(scale, 0, "Out of range of decimal bit");
    UtilsOfAssert.checkLesser(scale, 6, "Out of range of decimal bit");
    UtilsOfNumber.scale = scale;
  }

  /**
   * 找出数字中最大值
   *
   * @param array 数组
   * @return 返回最大的值
   */
  public static <R> R max(final Object array) {
    int index = maxIndex(array);
    return (R) Array.get(array, index);
  }

  /**
   * 找出数字中最大值索引
   *
   * @param array 数组
   * @return 返回最大的值的索引
   */
  public static int maxIndex(final Object array) {
    return findIndex(array, (v, max) -> v > max);
  }

  /**
   * 找出数字中条件索引
   *
   * @param array   数组
   * @param compare 比较条件{@code BiProcessing<Double, Double>(value,target)}
   * @return 返回条件索引
   */
  private static int findIndex(final Object array, final BiProcessing<Double, Double> compare) {
    UtilsOfAssert.checkIsArray(array, "Don't array");
    final int length = Array.getLength(array);
    if (length == 0) {
      throw new IllegalArgumentException("Array is empty");
    }
    int index = 0;
    for (int i = 1; i < length; i++) {
      Object value = Array.get(array, i);
      if (value instanceof Number) {
        double v = ((Number) value).doubleValue();
        double target = ((Number) value).doubleValue();
        if (compare.handle(v, target)) {
          index = i;
        }
      } else {
        throw new RuntimeException("Don't number array");
      }
    }
    return index;
  }

  /**
   * 找出数字中最小值
   *
   * @param array 数组
   * @return 返回最小的值
   */
  public static <R> R min(final Object array) {
    int index = minIndex(array);
    return (R) Array.get(array, index);
  }

  /**
   * 找出数字中最小值索引
   *
   * @param array 数组
   * @return 返回最小的值的索引
   */
  public static int minIndex(final Object array) {
    return findIndex(array, (v, max) -> v < max);
  }

  /**
   * 随机整数
   *
   * @param lowerBound 下界
   * @param upperBound 上界
   * @return 返回随机整数
   */
  public static int randomInt(final int lowerBound, final int upperBound) {
    UtilsOfAssert.checkGreater(upperBound, lowerBound, "Error Param!");
    if (lowerBound == upperBound) {
      return lowerBound;
    }
    int result = Integer.MIN_VALUE;
    while (result < lowerBound) {
      result = randomInt(upperBound);
    }
    return result;
  }

  /**
   * 随机整数
   *
   * @param bound 上界
   * @return 返回随机整数
   * @see Random#nextInt(int)
   */
  public static int randomInt(final int bound) {
    return getRandom().nextInt(bound);
  }

  private static Random getRandom() {
    return ThreadLocalRandom.current();
  }

  /**
   * 格式化数字
   *
   * @param number 数字
   * @param <R>    结果类型
   * @return 返回格式化后的结果
   */
  public static <R extends Number> R format(R number) {
    return format(number, 2, RoundingMode.HALF_UP);
  }

  /**
   * 格式化数字
   *
   * @param number       数字
   * @param scale        精度
   * @param roundingMode 格式化模式
   * @param <R>          结果类型
   * @return 返回格式化后的结果
   */
  public static <R extends Number> R format(R number, int scale, RoundingMode roundingMode) {
    if (scale < 0 || scale > 6) {
      scale = UtilsOfNumber.scale;
    }
    BigDecimal tempResult = BigDecimal.valueOf(number.doubleValue()).setScale(scale, roundingMode);
    return (R) numberConvert(tempResult, number.getClass());
  }

  static Object numberConvert(BigDecimal tempResult, Class<?> resultClass) {
    if (resultClass.equals(Byte.class)) {
      return tempResult.byteValueExact();
    } else if (resultClass.equals(Short.class)) {
      return tempResult.shortValueExact();
    } else if (resultClass.equals(Integer.class)) {
      return tempResult.intValueExact();
    } else if (resultClass.equals(Long.class)) {
      return tempResult.longValueExact();
    } else if (resultClass.equals(Float.class)) {
      return tempResult.floatValue();
    } else if (resultClass.equals(Double.class)) {
      return tempResult.doubleValue();
    } else if (resultClass.equals(BigInteger.class)) {
      return tempResult.toBigIntegerExact();
    } else if (resultClass.equals(BigDecimal.class)) {
      return tempResult;
    } else {
      throw new NoSupportException(resultClass);
    }
  }

  /**
   * 加法
   *
   * @param a           a值
   * @param b           b值
   * @param resultClass 结果Class
   * @param <R>         结果类型
   * @param <A>         a类型
   * @param <B>         b类型
   * @return 返回结果
   */
  public static <R extends Number, A extends Number, B extends Number> R add(A a, B b, Class<R> resultClass) {
    return numberOperate(a, b, BigDecimal::add, resultClass);
  }

  /**
   * 数值操作
   *
   * @param a           a值
   * @param b           b值
   * @param handler     a和b处理结果
   * @param resultClass 结果Class
   * @param <R>         结果类型
   * @param <A>         a类型
   * @param <B>         b类型
   * @return 返回结果
   */
  private static <R extends Number, A extends Number, B extends Number> R numberOperate(A a, B b,
      BiFunction<BigDecimal, BigDecimal, BigDecimal> handler, Class<R> resultClass) {
    BigDecimal nA = getNumber(a);
    BigDecimal nB = getNumber(b);
    BigDecimal tempResult = handler.apply(nA, nB);
    return (R) numberConvert(tempResult, resultClass);
  }

  private static BigDecimal getNumber(Number number) {
    return BigDecimal.valueOf(number.doubleValue());
  }

  /**
   * 减法
   *
   * @param a           a值
   * @param b           b值
   * @param resultClass 结果Class
   * @param <R>         结果类型
   * @param <A>         a类型
   * @param <B>         b类型
   * @return 返回结果
   */
  public static <R extends Number, A extends Number, B extends Number> R sub(A a, B b, Class<R> resultClass) {
    return numberOperate(a, b, BigDecimal::subtract, resultClass);
  }

  /**
   * 乘法
   *
   * @param a           a值
   * @param b           b值
   * @param resultClass 结果Class
   * @param <R>         结果类型
   * @param <A>         a类型
   * @param <B>         b类型
   * @return 返回结果
   */
  public static <R extends Number, A extends Number, B extends Number> R mul(A a, B b, Class<R> resultClass) {
    return numberOperate(a, b, BigDecimal::multiply, resultClass);
  }

  /**
   * 除法
   *
   * @param a           a值
   * @param b           b值
   * @param resultClass 结果Class
   * @param <R>         结果类型
   * @param <A>         a类型
   * @param <B>         b类型
   * @return 返回结果
   */
  public static <R extends Number, A extends Number, B extends Number> R div(A a, B b, Class<R> resultClass) {
    return numberOperate(a, b, (bigDecimal, bigDecimal2) -> bigDecimal.divide(bigDecimal2, scale, RoundingMode.HALF_UP), resultClass);
  }
}

/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import java.util.function.BiPredicate;

/**
 * 断言工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 16:56
 */
@SuppressWarnings("unchecked")
public final class UtilsOfAssert {

  public static void fail(final String message) {
    if (message == null) {
      throw new AssertionError();
    }
    throw new AssertionError(message);
  }

  public static <O> O checkIsNoNull(final O object) {
    return checkIsNoNull(object, "Object is null");
  }

  public static <O> O checkIsNoNull(final O object, final String message) {
    if (UtilsOfJudge.isNull(object)) {
      fail(message);
    }
    return object;
  }

  public static String checkValid(final String string) {
    return checkValid(string, "String is invalid");
  }

  public static String checkValid(final String string, final String message) {
    if (UtilsOfJudge.isInvalid(string)) {
      fail(message);
    }
    return string;
  }

  public static <Parent, Child extends Parent> Child checkIsInstance(final Parent object,
      final Class<Child> clz,
      final String message) {
    if (!clz.isInstance(object)) {
      fail(message);
    }
    return (Child) object;
  }

  public static void checkIsArray(final Object object, final String message) {
    if (UtilsOfJudge.isNoEmpty(object)) {
      fail(message);
    }
  }

  public static void checkIsTrue(final boolean b, final String message) {
    if (!b) {
      fail(message);
    }
  }

  public static void checkIsFalse(final boolean b, final String message) {
    if (b) {
      fail(message);
    }
  }

  public static void checkIsEmpty(final Object any, final String message) {
    if (UtilsOfJudge.isNoEmpty(any)) {
      fail(message);
    }
  }

  public static void checkIsNoEmpty(final Object any, final String message) {
    if (UtilsOfJudge.isEmpty(any)) {
      fail(message);
    }
  }

  public static void checkEqual(final Object origin, final Object target, final String message) {
    if (UtilsOfJudge.isNoHumanEqual(origin, target)) {
      fail(message);
    }
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////
  //数字检查
  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static void checkLesser(final Object origin, final Object target, final String message) {
    checkCompare(origin, target, message, (n1, n2) -> n1 >= n2);
  }

  public static void checkGreater(final Object origin, final Object target, final String message) {
    checkCompare(origin, target, message, (n1, n2) -> n1 <= n2);
  }

  public static void checkLesserEqual(final Object origin, final Object target, final String message) {
    checkCompare(origin, target, message, (n1, n2) -> n1 > n2);
  }

  public static void checkGreaterEqual(final Object origin, final Object target, final String message) {
    checkCompare(origin, target, message, (n1, n2) -> n1 < n2);
  }

  public static void checkPositive(final Object origin, final String message) {
    checkGreater(origin, 0, message);
  }

  public static void checkNoPositive(final Object origin, final String message) {
    checkLesserEqual(origin, 0, message);
  }

  public static void checkNegative(final Object origin, final String message) {
    checkLesser(origin, 0, message);
  }

  public static void checkNoNegative(final Object origin, final String message) {
    checkGreaterEqual(origin, 0, message);
  }

  private static void checkCompare(final Object origin, final Object target, final String message,
      final BiPredicate<Double, Double> predicate) {
    if (origin instanceof Number && target instanceof Number) {
      double o = ((Number) origin).doubleValue();
      double t = ((Number) target).doubleValue();
      if (predicate.test(o, t)) {
        fail(message);
      }
      return;
    }
    if (origin instanceof Comparable && target instanceof Comparable) {
      if (predicate.test((double) ((Comparable<Object>) origin).compareTo(target), UtilsOfConstant.D_ZERO)) {
        fail(message);
      }
      return;
    }
    throw new RuntimeException("Don't support");
  }
}

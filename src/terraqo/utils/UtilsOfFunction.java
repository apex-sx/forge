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
import java.util.Deque;
import java.util.Iterator;
import java.util.OptionalInt;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * 功能性工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 16:58
 */
@SuppressWarnings("unchecked")
public final class UtilsOfFunction {

  public static int index(int index, final int len) {
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

  public static <E> E first(final Object any) {
    UtilsOfAssert.checkIsNoEmpty(any, "Don't allow empty");
    Object first;
    if (any instanceof Collection) {
      Collection<?> collection = (Collection<?>) any;
      first = collection.iterator().next();
    } else if (any instanceof Iterable) {
      Iterable<?> iterable = (Iterable<?>) any;
      first = iterable.iterator().next();
    } else if (any instanceof Iterator) {
      Iterator<?> iterator = (Iterator<?>) any;
      first = iterator.next();
    } else if (any instanceof CharSequence) {
      CharSequence charSequence = (CharSequence) any;
      first = charSequence.charAt(0);
    } else if (UtilsOfJudge.isArray(any)) {
      first = Array.get(any, 0);
    } else {
      throw new NoSupportException(any);
    }
    return (E) first;
  }

  public static <R> R findFirstValue(final Object array, final Predicate<Object> judge) {
    int index = findFirstValueIndex(array, judge);
    if (UtilsOfJudge.isEOF(index)) {
      return null;
    }
    return (R) Array.get(array, index);
  }

  public static int findFirstValueIndex(final Object arrayOrCollection, final Predicate<Object> judge) {
    if (UtilsOfJudge.isArray(arrayOrCollection)) {
      final int length = Array.getLength(arrayOrCollection);
      OptionalInt index = IntStream.range(0, length).filter(i -> judge.test(Array.get(arrayOrCollection, i))).findFirst();
      return index.orElse(UtilsOfConstant.EOF);
    }
    if (UtilsOfJudge.isCollection(arrayOrCollection)) {
      Collection<?> collection = (Collection<?>) arrayOrCollection;
      int index = 0;
      for (Object o : collection) {
        if (judge.test(o)) {
          return index;
        }
      }
      return UtilsOfConstant.EOF;
    }
    throw new NoSupportException();
  }

  public static <E> void reserveForEach(Deque<E> deque, Consumer<E> consumer) {
    final int size = deque.size();
    IntStream.range(0, size).forEach(i -> consumer.accept(deque.pop()));
  }

  public static <E> void reserveForEach(Stack<E> stack, Consumer<E> consumer) {
    final int size = stack.size();
    IntStream.range(0, size).forEach(i -> consumer.accept(stack.pop()));
  }
}

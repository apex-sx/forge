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
import java.lang.Thread.State;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : Alfred X Fu
 * @date : 2020/4/16 20:01
 */
@SuppressWarnings("unchecked")
public final class UtilsOfJudge {

  protected static final transient Logger LOGGER = LoggerFactory.getLogger(UtilsOfJudge.class);

  public static boolean isCheckedException(final Throwable ex) {
    return !(ex instanceof RuntimeException || ex instanceof Error);
  }

  public static boolean isCompatibleWithThrowsClause(final Throwable ex, final Class<?>... declaredExceptions) {
    if (!isCheckedException(ex)) {
      return true;
    }
    if (declaredExceptions != null) {
      for (final Class<?> declaredException : declaredExceptions) {
        if (declaredException.isInstance(ex)) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean isContainsConstant(final Enum<?>[] enumValues, final String constant) {
    return isContainsConstant(enumValues, constant, false);
  }

  public static boolean isContainsConstant(final Enum<?>[] enumValues, final String constant, final boolean caseSensitive) {
    for (final Enum<?> candidate : enumValues) {
      if (caseSensitive ? candidate.toString().equals(constant) : candidate.toString()
          .equalsIgnoreCase(constant)) {
        return true;
      }
    }
    return false;
  }

  public static boolean isExistClass(final String classFullyQualifiedName) {
    return isExistClass(classFullyQualifiedName, ClassLoader.getSystemClassLoader()) ||
        isExistClass(classFullyQualifiedName, ClassLoader.getPlatformClassLoader());
  }

  public static boolean isExistClass(final String classFullyQualifiedName, final ClassLoader classLoader) {
    Class<?> clazz = null;
    try {
      clazz = Class.forName(classFullyQualifiedName, false, classLoader);
    } catch (final ClassNotFoundException ignored) {
      LOGGER.debug(
          "Class loader of [" + classLoader + "] could No be found [" + classFullyQualifiedName
              + "]");
    }
    return clazz != null;
  }

  public static <N extends Number> boolean isEOF(N result) {
    return result.intValue() == UtilsOfConstant.EOF;
  }

  public static boolean isTerminated(Thread thread) {
    return isNull(thread) || State.TERMINATED.equals(thread.getState());
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////
  //类型判断
  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static boolean isCollection(final Object object) {
    return object instanceof Collection;
  }

  public static boolean isNoCollection(final Object object) {
    return !isCollection(object);
  }

  public static boolean isArray(final Object object) {
    return object != null && object.getClass().isArray();
  }

  public static boolean isNoArray(final Object object) {
    return !isArray(object);
  }

  public static boolean isString(final Object object) {
    return isString(object.getClass());
  }

  public static boolean isNoString(final Object object) {
    return !isString(object);
  }

  public static boolean isString(final Class<?> clz) {
    return String.class.equals(clz);
  }

  public static boolean isNoString(final Class<?> clz) {
    return !isNumber(clz);
  }

  public static boolean isNumber(final Object object) {
    return isNumber(object.getClass());
  }

  public static boolean isNoNumber(final Object object) {
    return !isNumber(object);
  }

  public static boolean isNumber(final Class<?> clz) {
    if (Number.class.isAssignableFrom(clz)) {
      return true;
    }
    if (clz.isPrimitive()) {
      return !clz.equals(boolean.class) && !clz.equals(char.class);
    }
    return false;
  }

  public static boolean isNoNumber(final Class<?> clz) {
    return !isNumber(clz);
  }

  public static boolean isCharacter(final Object object) {
    return isCharacter(object.getClass());
  }

  public static boolean isNoCharacter(final Object object) {
    return !isCharacter(object);
  }

  public static boolean isCharacter(final Class<?> clz) {
    if (Character.class.isAssignableFrom(clz)) {
      return true;
    }
    if (clz.isPrimitive()) {
      return clz.equals(char.class);
    }
    return false;
  }

  public static boolean isNoCharacter(final Class<?> clz) {
    return !isCharacter(clz);
  }

  public static boolean isBoolean(final Object object) {
    return isBoolean(object.getClass());
  }

  public static boolean isNoBoolean(final Object object) {
    return !isBoolean(object);
  }

  public static boolean isBoolean(final Class<?> clz) {
    if (Boolean.class.isAssignableFrom(clz)) {
      return true;
    }
    if (clz.isPrimitive()) {
      return clz.equals(boolean.class);
    }
    return false;
  }

  public static boolean isNoBoolean(final Class<?> clz) {
    return !isBoolean(clz);
  }

  public static boolean isBasic(final Object object) {
    return isBasic(object.getClass());
  }

  public static boolean isNoBasic(final Object object) {
    return !isBasic(object);
  }

  public static boolean isBasic(final Class<?> clz) {
    if (isNumber(clz)) {
      return true;
    }
    if (isBoolean(clz)) {
      return true;
    }
    if (isCharacter(clz)) {
      return true;
    }
    return isString(clz);
  }

  public static boolean isNoBasic(final Class<?> clz) {
    return !isBasic(clz);
  }

  public static boolean isExBasic(final Object object) {
    return isBasic(object.getClass());
  }

  public static boolean isNoExBasic(final Object object) {
    return !isBasic(object);
  }

  public static boolean isExBasic(final Class<?> clz) {
    if (isBasic(clz)) {
      return true;
    }
    return Date.class.isAssignableFrom(clz);
  }

  public static boolean isNoExBasic(final Class<?> clz) {
    return !isBasic(clz);
  }

  public static <Parent, Child extends Parent> boolean isInstance(final Class<Child> clz, final Parent... objects) {
    return isAllMappingCorrect(objects, o -> isInstance(clz, o));
  }

  public static <Parent, Child extends Parent> boolean isInstance(final Class<Child> clz, final Parent object) {
    return clz.isInstance(object);
  }

  public static <Parent, Child extends Parent> boolean isNoInstance(final Class<Child> clz, final Parent... objects) {
    return isAllMappingCorrect(objects, o -> isNoInstance(clz, o));
  }

  public static <Parent, Child extends Parent> boolean isNoInstance(final Class<Child> clz, final Parent object) {
    return !clz.isInstance(object);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////
  //简单逻辑判断
  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static <T> T ifNull(T value, T defaultValue) {
    return value != null ? value : defaultValue;
  }

  public static <T> String ifNull(T value, String defaultValue) {
    if (null == value) {
      return defaultValue;
    }
    return Objects.toString(value);
  }

  public static <T> T ifElse(boolean value, T isTrue, T isFalse) {
    if (value) {
      return isTrue;
    }
    return isFalse;
  }

  public static <T> T ifEmpty(T value, T defaultValue) {
    if (null == value) {
      return defaultValue;
    }
    int length = -1;
    if (isArray(value)) {
      length = Array.getLength(value);
    } else if (value instanceof Collection) {
      length = ((Collection<?>) value).size();
    } else if (value instanceof String) {
      length = ((String) value).length();
    }
    if (isEOF(length)) {
      return value;
    }
    return length == 0 ? defaultValue : value;
  }

  public static <N extends Number> N ifInvalid(String value, N defaultValue) {
    if (null == value) {
      return defaultValue;
    }
    return (N) UtilsOfNumber.numberConvert(new BigDecimal(value), defaultValue.getClass());
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////
  //null判断
  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static boolean isNull(final Object object) {
    return object == null;
  }

  public static boolean isNoNull(final Object object) {
    return !isNull(object);
  }

  public static boolean isAllNull(final Object... objects) {
    return !isAnyNoNull();
  }

  public static boolean isAllNoNull(final Object... objects) {
    return !isAnyNull();
  }

  public static boolean isAnyNull(final Object... objects) {
    return isAnyMappingCorrect(objects, UtilsOfJudge::isNull);
  }

  public static boolean isAnyNoNull(final Object... objects) {
    return isAnyMappingCorrect(objects, UtilsOfJudge::isNoNull);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////
  //空判断
  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static boolean isEmpty(final Object any) {
    if (any == null) {
      return true;
    }
    if (any instanceof CharSequence) {
      return ((CharSequence) any).length() == 0;
    } else if (any instanceof Collection) {
      return ((Collection<?>) any).size() == 0;
    } else if (any instanceof Iterable) {
      return !((Iterable<?>) any).iterator().hasNext();
    } else if (any instanceof Iterator) {
      return ((Iterator<?>) any).hasNext();
    } else if (isArray(any)) {
      return Array.getLength(any) == 0;
    }
    throw new NoSupportException(any);
  }

  public static boolean isNoEmpty(final Object any) {
    return !isEmpty(any);
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////
  //字符判断
  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static String valid(final String string) {
    return string == null || string.length() == 0 ? "" : string;
  }

  public static String valid(final String string, final String defaultValue) {
    return string == null || string.length() == 0 ? defaultValue : string;
  }

  public static boolean isValid(final CharSequence charSequence) {
    return charSequence != null && charSequence.length() != 0;
  }

  public static boolean isInvalid(final CharSequence charSequence) {
    return charSequence == null || charSequence.length() == 0;
  }

  public static boolean isAllValid(final CharSequence... charSequences) {
    return !isAnyInvalid(charSequences);
  }

  public static boolean isAllValid(final Collection<? extends CharSequence> charSequences) {
    return !isAnyInvalid(charSequences);
  }

  public static boolean isAnyInvalid(final CharSequence... charSequences) {
    if (isEmpty(charSequences)) {
      return true;
    }
    return isAnyMappingCorrect(charSequences, o -> isInvalid((CharSequence) o));
  }

  public static boolean isAnyInvalid(final Collection<? extends CharSequence> charSequences) {
    if (isEmpty(charSequences)) {
      return true;
    }
    return isAnyMappingCorrect(charSequences, o -> isInvalid((CharSequence) o));
  }

  public static boolean isHasText(final CharSequence charSequence) {
    return !isNoHasText(charSequence);
  }

  public static boolean isNoHasText(final CharSequence charSequence) {
    if (isInvalid(charSequence)) {
      return true;
    }
    final int strLen = charSequence.length();
    return isRangeAllMappingCorrect(strLen, (i) -> Character.isWhitespace(charSequence.charAt(i)));
  }

  public static boolean isAllHasText(final CharSequence... charSequences) {
    return !isAnyNoHasText(charSequences);
  }

  public static boolean isAllHasText(final Collection<? extends CharSequence> charSequences) {
    return !isAnyNoHasText(charSequences);
  }

  public static boolean isAnyNoHasText(final CharSequence... charSequences) {
    if (isEmpty(charSequences)) {
      return true;
    }
    return isAnyMappingCorrect(charSequences, o -> isNoHasText((CharSequence) o));
  }

  public static boolean isAnyNoHasText(final Collection<? extends CharSequence> charSequences) {
    if (isEmpty(charSequences)) {
      return true;
    }
    return isAnyMappingCorrect(charSequences, o -> isNoHasText((CharSequence) o));
  }

  public static boolean isHasPrefix(final String string, final String prefix) {
    if (isAnyInvalid(string, prefix)) {
      return false;
    }
    return string.indexOf(prefix) == 0;
  }

  public static boolean isHasPrefixIgnoredCase(final String string, final String prefix) {
    if (isAnyInvalid(string, prefix)) {
      return false;
    }
    return string.toLowerCase().indexOf(prefix.toLowerCase()) == 0;
  }

  public static boolean isHasSuffix(final String string, final String suffix) {
    if (isAnyInvalid(string, suffix)) {
      return false;
    }
    return string.lastIndexOf(suffix) == string.length() - suffix.length();
  }

  public static boolean isHasSuffixIgnoredCase(final String string, final String suffix) {
    if (isAnyInvalid(string, suffix)) {
      return false;
    }
    return string.toLowerCase().lastIndexOf(suffix.toLowerCase()) == string.length() - suffix
        .length();
  }

  public static boolean isEndsWithIgnoreCase(final String str, final String suffix) {
    return (str != null && suffix != null && str.length() >= suffix.length() && str
        .regionMatches(true,
            str.length() - suffix
                .length(),
            suffix, 0,
            suffix.length()
        ));
  }

  public static boolean isStartWithIgnoreCase(final String str, final String prefix) {
    return (str != null && prefix != null && str.length() >= prefix.length() && str
        .regionMatches(true, 0, prefix,
            0, prefix.length()
        ));
  }

  public static boolean isContainsWhitespace(final CharSequence charSequence) {
    if (isInvalid(charSequence)) {
      return false;
    }
    return isRangeAnyMappingCorrect(charSequence.length(),
        i -> Character.isWhitespace(charSequence.charAt(i)));
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////
  //大小判断
  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static boolean isHumanEqual(final Object origin, final Object target) {
    if (origin == null) {
      return target == null;
    }
    if (origin == target) {
      return true;
    }
    if (origin.equals(target)) {
      return true;
    }
    if (isInstance(String.class, origin, target)) {
      String o = (String) origin;
      String t = (String) target;
      return o.equalsIgnoreCase(t);
    }
    if (isInstance(Number.class, origin, target)) {
      double o = ((Number) origin).doubleValue();
      double t = ((Number) target).doubleValue();
      return o == t;
    }
    if (isArray(origin) && isArray(target)) {
      return isArrayEquals(origin, target);
    }
    return false;
  }

  public static boolean isNoHumanEqual(final Object origin, final Object target) {
    return !isHumanEqual(origin, target);
  }

  private static boolean isArrayEquals(final Object o1, final Object o2) {
    if (o1 instanceof Object[] && o2 instanceof Object[]) {
      return Arrays.equals((Object[]) o1, (Object[]) o2);
    }
    if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
      return Arrays.equals((boolean[]) o1, (boolean[]) o2);
    }
    if (o1 instanceof byte[] && o2 instanceof byte[]) {
      return Arrays.equals((byte[]) o1, (byte[]) o2);
    }
    if (o1 instanceof char[] && o2 instanceof char[]) {
      return Arrays.equals((char[]) o1, (char[]) o2);
    }
    if (o1 instanceof double[] && o2 instanceof double[]) {
      return Arrays.equals((double[]) o1, (double[]) o2);
    }
    if (o1 instanceof float[] && o2 instanceof float[]) {
      return Arrays.equals((float[]) o1, (float[]) o2);
    }
    if (o1 instanceof int[] && o2 instanceof int[]) {
      return Arrays.equals((int[]) o1, (int[]) o2);
    }
    if (o1 instanceof long[] && o2 instanceof long[]) {
      return Arrays.equals((long[]) o1, (long[]) o2);
    }
    if (o1 instanceof short[] && o2 instanceof short[]) {
      return Arrays.equals((short[]) o1, (short[]) o2);
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////
  //数组及范围判断
  //////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static boolean isRangeAllMappingCorrect(final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.range(0, end), judge, IntStream::allMatch, true);
  }

  public static boolean isRangeAllMappingCorrect(final int start, final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.range(start, end), judge, IntStream::allMatch, true);
  }

  public static boolean isRangeAllMappingMistake(final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.range(0, end), judge, IntStream::allMatch, false);
  }

  public static boolean isRangeAllMappingMistake(final int start, final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.range(start, end), judge, IntStream::allMatch, false);
  }

  public static boolean isRangeAnyMappingCorrect(final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.range(0, end), judge, IntStream::anyMatch, true);
  }

  public static boolean isRangeAnyMappingCorrect(final int start, final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.range(start, end), judge, IntStream::anyMatch, true);
  }

  public static boolean isRangeAnyMappingMistake(final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.range(0, end), judge, IntStream::anyMatch, false);
  }

  public static boolean isRangeAnyMappingMistake(final int start, final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.range(start, end), judge, IntStream::anyMatch, false);
  }

  public static boolean isRangeClosedAllMappingCorrect(final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.rangeClosed(0, end), judge, IntStream::allMatch,
        true);
  }

  public static boolean isRangeClosedAllMappingCorrect(final int start, final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.rangeClosed(start, end), judge, IntStream::allMatch,
        true);
  }

  public static boolean isRangeClosedAllMappingMistake(final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.rangeClosed(0, end), judge, IntStream::allMatch,
        false);
  }

  public static boolean isRangeClosedAllMappingMistake(final int start, final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.rangeClosed(start, end), judge, IntStream::allMatch,
        false);
  }

  public static boolean isRangeClosedAnyMappingCorrect(final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.rangeClosed(0, end), judge, IntStream::anyMatch,
        true);
  }

  public static boolean isRangeClosedAnyMappingCorrect(final int start, final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.rangeClosed(start, end), judge, IntStream::anyMatch,
        true);
  }

  public static boolean isRangeClosedAnyMappingMistake(final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.rangeClosed(0, end), judge, IntStream::anyMatch,
        false);
  }

  public static boolean isRangeClosedAnyMappingMistake(final int start, final int end, final IntPredicate judge) {
    return isRangeMapping(() -> IntStream.rangeClosed(start, end), judge, IntStream::anyMatch,
        false);
  }

  private static boolean isRangeMapping(Supplier<IntStream> intStreamSupplier,
      final IntPredicate judge, final BiProcessing<IntStream, IntPredicate> handler,
      final boolean expected) {
    IntStream intStream = intStreamSupplier.get();
    return handler.handle(intStream, i -> expected == judge.test(i));
  }

  public static boolean isAllMappingCorrect(final Object arrayOrCollection, final Predicate<Object> judge) {
    return isMapping(arrayOrCollection, judge, IntStream::allMatch, true);
  }

  public static boolean isAllMappingMistake(final Object arrayOrCollection, final Predicate<Object> judge) {
    return isMapping(arrayOrCollection, judge, IntStream::allMatch, false);
  }

  public static boolean isAllMappingCorrect(final Object arrayA, final Object arrayB, final BiPredicate<Object, Object> judge) {
    return isMapping(arrayA, arrayB, judge, IntStream::allMatch, true);
  }

  public static boolean isAllMappingMistake(final Object arrayA, final Object arrayB, final BiPredicate<Object, Object> judge) {
    return isMapping(arrayA, arrayB, judge, IntStream::allMatch, false);
  }

  public static boolean isAnyMappingCorrect(final Object arrayOrCollection, final Predicate<Object> judge) {
    return isMapping(arrayOrCollection, judge, IntStream::anyMatch, true);
  }

  public static boolean isAnyMappingMistake(final Object arrayOrCollection, final Predicate<Object> judge) {
    return isMapping(arrayOrCollection, judge, IntStream::anyMatch, false);
  }

  public static boolean isAnyMappingCorrect(final Object arrayA, final Object arrayB, final BiPredicate<Object, Object> judge) {
    return isMapping(arrayA, arrayB, judge, IntStream::anyMatch, true);
  }

  public static boolean isAnyMappingMistake(final Object arrayA, final Object arrayB, final BiPredicate<Object, Object> judge) {
    return isMapping(arrayA, arrayB, judge, IntStream::anyMatch, false);
  }

  private static boolean isMapping(final Object arrayOrCollection, final Predicate<Object> judge,
      final BiProcessing<IntStream, IntPredicate> handler, final boolean expected) {
    if (isArray(arrayOrCollection)) {
      final int length = Array.getLength(arrayOrCollection);
      if (length == 0) {
        return expected;
      }
      return handler.handle(IntStream.range(0, length),
          i -> expected == judge.test(Array.get(arrayOrCollection, i)));
    }
    if (arrayOrCollection instanceof Collection) {
      Collection<Object> collection = (Collection<Object>) arrayOrCollection;
      Iterator<Object> iterator = collection.iterator();
      return handler.handle(IntStream.range(0, collection.size()), i -> {
        Object object = iterator.next();
        return expected == judge.test(object);
      });
    }
    throw new NoSupportException(arrayOrCollection);
  }

  private static boolean isMapping(final Object arrayA, final Object arrayB, final BiPredicate<Object, Object> judge,
      final BiProcessing<IntStream, IntPredicate> handler, final boolean expected) {
    if (isNoArray(arrayA)) {
      throw new IllegalArgumentException();
    }
    if (isNoArray(arrayB)) {
      throw new IllegalArgumentException();
    }
    final int length = Math.min(Array.getLength(arrayA), Array.getLength(arrayB));
    if (length == 0) {
      return expected;
    }
    return handler.handle(IntStream.range(0, length),
        i -> expected == judge.test(Array.get(arrayA, i), Array.get(arrayB, i)));
  }

  private static boolean isCompare(final Object origin, final Object target, final String message,
      final BiPredicate<Double, Double> predicate) {
    if (origin instanceof Number && target instanceof Number) {
      double o = ((Number) origin).doubleValue();
      double t = ((Number) target).doubleValue();
      return predicate.test(o, t);
    }
    if (origin instanceof Comparable && target instanceof Comparable) {
      return predicate.test((double) ((Comparable<Object>) origin).compareTo(target), UtilsOfConstant.D_ZERO);
    }
    throw new RuntimeException("Don't support");
  }
}

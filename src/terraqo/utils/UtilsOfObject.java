/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import axf.java.common.utils.exception.FailureException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

/**
 * 对象工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 17:00
 */
@SuppressWarnings("unchecked")
public final class UtilsOfObject {

  protected static final transient Logger LOGGER = LoggerFactory.getLogger(UtilsOfObject.class);
  private static final String ARRAY_START = "{";
  private static final String ARRAY_END = "}";
  private static final String EMPTY_ARRAY = "{}";
  private static final String ARRAY_ELEMENT_SEPARATOR = ", ";

  /**
   * 获取Unsafe
   *
   * @return {@code Unsafe}
   */
  public static Unsafe getUnsafe() {
    try {
      Field f = Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      return (Unsafe) f.get(null);
    } catch (Exception e) {
      throw new FailureException(e);
    }
  }

  /**
   * 深度克隆对象
   */
  public static <O extends Serializable> O deepClone(O obj) {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    serialize(obj, bout);
    return deserialize(new ByteArrayInputStream(bout.toByteArray()));
  }

  /**
   * 反序列化
   *
   * @see java.io.Serializable
   * <code>
   * private void writeObject(java.io.ObjectOutputStream out) throws IOException private void private Object writeReplace() throws
   * ObjectStreamException;
   * </code>
   * @see java.io.Externalizable
   * <code>
   * void writeExternal(ObjectOutput out) throws IOException
   * </code>
   */
  public static <O extends Serializable> void serialize(O obj, OutputStream out) {
    try {
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
      objectOutputStream.writeObject(obj);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 序列化
   *
   * @see java.io.Serializable
   * <code>
   * private void readObject(java.io.ObjectInputStream in) throws IOException,ClassNotFoundException private void readObjectNoData() throws
   * ObjectStreamException private Object readResolve() throws ObjectStreamException;
   * </code>
   * @see java.io.Externalizable
   * <code>
   * void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
   * </code>
   */
  public static <O extends Serializable> O deserialize(InputStream in) {
    try {
      ObjectInputStream objectInputStream = new ObjectInputStream(in);
      return (O) objectInputStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取对象的字符串识别串
   */
  public static String identity(final Object obj) {
    if (obj == null) {
      return UtilsOfConstant.EMPTY;
    }
    return obj.getClass().getName() + "@" + identityHex(obj);
  }

  /**
   * 获取对象的16进制识别码
   */
  public static String identityHex(final Object obj) {
    return Integer.toHexString(System.identityHashCode(obj));
  }

  public static <E extends Enum<?>> E caseInsensitiveValueOf(final Class<E> eClass,
      final String constant) {
    final E[] enumValues = eClass.getEnumConstants();
    for (final E candidate : enumValues) {
      if (candidate.toString().equalsIgnoreCase(constant)) {
        return candidate;
      }
    }
    return null;
  }

  public static Class<?> classForName(final String classFullyQualifiedName) {
    try {
      return Class.forName(classFullyQualifiedName);
    } catch (final ClassNotFoundException ignored) {
      LOGGER.warn("Not load class of [" + classFullyQualifiedName + "]");
    }
    return null;
  }

  public static Class<?> classForName(final String classFullyQualifiedName,
      final ClassLoader classLoader) {
    try {
      return Class.forName(classFullyQualifiedName, true, classLoader);
    } catch (final ClassNotFoundException ignored) {
      LOGGER.warn(
          "Class loader of [" + classLoader + "] could load [" + classFullyQualifiedName + "]");
    }
    return null;
  }

  public static <T> T simpleNewInstance(final Class<T> clz) {
    try {
      final Constructor<? extends T> constructor = clz.getDeclaredConstructor();
      return constructor.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static Object[] toObjectArray(final Object source) {
    if (source instanceof Object[]) {
      return (Object[]) source;
    }
    if (source == null) {
      return UtilsOfConstant.EMPTY_OBJECT_ARRAY;
    }
    if (!source.getClass().isArray()) {
      throw new IllegalArgumentException("Source is not an array: " + source);
    }
    final int length = Array.getLength(source);
    if (length == 0) {
      return UtilsOfConstant.EMPTY_OBJECT_ARRAY;
    }
    final Class<?> wrapperType = Array.get(source, 0).getClass();
    final Object[] newArray = (Object[]) Array.newInstance(wrapperType, length);
    for (int i = 0; i < length; i++) {
      newArray[i] = Array.get(source, i);
    }
    return newArray;
  }

  public static Object unwrapOptional(final Object obj) {
    if (obj instanceof Optional) {
      final Optional<?> optional = (Optional<?>) obj;
      if (optional.isEmpty()) {
        return null;
      }
      return optional.get();
    }
    return obj;
  }

  public static String nullSafeClassName(final Object obj) {
    return (obj != null ? obj.getClass().getName() : UtilsOfConstant.NULL);
  }

  public static int nullSafeHashCode(final Object obj) {
    if (obj == null) {
      return 0;
    }
    if (obj.getClass().isArray()) {
      if (obj instanceof Object[]) {
        return nullSafeHashCode((Object[]) obj);
      }
      if (obj instanceof boolean[]) {
        return nullSafeHashCode((boolean[]) obj);
      }
      if (obj instanceof byte[]) {
        return nullSafeHashCode((byte[]) obj);
      }
      if (obj instanceof char[]) {
        return nullSafeHashCode((char[]) obj);
      }
      if (obj instanceof double[]) {
        return nullSafeHashCode((double[]) obj);
      }
      if (obj instanceof float[]) {
        return nullSafeHashCode((float[]) obj);
      }
      if (obj instanceof int[]) {
        return nullSafeHashCode((int[]) obj);
      }
      if (obj instanceof long[]) {
        return nullSafeHashCode((long[]) obj);
      }
      if (obj instanceof short[]) {
        return nullSafeHashCode((short[]) obj);
      }
    }
    return obj.hashCode();
  }

  public static int nullSafeHashCode(final Object[] array) {
    if (array == null) {
      return 0;
    }
    int hash = UtilsOfConstant.INITIAL_HASH;
    for (final Object element : array) {
      hash = UtilsOfConstant.MULTIPLIER * hash + nullSafeHashCode(element);
    }
    return hash;
  }

  public static int nullSafeHashCode(final boolean[] array) {
    if (array == null) {
      return 0;
    }
    int hash = UtilsOfConstant.INITIAL_HASH;
    for (final boolean element : array) {
      hash = UtilsOfConstant.MULTIPLIER * hash + Boolean.hashCode(element);
    }
    return hash;
  }

  public static int nullSafeHashCode(final byte[] array) {
    if (array == null) {
      return 0;
    }
    int hash = UtilsOfConstant.INITIAL_HASH;
    for (final byte element : array) {
      hash = UtilsOfConstant.MULTIPLIER * hash + element;
    }
    return hash;
  }

  public static int nullSafeHashCode(final char[] array) {
    if (array == null) {
      return 0;
    }
    int hash = UtilsOfConstant.INITIAL_HASH;
    for (final char element : array) {
      hash = UtilsOfConstant.MULTIPLIER * hash + element;
    }
    return hash;
  }

  public static int nullSafeHashCode(final double[] array) {
    if (array == null) {
      return 0;
    }
    int hash = UtilsOfConstant.INITIAL_HASH;
    for (final double element : array) {
      hash = UtilsOfConstant.MULTIPLIER * hash + Double.hashCode(element);
    }
    return hash;
  }

  public static int nullSafeHashCode(final float[] array) {
    if (array == null) {
      return 0;
    }
    int hash = UtilsOfConstant.INITIAL_HASH;
    for (final float element : array) {
      hash = UtilsOfConstant.MULTIPLIER * hash + Float.hashCode(element);
    }
    return hash;
  }

  public static int nullSafeHashCode(final int[] array) {
    if (array == null) {
      return 0;
    }
    int hash = UtilsOfConstant.INITIAL_HASH;
    for (final int element : array) {
      hash = UtilsOfConstant.MULTIPLIER * hash + element;
    }
    return hash;
  }

  public static int nullSafeHashCode(final long[] array) {
    if (array == null) {
      return 0;
    }
    int hash = UtilsOfConstant.INITIAL_HASH;
    for (final long element : array) {
      hash = UtilsOfConstant.MULTIPLIER * hash + Long.hashCode(element);
    }
    return hash;
  }

  public static int nullSafeHashCode(final short[] array) {
    if (array == null) {
      return 0;
    }
    int hash = UtilsOfConstant.INITIAL_HASH;
    for (final short element : array) {
      hash = UtilsOfConstant.MULTIPLIER * hash + element;
    }
    return hash;
  }

  /**
   * 显示对象的字符串
   */
  public static String display(final Object obj) {
    if (obj == null) {
      return UtilsOfConstant.EMPTY;
    }
    return nullSafeToString(obj);
  }

  public static String nullSafeToString(final Object obj) {
    if (obj == null) {
      return UtilsOfConstant.NULL;
    }
    if (obj instanceof String) {
      return (String) obj;
    }
    if (obj instanceof Object[]) {
      return nullSafeToString((Object[]) obj);
    }
    if (obj instanceof boolean[]) {
      return nullSafeToString((boolean[]) obj);
    }
    if (obj instanceof byte[]) {
      return nullSafeToString((byte[]) obj);
    }
    if (obj instanceof char[]) {
      return nullSafeToString((char[]) obj);
    }
    if (obj instanceof double[]) {
      return nullSafeToString((double[]) obj);
    }
    if (obj instanceof float[]) {
      return nullSafeToString((float[]) obj);
    }
    if (obj instanceof int[]) {
      return nullSafeToString((int[]) obj);
    }
    if (obj instanceof long[]) {
      return nullSafeToString((long[]) obj);
    }
    if (obj instanceof short[]) {
      return nullSafeToString((short[]) obj);
    }
    final String str = obj.toString();
    return (str != null ? str : UtilsOfConstant.EMPTY);
  }

  public static String nullSafeToString(final Object[] array) {
    if (array == null) {
      return UtilsOfConstant.NULL;
    }
    final int length = array.length;
    if (length == 0) {
      return EMPTY_ARRAY;
    }
    final StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR,
        ARRAY_START,
        ARRAY_END
    );
    for (final Object o : array) {
      stringJoiner.add(String.valueOf(o));
    }
    return stringJoiner.toString();
  }

  public static String nullSafeToString(final boolean[] array) {
    if (array == null) {
      return UtilsOfConstant.NULL;
    }
    final int length = array.length;
    if (length == 0) {
      return EMPTY_ARRAY;
    }
    final StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR,
        ARRAY_START,
        ARRAY_END
    );
    for (final boolean b : array) {
      stringJoiner.add(String.valueOf(b));
    }
    return stringJoiner.toString();
  }

  public static String nullSafeToString(final byte[] array) {
    if (array == null) {
      return UtilsOfConstant.NULL;
    }
    final int length = array.length;
    if (length == 0) {
      return EMPTY_ARRAY;
    }
    final StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR,
        ARRAY_START,
        ARRAY_END
    );
    for (final byte b : array) {
      stringJoiner.add(String.valueOf(b));
    }
    return stringJoiner.toString();
  }

  public static String nullSafeToString(final char[] array) {
    if (array == null) {
      return UtilsOfConstant.NULL;
    }
    final int length = array.length;
    if (length == 0) {
      return EMPTY_ARRAY;
    }
    final StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR,
        ARRAY_START,
        ARRAY_END
    );
    for (final char c : array) {
      stringJoiner.add('\'' + String.valueOf(c) + '\'');
    }
    return stringJoiner.toString();
  }

  public static String nullSafeToString(final double[] array) {
    if (array == null) {
      return UtilsOfConstant.NULL;
    }
    final int length = array.length;
    if (length == 0) {
      return EMPTY_ARRAY;
    }
    final StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR,
        ARRAY_START,
        ARRAY_END
    );
    for (final double d : array) {
      stringJoiner.add(String.valueOf(d));
    }
    return stringJoiner.toString();
  }

  public static String nullSafeToString(final float[] array) {
    if (array == null) {
      return UtilsOfConstant.NULL;
    }
    final int length = array.length;
    if (length == 0) {
      return EMPTY_ARRAY;
    }
    final StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR,
        ARRAY_START,
        ARRAY_END
    );
    for (final float f : array) {
      stringJoiner.add(String.valueOf(f));
    }
    return stringJoiner.toString();
  }

  public static String nullSafeToString(final int[] array) {
    if (array == null) {
      return UtilsOfConstant.NULL;
    }
    final int length = array.length;
    if (length == 0) {
      return EMPTY_ARRAY;
    }
    final StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR,
        ARRAY_START,
        ARRAY_END
    );
    for (final int i : array) {
      stringJoiner.add(String.valueOf(i));
    }
    return stringJoiner.toString();
  }

  public static String nullSafeToString(final long[] array) {
    if (array == null) {
      return UtilsOfConstant.NULL;
    }
    final int length = array.length;
    if (length == 0) {
      return EMPTY_ARRAY;
    }
    final StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR,
        ARRAY_START,
        ARRAY_END
    );
    for (final long l : array) {
      stringJoiner.add(String.valueOf(l));
    }
    return stringJoiner.toString();
  }

  public static String nullSafeToString(final short[] array) {
    if (array == null) {
      return UtilsOfConstant.NULL;
    }
    final int length = array.length;
    if (length == 0) {
      return EMPTY_ARRAY;
    }
    final StringJoiner stringJoiner = new StringJoiner(ARRAY_ELEMENT_SEPARATOR,
        ARRAY_START,
        ARRAY_END
    );
    for (final short s : array) {
      stringJoiner.add(String.valueOf(s));
    }
    return stringJoiner.toString();
  }
}

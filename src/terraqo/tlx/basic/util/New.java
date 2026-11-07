package pxf.toolkit.basic.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.reflect.ReflectHelper;

/**
 * 新建操作
 *
 * @author potatoxf
 * @date 2021/4/12
 */
public final class New {

  private static final Logger LOG = LoggerFactory.getLogger(New.class);
  private static final String TEN = "10";
  private static final String DATE_FORMATTER = "yyyy-MM-dd";
  private static final String DATE_TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";
  private static final BigDecimal[] CACHE_BIG_DECIMAL = {
    BigDecimal.ZERO,
    BigDecimal.ONE,
    new BigDecimal(2),
    new BigDecimal(3),
    new BigDecimal(4),
    new BigDecimal(5),
    new BigDecimal(6),
    new BigDecimal(7),
    new BigDecimal(8),
    new BigDecimal(9),
    BigDecimal.TEN
  };
  private static final BigInteger[] CACHE_BIG_INTEGER = {
    BigInteger.ZERO,
    BigInteger.ONE,
    BigInteger.valueOf(2),
    BigInteger.valueOf(3),
    BigInteger.valueOf(4),
    BigInteger.valueOf(5),
    BigInteger.valueOf(6),
    BigInteger.valueOf(7),
    BigInteger.valueOf(8),
    BigInteger.valueOf(9),
    BigInteger.TEN
  };

  private New() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 构建实例
   *
   * @param clz {@code Class}
   * @param <T> 对象类型
   * @return {@code <></>}
   * @throws NullPointerException 如果参数为 {@code null}
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  public static <T> T instance(Class<?> clz) {
    return (T) ReflectHelper.newInstance(clz);
  }

  /**
   * 构建实例
   *
   * @param clz {@code Class}
   * @param args 参数
   * @param <T> 对象类型
   * @return {@code <></>}
   * @throws NullPointerException 如果参数为 {@code null}
   * @throws NoSuchMethodException 如果构造器不存在
   * @throws IllegalAccessException 如果构造器不是 {@code public}
   * @throws InvocationTargetException 如果调用方法发送异常
   * @throws InstantiationException 如果实例化发送异常
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  public static <T> T instance(@Nonnull Class<?> clz, @Nonnull Object... args)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    if (args.length == 0) {
      return New.instance(clz);
    }
    Class[] argTypes = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
    return (T) clz.getConstructor(argTypes).newInstance(args);
  }

  /**
   * 安全创建一个对象
   *
   * @param clz 对象类型
   * @param args 参数
   * @param <T> 对象类型
   * @return 创建后的对象
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  public static <T> T instanceCompatibly(@Nonnull Class<T> clz, @Nonnull Object... args)
      throws IllegalAccessException, InstantiationException {
    int modifiers = clz.getModifiers();
    if (!Modifier.isPublic(modifiers)) {
      throw new IllegalArgumentException();
    }
    if (args.length == 0) {
      return New.instance(clz);
    }
    Constructor<?>[] constructors = clz.getConstructors();
    List<Constructor<?>> constructorList = new ArrayList<>(constructors.length);
    for (Constructor<?> constructor : constructors) {
      Class<?>[] parameterTypes = constructor.getParameterTypes();
      if (parameterTypes.length != args.length) {
        continue;
      }
      boolean b = true;
      for (int i = 0; i < args.length; i++) {
        if (args[i] == null) {
          if (parameterTypes[i].isPrimitive()) {
            b = false;
            break;
          }
          continue;
        }
        if (!Is.assignmentCompatible(args[i].getClass(), parameterTypes[i])) {
          b = false;
          break;
        }
      }
      if (b) {
        constructorList.add(constructor);
      }
    }
    for (Constructor<?> constructor : constructorList) {
      try {
        return (T) constructor.newInstance(args);
      } catch (InvocationTargetException | InstantiationException e) {
        e.printStackTrace();
      }
    }
    throw new InstantiationException(
        String.format(
            "Failed to create object in class [%s] constructor %s", clz, constructorList));
  }


  @SuppressWarnings("unchecked")
  public static <T> T[] array(@Nonnull T[] a, int newLength) {
    return (T[]) Array.newInstance(a.getClass().getComponentType(), newLength);
  }

  @SuppressWarnings("unchecked")
  public static <T> T[] array(@Nonnull Class<T> clz, int newLength) {
    Class<?> componentType = clz.getComponentType();
    if (componentType == null) {
      componentType = clz;
    }
    return (T[]) Array.newInstance(componentType, newLength);
  }

  @Nonnull
  public static DecimalFormat commonDecimalFormat() {
    DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
    return decimalFormat;
  }

  @Nonnull
  public static BigDecimal bigDecimal(int v) {
    if (Is.inside(v, 0, CACHE_BIG_DECIMAL.length)) {
      return CACHE_BIG_DECIMAL[v];
    }
    return new BigDecimal(v);
  }

  @Nonnull
  public static BigDecimal bigDecimal(long v) {
    int iv = (int) v;
    if ((long) iv == v) {
      return bigDecimal(iv);
    }
    return new BigDecimal(v);
  }

  @Nonnull
  public static BigDecimal bigDecimal(double v) {
    return BigDecimal.valueOf(v);
  }

  @Nullable
  public static BigDecimal bigDecimal(@Nullable Number v) {
    return v == null ? null : BigDecimal.valueOf(v.doubleValue());
  }

  @Nullable
  public static BigDecimal bigDecimal(@Nullable String v) {
    if (Is.empty(v)) {
      return null;
    }
    if (Is.number(v)) {
      return getCacheBigDecimalElseCreate(v);
    }
    if (LOG.isErrorEnabled()) {
      LOG.error(String.format("Number format error for [%s]", v));
    }
    return null;
  }

  @Nonnull
  public static BigDecimal bigDecimalWithThrow(@Nullable Number v) {
    if (Is.nvl(v)) {
      throw new IllegalArgumentException("The number must not be null");
    }
    return BigDecimal.valueOf(v.doubleValue());
  }

  @Nonnull
  public static BigDecimal bigDecimalWithThrow(@Nullable String v) {
    if (Is.empty(v)) {
      throw new IllegalArgumentException("The number must not be null");
    }
    if (Is.number(v)) {
      return getCacheBigDecimalElseCreate(v);
    }
    throw new NumberFormatException(String.format("Number format error for [%s]", v));
  }

  @Nonnull
  public static BigInteger bigInteger(int v) {
    if (Is.inside(v, 0, CACHE_BIG_INTEGER.length)) {
      return CACHE_BIG_INTEGER[v];
    }
    return BigInteger.valueOf(v);
  }

  @Nonnull
  public static BigInteger bigInteger(long v) {
    int iv = (int) v;
    if ((long) iv == v) {
      return bigInteger(iv);
    }
    return BigInteger.valueOf(v);
  }

  @Nullable
  public static BigInteger bigInteger(@Nullable Number v) {
    return v == null ? null : BigInteger.valueOf(v.longValue());
  }

  @Nullable
  public static BigInteger bigInteger(@Nullable String v) {
    if (Is.empty(v)) {
      return null;
    }
    if (Is.integerNumber(v)) {
      return getCacheBigIntegerElseCreate(v);
    }
    if (LOG.isErrorEnabled()) {
      LOG.error(String.format("Number format error for [%s]", v));
    }
    return null;
  }

  @Nonnull
  public static BigInteger bigIntegerWithThrow(@Nullable Number v) {
    if (Is.nvl(v)) {
      throw new IllegalArgumentException("The number must not be null");
    }
    return BigInteger.valueOf(v.longValue());
  }

  @Nonnull
  public static BigInteger bigIntegerWithThrow(@Nullable String v) {
    if (Is.empty(v)) {
      throw new IllegalArgumentException("The number must not be null");
    }
    if (Is.integerNumber(v)) {
      return getCacheBigIntegerElseCreate(v);
    }
    throw new NumberFormatException(String.format("Number format error for [%s]", v));
  }

  private static BigDecimal getCacheBigDecimalElseCreate(@Nonnull String v) {
    if (v.length() == 1) {
      int i = Integer.parseInt(v);
      return CACHE_BIG_DECIMAL[i];
    }
    if (TEN.equals(v)) {
      return BigDecimal.TEN;
    }
    return new BigDecimal(v);
  }

  private static BigInteger getCacheBigIntegerElseCreate(@Nonnull String v) {
    if (v.length() == 1) {
      int i = Integer.parseInt(v);
      return CACHE_BIG_INTEGER[i];
    }
    if (TEN.equals(v)) {
      return BigInteger.TEN;
    }
    return new BigInteger(v);
  }
}

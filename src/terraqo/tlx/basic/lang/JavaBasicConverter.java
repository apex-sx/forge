package pxf.toolkit.basic.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.Date;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.exception.UnsupportedException;
import pxf.toolkit.basic.util.Cast;
import pxf.toolkit.basic.util.New;

/**
 * java中的类型强制转换
 *
 * <p>符合java中类型转换规则
 *
 * @author potatoxf
 * @date 2020/11/29
 */
@SuppressWarnings("unchecked")
public final class JavaBasicConverter {

  private static final Logger LOG = LoggerFactory.getLogger(JavaBasicConverter.class);
  /** {@code true}的 {@code Character}的默认值 */
  private static final char C_DEFAULT_TRUE = 'T';
  /** {@code false}的 {@code Character}的默认值 */
  private static final char C_DEFAULT_FALSE = 'F';
  /** 空的{@code String}的 {@code Character}的默认值 */
  private static final char C_DEFAULT_EMPTY = ' ';
  /** {@code String}解析数字失败的默认值 */
  private static final String S_DEFAULT_NUMBER = "-1";
  /** {@code Boolean}的真值的值 */
  private static final String S_TRUE = "true";

  private JavaBasicConverter() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 转换至目标类型
   *
   * <p>注意： 1.该方法不抛出异常，只进行日志记录 2.返回结果可能为 {@code null}，注意拆包的NPE异常
   *
   * @param object 对象
   * @param targetClass 目标 {@code Class}
   * @param <T> 目标类型
   * @return 返回目标类型对象，如果失败则返回 {@code null}
   */
  @Nullable
  public static <T> T cast(Object object, Class<T> targetClass) {
    try {
      return cast(object, targetClass, false);
    } catch (UnsupportedException ignored) {
      return null;
    }
  }

  /**
   * 转换至目标类型
   *
   * <p>注意： 1.返回结果可能为 {@code null}，注意拆包的NPE异常
   *
   * @param object 对象
   * @param targetClass 目标 {@code Class}
   * @param <T> 目标类型
   * @return 返回目标类型对象，参数 {@code object}或 {@code targetClass}为 {@code null}时返回 {@code null}
   * @throws UnsupportedException 当不支持转换时抛出该异常
   */
  @Nullable
  public static <T> T cast(Object object, Class<T> targetClass, boolean isThrowException) {
    if (isReturnNull(object, targetClass)) {
      return null;
    }
    if (String.class.equals(targetClass)) {
      return (T) object.toString();
    }
    Class<?> src = wrapperClass(object.getClass());
    Class<?> dest = wrapperClass(targetClass);
    if (dest.equals(src)) {
      return (T) dest.cast(object);
    }
    return (T) cast(object, src, dest, isThrowException);
  }

  /**
   * 转换至目标类型
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object cast(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    if (isNumberClass(srcClass) && isNumberClass(destClass)) {
      return castNumberToNumber(object, srcClass, destClass, isThrowException);
    }
    if (isNumberClass(srcClass)) {
      return castNumberToObject(object, srcClass, destClass, isThrowException);
    }
    if (isNumberClass(destClass)) {
      return castObjectToNumber(object, srcClass, destClass, isThrowException);
    }
    return castObject(object, srcClass, destClass, isThrowException);
  }

  /**
   * 从数字转换到数字
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castNumberToNumber(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    Number number = (Number) object;
    if (destClass == Integer.class) {
      return number.intValue();
    }
    if (destClass == Double.class) {
      return number.doubleValue();
    }
    if (destClass == Long.class) {
      return number.longValue();
    }
    if (destClass == Float.class) {
      return number.longValue();
    }
    if (destClass == Short.class) {
      return number.shortValue();
    }
    if (destClass == Byte.class) {
      return number.byteValue();
    }
    if (destClass == BigDecimal.class) {
      return New.bigDecimalWithThrow(number);
    }
    if (destClass == BigInteger.class) {
      return New.bigIntegerWithThrow(number);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从对象转换到数字
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castObjectToNumber(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    if (Boolean.class == srcClass) {
      return castBooleanToNumber(object, srcClass, destClass, isThrowException);
    }
    if (Character.class == srcClass) {
      return castCharacterToNumber(object, srcClass, destClass, isThrowException);
    }
    if (String.class == srcClass) {
      return castStringToNumber(object, srcClass, destClass, isThrowException);
    }
    if (Date.class == srcClass) {
      return castDateToNumber(object, srcClass, destClass, isThrowException);
    }
    if (Instant.class == srcClass) {
      return casInstantToNumber(object, srcClass, destClass, isThrowException);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从 {@code Boolean}转换到数字
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castBooleanToNumber(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    boolean b = (boolean) object;
    Number number = b ? 1 : 0;
    if (destClass == Byte.class) {
      return number.byteValue();
    }
    if (destClass == Short.class) {
      return number.shortValue();
    }
    if (destClass == Integer.class) {
      return number.intValue();
    }
    if (destClass == Long.class) {
      return number.longValue();
    }
    if (destClass == Float.class) {
      return number.longValue();
    }
    if (destClass == Double.class) {
      return number.doubleValue();
    }
    if (destClass == BigDecimal.class) {
      return New.bigDecimalWithThrow(number);
    }
    if (destClass == BigInteger.class) {
      return New.bigIntegerWithThrow(number);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从 {@code Character}转换到数字
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castCharacterToNumber(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    char c = (char) object;
    Number number = (int) c;
    if (destClass == Byte.class) {
      return number.byteValue();
    }
    if (destClass == Short.class) {
      return number.shortValue();
    }
    if (destClass == Integer.class) {
      return number.intValue();
    }
    if (destClass == Long.class) {
      return number.longValue();
    }
    if (destClass == Float.class) {
      return number.longValue();
    }
    if (destClass == Double.class) {
      return number.doubleValue();
    }
    if (destClass == BigDecimal.class) {
      return New.bigDecimalWithThrow(number);
    }
    if (destClass == BigInteger.class) {
      return New.bigIntegerWithThrow(number);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从字符串转换到数字
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castStringToNumber(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    String string = (String) object;
    if (string.length() == 0) {
      string = S_DEFAULT_NUMBER;
    }
    try {
      if (destClass == Byte.class) {
        return Byte.parseByte(string);
      }
      if (destClass == Short.class) {
        return Short.parseShort(string);
      }
      if (destClass == Integer.class) {
        return Integer.parseInt(string);
      }
      if (destClass == Long.class) {
        return Long.parseLong(string);
      }
      if (destClass == Float.class) {
        return Float.parseFloat(string);
      }
      if (destClass == Double.class) {
        return Double.parseDouble(string);
      }
      if (destClass == BigDecimal.class) {
        return New.bigDecimalWithThrow(string);
      }
      if (destClass == BigInteger.class) {
        return New.bigIntegerWithThrow(string);
      }
    } catch (NumberFormatException e) {
      if (isThrowException) {
        throw new UnsupportedException(
            e,
            "The numeric string '%s' conversion to conversion to type %s parsing error",
            string,
            destClass);
      }
      if (LOG.isWarnEnabled()) {
        LOG.warn(
            String.format(
                "The numeric string '%s' conversion to conversion to type %s parsing error",
                string, destClass));
      }
      return castStringToNumber(S_DEFAULT_NUMBER, srcClass, destClass, false);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从 {@code Date}转换到数字
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castDateToNumber(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    Number number = ((Date) object).getTime();
    if (destClass == Byte.class) {
      return number.byteValue();
    }
    if (destClass == Short.class) {
      return number.shortValue();
    }
    if (destClass == Integer.class) {
      return number.intValue();
    }
    if (destClass == Long.class) {
      return number.longValue();
    }
    if (destClass == Float.class) {
      return number.longValue();
    }
    if (destClass == Double.class) {
      return number.doubleValue();
    }
    if (destClass == BigDecimal.class) {
      return New.bigDecimalWithThrow(number);
    }
    if (destClass == BigInteger.class) {
      return New.bigIntegerWithThrow(number);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从 {@code Instant}转换到数字
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object casInstantToNumber(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    return castDateToNumber(Date.from((Instant) object), srcClass, destClass, isThrowException);
  }

  /**
   * 从数字转换到对象
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castNumberToObject(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    if (Boolean.class == destClass) {
      return castNumberToBoolean(object, srcClass, destClass, isThrowException);
    }
    if (Character.class == destClass) {
      return castNumberToCharacter(object, srcClass, destClass, isThrowException);
    }
    if (Date.class == destClass) {
      return castNumberToDate(object, srcClass, destClass, isThrowException);
    }
    if (Instant.class == destClass) {
      return castNumberToInstant(object, srcClass, destClass, isThrowException);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从数字转换到 {@code Boolean}
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castNumberToBoolean(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    return ((Number) object).intValue() != 0;
  }

  /**
   * 从数字转换到 {@code Character}
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castNumberToCharacter(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    return (char) ((Number) object).intValue();
  }

  /**
   * 从数字转换到 {@code Date}
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castNumberToDate(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    if (srcClass == Long.class) {
      return new Date((Long) object);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从数字转换到 {@code Instant}
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castNumberToInstant(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    return ((Date) castNumberToDate(object, srcClass, destClass, isThrowException)).toInstant();
  }

  /**
   * 从对象之间转换
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castObject(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    if (String.class == srcClass) {
      return castStringToOther(object, srcClass, destClass, isThrowException);
    }
    if (Boolean.class == srcClass) {
      return castBooleanToOther(object, srcClass, destClass, isThrowException);
    }
    if (Character.class == srcClass) {
      return castCharacterToOther(object, srcClass, destClass, isThrowException);
    }
    return castObjectToCompatibleObject(object, srcClass, destClass, isThrowException);
  }

  /**
   * 从字符串转换其它类型
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castStringToOther(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    String string = (String) object;
    if (Boolean.class == destClass) {
      if (string.length() == 0) {
        return false;
      }
      return S_TRUE.equals(string);
    }
    if (Character.class == destClass) {
      if (string.length() == 0) {
        return C_DEFAULT_EMPTY;
      }
      return string.charAt(0);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从 {@code Boolean}转换其它类型
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castBooleanToOther(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    boolean b = (boolean) object;
    if (Character.class == destClass) {
      return b ? C_DEFAULT_TRUE : C_DEFAULT_FALSE;
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从 {@code Character}转换其它类型
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castCharacterToOther(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    char c = (char) object;
    if (Boolean.class == destClass) {
      return C_DEFAULT_TRUE == c;
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 从对象转换其它兼容类型
   *
   * @param object 对象
   * @param srcClass 原来类型
   * @param destClass 目标类型
   * @param isThrowException 是否抛出异常
   * @return 返回目标类型值
   * @throws UnsupportedException 当 {@code isThrowException}为 {@code true}时，
   *     并且转换失败时抛出该异常，否则只进行日志记录并返回 {@code null}
   */
  private static Object castObjectToCompatibleObject(
      Object object, Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    if (destClass.isAssignableFrom(srcClass)) {
      return destClass.cast(object);
    }
    return throwNoSupportRuntimeException(srcClass, destClass, isThrowException);
  }

  /**
   * 判断是否是数字类
   *
   * @param clazz {@code Class}
   * @return 如果是数字类返回 {@code true}，否则 {@code false}
   */
  private static boolean isNumberClass(Class<?> clazz) {
    return Number.class.isAssignableFrom(clazz);
  }

  /**
   * 返回包装类型
   *
   * @param clazz {@code Class}
   * @return 如果是原生类型，则返回包装类型，否则原样返回
   */
  private static Class<?> wrapperClass(Class<?> clazz) {
    return Cast.wrapperClass(clazz);
  }

  /**
   * 判断是否该返回 {@code null}
   *
   * @param object 对象
   * @param destClass 模板类型
   * @return 如果是需要返回 {@code null}则返回 {@code true}，否则 {@code false}
   */
  private static boolean isReturnNull(Object object, Class<?> destClass) {
    return object == null
        || destClass == null
        || void.class == destClass
        || Void.class == destClass;
  }

  private static Object throwNoSupportRuntimeException(
      Class<?> srcClass, Class<?> destClass, boolean isThrowException) {
    if (isThrowException) {
      throw new UnsupportedException(
          "From type '%s' to type '%s' is not supported", srcClass, destClass);
    }
    if (LOG.isWarnEnabled()) {
      LOG.warn(String.format("From type '%s' to type '%s' is not supported", srcClass, destClass));
    }
    return null;
  }
}

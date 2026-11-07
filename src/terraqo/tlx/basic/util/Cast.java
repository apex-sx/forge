package pxf.toolkit.basic.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import org.apache.commons.io.IOUtils;
import pxf.toolkit.basic.exception.IOFlowException;

/**
 * 转换操作
 *
 * @author potatoxf
 * @date 2021/3/12
 */
public final class Cast {

  private Cast() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  @Nonnull
  public static String hexCodePoint(int codepoint) {
    return Integer.toHexString(codepoint).toUpperCase();
  }

  /**
   * 转换成小写
   *
   * @param input 输入字符串数组
   * @return {@code String[]}
   */
  public static String[] lowercase(String[] input) {
    for (int i = 0; i < input.length; i++) {
      if (input[i] != null) {
        input[i] = input[i].toLowerCase();
      }
    }
    return input;
  }

  /**
   * 转换成小写
   *
   * @param input 输入字符串数组
   * @param locale 语言区域
   * @return {@code String[]}
   */
  public static String[] lowercase(String[] input, Locale locale) {
    for (int i = 0; i < input.length; i++) {
      if (input[i] != null) {
        input[i] = input[i].toLowerCase(locale);
      }
    }
    return input;
  }

  /**
   * 转换成大写
   *
   * @param input 输入字符串数组
   * @return {@code String[]}
   */
  public static String[] uppercase(String[] input) {
    for (int i = 0; i < input.length; i++) {
      if (input[i] != null) {
        input[i] = input[i].toUpperCase();
      }
    }
    return input;
  }

  /**
   * 转换成大写
   *
   * @param input 输入字符串数组
   * @param locale 语言区域
   * @return {@code String[]}
   */
  public static String[] uppercase(String[] input, Locale locale) {
    for (int i = 0; i < input.length; i++) {
      if (input[i] != null) {
        input[i] = input[i].toUpperCase(locale);
      }
    }
    return input;
  }

  /**
   * 返回包装类型
   *
   * @param clazz {@code Class}
   * @return 如果是原生类型，则返回包装类型，否则原样返回
   */
  public static Class<?> wrapperClass(Class<?> clazz) {
    if (clazz.isPrimitive()) {
      if (boolean.class.equals(clazz)) {
        return Boolean.class;
      }
      if (byte.class.equals(clazz)) {
        return Byte.class;
      }
      if (char.class.equals(clazz)) {
        return Character.class;
      }
      if (short.class.equals(clazz)) {
        return Short.class;
      }
      if (int.class.equals(clazz)) {
        return Integer.class;
      }
      if (long.class.equals(clazz)) {
        return Long.class;
      }
      if (float.class.equals(clazz)) {
        return Float.class;
      }
      if (double.class.equals(clazz)) {
        return Double.class;
      }
    }
    return clazz;
  }

  /**
   * 转成最近被整除的被除数
   *
   * @param dividend 被除数
   * @param divisor 除数
   * @return 返回可以被整除的被除数
   */
  public static int ceilingNum(int dividend, int divisor) {
    return (dividend / divisor) * divisor;
  }

  /**
   * 转成最近被整除的被除数
   *
   * @param dividend 被除数
   * @param divisor 除数
   * @return 返回可以被整除的被除数
   */
  public static long ceilingNum(long dividend, long divisor) {
    return (dividend / divisor) * divisor;
  }

  // --------------------------------------------------------------------------- START 日期直接互转

  public static Date date(long timestamp) {
    return new Date(timestamp);
  }

  public static Date date(Instant instant) {
    return Date.from(instant);
  }

  public static Date date(LocalDate localDate) {
    return Cast.date(localDate, ZoneId.systemDefault());
  }

  public static Date date(LocalDate localDate, ZoneId zoneId) {
    return Date.from(localDate.atStartOfDay().atZone(zoneId).toInstant());
  }

  public static Date date(LocalDateTime localDateTime) {
    return Cast.date(localDateTime, ZoneId.systemDefault());
  }

  public static Date date(LocalDateTime localDateTime, ZoneId zoneId) {
    return Date.from(localDateTime.atZone(zoneId).toInstant());
  }

  public static Instant instant(long timestamp) {
    return Instant.ofEpochMilli(timestamp);
  }

  public static Instant instant(Date date) {
    return date.toInstant();
  }

  public static LocalDate localDate(long timestamp) {
    return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static LocalDate localDate(long timestamp, ZoneId zoneId) {
    return Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDate();
  }

  public static LocalDate localDate(Instant instant) {
    return Cast.localDate(instant.toEpochMilli(), ZoneId.systemDefault());
  }

  public static LocalDate localDate(Instant instant, ZoneId zoneId) {
    return Cast.localDate(instant.toEpochMilli(), zoneId);
  }

  public static LocalDate localDate(Date date) {
    return Cast.localDate(date.getTime(), ZoneId.systemDefault());
  }

  public static LocalDate localDate(Date date, ZoneId zoneId) {
    return Cast.localDate(date.getTime(), zoneId);
  }

  public static LocalTime localTime(long timestamp) {
    return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalTime();
  }

  public static LocalTime localTime(long timestamp, ZoneId zoneId) {
    return Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalTime();
  }

  public static LocalTime localTime(Instant instant) {
    return Cast.localTime(instant.toEpochMilli(), ZoneId.systemDefault());
  }

  public static LocalTime localTime(Instant instant, ZoneId zoneId) {
    return Cast.localTime(instant.toEpochMilli(), zoneId);
  }

  public static LocalTime localTime(Date date) {
    return Cast.localTime(date.getTime(), ZoneId.systemDefault());
  }

  public static LocalTime localTime(Date date, ZoneId zoneId) {
    return Cast.localTime(date.getTime(), zoneId);
  }

  public static LocalDateTime localDateTime(long timestamp) {
    return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  public static LocalDateTime localDateTime(long timestamp, ZoneId zoneId) {
    return Instant.ofEpochMilli(timestamp).atZone(zoneId).toLocalDateTime();
  }

  public static LocalDateTime localDateTime(Instant instant) {
    return Cast.localDateTime(instant.toEpochMilli(), ZoneId.systemDefault());
  }

  public static LocalDateTime localDateTime(Instant instant, ZoneId zoneId) {
    return Cast.localDateTime(instant.toEpochMilli(), zoneId);
  }

  public static LocalDateTime localDateTime(Date date) {
    return Cast.localDateTime(date.getTime(), ZoneId.systemDefault());
  }

  public static LocalDateTime localDateTime(Date date, ZoneId zoneId) {
    return Cast.localDateTime(date.getTime(), zoneId);
  }

  // --------------------------------------------------------------------------- END 日期直接互转

  /**
   * 解析 {@code Date}值
   *
   * @param value 值
   * @return 返回值
   */
  public static Date dateValue(Object value) {
    return dateValue(value, TimeHelper.nowDate());
  }

  /**
   * 解析 {@code Date}值
   *
   * @param value 值
   * @return 返回值
   */
  public static Date dateValue(Object value, Date defaultValue) {
    if (value instanceof String) {
      return dateValue((String) value, defaultValue);
    }
    if (value instanceof CharSequence) {
      return dateValue(value.toString(), defaultValue);
    }
    if (value instanceof Date) {
      return new Date(TimeHelper.getDayMillis(((Date) value).getTime()));
    }
    if (value instanceof TemporalAccessor) {
      return new Date(
          TimeHelper.getDayMillis(Instant.from((TemporalAccessor) value).getEpochSecond()));
    }
    return defaultValue;
  }

  /**
   * 解析 {@code Date}值
   *
   * @param value 值
   * @return 返回值
   */
  public static Date dateTimeValue(Object value) {
    return Cast.dateTimeValue(value, new Date());
  }

  /**
   * 解析 {@code Date}值
   *
   * @param value 值
   * @return 返回值
   */
  public static Date dateTimeValue(Object value, Date defaultValue) {
    if (value instanceof String) {
      return dateTimeValue((String) value, defaultValue);
    }
    if (value instanceof CharSequence) {
      return dateTimeValue(value.toString(), defaultValue);
    }
    if (value instanceof Date) {
      return (Date) value;
    }
    if (value instanceof TemporalAccessor) {
      return new Date(Instant.from((TemporalAccessor) value).getEpochSecond());
    }
    return defaultValue;
  }

  /**
   * 解析 {@code Date}值
   *
   * @param value 值
   * @return 返回值
   */
  public static Date dateValue(String value, Date defaultValue) {
    if (value != null) {
      Date result = TimeHelper.parseDefaultDate(value);
      if (result != null) {
        return result;
      }
    }
    return defaultValue;
  }

  /**
   * 解析 {@code Date}值
   *
   * @param value 值
   * @return 返回值
   */
  public static Date dateTimeValue(String value, Date defaultValue) {
    if (value != null) {
      Date result = TimeHelper.parseDefaultDate(value);
      if (result != null) {
        return result;
      }
    }
    return defaultValue;
  }

  /**
   * 二进制转int
   *
   * @param binaryString 二进制字符串
   * @return {@code int}
   */
  public static int binaryToInt(String binaryString) {
    return Integer.parseInt(binaryString, 2);
  }

  /**
   * 二进制转long
   *
   * @param binaryString 二进制字符串
   * @return {@code long}
   */
  public static long binaryToLong(String binaryString) {
    return Long.parseLong(binaryString, 2);
  }

  /**
   * 解析 {@code String}值
   *
   * @param value 值
   * @return 返回值 {@code String}
   */
  public static String stringValue(Object value, String defaultValue) {
    if (value != null) {
      return value.toString();
    }
    return defaultValue;
  }

  /**
   * 解析 {@code Number}值
   *
   * @param value 值
   * @return 返回值 {@code long}或 {@code double}
   */
  public static Number numberValue(String value) {
    return numberValue(value, -1L);
  }

  /**
   * 解析 {@code Number}值
   *
   * @param value 值
   * @return 返回值 {@code long}或 {@code double}
   */
  public static Number numberValue(String value, long defaultValue) {
    if (value == null || value.length() == 0) {
      return defaultValue;
    }
    if (value.contains(".")) {
      return Cast.doubleValue(value, defaultValue);
    }
    return Cast.longValue(value, defaultValue);
  }

  /**
   * 解析 {@code boolean}值
   *
   * @param value 值
   * @return 返回值
   */
  public static boolean booleanValue(Object value) {
    return value(value, Boolean.FALSE);
  }

  /**
   * 解析 {@code boolean}值
   *
   * @param value 值
   * @return 返回值
   */
  public static boolean booleanValue(Number value) {
    return value(value, Boolean.FALSE);
  }

  /**
   * 解析 {@code boolean}值
   *
   * @param value 值
   * @return 返回值
   */
  public static boolean booleanValue(String value) {
    return value(value, Boolean.FALSE);
  }

  /**
   * 解析 {@code boolean}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static boolean booleanValue(Object value, boolean defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code boolean}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static boolean booleanValue(Number value, boolean defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code boolean}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static boolean booleanValue(String value, boolean defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code char}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static char charValue(Object value, char defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code char}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static char charValue(Number value, char defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code char}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static char charValue(String value, char defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code byte}值
   *
   * @param value 值
   * @return 返回值
   */
  public static byte byteValue(Object value) {
    return value(value, Const.ZERO_BYTE);
  }

  /**
   * 解析 {@code byte}值
   *
   * @param value 值
   * @return 返回值
   */
  public static byte byteValue(Number value) {
    return value(value, Const.ZERO_BYTE);
  }

  /**
   * 解析 {@code byte}值
   *
   * @param value 值
   * @return 返回值
   */
  public static byte byteValue(String value) {
    return value(value, Const.ZERO_BYTE);
  }

  /**
   * 解析 {@code byte}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static byte byteValue(Object value, byte defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code byte}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static byte byteValue(Number value, byte defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code byte}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static byte byteValue(String value, byte defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code short}值
   *
   * @param value 值
   * @return 返回值
   */
  public static short shortValue(Object value) {
    return value(value, Const.ZERO_SHORT);
  }

  /**
   * 解析 {@code short}值
   *
   * @param value 值
   * @return 返回值
   */
  public static short shortValue(Number value) {
    return value(value, Const.ZERO_SHORT);
  }

  /**
   * 解析 {@code short}值
   *
   * @param value 值
   * @return 返回值
   */
  public static short shortValue(String value) {
    return value(value, Const.ZERO_SHORT);
  }

  /**
   * 解析 {@code short}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static short shortValue(Object value, short defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code short}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static short shortValue(Number value, short defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code short}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static short shortValue(String value, short defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code int}值
   *
   * @param value 值
   * @return 返回值
   */
  public static int intValue(Object value) {
    return value(value, Const.ZERO_INT);
  }

  /**
   * 解析 {@code int}值
   *
   * @param value 值
   * @return 返回值
   */
  public static int intValue(Number value) {
    return value(value, Const.ZERO_INT);
  }

  /**
   * 解析 {@code int}值
   *
   * @param value 值
   * @return 返回值
   */
  public static int intValue(String value) {
    return value(value, Const.ZERO_INT);
  }

  /**
   * 解析 {@code int}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static int intValue(Object value, int defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code int}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static int intValue(Number value, int defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code int}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static int intValue(String value, int defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code long}值
   *
   * @param value 值
   * @return 返回值
   */
  public static long longValue(Object value) {
    return value(value, Const.ZERO_LONG);
  }

  /**
   * 解析 {@code long}值
   *
   * @param value 值
   * @return 返回值
   */
  public static long longValue(Number value) {
    return value(value, Const.ZERO_LONG);
  }

  /**
   * 解析 {@code long}值
   *
   * @param value 值
   * @return 返回值
   */
  public static long longValue(String value) {
    return value(value, Const.ZERO_LONG);
  }

  /**
   * 解析 {@code long}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static long longValue(Object value, long defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code long}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static long longValue(Number value, long defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code long}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static long longValue(String value, long defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code float}值
   *
   * @param value 值
   * @return 返回值
   */
  public static float floatValue(Object value) {
    return value(value, Const.ZERO_FLOAT);
  }

  /**
   * 解析 {@code float}值
   *
   * @param value 值
   * @return 返回值
   */
  public static float floatValue(Number value) {
    return value(value, Const.ZERO_FLOAT);
  }

  /**
   * 解析 {@code float}值
   *
   * @param value 值
   * @return 返回值
   */
  public static float floatValue(String value) {
    return value(value, Const.ZERO_FLOAT);
  }

  /**
   * 解析 {@code float}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static float floatValue(Object value, float defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code float}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static float floatValue(Number value, float defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code float}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static float floatValue(String value, float defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code double}值
   *
   * @param value 值
   * @return 返回值
   */
  public static double doubleValue(Object value) {
    return value(value, Const.ZERO_DOUBLE);
  }

  /**
   * 解析 {@code double}值
   *
   * @param value 值
   * @return 返回值
   */
  public static double doubleValue(Number value) {
    return value(value, Const.ZERO_DOUBLE);
  }

  /**
   * 解析 {@code double}值
   *
   * @param value 值
   * @return 返回值
   */
  public static double doubleValue(String value) {
    return value(value, Const.ZERO_DOUBLE);
  }

  /**
   * 解析 {@code double}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static double doubleValue(Object value, double defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code double}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static double doubleValue(Number value, double defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code double}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static double doubleValue(String value, double defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code BigInteger}值
   *
   * @param value 值
   * @return 返回值
   */
  public static BigInteger bigIntegerValue(Object value) {
    return value(value, BigInteger.ZERO);
  }

  /**
   * 解析 {@code BigInteger}值
   *
   * @param value 值
   * @return 返回值
   */
  public static BigInteger bigIntegerValue(Number value) {
    return value(value, BigInteger.ZERO);
  }

  /**
   * 解析 {@code BigInteger}值
   *
   * @param value 值
   * @return 返回值
   */
  public static BigInteger bigIntegerValue(String value) {
    return value(value, BigInteger.ZERO);
  }

  /**
   * 解析 {@code BigInteger}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static BigInteger bigIntegerValue(Object value, BigInteger defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code BigInteger}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static BigInteger bigIntegerValue(Number value, BigInteger defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code BigInteger}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static BigInteger bigIntegerValue(String value, BigInteger defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code BigDecimal}值
   *
   * @param value 值
   * @return 返回值
   */
  public static BigDecimal bigDecimalValue(Object value) {
    return value(value, BigDecimal.ZERO);
  }

  /**
   * 解析 {@code BigDecimal}值
   *
   * @param value 值
   * @return 返回值
   */
  public static BigDecimal bigDecimalValue(Number value) {
    return value(value, BigDecimal.ZERO);
  }

  /**
   * 解析 {@code BigDecimal}值
   *
   * @param value 值
   * @return 返回值
   */
  public static BigDecimal bigDecimalValue(String value) {
    return value(value, BigDecimal.ZERO);
  }

  /**
   * 解析 {@code BigDecimal}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static BigDecimal bigDecimalValue(Object value, BigDecimal defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code BigDecimal}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static BigDecimal bigDecimalValue(Number value, BigDecimal defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * 解析 {@code BigDecimal}值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  public static BigDecimal bigDecimalValue(String value, BigDecimal defaultValue) {
    return value(value, defaultValue);
  }

  /**
   * int值转byte数组，使用大端字节序（高位字节在前，低位字节在后）
   *
   * @param value 值
   * @return byte数组
   */
  public static byte[] bytesValue(int value) {
    byte[] result = new byte[4];

    result[0] = (byte) (value >> 24);
    result[1] = (byte) (value >> 16);
    result[2] = (byte) (value >> 8);
    result[3] = (byte) (value /* >> 0 */);

    return result;
  }

  /**
   * byte数组转int，使用大端字节序（高位字节在前，低位字节在后）
   *
   * @param value byte数组
   * @return int
   */
  public static int intValue(byte[] value) {
    return (value[0] & 0xff) << 24 //
        | (value[1] & 0xff) << 16 //
        | (value[2] & 0xff) << 8 //
        | (value[3] & 0xff);
  }

  /**
   * 以无符号字节数组的形式返回传入值。
   *
   * @param value 需要转换的值
   * @return 无符号bytes
   */
  public static byte[] unsignedByteArray(BigInteger value) {
    byte[] bytes = value.toByteArray();

    if (bytes[0] == 0) {
      byte[] tmp = new byte[bytes.length - 1];
      System.arraycopy(bytes, 1, tmp, 0, tmp.length);

      return tmp;
    }

    return bytes;
  }

  /**
   * 以无符号字节数组的形式返回传入值。
   *
   * @param length bytes长度
   * @param value 需要转换的值
   * @return 无符号bytes
   */
  public static byte[] unsignedByteArray(int length, BigInteger value) {
    byte[] bytes = value.toByteArray();
    if (bytes.length == length) {
      return bytes;
    }

    int start = bytes[0] == 0 ? 1 : 0;
    int count = bytes.length - start;

    if (count > length) {
      throw new IllegalArgumentException("standard length exceeded for value");
    }

    byte[] tmp = new byte[length];
    System.arraycopy(bytes, start, tmp, tmp.length - count, count);
    return tmp;
  }

  // ---------------------------------------------------------------------------

  /**
   * 将图像转为字节数组
   *
   * @param bufferedImage 图像
   * @param format 格式
   * @return {@code byte[]}
   */
  public static byte[] bytesFromImage(BufferedImage bufferedImage, String format) {
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ImageIO.write(bufferedImage, format, byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  /**
   * 将文件转为字节数组
   *
   * @param file 文件
   * @return {@code byte[]}
   */
  public static byte[] bytesFromFile(File file) {
    try {
      return IOUtils.toByteArray(new FileInputStream(file));
    } catch (IOException e) {
      throw new IOFlowException(e);
    }
  }

  // ---------------------------------------------------------------------------

  public static String urlFromFile(File file) {
    file = file.getAbsoluteFile();
    return "file://" + Legalize.path(file.getAbsolutePath(), file.isDirectory());
  }

  // ---------------------------------------------------------------------------

  /**
   * 返回集合的索引映射Map
   *
   * @param collection 集合
   * @param <E> 元素类型
   * @return {@code <E> Prop<Integer, E>}
   */
  @Nonnull
  public static <E> Map<Integer, E> indexMap(Collection<E> collection) {
    Map<Integer, E> result = new HashMap<>(collection.size());
    Iterator<E> iterator = collection.iterator();
    for (int i = 0; iterator.hasNext(); i++) {
      result.put(i, iterator.next());
    }
    return result;
  }

  // ---------------------------------------------------------------------------

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static boolean value(Object value, boolean defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return ((Number) value).intValue() != 0;
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static boolean value(Number value, boolean defaultValue) {
    if (value != null) {
      return value.intValue() != 0;
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static boolean value(String value, boolean defaultValue) {
    if (value != null) {
      try {
        return Boolean.parseBoolean(value);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static char value(Object value, char defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return Character.toChars(((Number) value).intValue())[0];
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static char value(Number value, char defaultValue) {
    if (value != null) {
      return Character.toChars(value.intValue())[0];
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static char value(String value, char defaultValue) {
    if (value != null && value.length() != 0) {
      try {
        return value.charAt(0);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static byte value(Object value, byte defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return ((Number) value).byteValue();
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static byte value(Number value, byte defaultValue) {
    if (value != null) {
      return value.byteValue();
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static byte value(String value, byte defaultValue) {
    if (value != null) {
      try {
        return Byte.parseByte(value);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static short value(Object value, short defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return ((Number) value).shortValue();
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static short value(Number value, short defaultValue) {
    if (value != null) {
      return value.shortValue();
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static short value(String value, short defaultValue) {
    if (value != null) {
      try {
        return Short.parseShort(value);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static int value(Object value, int defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return ((Number) value).intValue();
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static int value(Number value, int defaultValue) {
    if (value != null) {
      return value.intValue();
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static int value(String value, int defaultValue) {
    if (value != null) {
      try {
        return Integer.parseInt(value);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static long value(Object value, long defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return ((Number) value).longValue();
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static long value(Number value, long defaultValue) {
    if (value != null) {
      return value.longValue();
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static long value(String value, long defaultValue) {
    if (value != null) {
      try {
        return Long.parseLong(value);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static float value(Object value, float defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return ((Number) value).floatValue();
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static float value(Number value, float defaultValue) {
    if (value != null) {
      return value.floatValue();
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static float value(String value, float defaultValue) {
    if (value != null) {
      try {
        return Float.parseFloat(value);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static double value(Object value, double defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return ((Number) value).doubleValue();
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static double value(Number value, double defaultValue) {
    if (value != null) {
      return value.doubleValue();
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static double value(String value, double defaultValue) {
    if (value != null) {
      try {
        return Double.parseDouble(value);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static BigInteger value(Object value, BigInteger defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return BigInteger.valueOf(((Number) value).longValue());
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static BigInteger value(Number value, BigInteger defaultValue) {
    if (value != null) {
      return BigInteger.valueOf(value.longValue());
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static BigInteger value(String value, BigInteger defaultValue) {
    if (value != null) {
      try {
        return new BigInteger(value);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static BigDecimal value(Object value, BigDecimal defaultValue) {
    if (value != null) {
      if (value instanceof Number) {
        return BigDecimal.valueOf(((Number) value).doubleValue());
      } else {
        return value(value.toString(), defaultValue);
      }
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static BigDecimal value(Number value, BigDecimal defaultValue) {
    if (value != null) {
      return BigDecimal.valueOf(value.doubleValue());
    }
    return defaultValue;
  }

  /**
   * 解析值
   *
   * @param value 值
   * @param defaultValue 默认值
   * @return 返回值
   */
  static BigDecimal value(String value, BigDecimal defaultValue) {
    if (value != null) {
      try {
        return new BigDecimal(value);
      } catch (NumberFormatException ignored) {
      }
    }
    return defaultValue;
  }
}

package pxf.toolkit.basic.util;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 有效类
 *
 * @author potatoxf
 * @date 2021/4/21
 */
public final class Valid {

  private Valid() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  public static <T> T object(T value, T defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return value;
  }

  public static String string(Object value) {
    return string(value, Empty.STRING);
  }

  public static String string(Object value, String defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    return value.toString();
  }

  /**
   * 处理一个有效 {@code BigDecimal}
   *
   * @param value 判断的值
   * @return 返回一个有效 {@code BigDecimal}，如果 @{@code value}为 {@code null}或者不是数字，则返回该值
   */
  public static DecimalFormat decimalFormat(DecimalFormat value) {
    if (Is.nvl(value)) {
      return New.commonDecimalFormat();
    }
    return value;
  }

  /**
   * 处理一个有效 {@code BigDecimal}
   *
   * @param value 判断的值
   * @return 返回一个有效 {@code BigDecimal}，如果 @{@code value}为 {@code null}或者不是数字，则返回该值
   */
  public static BigDecimal bigDecimal(Object value) {
    return bigDecimal(value, BigDecimal.ZERO);
  }

  /**
   * 处理一个有效 {@code BigDecimal}
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效 {@code BigDecimal}，如果 @{@code value}为 {@code null}或者不是数字，则返回该值
   */
  public static BigDecimal bigDecimal(Object value, Number defaultValue) {
    return bigDecimal(value, defaultValue == null ? null : New.bigDecimal(defaultValue));
  }

  /**
   * 处理一个有效 {@code BigDecimal}
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效 {@code BigDecimal}，如果 @{@code value}为 {@code null}或者不是数字，则返回该值
   */
  public static BigDecimal bigDecimal(Object value, BigDecimal defaultValue) {
    if (value == null) {
      return defaultValue;
    }
    if (value instanceof BigDecimal) {
      return (BigDecimal) value;
    }
    if (value instanceof Number) {
      return New.bigDecimal((Number) value);
    }
    String num = value.toString();
    if (Is.number(num)) {
      return New.bigDecimal(num);
    }
    return defaultValue;
  }

  /**
   * 处理一个有效 {@code String}值
   *
   * @param value 判断的值
   * @return 返回一个有效 {@code BigDecimal}，如果 @{@code value}为 {@code null}，则返回该空字符串
   */
  @Nonnull
  public static String val(@Nullable String value) {
    if (Is.nvl(value)) {
      return Empty.STRING;
    }
    return value;
  }

  /**
   * 处理一个有效字符串值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @param <T> 参数类型
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  @Nonnull
  public static <T> String val(@Nullable T value, @Nonnull String defaultValue) {
    return value == null ? defaultValue : value.toString();
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @param <T> 参数类型
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  @Nonnull
  public static <T> T val(@Nullable T value, @Nonnull T defaultValue) {
    return value == null ? defaultValue : value;
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该 {@code false}
   */
  public static boolean val(@Nullable Boolean value) {
    return val(value, false);
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  public static boolean val(@Nullable Boolean value, boolean defaultValue) {
    if (Is.nvl(value)) {
      return defaultValue;
    }
    return value;
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该 {@code ' '}
   */
  public static char val(@Nullable Character value) {
    return val(value, ' ');
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  public static char val(@Nullable Character value, char defaultValue) {
    if (Is.nvl(value)) {
      return defaultValue;
    }
    return value;
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该 {@code -1}
   */
  public static byte val(@Nullable Byte value) {
    return val(value, (byte) Const.EOF);
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  public static byte val(@Nullable Byte value, byte defaultValue) {
    if (Is.nvl(value)) {
      return defaultValue;
    }
    return value;
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该 {@code -1}
   */
  public static short val(@Nullable Short value) {
    return val(value, (short) Const.EOF);
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  public static short val(@Nullable Short value, short defaultValue) {
    if (Is.nvl(value)) {
      return defaultValue;
    }
    return value;
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该 {@code -1}
   */
  public static int val(@Nullable Integer value) {
    return val(value, Const.EOF);
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  public static int val(@Nullable Integer value, int defaultValue) {
    if (Is.nvl(value)) {
      return defaultValue;
    }
    return value;
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该 {@code -1}
   */
  public static long val(@Nullable Long value) {
    return val(value, Const.EOF);
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  public static long val(@Nullable Long value, long defaultValue) {
    if (Is.nvl(value)) {
      return defaultValue;
    }
    return value;
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该 {@code -1}
   */
  public static float val(@Nullable Float value) {
    return val(value, Const.EOF);
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  public static float val(@Nullable Float value, float defaultValue) {
    if (Is.nvl(value)) {
      return defaultValue;
    }
    return value;
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该 {@code -1}
   */
  public static double val(@Nullable Double value) {
    return val(value, Const.EOF);
  }

  /**
   * 处理一个有效值
   *
   * @param value 判断的值
   * @param defaultValue 默认值
   * @return 返回一个有效值，如果 @{@code value}为 {@code null}，则返回该值
   */
  public static double val(@Nullable Double value, double defaultValue) {
    if (Is.nvl(value)) {
      return defaultValue;
    }
    return value;
  }

  @Nonnull
  public static boolean[] empty(@Nullable boolean[] val) {
    if (val != null) {
      return val;
    }
    return Empty.BOOLEAN_ARRAY;
  }

  @Nonnull
  public static byte[] empty(@Nullable byte[] val) {
    if (val != null) {
      return val;
    }
    return Empty.BYTE_ARRAY;
  }

  @Nonnull
  public static char[] empty(@Nullable char[] val) {
    if (val != null) {
      return val;
    }
    return Empty.CHAR_ARRAY;
  }

  @Nonnull
  public static short[] empty(@Nullable short[] val) {
    if (val != null) {
      return val;
    }
    return Empty.SHORT_ARRAY;
  }

  @Nonnull
  public static int[] empty(@Nullable int[] val) {
    if (val != null) {
      return val;
    }
    return Empty.INT_ARRAY;
  }

  @Nonnull
  public static long[] empty(@Nullable long[] val) {
    if (val != null) {
      return val;
    }
    return Empty.LONG_ARRAY;
  }

  @Nonnull
  public static float[] empty(@Nullable float[] val) {
    if (val != null) {
      return val;
    }
    return Empty.FLOAT_ARRAY;
  }

  @Nonnull
  public static double[] empty(@Nullable double[] val) {
    if (val != null) {
      return val;
    }
    return Empty.DOUBLE_ARRAY;
  }

  @Nonnull
  public static String[] empty(@Nullable String[] val) {
    if (val != null) {
      return val;
    }
    return Empty.STRING_ARRAY;
  }

  @Nonnull
  public static Object[] empty(@Nullable Object[] val) {
    if (val != null) {
      return val;
    }
    return Empty.OBJECT_ARRAY;
  }

  @Nonnull
  public static Class<?>[] empty(@Nullable Class<?>[] val) {
    if (val != null) {
      return val;
    }
    return Empty.CLASS_ARRAY;
  }

  public static int gt(int value, int targetValue, int defaultValue) {
    if (value > targetValue) {
      return value;
    }
    return defaultValue;
  }

  public static int gtEq(int value, int targetValue, int defaultValue) {
    if (value >= targetValue) {
      return value;
    }
    return defaultValue;
  }

  public static int lt(int value, int targetValue, int defaultValue) {
    if (value < targetValue) {
      return value;
    }
    return defaultValue;
  }

  public static int ltEq(int value, int targetValue, int defaultValue) {
    if (value <= targetValue) {
      return value;
    }
    return defaultValue;
  }

  public static File file(String path) {
    return path == null ? null : new File(path);
  }
}

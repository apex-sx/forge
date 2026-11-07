package pxf.toolkit.basic.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 计算操作
 *
 * @author potatoxf
 * @date 2021/4/3
 */
public final class Calculate {

  /** 默认除法运算精度 */
  private static final int DEFAULT_DIV_SCALE = 10;

  private Calculate() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 计算文本显示长度，默认半角为 {@code 1}，全角为 {@code 2}
   *
   * @param str 文本
   * @return 返回计算后显示长度
   */
  public static int charLength(String str) {
    return (int) charLength(str, 1, 2, 0);
  }

  /**
   * 计算文本显示长度
   *
   * @param text 文本
   * @param half 半角长度
   * @param full 全角长度
   * @param defaultLength 默认长度，当没有字符时
   * @return 返回计算后显示长度
   */
  public static double charLength(String text, double half, double full, double defaultLength) {
    if (text == null || text.length() == 0) {
      return defaultLength;
    }
    double len = 0;
    for (int i = 0; i < text.length(); ++i) {
      if (Is.quadEm(text.codePointAt(i))) {
        len = len + full;
      } else {
        len = len + half;
      }
    }
    return len;
  }

  /**
   * 提供精确的加法运算
   *
   * @param v1 被加数
   * @param v2 加数
   * @return 和
   */
  public static double add(float v1, float v2) {
    return add(Float.toString(v1), Float.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的加法运算
   *
   * @param v1 被加数
   * @param v2 加数
   * @return 和
   */
  public static double add(float v1, double v2) {
    return add(Float.toString(v1), Double.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的加法运算
   *
   * @param v1 被加数
   * @param v2 加数
   * @return 和
   */
  public static double add(double v1, float v2) {
    return add(Double.toString(v1), Float.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的加法运算
   *
   * @param v1 被加数
   * @param v2 加数
   * @return 和
   */
  public static double add(double v1, double v2) {
    return add(Double.toString(v1), Double.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的加法运算
   *
   * @param v1 被加数
   * @param v2 加数
   * @return 和
   */
  public static double add(Double v1, Double v2) {
    return add(v1, (Number) v2).doubleValue();
  }

  /**
   * 提供精确的加法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param v1 被加数
   * @param v2 加数
   * @return 和
   */
  public static BigDecimal add(Number v1, Number v2) {
    return add(new Number[] {v1, v2});
  }

  /**
   * 提供精确的加法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param values 多个被加值
   * @return 和
   */
  public static BigDecimal add(Number... values) {
    if (Is.empty(values)) {
      return BigDecimal.ZERO;
    }

    Number value = values[0];
    BigDecimal result = new BigDecimal(null == value ? "0" : value.toString());
    for (int i = 1; i < values.length; i++) {
      value = values[i];
      if (null != value) {
        result = result.add(new BigDecimal(value.toString()));
      }
    }
    return result;
  }

  /**
   * 提供精确的加法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param values 多个被加值
   * @return 和
   */
  public static BigDecimal add(String... values) {
    if (Is.empty(values)) {
      return BigDecimal.ZERO;
    }

    String value = values[0];
    BigDecimal result = new BigDecimal(null == value ? "0" : value);
    for (int i = 1; i < values.length; i++) {
      value = values[i];
      if (null != value) {
        result = result.add(new BigDecimal(value));
      }
    }
    return result;
  }

  /**
   * 提供精确的加法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param values 多个被加值
   * @return 和
   */
  public static BigDecimal add(BigDecimal... values) {
    if (Is.empty(values)) {
      return BigDecimal.ZERO;
    }

    BigDecimal value = values[0];
    BigDecimal result = null == value ? BigDecimal.ZERO : value;
    for (int i = 1; i < values.length; i++) {
      value = values[i];
      if (null != value) {
        result = result.add(value);
      }
    }
    return result;
  }

  /**
   * 提供精确的减法运算
   *
   * @param v1 被减数
   * @param v2 减数
   * @return 差
   */
  public static double sub(float v1, float v2) {
    return sub(Float.toString(v1), Float.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的减法运算
   *
   * @param v1 被减数
   * @param v2 减数
   * @return 差
   */
  public static double sub(float v1, double v2) {
    return sub(Float.toString(v1), Double.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的减法运算
   *
   * @param v1 被减数
   * @param v2 减数
   * @return 差
   */
  public static double sub(double v1, float v2) {
    return sub(Double.toString(v1), Float.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的减法运算
   *
   * @param v1 被减数
   * @param v2 减数
   * @return 差
   */
  public static double sub(double v1, double v2) {
    return sub(Double.toString(v1), Double.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的减法运算
   *
   * @param v1 被减数
   * @param v2 减数
   * @return 差
   */
  public static double sub(Double v1, Double v2) {
    return sub(v1, (Number) v2).doubleValue();
  }

  /**
   * 提供精确的减法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param v1 被减数
   * @param v2 减数
   * @return 差
   */
  public static BigDecimal sub(Number v1, Number v2) {
    return sub(new Number[] {v1, v2});
  }

  /**
   * 提供精确的减法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param values 多个被减值
   * @return 差
   */
  public static BigDecimal sub(Number... values) {
    if (Is.empty(values)) {
      return BigDecimal.ZERO;
    }

    Number value = values[0];
    BigDecimal result = new BigDecimal(null == value ? "0" : value.toString());
    for (int i = 1; i < values.length; i++) {
      value = values[i];
      if (null != value) {
        result = result.subtract(new BigDecimal(value.toString()));
      }
    }
    return result;
  }

  /**
   * 提供精确的减法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param values 多个被减值
   * @return 差
   */
  public static BigDecimal sub(String... values) {
    if (Is.empty(values)) {
      return BigDecimal.ZERO;
    }

    String value = values[0];
    BigDecimal result = new BigDecimal(null == value ? "0" : value);
    for (int i = 1; i < values.length; i++) {
      value = values[i];
      if (null != value) {
        result = result.subtract(new BigDecimal(value));
      }
    }
    return result;
  }

  /**
   * 提供精确的减法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param values 多个被减值
   * @return 差
   */
  public static BigDecimal sub(BigDecimal... values) {
    if (Is.empty(values)) {
      return BigDecimal.ZERO;
    }

    BigDecimal value = values[0];
    BigDecimal result = null == value ? BigDecimal.ZERO : value;
    for (int i = 1; i < values.length; i++) {
      value = values[i];
      if (null != value) {
        result = result.subtract(value);
      }
    }
    return result;
  }

  /**
   * 提供精确的乘法运算
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @return 积
   */
  public static double mul(float v1, float v2) {
    return mul(Float.toString(v1), Float.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的乘法运算
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @return 积
   */
  public static double mul(float v1, double v2) {
    return mul(Float.toString(v1), Double.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的乘法运算
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @return 积
   */
  public static double mul(double v1, float v2) {
    return mul(Double.toString(v1), Float.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的乘法运算
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @return 积
   */
  public static double mul(double v1, double v2) {
    return mul(Double.toString(v1), Double.toString(v2)).doubleValue();
  }

  /**
   * 提供精确的乘法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @return 积
   */
  public static double mul(Double v1, Double v2) {
    return mul(v1, (Number) v2).doubleValue();
  }

  /**
   * 提供精确的乘法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @return 积
   */
  public static BigDecimal mul(Number v1, Number v2) {
    return mul(new Number[] {v1, v2});
  }

  /**
   * 提供精确的乘法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param values 多个被乘值
   * @return 积
   */
  public static BigDecimal mul(Number... values) {
    if (Is.empty(values)) {
      return BigDecimal.ZERO;
    }

    Number value = values[0];
    BigDecimal result = new BigDecimal(null == value ? "0" : value.toString());
    for (int i = 1; i < values.length; i++) {
      value = values[i];
      if (null != value) {
        result = result.multiply(new BigDecimal(value.toString()));
      }
    }
    return result;
  }

  /**
   * 提供精确的乘法运算
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @return 积
   */
  public static BigDecimal mul(String v1, String v2) {
    return mul(new BigDecimal(v1), new BigDecimal(v2));
  }

  /**
   * 提供精确的乘法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param values 多个被乘值
   * @return 积
   */
  public static BigDecimal mul(String... values) {
    if (Is.empty(values)) {
      return BigDecimal.ZERO;
    }

    String value = values[0];
    BigDecimal result = new BigDecimal(null == value ? "0" : value);
    for (int i = 1; i < values.length; i++) {
      value = values[i];
      if (null != value) {
        result = result.multiply(new BigDecimal(value));
      }
    }
    return result;
  }

  /**
   * 提供精确的乘法运算<br>
   * 如果传入多个值为null或者空，则返回0
   *
   * @param values 多个被乘值
   * @return 积
   */
  public static BigDecimal mul(BigDecimal... values) {
    if (Is.empty(values)) {
      return BigDecimal.ZERO;
    }

    BigDecimal value = values[0];
    BigDecimal result = null == value ? BigDecimal.ZERO : value;
    for (int i = 1; i < values.length; i++) {
      value = values[i];
      if (null != value) {
        result = result.multiply(value);
      }
    }
    return result;
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @return 两个参数的商
   */
  public static double div(float v1, float v2) {
    return div(v1, v2, DEFAULT_DIV_SCALE);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @return 两个参数的商
   */
  public static double div(float v1, double v2) {
    return div(v1, v2, DEFAULT_DIV_SCALE);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @return 两个参数的商
   */
  public static double div(double v1, float v2) {
    return div(v1, v2, DEFAULT_DIV_SCALE);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @return 两个参数的商
   */
  public static double div(double v1, double v2) {
    return div(v1, v2, DEFAULT_DIV_SCALE);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @return 两个参数的商
   */
  public static double div(Double v1, Double v2) {
    return div(v1, v2, DEFAULT_DIV_SCALE);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @return 两个参数的商
   */
  public static BigDecimal div(Number v1, Number v2) {
    return div(v1, v2, DEFAULT_DIV_SCALE);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况的时候,精确到小数点后10位,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @return 两个参数的商
   */
  public static BigDecimal div(String v1, String v2) {
    return div(v1, v2, DEFAULT_DIV_SCALE);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @return 两个参数的商
   */
  public static double div(float v1, float v2, int scale) {
    return div(v1, v2, scale, RoundingMode.HALF_UP);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @return 两个参数的商
   */
  public static double div(float v1, double v2, int scale) {
    return div(v1, v2, scale, RoundingMode.HALF_UP);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @return 两个参数的商
   */
  public static double div(double v1, float v2, int scale) {
    return div(v1, v2, scale, RoundingMode.HALF_UP);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @return 两个参数的商
   */
  public static double div(double v1, double v2, int scale) {
    return div(v1, v2, scale, RoundingMode.HALF_UP);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @return 两个参数的商
   */
  public static double div(Double v1, Double v2, int scale) {
    return div(v1, v2, scale, RoundingMode.HALF_UP);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @return 两个参数的商
   */
  public static BigDecimal div(Number v1, Number v2, int scale) {
    return div(v1, v2, scale, RoundingMode.HALF_UP);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度,后面的四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @return 两个参数的商
   */
  public static BigDecimal div(String v1, String v2, int scale) {
    return div(v1, v2, scale, RoundingMode.HALF_UP);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 两个参数的商
   */
  public static double div(float v1, float v2, int scale, RoundingMode roundingMode) {
    return div(Float.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 两个参数的商
   */
  public static double div(float v1, double v2, int scale, RoundingMode roundingMode) {
    return div(Float.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 两个参数的商
   */
  public static double div(double v1, float v2, int scale, RoundingMode roundingMode) {
    return div(Double.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 两个参数的商
   */
  public static double div(double v1, double v2, int scale, RoundingMode roundingMode) {
    return div(Double.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 两个参数的商
   */
  public static double div(Double v1, Double v2, int scale, RoundingMode roundingMode) {
    return div(v1, (Number) v2, scale, roundingMode).doubleValue();
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 两个参数的商
   */
  public static BigDecimal div(Number v1, Number v2, int scale, RoundingMode roundingMode) {
    return div(v1.toString(), v2.toString(), scale, roundingMode);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 两个参数的商
   */
  public static BigDecimal div(String v1, String v2, int scale, RoundingMode roundingMode) {
    return div(new BigDecimal(v1), new BigDecimal(v2), scale, roundingMode);
  }

  /**
   * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 精确度，如果为负值，取绝对值
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 两个参数的商
   */
  public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
    if (null == v1) {
      return BigDecimal.ZERO;
    }
    if (scale < 0) {
      scale = -scale;
    }
    return v1.divide(v2, scale, roundingMode);
  }

  // -------------------------------------------------------------------------------------------
  // round

  /**
   * 保留固定位数小数<br>
   * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
   * 例如保留2位小数：123.456789 =》 123.46
   *
   * @param v 值
   * @param scale 保留小数位数
   * @return 新值
   */
  public static BigDecimal round(double v, int scale) {
    return round(v, scale, RoundingMode.HALF_UP);
  }

  /**
   * 保留固定位数小数<br>
   * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
   * 例如保留2位小数：123.456789 =》 123.46
   *
   * @param v 值
   * @param scale 保留小数位数
   * @return 新值
   */
  public static String roundStr(double v, int scale) {
    return round(v, scale).toString();
  }

  /**
   * 保留固定位数小数<br>
   * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
   * 例如保留2位小数：123.456789 =》 123.46
   *
   * @param numberStr 数字值的字符串表现形式
   * @param scale 保留小数位数
   * @return 新值
   */
  public static BigDecimal round(String numberStr, int scale) {
    return round(numberStr, scale, RoundingMode.HALF_UP);
  }

  /**
   * 保留固定位数小数<br>
   * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
   * 例如保留2位小数：123.456789 =》 123.46
   *
   * @param number 数字值
   * @param scale 保留小数位数
   * @return 新值
   */
  public static BigDecimal round(BigDecimal number, int scale) {
    return round(number, scale, RoundingMode.HALF_UP);
  }

  /**
   * 保留固定位数小数<br>
   * 采用四舍五入策略 {@link RoundingMode#HALF_UP}<br>
   * 例如保留2位小数：123.456789 =》 123.46
   *
   * @param numberStr 数字值的字符串表现形式
   * @param scale 保留小数位数
   * @return 新值
   */
  public static String roundStr(String numberStr, int scale) {
    return round(numberStr, scale).toString();
  }

  /**
   * 保留固定位数小数<br>
   * 例如保留四位小数：123.456789 =》 123.4567
   *
   * @param v 值
   * @param scale 保留小数位数
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 新值
   */
  public static BigDecimal round(double v, int scale, RoundingMode roundingMode) {
    return round(Double.toString(v), scale, roundingMode);
  }

  /**
   * 保留固定位数小数<br>
   * 例如保留四位小数：123.456789 =》 123.4567
   *
   * @param v 值
   * @param scale 保留小数位数
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 新值
   */
  public static String roundStr(double v, int scale, RoundingMode roundingMode) {
    return round(v, scale, roundingMode).toString();
  }

  /**
   * 保留固定位数小数<br>
   * 例如保留四位小数：123.456789 =》 123.4567
   *
   * @param numberStr 数字值的字符串表现形式
   * @param scale 保留小数位数，如果传入小于0，则默认0
   * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
   * @return 新值
   */
  public static BigDecimal round(String numberStr, int scale, RoundingMode roundingMode) {
    if (scale < 0) {
      scale = 0;
    }
    return round(Cast.bigDecimalValue(numberStr), scale, roundingMode);
  }

  /**
   * 保留固定位数小数<br>
   * 例如保留四位小数：123.456789 =》 123.4567
   *
   * @param number 数字值
   * @param scale 保留小数位数，如果传入小于0，则默认0
   * @param roundingMode 保留小数的模式 {@link RoundingMode}，如果传入null则默认四舍五入
   * @return 新值
   */
  public static BigDecimal round(BigDecimal number, int scale, RoundingMode roundingMode) {
    if (null == number) {
      number = BigDecimal.ZERO;
    }
    if (scale < 0) {
      scale = 0;
    }
    if (null == roundingMode) {
      roundingMode = RoundingMode.HALF_UP;
    }

    return number.setScale(scale, roundingMode);
  }

  /**
   * 保留固定位数小数<br>
   * 例如保留四位小数：123.456789 =》 123.4567
   *
   * @param numberStr 数字值的字符串表现形式
   * @param scale 保留小数位数
   * @param roundingMode 保留小数的模式 {@link RoundingMode}
   * @return 新值
   */
  public static String roundStr(String numberStr, int scale, RoundingMode roundingMode) {
    return round(numberStr, scale, roundingMode).toString();
  }

  /**
   * 四舍六入五成双计算法
   *
   * <p>四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
   *
   * <pre>
   * 算法规则:
   * 四舍六入五考虑，
   * 五后非零就进一，
   * 五后皆零看奇偶，
   * 五前为偶应舍去，
   * 五前为奇要进一。
   * </pre>
   *
   * @param number 需要科学计算的数据
   * @param scale 保留的小数位
   * @return 结果
   */
  public static BigDecimal roundHalfEven(Number number, int scale) {
    return roundHalfEven(Cast.bigDecimalValue(number), scale);
  }

  /**
   * 四舍六入五成双计算法
   *
   * <p>四舍六入五成双是一种比较精确比较科学的计数保留法，是一种数字修约规则。
   *
   * <pre>
   * 算法规则:
   * 四舍六入五考虑，
   * 五后非零就进一，
   * 五后皆零看奇偶，
   * 五前为偶应舍去，
   * 五前为奇要进一。
   * </pre>
   *
   * @param value 需要科学计算的数据
   * @param scale 保留的小数位
   * @return 结果
   */
  public static BigDecimal roundHalfEven(BigDecimal value, int scale) {
    return round(value, scale, RoundingMode.HALF_EVEN);
  }

  /**
   * 保留固定小数位数，舍去多余位数
   *
   * @param number 需要科学计算的数据
   * @param scale 保留的小数位
   * @return 结果
   */
  public static BigDecimal roundDown(Number number, int scale) {
    return roundDown(Cast.bigDecimalValue(number), scale);
  }

  /**
   * 保留固定小数位数，舍去多余位数
   *
   * @param value 需要科学计算的数据
   * @param scale 保留的小数位
   * @return 结果
   */
  public static BigDecimal roundDown(BigDecimal value, int scale) {
    return round(value, scale, RoundingMode.DOWN);
  }

  // -------------------------------------------------------------------------------------------
  // decimalFormat

  /**
   * 格式化double<br>
   * 对 {@link DecimalFormat} 做封装<br>
   *
   * @param pattern 格式 格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。<br>
   *     <ul>
   *       <li>0 =》 取一位整数
   *       <li>0.00 =》 取一位整数和两位小数
   *       <li>00.000 =》 取两位整数和三位小数
   *       <li># =》 取所有整数部分
   *       <li>#.##% =》 以百分比方式计数，并取两位小数
   *       <li>#.#####E0 =》 显示为科学计数法，并取五位小数
   *       <li>,### =》 每三位以逗号进行分隔，例如：299,792,458
   *       <li>光速大小为每秒,###米 =》 将格式嵌入文本
   *     </ul>
   *
   * @param value 值
   * @return 格式化后的值
   */
  public static String decimalFormat(String pattern, double value) {
    return new DecimalFormat(pattern).format(value);
  }

  /**
   * 格式化double<br>
   * 对 {@link DecimalFormat} 做封装<br>
   *
   * @param pattern 格式 格式中主要以 # 和 0 两种占位符号来指定数字长度。0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置。<br>
   *     <ul>
   *       <li>0 =》 取一位整数
   *       <li>0.00 =》 取一位整数和两位小数
   *       <li>00.000 =》 取两位整数和三位小数
   *       <li># =》 取所有整数部分
   *       <li>#.##% =》 以百分比方式计数，并取两位小数
   *       <li>#.#####E0 =》 显示为科学计数法，并取五位小数
   *       <li>,### =》 每三位以逗号进行分隔，例如：299,792,458
   *       <li>光速大小为每秒,###米 =》 将格式嵌入文本
   *     </ul>
   *
   * @param value 值
   * @return 格式化后的值
   */
  public static String decimalFormat(String pattern, long value) {
    return new DecimalFormat(pattern).format(value);
  }

  /**
   * 格式化金额输出，每三位用逗号分隔
   *
   * @param value 金额
   * @return 格式化后的值
   */
  public static String decimalFormatMoney(double value) {
    return decimalFormat(",##0.00", value);
  }

  /**
   * 格式化百分比，小数采用四舍五入方式
   *
   * @param number 值
   * @param scale 保留小数位数
   * @return 百分比
   */
  public static String formatPercent(double number, int scale) {
    final NumberFormat format = NumberFormat.getPercentInstance();
    format.setMaximumFractionDigits(scale);
    return format.format(number);
  }

  // -------------------------------------------------------------------------------------------
  // generateXXX

  /**
   * 生成不重复随机数 根据给定的最小数字和最大数字，以及随机数的个数，产生指定的不重复的数组
   *
   * @param begin 最小数字（包含该数）
   * @param end 最大数字（不包含该数）
   * @param size 指定产生随机数的个数
   * @return 随机int数组
   */
  public static int[] generateRandomNumber(int begin, int end, int size) {
    if (begin > end) {
      int temp = begin;
      begin = end;
      end = temp;
    }
    // 加入逻辑判断，确保begin<end并且size不能大于该表示范围
    if ((end - begin) < size) {
      throw new IllegalArgumentException("Size is larger than range between begin and end!");
    }
    // 种子你可以随意生成，但不能重复
    int[] seed = new int[end - begin];

    for (int i = begin; i < end; i++) {
      seed[i - begin] = i;
    }
    int[] ranArr = new int[size];
    Random ran = new Random();
    // 数量你可以自己定义。
    for (int i = 0; i < size; i++) {
      // 得到一个位置
      int j = ran.nextInt(seed.length - i);
      // 得到那个位置的数值
      ranArr[i] = seed[j];
      // 将最后一个未用的数字放到这里
      seed[j] = seed[seed.length - 1 - i];
    }
    return ranArr;
  }

  /**
   * 生成不重复随机数 根据给定的最小数字和最大数字，以及随机数的个数，产生指定的不重复的数组
   *
   * @param begin 最小数字（包含该数）
   * @param end 最大数字（不包含该数）
   * @param size 指定产生随机数的个数
   * @return 随机int数组
   */
  public static Integer[] generateBySet(int begin, int end, int size) {
    if (begin > end) {
      int temp = begin;
      begin = end;
      end = temp;
    }
    // 加入逻辑判断，确保begin<end并且size不能大于该表示范围
    if ((end - begin) < size) {
      throw new IllegalArgumentException("Size is larger than range between begin and end!");
    }

    Random ran = new Random();
    Set<Integer> set = new HashSet<>();
    while (set.size() < size) {
      set.add(begin + ran.nextInt(end - begin));
    }

    return set.toArray(new Integer[size]);
  }

  // -------------------------------------------------------------------------------------------
  // range

  /**
   * 从0开始给定范围内的整数列表，步进为1
   *
   * @param stop 结束（包含）
   * @return 整数列表
   */
  public static int[] range(int stop) {
    return range(0, stop);
  }

  /**
   * 给定范围内的整数列表，步进为1
   *
   * @param start 开始（包含）
   * @param stop 结束（包含）
   * @return 整数列表
   */
  public static int[] range(int start, int stop) {
    return range(start, stop, 1);
  }

  /**
   * 给定范围内的整数列表
   *
   * @param start 开始（包含）
   * @param stop 结束（包含）
   * @param step 步进
   * @return 整数列表
   */
  public static int[] range(int start, int stop, int step) {
    if (start < stop) {
      step = Math.abs(step);
    } else if (start > stop) {
      step = -Math.abs(step);
    } else { // start == end
      return new int[] {start};
    }

    int size = Math.abs((stop - start) / step) + 1;
    int[] values = new int[size];
    int index = 0;
    for (int i = start; (step > 0) ? i <= stop : i >= stop; i += step) {
      values[index] = i;
      index++;
    }
    return values;
  }

  /**
   * 将给定范围内的整数添加到已有集合中，步进为1
   *
   * @param start 开始（包含）
   * @param stop 结束（包含）
   * @param values 集合
   * @return 集合
   */
  public static Collection<Integer> appendRange(int start, int stop, Collection<Integer> values) {
    return appendRange(start, stop, 1, values);
  }

  /**
   * 将给定范围内的整数添加到已有集合中
   *
   * @param start 开始（包含）
   * @param stop 结束（包含）
   * @param step 步进
   * @param values 集合
   * @return 集合
   */
  public static Collection<Integer> appendRange(
      int start, int stop, int step, Collection<Integer> values) {
    if (start < stop) {
      step = Math.abs(step);
    } else if (start > stop) {
      step = -Math.abs(step);
    } else { // start == end
      values.add(start);
      return values;
    }

    for (int i = start; (step > 0) ? i <= stop : i >= stop; i += step) {
      values.add(i);
    }
    return values;
  }

  // -------------------------------------------------------------------------------------------
  // others

  /**
   * 计算阶乘
   *
   * <p>n! = n * (n-1) * ... * end
   *
   * @param start 阶乘起始
   * @param end 阶乘结束
   * @return 结果
   */
  public static long factorial(long start, long end) {
    if (start < end) {
      return 0L;
    }
    if (start == end) {
      return 1L;
    }
    return start * factorial(start - 1, end);
  }

  /**
   * 计算阶乘
   *
   * <p>n! = n * (n-1) * ... * 2 * 1
   *
   * @param n 阶乘起始
   * @return 结果
   */
  public static long factorial(long n) {
    return factorial(n, 1);
  }

  /**
   * 平方根算法<br>
   * 推荐使用 {@link Math#sqrt(double)}
   *
   * @param x 值
   * @return 平方根
   */
  public static long sqrt(long x) {
    long y = 0;
    long b = (~Long.MAX_VALUE) >>> 1;
    while (b > 0) {
      if (x >= y + b) {
        x -= y + b;
        y >>= 1;
        y += b;
      } else {
        y >>= 1;
      }
      b >>= 2;
    }
    return y;
  }

  /**
   * 可以用于计算双色球、大乐透注数的方法<br>
   * 比如大乐透35选5可以这样调用processMultiple(7,5); 就是数学中的：C75=7*6/2*1
   *
   * @param selectNum 选中小球个数
   * @param minNum 最少要选中多少个小球
   * @return 注数
   */
  public static int processMultiple(int selectNum, int minNum) {
    int result;
    result = mathSubnode(selectNum, minNum) / mathNode(selectNum - minNum);
    return result;
  }

  /**
   * 最大公约数
   *
   * @param m 第一个值
   * @param n 第二个值
   * @return 最大公约数
   */
  public static int divisor(int m, int n) {
    while (m % n != 0) {
      int temp = m % n;
      m = n;
      n = temp;
    }
    return n;
  }

  /**
   * 最小公倍数
   *
   * @param m 第一个值
   * @param n 第二个值
   * @return 最小公倍数
   */
  public static int multiple(int m, int n) {
    return m * n / divisor(m, n);
  }

  /**
   * 获得数字对应的二进制字符串
   *
   * @param number 数字
   * @return 二进制字符串
   */
  public static String getBinaryStr(Number number) {
    if (number instanceof Long) {
      return Long.toBinaryString((Long) number);
    } else if (number instanceof Integer) {
      return Integer.toBinaryString((Integer) number);
    } else {
      return Long.toBinaryString(number.longValue());
    }
  }

  /**
   * 计算等份个数
   *
   * @param total 总数
   * @param part 每份的个数
   * @return 分成了几份
   */
  public static int count(int total, int part) {
    return (total % part == 0) ? (total / part) : (total / part + 1);
  }

  /**
   * 空转0
   *
   * @param decimal {@link BigDecimal}，可以为{@code null}
   * @return {@link BigDecimal}参数为空时返回0的值
   */
  public static BigDecimal null2Zero(BigDecimal decimal) {

    return decimal == null ? BigDecimal.ZERO : decimal;
  }

  /**
   * 如果给定值为0，返回1，否则返回原值
   *
   * @param value 值
   * @return 1或非0值
   */
  public static int zero2One(int value) {
    return 0 == value ? 1 : value;
  }

  /**
   * 把给定的总数平均分成N份，返回每份的个数<br>
   * 当除以分数有余数时每份+1
   *
   * @param total 总数
   * @param partCount 份数
   * @return 每份的个数
   */
  public static int partValue(int total, int partCount) {
    return partValue(total, partCount, true);
  }

  /**
   * 把给定的总数平均分成N份，返回每份的个数<br>
   * 如果isPlusOneWhenHasRem为true，则当除以分数有余数时每份+1，否则丢弃余数部分
   *
   * @param total 总数
   * @param partCount 份数
   * @param isPlusOneWhenHasRem 在有余数时是否每份+1
   * @return 每份的个数
   */
  public static int partValue(int total, int partCount, boolean isPlusOneWhenHasRem) {
    int partValue = total / partCount;
    if (total % partCount != 0 && isPlusOneWhenHasRem) {
      partValue += 1;
    }
    return partValue;
  }

  /**
   * 提供精确的幂运算
   *
   * @param number 底数
   * @param n 指数
   * @return 幂的积
   */
  public static BigDecimal pow(Number number, int n) {
    return pow(Cast.bigDecimalValue(number), n);
  }

  /**
   * 提供精确的幂运算
   *
   * @param number 底数
   * @param n 指数
   * @return 幂的积
   */
  public static BigDecimal pow(BigDecimal number, int n) {
    return number.pow(n);
  }

  /**
   * 无符号bytes转{@link BigInteger}
   *
   * @param buf buf 无符号bytes
   * @return {@link BigInteger}
   */
  public static BigInteger fromUnsignedByteArray(byte[] buf) {
    return new BigInteger(1, buf);
  }

  /**
   * 无符号bytes转{@link BigInteger}
   *
   * @param buf 无符号bytes
   * @param off 起始位置
   * @param length 长度
   * @return {@link BigInteger}
   */
  public static BigInteger fromUnsignedByteArray(byte[] buf, int off, int length) {
    byte[] mag = buf;
    if (off != 0 || length != buf.length) {
      mag = new byte[length];
      System.arraycopy(buf, off, mag, 0, length);
    }
    return new BigInteger(1, mag);
  }

  private static int mathSubnode(int selectNum, int minNum) {
    if (selectNum == minNum) {
      return 1;
    } else {
      return selectNum * mathSubnode(selectNum - 1, minNum);
    }
  }

  private static int mathNode(int selectNum) {
    if (selectNum == 0) {
      return 1;
    } else {
      return selectNum * mathNode(selectNum - 1);
    }
  }

  /**
   * 去掉数字尾部的数字标识，例如12D，44.0F，22L中的最后一个字母
   *
   * @param number 数字字符串
   * @return 去掉标识的字符串
   */
  private static String removeNumberFlag(String number) {
    // 去掉类型标识的结尾
    final int lastPos = number.length() - 1;
    final char lastCharUpper = Character.toUpperCase(number.charAt(lastPos));
    if ('D' == lastCharUpper || 'L' == lastCharUpper || 'F' == lastCharUpper) {
      number = number.substring(0, lastPos);
    }
    return number;
  }
  // -------------------------------------------------------------------------------------------
  // Private method end
}

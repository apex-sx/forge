package pxf.toolkit.basic.text.parser.capitulation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import pxf.toolkit.basic.util.Is;

/**
 * 中文整数转大写助手
 *
 * @author potatoxf
 * @date 2021/5/22
 */
public final class ChineseIntegerHelper {

  /** 0-9对应数字 */
  private final String[] digit;

  /** 小单位序列 */
  private final String[] smallUnit;

  /** 大单位序列 */
  private final String[] bigUnit;

  /** 间隔，没多少个小单位添加大单位 */
  private final int interval;

  /** 简化书写 */
  private final Map<String, String> simplification;

  /**
   * 构造函数
   *
   * @param digit 0-9对应数字
   * @param smallUnit 小单位
   * @param bigUnit 大单位序列
   * @param simplificationKeyValue 简化书写的元素
   */
  public ChineseIntegerHelper(
      @Nonnull String[] digit,
      @Nonnull String[] smallUnit,
      @Nonnull String[] bigUnit,
      String... simplificationKeyValue) {

    this.digit = digit;
    this.smallUnit = smallUnit;
    this.bigUnit = bigUnit;
    this.interval = smallUnit.length;
    if (Is.empty(simplificationKeyValue)) {
      this.simplification = null;
    } else {
      int len = simplificationKeyValue.length / 2;
      Map<String, String> map = new HashMap<>(len, 1);
      for (int i = 0; i < len; i++) {
        map.put(simplificationKeyValue[i++], simplificationKeyValue[i]);
      }
      this.simplification = map;
    }
  }

  /**
   * 构造函数
   *
   * @param digit 0-9对应数字
   * @param smallUnit 小单位序列
   * @param bigUnit 大单位序列
   * @param simplification 简化书写
   */
  public ChineseIntegerHelper(
      final @Nonnull String[] digit,
      final @Nonnull String[] smallUnit,
      final @Nonnull String[] bigUnit,
      final @Nullable Map<String, String> simplification) {

    this.digit = digit;
    this.smallUnit = smallUnit;
    this.bigUnit = bigUnit;
    this.interval = smallUnit.length;
    this.simplification = simplification;
  }

  /**
   * 解析整数部分
   *
   * @param integerNumber 整数
   * @return 返回正规整数
   */
  public String resolve(String integerNumber) {
    if (!Is.integerNumber(integerNumber)) {
      throw new IllegalArgumentException("Input parameter is not an integerNumber");
    }
    return resolveNumber(integerNumber);
  }
  /**
   * 解析整数部分
   *
   * @param integerNumber 整数
   * @return 返回正规整数
   */
  String resolveNumber(String integerNumber) {
    LinkedList<String> stack = new LinkedList<>();
    int length = integerNumber.length();
    for (int i = 0; i < length; i++) {
      char c = integerNumber.charAt(length - i - 1);
      String digitString;
      if (c == '0') {
        digitString = "0";
      } else {
        digitString = digit[Character.digit(c, 10)] + smallUnit[i % interval];
      }
      if (i % interval == 0) {
        stack.push(digitString + bigUnit[i / interval]);
      } else {
        stack.push(digitString);
      }
    }
    StringBuilder sb = new StringBuilder(integerNumber.length() * 2);
    boolean lastZero = false;
    for (String s : stack) {
      if ("0".equals(s)) {
        if (!lastZero) {
          sb.append(digit[0]);
          lastZero = true;
        }
      } else {
        lastZero = false;
        if (s.startsWith("0")) {
          sb.append(s.substring(1));
        } else {
          if (simplification == null) {
            sb.append(s);
          } else {
            sb.append(simplification.getOrDefault(s, s));
          }
        }
      }
    }
    return sb.toString();
  }

  public int getMaxSupportLength() {
    return bigUnit.length * interval;
  }
}

package pxf.toolkit.basic.text.parser.capitulation;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.annotation.Nonnull;
import pxf.toolkit.basic.util.Empty;

/**
 * 大写数字
 *
 * @author potatoxf
 * @date 2021/5/22
 */
public interface CapitalizedTextParser {

  /**
   * 解析合格金额
   *
   * @param number 阿拉伯数字金额
   * @return 返回正规的金额数字
   */
  default String parse(BigDecimal number) {
    if (number == null) {
      return Empty.STRING;
    }
    return parse(number.toEngineeringString());
  }

  /**
   * 解析合格金额
   *
   * @param number 阿拉伯数字金额
   * @return 返回正规的金额数字
   */
  default String parse(BigInteger number) {
    if (number == null) {
      return Empty.STRING;
    }
    return parse(number.toString());
  }

  /**
   * 解析合格金额
   *
   * @param number 阿拉伯数字金额
   * @return 返回正规的金额数字
   */
  default String parse(int number) {
    return parse(String.valueOf(number));
  }
  /**
   * 解析合格金额
   *
   * @param number 阿拉伯数字金额
   * @return 返回正规的金额数字
   */
  default String parse(long number) {
    return parse(String.valueOf(number));
  }

  /**
   * 解析合格金额
   *
   * @param number 阿拉伯数字金额
   * @return 返回正规的金额数字
   */
  default String parse(float number) {
    return parse(String.valueOf(number));
  }

  /**
   * 解析合格金额
   *
   * @param number 阿拉伯数字金额
   * @return 返回正规的金额数字
   */
  default String parse(double number) {
    return parse(String.valueOf(number));
  }

  /**
   * 解析合格金额
   *
   * @param number 阿拉伯数字金额
   * @return 返回正规的金额数字
   */
  @Nonnull
  String parse(@Nonnull String number);
}

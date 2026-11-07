/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.regularamount;

import com.utils.UtilsOfAssert;
import com.utils.UtilsOfConstant;
import com.utils.UtilsOfJudge;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author : Alfred X Fu
 * @date : 2020/4/24 11:40
 */
public abstract class AbstractRegularAmount implements RegularAmount {

  /**
   * 阿拉伯数字正则表达式
   */
  public static final String ARABIC_NUMERALS_REG_EXP = "^((\\d\\.\\d*)|([1-9]\\d*(\\.\\d*)?))$";
  /**
   * 阿拉伯数字正则表达式
   */
  private static final Pattern ARABIC_NUMERALS_PATTERN = Pattern.compile(ARABIC_NUMERALS_REG_EXP);

  /**
   * 检查是否是阿拉伯数字
   *
   * @param string 字符串
   */
  private static void checkIsArabicNumerals(String string) {
    Matcher matcher = ARABIC_NUMERALS_PATTERN.matcher(string);
    if (!matcher.matches()) {
      throw new IllegalArgumentException("Don't arabic numerals");
    }
  }

  private int decimalBit = 3;

  public void setDecimalBit(int decimalBit) {
    this.decimalBit = decimalBit;
  }

  public int getDecimalBit() {
    return decimalBit;
  }

  /**
   * 解析合格金额
   *
   * @param amount 阿拉伯数字金额
   *
   * @return 返回正规的金额数字
   */
  @Override
  public final String parse(String amount) {
    UtilsOfAssert.checkValid(amount, "Amount is invalid");
    checkIsArabicNumerals(amount);
    int index = amount.indexOf(UtilsOfConstant.C_DOT);
    String integer;
    String decimal;
    if (UtilsOfJudge.isEOF(index)) {
      integer = amount;
      decimal = UtilsOfConstant.EMPTY;
    } else {
      integer = amount.substring(0, index);
      BigDecimal bigDecimal = new BigDecimal("0" + amount.substring(index));
      bigDecimal = bigDecimal.setScale(decimalBit, RoundingMode.HALF_UP);
      decimal = String.valueOf(bigDecimal.doubleValue()).substring(2);
    }
    integer = parseInteger(integer);
    if (!UtilsOfJudge.isEOF(index)) {
      decimal = parseDecimal(decimal);
    }
    return marge(integer, decimal);
  }

  /**
   * 合并正规整数和小数部分
   *
   * @param integer 整数部分
   * @param decimal 小数部分，没有返回空字符串
   *
   * @return 合并成正规的数字
   */
  protected abstract String marge(String integer, String decimal);

  /**
   * 解析整数部分
   *
   * @param amount 整数
   *
   * @return 返回正规整数
   */
  protected abstract String parseInteger(String amount);

  /**
   * 解析小数部分
   *
   * @param amount 小数
   *
   * @return 返回正规小数
   */
  protected abstract String parseDecimal(String amount);
}

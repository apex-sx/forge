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
import com.utils.UtilsOfFunction;
import com.utils.UtilsOfJudge;
import java.util.Deque;
import java.util.LinkedList;

/**
 * @author : Alfred X Fu
 * @date : 2020/4/24 11:39
 */
public class ChineseCapitalized extends AbstractRegularAmount {

  private static final int MAX_INTEGER_BIT = 13;
  private static final int MAX_DECIMAL_BIT = 3;
  private static final int SEGMENT_BIT_AMOUNT = 4;
  private static final String IGNORED_ZERO = "零";
  private static final String IGNORED_BIT_UNIT = "拾";
  private static final String INTEGER_UNIT = "元";
  private static final String DECIMAL_FINAL_UNIT = "元整";
  private static final String[] DIGIT = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
  private static final String[] INTEGER_BIT_UNIT = {"", "拾", "佰", "仟"};
  private static final String[] DECIMAL_BIT_UNIT = {"角", "分", "厘"};
  private static final String[] SEGMENT_UNIT = {"万", "亿", "兆"};

  /**
   * 合并正规整数和小数部分
   *
   * @param integer 整数部分
   * @param decimal 小数部分，没有返回空字符串
   *
   * @return 合并成正规的数字
   */
  @Override
  protected String marge(String integer, String decimal) {
    if (UtilsOfJudge.isInvalid(decimal)) {
      return integer + DECIMAL_FINAL_UNIT;
    }
    return integer + INTEGER_UNIT + decimal;
  }

  private String numberString(char c) {
    final int v = Character.digit(c, 10);
    return DIGIT[v];
  }

  /**
   * 解析整数部分
   *
   * @param amount 整数
   *
   * @return 返回正规整数
   */
  @Override
  protected String parseInteger(final String amount) {
    final int length = amount.length();
    UtilsOfAssert.checkLesserEqual(length, MAX_INTEGER_BIT,
        "Integer bit don't allow greater than " + MAX_INTEGER_BIT);
    final String[] segmentUnit = generateIntegerSegmentUnits(length);
    final int segmentLength = segmentUnit.length;
    Deque<String> stack = new LinkedList<>();
    int bit = 0;
    for (int i = 0; i < segmentLength && bit < length; i++) {
      for (int j = 0; j < SEGMENT_BIT_AMOUNT; j++) {
        bit = i * SEGMENT_BIT_AMOUNT + j;
        if (bit >= length) {
          break;
        }
        final String cv = numberString(amount.charAt(length - 1 - bit));
        //栈为空，不允许首位是零
        if (stack.isEmpty()) {
          if (isAllowAddBitUnit(cv)) {
            stack.push(INTEGER_BIT_UNIT[j]);
            stack.push(cv);
          }
          continue;
        }
        String top = stack.peek();
        boolean isNoRepeatZero = !IGNORED_ZERO.equals(top) || !IGNORED_ZERO.equals(cv);
        //不是重复的零
        if (isNoRepeatZero) {
          //是否允许添加位单位
          if (isAllowAddBitUnit(cv)) {
            stack.push(INTEGER_BIT_UNIT[j]);
          }
          //是否允许添加当前值
          if (isAllowAddBitValue(top, INTEGER_BIT_UNIT[j], cv)) {
            stack.push(cv);
          }
        }
      }
      if (i < segmentLength - 1) {
        stack.push(segmentUnit[i + 1]);
      }
    }
    StringBuilder sb = new StringBuilder(amount.length() * 4 + 10);
    UtilsOfFunction.reserveForEach(stack, sb::append);
    return sb.toString();
  }

  /**
   * 解析小数部分
   *
   * @param amount 小数
   *
   * @return 返回正规小数
   */
  @Override
  protected String parseDecimal(String amount) {
    int decimal = getDecimalBit();
    UtilsOfAssert.checkLesserEqual(decimal, DECIMAL_BIT_UNIT.length,
        "Don't support decimal bit more than " + DECIMAL_BIT_UNIT.length);
    StringBuilder sb = new StringBuilder(decimal * 4 + 10);
    for (int i = 0; i < amount.length(); i++) {
      final String cv = numberString(amount.charAt(i));
      if (isAllowAddBitValue("", "", cv)) {
        sb.append(cv);
      }
      if (isAllowAddBitUnit(cv)) {
        sb.append(DECIMAL_BIT_UNIT[i]);
      }
    }
    return sb.toString();
  }

  private String[] generateIntegerSegmentUnits(int integerBit) {
    final int repeat = integerBit / SEGMENT_BIT_AMOUNT + 1;
    String[] strings = new String[repeat];
    strings[0] = UtilsOfConstant.EMPTY;
    String level = "";
    for (int i = 1, j = 0; i < repeat; i++, j++) {
      strings[i] = SEGMENT_UNIT[j];
    }
    return strings;
  }

  private boolean isAllowAddBitUnit(String current) {
    return !IGNORED_ZERO.equals(current);
  }

  private boolean isAllowAddBitValue(String top, String currentUnit, String current) {
    if (isSegmentUnit(top) && IGNORED_ZERO.equals(current)) {
      return false;
    }
    return !IGNORED_BIT_UNIT.equals(currentUnit);
  }

  private boolean isSegmentUnit(String string) {
    return UtilsOfJudge.isAnyMappingCorrect(SEGMENT_UNIT, o -> o.equals(string));
  }
}

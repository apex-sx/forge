/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math.constants;

import java.math.BigDecimal;
import java.util.function.BiFunction;

/**
 * 计算符号
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public enum Symbol {
  /**
   * 左括号
   */
  LEFT_BRACKETS(Integer.MAX_VALUE, (a, b) -> {
    throw new RuntimeException("Don't allow!");
  }),
  /**
   * 右括号
   */
  RIGHT_BRACKETS(Integer.MAX_VALUE, (a, b) -> {
    throw new RuntimeException("Don't allow!");
  }),
  /**
   * 加
   */
  PLUS(0, BigDecimal::add),
  /**
   * 减
   */
  MINUS(0, BigDecimal::subtract),
  /**
   * 乘
   */
  MULTIPLY(1, BigDecimal::multiply),
  /**
   * 除
   */
  DIVIDE(1, BigDecimal::divide);
  /**
   * 优先级，值越大优先级越高
   */
  private final int priority;
  /**
   * 计算执行
   */
  private final BiFunction<BigDecimal, BigDecimal, BigDecimal> computeExec;

  Symbol(final int priority, final BiFunction<BigDecimal, BigDecimal, BigDecimal> computeExec) {
    this.priority = priority;
    this.computeExec = computeExec;
  }

  /**
   * 判断该字符串是否是符号
   *
   * @param ch 符号字符串
   * @return true为是支持的符号字符串，否则为false
   */
  public static boolean isSymbol(final char ch) {
    return Symbol.isSymbol(ch + "");
  }

  /**
   * 判断该字符串是否是符号
   *
   * @param ch 符号字符串
   * @return true为是支持的符号字符串，否则为false
   */
  public static boolean isSymbol(final String ch) {
    try {
      Symbol.parse(ch);
      return true;
    } catch (final Exception ignored) {
    }
    return false;
  }

  /**
   * 将字符串解析成CalculatorSymbol
   *
   * @param ch 符号字符串
   * @return Symbol
   */
  public static Symbol parse(final String ch) {
    if ("{".equals(ch) || "[".equals(ch) || "(".equals(ch)) {
      return Symbol.LEFT_BRACKETS;
    }
    if ("}".equals(ch) || "]".equals(ch) || ")".equals(ch)) {
      return Symbol.RIGHT_BRACKETS;
    }
    if ("+".equals(ch)) {
      return Symbol.PLUS;
    }
    if ("-".equals(ch)) {
      return Symbol.MINUS;
    }
    if ("*".equals(ch)) {
      return Symbol.MULTIPLY;
    }
    if ("/".equals(ch)) {
      return Symbol.DIVIDE;
    }
    throw new RuntimeException("Don't Support symbol of [" + ch + "]");
  }

  public static boolean isLeftBrackets(final char ch) {
    return Symbol.isLeftBrackets(ch + "");
  }

  public static boolean isLeftBrackets(final String ch) {
    return "{".equals(ch) || "[".equals(ch) || "(".equals(ch);
  }

  public static boolean isRightBrackets(final char ch) {
    return Symbol.isRightBrackets(ch + "");
  }

  public static boolean isRightBrackets(final String ch) {
    return "}".equals(ch) || "]".equals(ch) || ")".equals(ch);
  }

  /**
   * 将字符串解析成CalculatorSymbol
   *
   * @param ch 符号字符串
   * @return Symbol
   */
  public static Symbol parse(final char ch) {
    return Symbol.parse(ch + "");
  }

  /**
   * 获取优先级
   *
   * @return 数值
   * @see Integer#MAX_VALUE 最大值
   * @see Integer#MIN_VALUE 最小值
   */
  public int getPriority() {
    return this.priority;
  }

  public BigDecimal compute(final BigDecimal a, final BigDecimal b) {
    return this.computeExec.apply(a, b);
  }

  /**
   * 判断该符号的优先级是否等于另一个符号
   *
   * @param symbol 另一个符号
   * @return true为等于，否则为false
   */
  public boolean isEq(final Symbol symbol) {
    return this.priority == symbol.priority;
  }

  /**
   * 判断该符号的优先级是否大于另一个符号
   *
   * @param symbol 另一个符号
   * @return true为大于，否则为false
   */
  public boolean isGt(final Symbol symbol) {
    return this.priority > symbol.priority;
  }

  /**
   * 判断该符号的优先级是否小于另一个符号
   *
   * @param symbol 另一个符号
   * @return true为小于，否则为false
   */
  public boolean isLt(final Symbol symbol) {
    return this.priority < symbol.priority;
  }
}

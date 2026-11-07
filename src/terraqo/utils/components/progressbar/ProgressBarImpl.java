/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.components.progressbar;

import com.utils.UtilsOfJudge;
import axf.java.common.utils.UtilsOfString;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 进度条实现
 *
 * @author : Alfred X Fu
 * @date : 2020/4/17 10:18
 */
class ProgressBarImpl implements ProgressBar {

  /**
   * 完成
   */
  private static final double FINISH = 1d;
  /**
   * 一位
   */
  private static final int ONE_BIT = 10;
  /**
   * 两位
   */
  private static final int TWO_BIT = 100;
  /**
   * 总计字符
   */
  private static final int FIX_CHAR_COUNT = 105;
  /**
   * 全部进度
   */
  private static final int FULL_PROGRESS_COUNT = 100;
  /**
   * 未完成字符
   */
  private static final char UNFINISHED_CHAR = ' ';
  /**
   * 默认完成字符
   */
  private static final char DEFAULT_FINISH_CHAR = '=';
  /**
   * 左括号
   */
  private static final char LEFT_BRACKET = '[';
  /**
   * 右括号
   */
  private static final char RIGHT_BRACKET = ']';
  /**
   * 变化字符
   */
  private static final char[] DEFAULT_CHANGE_CHARS = {'\\', '—', '/'};
  /**
   * 输出流
   */
  private final PrintStream printStream;
  /**
   * 填充字符
   */
  private final char finishChar;
  /**
   * 变化字符
   */
  private final char[] changeChars;
  /**
   * 改变字符数字统计
   */
  private final AtomicInteger changeCharCount = new AtomicInteger(0);
  /**
   * 是否第一次显示
   */
  private final AtomicBoolean isFirstShow = new AtomicBoolean(true);
  /**
   * 是否完成显示
   */
  private final AtomicBoolean isFinishShow = new AtomicBoolean(false);

  public ProgressBarImpl() {
    this(System.out, DEFAULT_FINISH_CHAR, DEFAULT_CHANGE_CHARS);
  }

  public ProgressBarImpl(PrintStream printStream, char finishChar, char... changeChars) {
    this.printStream = printStream;
    this.finishChar = finishChar;
    this.changeChars = UtilsOfJudge.ifEmpty(changeChars, DEFAULT_CHANGE_CHARS);
  }

  /**
   * 显示
   */
  @Override
  public void show() {
    update(0);
  }

  /**
   * 显示进度条
   *
   * @param value 值，范围在0-1之间
   */
  @Override
  public void update(double value) {
    if (!(value >= 0 && value <= 1)) {
      throw new IllegalArgumentException("Value must between 0 and 1");
    }
    if (value == FINISH) {
      if (isFinishShow.get()) {
        return;
      }
      isFinishShow.set(true);
    }
    String result = getProcess(value);
    String backspace = getBackspace(value);
    boolean isNoFirstShow = !isFirstShow.get();
    if (isNoFirstShow) {
      printStream.print(backspace);
    } else {
      isFirstShow.set(false);
    }
    printStream.print(result);
  }

  public PrintStream getPrintStream() {
    return printStream;
  }

  private String getProcess(double value) {
    int count = (int) (FULL_PROGRESS_COUNT * value);
    int emptyCount = FULL_PROGRESS_COUNT - count;
    if (value != FINISH) {
      final String finished = UtilsOfString.repeat(finishChar, count - 1);
      final String unfinished = UtilsOfString.repeat(UNFINISHED_CHAR, emptyCount);
      final String change = getChangeChar();
      return
          LEFT_BRACKET + finished + change + unfinished + RIGHT_BRACKET + getPercentage(count);
    }
    final String finished = UtilsOfString.repeat(finishChar, count);
    return LEFT_BRACKET + finished + RIGHT_BRACKET + getPercentage(count);
  }

  private String getPercentage(int value) {
    return " " + value + "%";
  }

  private String getChangeChar() {
    int index = changeCharCount.incrementAndGet();
    if (index < 0) {
      changeCharCount.set(0);
      index = 0;
    }
    index = index % changeChars.length;
    return changeChars[index] + "";
  }

  private String getBackspace(double value) {
    int count = FIX_CHAR_COUNT;
    int percentage = (int) (value * FULL_PROGRESS_COUNT);
    if (percentage < ONE_BIT) {
      count += 1;
    } else if (percentage < TWO_BIT) {
      count += 2;
    } else {
      count += 3;
    }
    return UtilsOfString.repeat('\b', count);
  }
}

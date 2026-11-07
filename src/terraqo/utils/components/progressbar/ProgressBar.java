/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.components.progressbar;

import com.utils.UtilsOfThread;
import java.io.PrintStream;
import java.util.function.Supplier;

/**
 * 进度条
 *
 * @author : Alfred X Fu
 * @date : 2020/4/17 10:14
 */
public interface ProgressBar {

  /**
   * 完成值
   */
  double FINISH_VALUE = 1d;
  /**
   * 刷新间隔
   */
  long FLUSH_INTERVAL = 50;

  /**
   * 构建默认进度条
   *
   * @return 构建默认{@code ProgressBar}
   */
  static ProgressBar build() {
    return new ProgressBarImpl();
  }

  /**
   * 构建进度条
   *
   * @param out         打印流
   * @param finishChar  完成字符
   * @param changeChars 变化字符
   * @return 构建默认{@code ProgressBar}
   */
  static ProgressBar build(PrintStream out, char finishChar, char... changeChars) {
    return new ProgressBarImpl(out, finishChar, changeChars);
  }

  /**
   * 构建显示任务
   *
   * @param supplier 进度提供器
   * @return {@code Runnable}
   */
  static Runnable buildDisplayTask(Supplier<Double> supplier) {
    return buildDisplayTask(build(), supplier, FLUSH_INTERVAL);
  }

  /**
   * 构建显示任务
   *
   * @param progressBar   进度条
   * @param supplier      进度提供器
   * @param flushInterval 更新间隔
   * @return {@code Runnable}
   */
  static Runnable buildDisplayTask(ProgressBar progressBar, Supplier<Double> supplier, long flushInterval) {
    return () -> {
      progressBar.show();
      while (true) {
        double d = supplier.get();
        progressBar.update(d);
        UtilsOfThread.sleep(flushInterval);
        if (d == FINISH_VALUE) {
          break;
        }
      }
      if (progressBar instanceof ProgressBarImpl) {
        ((ProgressBarImpl) progressBar).getPrintStream().println();
      }
    };
  }

  /**
   * 显示
   */
  void show();

  /**
   * 显示进度条
   *
   * @param value 值，范围在0-1之间
   */
  void update(double value);
}

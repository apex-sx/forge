/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import java.io.Closeable;

/**
 * 资源工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 16:55
 */
public final class UtilsOfResource {

  /**
   * 关闭可关闭对象
   *
   * @param closeables 可关闭对象
   */
  public static void close(Closeable... closeables) {
    if (null == closeables) {
      return;
    }
    for (Closeable closeable : closeables) {
      if (null == closeable) {
        continue;
      }
      try {
        closeable.close();
      } catch (Exception ignored) {
      }
    }
  }
}

/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.filereceiver;

import axf.java.common.utils.function.Radio;
import java.io.Closeable;
import java.net.URL;

/**
 * 文件接受器
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 12:34
 */
public interface FileReceiver extends Radio, Closeable {

  /**
   * 下载
   *
   * @param from 从哪里下载
   * @param to   保存到哪里
   */
  void download(URL from, String to);
}

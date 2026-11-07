/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.filestorage;

import axf.java.common.utils.function.Updated;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 文件储存组件
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 10:22
 */
public interface FileStorage extends Updated {

  /**
   * 储存
   *
   * @param diskOffset 磁盘偏移地址
   * @param data       数据，所有数据
   * @return 返回写入数据长度
   * @throws IOException 当储存文件出错时抛出
   */
  int store(long diskOffset, ByteBuffer data) throws IOException;

  /**
   * 储存，从该文件磁盘0位置开始储存
   *
   * @param data 数据，所有数据
   * @return 返回写入数据长度
   * @throws IOException 当储存文件出错时抛出
   */
  default int store(ByteBuffer data) throws IOException {
    return store(0, data);
  }

  /**
   * 储存，从该文件磁盘0位置开始储存
   *
   * @param data 数据，所有数据
   * @return 返回写入数据长度
   * @throws IOException 当储存文件出错时抛出
   */
  default int store(byte[] data) throws IOException {
    return store(0, data);
  }

  /**
   * 储存
   *
   * @param diskOffset 磁盘偏移地址
   * @param data       数据，所有数据
   * @return 返回写入数据长度
   * @throws IOException 当储存文件出错时抛出
   */
  default int store(long diskOffset, byte[] data) throws IOException {
    return store(diskOffset, data, 0, data.length);
  }

  /**
   * 储存，从该文件磁盘0位置开始储存
   *
   * @param data   数据
   * @param offset 数据偏移位置
   * @param length 数据长度
   * @return 返回写入数据长度
   * @throws IOException 当储存文件出错时抛出
   */
  default int store(byte[] data, int offset, int length) throws IOException {
    return store(0, data, offset, length);
  }

  /**
   * 储存
   *
   * @param diskOffset 磁盘偏移地址
   * @param data       数据
   * @param offset     数据偏移位置
   * @param length     数据长度
   * @return 返回写入数据长度
   * @throws IOException 当储存文件出错时抛出
   */
  default int store(long diskOffset, byte[] data, int offset, int length) throws IOException {
    return store(offset, ByteBuffer.wrap(data, offset, length));
  }
}

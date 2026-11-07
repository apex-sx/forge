/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.filestorage;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 分段存储线程缓冲
 * <p>
 * 这不是线程安全的，每个线程需要有一个实例
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 12:18
 */
public class FragmentationThreadBuffer implements FileStorage {

  /**
   * 默认缓存大小
   */
  private static final int DEFAULT_CACHE_SIZE = 1024 * 1024;
  /**
   * 磁盘起始偏移地址
   */
  private final long diskStartOffset;
  /**
   * 上界
   */
  private final long upperBound;
  /**
   * 分段储存
   */
  private final FileStorage storage;
  /**
   * 缓存
   */
  private final ByteBuffer cache;
  /**
   * 写入磁盘偏移地址
   */
  private long diskOffset;
  /**
   * 缓存偏移地址
   */
  private int offset;

  public FragmentationThreadBuffer(FileStorage fileStorage, long diskOffset, long upperBound) {
    this(fileStorage, diskOffset, upperBound, DEFAULT_CACHE_SIZE);
  }

  public FragmentationThreadBuffer(FileStorage fileStorage, long diskOffset, long upperBound, int cacheSize) {
    this.storage = fileStorage;
    this.diskStartOffset = diskOffset;
    this.upperBound = upperBound;
    this.cache = ByteBuffer.allocate(cacheSize);
    this.diskOffset = this.diskStartOffset;
    this.offset = 0;
  }

  @Override
  public int store(long diskOffset, ByteBuffer data) throws IOException {
    int length = data.position();
    final int capacity = data.capacity();
    // 当前缓冲区已满，或者剩余容量不够容纳新数据
    if (offset + length > capacity || length == capacity) {
      // 将缓冲区中的数据写入文件
      flush();
    }
    cache.position(offset);
    data.flip();
    cache.put(data);
    offset += length;
    return length;
  }

  @Override
  public void flush() throws IOException {
    int length;
    cache.flip();
    length = storage.store(diskOffset, cache);
    cache.clear();
    diskOffset += length;
    offset = 0;
  }

  @Override
  public void close() throws IOException {
    if (diskOffset < upperBound) {
      flush();
    }
  }

  public void afresh() {
    cache.clear();
    diskOffset = diskStartOffset;
    offset = 0;
  }
}

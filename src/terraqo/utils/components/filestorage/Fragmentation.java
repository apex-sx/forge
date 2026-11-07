/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.filestorage;

import com.utils.UtilsOfFile;
import com.utils.UtilsOfNumber;
import com.utils.UtilsOfResource;
import axf.java.common.utils.exception.ImpossibleOccurException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 分段存储文件
 * <p>
 * 这是线程安全的，一般来说这个共享在每个线程中
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 10:35
 */
public class Fragmentation implements FileStorage {

  /**
   * 随机访问文件
   */
  private final RandomAccessFile storeFile;
  /**
   * 文件管道
   */
  private final FileChannel storeChanel;
  /**
   * 文件位置
   */
  private final Path path;
  /**
   * 总文件大小
   */
  private final long fileSize;
  /**
   * 总计写入
   */
  private final AtomicLong totalWrites = new AtomicLong(0);

  private Fragmentation(long fileSize, Path path, RandomAccessFile storeFile) {
    this.fileSize = fileSize;
    this.path = path;
    this.storeFile = storeFile;
    this.storeChanel = storeFile.getChannel();
  }

  public static Fragmentation build(final Path savePath, final long fileSize) {
    Path newSavePath = UtilsOfFile.createFileIfExistRenameAndSpecifySize(savePath, fileSize);
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(savePath.toFile(), "rw");
      return new Fragmentation(fileSize, newSavePath, randomAccessFile);
    } catch (FileNotFoundException ignored) {
      throw new ImpossibleOccurException();
    }
  }

  @Override
  public int store(long diskOffset, ByteBuffer data) throws IOException {
    int length = data.limit();
    storeChanel.write(data, diskOffset);
    totalWrites.addAndGet(length);
    return length;
  }

  @Override
  public void flush() throws IOException {
  }

  @Override
  public void close() throws IOException {
    UtilsOfResource.close(storeFile, storeChanel);
  }

  public Path getPath() {
    return path;
  }

  public long getWritten() {
    return totalWrites.get();
  }

  public double getRatio() {
    return UtilsOfNumber.div(getWritten(), fileSize, Double.class);
  }
}

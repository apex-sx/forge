/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.filereceiver;

import com.utils.UtilsOfAssert;
import com.utils.UtilsOfResource;
import com.utils.com.basic.TaskManager;
import com.utils.components.filestorage.FileStorage;
import com.utils.components.filestorage.Fragmentation;
import axf.java.common.utils.function.Pair;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * 多线程下载任务分发管理器
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 21:54
 */
class MultiplyThreadingDownloadTaskManager extends TaskManager {

  /**
   * 请求URL
   */
  private final URL requestURL;
  /**
   * 储存
   */
  private final FileStorage fileStorage;
  /**
   * 文件大小
   */
  private final long fileSize;

  public MultiplyThreadingDownloadTaskManager(Thread mainTaskThread, int taskCount, URL requestURL, FileStorage fileStorage,
      long fileSize) {
    super(mainTaskThread, taskCount);
    this.requestURL = requestURL;
    this.fileStorage = fileStorage;
    this.fileSize = fileSize;
  }

  public MultiplyThreadingDownloadTaskManager(Thread mainTaskThread, int taskCount, long awaitTime, TimeUnit timeUnit, URL requestURL,
      FileStorage fileStorage,
      long fileSize) {
    super(mainTaskThread, taskCount, awaitTime, timeUnit);
    this.requestURL = requestURL;
    this.fileStorage = fileStorage;
    this.fileSize = fileSize;
  }

  public URL getRequestURL() {
    return requestURL;
  }

  public FileStorage getFileStorage() {
    return fileStorage;
  }

  public long getFileSize() {
    return fileSize;
  }

  public Pair<Long, Long> getDownloadRange(int taskIndex) {
    int subTaskCount = getSubTaskCount();
    UtilsOfAssert.checkGreaterEqual(taskIndex, 0, "Task index don't allow lesser than 0");
    UtilsOfAssert.checkLesser(taskIndex, subTaskCount, "Task index more than total task count for " + subTaskCount);
    long block = Math.floorDiv(fileSize, subTaskCount);
    long start = Math.multiplyExact(block, taskIndex);
    long end = taskIndex == subTaskCount - 1 ? fileSize : Math.multiplyExact(block, taskIndex + 1) - 1;
    return Pair.of(start, end);
  }

  @Override
  public double tempo() {
    if (fileStorage instanceof Fragmentation) {
      return ((Fragmentation) fileStorage).getRatio();
    }
    return super.tempo();
  }

  @Override
  protected void doCancel() {
    UtilsOfResource.close(fileStorage);
  }

  @Override
  public void doCleanUp() {
    UtilsOfResource.close(fileStorage);
  }
}
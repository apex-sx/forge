/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.filereceiver;

/**
 * @author : Alfred X Fu
 * @date : 2020/4/18 21:56
 */

import com.utils.UtilsOfHttp;
import com.utils.UtilsOfResource;
import com.utils.com.basic.TaskManager;
import com.utils.components.filestorage.FileStorage;
import com.utils.components.filestorage.FragmentationThreadBuffer;
import axf.java.common.utils.function.SubTask;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * 下载子任务
 *
 * @author Viscent Huang
 */
class MultiplyThreadingDownloadSubTask implements SubTask {

  private final MultiplyThreadingDownloadTaskManager taskManager;
  private final FileStorage buffer;
  private final long lowerBound;
  private final long upperBound;

  public MultiplyThreadingDownloadSubTask(MultiplyThreadingDownloadTaskManager taskManager, long lowerBound, long upperBound) {
    this.taskManager = taskManager;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
    this.buffer = new FragmentationThreadBuffer(taskManager.getFileStorage(), lowerBound, upperBound);
  }

  @Override
  public void run() {
    if (taskManager.isCanceled()) {
      return;
    }
    ReadableByteChannel channel = null;
    try {
      InputStream inputStream = buildInputStream();
      channel = Channels.newChannel(inputStream);
      ByteBuffer buf = ByteBuffer.allocate(1024);
      while (!taskManager.isCanceled() && channel.read(buf) > 0) {
        // 将从网络读取的数据写入缓冲区
        buffer.store(buf);
        buf.clear();
      }
      taskManager.handUp();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      UtilsOfResource.close(channel, buffer);
    }
  }

  @Override
  public TaskManager getTaskManager() {
    return taskManager;
  }

  protected InputStream buildInputStream() throws IOException {
    return UtilsOfHttp.issueInputStream(taskManager.getRequestURL(), lowerBound, upperBound);
  }

  public long getLowerBound() {
    return lowerBound;
  }

  public long getUpperBound() {
    return upperBound;
  }
}

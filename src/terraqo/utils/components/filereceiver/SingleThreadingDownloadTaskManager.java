/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.filereceiver;

import com.utils.components.filestorage.FileStorage;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * 单线程下载任务分发管理器
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 21:54
 */
class SingleThreadingDownloadTaskManager extends MultiplyThreadingDownloadTaskManager {

  public SingleThreadingDownloadTaskManager(Thread mainTaskThread, URL requestURL, FileStorage fileStorage, long fileSize) {
    super(mainTaskThread, 1, requestURL, fileStorage, fileSize);
  }

  public SingleThreadingDownloadTaskManager(Thread mainTaskThread, long awaitTime, TimeUnit timeUnit, URL requestURL,
      FileStorage fileStorage, long fileSize) {
    super(mainTaskThread, 1, awaitTime, timeUnit, requestURL, fileStorage, fileSize);
  }
}
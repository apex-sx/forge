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
import java.io.IOException;
import java.io.InputStream;

/**
 * 下载子任务
 *
 * @author Viscent Huang
 */
class SingleThreadingDownloadSubTask extends MultiplyThreadingDownloadSubTask {

  public SingleThreadingDownloadSubTask(SingleThreadingDownloadTaskManager taskManager, long fileSize) {
    super(taskManager, 0, fileSize);
  }

  @Override
  protected InputStream buildInputStream() throws IOException {
    SingleThreadingDownloadTaskManager taskManager = (SingleThreadingDownloadTaskManager) getTaskManager();
    return UtilsOfHttp.issueInputStream(taskManager.getRequestURL());
  }
}

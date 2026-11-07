/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.components.filereceiver;

import com.utils.UtilsOfHttp;
import com.utils.UtilsOfJudge;
import com.utils.com.basic.TaskManager;
import com.utils.components.filestorage.Fragmentation;
import axf.java.common.utils.function.Pair;
import axf.java.common.utils.function.SubTask;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Supplier;

/**
 * 多线程本地文件接收器
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 12:39
 */
public class SingleThreadingLocalStorageFileReceiver extends MultiplyThreadingLocalStorageFileReceiver {

  public SingleThreadingLocalStorageFileReceiver(
      Supplier<ThreadPoolExecutor> threadPoolExecutorSupplier) {
    super(threadPoolExecutorSupplier);
  }

  @Override
  protected TaskManager buildTaskManager(URL from, String to) {
    final Thread current = Thread.currentThread();
    TaskManager manager = taskManagers.get(current);
    if (UtilsOfJudge.isNull(manager) || manager.isFinish()) {
      long fileSize = UtilsOfHttp.resolveFileSize(from);
      Fragmentation storage = Fragmentation.build(Path.of(to), fileSize);
      manager = new SingleThreadingDownloadTaskManager(current, from, storage, fileSize);
      taskManagers.put(current, manager);
    }
    return manager;
  }

  @Override
  protected List<SubTask> buildSubTask(TaskManager taskManager) {
    SingleThreadingDownloadTaskManager manager = (SingleThreadingDownloadTaskManager) taskManager;
    Pair<Long, Long> range = manager.getDownloadRange(0);
    return List.of(new SingleThreadingDownloadSubTask(manager, manager.getFileSize()));
  }
}

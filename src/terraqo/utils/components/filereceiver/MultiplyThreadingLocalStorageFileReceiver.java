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
import com.utils.UtilsOfThread;
import com.utils.com.basic.TaskManager;
import com.utils.components.filestorage.Fragmentation;
import axf.java.common.utils.function.Pair;
import axf.java.common.utils.function.SubTask;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 多线程本地文件接收器
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 12:39
 */
public class MultiplyThreadingLocalStorageFileReceiver implements FileReceiver {

  private static final Logger LOGGER = LoggerFactory.getLogger(MultiplyThreadingLocalStorageFileReceiver.class);
  /**
   * 清理任务休息时间
   */
  public static final long CLEAN_TASK_REST_TIME = 1000;
  /**
   * 下载任务
   */
  protected final Map<Thread, TaskManager> taskManagers = new ConcurrentHashMap<>(2);
  /**
   * 线程池
   */
  protected final ThreadPoolExecutor threadPoolExecutor;
  /**
   * 关闭信号
   */
  private final AtomicBoolean closedSignal = new AtomicBoolean(false);
  /**
   * 子任务个数
   */
  private final int subTaskCount;

  public MultiplyThreadingLocalStorageFileReceiver(Supplier<ThreadPoolExecutor> threadPoolExecutorSupplier) {
    this(2, threadPoolExecutorSupplier);
  }

  public MultiplyThreadingLocalStorageFileReceiver(int subTaskCount, Supplier<ThreadPoolExecutor> threadPoolExecutorSupplier) {
    this.subTaskCount = subTaskCount;
    this.threadPoolExecutor = threadPoolExecutorSupplier.get();
    this.threadPoolExecutor.execute(new CleanTask());
  }

  @Override
  public void download(URL from, String to) {
    checkCloseSignal();
    TaskManager manager = buildTaskManager(from, to);
    List<SubTask> subTaskList = buildSubTask(manager);
    subTaskList.forEach(this::dispatchWork);
  }

  @Override
  public double tempo() {
    checkCloseSignal();
    Thread current = Thread.currentThread();
    TaskManager manager = checkAndGetTaskManager();
    return manager.tempo();
  }

  @Override
  public void close() throws IOException {
    closedSignal.compareAndSet(false, true);
  }

  protected TaskManager buildTaskManager(URL from, String to) {
    final Thread current = Thread.currentThread();
    TaskManager manager = taskManagers.get(current);
    if (UtilsOfJudge.isNull(manager) || manager.isFinish()) {
      long fileSize = UtilsOfHttp.resolveFileSize(from);
      Fragmentation storage = Fragmentation.build(Path.of(to), fileSize);
      manager = new MultiplyThreadingDownloadTaskManager(current, subTaskCount, from, storage, fileSize);
      taskManagers.put(current, manager);
    }
    return manager;
  }

  protected List<SubTask> buildSubTask(TaskManager taskManager) {
    MultiplyThreadingDownloadTaskManager manager = (MultiplyThreadingDownloadTaskManager) taskManager;
    List<SubTask> subTaskList = new ArrayList<>(manager.getSubTaskCount());
    IntStream.range(0, manager.getSubTaskCount()).forEach(i -> {
      Pair<Long, Long> range = manager.getDownloadRange(i);
      subTaskList.add(new MultiplyThreadingDownloadSubTask(manager, range.first(), range.second()));
    });
    return subTaskList;
  }

  public TaskManager getTaskManager() {
    checkCloseSignal();
    return checkAndGetTaskManager();
  }

  private TaskManager checkAndGetTaskManager() {
    Thread current = Thread.currentThread();
    if (!taskManagers.containsKey(current)) {
      throw new RuntimeException("The current thread has no task");
    }
    return taskManagers.get(current);
  }

  private void checkCloseSignal() {
    if (closedSignal.get()) {
      throw new RuntimeException("File receiver already closed");
    }
  }

  private void dispatchWork(final SubTask subTask) {
    threadPoolExecutor.execute(() -> {
      try {
        subTask.run();
      } catch (Exception e) {
        e.printStackTrace();
        TaskManager taskManager = subTask.getTaskManager();
        taskManager.cancel();
      }
    });
  }

  private class CleanTask implements Runnable {

    @Override
    public void run() {
      while (!isClosed()) {
        UtilsOfThread.sleep(CLEAN_TASK_REST_TIME);
        Set<Entry<Thread, TaskManager>> set = taskManagers.entrySet();
        Iterator<Entry<Thread, TaskManager>> iterator = set.iterator();
        while (iterator.hasNext()) {
          Entry<Thread, TaskManager> entry = iterator.next();
          Thread thread = entry.getKey();
          TaskManager manager = entry.getValue();
          if (UtilsOfJudge.isTerminated(thread) && manager.isFinish()) {
            manager.cleanup();
            iterator.remove();
          }
        }
      }
      LOGGER.debug("Clean close!");
    }

    /**
     * 是否关闭
     *
     * @return true为关闭否则为false
     */
    private boolean isClosed() {
      return closedSignal.get() && taskManagers.isEmpty();
    }
  }
}

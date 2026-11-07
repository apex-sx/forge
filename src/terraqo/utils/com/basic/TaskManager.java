/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/

package com.utils.com.basic;

import com.utils.UtilsOfNumber;
import axf.java.common.utils.exception.OvertimeException;
import axf.java.common.utils.function.Task;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 任务管理
 * <p>
 * 这是线程安全的，相当于是多线程管理器。
 * <p>
 * 一个任务划分为多个子任务 每一个子任务都在一个线程里工作，并且他们共享一个管理器
 *
 * @author : Alfred X Fu
 * @date : 2020/4/18 15:30
 */
public class TaskManager implements Task, Comparable<TaskManager> {

  /**
   * 默认等待时间
   */
  protected static final long DEFAULT_AWAIT_TIME = 10;
  /**
   * 创建时间
   */
  private final long createTime = System.currentTimeMillis();
  /**
   * 分派任务的线程
   */
  private final Thread dispatchThread;
  /**
   * 任务是否停止
   */
  private final AtomicBoolean canceled;
  /**
   * 子任务数量
   */
  private final int subTaskCount;
  /**
   * 统计是否完成
   */
  private final CountDownLatch taskStatics;
  /**
   * 等待时间
   */
  private final long awaitTime;
  /**
   * 时间单位
   */
  private final TimeUnit timeUnit;

  public TaskManager(Thread dispatchThread, int subTaskCount) {
    this(dispatchThread, subTaskCount, DEFAULT_AWAIT_TIME, TimeUnit.MINUTES);
  }

  public TaskManager(Thread dispatchThread, int subTaskCount, long awaitTime, TimeUnit timeUnit) {
    this.dispatchThread = dispatchThread;
    this.subTaskCount = subTaskCount;
    this.awaitTime = awaitTime;
    this.timeUnit = timeUnit;
    this.canceled = new AtomicBoolean(false);
    this.taskStatics = new CountDownLatch(subTaskCount);
  }

  @Override
  public final Thread getDispatchThread() {
    return dispatchThread;
  }

  @Override
  public void handUp() {
    taskStatics.countDown();
  }

  @Override
  public int getSubTaskCount() {
    return subTaskCount;
  }

  @Override
  public boolean isFinish() {
    return taskStatics.getCount() == 0;
  }

  @Override
  public final boolean isCanceled() {
    return canceled.get();
  }

  @Override
  public final void cancel() {
    if (canceled.compareAndSet(false, true)) {
      doCancel();
    }
  }

  @Override
  public double tempo() {
    int finished = UtilsOfNumber.sub(subTaskCount, taskStatics.getCount(), Integer.class);
    return UtilsOfNumber.div(finished, subTaskCount, Double.class);
  }

  @Override
  public final void cleanup() {
    if (isCanceled()) {
      return;
    }
    if (isFinish()) {
      doCleanUp();
      return;
    }
    try {
      taskStatics.await(awaitTime, timeUnit);
    } catch (InterruptedException e) {
      throw new OvertimeException("Don't completed within the stipulated time", e);
    }
  }

  protected void doCancel() {

  }

  public void doCleanUp() {

  }

  @Override
  public int compareTo(TaskManager other) {
    long diff = Math.subtractExact(createTime, other.createTime);
    if (diff == 0) {
      return 0;
    }
    return diff < 0 ? -1 : 1;
  }
}

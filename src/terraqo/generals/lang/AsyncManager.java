/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.utils.GeneralThread;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 异步任务管理器
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public class AsyncManager {
    public static final AtomicReference<AsyncManager> GLOBAL = new AtomicReference<>();

    public static AsyncManager global() {
        AsyncManager value = GLOBAL.get();
        if (value == null) {
            throw new RuntimeException("No set global 'AsyncManager' instance");
        }
        return value;
    }

    /**
     * 操作延迟10毫秒
     */
    private final int delayExecuteSecond;
    /**
     * 异步操作任务调度线程池
     */
    private final ScheduledExecutorService scheduledExecutorService;

    public AsyncManager(ScheduledExecutorService scheduledExecutorService, int delayExecuteSecond) {
        this.scheduledExecutorService = Objects.requireNonNull(scheduledExecutorService);
        this.delayExecuteSecond = Math.max(delayExecuteSecond, 5);
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task) {
        scheduledExecutorService.schedule(task, delayExecuteSecond, TimeUnit.MILLISECONDS);
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public <V> ScheduledFuture<V> execute(Callable<V> task) {
        return scheduledExecutorService.schedule(task, delayExecuteSecond, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        GeneralThread.shutdownAndAwaitTermination(scheduledExecutorService);
    }
}

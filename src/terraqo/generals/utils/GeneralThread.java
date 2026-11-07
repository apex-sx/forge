/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;


import com.github.potatoxf.generals.Log;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author potatoxf
 */
public interface GeneralThread {

    /**
     * 休眠
     *
     * @param millis 毫秒数
     */
    static void sleep(long millis) {
        try {
            if (Log.isEnabledTrace()) {
                Thread thread = Thread.currentThread();
                Log.trace("The [" + thread + "] going to ready to rest " + millis + " millisecond");
            }
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            if (Log.isEnabledError()) {
                Thread thread = Thread.currentThread();
                Log.error("The [" + thread + "] was interrupted when it is resting", e);
            }
        }
    }

    /**
     * Sleeps for a guaranteed minimum duration unless interrupted.
     * <p>
     * This method exists because Thread.sleep(100) can sleep for 0, 70, 100 or 200ms or anything else it deems appropriate. Read
     * {@link Thread#sleep(long, int)}} for further interesting details.
     * </p>
     *
     * @param duration the sleep duration.
     * @throws InterruptedException if interrupted
     * @see Thread#sleep(long, int)
     */
    static void sleep(final Duration duration) throws InterruptedException {
        // Using this method avoids depending on the vagaries of the precision and accuracy of system timers and schedulers.
        final Instant finishInstant = Instant.now().plus(duration);
        Duration remainingDuration = duration;
        do {
            Thread.sleep(remainingDuration.toMillis(), remainingDuration.getNano() % 1_000_000);
            remainingDuration = Duration.between(Instant.now(), finishInstant);
        } while (!remainingDuration.isNegative());
    }

    /**
     * 休眠直到
     *
     * @param startTime 开始时间
     * @param millis    毫秒数
     */
    static void sleepUntil(long startTime, long millis) {
        long now = System.currentTimeMillis();
        long until = startTime + millis;
        Log.trace("Sleeping until %s", until);
        if (now < until) {
            GeneralThread.sleep(until - now);
        }
    }

    /**
     * 随机休眠
     *
     * @param maxMillis 最大毫秒数
     * @return 返回随机休眠时间
     */
    static int sleepWithRandom(int maxMillis) {
        return GeneralThread.sleepWithRandom(0, maxMillis);
    }

    /**
     * 随机休眠
     *
     * @param maxMillisecond 最大毫秒数
     * @param minMillisecond 最小毫秒数
     */
    static int sleepWithRandom(int minMillisecond, int maxMillisecond) {
        int minMillis = Math.max(Math.min(minMillisecond, maxMillisecond), 0);
        int maxMillis = Math.max(Math.max(minMillisecond, maxMillisecond), 0);
        int sleepTime;
        if (maxMillis == minMillis) {
            sleepTime = minMillis;
        } else {
            sleepTime = ThreadLocalRandom.current().nextInt(Math.abs(maxMillisecond - minMillis)) + minMillis;
        }
        GeneralThread.sleep(sleepTime);
        return sleepTime;
    }

    /**
     * 停止线程池。先使用shutdown, 停止接收新任务并尝试完成所有已存在任务。
     * 如果超时，则调用shutdownNow, 取消在workQueue中Pending的任务并中断所有阻塞函数。
     * 如果仍人超時，則強制退出。另对在shutdown时线程本身被调用中断做了处理.
     *
     * @param pool 线程池
     */
    static void shutdownAndAwaitTermination(ExecutorService pool) {
        GeneralThread.shutdownAndAwaitTermination(pool, 120);
    }

    /**
     * 停止线程池。先使用shutdown, 停止接收新任务并尝试完成所有已存在任务。
     * 如果超时，则调用shutdownNow, 取消在workQueue中Pending的任务并中断所有阻塞函数。
     * 如果仍人超時，則強制退出。另对在shutdown时线程本身被调用中断做了处理.
     *
     * @param pool           线程池
     * @param timeoutSeconds 超时秒数
     */
    static void shutdownAndAwaitTermination(ExecutorService pool, int timeoutSeconds) {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(timeoutSeconds, TimeUnit.SECONDS)) {
                        Log.info("Pool did not terminate");
                    }
                }
            } catch (InterruptedException ie) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Starts monitoring the current thread.
     *
     * @param executorService The thread pool
     * @param timeout The timeout amount. or no timeout if the value is zero or less.
     */
    static void startThreadMonitor(ExecutorService executorService, Duration timeout) {
        GeneralThread.startThreadMonitor(executorService, Thread.currentThread(), timeout);
    }

    /**
     * Starts monitoring the specified thread.
     *
     * @param executorService The thread pool
     * @param thread          The thread to monitor
     * @param timeout         The timeout amount. or no timeout if the value is zero or less.
     */
    static void startThreadMonitor(ExecutorService executorService, Thread thread, Duration timeout) {
        if (timeout.isZero() || timeout.isNegative()) return;
        Runnable threadMonitor = GeneralThread.createThreadMonitor(thread, timeout);
        executorService.submit(threadMonitor);
    }

    /**
     * Monitors a thread, interrupting it if it reaches the specified timeout.
     * <p>
     * This works by sleeping until the specified timeout amount and then interrupting the thread being monitored. If the
     * thread being monitored completes its work before being interrupted, it should {@code interrupt()} the <em>monitor</em>
     * thread.
     * </p>
     *
     * <pre>
     * Duration timeout = Duration.ofSeconds(1);
     * try {
     *     Thread monitor = ThreadMonitor.start(timeout);
     *     // do some work here
     *     ThreadMonitor.stop(monitor);
     * } catch (InterruptedException e) {
     *     // timed amount was reached
     * }
     * </pre>
     *
     * @param thread  The thread to monitor
     * @param timeout The timeout amount. or no timeout if the value is zero or less.
     * @return Runnable
     */
    static Runnable createThreadMonitor(final Thread thread, final Duration timeout) {
        return () -> {
            try {
                //Sleeps until the specified timeout amount and then interrupt the thread being monitored.
                GeneralThread.sleep(timeout);
                thread.interrupt();
            } catch (final InterruptedException ignored) {
                // timeout not reached
            }
        };
    }
}

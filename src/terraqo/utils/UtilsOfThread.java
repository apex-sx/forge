/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 19:41
 */
public final class UtilsOfThread {

  private static final Logger LOGGER = LoggerFactory.getLogger(UtilsOfThread.class);
  private static final long MIN_TIME = 0;
  private static final long DEFAULT_SLEEP_TIME = 1000;

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public static void randomSleep(long maxMillis) {
    randomSleep(maxMillis, MIN_TIME);
  }

  public static void randomSleep(long maxMillis, long minMillis) {
    minMillis = UtilsOfJudge.ifElse(minMillis <= MIN_TIME, MIN_TIME, minMillis);
    maxMillis = UtilsOfJudge.ifElse(maxMillis <= MIN_TIME, DEFAULT_SLEEP_TIME, maxMillis);
    UtilsOfAssert.checkGreater(maxMillis, minMillis, "Max millisecond must greater than min millisecond or equal");
    long diff = Math.subtractExact(maxMillis, minMillis);
    int realDiff = (int) diff;
    UtilsOfAssert.checkEqual(diff, realDiff, "Real different more than range of int type");
    long sleepTime = maxMillis == minMillis ? minMillis : random().nextInt(realDiff) + minMillis;
    sleep(sleepTime);
  }

  public static void startThread(Thread... threads) {
    for (Thread t : threads) {
      t.start();
    }
  }

  public static void startAndWaitTerminated(Thread... threads) throws InterruptedException {
    for (Thread t : threads) {
      t.start();
    }
    for (Thread t : threads) {
      t.join();
    }
  }

  public static void startAndWaitTerminated(Iterable<Thread> threads) throws InterruptedException {
    for (Thread t : threads) {
      t.start();
    }
    for (Thread t : threads) {
      t.join();
    }
  }

  public static Runnable delayedAction(Runnable action, long delayMillis) {
    return () -> {
      sleep(delayMillis);
      action.run();
    };
  }

  private static Random random() {
    return ThreadLocalRandom.current();
  }
}

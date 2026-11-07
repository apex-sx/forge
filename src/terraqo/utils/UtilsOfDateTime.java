/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 时间工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 16:59
 */
public final class UtilsOfDateTime {

  public static final String DEFAULT_DATE_TiME_FORMAT = "yyyy-MM-dd hh:mm:ss";
  private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat();
  private static final Lock SIMPLE_DATE_FORMAT_LOCK = new ReentrantLock();

  public static String newDateStr(long epochMilli) {
    return newDateStr(epochMilli, DEFAULT_DATE_TiME_FORMAT);
  }

  public static String newDateStr(long epochMilli, String dateFormat) {
    return format(new Date(epochMilli), dateFormat);
  }

  public static String format(Date date, String pattern) {
    try {
      SimpleDateFormat simpleDateFormat = gainSimpleDateFormat(pattern);
      return simpleDateFormat.format(date);
    } finally {
      releaseSimpleDateFormat();
    }
  }

  public static SimpleDateFormat gainSimpleDateFormat(String pattern) {
    SIMPLE_DATE_FORMAT_LOCK.lock();
    SIMPLE_DATE_FORMAT.applyPattern(pattern);
    return SIMPLE_DATE_FORMAT;
  }

  public static void releaseSimpleDateFormat() {
    SIMPLE_DATE_FORMAT_LOCK.unlock();
  }

  public static Date parse(String datetime, String pattern) {
    SimpleDateFormat simpleDateFormat = gainSimpleDateFormat(pattern);
    try {
      return simpleDateFormat.parse(datetime);
    } catch (ParseException e) {
      e.printStackTrace();
      return new Date();
    } finally {
      releaseSimpleDateFormat();
    }
  }
}

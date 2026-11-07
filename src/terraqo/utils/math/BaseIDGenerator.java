/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils.math;

import com.utils.UtilsOfDateTime;

/**
 * ID生成器
 *
 * @author : Alfred X Fu
 * @date : 2020/2/6 17:15
 */
public abstract class BaseIDGenerator {

  /**
   * 实例化一个默认ID生成器
   *
   * @return ID生成器
   * @see Default
   */
  public static BaseIDGenerator newDefaultInstance() {
    return BaseIDGenerator.newDefaultInstance(0, 0);
  }

  /**
   * 实例化一个ID生成器
   *
   * @param workId 0-31的工作中心的id号
   * @param dataId 0-31的数据中心的id号
   * @return ID生成器
   * @see Default
   */
  public static BaseIDGenerator newDefaultInstance(final int workId, final int dataId) {
    if (workId > Default.MAX_WORKER_ID || workId < 0) {
      throw new IllegalArgumentException(
          String
              .format("Worker Id can't be greater than %d or less than 0", Default.MAX_WORKER_ID));
    }
    if (dataId > Default.MAX_DATA_CENTER_ID || dataId < 0) {
      throw new IllegalArgumentException(
          String.format("DataCenter Id can't be greater than %d or less than 0",
              Default.MAX_DATA_CENTER_ID));
    }
    return new Default(workId, dataId);
  }

  /**
   * 下个ID号
   *
   * @return long类型数字
   */
  public abstract long nextID();

  /**
   * 下一个根据ID生成的时间戳
   *
   * @return 时间戳
   */
  public abstract String nextIDString();

  /**
   * ID生成算法核心
   */
  public static final class Default extends BaseIDGenerator {
    //================================================Algorithm's Parameter=============================================
    /**
     * 系统开始时间截 (UTC 2017-06-28 00:00:00)
     */
    public static final long START_TIME = 1498608000000L;
    /**
     * 机器id所占的位数
     */
    public static final long WORKER_ID_BITS = 5L;
    /**
     * 数据标识id所占的位数
     */
    public static final long DATA_CENTER_ID_BITS = 5L;
    /**
     * 支持的最大机器id(十进制)，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) -1L左移5位 (worker id所占位数) 即5位二进制所能获得的最大十进制数 - 31
     */
    public static final long MAX_WORKER_ID = ~(-1L << Default.WORKER_ID_BITS);
    /**
     * 支持的最大数据标识id - 31
     */
    public static final long MAX_DATA_CENTER_ID = ~(-1L << Default.DATA_CENTER_ID_BITS);
    /**
     * 序列在id中占的位数
     */
    public static final long SEQUENCE_BITS = 12L;
    /**
     * 机器ID左移位数 - 12 (即末sequence所占用的位数)
     */
    public static final long WORKER_ID_MOVE_BITS = Default.SEQUENCE_BITS;
    /**
     * 数据标识id左移位数 - 17(12+5)
     */
    public static final long DATA_CENTER_ID_MOVE_BITS =
        Default.SEQUENCE_BITS + Default.WORKER_ID_BITS;
    /**
     * 时间截向 左移位数 - 22(5+5+12)
     */
    public static final long TIMESTAMP_MOVE_BITS =
        Default.SEQUENCE_BITS + Default.WORKER_ID_BITS + Default.DATA_CENTER_ID_BITS;
    /**
     * 生成序列的掩码(12位所对应的最大整数值)，这里为4095 (0b111111111111=0xfff=4095)
     */
    public static final long SEQUENCE_MASK = ~(-1L << Default.SEQUENCE_BITS);
    //=================================================Works's Parameter================================================
    /**
     * 工作机器ID(0~31)
     */
    private final long workerId;
    /**
     * 数据中心ID(0~31)
     */
    private final long dataCenterId;
    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;
    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;
    //===============================================Constructors=======================================================

    /**
     * 构造函数
     *
     * @param workId 工作ID (0~31)
     * @param dataId 数据中心ID (0~31)
     */
    public Default(final long workId, final long dataId) {
      this.workerId = workId;
      this.dataCenterId = dataId;
    }
// ===================================================Method========================================================

    /**
     * 线程安全的获得下一个ID的方法
     *
     * @return 返回long类型的ID
     */
    @Override
    public synchronized long nextID() {
      final long timestamp = this.generatorNewTime();
      //上次生成ID的时间截
      this.lastTimestamp = timestamp;
      //移位并通过或运算拼到一起组成64位的ID
      return ((timestamp - Default.START_TIME) << Default.TIMESTAMP_MOVE_BITS) | (this.dataCenterId
          << Default.DATA_CENTER_ID_MOVE_BITS) | (this.workerId << Default.WORKER_ID_MOVE_BITS)
          | this.sequence;
    }

    @Override
    public final synchronized String nextIDString() {
      return UtilsOfDateTime.newDateStr(this.nextID(), "yyyyMMddhhmmss");
    }

    private long generatorNewTime() {
      long timestamp = this.currentTime();
      //如果当前时间小于上一次ID生成的时间戳: 说明系统时钟回退过 - 这个时候应当抛出异常
      if (timestamp < this.lastTimestamp) {
        throw new RuntimeException(
            String.format(
                "Clock moved backwards.  Refusing DEFAULT_TO generate id for %d milliseconds",
                this.lastTimestamp - timestamp
            ));
      }
      //如果是同一时间生成的，则进行毫秒内序列
      if (this.lastTimestamp == timestamp) {
        this.sequence = (this.sequence + 1) & Default.SEQUENCE_MASK;
        //毫秒内序列溢出 即 序列 > 4095
        if (this.sequence == 0) {
          //阻塞到下一个毫秒,获得新的时间戳
          timestamp = this.blockTillNextMillis(this.lastTimestamp);
        }
      }
      //时间戳改变，毫秒内序列重置
      else {
        this.sequence = 0L;
      }
      return timestamp;
    }

    /**
     * 阻塞到下一个毫秒 即 直到获得新的时间戳
     *
     * @param lastTimestamp 上次时间戳
     * @return 获取下一个时间戳
     */
    private long blockTillNextMillis(final long lastTimestamp) {
      long timestamp = this.currentTime();
      while (timestamp <= lastTimestamp) {
        timestamp = this.currentTime();
      }
      return timestamp;
    }

    /**
     * 获得以毫秒为单位的当前时间
     *
     * @return 当前毫秒数
     */
    private long currentTime() {
      return System.currentTimeMillis();
    }
  }
}

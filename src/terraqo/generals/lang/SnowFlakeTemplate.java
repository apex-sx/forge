/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

/**
 * SnowFlake模板
 *
 * @author potatoxf
 */
public final class SnowFlakeTemplate {
    /**
     * 工作机器ID值
     */
    private final int workerIdValue;
    /**
     * 数据中心ID值
     */
    private final int centerIdValue;
    /**
     * 时间戳位移
     */
    private final int timestampShift;
    /**
     * 序列掩码
     */
    private final int sequenceMask;
    /**
     * 毫秒内序列
     */
    private int sequence = 0;
    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    /**
     * 构造函数
     *
     * @param centerIdBits 数据id所占的位数(1~5)
     * @param workerIdBits 机器id所占的位数(1~5)
     * @param sequenceBits 序列在id中占的位数(1~12)
     * @param centerId     中心ID，范围(0 ~ ~(-1 << centerIdBits))
     * @param workerId     工作ID，范围(0 ~ ~(-1 << workerIdBits))
     */
    public SnowFlakeTemplate(int centerIdBits, int workerIdBits, int sequenceBits, int centerId, int workerId) {
        if (centerIdBits <= 0 || centerIdBits > 5) {
            throw new IllegalArgumentException("The center id bits can't be greater than 5 or less than 0");
        }
        if (workerIdBits <= 0 || workerIdBits > 5) {
            throw new IllegalArgumentException("The center id bits can't be greater than 5 or less than 0");
        }
        if (sequenceBits <= 0 || sequenceBits > 12) {
            throw new IllegalArgumentException("The sequence bits can't be greater than 12 or less than 0");
        }
        if (centerId > ~(-1 << centerIdBits) || centerId < 0) {
            throw new IllegalArgumentException("The center id can't be greater than " + ~(-1 << centerIdBits) + " or less than 0");
        }
        if (workerId > ~(-1 << workerIdBits) || workerId < 0) {
            throw new IllegalArgumentException("The worker id can't be greater than " + ~(-1 << workerIdBits) + " or less than 0");
        }
        this.workerIdValue = workerId << sequenceBits;
        this.centerIdValue = centerId << (sequenceBits + workerIdBits);
        this.timestampShift = sequenceBits + workerIdBits + centerIdBits;
        this.sequenceMask = ~(-1 << sequenceBits);
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextLongId() {
        long l = Long.MAX_VALUE & (nextDiffTimestamp().longValue() << timestampShift);
        return l | (long) centerIdValue | (long) workerIdValue | sequence;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized int nextIntId() {
        int l = Integer.MAX_VALUE & (nextDiffTimestamp().intValue() << timestampShift);
        return l | centerIdValue | workerIdValue | sequence;
    }

    private Number nextDiffTimestamp() {
        long timestamp = System.currentTimeMillis();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                do timestamp = System.currentTimeMillis(); while (timestamp <= lastTimestamp);
            }
        } else {
            //时间戳改变，毫秒内序列重置
            sequence = 0;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;
        return timestamp - 1740000000000L;
    }
}
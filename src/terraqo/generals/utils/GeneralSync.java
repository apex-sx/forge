/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import java.util.concurrent.locks.StampedLock;
import java.util.function.Supplier;

/**
 * @author potatoxf
 */
public interface GeneralSync {

    /**
     * 乐观同步读取
     *
     * @param stampedLock cas锁
     * @param reader      读取动作
     * @param <T>         读取结果类型
     * @return 返回读取结果
     */
    static <T> T syncOptimisticRead(StampedLock stampedLock, Supplier<T> reader) {
        long l = stampedLock.tryOptimisticRead();
        T result;
        if (stampedLock.validate(l)) {
            result = reader.get();
        } else {
            l = stampedLock.readLock();
            try {
                result = reader.get();
            } finally {
                stampedLock.unlockRead(l);
            }
        }
        return result;
    }

    /**
     * 同步读取
     *
     * @param stampedLock cas锁
     * @param reader      读取动作
     * @param <T>         读取结果类型
     * @return 返回读取结果
     */
    static <T> T syncRead(StampedLock stampedLock, Supplier<T> reader) {
        long l = stampedLock.readLock();
        try {
            return reader.get();
        } finally {
            stampedLock.unlockRead(l);
        }
    }

    /**
     * 同步写入
     *
     * @param stampedLock cas锁
     * @param writer      写入动作
     * @param <T>         写入结果类型
     * @return 返回写入结果
     */
    static <T> T syncWrite(StampedLock stampedLock, Supplier<T> writer) {
        long l = stampedLock.writeLock();
        try {
            return writer.get();
        } finally {
            stampedLock.unlockWrite(l);
        }
    }
}

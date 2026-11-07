/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

/**
 * 验证更新器
 *
 * @author potatoxf
 */
public interface CryptoUpdater {
    /**
     * 重置
     *
     * @throws GeneralSecurityException 抛出异常
     */
    void reset() throws GeneralSecurityException;

    /**
     * 更新指纹数据
     *
     * @param input 输入
     * @throws GeneralSecurityException 抛出异常
     */
    default void update(byte input) throws GeneralSecurityException {
        update(new byte[]{input});
    }

    /**
     * 更新指纹数据
     *
     * @param input 输入
     * @throws GeneralSecurityException 抛出异常
     */
    default void update(byte[] input) throws GeneralSecurityException {
        update(input, 0, input.length);
    }

    /**
     * 更新指纹数据
     *
     * @param input  输入
     * @param offset 偏移
     * @param len    长度
     * @throws GeneralSecurityException 抛出异常
     */
    void update(byte[] input, int offset, int len) throws GeneralSecurityException;

    /**
     * 更新指纹数据
     *
     * @param input 输入
     * @throws GeneralSecurityException 抛出异常
     */
    void update(ByteBuffer input) throws GeneralSecurityException;
}

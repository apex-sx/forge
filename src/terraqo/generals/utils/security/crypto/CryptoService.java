/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security.crypto;

import com.github.potatoxf.generals.utils.coder.SupportCoderWithBase64;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * Crypto服务
 *
 * @author potatoxf
 */
public interface CryptoService extends SupportCoderWithBase64 {

    /**
     * 加密数据
     *
     * @param data 数据
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    byte[] encrypt(byte[] data) throws GeneralSecurityException;

    /**
     * 解密数据
     *
     * @param data 数据
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    byte[] decrypt(byte[] data) throws GeneralSecurityException;

    /**
     * 加密数据
     *
     * @param data 数据
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     * @throws IOException              如果读取数据出现异常
     */
    InputStream encrypt(InputStream data) throws GeneralSecurityException, IOException;

    /**
     * 解密数据
     *
     * @param data 数据
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     * @throws IOException              如果读取数据出现异常
     */
    InputStream decrypt(InputStream data) throws GeneralSecurityException, IOException;

    /**
     * 加密数据
     *
     * @param data 数据
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default String encryptToBase64(byte[] data) throws GeneralSecurityException {
        return encodeWithBase64(encrypt(data));
    }

    /**
     * 解密数据
     *
     * @param data 数据
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default byte[] decryptFromBase64(String data) throws GeneralSecurityException {
        return decrypt(decodeWithBase64(data));
    }
}

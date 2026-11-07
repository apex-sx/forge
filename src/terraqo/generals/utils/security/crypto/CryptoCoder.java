/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security.crypto;

import com.github.potatoxf.generals.utils.coder.SupportCoderWithBase64;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;

/**
 * @author potatoxf
 */
public interface CryptoCoder extends SupportCoderWithBase64 {

    /**
     * 加密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    byte[] encrypt(byte[] data, byte[] key) throws GeneralSecurityException;

    /**
     * 解密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    byte[] decrypt(byte[] data, byte[] key) throws GeneralSecurityException;

    /**
     * 加密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    InputStream encrypt(InputStream data, byte[] key) throws GeneralSecurityException, IOException;

    /**
     * 解密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    InputStream decrypt(InputStream data, byte[] key) throws GeneralSecurityException, IOException;

    /**
     * 加密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default String encryptToBase64(byte[] data, Key key) throws GeneralSecurityException {
        return encryptToBase64(data, key.getEncoded());
    }

    /**
     * 解密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default byte[] decryptFromBase64(String data, Key key) throws GeneralSecurityException {
        return decryptFromBase64(data, key.getEncoded());
    }

    /**
     * 加密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default String encryptToBase64(byte[] data, byte[] key) throws GeneralSecurityException {
        return encodeWithBase64(encrypt(data, key));
    }

    /**
     * 解密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default byte[] decryptFromBase64(String data, byte[] key) throws GeneralSecurityException {
        return decrypt(decodeWithBase64(data), key);
    }

    /**
     * 加密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default byte[] encrypt(byte[] data, Key key) throws GeneralSecurityException {
        return encrypt(data, key.getEncoded());
    }

    /**
     * 解密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default byte[] decrypt(byte[] data, Key key) throws GeneralSecurityException {
        return decrypt(data, key.getEncoded());
    }

    /**
     * 加密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default InputStream encrypt(InputStream data, Key key) throws GeneralSecurityException, IOException {
        return encrypt(data, key.getEncoded());
    }

    /**
     * 解密数据
     *
     * @param data 数据
     * @param key  密码
     * @return 返回处理后数据
     * @throws GeneralSecurityException 抛出异常
     */
    default InputStream decrypt(InputStream data, Key key) throws GeneralSecurityException, IOException {
        return decrypt(data, key.getEncoded());
    }
}

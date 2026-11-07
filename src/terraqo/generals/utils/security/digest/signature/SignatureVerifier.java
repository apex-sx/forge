/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.signature;

import com.github.potatoxf.generals.utils.security.CryptoUpdater;

import java.security.GeneralSecurityException;

/**
 * 签名验证器
 *
 * @author potatoxf
 */
public interface SignatureVerifier extends CryptoUpdater {
    /**
     * 验证
     *
     * @param signature 签名
     * @return 如果成功验证返回true，否则返回false
     */
    boolean verify(byte[] signature) throws GeneralSecurityException;
}

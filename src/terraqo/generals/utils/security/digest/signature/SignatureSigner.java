/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.signature;

import com.github.potatoxf.generals.utils.security.CryptoUpdater;

import java.security.GeneralSecurityException;

/**
 * 签名器
 *
 * @author potatoxf
 */
public interface SignatureSigner extends CryptoUpdater {
    /**
     * 签证
     *
     * @return 返回签证结果
     */
    byte[] sign() throws GeneralSecurityException;
}

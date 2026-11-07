/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security.digest.summary;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;

/**
 * Mac信息摘要
 *
 * @author potatoxf
 */
public final class CryptoSummaryMac extends AbstractCryptoSummary<CryptoSummaryMac> {
    public CryptoSummaryMac(Builder<CryptoSummaryMac> builder) {
        super(builder);
    }

    @Override
    public SummaryUpdater getCryptoSummaryCoder() throws GeneralSecurityException {
        return new SummaryUpdaterMac(Objects.requireNonNull(createMac(), "The mac must be init"));
    }

    /**
     * 创建 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成<br>
     * 当指定keySize&lt;0时，AES默认长度为128，其它算法不指定
     *
     * @param keySize 密钥长度，&lt;0表示不设定密钥长度，即使用默认长度
     * @return {@link SecretKey}
     */
    public SecretKey generateSecretKey(int keySize) throws GeneralSecurityException {
        return generateSecretKey(keySize, (byte[]) null);
    }

    /**
     * 创建 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成<br>
     * 当指定keySize&lt;0时，AES默认长度为128，其它算法不指定
     *
     * @param keySize 密钥长度，&lt;0表示不设定密钥长度，即使用默认长度
     * @param seed    种子
     * @return {@link SecretKey}
     */
    public SecretKey generateSecretKey(int keySize, byte[] seed) throws GeneralSecurityException {
        return generateSecretKey(keySize, null == seed ? new SecureRandom() : new SecureRandom(seed));
    }

    /**
     * 创建 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成<br>
     * 当指定keySize&lt;0时，AES默认长度为128，其它算法不指定
     *
     * @param keySize 密钥长度，&lt;0表示不设定密钥长度，即使用默认长度
     * @param random  随机数生成器，null表示默认
     * @return {@link SecretKey}
     */
    public SecretKey generateSecretKey(int keySize, SecureRandom random) throws GeneralSecurityException {
        KeyGenerator keyGenerator = createKeyGenerator();
        if (null == random) {
            keyGenerator.init(keySize);
        } else {
            keyGenerator.init(keySize, random);
        }
        return keyGenerator.generateKey();
    }

    public CryptoSummaryMac init(byte[] key) throws GeneralSecurityException {
        return init(key, null);
    }

    public CryptoSummaryMac init(byte[] key, AlgorithmParameterSpec algorithmParameterSpec) throws GeneralSecurityException {
        return init(key == null || key.length == 0 ? null : new SecretKeySpec(key, algorithm), algorithmParameterSpec);
    }

    public CryptoSummaryMac init(Key key) throws GeneralSecurityException {
        return init(key, null);
    }

    public CryptoSummaryMac init(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws GeneralSecurityException {
        Mac mac = createMac();
        if (key == null) {
            throw new GeneralSecurityException();
        }
        if (algorithmParameterSpec == null) {
            mac.init(key);
        } else {
            mac.init(key, algorithmParameterSpec);
        }
        return this;
    }
}

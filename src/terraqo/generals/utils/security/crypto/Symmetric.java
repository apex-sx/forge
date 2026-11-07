/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security.crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

/**
 * 对称Crypto
 *
 * @author potatoxf
 */
public final class Symmetric extends Crypto<Symmetric> {

    public Symmetric(Builder<Symmetric> builder, int[] keySpecSize, int[][] keySizeRange) {
        super(builder, keySpecSize, keySizeRange);
    }

    //------------------------------------------------------------------------------------------------------------------
    //生成SecretKey
    //------------------------------------------------------------------------------------------------------------------

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
        keySize = checkKeySize(keySize);
        KeyGenerator keyGenerator = createKeyGenerator();
        if (null == random) {
            keyGenerator.init(keySize);
        } else {
            keyGenerator.init(keySize, random);
        }
        return keyGenerator.generateKey();
    }

    /**
     * 获取 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param key 密钥，如果为{@code null} 自动生成随机密钥
     * @return {@link SecretKey}
     */
    public SecretKey getSecretKey(String key) throws GeneralSecurityException {
        return getSecretKey(key.getBytes(StandardCharsets.US_ASCII));
    }

    /**
     * 生成 {@link SecretKey}，仅用于对称加密和摘要算法密钥生成
     *
     * @param key 密钥，如果为{@code null} 自动生成随机密钥
     * @return {@link SecretKey}
     */
    public SecretKey getSecretKey(byte[] key) throws GeneralSecurityException {
        return getSecretKey(generateKeySpec(key));
    }

    /**
     * 获取 {@link SecretKey}，仅用于对称加密和摘要算法
     *
     * @param keySpec {@link KeySpec}
     * @return {@link SecretKey}
     */
    public SecretKey getSecretKey(KeySpec keySpec) throws GeneralSecurityException {
        return createSecretKeyFactory().generateSecret(keySpec);
    }

    /**
     * 获取 {@link CryptoCoder}
     *
     * @return {@link CryptoCoder}
     */
    public CryptoCoder getCryptoCoder() throws GeneralSecurityException {
        return getCryptoCoder(-1);
    }

    /**
     * 获取 {@link CryptoCoder}
     *
     * @param keySize 键大小
     * @return {@link CryptoCoder}
     */
    public CryptoCoder getCryptoCoder(int keySize) throws GeneralSecurityException {
        return new CryptoCoderImpl(this, checkKeySize(keySize), this::getSecretKey);
    }

    /**
     * 获取{@code CryptoService}
     *
     * @param base64Key base64格式密匙
     * @return {@code CryptoService}
     * @throws GeneralSecurityException 如果发生异常
     */

    public CryptoService getCryptoService(String base64Key) throws GeneralSecurityException {
        return new Service(getCryptoCoder(), decodeWithBase64(base64Key));
    }

    /**
     * 获取{@code CryptoService}
     *
     * @param key 格式密匙
     * @return {@code CryptoService}
     * @throws GeneralSecurityException 如果发生异常
     */

    public CryptoService getCryptoService(byte[] key) throws GeneralSecurityException {
        return new Service(getCryptoCoder(), key);
    }

    /**
     * 对称Crypto服务
     */
    private static final class Service implements CryptoService {
        private final CryptoCoder cryptoCoder;
        private final byte[] key;

        private Service(CryptoCoder cryptoCoder, byte[] key) {
            this.cryptoCoder = cryptoCoder;
            this.key = key;
        }

        @Override
        public byte[] encrypt(byte[] data) throws GeneralSecurityException {
            return cryptoCoder.encrypt(data, key);
        }


        @Override
        public byte[] decrypt(byte[] data) throws GeneralSecurityException {
            return cryptoCoder.decrypt(data, key);
        }


        @Override
        public InputStream encrypt(InputStream data) throws GeneralSecurityException, IOException {
            return cryptoCoder.encrypt(data, key);
        }


        @Override
        public InputStream decrypt(InputStream data) throws GeneralSecurityException, IOException {
            return cryptoCoder.decrypt(data, key);
        }
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security.crypto;

import com.github.potatoxf.generals.utils.GeneralIO;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.*;

/**
 * 非对称Crypto
 *
 * @author potatoxf
 */
public final class Asymmetric extends Crypto<Asymmetric> {
    public Asymmetric(Builder<Asymmetric> builder, int[] keySpecSize, int[][] keySizeRange) {
        super(builder, keySpecSize, keySizeRange);
    }

    //------------------------------------------------------------------------------------------------------------------
    //生成KeyPair
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 创建用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param keySize 密钥模（modulus ）长度
     * @return {@link KeyPair}
     */

    public KeyPair generateKeyPair(int keySize) throws GeneralSecurityException {
        return generateKeyPair(keySize, new SecureRandom(), null, null);
    }

    /**
     * 创建用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param keySize   密钥模（modulus ）长度（单位bit）
     * @param random    {@link SecureRandom} 对象，创建时可选传入seed
     * @param paramSpec {@link AlgorithmParameterSpec}
     * @return {@link KeyPair}
     * @throws GeneralSecurityException 如果发生异常
     */

    public KeyPair generateKeyPair(int keySize,
                                   SecureRandom random,
                                   AlgorithmParameterSpec paramSpec) throws GeneralSecurityException {
        return generateKeyPair(keySize, random, null, paramSpec);
    }

    //------------------------------------------------------------------------------------------------------------------
    //生成PrivateKey
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 创建用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * @param keySize        密钥模（modulus ）长度（单位bit）
     * @param random         {@link SecureRandom} 对象，创建时可选传入seed
     * @param paramSpecClass {@link AlgorithmParameterSpec}
     * @return {@link KeyPair}
     * @throws GeneralSecurityException 如果发生异常
     */

    public KeyPair generateKeyPair(int keySize,
                                   SecureRandom random,
                                   Class<? extends AlgorithmParameterSpec> paramSpecClass) throws GeneralSecurityException {
        return generateKeyPair(keySize, random, paramSpecClass, null);
    }

    /**
     * 创建用于非对称加密的公钥和私钥<br>
     * 密钥对生成算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyPairGenerator
     *
     * <p>对于非对称加密算法，密钥长度有严格限制，具体如下：
     *
     * <p><b>RSA：</b>
     *
     * <pre>
     * RS256、PS256：2048 bits
     * RS384、PS384：3072 bits
     * RS512、RS512：4096 bits
     * </pre>
     *
     * <p><b>EC（Elliptic Curve）：</b>
     *
     * <pre>
     * EC256：256 bits
     * EC384：384 bits
     * EC512：512 bits
     * </pre>
     *
     * @param keySize                密钥模（modulus ）长度（单位bit）
     * @param random                 {@link SecureRandom} 对象，创建时可选传入seed
     * @param paramSpecClass         {@link AlgorithmParameterSpec}
     * @param algorithmParameterSpec {@link AlgorithmParameterSpec}
     * @return {@link KeyPair}
     * @throws GeneralSecurityException 如果发生异常
     */

    public KeyPair generateKeyPair(int keySize,
                                   SecureRandom random,
                                   Class<? extends AlgorithmParameterSpec> paramSpecClass,
                                   AlgorithmParameterSpec algorithmParameterSpec) throws GeneralSecurityException {
        keySize = checkKeySize(keySize);
        // SM2算法需要单独定义其曲线生成
        if ("SM2".equalsIgnoreCase(algorithm)) {
            // SM2默认曲线
            algorithmParameterSpec = new ECGenParameterSpec("sm2p256v1");
        } else if (paramSpecClass != null && algorithmParameterSpec == null) {
            try {
                AlgorithmParameterGenerator algorithmParameterGenerator = createAlgorithmParameterGenerator();
                // 初始化参数生成器
                algorithmParameterGenerator.init(keySize);
                // 生成算法参数
                AlgorithmParameters algorithmParameters = algorithmParameterGenerator.generateParameters();
                algorithmParameterSpec = algorithmParameters.getParameterSpec(paramSpecClass);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }
        // 实例化密钥生成器
        KeyPairGenerator keyPairGenerator = createKeyPairGenerator();

        // 密钥模（modulus ）长度初始化定义
        // key长度适配修正
        if ("EC".equalsIgnoreCase(algorithm) && keySize > 256) {
            // 对于EC（EllipticCurve）算法，密钥长度有限制，在此使用默认256
            keySize = 256;
        }
        if (null != random) {
            keyPairGenerator.initialize(keySize, random);
        } else {
            keyPairGenerator.initialize(keySize);
        }

        // 自定义初始化参数
        if (algorithmParameterSpec != null) {
            if (null != random) {
                keyPairGenerator.initialize(algorithmParameterSpec, random);
            } else {
                keyPairGenerator.initialize(algorithmParameterSpec);
            }
        }
        // 生成密钥对
        return keyPairGenerator.generateKeyPair();
    }

    //------------------------------------------------------------------------------------------------------------------
    //获取PrivateKey
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 采用PKCS#8规范，此规范定义了私钥信息语法和加密私钥语法<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param key 密钥，PKCS#8格式
     * @return 私钥 {@link PrivateKey}
     */

    public PrivateKey getPrivateKeyFromFile(String key) throws GeneralSecurityException {
        try {
            return getPrivateKey(GeneralIO.read(key));
        } catch (IOException e) {
            throw new GeneralSecurityException("Error read public key from [" + key + "]", e);
        }
    }

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 采用PKCS#8规范，此规范定义了私钥信息语法和加密私钥语法<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param key 密钥，PKCS#8格式
     * @return 私钥 {@link PrivateKey}
     */

    public PrivateKey getPrivateKeyFromBase64(String key) throws GeneralSecurityException {
        return getPrivateKey(decodeWithBase64(key));
    }

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 采用PKCS#8规范，此规范定义了私钥信息语法和加密私钥语法<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param key 密钥，PKCS#8格式
     * @return 私钥 {@link PrivateKey}
     */

    public PrivateKey getPrivateKey(byte[] key) throws GeneralSecurityException {
        return getPrivateKey(new PKCS8EncodedKeySpec(key));
    }

    /**
     * 生成私钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param keySpec {@link KeySpec}
     * @return 私钥 {@link PrivateKey}
     */

    public PrivateKey getPrivateKey(KeySpec keySpec) throws GeneralSecurityException {
        return createKeyFactory().generatePrivate(keySpec);
    }

    //------------------------------------------------------------------------------------------------------------------
    //获取PublicKey
    //------------------------------------------------------------------------------------------------------------------

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 采用X509证书规范<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param key 密钥，必须为DER编码存储
     * @return 公钥 {@link PublicKey}
     */

    public PublicKey getPublicKeyFromFile(String key) throws GeneralSecurityException {
        try {
            FileInputStream in = new FileInputStream(key);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len;
            byte[] b = new byte[512];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            return getPublicKey(out.toByteArray());
        } catch (IOException e) {
            throw new GeneralSecurityException("Error read public key from [" + key + "]", e);
        }
    }

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 采用X509证书规范<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param key 密钥，必须为DER编码存储
     * @return 公钥 {@link PublicKey}
     */

    public PublicKey getPublicKeyFromBase64(String key) throws GeneralSecurityException {
        return getPublicKey(decodeWithBase64(key));
    }

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 采用X509证书规范<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param key 密钥，必须为DER编码存储
     * @return 公钥 {@link PublicKey}
     */

    public PublicKey getPublicKey(byte[] key) throws GeneralSecurityException {
        return getPublicKey(new X509EncodedKeySpec(key));
    }

    /**
     * 生成公钥，仅用于非对称加密<br>
     * 算法见：https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#KeyFactory
     *
     * @param keySpec {@link KeySpec}
     * @return 公钥 {@link PublicKey}
     */

    public PublicKey getPublicKey(KeySpec keySpec) throws GeneralSecurityException {
        return createKeyFactory().generatePublic(keySpec);
    }

    /**
     * 获取公匙{@code CryptoCoder}
     *
     * @return {@code CryptoCoder}
     * @throws GeneralSecurityException 如果发生异常
     */

    public CryptoCoder getPublicCryptoCoder() throws GeneralSecurityException {
        return this.getPublicCryptoCoder(-1);
    }

    /**
     * 获取公匙{@code CryptoCoder}
     *
     * @param keySize KeySize大小
     * @return {@code CryptoCoder}
     * @throws GeneralSecurityException 如果发生异常
     */

    public CryptoCoder getPublicCryptoCoder(int keySize) throws GeneralSecurityException {
        return new CryptoCoderImpl(this, checkKeySize(keySize), this::getPublicKey);
    }

    /**
     * 获取私匙{@code CryptoCoder}
     *
     * @return {@code CryptoCoder}
     * @throws GeneralSecurityException 如果发生异常
     */

    public CryptoCoder getPrivateCryptoCoder() throws GeneralSecurityException {
        return this.getPrivateCryptoCoder(-1);
    }

    /**
     * 获取私匙{@code CryptoCoder}
     *
     * @param keySize KeySize大小
     * @return {@code CryptoCoder}
     * @throws GeneralSecurityException 如果发生异常
     */

    public CryptoCoder getPrivateCryptoCoder(int keySize) throws GeneralSecurityException {
        return new CryptoCoderImpl(this, checkKeySize(keySize), this::getPrivateKey);
    }

    /**
     * 获取私匙{@code CryptoCoder}
     *
     * @param base64PublicKey  base64格式公匙
     * @param base64PrivateKey base64格式私匙
     * @param isPublicEncrypt  是否公匙加密
     * @return {@code CryptoCoder}
     * @throws GeneralSecurityException 如果发生异常
     */

    public CryptoService getCryptoService(String base64PublicKey,
                                          String base64PrivateKey,
                                          boolean isPublicEncrypt) throws GeneralSecurityException {
        return getCryptoService(decodeWithBase64(base64PublicKey), decodeWithBase64(base64PrivateKey), isPublicEncrypt);
    }

    /**
     * 获取私匙{@code CryptoCoder}
     *
     * @param base64PublicKey  base64格式公匙
     * @param base64PrivateKey base64格式私匙
     * @param isPublicEncrypt  是否公匙加密
     * @return {@code CryptoCoder}
     * @throws GeneralSecurityException 如果发生异常
     */

    public CryptoService getCryptoService(byte[] base64PublicKey,
                                          byte[] base64PrivateKey,
                                          boolean isPublicEncrypt) throws GeneralSecurityException {
        return new Service(getPublicCryptoCoder(), base64PublicKey, getPrivateCryptoCoder(), base64PrivateKey, isPublicEncrypt);
    }

    /**
     * 非对称Crypto服务
     */
    private static final class Service implements CryptoService {
        private final CryptoCoder publicCryptoCoder, privateCryptoCoder;
        private final byte[] publicKey, privateKey;
        private final boolean isPublicEncrypt;

        private Service(CryptoCoder publicCryptoCoder, byte[] publicKey, CryptoCoder privateCryptoCoder, byte[] privateKey, boolean isPublicEncrypt) {
            this.publicCryptoCoder = publicCryptoCoder;
            this.publicKey = publicKey;
            this.privateCryptoCoder = privateCryptoCoder;
            this.privateKey = privateKey;
            this.isPublicEncrypt = isPublicEncrypt;
        }

        @Override
        public byte[] encrypt(byte[] data) throws GeneralSecurityException {
            if (isPublicEncrypt) {
                return publicCryptoCoder.encrypt(data, publicKey);
            } else {
                return privateCryptoCoder.encrypt(data, privateKey);
            }
        }

        @Override
        public byte[] decrypt(byte[] data) throws GeneralSecurityException {
            if (isPublicEncrypt) {
                return privateCryptoCoder.decrypt(data, privateKey);
            } else {
                return publicCryptoCoder.decrypt(data, publicKey);
            }
        }

        @Override
        public InputStream encrypt(InputStream data) throws GeneralSecurityException, IOException {
            if (isPublicEncrypt) {
                return publicCryptoCoder.encrypt(data, publicKey);
            } else {
                return privateCryptoCoder.encrypt(data, privateKey);
            }
        }

        @Override
        public InputStream decrypt(InputStream data) throws GeneralSecurityException, IOException {
            if (isPublicEncrypt) {
                return publicCryptoCoder.decrypt(data, publicKey);
            } else {
                return privateCryptoCoder.decrypt(data, privateKey);
            }
        }
    }
}

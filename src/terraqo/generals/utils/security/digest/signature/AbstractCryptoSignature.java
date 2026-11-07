/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.utils.security.digest.signature;

import com.github.potatoxf.generals.utils.security.digest.CryptoDigest;
import com.github.potatoxf.generals.utils.GeneralIO;

import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAKey;
import java.security.spec.*;

/**
 * 信息摘要
 *
 * @author potatoxf
 */
public abstract class AbstractCryptoSignature<I extends AbstractCryptoSignature<I>> extends CryptoDigest<I> {
    AbstractCryptoSignature(Builder<I> builder) {
        super(builder);
    }

    public Signature getInitSignature(AlgorithmParameterSpec algorithmParameterSpec) throws GeneralSecurityException {
        Signature signature = createSignature();
        if (algorithmParameterSpec != null) {
            signature.setParameter(algorithmParameterSpec);
        }
        return signature;
    }

    /**
     * 如果枚举实例表示 RSA 公钥/私钥对签名算法，则返回{@code true}，否则{@code false}
     */
    public boolean isRsa() {
        return "RSA".equalsIgnoreCase(family());
    }

    @Override
    public void assertValid(Key key, boolean signing) throws GeneralSecurityException {
        super.assertValid(key, signing);
        if (signing && !(key instanceof PrivateKey)) {
            throw new InvalidKeyException(" signing keys must be PrivateKey instances.");
        }
        //RSA
        if (isRsa()) {
            String familyName = family();
            if (!(key instanceof RSAKey)) {
                throw new InvalidKeyException(familyName + " " + keyType(signing) + " keys must be RSAKey instances.");
            }
            RSAKey rsaKey = (RSAKey) key;
            int size = rsaKey.getModulus().bitLength();
            int minKeyLength = minKeyLength();
            if (size < minKeyLength) {
                String name = value();
                String section = name.startsWith("P") ? "3.5" : "3.3";
                throw new GeneralSecurityException("The " + keyType(signing) + " key's size is " + size + " bits which is not secure " +
                        "enough for the " + name + " algorithm.  The JWT JWA Specification (RFC 7518, Section " +
                        section + ") states that keys used with " + name + " MUST have a size >= " +
                        minKeyLength + " bits.");
            }
        }
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
            return getPublicKey(GeneralIO.read(key));
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

    public AlgorithmParameterSpec createAlgorithmParameterSpec() throws GeneralSecurityException {
        if ("PS256".equalsIgnoreCase(value)) {
            return new PSSParameterSpec(MGF1ParameterSpec.SHA256.getDigestAlgorithm(), "MGF1", MGF1ParameterSpec.SHA256, 32, 1);
        } else if ("PS384".equalsIgnoreCase(value)) {
            return new PSSParameterSpec(MGF1ParameterSpec.SHA384.getDigestAlgorithm(), "MGF1", MGF1ParameterSpec.SHA384, 48, 1);
        } else if ("PS512".equalsIgnoreCase(value)) {
            return new PSSParameterSpec(MGF1ParameterSpec.SHA512.getDigestAlgorithm(), "MGF1", MGF1ParameterSpec.SHA512, 64, 1);
        }
        return null;
    }

    public SignatureVerifier getCryptoSignatureVerifier(PublicKey publicKey) throws GeneralSecurityException {
        return new SignatureMain(this, publicKey);
    }

    public SignatureVerifier getCryptoSignatureVerifier(PrivateKey privateKey) throws GeneralSecurityException {
        return new SignatureMain(this, privateKey);
    }

    public SignatureSigner getCryptoSignatureSigner(PrivateKey privateKey) throws GeneralSecurityException {
        return new SignatureMain(this, privateKey);
    }

    public SignatureService getCryptoSignatureService(KeyPair keyPair) throws GeneralSecurityException {
        return getCryptoSignatureService(keyPair.getPublic(), keyPair.getPrivate());
    }

    public SignatureService getCryptoSignatureService(PrivateKey privateKey) throws GeneralSecurityException {
        return new SignatureServiceImpl(getCryptoSignatureSigner(privateKey), getCryptoSignatureVerifier(privateKey));
    }

    public SignatureService getCryptoSignatureService(PublicKey publicKey, PrivateKey privateKey) throws GeneralSecurityException {
        return new SignatureServiceImpl(getCryptoSignatureSigner(privateKey), getCryptoSignatureVerifier(publicKey));
    }
}
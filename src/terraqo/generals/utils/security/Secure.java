/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security;

import com.github.potatoxf.generals.utils.GeneralSecurity;
import com.github.potatoxf.generals.utils.coder.SupportCoderWithBase64;
import com.github.potatoxf.generals.symbolic.This;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import java.security.*;
import java.util.Objects;

/**
 * Crypto工具类
 *
 * @author potatoxf
 */
public abstract class Secure<I extends Secure<I>> implements This<I>, SupportCoderWithBase64 {
    static {
        GeneralSecurity.enableBouncyCastleIfPossible(); //PS256, PS384, PS512 on <= JDK 10 require BC
    }

    /**
     * 算法
     */
    protected final String value;
    /**
     * 算法
     */
    protected final String algorithm;
    /**
     * 类别
     */
    private final String family;
    /**
     * 是否jdk标准
     */
    private final boolean jdkStandard;
    /**
     * 描述
     */
    private final String description;
    /**
     * 提供器名称
     */
    private final String providerName;
    /**
     * 提供器
     */
    private final Provider provider;

    protected Secure(Builder<I, ?> builder) {
        this.value = builder.getValue();
        this.algorithm = builder.getAlgorithm();
        this.family = builder.getFamily();
        this.jdkStandard = builder.isJdkStandard();
        this.description = builder.getDescription();
        this.providerName = builder.getProviderName();
        this.provider = builder.getProvider();
    }

    /**
     * 返回 JWA 算法名称常量
     */
    public final String value() {
        return this.value;
    }

    /**
     * 返回用于计算签名的 JCA 算法的名称
     */
    public final String algorithm() {
        return this.algorithm;
    }

    /**
     * 提供名称
     *
     * @return 返回提供名称
     */
    public final Provider provider() {
        return provider;
    }

    /**
     * 提供器
     *
     * @return 返回提供器
     */
    public final String providerName() {
        return providerName;
    }

    /**
     * 返回签名算法的密码系列名称
     */
    public final String family() {
        return this.family;
    }

    /**
     * 如果标准 JDK 发行版支持该算法，则返回 {@code true}
     * 如果算法实现不在 JDK 中，必须由单独的运行时 JCA 提供程序提供（如BouncyCastle）
     */
    public final boolean jdkStandard() {
        return this.jdkStandard;
    }

    /**
     * 返回 JWA 算法描述
     */
    public final String description() {
        return this.description;
    }

    /**
     * 创建{@code Cipher}
     *
     * @return {@code Cipher}
     * @throws GeneralSecurityException 如果发生异常
     */
    public Cipher createCipher() throws GeneralSecurityException {
        return CryptoInstance.CIPHER.getInstance(algorithm(), providerName(), provider());
    }

    /**
     * 创建{@code SecretKeyFactory}
     *
     * @return {@code SecretKeyFactory}
     * @throws GeneralSecurityException 如果发生异常
     */
    public SecretKeyFactory createSecretKeyFactory() throws GeneralSecurityException {
        return CryptoInstance.SECRET_KEY_FACTORY.getInstance(algorithm(), providerName(), provider());
    }

    /**
     * 创建{@code KeyGenerator}
     *
     * @return {@code KeyGenerator}
     * @throws GeneralSecurityException 如果发生异常
     */
    public KeyGenerator createKeyGenerator() throws GeneralSecurityException {
        return CryptoInstance.KEY_GENERATOR.getInstance(algorithm(), providerName(), provider());
    }

    /**
     * 创建{@code AlgorithmParameterGenerator}
     *
     * @return {@code AlgorithmParameterGenerator}
     * @throws GeneralSecurityException 如果发生异常
     */
    public AlgorithmParameterGenerator createAlgorithmParameterGenerator() throws GeneralSecurityException {
        return CryptoInstance.ALGORITHM_PARAMETER_GENERATOR.getInstance(algorithm(), providerName(), provider());
    }

    /**
     * 创建{@code KeyPairGenerator}
     *
     * @return {@code KeyPairGenerator}
     * @throws GeneralSecurityException 如果发生异常
     */
    public KeyPairGenerator createKeyPairGenerator() throws GeneralSecurityException {
        return CryptoInstance.KEY_PAIR_GENERATOR.getInstance(algorithm(), providerName(), provider());
    }

    /**
     * 创建{@code KeyFactory}
     *
     * @return {@code KeyFactory}
     * @throws GeneralSecurityException 如果发生异常
     */
    public KeyFactory createKeyFactory() throws GeneralSecurityException {
        return CryptoInstance.KEY_FACTORY.getInstance(algorithm(), providerName(), provider());
    }

    /**
     * 创建{@code Mac}
     *
     * @return {@code Mac}
     * @throws GeneralSecurityException 如果发生异常
     */
    public Mac createMac() throws GeneralSecurityException {
        return CryptoInstance.MAC.getInstance(algorithm(), providerName(), provider());
    }

    /**
     * 创建{@code MessageDigest}
     *
     * @return {@code MessageDigest}
     * @throws GeneralSecurityException 如果发生异常
     */
    public MessageDigest createMessageDigest() throws GeneralSecurityException {
        return CryptoInstance.MESSAGE_DIGEST.getInstance(algorithm(), providerName(), provider());
    }

    /**
     * 创建{@code Signature}
     *
     * @return {@code Signature}
     * @throws GeneralSecurityException 如果发生异常
     */
    public Signature createSignature() throws GeneralSecurityException {
        return CryptoInstance.SIGNATURE.getInstance(algorithm(), providerName(), provider());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Secure<?> secure = (Secure<?>) o;
        return jdkStandard == secure.jdkStandard
                && Objects.equals(value, secure.value)
                && Objects.equals(algorithm, secure.algorithm)
                && Objects.equals(family, secure.family);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, algorithm, family, jdkStandard);
    }

    /**
     * Secure构建器
     */
    public abstract static class Builder<T extends Secure<T>, B extends Builder<T, B>> implements This<B> {
        protected String value;
        protected String algorithm;
        protected boolean jdkStandard;
        protected String family;
        protected String description;
        protected Provider provider;
        protected String providerName;

        protected Builder() {
        }

        /**
         * 构建{@code T extends Secure<T>}
         *
         * @return {@code T extends Secure<T>}
         */
        public abstract T build();

        public String getValue() {
            return value;
        }

        public B setValue(String value) {
            this.value = value;
            return this$();
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public B setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
            return this$();
        }

        public boolean isJdkStandard() {
            return jdkStandard;
        }

        public String getFamily() {
            return family;
        }

        public B setFamily(String family) {
            this.family = family;
            return this$();
        }

        public String getDescription() {
            return description;
        }

        public B setDescription(String description) {
            this.description = description;
            return this$();
        }

        public Provider getProvider() {
            return provider;
        }

        public B setProvider(Provider provider) {
            this.provider = provider;
            return this$();
        }

        public String getProviderName() {
            return providerName;
        }

        public B setProviderName(String providerName) {
            this.providerName = providerName;
            return this$();
        }

        public B setJdkStandard() {
            this.jdkStandard = true;
            return this$();
        }
    }
}
/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.security;

import com.github.potatoxf.generals.lang.ReferenceThreadLocal;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import java.lang.reflect.Method;
import java.security.*;

/**
 * Crypto实例
 *
 * @author potatoxf
 */
@SuppressWarnings("unchecked")
public final class CryptoInstance<T> {

    public static final CryptoInstance<Cipher> CIPHER = new CryptoInstance<>(Cipher.class);
    public static final CryptoInstance<AlgorithmParameterGenerator> ALGORITHM_PARAMETER_GENERATOR = new CryptoInstance<>(AlgorithmParameterGenerator.class);

    //------------------------------------------------------------------------------------------------------------------
    //摘要
    //------------------------------------------------------------------------------------------------------------------

    public static final CryptoInstance<Mac> MAC = new CryptoInstance<>(Mac.class);
    public static final CryptoInstance<Signature> SIGNATURE = new CryptoInstance<>(Signature.class);
    public static final CryptoInstance<MessageDigest> MESSAGE_DIGEST = new CryptoInstance<>(MessageDigest.class);

    //------------------------------------------------------------------------------------------------------------------
    //对称
    //------------------------------------------------------------------------------------------------------------------

    public static final CryptoInstance<SecretKeyFactory> SECRET_KEY_FACTORY = new CryptoInstance<>(SecretKeyFactory.class);
    public static final CryptoInstance<KeyGenerator> KEY_GENERATOR = new CryptoInstance<>(KeyGenerator.class);

    //------------------------------------------------------------------------------------------------------------------
    //非对称
    //------------------------------------------------------------------------------------------------------------------

    public static final CryptoInstance<KeyFactory> KEY_FACTORY = new CryptoInstance<>(KeyFactory.class);
    public static final CryptoInstance<KeyPairGenerator> KEY_PAIR_GENERATOR = new CryptoInstance<>(KeyPairGenerator.class);


    private final ReferenceThreadLocal<T> cache = ReferenceThreadLocal.soft();
    private final Class<T> type;
    private final Method method1, method2, method3;

    private CryptoInstance(Class<T> type) {
        this.type = type;
        Method method;
        try {
            method = type.getMethod("getInstance", String.class);
        } catch (NoSuchMethodException ignored) {
            method = null;
        }
        this.method1 = method;
        try {
            method = type.getMethod("getInstance", String.class, String.class);
        } catch (NoSuchMethodException ignored) {
            method = null;
        }
        this.method2 = method;
        try {
            method = type.getMethod("getInstance", String.class, Provider.class);
        } catch (NoSuchMethodException ignored) {
            method = null;
        }
        this.method3 = method;
    }

    /**
     * 创建实例
     *
     * @param algorithm 算法
     * @return 返回实例
     */
    public T getInstance(String algorithm) throws GeneralSecurityException {
        return this.getInstance(algorithm, null, null);
    }

    /**
     * 创建实例
     *
     * @param algorithm    算法
     * @param providerName 提供名称
     * @return 返回实例
     */
    public T getInstance(String algorithm, String providerName) throws GeneralSecurityException {
        return this.getInstance(algorithm, providerName, null);
    }

    /**
     * 创建实例
     *
     * @param algorithm 算法
     * @param provider  提供器
     * @return 返回实例
     */
    public T getInstance(String algorithm, Provider provider) throws GeneralSecurityException {
        return this.getInstance(algorithm, null, provider);
    }

    /**
     * 创建实例
     *
     * @param algorithm    算法
     * @param providerName 提供名称
     * @param provider     提供器
     * @return 返回实例
     */
    public T getInstance(String algorithm, String providerName, Provider provider) throws GeneralSecurityException {
        T t = cache.get();
        if (t == null) {
            Throwable e1 = null, e2 = null, e3 = null;
            if (provider != null && method3 != null) {
                try {
                    t = (T) method3.invoke(null, algorithm, provider);
                    if (t != null) cache.set(t);
                    return t;
                } catch (Throwable e) {
                    e3 = e;
                }
            }
            if (providerName != null && method2 != null) {
                try {
                    t = (T) method2.invoke(null, algorithm, providerName);
                    if (t != null) cache.set(t);
                    return t;
                } catch (Throwable e) {
                    e2 = e;
                }
            }
            if (method1 != null) {
                try {
                    t = (T) method1.invoke(null, algorithm);
                    if (t != null) cache.set(t);
                    return t;
                } catch (Throwable e) {
                    e1 = e;
                }
            }
            String message = "Cannot to create instance [" + type.getName() + "] cause by";
            if (e1 != null && e1.getMessage() != null) {
                message += "\n [" + e1.getMessage() + "]";
            }
            if (e2 != null && e2.getMessage() != null) {
                message += "\n [" + e2.getMessage() + "]";
            }
            if (e3 != null && e3.getMessage() != null) {
                message += "\n [" + e3.getMessage() + "]";
            }
            throw new GeneralSecurityException(message);
        }
        return t;
    }
}

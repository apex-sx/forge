/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.cache.Caching;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 密码验证器
 *
 * @author potatoxf
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PasswordVerifier {
    /**
     * 最大尝试次数
     */
    private int maxRetryCount = 5;
    /**
     * 次数缓存
     */
    private Caching<String, AtomicInteger> caching;
    /**
     * 加密器
     */
    private Function<String[], String> encryptor;

    /**
     * 验证密码是否正确
     *
     * @param passwordData 密码数据
     * @param input        输入密码
     */
    public final void validate(PasswordData<?> passwordData, String input) {
        this.validate(passwordData.getUserAccount(), passwordData.getUserPassword(), passwordData.getUserSalt(), input);
    }

    /**
     * 验证密码是否正确
     *
     * @param account  账户
     * @param password 密码
     * @param salt     随机盐
     * @param input    输入密码
     */
    public final void validate(String account, String password, String salt, String input) {
        if (caching == null) {
            throw new IllegalArgumentException("The caching must be not null");
        }
        if (encryptor == null) {
            throw new IllegalArgumentException("The encrypt must be not null");
        }
        AtomicInteger retryCount = caching.get(account, false, (k, v) -> new AtomicInteger(0));
        if (retryCount.incrementAndGet() > maxRetryCount) {
            throw new PasswordRetryException("Password entered incorrectly %s times", maxRetryCount);
        }

        if (!matches(account, password, salt, input)) {
            throw new PasswordMatchException("User does not exist/password is incorrect");
        } else {
            caching.del(account);
        }
    }

    /**
     * 验证密码是否匹配
     *
     * @param passwordData 密码数据
     * @param input        输入密码
     */
    public final boolean matches(PasswordData<?> passwordData, String input) {
        return this.matches(passwordData.getUserAccount(), passwordData.getUserPassword(), passwordData.getUserSalt(), input);
    }

    /**
     * 验证密码是否匹配
     *
     * @param account  账户
     * @param password 密码
     * @param salt     随机盐
     * @param input    输入密码
     */
    public final boolean matches(String account, String password, String salt, String input) {
        return Objects.equals(password, encrypt(account, input, salt));
    }

    /**
     * 加密密码数据并重新设置密码再返回
     *
     * @param passwordData 密码数据
     * @return 返回加密后的密码数据
     */
    public final String encryptAndSet(PasswordData<?> passwordData) {
        String encrypt = this.encrypt(passwordData.getUserAccount(), passwordData.getUserPassword(), passwordData.getUserSalt());
        passwordData.setUserPassword(encrypt);
        return encrypt;
    }

    /**
     * 加密密码数据并重新设置密码再返回
     *
     * @param passwordData 密码数据
     * @param input        输入密码
     * @return 返回加密后的密码数据
     */
    public final String encryptAndSet(PasswordData<?> passwordData, String input) {
        String encrypt = this.encrypt(passwordData.getUserAccount(), input, passwordData.getUserSalt());
        passwordData.setUserPassword(encrypt);
        return encrypt;
    }

    /**
     * 加密密码数据并返回
     *
     * @param account  账户
     * @param password 密码
     * @param salt     随机盐
     * @return 返回加密后的密码数据
     */
    public final String encrypt(String account, String password, String salt) {
        return encryptor.apply(new String[]{account, password, salt});
    }

    public int getMaxRetryCount() {
        return maxRetryCount;
    }

    public PasswordVerifier setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
        return this;
    }

    public Caching<String, AtomicInteger> getCaching() {
        return caching;
    }

    public PasswordVerifier setCaching(Caching<String, AtomicInteger> caching) {
        this.caching = caching;
        return this;
    }

    public Function<String[], String> getEncryptor() {
        return encryptor;
    }

    public PasswordVerifier setEncryptor(Function<String[], String> encryptor) {
        this.encryptor = encryptor;
        return this;
    }
}

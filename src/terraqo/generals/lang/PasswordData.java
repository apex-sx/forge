/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.symbolic.This;

/**
 * @author potatoxf
 */
@SuppressWarnings("UnusedReturnValue")
public interface PasswordData<T extends PasswordData<T>> extends This<T> {
    /**
     * 获取用户账户
     *
     * @return 返回用户账户
     */
    String getUserAccount();

    /**
     * 设置用户账户
     *
     * @param userAccount 用户账户
     * @return this
     */
    T setUserAccount(String userAccount);

    /**
     * 获取用户密码
     *
     * @return 返回用户密码
     */
    String getUserPassword();

    /**
     * 设置用户密码
     *
     * @param userPassword 用户密码
     * @return this
     */
    T setUserPassword(String userPassword);

    /**
     * 获取用户随机盐
     *
     * @return 返回用户随机盐
     */
    String getUserSalt();

    /**
     * 设置用户随机盐
     *
     * @param userSalt 用户随机盐
     * @return this
     */
    T setUserSalt(String userSalt);
}

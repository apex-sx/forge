/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

/**
 * 用户错误最大次数异常类
 *
 * @author poatoxf
 */
public class PasswordRetryException extends PasswordException {
    private static final long serialVersionUID = 1L;

    public PasswordRetryException(String message, Object... args) {
        super("[retry]" + message, args);
    }
}

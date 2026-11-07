/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

/**
 * 用户密码不正确或不符合规范异常类
 *
 * @author poatoxf
 */
public class PasswordMatchException extends PasswordException {
    private static final long serialVersionUID = 1L;

    public PasswordMatchException(String message, Object... args) {
        super("[match]" + message, args);
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

/**
 * 密码异常
 *
 * @author potatoxf
 */
public class PasswordException extends MessageI18nRuntimeException {
    private static final long serialVersionUID = 1L;

    public PasswordException(String message, Object... args) {
        super("[password]" + message, args);
    }
}

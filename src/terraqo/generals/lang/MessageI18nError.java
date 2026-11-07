/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

/**
 * 支持国家化语言消息异常
 *
 * @author potatoxf
 */
public abstract class MessageI18nError extends SupportI18nError {
    private static final long serialVersionUID = 1L;

    public MessageI18nError(String message, Object... args) {
        super(Internationalization.MESSAGE_BASE_NAME + message, args);
    }
}

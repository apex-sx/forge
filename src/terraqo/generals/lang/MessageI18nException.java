/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

/**
 * 支持国家化语言消息异常
 *
 * @author potatoxf
 */
public abstract class MessageI18nException extends SupportI18nException {
    private static final long serialVersionUID = 1L;

    public MessageI18nException(String message, Object... args) {
        super(Internationalization.MESSAGE_BASE_NAME + message, args);
    }
}

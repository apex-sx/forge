/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.cache;

import com.github.potatoxf.generals.lang.SupportI18nRuntimeException;

/**
 * 缓存异常
 *
 * @author potatoxf
 */
public class CachingException extends SupportI18nRuntimeException {
    private static final long serialVersionUID = 1L;
    public CachingException(String message, Object... args) {
        super(message, args);
    }
}

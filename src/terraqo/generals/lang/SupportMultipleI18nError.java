/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import java.util.Objects;

/**支持国家化语言列表运行错误
 * @author potatoxf
 */
public class SupportMultipleI18nError extends Error {
    private static final long serialVersionUID = 1L;

    private final SupportMultipleI18n supportMultipleI18n;

    public SupportMultipleI18nError(SupportMultipleI18n supportMultipleI18n) {
        super("If multiple anomalies are detected, check SupportMultipleI18n.");
        this.supportMultipleI18n = Objects.requireNonNull(supportMultipleI18n);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return supportMultipleI18n.getContent();
    }

    public SupportMultipleI18n supportMultipleI18n() {
        return supportMultipleI18n;
    }

    public boolean isEmpty() {
        return supportMultipleI18n.isEmpty();
    }
}

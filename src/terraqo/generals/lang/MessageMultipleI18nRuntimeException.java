/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

/**
 * 支持国家化语言列表运行消息异常
 *
 * @author potatoxf
 */
public class MessageMultipleI18nRuntimeException extends SupportMultipleI18nRuntimeException {
    private static final long serialVersionUID = 1L;

    public MessageMultipleI18nRuntimeException() {
        super(new SupportMultipleI18n(Internationalization.MESSAGE_BASE_NAME));
    }

    /**
     * 构造异常支持国家化语言
     *
     * @param key     国际化键
     * @param content 国际化默认内容
     * @return {@link SupportI18n}
     */
    public MessageMultipleI18nRuntimeException addSupportI18n(String key, String content, Object... args) {
        this.supportMultipleI18n().addSupportI18n(key, content, args);
        return this;
    }
}

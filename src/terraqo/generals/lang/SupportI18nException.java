/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.utils.GeneralException;

import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * 支持国家化语言异常
 *
 * @author potatoxf
 */
public abstract class SupportI18nException extends Exception {
    private static final long serialVersionUID = 1L;

    private final SupportI18n supportI18n;

    public SupportI18nException(String message, Object... args) {
        super(message);
        Throwable throwable = null;
        if (args != null && args.length > 0) {
            if (args[args.length - 1] instanceof Throwable) {
                throwable = (Throwable) args[args.length - 1];
                args = Arrays.copyOf(args, args.length - 1);
            }
        } else if (args == null) {
            args = new Object[0];
        }
        supportI18n = SupportI18n.ofThrowable(this, args);
        this.initCause(throwable);
        GeneralException.initExceptionMessage(this, supportI18n.getContent((ResourceBundle) null));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return supportI18n.getContent();
    }

    /**
     * 获取参数
     *
     * @return 返回参数
     */
    public final Object[] args(){
        return supportI18n.args();
    }
}

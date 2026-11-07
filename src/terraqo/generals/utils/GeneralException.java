/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author potatoxf
 */
public interface GeneralException {
    /**
     * A reference to Throwable initCause method
     */
     Method INIT_CAUSE_METHOD = GeneralClass.getMethod(false, false, Throwable.class, "initCause", Throwable.class);
    /**
     * A reference to Throwable detailMessage field
     */
    Field DETAIL_MESSAGE_FIELD = GeneralClass.getField(false, false, Throwable.class, "detailMessage");

    /**
     * 初始化异常原因
     *
     * @param exception 异常
     * @param cause     异常原因
     * @return 如果初始化返回true，否则返回false
     */
    static boolean initExceptionCause(Throwable exception, Throwable cause) {
        if (INIT_CAUSE_METHOD != null && cause != null) {
            try {
                INIT_CAUSE_METHOD.invoke(exception, cause);
                return true;
            } catch (Throwable e) {
                return false; // can't initialize cause
            }
        }
        return false;
    }

    /**
     * 初始化异常信息
     *
     * @param exception     异常
     * @param detailMessage 异常信心
     * @return 如果初始化返回true，否则返回false
     */
    static boolean initExceptionMessage(Throwable exception, String detailMessage) {
        if (DETAIL_MESSAGE_FIELD != null && detailMessage != null) {
            try {
                DETAIL_MESSAGE_FIELD.set(exception, detailMessage);
                return true;
            } catch (Throwable e) {
                return false; // can't initialize cause
            }
        }
        return false;
    }

    static String printToString(String message, String split, Throwable e) {
        StringWriter out = new StringWriter();
        if (message != null) {
            out.append(message);
            out.append(split);
        }
        PrintWriter printWriter = new PrintWriter(out);
        e.printStackTrace(printWriter);
        return out.toString();
    }
}

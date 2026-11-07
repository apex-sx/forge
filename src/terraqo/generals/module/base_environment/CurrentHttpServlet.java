/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.module.base_environment;

import com.github.potatoxf.generals.Log;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 当前全局Servlet
 *
 * @author potatoxf
 */
public interface CurrentHttpServlet {
    AtomicReference<CurrentHttpServlet> GLOBAL = new AtomicReference<>();

    static CurrentHttpServlet global() {
        CurrentHttpServlet value = GLOBAL.get();
        if (value == null) {
            throw new RuntimeException("No set global '" + CurrentHttpServlet.class.getSimpleName() + "' instance");
        }
        return value;
    }

    /**
     * 获取request
     */
    static HttpServletRequest getsHttpServletRequest() {
        try {
            return CurrentHttpServlet.getsHttpServletRequestSafely();
        } catch (Throwable e) {
            Log.warn("Error to g et current 'HttpServletRequest'", e);
            return null;
        }
    }

    /**
     * 获取request
     */
    static HttpServletRequest getsHttpServletRequestSafely() {
        return global().getHttpServletRequest();
    }

    /**
     * 获取response
     */
    static HttpServletResponse getsHttpServletResponse() {
        try {
            return CurrentHttpServlet.getsHttpServletResponseSafely();
        } catch (Throwable e) {
            Log.warn("Error to g et current 'HttpServletResponse'", e);
            return null;
        }
    }

    /**
     * 获取response
     */
    static HttpServletResponse getsHttpServletResponseSafely() {
        return global().getHttpServletResponse();
    }

    /**
     * 获取session
     */
    static HttpSession getsHttpSession() {
        try {
            return CurrentHttpServlet.getsHttpSessionSafely();
        } catch (Throwable e) {
            Log.warn("Error to g et current 'HttpSession'", e);
            return null;
        }
    }

    /**
     * 获取session
     */
    static HttpSession getsHttpSessionSafely() {
        return global().getHttpSession();
    }

    /**
     * 获取request
     */
    HttpServletRequest getHttpServletRequest();

    /**
     * 获取response
     */
    HttpServletResponse getHttpServletResponse();

    /**
     * 获取session
     */
    default HttpSession getHttpSession() {
        return getHttpServletRequest().getSession();
    }
}

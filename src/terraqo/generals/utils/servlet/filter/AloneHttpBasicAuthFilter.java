/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.servlet.filter;


import com.github.potatoxf.generals.utils.servlet.AdapterFilter;
import com.github.potatoxf.generals.utils.servlet.AdapterHttpSessionListener;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 独自用户认证
 *
 * @author potatoxf
 * *
 */
public class AloneHttpBasicAuthFilter implements AdapterFilter, AdapterHttpSessionListener {
    /**
     * 指定值
     */
    private static final Object VALUE = new Object();
    /**
     * 保存是否有用户登录
     */
    private final AtomicBoolean hasUser = new AtomicBoolean(false);
    /**
     * 存贮在会话键
     */
    private final String key;
    /**
     * 用户名
     */
    private final String username;
    /**
     * 密码
     */
    private final String password;
    /**
     * 有效时间
     */
    private int availableTimeSecond = 60 * 60;

    public AloneHttpBasicAuthFilter(String key, String username, String password) {
        this.key = key;
        this.username = username;
        this.password = password;
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        Object object = session.getAttribute(key);
        if (hasUser.get()) {
            if (object != null) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                response.setStatus(401);
                response.setHeader("WWW-Authenticate", "Basic realm=Only one user is allowed to log in!");
            }
        } else {
            boolean isSuccess = false;
            String basicValue = request.getHeader("Authorization");
            if (basicValue != null && !basicValue.isEmpty()) {
                String[] authorizationValue = basicValue.split(" ");
                if (authorizationValue.length == 2) {
                    String base64Encoded = new String(Base64.getDecoder().decode(authorizationValue[1]));
                    if (!base64Encoded.isEmpty()) {
                        String[] userAndPwd = base64Encoded.split(":");
                        if (userAndPwd.length == 2
                                && username.equals(userAndPwd[0])
                                && password.equals(userAndPwd[1])) {
                            if (hasUser.compareAndSet(false, true)) {
                                isSuccess = true;
                                session.setAttribute(key, VALUE);
                                session.setMaxInactiveInterval(availableTimeSecond);
                                filterChain.doFilter(servletRequest, servletResponse);
                            }
                        }
                    }
                }
            }
            if (!isSuccess) {
                response.setStatus(401);
                response.setHeader("WWW-Authenticate", "Basic realm=Verify Http Basic permissions!");
            }
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        AdapterHttpSessionListener.super.sessionDestroyed(event);
        Object attribute = event.getSession().getAttribute(key);
        if (attribute != null) {
            hasUser.compareAndSet(true, false);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AloneHttpBasicAuthFilter)) return false;

        AloneHttpBasicAuthFilter that = (AloneHttpBasicAuthFilter) o;

        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public boolean hasUser() {
        return hasUser.get();
    }

    public String getKey() {
        return key;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getAvailableTimeSecond() {
        return availableTimeSecond;
    }

    public void setAvailableTimeSecond(int availableTimeSecond) {
        this.availableTimeSecond = availableTimeSecond;
    }
}

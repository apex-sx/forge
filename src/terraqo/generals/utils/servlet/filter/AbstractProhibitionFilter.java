/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.servlet.filter;

import com.github.potatoxf.generals.utils.servlet.AdapterFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * 禁止过滤器
 *
 * @author potatoxf
 */
public abstract class AbstractProhibitionFilter implements AdapterFilter {
    protected final Charset charset;

    public AbstractProhibitionFilter(Charset charset) {
        this.charset = Objects.requireNonNull(charset);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        Object data = getData(request, response);
        if (isProhibit(request, response, data)) {
            String html = getProhibitHtml(request, response, data);
            response.setStatus(status());
            response.setHeader("Content-Type", "text/html; charset=" + charset.name());
            response.getWriter().write(html);
            response.getWriter().close();
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 禁止时返回的状态码
     *
     * @return 返回的状态码
     */
    protected abstract int status();

    /**
     * 获取数据
     *
     * @param request  请求
     * @param response 响应
     * @return 返回数据
     */
    protected abstract Object getData(HttpServletRequest request, HttpServletResponse response);

    /**
     * 判断是否禁止
     *
     * @param request  请求
     * @param response 响应
     * @param data     数据
     * @return 如果禁止返回true，否则返回false
     */
    protected abstract boolean isProhibit(HttpServletRequest request, HttpServletResponse response, Object data);

    /**
     * 返回禁止时的HTML
     *
     * @param request  请求
     * @param response 响应
     * @param data     数据
     * @return 返回HTML
     */
    protected abstract String getProhibitHtml(HttpServletRequest request, HttpServletResponse response, Object data);
}

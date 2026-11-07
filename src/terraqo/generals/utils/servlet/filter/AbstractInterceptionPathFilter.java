/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.servlet.filter;


import com.github.potatoxf.generals.utils.servlet.AdapterFilter;
import com.github.potatoxf.generals.utils.servlet.FilterUrlPatternSupplier;
import com.github.potatoxf.generals.utils.GeneralServlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 拦截指定路径过滤器
 *
 * @author potatoxf
 */
public abstract class AbstractInterceptionPathFilter implements AdapterFilter, FilterUrlPatternSupplier {
    /**
     * 拦截路径
     */
    private Set<String> interceptionPaths;
    /**
     * 是否忽略大小写
     */
    protected boolean isIgnoreCase = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        GeneralServlet.configString(filterConfig, "interceptionPaths", s -> {
            String[] interceptionPathsArray = s.split(",");
            if (interceptionPaths == null) interceptionPaths = new HashSet<>();
            for (String interceptionPath : interceptionPaths) {
                if (interceptionPath != null && !(interceptionPath = interceptionPath.trim()).isEmpty()) {
                    interceptionPaths.add(interceptionPath);
                }
            }
        });
        GeneralServlet.configBoolean(filterConfig, "isIgnoreCase", this::setIgnoreCase);
    }

    @Override
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();
        boolean isHandle = false;
        if (interceptionPaths != null) {
            if (!isIgnoreCase) {
                if (interceptionPaths.contains(servletPath)) {
                    isHandle = true;
                }
            } else {
                for (String interception : interceptionPaths) {
                    if (interception.equalsIgnoreCase(servletPath)) {
                        isHandle = true;
                        break;
                    }
                }
            }
        }
        if (isHandle) {
            doFilter(servletPath, request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

    /**
     * 指定拦截请求路径{@link #interceptionPaths}
     *
     * @param servletPath 请求路径
     * @param request     {@code HttpServletRequest}
     * @param response    {@code HttpServletResponse}
     * @param chain       {@code FilterChain}
     * @throws IOException      if an I/O related error has occurred during the processing
     * @throws ServletException if an exception occurs that interferes with the filter's normal operation
     */
    protected abstract void doFilter(String servletPath, HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

    /**
     * Get the URL patterns that the filter will be registered against. This will replace
     * any previously specified URL patterns.
     *
     * @return the URL patterns
     */
    @Override
    public Set<String> getUrlPatterns() {
        Set<String> result = new HashSet<>();
        if (interceptionPaths == null) {
            result.add("/*");
        } else {
            result.addAll(interceptionPaths);
        }
        return result;
    }

    public Set<String> getInterceptionPaths() {
        return interceptionPaths;
    }

    public void setInterceptionPaths(Set<String> interceptionPaths) {
        this.interceptionPaths = interceptionPaths;
    }

    public boolean isIgnoreCase() {
        return isIgnoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        isIgnoreCase = ignoreCase;
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.servlet.filter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Supplier;


/**
 * 不信任方法过滤器
 *
 * @author potatoxf
 */
public class DistrustMethodFilter extends AbstractProhibitionFilter {
    private final Set<String>      distrustMethodSet = new CopyOnWriteArraySet<>();
    private final Supplier<String> distrustMethodHtmlSupplier;

    public DistrustMethodFilter() {
        this(() -> "Method Not Allowed!", StandardCharsets.UTF_8);
    }

    public DistrustMethodFilter(Supplier<String> distrustMethodHtmlSupplier, Charset charset) {
        super(charset);
        this.distrustMethodHtmlSupplier = Objects.requireNonNull(distrustMethodHtmlSupplier);
    }

    public void addDistrustMethods(String... methods) {
        Collections.addAll(distrustMethodSet, methods);
    }

    @Override
    protected int status() {
        return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
    }

    @Override
    protected Object getData(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    protected boolean isProhibit(HttpServletRequest request, HttpServletResponse response, Object data) {
        return distrustMethodSet.contains(request.getMethod());
    }

    @Override
    protected String getProhibitHtml(HttpServletRequest request, HttpServletResponse response, Object data) {
        return distrustMethodHtmlSupplier.get();
    }
}

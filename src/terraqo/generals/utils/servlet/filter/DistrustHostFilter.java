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
import java.util.function.Function;

/**
 * 不信任主机过滤器
 *
 * @author potatoxf
 */
public class DistrustHostFilter extends AbstractProhibitionFilter {
    private final Set<String> distrustHostSet = new CopyOnWriteArraySet<>();
    private final Function<String, String> distrustHostHtmlSupplier;

    public DistrustHostFilter() {
        this((host) -> "The [" + host + "] is not a trust host", StandardCharsets.UTF_8);
    }

    public DistrustHostFilter(Function<String, String> distrustHostHtmlSupplier, Charset charset) {
        super(charset);
        this.distrustHostHtmlSupplier = Objects.requireNonNull(distrustHostHtmlSupplier);
    }

    public void addDistrustHosts(String... distrustHosts) {
        if (distrustHosts != null) {
            Collections.addAll(distrustHostSet, distrustHosts);
        }
    }

    @Override
    protected int status() {
        return HttpServletResponse.SC_FORBIDDEN;
    }

    @Override
    protected Object getData(HttpServletRequest request, HttpServletResponse response) {
        return request.getRequestURL().toString();
    }

    @Override
    protected boolean isProhibit(HttpServletRequest request, HttpServletResponse response, Object data) {
        return distrustHostSet.contains((String) data);
    }

    @Override
    protected String getProhibitHtml(HttpServletRequest request, HttpServletResponse response, Object data) {
        return distrustHostHtmlSupplier.apply((String) data);
    }
}

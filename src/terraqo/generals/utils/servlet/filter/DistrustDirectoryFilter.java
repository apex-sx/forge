/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.servlet.filter;

import com.github.potatoxf.generals.utils.GeneralIF;
import com.github.potatoxf.generals.utils.GeneralString;
import com.github.potatoxf.generals.utils.Safe;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 不信任目录过滤器
 *
 * @author potatoxf
 */
public class DistrustDirectoryFilter extends AbstractProhibitionFilter {
    private final Set<String>                          distrustDirectorySet = new CopyOnWriteArraySet<>();
    private final Supplier<String>                     distrustDirectoryHtmlSupplier;
    private final Function<HttpServletRequest, String> urlGetter;

    public DistrustDirectoryFilter(Function<HttpServletRequest, String> urlGetter) {
        this(urlGetter, () -> "Preview source files from untrusted directories, please stop accessing!", StandardCharsets.UTF_8);
    }

    public DistrustDirectoryFilter(Function<HttpServletRequest, String> urlGetter, Supplier<String> distrustDirectoryHtmlSupplier, Charset charset) {
        super(charset);
        this.distrustDirectoryHtmlSupplier = Objects.requireNonNull(distrustDirectoryHtmlSupplier);
        this.urlGetter = urlGetter;
    }

    public void addDistrustDirectories(String... distrustDirectories) {
        for (String distrustDirectory : distrustDirectories) {
            if (distrustDirectory == null || distrustDirectory.isEmpty()) continue;
            distrustDirectorySet.add(Safe.newPathString(distrustDirectory,"/"));
        }
    }

    @Override
    protected int status() {
        return HttpServletResponse.SC_FORBIDDEN;
    }

    @Override
    protected Object getData(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    protected boolean isProhibit(HttpServletRequest request, HttpServletResponse response, Object data) {
        String urlString = urlGetter.apply(request);
        if (urlString == null) return false;
        //判断URL是否合法
        if (!GeneralIF.isHttpOrHttpsUrl(urlString)) return true;
        try {
            URL url = new URL(urlString);
            String filePath = Safe.newPathString(url.getPath());
            for (String distrustDirectory : distrustDirectorySet) {
                return filePath.startsWith(distrustDirectory);
            }
            return false;
        } catch (Throwable e) {
            return true;
        }
    }

    @Override
    protected String getProhibitHtml(HttpServletRequest request, HttpServletResponse response, Object data) {
        return distrustDirectoryHtmlSupplier.get();
    }
}

/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.servlet.filter;

import com.github.potatoxf.generals.Log;
import com.github.potatoxf.generals.utils.servlet.AdapterFilter;
import com.github.potatoxf.generals.utils.GeneralString;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * url映射转发
 *
 * @author potatoxf
 */
@WebFilter(filterName = "urlMappingForwardFilter", urlPatterns = "/*",
        initParams = {
                @WebInitParam(name = UrlMappingForwardFilter.FULL_CLASSPATH, value = UrlMappingForwardFilter.URL_FULL_MAPPING_PROPERTIES),
                @WebInitParam(name = UrlMappingForwardFilter.PREFIX_CLASSPATH, value = UrlMappingForwardFilter.URL_PREFIX_MAPPING_PROPERTIES)
        }
)
public class UrlMappingForwardFilter implements AdapterFilter {
    public static final String FULL_CLASSPATH = "url-full-mapping-properties-classpath";
    public static final String PREFIX_CLASSPATH = "url-prefix-mapping-properties-classpath";
    public static final String URL_FULL_MAPPING_PROPERTIES = "url-full-mapping.properties";
    public static final String URL_PREFIX_MAPPING_PROPERTIES = "url-prefix-mapping.properties";
    private volatile Properties urlFullMapping;
    private volatile Properties urlPrefixMapping;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String fullClasspath = filterConfig.getInitParameter(UrlMappingForwardFilter.FULL_CLASSPATH);
        if (fullClasspath == null) {
            fullClasspath = UrlMappingForwardFilter.URL_FULL_MAPPING_PROPERTIES;
        }
        try {
            Properties properties = new Properties();
            properties.load(ClassLoader.getSystemResourceAsStream(fullClasspath));

            urlFullMapping = properties;
        } catch (Throwable e) {
            if (Log.isEnabledWarn()) {
                Log.warn("Can't load url full mapping properties [%s]",fullClasspath, e);
            }
        }
        String prefixClasspath = filterConfig.getInitParameter(UrlMappingForwardFilter.PREFIX_CLASSPATH);
        if (prefixClasspath == null) {
            prefixClasspath = UrlMappingForwardFilter.URL_PREFIX_MAPPING_PROPERTIES;
        }
        try {
            Properties properties = new Properties();
            properties.load(ClassLoader.getSystemResourceAsStream(prefixClasspath));
            urlPrefixMapping = properties;
        } catch (Throwable e) {
            if (Log.isEnabledWarn()) {
                Log.warn("Can't load url prefix mapping properties [%s]",prefixClasspath, e);
            }
        }
        if (Log.isEnabledDebug()) {
            if (urlFullMapping != null) {
                Log.debug("URL FULL MAPPING: ");
                if (urlFullMapping.isEmpty()) {
                    Log.debug("\tEmpty");
                } else {
                    for (Object key : urlFullMapping.keySet()) {
                        Log.debug("\t" + key + " ==> " + urlFullMapping.get(key));
                    }
                }
            }

            if (urlPrefixMapping != null) {
                Log.debug("URL PREFIX MAPPING: ");
                if (urlPrefixMapping.isEmpty()) {
                    Log.debug("\tEmpty");
                } else {
                    for (Object key : urlPrefixMapping.keySet()) {
                        Log.debug("\t" + key + " ==> " + urlPrefixMapping.get(key));
                    }
                }
            }

        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String contextPath = httpServletRequest.getContextPath();
        String servletPath = httpServletRequest.getServletPath();
        String requestURI = httpServletRequest.getRequestURI();


        String toUrl = null;

        // 处理url全等映射
        if (urlFullMapping != null && !urlFullMapping.isEmpty()) {
            toUrl = urlFullMapping.getProperty(servletPath);
            if (toUrl != null) {
                toUrl = contextPath + handleUrl(toUrl);
            } else {
                toUrl = urlFullMapping.getProperty(requestURI);
            }
        }


        if (toUrl != null) {
            toUrl = handleUrl(toUrl);
        } else {
            if (urlPrefixMapping != null && !urlPrefixMapping.isEmpty()) {
                for (Object key : urlPrefixMapping.keySet()) {
                    String urlPrefix = String.valueOf(key);
                    if (servletPath.startsWith(urlPrefix)) {
                        Object property = urlPrefixMapping.get(key);
                        if (property != null) {
                            toUrl = handleUrl(property) + handleUrl(servletPath.substring(urlPrefix.length()));
                            toUrl = contextPath + toUrl;
                        }
                        break;
                    } else if (requestURI.startsWith(urlPrefix)) {
                        Object property = urlPrefixMapping.get(key);
                        if (property != null) {
                            toUrl = handleUrl(property) + handleUrl(requestURI.substring(urlPrefix.length()));
                        }
                        break;
                    }
                }
            }
        }

        if (toUrl != null) {
            if (Log.isEnabledInfo()) {
                Log.info(String.format("The URL forward from [%s] , to [%s]", requestURI, toUrl));
            }
            httpServletRequest.getRequestDispatcher(toUrl).forward(httpServletRequest, httpServletResponse);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String handleUrl(Object object) {
        return "/" + GeneralString.trimCharacter(String.valueOf(object), " /\\");
    }

    @Override
    public void destroy() {
        urlFullMapping = null;
        urlPrefixMapping = null;
    }
}
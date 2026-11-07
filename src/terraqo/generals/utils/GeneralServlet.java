/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;


import com.github.potatoxf.generals.Log;
import com.github.potatoxf.generals.module.base_environment.CurrentHttpServlet;

import javax.servlet.FilterConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author potatoxf
 */
public interface GeneralServlet {
    String UNKNOWN = "unknown";

    /**
     * 解析参数通过同一前缀
     *
     * @param request 请求
     * @param prefix  前缀
     * @param setting 设置函数
     */
    static void parseParameterBySamePrefix(HttpServletRequest request, String prefix, BiConsumer<String, String[]> setting) {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String[] value = request.getParameterValues(name);
            if (GeneralIF.isNoEmpty(prefix)) {
                if (name.startsWith(prefix)) {
                    name = name.substring(prefix.length());
                }
            }
            setting.accept(name, value);
        }
    }

    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    static String renderJsonString(HttpServletResponse response, String string, Charset charset) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding(charset.name());
            response.getWriter().print(string);
        } catch (IOException e) {
            Log.error("Error to renderString", e);
        }
        return null;
    }

    /**
     * 是否是Ajax异步请求
     */
    static boolean isAjaxRequest(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.contains("application/json")) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.contains("XMLHttpRequest")) {
            return true;
        }

        String uri = request.getRequestURI();
        if (GeneralIF.isContainAny(uri, ".json", ".xml")) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        if (GeneralIF.isContainAny(ajax, "json", "xml")) {
            return true;
        }

        return false;
    }

    static String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取Integer参数
     */
    static int getParameterToInt(String name) {
        return Integer.parseInt(getRequest().getParameter(name));
    }

    static String getDomain(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String contextPath = request.getServletContext().getContextPath();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).append(contextPath).toString();
    }

    /**
     * 获取String参数
     */
    static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 获取request
     */
    static HttpServletRequest getRequest() {
        return CurrentHttpServlet.getsHttpServletRequest();
    }

    /**
     * 获取response
     */
    static HttpServletResponse getResponse() {
        return CurrentHttpServlet.getsHttpServletResponse();
    }

    /**
     * 获取session
     */
    static HttpSession getSession() {
        return CurrentHttpServlet.getsHttpSession();
    }

    /**
     * 提取完整的URL
     *
     * @param request          请求
     * @param includeParameter 是否包括参数
     * @return 返回完整的URL
     */
    static String extractURL(HttpServletRequest request, boolean includeParameter) {
        // 获取协议
        String scheme = request.getScheme();
        // 获取服务器名
        String serverName = request.getServerName();
        // 获取服务器端口，如果不是默认端口则需要包含它
        int serverPort = request.getServerPort();
        // 获取请求的URI部分
        String uri = request.getRequestURI();
        // 获取查询字符串（如果有）
        String queryString = request.getQueryString();
        // 构建URL，如果端口是默认端口，则不需要添加端口号到URL中
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        if (serverPort != 80 && serverPort != 443) { // 默认HTTP端口是80，HTTPS是443，如果不是这些端口，则需要添加端口号
            url.append(":").append(serverPort);
        }
        url.append(uri);
        if (includeParameter && queryString != null) { // 如果存在查询字符串，则添加它
            url.append("?").append(queryString);
        }
        return url.toString();
    }

    /**
     * 获取请求来源的IP,可以适应前置部署有Nginx等反向代理软件等的情况. HTTP_CLIENT_IP 无法伪造，所以放在第一个
     * <p>
     *
     * <p>
     * headerNames参数用于自定义检测的Header<br>
     * 需要注意的是，使用此方法获取的客户IP地址必须在Http服务器（例如Nginx）中配置头信息，否则容易造成IP伪造
     * </p>
     *
     * @param request          请求对象{@link HttpServletRequest}
     * @param otherHeaderNames 自定义头，通常在Http服务器（例如Nginx）中配置
     * @return 获取请求来源的IP
     */
    static String getRequestSourceIp(HttpServletRequest request, String... otherHeaderNames) {
        String ip = request.getHeader("HTTP_CLIENT_IP");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-FORWARDED-FOR");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        for (String header : otherHeaderNames) {
            ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
                break;
            }
        }
        ip = request.getRemoteAddr();
        // 多级反向代理检测
        if (ip != null && ip.indexOf(',') > 0) {
            List<String> ips = GeneralString.splitString(ip, ",", " \t\r\n", s -> s);
            for (String subIp : ips) {
                if (!UNKNOWN.equalsIgnoreCase(subIp)) {
                    ip = subIp;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * @param request 请求
     * @return ua
     */
    static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }

    /**
     * 返回附件
     *
     * @param response 响应
     * @param filename 文件名
     * @param content  附件内容
     * @throws IOException 如果发生异常
     */
    static void writeAttachment(HttpServletResponse response,
                                byte[] content,
                                String filename,
                                String filenameEncoding) throws IOException {
        // 设置 header 和 contentType
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, filenameEncoding));
        response.setContentType("application/octet-stream");
        // 输出附件
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(content);
    }

    static String getCookiePath(HttpServletRequest request) {
        return getCookiePath(request, "/");
    }

    static String getCookiePath(HttpServletRequest request, String defaultPath) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : defaultPath;
    }

    static void cancelCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath(getCookiePath(request));
        response.addCookie(cookie);
    }

    static String charsetName(HttpServletRequest request, Charset defaultCharset) {
        String encoding = request.getParameter("encoding");
        String name;
        try {
            name = Charset.forName(encoding).name();
            if (Log.isEnabledDebug()) {
                Log.debug("Return encoding [" + name + "] from request parameter with key [encoding]");
            }
            return name;
        } catch (Throwable ignored) {
        }
        encoding = request.getCharacterEncoding();
        try {
            name = Charset.forName(encoding).name();
            if (Log.isEnabledDebug()) {
                Log.debug("Return encoding [" + name + "] from request CharacterEncoding");
            }
            return name;
        } catch (Throwable ignored) {
        }
        if (defaultCharset != null) {
            name = defaultCharset.name();
            if (Log.isEnabledDebug()) {
                Log.debug("Return encoding [" + name + "] from defaultCharset");
            }
            return name;
        }
        name = StandardCharsets.UTF_8.name();
        if (Log.isEnabledDebug()) {
            Log.debug("Return default encoding [" + name + "]");
        }
        return name;
    }

    /**
     * 配置指定值
     *
     * @param filterConfig 过滤器配置
     * @param key          参数键
     * @param setter       属性设置器
     */
    static void configString(FilterConfig filterConfig, String key, Consumer<String> setter) {
        String value = filterConfig.getInitParameter(key);
        if (value != null) {
            setter.accept(value);
        }
    }

    /**
     * 配置指定值
     *
     * @param filterConfig 过滤器配置
     * @param key          参数键
     * @param setter       属性设置器
     */
    static void configBoolean(FilterConfig filterConfig, String key, Consumer<Boolean> setter) {
        String value = filterConfig.getInitParameter(key);
        if (value != null) {
            setter.accept(Boolean.parseBoolean(value));
        }
    }

    /**
     * 配置指定值
     *
     * @param filterConfig 过滤器配置
     * @param key          参数键
     * @param setter       属性设置器
     */
    static void configInteger(FilterConfig filterConfig, String key, Consumer<Integer> setter) {
        String value = filterConfig.getInitParameter(key);
        if (value != null) {
            try {
                setter.accept(Integer.parseInt(value));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    /**
     * 配置指定值
     *
     * @param filterConfig 过滤器配置
     * @param key          参数键
     * @param setter       属性设置器
     */
    static void configLong(FilterConfig filterConfig, String key, Consumer<Long> setter) {
        String value = filterConfig.getInitParameter(key);
        if (value != null) {
            try {
                setter.accept(Long.parseLong(value));
            } catch (NumberFormatException ignored) {
            }
        }
    }

    /**
     * 配置指定值
     *
     * @param filterConfig 过滤器配置
     * @param key          参数键
     * @param setter       属性设置器
     */
    static void configDouble(FilterConfig filterConfig, String key, Consumer<Double> setter) {
        String value = filterConfig.getInitParameter(key);
        if (value != null) {
            try {
                setter.accept(Double.parseDouble(value));
            } catch (NumberFormatException ignored) {
            }
        }
    }
}

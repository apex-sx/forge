/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.servlet.filter;

import com.github.potatoxf.generals.utils.GeneralServlet;
import com.github.potatoxf.generals.utils.text.CaptchaGenerator;
import com.github.potatoxf.generals.utils.text.TextImageData;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Objects;

/**
 * 验证码异常
 *
 * @author potatoxf
 */
public class CaptchaInterceptionPathFilter extends AbstractInterceptionPathFilter {
    /**
     * 验证码生成期
     */
    private final CaptchaGenerator captchaGenerator;
    /**
     * 验证码缓存键
     */
    private volatile String captchaKey = CaptchaInterceptionPathFilter.class + ".SESSION_KEY_CAPTCHA";
    /**
     * 验证码时间缓存键
     */
    private volatile String captchaTimeKey = CaptchaInterceptionPathFilter.class + ".SESSION_KEY_CAPTCHA_TIME";
    /**
     * 验证路径
     */
    private volatile String verifyPath = "/login";
    /**
     * 验证用户输入参数名
     */
    private volatile String verifyUserInputParameter = "verifyResult";
    /**
     * 验证结果缓存键
     */
    private volatile String verifyResultKey = CaptchaInterceptionPathFilter.class + ".SESSION_KEY_VERIFY_RESULT";
    /**
     * 刷新间隔差值
     */
    private volatile long flushIntervalTime = 1000 * 30;
    /**
     * 唯一标识参数名
     */
    private volatile String uniqueParameter = "timestamp";
    /**
     * 唯一标识缓存键
     */
    private volatile String uniqueKey = CaptchaInterceptionPathFilter.class + ".SESSION_KEY_UNIQUE";

    public CaptchaInterceptionPathFilter(CaptchaGenerator captchaGenerator) {
        this.captchaGenerator = captchaGenerator;
        setInterceptionPaths(Collections.singleton("/captcha"));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        super.init(filterConfig);
        GeneralServlet.configString(filterConfig, "captchaKey", this::setCaptchaKey);
        GeneralServlet.configString(filterConfig, "captchaTimeKey", this::setCaptchaTimeKey);
        GeneralServlet.configString(filterConfig, "verifyPath", this::setVerifyPath);
        GeneralServlet.configString(filterConfig, "verifyUserInputParameter", this::setVerifyUserInputParameter);
        GeneralServlet.configString(filterConfig, "verifyResultKey", this::setVerifyResultKey);
        GeneralServlet.configInteger(filterConfig, "flushIntervalTime", this::setFlushIntervalTime);
        GeneralServlet.configString(filterConfig, "uniqueParameter", this::setUniqueParameter);
        GeneralServlet.configString(filterConfig, "uniqueKey", this::setUniqueKey);
    }

    @Override
    protected void doFilter(String servletPath, HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession httpSession = request.getSession();
        String captchaObject = (String) httpSession.getAttribute(captchaKey);
        TextImageData captcha;
        //如果是验证路径
        if (isIgnoreCase ? servletPath.equalsIgnoreCase(verifyPath) : servletPath.equals(verifyPath)) {
            //如果已经有了验证码
            if (captchaObject != null) {
                captcha = TextImageData.ofBase64String(captchaObject);
                if (Objects.equals(captcha.getText(), request.getParameter(verifyUserInputParameter))) {
                    httpSession.setAttribute(verifyResultKey, true);
                }
            }
            chain.doFilter(request, response);
            return;
        }
        // Set to expire far in the past.
        response.setDateHeader("Expires", -1);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        Long captchaTimeObject = (Long) httpSession.getAttribute(captchaTimeKey);
        boolean isFlush = true;
        String unique = request.getParameter(uniqueParameter);
        String last = (String) httpSession.getAttribute(uniqueKey);
        if ((unique == null && last == null) || (last != null && last.equals(unique))) {
            isFlush = false;
        }
        if (unique != null) {
            httpSession.setAttribute(uniqueKey, unique);
        }
        if (isFlush || captchaObject == null || captchaTimeObject == null || System.currentTimeMillis() - captchaTimeObject >= flushIntervalTime) {
            captcha = captchaGenerator.generate();
            // store the text in the session
            httpSession.setAttribute(captchaKey, captcha.toBase64String());
            // store the date in the session so that it can be compared
            // against to make sure someone hasn't taken too long to enter
            // their kaptcha
            httpSession.setAttribute(captchaTimeKey, System.currentTimeMillis());
        } else {
            captcha = TextImageData.ofBase64String(captchaObject);
        }
        response.setContentType(URLConnection.guessContentTypeFromName("image." + captcha.getFormat()));
        response.setStatus(200);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(captcha.getImage());
        outputStream.close();
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public void setCaptchaTimeKey(String captchaTimeKey) {
        this.captchaTimeKey = captchaTimeKey;
    }

    public void setVerifyPath(String verifyPath) {
        this.verifyPath = verifyPath;
    }

    public void setVerifyUserInputParameter(String verifyUserInputParameter) {
        this.verifyUserInputParameter = verifyUserInputParameter;
    }

    public void setVerifyResultKey(String verifyResultKey) {
        this.verifyResultKey = verifyResultKey;
    }

    public void setFlushIntervalTime(long flushIntervalTime) {
        this.flushIntervalTime = flushIntervalTime;
    }

    public void setUniqueParameter(String uniqueParameter) {
        this.uniqueParameter = uniqueParameter;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }
}

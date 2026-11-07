/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import com.github.potatoxf.generals.Log;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 国际化
 *
 * @author potatoxf
 */
public interface Internationalization {
    /**
     * 消息基础名称
     */
    String MESSAGE_BASE_NAME = "[META-INF/i18n/messages]";

    /**
     * 国际化基础名称
     *
     * @return 国际化基础名称
     */
    String baseName();

    /**
     * 国际化键
     *
     * @return 国际化键
     */
    String key();

    /**
     * 国际化默认内容
     *
     * @return 国际化默认内容
     */
    String content();

    /**
     * 消息参数
     *
     * @return 消息参数
     */
    default Object[] args() {
        return new Object[0];
    }

    /**
     * 默认{@link Locale}
     *
     * @return 默认{@link Locale}
     */
    default Locale locale() {
        return Locale.getDefault();
    }

    /**
     * 获取内容
     *
     * @return 返回描述
     */
    default String getContent() {
        return this.getContent(locale());
    }

    /**
     * 获取内容
     *
     * @param locale 本地化配置
     * @return 返回内容
     */
    default String getContent(Locale locale) {
        ResourceBundle resourceBundle = null;
        String baseName = this.baseName();
        if (baseName != null) {
            if (locale == null) {
                locale = this.locale();
            }
            try {
                resourceBundle = locale == null ? ResourceBundle.getBundle(baseName) : ResourceBundle.getBundle(baseName, locale);
            } catch (Throwable e) {
                if (Log.isEnabledWarn()) {
                    Log.warn("Error to get ResourceBundle", e);
                }
            }
        }
        return this.getContent(resourceBundle);
    }

    /**
     * 获取内容
     *
     * @param resourceBundle i18语言资源包
     * @return 返回描述
     */
    default String getContent(ResourceBundle resourceBundle) {
        String string = null;
        if (resourceBundle != null) {
            String key = key();
            if (key != null && resourceBundle.containsKey(key)) {
                string = resourceBundle.getString(key);
            }
        }
        if (string == null) {
            string = this.content();
        }
        if (string != null) {
            Object[] args = args();
            if (args != null) {
                string = MessageFormat.format(string, args);
            }
        }
        return string;
    }

    /**
     * 获取内容json字符串
     * <p>
     * {"zh":"...","en:"..."}
     *
     * @param locales 本地化配置
     * @return 返回内容json字符串
     */
    default String getContentJsonString(Locale... locales) {
        Collection<Locale> set = new LinkedHashSet<>();
        if (locales != null) {
            for (Locale locale : locales) {
                if (locale == null) continue;
                set.add(locale);
            }
        }
        SimpleJSONObjectStringBuilder builder = new SimpleJSONObjectStringBuilder();
        for (Locale locale : set) {
            String key = locale.getLanguage();
            String value = getContent(locale);
            builder.add(key, value);
        }
        return builder.toString();
    }
}

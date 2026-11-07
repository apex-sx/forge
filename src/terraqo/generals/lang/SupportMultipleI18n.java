/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.lang;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 支持国家化语言列表
 *
 * @author potatoxf
 */
@SuppressWarnings("UnusedReturnValue")
public final class SupportMultipleI18n implements Internationalization {
    /**
     * 支持国家化语言列表
     */
    private final Map<String, SupportI18n> typeMap = new LinkedHashMap<>();

    /**
     * 国际化基础名称
     */
    private final String baseName;

    public SupportMultipleI18n(String baseName) {
        this.baseName = baseName;
    }

    /**
     * 构造异常支持国家化语言
     *
     * @param key     国际化键
     * @param content 国际化默认内容
     * @return {@link SupportI18n}
     */
    public SupportMultipleI18n addSupportI18n(String key, String content, Object... args) {
        this.typeMap.put(key, new SupportI18n(baseName, key, content, args));
        return this;
    }

    public boolean isEmpty() {
        return this.typeMap.isEmpty();
    }

    /**
     * 国际化基础名称
     *
     * @return 国际化基础名称
     */
    @Override
    public String baseName() {
        return baseName;
    }

    /**
     * 国际化键
     *
     * @return 国际化键
     */
    @Override
    public String key() {
        return "[" + typeMap.values().stream().map(e -> "\"" + e.key() + "\"").collect(Collectors.joining(",")) + "]";
    }

    /**
     * 国际化默认内容
     *
     * @return 国际化默认内容
     */
    @Override
    public String content() {
        return "[" + typeMap.values().stream().map(e -> "\"" + e.content() + "\"").collect(Collectors.joining(",")) + "]";
    }

    /**
     * 获取内容
     *
     * @return 返回描述
     */
    @Override
    public String getContent() {
        return "[" + typeMap.values().stream().map(e -> "\"" + e.getContent() + "\"").collect(Collectors.joining(",")) + "]";
    }

    /**
     * 获取内容
     *
     * @param locale 本地化配置
     * @return 返回描述
     */
    @Override
    public String getContent(Locale locale) {
        return "[" + typeMap.values().stream().map(e -> "\"" + e.getContent(locale) + "\"").collect(Collectors.joining(",")) + "]";
    }

    /**
     * 获取内容
     *
     * @param resourceBundle i18语言资源包
     * @return 返回描述
     */
    @Override
    public String getContent(ResourceBundle resourceBundle) {
        return "[" + typeMap.values().stream().map(e -> "\"" + e.getContent(resourceBundle) + "\"").collect(Collectors.joining(",")) + "]";
    }

    /**
     * 获取内容json字符串
     * <p>
     * {"type1":{"zh":"...","en:"..."},"type2":{"zh":"...","en:"..."}}
     *
     * @param locales 本地化配置
     * @return 返回内容json字符串
     */
    @Override
    public String getContentJsonString(Locale... locales) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        this.typeMap.forEach((k, v) -> {
            sb.append("\"").append(k).append("\"").append(":").append(v.getContentJsonString(locales)).append(",");
        });
        sb.setLength(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

    /**
     * 将{@link SupportMultipleI18n}转成json数组字符串
     *
     * @return 返回json数组字符串
     */
    @Override
    public String toString() {
        return getContentJsonString(Locale.CHINESE, Locale.ENGLISH);
    }
}

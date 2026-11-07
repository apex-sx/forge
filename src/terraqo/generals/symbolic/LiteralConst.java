/*
 * Copyright (c) 2025.
 */
package com.github.potatoxf.generals.symbolic;


import com.github.potatoxf.generals.lang.SupportI18n;
import com.github.potatoxf.generals.utils.GeneralAnnotation;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 常量
 *
 * @author potatoxf
 */
public interface LiteralConst<T extends LiteralConst<T>> extends This<T>, Comparable<T> {
    /**
     * 获取{@code SupportI18n}
     *
     * @return 返回{@code SupportI18n}
     */
    SupportI18n supportI18n();

    /**
     * 通过身份牌查找
     *
     * @return 返回唯一身份牌
     */
    default int getCode() {
        if (this$() instanceof Enum) {
            return ((Enum<?>) this$()).ordinal();
        }
        List<LiteralCode> annotations = GeneralAnnotation.lookupAnnotation(getClass(), LiteralCode.class);
        if (!annotations.isEmpty()) {
            LiteralCode annotation = annotations.get(0);
            if (annotation != null) {
                return annotation.value();
            }
        }
        throw new UnsupportedOperationException("No find class code by annotation Literals");
    }

    /**
     * 通过唯一名称查找
     *
     * @return 返回名称
     */
    default String getName() {
        if (this$() instanceof Enum) {
            return ((Enum<?>) this$()).name();
        }
        throw new UnsupportedOperationException("No Enum class please override");
    }

    /**
     * 获取内容
     *
     * @return 返回描述
     */
    default String getContent() {
        return supportI18n().getContent();
    }

    /**
     * 获取内容
     *
     * @param locale 本地化配置
     * @return 返回描述
     */
    default String getContent(Locale locale) {
        return supportI18n().getContent(locale);
    }

    /**
     * 获取内容
     *
     * @param resourceBundle i18语言资源包
     * @return 返回描述
     */
    default String getContent(ResourceBundle resourceBundle) {
        return supportI18n().getContent(resourceBundle);
    }

    /**
     * 优先级
     *
     * @param other 另一个元素
     * @return 返回比较值
     */
    @Override
    default int compareTo(T other) {
        return Integer.compare(getCode(), other.getCode());
    }

}

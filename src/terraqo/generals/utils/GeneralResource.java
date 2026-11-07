/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;


import com.github.potatoxf.generals.Log;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author potatoxf
 */
public interface GeneralResource {

    /**
     * 获取 {@link ResourceBundle}
     *
     * @param isThrow      是否抛出移除
     * @param i18nBaseName 国际化语言资源包基础名
     * @param locale       国际化语言位置
     * @return 返回 {@link ResourceBundle}
     */
    static ResourceBundle getResourceBundle(boolean isThrow, String i18nBaseName, Locale locale) {
        try {
            return locale == null ? ResourceBundle.getBundle(i18nBaseName) : ResourceBundle.getBundle(i18nBaseName, locale);
        } catch (MissingResourceException e) {
            return Log.warnOrThrowError(isThrow, e, "Error to getting resource from '" + i18nBaseName + ".properties' " + (locale != null ? "with '" + locale + "'" : ""));
        }
    }
}

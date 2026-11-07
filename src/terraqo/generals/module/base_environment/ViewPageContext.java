/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.module.base_environment;

import java.util.Map;

/**
 * 视图页面环境
 *
 * @author potatoxf
 */
public interface ViewPageContext {
    /**
     * 设置视图名称
     *
     * @param viewName 视图名称
     * @return this
     */
    ViewPageContext setViewName(String viewName);

    /**
     * 设置视图名称
     *
     * @param attributeName  属性名称
     * @param attributeValue 属性值
     * @return this
     */
    ViewPageContext addObject(String attributeName, Object attributeValue);

    /**
     * 设置视图名称
     *
     * @param modelMap 属性Map
     * @return this
     */
    ViewPageContext addAllObjects(Map<String, ?> modelMap);

    /**
     * 复制到一个对象内容
     *
     * @param object 输入对象
     * @param <T>    对象类型
     * @return 返回输入对象
     */
    <T> T copyTo(T object);

    /**
     * 转换环境内容
     *
     * @param <T> 子类实现类型
     * @return 返回环境内容
     */
    <T> T toContext();
}

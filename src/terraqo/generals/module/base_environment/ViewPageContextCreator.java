/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.module.base_environment;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 视图页面环境创建器
 *
 * @author potatoxf
 */
public interface ViewPageContextCreator {
    AtomicReference<ViewPageContextCreator> GLOBAL = new AtomicReference<>();

    static ViewPageContextCreator global() {
        ViewPageContextCreator value = GLOBAL.get();
        if (value == null) {
            throw new RuntimeException("No set global '" + ViewPageContextCreator.class.getSimpleName() + "' instance");
        }
        return value;
    }

    /**
     * 创建
     *
     * @return 返回当前用户对象
     */
    static ViewPageContext create() {
        try {
            return global().newViewPageContext();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to create the ViewPageContextCreator", e);
        }
    }

    /**
     * 创建
     *
     * @return 返回当前用户对象
     */
    static ViewPageContext create(String viewName) {
        try {
            return global().newViewPageContext(viewName);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to create the ViewPageContextCreator", e);
        }
    }

    default ViewPageContext newViewPageContext() {
        return this.newViewPageContext(null);
    }

    ViewPageContext newViewPageContext(String viewName);
}

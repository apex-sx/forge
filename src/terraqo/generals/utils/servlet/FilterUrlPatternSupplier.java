/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.servlet;

import java.util.Set;

/**
 * 获取URL匹配模式
 *
 * @author potatoxf
 */
public interface FilterUrlPatternSupplier {

    /**
     * Get the URL patterns that the filter will be registered against. This will replace
     * any previously specified URL patterns.
     *
     * @return the URL patterns
     */
    Set<String> getUrlPatterns();
}

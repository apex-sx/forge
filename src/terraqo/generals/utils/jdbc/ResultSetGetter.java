/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link ResultSet}获取器
 *
 * @author potatoxf
 */
@FunctionalInterface
public
interface ResultSetGetter {

    /**
     * 获取指定列值
     *
     * @param resultSet   {@link ResultSet}
     * @param columnIndex 列索引
     * @return 返回获取值
     * @throws SQLException 抛出的异常
     */
    Object apply(ResultSet resultSet, int columnIndex) throws SQLException;
}

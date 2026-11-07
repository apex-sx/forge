/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * {@link PreparedStatement}设置器
 *
 * @author potatoxf
 */
public interface PreparedStatementSetter {
    /**
     * 设置{@link PreparedStatement}参数值
     *
     * @param preparedStatement {@link PreparedStatement}
     * @param parameterIndex    参数索引
     * @param value             要设置的值
     * @throws SQLException 如果发生错误
     */
    void apply(PreparedStatement preparedStatement, int parameterIndex, Object value) throws SQLException;
}
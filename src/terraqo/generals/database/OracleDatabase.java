/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.database;

/**
 * Oracle
 *
 * @author potatoxf
 */
public interface OracleDatabase extends Database {
    OracleDatabase DEFAULT = new OracleDatabase() {
    };

    /**
     * 数据库名称
     *
     * @return 返回数据库名称
     */
    @Override
    default String name() {
        return "Oracle";
    }
}

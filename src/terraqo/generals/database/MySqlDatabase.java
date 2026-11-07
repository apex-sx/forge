/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.database;

/**
 * MySQL
 *
 * @author potatoxf
 */
public interface MySqlDatabase extends Database {
    MySqlDatabase DEFAULT = new MySqlDatabase() {
    };

    /**
     * 数据库名称
     *
     * @return 返回数据库名称
     */
    @Override
    default String name() {
        return "MySql";
    }
}

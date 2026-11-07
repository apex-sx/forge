/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.utils;

import com.github.potatoxf.generals.database.Database;
import com.github.potatoxf.generals.lang.SqlSupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author potatoxf
 */
public interface GeneralSql {

    static String checkSqlEntry(String name, boolean upper) {
        if (name == null) {
            throw new IllegalArgumentException("The sql entry must no null");
        }
        name = name.trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The sql entry must no empty");
        }
        char c = name.charAt(0);
        if (!Character.isLetter(c) && c != '_') {
            throw new IllegalArgumentException("The Character of '" + c + "' no support at 0");
        }
        for (int i = 1; i < name.length(); i++) {
            c = name.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') {
                throw new IllegalArgumentException("The Character of '" + c + "' no support at " + i);
            }
        }
        name = upper ? name.toUpperCase() : name.toLowerCase();
        return name;
    }

    static String checkSqlValue(Object value, boolean upper) {
        return value == null ? (upper ? "NULL" : "null") : "?";
    }

    /**
     * 构建更新或插入Sql
     *
     * @param database 数据库类型
     * @param table    表名
     * @param columns  列名，在id之前的为ID列名
     * @param args     参数，在id之前的为ID参数
     * @param id       主键数量，默认为0，表示在columns与args前id列为主键，如果为0则是插入语句
     * @return 返回Sql与参数
     */
    static SqlSupplier edit(Database database, String table, String[] columns, Object[] args, int id) {
        if (id <= 0) id = 0;
        boolean update = true;
        if (id > 0) {
            for (int i = 0; i < id && update; i++) update = args[i] != null;
        }
        if (id == 0 || !update) {
            return GeneralSql.insert(database, table, columns, args);
        } else {
            return GeneralSql.update(database, table, columns, args, id);
        }
    }

    /**
     * 构建插入Sql
     *
     * @param database 数据库类型
     * @param table    表名
     * @param columns  列名，在id之前的为ID列名
     * @param args     参数，在id之前的为ID参数
     * @return 返回Sql与参数
     */
    static SqlSupplier insert(Database database, String table, String[] columns, Object[] args) {
        if (columns.length != args.length) {
            throw new IllegalArgumentException("The number of column and value no match");
        }
        int length = columns.length;
        StringBuilder sql = new StringBuilder(100);
        sql.append("insert into ").append(checkSqlEntry(table, false));
        sql.append("(").append(checkSqlEntry(columns[0], false));
        for (int i = 1; i < length; i++) {
            sql.append(", ").append(checkSqlEntry(columns[i], false));
        }
        sql.append(") values (").append(checkSqlValue(args[0], false));
        for (int i = 1; i < length; i++) {
            sql.append(", ").append(checkSqlValue(args[i], false));
        }
        sql.append(")");
        return new SqlSupplier(sql.toString(), Arrays.stream(args).filter(Objects::nonNull).toArray());
    }

    /**
     * 构建更新Sql
     *
     * @param database 数据库类型
     * @param table    表名
     * @param columns  列名，在id之前的为ID列名
     * @param args     参数，在id之前的为ID参数
     * @param id       主键数量，默认为1，表示在columns与args前id列为主键
     * @return 返回Sql与参数
     */
    static SqlSupplier update(Database database, String table, String[] columns, Object[] args, int id) {
        if (columns.length != args.length) {
            throw new IllegalArgumentException("The number of column and value no match");
        }
        if (id <= 1) id = 1;
        int length = columns.length;
        if (id >= length) {
            throw new IllegalArgumentException("Specifies that the number of " + id + " primary keys exceeds the number of parameters given " + length);
        }
        StringBuilder sql = new StringBuilder();
        List<Object> list = new ArrayList<>();
        sql.append("update ").append(checkSqlEntry(table, false));
        sql.append(" set ");
        boolean first = true;
        for (int i = id; i < length; i++) {
            if (args[i] != null) {
                if (!first) sql.append(", ");
                else first = false;
                sql.append(checkSqlEntry(columns[i], false)).append(" = ").append(checkSqlValue(args[i], false));
                list.add(args[i]);
            }
        }
        sql.append(" where 1=1");
        for (int i = 0; i < id; i++) {
            if (args[i] != null) {
                sql.append(" and ");
                sql.append(checkSqlEntry(columns[i], false));
                sql.append(" = ?");
                list.add(args[i]);
            }
        }
        return new SqlSupplier(sql.toString(), list.toArray());
    }

    static String createTable(Database database, String table, String[] columnNames, String[] columnConfigs) {
        return GeneralSql.createTable(database, table, columnNames, columnConfigs, null, null);
    }

    static String createTable(Database database, String table, String[] columnNames, String[] columnConfigs, String columnFooter, String tableFooter) {
        if (columnNames.length != columnConfigs.length) {
            throw new IllegalArgumentException("The number of column name and column config no match");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("create table ").append(checkSqlEntry(table, false)).append("( ");
        sql.append(columnNames[0]).append(" ");
        if (GeneralIF.isNoEmpty(columnNames[0])) {
            sql.append(columnConfigs[0]).append(" ");
        }
        for (int i = 1; i < columnNames.length; i++) {
            sql.append(",").append(columnNames[i]).append(" ");
            if (GeneralIF.isNoEmpty(columnNames[i])) {
                sql.append(columnConfigs[i]).append(" ");
            }
        }
        if (GeneralIF.isNoEmpty(columnFooter)) {
            sql.append(",").append(columnFooter).append(" ");
        }
        sql.append(")");
        if (GeneralIF.isNoEmpty(tableFooter)) {
            sql.append(tableFooter);
        }
        return sql.toString();
    }
}

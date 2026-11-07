/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.database;

import com.github.potatoxf.generals.Kit;
import com.github.potatoxf.generals.utils.GeneralSql;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 内置数据库
 *
 * @author potatoxf
 */
@SuppressWarnings("unused")
public abstract class BuiltDataBase {
    /**
     * 是否已初始化
     */
    private static final AtomicBoolean initial = new AtomicBoolean(false);
    /**
     * 数据库文件路径
     */
    private final Path dbPath;
    /**
     * 表名
     */
    private final String table;

    public BuiltDataBase(String table) throws SQLException {
        this("BuiltDataBase.sqlite", table);
    }

    public BuiltDataBase(String dbPath, String table) throws SQLException {
        this(Paths.get(dbPath), table);
    }

    public BuiltDataBase(Path dbPath, String table) throws SQLException {
        this(Kit.RUNTIME_CACHE_PATH, dbPath, table);
    }

    /**
     * @param dirPath 目录路径
     * @param dbPath  数据文件路径，如果是绝对路径则使用该路径，如果是相对路径则与目录路径拼接
     * @param table   表面
     * @throws SQLException 如果发生错误
     */
    private BuiltDataBase(Path dirPath, Path dbPath, String table) throws SQLException {
        this.dbPath = dbPath.isAbsolute() ? dbPath : dirPath.resolve(dbPath);
        this.table = GeneralSql.checkSqlEntry(table, false);
        try (Connection connection = this.connection()) {
            try (Statement statement = connection.createStatement()) {
                //判断是否已经创建表，如果已创建则不初始化标
                try {
                    statement.execute("select 1 from " + this.table);
                    return;
                } catch (SQLException ignored) {
                }
                //创建表
                String sql = GeneralSql.createTable(null, this.table, columnNames(), columnConfigs());
                statement.execute(sql);
                //判断是否已经创建表
                try {
                    statement.execute("select 1 from " + this.table);
                } catch (SQLException ignored) {
                    throw new SQLException("Error to create table '" + this.table + "'");
                }
            }
            String[] columns = columnNames();
            Object[] args = Kit.emptyArguments(columns.length);
            String sql = GeneralSql.insert(null, this.table, columns, args).sql();

            connection.setAutoCommit(false);
            try (PreparedStatement prepareStatement = connection.prepareStatement(sql)) {
                try (BufferedReader bufferedReader = openCSV()) {
                    String line;
                    int i = 0;
                    int len = 5000;
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] data = line.split(",");
                        setData(prepareStatement, data);
                        prepareStatement.addBatch();
                        i++;
                        if (i % len == 0) {
                            prepareStatement.executeBatch();
                        }
                    }
                    if (i % len != 0) {
                        prepareStatement.executeBatch();
                    }
                    connection.commit();
                } catch (IOException e) {
                    throw new SQLException("Error to init table of '" + this.table + "' data", e);
                }
            }
        }
    }

    /**
     * 获取{@link Connection}
     *
     * @param dbPath 数据文件路径
     * @return 返回{@link Connection}
     */
    public static Connection getConnection(Path dbPath) {
        try {
            if (!initial.get()) {
                Class.forName("org.sqlite.JDBC");
                initial.set(true);
            }
            if (!Files.exists(dbPath)) {
                try {
                    Files.createDirectories(dbPath.getParent());
                } catch (IOException ignored) {
                }
                try {
                    Files.createFile(dbPath);
                } catch (IOException ignored) {
                }
            }
            return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 连接{@link Connection}
     *
     * @return 返回{@link Connection}
     */
    public final Connection connection() {
        return BuiltDataBase.getConnection(dbPath);
    }

    /**
     * 获取表名
     *
     * @return 返回表名
     */
    public final String table() {
        return table;
    }

    /**
     * 列名称，与列配置一一对应
     *
     * @return {@code String[]}
     */
    protected abstract String[] columnNames();

    /**
     * 列配置，与列名称一一对应
     *
     * @return {@code String[]}
     */
    protected abstract String[] columnConfigs();

    /**
     * 打开CSV文件
     *
     * @return {@link BufferedReader}
     * @throws IOException 如果出现异常
     */
    protected abstract BufferedReader openCSV() throws IOException;

    /**
     * 设置数据
     *
     * @param prepareStatement {@link PreparedStatement}
     * @param csvRow           CSV行数据
     * @throws SQLException 如果发生异常
     */
    protected abstract void setData(PreparedStatement prepareStatement, String[] csvRow) throws SQLException;
}

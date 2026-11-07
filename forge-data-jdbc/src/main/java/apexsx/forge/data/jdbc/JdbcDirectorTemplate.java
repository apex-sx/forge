package apexsx.forge.data.jdbc;

import apexsx.forge.core.Logger;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JDBC导演模板类，提供数据库操作的模板方法和配置管理
 *
 * @author zhitron
 */
public class JdbcDirectorTemplate {

    /**
     * 结果集管理器实例，用于处理结果集相关操作
     */
    private ResultSetManager resultSetManager = ResultSetManager.DEFAULT;

    /**
     * 预处理语句管理器实例，用于处理预处理语句相关操作
     */
    private PreparedStatementManager preparedStatementManager = PreparedStatementManager.DEFAULT;

    /**
     * 是否忽略SQL警告标志，如果为false则在出现SQL警告时抛出异常
     */
    private boolean ignoreWarnings = true;

    /**
     * fetchSize属性值，用于设置查询语句的fetchSize属性
     * 当值为非负数时生效，默认值为-1表示不设置
     */
    private Integer fetchSize = null;

    /**
     * maxRows属性值，用于设置查询语句的maxRows属性
     * 当值为非负数时生效，默认值为-1表示不设置
     */
    private Integer maxRows = null;

    /**
     * queryTimeout属性值，用于设置查询语句的queryTimeout属性
     * 当值为非负数时生效，默认值为-1表示不设置
     */
    private Integer queryTimeout = null;

    /**
     * 是否跳过结果处理标志，如果为true则跳过所有存储过程调用的结果检查
     * 可用于避免某些旧版Oracle JDBC驱动程序（如10.1.0.2）中的bug
     */
    private boolean skipResultsProcessing = false;

    /**
     * 是否跳过未声明结果标志，如果为true则跳过来自存储过程调用的所有没有对应SqlOutParameter声明的结果
     * 除非skipResultsProcessing变量设置为true，否则其他结果处理仍会进行
     */
    private boolean skipUndeclaredResults = false;

    /**
     * JDBC导演实例，用于执行具体的数据库操作
     */
    private final JdbcDirector jdbcDirector;

    /**
     * 构造一个新的JdbcDirectorTemplate实例
     *
     * @param jdbcDirector JDBC导演实例，用于执行具体的数据库操作，不能为null
     */
    public JdbcDirectorTemplate(JdbcDirector jdbcDirector) {
        this.jdbcDirector = Objects.requireNonNull(jdbcDirector);
    }

    /**
     * 执行查询并返回单个对象结果。
     * <p>
     * 该方法专门用于查询预期返回单行结果的场景。它会调用底层的query方法，
     * 并设置multiplyResult参数为false，表示只处理第一行结果。
     * 如果查询结果为空，将返回null；如果有多个结果，只会返回第一个。
     * 此方法使用默认的RESULT_DATA_ROW_MAPPING作为行映射函数。
     *
     * @param <T>     返回对象的类型
     * @param context 连接数据源相关信息
     * @param sql     要执行的SQL语句
     * @param params  SQL中的命名参数映射
     * @param args    SQL中的位置参数列表
     * @param type    返回对象的Class类型
     * @return 查询结果对象，如果无结果则返回null
     */
    public final <T> T[] findArray(Object context, String sql, Map<String, Object> params, List<Object> args, Class<T> type) {
        return checkSelectSQL(sql).queryObject(context, sql, params, args, createExtractorWithAllColumnToArray(type));
    }

    /**
     * 执行查询并返回单个对象结果。
     * <p>
     * 该方法专门用于查询预期返回单行结果的场景。它会调用底层的query方法，
     * 并设置multiplyResult参数为false，表示只处理第一行结果。
     * 如果查询结果为空，将返回null；如果有多个结果，只会返回第一个。
     * 此方法使用默认的RESULT_DATA_ROW_MAPPING作为行映射函数。
     *
     * @param <T>     返回对象的类型
     * @param context 连接数据源相关信息
     * @param sql     要执行的SQL语句
     * @param params  SQL中的命名参数映射
     * @param args    SQL中的位置参数列表
     * @param type    返回对象的Class类型
     * @return 查询结果对象，如果无结果则返回null
     */
    public final <T> T findObject(Object context, String sql, Map<String, Object> params, List<Object> args, Class<T> type) {
        return checkSelectSQL(sql).queryObject(context, sql, params, args, createExtractorWithSingleColumn(type));
    }

    /**
     * 执行查询并返回单个Map结果。
     * <p>
     * 该方法专门用于查询预期返回单行结果的场景。它会调用底层的query方法，
     * 并设置multiplyResult参数为false，表示只处理第一行结果。
     * 如果查询结果为空，将返回null；如果有多个结果，只会返回第一个。
     * 此方法使用默认的RESULT_DATA_ROW_MAPPING作为行映射函数。
     *
     * @param context 连接数据源相关信息
     * @param sql     要执行的SQL语句
     * @param params  SQL中的命名参数映射
     * @param args    SQL中的位置参数列表
     * @return 查询结果对象，如果无结果则返回null
     */
    public final <S extends Statement> ResultMap findMap(Object context, String sql, Map<String, Object> params, List<Object> args) {
        return checkSelectSQL(sql).queryObject(context, sql, params, args, createExtractorWithAllColumn());
    }

    /**
     * 执行查询并返回对象列表结果。
     * <p>
     * 该方法专门用于查询预期返回多行结果的场景。它会自动处理连接获取、预处理语句创建和参数设置。
     *
     * @param <T>     返回对象列表中元素的类型
     * @param context 连接数据源相关信息
     * @param sql     要执行的SQL语句
     * @param params  SQL中的命名参数映射
     * @param args    SQL中的位置参数列表
     * @param type    返回对象的Class类型
     * @return 包含所有查询结果的列表，如果无结果则返回空列表
     * @throws IllegalArgumentException 如果SQL语句为null或空
     */
    public final <T> List<T[]> listArray(Object context, String sql, Map<String, Object> params, List<Object> args, Class<T> type) {
        return checkSelectSQL(sql).queryList(context, sql, params, args, createExtractorWithAllColumnToArray(type));
    }

    /**
     * 执行查询并返回对象列表结果。
     * <p>
     * 该方法专门用于查询预期返回多行结果的场景。它会自动处理连接获取、预处理语句创建和参数设置。
     *
     * @param <T>     返回对象列表中元素的类型
     * @param context 连接数据源相关信息
     * @param sql     要执行的SQL语句
     * @param params  SQL中的命名参数映射
     * @param args    SQL中的位置参数列表
     * @param type    返回对象的Class类型
     * @return 包含所有查询结果的列表，如果无结果则返回空列表
     * @throws IllegalArgumentException 如果SQL语句为null或空
     */
    public final <T> List<T> listObject(Object context, String sql, Map<String, Object> params, List<Object> args, Class<T> type) {
        return checkSelectSQL(sql).queryList(context, sql, params, args, createExtractorWithSingleColumn(type));
    }

    /**
     * 执行查询并返回Map对象列表结果。
     * <p>
     * 该方法专门用于查询预期返回多行结果的场景。它会自动处理连接获取、预处理语句创建和参数设置。
     *
     * @param context 连接数据源相关信息
     * @param sql     要执行的SQL语句
     * @param params  SQL中的命名参数映射
     * @param args    SQL中的位置参数列表
     * @return 包含所有查询结果的列表，如果无结果则返回空列表
     * @throws IllegalArgumentException 如果SQL语句为null或空
     */
    public List<ResultMap> listMap(Object context, String sql, Map<String, Object> params, List<Object> args) {
        return checkSelectSQL(sql).queryList(context, sql, params, args, createExtractorWithAllColumn());
    }

    /**
     * 执行查询并返回单个对象结果。
     * <p>
     * 该方法专门用于查询预期返回单行结果的场景。它会自动处理连接获取、预处理语句创建和参数设置。
     * 如果查询结果为空，将返回null；如果有多个结果，只会返回第一个。
     * </p>
     *
     * @param <E>        返回对象的类型
     * @param context    连接数据源相关信息
     * @param sql        要执行的SQL语句
     * @param params     SQL中的命名参数映射
     * @param args       SQL中的位置参数列表
     * @param rowMapping 行映射函数，用于将结果集的每一行转换为指定类型的对象
     * @return 查询结果对象，如果无结果则返回null
     * @throws IllegalArgumentException 如果SQL语句为null或空
     */
    public final <E> E queryObject(Object context, String sql, Map<String, Object> params, List<Object> args, JdbcFunction<ResultSetRow, E> rowMapping) {
        return checkSelectSQL(sql).executeQueryObject(getConnectionProvider(context, sql, params, args), getPreparedStatementProvider(sql, params, args), getResultProvider(), rowMapping);
    }

    /**
     * 执行查询并返回对象列表结果。
     * <p>
     * 该方法专门用于查询预期返回多行结果的场景。它会自动处理连接获取、预处理语句创建和参数设置。
     * </p>
     *
     * @param <E>        返回对象列表中元素的类型
     * @param context    连接数据源相关信息
     * @param sql        要执行的SQL语句
     * @param params     SQL中的命名参数映射
     * @param args       SQL中的位置参数列表
     * @param rowMapping 行映射函数，用于将结果集的每一行转换为指定类型的对象
     * @return 包含所有查询结果的列表，如果无结果则返回空列表
     * @throws IllegalArgumentException 如果SQL语句为null或空
     */
    public final <E> List<E> queryList(Object context, String sql, Map<String, Object> params, List<Object> args, JdbcFunction<ResultSetRow, E> rowMapping) {
        return checkSelectSQL(sql).executeQueryList(getConnectionProvider(context, sql, params, args), getPreparedStatementProvider(sql, params, args), getResultProvider(), rowMapping);
    }


    /**
     * 执行查询并返回单个对象结果。
     * <p>
     * 该方法专门用于查询预期返回单行结果的场景。它会调用底层的query方法，
     * 并设置multiplyResult参数为false，表示只处理第一行结果。
     * 如果查询结果为空，将返回null；如果有多个结果，只会返回第一个。
     * </p>
     *
     * @param <S>                语句类型（例如 Statement, PreparedStatement, CallableStatement）
     * @param <E>                返回对象的类型
     * @param connectionProvider 连接提供函数，用于获取数据库连接
     * @param statementProvider  语句提供函数，用于创建数据库执行语句
     * @param resultProvider     结果集提供函数，用于执行查询并返回结果集
     * @param rowMapping         行映射函数，用于将结果集的每一行转换为指定类型的对象
     * @return 查询结果对象，如果无结果则返回null
     */
    public final <S extends Statement, E> E executeQueryObject(JdbcFunction<JdbcDirector, ConnectionEntry> connectionProvider,
                                                               JdbcFunction<ConnectionEntry, S> statementProvider,
                                                               JdbcFunction<S, ResultSet> resultProvider,
                                                               JdbcFunction<ResultSetRow, E> rowMapping) {
        return this.executeQuery(connectionProvider, statementProvider, resultProvider, rowMapping, false).get(0);
    }

    /**
     * 执行查询并返回对象列表结果。
     * <p>
     * 该方法专门用于查询预期返回多行结果的场景。它会调用底层的query方法，
     * 并设置multiplyResult参数为true，表示处理所有结果行。
     * </p>
     *
     * @param <S>                语句类型（例如 Statement, PreparedStatement, CallableStatement）
     * @param <E>                返回对象列表中元素的类型
     * @param connectionProvider 连接提供函数，用于获取数据库连接
     * @param statementProvider  语句提供函数，用于创建数据库执行语句
     * @param resultProvider     结果集提供函数，用于执行查询并返回结果集
     * @param rowMapping         行映射函数，用于将结果集的每一行转换为指定类型的对象
     * @return 包含所有查询结果的列表，如果无结果则返回空列表
     */
    public final <S extends Statement, E> List<E> executeQueryList(JdbcFunction<JdbcDirector, ConnectionEntry> connectionProvider,
                                                                   JdbcFunction<ConnectionEntry, S> statementProvider,
                                                                   JdbcFunction<S, ResultSet> resultProvider,
                                                                   JdbcFunction<ResultSetRow, E> rowMapping) {
        return this.executeQuery(connectionProvider, statementProvider, resultProvider, rowMapping, true);
    }

    /**
     * 执行查询操作的核心方法。
     * <p>
     * 这是一个通用的查询方法，根据multiplyResult参数决定是处理单行结果还是多行结果。
     * 当multiplyResult为false时，只处理结果集的第一行；为true时，处理所有行。
     * </p>
     *
     * @param <S>                语句类型（例如 Statement, PreparedStatement, CallableStatement）
     * @param <E>                返回对象的类型
     * @param connectionProvider 连接提供函数，用于获取数据库连接
     * @param statementProvider  语句提供函数，用于创建数据库执行语句
     * @param resultProvider     结果集提供函数，用于执行查询并返回结果集
     * @param rowMapping         行映射函数，用于将结果集的每一行转换为指定类型的对象
     * @param multiplyResult     是否处理多行结果的标志，true表示处理所有行，false表示只处理第一行
     * @return 查询结果列表，根据multiplyResult参数可能包含0个、1个或多个元素
     * @throws PersistenceException 当执行查询过程中发生错误时抛出
     */
    private <S extends Statement, E> List<E> executeQuery(JdbcFunction<JdbcDirector, ConnectionEntry> connectionProvider,
                                                          JdbcFunction<ConnectionEntry, S> statementProvider,
                                                          JdbcFunction<S, ResultSet> resultProvider,
                                                          JdbcFunction<ResultSetRow, E> rowMapping,
                                                          boolean multiplyResult) throws PersistenceException {
        if (Logger.isEnabledDebug()) {
            Logger.debug("Starting executeQuery with multiplyResult: %s", multiplyResult);
        }

        try {
            return this.executeQuery(connectionProvider, statementProvider, resultProvider, resultSet -> {
                ResultSetRow resultSetRow = resultSetManager.createResultSetRow(resultSet);
                List<E> results;
                if (multiplyResult) {
                    results = new ArrayList<>();
                    while (resultSetRow.next()) {
                        try {
                            E e = rowMapping.apply(resultSetRow);
                            if (e != null) {
                                results.add(e);
                            }
                        } catch (SQLException ex) {
                            if (Logger.isEnabledError()) {
                                Logger.error("SQLException occurred during row mapping in multi-row mode: %s", ex.getMessage(), ex);
                            }
                            throw new PersistenceException("Error occurred while mapping result set row to object in multi-row mode", ex);
                        }
                    }
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Processed %d rows in multi-row mode", results.size());
                    }
                } else {
                    results = new ArrayList<>(1);
                    try {
                        if (resultSetRow.next()) {
                            results.add(rowMapping.apply(resultSetRow));
                            if (Logger.isEnabledDebug()) {
                                Logger.debug("Processed 1 row in single-row mode");
                            }
                        } else {
                            results.add(null);
                            if (Logger.isEnabledDebug()) {
                                Logger.debug("No rows found in single-row mode");
                            }
                        }
                    } catch (SQLException ex) {
                        if (Logger.isEnabledError()) {
                            Logger.error("SQLException occurred during row mapping in single-row mode: %s", ex.getMessage(), ex);
                        }
                        throw new PersistenceException("Error occurred while mapping result set row to object in single-row mode", ex);
                    }
                }
                return results;
            });
        } catch (PersistenceException ex) {
            // 重新抛出已有的持久化异常
            throw ex;
        } catch (Exception ex) {
            // 捕获其他未预期的异常并包装成持久化异常
            if (Logger.isEnabledError()) {
                Logger.error("Unexpected error occurred during query execution: %s", ex.getMessage(), ex);
            }
            throw new PersistenceException("Unexpected error occurred during query execution", ex);
        }
    }

    /**
     * 执行带有结果集处理的数据库操作。
     * <p>
     * 该方法管理数据库操作的完整生命周期，包括：
     * 连接获取、语句创建、结果集处理、事务控制和资源清理。
     * </p>
     *
     * @param <S>                语句类型（例如 PreparedStatement, CallableStatement）
     * @param <R>                操作的返回类型
     * @param connectionProvider 获取数据库连接的函数
     * @param statementProvider  创建语句的函数
     * @param resultProvider     执行并提供结果集的函数
     * @param resultConsumer     处理结果集并生成结果的函数
     * @return 数据库操作的结果
     * @throws PersistenceException 当执行查询过程中发生错误时抛出
     */
    public final <S extends Statement, R> R executeQuery(JdbcFunction<JdbcDirector, ConnectionEntry> connectionProvider,
                                                         JdbcFunction<ConnectionEntry, S> statementProvider,
                                                         JdbcFunction<S, ResultSet> resultProvider,
                                                         JdbcFunction<ResultSet, R> resultConsumer) throws PersistenceException {
        if (Logger.isEnabledDebug()) {
            Logger.debug("Starting executeQuery with result consumer");
        }

        try {
            return this.execute(connectionProvider, statementProvider, input -> {
                ResultSet resultSet = null;
                try {
                    resultSet = resultProvider.apply(input);
                    if (resultSet == null) {
                        if (Logger.isEnabledError()) {
                            Logger.error("Failed to acquire result set");
                        }
                        throw new PersistenceException("Failed to acquire result set");
                    }

                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Successfully acquired result set");
                    }

                    R result = resultConsumer.apply(resultSet);

                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Successfully processed result set");
                    }

                    return result;
                } catch (SQLException e) {
                    if (Logger.isEnabledError()) {
                        Logger.error("SQLException occurred during query execution: %s", e.getMessage(), e);
                    }
                    throw new PersistenceException("SQL exception occurred during query execution: " + e.getMessage(), e);
                } finally {
                    JdbcUtils.closeQuietly(resultSet);
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("ResultSet closed");
                    }
                }
            });
        } catch (PersistenceException ex) {
            // 重新抛出已有的持久化异常
            throw ex;
        } catch (Exception ex) {
            // 捕获其他未预期的异常并包装成持久化异常
            if (Logger.isEnabledError()) {
                Logger.error("Unexpected error occurred during query execution with result consumer: %s", ex.getMessage(), ex);
            }
            throw new PersistenceException("Unexpected error occurred during query execution with result consumer: " + ex.getMessage(), ex);
        }
    }

    /**
     * 执行带有直接语句处理的数据库操作。
     * <p>
     * 该方法管理数据库操作的完整生命周期，包括：
     * 连接获取、语句创建、语句执行、事务控制、警告处理和资源清理。
     * </p>
     *
     * @param <S>                语句类型（例如 PreparedStatement, CallableStatement）
     * @param <R>                操作的返回类型
     * @param connectionProvider 获取数据库连接的函数
     * @param statementProvider  创建语句的函数
     * @param statementConsumer  执行语句并生成结果的函数
     * @return 数据库操作的结果
     * @throws PersistenceException 当执行语句过程中发生错误时抛出
     */
    public final <S extends Statement, R> R execute(JdbcFunction<JdbcDirector, ConnectionEntry> connectionProvider,
                                                    JdbcFunction<ConnectionEntry, S> statementProvider,
                                                    JdbcFunction<S, R> statementConsumer) throws PersistenceException {
        if (Logger.isEnabledDebug()) {
            Logger.debug("Starting execute with statement consumer");
        }

        try {
            return this.execute(connectionProvider, input -> {
                S statement = null;
                try {
                    statement = statementProvider.apply(input);
                    if (statement == null) {
                        if (Logger.isEnabledError()) {
                            Logger.error("Failed to acquire PreparedStatement");
                        }
                        throw new PersistenceException("Failed to acquire PreparedStatement");
                    }

                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Successfully acquired statement: %s", statement.getClass().getSimpleName());
                    }

                    R result = statementConsumer.apply(statement);

                    handleWarnings(statement);

                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Successfully executed statement");
                    }

                    return result;
                } catch (SQLException e) {
                    if (Logger.isEnabledError()) {
                        Logger.error("SQLException occurred during statement execution: %s", e.getMessage(), e);
                    }
                    throw new PersistenceException("SQL exception occurred during statement execution: " + e.getMessage(), e);
                } finally {
                    JdbcUtils.closeQuietly(statement);
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Statement closed");
                    }
                }
            });
        } catch (PersistenceException ex) {
            // 重新抛出已有的持久化异常
            throw ex;
        } catch (Exception ex) {
            // 捕获其他未预期的异常并包装成持久化异常
            if (Logger.isEnabledError()) {
                Logger.error("Unexpected error occurred during statement execution: %s", ex.getMessage(), ex);
            }
            throw new PersistenceException("Unexpected error occurred during statement execution: " + ex.getMessage(), ex);
        }
    }

    /**
     * 执行带有连接管理的数据库操作。
     * <p>
     * 该方法管理数据库操作的完整生命周期，包括：
     * 连接获取、事务控制、操作执行和连接释放。
     * </p>
     *
     * @param <R>                操作的返回类型
     * @param connectionProvider 获取数据库连接的函数
     * @param connectionConsumer 使用连接执行操作的函数
     * @return 数据库操作的结果
     * @throws PersistenceException 当执行连接操作过程中发生错误时抛出
     */
    public final <R> R execute(JdbcFunction<JdbcDirector, ConnectionEntry> connectionProvider,
                               JdbcFunction<ConnectionEntry, R> connectionConsumer) throws PersistenceException {
        if (Logger.isEnabledDebug()) {
            Logger.debug("Starting execute with connection consumer");
        }

        ConnectionEntry connectionEntry = null;
        try {
            connectionEntry = connectionProvider.apply(jdbcDirector);
            if (connectionEntry == null) {
                if (Logger.isEnabledError()) {
                    Logger.error("Failed to acquire ConnectionEntry");
                }
                throw new PersistenceException("Failed to acquire ConnectionEntry");
            }

            if (Logger.isEnabledDebug()) {
                Logger.debug("Successfully acquired connection entry");
            }

            jdbcDirector.beginTransaction(connectionEntry);

            if (Logger.isEnabledDebug()) {
                Logger.debug("Transaction started");
            }

            R result = connectionConsumer.apply(connectionEntry);

            jdbcDirector.commitTransaction(connectionEntry);

            if (Logger.isEnabledDebug()) {
                Logger.debug("Transaction committed");
            }

            return result;

        } catch (SQLException e) {
            if (Logger.isEnabledError()) {
                Logger.error("SQLException occurred during transaction execution: %s", e.getMessage(), e);
            }
            jdbcDirector.rollbackTransactionSafely(connectionEntry);

            if (Logger.isEnabledDebug()) {
                Logger.debug("Transaction rolled back");
            }

            throw new PersistenceException("SQL exception occurred during transaction execution: " + e.getMessage(), e);
        } catch (PersistenceException ex) {
            // 重新抛出已有的持久化异常，同时确保回滚事务
            jdbcDirector.rollbackTransactionSafely(connectionEntry);

            if (Logger.isEnabledDebug()) {
                Logger.debug("Transaction rolled back due to PersistenceException");
            }

            throw ex;
        } catch (Exception ex) {
            // 捕获其他未预期的异常并包装成持久化异常，同时确保回滚事务
            if (Logger.isEnabledError()) {
                Logger.error("Unexpected error occurred during transaction execution: %s", ex.getMessage(), ex);
            }
            jdbcDirector.rollbackTransactionSafely(connectionEntry);

            if (Logger.isEnabledDebug()) {
                Logger.debug("Transaction rolled back due to unexpected error");
            }

            throw new PersistenceException("Unexpected error occurred during transaction execution: " + ex.getMessage(), ex);
        } finally {
            jdbcDirector.releaseConnectionSafely(connectionEntry);

            if (Logger.isEnabledDebug()) {
                Logger.debug("Connection released");
            }
        }
    }

    /**
     * 检查SQL语句是否有效
     *
     * @param sql 要检查的SQL语句
     * @return 当前JdbcDirectorTemplate实例
     * @throws PersistenceException 如果SQL语句为null或空
     */
    protected JdbcDirectorTemplate checkSelectSQL(String sql) throws PersistenceException {
        if (sql == null || sql.isEmpty()) {
            if (Logger.isEnabledError()) {
                Logger.error("SQL statement is null or empty");
            }
            throw new PersistenceException("SQL statement must not be null or empty.");
        }

        if (Logger.isEnabledDebug()) {
            Logger.debug("SQL statement checked successfully: %s", sql);
        }

        return this;
    }

    /**
     * 如果不忽略警告则抛出 SQLWarningException，
     * 否则记录警告（在调试级别）。
     *
     * @param statement 当前的 JDBC 语句
     * @throws PersistenceException 如果不忽略警告
     */
    protected void handleWarnings(Statement statement) throws PersistenceException {
        if (statement == null) {
            if (Logger.isEnabledDebug()) {
                Logger.debug("Statement is null, skipping warning handling");
            }
            return;
        }

        SQLWarning warningToLog;
        try {
            warningToLog = statement.getWarnings();
        } catch (SQLException e) {
            // Log exception when retrieving warnings but don't interrupt main flow
            if (Logger.isEnabledWarn()) {
                Logger.warn("Unable to retrieve SQL warnings: " + e.getMessage(), e);
            }
            return;
        }

        if (ignoreWarnings) {
            if (Logger.isEnabledWarn() && warningToLog != null) {
                while (warningToLog != null) {
                    String logMessage = "SQL warning ignored: SQL state '" +
                            warningToLog.getSQLState() +
                            "', error code '" +
                            warningToLog.getErrorCode() +
                            "', message [" +
                            warningToLog.getMessage() +
                            "]";
                    Logger.warn(logMessage);
                    warningToLog = warningToLog.getNextWarning();
                }
            }
        } else {
            handleWarnings(warningToLog);
        }
    }

    /**
     * 如果遇到实际警告则抛出 SQLWarningException。
     *
     * @param warning 来自当前语句的警告对象。
     *                可能为 {@code null}，在这种情况下此方法不执行任何操作。
     * @throws PersistenceException 如果需要抛出实际警告
     */
    protected void handleWarnings(SQLWarning warning) throws PersistenceException {
        if (warning != null) {
            if (Logger.isEnabledError()) {
                Logger.error("SQL warnings encountered and not ignored: %s", warning.getMessage());
            }

            StringBuilder warningDetails = new StringBuilder();
            SQLWarning currentWarning = warning;
            while (currentWarning != null) {
                warningDetails.append("SQL State: ").append(currentWarning.getSQLState())
                        .append(", Error Code: ").append(currentWarning.getErrorCode())
                        .append(", Message: ").append(currentWarning.getMessage())
                        .append("; ");
                currentWarning = currentWarning.getNextWarning();
            }

            throw new PersistenceException("Warnings not ignored: " + warningDetails);
        }
    }

    /**
     * 获取连接提供者函数
     *
     * @param context 连接上下文信息
     * @param sql     SQL语句
     * @param params  命名参数映射
     * @param args    位置参数列表
     * @return 连接提供者函数
     * @throws PersistenceException 当创建连接提供者过程中发生错误时抛出
     */
    protected JdbcFunction<JdbcDirector, ConnectionEntry> getConnectionProvider(Object context, String sql, Map<String, Object> params, List<Object> args) throws PersistenceException {
        if (Logger.isEnabledDebug()) {
            Logger.debug("Creating connection provider for context: %s, sql: %s", context, sql);
        }

        return jdbcDirector -> {
            try {
                if (Logger.isEnabledDebug()) {
                    Logger.debug("Acquiring connection from jdbcDirector");
                }
                return jdbcDirector.acquireConnection(context, sql, params, args);
            } catch (PersistenceException e) {
                if (Logger.isEnabledError()) {
                    Logger.error("SQLException occurred while acquiring connection: %s", e.getMessage(), e);
                }
                throw new PersistenceException("Failed to acquire database connection: " + e.getMessage(), e);
            }
        };
    }

    /**
     * 创建一个用于生成 PreparedStatement 的函数。
     * <p>
     * 该函数负责处理 SQL 语句的参数替换和参数绑定。它支持两种参数形式：
     * 1. 命名参数：通过 params 映射进行占位符替换
     * 2. 位置参数：通过 args 列表按索引绑定到 PreparedStatement
     * <p>
     * 对于每个参数，方法会检查是否为 PreparedStatementParameter 类型，
     * 如果是，则从中提取参数值和 SQL 类型信息；否则直接使用参数值。
     * <p>
     * 参数绑定时优先使用 PreparedStatementManager 中注册的特定类型处理器，
     * 如果没有找到对应的处理器，则使用 setObject 方法进行通用绑定。
     * </p>
     *
     * @param sql    原始 SQL 语句，可能包含命名占位符
     * @param params 命名参数映射，用于替换 SQL 中的占位符
     * @param args   位置参数列表，按顺序绑定到 PreparedStatement
     * @return 一个函数，接受 ConnectionEntry 并返回配置好的 PreparedStatement
     * @throws PersistenceException 当创建PreparedStatement提供者过程中发生错误时抛出
     */
    protected JdbcFunction<ConnectionEntry, PreparedStatement> getPreparedStatementProvider(String sql, Map<String, Object> params, List<Object> args) throws PersistenceException {
        if (Logger.isEnabledDebug()) {
            Logger.debug("Creating prepared statement provider for sql: %s", sql);
        }

        return connectionEntry -> {
            // 获取数据库连接
            Connection connection = connectionEntry.connectionInstance();
            if (Logger.isEnabledInfo()) {
                Logger.info("Executing SQL connection: %s", connection);
            }

            try {
                // 处理命名参数替换
                String finalSql = params != null && !params.isEmpty() ? replacePlaceholder(sql, params, false) : sql;
                if (Logger.isEnabledInfo()) {
                    Logger.info("Executing SQL: %s", finalSql);
                }

                // 创建 PreparedStatement
                PreparedStatement preparedStatement = connection.prepareStatement(finalSql);
                Integer fetchSize = getFetchSize(connectionEntry, preparedStatement);
                if (fetchSize != null) {
                    preparedStatement.setFetchSize(fetchSize);
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Set fetch size: %s", fetchSize);
                    }
                }
                Integer maxRows = getMaxRows(connectionEntry, preparedStatement);
                if (maxRows != null) {
                    preparedStatement.setMaxRows(maxRows);
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Set max rows: %s", maxRows);
                    }
                }
                Integer queryTimeout = getQueryTimeout(connectionEntry, preparedStatement);
                if (queryTimeout != null) {
                    preparedStatement.setQueryTimeout(queryTimeout);
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Set query timeout: %s", queryTimeout);
                    }
                }
                // 如果没有位置参数，直接返回
                if (args == null || args.isEmpty()) {
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("No positional parameters to bind");
                    }
                    return preparedStatement;
                }

                // 绑定位置参数
                if (Logger.isEnabledDebug()) {
                    Logger.debug("Binding %s positional parameters", args.size());
                }

                for (int index = 1; index <= args.size(); index++) {
                    Object object = args.get(index - 1), parameterValue;
                    SQLType parameterSQLType = null;

                    // 检查参数是否为 PreparedStatementParameter 类型
                    if (object instanceof PreparedStatementParameter preparedStatementParameter) {
                        parameterValue = preparedStatementParameter.getParameterValue();
                        parameterSQLType = preparedStatementParameter.getParameterSQLType();
                    } else {
                        parameterValue = object;
                    }

                    // 根据是否有明确的 SQL 类型信息来绑定参数
                    if (parameterSQLType == null) {
                        // 使用类型特定的 setter 或通用 setObject 方法
                        PreparedStatementSetter<Object> setter = preparedStatementManager.get(parameterValue.getClass());
                        if (setter != null) {
                            setter.set(preparedStatement, index, parameterValue);
                            if (Logger.isEnabledDebug()) {
                                Logger.debug("Bound parameter at index %s with specific setter for type: %s", index, parameterValue.getClass().getName());
                            }
                        } else {
                            preparedStatement.setObject(index, parameterValue);
                            if (Logger.isEnabledDebug()) {
                                Logger.debug("Bound parameter at index %s with generic setObject for value: %s", index, parameterValue);
                            }
                        }
                    } else {
                        // 处理带有明确 SQL 类型的参数
                        if (parameterValue == null) {
                            preparedStatement.setNull(index, parameterSQLType.getVendorTypeNumber());
                            if (Logger.isEnabledDebug()) {
                                Logger.debug("Set NULL parameter at index %s with SQL type: %s", index, parameterSQLType);
                            }
                        } else {
                            preparedStatement.setObject(index, parameterValue, parameterSQLType);
                            if (Logger.isEnabledDebug()) {
                                Logger.debug("Bound parameter at index %s with explicit SQL type: %s", index, parameterSQLType);
                            }
                        }
                    }
                }
                return preparedStatement;
            } catch (SQLException e) {
                if (Logger.isEnabledError()) {
                    Logger.error("SQLException occurred while preparing statement: %s", e.getMessage(), e);
                }
                throw new PersistenceException("Failed to prepare statement: " + e.getMessage(), e);
            } catch (Exception e) {
                if (Logger.isEnabledError()) {
                    Logger.error("Unexpected error occurred while preparing statement: %s", e.getMessage(), e);
                }
                throw new PersistenceException("Unexpected error occurred while preparing statement: " + e.getMessage(), e);
            }
        };
    }

    /**
     * 占位符表达式
     */
    public static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{[^{}$]+}");

    /**
     * 替换字符串中的占位符
     * <p>
     * 使用提供的参数映射来替换字符串中的占位符。占位符格式为${...}，其中...表示占位符的键。
     * 例如，对于字符串"Hello, ${name}!"和参数映射{name: "World"}，结果将是"Hello, World!"。
     * </p>
     *
     * @param input      包含占位符的模板字符串，如果为null或空则直接返回
     * @param params     用于替换占位符的参数映射，键为占位符名称，值为替换内容
     * @param ignoreNull 当占位符对应的值为null时是否忽略处理，true表示忽略(替换为空字符串)，false表示替换为"null"
     * @return 替换占位符后的字符串，如果输入为null则返回null
     */
    public static String replacePlaceholder(String input, Map<String, Object> params, boolean ignoreNull) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        StringBuilder sb = new StringBuilder(input.length());
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
        while (matcher.find()) {
            String ph = matcher.group();
            // 安全提取占位符内容（去除前后大括号）
            String placeholderContent = ph.length() > 3 ? ph.substring(2, ph.length() - 1) : "";
            // 使用paramSupplier获取实际替换值
            String replacement = null;
            if (params != null) {
                Object value = params.get(placeholderContent);
                if (value instanceof String string) {
                    replacement = string;
                }
            }
            // 对替换值进行转义处理
            if (replacement != null) {
                matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
            } else {
                if (ignoreNull) {
                    matcher.appendReplacement(sb, "");
                } else {
                    matcher.appendReplacement(sb, "null");
                }
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取结果集提供者函数
     *
     * @return 结果集提供者函数
     */
    protected JdbcFunction<PreparedStatement, ResultSet> getResultProvider() {
        if (Logger.isEnabledDebug()) {
            Logger.debug("Creating result provider");
        }

        return PreparedStatement::executeQuery;
    }

    /**
     * 默认的结果数据行映射函数，将结果集行转换为ResultData对象
     *
     * @return 行映射函数
     * @throws PersistenceException 当创建所有列提取器过程中发生错误时抛出
     */
    protected JdbcFunction<ResultSetRow, ResultMap> createExtractorWithAllColumn() throws PersistenceException {
        if (Logger.isEnabledDebug()) {
            Logger.debug("Creating all column extractor");
        }

        return resultSetRow -> {
            try {
                int columnCount = resultSetRow.getColumnCount();
                ResultMap result = new ResultMap();
                for (int index = 1; index <= columnCount; index++) {
                    result.put(resultSetRow.getColumnLabel(index), resultSetRow.get(index, Object.class));
                }

                if (Logger.isEnabledDebug()) {
                    Logger.debug("Extracted %s columns into ResultMap", columnCount);
                }

                return result;
            } catch (SQLException e) {
                if (Logger.isEnabledError()) {
                    Logger.error("SQLException occurred while extracting all columns: %s", e.getMessage(), e);
                }
                throw new PersistenceException("Failed to extract all columns from result set row: " + e.getMessage(), e);
            } catch (Exception e) {
                if (Logger.isEnabledError()) {
                    Logger.error("Unexpected error occurred while extracting all columns: %s", e.getMessage(), e);
                }
                throw new PersistenceException("Unexpected error occurred while extracting all columns from result set row: " + e.getMessage(), e);
            }
        };
    }

    /**
     * 默认的结果数据行映射函数，将结果集行转换为ResultData对象
     *
     * @param <T> 泛型类型参数
     * @return 行映射函数
     * @throws PersistenceException 当创建所有列提取器过程中发生错误时抛出
     */
    protected <T> JdbcFunction<ResultSetRow, T[]> createExtractorWithAllColumnToArray(Class<T> type) throws PersistenceException {
        if (type == null) {
            throw new PersistenceException("Type cannot be null");
        }
        if (Logger.isEnabledDebug()) {
            Logger.debug("Creating all column extractor");
        }

        return resultSetRow -> {
            try {
                int columnCount = resultSetRow.getColumnCount();
                T[] result = (T[]) Array.newInstance(type, columnCount);
                for (int index = 1; index <= columnCount; index++) {
                    result[index - 1] = resultSetRow.get(index, type);
                }

                if (Logger.isEnabledDebug()) {
                    Logger.debug("Extracted %s columns into ResultMap", columnCount);
                }

                return result;
            } catch (SQLException e) {
                if (Logger.isEnabledError()) {
                    Logger.error("SQLException occurred while extracting all columns: %s", e.getMessage(), e);
                }
                throw new PersistenceException("Failed to extract all columns from result set row: " + e.getMessage(), e);
            } catch (Exception e) {
                if (Logger.isEnabledError()) {
                    Logger.error("Unexpected error occurred while extracting all columns: %s", e.getMessage(), e);
                }
                throw new PersistenceException("Unexpected error occurred while extracting all columns from result set row: " + e.getMessage(), e);
            }
        };
    }

    /**
     * 单列结果数据行映射函数
     *
     * @param type 目标类型
     * @param <T>  泛型类型参数
     * @return 行映射函数
     * @throws PersistenceException 当创建单列提取器过程中发生错误时抛出
     */
    protected <T> JdbcFunction<ResultSetRow, T> createExtractorWithSingleColumn(Class<T> type) throws PersistenceException {
        if (type == null) {
            throw new PersistenceException("Type cannot be null");
        }
        if (Logger.isEnabledDebug()) {
            Logger.debug("Creating single column extractor for type: %s", type.getName());
        }

        return resultSetRow -> {
            try {
                return resultSetRow.get(1, type);
            } catch (SQLException e) {
                if (Logger.isEnabledError()) {
                    Logger.error("SQLException occurred while extracting single column: %s", e.getMessage(), e);
                }
                throw new PersistenceException("Failed to extract single column from result set row. Type: " + type.getName() + ", Error: " + e.getMessage(), e);
            } catch (Exception e) {
                if (Logger.isEnabledError()) {
                    Logger.error("Unexpected error occurred while extracting single column: %s", e.getMessage(), e);
                }
                throw new PersistenceException("Unexpected error occurred while extracting single column from result set row. Type: " + type.getName() + ", Error: " + e.getMessage(), e);
            }
        };
    }

    /**
     * 获取fetchSize配置值
     *
     * @param connectionEntry 连接条目
     * @param statement       SQL语句对象
     * @return fetchSize值
     */
    protected Integer getFetchSize(ConnectionEntry connectionEntry, Statement statement) {
        return fetchSize;
    }

    /**
     * 获取maxRows配置值
     *
     * @param connectionEntry 连接条目
     * @param statement       SQL语句对象
     * @return maxRows值
     */
    protected Integer getMaxRows(ConnectionEntry connectionEntry, Statement statement) {
        return maxRows;
    }

    /**
     * 获取queryTimeout配置值
     *
     * @param connectionEntry 连接条目
     * @param statement       SQL语句对象
     * @return queryTimeout值
     */
    protected Integer getQueryTimeout(ConnectionEntry connectionEntry, Statement statement) {
        return queryTimeout;
    }

    /**
     * 设置结果集管理器
     *
     * @param resultSetManager 结果集管理器实例
     * @return 当前JdbcDirectorTemplate实例，支持链式调用
     */
    public JdbcDirectorTemplate setResultSetManager(ResultSetManager resultSetManager) {
        this.resultSetManager = resultSetManager;
        return this;
    }

    /**
     * 设置预处理语句管理器
     *
     * @param preparedStatementManager 预处理语句管理器实例
     * @return 当前JdbcDirectorTemplate实例，支持链式调用
     */
    public JdbcDirectorTemplate setPreparedStatementManager(PreparedStatementManager preparedStatementManager) {
        this.preparedStatementManager = preparedStatementManager;
        return this;
    }

    /**
     * 设置是否忽略SQL警告
     *
     * @param ignoreWarnings 是否忽略SQL警告，false表示不忽略（出现警告时抛出异常）
     * @return 当前JdbcDirectorTemplate实例，支持链式调用
     */
    public JdbcDirectorTemplate setIgnoreWarnings(boolean ignoreWarnings) {
        this.ignoreWarnings = ignoreWarnings;
        return this;
    }

    /**
     * 设置fetchSize属性值
     *
     * @param fetchSize fetchSize属性值，用于设置查询语句的fetchSize属性
     * @return 当前JdbcDirectorTemplate实例，支持链式调用
     */
    public JdbcDirectorTemplate setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    /**
     * 设置maxRows属性值
     *
     * @param maxRows maxRows属性值，用于设置查询语句的maxRows属性
     * @return 当前JdbcDirectorTemplate实例，支持链式调用
     */
    public JdbcDirectorTemplate setMaxRows(int maxRows) {
        this.maxRows = maxRows;
        return this;
    }

    /**
     * 设置queryTimeout属性值
     *
     * @param queryTimeout queryTimeout属性值，用于设置查询语句的queryTimeout属性
     * @return 当前JdbcDirectorTemplate实例，支持链式调用
     */
    public JdbcDirectorTemplate setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        return this;
    }

    /**
     * 设置是否跳过结果处理
     *
     * @param skipResultsProcessing 是否跳过结果处理，true表示跳过所有存储过程调用的结果检查
     * @return 当前JdbcDirectorTemplate实例，支持链式调用
     */
    public JdbcDirectorTemplate setSkipResultsProcessing(boolean skipResultsProcessing) {
        this.skipResultsProcessing = skipResultsProcessing;
        return this;
    }

    /**
     * 设置是否跳过未声明结果
     *
     * @param skipUndeclaredResults 是否跳过未声明结果，true表示跳过来自存储过程调用的所有没有对应SqlOutParameter声明的结果
     * @return 当前JdbcDirectorTemplate实例，支持链式调用
     */
    public JdbcDirectorTemplate setSkipUndeclaredResults(boolean skipUndeclaredResults) {
        this.skipUndeclaredResults = skipUndeclaredResults;
        return this;
    }
}

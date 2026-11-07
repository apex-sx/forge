package apexsx.forge.data.jdbc;


import apexsx.forge.core.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * JDBC 操作协调器，负责管理数据源连接、事务控制及连接生命周期。
 *
 * <p>此类通过组合模式提供对数据库操作的统一入口。它利用数据源路由器动态选择合适的数据源，
 * 并根据配置设置事务隔离级别和自动提交行为。此外，它支持线程内连接复用以提高性能。</p>
 *
 * <p>JdbcDirector 实例通常通过 JdbcDirectorBuilder 构建，确保灵活且类型安全的配置方式。</p>
 *
 * @author zhitron
 */
public class JdbcDirector {
    /**
     * 默认的数据上下文对象，用于无特定数据关联的场景。
     */
    private static final Object DEFAULT_DATA = null;

    /**
     * 默认的SQL语句标识，用于无具体SQL操作的场景。
     */
    private static final String DEFAULT_SQL = null;

    /**
     * 数据源路由器，用于根据上下文动态选择合适的数据源。
     */
    private final DataSourceRouter dataSourceRouter;

    /**
     * 事务隔离级别，定义数据库事务的隔离程度。
     */
    private final TransactionIsolation transactionIsolation;

    /**
     * 期望的自动提交状态，用于配置连接的行为。
     */
    private final boolean desiredAutoCommit;

    /**
     * 构造一个新的 JdbcDirector 实例。
     *
     * @param dataSourceRouter     数据源路由器，用于动态选择数据源
     * @param transactionIsolation 事务隔离级别
     * @param desiredAutoCommit    期望的自动提交行为
     */
    protected JdbcDirector(DataSourceRouter dataSourceRouter, TransactionIsolation transactionIsolation, boolean desiredAutoCommit) {
        this.dataSourceRouter = Objects.requireNonNull(dataSourceRouter);
        this.transactionIsolation = transactionIsolation == null ? TransactionIsolation.NONE : transactionIsolation;
        this.desiredAutoCommit = desiredAutoCommit;
    }

    /**
     * 获取一个 JdbcDirectorBuilder 实例，用于构建配置好的 JdbcDirector 对象。
     *
     * @return 返回一个新的 JdbcDirectorBuilder 实例
     */
    public static JdbcDirectorBuilder builder() {
        return new JdbcDirectorBuilder();
    }

    /**
     * 获取当前使用的数据源路由器。
     *
     * @return 返回数据源路由器实例
     */
    public DataSourceRouter dataSourceRouter() {
        return dataSourceRouter;
    }

    /**
     * 获取当前事务隔离级别。
     *
     * @return 返回事务隔离级别枚举值
     */
    public TransactionIsolation transactionIsolation() {
        return transactionIsolation;
    }

    /**
     * 获取期望的自动提交行为。
     *
     * @return 如果期望自动提交返回true，否则返回false
     */
    public boolean desiredAutoCommit() {
        return desiredAutoCommit;
    }

    /**
     * 根据提供的数据上下文、SQL语句及相关参数获取对应的数据源条目。
     *
     * <p>此方法主要用于根据运行时上下文动态确定应该使用哪个数据源。它通过委托给
     * {@link DataSourceRouter} 来完成实际的数据源选择逻辑。</p>
     *
     * <p>方法流程如下：
     * <ol>
     *   <li>如果启用了调试模式，则记录获取数据源的日志</li>
     *   <li>获取当前配置的数据源路由器实例</li>
     *   <li>验证数据源路由器不为null，否则抛出异常</li>
     *   <li>调用路由器的 determineDataSource 方法确定具体数据源</li>
     *   <li>验证返回的数据源条目不为null，否则抛出异常</li>
     *   <li>返回找到的数据源条目</li>
     * </ol>
     * </p>
     *
     * @param context 数据上下文对象，用于帮助路由器决策使用哪个数据源
     * @param sql     即将执行的SQL语句，可用于读写分离等场景
     * @param params  SQL语句中的命名参数映射，可用于更复杂的路由逻辑
     * @param args    SQL语句中的位置参数列表，可用于更复杂的路由逻辑
     * @return 确定的数据源条目对象，包含实际的DataSource实例
     * @throws PersistenceException 当数据源路由器或确定的数据源条目为null时抛出
     */
    public DataSourceEntry acquireDataSource(Object context, String sql, Map<String, Object> params, List<Object> args) throws PersistenceException {
        // 调试模式下输出连接开启日志，便于跟踪数据源获取过程
        if (Logger.isEnabledDebug()) {
            Logger.debug("Opening JDBC DataSource");
        }

        // 获取当前JdbcDirector配置的数据源路由器实例
        DataSourceRouter dataSourceRouter = this.dataSourceRouter();
        // 对数据源路由器进行空值检查，确保其已被正确初始化
        if (dataSourceRouter == null) {
            throw new PersistenceException("Failed to get DataSourceRouter with a null value");
        }

        // 委托数据源路由器根据上下文参数确定具体要使用哪个数据源
        DataSourceEntry dataSourceEntry = dataSourceRouter.determineDataSource(context, sql, params, args);
        // 验证路由器返回的数据源条目不为null，确保后续操作的安全性
        if (dataSourceEntry == null) {
            throw new PersistenceException("Failed to get DataSourceEntry with a null value");
        }
        // 返回成功获取的数据源条目
        return dataSourceEntry;
    }

    /**
     * 获取一个数据库连接条目，使用默认的数据上下文和SQL参数。
     *
     * <p>这是最简单的获取连接的方法，适用于不需要特定数据源路由的场景。
     * 它使用预定义的默认值作为参数调用完整的acquireConnection方法。</p>
     *
     * @return 成功建立的数据库连接条目对象
     * @throws PersistenceException 当无法获取数据库连接时抛出
     */
    public final ConnectionEntry acquireConnection() throws PersistenceException {
        return this.acquireConnection(DEFAULT_DATA, DEFAULT_SQL, Collections.emptyMap(), Collections.emptyList());
    }

    /**
     * 使用指定的数据上下文获取一个数据库连接条目，其他参数使用默认值。
     *
     * <p>此方法允许开发者基于特定业务上下文获取相应的数据库连接，
     * 例如在多租户系统中根据租户ID选择不同的数据库。</p>
     *
     * @param context 数据上下文对象，用于数据源路由决策
     * @return 成功建立的数据库连接条目对象
     * @throws PersistenceException 当无法获取数据库连接时抛出
     */
    public final ConnectionEntry acquireConnection(Object context) throws PersistenceException {
        return this.acquireConnection(context, DEFAULT_SQL, Collections.emptyMap(), Collections.emptyList());
    }

    /**
     * 使用指定的SQL语句和可变参数获取一个数据库连接条目，数据上下文使用默认值。
     *
     * <p>此方法适用于需要根据具体的SQL语句进行数据源路由的场景，
     * 如读写分离架构中根据SQL类型选择主库或从库。</p>
     *
     * @param sql    要执行的SQL语句，用于数据源路由判断
     * @param params SQL语句中的命名参数映射
     * @param args   SQL语句中的位置参数，作为可变参数传递
     * @return 成功建立的数据库连接条目对象
     * @throws PersistenceException 当无法获取数据库连接时抛出
     */
    public final ConnectionEntry acquireConnection(String sql, Map<String, Object> params, Object... args) throws PersistenceException {
        return this.acquireConnection(DEFAULT_DATA, sql, params, args);
    }

    /**
     * 使用指定的SQL语句和参数列表获取一个数据库连接条目，数据上下文使用默认值。
     *
     * <p>与使用可变参数版本类似，但参数以List形式传入，
     * 适用于已经拥有参数列表集合的场景。</p>
     *
     * @param sql    要执行的SQL语句，用于数据源路由判断
     * @param params SQL语句中的命名参数映射
     * @param args   SQL语句中的位置参数列表
     * @return 成功建立的数据库连接条目对象
     * @throws PersistenceException 当无法获取数据库连接时抛出
     */
    public final ConnectionEntry acquireConnection(String sql, Map<String, Object> params, List<Object> args) throws PersistenceException {
        return this.acquireConnection(DEFAULT_DATA, sql, params, args);
    }

    /**
     * 使用指定的数据上下文、SQL语句和可变参数获取一个数据库连接条目。
     *
     * <p>这是一个便捷方法，将可变参数转换为List后调用核心方法。</p>
     *
     * @param context 数据上下文对象，用于数据源路由决策
     * @param sql     要执行的SQL语句，用于数据源路由判断
     * @param params  SQL语句中的命名参数映射
     * @param args    SQL语句中的位置参数，作为可变参数传递
     * @return 成功建立的数据库连接条目对象
     * @throws PersistenceException 当无法获取数据库连接时抛出
     */
    public final ConnectionEntry acquireConnection(Object context, String sql, Map<String, Object> params, Object... args) throws PersistenceException {
        return this.acquireConnection(context, sql, params, Arrays.asList(args));
    }

    /**
     * 获取一个数据库连接条目，这是获取连接的核心方法。
     *
     * <p>该方法实现了完整的连接获取流程：
     * <ol>
     *   <li>通过acquireDataSource方法确定要使用的数据源</li>
     *   <li>从数据源中获取实际的数据库连接</li>
     *   <li>创建ConnectionEntry对象封装数据源和连接信息</li>
     *   <li>根据配置设置连接的事务隔离级别</li>
     *   <li>根据配置设置连接的自动提交行为</li>
     * </ol>
     * </p>
     *
     * <p>需要注意的是，此方法不会缓存或复用连接，每次调用都会创建新的连接。
     * 连接的管理和复用应当在更高层实现（如连接池）。</p>
     *
     * @param context 数据上下文对象，用于数据源路由决策
     * @param sql     要执行的SQL语句，用于数据源路由判断
     * @param params  SQL语句中的命名参数映射
     * @param args    SQL语句中的位置参数列表
     * @return 成功建立的数据库连接条目对象
     * @throws PersistenceException 当无法获取数据库连接或配置连接属性时抛出
     */
    public ConnectionEntry acquireConnection(Object context, String sql, Map<String, Object> params, List<Object> args) throws PersistenceException {
        try {
            // 通过数据源路由器获取适合当前上下文的数据源条目
            DataSourceEntry dataSourceEntry = acquireDataSource(context, sql, params, args);
            // 从数据源条目中获取实际的数据源对象
            DataSource dataSource = dataSourceEntry.dataSource();
            // 从数据源中获取数据库连接
            Connection connection = dataSource.getConnection();
            // 验证获取到的连接不为null
            if (connection == null) {
                throw new PersistenceException("Failed to get Connection with a null value");
            }
            // 创建连接条目对象，封装数据源和连接信息
            ConnectionEntry connectionEntry = new ConnectionEntry(dataSourceEntry, connection);
            // 获取当前配置的事务隔离级别
            TransactionIsolation transactionIsolation = this.transactionIsolation();
            // 如果配置了事务隔离级别，则应用到连接上
            if (transactionIsolation != null) {
                connection.setTransactionIsolation(transactionIsolation.level);
            }
            // 获取期望的自动提交状态
            boolean desiredAutoCommit = this.desiredAutoCommit();
            try {
                // 检查当前连接的自动提交状态是否与期望的一致
                if (connection.getAutoCommit() != desiredAutoCommit) {
                    // 如果启用了调试模式，记录自动提交状态变更日志
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Setting autocommit to " + desiredAutoCommit + " on JDBC Connection [" + connection + "]");
                    }
                    // 设置连接的自动提交状态
                    connection.setAutoCommit(desiredAutoCommit);
                }
            } catch (SQLException e) {
                // 处理设置自动提交状态时可能发生的异常
                // 只有实现非常差的驱动程序才会在这里失败，我们能做的不多
                throw new TransactionException("Error configuring AutoCommit. Your driver may not support getAutoCommit() or setAutoCommit(). Requested setting: " + desiredAutoCommit + ".  Cause: " + e.getMessage(), e);
            }
            // 返回成功创建的连接条目
            return connectionEntry;
        } catch (SQLException e) {
            // 处理获取连接过程中可能发生的SQL异常
            throw new PersistenceException("Error getting connection. Cause: " + e.getMessage(), e);
        }
    }

    /**
     * 释放指定的数据库连接。
     *
     * <p>在关闭连接之前，如果连接处于非自动提交状态，则尝试将其设置为自动提交，
     * 以避免某些数据库要求在关闭前必须提交或回滚事务。</p>
     *
     * <p>此方法会安全地尝试将连接的自动提交模式设置为true（如果尚未处于该模式），
     * 然后关闭连接。如果在设置自动提交或关闭连接过程中发生异常，将记录错误并抛出
     * PersistenceException。</p>
     *
     * @param connectionEntry 要释放的连接条目
     * @throws PersistenceException 当释放连接时发生SQL异常时抛出
     */
    public void releaseConnection(ConnectionEntry connectionEntry) throws PersistenceException {
        try {
            if (connectionEntry != null) {
                Connection connection = connectionEntry.connectionInstance();
                if (!connection.isClosed()) {
                    // 如果当前连接不是自动提交模式，先设置为自动提交，防止部分数据库在关闭连接时报错
                    try {
                        if (!connection.getAutoCommit()) {
                            // Not call commit/rollback on a connectionEntry if just selects were performed.
                            // Some databases start transactions with select statements
                            // and they mandate a commit/rollback before closing the connectionEntry.
                            // A workaround is setting the autocommit to true before closing the connectionEntry.
                            // Sybase throws an exception here.
                            if (Logger.isEnabledDebug()) {
                                Logger.debug("Resetting autocommit to true on JDBC Connection [" + connectionEntry + "]");
                            }
                            connection.setAutoCommit(true);
                        }
                    } catch (SQLException e) {
                        if (Logger.isEnabledDebug()) {
                            Logger.debug("Error resetting autocommit to true before closing the connectionEntry.  Cause: " + e.getMessage());
                        }
                    }
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Releasing JDBC Connection [" + connectionEntry + "]");
                    }
                    connection.close();
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException("Error releasing connection. Cause: " + e.getMessage(), e);
        }
    }

    /**
     * 安全地释放指定的数据库连接。
     *
     * <p>调用{@link #releaseConnection(ConnectionEntry)}方法，并捕获所有异常。
     * 如果发生异常，将记录错误日志并返回false。</p>
     *
     * @param connectionEntry 要释放的连接条目
     * @return 如果连接成功释放返回true，否则返回false
     */
    public boolean releaseConnectionSafely(ConnectionEntry connectionEntry) {
        try {
            this.releaseConnection(connectionEntry);
            return true;
        } catch (Exception e) {
            if (Logger.isEnabledError()) {
                Logger.error("Error to release connection! ConnectionEntry: " + connectionEntry + ", Message: " + e.getMessage(), e);
            }
            return false;
        }
    }

    /**
     * 开始事务处理。
     *
     * <p>将连接的自动提交模式设置为false，从而显式地开始一个事务。</p>
     *
     * <p>如果连接为空或已关闭，或在设置自动提交模式时发生异常，将抛出TransactionException。</p>
     *
     * @param connectionEntry 要开启事务的连接条目
     * @throws PersistenceException 当连接为空、已关闭或设置自动提交模式时发生SQL异常时抛出
     */
    public void beginTransaction(ConnectionEntry connectionEntry) throws PersistenceException {
        try {
            // 检查连接条目是否为空，避免空指针异常
            if (connectionEntry != null) {
                Connection connection = connectionEntry.connectionInstance();
                // 只有在当前不是手动提交模式时才设置为手动提交，避免重复设置
                if (!connection.getAutoCommit()) {
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Begin transaction on JDBC Connection [" + connection + "]");
                    }
                    connection.setAutoCommit(false);
                }
            }
        } catch (SQLException e) {
            throw new TransactionException("Error begin transaction. Cause: " + e.getMessage(), e);
        }
    }

    /**
     * 安全地开始事务处理。
     *
     * <p>调用{@link #beginTransaction(ConnectionEntry)}方法，并捕获所有异常。
     * 如果发生异常，将记录错误日志并返回false。</p>
     *
     * @param connectionEntry 要开启事务的连接条目
     * @return 如果事务成功开始返回true，否则返回false
     */
    public boolean beginTransactionSafely(ConnectionEntry connectionEntry) {
        try {
            this.beginTransaction(connectionEntry);
            return true;
        } catch (Exception e) {
            if (Logger.isEnabledError()) {
                Logger.error("Error to begin transaction! ConnectionEntry: " + connectionEntry + ", Message: " + e.getMessage(), e);
            }
            return false;
        }
    }

    /**
     * 提交当前事务。
     *
     * <p>仅当连接处于非自动提交模式时才会执行提交操作。</p>
     *
     * <p>如果连接为空或已关闭，或在提交过程中发生异常，将抛出TransactionException。</p>
     *
     * @param connectionEntry 要提交事务的连接条目
     * @throws PersistenceException 当提交事务过程中发生错误时抛出
     */
    public void commitTransaction(ConnectionEntry connectionEntry) throws PersistenceException {
        try {
            // 检查连接条目是否为空
            if (connectionEntry != null) {
                Connection connection = connectionEntry.connectionInstance();
                // 仅在非自动提交模式下执行提交
                if (!connection.getAutoCommit()) {
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Commit transaction on JDBC Connection [" + connection + "]");
                    }
                    connection.commit();
                }
            }
        } catch (Exception e) {
            throw new TransactionException("Error commit transaction. Cause: " + e.getMessage(), e);
        }
    }


    /**
     * 安全地提交当前事务。
     *
     * <p>调用{@link #commitTransaction(ConnectionEntry)}方法，并捕获所有异常。
     * 如果发生异常，将记录错误日志并返回false。</p>
     *
     * @param connectionEntry 要提交事务的连接条目
     * @return 如果事务成功提交返回true，否则返回false
     */
    public boolean commitTransactionSafely(ConnectionEntry connectionEntry) {
        try {
            this.commitTransaction(connectionEntry);
            return true;
        } catch (Exception e) {
            if (Logger.isEnabledError()) {
                Logger.error("Error to commit transaction! ConnectionEntry: " + connectionEntry + ", Message: " + e.getMessage(), e);
            }
            return false;
        }
    }

    /**
     * 回滚当前事务。
     *
     * <p>仅当连接处于非自动提交模式时才会执行回滚操作。</p>
     *
     * <p>如果连接为空或已关闭，或在回滚过程中发生异常，将抛出TransactionException。</p>
     *
     * @param connectionEntry 要回滚事务的连接条目
     * @throws PersistenceException 当回滚事务过程中发生错误时抛出
     */
    public void rollbackTransaction(ConnectionEntry connectionEntry) throws PersistenceException {
        try {
            // 检查连接条目是否为空
            if (connectionEntry != null) {
                Connection connection = connectionEntry.connectionInstance();
                // 仅在非自动提交模式下执行回滚
                if (!connection.getAutoCommit()) {
                    if (Logger.isEnabledDebug()) {
                        Logger.debug("Rollback transaction on JDBC Connection [" + connection + "]");
                    }
                    connection.rollback();
                }
            }
        } catch (Exception e) {
            throw new TransactionException("Error rollback transaction. Cause: " + e.getMessage(), e);
        }
    }

    /**
     * 安全地回滚当前事务。
     *
     * <p>调用{@link #rollbackTransaction(ConnectionEntry)}方法，并捕获所有异常。
     * 如果发生异常，将记录错误日志并返回false。</p>
     *
     * @param connectionEntry 要回滚事务的连接条目
     * @return 如果事务成功回滚返回true，否则返回false
     */
    public boolean rollbackTransactionSafely(ConnectionEntry connectionEntry) {
        try {
            this.rollbackTransaction(connectionEntry);
            return true;
        } catch (Exception e) {
            if (Logger.isEnabledError()) {
                Logger.error("Error to rollback transaction! ConnectionEntry: " + connectionEntry + ", Message: " + e.getMessage(), e);
            }
            return false;
        }
    }

}

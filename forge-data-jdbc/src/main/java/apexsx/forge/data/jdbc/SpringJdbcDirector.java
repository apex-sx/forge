package apexsx.forge.data.jdbc;

import terraqo.cheetah.Logger;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * Spring JDBC 操作协调器，负责管理数据源连接、事务控制及连接生命周期。
 *
 * <p>此类继承自 JdbcDirector，专门用于与 Spring 框架集成。它利用 Spring 的 DataSourceUtils
 * 来获取和释放数据库连接，从而更好地与 Spring 的事务管理机制协同工作。</p>
 *
 * <p>SpringJdbcDirector 实例通常通过 SpringJdbcDirectorBuilder 构建，确保灵活且类型安全的配置方式。</p>
 *
 * @author zhitron
 */
public class SpringJdbcDirector extends JdbcDirector {

    /**
     * Spring DataSourceUtils 类中的 getConnection 方法引用。
     * 用于通过反射调用 Spring 提供的获取数据库连接的方法
     */
    private static Method getConnection;

    /**
     * Spring DataSourceUtils 类中的 releaseConnection 方法引用。
     * 用于通过反射调用 Spring 提供的释放数据库连接的方法
     */
    private static Method releaseConnection;

    /**
     * Spring DataSourceUtils 类中的 isConnectionTransactional 方法引用。
     * 用于通过反射调用 Spring 提供的判断连接是否处于事务管理中的方法
     */
    private static Method isConnectionTransactional;

    /*
     * 静态初始化块，用于加载 Spring DataSourceUtils 类及其相关方法。
     * 如果加载失败，将记录错误日志。
     *
     * 此静态块在类首次加载时执行，确保能够正确获取 Spring 框架的相关工具方法引用
     * 以便后续在运行时通过反射机制调用这些方法
     */
    static {
        try {
            // 动态加载 Spring 的 DataSourceUtils 工具类
            Class<?> DataSourceUtils = Class.forName("org.springframework.jdbc.datasource.DataSourceUtils");
            // 获取 DataSourceUtils 中的 getConnection 方法，该方法接受 DataSource 参数
            getConnection = DataSourceUtils.getMethod("getConnection", DataSource.class);
            // 获取 DataSourceUtils 中的 releaseConnection 方法，该方法接受 Connection 和 DataSource 参数
            releaseConnection = DataSourceUtils.getMethod("releaseConnection", Connection.class, DataSource.class);
            // 获取 DataSourceUtils 中的 isConnectionTransactional 方法，该方法接受 Connection 和 DataSource 参数
            isConnectionTransactional = DataSourceUtils.getMethod("isConnectionTransactional", Connection.class, DataSource.class);
        } catch (Exception e) {
            // 当发生可预期异常（如 ClassNotFoundException 或 NoSuchMethodException）时记录错误日志
            if (Logger.isEnabledError()) {
                Logger.error("Error initializing SpringJdbcDirector. Failed to load Spring DataSourceUtils methods. Cause: " + e.getMessage(), e);
            }
        } catch (Throwable t) {
            // 当发生不可预期的严重错误时记录错误日志并抛出 PersistenceException 异常
            if (Logger.isEnabledError()) {
                Logger.error("Unexpected error initializing SpringJdbcDirector. Cause: " + t.getMessage(), t);
            }
            throw new PersistenceException("Unexpected error initializing SpringJdbcDirector. Cause: " + t.getMessage(), t);
        }
    }

    /**
     * 构造一个新的 SpringJdbcDirector 实例。
     *
     * @param dataSourceRouter     数据源路由器，用于动态选择数据源
     * @param transactionIsolation 事务隔离级别，定义事务的隔离程度
     * @param autoCommit           期望的自动提交行为，true表示自动提交，false表示手动提交
     */
    protected SpringJdbcDirector(DataSourceRouter dataSourceRouter, TransactionIsolation transactionIsolation, boolean autoCommit) {
        super(dataSourceRouter, transactionIsolation, autoCommit);
    }

    /**
     * 获取一个 SpringJdbcDirectorBuilder 实例，用于构建配置好的 SpringJdbcDirector 对象。
     *
     * @return 返回一个新的 SpringJdbcDirectorBuilder 实例，用于链式配置和构建 SpringJdbcDirector
     */
    public static SpringJdbcDirectorBuilder builder() {
        return new SpringJdbcDirectorBuilder();
    }

    /**
     * 获取一个数据库连接，使用指定的数据上下文、SQL语句和参数列表。
     *
     * <p>此方法会尝试复用当前线程已有的连接，如果没有则创建新的连接，并根据配置设置
     * 事务隔离级别和自动提交行为。该方法使用 Spring 的 DataSourceUtils 来获取连接。</p>
     *
     * @param context 数据上下文对象，用于确定使用哪个数据源
     * @param sql     SQL语句，用于分析和路由到合适的数据源
     * @param params  SQL语句中的命名参数映射，用于参数化查询
     * @param args    参数列表，用于替换SQL中的占位符
     * @return 返回一个数据库连接条目，包含连接信息和相关元数据
     * @throws PersistenceException 当获取连接过程中发生错误时抛出
     */
    @Override
    public ConnectionEntry acquireConnection(Object context, String sql, Map<String, Object> params, List<Object> args) throws PersistenceException {
        // 首先通过父类方法获取数据源条目
        DataSourceEntry dataSourceEntry = super.acquireDataSource(context, sql, params, args);
        try {
            // 从数据源条目中获取实际的数据源对象
            DataSource dataSource = dataSourceEntry.dataSource();
            // 如果启用了TRACE级别的日志，则记录获取连接的日志信息
            if (Logger.isEnabledTrace()) {
                Logger.trace("Acquiring connection from DataSource: %s", dataSource);
            }

            // 使用反射调用 Spring 的 DataSourceUtils.getConnection 方法获取数据库连接
            Connection connection = (Connection) getConnection.invoke(null, dataSource);
            // 获取连接当前的自动提交状态
            boolean autoCommit = connection.getAutoCommit();
            // 使用反射调用 Spring 的 DataSourceUtils.isConnectionTransactional 方法判断连接是否处于Spring事务管理中
            boolean connectionTransactional = (boolean) isConnectionTransactional.invoke(null, connection, dataSource);

            // 如果启用了DEBUG级别的日志，则记录连接是否受Spring事务管理的信息
            if (Logger.isEnabledDebug()) {
                Logger.debug("JDBC Connection [%s] will" + (connectionTransactional ? " " : " not ") + "be managed by Spring", connection);
            }

            // 创建并返回一个封装了Spring连接信息的连接条目对象
            return new SpringConnectionEntry(dataSourceEntry, connection, autoCommit, connectionTransactional);
        } catch (Exception e) {
            // 当发生可预期异常时记录错误日志并抛出 PersistenceException 异常
            if (Logger.isEnabledError()) {
                Logger.error("Failed to acquire connection. Context: %s, SQL: %s. Cause: %s", context, sql, e.getMessage(), e);
            }
            throw new PersistenceException("Error acquiring connection entry. DataSource: " + dataSourceEntry + ", Cause: " + e, e);
        } catch (Throwable t) {
            // 当发生不可预期的严重错误时记录错误日志并抛出 PersistenceException 异常
            if (Logger.isEnabledError()) {
                Logger.error("Unexpected error while acquiring connection. Context: %s, SQL: %s. Cause: %s", context, sql, t.getMessage(), t);
            }
            throw new PersistenceException("Unexpected error while acquiring connection. Context: " + context + ", SQL: " + sql + ". Cause: " + t.getMessage(), t);
        }
    }

    /**
     * 释放指定的数据库连接。
     *
     * <p>如果连接是 SpringConnectionEntry 类型，则使用 Spring 的 DataSourceUtils 来释放连接。
     * 否则，调用父类的 releaseConnection 方法。</p>
     *
     * @param connectionEntry 要释放的连接条目，包含连接和相关元数据
     * @throws PersistenceException 当释放连接过程中发生错误时抛出
     */
    @Override
    public void releaseConnection(ConnectionEntry connectionEntry) throws PersistenceException {
        // 判断连接条目是否为 SpringConnectionEntry 类型，即是否由Spring管理的连接
        if (connectionEntry instanceof SpringConnectionEntry) {
            // 将连接条目转换为 SpringConnectionEntry 类型以便访问特定属性
            SpringConnectionEntry springConnectionEntry = (SpringConnectionEntry) connectionEntry;
            try {
                // 确保连接条目不为空后再进行释放操作
                if (connectionEntry != null) {
                    // 如果启用了TRACE级别的日志，则记录释放连接的日志信息
                    if (Logger.isEnabledTrace()) {
                        Logger.trace("Releasing Spring-managed connection: %s", connectionEntry.connectionInstance());
                    }
                    // 使用反射调用 Spring 的 DataSourceUtils.releaseConnection 方法释放连接
                    releaseConnection.invoke(null, connectionEntry.connectionInstance(), connectionEntry.dataSourceEntry().dataSource());
                }
            } catch (Exception e) {
                // 当发生可预期异常时记录错误日志并抛出 PersistenceException 异常
                if (Logger.isEnabledError()) {
                    Logger.error("Failed to release Spring-managed connection: %s. Cause: %s", connectionEntry.connectionInstance(), e.getMessage(), e);
                }
                throw new PersistenceException("Error releasing connection entry. Connection: " + connectionEntry + ", Cause: " + e, e);
            } catch (Throwable t) {
                // 当发生不可预期的严重错误时记录错误日志并抛出 PersistenceException 异常
                if (Logger.isEnabledError()) {
                    Logger.error("Unexpected error while releasing Spring-managed connection: %s. Cause: %s", connectionEntry.connectionInstance(), t.getMessage(), t);
                }
                throw new PersistenceException("Unexpected error while releasing Spring-managed connection: " + connectionEntry.connectionInstance() + ". Cause: " + t.getMessage(), t);
            }
        } else {
            // 如果不是Spring管理的连接，则启用TRACE级别日志并委托给父类处理
            if (Logger.isEnabledTrace()) {
                Logger.trace("Delegating connection release to parent class: %s", connectionEntry);
            }
            super.releaseConnection(connectionEntry);
        }
    }

    /**
     * 开始事务处理。
     *
     * <p>如果连接是 SpringConnectionEntry 类型，则记录日志表明事务由 Spring 控制。
     * 否则，调用父类的 beginTransaction 方法。</p>
     *
     * @param connectionEntry 要开启事务的连接条目
     * @throws PersistenceException 当开启事务过程中发生错误时抛出
     */
    @Override
    public void beginTransaction(ConnectionEntry connectionEntry) throws PersistenceException {
        // 判断连接条目是否为 SpringConnectionEntry 类型，即是否由Spring管理的连接
        if (connectionEntry instanceof SpringConnectionEntry) {
            // 如果启用了DEBUG级别的日志，则记录事务由Spring控制的信息
            if (Logger.isEnabledDebug()) {
                Logger.debug("The opening transaction is controlled by Spring for connection: %s", connectionEntry.connectionInstance());
            }
            // 对于Spring管理的连接，不需要显式开启事务，因为Spring会自动管理
        } else {
            // 如果启用了TRACE级别的日志，则记录委托给父类处理的信息
            if (Logger.isEnabledTrace()) {
                Logger.trace("Delegating transaction begin to parent class: %s", connectionEntry);
            }
            // 对于非Spring管理的连接，委托给父类处理事务开启逻辑
            super.beginTransaction(connectionEntry);
        }
    }

    /**
     * 提交当前事务。
     *
     * <p>如果连接是 SpringConnectionEntry 类型，并且连接不是由 Spring 管理的事务，
     * 也不是自动提交的，则执行提交操作。否则，调用父类的 commitTransaction 方法。</p>
     *
     * @param connectionEntry 要提交事务的连接条目
     * @throws PersistenceException 当提交事务过程中发生错误时抛出
     */
    @Override
    public void commitTransaction(ConnectionEntry connectionEntry) throws PersistenceException {
        // 判断连接条目是否为 SpringConnectionEntry 类型，即是否由Spring管理的连接
        if (connectionEntry instanceof SpringConnectionEntry) {
            // 将连接条目转换为 SpringConnectionEntry 类型以便访问特定属性
            SpringConnectionEntry springConnectionEntry = (SpringConnectionEntry) connectionEntry;
            try {
                // 确保连接条目不为空后再进行提交操作
                if (connectionEntry != null) {
                    // 获取连接实例
                    Connection connection = connectionEntry.connectionInstance();
                    // 如果启用了TRACE级别的日志，则记录尝试提交事务的日志信息
                    if (Logger.isEnabledTrace()) {
                        Logger.trace("Attempting to commit transaction for connection: %s, springTransactional: %b, autoCommit: %b",
                                connection, springConnectionEntry.springTransactional, springConnectionEntry.autoCommit);
                    }

                    // 只有当连接不由Spring事务管理且不是自动提交模式时才手动提交事务
                    if (!springConnectionEntry.springTransactional && !springConnectionEntry.autoCommit) {
                        // 如果启用了DEBUG级别的日志，则记录提交连接事务的日志信息
                        if (Logger.isEnabledDebug()) {
                            Logger.debug("Committing JDBC Connection [%s]", connection);
                        }
                        // 执行连接的commit方法提交事务
                        connection.commit();
                    } else {
                        // 如果启用了TRACE级别的日志，则记录跳过提交操作的日志信息
                        if (Logger.isEnabledTrace()) {
                            Logger.trace("Skipping commit for connection [%s]. Spring transactional: %b, Auto commit: %b",
                                    connection, springConnectionEntry.springTransactional, springConnectionEntry.autoCommit);
                        }
                    }
                }
            } catch (Exception e) {
                // 当发生可预期异常时记录错误日志并抛出 TransactionException 异常
                if (Logger.isEnabledError()) {
                    Logger.error("Failed to commit transaction for connection: %s. Cause: %s",
                            connectionEntry.connectionInstance(), e.getMessage(), e);
                }
                throw new TransactionException("Error committing transaction. Connection: " + connectionEntry + ", Cause: " + e.getMessage(), e);
            } catch (Throwable t) {
                // 当发生不可预期的严重错误时记录错误日志并抛出 PersistenceException 异常
                if (Logger.isEnabledError()) {
                    Logger.error("Unexpected error while committing transaction for connection: %s. Cause: %s",
                            connectionEntry.connectionInstance(), t.getMessage(), t);
                }
                throw new PersistenceException("Unexpected error while committing transaction for connection: " + connectionEntry.connectionInstance() + ". Cause: " + t.getMessage(), t);
            }
        } else {
            // 如果不是Spring管理的连接，则启用TRACE级别日志并委托给父类处理
            if (Logger.isEnabledTrace()) {
                Logger.trace("Delegating transaction commit to parent class: %s", connectionEntry);
            }
            super.commitTransaction(connectionEntry);
        }
    }

    /**
     * 回滚当前事务。
     *
     * <p>如果连接是 SpringConnectionEntry 类型，并且连接不是由 Spring 管理的事务，
     * 也不是自动提交的，则执行回滚操作。否则，调用父类的 rollbackTransaction 方法。</p>
     *
     * @param connectionEntry 要回滚事务的连接条目
     * @throws PersistenceException 当回滚事务过程中发生错误时抛出
     */
    @Override
    public void rollbackTransaction(ConnectionEntry connectionEntry) throws PersistenceException {
        // 判断连接条目是否为 SpringConnectionEntry 类型，即是否由Spring管理的连接
        if (connectionEntry instanceof SpringConnectionEntry) {
            // 将连接条目转换为 SpringConnectionEntry 类型以便访问特定属性
            SpringConnectionEntry springConnectionEntry = (SpringConnectionEntry) connectionEntry;
            try {
                // 确保连接条目不为空后再进行回滚操作
                if (connectionEntry != null) {
                    // 获取连接实例
                    Connection connection = connectionEntry.connectionInstance();
                    // 如果启用了TRACE级别的日志，则记录尝试回滚事务的日志信息
                    if (Logger.isEnabledTrace()) {
                        Logger.trace("Attempting to rollback transaction for connection: %s, springTransactional: %b, autoCommit: %b",
                                connection, springConnectionEntry.springTransactional, springConnectionEntry.autoCommit);
                    }

                    // 只有当连接不由Spring事务管理且不是自动提交模式时才手动回滚事务
                    if (!springConnectionEntry.springTransactional && !springConnectionEntry.autoCommit) {
                        // 如果启用了DEBUG级别的日志，则记录回滚连接事务的日志信息
                        if (Logger.isEnabledDebug()) {
                            Logger.debug("Rolling back JDBC Connection [%s]", connection);
                        }
                        // 执行连接的rollback方法回滚事务
                        connection.rollback();
                    } else {
                        // 如果启用了TRACE级别的日志，则记录跳过回滚操作的日志信息
                        if (Logger.isEnabledTrace()) {
                            Logger.trace("Skipping rollback for connection [%s]. Spring transactional: %b, Auto commit: %b",
                                    connection, springConnectionEntry.springTransactional, springConnectionEntry.autoCommit);
                        }
                    }
                }
            } catch (Exception e) {
                // 当发生可预期异常时记录错误日志并抛出 TransactionException 异常
                if (Logger.isEnabledError()) {
                    Logger.error("Failed to rollback transaction for connection: %s. Cause: %s",
                            connectionEntry.connectionInstance(), e.getMessage(), e);
                }
                throw new TransactionException("Error rolling back transaction. Connection: " + connectionEntry + ", Cause: " + e.getMessage(), e);
            } catch (Throwable t) {
                // 当发生不可预期的严重错误时记录错误日志并抛出 PersistenceException 异常
                if (Logger.isEnabledError()) {
                    Logger.error("Unexpected error while rolling back transaction for connection: %s. Cause: %s",
                            connectionEntry.connectionInstance(), t.getMessage(), t);
                }
                throw new PersistenceException("Unexpected error while rolling back transaction for connection: " + connectionEntry.connectionInstance() + ". Cause: " + t.getMessage(), t);
            }
        } else {
            // 如果不是Spring管理的连接，则启用TRACE级别日志并委托给父类处理
            if (Logger.isEnabledTrace()) {
                Logger.trace("Delegating transaction rollback to parent class: %s", connectionEntry);
            }
            super.rollbackTransaction(connectionEntry);
        }
    }

    /**
     * Spring 连接条目，用于封装数据源条目、数据库连接实例以及连接的自动提交和事务管理状态。
     * 这是一个私有的静态内部类，仅在 SpringJdbcDirector 内部使用
     */
    private static final class SpringConnectionEntry extends ConnectionEntry {
        /**
         * 连接的自动提交状态。
         * true表示连接处于自动提交模式，false表示需要手动提交事务
         */
        private final boolean autoCommit;

        /**
         * 连接是否由 Spring 管理事务。
         * true表示连接的事务由Spring框架管理，false表示需要手动管理事务
         */
        private final boolean springTransactional;

        /**
         * 创建一个新的 SpringConnectionEntry 实例。
         *
         * @param dataSourceEntry     数据源条目，必须非空，包含数据源相关信息
         * @param connectionInstance  数据库连接实例，必须非空，实际的数据库连接对象
         * @param autoCommit          连接的自动提交状态，true表示自动提交，false表示手动提交
         * @param springTransactional 连接是否由 Spring 管理事务，true表示由Spring管理，false表示手动管理
         * @throws NullPointerException 如果任一参数为 null
         */
        public SpringConnectionEntry(DataSourceEntry dataSourceEntry, Connection connectionInstance, boolean autoCommit, boolean springTransactional) {
            // 调用父类构造函数初始化数据源条目和连接实例
            super(dataSourceEntry, connectionInstance);
            this.autoCommit = autoCommit;
            this.springTransactional = springTransactional;
        }

        /**
         * 重写toString方法，提供连接条目的字符串表示形式
         *
         * @return 包含连接信息、自动提交状态、Spring事务管理状态和数据源信息的字符串
         */
        @Override
        public String toString() {
            return "SpringConnectionEntry{" +
                    "connection=" + connectionInstance() +
                    ", autoCommit=" + autoCommit +
                    ", springTransactional=" + springTransactional +
                    ", dataSource=" + dataSourceEntry() +
                    '}';
        }
    }
}

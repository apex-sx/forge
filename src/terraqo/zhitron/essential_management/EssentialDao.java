package com.github.zhitron.essential_management;

import com.github.zhitron.effortless.Log;
import com.github.zhitron.effortless.jdbc.ConnectionEntry;
import com.github.zhitron.effortless.jdbc.JdbcDirector;
import com.github.zhitron.effortless.jdbc.PersistenceException;
import com.github.zhitron.effortless.lang.LazyHolder;
import com.github.zhitron.effortless.lang.LazyValidatedStringCache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * @author zhitron
 */
public abstract class EssentialDao<Entity extends EssentialEntity<Entity>, Dao extends EssentialDao<Entity, Dao>> {

    private static final String[] ESSENTIAL_COLUMN_NAMES = {
            "DATA_SORTED",
            "DATA_STATUS",
            "REMOVAL_FLAG",
            "INVISIBLE_FLAG",
            "DISPLAY_NAME",
            "DISPLAY_DESC",
            "DEFINE_TIME",
            "BELONG_BY_ID",
            "BELONG_BY_TYPE",
            "BELONG_BY_NAME",
            "CREATE_TIME",
            "CREATE_BY_ID",
            "CREATE_BY_TYPE",
            "CREATE_BY_NAME",
            "UPDATE_TIME",
            "UPDATE_BY_ID",
            "UPDATE_BY_TYPE",
            "UPDATE_BY_NAME",
            "ACCESS_TIME",
            "ACCESS_BY_ID",
            "ACCESS_BY_TYPE",
            "ACCESS_BY_NAME",
            "TENANT_ID",
            "TENANT_TYPE",
            "TENANT_NAME"
    };
    private final Entity essentialEntityTemplate;

    private final JdbcDirector jdbcDirector;

    private final LazyHolder<LazyValidatedStringCache> cacheTableName = new LazyHolder<>(() -> {
        return LazyValidatedStringCache.of(() -> check(configTableName()));
    });
    private final LazyHolder<LazyValidatedStringCache> cachePrimaryKeyColumnName = new LazyHolder<>(() -> {
        return LazyValidatedStringCache.of(() -> check(configPrimaryKeyColumnName()));
    });
    private final LazyHolder<LazyValidatedStringCache> cacheSingleInsertSql = new LazyHolder<>();
    private final LazyHolder<LazyValidatedStringCache> cacheSingleDeleteSql = new LazyHolder<>();

    protected EssentialDao(Entity essentialEntityTemplate, JdbcDirector jdbcDirector) {
        this.essentialEntityTemplate = Objects.requireNonNull(essentialEntityTemplate);
        this.jdbcDirector = Objects.requireNonNull(jdbcDirector);
    }

    private String check(String token) {
        if (token == null) {
            throw new RuntimeException("The Database name cannot be null");
        }
        Pattern compile = Pattern.compile("[^a-zA-Z0-9_]");
        if (compile.matcher(token).find()) {
            throw new RuntimeException("The Database name cannot contain special characters");
        }
        return token;
    }

    protected abstract String configTableName();

    protected abstract String configPrimaryKeyColumnName();

    protected abstract String[] configColumnsName();

    protected abstract Supplier<Object>[] configColumnsDataFetcher();

    public final boolean singleInsert(Entity entity) {
        if (entity == null) return false;
        String sql = singleInsertSql();
        ConnectionEntry connectionEntry = null;
        try {
            connectionEntry = jdbcDirector.getConnection();
            jdbcDirector.beginTransactionSafely(connectionEntry);
            Connection connection = connectionEntry.connectionInstance();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                Supplier<Object>[] configDataFetchers = configColumnsDataFetcher();
                for (int i = 0; i < configDataFetchers.length; i++) {
                    ps.setObject(i + 1, configDataFetchers[0].get());
                }
                return ps.executeUpdate() == 1 && jdbcDirector.commitTransactionSafely(connectionEntry);
            }
        } catch (Exception e) {
            jdbcDirector.rollbackTransactionSafely(connectionEntry);
            if (Log.isEnabledError()) {
                Log.error("Error to insert sql!", e);
            }
            return false;
        } finally {
            jdbcDirector.releaseConnectionSafely(connectionEntry);
        }
    }

    private String singleInsertSql() {
        return cacheSingleInsertSql.getOrComputeIfAbsent(() -> LazyValidatedStringCache.of(() -> {
            String tableName = cacheTableName.get().toString();
            String[] configColumnName = configColumnsName();
            Supplier<Object>[] configDataFetchers = configColumnsDataFetcher();
            if (configColumnName.length != configDataFetchers.length) {
                throw new PersistenceException("The number of column names does not match the number of column data fetchers");
            }
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ").append(tableName).append(" (");
            for (String columnName : configColumnName) {
                sql.append(columnName).append(",");
            }
            for (String essentialColumnName : ESSENTIAL_COLUMN_NAMES) {
                sql.append(essentialColumnName).append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(") VALUES (");
            for (Supplier<Object> columnData : configDataFetchers) {
                sql.append("?,");
            }
            for (String essentialColumnName : ESSENTIAL_COLUMN_NAMES) {
                sql.append("?,");
            }
            sql.deleteCharAt(sql.length() - 1);
            return sql.toString();
        })).get();
    }


    public final boolean singleDelete(Long id) {
        if (id == null) return false;
        String sql = singleDeleteSql();
        ConnectionEntry connectionEntry = null;
        try {
            connectionEntry = jdbcDirector.getConnection();
            jdbcDirector.beginTransactionSafely(connectionEntry);
            Connection connection = connectionEntry.connectionInstance();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, id);
                boolean result = ps.executeUpdate() == 1;
                return ps.executeUpdate() == 1 && jdbcDirector.commitTransactionSafely(connectionEntry);
            }
        } catch (Exception e) {
            jdbcDirector.rollbackTransactionSafely(connectionEntry);
            if (Log.isEnabledError()) {
                Log.error("Error to insert sql!", e);
            }
            return false;
        } finally {
            jdbcDirector.releaseConnectionSafely(connectionEntry);
        }
    }

    private String singleDeleteSql() {
        return cacheSingleDeleteSql.getOrComputeIfAbsent(() -> LazyValidatedStringCache.of(() -> {
            return "DELETE FROM " + cacheTableName.get() + " WHERE " + cachePrimaryKeyColumnName.get() + " = ?";
        })).get();
    }

    public final boolean singleRemove(Long id) {
        if (id == null) return false;
        String sql = singleRemoveSql();
        ConnectionEntry connectionEntry = null;
        try {
            connectionEntry = jdbcDirector.getConnection();
            jdbcDirector.beginTransactionSafely(connectionEntry);
            Connection connection = connectionEntry.connectionInstance();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, id);
                boolean result = ps.executeUpdate() == 1;
                return ps.executeUpdate() == 1 && jdbcDirector.commitTransactionSafely(connectionEntry);
            }
        } catch (Exception e) {
            jdbcDirector.rollbackTransactionSafely(connectionEntry);
            if (Log.isEnabledError()) {
                Log.error("Error to insert sql!", e);
            }
            return false;
        } finally {
            jdbcDirector.releaseConnectionSafely(connectionEntry);
        }
    }

    private String singleRemoveSql() {
        return cacheSingleDeleteSql.getOrComputeIfAbsent(() -> LazyValidatedStringCache.of(() -> {
            return "UPDATE " + cacheTableName.get() + " SET REMOVAL_FLAG = 1 WHERE " + cachePrimaryKeyColumnName.get() + " = ?";
        })).get();
    }

    public final boolean singleUpdate(Entity entity) {
        return false;
    }

    private String singleUpdateSql() {
        return cacheSingleDeleteSql.getOrComputeIfAbsent(() -> LazyValidatedStringCache.of(() -> {
            return "UPDATE " + cacheTableName.get() + " SET REMOVAL_FLAG = 1 WHERE " + cachePrimaryKeyColumnName.get() + " = ?";
        })).get();
    }


    public final Entity singleSelect(Long id) {
        return null;
    }


    public final int multipleInsert(Collection<Entity> entities) {
        if (entities == null || entities.isEmpty()) {
            return 0;
        }
        final int BATCH_SIZE = 1000; // 定义批量插入的最大数量
        ConnectionEntry connectionEntry = jdbcDirector.getConnection();
        try {
            jdbcDirector.beginTransaction(connectionEntry);
            Connection connection = connectionEntry.connectionInstance();
            String insertSql = singleInsertSql();
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                Supplier<Object>[] configDataFetchers = configColumnsDataFetcher();

                int count = 0;
                for (Entity entity : entities) {
                    int index = 1;
                    for (Supplier<Object> configDataFetcher : configDataFetchers) {
                        preparedStatement.setObject(index++, configDataFetcher.get());
                    }
                    preparedStatement.addBatch();

                    if (++count % BATCH_SIZE == 0) {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                    }
                }

                // 执行剩余的批次
                int[] results = preparedStatement.executeBatch();
                int total = 0;
                for (int result : results) {
                    total += result;
                }

                jdbcDirector.commitTransaction(connectionEntry);
                return total;
            }
        } catch (SQLException e) {
            jdbcDirector.rollbackTransaction(connectionEntry);
            if (Log.isEnabledError()) {
                Log.error("Error to execute sql!", e);
            }
            throw new PersistenceException("Error to execute sql!", e);
        } catch (Exception e) {
            jdbcDirector.rollbackTransaction(connectionEntry);
            if (Log.isEnabledError()) {
                Log.error("Unexpected error during batch insert", e);
            }
            throw new PersistenceException("Error to execute sql!", e);
        } finally {
            jdbcDirector.releaseConnection(connectionEntry);
        }
    }


    public final int multipleDelete(Collection<Long> ids) {
        return 0;
    }

    public final int multipleDelete(Entity entity) {
        return 0;
    }

    public final int multipleRemove(Collection<Long> ids) {
        return 0;
    }

    public final int multipleRemove(Entity entity) {
        return 0;
    }

    public final int multipleUpdate(Entity entity, Entity condition) {
        return 0;
    }

    public final int multipleUpdate(Entity entity, Collection<Long> ids) {
        return 0;
    }

    public final List<Entity> multipleSelect(Collection<Long> ids) {
        return null;
    }

    public final List<Entity> multipleSelect(Entity entity) {
        return null;
    }


}

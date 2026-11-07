package apexsx.forge.data.jdbc;


import apexsx.forge.core.Logger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class JdbcDirectorTest {
    static {
        JdbcDirectorHolder.setJdbcDirector(jdbcDirectorBuilder -> {

            jdbcDirectorBuilder.transactionIsolation(TransactionIsolation.SERIALIZABLE);
            try (InputStream input = JdbcDirectorTest.class.getClassLoader().getResourceAsStream("config.properties")) {
                Properties prop = new Properties();
                if (input == null) {
                    throw new RuntimeException("Unable to find config.properties");
                }
                prop.load(input);

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(prop.getProperty("db.url"));
                config.setUsername(prop.getProperty("db.user"));
                config.setPassword(prop.getProperty("db.password"));
                config.setDriverClassName(prop.getProperty("db.driver"));
                // 可选配置
                config.setMaximumPoolSize(10);
                config.setMinimumIdle(5);
                config.setIdleTimeout(30000);
                config.setMaxLifetime(1800000);
                config.setConnectionTimeout(30000);

                jdbcDirectorBuilder.defaultDataSource(new HikariDataSource(config));
            } catch (Exception e) {
                throw new RuntimeException("Error loading database configuration", e);
            }
        });
        JdbcDirectorHolder.setJdbcDirector(jdbcDirectorBuilder -> {
            DataSource defaultDataSource = buildHikariDataSource(); // 默认数据源
            DataSource dataSource1 = buildHikariDataSource();       // 数据源 1
            DataSource dataSource2 = buildHikariDataSource();       // 数据源 2
            jdbcDirectorBuilder
                    .defaultDataSource(defaultDataSource)
                    .addDataSource(DataSourceKey.of("db1"), dataSource1)
                    .addDataSource(DataSourceKey.of("db2"), dataSource2)
                    .addDataSourceKeyRouter((data, sql, params, arguments) -> {
                        if (data instanceof Integer) {
                            if ((Integer) data == 1) {
                                return DataSourceKey.of("db1");
                            } else if ((Integer) data == 2) {
                                return DataSourceKey.of("db2");
                            }
                        }
                        return null;
                    })
                    .transactionIsolation(TransactionIsolation.SERIALIZABLE)
                    .desiredAutoCommit(false);
        });
    }

    public static HikariDataSource buildHikariDataSource() {
        try (InputStream input = ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("Unable to find config.properties");
            }
            prop.load(input);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(prop.getProperty("db.url"));
            config.setUsername(prop.getProperty("db.user"));
            config.setPassword(prop.getProperty("db.password"));
            config.setDriverClassName(prop.getProperty("db.driver"));
            // 可选配置
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(30000);
            return new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Error loading database configuration", e);
        }
    }


    @Test
    public void test_connection() {
        JdbcDirector jdbcDirector = JdbcDirectorHolder.getJdbcDirector();
        ConnectionEntry connectionEntry = jdbcDirector.acquireConnection();
        try (Connection connection = connectionEntry.connectionInstance()) {
            Logger.info("Database connection established successfully!");
        } catch (SQLException e) {
            Logger.error("Failed to connect to the database.", e);
        } finally {
            jdbcDirector.releaseConnection(connectionEntry);
        }
    }
    @Test
    public void test1() {
        JdbcDirector jdbcDirector = JdbcDirectorHolder.getJdbcDirector();
        ConnectionEntry connectionEntry = jdbcDirector.acquireConnection();
        try (Connection connection = connectionEntry.connectionInstance()) {
            System.out.println("Database connection established successfully!");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }finally {
            jdbcDirector.releaseConnection(connectionEntry);
        }
    }
    @Test
    public void test2() {
        JdbcDirector jdbcDirector = JdbcDirectorHolder.getJdbcDirector();

        // 测试使用默认数据源
        ConnectionEntry defaultConnection = jdbcDirector.acquireConnection();
        DataSourceEntry dataSourceEntry = defaultConnection.dataSourceEntry();

        assertEquals(dataSourceEntry.dataSourceKey(), DataSourceKey.DEFAULT);

        // 测试根据 data 参数选择数据源 1
        ConnectionEntry db1Connection = jdbcDirector.acquireConnection(1);
        dataSourceEntry = db1Connection.dataSourceEntry();
        assertEquals(dataSourceEntry.dataSourceKey(), DataSourceKey.of("db1"));

        // 测试根据 data 参数选择数据源 2
        ConnectionEntry db2Connection = jdbcDirector.acquireConnection(2);
        dataSourceEntry = db2Connection.dataSourceEntry();
        assertEquals(dataSourceEntry.dataSourceKey(), DataSourceKey.of("db2"));

        // 测试无效的 data 参数，应该使用默认数据源
        ConnectionEntry invalidConnection = jdbcDirector.acquireConnection(999);
        dataSourceEntry = invalidConnection.dataSourceEntry();
        assertEquals(dataSourceEntry.dataSourceKey(), DataSourceKey.DEFAULT);

        // 清理连接
        jdbcDirector.releaseConnection(defaultConnection);
        jdbcDirector.releaseConnection(db1Connection);
        jdbcDirector.releaseConnection(db2Connection);
        jdbcDirector.releaseConnection(invalidConnection);
    }
}

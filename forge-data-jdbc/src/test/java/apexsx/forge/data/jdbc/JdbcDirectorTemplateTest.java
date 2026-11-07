package apexsx.forge.data.jdbc;


import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author zhitron
 */
public class JdbcDirectorTemplateTest {
    JdbcDirectorTemplate jdbcDirectorTemplate;

    public void setUp() {
        JdbcDirectorHolder.setJdbcDirector(jdbcDirectorBuilder -> {
            DataSource defaultDataSource = JdbcDirectorTest.buildHikariDataSource(); // 默认数据源
            //DataSource dataSource1 = JdbcDirectorTest.buildHikariDataSource();       // 数据源 1
            //DataSource dataSource2 = JdbcDirectorTest.buildHikariDataSource();       // 数据源 2
            jdbcDirectorBuilder
                    .defaultDataSource(defaultDataSource)
                    //.addDataSource(DataSourceKey.of("db1"), dataSource1)
                    //.addDataSource(DataSourceKey.of("db2"), dataSource2)
                    //.addDataSourceKeyRouter((data, sql, params, arguments) -> {
                    //    if (data instanceof Integer) {
                    //        if ((Integer) data == 1) {
                    //            return DataSourceKey.of("db1");
                    //        } else if ((Integer) data == 2) {
                    //            return DataSourceKey.of("db2");
                    //        }
                    //    }
                    //    return null;
                    //})
                    .transactionIsolation(TransactionIsolation.SERIALIZABLE)
                    .desiredAutoCommit(false);
        });
        jdbcDirectorTemplate = new JdbcDirectorTemplate(JdbcDirectorHolder.getJdbcDirector());
    }

    @Test
    public void test() {
        List<String> resultData = jdbcDirectorTemplate.listObject(null, "SELECT menu_auth FROM bs_menu", null, null, String.class);
        for (String resultDatum : resultData) {
            System.out.println(resultDatum);
        }
    }
}

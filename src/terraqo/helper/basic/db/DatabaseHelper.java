package potatoxf.helper.basic.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import potatoxf.helper.api.map.CaseInsensitiveMap;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

/**
 * 数据助手
 *
 * @author potatoxf
 * @date 2022/01/28
 */
public class DatabaseHelper {
  public static final Map<String, DBType> DEFAULT_DATABASE_TYPE_MAPPINGS =
      getDefaultDBTypeMappings();
  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

  /**
   * 获取数据库类型
   *
   * @param dataSource 数据源
   * @return 返回数据类型字符串
   */
  public static DBType getDBType(DataSource dataSource) {
    Map<String, DBType> databaseTypeMappings = DEFAULT_DATABASE_TYPE_MAPPINGS;
    DBType databaseType;
    Connection connection = null;
    try {
      connection = dataSource.getConnection();
      DatabaseMetaData databaseMetaData = connection.getMetaData();
      String databaseProductName = databaseMetaData.getDatabaseProductName();
      LOGGER.info("Database product name: '{}'", databaseProductName);
      databaseType = databaseTypeMappings.get(databaseProductName);
      if (databaseType == null) {
        throw new RuntimeException(
            "Couldn't deduct database type from database product name '"
                + databaseProductName
                + "' in "
                + databaseTypeMappings.keySet());
      }
      LOGGER.info("Using database type: {}", databaseType);
    } catch (SQLException e) {
      throw new RuntimeException("Couldn't deduct database type", e);
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        LOGGER.error("Exception while closing the Database connection", e);
      }
    }
    return databaseType;
  }

  private static Map<String, DBType> getDefaultDBTypeMappings() {
    Map<String, DBType> databaseTypeMappings = new CaseInsensitiveMap<>();
    databaseTypeMappings.put("H2", DBType.H2);
    databaseTypeMappings.put("HSQL Database Engine", DBType.HSQL);
    databaseTypeMappings.put("MySQL", DBType.MYSQL);
    databaseTypeMappings.put("Oracle", DBType.ORACLE);
    databaseTypeMappings.put("PostgreSQL", DBType.POSTGRES);
    databaseTypeMappings.put("Microsoft SQL Server", DBType.MSSQL);
    databaseTypeMappings.put("DB2", DBType.DB2);
    databaseTypeMappings.put("DB2/NT", DBType.DB2);
    databaseTypeMappings.put("DB2/NT64", DBType.DB2);
    databaseTypeMappings.put("DB2 UDP", DBType.DB2);
    databaseTypeMappings.put("DB2/LINUX", DBType.DB2);
    databaseTypeMappings.put("DB2/LINUX390", DBType.DB2);
    databaseTypeMappings.put("DB2/LINUXX8664", DBType.DB2);
    databaseTypeMappings.put("DB2/LINUXZ64", DBType.DB2);
    databaseTypeMappings.put("DB2/LINUXPPC64", DBType.DB2);
    databaseTypeMappings.put("DB2/400 SQL", DBType.DB2);
    databaseTypeMappings.put("DB2/6000", DBType.DB2);
    databaseTypeMappings.put("DB2 UDB iSeries", DBType.DB2);
    databaseTypeMappings.put("DB2/AIX64", DBType.DB2);
    databaseTypeMappings.put("DB2/HPUX", DBType.DB2);
    databaseTypeMappings.put("DB2/HP64", DBType.DB2);
    databaseTypeMappings.put("DB2/SUN", DBType.DB2);
    databaseTypeMappings.put("DB2/SUN64", DBType.DB2);
    databaseTypeMappings.put("DB2/PTX", DBType.DB2);
    databaseTypeMappings.put("DB2/2", DBType.DB2);
    databaseTypeMappings.put("DB2 UDB AS400", DBType.DB2);
    return Collections.unmodifiableMap(databaseTypeMappings);
  }
}

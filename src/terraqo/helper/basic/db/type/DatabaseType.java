package potatoxf.helper.basic.db.type;

import potatoxf.helper.api.HelperOnClass;
import potatoxf.helper.api.Whether;
import potatoxf.helper.basic.db.DBType;

import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据库类型
 *
 * @author potatoxf
 * @date 2021/7/9
 */
public enum DatabaseType {
  MYSQL(DBType.MYSQL, MySqlDataType.class),
  ORACLE(DBType.ORACLE, OracleDataType.class);
  private final DBType dbType;
  private final Class<? extends DatabaseDataType> type;
  private final Map<String, DatabaseDataType> databaseDataTypes = new LinkedHashMap<>();

  DatabaseType(DBType dbType, Class<? extends DatabaseDataType> type) {
    this.dbType = dbType;
    this.type = type;
    Arrays.stream(HelperOnClass.getContextFields(type, Modifier.STATIC & Modifier.FINAL, null))
        .map(
            field -> {
              try {
                return (DatabaseDataType) field.get(null);
              } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
              }
            })
        .filter(Whether::nonvl)
        .forEach(
            databaseDataType ->
                databaseDataTypes.put(databaseDataType.toString(), databaseDataType));
  }

  public static DatabaseType parse(Connection connection) {
    String databaseProductName;
    try {
      databaseProductName = connection.getMetaData().getDatabaseProductName();
    } catch (SQLException e) {
      throw new RuntimeException("Unable to get database name", e);
    }
    DatabaseType databaseType = DatabaseType.valueOf(databaseProductName.toUpperCase());
    if (databaseType == null) {
      throw new UnsupportedOperationException();
    }
    return databaseType;
  }

  /**
   * 获取数据库类型
   *
   * @return {@code Class<? extends DatabaseDataType>}
   */
  public Class<? extends DatabaseDataType> getDatabaseType() {
    return type;
  }

  /**
   * 获取数据数据类型列表
   *
   * @return {@code Collection<String>}
   */
  public Collection<String> getDatabaseDataTypes() {
    return databaseDataTypes.keySet();
  }
  /**
   * 获取数据库数据类型
   *
   * @param dataType 数据类型
   * @return {@code DatabaseDataType}
   */
  public DatabaseDataType getDatabaseDataType(String dataType) {
    DatabaseDataType databaseDataType = databaseDataTypes.get(dataType);
    if (databaseDataType == null) {
      throw new RuntimeException(
          String.format("Database [%s] hasn't registered dataType [%s]", dataType, dataType));
    }
    return databaseDataType;
  }

  /**
   * 获取Java数据类型
   *
   * @param dataType 数据类型
   * @return {@code Class<?>}
   */
  public Class<?> getJavaType(String dataType) {
    return getDatabaseDataType(dataType).getJavaType();
  }
}

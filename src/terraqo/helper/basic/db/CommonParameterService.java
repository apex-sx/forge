package potatoxf.helper.basic.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import potatoxf.helper.api.HelperOnLambda;
import potatoxf.helper.api.function.SupplierThrow;
import potatoxf.helper.basic.db.sql.crud.Update;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author potatoxf
 * @date 2022/05/13
 */
public final class CommonParameterService {
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonParameterService.class);
  private static final StringTemplate SELECT_ALL = StringTemplate.of("SELECT * FROM %s");
  private static final StringTemplate UPDATE_REF =
      StringTemplate.of(
          "UPDATE %s SET CP_NUMBER_VALUE=? WHERE CP_MODULE=? AND CP_TYPE=? AND CP_NAME=? AND CP_PERSON=? AND CP_ENUMERABLE=? AND CP_REF=0");

  private static final StringTemplate INSERT_VALUE =
      StringTemplate.of(
          "INSERT INTO %s (CP_MODULE, CP_TYPE, CP_NAME, CP_PERSON, CP_ENUMERABLE, CP_REF, CP_TOKEN_ID, CP_STRING_VALUE, CP_NUMBER_VALUE, CP_SORTED, CP_DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

  private static final StringTemplate DELETE_SERIES =
      StringTemplate.of(
          "DELETE FROM %s WHERE CP_MODULE=? AND CP_TYPE=? AND CP_NAME=? AND CP_PERSON=? AND CP_ENUMERABLE=?");

  private static final String CP_MODULE = "CP_MODULE";
  private static final String CP_TYPE = "CP_TYPE";
  private static final String CP_NAME = "CP_NAME";
  private static final String CP_PERSON = "CP_PERSON";
  private static final String CP_ENUMERABLE = "CP_ENUMERABLE";
  private static final String CP_REF = "CP_REF";
  private static final String CP_TOKEN_ID = "CP_TOKEN_ID";
  private static final String CP_STRING_VALUE = "CP_STRING_VALUE";
  private static final String CP_NUMBER_VALUE = "CP_NUMBER_VALUE";
  private static final String CP_SORTED = "CP_SORTED";
  private static final String CP_DESCRIPTION = "CP_DESCRIPTION";
  private final String tableName = "COMMON_PARAMETER";
  private final SupplierThrow<Connection, SQLException> connectionSupplierThrow;
  private final Map<CommonParameterKey, CommonParameterValue> keyDataCache =
      new ConcurrentHashMap<>();
  private final Map<CommonParameterKey.MainKey, List<CommonParameterValue>> mainKeyDataCache =
      new ConcurrentHashMap<>();

  private final AtomicBoolean isFlushSuccess = new AtomicBoolean(false);

  public CommonParameterService(SupplierThrow<Connection, SQLException> connectionSupplierThrow) {
    this.connectionSupplierThrow = connectionSupplierThrow;
    flush();
  }

  public static CommonParameterService getInstance() {
    return new CommonParameterService(null);
  }

  /**
   * @param commonParameterKey
   * @throws SQLException
   */
  private void deleteCommonParameterValueByMainKey(CommonParameterKey commonParameterKey)
      throws SQLException {
    try (Connection connection = connectionSupplierThrow.get()) {
      PreparedStatement preparedStatement =
          connection.prepareStatement(DELETE_SERIES.format(tableName));
      preparedStatement.setString(1, commonParameterKey.getModule());
      preparedStatement.setString(2, commonParameterKey.getType());
      preparedStatement.setString(3, commonParameterKey.getName());
      preparedStatement.setString(4, commonParameterKey.getPerson());
      preparedStatement.setBoolean(5, commonParameterKey.isEnumerable());
      int row = preparedStatement.executeUpdate();
      if (row <= 0) {
        throw new SQLDataException("Failed to update ref for [" + commonParameterKey + "]");
      }
    }
  }

  /**
   * @param commonParameterValue
   * @throws SQLException
   */
  private void insertCommonParameterValue(CommonParameterValue commonParameterValue)
      throws SQLException {
    if (commonParameterValue.getRef() == 0) {
      throw new IllegalArgumentException();
    }
    try (Connection connection = connectionSupplierThrow.get()) {
      PreparedStatement preparedStatement =
          connection.prepareStatement(INSERT_VALUE.format(tableName));
      preparedStatement.setString(1, commonParameterValue.getModule());
      preparedStatement.setString(2, commonParameterValue.getType());
      preparedStatement.setString(3, commonParameterValue.getName());
      preparedStatement.setString(4, commonParameterValue.getPerson());
      preparedStatement.setBoolean(5, commonParameterValue.isEnumerable());
      preparedStatement.setInt(6, commonParameterValue.getRef());
      preparedStatement.setInt(7, commonParameterValue.getTokenId());
      JdbcHelper.setStatementValue(
          preparedStatement, 8, commonParameterValue.getStringValue(null), Types.VARCHAR);
      JdbcHelper.setStatementValue(
          preparedStatement, 9, commonParameterValue.getNumberValue(null), Types.NUMERIC);
      preparedStatement.setInt(10, commonParameterValue.getSorted());
      preparedStatement.setString(11, commonParameterValue.getDescription(null));
      int row = preparedStatement.executeUpdate();
      if (row <= 0) {
        throw new SQLDataException(
            "Failed to update ref for [" + commonParameterValue.getKey() + "]");
      }
    }
  }

  /**
   * @param commonParameterValue
   * @throws SQLException
   */
  private void updateCommonParameterValue(CommonParameterValue commonParameterValue)
      throws SQLException {
    if (commonParameterValue.getRef() <= 0) {
      throw new IllegalArgumentException();
    }
    Update update =
        Update.from(tableName)
            .setIfNoNull(CP_TOKEN_ID, commonParameterValue.getTokenId())
            .setIfNoNull(CP_STRING_VALUE, commonParameterValue.getStringValue())
            .setIfNoNull(CP_NUMBER_VALUE, commonParameterValue.getNumberValue())
            .setIfNoNull(CP_DESCRIPTION, commonParameterValue.getDescription())
            .eq(CP_MODULE, commonParameterValue.getModule())
            .eq(CP_TYPE, commonParameterValue.getType())
            .eq(CP_NAME, commonParameterValue.getName())
            .eq(CP_PERSON, commonParameterValue.getPerson())
            .eq(CP_ENUMERABLE, commonParameterValue.isEnumerable())
            .eq(CP_REF, commonParameterValue.getRef());
    try (Connection connection = connectionSupplierThrow.get()) {
      int row = update.createPreparedStatement(connection).executeUpdate();
      if (row <= 0) {
        throw new SQLDataException(
            "Failed to update ref for [" + commonParameterValue.getKey() + "]");
      }
    }
  }

  /**
   * @param commonParameterValue
   * @param ref
   * @throws SQLException
   */
  private void updateCommonParameterValueRef(CommonParameterValue commonParameterValue, int ref)
      throws SQLException {
    if (commonParameterValue.getRef() <= 0) {
      throw new IllegalArgumentException();
    }
    try (Connection connection = connectionSupplierThrow.get()) {
      PreparedStatement preparedStatement =
          connection.prepareStatement(UPDATE_REF.format(tableName));
      preparedStatement.setInt(1, ref);
      preparedStatement.setString(2, commonParameterValue.getModule());
      preparedStatement.setString(3, commonParameterValue.getType());
      preparedStatement.setString(4, commonParameterValue.getName());
      preparedStatement.setString(5, commonParameterValue.getPerson());
      preparedStatement.setBoolean(6, commonParameterValue.isEnumerable());
      int row = preparedStatement.executeUpdate();
      if (row <= 0) {
        throw new SQLDataException(
            "Failed to update ref for [" + commonParameterValue.getKey() + "]");
      }
    }
  }

  /**
   * @param numberValue
   * @param stringValue
   */
  public void updateInitValue(Number numberValue, String stringValue) {}

  /**
   * @param module
   * @param type
   * @param name
   * @param isEnumerable
   * @param tokenId
   * @return
   */
  public CommonParameterValue getOrCreate(
      final String module,
      final String type,
      final String name,
      final boolean isEnumerable,
      final int tokenId) {
    final CommonParameterKey refKey =
        CommonParameterKey.of(module, type, name, null, isEnumerable, 0);
    CommonParameterKey key = CommonParameterKey.of(refKey, -1);
    if (!isFlushSuccess.get()) {
      return CommonParameterValue.of(key, tokenId);
    }
    CommonParameterValue value;
    try {
      CommonParameterValue refValue = keyDataCache.get(refKey);
      if (refValue != null) {
        int refIndex = refValue.getNumberValue(1).intValue();
        key = CommonParameterKey.of(refKey, refIndex);
        value = keyDataCache.get(key);
        if (value != null) {
          return value;
        }
        List<CommonParameterValue> list = getCommonParameterValues(refKey);
        if (list.isEmpty()) {
          key = CommonParameterKey.of(refKey, 1);
          value = CommonParameterValue.of(key, tokenId);
          insertCommonParameterValue(value);
          saveCacheValue(value);
        } else {
          list =
              list.stream()
                  .filter(v -> v.getRef() < refIndex)
                  .sorted(Comparator.comparingInt(CommonParameterValue::getRef))
                  .collect(Collectors.toList());
          value = list.get(list.size() - 1);
          updateCommonParameterValueRef(refValue, value.getRef());
        }
      } else {
        deleteCommonParameterValueByMainKey(refKey);
        clearCacheValue(refKey);
        refValue = CommonParameterValue.of(refKey, tokenId, null, 1);
        value = CommonParameterValue.of(refKey, 1, tokenId);
        insertCommonParameterValue(value);
        saveCacheValue(value);
        insertCommonParameterValue(refValue);
        saveCacheValue(refValue);
      }
      return value;
    } catch (SQLException e) {
      throw new RuntimeException("Could not get or create " + refKey + " public parameter", e);
    }
  }

  /**
   * @param commonParameterKey
   * @return
   */
  @Nonnull
  private List<CommonParameterValue> getCommonParameterValues(
      CommonParameterKey commonParameterKey) {
    List<CommonParameterValue> list = mainKeyDataCache.get(commonParameterKey.getEntryKey());
    if (list == null) {
      synchronized (mainKeyDataCache) {
        if (list == null) {
          list = new CopyOnWriteArrayList<>();
          mainKeyDataCache.put(commonParameterKey.getEntryKey(), list);
        }
      }
    }
    return list;
  }

  /**
   * @param commonParameterValue
   */
  private void saveCacheValue(@Nonnull CommonParameterValue commonParameterValue) {
    CommonParameterKey key = commonParameterValue.getKey();
    if (!keyDataCache.containsKey(key)) {
      keyDataCache.put(key, commonParameterValue);
      getCommonParameterValues(key).add(commonParameterValue);
    }
  }

  /**
   * @param commonParameterKey
   */
  private void clearCacheValue(@Nonnull CommonParameterKey commonParameterKey) {
    List<CommonParameterValue> list = mainKeyDataCache.get(commonParameterKey.getEntryKey());
    if (!list.isEmpty()) {
      list.forEach(v -> keyDataCache.remove(v.getKey()));
      list.clear();
    }
  }

  /** */
  private void flush() {
    try {
      try (Connection connection = connectionSupplierThrow.get()) {
        PreparedStatement preparedStatement =
            connection.prepareStatement(SELECT_ALL.format(tableName));
        ResultSet resultSet = preparedStatement.getResultSet();
        ResultSetMetaData metaData = resultSet.getMetaData();

        while (resultSet.next()) {
          CommonParameterKey key =
              CommonParameterKey.of(
                  resultSet.getString(CP_MODULE),
                  resultSet.getString(CP_TYPE),
                  resultSet.getString(CP_NAME),
                  resultSet.getString(CP_PERSON),
                  resultSet.getBoolean(CP_ENUMERABLE),
                  resultSet.getInt(CP_REF));
          CommonParameterValue value = keyDataCache.get(key);
          if (value == null) {
            value =
                CommonParameterValue.of(
                    key,
                    resultSet.getInt(CP_TOKEN_ID),
                    resultSet.getString(CP_STRING_VALUE),
                    resultSet.getBigDecimal(CP_NUMBER_VALUE),
                    resultSet.getInt(CP_SORTED),
                    resultSet.getString(CP_DESCRIPTION));
            keyDataCache.put(key, value);
          } else {
            value.setTokenId(resultSet.getInt(CP_TOKEN_ID));
            value.setStringValue(resultSet.getString(CP_STRING_VALUE));
            value.setNumberValue(resultSet.getBigDecimal(CP_NUMBER_VALUE));
            value.setSorted(resultSet.getInt(CP_SORTED));
            value.setDescription(resultSet.getString(CP_DESCRIPTION));
          }
        }

        mainKeyDataCache.putAll(
            keyDataCache.values().stream()
                .collect(
                    Collectors.groupingBy(
                        commonParameterValue -> commonParameterValue.getKey().getEntryKey(),
                        HelperOnLambda.toSyncList())));
      }
      isFlushSuccess.set(true);
    } catch (SQLException e) {
      isFlushSuccess.set(false);
      keyDataCache.clear();
      mainKeyDataCache.clear();
      LOGGER.error("Unable to refresh data from database", e);
    }
  }

  public void resetDefault(CommonParameterValue commonParameterValue) {
    CommonParameterKey key = commonParameterValue.getKey();
    // 未初始化
    if (key.getRef() < 0) {
      return;
    }
    try {
      updateCommonParameterValue(commonParameterValue);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void resetDefaultDescription(CommonParameterValue commonParameterValue) {}
}

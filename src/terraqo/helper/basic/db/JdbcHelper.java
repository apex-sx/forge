package potatoxf.helper.basic.db;

import potatoxf.helper.api.HelperOnClass;
import potatoxf.helper.api.HelperOnLambda;
import potatoxf.helper.api.Tools;
import potatoxf.helper.api.map.Parametric;
import potatoxf.helper.api.lang.CategorySelector;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Jdbc助手类
 *
 * @author potatoxf
 * @date 2022/05/13
 */
public class JdbcHelper {

  /**
   * @see InputStreamResultSetValueUpdater#doUpdateValue(ResultSet, int, InputStream, Parametric)
   */
  public static final String AS_BLOB = "AS_BLOB";
  /**
   * @see InputStreamResultSetValueUpdater#doUpdateValue(ResultSet, int, InputStream, Parametric)
   */
  public static final String AS_ASCII = "AS_ASCII";
  /**
   * @see ReaderStatementSetter#doSetValue(PreparedStatement, int, Reader, Parametric)
   * @see InputStreamStatementSetter#doSetValue(PreparedStatement, int, InputStream, Parametric)
   * @see ReaderResultSetValueUpdater#doUpdateValue(ResultSet, int, Reader, Parametric)
   * @see InputStreamResultSetValueUpdater#doUpdateValue(ResultSet, int, InputStream, Parametric)
   */
  public static final String LENGTH = "LENGTH";
  /**
   * @see StringStatementSetter#doSetValue(PreparedStatement, int, String, Parametric)
   * @see ReaderStatementSetter#doSetValue(PreparedStatement, int, Reader, Parametric)
   * @see StringResultSetValueUpdater#doUpdateValue(ResultSet, int, String, Parametric)
   * @see ReaderResultSetValueUpdater#doUpdateValue(ResultSet, int, Reader, Parametric)
   */
  public static final String AS_N = "AS_N";
  /**
   * @see ReaderStatementSetter#doSetValue(PreparedStatement, int, Reader, Parametric)
   * @see ReaderResultSetValueUpdater#doUpdateValue(ResultSet, int, Reader, Parametric)
   */
  public static final String AS_CLOB = "AS_CLOB";
  /**
   * @see DateStatementSetter#doSetValue(PreparedStatement, int, Date, Parametric)
   * @see SqlDateStatementSetter#doSetValue(PreparedStatement, int, java.sql.Date, Parametric)
   * @see SqlTimeStatementSetter#doSetValue(PreparedStatement, int, Time, Parametric)
   * @see SqlTimestampStatementSetter#doSetValue(PreparedStatement, int, Timestamp, Parametric)
   * @see DateResultSetValueUpdater#doUpdateValue(ResultSet, int, Date, Parametric)
   * @see SqlDateResultSetValueUpdater#doUpdateValue(ResultSet, int, java.sql.Date, Parametric)
   * @see SqlTimeResultSetValueUpdater#doUpdateValue(ResultSet, int, Time, Parametric)
   * @see SqlTimestampResultSetValueUpdater#doUpdateValue(ResultSet, int, Timestamp, Parametric)
   */
  public static final String CALENDAR = "CALENDAR";
  /**
   * @see DateStatementSetter#doSetValue(PreparedStatement, int, Date, Parametric)
   * @see DateResultSetValueUpdater#doUpdateValue(ResultSet, int, Date, Parametric)
   */
  public static final String DATE_TIME_TYPE_CLASS = "DATE_TIME_TYPE_CLASS";
  /**
   * @see StatementSetter#setValue(PreparedStatement, int, Object, Parametric)
   */
  public static final String JAVA_SQL_TYPES = "java.sql.Types";

  private static final Map<Integer, String> TYPE_NAME =
      Collections.unmodifiableMap(getSqlTypeMapping());
  private static final CategorySelector<StatementSetter<?>> PREPARED_STATEMENT_PARAMETER_SETTER =
      CategorySelector.of(
          false,
          false,
          new NumberStatementSetter(),
          new BooleanStatementSetter(),
          new ByteStatementSetter(),
          new ShortStatementSetter(),
          new IntegerStatementSetter(),
          new LongStatementSetter(),
          new FloatStatementSetter(),
          new DoubleStatementSetter(),
          new BigDecimalStatementSetter(),
          new StringStatementSetter(),
          new DateStatementSetter(),
          new SqlDateStatementSetter(),
          new SqlTimestampStatementSetter(),
          new SqlTimeStatementSetter(),
          new BytesStatementSetter(),
          new ClobStatementSetter(),
          new NClobStatementSetter(),
          new ReaderStatementSetter(),
          new BlobStatementSetter(),
          new InputStreamStatementSetter(),
          new ArrayStatementSetter(),
          new URLStatementSetter(),
          new SQLXMLStatementSetter(),
          new RefStatementSetter(),
          new RowIdStatementSetter());

  private static final CategorySelector<ResultSetValueUpdater<?>> RESULT_SET_VALUE_UPDATER =
      CategorySelector.of(
          false,
          false,
          new BooleanResultSetValueUpdater(),
          new ByteResultSetValueUpdater(),
          new ShortResultSetValueUpdater(),
          new IntegerResultSetValueUpdater(),
          new LongResultSetValueUpdater(),
          new FloatResultSetValueUpdater(),
          new DoubleResultSetValueUpdater(),
          new BigDecimalResultSetValueUpdater(),
          new StringResultSetValueUpdater(),
          new DateResultSetValueUpdater(),
          new SqlDateResultSetValueUpdater(),
          new SqlTimestampResultSetValueUpdater(),
          new SqlTimeResultSetValueUpdater(),
          new BytesResultSetValueUpdater(),
          new ClobResultSetValueUpdater(),
          new NClobResultSetValueUpdater(),
          new ReaderResultSetValueUpdater(),
          new BlobResultSetValueUpdater(),
          new InputStreamResultSetValueUpdater(),
          new ArrayResultSetValueUpdater(),
          new SQLXMLResultSetValueUpdater(),
          new RefResultSetValueUpdater(),
          new RowIdResultSetValueUpdater());

  private static final CategorySelector<ResultSetValueGetter<?>> RESULT_SET_VALUE_GETTER =
      CategorySelector.of(
          false,
          false,
          new BooleanResultSetValueGetter(),
          new NumberResultSetValueGetter(),
          new ByteResultSetValueGetter(),
          new ShortResultSetValueGetter(),
          new IntegerResultSetValueGetter(),
          new LongResultSetValueGetter(),
          new FloatResultSetValueGetter(),
          new DoubleResultSetValueGetter(),
          new BigDecimalResultSetValueGetter(),
          new StringResultSetValueGetter(),
          new DateResultSetValueGetter(),
          new SqlDateResultSetValueGetter(),
          new SqlTimestampResultSetValueGetter(),
          new SqlTimeResultSetValueGetter(),
          new BytesResultSetValueGetter(),
          new ClobResultSetValueGetter(),
          new NClobResultSetValueGetter(),
          new ReaderResultSetValueGetter(),
          new BlobResultSetValueGetter(),
          new InputStreamResultSetValueGetter(),
          new ArrayResultSetValueGetter(),
          new SQLXMLResultSetValueGetter(),
          new RefResultSetValueGetter(),
          new RowIdResultSetValueGetter());

  /**
   * 是否支持数据类型
   *
   * @param clz 类型
   * @return 如果支持返回true，否则返回false
   */
  public static boolean isSupportType(Class<?> clz) {
    return PREPARED_STATEMENT_PARAMETER_SETTER.containsAction(clz);
  }

  /**
   * 获取数据类型所支持的参数
   *
   * @param clz 类型
   * @return 参数数组
   */
  public static String[] getSupportParameters(Class<?> clz) {
    StatementSetter<?> statementSetter = PREPARED_STATEMENT_PARAMETER_SETTER.selectAction(clz);
    if (statementSetter == null) {
      return null;
    }
    return statementSetter.parameters();
  }

  /**
   * 获取数据类型所支持的参数的描述
   *
   * @param clz 类型
   * @param parameter 参数
   * @return 参数描述
   */
  public static String getSupportParameterDescription(Class<?> clz, String parameter) {
    StatementSetter<?> statementSetter = PREPARED_STATEMENT_PARAMETER_SETTER.selectAction(clz);
    if (statementSetter == null) {
      return null;
    }
    return statementSetter.description(parameter);
  }

  /**
   * 设置{@code PreparedStatement}值
   *
   * @param preparedStatement {@code PreparedStatement}
   * @param startIndex 起始参数索引
   * @param values 多个值，{@code ResultSet}支持的值
   * @throws SQLException 如果发生异常
   */
  public static void setStatementValues(
      PreparedStatement preparedStatement, int startIndex, Object... values) throws SQLException {
    Parametric parametric = new Parametric();
    int i = startIndex;
    for (Object value : values) {
      setStatementValue(preparedStatement, i++, value, parametric);
    }
  }

  /**
   * 设置{@code PreparedStatement}值
   *
   * @param preparedStatement {@code PreparedStatement}
   * @param parameterIndex 参数索引
   * @param value 值，{@code PreparedStatement}支持的值
   * @throws SQLException 如果发生异常
   */
  public static void setStatementValue(
      PreparedStatement preparedStatement, int parameterIndex, Object value, Integer sqlType)
      throws SQLException {
    Parametric parametric = new Parametric();
    if (sqlType != null) {
      parametric.put(JAVA_SQL_TYPES, sqlType);
    }
    setStatementValue(preparedStatement, parameterIndex, value, parametric);
  }

  /**
   * 设置{@code PreparedStatement}值
   *
   * @param preparedStatement {@code PreparedStatement}
   * @param parameterIndex 参数索引
   * @param value 值，{@code ResultSet}支持的值
   * @param parametric 其它参数
   * @throws SQLException 如果发生异常
   */
  @SuppressWarnings("unchecked")
  public static void setStatementValue(
      PreparedStatement preparedStatement, int parameterIndex, Object value, Parametric parametric)
      throws SQLException {
    if (parametric == null) {
      parametric = new Parametric();
    }
    if (!parametric.containsKey(JAVA_SQL_TYPES) && value == null) {
      return;
    }
    if (value == null) {
      PREPARED_STATEMENT_PARAMETER_SETTER
          .selectAction(String.class)
          .setValue(preparedStatement, parameterIndex, null, parametric);
    } else {
      Class<?> clz = value.getClass();
      StatementSetter<Object> statementSetter =
          (StatementSetter<Object>) PREPARED_STATEMENT_PARAMETER_SETTER.selectAction(clz);
      if (statementSetter == null) {
        throw new SQLDataException("The data type [" + clz + "] not supported");
      }
      statementSetter.setValue(preparedStatement, parameterIndex, value, parametric);
    }
  }

  /**
   * 设置结果集里更新值
   *
   * @param resultSet 结果集
   * @param startColumn 起始参数索引
   * @param values 多个值，{@code ResultSet}支持的值
   * @throws SQLException 如果发生异常
   */
  public static void updateResultSetValue(ResultSet resultSet, int startColumn, Object... values)
      throws SQLException {
    Parametric parametric = new Parametric();
    int i = startColumn;
    for (Object value : values) {
      updateResultSetValue(resultSet, i++, value, parametric);
    }
  }

  /**
   * 设置结果集里更新值
   *
   * @param resultSet 结果集
   * @param label 标签名
   * @param value 值，{@code ResultSet}支持的值
   * @throws SQLException 如果发生异常
   */
  public static void updateResultSetValue(ResultSet resultSet, String label, Object value)
      throws SQLException {
    updateResultSetValue(resultSet, label, value, null);
  }

  /**
   * 设置结果集里更新值
   *
   * @param resultSet 结果集
   * @param column 指定列
   * @param value 值，{@code ResultSet}支持的值
   * @throws SQLException 如果发生异常
   */
  public static void updateResultSetValue(ResultSet resultSet, int column, Object value)
      throws SQLException {
    updateResultSetValue(resultSet, column, value, null);
  }

  /**
   * 设置结果集里更新值
   *
   * @param resultSet 结果集
   * @param label 标签名
   * @param value 值，{@code ResultSet}支持的值
   * @param parametric 其它参数
   * @throws SQLException 如果发生异常
   */
  public static void updateResultSetValue(
      ResultSet resultSet, String label, Object value, Parametric parametric) throws SQLException {
    updateResultSetValue(resultSet, resultSet.findColumn(label), value, parametric);
  }

  /**
   * 设置结果集里更新值
   *
   * @param resultSet 结果集
   * @param parameterIndex 参数索引
   * @param value 值，{@code ResultSet}支持的值
   * @param parametric 其它参数
   * @throws SQLException 如果发生异常
   */
  @SuppressWarnings("unchecked")
  public static void updateResultSetValue(
      ResultSet resultSet, int parameterIndex, Object value, Parametric parametric)
      throws SQLException {
    if (parametric == null) {
      parametric = new Parametric();
    }
    Class<?> clz = value.getClass();
    ResultSetValueUpdater<Object> resultSetValueUpdater =
        (ResultSetValueUpdater<Object>) RESULT_SET_VALUE_UPDATER.selectAction(clz);
    if (resultSetValueUpdater == null) {
      throw new SQLDataException("The data type [" + clz + "] not supported");
    }
    resultSetValueUpdater.updateValue(resultSet, parameterIndex, value, parametric);
  }

  /**
   * 获取结果集指定列的值
   *
   * @param resultSet 结果集
   * @param column 指定列
   * @throws SQLException 如果发生异常，或者期望类型不符合数据库兼容类型
   */
  public static Object getResultSetValue(ResultSet resultSet, int column) throws SQLException {
    return getResultSetValue(resultSet, column, null, null);
  }

  /**
   * 获取结果集指定列的值
   *
   * @param resultSet 结果集
   * @param column 指定列
   * @throws SQLException 如果发生异常，或者期望类型不符合数据库兼容类型
   */
  public static <T> T getResultSetValue(ResultSet resultSet, int column, Class<T> fieldType)
      throws SQLException {
    return getResultSetValue(resultSet, column, fieldType, null);
  }

  /**
   * 获取结果集指定列的值
   *
   * @param resultSet 结果集
   * @param column 指定列
   * @param exceptType 期望获取类型
   * @param parametric 其它参数
   * @throws SQLException 如果发生异常，或者期望类型不符合数据库兼容类型
   */
  public static <T> T getResultSetValue(
      ResultSet resultSet, int column, Class<T> exceptType, Parametric parametric)
      throws SQLException {
    if (parametric == null) {
      parametric = new Parametric();
    }
    if (exceptType == null) {
      try {
        exceptType = (Class<T>) Class.forName(resultSet.getMetaData().getColumnClassName(column));
      } catch (ClassNotFoundException e) {
        throw new SQLException(
            "The database type [" + exceptType + "] is not supported in java", e);
      }
    }
    ResultSetValueGetter<?> resultSetValueGetter = RESULT_SET_VALUE_GETTER.selectAction(exceptType);
    if (resultSetValueGetter == null) {
      throw new SQLDataException("The data type [" + exceptType + "] not supported");
    }
    return (T) resultSetValueGetter.getValue(resultSet, column, parametric);
  }

  /**
   * 获取结果集指定列的值
   *
   * @param resultSet 结果集
   * @param column 指定列
   * @throws SQLException 如果发生异常，或者期望类型不符合数据库兼容类型
   */
  public static Object getResultSetValue(ResultSet resultSet, String column) throws SQLException {
    return getResultSetValue(resultSet, column, null, null);
  }

  /**
   * 获取结果集指定列的值
   *
   * @param resultSet 结果集
   * @param column 指定列
   * @throws SQLException 如果发生异常，或者期望类型不符合数据库兼容类型
   */
  public static <T> T getResultSetValue(ResultSet resultSet, String column, Class<T> fieldType)
      throws SQLException {
    return getResultSetValue(resultSet, column, fieldType, null);
  }

  /**
   * 获取结果集指定列的值
   *
   * @param resultSet 结果集
   * @param column 指定列
   * @param exceptType 期望获取类型
   * @param parametric 其它参数
   * @throws SQLException 如果发生异常，或者期望类型不符合数据库兼容类型
   */
  public static <T> T getResultSetValue(
      ResultSet resultSet, String column, Class<T> exceptType, Parametric parametric)
      throws SQLException {
    return getResultSetValue(resultSet, resultSet.findColumn(column), exceptType, parametric);
  }

  private static Map<Integer, String> getSqlTypeMapping() {
    return Arrays.stream(Types.class.getDeclaredFields())
        .filter(
            field -> HelperOnClass.isMatchModifiers(field, Modifier.STATIC & Modifier.FINAL, null))
        .filter(field -> field.getType() == int.class)
        .collect(
            Collectors.groupingBy(
                field -> {
                  try {
                    return (Integer) field.get(null);
                  } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                  }
                },
                HashMap::new,
                HelperOnLambda.toSingle(opt -> opt.get().getName())));
  }

  private static class NumberStatementSetter extends StatementSetter<Number> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        Number value,
        Parametric parametric)
        throws SQLException {
      preparedStatement.setBigDecimal(parameterIndex, new BigDecimal(value.toString()));
    }
  }

  private static class BooleanStatementSetter extends StatementSetter<Boolean> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        Boolean value,
        Parametric parametric)
        throws SQLException {
      preparedStatement.setBoolean(parameterIndex, value);
    }
  }

  private static class ByteStatementSetter extends StatementSetter<Byte> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Byte value, Parametric parametric)
        throws SQLException {
      preparedStatement.setByte(parameterIndex, value);
    }
  }

  private static class ShortStatementSetter extends StatementSetter<Short> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Short value, Parametric parametric)
        throws SQLException {
      preparedStatement.setShort(parameterIndex, value);
    }
  }

  private static class IntegerStatementSetter extends StatementSetter<Integer> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        Integer value,
        Parametric parametric)
        throws SQLException {
      preparedStatement.setInt(parameterIndex, value);
    }
  }

  private static class LongStatementSetter extends StatementSetter<Long> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Long value, Parametric parametric)
        throws SQLException {
      preparedStatement.setLong(parameterIndex, value);
    }
  }

  private static class FloatStatementSetter extends StatementSetter<Float> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Float value, Parametric parametric)
        throws SQLException {
      preparedStatement.setFloat(parameterIndex, value);
    }
  }

  private static class DoubleStatementSetter extends StatementSetter<Double> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        Double value,
        Parametric parametric)
        throws SQLException {
      preparedStatement.setDouble(parameterIndex, value);
    }
  }

  private static class BigDecimalStatementSetter extends StatementSetter<BigDecimal> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        BigDecimal value,
        Parametric parametric)
        throws SQLException {
      preparedStatement.setBigDecimal(parameterIndex, value);
    }
  }

  private static class StringStatementSetter extends StatementSetter<String> {
    @Override
    public String[] parameters() {
      return new String[] {AS_N};
    }

    @Override
    public String description(String parameter) {
      if (AS_N.equals(parameter)) {
        return "Whether the reader is used as N character";
      }
      return super.description(parameter);
    }

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        String value,
        Parametric parametric)
        throws SQLException {

      boolean asN = parametric.getBooleanValue(AS_N, false);
      if (asN) {
        preparedStatement.setNString(parameterIndex, value);
      } else {
        try {
          preparedStatement.setString(parameterIndex, value);
        } catch (SQLException e) {
          preparedStatement.setNString(parameterIndex, value);
        }
      }
    }
  }

  private static class DateStatementSetter extends StatementSetter<Date> {
    @Override
    public String[] parameters() {
      return new String[] {CALENDAR, DATE_TIME_TYPE_CLASS};
    }

    @Override
    public String description(String parameter) {
      if (CALENDAR.equals(parameter)) {
        return "Specify the calendar when setting the time，Must be of type [java.util.Calendar]";
      } else if (DATE_TIME_TYPE_CLASS.equals(parameter)) {
        return "Specifies that the time to be set [java.util.Date] is converted into a data type, the types are [java.sql.Timestamp, java.sql.Time, java.sql.Date], and the default is converted to [java.sql.Date]";
      }
      return super.description(parameter);
    }

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Date value, Parametric parametric)
        throws SQLException {
      Object calendarObject = parametric.get(CALENDAR);
      Calendar calendar = null;
      if (calendarObject instanceof Calendar) {
        calendar = (Calendar) calendarObject;
      }
      String dataTypeClass = parametric.getStringValue(DATE_TIME_TYPE_CLASS);
      Class<?> clz = null;
      if (dataTypeClass != null) {
        try {
          clz = Class.forName(dataTypeClass);
        } catch (ClassNotFoundException e) {
          clz = null;
        }
      }
      if (Timestamp.class == clz) {
        if (calendar == null) {
          preparedStatement.setTimestamp(parameterIndex, new Timestamp(value.getTime()));
        } else {
          preparedStatement.setTimestamp(parameterIndex, new Timestamp(value.getTime()), calendar);
        }
      } else if (Time.class == clz) {
        if (calendar == null) {
          preparedStatement.setTime(parameterIndex, new Time(value.getTime()));
        } else {
          preparedStatement.setTime(parameterIndex, new Time(value.getTime()), calendar);
        }
      } else {
        if (calendar == null) {
          preparedStatement.setDate(parameterIndex, new java.sql.Date(value.getTime()));
        } else {
          preparedStatement.setDate(parameterIndex, new java.sql.Date(value.getTime()), calendar);
        }
      }
    }
  }

  private static class SqlDateStatementSetter extends StatementSetter<java.sql.Date> {
    @Override
    public String[] parameters() {
      return new String[] {CALENDAR};
    }

    @Override
    public String description(String parameter) {
      if (CALENDAR.equals(parameter)) {
        return "Specify the calendar when setting the time，Must be of type [java.util.Calendar]";
      }
      return super.description(parameter);
    }

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        java.sql.Date value,
        Parametric parametric)
        throws SQLException {
      Object calendarObject = parametric.get(CALENDAR);
      Calendar calendar = null;
      if (calendarObject instanceof Calendar) {
        calendar = (Calendar) calendarObject;
      }
      if (calendar == null) {
        preparedStatement.setDate(parameterIndex, new java.sql.Date(value.getTime()));
      } else {
        preparedStatement.setDate(parameterIndex, new java.sql.Date(value.getTime()), calendar);
      }
    }
  }

  private static class SqlTimestampStatementSetter extends StatementSetter<Timestamp> {
    @Override
    public String[] parameters() {
      return new String[] {CALENDAR};
    }

    @Override
    public String description(String parameter) {
      if (CALENDAR.equals(parameter)) {
        return "Specify the calendar when setting the time，Must be of type [java.util.Calendar]";
      }
      return super.description(parameter);
    }

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        Timestamp value,
        Parametric parametric)
        throws SQLException {
      Object calendarObject = parametric.get(CALENDAR);
      Calendar calendar = null;
      if (calendarObject instanceof Calendar) {
        calendar = (Calendar) calendarObject;
      }
      if (calendar == null) {
        preparedStatement.setTimestamp(parameterIndex, new Timestamp(value.getTime()));
      } else {
        preparedStatement.setTimestamp(parameterIndex, new Timestamp(value.getTime()), calendar);
      }
    }
  }

  private static class SqlTimeStatementSetter extends StatementSetter<Time> {
    @Override
    public String[] parameters() {
      return new String[] {CALENDAR};
    }

    @Override
    public String description(String parameter) {
      if (CALENDAR.equals(parameter)) {
        return "Specify the calendar when setting the time，Must be of type [java.util.Calendar]";
      }
      return super.description(parameter);
    }

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Time value, Parametric parametric)
        throws SQLException {
      Object calendarObject = parametric.get(CALENDAR);
      Calendar calendar = null;
      if (calendarObject instanceof Calendar) {
        calendar = (Calendar) calendarObject;
      }
      if (calendar == null) {
        preparedStatement.setTime(parameterIndex, new Time(value.getTime()));
      } else {
        preparedStatement.setTime(parameterIndex, new Time(value.getTime()), calendar);
      }
    }
  }

  private static class BytesStatementSetter extends StatementSetter<byte[]> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        byte[] value,
        Parametric parametric)
        throws SQLException {
      preparedStatement.setBytes(parameterIndex, value);
    }
  }

  private static class ClobStatementSetter extends StatementSetter<Clob> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Clob value, Parametric parametric)
        throws SQLException {
      preparedStatement.setClob(parameterIndex, value);
    }
  }

  private static class NClobStatementSetter extends StatementSetter<NClob> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, NClob value, Parametric parametric)
        throws SQLException {
      preparedStatement.setNClob(parameterIndex, value);
    }
  }

  private static class ReaderStatementSetter extends StatementSetter<Reader> {

    @Override
    public String[] parameters() {
      return new String[] {AS_CLOB, AS_N, LENGTH};
    }

    @Override
    public String description(String parameter) {
      if (AS_CLOB.equals(parameter)) {
        return "Whether the reader is a clob";
      } else if (AS_N.equals(parameter)) {
        return "Whether the reader is used as N character";
      } else if (LENGTH.equals(parameter)) {
        return "Specifies the reader length";
      }
      return super.description(parameter);
    }

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        Reader value,
        Parametric parametric)
        throws SQLException {
      boolean asClob = parametric.getBooleanValue(AS_CLOB, false);
      boolean asN = parametric.getBooleanValue(AS_N, false);
      Integer length = parametric.getIntegerValue(LENGTH);
      if (asClob) {
        if (asN) {
          if (length == null) {
            preparedStatement.setNClob(parameterIndex, value);
          } else {
            preparedStatement.setNClob(parameterIndex, value, length);
          }
        } else {
          if (length == null) {
            preparedStatement.setClob(parameterIndex, value);
          } else {
            preparedStatement.setClob(parameterIndex, value, length);
          }
        }
      } else {
        if (asN) {
          if (length == null) {
            preparedStatement.setNCharacterStream(parameterIndex, value);
          } else {
            preparedStatement.setNCharacterStream(parameterIndex, value, length);
          }
        } else {
          if (length == null) {
            preparedStatement.setCharacterStream(parameterIndex, value);
          } else {
            preparedStatement.setCharacterStream(parameterIndex, value, length);
          }
        }
      }
    }
  }

  private static class BlobStatementSetter extends StatementSetter<Blob> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Blob value, Parametric parametric)
        throws SQLException {
      preparedStatement.setBlob(parameterIndex, value);
    }
  }

  private static class InputStreamStatementSetter extends StatementSetter<InputStream> {

    @Override
    public String[] parameters() {
      return new String[] {AS_BLOB, AS_ASCII, LENGTH};
    }

    @Override
    public String description(String parameter) {
      if (AS_BLOB.equals(parameter)) {
        return "Whether the input stream is a blob";
      } else if (AS_ASCII.equals(parameter)) {
        return "Whether the input stream is used as an Ascii character input stream";
      } else if (LENGTH.equals(parameter)) {
        return "Specifies the input stream length";
      }
      return super.description(parameter);
    }

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        InputStream value,
        Parametric parametric)
        throws SQLException {
      boolean asBlob = parametric.getBooleanValue(AS_BLOB, false);
      boolean asAscii = parametric.getBooleanValue(AS_ASCII, false);
      Long length = parametric.getLongValue(LENGTH);
      if (asBlob) {
        if (length == null) {
          preparedStatement.setBlob(parameterIndex, value);
        } else {
          preparedStatement.setBlob(parameterIndex, value, length);
        }
      } else {
        if (asAscii) {
          if (length == null) {
            preparedStatement.setAsciiStream(parameterIndex, value);
          } else {
            preparedStatement.setAsciiStream(parameterIndex, value, length);
          }
        } else {
          if (length == null) {
            preparedStatement.setBinaryStream(parameterIndex, value);
          } else {
            preparedStatement.setBinaryStream(parameterIndex, value, length);
          }
        }
      }
    }
  }

  private static class ArrayStatementSetter extends StatementSetter<Array> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Array value, Parametric parametric)
        throws SQLException {
      preparedStatement.setArray(parameterIndex, value);
    }
  }

  private static class URLStatementSetter extends StatementSetter<URL> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, URL value, Parametric parametric)
        throws SQLException {
      preparedStatement.setURL(parameterIndex, value);
    }
  }

  private static class SQLXMLStatementSetter extends StatementSetter<SQLXML> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement,
        int parameterIndex,
        SQLXML value,
        Parametric parametric)
        throws SQLException {
      preparedStatement.setSQLXML(parameterIndex, value);
    }
  }

  private static class RefStatementSetter extends StatementSetter<Ref> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, Ref value, Parametric parametric)
        throws SQLException {
      preparedStatement.setRef(parameterIndex, value);
    }
  }

  private static class RowIdStatementSetter extends StatementSetter<RowId> {

    @Override
    public void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, RowId value, Parametric parametric)
        throws SQLException {
      preparedStatement.setRowId(parameterIndex, value);
    }
  }

  private abstract static class StatementSetter<T> extends CommonCategory<T> {
    public final void setValue(
        PreparedStatement preparedStatement, int parameterIndex, T value, Parametric parametric)
        throws SQLException {
      if (value == null) {
        Integer type = parametric.getIntegerValue(JAVA_SQL_TYPES);
        if (type == null) {
          throw new SQLDataException(
              "When the value cannot be set to null, the data type cannot be obtained--->Index: ["
                  + parameterIndex
                  + "] Parameter: ["
                  + JAVA_SQL_TYPES
                  + "]");
        }
        preparedStatement.setNull(parameterIndex, type);

      } else {
        doSetValue(preparedStatement, parameterIndex, value, parametric);
      }
    }

    public abstract void doSetValue(
        PreparedStatement preparedStatement, int parameterIndex, T value, Parametric parametric)
        throws SQLException;
  }

  private static class BooleanResultSetValueUpdater extends ResultSetValueUpdater<Boolean> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Boolean value, Parametric parametric)
        throws SQLException {
      resultSet.updateBoolean(column, value);
    }
  }

  private static class ByteResultSetValueUpdater extends ResultSetValueUpdater<Byte> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Byte value, Parametric parametric)
        throws SQLException {
      resultSet.updateByte(column, value);
    }
  }

  private static class ShortResultSetValueUpdater extends ResultSetValueUpdater<Short> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Short value, Parametric parametric)
        throws SQLException {
      resultSet.updateShort(column, value);
    }
  }

  private static class IntegerResultSetValueUpdater extends ResultSetValueUpdater<Integer> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Integer value, Parametric parametric)
        throws SQLException {
      resultSet.updateInt(column, value);
    }
  }

  private static class LongResultSetValueUpdater extends ResultSetValueUpdater<Long> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Long value, Parametric parametric)
        throws SQLException {
      resultSet.updateLong(column, value);
    }
  }

  private static class FloatResultSetValueUpdater extends ResultSetValueUpdater<Float> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Float value, Parametric parametric)
        throws SQLException {
      resultSet.updateFloat(column, value);
    }
  }

  private static class DoubleResultSetValueUpdater extends ResultSetValueUpdater<Double> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Double value, Parametric parametric)
        throws SQLException {
      resultSet.updateDouble(column, value);
    }
  }

  private static class BigDecimalResultSetValueUpdater extends ResultSetValueUpdater<BigDecimal> {

    @Override
    public void doUpdateValue(
        ResultSet resultSet, int column, BigDecimal value, Parametric parametric)
        throws SQLException {
      resultSet.updateBigDecimal(column, value);
    }
  }

  private static class StringResultSetValueUpdater extends ResultSetValueUpdater<String> {
    @Override
    public String[] parameters() {
      return new String[] {AS_N};
    }

    @Override
    public String description(String parameter) {
      if (AS_N.equals(parameter)) {
        return "Whether the reader is used as N character";
      }
      return super.description(parameter);
    }

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, String value, Parametric parametric)
        throws SQLException {

      boolean asN = parametric.getBooleanValue(AS_N, false);
      if (asN) {
        resultSet.updateNString(column, value);
      } else {
        try {
          resultSet.updateString(column, value);
        } catch (SQLException e) {
          resultSet.updateNString(column, value);
        }
      }
    }
  }

  private static class DateResultSetValueUpdater extends ResultSetValueUpdater<Date> {
    @Override
    public String[] parameters() {
      return new String[] {DATE_TIME_TYPE_CLASS};
    }

    @Override
    public String description(String parameter) {
      if (DATE_TIME_TYPE_CLASS.equals(parameter)) {
        return "Specifies that the time to be set [java.util.Date] is converted into a data type, the types are [java.sql.Timestamp, java.sql.Time, java.sql.Date], and the default is converted to [java.sql.Date]";
      }
      return super.description(parameter);
    }

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Date value, Parametric parametric)
        throws SQLException {
      String dataTypeClass = parametric.getStringValue(DATE_TIME_TYPE_CLASS);
      Class<?> clz = null;
      if (dataTypeClass != null) {
        try {
          clz = Class.forName(dataTypeClass);
        } catch (ClassNotFoundException e) {
          clz = null;
        }
      }
      if (Timestamp.class == clz) {
        resultSet.updateTimestamp(column, new Timestamp(value.getTime()));
      } else if (Time.class == clz) {
        resultSet.updateTime(column, new Time(value.getTime()));
      } else {
        resultSet.updateDate(column, new java.sql.Date(value.getTime()));
      }
    }
  }

  private static class SqlDateResultSetValueUpdater extends ResultSetValueUpdater<java.sql.Date> {

    @Override
    public void doUpdateValue(
        ResultSet resultSet, int column, java.sql.Date value, Parametric parametric)
        throws SQLException {
      resultSet.updateDate(column, value);
    }
  }

  private static class SqlTimestampResultSetValueUpdater extends ResultSetValueUpdater<Timestamp> {

    @Override
    public void doUpdateValue(
        ResultSet resultSet, int column, Timestamp value, Parametric parametric)
        throws SQLException {
      resultSet.updateTimestamp(column, value);
    }
  }

  private static class SqlTimeResultSetValueUpdater extends ResultSetValueUpdater<Time> {
    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Time value, Parametric parametric)
        throws SQLException {
      resultSet.updateTime(column, new Time(value.getTime()));
    }
  }

  private static class BytesResultSetValueUpdater extends ResultSetValueUpdater<byte[]> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, byte[] value, Parametric parametric)
        throws SQLException {
      resultSet.updateBytes(column, value);
    }
  }

  private static class ClobResultSetValueUpdater extends ResultSetValueUpdater<Clob> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Clob value, Parametric parametric)
        throws SQLException {
      resultSet.updateClob(column, value);
    }
  }

  private static class NClobResultSetValueUpdater extends ResultSetValueUpdater<NClob> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, NClob value, Parametric parametric)
        throws SQLException {
      resultSet.updateNClob(column, value);
    }
  }

  private static class ReaderResultSetValueUpdater extends ResultSetValueUpdater<Reader> {

    @Override
    public String[] parameters() {
      return new String[] {AS_CLOB, AS_N, LENGTH};
    }

    @Override
    public String description(String parameter) {
      if (AS_CLOB.equals(parameter)) {
        return "Whether the reader is a clob";
      } else if (AS_N.equals(parameter)) {
        return "Whether the reader is used as N character";
      } else if (LENGTH.equals(parameter)) {
        return "Specifies the reader length";
      }
      return super.description(parameter);
    }

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Reader value, Parametric parametric)
        throws SQLException {
      boolean asClob = parametric.getBooleanValue(AS_CLOB, false);
      boolean asN = parametric.getBooleanValue(AS_N, false);
      Integer length = parametric.getIntegerValue(LENGTH);
      if (asClob) {
        if (asN) {
          if (length == null) {
            resultSet.updateNClob(column, value);
          } else {
            resultSet.updateNClob(column, value, length);
          }
        } else {
          if (length == null) {
            resultSet.updateClob(column, value);
          } else {
            resultSet.updateClob(column, value, length);
          }
        }
      } else {
        if (asN) {
          if (length == null) {
            resultSet.updateNCharacterStream(column, value);
          } else {
            resultSet.updateNCharacterStream(column, value, length);
          }
        } else {
          if (length == null) {
            resultSet.updateCharacterStream(column, value);
          } else {
            resultSet.updateCharacterStream(column, value, length);
          }
        }
      }
    }
  }

  private static class BlobResultSetValueUpdater extends ResultSetValueUpdater<Blob> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Blob value, Parametric parametric)
        throws SQLException {
      resultSet.updateBlob(column, value);
    }
  }

  private static class InputStreamResultSetValueUpdater extends ResultSetValueUpdater<InputStream> {

    @Override
    public String[] parameters() {
      return new String[] {AS_BLOB, AS_ASCII, LENGTH};
    }

    @Override
    public String description(String parameter) {
      if (AS_BLOB.equals(parameter)) {
        return "Whether the input stream is a blob";
      } else if (AS_ASCII.equals(parameter)) {
        return "Whether the input stream is used as an Ascii character input stream";
      } else if (LENGTH.equals(parameter)) {
        return "Specifies the input stream length";
      }
      return super.description(parameter);
    }

    @Override
    public void doUpdateValue(
        ResultSet resultSet, int column, InputStream value, Parametric parametric)
        throws SQLException {
      boolean asBlob = parametric.getBooleanValue(AS_BLOB, false);
      boolean asAscii = parametric.getBooleanValue(AS_ASCII, false);
      Long length = parametric.getLongValue(LENGTH);
      if (asBlob) {
        if (length == null) {
          resultSet.updateBlob(column, value);
        } else {
          resultSet.updateBlob(column, value, length);
        }
      } else {
        if (asAscii) {
          if (length == null) {
            resultSet.updateAsciiStream(column, value);
          } else {
            resultSet.updateAsciiStream(column, value, length);
          }
        } else {
          if (length == null) {
            resultSet.updateBinaryStream(column, value);
          } else {
            resultSet.updateBinaryStream(column, value, length);
          }
        }
      }
    }
  }

  private static class ArrayResultSetValueUpdater extends ResultSetValueUpdater<Array> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Array value, Parametric parametric)
        throws SQLException {
      resultSet.updateArray(column, value);
    }
  }

  private static class SQLXMLResultSetValueUpdater extends ResultSetValueUpdater<SQLXML> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, SQLXML value, Parametric parametric)
        throws SQLException {
      resultSet.updateSQLXML(column, value);
    }
  }

  private static class RefResultSetValueUpdater extends ResultSetValueUpdater<Ref> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, Ref value, Parametric parametric)
        throws SQLException {
      resultSet.updateRef(column, value);
    }
  }

  private static class RowIdResultSetValueUpdater extends ResultSetValueUpdater<RowId> {

    @Override
    public void doUpdateValue(ResultSet resultSet, int column, RowId value, Parametric parametric)
        throws SQLException {
      resultSet.updateRowId(column, value);
    }
  }

  private abstract static class ResultSetValueUpdater<T> extends CommonCategory<T> {

    public final void updateValue(ResultSet resultSet, int column, T value, Parametric parametric)
        throws SQLException {
      if (value == null) {
        resultSet.updateNull(column);
      } else {
        doUpdateValue(resultSet, column, value, parametric);
      }
    }

    public abstract void doUpdateValue(
        ResultSet resultSet, int column, T value, Parametric parametric) throws SQLException;
  }

  private static class NumberResultSetValueGetter extends ResultSetValueGetter<Number> {
    @Override
    public Number doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet,
          column,
          Types.NUMERIC,
          Types.TINYINT,
          Types.SMALLINT,
          Types.INTEGER,
          Types.BIGINT,
          Types.FLOAT,
          Types.DOUBLE,
          Types.DECIMAL);
      return resultSet.getBigDecimal(column);
    }
  }

  private static class BooleanResultSetValueGetter extends ResultSetValueGetter<Boolean> {

    @Override
    public Boolean doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet,
          column,
          Types.TINYINT,
          Types.SMALLINT,
          Types.INTEGER,
          Types.INTEGER,
          Types.CHAR,
          Types.VARCHAR);
      return resultSet.getBoolean(column);
    }
  }

  private static class ByteResultSetValueGetter extends ResultSetValueGetter<Byte> {
    @Override
    public Byte doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.NUMERIC, Types.TINYINT);
      return resultSet.getByte(column);
    }
  }

  private static class ShortResultSetValueGetter extends ResultSetValueGetter<Short> {
    @Override
    public Short doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.NUMERIC, Types.TINYINT, Types.SMALLINT);
      return resultSet.getShort(column);
    }
  }

  private static class IntegerResultSetValueGetter extends ResultSetValueGetter<Integer> {
    @Override
    public Integer doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet, column, Types.NUMERIC, Types.TINYINT, Types.SMALLINT, Types.INTEGER);
      return resultSet.getInt(column);
    }
  }

  private static class LongResultSetValueGetter extends ResultSetValueGetter<Long> {
    @Override
    public Long doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet,
          column,
          Types.NUMERIC,
          Types.TINYINT,
          Types.SMALLINT,
          Types.INTEGER,
          Types.BIGINT);
      return resultSet.getLong(column);
    }
  }

  private static class FloatResultSetValueGetter extends ResultSetValueGetter<Float> {
    @Override
    public Float doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet,
          column,
          Types.NUMERIC,
          Types.TINYINT,
          Types.SMALLINT,
          Types.INTEGER,
          Types.BIGINT,
          Types.FLOAT);
      return resultSet.getFloat(column);
    }
  }

  private static class DoubleResultSetValueGetter extends ResultSetValueGetter<Double> {
    @Override
    public Double doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet,
          column,
          Types.NUMERIC,
          Types.TINYINT,
          Types.SMALLINT,
          Types.INTEGER,
          Types.BIGINT,
          Types.FLOAT,
          Types.DOUBLE);
      return resultSet.getDouble(column);
    }
  }

  private static class BigDecimalResultSetValueGetter extends ResultSetValueGetter<BigDecimal> {
    @Override
    public BigDecimal doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet,
          column,
          Types.NUMERIC,
          Types.TINYINT,
          Types.SMALLINT,
          Types.INTEGER,
          Types.BIGINT,
          Types.FLOAT,
          Types.DOUBLE,
          Types.DECIMAL);
      return resultSet.getBigDecimal(column);
    }
  }

  private static class StringResultSetValueGetter extends ResultSetValueGetter<String> {

    @Override
    public String doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet,
          column,
          Types.CHAR,
          Types.VARCHAR,
          Types.LONGVARCHAR,
          Types.NCHAR,
          Types.NVARCHAR,
          Types.LONGNVARCHAR,
          Types.CLOB,
          Types.NCLOB);
      int columnType = resultSet.getMetaData().getColumnType(column);
      if (columnType == Types.NCHAR
          || columnType == Types.NVARCHAR
          || columnType == Types.LONGNVARCHAR
          || columnType == Types.NCLOB) {
        return resultSet.getNString(column);
      } else {
        return resultSet.getString(column);
      }
    }
  }

  private static class DateResultSetValueGetter extends ResultSetValueGetter<Date> {

    @Override
    public String[] parameters() {
      return new String[] {CALENDAR};
    }

    @Override
    public String description(String parameter) {
      if (CALENDAR.equals(parameter)) {
        return "Specify the calendar when setting the time，Must be of type [java.util.Calendar]";
      }
      return super.description(parameter);
    }

    @Override
    public Date doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet,
          column,
          Types.DATE,
          Types.TIMESTAMP,
          Types.TIMESTAMP_WITH_TIMEZONE,
          Types.TIME,
          Types.TIME_WITH_TIMEZONE);
      int columnType = resultSet.getMetaData().getColumnType(column);
      if (columnType == Types.TIME) {
        Date date = resultSet.getTime(column);
        if (date != null) {
          return new Date(date.getTime());
        }
      } else if (columnType == Types.TIMESTAMP) {
        Date date = resultSet.getTimestamp(column);
        if (date != null) {
          return new Date(date.getTime());
        }
      } else if (columnType == Types.TIME_WITH_TIMEZONE) {
        Object calendarObject = parametric.get(CALENDAR);
        Calendar calendar = null;
        if (calendarObject instanceof Calendar) {
          calendar = (Calendar) calendarObject;
        }
        Date date;
        if (calendar == null) {
          date = resultSet.getTime(column);
        } else {
          date = resultSet.getTime(column, calendar);
        }
        if (date != null) {
          return new Date(date.getTime());
        }
      } else if (columnType == Types.TIMESTAMP_WITH_TIMEZONE) {
        Object calendarObject = parametric.get(CALENDAR);
        Calendar calendar = null;
        if (calendarObject instanceof Calendar) {
          calendar = (Calendar) calendarObject;
        }
        Date date;
        if (calendar == null) {
          date = resultSet.getTimestamp(column);
        } else {
          date = resultSet.getTimestamp(column, calendar);
        }
        if (date != null) {
          return new Date(date.getTime());
        }
      } else {
        Object calendarObject = parametric.get(CALENDAR);
        Calendar calendar = null;
        if (calendarObject instanceof Calendar) {
          calendar = (Calendar) calendarObject;
        }
        Date date;
        if (calendar == null) {
          date = resultSet.getDate(column);
        } else {
          date = resultSet.getDate(column, calendar);
        }
        if (date != null) {
          return new Date(date.getTime());
        }
      }
      return null;
    }
  }

  private static class SqlDateResultSetValueGetter extends ResultSetValueGetter<java.sql.Date> {

    @Override
    public String[] parameters() {
      return new String[] {CALENDAR};
    }

    @Override
    public String description(String parameter) {
      if (CALENDAR.equals(parameter)) {
        return "Specify the calendar when setting the time，Must be of type [java.util.Calendar]";
      }
      return super.description(parameter);
    }

    @Override
    public java.sql.Date doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.DATE);
      Object calendarObject = parametric.get(CALENDAR);
      Calendar calendar = null;
      if (calendarObject instanceof Calendar) {
        calendar = (Calendar) calendarObject;
      }
      if (calendar == null) {
        return resultSet.getDate(column);
      }
      return resultSet.getDate(column, calendar);
    }
  }

  private static class SqlTimestampResultSetValueGetter extends ResultSetValueGetter<Timestamp> {

    @Override
    public String[] parameters() {
      return new String[] {CALENDAR};
    }

    @Override
    public String description(String parameter) {
      if (CALENDAR.equals(parameter)) {
        return "Specify the calendar when setting the time，Must be of type [java.util.Calendar]";
      }
      return super.description(parameter);
    }

    @Override
    public Timestamp doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE);
      Object calendarObject = parametric.get(CALENDAR);
      Calendar calendar = null;
      if (calendarObject instanceof Calendar) {
        calendar = (Calendar) calendarObject;
      }
      if (calendar == null) {
        return resultSet.getTimestamp(column);
      }
      return resultSet.getTimestamp(column, calendar);
    }
  }

  private static class SqlTimeResultSetValueGetter extends ResultSetValueGetter<Time> {

    @Override
    public String[] parameters() {
      return new String[] {CALENDAR};
    }

    @Override
    public String description(String parameter) {
      if (CALENDAR.equals(parameter)) {
        return "Specify the calendar when setting the time，Must be of type [java.util.Calendar]";
      }
      return super.description(parameter);
    }

    @Override
    public Time doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.TIME, Types.TIME_WITH_TIMEZONE);
      Object calendarObject = parametric.get(CALENDAR);
      Calendar calendar = null;
      if (calendarObject instanceof Calendar) {
        calendar = (Calendar) calendarObject;
      }
      if (calendar == null) {
        return resultSet.getTime(column);
      }
      return resultSet.getTime(column, calendar);
    }
  }

  private static class BytesResultSetValueGetter extends ResultSetValueGetter<byte[]> {
    @Override
    public byte[] doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY);
      return resultSet.getBytes(column);
    }
  }

  private static class ClobResultSetValueGetter extends ResultSetValueGetter<Clob> {
    @Override
    public Clob doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.CLOB);
      return resultSet.getClob(column);
    }
  }

  private static class NClobResultSetValueGetter extends ResultSetValueGetter<NClob> {
    @Override
    public NClob doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.NCLOB);
      return resultSet.getNClob(column);
    }
  }

  private static class ReaderResultSetValueGetter extends ResultSetValueGetter<Reader> {
    @Override
    public Reader doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(
          resultSet,
          column,
          Types.CHAR,
          Types.VARCHAR,
          Types.LONGVARCHAR,
          Types.NCHAR,
          Types.NVARCHAR,
          Types.LONGNVARCHAR);
      int columnType = resultSet.getMetaData().getColumnType(column);
      if (columnType == Types.NCHAR
          || columnType == Types.NVARCHAR
          || columnType == Types.LONGNVARCHAR) {
        return resultSet.getNCharacterStream(column);
      } else {
        return resultSet.getCharacterStream(column);
      }
    }
  }

  private static class BlobResultSetValueGetter extends ResultSetValueGetter<Blob> {
    @Override
    public Blob doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY);
      return resultSet.getBlob(column);
    }
  }

  private static class InputStreamResultSetValueGetter extends ResultSetValueGetter<InputStream> {
    @Override
    public InputStream doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY);
      return resultSet.getBinaryStream(column);
    }
  }

  private static class ArrayResultSetValueGetter extends ResultSetValueGetter<Array> {
    @Override
    public Array doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.ARRAY);
      return resultSet.getArray(column);
    }
  }

  private static class SQLXMLResultSetValueGetter extends ResultSetValueGetter<SQLXML> {
    @Override
    public SQLXML doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.SQLXML);
      return resultSet.getSQLXML(column);
    }
  }

  private static class RefResultSetValueGetter extends ResultSetValueGetter<Ref> {
    @Override
    public Ref doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.REF);
      return resultSet.getRef(column);
    }
  }

  private static class RowIdResultSetValueGetter extends ResultSetValueGetter<RowId> {
    @Override
    public RowId doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      checkExceptType(resultSet, column, Types.ROWID);
      return resultSet.getRowId(column);
    }
  }

  private abstract static class ResultSetValueGetter<T> extends CommonCategory<T> {

    public final T getValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException {
      T result = doGetValue(resultSet, column, parametric);
      if (resultSet.wasNull()) {
        return null;
      }
      return result;
    }

    public abstract T doGetValue(ResultSet resultSet, int column, Parametric parametric)
        throws SQLException;

    protected void checkExceptType(ResultSet resultSet, Object target, int... types)
        throws SQLException {
      int column =
          target instanceof Integer ? (Integer) target : resultSet.findColumn(target.toString());
      ResultSetMetaData metaData = resultSet.getMetaData();
      int columnType = metaData.getColumnType(column);
      for (int type : types) {
        if (type == columnType) {
          return;
        }
      }
      Class<?> clz = (Class<?>) category();
      StringBuilder message = new StringBuilder();
      message
          .append("The column(")
          .append(column)
          .append(") type is [")
          .append(TYPE_NAME.get(columnType))
          .append(":")
          .append(columnType)
          .append("],but require class [")
          .append(clz)
          .append("] and sql types is [");
      message.append(TYPE_NAME.get(types[0])).append("=").append(types[0]);
      for (int i = 1; i < types.length; i++) {
        message.append(",").append(TYPE_NAME.get(types[i])).append("=").append(types[i]);
      }
      message.append("]");
      throw new SQLException(message.toString());
    }
  }

  private abstract static class CommonCategory<T> implements CategorySelector.Category {

    private final Class<?> clz;

    public CommonCategory() {
      this.clz = HelperOnClass.extractGenericFromSuperclass(getClass(), 0);
    }

    @Override
    public final Object category() {
      return clz;
    }

    public String[] parameters() {
      return Tools.EMPTY_STRING_ARRAY;
    }

    public String description(String parameter) {
      return "Parameter [" + parameter + "] is not supported";
    }
  }
}

package pxf.toolkit.basic.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import pxf.toolkit.basic.lang.CategorySelector;

/**
 * Jdbc助手类
 *
 * @author potatoxf
 * @date 2021/3/27
 */
public class SQLHelper {

  public static final CategorySelector<PreparedStatementSetter>
      PREPARED_STATEMENT_SETTER_CATEGORY_SELECTOR =
          CategorySelector.of(
              new BooleanPreparedStatementSetter(),
              new BytePreparedStatementSetter(),
              new IntegerPreparedStatementSetter(),
              new LongPreparedStatementSetter(),
              new FloatPreparedStatementSetter(),
              new DoublePreparedStatementSetter(),
              new StringPreparedStatementSetter(),
              new BigDecimalPreparedStatementSetter(),
              new BigIntegerPreparedStatementSetter(),
              new DatePreparedStatementSetter(),
              new TimePreparedStatementSetter(),
              new TimestampPreparedStatementSetter(),
              new JavaDatePreparedStatementSetter());

  public static PreparedStatement setValue(
      PreparedStatement preparedStatement, int parameterIndex, Object value) throws SQLException {
    PreparedStatementSetter preparedStatementSetter =
        PREPARED_STATEMENT_SETTER_CATEGORY_SELECTOR.selectAction(value.getClass());
    preparedStatementSetter.setValue(preparedStatement, parameterIndex, value);
    return preparedStatement;
  }

  public static PreparedStatement setBitWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Byte value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.BIT);
    } else {
      preparedStatement.setByte(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setTinyintWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.TINYINT);
    } else {
      preparedStatement.setByte(parameterIndex, value.byteValue());
    }
    return preparedStatement;
  }

  public static PreparedStatement setSmallintWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.SMALLINT);
    } else {
      preparedStatement.setShort(parameterIndex, value.shortValue());
    }
    return preparedStatement;
  }

  public static PreparedStatement setIntegerWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.INTEGER);
    } else {
      preparedStatement.setLong(parameterIndex, value.intValue());
    }
    return preparedStatement;
  }

  public static PreparedStatement setBigintWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.BIGINT);
    } else {
      preparedStatement.setLong(parameterIndex, value.longValue());
    }
    return preparedStatement;
  }

  public static PreparedStatement setFloatWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.FLOAT);
    } else {
      preparedStatement.setLong(parameterIndex, value.longValue());
    }
    return preparedStatement;
  }

  public static PreparedStatement setDoubleWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.DOUBLE);
    } else {
      preparedStatement.setLong(parameterIndex, value.longValue());
    }
    return preparedStatement;
  }

  public static PreparedStatement setNumericWithNull(
      PreparedStatement preparedStatement, int parameterIndex, BigDecimal value)
      throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.NUMERIC);
    } else {
      preparedStatement.setBigDecimal(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setDecimalWithNull(
      PreparedStatement preparedStatement, int parameterIndex, BigDecimal value)
      throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.DECIMAL);
    } else {
      preparedStatement.setBigDecimal(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setCharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, String value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.CHAR);
    } else {
      preparedStatement.setString(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setVarcharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, String value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.VARCHAR);
    } else {
      preparedStatement.setString(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setLongVarcharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, String value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.LONGVARCHAR);
    } else {
      preparedStatement.setString(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setDateWithNull(
      PreparedStatement preparedStatement, int parameterIndex, java.util.Date value)
      throws SQLException {
    return setTimestampWithNull(
        preparedStatement, parameterIndex, value == null ? null : new Date(value.getTime()));
  }

  public static PreparedStatement setDateWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Date value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.DATE);
    } else {
      preparedStatement.setDate(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setTimeWithNull(
      PreparedStatement preparedStatement, int parameterIndex, java.util.Date value)
      throws SQLException {
    return setTimestampWithNull(
        preparedStatement, parameterIndex, value == null ? null : new Time(value.getTime()));
  }

  public static PreparedStatement setTimeWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Time value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.TIME);
    } else {
      preparedStatement.setTime(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setTimestampWithNull(
      PreparedStatement preparedStatement, int parameterIndex, java.util.Date value)
      throws SQLException {
    return setTimestampWithNull(
        preparedStatement, parameterIndex, value == null ? null : new Timestamp(value.getTime()));
  }

  public static PreparedStatement setTimestampWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Timestamp value)
      throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.TIMESTAMP);
    } else {
      preparedStatement.setTimestamp(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setBinaryWithNull(
      PreparedStatement preparedStatement, int parameterIndex, byte[] value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.BINARY);
    } else {
      preparedStatement.setBytes(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setVarbinaryWithNull(
      PreparedStatement preparedStatement, int parameterIndex, byte[] value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.VARBINARY);
    } else {
      preparedStatement.setBytes(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setLongVarbinaryWithNull(
      PreparedStatement preparedStatement, int parameterIndex, byte[] value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.LONGVARBINARY);
    } else {
      preparedStatement.setBytes(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setStructWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Struct value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.STRUCT);
    } else {
      preparedStatement.setObject(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setArrayWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Array value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.ARRAY);
    } else {
      preparedStatement.setArray(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setBlobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Blob value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.BLOB);
    } else {
      preparedStatement.setBlob(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setBlobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, InputStream value)
      throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.BLOB);
    } else {
      preparedStatement.setBlob(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setClobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Clob value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.CLOB);
    } else {
      preparedStatement.setClob(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setClobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Reader value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.CLOB);
    } else {
      preparedStatement.setClob(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setRefWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Ref value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.REF);
    } else {
      preparedStatement.setRef(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setDataLinkWithNull(
      PreparedStatement preparedStatement, int parameterIndex, URL value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.DATALINK);
    } else {
      preparedStatement.setURL(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setDataLinkWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Boolean value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.BOOLEAN);
    } else {
      preparedStatement.setBoolean(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setRowIdWithNull(
      PreparedStatement preparedStatement, int parameterIndex, RowId value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.ROWID);
    } else {
      preparedStatement.setRowId(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setDataLinkWithNull(
      PreparedStatement preparedStatement, int parameterIndex, RowId value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.ROWID);
    } else {
      preparedStatement.setRowId(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setNcharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Reader value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.NCHAR);
    } else {
      preparedStatement.setNCharacterStream(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setLongNVarcharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Reader value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.LONGNVARCHAR);
    } else {
      preparedStatement.setNCharacterStream(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setNClobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, NClob value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.NCLOB);
    } else {
      preparedStatement.setNClob(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setNClobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Reader value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.NCLOB);
    } else {
      preparedStatement.setNClob(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static PreparedStatement setSQLXMLWithNull(
      PreparedStatement preparedStatement, int parameterIndex, SQLXML value) throws SQLException {
    if (value == null) {
      preparedStatement.setNull(parameterIndex, Types.SQLXML);
    } else {
      preparedStatement.setSQLXML(parameterIndex, value);
    }
    return preparedStatement;
  }

  public static void setAutoCommitConnection(Connection connection, boolean autoCommit)
      throws SQLException {
    if (connection != null && autoCommit != connection.getAutoCommit()) {
      connection.setAutoCommit(autoCommit);
    }
  }

  public static void commitConnection(Connection connection) throws SQLException {
    if (connection != null && !connection.getAutoCommit()) {
      connection.commit();
    }
  }

  public static void rollbackConnection(Connection connection) throws SQLException {
    if (connection != null && !connection.getAutoCommit()) {
      connection.rollback();
    }
  }

  public static void closeConnection(Connection connection) throws SQLException {
    if (connection != null && !connection.isClosed()) {
      connection.close();
    }
  }

  private interface PreparedStatementSetter extends CategorySelector.Category {

    void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException;
  }

  private static class BooleanPreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Short.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setBoolean(parameterIndex, (Boolean) value);
    }
  }

  private static class BytePreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Byte.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setByte(parameterIndex, (Byte) value);
    }
  }

  private static class ShortPreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Short.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setShort(parameterIndex, (Short) value);
    }
  }

  private static class IntegerPreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Integer.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setInt(parameterIndex, (Integer) value);
    }
  }

  private static class LongPreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Long.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setLong(parameterIndex, (Long) value);
    }
  }

  private static class FloatPreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Float.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setFloat(parameterIndex, (Float) value);
    }
  }

  private static class DoublePreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Double.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setDouble(parameterIndex, (Double) value);
    }
  }

  private static class StringPreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return String.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setString(parameterIndex, (String) value);
    }
  }

  private static class BigDecimalPreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return BigDecimal.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setBigDecimal(parameterIndex, (BigDecimal) value);
    }
  }

  private static class BigIntegerPreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return BigInteger.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setLong(parameterIndex, ((BigInteger) value).longValueExact());
    }
  }

  private static class DatePreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Date.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setDate(parameterIndex, (Date) value);
    }
  }

  private static class TimePreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Time.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setTime(parameterIndex, (Time) value);
    }
  }

  private static class TimestampPreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return Timestamp.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setTimestamp(parameterIndex, (Timestamp) value);
    }
  }

  private static class JavaDatePreparedStatementSetter implements PreparedStatementSetter {

    @Override
    public Object category() {
      return java.util.Date.class;
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int parameterIndex, Object value)
        throws SQLException {
      preparedStatement.setTimestamp(
          parameterIndex, new Timestamp(((java.util.Date) value).getTime()));
    }
  }
}

package pxf.toolkit.basic.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jdbc静默助手类
 *
 * @author potatoxf
 * @date 2021/3/27
 */
public class SQLSilentHelper {

  private static final Logger LOG = LoggerFactory.getLogger(SQLSilentHelper.class);

  public static PreparedStatement setValue(
      PreparedStatement preparedStatement, int parameterIndex, Object value) {
    try {
      return SQLHelper.setValue(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setBitWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Byte value) {
    try {
      return SQLHelper.setBitWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setTinyintWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) {
    try {
      return SQLHelper.setTinyintWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setSmallintWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) {
    try {
      return SQLHelper.setSmallintWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setIntegerWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) {
    try {
      return SQLHelper.setIntegerWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setBigintWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) {
    try {
      return SQLHelper.setBigintWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setFloatWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) {
    try {
      return SQLHelper.setFloatWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setDoubleWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Number value) {
    try {
      return SQLHelper.setDoubleWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setNumericWithNull(
      PreparedStatement preparedStatement, int parameterIndex, BigDecimal value) {
    try {
      return SQLHelper.setNumericWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setDecimalWithNull(
      PreparedStatement preparedStatement, int parameterIndex, BigDecimal value) {
    try {
      return SQLHelper.setDecimalWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setCharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, String value) {
    try {
      return SQLHelper.setCharWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setVarcharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, String value) {
    try {
      return SQLHelper.setVarcharWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setLongVarcharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, String value) {
    try {
      return SQLHelper.setLongVarcharWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setDateWithNull(
      PreparedStatement preparedStatement, int parameterIndex, java.util.Date value) {
    try {
      return SQLHelper.setDateWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setDateWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Date value) {
    try {
      return SQLHelper.setDateWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setTimeWithNull(
      PreparedStatement preparedStatement, int parameterIndex, java.util.Date value) {
    try {
      return SQLHelper.setTimeWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setTimeWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Time value) {
    try {
      return SQLHelper.setTimeWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setTimestampWithNull(
      PreparedStatement preparedStatement, int parameterIndex, java.util.Date value) {
    try {
      return SQLHelper.setTimestampWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setTimestampWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Timestamp value) {
    try {
      return SQLHelper.setTimestampWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setBinaryWithNull(
      PreparedStatement preparedStatement, int parameterIndex, byte[] value) {
    try {
      return SQLHelper.setBinaryWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setVarbinaryWithNull(
      PreparedStatement preparedStatement, int parameterIndex, byte[] value) {
    try {
      return SQLHelper.setVarbinaryWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setLongVarbinaryWithNull(
      PreparedStatement preparedStatement, int parameterIndex, byte[] value) {
    try {
      return SQLHelper.setLongVarbinaryWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setStructWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Struct value) {
    try {
      return SQLHelper.setStructWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setArrayWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Array value) {
    try {
      return SQLHelper.setArrayWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setBlobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Blob value) {
    try {
      return SQLHelper.setBlobWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setBlobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, InputStream value) {
    try {
      return SQLHelper.setBlobWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setClobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Clob value) {
    try {
      return SQLHelper.setClobWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setClobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Reader value) {
    try {
      return SQLHelper.setClobWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setRefWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Ref value) {
    try {
      return SQLHelper.setRefWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setDataLinkWithNull(
      PreparedStatement preparedStatement, int parameterIndex, URL value) {
    try {
      return SQLHelper.setDataLinkWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setDataLinkWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Boolean value) {
    try {
      return SQLHelper.setDataLinkWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setRowIdWithNull(
      PreparedStatement preparedStatement, int parameterIndex, RowId value) {
    try {
      return SQLHelper.setRowIdWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setDataLinkWithNull(
      PreparedStatement preparedStatement, int parameterIndex, RowId value) {
    try {
      return SQLHelper.setDataLinkWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setNcharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Reader value) {
    try {
      return SQLHelper.setNcharWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setLongNVarcharWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Reader value) {
    try {
      return SQLHelper.setLongNVarcharWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setNClobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, NClob value) {
    try {
      return SQLHelper.setNClobWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setNClobWithNull(
      PreparedStatement preparedStatement, int parameterIndex, Reader value) {
    try {
      return SQLHelper.setNClobWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static PreparedStatement setSQLXMLWithNull(
      PreparedStatement preparedStatement, int parameterIndex, SQLXML value) {
    try {
      return SQLHelper.setSQLXMLWithNull(preparedStatement, parameterIndex, value);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static void setAutoCommitConnection(Connection connection, boolean autoCommit) {
    try {
      SQLHelper.setAutoCommitConnection(connection, autoCommit);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static void commitConnection(Connection connection) {
    try {
      SQLHelper.commitConnection(connection);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static void rollbackConnection(Connection connection) {
    try {
      SQLHelper.commitConnection(connection);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }

  public static void closeConnection(Connection connection) {
    try {
      SQLHelper.commitConnection(connection);
    } catch (SQLException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("SQL Exception", e);
      }
      throw new SQLRuntimeException(e);
    }
  }
}

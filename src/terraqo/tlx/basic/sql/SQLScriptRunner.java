package pxf.toolkit.basic.sql;

import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pxf.toolkit.basic.lang.iterators.LineIterable;
import pxf.toolkit.basic.util.Clear;
import pxf.toolkit.basic.util.Empty;

/**
 * 脚本运行
 *
 * @author potatoxf
 * @date 2021/3/28
 */
public class SQLScriptRunner {

  public static final String ERROR_EXECUTING_TEMPLATE = "Error executing: %n%s";
  private static final Logger LOG = LoggerFactory.getLogger(SQLScriptRunner.class);
  private static final String LINE_SEPARATOR = System.lineSeparator();
  private static final Pattern DELIMITER_PATTERN =
      Pattern.compile(
          "^\\s*((--)|(//))?\\s*(//)?\\s*@DELIMITER\\s+([^\\s]+)", Pattern.CASE_INSENSITIVE);
  private static final String DEFAULT_DELIMITER = ";";

  private final Connection connection;
  private StringBuilder returnResult;
  private boolean stopOnError = true;
  private boolean throwWarning = false;
  private boolean autoCommit = true;
  private boolean sendFullScript = true;
  private boolean removeCRs = true;
  private boolean escapeProcessing = true;
  private String delimiter = DEFAULT_DELIMITER;
  private boolean fullLineDelimiter;

  public SQLScriptRunner(Connection connection) {
    this.connection = connection;
  }

  public void runScript(Reader reader) {
    returnResult = new StringBuilder();
    SQLSilentHelper.setAutoCommitConnection(connection, autoCommit);
    try {
      LineIterable lineIterable = new LineIterable(reader);
      if (sendFullScript) {
        executeFullScript(lineIterable);
      } else {
        executeLineByLine(lineIterable);
      }
    } finally {
      SQLSilentHelper.rollbackConnection(connection);
    }
  }

  private void executeFullScript(LineIterable lineIterable) {
    StringBuilder script = new StringBuilder();
    try {
      for (String line : lineIterable) {
        script.append(line);
        script.append(LINE_SEPARATOR);
      }
      String command = script.toString();
      if (LOG.isDebugEnabled()) {
        LOG.debug(command);
      }
      executeStatement(command);
      SQLHelper.commitConnection(connection);
    } catch (SQLException e) {
      String message = String.format(ERROR_EXECUTING_TEMPLATE, script);
      if (LOG.isErrorEnabled()) {
        LOG.error(message, e);
      }
      throw new SQLRuntimeException(message, e);
    }
  }

  private void executeLineByLine(LineIterable lineIterable) {
    StringBuilder command = new StringBuilder();
    try {
      for (String line : lineIterable) {
        handleLine(command, line);
      }
      SQLHelper.commitConnection(connection);
      if (command.toString().trim().length() > 0) {
        throw new SQLRuntimeException(
            "Line missing end-of-line terminator (" + delimiter + ") => " + command);
      }
    } catch (SQLException e) {
      String message = String.format(ERROR_EXECUTING_TEMPLATE, command);
      if (LOG.isErrorEnabled()) {
        LOG.error(message, e);
      }
      throw new SQLRuntimeException(message, e);
    }
  }

  private void handleLine(StringBuilder command, String line) throws SQLException {
    String trimmedLine = line.trim();
    if (isLineComment(trimmedLine)) {
      Matcher matcher = DELIMITER_PATTERN.matcher(trimmedLine);
      if (matcher.find()) {
        delimiter = matcher.group(5);
      }
      if (LOG.isDebugEnabled()) {
        LOG.info(trimmedLine);
      }
    } else if (isCommandReadyToExecute(trimmedLine)) {
      command.append(line, 0, line.lastIndexOf(delimiter));
      command.append(LINE_SEPARATOR);
      String statement = Clear.stringBuilder(command);
      if (LOG.isDebugEnabled()) {
        LOG.info(statement);
      }
      executeStatement(statement);
    } else if (trimmedLine.length() > 0) {
      command.append(line);
      command.append(LINE_SEPARATOR);
    }
  }

  private boolean isLineComment(String trimmedLine) {
    return trimmedLine.startsWith("//") || trimmedLine.startsWith("--");
  }

  private boolean isCommandReadyToExecute(String trimmedLine) {
    // issue #561 remove anything after the delimiter
    return !fullLineDelimiter && trimmedLine.contains(delimiter)
        || fullLineDelimiter && trimmedLine.equals(delimiter);
  }

  private void executeStatement(String command) throws SQLException {
    try {
      Statement statement = connection.createStatement();
      statement.setEscapeProcessing(escapeProcessing);
      String sql = command;
      if (removeCRs) {
        sql = sql.replace("\r\n", "\n");
      }
      boolean hasResults = statement.execute(sql);
      while (!(!hasResults && statement.getUpdateCount() == -1)) {
        checkWarnings(statement);
        handleResults(statement, hasResults);
        hasResults = statement.getMoreResults();
      }
    } catch (SQLWarning e) {
      throw e;
    } catch (SQLException e) {
      if (stopOnError) {
        throw e;
      } else {
        if (LOG.isErrorEnabled()) {
          LOG.error(String.format(ERROR_EXECUTING_TEMPLATE, command), e);
        }
      }
    }
    // Ignore to workaround a bug in some connection pools
    // (Does anyone know the details of the bug?)
  }

  private void checkWarnings(Statement statement) throws SQLException {
    if (!throwWarning) {
      return;
    }
    // In Oracle, CREATE PROCEDURE, FUNCTION, etc. returns warning
    // instead of throwing exception if there is compilation error.
    SQLWarning warning = statement.getWarnings();
    if (warning != null) {
      throw warning;
    }
  }

  private void handleResults(Statement statement, boolean hasResults) throws SQLException {
    if (!hasResults) {
      return;
    }
    ResultSet rs = statement.getResultSet();
    ResultSetMetaData md = rs.getMetaData();
    int cols = md.getColumnCount();
    for (int i = 0; i < cols; i++) {
      String name = md.getColumnLabel(i + 1);
      returnResult.append(name).append("\t");
    }
    returnResult.append(LINE_SEPARATOR);
    while (rs.next()) {
      for (int i = 0; i < cols; i++) {
        String value = rs.getString(i + 1);
        returnResult.append(value).append("\t");
      }
      returnResult.append(LINE_SEPARATOR);
    }
  }

  public String getReturnResult() {
    if (returnResult == null) {
      return Empty.STRING;
    }
    return returnResult.toString();
  }

  public void setStopOnError(boolean stopOnError) {
    this.stopOnError = stopOnError;
  }

  public void setThrowWarning(boolean throwWarning) {
    this.throwWarning = throwWarning;
  }

  public void setAutoCommit(boolean autoCommit) {
    this.autoCommit = autoCommit;
  }

  public void setSendFullScript(boolean sendFullScript) {
    this.sendFullScript = sendFullScript;
  }

  public void setRemoveCRs(boolean removeCRs) {
    this.removeCRs = removeCRs;
  }

  public void setEscapeProcessing(boolean escapeProcessing) {
    this.escapeProcessing = escapeProcessing;
  }

  public void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  public void setFullLineDelimiter(boolean fullLineDelimiter) {
    this.fullLineDelimiter = fullLineDelimiter;
  }
}

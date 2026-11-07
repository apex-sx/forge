package pxf.toolkit.basic.sql;

/**
 * SQL异常
 *
 * @author potatoxf
 * @date 2021/3/28
 */
public class SQLRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 5224696788505678598L;

  public SQLRuntimeException(String message) {
    super(message);
  }

  public SQLRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public SQLRuntimeException(Throwable cause) {
    super(cause);
  }
}

package pxf.toolkit.basic.exception;

import pxf.toolkit.basic.ToolkitException;

/**
 * 解析异常
 *
 * @author potatoxf
 * @date 2021/3/25
 */
public class ParseException extends ToolkitException {

  public ParseException(Throwable e) {
    super(e);
  }

  public ParseException(String message, Object... args) {
    super(message, args);
  }

  public ParseException(Throwable e, String message, Object... args) {
    super(e, message, args);
  }
}

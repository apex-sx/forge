package pxf.toolkit.basic.exception;

import pxf.toolkit.basic.ToolkitException;

/**
 * 表达式异常
 *
 * @author potatoxf
 * @date 2021/5/7
 */
public class ExpressionException extends ToolkitException {

  public ExpressionException(Throwable e) {
    super(e);
  }

  public ExpressionException(String message, Object... args) {
    super(message, args);
  }

  public ExpressionException(Throwable e, String message, Object... args) {
    super(e, message, args);
  }
}

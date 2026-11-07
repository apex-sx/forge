package pxf.toolkit.basic.exception;

import pxf.toolkit.basic.ToolkitException;

/**
 * 非法调用错误
 *
 * @author potatoxf
 * @date 2021/4/23
 */
public class IllegalCallException extends ToolkitException {

  public IllegalCallException(String message, Object... args) {
    super(message, args);
  }

  public IllegalCallException(Throwable e, String message, Object... args) {
    super(e, message, args);
  }
}

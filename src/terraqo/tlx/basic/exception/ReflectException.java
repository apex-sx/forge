package pxf.toolkit.basic.exception;

import pxf.toolkit.basic.ToolkitException;

/**
 * @author potatoxf
 * @date 2021/5/10
 */
public class ReflectException extends ToolkitException {

  public ReflectException(Throwable e) {
    super(e);
  }

  public ReflectException(String message, Object... args) {
    super(message, args);
  }

  public ReflectException(Throwable e, String message, Object... args) {
    super(e, message, args);
  }
}

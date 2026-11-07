package pxf.toolkit.basic.exception;

import pxf.toolkit.basic.ToolkitException;

/**
 * @author potatoxf
 * @date 2021/4/22
 */
public class UnsupportedException extends ToolkitException {

  public UnsupportedException(String message, Object... args) {
    super(message, args);
  }

  public UnsupportedException(Throwable e, String message, Object... args) {
    super(e, message, args);
  }
}

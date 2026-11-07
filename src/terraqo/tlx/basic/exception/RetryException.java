package pxf.toolkit.basic.exception;

import pxf.toolkit.basic.ToolkitException;

/**
 * 重新尝试异常，重试多次后的异常
 *
 * @author potatoxf
 * @date 2021/3/28
 */
public class RetryException extends ToolkitException {

  public RetryException(Throwable e) {
    super(e);
  }

  public RetryException(String message, Object... args) {
    super(message, args);
  }

  public RetryException(Throwable e, String message, Object... args) {
    super(e, message, args);
  }
}

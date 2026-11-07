package pxf.toolkit.basic.exception;

import pxf.toolkit.basic.ToolkitException;
import pxf.toolkit.basic.function.Retryable;

/**
 * 表示可能是暂时的错误情况，即只需在间隔后重试同样的操作即可消除错误状态
 *
 * @author potatoxf
 * @date 2021/3/28
 */
public class TemporaryException extends ToolkitException {

  /** 可新尝试 */
  private Retryable retryable;

  public TemporaryException(Throwable e) {
    super(e);
  }

  public TemporaryException(String message, Object... args) {
    super(message, args);
  }

  public TemporaryException(Throwable e, String message, Object... args) {
    super(e, message, args);
  }

  public Retryable getRetryable() {
    return retryable;
  }

  public void setRetryable(Retryable retryable) {
    this.retryable = retryable;
  }
}

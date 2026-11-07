package pxf.toolkit.basic.exception;

import pxf.toolkit.basic.ToolkitException;

/**
 * 非正常代码异常
 *
 * <p>抛出此错误代表这代码逻辑错误，一般不可能抛出该异常，如果抛出该错误需要检测代码
 *
 * @author potatoxf
 * @date 2021/4/22
 */
public class AbnormalException extends ToolkitException {

  public AbnormalException() {
    super("Code error, please check the code");
  }

  public AbnormalException(String message, Object... args) {
    super(message, args);
  }

  public AbnormalException(Throwable e, String message, Object... args) {
    super(e, message, args);
  }
}

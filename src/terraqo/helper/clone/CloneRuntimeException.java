package potatoxf.helper.clone;

import potatoxf.helper.HelpString;
import potatoxf.helper.exceptions.ExceptionUtil;

/**
 * 克隆异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class CloneRuntimeException extends RuntimeException {
  private static final long serialVersionUID = 6774837422188798989L;

  public CloneRuntimeException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public CloneRuntimeException(String message) {
    super(message);
  }

  public CloneRuntimeException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public CloneRuntimeException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public CloneRuntimeException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

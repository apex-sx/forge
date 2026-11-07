package potatoxf.helper.thread;

import potatoxf.helper.HelpString;
import potatoxf.helper.exceptions.ExceptionUtil;

/**
 * 工具类异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class ThreadException extends RuntimeException {
  private static final long serialVersionUID = 5253124428623713216L;

  public ThreadException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public ThreadException(String message) {
    super(message);
  }

  public ThreadException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public ThreadException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public ThreadException(
      String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
    super(message, throwable, enableSuppression, writableStackTrace);
  }

  public ThreadException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

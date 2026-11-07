package potatoxf.helper.exceptions;

import potatoxf.helper.HelpString;

/**
 * 工具类异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class UtilException extends RuntimeException {
  private static final long serialVersionUID = 8247610319171014183L;

  public UtilException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public UtilException(String message) {
    super(message);
  }

  public UtilException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public UtilException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public UtilException(
      String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
    super(message, throwable, enableSuppression, writableStackTrace);
  }

  public UtilException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

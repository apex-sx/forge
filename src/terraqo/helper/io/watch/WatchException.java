package potatoxf.helper.io.watch;

import potatoxf.helper.HelpString;
import potatoxf.helper.exceptions.ExceptionUtil;

/**
 * 监听异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class WatchException extends RuntimeException {
  private static final long serialVersionUID = 8068509879445395353L;

  public WatchException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public WatchException(String message) {
    super(message);
  }

  public WatchException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public WatchException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public WatchException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

package potatoxf.helper.cron;

import potatoxf.helper.HelpString;

/**
 * 定时任务异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class CronException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public CronException(Throwable e) {
    super(e.getMessage(), e);
  }

  public CronException(String message) {
    super(message);
  }

  public CronException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public CronException(
      String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
    super(message, throwable, enableSuppression, writableStackTrace);
  }

  public CronException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

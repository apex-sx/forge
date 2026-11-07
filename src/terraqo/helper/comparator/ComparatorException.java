package potatoxf.helper.comparator;

import potatoxf.helper.HelpString;
import potatoxf.helper.exceptions.ExceptionUtil;

/**
 * 比较异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class ComparatorException extends RuntimeException {
  private static final long serialVersionUID = 4475602435485521971L;

  public ComparatorException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public ComparatorException(String message) {
    super(message);
  }

  public ComparatorException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public ComparatorException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public ComparatorException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

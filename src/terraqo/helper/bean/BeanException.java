package potatoxf.helper.bean;

import potatoxf.helper.HelpString;
import potatoxf.helper.exceptions.ExceptionUtil;

/**
 * Bean异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class BeanException extends RuntimeException {
  private static final long serialVersionUID = -8096998667745023423L;

  public BeanException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public BeanException(String message) {
    super(message);
  }

  public BeanException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public BeanException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public BeanException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

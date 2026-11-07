package potatoxf.helper.convert;

import potatoxf.helper.HelpString;
import potatoxf.helper.exceptions.ExceptionUtil;

/**
 * 转换异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class ConvertException extends RuntimeException {
  private static final long serialVersionUID = 4730597402855274362L;

  public ConvertException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public ConvertException(String message) {
    super(message);
  }

  public ConvertException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public ConvertException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public ConvertException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

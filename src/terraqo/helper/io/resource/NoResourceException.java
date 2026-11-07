package potatoxf.helper.io.resource;

import potatoxf.helper.HelpString;
import potatoxf.helper.exceptions.ExceptionUtil;
import potatoxf.helper.io.IORuntimeException;

/**
 * 资源文件或资源不存在异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class NoResourceException extends IORuntimeException {
  private static final long serialVersionUID = -623254467603299129L;

  public NoResourceException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public NoResourceException(String message) {
    super(message);
  }

  public NoResourceException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public NoResourceException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public NoResourceException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }

  /**
   * 导致这个异常的异常是否是指定类型的异常
   *
   * @param clazz 异常类
   * @return 是否为指定类型异常
   */
  @Override
  public boolean causeInstanceOf(Class<? extends Throwable> clazz) {
    final Throwable cause = this.getCause();
    return clazz.isInstance(cause);
  }
}

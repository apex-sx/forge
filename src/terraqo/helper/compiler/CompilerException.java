package potatoxf.helper.compiler;

import potatoxf.helper.HelpString;
import potatoxf.helper.exceptions.ExceptionUtil;

/**
 * 编译异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class CompilerException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public CompilerException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public CompilerException(String message) {
    super(message);
  }

  public CompilerException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public CompilerException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public CompilerException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

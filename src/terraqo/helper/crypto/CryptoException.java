package potatoxf.helper.crypto;

import potatoxf.helper.HelpString;
import potatoxf.helper.exceptions.ExceptionUtil;

/**
 * 加密异常
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class CryptoException extends RuntimeException {
  private static final long serialVersionUID = 8068509879445395353L;

  public CryptoException(Throwable e) {
    super(ExceptionUtil.getMessage(e), e);
  }

  public CryptoException(String message) {
    super(message);
  }

  public CryptoException(String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params));
  }

  public CryptoException(String message, Throwable throwable) {
    super(message, throwable);
  }

  public CryptoException(
      String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
    super(message, throwable, enableSuppression, writableStackTrace);
  }

  public CryptoException(Throwable throwable, String messageTemplate, Object... params) {
    super(HelpString.format(messageTemplate, params), throwable);
  }
}

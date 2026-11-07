package pxf.toolkit.basic;

/**
 * 工具异常
 *
 * @author potatoxf
 * @date 2021/4/23
 */
public class ToolkitException extends RuntimeException {

  public ToolkitException(Throwable e) {
    super(e);
  }

  public ToolkitException(String message, Object... args) {
    super(handleExceptionMessage(message, args));
  }

  public ToolkitException(Throwable e, String message, Object... args) {
    super(handleExceptionMessage(message, args), e);
  }

  public static String handleExceptionMessage(String message, Object... args) {
    if (args == null || args.length == 0) {
      return message;
    }
    return String.format(message, args);
  }
}

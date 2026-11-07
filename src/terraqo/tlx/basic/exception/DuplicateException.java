package pxf.toolkit.basic.exception;

/**
 * 重复异常
 *
 * @author potatoxf
 * @date 2021/5/3
 */
public class DuplicateException extends MessageHookException {

  public DuplicateException() {
    super();
  }

  public DuplicateException(String... propertyNames) {
    super(propertyNames);
  }
}

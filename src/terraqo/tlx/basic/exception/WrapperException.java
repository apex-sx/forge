package pxf.toolkit.basic.exception;

import pxf.toolkit.basic.ToolkitException;

/**
 * 包裹异常
 *
 * <p>当需要将异常以运行时异常的形式向上抛出
 *
 * @author potatoxf
 * @date 2021/3/25
 */
public final class WrapperException extends ToolkitException {

  public WrapperException(Throwable cause) {
    super(cause, "Parcel exception, please check the superior exception in the stack");
  }
}

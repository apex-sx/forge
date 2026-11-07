package pxf.toolkit.basic.exception;

import java.io.IOException;
import pxf.toolkit.basic.ToolkitException;

/**
 * IO流异常
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public class IOFlowException extends ToolkitException {

  public IOFlowException(String message) {
    super(message);
  }

  public IOFlowException(IOException cause) {
    super(cause);
  }

  public IOFlowException(String message, IOException cause) {
    super(message, cause);
  }
}

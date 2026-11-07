package pxf.toolkit.basic.reflect;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * 调用目标执行链
 *
 * @author potatoxf
 * @date 2021/5/8
 */
public final class InvocationTargetActuatorChain {

  private final List<InvocationTargetActuator> chain;
  private int point = 0;

  InvocationTargetActuatorChain(List<InvocationTargetActuator> chain) {
    Collections.sort(chain);
    this.chain = chain;
  }

  /**
   * 执行
   *
   * @param invocationTarget 调用目标
   * @param args 参数
   * @return 返回值
   */
  public Object execute(InvocationTarget invocationTarget, Object... args)
      throws InvocationTargetException {
    if (chain.size() > point) {
      InvocationTargetActuator invocationTargetActuator = chain.get(point++);
      return invocationTargetActuator.execute(invocationTarget, this, args);
    }
    throw new InvocationTargetException(null, "Fail to call '" + invocationTarget + "'");
  }
}

package pxf.toolkit.basic.reflect;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import pxf.toolkit.basic.function.Builder;

/**
 * 调用目标工厂
 *
 * @author potatoxf
 * @date 2021/5/8
 */
public class InvocationTargetActuatorChainBuilder
    implements Builder<InvocationTargetActuatorChain> {

  private List<InvocationTargetActuator> chain;

  /**
   * 注册 {@code InvocationTargetActuator}
   *
   * @param invocationTargetActuator {@code InvocationTargetActuator}
   */
  public void register(InvocationTargetActuator invocationTargetActuator) {
    if (chain == null) {
      chain = new LinkedList<>();
    }
    chain.add(invocationTargetActuator);
  }
  /**
   * 构建对象
   *
   * @return {@code InvocationTargetActuatorChain}
   */
  @Override
  public InvocationTargetActuatorChain build() {
    return new InvocationTargetActuatorChain(new ArrayList<>(chain));
  }
}

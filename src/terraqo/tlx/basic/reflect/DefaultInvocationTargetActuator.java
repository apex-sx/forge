package pxf.toolkit.basic.reflect;

import java.lang.reflect.InvocationTargetException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 调用目标执行器
 *
 * @author potatoxf
 * @date 2021/5/8
 */
public class DefaultInvocationTargetActuator implements InvocationTargetActuator {

  /**
   * 执行
   *
   * @param invocationTarget 调用目标
   * @param invocationTargetActuatorChain 执行链
   * @param args 参数
   * @return 返回值
   * @throws InvocationTargetException 当调用目标发生错误时
   */
  @Nullable
  @Override
  public Object execute(
      @Nonnull InvocationTarget invocationTarget,
      @Nonnull InvocationTargetActuatorChain invocationTargetActuatorChain,
      @Nullable Object... args)
      throws InvocationTargetException {
    Class<?> type = invocationTarget.getType();
    if (type != null && invocationTarget.getMethodName() != null) {
      try {
        return ReflectHelper.invokeStaticMethod(
            type, invocationTarget.getMethodName(), invocationTarget.getRealParamValues());
      } catch (NoSuchMethodException e1) {
        Object target = ReflectHelper.newInstance(type);
        try {
          return ReflectHelper.invokeMethod(
              type,
              target,
              invocationTarget.getMethodName(),
              invocationTarget.getRealParamValues());
        } catch (NoSuchMethodException ignored) {
        }
      }
    }
    return invocationTargetActuatorChain.execute(invocationTarget, args);
  }
}

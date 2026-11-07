package pxf.toolkit.basic.function;

import javax.annotation.Nullable;

/**
 * 参数提供器
 *
 * @param <P> 参数类型
 * @param <T> 结果类型
 * @author potatoxf
 * @date 2021/2/6
 */
@FunctionalInterface
public interface Factory<P, T> {

  /**
   * 获取一个结果
   *
   * @param param 参数
   * @return 获取一个结果
   */
  @Nullable
  T get(@Nullable P param);
}

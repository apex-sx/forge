package pxf.toolkit.basic.function;

import java.util.Map;
import javax.annotation.Nullable;

/**
 * 工厂
 *
 * @author potatoxf
 * @date 2021/2/6
 */
@FunctionalInterface
public interface CommonFactory<T> extends Factory<Map, T> {

  /**
   * 获取一个结果
   *
   * @param mapParam {@code Prop}参数
   * @return 获取一个结果
   */
  @Nullable
  @Override
  T get(@Nullable Map mapParam);
}

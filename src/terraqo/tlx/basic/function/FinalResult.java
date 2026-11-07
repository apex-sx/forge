package pxf.toolkit.basic.function;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 最终结果，一般用户执行不是通过返回值返回
 *
 * @author potatoxf
 * @date 2021/1/11
 */
@FunctionalInterface
public interface FinalResult<T> extends Supplier<Optional<T>> {

  /**
   * 获取最终结果
   *
   * @return {@code Optional<T>}
   */
  @Override
  Optional<T> get();
}

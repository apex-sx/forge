package pxf.toolkit.basic.function;

/**
 * 构建器顶级接口
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public interface Builder<T> {

  /**
   * 构建对象
   *
   * @return {@code T}
   */
  T build();
}

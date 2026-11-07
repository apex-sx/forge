package pxf.toolkit.basic.function;

/**
 * 自身引用
 *
 * @author potatoxf
 * @date 2021/3/12
 */
public interface Reference<T> {

  /**
   * 返回自身引用
   *
   * @return 返回自身引用
   */
  @SuppressWarnings("unchecked")
  default T self() {
    return (T) this;
  }
}

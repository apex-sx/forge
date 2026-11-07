package potatoxf.helper.lang.copier;

/**
 * 拷贝接口
 *
 * @author potatoxf
 * @date 2021/9/26
 * @param <T> 拷贝目标类型
 */
@FunctionalInterface
public interface Copier<T> {
  /**
   * 执行拷贝
   *
   * @return 拷贝的目标
   */
  T copy();
}

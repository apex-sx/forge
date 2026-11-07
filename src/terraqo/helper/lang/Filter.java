package potatoxf.helper.lang;

/**
 * 过滤器接口
 *
 * @author potatoxf
 * @date 2021/9/26
 */
@FunctionalInterface
public interface Filter<T> {
  /**
   * 是否接受对象
   *
   * @param t 检查的对象
   * @return 是否接受对象
   */
  boolean accept(T t);
}

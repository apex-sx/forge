package potatoxf.helper.lang.hash;

/**
 * Hash计算接口
 *
 * @param <T> 被计算hash的对象类型
 * @author potatoxf
 * @date 2021/9/26
 */
@FunctionalInterface
public interface Hash64<T> extends Hash<T> {
  /**
   * 计算Hash值
   *
   * @param t 对象
   * @return hash
   */
  long hash64(T t);

  @Override
  default Number hash(T t) {
    return hash64(t);
  }
}

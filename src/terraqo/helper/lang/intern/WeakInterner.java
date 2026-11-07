package potatoxf.helper.lang.intern;

import potatoxf.helper.api.map.WeakConcurrentMap;

/**
 * 使用WeakHashMap(线程安全)存储对象的规范化对象，注意此对象需单例使用！<br>
 *
 * @author potatoxf
 * @date 2021/9/26
 */
public class WeakInterner<T> implements Interner<T> {

  private final WeakConcurrentMap<T, T> cache = new WeakConcurrentMap<>();

  @Override
  public T intern(T sample) {
    if (null == sample) {
      return null;
    }
    return cache.computeIfAbsent(sample, (key) -> sample);
  }
}

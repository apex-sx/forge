package potatoxf.helper.api.map;

import potatoxf.helper.util.ReferenceUtil;

import java.lang.ref.Reference;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 线程安全的WeakMap实现<br>
 * 参考：jdk.management.resource.internal.WeakKeyConcurrentHashMap
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author potatoxf
 * @date 2021/9/26
 */
public class WeakConcurrentMap<K, V> extends ReferenceConcurrentMap<K, V> {

  /** 构造 */
  public WeakConcurrentMap() {
    this(new ConcurrentHashMap<>());
  }

  /**
   * 构造
   *
   * @param raw {@link ConcurrentMap}实现
   */
  public WeakConcurrentMap(ConcurrentMap<Reference<K>, V> raw) {
    super(raw, ReferenceUtil.ReferenceType.WEAK);
  }
}

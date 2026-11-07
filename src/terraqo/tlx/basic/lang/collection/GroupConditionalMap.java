package pxf.toolkit.basic.lang.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * 分组条件Map，当满足条件时才进行插入
 *
 * @author potatoxf
 * @date 2021/5/15
 */
public class GroupConditionalMap<K, V> extends HashMap<K, V> {

  private final ValueComparor<V> valueComparor;

  public GroupConditionalMap(ValueComparor<V> valueComparor) {
    this.valueComparor = valueComparor != null ? valueComparor : (o, n) -> true;
  }

  @Override
  public V put(K key, V value) {
    V v = get(key);
    if (v != null) {
      boolean compare = valueComparor.compare(v, value);
      if (!compare) {
        return v;
      }
    }
    return super.put(key, value);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    throw new UnsupportedOperationException("Does not support 'putAll' operations");
  }

  @Override
  public V putIfAbsent(K key, V value) {
    V v = get(key);
    if (v != null) {
      boolean compare = valueComparor.compare(v, value);
      if (!compare) {
        return v;
      }
    }
    return super.putIfAbsent(key, value);
  }

  @FunctionalInterface
  public interface ValueComparor<V> {

    /**
     * 新值和旧值比较器
     *
     * @param oldValue 老值
     * @param newValue 新值
     * @return 如果 {@code true}则新值取代旧值，否则不取代
     */
    boolean compare(V oldValue, V newValue);
  }
}

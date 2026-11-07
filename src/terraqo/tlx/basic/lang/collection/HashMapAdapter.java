package pxf.toolkit.basic.lang.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

/**
 * HashMap适配器
 *
 * @author potatoxf
 * @date 2020/11/25
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class HashMapAdapter<K, V> implements Map<K, V>, Cloneable, Serializable {

  private static final long serialVersionUID = -8604470181398171643L;
  protected final HashMap hashMap;

  public HashMapAdapter() {
    hashMap = new HashMap<>();
  }

  public HashMapAdapter(int initialCapacity, float loadFactor) {
    hashMap = new HashMap<>(initialCapacity, loadFactor);
  }

  public HashMapAdapter(int initialCapacity) {
    hashMap = new HashMap<>(initialCapacity);
  }

  public HashMapAdapter(Map<? extends K, ? extends V> m) {
    hashMap = new HashMap<>(m);
  }

  @Override
  public int size() {
    return hashMap.size();
  }

  @Override
  public boolean isEmpty() {
    return hashMap.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return hashMap.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return hashMap.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return (V) hashMap.get(key);
  }

  @Override
  public V put(K key, V value) {
    return (V) hashMap.put(key, value);
  }

  @Override
  public V remove(Object key) {
    return (V) hashMap.remove(key);
  }

  @Override
  public void putAll(@Nonnull Map<? extends K, ? extends V> m) {
    hashMap.putAll(m);
  }

  @Override
  public void clear() {
    hashMap.clear();
  }

  @Nonnull
  @Override
  public Set<K> keySet() {
    return hashMap.keySet();
  }

  @Nonnull
  @Override
  public Collection<V> values() {
    return hashMap.values();
  }

  @Nonnull
  @Override
  public Set<Entry<K, V>> entrySet() {
    return hashMap.entrySet();
  }
}

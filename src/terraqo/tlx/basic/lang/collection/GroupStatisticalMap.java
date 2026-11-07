package pxf.toolkit.basic.lang.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * 分组统计计算
 *
 * @author potatoxf
 * @date 2021/5/15
 */
public class GroupStatisticalMap<K> extends HashMap<K, Integer> {

  public static final int DEFAULT_INIT_VALUE = 0;

  private final int defaultInitValue;

  public GroupStatisticalMap() {
    this(DEFAULT_INIT_VALUE);
  }

  public GroupStatisticalMap(int defaultInitValue) {
    this.defaultInitValue = defaultInitValue;
  }

  /**
   * 在原来基础上加值，如果没有则创建值再加值
   *
   * @param key 键
   * @param value 值
   * @return 返回新值
   */
  public int add(K key, int value) {
    Integer oldValue = get(key);
    int newValue;
    if (oldValue == null) {
      newValue = defaultInitValue + value;
    } else {
      newValue = oldValue + value;
    }
    put(key, newValue);
    return newValue;
  }
  /**
   * 在原来基础上自加值，如果没有则创建值自加值
   *
   * @param key 键
   * @return 返回新值
   */
  public int increase(K key) {
    return add(key, 1);
  }
  /**
   * 在原来基础上减值，如果没有则创建值再减值
   *
   * @param key 键
   * @param value 值
   * @return 返回新值
   */
  public int sub(K key, int value) {
    Integer oldValue = get(key);
    int newValue;
    if (oldValue == null) {
      newValue = defaultInitValue - value;
    } else {
      newValue = oldValue - value;
    }
    put(key, newValue);
    return newValue;
  }
  /**
   * 在原来基础上自减值，如果没有则创建值自减值
   *
   * @param key 键
   * @return 返回新值
   */
  public int decrease(K key) {
    return sub(key, 1);
  }

  @Override
  public Integer put(K key, Integer value) {
    if (value == null) {
      value = defaultInitValue;
    }
    return super.put(key, value);
  }

  @Override
  public void putAll(Map<? extends K, ? extends Integer> m) {
    throw new UnsupportedOperationException("Does not support 'putAll' operations");
  }

  @Override
  public Integer putIfAbsent(K key, Integer value) {
    if (value == null) {
      value = defaultInitValue;
    }
    return super.putIfAbsent(key, value);
  }
}

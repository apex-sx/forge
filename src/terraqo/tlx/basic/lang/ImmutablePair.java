package pxf.toolkit.basic.lang;

/**
 * 值不允许改变的键值对
 *
 * @author potatoxf
 * @date 2021/4/13
 */
public class ImmutablePair<K, V> extends Pair<K, V> {

  public ImmutablePair(K key, V value) {
    super.setKey(key);
    super.setValue(value);
  }

  @Override
  public void setKey(K key) {
    throw new UnsupportedOperationException("Does not support modifier keys");
  }

  @Override
  public void setValue(V value) {
    throw new UnsupportedOperationException("Does not support modifying values");
  }
}

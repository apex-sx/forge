package pxf.toolkit.basic.lang.comparators;

import java.util.Map;

/**
 * @author potatoxf
 * @date 2021/4/1
 */
public class MapComparator extends AbstractComparator<Map> {

  private final Object key;

  public MapComparator(Object key) {
    this(key, false);
  }

  public MapComparator(Object key, boolean isNullElementLast) {
    super(isNullElementLast);
    this.key = key;
  }

  @Override
  protected int doCompare(Map o1, Map o2) {
    Object v1 = o1.get(key);
    Object v2 = o2.get(key);
    if (v1 == null && v2 == null) {
      return 0;
    }
    if (v1 == null) {
      return -1;
    }
    if (v2 == null) {
      return 1;
    }
    Class<?> c1 = v1.getClass();
    Class<?> c2 = v2.getClass();
    if (v1 instanceof Comparable && v2 instanceof Comparable) {
      if (c1.equals(c2)) {
        return ((Comparable) v1).compareTo(v2);
      }
      if (c1.isAssignableFrom(c2)) {
        return ((Comparable) v1).compareTo(v2);
      }
      if (c2.isAssignableFrom(c1)) {
        return -((Comparable) v2).compareTo(v1);
      }
      throw new IllegalArgumentException(
          String.format(
              "The key [%s] association value type does not match for [%s] and [%s]", key, c1, c2));
    }
    throw new IllegalArgumentException(
        String.format(
            "The key [%s] association value type does not implements comparator for [%s] and [%s]",
            key, c1, c2));
  }
}

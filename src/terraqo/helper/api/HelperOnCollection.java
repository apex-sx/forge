package potatoxf.helper.api;

import potatoxf.helper.api.function.FactoryThrow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 集合助手类
 *
 * @author potatoxf
 * @date 2021/7/23
 */
@SuppressWarnings("unchecked")
public final class HelperOnCollection {

  private HelperOnCollection() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * @param main
   * @param others
   * @param <T>
   * @return
   */
  @SafeVarargs
  public static <T> Set<T> differenceSet(Collection<T> main, Collection<T>... others) {
    Set<T> result = new HashSet<>(main);
    for (Collection<T> other : others) {
      result.removeAll(other);
    }
    return result;
  }

  /**
   * 通过字符串将集合里的元素去重
   *
   * @param inputs 输入集合
   * @param <T> 元素类型
   * @return 去重后的集合
   */
  public static <T> Collection<T> distinctByString(Collection<T> inputs) {
    return distinctByString(inputs, null);
  }
  /**
   * 通过字符串将集合里的元素去重
   *
   * @param inputs 输入集合
   * @param toStringHandler 将元素转换成字符串
   * @param <T> 元素类型
   * @return 去重后的集合
   */
  public static <T> Collection<T> distinctByString(
      Collection<T> inputs, Function<T, String> toStringHandler) {
    if (toStringHandler == null) {
      toStringHandler = T::toString;
    }
    Map<String, T> distinct = new LinkedHashMap<>(inputs.size());
    for (T t : inputs) {
      distinct.put(toStringHandler.apply(t), t);
    }
    return distinct.values();
  }

  /**
   * @param removedCollection
   * @param targetCollection
   * @param condition
   * @param <T>
   */
  public static <T> void removeAllByCondition(
      Collection<? extends T> removedCollection,
      Collection<? extends T> targetCollection,
      BiPredicate<? super T, ? super T> condition) {
    Iterator<? extends T> it1 = removedCollection.iterator();
    while (it1.hasNext()) {
      T a1 = it1.next();
      Iterator<? extends T> it2 = targetCollection.iterator();
      boolean isFind = false;
      while (it2.hasNext()) {
        T a2 = it2.next();
        if (condition.test(a1, a2)) {
          isFind = true;
          break;
        }
      }
      if (!isFind) {
        it1.remove();
      }
    }
  }

  /**
   * @param collection1
   * @param collection2
   * @param targetExtractor
   * @param targetComparator
   * @param targetCondition
   */
  public static <E, T> List<T>[] compareListByCondition(
      final Collection<E> collection1,
      final Collection<E> collection2,
      final Function<E, T> targetExtractor,
      final Comparator<T> targetComparator,
      final BiPredicate<T, T> targetCondition) {

    Stream<T> stream1 =
        HelperOnCollection.differenceSet(collection1, collection2).stream().map(targetExtractor);
    if (targetComparator != null) {
      stream1 = stream1.sorted(targetComparator);
    }
    List<T> list1 = stream1.collect(Collectors.toList());

    Stream<T> stream2 =
        HelperOnCollection.differenceSet(collection2, collection1).stream().map(targetExtractor);
    if (targetComparator != null) {
      stream2 = stream2.sorted(targetComparator);
    }
    List<T> list2 = stream2.collect(Collectors.toList());

    HelperOnCollection.removeAllByCondition(list1, list2, targetCondition);
    HelperOnCollection.removeAllByCondition(list2, list1, targetCondition);
    return new List[] {list1, list2};
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素数组
   */
  public static void addAll(Collection<Boolean> container, boolean... elements) {
    for (boolean element : elements) {
      container.add(element);
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素数组
   */
  public static void addAll(Collection<Byte> container, byte... elements) {
    for (byte element : elements) {
      container.add(element);
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素数组
   */
  public static void addAll(Collection<Character> container, char... elements) {
    for (char element : elements) {
      container.add(element);
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素数组
   */
  public static void addAll(Collection<Short> container, short... elements) {
    for (short element : elements) {
      container.add(element);
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素数组
   */
  public static void addAll(Collection<Integer> container, int... elements) {
    for (int element : elements) {
      container.add(element);
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素数组
   */
  public static void addAll(Collection<Long> container, long... elements) {
    for (long element : elements) {
      container.add(element);
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素数组
   */
  public static void addAll(Collection<Float> container, float... elements) {
    for (float element : elements) {
      container.add(element);
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素数组
   */
  public static void addAll(Collection<Double> container, double... elements) {
    for (double element : elements) {
      container.add(element);
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素数组
   */
  public static <E> void addAll(Collection<E> container, E[] elements) {
    container.addAll(Arrays.asList(elements));
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素
   */
  public static <E> void addAll(Collection<E> container, Enumeration<E> elements) {
    while (elements.hasMoreElements()) {
      container.add(elements.nextElement());
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素
   */
  public static <E> void addAll(Collection<E> container, Iterable<E> elements) {
    for (E element : elements) {
      container.add(element);
    }
  }

  /**
   * 将所有数组元素添加到集合中
   *
   * @param container 集合容器
   * @param elements 元素
   */
  public static <E> void addAll(Collection<E> container, Iterator<E> elements) {
    while (elements.hasNext()) {
      container.add(elements.next());
    }
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param container Map集合
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  public static boolean getBooleanValue(Map<?, ?> container, Object key, boolean defaultValue) {
    Boolean value = getBooleanValue(container, key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param container Map集合
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  public static char getCharValue(Map<?, ?> container, Object key, char defaultValue) {
    Character value = getCharacterValue(container, key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param container Map集合
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  public static byte getByteValue(Map<?, ?> container, Object key, byte defaultValue) {
    Byte value = getByteValue(container, key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param container Map集合
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  public static short getShortValue(Map<?, ?> container, Object key, short defaultValue) {
    Short value = getShortValue(container, key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param container Map集合
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  public static int getIntValue(Map<?, ?> container, Object key, int defaultValue) {
    Integer value = getIntegerValue(container, key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param container Map集合
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  public static long getLongValue(Map<?, ?> container, Object key, long defaultValue) {
    Long value = getLongValue(container, key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param container Map集合
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  public static float getFloatValue(Map<?, ?> container, Object key, float defaultValue) {
    Float value = getFloatValue(container, key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回默认值
   *
   * @param container Map集合
   * @param key 键
   * @param defaultValue 默认值
   * @return 返回获取到的值，如果不存在或是不符合要求返回默认值
   */
  public static double getDoubleValue(Map<?, ?> container, Object key, double defaultValue) {
    Double value = getDoubleValue(container, key);
    if (value != null) {
      return value;
    }
    return defaultValue;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Boolean getBooleanValue(Map<?, ?> container, Object key) {
    if (container != null) {
      Object value = container.get(key);
      if (value != null) {
        if (value instanceof Boolean) {
          return (Boolean) value;
        } else {
          Number numberValue = getNumberValue(container, key);
          if (numberValue != null) {
            return numberValue.intValue() != 0;
          } else {
            String booleanString;
            if (value instanceof String) {
              booleanString = (String) value;
            } else {
              booleanString = value.toString();
            }
            if ("true".equalsIgnoreCase(booleanString)) {
              return true;
            }
            if ("false".equalsIgnoreCase(booleanString)) {
              return false;
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Character getCharacterValue(Map<?, ?> container, Object key) {
    if (container != null) {
      Object value = container.get(key);
      if (value != null) {
        if (value instanceof Character) {
          return (Character) value;
        } else {
          Number numberValue = getNumberValue(container, key);
          if (numberValue != null) {
            int i = numberValue.intValue();
            if (i >= 0 && i <= 9) {
              return String.valueOf(i).charAt(0);
            }
          } else {
            CharSequence string;
            if (value instanceof CharSequence) {
              string = (CharSequence) value;
            } else {
              string = value.toString();
            }
            if (string.length() == 1) {
              return string.charAt(0);
            }
          }
        }
      }
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Byte getByteValue(Map<?, ?> container, Object key) {
    Number numberValue = getNumberValue(container, key);
    if (numberValue != null) {
      return numberValue.byteValue();
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Short getShortValue(Map<?, ?> container, Object key) {
    Number numberValue = getNumberValue(container, key);
    if (numberValue != null) {
      return numberValue.shortValue();
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Integer getIntegerValue(Map<?, ?> container, Object key) {
    Number numberValue = getNumberValue(container, key);
    if (numberValue != null) {
      return numberValue.intValue();
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Long getLongValue(Map<?, ?> container, Object key) {
    Number numberValue = getNumberValue(container, key);
    if (numberValue != null) {
      return numberValue.longValue();
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Float getFloatValue(Map<?, ?> container, Object key) {
    Number numberValue = getNumberValue(container, key);
    if (numberValue != null) {
      return numberValue.floatValue();
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Double getDoubleValue(Map<?, ?> container, Object key) {
    Number numberValue = getNumberValue(container, key);
    if (numberValue != null) {
      return numberValue.doubleValue();
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static BigInteger getBigIntegerValue(Map<?, ?> container, Object key) {
    Number numberValue = getNumberValue(container, key);
    if (numberValue != null) {
      if (numberValue instanceof BigDecimal) {
        return ((BigDecimal) numberValue).toBigIntegerExact();
      } else {
        return BigInteger.valueOf(numberValue.longValue());
      }
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static BigDecimal getBigDecimalValue(Map<?, ?> container, Object key) {
    Number numberValue = getNumberValue(container, key);
    if (numberValue != null) {
      if (numberValue instanceof BigDecimal) {
        return (BigDecimal) numberValue;
      } else {
        return BigDecimal.valueOf(numberValue.doubleValue());
      }
    }
    return null;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static String getStringValue(Map<?, ?> container, Object key) {
    return getStringValue(container, key, null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static String getStringValue(Map<?, ?> container, Object key, String defaultValue) {
    if (container != null) {
      Object value = container.get(key);
      if (value != null) {
        if (value instanceof String) {
          return (String) value;
        } else {
          return value.toString();
        }
      }
    }
    return defaultValue;
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Object getObjectValue(Map<?, ?> container, Object key) {
    return getObjectValue(container, key, null);
  }

  /**
   * 获取元素，如果不存在或是不符合要求返回null
   *
   * @param container Map集合
   * @param key 键
   * @return 返回获取到的值，如果不存在或是不符合要求返回null
   */
  public static Object getObjectValue(Map<?, ?> container, Object key, Object defaultValue) {
    if (container != null) {
      Object value = container.get(key);
      if (value != null) {
        return value;
      }
    }
    return defaultValue;
  }

  /**
   * 获取数字值
   *
   * @param container Map容器
   * @param key 键
   * @return 返回数字元素
   */
  private static Number getNumberValue(Map<?, ?> container, Object key) {
    if (container != null) {
      Object value = container.get(key);
      if (value != null) {
        if (value instanceof Number) {
          return (Number) value;
        } else {
          String numberString;
          if (value instanceof String) {
            numberString = (String) value;
          } else {
            numberString = value.toString();
          }
          try {
            return new BigDecimal(numberString);
          } catch (NumberFormatException ignored) {
          }
        }
      }
    }
    return null;
  }

  /**
   * 构造不可修改Set
   *
   * @param values 一系列值
   * @param <T> 类型
   * @return 返回Set
   */
  @SafeVarargs
  public static <T> Set<T> ofUnmodifiableSet(T... values) {
    return Collections.unmodifiableSet(ofSet(values));
  }

  /**
   * 构造Set
   *
   * @param values 一系列值
   * @param <T> 类型
   * @return 返回Set
   */
  @SafeVarargs
  public static <T> Set<T> ofSet(T... values) {
    Set<T> set = new HashSet<>();
    Collections.addAll(set, values);
    return set;
  }

  /**
   * 构造Set
   *
   * @param values 一系列值
   * @param <T> 类型
   * @return 返回Set
   */
  @SafeVarargs
  public static <T> Set<T> ofLinkedSet(T... values) {
    Set<T> set = new LinkedHashSet<T>();
    Collections.addAll(set, values);
    return set;
  }

  /**
   * 构造不可修改List
   *
   * @param values 一系列值
   * @param <T> 类型
   * @return 返回List
   */
  @SafeVarargs
  public static <T> List<T> ofUnmodifiableList(T... values) {
    return Collections.unmodifiableList(ofList(values));
  }

  /**
   * 构造List
   *
   * @param values 一系列值
   * @param <T> 类型
   * @return 返回List
   */
  @SafeVarargs
  public static <T> List<T> ofList(T... values) {
    List<T> list = new ArrayList<>(values.length);
    Collections.addAll(list, values);
    return list;
  }

  /**
   * 构造Map
   *
   * @param k 键
   * @param v 值
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(K k, V v) {
    Map<K, V> map = new LinkedHashMap<K, V>(1, 1);
    map.put(k, v);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2) {
    Map<K, V> map = new LinkedHashMap<K, V>(2, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2, K k3, V v3) {
    Map<K, V> map = new LinkedHashMap<K, V>(3, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    Map<K, V> map = new LinkedHashMap<K, V>(4, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
    Map<K, V> map = new LinkedHashMap<K, V>(5, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
    Map<K, V> map = new LinkedHashMap<K, V>(6, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
    Map<K, V> map = new LinkedHashMap<K, V>(7, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8) {
    Map<K, V> map = new LinkedHashMap<K, V>(8, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9) {
    Map<K, V> map = new LinkedHashMap<K, V>(9, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10) {
    Map<K, V> map = new LinkedHashMap<K, V>(10, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11) {
    Map<K, V> map = new LinkedHashMap<K, V>(11, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12) {
    Map<K, V> map = new LinkedHashMap<K, V>(11, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12,
      K k13,
      V v13) {
    Map<K, V> map = new LinkedHashMap<K, V>(13, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12,
      K k13,
      V v13,
      K k14,
      V v14) {
    Map<K, V> map = new LinkedHashMap<K, V>(14, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12,
      K k13,
      V v13,
      K k14,
      V v14,
      K k15,
      V v15) {
    Map<K, V> map = new LinkedHashMap<K, V>(15, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    map.put(k15, v15);
    return map;
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12,
      K k13,
      V v13,
      K k14,
      V v14,
      K k15,
      V v15,
      K k16,
      V v16) {
    Map<K, V> map = new LinkedHashMap<K, V>(16, 1);
    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);
    map.put(k4, v4);
    map.put(k5, v5);
    map.put(k6, v6);
    map.put(k7, v7);
    map.put(k8, v8);
    map.put(k9, v9);
    map.put(k10, v10);
    map.put(k11, v11);
    map.put(k12, v12);
    map.put(k13, v13);
    map.put(k14, v14);
    map.put(k15, v15);
    map.put(k16, v16);
    return map;
  }

  /**
   * 构造Map
   *
   * @param k 键
   * @param v 值
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(K k, V v) {
    return Collections.unmodifiableMap(ofMap(k, v));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(K k1, V v1, K k2, V v2) {
    return Collections.unmodifiableMap(ofMap(k1, v1, k2, v2));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(K k1, V v1, K k2, V v2, K k3, V v3) {
    return Collections.unmodifiableMap(ofMap(k1, v1, k2, v2, k3, v3));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
    return Collections.unmodifiableMap(ofMap(k1, v1, k2, v2, k3, v3, k4, v4));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
    return Collections.unmodifiableMap(ofMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
    return Collections.unmodifiableMap(ofMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
    return Collections.unmodifiableMap(
        ofMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8) {
    Map<K, V> map = new HashMap<K, V>(8, 1);
    return Collections.unmodifiableMap(
        ofMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9) {
    return Collections.unmodifiableMap(
        ofMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10) {
    return Collections.unmodifiableMap(
        ofMap(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11) {
    return Collections.unmodifiableMap(
        ofMap(
            k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11,
            v11));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12) {
    return Collections.unmodifiableMap(
        ofMap(
            k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11,
            v11, k12, v12));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12,
      K k13,
      V v13) {
    return Collections.unmodifiableMap(
        ofMap(
            k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11,
            v11, k13, v13));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12,
      K k13,
      V v13,
      K k14,
      V v14) {
    return Collections.unmodifiableMap(
        ofMap(
            k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11,
            v11, k13, v13, k14, v14));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12,
      K k13,
      V v13,
      K k14,
      V v14,
      K k15,
      V v15) {
    return Collections.unmodifiableMap(
        ofMap(
            k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11,
            v11, k13, v13, k14, v14, k15, v15));
  }

  /**
   * 构造Map
   *
   * @param <K> 键类型
   * @param <V> 值类型
   * @return 返回Map
   */
  public static <K, V> Map<K, V> ofUnmodifiableMap(
      K k1,
      V v1,
      K k2,
      V v2,
      K k3,
      V v3,
      K k4,
      V v4,
      K k5,
      V v5,
      K k6,
      V v6,
      K k7,
      V v7,
      K k8,
      V v8,
      K k9,
      V v9,
      K k10,
      V v10,
      K k11,
      V v11,
      K k12,
      V v12,
      K k13,
      V v13,
      K k14,
      V v14,
      K k15,
      V v15,
      K k16,
      V v16) {
    return Collections.unmodifiableMap(
        ofMap(
            k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8, k9, v9, k10, v10, k11,
            v11, k13, v13, k14, v14, k15, v15, k16, v16));
  }

  public static <E extends Enum<E>> Map<String, E> ofMapFromEnum(
      @Nonnull Class<E> enumClass, @Nonnull Map<String, E> container) {
    return ofMapFromEnum(enumClass, container, null);
  }

  /**
   * 从枚举类构造{@code ReadOnlyCaseInsensitiveMap}
   *
   * @param enumClass 枚举类
   * @param keysSupplier 键值提供器
   * @param <E> 枚举类型
   * @return {@code ReadOnlyCaseInsensitiveMap}
   */
  public static <E extends Enum<E>> Map<String, E> ofMapFromEnum(
      @Nonnull Class<E> enumClass,
      @Nonnull Map<String, E> container,
      @Nullable FactoryThrow<E, String[], RuntimeException> keysSupplier) {
    if (enumClass.isEnum()) {
      E[] enumConstants = enumClass.getEnumConstants();
      for (E enumConstant : enumConstants) {
        container.put(enumConstant.name(), enumConstant);
      }
      if (keysSupplier != null) {
        for (E enumConstant : enumConstants) {
          String[] keys = keysSupplier.handle(enumConstant);
          if (Whether.noEmpty(keys)) {
            for (String key : keys) {
              container.put(key, enumConstant);
            }
          }
        }
      }
    }
    return container;
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * <p>默认： 跳过空值 跳过异常
   *
   * @param input 输入{@code Map<K,V>}
   * @param factoryThrow 值处理器
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   */
  public static <K, V, NV, E extends Throwable> Map<K, NV> processMapValue(
      Map<K, V> input, FactoryThrow<V, NV, E> factoryThrow) {
    return processMapValue(input, factoryThrow, true);
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * <p>默认： 跳过异常
   *
   * @param input 输入{@code Map<K,V>}
   * @param factoryThrow 值处理器
   * @param isSkipNull 是否跳过空值，如果不跳过空值则值可能为null
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   */
  public static <K, V, NV, E extends Throwable> Map<K, NV> processMapValue(
      Map<K, V> input, FactoryThrow<V, NV, E> factoryThrow, boolean isSkipNull) {
    Map<K, NV> container = new HashMap<K, NV>(input.size(), 1);
    processMapValue(input, container, factoryThrow, isSkipNull);
    return container;
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * <p>默认： 跳过空值 跳过异常
   *
   * @param input 输入{@code Map<K,V>}
   * @param container 容器{@code Map<K,NV>}
   * @param factoryThrow 值处理器
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   */
  public static <K, V, NV, E extends Throwable> void processMapValue(
      Map<K, V> input, Map<K, NV> container, FactoryThrow<V, NV, E> factoryThrow) {
    processMapValue(input, container, factoryThrow, true);
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * <p>默认： 跳过异常
   *
   * @param input 输入{@code Map<K,V>}
   * @param container 容器{@code Map<K,NV>}
   * @param factoryThrow 值处理器
   * @param isSkipNull 是否跳过空值，如果不跳过空值则值可能为null
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   */
  public static <K, V, NV, E extends Throwable> void processMapValue(
      Map<K, V> input,
      Map<K, NV> container,
      FactoryThrow<V, NV, E> factoryThrow,
      boolean isSkipNull) {
    try {
      doProcessMap(input, container, factoryThrow, isSkipNull, true);
    } catch (Throwable ignored) {
      // haven't exception
    }
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * <p>默认： 跳过空值 不跳过异常
   *
   * @param input 输入{@code Map<K,V>}
   * @param factoryThrow 值处理器
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   * @throws E 如果失败抛出异常
   */
  public static <K, V, NV, E extends Throwable> Map<K, NV> processMapValueThrow(
      Map<K, V> input, FactoryThrow<V, NV, E> factoryThrow) throws E {
    return processMapValueThrow(input, factoryThrow, true);
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * <p>默认： 不跳过异常
   *
   * @param input 输入{@code Map<K,V>}
   * @param factoryThrow 值处理器
   * @param isSkipNull 是否跳过空值，如果不跳过空值则值可能为null
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   * @throws E 如果失败抛出异常
   */
  public static <K, V, NV, E extends Throwable> Map<K, NV> processMapValueThrow(
      Map<K, V> input, FactoryThrow<V, NV, E> factoryThrow, boolean isSkipNull) throws E {
    return processMapValueThrow(input, factoryThrow, isSkipNull, false);
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * @param input 输入{@code Map<K,V>}
   * @param factoryThrow 值处理器
   * @param isSkipNull 是否跳过空值，如果不跳过空值则值可能为null
   * @param isSkipException 是否跳过异常，如果不跳过异常则发生异常的时候会终止循环
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   * @throws E 如果失败抛出异常
   */
  public static <K, V, NV, E extends Throwable> Map<K, NV> processMapValueThrow(
      Map<K, V> input,
      FactoryThrow<V, NV, E> factoryThrow,
      boolean isSkipNull,
      boolean isSkipException)
      throws E {
    Map<K, NV> container = new HashMap<K, NV>(input.size(), 1);
    doProcessMap(input, container, factoryThrow, isSkipNull, isSkipException);
    return container;
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * <p>默认： 跳过空值 不跳过异常
   *
   * @param input 输入{@code Map<K,V>}
   * @param container 容器{@code Map<K,NV>}
   * @param factoryThrow 值处理器
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   * @throws E 如果失败抛出异常
   */
  public static <K, V, NV, E extends Throwable> void processMapValueThrow(
      Map<K, V> input, Map<K, NV> container, FactoryThrow<V, NV, E> factoryThrow) throws E {
    doProcessMap(input, container, factoryThrow, true, false);
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * <p>默认： 不跳过异常
   *
   * @param input 输入{@code Map<K,V>}
   * @param container 容器{@code Map<K,NV>}
   * @param factoryThrow 值处理器
   * @param isSkipNull 是否跳过空值，如果不跳过空值则值可能为null
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   * @throws E 如果失败抛出异常
   */
  public static <K, V, NV, E extends Throwable> void processMapValueThrow(
      Map<K, V> input,
      Map<K, NV> container,
      FactoryThrow<V, NV, E> factoryThrow,
      boolean isSkipNull)
      throws E {
    doProcessMap(input, container, factoryThrow, isSkipNull, false);
  }

  /**
   * 处理Map的值映射，例如{@code Map<K,V>--->Map<K,NV>}
   *
   * @param input 输入{@code Map<K,V>}
   * @param container 容器{@code Map<K,NV>}
   * @param factoryThrow 值处理器
   * @param isSkipNull 是否跳过空值，如果不跳过空值则值可能为null
   * @param isSkipException 是否跳过异常，如果不跳过异常则发生异常的时候会终止循环
   * @param <K> 键类型
   * @param <V> 值类型
   * @param <NV> 新值类型
   * @param <E> 异常类型
   * @throws E 如果失败抛出异常
   */
  private static <K, V, NV, E extends Throwable> void doProcessMap(
      Map<K, V> input,
      Map<K, NV> container,
      FactoryThrow<V, NV, E> factoryThrow,
      boolean isSkipNull,
      boolean isSkipException)
      throws E {
    for (Map.Entry<K, V> entry : input.entrySet()) {
      try {
        NV newValue = factoryThrow.handle(entry.getValue());
        if (newValue != null || !isSkipNull) {
          container.put(entry.getKey(), newValue);
        }
      } catch (Throwable e) {
        if (!isSkipException) {
          //noinspection unchecked
          throw (E) e;
        }
      }
    }
  }

  /**
   * 将Map中的值转换成数组
   *
   * @param map {@code Map<K,Object>}
   * @param keys 键
   * @param <K> 键类型
   * @return 返回数组
   */
  @SafeVarargs
  public static <K> Object[] toValueArray(Map<K, Object> map, K... keys) {
    Object[] result = new Object[keys.length];
    for (int i = 0; i < keys.length; i++) {
      result[i] = map.get(keys[i]);
    }
    return result;
  }

  /**
   * 将Map转换成
   *
   * @param input {@code Map<?, T>}
   * @return {@code Map<String, Object>}
   */
  public static <T> Map<String, T> toStringObjectMap(Map<?, T> input) {
    Map<String, T> map = new HashMap<String, T>(input.size(), 1);
    for (Map.Entry<?, T> entry : input.entrySet()) {
      Object key = entry.getKey();
      if (key instanceof String) {
        map.put(key.toString(), entry.getValue());
      }
    }
    return map;
  }

  /**
   * 反置容器Map
   *
   * @param input 输入Map
   * @param container 容器Map
   * @param <K1> 键类型
   * @param <K2> 键类型
   */
  public static <K1, K2> void reserveMap(
      Map<? extends K1, ? extends K2> input, Map<? super K2, ? super K1> container) {
    for (K1 k1 : input.keySet()) {
      container.put(input.get(k1), k1);
    }
  }

  /**
   * 将列表的Map按照指定键进行分组
   *
   * @param mapList ma列表
   * @param keys 多个键值
   * @param <K> 键类型
   * @param <V> 值类型
   * @return {@code List<Map<K, V>>>}
   */
  public static <K, V> Map<Map<K, V>, List<Map<K, V>>> groupBy(
      List<? extends Map<K, V>> mapList, K... keys) {
    Map<Map<K, V>, List<Map<K, V>>> result = new LinkedHashMap<>();
    for (Map<K, V> entry : mapList) {
      Map<K, V> mapKey = new LinkedHashMap<>();
      for (K key : keys) {
        mapKey.put(key, entry.get(key));
      }
      result.computeIfAbsent(mapKey, k -> new LinkedList<>()).add(entry);
    }
    return result;
  }

  public static <T> Set<T> toSet(T[] input) {
    return toSet(input, t -> t);
  }

  public static <T> Set<T> toSet(Collection<T> input) {
    return toSet(input, t -> t);
  }

  public static <T, R> Set<R> toSet(T[] input, Function<? super T, ? extends R> converter) {
    return input == null || input.length == 0
        ? new LinkedHashSet<>()
        : Arrays.stream(input)
            .map(converter)
            .filter(Whether::nonvl)
            .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public static <T, R> Set<R> toSet(
      Collection<T> input, Function<? super T, ? extends R> converter) {
    return input == null
        ? new LinkedHashSet<>()
        : input.stream()
            .map(converter)
            .filter(Whether::nonvl)
            .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public static <T> List<T> unmodifiableList(List<? extends T> list, boolean isPossibleNull) {
    return list == null
        ? (isPossibleNull ? null : Collections.emptyList())
        : Collections.unmodifiableList(list);
  }

  public static <T> Set<T> unmodifiableSet(Set<? extends T> set, boolean isPossibleNull) {
    return set == null
        ? (isPossibleNull ? null : Collections.emptySet())
        : Collections.unmodifiableSet(set);
  }

  public static <K, V> Map<K, V> unmodifiableMap(
      Map<? extends K, ? extends V> map, boolean isPossibleNull) {
    return map == null
        ? (isPossibleNull ? null : Collections.emptyMap())
        : Collections.unmodifiableMap(map);
  }

  public static <K, V> Map<K, Integer> sortMap(
      @Nonnull Map<K, V> sortedMap,
      @Nonnull Comparator<K> comparator,
      @Nonnull Map<K, Integer> sortResult) {
    List<K> list = sortMap(sortedMap, comparator);
    IntStream.range(0, list.size()).forEach(i -> sortResult.put(list.get(i), i));
    return sortResult;
  }

  public static <K, V> List<K> sortMap(
      @Nonnull Map<K, V> sortedMap, @Nonnull Comparator<K> comparator) {
    List<K> list = new ArrayList<>(sortedMap.size());
    sortedMap.keySet().stream().sorted(comparator).forEach(list::add);
    return list;
  }

  public static <K, V> Map<K, V> filterMap(
      @Nonnull Map<K, V> filteredMap,
      @Nonnull BiPredicate<K, V> condition,
      @Nonnull Map<K, V> filterResult) {
    filteredMap.forEach(
        (k, v) -> {
          if (condition.test(k, v)) {
            filterResult.put(k, v);
          }
        });
    return filterResult;
  }
}

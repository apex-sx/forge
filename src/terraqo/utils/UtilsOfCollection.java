/*******************************************************************************
 * Copyright (c) 2020.
 * This is Utils in Java Project.
 * Follow Apache License 2.0
 * Author: 付祥 alisa by Alfred X F
 * Github: https://github.com/alfredxf
 ******************************************************************************/
package com.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 * 集合工具类
 *
 * @author : Alfred X Fu
 * @date : 2020/4/16 17:00
 */
@SuppressWarnings("unchecked")
public final class UtilsOfCollection {

  /**
   * 默认初始容量
   */
  private static final int INITIAL_CAPACITY = 8;
  /**
   * 键值数量
   */
  private static final int KEY_VALUE_COUNT = 2;

  public static <E> ArrayList<E> arrayList() {
    return new ArrayList<>(INITIAL_CAPACITY);
  }

  public static <E> ArrayList<E> arrayList(Consumer<ArrayList<E>> consumer) {
    ArrayList<E> collection = new ArrayList<>(INITIAL_CAPACITY);
    consumer.accept(collection);
    return collection;
  }

  public static <E> LinkedList<E> linkedList() {
    return new LinkedList<>();
  }

  public static <E> LinkedList<E> linkedList(Consumer<LinkedList<E>> consumer) {
    LinkedList<E> collection = new LinkedList<>();
    consumer.accept(collection);
    return collection;
  }

  public static <E> HashSet<E> hashSet() {
    return new HashSet<>(INITIAL_CAPACITY);
  }

  public static <E> HashSet<E> hashSet(Consumer<HashSet<E>> consumer) {
    HashSet<E> collection = new HashSet<>(INITIAL_CAPACITY);
    consumer.accept(collection);
    return collection;
  }

  public static <E extends Comparable<?>> TreeSet<E> treeSet() {
    return new TreeSet<>();
  }

  public static <E extends Comparable<?>> TreeSet<E> treeSet(Consumer<TreeSet<E>> consumer) {
    TreeSet<E> collection = new TreeSet<>();
    consumer.accept(collection);
    return collection;
  }

  public static <K, V> HashMap<K, V> hashMap() {
    return new HashMap<>(INITIAL_CAPACITY);
  }

  public static <K, V> HashMap<K, V> hashMap(Consumer<HashMap<K, V>> consumer) {
    HashMap<K, V> collection = new HashMap<>(INITIAL_CAPACITY);
    consumer.accept(collection);
    return collection;
  }

  public static <K, V> LinkedHashMap<K, V> linkedHashMap() {
    return new LinkedHashMap<>();
  }

  public static <K, V> LinkedHashMap<K, V> linkedHashMap(Consumer<HashMap<K, V>> consumer) {
    LinkedHashMap<K, V> collection = new LinkedHashMap<>(INITIAL_CAPACITY);
    consumer.accept(collection);
    return collection;
  }

  @SafeVarargs
  public static <E> Map<E, E> toMap(final E... elements) {
    if (UtilsOfJudge.isEmpty(elements)) {
      return Collections.emptyMap();
    }
    UtilsOfAssert.checkIsTrue(elements.length % KEY_VALUE_COUNT == 0, "Element don't match");
    if (elements.length == 2) {
      return Collections.singletonMap(elements[0], elements[1]);
    }
    Map<E, E> map = new HashMap<>(elements.length / KEY_VALUE_COUNT);
    for (int i = 0; i < elements.length; i += KEY_VALUE_COUNT) {
      map.put(elements[0], elements[1]);
    }
    return map;
  }

  public static <E> Map<E, E> reserveMap(final Map<E, E> map) {
    return reserveMap(map, new HashMap<>(map.size()));
  }

  public static <E> Map<E, E> reserveMap(final Map<E, E> map, Map<E, E> newMap) {
    if (UtilsOfJudge.isEmpty(map)) {
      return newMap;
    }
    map.forEach((k, v) -> newMap.put(v, k));
    return newMap;
  }

  @SafeVarargs
  public static <E> List<E> toList(final E... elements) {
    if (UtilsOfJudge.isEmpty(elements)) {
      return Collections.emptyList();
    }
    if (elements.length == 1) {
      return Collections.singletonList(elements[0]);
    } else {
      return List.of(elements);
    }

  }

  public static <E> List<E> toList(final Enumeration<E> enumeration) {
    final List<E> set = arrayList();
    enumeration.asIterator().forEachRemaining(set::add);
    return set;
  }

  public static Set<Character> toSet(final char... elements) {
    if (UtilsOfJudge.isEmpty(elements)) {
      return Collections.emptySet();
    }
    Set<Character> set = new HashSet<>(elements.length);
    for (char element : elements) {
      set.add(element);
    }
    return set;
  }

  @SafeVarargs
  public static <E> Set<E> toSet(final E... elements) {
    if (UtilsOfJudge.isEmpty(elements)) {
      return Collections.emptySet();
    }
    if (elements.length == 1) {
      return Collections.singleton(elements[0]);
    } else {
      return Set.of(elements);
    }

  }

  public static <E> Set<E> toSet(final Enumeration<E> enumeration) {
    final Set<E> set = hashSet();
    enumeration.asIterator().forEachRemaining(set::add);
    return set;
  }

  public static <K, V> V getValueOrDefault(Map<K, V> map, K key, V defaultValue) {
    if (map.containsKey(key)) {
      return map.get(key);
    }
    return defaultValue;
  }

  public static <K, R> R getUncertaintyValue(Map<K, ?> map, K key) {
    if (map.containsKey(key)) {
      return (R) map.get(key);
    }
    return null;
  }

  public static <K, R> R getUncertaintyValueOrDefault(Map<K, ?> map, K key, R defaultValue) {
    if (map.containsKey(key)) {
      return (R) map.get(key);
    }
    return defaultValue;
  }
}

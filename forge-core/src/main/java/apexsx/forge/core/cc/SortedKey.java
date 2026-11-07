/*
 * Copyright (c) 2026.
 */

package apexsx.forge.core.cc;

import java.util.Objects;

/**
 * 用于排序的键值对记录类，包含一个键和一个顺序值
 *
 * @param <K> 键的类型
 * @author Terraqo
 */
public record SortedKey<K>(K key, int order) implements Comparable<SortedKey<K>> {

    /**
     * 构造函数，创建一个SortedKey实例，顺序值默认为Integer.MIN_VALUE
     *
     * @param key 键值
     */
    public SortedKey(K key) {
        this(key, Integer.MIN_VALUE);
    }

    /**
     * 比较两个SortedKey对象的顺序值
     *
     * @param o 另一个SortedKey对象
     * @return 按顺序值比较的结果
     */
    @Override
    public int compareTo(SortedKey<K> o) {
        return Integer.compare(order, o.order);
    }

    /**
     * 比较两个SortedKey对象是否相等，只比较键值部分
     *
     * @param o 另一个对象
     * @return 如果键值相等则返回true，否则返回false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SortedKey<?> sortedKey)) return false;
        return Objects.equals(key, sortedKey.key);
    }

    /**
     * 计算SortedKey对象的哈希值，只基于键值计算
     *
     * @return 键值的哈希值
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

    /**
     * 返回SortedKey对象的字符串表示，即键的字符串表示
     *
     * @return 键的字符串表示
     */
    @Override
    public String toString() {
        return key.toString();
    }
}

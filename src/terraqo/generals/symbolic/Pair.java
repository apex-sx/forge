/*
 * Copyright (c) 2025.
 */

package com.github.potatoxf.generals.symbolic;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * 键值对
 *
 * @author potatoxf
 */
public class Pair<K, V> implements Map.Entry<K, V>, SelectedOption<K, V>, Serializable {
    private static final long serialVersionUID = 1L;
    private final K key;
    protected volatile V value;

    public Pair(K key) {
        this(key, null);
    }

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair<?, ?>)) return false;
        return Objects.equals(getKey(), ((Pair<?, ?>) o).getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public String toString() {
        return getKey() + " = " + getValue();
    }
}

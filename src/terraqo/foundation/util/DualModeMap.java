package io.github.terraqo.foundation.util;

import java.util.*;

/**
 * A Map implementation that supports two modes:
 * 1. Delegate Mode: Wraps an existing Map instance.
 * 2. Minimal Mode: Uses parallel lists for keys and values with a custom hash index for faster lookups.
 *
 * @author Terraqo
 */
public class DualModeMap<K, V> extends DelegateMap<K, V> {
    protected final boolean minimal;
    protected final List<K> keys;
    protected final List<V> values;
    private transient byte[] table;

    public DualModeMap(Map<K, V> delegate, boolean unmodifiable) {
        super(delegate, unmodifiable, false);
        this.keys = null;
        this.values = null;
        this.minimal = false;
        this.table = null;
    }

    public DualModeMap(List<K> keys, List<V> values, boolean unmodifiable) {
        super(Collections.emptyMap(), unmodifiable, false);
        if (keys == null || values == null) {
            throw new IllegalArgumentException("Names and values lists cannot be null");
        }
        if (keys.size() != values.size()) {
            throw new IllegalArgumentException("Names and values lists must have the same size");
        }
        if (unmodifiable) {
            keys = Collections.unmodifiableList(keys);
            values = Collections.unmodifiableList(values);
        }
        this.keys = keys;
        this.values = values;
        this.minimal = true;
        this.table = new byte[32];
        for (int i = 0; i < keys.size(); i++) {
            this.addHashIndex(keys.get(i), i);
        }
    }


    @Override
    public int size() {
        if (!minimal) {
            return super.size();
        } else {
            return keys.size();
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        if (!minimal) {
            return super.containsKey(key);
        } else {
            return this.findIndex(key) >= 0;
        }
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            return false;
        }
        if (!minimal) {
            return super.containsValue(value);
        }
        for (V v : values) {
            if (v.equals(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        if (key == null) {
            return null;
        }
        if (!minimal) {
            return super.get(key);
        } else {
            int index = this.findIndex(key);
            return index != -1 ? values.get(index) : null;
        }
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("Key and value must not be null");
        }

        V oldValue = null;
        if (!minimal) {
            oldValue = super.put(key, value);
        } else {
            int index = this.findIndex(key);
            if (index != -1) {
                oldValue = values.set(index, value);
            } else {
                index = keys.size();
                keys.add(key);
                values.add(value);
                this.addHashIndex(key, index);
            }
        }
        return oldValue;
    }

    @Override
    public V remove(Object key) {
        if (key == null) {
            return null;
        }
        if (!minimal) {
            return super.remove(key);
        } else {
            int index = findIndex(key);
            if (index != -1) {
                V oldValue = values.get(index);
                delHashIndex(index);
                keys.remove(index);
                values.remove(index);
                return oldValue;
            }
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        if (!minimal) {
            super.clear();
        } else {
            table = new byte[32];
            keys.clear();
            values.clear();
        }
    }

    @Override
    public Set<K> keySet() {
        if (!minimal) {
            return super.keySet();
        } else {
            // Return an unmodifiable view or a copy to prevent external modification breaking internal state
            return Collections.unmodifiableSet(new LinkedHashSet<>(keys));
        }
    }

    @Override
    public Collection<V> values() {
        if (!minimal) {
            return super.values();
        } else {
            return Collections.unmodifiableList(values);
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        if (!minimal) {
            return super.entrySet();
        } else {
            // Efficiently create entry set from lists
            Set<Entry<K, V>> entries = new LinkedHashSet<>();
            for (int i = 0; i < keys.size(); i++) {
                entries.add(Map.entry(keys.get(i), values.get(i)));
            }
            return Collections.unmodifiableSet(entries);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (!minimal) {
            return super.equals(object);
        } else {
            if (this == object) {
                return true;
            }
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            DualModeMap<?, ?> that = (DualModeMap<?, ?>) object;
            int size = keys.size();
            if (size != that.keys.size()) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                Object thisKey = keys.get(i);
                Object thatKey = that.keys.get(i);
                if (!Objects.equals(thisKey, thatKey)) {
                    return false;
                }
                Object thisVal = values.get(i);
                Object thatVal = that.values.get(i);
                if (!Objects.equals(thisVal, thatVal)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        if (!minimal) {
            return super.hashCode();
        } else {
            // Standard Map hashCode calculation: sum of (key.hashCode() ^ value.hashCode())
            int h = 0;
            for (int i = 0; i < keys.size(); i++) {
                Object key = keys.get(i), value = values.get(i);
                h += (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
            }
            return h;
        }
    }

    @Override
    public String toString() {
        if (!minimal) {
            return super.toString();
        } else {
            StringBuilder sb = new StringBuilder("{");
            int size = keys.size();
            if (size > 0) {
                int i = 0;
                Object key = keys.get(i), value = values.get(i);
                sb.append(key == this ? "(this Map)" : key);
                sb.append("=");
                sb.append(value == this ? "(this Map)" : value);
                for (i++; i < size; i++) {
                    sb.append(", ");
                    key = keys.get(i);
                    value = values.get(i);
                    sb.append(key == this ? "(this Map)" : key);
                    sb.append("=");
                    sb.append(value == this ? "(this Map)" : value);
                }
            }
            sb.append("}");
            return sb.toString();
        }
    }

    private int findIndex(Object key) {
        int index = getHashIndex(key);
        if (index != -1) {
            // Verify due to potential hash collisions in the simple hash table
            if (key.equals(keys.get(index))) {
                return index;
            }
        }
        // Fallback: linear search if hash probe failed or collided
        // Note: lastIndexOf is O(N). For better performance in high-collision scenarios, 
        // a more robust hash structure or chaining would be needed. 
        // Given the byte[] constraint, we accept this fallback.
        for (int i = 0; i < keys.size(); i++) {
            if (key.equals(keys.get(i))) {
                return i;
            }
        }
        return -1;
    }

    private void addHashIndex(Object key, int index) {
        if (index < 0 || index > 254) {
            // Cannot store this index in a byte-based table safely with our encoding
            // Fallback will handle retrieval via linear scan if needed, but we lose fast path
            return;
        }
        int slot = hashSlotFor(key);
        // Simple overwrite strategy. Collisions are handled by foundIndex fallback.
        table[slot] = (byte) (index + 1);
    }

    private void delHashIndex(int index) {
        if (index < 0 || index > 254) {
            return;
        }
        byte target = (byte) (index + 1);
        for (int i = 0; i < table.length; i++) {
            if (table[i] == target) {
                table[i] = 0;
            } else if (table[i] > target) {
                // Decrement indices that were shifted?
                // No, this logic assumes the hash table stores absolute indices.
                // When an item is removed from the list, all subsequent items shift down.
                // So any stored index > removed index must be decremented.
                table[i]--;
            }
        }
    }

    private int getHashIndex(Object key) {
        int slot = hashSlotFor(key);
        return (table[slot] & 0xff) - 1;
    }

    private int hashSlotFor(Object element) {
        return element.hashCode() & (table.length - 1);
    }
}
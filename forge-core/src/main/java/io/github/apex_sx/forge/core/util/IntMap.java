/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author ApexSX
 */
package io.github.apex_sx.forge.core.util;

import java.util.Arrays;
import java.util.PrimitiveIterator;

/**
 * 零依赖、无装箱的 int → int 映射，采用开放寻址与线性探测。
 * <p>
 * 用调用方指定的 {@code emptyKey} 标记空槽，该键对应的值单独存储，因此全部 int 键均可使用。
 * 查找、插入与删除均摊 O(1)，删除时回填后继元素以保持探测链完整。
 * 遍历顺序不保证稳定，遍历期间的结构修改会触发快速失败。
 *
 * @author ApexSX
 */
public final class IntMap extends BaseContainer {
    /**
     * 调用方指定的空槽标记键。
     */
    private final int emptyKey;
    /**
     * 空槽标记键对应的值。
     */
    private int emptyKeyValue;
    /**
     * 键哈希表，值为 {@code emptyKey} 的位置表示空槽。
     */
    private int[] keys;
    /**
     * 与 {@link #keys} 下标一一对应的值数组。
     */
    private int[] values;

    /**
     * 创建默认容量为 16 的空映射。
     *
     * @param emptyKey 空槽标记键，该键仍可存入映射
     */
    public IntMap(int emptyKey) {
        this(16, emptyKey);
    }

    /**
     * 创建指定初始容量的空映射。
     *
     * @param initialCapacity 初始容量，实际容量会向上取整为 2 的幂
     * @param emptyKey        空槽标记键，该键仍可存入映射
     */
    public IntMap(int initialCapacity, int emptyKey) {
        super(initialCapacity, 3, 4);
        this.emptyKey = emptyKey;
        this.keys = newIntArray();
        this.values = newIntArray();
        if (emptyKey != 0) {
            Arrays.fill(this.keys, emptyKey);
        }
    }

    /**
     * 将指定键映射到指定值；键已存在时覆盖原值。
     *
     * @param key   键
     * @param value 值
     */
    public void put(int key, int value) {
        if (key == emptyKey) {
            if (!isEmptyElementPresent()) {
                setEmptyElementPresent(true);
                increaseSize();
                markModification();
            }
            emptyKeyValue = value;
            return;
        }
        int idx = slotOf(key);
        while (keys[idx] != emptyKey) {
            if (keys[idx] == key) {
                values[idx] = value;
                return;
            }
            idx = nextIndex(idx);
        }
        keys[idx] = key;
        values[idx] = value;
        increaseSize();
        markModification();
        ensureCapacity();
    }

    /**
     * 返回键对应的值，键不存在时返回 {@code 0}。
     *
     * @param key 键
     * @return 键对应的值，不存在时返回 {@code 0}
     */
    public int get(int key) {
        if (key == emptyKey) {
            return isEmptyElementPresent() ? emptyKeyValue : 0;
        }
        int idx = slotOf(key);
        while (keys[idx] != emptyKey) {
            if (keys[idx] == key) {
                return values[idx];
            }
            idx = nextIndex(idx);
        }
        return 0;
    }

    /**
     * 返回键对应的值，键不存在时返回指定默认值。
     * 当映射中可能真实存储 {@code 0} 值时，可用本方法区分“键不存在”与“值为 0”。
     *
     * @param key          键
     * @param defaultValue 键不存在时返回的默认值
     * @return 键对应的值或 {@code defaultValue}
     */
    public int getOrDefault(int key, int defaultValue) {
        if (key == emptyKey) {
            return isEmptyElementPresent() ? emptyKeyValue : defaultValue;
        }
        int idx = slotOf(key);
        while (keys[idx] != emptyKey) {
            if (keys[idx] == key) {
                return values[idx];
            }
            idx = nextIndex(idx);
        }
        return defaultValue;
    }

    /**
     * 判断映射是否包含指定键。
     *
     * @param key 键
     * @return 包含时返回 {@code true}
     */
    public boolean containsKey(int key) {
        if (key == emptyKey) {
            return isEmptyElementPresent();
        }
        int idx = slotOf(key);
        while (keys[idx] != emptyKey) {
            if (keys[idx] == key) {
                return true;
            }
            idx = nextIndex(idx);
        }
        return false;
    }

    /**
     * 删除指定键及其映射，并通过回填后继元素保持探测链可用。
     *
     * @param key 键
     * @return 原映射值，键不存在时返回 {@code 0}
     */
    public int remove(int key) {
        if (key == emptyKey) {
            if (!isEmptyElementPresent()) {
                return 0;
            }
            setEmptyElementPresent(false);
            decreaseSize();
            markModification();
            int value = emptyKeyValue;
            emptyKeyValue = 0;
            return value;
        }
        int idx = slotOf(key);
        while (keys[idx] != emptyKey) {
            if (keys[idx] == key) {
                int value = values[idx];
                int current = nextIndex(idx);
                while (keys[current] != emptyKey) {
                    int home = slotOf(keys[current]);
                    // 仅当后继元素离自己的原始槽位更远时，把它回填到当前空槽才不会破坏查找链。
                    if (maskIndex(current - home) > maskIndex(idx - home)) {
                        keys[idx] = keys[current];
                        values[idx] = values[current];
                        idx = current;
                    }
                    current = nextIndex(current);
                }
                keys[idx] = emptyKey;
                values[idx] = 0;
                decreaseSize();
                markModification();
                return value;
            }
            idx = nextIndex(idx);
        }
        return 0;
    }

    /**
     * 将全部键导出为数组，顺序不保证稳定。
     *
     * @return 包含所有键的新数组
     */
    public int[] toKeyArray() {
        int expected = modificationMark();
        int[] result = new int[size()];
        int count = 0;
        if (isEmptyElementPresent()) {
            result[count++] = emptyKey;
        }
        for (int key : keys) {
            if (key != emptyKey) {
                result[count++] = key;
            }
        }
        verifyModification(expected);
        return result;
    }

    /**
     * 将全部值导出为数组，顺序不保证稳定。
     *
     * @return 包含所有值的新数组
     */
    public int[] toValueArray() {
        int expected = modificationMark();
        int[] result = new int[size()];
        int count = 0;
        if (isEmptyElementPresent()) {
            result[count++] = emptyKeyValue;
        }
        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != emptyKey) {
                result[count++] = values[i];
            }
        }
        verifyModification(expected);
        return result;
    }

    /**
     * 返回按内部顺序遍历所有键的迭代器（无装箱）。
     * 迭代器不支持 {@code remove}，结构修改会导致后续访问快速失败。
     *
     * @return int 键迭代器
     */
    public PrimitiveIterator.OfInt keyIterator() {
        return new OfInt(this, keys, emptyKey, true);
    }

    /**
     * 返回按内部顺序遍历所有值的迭代器（无装箱），顺序与 {@link #keyIterator()} 对应。
     * 迭代器不支持 {@code remove}，结构修改会导致后续访问快速失败。
     *
     * @return int 值迭代器
     */
    public PrimitiveIterator.OfInt valueIterator() {
        return new OfInt(this, keys, emptyKey, true) {
            @Override
            protected int valueAt(int index) {
                return values[index];
            }

            @Override
            protected int emptyValue() {
                return emptyKeyValue;
            }
        };
    }

    /**
     * 在哈希表扩容后重建内部存储结构。
     * 创建与新容量匹配的键、值数组，将空键填充为空槽位，再把旧数组中所有非空条目重新散列到新数组中。
     *
     * @param newCapacity 扩容后的新容量
     */
    @Override
    protected void onResize(int newCapacity) {
        int[] oldKeys = keys;
        int[] oldValues = values;
        keys = newIntArray();
        values = newIntArray();
        if (emptyKey != 0) {
            Arrays.fill(keys, emptyKey);
        }
        for (int i = 0; i < oldKeys.length; i++) {
            if (oldKeys[i] != emptyKey) {
                place(oldKeys[i], oldValues[i]);
            }
        }
    }

    /**
     * 清空集合，仅重置元素数量而保留容量。
     */
    @Override
    public void clear() {
        if (isNotEmpty()) {
            markModification();
        }
        super.clear();
        setEmptyElementPresent(false);
        emptyKeyValue = 0;
        Arrays.fill(keys, emptyKey);
        Arrays.fill(values, 0);
    }

    /**
     * 把键值对放入当前哈希表，仅供扩容重建使用。
     */
    private void place(int key, int value) {
        int idx = slotOf(key);
        while (keys[idx] != emptyKey) {
            idx = nextIndex(idx);
        }
        keys[idx] = key;
        values[idx] = value;
    }
}

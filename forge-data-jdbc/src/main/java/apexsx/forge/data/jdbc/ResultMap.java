package apexsx.forge.data.jdbc;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 忽略大小写的有序Map实现
 * 保持插入顺序，同时支持大小写不敏感的键查找
 *
 * @author zhitron
 */
public final class ResultMap implements Map<String, Object>, Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    /**
     * 存储实际数据的 LinkedHashMap，保持插入顺序
     */
    private final LinkedHashMap<String, Object> nativeDataStorage = new LinkedHashMap<>();
    /**
     * 用于大小写不敏感键查找的 TreeMap
     * key: 大小写不敏感的键, value: 实际存储在 nativeDataStorage 中的键
     */
    private final TreeMap<String, String> keyCaseInsensitiveFinder = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * 返回此映射中的键-值映射关系数
     *
     * @return 此映射中的键-值映射关系数
     */
    @Override
    public int size() {
        return nativeDataStorage.size();
    }

    /**
     * 如果此映射未包含键-值映射关系，则返回 true
     *
     * @return 如果此映射未包含键-值映射关系，则返回 true
     */
    @Override
    public boolean isEmpty() {
        return nativeDataStorage.isEmpty();
    }

    /**
     * 如果此映射包含指定键的映射关系，则返回 true
     *
     * @param key 指定的键
     * @return 如果此映射包含指定键的映射关系，则返回 true
     */
    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String) {
            return keyCaseInsensitiveFinder.containsKey(key);
        }
        return false;
    }

    /**
     * 如果此映射将一个或多个键映射到指定值，则返回 true
     *
     * @param value 指定值
     * @return 如果此映射将一个或多个键映射到指定值，则返回 true
     */
    @Override
    public boolean containsValue(Object value) {
        return nativeDataStorage.containsValue(value);
    }

    /**
     * 返回指定键所映射的值，如果此映射不包含该键的映射关系，则返回 null
     *
     * @param key 要返回其关联值的键
     * @return 指定键所映射的值，如果此映射不包含该键的映射关系，则返回 null
     */
    @Override
    public Object get(Object key) {
        if (key instanceof String) {
            String actualKey = keyCaseInsensitiveFinder.get(key);
            if (actualKey != null) {
                return nativeDataStorage.get(actualKey);
            }
        }
        return null;
    }

    /**
     * 将指定的值与此映射中的指定键关联（可选操作）
     *
     * @param key   键
     * @param value 值
     * @return 与指定键关联的先前值；如果该键没有映射关系，则返回 null
     */
    @Override
    public Object put(String key, Object value) {
        // 查找已存在的大小写变体键
        String existingKey = keyCaseInsensitiveFinder.get(key);
        Object oldValue = null;

        if (existingKey != null) {
            // 如果存在，则替换值
            oldValue = nativeDataStorage.remove(existingKey);
        }
        // 如果不存在，则添加新键值对
        nativeDataStorage.put(key, value);

        // 更新keyMap，保持大小写不敏感映射
        keyCaseInsensitiveFinder.put(key, key);

        return oldValue;
    }

    /**
     * 如果存在，则从此映射中移除指定键的映射关系（可选操作）
     *
     * @param key 要移除其映射关系的键
     * @return 与指定键关联的先前值；如果该键没有映射关系，则返回 null
     */
    @Override
    public Object remove(Object key) {
        if (key instanceof String) {
            String actualKey = keyCaseInsensitiveFinder.remove(key);
            if (actualKey != null) {
                return nativeDataStorage.remove(actualKey);
            }
        }
        return null;
    }

    /**
     * 从指定映射中将所有映射关系复制到此映射中（可选操作）
     *
     * @param m 包含要存储在此映射中的键值对的映射
     */
    @Override
    public void putAll(Map<? extends String, ?> m) {
        for (Entry<? extends String, ?> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 从此映射中移除所有映射关系（可选操作）
     */
    @Override
    public void clear() {
        nativeDataStorage.clear();
        keyCaseInsensitiveFinder.clear();
    }

    /**
     * 返回此映射中包含的键的 Set 视图
     *
     * @return 此映射中包含的键的 set 视图
     */
    @Override
    public Set<String> keySet() {
        return nativeDataStorage.keySet();
    }

    /**
     * 返回此映射中包含的值的 Collection 视图
     *
     * @return 此映射中包含的值的 collection 视图
     */
    @Override
    public Collection<Object> values() {
        return nativeDataStorage.values();
    }

    /**
     * 返回此映射中包含的映射关系的 Set 视图
     *
     * @return 此映射中包含的映射关系的 set 视图
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return nativeDataStorage.entrySet();
    }

    /**
     * 返回指定键所映射的值，如果此映射不包含该键的映射关系，则返回 defaultValue
     *
     * @param key          要返回其关联值的键
     * @param defaultValue 如果此映射不包含该键的映射关系，则返回的默认值
     * @return 指定键所映射的值，如果此映射不包含该键的映射关系，则返回 defaultValue
     */
    @Override
    public Object getOrDefault(Object key, Object defaultValue) {
        if (key instanceof String) {
            String actualKey = keyCaseInsensitiveFinder.get(key);
            if (actualKey != null) {
                return nativeDataStorage.getOrDefault(actualKey, defaultValue);
            }
        }
        return defaultValue;
    }

    /**
     * 如果指定键尚未与某个值关联（或映射到 null），则将其与给定值关联并返回 null，
     * 否则返回当前值
     *
     * @param key   与指定值关联的键
     * @param value 如果该键未与某个值关联，则与该键关联的值
     * @return 与指定键关联的先前值；如果该键没有映射关系，则返回 null
     */
    @Override
    public Object putIfAbsent(String key, Object value) {
        // 检查是否存在大小写变体的键
        String existingKey = keyCaseInsensitiveFinder.get(key);

        if (existingKey != null) {
            // 键已存在，不执行操作
            return nativeDataStorage.get(existingKey);
        } else {
            // 键不存在，添加新键值对
            nativeDataStorage.put(key, value);
            keyCaseInsensitiveFinder.put(key, key);
            return null;
        }
    }

    /**
     * 仅当指定键精确地映射到指定值时才删除该条目
     *
     * @param key   要移除其映射关系的键
     * @param value 与键关联的预期值
     * @return 如果值正好匹配，则移除该条目并返回 true
     */
    @Override
    public boolean remove(Object key, Object value) {
        if (key instanceof String) {
            String actualKey = keyCaseInsensitiveFinder.get(key);
            if (actualKey != null && Objects.equals(nativeDataStorage.get(actualKey), value)) {
                nativeDataStorage.remove(actualKey);
                keyCaseInsensitiveFinder.remove(key);
                return true;
            }
        }
        return false;
    }

    /**
     * 仅当当前映射到指定值时才替换指定键的条目
     *
     * @param key      与指定值关联的键
     * @param oldValue 与键关联的预期值
     * @param newValue 要存储在指定键下的值
     * @return 如果值被替换，则返回 true
     */
    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        String actualKey = keyCaseInsensitiveFinder.get(key);
        if (actualKey != null && Objects.equals(nativeDataStorage.get(actualKey), oldValue)) {
            nativeDataStorage.put(actualKey, newValue);
            return true;
        }
        return false;
    }

    /**
     * 使用给定函数替换此映射的每个条目的值，直到所有条目都已处理完毕
     *
     * @param function 用于替换每个条目值的函数
     */
    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        nativeDataStorage.replaceAll(function);
        // 重新构建 keyMap 以确保一致性
        keyCaseInsensitiveFinder.clear();
        for (String key : nativeDataStorage.keySet()) {
            keyCaseInsensitiveFinder.put(key, key);
        }
    }

    /**
     * 如果指定键尚未与值关联（或映射到 null），
     * 则尝试使用给定的映射函数计算其值，并将其输入到此映射中，除非为 null
     *
     * @param key             与指定值关联的键
     * @param mappingFunction 用于计算值的函数
     * @return 与指定键关联的当前（现有或计算出的）值，如果计算结果为 null，则返回 null
     */
    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        String actualKey = keyCaseInsensitiveFinder.get(key);

        if (actualKey != null) {
            // 键已存在（包括大小写变体）
            return nativeDataStorage.get(actualKey);
        } else {
            // 键不存在，计算新值
            Object newValue = mappingFunction.apply(key);
            if (newValue != null) {
                nativeDataStorage.put(key, newValue);
                keyCaseInsensitiveFinder.put(key, key);
            }
            return newValue;
        }
    }

    /**
     * 如果指定键的值存在且非 null，则尝试计算给定键的新映射
     *
     * @param key               键，其关联值将用于计算新映射
     * @param remappingFunction 重新映射函数，用于计算值
     * @return 与指定键关联的新值，如果已移除该条目或计算结果为 null，则返回 null
     */
    @Override
    public Object computeIfPresent(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        String actualKey = keyCaseInsensitiveFinder.get(key);

        if (actualKey != null) {
            Object oldValue = nativeDataStorage.get(actualKey);
            Object newValue = remappingFunction.apply(actualKey, oldValue);

            if (newValue != null) {
                nativeDataStorage.put(actualKey, newValue);
                return newValue;
            } else {
                nativeDataStorage.remove(actualKey);
                keyCaseInsensitiveFinder.remove(key);
                return null;
            }
        }
        return null;
    }

    /**
     * 尝试计算指定键及其当前映射值的映射（如果没有当前映射，则为 null）
     *
     * @param key               键，其关联值将用于计算新映射
     * @param remappingFunction 重新映射函数，用于计算值
     * @return 与指定键关联的新值，如果已移除该条目或计算结果为 null，则返回 null
     */
    @Override
    public Object compute(String key, BiFunction<? super String, ? super Object, ?> remappingFunction) {
        String actualKey = keyCaseInsensitiveFinder.get(key);
        Object oldValue = (actualKey != null) ? nativeDataStorage.get(actualKey) : null;
        Object newValue = remappingFunction.apply(key, oldValue);

        if (newValue != null) {
            if (actualKey != null) {
                nativeDataStorage.put(actualKey, newValue);
            } else {
                nativeDataStorage.put(key, newValue);
                keyCaseInsensitiveFinder.put(key, key);
            }
            return newValue;
        } else {
            if (actualKey != null) {
                nativeDataStorage.remove(actualKey);
                keyCaseInsensitiveFinder.remove(key);
            }
            return null;
        }
    }

    /**
     * 如果指定的键尚未与值关联，或者与 null 关联，
     * 则将其与给定的非 null 值关联。否则，将与给定值合并
     *
     * @param key               要与值关联的键
     * @param value             如果该键未与值关联，则与该键关联的非 null 值
     * @param remappingFunction 用于重新映射的函数
     * @return 新值，如果移除了该条目，则返回 null
     */
    @Override
    public Object merge(String key, Object value, BiFunction<? super Object, ? super Object, ?> remappingFunction) {
        String actualKey = keyCaseInsensitiveFinder.get(key);
        Object oldValue = (actualKey != null) ? nativeDataStorage.get(actualKey) : null;
        Object newValue = (oldValue == null) ? value : remappingFunction.apply(oldValue, value);

        if (newValue != null) {
            if (actualKey != null) {
                nativeDataStorage.put(actualKey, newValue);
            } else {
                nativeDataStorage.put(key, newValue);
                keyCaseInsensitiveFinder.put(key, key);
            }
            return newValue;
        } else {
            if (actualKey != null) {
                nativeDataStorage.remove(actualKey);
                keyCaseInsensitiveFinder.remove(key);
            }
            return null;
        }
    }

    /**
     * 创建并返回此对象的一个副本
     *
     * @return 此实例的一个克隆
     * @throws CloneNotSupportedException 如果克隆不被支持
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        try {
            ResultMap cloned = new ResultMap();

            // 深拷贝 dataMap
            cloned.nativeDataStorage.putAll(this.nativeDataStorage);

            // 深拷贝 keyMap
            cloned.keyCaseInsensitiveFinder.putAll(this.keyCaseInsensitiveFinder);

            return cloned;
        } catch (Exception e) {
            throw new CloneNotSupportedException("Cloning failed: " + e.getMessage());
        }
    }

    /**
     * 返回此映射的字符串表示形式
     *
     * @return 此映射的字符串表示形式
     */
    @Override
    public String toString() {
        return nativeDataStorage.toString();
    }

}

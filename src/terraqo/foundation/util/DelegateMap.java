package io.github.terraqo.foundation.util;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 一个基于委托模式实现的 {@link Map} 接口类。
 * <p>
 * 该类通过持有一个内部的 delegate map 实例，并将所有 {@link Map} 接口的方法调用
 * 转发给该 delegate 来实现功能。它支持在构造时选择性地包装为不可修改映射
 * ({@link Collections#unmodifiableMap(Map)}) 或同步映射
 * ({@link Collections#synchronizedMap(Map)})。
 * </p>
 * <p>
 * 子类可以通过重写 {@link #delegate()} 方法返回强类型的 delegate 引用，
 * 以便在子类中访问特定于实现的方法。
 * </p>
 *
 * @param <K> 映射中键的类型
 * @param <V> 映射中值的类型
 * @author Terraqo
 */
public class DelegateMap<K, V> implements Map<K, V> {

    /**
     * 内部委托的 Map 实例。所有操作都将转发到此实例。
     * 根据构造函数参数，此实例可能是原始 map、不可修改视图或同步视图。
     */
    private final Map<K, V> delegate;

    /**
     * 构造一个新的 DelegateMap。
     *
     * @param delegate       作为底层存储的实际 Map 实例，不能为 null
     * @param isUnmodifiable 如果为 true，则将 delegate 包装为不可修改的 Map。
     *                       注意：如果此参数为 true，则同步相关参数将被忽略，
     *                       且不会进行同步包装，因为不可修改映射通常是线程安全的（只要底层 map 在发布后不被修改）。
     * @param isSynchronized 如果为 true 且 isUnmodifiable 为 false，则将 delegate 包装为同步的 Map。
     * @throws NullPointerException 如果 delegate 为 null
     */
    public DelegateMap(Map<K, V> delegate, boolean isUnmodifiable, boolean isSynchronized) {
        Objects.requireNonNull(delegate, "The delegate cannot be null");
        if (isUnmodifiable) {
            // 优先处理不可修改需求，此时同步标志被忽略
            delegate = this.doUnmodifiable(delegate);
        } else {
            if (isSynchronized) {
                delegate = this.doSynchronized(delegate);
            }
        }
        this.delegate = delegate;
    }

    /**
     * 将给定的 delegate 包装为不可修改的 Map。
     * <p>
     * 此方法使用 {@link Collections#unmodifiableMap(Map)} 创建原始 map 的不可修改视图。
     * </p>
     *
     * @param delegate 要包装的原始 Map 实例
     * @return 不可修改的 Map 视图
     */
    protected Map<K, V> doUnmodifiable(Map<K, V> delegate) {
        return Collections.unmodifiableMap(delegate);
    }

    /**
     * 将给定的 delegate 包装为同步的 Map。
     * <p>
     * 此方法使用自定义的 {@link SynchronizedMap} 创建原始 map 的同步视图。
     * </p>
     *
     * @param delegate 要包装的原始 Map 实例
     * @return 同步的 Map 视图
     */
    protected Map<K, V> doSynchronized(Map<K, V> delegate) {
        return new SynchronizedMap<>(delegate, delegate);
    }

    /**
     * 返回强类型的委托 Map 实例。
     * <p>
     * 此方法主要用于子类，以便在不进行额外强制转换的情况下访问具体 delegate 类型的特有方法。
     * </p>
     *
     * @return 强类型的 delegate Map 实例
     */
    protected Map<K, V> delegate() {
        return delegate;
    }

    /**
     * 比较此映射与指定对象是否相等。
     * <p>
     * 当且仅当给定对象也是一个映射，并且两个映射表示相同的映射关系时，才返回 true。
     * 即，两个映射的大小相同，并且对于第一个映射中的每个键，第二个映射中也包含相同的键和对应的值。
     * </p>
     *
     * @param object 要与之比较相等性的对象
     * @return 如果指定对象等于此映射，则返回 true
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        DelegateMap<?, ?> that = (DelegateMap<?, ?>) object;
        return delegate().equals(that.delegate());
    }

    /**
     * 返回此映射的哈希码值。
     * <p>
     * 映射的哈希码定义为映射 entrySet() 视图中每个条目的哈希码之和。
     * 这确保了 m1.equals(m2) 意味着 m1.hashCode() == m2.hashCode()，
     * 符合 {@link Object#hashCode()} 的一般约定。
     * </p>
     *
     * @return 此映射的哈希码值
     */
    @Override
    public int hashCode() {
        return delegate().hashCode();
    }

    /**
     * 返回此映射的字符串表示形式。
     * <p>
     * 字符串表示形式由一组键值对组成， enclosed in curly braces ("{}")。
     * 相邻的元素由字符 ", "（逗号和空格）分隔。
     * 每个键值对都按照 key=value 的形式呈现。
     * </p>
     *
     * @return 此映射的字符串表示形式
     */
    @Override
    public String toString() {
        return delegate().toString();
    }

    /**
     * 返回此映射中的键值映射数量。
     *
     * @return 此映射中的键值映射数量
     */
    @Override
    public int size() {
        return delegate().size();
    }

    /**
     * 如果此映射不包含键值映射，则返回 true。
     *
     * @return 如果此映射不包含键值映射，则返回 true
     */
    @Override
    public boolean isEmpty() {
        return delegate().isEmpty();
    }

    /**
     * 如果此映射包含指定键的映射，则返回 true。
     *
     * @param key 测试其在此映射中是否存在性的键
     * @return 如果此映射包含指定键的映射，则返回 true
     * @throws ClassCastException   如果键是此映射不兼容的类型（可选）
     * @throws NullPointerException 如果指定键为 null 且此映射不允许 null 键（可选）
     */
    @Override
    public boolean containsKey(Object key) {
        return delegate().containsKey(key);
    }

    /**
     * 如果此映射将一个或多个键映射到指定值，则返回 true。
     * <p>
     * 更正式地说，当且仅当此映射包含至少一个到值 v 的映射，使得 (value==null ? v==null : value.equals(v)) 时，返回 true。
     * 对于大多数 Map 接口的实现来说，此操作可能需要线性时间。
     * </p>
     *
     * @param value 测试其在此映射中是否存在性的值
     * @return 如果此映射将一个或多个键映射到指定值，则返回 true
     * @throws ClassCastException   如果值是该映射不兼容的类型（可选）
     * @throws NullPointerException 如果指定值为 null 且此映射不允许 null 值（可选）
     */
    @Override
    public boolean containsValue(Object value) {
        return delegate().containsValue(value);
    }

    /**
     * 返回指定键所映射的值；如果此映射不包含该键的映射，则返回 null。
     * <p>
     * 更正式地说，如果此映射包含从键 k 到值 v 的映射，使得 (key==null ? k==null : key.equals(k))，
     * 则此方法返回 v；否则返回 null。（最多只能有一个这样的映射。）
     * </p>
     * <p>
     * 如果此映射允许 null 值，则返回 null 并不一定表明该映射不包含该键的映射；
     * 也可能该映射显式地将键映射到 null。可以使用 {@link #containsKey containsKey} 操作来区分这两种情况。
     * </p>
     *
     * @param key 要返回其关联值的键
     * @return 指定键所映射的值；如果此映射不包含该键的映射，则返回 null
     * @throws ClassCastException   如果键是此映射不兼容的类型（可选）
     * @throws NullPointerException 如果指定键为 null 且此映射不允许 null 键（可选）
     */
    @Override
    public V get(Object key) {
        return delegate().get(key);
    }

    /**
     * 将指定值与此映射中的指定键关联（可选操作）。
     * <p>
     * 如果映射先前包含键的映射，则旧值将被替换。
     * </p>
     *
     * @param key   与指定值关联的键
     * @param value 与指定键关联的值
     * @return 与 key 关联的先前值；如果 key 没有映射，则返回 null。
     * （如果实现支持 null 值，则返回 null 也可能表明映射先前将 null 与 key 关联。）
     * @throws UnsupportedOperationException 如果此映射不支持 put 操作
     * @throws ClassCastException            如果指定键或值的类阻止将其存储在此映射中
     * @throws NullPointerException          如果指定键或值为 null，且此映射不允许 null 键或值
     * @throws IllegalArgumentException      如果指定键或值的某些属性阻止将其存储在此映射中
     */
    @Override
    public V put(K key, V value) {
        return delegate().put(key, value);
    }

    /**
     * 如果存在键的映射，则从该映射中移除它（可选操作）。
     * <p>
     * 更正式地说，如果此映射包含从键 k 到值 v 的映射，使得 (key==null ? k==null : key.equals(k))，
     * 则移除该映射。（该映射最多只能包含一个这样的映射。）
     * </p>
     * <p>
     * 返回此映射先前与键关联的值；如果映射不包含键的映射，则返回 null。
     * </p>
     * <p>
     * 如果此映射允许 null 值，则返回 null 并不一定表明该映射不包含该键的映射；
     * 也可能该映射显式地将键映射到 null。
     * </p>
     * <p>
     * 一旦调用返回，映射将不再包含指定键的映射。
     * </p>
     *
     * @param key 要从映射中移除其映射的键
     * @return 与 key 关联的先前值；如果 key 没有映射，则返回 null。
     * @throws UnsupportedOperationException 如果此映射不支持 remove 操作
     * @throws ClassCastException            如果键是此映射不兼容的类型（可选）
     * @throws NullPointerException          如果指定键为 null 且此映射不允许 null 键（可选）
     */
    @Override
    public V remove(Object key) {
        return delegate().remove(key);
    }

    /**
     * 将指定映射中的所有映射复制到此映射（可选操作）。
     * <p>
     * 此调用的效果等同于对此映射对指定映射中的每个键 k 到值 v 的映射执行一次 {@link #put(Object, Object) put(k, v)}。
     * 如果在操作进行期间修改了指定的映射，则此操作的行为是不确定的。
     * </p>
     *
     * @param m 要存储在此映射中的映射
     * @throws UnsupportedOperationException 如果此映射不支持 putAll 操作
     * @throws ClassCastException            如果指定映射的键或值的类阻止将其存储在此映射中
     * @throws NullPointerException          如果指定映射为 null，或者如果此映射不允许 null 键或值，
     *                                       且指定映射包含 null 键或值
     * @throws IllegalArgumentException      如果指定映射的某些键或值的属性阻止将其存储在此映射中
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        delegate().putAll(m);
    }

    /**
     * 从此映射中移除所有映射（可选操作）。
     * <p>
     * 此调用返回后，映射将为空。
     * </p>
     *
     * @throws UnsupportedOperationException 如果此映射不支持 clear 操作
     */
    @Override
    public void clear() {
        delegate().clear();
    }

    /**
     * 返回此映射中包含的键的 {@link Set} 视图。
     * <p>
     * 该集合由映射支持，因此对映射的更改会反映在集合中，反之亦然。
     * 如果在对集合进行迭代时修改了映射（除了通过迭代器自己的 remove 操作），则迭代的结果是不确定的。
     * 该集合支持元素移除，通过 Iterator.remove、Set.remove、removeAll、retainAll 和 clear 操作
     * 从映射中移除相应的映射。它不支持 add 或 addAll 操作。
     * </p>
     *
     * @return 此映射中包含的键的 set 视图
     */
    @Override
    public Set<K> keySet() {
        return delegate().keySet();
    }

    /**
     * 返回此映射中包含的值的 {@link Collection} 视图。
     * <p>
     * 该集合由映射支持，因此对映射的更改会反映在集合中，反之亦然。
     * 如果在对集合进行迭代时修改了映射（除了通过迭代器自己的 remove 操作），则迭代的结果是不确定的。
     * 该集合支持元素移除，通过 Iterator.remove、Collection.remove、removeAll、retainAll 和 clear 操作
     * 从映射中移除相应的映射。它不支持 add 或 addAll 操作。
     * </p>
     *
     * @return 此映射中包含的值的 collection 视图
     */
    @Override
    public Collection<V> values() {
        return delegate().values();
    }

    /**
     * 返回此映射中包含的映射关系的 {@link Set} 视图。
     * <p>
     * 该集合由映射支持，因此对映射的更改会反映在集合中，反之亦然。
     * 如果在对集合进行迭代时修改了映射（除了通过迭代器自己的 remove 操作，
     * 或通过迭代器返回的映射项上的 setValue 操作），则迭代的结果是不确定的。
     * 该集合支持元素移除，通过 Iterator.remove、Set.remove、removeAll、retainAll 和 clear 操作
     * 从映射中移除相应的映射。它不支持 add 或 addAll 操作。
     * </p>
     *
     * @return 此映射中包含的映射关系的 set 视图
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return delegate().entrySet();
    }

    /**
     * 返回指定键所映射的值；如果此映射不包含该键的映射，则返回 defaultValue。
     *
     * @param key          要返回其关联值的键
     * @param defaultValue 如果此映射不包含指定键的映射，则返回的默认值
     * @return 指定键所映射的值；如果此映射不包含该键的映射，则返回 defaultValue
     */
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return delegate().getOrDefault(key, defaultValue);
    }

    /**
     * 对此映射中的每个条目执行给定的操作，直到所有条目都被处理或该操作抛出异常。
     * <p>
     * 迭代顺序由该映射的 {@link #entrySet()} 视图的迭代顺序决定。
     * </p>
     *
     * @param action 要对每个条目执行的操作
     * @throws NullPointerException 如果指定的 action 为 null
     */
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        delegate().forEach(action);
    }

    /**
     * 将每个条目的值替换为对该条目调用给定函数的结果，直到所有条目都被处理或该函数抛出异常。
     * <p>
     * 函数抛出的异常将传递给调用者。
     * </p>
     *
     * @param function 应用于每个条目的函数
     * @throws UnsupportedOperationException 如果此映射不支持 replaceAll 操作
     * @throws ClassCastException            如果函数的结果导致类型不匹配
     * @throws NullPointerException          如果指定的 function 为 null，或者函数返回 null 且此映射不允许 null 值
     * @throws IllegalArgumentException      如果函数的某些属性阻止将其存储在此映射中
     */
    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        delegate().replaceAll(function);
    }

    /**
     * 如果指定的键尚未与值关联（或映射到 null），则将其与给定值关联并返回 null；否则返回当前值。
     *
     * @param key   与指定值关联的键
     * @param value 与指定键关联的值
     * @return 与指定键关联的先前值；如果指定键没有映射，则返回 null。如果实现支持 null 值，则返回 null 也可能表明映射先前将 null 与 key 关联。
     * @throws UnsupportedOperationException 如果此映射不支持 put 操作
     * @throws ClassCastException            如果指定键或值的类阻止将其存储在此映射中
     * @throws NullPointerException          如果指定键或值为 null，且此映射不允许 null 键或值
     * @throws IllegalArgumentException      如果指定键或值的某些属性阻止将其存储在此映射中
     */
    @Override
    public V putIfAbsent(K key, V value) {
        return delegate().putIfAbsent(key, value);
    }

    /**
     * 仅当指定键当前映射到指定值时，才移除该键的映射。
     *
     * @param key   预期映射关联的键
     * @param value 预期映射关联的值
     * @return 如果值被移除，则返回 true
     * @throws UnsupportedOperationException 如果此映射不支持 remove 操作
     * @throws ClassCastException            如果键或值是不兼容的类型（可选）
     * @throws NullPointerException          如果指定键或值为 null，且此映射不允许 null 键或值（可选）
     */
    @Override
    public boolean remove(Object key, Object value) {
        return delegate().remove(key, value);
    }

    /**
     * 仅当指定键当前映射到指定值时，才替换该键的映射。
     *
     * @param key      预期映射关联的键
     * @param oldValue 预期映射关联的值
     * @param newValue 与指定键关联的新值
     * @return 如果值被替换，则返回 true
     * @throws UnsupportedOperationException 如果此映射不支持修改操作
     * @throws ClassCastException            如果指定键或值的类阻止将其存储在此映射中
     * @throws NullPointerException          如果指定键或值为 null，且此映射不允许 null 键或值
     * @throws IllegalArgumentException      如果指定键或值的某些属性阻止将其存储在此映射中
     */
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return delegate().replace(key, oldValue, newValue);
    }

    /**
     * 仅当指定键当前映射到某个值时，才替换该键的映射。
     *
     * @param key   预期映射关联的键
     * @param value 与指定键关联的新值
     * @return 与指定键关联的先前值；如果指定键没有映射，则返回 null
     * @throws UnsupportedOperationException 如果此映射不支持修改操作
     * @throws ClassCastException            如果指定键或值的类阻止将其存储在此映射中
     * @throws NullPointerException          如果指定键或值为 null，且此映射不允许 null 键或值
     * @throws IllegalArgumentException      如果指定键或值的某些属性阻止将其存储在此映射中
     */
    @Override
    public V replace(K key, V value) {
        return delegate().replace(key, value);
    }

    /**
     * 如果指定的键尚未与值关联（或映射到 null），则尝试使用给定的映射函数计算其值，并在非 null 时将其输入此映射。
     * <p>
     * 如果映射函数返回 null，则不记录任何映射。
     * </p>
     *
     * @param key             与值关联的键
     * @param mappingFunction 用于计算值的函数
     * @return 与指定键关联的当前（现有或计算出的）值；如果计算出的值为 null，则返回 null
     * @throws UnsupportedOperationException 如果此映射不支持 computeIfAbsent 操作
     * @throws NullPointerException          如果指定的 mappingFunction 为 null
     * @throws ClassCastException            如果映射函数的结果导致类型不匹配
     * @throws RuntimeException              如果映射函数抛出异常
     */
    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return delegate().computeIfAbsent(key, mappingFunction);
    }

    /**
     * 如果指定键的值存在且非 null，则根据旧值计算新值。如果新值非 null，则更新映射；否则移除映射。
     *
     * @param key               与值关联的键
     * @param remappingFunction 用于计算新值的函数
     * @return 与指定键关联的新值；如果不存在关联值，则返回 null；如果计算出的值为 null，则移除映射并返回 null
     * @throws UnsupportedOperationException 如果此映射不支持 computeIfPresent 操作
     * @throws NullPointerException          如果指定的 remappingFunction 为 null
     * @throws ClassCastException            如果重映射函数的结果导致类型不匹配
     * @throws RuntimeException              如果重映射函数抛出异常
     */
    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return delegate().computeIfPresent(key, remappingFunction);
    }

    /**
     * 尝试根据键及其当前映射值（如果存在）计算新值。如果新值非 null，则更新映射；否则移除映射。
     *
     * @param key               与值关联的键
     * @param remappingFunction 用于计算新值的函数
     * @return 与指定键关联的新值；如果不存在关联值，则返回 null
     * @throws UnsupportedOperationException 如果此映射不支持 compute 操作
     * @throws NullPointerException          如果指定的 remappingFunction 为 null
     * @throws ClassCastException            如果重映射函数的结果导致类型不匹配
     * @throws RuntimeException              如果重映射函数抛出异常
     */
    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return delegate().compute(key, remappingFunction);
    }

    /**
     * 如果指定的键尚未与值关联（或映射到 null），则将其与给定值关联；否则，使用给定的重映射函数计算新值并替换旧值。
     * <p>
     * 如果重映射函数返回 null，则移除该键的映射。
     * </p>
     *
     * @param key               与值关联的键
     * @param value             如果不存在关联值，则与键关联的非 null 值；或如果存在关联值，则传递给重映射函数的值
     * @param remappingFunction 用于计算新值的函数
     * @return 与指定键关联的新值；如果不存在关联值，则返回给定的 value；如果计算出的值为 null，则返回 null
     * @throws UnsupportedOperationException 如果此映射不支持 merge 操作
     * @throws NullPointerException          如果指定的 remappingFunction 为 null
     * @throws ClassCastException            如果重映射函数的结果导致类型不匹配
     * @throws RuntimeException              如果重映射函数抛出异常
     */
    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return delegate().merge(key, value, remappingFunction);
    }
}
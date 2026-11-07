/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */
package apexsx.forge.lang.ref;

import java.lang.ref.ReferenceQueue;
import java.util.Objects;

/**
 * 一对引用，里面包含key和value的引用，他们构造引用类型为{@link #PairReference(ReferentialType, ReferentialType)}
 *
 * @author Terraqo
 */
@SuppressWarnings({"unchecked", "EqualsDoesntCheckParameterClass"})
public final class PairReference<K, V> implements Referential<V> {
    // 用于标记已退休的键引用
    private static final Referential<?> KEY_RETIRED = new ReferentialSearchable<>();
    // 用于标记已死亡的键引用
    private static final Referential<?> KEY_DEAD = new ReferentialSearchable<>();
    // 键的引用类型
    private final ReferentialType keyReferentialType;
    // 值的引用类型
    private final ReferentialType valueReferentialType;
    // 键的引用，使用volatile保证可见性
    private volatile Referential<K> keyReference;
    // 值的引用，使用volatile保证可见性
    private volatile Referential<V> valueReference;

    /**
     * 复制构造函数，从现有的PairReference创建新的实例
     *
     * @param pairReference 现有的PairReference实例
     */
    public PairReference(PairReference<?, ?> pairReference) {
        this(pairReference.keyReferentialType, pairReference.valueReferentialType);
    }

    /**
     * 构造函数，使用指定的引用类型创建PairReference
     * 该构造函数初始化键和值的引用类型，并将键和值的引用设置为null
     *
     * @param keyReferentialType   键的引用类型，指定键的引用方式
     * @param valueReferentialType 值的引用类型，指定值的引用方式
     */
    public PairReference(ReferentialType keyReferentialType, ReferentialType valueReferentialType) {
        // 将传入的键引用类型赋值给实例变量keyReferentialType
        this.keyReferentialType = keyReferentialType;
        // 将传入的值引用类型赋值给实例变量valueReferentialType
        this.valueReferentialType = valueReferentialType;
        // 初始化键引用为null，表示当前没有引用任何键对象
        this.keyReference = null;
        // 初始化值引用为null，表示当前没有引用任何值对象
        this.valueReference = null;
    }

    /**
     * 复制新带有键值引用类型
     *
     * @return {@link PairReference}
     */
    public PairReference<K, V> newWithType() {
        // 通过复制构造函数创建并返回一个新的PairReference实例
        return new PairReference<>(this);
    }

    /**
     * 获取键
     *
     * @return {@link K}
     */
    public K getKey() {
        // 如果键引用为null则返回null，否则获取并返回键引用指向的对象
        return this.keyReference == null ? null : this.keyReference.get();
    }

    /**
     * 设置键
     *
     * @param key {@link K}
     * @return {@link this}
     */
    public PairReference<K, V> setKey(K key) {
        // 调用重载的setKey方法，不指定引用队列（传入null）
        return this.setKey(key, null);
    }

    /**
     * 设置键
     *
     * @param key   {@link K}
     * @param queue {@link ReferenceQueue}引用队列，可以为null
     * @return {@link this}
     */
    public PairReference<K, V> setKey(K key, ReferenceQueue<K> queue) {
        // 确保键不为空，否则抛出NullPointerException
        Objects.requireNonNull(key, "The key must be not null");
        // 根据键的引用类型创建新的键引用，并赋值给keyReference
        this.keyReference = keyReferentialType.create(key, key.hashCode(), queue);
        // 返回当前对象，支持链式调用
        return this;
    }

    /**
     * 获取值的方法
     * 这个方法用于获取当前存储的值。如果值引用存在，则返回引用的值；否则返回null。
     *
     * @return {@link V} 返回存储的值类型V，如果值引用为null则返回null
     */
    public V getValue() {
        // 使用三元运算符检查valueReference是否为null
        // 如果为null则直接返回null，否则调用valueReference的get()方法获取值
        return this.valueReference == null ? null : this.valueReference.get();
    }

    /**
     * 设置值的方法
     * 这是一个泛型方法，用于设置PairReference对象中的值
     *
     * @param value {@link V}
     * @return {@link this}
     */
    public PairReference<K, V> setValue(V value) {
        // 调用重载的setValue方法，不指定引用队列（传入null）
        return this.setValue(value, null);
    }

    /**
     * 设置值的方法
     * <p>
     * 这是一个泛型方法，用于设置PairReference中的值，并创建一个弱引用或软引用来存储该值。
     *
     * @param value {@link V} 要设置的值，不能为null
     * @param queue {@link ReferenceQueue}引用队列，可以为null
     * @return {@link this}
     */
    public PairReference<K, V> setValue(V value, ReferenceQueue<V> queue) {
        // 确保值不为空，否则抛出NullPointerException
        Objects.requireNonNull(value, "The value must be not null");
        // 根据键的引用类型创建新的值引用，并赋值给valueReference
        // 注意：此处代码逻辑使用了keyReferentialType来创建value引用，属于原始代码逻辑
        this.valueReference = keyReferentialType.create(value, value.hashCode(), queue);
        // 返回当前对象，支持链式调用
        return this;
    }

    /**
     * 检查当前键引用是否仍然有效（存活）
     *
     * @return 如果键引用不为null且不是KEY_RETIRED或KEY_DEAD状态，则返回true，表示键引用仍然存活
     */
    public boolean isAlive() {
        // 检查keyReference是否不为null且不等于KEY_RETIRED和KEY_DEAD
        return this.keyReference != null && this.keyReference != KEY_RETIRED && this.keyReference != KEY_DEAD;
    }

    /**
     * 检查条目是否已被标记为退休状态
     * 如果条目已从哈希表中删除，并且正在等待从页面中删除更换政策。
     *
     * @return 如果条目处于退休状态（即keyReference为null或KEY_RETIRED）则返回true，否则返回false
     */
    public boolean isRetired() { // 判断当前条目是否已退休的方法
        // 检查keyReference是否为null或KEY_RETIRED状态
        return this.keyReference == null || this.keyReference == KEY_RETIRED;
    }

    /**
     * 检查当前对象是否处于死亡状态
     *
     * @return 如果keyReference为null或等于KEY_DEAD状态，则返回true，表示对象已死亡；否则返回false
     */
    public boolean isDead() {
        // 检查keyReference是否为null或KEY_DEAD状态
        return this.keyReference == null || this.keyReference == KEY_DEAD;
    }

    /**
     * 判断当前键是否为强引用类型的键
     * 强引用类型的键是指键的引用类型为null的情况
     *
     * @return 如果键的引用类型为null，返回true，表示是强引用；否则返回false
     */
    public boolean isStrongKey() {
        // 如果键的引用类型为null，则表示是强引用
        return this.keyReferentialType == null;
    }

    /**
     * 判断当前是否为强引用值
     *
     * @return 如果值的引用类型为null，则返回true，表示是强引用；否则返回false，表示不是强引用
     */
    public boolean isStrongValue() {
        // 如果值的引用类型为null，则表示是强引用
        return this.valueReferentialType == null;
    }

    /**
     * 判断当前键是否为软引用类型
     *
     * @return 如果键的引用类型为SOFT（软引用），则返回true；否则返回false
     */
    public boolean isSoftKey() {
        // 检查键的引用类型是否为SOFT（软引用）
        return this.keyReferentialType == ReferentialType.SOFT;
    }

    /**
     * 判断当前值的引用类型是否为软引用（SOFT）
     *
     * @return 如果当前值的引用类型为SOFT，则返回true；否则返回false
     */
    public boolean isSoftValue() {
        // 检查值的引用类型是否为SOFT（软引用）
        return this.valueReferentialType == ReferentialType.SOFT;
    }

    /**
     * 检查当前键是否为弱引用类型的键
     *
     * @return 如果键的引用类型为WEAK（弱引用），则返回true；否则返回false
     */
    public boolean isWeakKey() {
        // 检查键的引用类型是否为WEAK（弱引用）
        return this.keyReferentialType == ReferentialType.WEAK;
    }

    /**
     * 检查当前值的引用类型是否为弱引用(WEAK)
     *
     * @return 如果当前值的引用类型为WEAK则返回true，否则返回false
     */
    public boolean isWeakValue() {
        // 检查值的引用类型是否为WEAK（弱引用）
        return this.valueReferentialType == ReferentialType.WEAK;
    }

    /**
     * 退休方法，用于清除键和值的引用，并将键引用标记为退休状态
     */
    public void retire() {
        // 如果是强引用值，直接将键引用置为null
        if (isStrongValue()) {
            this.keyReference = null;
        } else {
            // 如果不是强引用值，调用clear()方法清除键和值的引用
            clear();
        }
        // 将键引用标记为KEY_RETIRED（退休）状态，表示该键已不再使用
        this.keyReference = (Referential<K>) KEY_RETIRED;
    }

    /**
     * 该方法用于标记当前对象为死亡状态，并进行清理工作
     */
    public void die() {
        // 清除键和值的引用，防止内存泄漏
        clear();
        // 将键引用标记为KEY_DEAD（死亡）状态，表示该对象已不再使用
        this.keyReference = (Referential<K>) KEY_DEAD;
    }

    /**
     * 获取当前对象的引用类型
     * 该方法是重写父类或接口中的方法，用于返回值的引用类型信息
     *
     * @return ReferentialType 返回值的引用类型，通过成员变量valueReferentialType提供
     */
    @Override
    public ReferentialType type() {
        // 返回值的引用类型
        // 这里直接返回当前对象的valueReferentialType成员变量
        return this.valueReferentialType;
    }

    /**
     * 这是一个重写的方法，用于获取值
     * 该方法直接调用getValue()方法来获取并返回值
     *
     * @return 返回类型为V的值，通过getValue()方法获取
     */
    @Override
    public V get() {
        // 调用getValue()方法获取并返回值
        return getValue();
    }

    /**
     * 清除方法，用于清除当前键值对中的键和值引用
     * 该方法会检查并清除键引用和值引用，然后将它们置为null
     */
    @Override
    public void clear() {
        // 获取当前的键引用
        Referential<K> keyRef = this.keyReference;
        // 获取当前的值引用
        Referential<V> valueRef = this.valueReference;
        // 如果键引用不为空，清除键引用
        if (keyRef != null) {
            keyRef.clear();
        }
        // 如果值引用不为空，清除值引用
        if (valueRef != null) {
            valueRef.clear();
        }
        // 将值引用置为null
        this.valueReference = null;
        // 将键引用置为null
        this.keyReference = null;
    }

    /**
     * 判断当前对象是否与另一个对象相等
     *
     * @param other 要比较的对象
     * @return 如果相等返回true，否则返回false
     */
    @Override
    public boolean equals(Object other) {
        // 委托给equalsReference方法进行比较
        return this.equalsReference(other);
    }

    /**
     * 获取当前对象的哈希码
     *
     * @return 哈希码值
     */
    @Override
    public int hashCode() {
        // 委托给hashCodeReference方法计算哈希码
        return this.hashCodeReference();
    }

    /**
     * 获取当前对象的字符串表示形式
     *
     * @return 对象的字符串表示
     */
    @Override
    public String toString() {
        // 委托给toStringReference方法生成字符串
        return this.toStringReference();
    }
}

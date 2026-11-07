/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */
package apexsx.forge.core.cc;

import java.io.Serial;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 通用泛型键值对 Pair 对象，继承自 {@link Tuple}，实现 {@link Map.Entry}。
 * <p>可以直接用于 Map 的迭代、转换，复用 Tuple 的全部能力：环形索引、克隆、序列化、比较、equals/hashCode 等。</p>
 * <ul>
 *     <li>底层固定二元元组：下标0存储key，下标1存储value；长度固定为2，不支持增删元素</li>
 *     <li>immutable（继承父类）：为true时禁止调用{@link #setValue(Object)}修改value，抛出{@link UnsupportedOperationException}</li>
 *     <li>asKey（继承父类）：控制equals/hashCode/compareTo行为；asKey=true时key+value整体参与相等判断；asKey=false仅key参与判断</li>
 *     <li>key：无论可变/不可变模式，key均无法修改；底层对应下标0，没有对外提供setKey方法</li>
 *     <li>可变实例：仅value可修改，支持{@link #setValue(Object)}；支持链式扩展（可自行补充withValue）</li>
 *     <li>不可变实例：value禁止修改，调用setValue直接抛异常</li>
 * </ul>
 * <p>⚠️注意：Pair是长度固定为2的Tuple，继承父类所有API，例如get/set/sub/extend/clone等；
 * 调用父类原始set(0,xxx)可以绕过Pair重写的set方法直接修改key，业务层应当避免该用法。</p>
 *
 * @param <K> Key 键类型
 * @param <V> Value 值类型
 * @author Terraqo
 */
@SuppressWarnings("unchecked")
public class Pair<K, V> extends Tuple implements Map.Entry<K, V> {

    /**
     * 序列化版本号。
     * <p>{@link Serial}注解标识序列化版本字段，用于对象序列化/反序列化版本校验。
     * 继承Tuple的序列化能力，保证Pair对象序列化兼容性；修改类结构需要同步更新该版本号。</p>
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 内部完整构造函数。
     * <p>仅本包、子类可访问；外部禁止直接new，必须使用静态工厂方法。</p>
     * <p>底层把 key、value 封装为二元数组交给父类Tuple构造；
     * 下标0=key，下标1=value。</p>
     *
     * @param immutable 是否不可变标记；true=不可变，禁止setValue修改value；false=可变，允许修改value
     * @param asKey     是否开启asKey模式，继承Tuple语义：true全部元素参与equals/hash/compare；false仅第一个元素(key)参与
     * @param key       键对象，允许为null
     * @param value     值对象，允许为null
     */
    protected Pair(boolean immutable, boolean asKey, K key, V value) {
        // 调用父类Tuple构造，传入两个元素：0号位置key，1号位置value
        super(immutable, asKey, key, value);
    }

    /**
     * 构建【不可变Pair】实例(asKey=false)。
     * <ul>
     *     <li>key不可修改；value也不可修改，调用{@link #setValue(Object)}会抛出异常</li>
     *     <li>equals/hashCode/compareTo：仅使用key参与计算，忽略value</li>
     * </ul>
     *
     * @param key   键对象，可以为null
     * @param value 值对象，可以为null
     * @return 新建不可变Pair&lt;K,V&gt;实例，固定二元结构
     */
    public static <K, V> Pair<K, V> with(K key, V value) {
        // 第一个参数 true → immutable=true 不可变
        return new Pair<>(true, false, key, value);
    }

    /**
     * 构建【asKey模式 不可变Pair】实例(asKey=true)。
     * <ul>
     *     <li>key不可修改；value不可修改，调用setValue抛出异常</li>
     *     <li>equals/hashCode/compareTo：key+value两个元素全部参与比较与哈希计算</li>
     * </ul>
     *
     * @param key   键对象，可以为null
     * @param value 值对象，可以为null
     * @return 新建asKey模式不可变Pair&lt;K,V&gt;实例
     */
    public static <K, V> Pair<K, V> withKey(K key, V value) {
        return new Pair<>(true, true, key, value);
    }

    /**
     * 构建【普通可变Pair】实例(asKey=false)。
     * <ul>
     *     <li>key不可修改；value允许通过{@link #setValue(Object)}修改</li>
     *     <li>equals/hashCode/compareTo：仅使用key参与计算，忽略value</li>
     * </ul>
     *
     * @param key   键对象，可以为null
     * @param value 值对象，可以为null
     * @return 新建可变Pair&lt;K,V&gt;实例，固定二元结构
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        // 第一个参数 false → immutable=false 可变
        return new Pair<>(false, false, key, value);
    }

    /**
     * 构建【asKey模式可变Pair】实例(asKey=true)。
     * <ul>
     *     <li>key不可修改；value允许通过{@link #setValue(Object)}修改</li>
     *     <li>equals/hashCode/compareTo：key+value两个元素全部参与比较与哈希计算</li>
     * </ul>
     *
     * @param key   键对象，可以为null
     * @param value 值对象，可以为null
     * @return 新建asKey模式可变Pair&lt;K,V&gt;实例
     */
    public static <K, V> Pair<K, V> ofKey(K key, V value) {
        return new Pair<>(false, true, key, value);
    }

    /**
     * 基于{@link Map.Entry}复制生成Pair对象。
     * <p>拷贝entry的key、value，不持有原entry的引用；修改新Pair不会影响原始entry。
     * 可自由指定新Pair的 immutable（是否不可变）与 asKey（相等判断模式）。</p>
     *
     * @param entry     源Map.Entry，不能为null
     * @param immutable 是否设置为不可变实例
     * @param asKey     是否开启asKey全字段相等判断模式
     * @return 拷贝得到的Pair实例
     * @throws NullPointerException 当传入entry为null抛出
     */
    public static <K, V> Pair<K, V> fromEntry(Map.Entry<K, V> entry, boolean immutable, boolean asKey) {
        // 非空校验，源Entry不能为空
        Objects.requireNonNull(entry, "entry must not be null");
        // 提取原entry的key、value，构造新Pair
        return new Pair<>(immutable, asKey, entry.getKey(), entry.getValue());
    }

    /**
     * 获取Entry的key，对应Tuple内部下标0。
     *
     * @return key对象，可为null
     */
    @Override
    public K getKey() {
        // 调用父类Tuple#get(int) 获取下标0元素，强转为K泛型
        return (K) get(0);
    }

    /**
     * 获取Entry的value，对应Tuple内部下标1。
     *
     * @return value对象，可为null
     */
    @Override
    public V getValue() {
        // 调用父类Tuple#get(int) 获取下标1元素，强转为V泛型
        return (V) get(1);
    }

    /**
     * 修改value值，对应Tuple下标1。
     * <p>如果Pair为不可变模式(immutable=true)，调用会抛出{@link UnsupportedOperationException}。</p>
     *
     * @param value 新的value，可以为null
     * @return 返回修改之前的旧value
     * @throws UnsupportedOperationException 不可变Pair调用此方法
     */
    @Override
    public V setValue(V value) {
        // 设置下标1位置（value），返回旧值
        return (V) set(1, value);
    }

    /**
     * 重写元组set方法：环形索引修改元素；仅可变元组允许调用。
     * <p>Pair强制禁止修改下标0（key），无论实例是否可变。
     * 即使是可变Pair，key也不允许被替换，只允许修改下标1的value。</p>
     *
     * @param index 环形索引（支持负数环形寻址，由父类at()解析为真实下标）
     * @param value 设置的新值，可以null
     * @return 被替换的旧元素
     * @throws UnsupportedOperationException 1.尝试修改key(下标0)；2.不可变元组调用
     * @throws IndexOutOfBoundsException     空元组调用
     */
    @Override
    public Object set(int index, Object value) {
        // at(index): 将传入的环形索引转换为真实数组下标
        int real = at(index);
        // 禁止修改key，真实下标等于0直接抛异常
        if (real == 0) {
            throw new UnsupportedOperationException("Pair key cannot be modified, do not set index 0");
        }
        // real=1，交给父类Tuple.set完成value修改，父类内部会校验immutable不可变标记
        return super.set(index, value);
    }

    /**
     * 克隆元组实例；底层elements数组独立拷贝，原对象与克隆对象数据隔离。
     * <p>克隆继承原实例 immutable、asKey 属性。
     * <ul>
     *     <li>可变元组clone得到可变元组，修改clone不会影响原对象</li>
     *     <li>不可变元组clone得到不可变元组</li>
     * </ul>
     * <p>注意：只复制容器数组，key、value对象本身是浅拷贝，不会递归克隆内部对象。</p>
     *
     * @return 全新Pair克隆实例（返回类型协变，不再返回父类Tuple）
     */
    @Override
    public Pair<K, V> clone() {
        // 浅拷贝父类内部elements数组，生成独立副本
        Object[] copied = Arrays.copyOf(this.elements, this.elements.length);
        // 使用拷贝后的数组元素，复用原对象immutable、asKey标记构造新Pair
        return new Pair<>(this.immutable, this.asKey, (K) copied[0], (V) copied[1]);
    }
}
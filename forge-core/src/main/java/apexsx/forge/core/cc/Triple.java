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
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 通用泛型三元元组 Triple 对象，继承自 {@link Tuple}，实现 {@code  Map.Entry<K,V>}，支持序列化与克隆。
 * <p>结构固定为三元：下标0=key，下标1=value，下标2=element；长度固定为3，不支持动态增删元素。</p>
 * <p>复用 Tuple 的全部底层能力：环形索引、克隆、序列化、equals/hashCode/compareTo 比较逻辑。</p>
 * <ul>
 *     <li>immutable（继承父类）：true = 不可变；禁止修改 value / element，调用修改方法抛出 {@link UnsupportedOperationException}</li>
 *     <li>asKey（继承父类）：控制equals/hashCode/compareTo行为；
 *         asKey=true：key+value+element 三者整体参与相等判断；
 *         asKey=false：仅 key(下标0)参与相等判断，忽略 value、element。
 *     </li>
 *     <li>key：无论可变/不可变模式，key 均禁止修改；无对外 setKey 方法</li>
 *     <li>可变实例(immutable=false)：允许修改 value(下标1)、element(下标2)；key依旧不可修改</li>
 *     <li>不可变实例(immutable=true)：value、element 全部禁止修改，setValue / setElement 调用抛异常</li>
 * </ul>
 * <p>⚠️风险提示：Triple继承父类全部API；直接调用父类原生 {@code super.set(0,xxx)} 可以绕过本类重写保护修改key，业务代码禁止此种用法。</p>
 *
 * @param <K> Key 键类型，对应下标0
 * @param <V> Value 值类型，对应下标1
 * @param <E> Element 第三个元素类型，对应下标2
 * @author Terraqo
 */
@SuppressWarnings("unchecked")
public class Triple<K, V, E> extends Tuple implements Map.Entry<K, V>, Serializable, Cloneable {

    /**
     * 序列化版本号。
     * <p>{@link Serial}注解标识序列化版本字段，用于对象序列化/反序列化版本校验。
     * 继承Tuple的序列化能力，保证Triple对象序列化兼容性；类结构发生变更时需要同步更新该版本号。</p>
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 内部完整构造函数。
     * <p>仅本包、子类可访问；外部代码禁止直接 new，必须使用静态工厂方法 with / withKey / of / ofKey。</p>
     * <p>底层三元数组分配给父类Tuple：下标0=key，下标1=value，下标2=element。</p>
     *
     * @param immutable 是否不可变标记；true=不可变，禁止修改value、element；false=可变，允许修改value、element
     * @param asKey     是否开启asKey模式；true全部三个元素参与equals/hash/compare；false仅key参与判断
     * @param key       键对象，允许为null
     * @param value     值对象，允许为null
     * @param element   第三个附加元素，允许为null
     */
    protected Triple(boolean immutable, boolean asKey, K key, V value, E element) {
        // 调用父类Tuple构造，传入3个元素：0=key，1=value，2=element
        super(immutable, asKey, key, value, element);
    }

    /**
     * 构建【不可变Triple】实例(asKey=false)。
     * <ul>
     *     <li>key、value、element全部不可修改；调用 setValue / setElement 抛出异常</li>
     *     <li>equals/hashCode/compareTo：仅使用key参与计算，忽略 value、element</li>
     * </ul>
     *
     * @param key     键对象，可以为null
     * @param value   值对象，可以为null
     * @param element 第三个附加元素，可以为null
     * @return 新建不可变 Triple&lt;K,V,E&gt;实例，固定三元结构
     */
    public static <K, V, E> Triple<K, V, E> with(K key, V value, E element) {
        // 第一个参数 true → immutable=true 不可变
        return new Triple<>(true, false, key, value, element);
    }

    /**
     * 构建【asKey模式 不可变Triple】实例(asKey=true)。
     * <ul>
     *     <li>key、value、element全部不可修改；调用 setValue / setElement 抛出异常</li>
     *     <li>equals/hashCode/compareTo：key+value+element三者全部参与比较与哈希计算</li>
     * </ul>
     *
     * @param key     键对象，可以为null
     * @param value   值对象，可以为null
     * @param element 第三个附加元素，可以为null
     * @return 新建asKey模式不可变 Triple&lt;K,V,E&gt;实例
     */
    public static <K, V, E> Triple<K, V, E> withKey(K key, V value, E element) {
        return new Triple<>(true, true, key, value, element);
    }

    /**
     * 构建【普通可变Triple】实例(asKey=false)。
     * <ul>
     *     <li>key不可修改；允许通过 setValue 修改value、setElement修改element</li>
     *     <li>equals/hashCode/compareTo：仅使用key参与计算，忽略 value、element</li>
     * </ul>
     *
     * @param key     键对象，可以为null
     * @param value   值对象，可以为null
     * @param element 第三个附加元素，可以为null
     * @return 新建可变 Triple&lt;K,V,E&gt;实例
     */
    public static <K, V, E> Triple<K, V, E> of(K key, V value, E element) {
        // 第一个参数 false → immutable=false 可变
        return new Triple<>(false, false, key, value, element);
    }

    /**
     * 构建【asKey模式可变Triple】实例(asKey=true)。
     * <ul>
     *     <li>key不可修改；允许通过 setValue 修改value、setElement修改element</li>
     *     <li>equals/hashCode/compareTo：key+value+element三者全部参与比较与哈希计算</li>
     * </ul>
     *
     * @param key     键对象，可以为null
     * @param value   值对象，可以为null
     * @param element 第三个附加元素，可以为null
     * @return 新建asKey模式可变 Triple&lt;K,V,E&gt;实例
     */
    public static <K, V, E> Triple<K, V, E> ofKey(K key, V value, E element) {
        return new Triple<>(false, true, key, value, element);
    }

    /**
     * 基于{@link Map.Entry}复制生成Triple对象。
     * <p>拷贝entry的key、value，额外传入第三个element；不持有原entry引用；修改新Triple不会影响原始entry。
     * 可自由指定新Triple的 immutable（是否不可变）与 asKey（相等判断模式）。</p>
     *
     * @param entry     源Map.Entry，不能为null
     * @param element   追加的第三个元素
     * @param immutable 是否设置为不可变实例
     * @param asKey     是否开启asKey全字段相等判断模式
     * @return 拷贝得到的Triple实例
     * @throws NullPointerException 当传入entry为null抛出
     */
    public static <K, V, E> Triple<K, V, E> fromEntry(Map.Entry<K, V> entry, E element, boolean immutable, boolean asKey) {
        // 非空校验，源Entry不能为空
        Objects.requireNonNull(entry, "entry must not be null");
        // 提取原entry的key、value，加上传入element构造Triple
        return new Triple<>(immutable, asKey, entry.getKey(), entry.getValue(), element);
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
     * <p>如果Triple为不可变模式(immutable=true)，调用会抛出{@link UnsupportedOperationException}。</p>
     *
     * @param value 新的value，可以为null
     * @return 返回修改之前的旧value
     * @throws UnsupportedOperationException 不可变Triple调用此方法
     */
    @Override
    public V setValue(V value) {
        // 设置下标1位置（value），返回旧值
        return (V) set(1, value);
    }

    /**
     * 获取第三个附加元素 element，对应Tuple内部下标2。
     *
     * @return element对象，可为null
     */
    public E getElement() {
        // 调用父类Tuple#get(int) 获取下标2元素，强转为E泛型
        return (E) get(2);
    }

    /**
     * 修改第三个附加元素 element，对应Tuple下标2。
     * <p>如果Triple为不可变模式(immutable=true)，调用会抛出{@link UnsupportedOperationException}。</p>
     *
     * @param value 新的element值，可以为null
     * @return 返回修改之前的旧element
     * @throws UnsupportedOperationException 不可变Triple调用此方法
     */
    public E setElement(E value) {
        // 设置下标2位置（element），返回旧值
        return (E) set(2, value);
    }

    /**
     * 重写元组set方法：环形索引修改元素；仅可变元组允许调用。
     * <p>Triple强制禁止修改下标0（key），无论实例是否可变。
     * 即使是可变Triple，key也不允许被替换；下标1(value)、下标2(element)允许修改。</p>
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
            throw new UnsupportedOperationException("Triple key cannot be modified, do not set index 0");
        }
        // real=1 / real=2，交给父类Tuple.set完成修改，父类内部校验immutable不可变标记
        return super.set(index, value);
    }

    /**
     * 克隆元组实例；底层elements数组独立拷贝，原对象与克隆对象数据隔离。
     * <p>克隆继承原实例 immutable、asKey 属性。
     * <ul>
     *     <li>可变元组clone得到可变元组，修改clone不会影响原对象</li>
     *     <li>不可变元组clone得到不可变元组</li>
     * </ul>
     * <p>注意：只复制存储元素的容器数组，key、value、element对象本身为浅拷贝，不会递归克隆内部对象。</p>
     *
     * @return 全新Triple克隆实例（协变返回类型，不再返回父类Tuple）
     */
    @Override
    public Triple<K, V, E> clone() {
        // 浅拷贝父类内部elements数组，生成独立副本
        Object[] copied = Arrays.copyOf(this.elements, this.elements.length);
        // 使用拷贝后的数组元素，复用原对象immutable、asKey标记构造新Triple
        return new Triple<>(this.immutable, this.asKey, (K) copied[0], (V) copied[1], (E) copied[2]);
    }
}
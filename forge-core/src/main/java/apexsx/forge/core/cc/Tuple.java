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
import java.util.*;
import java.util.stream.Stream;

/**
 * 通用元组容器，支持【可变 / 不可变】模式，内置环形索引寻址。
 * <p><b>核心特性：</b>
 * <ul>
 *     <li>环形索引：get/set/sub，负数、大于size索引自动取模映射合法下标 [0, size)</li>
 *     <li>长度固定：可变元组仅允许修改元素值，不能新增、删除元素；不可变元组禁止set操作</li>
 *     <li>asKey标记：改变equals/hashCode比较策略，用于Map键场景；
 *         <ul>
 *             <li>双方asKey=true：全部元素深度比较</li>
 *             <li>任意一方asKey=false：仅比较第一个元素</li>
 *         </ul>
 *     </li>
 *     <li>extend/sub：返回新Tuple实例，默认继承原实例 immutable / asKey 属性；原对象不会被修改</li>
 *     <li>实现Serializable：支持Java序列化；实现Comparable预留元组比较能力</li>
 *     <li>hashCode无缓存，每次调用实时计算；可变元组修改元素后hash会自动变化</li>
 * </ul>
 *
 * <p><b>⚠️重要风险警告：</b>
 * <ol>
 *     <li>工厂方法 <code>of / with / ofKey / withKey</code> <b>直接持有传入数组引用</b>，外部修改原数组会篡改元组内部状态！</li>
 *     <li>可变元组不要放入HashMap/HashSet作为key；set修改元素后hashCode发生变化，会彻底破坏哈希集合</li>
 *     <li>equals不是标准全元素相等，受asKey标记控制；如需完整深度相等请使用{@link #deepEquals(Tuple)}</li>
 *     <li>compareTo当前未实现，现在永远返回0，排序行为是错误的</li>
 * </ol>
 *
 * @author Terraqo
 */
public class Tuple implements Iterable<Object>, Comparable<Tuple>, Serializable, Cloneable {

    /**
     * 序列化版本号，{@link Serial}注解标识序列化版本字段，用于对象序列化/反序列化版本校验。
     * <p>继承Tuple的序列化能力，保证Pair对象序列化兼容性。</p>
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * true = 不可变元组，禁止set修改元素；false = 可变元组，允许set修改元素
     * <br>无论可变/不可变，元组数组长度固定，不能增删元素
     */
    protected final boolean immutable;

    /**
     * asKey模式开关，控制equals、hashCode计算策略
     * <ul>
     * <li>true：equals比较全部元素；hashCode聚合全部元素哈希</li>
     * <li>false：equals仅比较第一个元素；hashCode只取第一个元素哈希</li>
     * </ul>
     */
    protected final boolean asKey;

    /**
     * 元组底层存储数组。
     * <p>⚠️当前工厂直接复用外部传入数组，外部修改数组会直接改变Tuple内部；构造传入拷贝数组才能隔离。
     */
    protected final Object[] elements;

    /**
     * 受保护构造器，仅内部/子类调用，外部请使用静态工厂方法。
     *
     * @param immutable 是否不可变
     * @param asKey     是否开启asKey键模式
     * @param elements  元素数组，禁止传入null；注意：不会自动拷贝，调用方需要保证传入拷贝后的安全数组
     * @throws NullPointerException elements为null抛出
     */
    protected Tuple(boolean immutable, boolean asKey, Object... elements) {
        Objects.requireNonNull(elements, "elements array cannot be null");
        this.immutable = immutable;
        this.asKey = asKey;
        this.elements = elements;
    }

    /**
     * 根据输入数组生成只读迭代器。
     * <p>⚠️迭代器直接持有源数组引用；外部修改源数组，迭代结果会随之变化。
     *
     * @param elements 源数组
     * @param <E>      元素类型
     * @return 不支持remove操作的迭代器
     */
    @SafeVarargs
    public static <E> Iterator<E> iterator(E... elements) {
        return new Iter<>(elements);
    }

    /**
     * 创建【不可变普通元组】asKey=false。
     * <p>⚠️注意：直接复用传入的values数组，外部修改原数组会改变元组内部数据！
     *
     * @param values 元组元素
     * @return 不可变Tuple实例，调用set抛异常
     */
    public static Tuple with(Object... values) {
        return new Tuple(true, false, values);
    }

    /**
     * 创建【不可变键模式元组】asKey=true，equals/hashCode使用全部元素参与计算。
     * <p>⚠️注意：直接复用传入的values数组，外部修改原数组会改变元组内部数据！
     *
     * @param values 元组元素
     * @return 不可变Tuple实例
     */
    public static Tuple withKey(Object... values) {
        return new Tuple(true, true, values);
    }

    /**
     * 创建【可变普通元组】asKey=false。
     * <p>⚠️注意：直接复用传入的values数组，外部修改原数组会改变元组内部数据！
     *
     * @param values 元组元素
     * @return 可变Tuple实例，可以set修改元素
     */
    public static Tuple of(Object... values) {
        return new Tuple(false, false, values);
    }

    /**
     * 创建【可变键模式元组】asKey=true。
     * <p>⚠️注意：直接复用传入的values数组，外部修改原数组会改变元组内部数据！
     * <p>强烈不建议把可变asKey元组放入HashMap/HashSet。
     *
     * @param values 元组元素
     * @return 可变Tuple实例
     */
    public static Tuple ofKey(Object... values) {
        return new Tuple(false, true, values);
    }

    /**
     * 判断元组是否为空。
     *
     * @return true空元组；false存在元素
     */
    public boolean isEmpty() {
        return elements.length == 0;
    }

    /**
     * 获取元组元素总数量。
     *
     * @return 底层数组长度；0代表空元组
     */
    public int size() {
        return elements.length;
    }

    /**
     * 查找目标元素第一次出现的下标，从索引0开始向后搜索，使用深度相等{@link Objects#deepEquals(Object, Object)}。
     *
     * @param value 待查找对象，可以为null
     * @return 元素下标；找不到返回-1
     */
    public int at(Object value) {
        return this.at(value, 0);
    }

    /**
     * 从指定起始偏移位置向后搜索目标元素，深度相等匹配。
     *
     * @param value 待查找对象，可以为null
     * @param index 搜索起始下标（原生下标，非环形索引）
     * @return 匹配元素下标；找不到返回-1
     */
    public int at(Object value, int index) {
        for (int i = index; i < elements.length; i++) {
            if (Objects.deepEquals(elements[i], value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 判断元组是否包含目标元素，深度相等比较。
     *
     * @param value 待查找对象，可以为null
     * @return true存在；false不存在
     */
    public boolean has(Object value) {
        for (Object element : elements) {
            if (Objects.deepEquals(element, value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 环形索引获取元素；支持负数、超过size的索引。
     *
     * @param index 环形任意整数索引
     * @return 对应位置元素，可以返回null
     * @throws IndexOutOfBoundsException 空元组调用
     */
    public Object get(int index) {
        return elements[at(index)];
    }

    /**
     * 环形索引修改元素；仅可变元组允许调用。
     *
     * @param index 环形索引
     * @param value 设置的新值，可以null
     * @return 被替换的旧元素
     * @throws UnsupportedOperationException 不可变元组调用
     * @throws IndexOutOfBoundsException     空元组调用
     */
    public Object set(int index, Object value) {
        if (immutable) {
            throw new UnsupportedOperationException("Tuple is immutable, cannot set element");
        }
        int idx = at(index);
        Object old = elements[idx];
        elements[idx] = value;
        return old;
    }

    /**
     * 拼接另一个Tuple，返回新Tuple实例，继承当前immutable、asKey属性；原实例不修改。
     *
     * @param tuple 待拼接元组；null视为空元组
     * @return 新Tuple实例
     */
    public Tuple extend(Tuple tuple) {
        if (tuple == null) {
            return extend();
        }
        return extend(tuple.elements);
    }

    /**
     * 追加一组元素，返回新Tuple实例，继承当前immutable、asKey属性；原实例不修改。
     *
     * @param values 需要追加的元素
     * @return 新Tuple实例
     */
    public Tuple extend(Object... values) {
        if (values == null || values.length == 0) {
            // 空追加：拷贝当前数组返回新实例，切断数组引用共享
            return new Tuple(immutable, asKey, Arrays.copyOf(elements, elements.length));
        }
        int newLen = elements.length + values.length;
        Object[] newArr = Arrays.copyOf(elements, newLen);
        System.arraycopy(values, 0, newArr, elements.length, values.length);
        return new Tuple(immutable, asKey, newArr);
    }

    /**
     * 截取子元组；start、end做环形索引规范化；返回新Tuple继承原实例immutable/asKey。
     * <p>支持环形切片：例如size=4 start=3 end=1，会拼接末尾段+头部段。
     *
     * @param start 起始环形索引（包含）
     * @param end   结束环形索引（不包含）
     * @return 子元组实例
     * @throws IndexOutOfBoundsException 当前元组为空
     */
    public Tuple sub(int start, int end) {
        int s = at(start);
        int e = at(end);
        Object[] slice;
        if (s <= e) {
            slice = Arrays.copyOfRange(elements, s, e);
        } else {
            Object[] part1 = Arrays.copyOfRange(elements, s, elements.length);
            Object[] part2 = Arrays.copyOfRange(elements, 0, e);
            slice = new Object[part1.length + part2.length];
            System.arraycopy(part1, 0, slice, 0, part1.length);
            System.arraycopy(part2, 0, slice, part1.length, part2.length);
        }
        return new Tuple(immutable, asKey, slice);
    }

    /**
     * 获取元素顺序Stream流。
     *
     * @return Stream<Object>
     */
    public Stream<Object> stream() {
        return Arrays.stream(elements);
    }

    /**
     * 转换为ArrayList，返回全新List集合。
     * 修改List不会影响元组；修改元组也不会影响已生成List。
     *
     * @return ArrayList包含全部元素
     */
    public List<Object> toList() {
        return new ArrayList<>(Arrays.asList(elements));
    }

    /**
     * 返回拷贝后的全新Object数组，修改返回数组不影响元组。
     *
     * @return Object[]
     */
    public Object[] toArray() {
        return Arrays.copyOf(elements, elements.length);
    }

    /**
     * 将元组元素复制到目标数组；行为参考Collection.toArray。
     *
     * @param array 目标填充数组，不能null
     * @return 填充后的数组；容量不足会新建数组
     * @throws NullPointerException array为null抛出
     */
    public Object[] toArray(Object[] array) {
        Objects.requireNonNull(array);
        final int len = elements.length;
        if (array.length < len) {
            array = Arrays.copyOf(array, len);
        }
        System.arraycopy(elements, 0, array, 0, len);
        return array;
    }

    /**
     * 标准深度全量比较，忽略asKey标记，逐个元素深度对比。
     * <p>不受asKey影响，两个元组全部元素深度相等返回true。
     *
     * @param other 对比元组
     * @return true全部元素深度相等
     */
    public boolean deepEquals(Tuple other) {
        if (other == null) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (elements.length != other.elements.length) {
            return false;
        }
        for (int i = 0; i < elements.length; i++) {
            if (!Objects.deepEquals(elements[i], other.elements[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回元组迭代器，迭代器不支持remove操作。
     *
     * @return Iterator<Object>
     */
    @Override
    public Iterator<Object> iterator() {
        return Tuple.iterator(elements);
    }

    /**
     * 元组比较器；当前未实现，占位实现。
     * <p>⚠️现在永远返回0，会导致TreeSet/TreeMap排序完全错乱！
     *
     * @param o 待比较元组
     * @return 比较结果
     */
    @Override
    public int compareTo(Tuple o) {
        if (o == null) {
            throw new NullPointerException("Cannot compare to null Tuple");
        }

        // 两个空元组相等
        if (this.isEmpty() && o.isEmpty()) {
            return 0;
        }
        // 一方为空
        if (this.isEmpty()) return 1;
        if (o.isEmpty()) return -1;

        boolean thisAsKey = this.asKey;
        boolean otherAsKey = o.asKey;

        if (thisAsKey && otherAsKey) {
            // 双方asKey=true：完整全部元素比较
            int lenCmp = Integer.compare(this.size(), o.size());
            if (lenCmp != 0) {
                return lenCmp;
            }
            for (int i = 0; i < elements.length; i++) {
                Object a = elements[i];
                Object b = o.elements[i];
                int r = compareSingleElement(a, b);
                if (r == 0) {
                    continue;
                }
                return r;
            }
            return 0;
        } else if (thisAsKey) {
            // 本方asKey=true，对方asKey=false；equals要求本方size必须等于1，否则equals=false
            if (this.size() != 1) {
                // equals返回false；compareTo返回非0，这里约定：大于
                return 1;
            }
            // 本方size=1，对比第一个元素
            Object a = this.elements[0];
            Object b = o.elements[0];
            return compareSingleElement(a, b);
        } else if (otherAsKey) {
            // 本方asKey=false，对方asKey=true；equals要求对方size必须等于1，否则equals=false
            if (o.size() != 1) {
                return 1;
            }
            Object a = this.elements[0];
            Object b = o.elements[0];
            return compareSingleElement(a, b);
        } else {
            // 双方都不是asKey：只比较第一个元素
            Object a = this.elements[0];
            Object b = o.elements[0];
            return compareSingleElement(a, b);
        }
    }

    /**
     * 辅助方法：比较单个元素，处理null、Comparable校验
     */
    @SuppressWarnings("unchecked")
    private int compareSingleElement(Object a, Object b) {
        if (Objects.deepEquals(a, b)) {
            return 0;
        }
        if (a == null) return -1;
        if (b == null) return 1;
        if (!(a instanceof Comparable<?> ca)) {
            throw new IllegalArgumentException("Element not Comparable: " + a.getClass().getName());
        }
        return ((Comparable<Object>) ca).compareTo(b);
    }

    /**
     * equals逻辑受asKey标记控制；空元组与空元组判定相等。
     * <p>⚠️不是标准全部元素比较；如需完整深度相等请调用 {@link #deepEquals(Tuple)}。
     *
     * @param o 对比对象
     * @return 是否相等
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tuple other)) {
            return false;
        }
        // 两个空元组直接相等
        if (elements.length == 0 && other.elements.length == 0) {
            return true;
        }
        if (asKey && other.asKey) {
            if (elements.length != other.elements.length) {
                return false;
            }
            for (int i = 0; i < elements.length; i++) {
                if (!Objects.deepEquals(elements[i], other.elements[i])) {
                    return false;
                }
            }
            return true;
        } else if (asKey) {
            if (elements.length == 1 && other.elements.length != 0) {
                return Objects.deepEquals(elements[0], other.elements[0]);
            }
            return false;
        } else if (other.asKey) {
            if (elements.length != 0 && other.elements.length == 1) {
                return Objects.deepEquals(elements[0], other.elements[0]);
            }
            return false;
        } else {
            if (elements.length == 0 || other.elements.length == 0) {
                return false;
            }
            return Objects.deepEquals(elements[0], other.elements[0]);
        }
    }

    /**
     * 计算哈希码，受asKey控制；每次调用实时计算，无缓存。
     * <ul>
     * <li>asKey=true：全部元素参与hash计算</li>
     * <li>asKey=false：只取第一个元素参与hash计算</li>
     * </ul>
     *
     * @return int哈希值
     */
    @Override
    public final int hashCode() {
        int h = 0;
        if (elements.length != 0) {
            h = 1;
            int len = asKey ? elements.length : 1;
            for (int i = 0; i < len; i++) {
                h = 31 * h + Objects.hashCode(elements[i]);
            }
        }
        return h;
    }

    /**
     * 格式化输出元组字符串。
     * <ul>
     * <li>asKey=true：格式 (k0, k1, k2)，全部元素作为key</li>
     * <li>asKey=false：格式 (key)=[val1, val2...]，第一个元素为key，其余元素为value列表</li>
     * <li>空元组asKey=true输出 {@code ()}；空元组asKey=false输出 {@code ()=[]}</li>
     * </ul>
     *
     * @return 格式化字符串
     */
    @Override
    public String toString() {
        final int len = elements.length;
        if (asKey) {
            // asKey模式：全部元素放入 ()
            if (len == 0) {
                return "()";
            }
            StringBuilder sb = new StringBuilder(len * 16);
            sb.append('(');
            for (int i = 0; i < len; i++) {
                sb.append(elements[i]);
                if (i != len - 1) {
                    sb.append(", ");
                }
            }
            sb.append(')');
            return sb.toString();
        } else {
            // 普通模式 (key)=[value...]
            StringBuilder sb = new StringBuilder(len * 20);
            // key部分：第一个元素
            sb.append('(');
            if (len >= 1) {
                sb.append(elements[0]);
            }
            sb.append(')');
            sb.append('=');
            // value部分：下标1之后所有元素
            sb.append('[');
            for (int i = 1; i < len; i++) {
                sb.append(elements[i]);
                if (i != len - 1) {
                    sb.append(", ");
                }
            }
            sb.append(']');
            return sb.toString();
        }
    }

    /**
     * 环形索引规范化；将任意整数index映射到[0,size‑1]有效下标。
     *
     * @param index 原始索引，可以负数、大于size
     * @return 合法数组下标
     * @throws IndexOutOfBoundsException 元组为空
     */
    private int at(int index) {
        final int len = elements.length;
        if (len == 0) {
            throw new IndexOutOfBoundsException("Tuple is empty, cannot access index");
        }
        int mod = index % len;
        if (mod < 0) {
            mod += len;
        }
        return mod;
    }

    /**
     * 克隆元组实例；底层elements数组独立拷贝，原对象与克隆对象数据隔离。
     * <p>克隆继承原实例 immutable、asKey 属性。
     * <ul>
     *     <li>可变元组clone得到可变元组，修改clone不会影响原对象</li>
     *     <li>不可变元组clone得到不可变元组</li>
     * </ul>
     *
     * @return 全新Tuple克隆实例
     */
    @Override
    public Tuple clone() {
        // 直接调用受保护构造器，传入拷贝后的数组，规避final字段无法重新赋值问题
        Object[] copiedElements = Arrays.copyOf(this.elements, this.elements.length);
        return new Tuple(this.immutable, this.asKey, copiedElements);
    }

    /**
     * Iter 是一个泛型迭代器类，用于遍历数组中的元素
     * 实现了 Iterator 接口，提供标准的迭代器方法
     *
     * @param <E> 迭代器元素的类型
     */
    private static final class Iter<E> implements Iterator<E> {
        /**
         * 存储要迭代的数组元素
         */
        private final E[] elements;
        /**
         * 当前迭代的位置索引
         * 初始值为0，每次调用next()方法后递增
         */
        private int index = 0;

        /**
         * 构造函数，使用给定的数组创建迭代器
         *
         * @param elements 要迭代的数组，不能为null
         * @throws NullPointerException 如果elements参数为null则抛出异常
         */
        private Iter(E[] elements) {
            this.elements = Objects.requireNonNull(elements, "The elements array must not be null");
        }

        /**
         * 检查是否还有下一个元素
         *
         * @return 如果还有下一个元素则返回 true，否则返回 false
         */
        @Override
        public boolean hasNext() {
            // 检查当前索引是否小于数组长度，如果是则表示还有下一个元素
            return index < elements.length;
        }

        /**
         * 获取下一个元素并移动迭代器位置
         *
         * @return 数组中的下一个元素
         * @throws NoSuchElementException 如果没有更多元素时调用此方法
         */
        @Override
        public E next() {
            // 首先检查是否有下一个元素，如果没有则抛出异常
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            // 返回当前位置的元素并将索引递增
            return elements[index++];
        }

        /**
         * 移除当前元素（不支持的操作）
         *
         * @throws UnsupportedOperationException 始终抛出此异常，因为ArrayIterator不支持移除操作
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove operation is not supported by ArrayIterator");
        }
    }
}
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

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;

/**
 * 原始类型容器的公共基类，提供扰动哈希、容量管理、扩容调度、快速失败校验与迭代器骨架。
 * <p>
 * 容量恒为 2 的幂，下标可用 {@code value & mask} 代替取模；当元素数量达到
 * {@code capacity * numerator / denominator} 时触发扩容。子类实现 {@link #onResize(int)}
 * 迁移数据，并通过 {@link #markModification()} 与迭代器配合实现快速失败。
 *
 * @author ApexSX
 */
public abstract class BaseContainer {
    /**
     * 容量上限，保证 {@code capacity << 1} 不会溢出为负数。
     */
    private static final int MAX_CAPACITY = 1 << 30;

    /**
     * 返回 {@code int} 值的扰动哈希，供开放寻址计算起始槽位。
     *
     * @param input 输入值
     * @return 扰动后的哈希
     */
    public static int hash(int input) {
        return hashcode(Integer.hashCode(input));
    }

    /**
     * 返回 {@code long} 值的扰动哈希，供开放寻址计算起始槽位。
     *
     * @param input 输入值
     * @return 扰动后的哈希
     */
    public static int hash(long input) {
        return hashcode(Long.hashCode(input));
    }

    /**
     * 返回 {@code float} 值的扰动哈希，供开放寻址计算起始槽位。
     *
     * @param input 输入值
     * @return 扰动后的哈希
     */
    public static int hash(float input) {
        return hashcode(Float.hashCode(input));
    }

    /**
     * 返回 {@code double} 值的扰动哈希，供开放寻址计算起始槽位。
     *
     * @param input 输入值
     * @return 扰动后的哈希
     */
    public static int hash(double input) {
        return hashcode(Double.hashCode(input));
    }

    /**
     * 混合原始哈希的高位与低位信息，使低位分布更均匀，降低开放寻址冲突概率。
     *
     * @param input 原始哈希值
     * @return 扰动后的哈希值
     */
    public static int hashcode(int input) {
        int h = input;
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }

    /**
     * 装载因子的分子。
     */
    private final int numeratorFactor;
    /**
     * 装载因子的分母。
     */
    private final int denominatorFactor;
    /**
     * 当前容量，始终为 2 的幂。
     */
    private int capacity;
    /**
     * 索引掩码，等于 {@code capacity - 1}。
     */
    private int mask;
    /**
     * 扩容阈值，等于 {@code capacity * numeratorFactor / denominatorFactor}。
     */
    private int limit;
    /**
     * 当前元素数量。
     */
    private int size;
    /**
     * 修改计数器，用于迭代器快速失败。
     */
    private int modificationMark;
    /**
     * 是否单独存储了空槽元素（该元素不在内部数组中）。
     */
    private boolean emptyElementPresent;

    /**
     * 构造集合基类，装载因子会归一化为不大于 1 的分数。
     *
     * @param initialCapacity   初始容量
     * @param numeratorFactor   装载因子分子
     * @param denominatorFactor 装载因子分母
     */
    public BaseContainer(int initialCapacity, int numeratorFactor, int denominatorFactor) {
        numeratorFactor = Math.max(1, numeratorFactor);
        denominatorFactor = Math.max(1, denominatorFactor);
        this.numeratorFactor = Math.min(numeratorFactor, denominatorFactor);
        this.denominatorFactor = Math.max(numeratorFactor, denominatorFactor);
        this.init(initialCapacity);
    }

    /**
     * 初始化容量、掩码、阈值与元素数量；不重置修改计数，避免扩容破坏遍历快速失败语义。
     *
     * @param initialCapacity 初始容量
     * @return 初始化后的容量
     */
    protected final int init(int initialCapacity) {
        int normalized = Math.min(Math.max(4, initialCapacity), MAX_CAPACITY);
        this.capacity = Integer.highestOneBit(normalized - 1) << 1;
        this.mask = this.capacity - 1;
        this.limit = this.capacity * this.numeratorFactor / this.denominatorFactor;
        this.size = 0;
        return this.capacity;
    }

    /**
     * 扩容为当前容量的两倍。
     * <p>
     * 更新容量、掩码与阈值至新容量对应的值，元素数量保持不变。
     *
     * @return 扩容后的新容量
     */
    protected int growCapacity() {
        if (this.capacity >= MAX_CAPACITY) {
            throw new IllegalStateException("Container capacity overflow");
        }
        int oldSize = this.size;
        int newCapacity = this.init(this.capacity << 1);
        this.size = oldSize;
        return newCapacity;
    }

    /**
     * 创建长度为当前容量的对象数组。
     *
     * @return 长度为当前容量的对象数组
     */
    protected final Object[] newObjectArray() {
        return new Object[capacity()];
    }

    /**
     * 创建长度为当前容量的 int 数组，用于扩容后重建内部存储。
     *
     * @return 长度为当前容量的 int 数组
     */
    protected final int[] newIntArray() {
        return new int[capacity()];
    }

    /**
     * 创建长度为当前容量的 long 数组，用于扩容后重建内部存储。
     *
     * @return 长度为当前容量的 long 数组
     */
    protected final long[] newLongArray() {
        return new long[capacity()];
    }

    /**
     * 创建长度为当前容量的 float 数组，用于扩容后重建内部存储。
     *
     * @return 长度为当前容量的 float 数组
     */
    protected final float[] newFloatArray() {
        return new float[capacity()];
    }

    /**
     * 创建长度为当前容量的 double 数组，用于扩容后重建内部存储。
     *
     * @return 长度为当前容量的 double 数组
     */
    protected final double[] newDoubleArray() {
        return new double[capacity()];
    }

    /**
     * 用掩码把任意整数折算到 {@code [0, capacity)}。
     * <p>
     * 容量恒为 2 的幂，因此 {@code value & mask} 等价于对容量取模，且对负数同样有效；
     * 哈希寻址与循环队列的下标回绕均复用此方法。
     *
     * @param value 待折算的整数，可为负数或超出容量
     * @return 位于 {@code [0, capacity)} 内的下标
     */
    protected final int maskIndex(int value) {
        return value & mask;
    }

    /**
     * 返回线性探测的下一个槽位，到达表尾时回绕到表头。
     *
     * @param index 当前槽位下标（应位于 {@code [0, capacity)} 内）
     * @return 后一个槽位下标
     */
    protected final int nextIndex(int index) {
        return maskIndex(index + 1);
    }

    /**
     * 返回线性探测的前一个槽位，到达表头时回绕到表尾。
     *
     * @param index 当前槽位下标（应位于 {@code [0, capacity)} 内）
     * @return 前一个槽位下标
     */
    protected final int prevIndex(int index) {
        return maskIndex(index - 1);
    }

    /**
     * 返回 {@code int} 键的起始槽位，探测链从此处开始。
     *
     * @param key 键
     * @return 位于 {@code [0, capacity)} 内的起始槽位
     */
    protected final int slotOf(int key) {
        return maskIndex(hash(key));
    }

    /**
     * 返回 {@code long} 键的起始槽位，探测链从此处开始。
     *
     * @param key 键
     * @return 位于 {@code [0, capacity)} 内的起始槽位
     */
    protected final int slotOf(long key) {
        return maskIndex(hash(key));
    }

    /**
     * 返回 {@code double} 键的起始槽位，探测链从此处开始。
     *
     * @param key 键
     * @return 位于 {@code [0, capacity)} 内的起始槽位
     */
    protected final int slotOf(double key) {
        return maskIndex(hash(key));
    }

    /**
     * 设置当前元素数量，供扩容后恢复或重建索引时调用。
     *
     * @param size 新的元素数量
     */
    protected final void size(int size) {
        if (size < 0 || size > this.capacity) {
            throw new IndexOutOfBoundsException("Size: " + size + ", capacity: " + this.capacity);
        }
        this.size = size;
    }

    /**
     * 元素数量加一。
     *
     * @return 更新后的元素数量
     */
    protected final int increaseSize() {
        return ++this.size;
    }

    /**
     * 元素数量减一。
     *
     * @return 更新后的元素数量
     */
    protected final int decreaseSize() {
        return --this.size;
    }

    /**
     * 判断当前元素数量是否达到扩容阈值。
     *
     * @return 达到或超过阈值返回 {@code true}
     */
    protected final boolean isOverThreshold() {
        return size >= limit;
    }

    /**
     * 判断再写入指定数量的元素后是否会超过扩容阈值。
     * <p>
     * 与 {@link #isOverThreshold()} 不同，本方法用于批量写入前的容量预检，
     * 允许写入后元素数量恰好等于阈值。
     *
     * @param count 本次计划写入的元素数量
     * @return 超过阈值返回 {@code true}
     */
    protected final boolean isOverThreshold(int count) {
        return size + count > limit;
    }

    /**
     * 判断容器是否单独存储了空槽元素。
     *
     * @return 存在单独的空槽元素返回 {@code true}
     */
    protected final boolean isEmptyElementPresent() {
        return emptyElementPresent;
    }

    /**
     * 设置容器是否单独存储了空槽元素。
     *
     * @param emptyElementPresent {@code true} 表示存在单独的空槽元素
     */
    protected final void setEmptyElementPresent(boolean emptyElementPresent) {
        this.emptyElementPresent = emptyElementPresent;
    }

    /**
     * 检查下标是否位于有效元素范围 {@code [0, size())} 内。
     *
     * @param index 待检查的下标
     * @throws IndexOutOfBoundsException 下标越界时抛出
     */
    protected final void verifyElementIndex(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size());
        }
    }

    /**
     * 检查修改次数是否与预期一致，用于遍历期间快速失败。
     *
     * @param expectedModification 遍历开始时记录的修改次数
     * @throws ConcurrentModificationException 修改次数与预期不一致时抛出
     */
    protected final void verifyModification(int expectedModification) {
        if (modificationMark != expectedModification) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * 获取当前修改次数。
     *
     * @return 当前修改次数
     */
    protected final int modificationMark() {
        return modificationMark;
    }

    /**
     * 修改次数加一，用于标记集合结构发生变化。
     */
    protected final void markModification() {
        modificationMark++;
    }

    /**
     * 按需扩容：元素数量达到阈值时翻倍容量，并回调 {@link #onResize(int)} 迁移数据。
     * 元素数量在扩容前后保持一致；子类应根据写入目标是否依赖旧容量，选择在写入前或写入后调用。
     */
    protected final void ensureCapacity() {
        if (isOverThreshold()) {
            onResize(growCapacity());
        }
    }

    /**
     * 为批量写入预扩容：容量不足时一次扩容到位，并回调 {@link #onResize(int)} 迁移数据。
     * 仅在发生扩容时才回调，避免无意义的整表复制。
     *
     * @param count 本次计划写入的元素数量，非正数时直接返回
     */
    protected final void ensureCapacityFor(int count) {
        if (count <= 0) {
            return;
        }
        if (isOverThreshold(count)) {
            do {
                growCapacity();
            } while (isOverThreshold(count));
            onResize(capacity());
        }
    }

    /**
     * 扩容后重建内部存储。
     * 调用时容量、掩码与阈值已更新为 {@code newCapacity} 对应的值，且 {@link #size()}
     * 仍为扩容前的元素数量；子类应通过 {@link #newIntArray()} 等工厂方法创建新数组并迁移旧数据。
     *
     * @param newCapacity 扩容后的新容量
     */
    protected abstract void onResize(int newCapacity);

    /**
     * 清空集合，仅重置元素数量而保留容量。
     */
    public void clear() {
        this.size = 0;
    }

    /**
     * 判断集合是否为空。
     *
     * @return 元素数量为 0 返回 {@code true}
     */
    public final boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * 判断集合是否非空。
     *
     * @return 元素数量不为 0 返回 {@code true}
     */
    public final boolean isNotEmpty() {
        return this.size != 0;
    }

    /**
     * 获取当前容量。
     *
     * @return 当前容量
     */
    public final int capacity() {
        return this.capacity;
    }

    /**
     * 获取当前元素数量。
     *
     * @return 当前元素数量
     */
    public final int size() {
        return this.size;
    }

    /**
     * 原始类型迭代器公共基类，封装修改计数快照、快速失败校验、空槽元素优先返回与游标推进。
     * <p>
     * 支持两种遍历模式：
     * <ul>
     *     <li><b>空槽标记模式</b>（{@code asMarker=true}）：传入空槽标记值，扫描整个数组并跳过
     *         等于标记的位置；若容器单独存储了空槽元素，则优先返回该元素。</li>
     *     <li><b>定长模式</b>（{@code asMarker=false}）：传入元素数量，按下标顺序遍历
     *         {@code [0, elementCount)}。</li>
     * </ul>
     * 子类持有源数组与空槽标记，实现 {@link #sourceLength()} 和 {@link #isEmptySlot(int)} 提供定位能力；
     * 迭代值默认取自源数组，若与定位数组不同（例如映射的值迭代器）可重写 {@code valueAt}。
     *
     * @author ApexSX
     */
    protected abstract static class AbstractPrimitiveIterator {
        /**
         * 所属容器，用于校验修改计数。
         */
        private final BaseContainer container;
        /**
         * 定长模式的元素数量，负数表示使用空槽标记模式。
         */
        private final int elementCount;
        /**
         * 创建迭代器时的修改计数快照。
         */
        private final int expectedModification;
        /**
         * 下一个待检查的数组下标。
         */
        private int cursor;
        /**
         * 是否还有单独存储的空槽元素待返回。
         */
        private boolean emptyPending;

        /**
         * 创建迭代器。
         *
         * @param container 所属容器，用于校验修改计数
         * @param input     {@code asMarker} 为 {@code false} 时表示待遍历的元素数量，
         *                  为 {@code true} 时由子类按自身类型解释为空槽标记值
         * @param asMarker  {@code true} 使用空槽标记模式，{@code false} 使用定长模式；
         *                  是否优先返回单独存储的空槽元素由容器的 {@link BaseContainer#isEmptyElementPresent()} 决定
         */
        protected AbstractPrimitiveIterator(BaseContainer container, int input, boolean asMarker) {
            if (!asMarker && input < 0) {
                throw new IllegalArgumentException("elementCount: " + input);
            }
            this.container = container;
            this.elementCount = asMarker ? -1 : input;
            this.expectedModification = container.modificationMark();
            this.emptyPending = container.isEmptyElementPresent();
        }

        /**
         * 判断是否存在下一个元素。
         *
         * @return 存在下一个元素返回 {@code true}
         */
        public final boolean hasNext() {
            container.verifyModification(expectedModification);
            return emptyPending || nextIndex() >= 0;
        }

        /**
         * 消耗单独存储的空槽元素（若存在），同时完成快速失败校验。
         * <p>
         * 子类 {@code nextX()} 应优先调用此方法，返回 {@code true} 时直接返回子类各自类型的空槽元素值。
         *
         * @return 消耗了空槽元素返回 {@code true}
         */
        protected final boolean consumeEmptyElement() {
            container.verifyModification(expectedModification);
            if (emptyPending) {
                emptyPending = false;
                return true;
            }
            return false;
        }

        /**
         * 查找下一个有效下标并推进游标，无有效元素时抛出 {@link NoSuchElementException}。
         *
         * @return 有效元素下标
         */
        protected final int advance() {
            container.verifyModification(expectedModification);
            int index = nextIndex();
            if (index < 0) {
                throw new NoSuchElementException();
            }
            cursor = index + 1;
            return index;
        }

        /**
         * 查找从当前游标开始的下一个有效下标，不修改游标。
         * <p>
         * 定长模式直接返回游标（未越界时）；空槽标记模式跳过由子类判定的空槽位。
         * 由于 {@link #hasNext()} 与 {@link #advance()} 都可能调用此方法，实现必须保持无副作用。
         *
         * @return 有效下标，遍历结束时返回 {@code -1}
         */
        private int nextIndex() {
            if (elementCount >= 0) {
                return cursor < elementCount ? cursor : -1;
            }
            int index = cursor;
            while (index < sourceLength() && isEmptySlot(index)) {
                index++;
            }
            return index < sourceLength() ? index : -1;
        }

        /**
         * 返回用于定位的源数组长度。
         *
         * @return 源数组长度
         */
        protected abstract int sourceLength();

        /**
         * 判断指定下标是否为需跳过的空槽。
         *
         * @param index 数组下标
         * @return 是空槽返回 {@code true}
         */
        protected abstract boolean isEmptySlot(int index);
    }

    /**
     * 无装箱 int 迭代器，以 {@code int[]} 为定位数组，骨架见 {@link AbstractPrimitiveIterator}。
     *
     * @author ApexSX
     */
    protected static class OfInt extends AbstractPrimitiveIterator implements PrimitiveIterator.OfInt {
        /**
         * 用于定位有效下标的数组。
         */
        private final int[] source;
        /**
         * 空槽标记值，仅空槽标记模式使用。
         */
        private final int emptyMarker;

        /**
         * @param container 所属容器，用于校验修改计数
         * @param source    用于定位有效下标的数组
         * @param input     {@code asMarker=true} 时为空槽标记值，否则为待遍历元素数量
         * @param asMarker  {@code true} 使用空槽标记模式，{@code false} 使用定长模式
         */
        protected OfInt(BaseContainer container, int[] source, int input, boolean asMarker) {
            super(container, input, asMarker);
            this.source = Objects.requireNonNull(source, "source");
            this.emptyMarker = asMarker ? input : 0;
        }

        @Override
        public final int nextInt() {
            if (consumeEmptyElement()) {
                return emptyValue();
            }
            return valueAt(advance());
        }

        @Override
        protected final int sourceLength() {
            return source.length;
        }

        @Override
        protected final boolean isEmptySlot(int index) {
            return source[index] == emptyMarker;
        }

        /**
         * 返回指定下标对应的迭代值，默认直接读取 {@link #source}。
         * <p>
         * 当迭代值与定位所用数组不同时，子类应重写此方法。
         *
         * @param index 有效元素下标
         * @return 该下标对应的值
         */
        protected int valueAt(int index) {
            return source[index];
        }

        /**
         * 返回单独存储的空槽元素对应的迭代值，默认返回 int 类型的空槽标记值本身。
         *
         * @return 空槽元素对应的值
         */
        protected int emptyValue() {
            return emptyMarker;
        }
    }

    /**
     * 无装箱 long 迭代器，以 {@code long[]} 为定位数组，骨架见 {@link AbstractPrimitiveIterator}。
     *
     * @author ApexSX
     */
    protected static class OfLong extends AbstractPrimitiveIterator implements PrimitiveIterator.OfLong {
        /**
         * 用于定位有效下标的数组。
         */
        private final long[] source;
        /**
         * 空槽标记值，仅空槽标记模式使用。
         */
        private final long emptyMarker;

        /**
         * @param container 所属容器，用于校验修改计数
         * @param source    用于定位有效下标的数组
         * @param input     {@code asMarker=true} 时为空槽标记值，否则为待遍历元素数量
         * @param asMarker  {@code true} 使用空槽标记模式，{@code false} 使用定长模式
         */
        protected OfLong(BaseContainer container, long[] source, int input, boolean asMarker) {
            super(container, input, asMarker);
            this.source = Objects.requireNonNull(source, "source");
            this.emptyMarker = asMarker ? input : 0;
        }

        @Override
        public final long nextLong() {
            if (consumeEmptyElement()) {
                return emptyValue();
            }
            return valueAt(advance());
        }

        @Override
        protected final int sourceLength() {
            return source.length;
        }

        @Override
        protected final boolean isEmptySlot(int index) {
            return source[index] == emptyMarker;
        }

        /**
         * 返回指定下标对应的迭代值，默认直接读取 {@link #source}。
         * <p>
         * 当迭代值与定位所用数组不同时，子类应重写此方法。
         *
         * @param index 有效元素下标
         * @return 该下标对应的值
         */
        protected long valueAt(int index) {
            return source[index];
        }

        /**
         * 返回单独存储的空槽元素对应的迭代值，默认返回 long 类型的空槽标记值本身。
         *
         * @return 空槽元素对应的值
         */
        protected long emptyValue() {
            return emptyMarker;
        }
    }

    /**
     * 无装箱 double 迭代器，以 {@code double[]} 为定位数组，骨架见 {@link AbstractPrimitiveIterator}。
     *
     * @author ApexSX
     */
    protected static class OfDouble extends AbstractPrimitiveIterator implements PrimitiveIterator.OfDouble {
        /**
         * 用于定位有效下标的数组。
         */
        private final double[] source;
        /**
         * 空槽标记值，仅空槽标记模式使用。
         */
        private final double emptyMarker;

        /**
         * @param container 所属容器，用于校验修改计数
         * @param source    用于定位有效下标的数组
         * @param input     {@code asMarker=true} 时为空槽标记值，否则为待遍历元素数量
         * @param asMarker  {@code true} 使用空槽标记模式，{@code false} 使用定长模式
         */
        protected OfDouble(BaseContainer container, double[] source, int input, boolean asMarker) {
            super(container, input, asMarker);
            this.source = Objects.requireNonNull(source, "source");
            this.emptyMarker = asMarker ? input : 0;
        }

        @Override
        public final double nextDouble() {
            if (consumeEmptyElement()) {
                return emptyValue();
            }
            return valueAt(advance());
        }

        @Override
        protected final int sourceLength() {
            return source.length;
        }

        @Override
        protected final boolean isEmptySlot(int index) {
            return source[index] == emptyMarker;
        }

        /**
         * 返回指定下标对应的迭代值，默认直接读取 {@link #source}。
         * <p>
         * 当迭代值与定位所用数组不同时，子类应重写此方法。
         *
         * @param index 有效元素下标
         * @return 该下标对应的值
         */
        protected double valueAt(int index) {
            return source[index];
        }

        /**
         * 返回单独存储的空槽元素对应的迭代值，默认返回 emptyMarker 本身。
         * 若需返回其他值，子类可重写此方法。
         *
         * @return 空槽元素对应的值
         */
        protected double emptyValue() {
            return emptyMarker;
        }
    }
}
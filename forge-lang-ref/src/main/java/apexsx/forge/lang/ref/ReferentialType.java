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
 * 引用类型：统一描述"以何种强度持有对象"以及"如何创建对应引用包装"。
 *
 * <p>本枚举既是 <b>类型描述</b>，也是 {@link Referential} 的 <b>工厂</b>：
 * 每个常量都实现 {@link #create(Object, int, ReferenceQueue)}，
 * 根据自身语义创建对应的包装实现类。
 * <pre>
 * 1. 如{@link java.lang.ref.SoftReference} 软引用，在GC报告内存不足时会被GC回收
 * 2. 如{@link java.lang.ref.WeakReference} 弱引用，在GC时发现弱引用会回收其对象
 * 3. 强引用，不会被自动回收，只有显式清除或失去所有强引用后才会释放
 * </pre>
 *
 * <p>包装内部会缓存创建时的哈希码：引用值被回收或清除后 {@code get()} 返回 {@code null}，
 * 此时若重新按值计算哈希码会发生变化，缓存能保证引用对象在哈希容器中的定位稳定。
 *
 * <p>创建引用时传入 {@link ReferenceQueue} 可把"引用值被回收"事件登记到队列，
 * 由专门的清理线程出队处理（参见 {@code ReferentialThreadLocal}）；
 * 传入 {@code null} 表示不关心回收通知。
 *
 * <p><b>注意：</b>三种包装的 {@code equals}/{@code hashCode} 均委托给被引用的值
 * （即按引用值判等），而非按包装对象身份判等；引用值被清除后该语义会退化，
 * 因此不宜在值已被回收后继续将其作为容器键使用。
 *
 * @author Terraqo
 * @see Referential
 */
// 统一屏蔽本文件的编译告警（内部包装类涉及泛型强制转换与 equals 重写）
@SuppressWarnings("ALL")
public enum ReferentialType {
    /**
     * 强引用：与普通对象引用等价，只要包装本身存活，被引用对象就不会被回收。
     * 强引用不参与引用队列通知，因此创建时会忽略传入的 {@code referenceQueue}。
     * 适用于"必须保证值一直存在"的场景；代价是无法感知也无法借助 GC 释放。
     */
    STRONG {
        /**
         * 创建强引用包装。
         *
         * @param referent       被引用的对象
         * @param hashCode       创建时缓存的哈希码
         * @param referenceQueue 引用队列；强引用不参与回收通知，此参数被忽略
         * @return 持有所需值的强引用包装
         */
        @Override
        public <T> Referential<T> create(T referent, int hashCode, ReferenceQueue<T> referenceQueue) {
            // 强引用无需引用队列，直接用值与哈希码构造
            return new StrongReference<>(referent, hashCode);
        }
    },
    /**
     * 软引用，在GC报告内存不足时会被GC回收。
     * 内存充足时保持存活，适合承载可重建、体积较大的缓存类数据。
     */
    SOFT {
        /**
         * 创建软引用包装，并可选地注册到引用队列。
         *
         * @param referent       被引用的对象
         * @param hashCode       创建时缓存的哈希码
         * @param referenceQueue 引用队列，可为 {@code null}；非空时引用被回收后会自动入队
         * @return 持有所需值的软引用包装
         */
        @Override
        public <T> Referential<T> create(T referent, int hashCode, ReferenceQueue<T> referenceQueue) {
            return new SoftReference<>(referent, hashCode, referenceQueue);
        }
    },
    /**
     * 弱引用，在GC时发现弱引用会回收其对象。
     * 只要发生可达性分析就会回收，不等待内存不足，适合"不阻止回收"的旁路数据。
     */
    WEAK {
        /**
         * 创建弱引用包装，并可选地注册到引用队列。
         *
         * @param referent       被引用的对象
         * @param hashCode       创建时缓存的哈希码
         * @param referenceQueue 引用队列，可为 {@code null}；非空时引用被回收后会自动入队
         * @return 持有所需值的弱引用包装
         */
        @Override
        public <T> Referential<T> create(T referent, int hashCode, ReferenceQueue<T> referenceQueue) {
            return new WeakReference<>(referent, hashCode, referenceQueue);
        }
    };

    /**
     * 使用默认哈希码创建引用对象（不注册引用队列）。
     * 等价于 {@code createWithDefaultHashCode(referent, null)}。
     *
     * @param referent 引用值
     * @param <T>      引用类型
     * @return 新创建的引用对象
     * @see #createWithDefaultHashCode(Object, ReferenceQueue)
     */
    public <T> Referential<T> createWithDefaultHashCode(T referent) {
        return this.createWithDefaultHashCode(referent, null);
    }

    /**
     * 使用默认哈希码（即 {@link Objects#hashCode(Object)}）和引用队列创建引用对象。
     *
     * <p>默认哈希码来自 {@code referent.hashCode()}，会对 {@code null} 做安全的 0 处理；
     * 若引用值重写了 {@code hashCode}，则此处使用的是重写后的值。
     * 需按对象身份而非业务值时，应改用 {@link #createWithIdentityHashCode(Object, ReferenceQueue)}。
     *
     * @param referent       引用值，可为 {@code null}
     * @param referenceQueue 引用队列，可以为null；非空时引用被回收后会入队
     * @param <T>            引用类型
     * @return 新创建的引用对象
     */
    public <T> Referential<T> createWithDefaultHashCode(T referent, ReferenceQueue<T> referenceQueue) {
        return this.create(referent, Objects.hashCode(referent), referenceQueue);
    }

    /**
     * 使用身份哈希码创建引用对象（不注册引用队列）。
     * 等价于 {@code createWithIdentityHashCode(referent, null)}。
     *
     * @param referent 引用值
     * @param <T>      引用类型
     * @return 新创建的引用对象
     * @see #createWithIdentityHashCode(Object, ReferenceQueue)
     */
    public <T> Referential<T> createWithIdentityHashCode(T referent) {
        return this.createWithIdentityHashCode(referent, null);
    }

    /**
     * 使用身份哈希码（{@link System#identityHashCode(Object)}）和引用队列创建引用对象。
     *
     * <p>身份哈希码不受引用值重写的 {@code hashCode} 影响，在对象生命周期内保持不变，
     * 适合以对象身份作为键的数据结构；存在理论上的哈希碰撞可能，
     * 因此比较时仍需配合 {@link Referential#equalsReference(Object)}。
     *
     * @param referent       引用值
     * @param referenceQueue 引用队列，可以为null；非空时引用被回收后会入队
     * @param <T>            引用类型
     * @return 新创建的引用对象
     */
    public <T> Referential<T> createWithIdentityHashCode(T referent, ReferenceQueue<T> referenceQueue) {
        return this.create(referent, System.identityHashCode(referent), referenceQueue);
    }

    /**
     * 使用指定的哈希码创建引用对象（不带引用队列）。
     * 等价于 {@code create(referent, hashCode, null)}，因此无法感知回收事件。
     *
     * <p>由调用方保证 {@code hashCode} 与引用值在存活期间的相等语义一致；
     * 该值会被包装缓存，供 {@link Referential#hashCodeReference()} 直接返回。
     *
     * @param referent 引用值
     * @param hashCode 引用值的哈希码
     * @param <T>      引用类型
     * @return 新创建的引用对象
     */
    public final <T> Referential<T> create(T referent, int hashCode) {
        return this.create(referent, hashCode, null);
    }

    /**
     * 使用指定的哈希码和引用队列创建引用对象。
     * 此方法为抽象方法，由每个枚举常量给出具体实现：
     * <ul>
     *   <li>{@link #STRONG} 返回持有强引用的实现，忽略引用队列；</li>
     *   <li>{@link #SOFT} 返回 {@link java.lang.ref.SoftReference} 的实现；</li>
     *   <li>{@link #WEAK} 返回 {@link java.lang.ref.WeakReference} 的实现。</li>
     * </ul>
     *
     * <p>本方法不做参数校验：允许 {@code referent} 与 {@code referenceQueue} 为 {@code null}，
     * 由具体实现与调用方约定语义。
     *
     * @param referent       引用值
     * @param hashCode       引用值的哈希码
     * @param referenceQueue 引用队列，可以为null；非空时引用被回收后会入队
     * @param <T>            引用类型
     * @return 新创建的引用对象
     */
    public abstract <T> Referential<T> create(T referent, int hashCode, ReferenceQueue<T> referenceQueue);

    /**
     * 强引用内部类：直接持有对象强引用，不继承 {@link java.lang.ref.Reference}。
     *
     * <p>只要该包装本身可达，被引用对象就不会被回收；仅当调用 {@link #clear()}
     * 或包装自身被回收时才会释放。类为包级可见，仅由 {@link #STRONG} 创建。
     *
     * @param <T> 引用值的类型
     */
    static class StrongReference<T> implements Referential<T> {
        /**
         * 被引用的对象；声明为 volatile，保证 {@link #clear()} 后对其他线程立即可见
         */
        volatile T referent;
        /**
         * 创建时缓存的哈希码；清除时置 0，避免残留值继续参与哈希计算
         */
        volatile int hashCode;

        /**
         * 构造强引用包装。
         *
         * @param referent 被引用的对象
         * @param hashCode 创建时缓存的哈希码
         */
        protected StrongReference(T referent, int hashCode) {
            this.referent = referent;
            this.hashCode = hashCode;
        }

        /**
         * 获取引用类型
         *
         * @return 引用类型，始终返回STRONG
         */
        @Override
        public ReferentialType type() {
            return STRONG;
        }

        /**
         * 获取引用的值
         *
         * @return 引用的值，如果已被清除则返回null
         */
        @Override
        public T get() {
            return referent;
        }

        /**
         * 清除引用，将引用值和哈希码置为默认值
         */
        @Override
        public void clear() {
            this.referent = null;
            this.hashCode = 0;
        }

        /**
         * 获取创建引用时缓存的哈希码，而非重新计算 {@code referent.hashCode()}。
         *
         * <p>引用值可能已被回收或已更换，重新计算会导致哈希值漂移，
         * 进而破坏其作为哈希容器键的定位；清除引用后该值被重置为 0。
         *
         * @return 缓存的哈希码
         */
        @Override
        public int hashCodeReference() {
            return hashCode;
        }

        /**
         * 比较对象是否相等，语义委托给 {@link Referential#equalsReference(Object)}：
         * 按被引用的值判等，并支持与 {@link java.lang.ref.Reference}、{@link Referential} 比较。
         * 引用值被回收后等价于与 {@code null} 比较。
         *
         * @param o 要比较的对象
         * @return 如果引用值相等返回true，否则返回false
         */
        @Override
        public final boolean equals(Object o) {
            return this.equalsReference(o);
        }

        /**
         * 获取对象的哈希码，委托给 {@link Referential#hashCodeReference()}，返回缓存的哈希码。
         *
         * @return 缓存的哈希码
         */
        @Override
        public final int hashCode() {
            return this.hashCodeReference();
        }

        /**
         * 获取对象的字符串表示，委托给 {@link Referential#toStringReference()}：
         * 输出被引用值的 {@code String.valueOf} 结果，引用失效后输出 {@code "null"}。
         *
         * @return 被引用值的字符串表示
         */
        @Override
        public final String toString() {
            return this.toStringReference();
        }
    }

    /**
     * 软引用内部类：继承 {@link java.lang.ref.SoftReference} 并实现 {@link Referential}。
     *
     * <p>仅在垃圾回收器报告内存不足时被回收，相较弱引用存活时间更长；
     * 在 JDK 自身的引用注册机制之外，额外缓存哈希码以支持引用失效后的稳定定位。
     *
     * @param <T> 引用值的类型
     */
    static class SoftReference<T> extends java.lang.ref.SoftReference<T> implements Referential<T> {
        /**
         * 创建时缓存的哈希码；清除时置 0
         */
        volatile int hashCode;

        /**
         * 构造软引用包装，并按需注册到引用队列。
         *
         * @param referent 被引用的对象
         * @param hashCode 创建时缓存的哈希码
         * @param queue    引用队列，可为 {@code null}；非空时引用被回收后会入队
         */
        protected SoftReference(T referent, int hashCode, ReferenceQueue<? super T> queue) {
            super(referent, queue);
            this.hashCode = hashCode;
        }

        /**
         * 获取引用类型
         *
         * @return 引用类型，始终返回SOFT
         */
        @Override
        public ReferentialType type() {
            return SOFT;
        }

        /**
         * 获取引用的值
         *
         * @return 引用的值，如果已被回收或清除则返回null
         */
        @Override
        public T get() {
            return super.get();
        }

        /**
         * 清除引用
         */
        @Override
        public void clear() {
            super.clear();
            this.hashCode = 0;
        }

        /**
         * 获取创建引用时缓存的哈希码，而非重新计算 {@code referent.hashCode()}。
         *
         * <p>引用值可能已被回收或已更换，重新计算会导致哈希值漂移，
         * 进而破坏其作为哈希容器键的定位；清除引用后该值被重置为 0。
         *
         * @return 缓存的哈希码
         */
        @Override
        public int hashCodeReference() {
            return hashCode;
        }

        /**
         * 比较对象是否相等，语义委托给 {@link Referential#equalsReference(Object)}：
         * 按被引用的值判等，并支持与 {@link java.lang.ref.Reference}、{@link Referential} 比较。
         * 引用值被回收后等价于与 {@code null} 比较。
         *
         * @param o 要比较的对象
         * @return 如果引用值相等返回true，否则返回false
         */
        @Override
        public final boolean equals(Object o) {
            return this.equalsReference(o);
        }

        /**
         * 获取对象的哈希码，委托给 {@link Referential#hashCodeReference()}，返回缓存的哈希码。
         *
         * @return 缓存的哈希码
         */
        @Override
        public final int hashCode() {
            return this.hashCodeReference();
        }

        /**
         * 获取对象的字符串表示，委托给 {@link Referential#toStringReference()}：
         * 输出被引用值的 {@code String.valueOf} 结果，引用失效后输出 {@code "null"}。
         *
         * @return 被引用值的字符串表示
         */
        @Override
        public final String toString() {
            return this.toStringReference();
        }
    }

    /**
     * 弱引用内部类：继承 {@link java.lang.ref.WeakReference} 并实现 {@link Referential}。
     *
     * <p>在下一次垃圾回收时若仅被弱引用可达则立即回收，无需等待内存不足；
     * 同样在 JDK 引用注册机制之外缓存哈希码，保证引用失效后容器定位稳定。
     *
     * @param <T> 引用值的类型
     */
    static class WeakReference<T> extends java.lang.ref.WeakReference<T> implements Referential<T> {
        /**
         * 创建时缓存的哈希码；清除时置 0
         */
        volatile int hashCode;

        /**
         * 构造弱引用包装，并按需注册到引用队列。
         *
         * @param referent 被引用的对象
         * @param hashCode 创建时缓存的哈希码
         * @param queue    引用队列，可为 {@code null}；非空时引用被回收后会入队
         */
        protected WeakReference(T referent, int hashCode, ReferenceQueue<? super T> queue) {
            super(referent, queue);
            this.hashCode = hashCode;
        }

        /**
         * 获取引用类型
         *
         * @return 引用类型，始终返回WEAK
         */
        @Override
        public ReferentialType type() {
            return WEAK;
        }

        /**
         * 获取引用的值
         *
         * @return 引用的值，如果已被回收或清除则返回null
         */
        @Override
        public T get() {
            return super.get();
        }

        /**
         * 清除引用
         */
        @Override
        public void clear() {
            super.clear();
            this.hashCode = 0;
        }

        /**
         * 获取创建引用时缓存的哈希码，而非重新计算 {@code referent.hashCode()}。
         *
         * <p>引用值可能已被回收或已更换，重新计算会导致哈希值漂移，
         * 进而破坏其作为哈希容器键的定位；清除引用后该值被重置为 0。
         *
         * @return 缓存的哈希码
         */
        @Override
        public int hashCodeReference() {
            return hashCode;
        }

        /**
         * 比较对象是否相等，语义委托给 {@link Referential#equalsReference(Object)}：
         * 按被引用的值判等，并支持与 {@link java.lang.ref.Reference}、{@link Referential} 比较。
         * 引用值被回收后等价于与 {@code null} 比较。
         *
         * @param o 要比较的对象
         * @return 如果引用值相等返回true，否则返回false
         */
        @Override
        public final boolean equals(Object o) {
            return this.equalsReference(o);
        }

        /**
         * 获取对象的哈希码，委托给 {@link Referential#hashCodeReference()}，返回缓存的哈希码。
         *
         * @return 缓存的哈希码
         */
        @Override
        public final int hashCode() {
            return this.hashCodeReference();
        }

        /**
         * 获取对象的字符串表示，委托给 {@link Referential#toStringReference()}：
         * 输出被引用值的 {@code String.valueOf} 结果，引用失效后输出 {@code "null"}。
         *
         * @return 被引用值的字符串表示
         */
        @Override
        public final String toString() {
            return this.toStringReference();
        }
    }
}

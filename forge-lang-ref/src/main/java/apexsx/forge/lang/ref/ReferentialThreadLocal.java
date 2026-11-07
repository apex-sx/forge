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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 提供线程局部引用变量的类，支持弱引用和软引用。
 *
 * <p>本类继承 {@link ThreadLocal}{@code <Object>}，但对外仍以 {@code T} 暴露：
 * 对弱/软引用而言，{@code ThreadLocalMap} 中存放的并不是业务值本身，而是一层
 * {@link Referential} 引用包装；这样即使线程长期存活（线程池、事件循环等），
 * 业务值仍可在垃圾回收器认为合适时被回收，避免强引用滞留造成的内存泄漏。
 *
 * <p>整体由三部分协作完成：
 * <ol>
 *   <li>{@link #GLOBAL_QUEUE}——所有实例、所有线程共享的 {@link ReferenceQueue}，
 *       弱/软引用被垃圾回收后会自动入队；</li>
 *   <li>{@link #TRACKED}——追踪"已创建但尚未清理"的引用包装，既是清理线程定位目标的依据，
 *       也是 {@link #pendingCleanupCount()} 的数据来源；</li>
 *   <li>{@link #SCHEDULER}——单线程守护调度器，每 500 毫秒轮询一次队列，批量出队、
 *       解除追踪并清除引用，使"线程设置值后不再调用 get()"的场景同样能被及时清理。</li>
 * </ol>
 *
 * <p>实例通过 {@link #weakWithInitial(Supplier)}、{@link #weak()}、
 * {@link #softWithInitial(Supplier)}、{@link #soft()} 创建。
 *
 * <p><b>使用注意：</b>
 * <ul>
 *   <li>弱/软引用可能在任意时刻被回收，{@link #get()} 在回收后返回 {@code null}，
 *       调用方需自行判断并按需重建；</li>
 *   <li>{@link #set(Object)} 传入 {@code null} 等价于 {@link #remove()}；</li>
 *   <li>线程结束或业务上不再需要该值时应显式调用 {@link #remove()}，
 *       否则在线程存活期间追踪项会一直保留。</li>
 * </ul>
 *
 * @param <T> 线程局部变量中存储的值的类型
 * @author Terraqo
 * @see Referential
 * @see ReferentialType
 */
@SuppressWarnings("unchecked")
public final class ReferentialThreadLocal<T> extends ThreadLocal<Object> {
    /**
     * 全局回收队列：所有实例、所有线程创建的弱/软引用共享同一个队列。
     * 引用被垃圾回收后会自动进入此队列，由 {@link #SCHEDULER} 统一出队处理。
     * 使用全局队列而非实例级队列，可避免每个 {@code ReferentialThreadLocal} 实例
     * 各自维护清理线程而导致的线程数量膨胀。
     */
    private static final ReferenceQueue<Object> GLOBAL_QUEUE = new ReferenceQueue<>();
    /**
     * 追踪表：记录当前仍处于"存活或待清理"状态的引用包装对象。
     * 值固定为 {@link Boolean#TRUE}，即把 {@link ConcurrentHashMap} 当作并发集合使用。
     * 作用有二：
     * <ul>
     *   <li>出队后据此精确定位到具体包装对象，执行 {@link Referential#clear()} 并解除追踪；</li>
     *   <li>为 {@link #pendingCleanupCount()} 提供待清理数量的观测口径。</li>
     * </ul>
     * 注意：此处持有的是引用包装的强引用，而包装内部只持有所需的弱/软引用，
     * 因此不会阻止业务值被回收；但由于 {@link Referential} 的 {@code equals}/{@code hashCode}
     * 委托给引用值，不同线程持有相等值时会被视为同一个条目，
     * 故 {@link #pendingCleanupCount()} 反映的是"不同值"的规模，而非引用实例的精确数量。
     */
    private static final ConcurrentHashMap<Referential<?>, Boolean> TRACKED = new ConcurrentHashMap<>();
    /**
     * 全局清理调度器：单线程、守护、优先级为 {@code MAX_PRIORITY - 2}。
     * 守护属性保证 JVM 退出时不会被其阻塞；本类不提供关闭方法，
     * 其生命周期与 JVM 一致。
     */
    private static final ScheduledExecutorService SCHEDULER;

    /*
     * 静态初始化：启动全局清理线程。
     * 采用 scheduleWithFixedDelay 而非固定频率调度，确保上一次清理执行完毕后
     * 才开始计算下一次延迟，避免任务堆积；初始延迟为 0，使首次清理尽快生效。
     */
    static {
        SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "ReferentialThreadLocal-Cleaner");
            // 守护线程：不阻止 JVM 正常退出
            thread.setDaemon(true);
            // 略低于最高优先级：清理任务不与业务线程抢占 CPU
            thread.setPriority(Thread.MAX_PRIORITY - 2);
            return thread;
        });
        SCHEDULER.scheduleWithFixedDelay(() -> {
            try {
                Referential<Object> ref;
                // 批量排空队列：poll() 为非阻塞，循环直至队列为空
                while ((ref = (Referential<Object>) GLOBAL_QUEUE.poll()) != null) {
                    // 解除追踪，避免 TRACKED 长期持有已失效的引用包装
                    TRACKED.remove(ref);
                    // 清除引用内部状态（引用值与缓存的哈希码）
                    ref.clear();
                }
            } catch (Exception ignored) {
                // 固定延迟调度下，任务抛出异常会导致后续执行被取消，
                // 因此这里必须吞掉异常以保证清理线程持续工作
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
    }

    /**
     * 创建引用包装时使用的引用类型。
     * 为 {@link ReferentialType#STRONG} 时直接存储原始值，不进行包装与追踪。
     */
    private final ReferentialType referentialType;
    /**
     * 初始值供应商。
     * 为 {@code null} 表示不提供初始值，此时 {@link #initialValue()} 返回 {@code null}。
     */
    private final Supplier<? extends T> initialValue;

    /**
     * 私有构造函数，用于创建ReferentialThreadLocal实例
     *
     * @param referentialType 引用类型（弱引用或软引用）
     * @param initialValue    初始值供应商
     */
    private ReferentialThreadLocal(ReferentialType referentialType, Supplier<? extends T> initialValue) {
        this.referentialType = referentialType;
        this.initialValue = initialValue;
    }

    /**
     * 创建一个带有初始值的弱引用线程局部变量。
     * 使用弱引用可以确保当没有其他强引用指向对象时，该对象可以在下一次垃圾回收时被回收。
     *
     * <p>供应商在每次 {@link #initialValue()} 被调用时执行，其返回值会被包装为弱引用
     * 并纳入全局回收队列管理。
     *
     * @param <S>      线程局部变量的值类型
     * @param supplier 用于产生初始值的供应商函数；可为 {@code null}，表示不提供初始值，
     *                 此时在未显式设置值的情况下 {@link #get()} 返回 {@code null}
     * @return 新的弱引用线程局部变量
     */
    public static <S> ReferentialThreadLocal<S> weakWithInitial(Supplier<? extends S> supplier) {
        return new ReferentialThreadLocal<>(ReferentialType.WEAK, supplier);
    }

    /**
     * 创建一个不带初始值的弱引用线程局部变量。
     * 初始值将为null，直到显式设置值为止。
     *
     * @param <S> 线程局部变量的值类型
     * @return 新的弱引用线程局部变量
     */
    public static <S> ReferentialThreadLocal<S> weak() {
        return new ReferentialThreadLocal<>(ReferentialType.WEAK, null);
    }

    /**
     * 创建一个带有初始值的软引用线程局部变量。
     * 使用软引用允许对象在内存不足时被垃圾回收，但在正常情况下保持存活，
     * 适合承载可重建、体积较大的缓存类数据。
     *
     * <p>供应商在每次 {@link #initialValue()} 被调用时执行，其返回值会被包装为软引用
     * 并纳入全局回收队列管理。
     *
     * @param <S>      线程局部变量的值类型
     * @param supplier 用于产生初始值的供应商函数；可为 {@code null}，表示不提供初始值，
     *                 此时在未显式设置值的情况下 {@link #get()} 返回 {@code null}
     * @return 新的软引用线程局部变量
     */
    public static <S> ReferentialThreadLocal<S> softWithInitial(Supplier<? extends S> supplier) {
        return new ReferentialThreadLocal<>(ReferentialType.SOFT, supplier);
    }

    /**
     * 创建一个不带初始值的软引用线程局部变量。
     * 初始值将为null，直到显式设置值为止。
     *
     * @param <S> 线程局部变量的值类型
     * @return 新的软引用线程局部变量
     */
    public static <S> ReferentialThreadLocal<S> soft() {
        return new ReferentialThreadLocal<>(ReferentialType.SOFT, null);
    }

    /**
     * 获取当前被追踪、尚未完成清理的引用包装数量。
     *
     * <p>该值取自 {@link #TRACKED} 的大小，是某一时刻的瞬时快照，
     * 仅用于监控与诊断（例如观察清理线程是否正常工作、是否存在引用长期滞留），
     * 不应作为业务逻辑的判断依据。
     *
     * <p>注意：由于 {@link Referential} 按引用值判等，相同值的多个引用会被合并计数，
     * 因此该值通常小于实际引用实例数。
     *
     * @return 待清理引用数量；在清理线程正常运行时，该值应随垃圾回收而逐步回落
     */
    public static int pendingCleanupCount() {
        return TRACKED.size();
    }

    /**
     * 返回当前线程对于此线程局部变量的"初始值"。
     * 当线程第一次通过{@link #get()}方法访问变量时，将调用此方法，
     * 除非线程之前调用了{@link #set(Object)}方法，在这种情况下，不会为此线程调用{@code initialValue}方法。
     * 正常情况下，每个线程最多调用一次此方法，但如果后续调用{@link #remove()}后又调用{@link #get()}，
     * 则可能会再次调用此方法。
     *
     * <p>实现说明：未配置供应商时直接返回 {@code null}；配置了供应商时先由供应商产生原始值，
     * 若引用类型不是 {@link ReferentialType#STRONG}，还会用该值创建一个 {@link Referential}
     * 包装（注册到 {@link #GLOBAL_QUEUE}）并放入 {@link #TRACKED}，以便后续被回收清理。
     *
     * <p><b>注意：</b>本方法返回的是供应商产生的原始值，而非上述 {@link Referential} 包装，
     * 与 {@link #get()} 中按包装解析的预期并不一致（原始值未实现 {@link Referential} 时会转换失败）。
     * 因此推荐在使用前始终通过 {@link #set(Object)} 显式设置值。
     *
     * @return 此线程局部变量的初始值；未配置供应商时返回 {@code null}
     */
    @Override
    protected T initialValue() {
        if (initialValue == null) {
            // 未配置供应商，无初始值可用
            return null;
        } else {
            // 由供应商产生原始值
            T value = initialValue.get();
            if (referentialType != ReferentialType.STRONG) {
                // 弱/软引用：额外创建引用包装并纳入全局追踪与回收队列
                int hashCode = Objects.hashCode(value);
                Referential<Object> referential = referentialType.create(value, hashCode, GLOBAL_QUEUE);
                TRACKED.put(referential, Boolean.TRUE);
            }
            // 强引用或已创建包装时，均直接返回原始值
            return value;
        }
    }

    /**
     * 获取当前线程在此线程局部变量中的值。
     * 如果变量对当前线程没有值，则首先初始化为调用{@link #initialValue}方法返回的值。
     * 如果引用中的值已被垃圾回收，则返回null。
     *
     * <p>处理流程：
     * <ol>
     *   <li>从底层 {@link ThreadLocal} 取出 {@link Referential} 包装；</li>
     *   <li>包装存在且引用值仍存活时，直接返回该值；</li>
     *   <li>包装存在但引用值已被垃圾回收（或被清除）时，从 {@link #TRACKED} 中移除该包装、
     *       调用 {@link Referential#clear()} 并执行 {@link #remove()}，然后返回 {@code null}。</li>
     * </ol>
     *
     * <p><b>注意：</b>底层 {@link ThreadLocal#get()} 在值缺失时会先调用 {@link #initialValue()}，
     * 而该方法返回的是原始值而非 {@link Referential} 包装；若该值未实现 {@link Referential}，
     * 此处的强制转换会抛出 {@link ClassCastException}。因此应在首次使用前通过
     * {@link #set(Object)} 显式设置值。
     *
     * @return 当前线程的此线程局部变量的值；引用不存在或引用值已被回收时返回 {@code null}
     */
    @Override
    public T get() {
        // 从 ThreadLocal 中取出引用包装（若尚未设置值，底层可能触发 initialValue）
        Referential<T> referential = (Referential<T>) super.get();
        if (referential != null) {
            // 从包装中取出实际值，弱/软引用可能已被回收而返回 null
            T value = referential.get();
            if (value != null) {
                // 值仍然存活，直接返回
                return value;
            } else {
                // 值已被回收：先解除追踪并清除包装，再移除 ThreadLocal 中的条目
                TRACKED.remove(referential);
                referential.clear();
                remove();
            }
        }
        // 引用不存在或值已被回收，返回 null
        return null;
    }

    /**
     * 将当前线程的此线程局部变量副本设置为指定值。
     * 大多数子类不需要重写此方法，而是完全依赖{@link #initialValue}方法来设置线程局部变量的值。
     *
     * <p>实现说明：
     * <ul>
     *   <li>传入 {@code null} 等价于 {@link #remove()}，即清理并移除当前线程的值；</li>
     *   <li>传入非 {@code null} 时，会先清除并移除此前的引用包装，避免旧包装继续被追踪；</li>
     *   <li>强引用类型直接存储原始值，不产生包装与追踪开销；
     *       弱/软引用类型则创建新的 {@link Referential} 包装、注册到 {@link #GLOBAL_QUEUE}
     *       并放入 {@link #TRACKED}，再把包装存入底层 {@link ThreadLocal}。</li>
     * </ul>
     *
     * <p>由于强引用分支存储的是原始值，而 {@link #get()} 期望读取 {@link Referential} 包装，
     * 该分支使用时需保证存储值本身可通过类型转换，建议仅在弱/软引用场景下使用本类。
     *
     * @param value 要存储在当前线程副本中的值；为 {@code null} 时移除当前线程的值
     */
    @Override
    public void set(Object value) {
        if (value == null) {
            // 值为 null 等价于移除，避免保留无意义的引用包装
            remove();
        } else {
            // 先清理旧包装：解除追踪并清空引用，防止旧包装滞留
            Referential<Object> referential = (Referential<Object>) super.get();
            if (referential != null) {
                TRACKED.remove(referential);
                referential.clear();
            }
            if (referentialType == ReferentialType.STRONG) {
                // 强引用：直接存储原始值，不包装也不纳入清理队列
                super.set(value);
            } else {
                // 弱/软引用：创建包装，注册到全局队列并纳入追踪，再存入 ThreadLocal
                referential = this.referentialType.create(value, Objects.hashCode(value), GLOBAL_QUEUE);
                TRACKED.put(referential, Boolean.TRUE);
                super.set(referential);
            }
        }
    }

    /**
     * 移除当前线程在此线程局部变量中的值，并释放相关引用。
     *
     * <p>执行步骤：
     * <ol>
     *   <li>取出当前线程持有的 {@link Referential} 包装；</li>
     *   <li>若包装非空，则从 {@link #TRACKED} 中移除追踪并调用 {@link Referential#clear()}
     *       清空引用值与缓存的哈希码，避免清理线程重复处理；</li>
     *   <li>最后调用父类 {@link ThreadLocal#remove()} 移除 {@code ThreadLocalMap} 中的条目，
     *       避免线程池场景下条目随线程长期存活。</li>
     * </ol>
     *
     * <p>当追踪表已在 {@link #get()} 中完成清理时，此方法对这些包装是幂等的。
     */
    @Override
    public void remove() {
        // 取出当前线程持有的引用包装，以便同步解除追踪
        Referential<Object> old = (Referential<Object>) super.get();
        if (old != null) {
            // 解除追踪并清空引用，避免残留死引用
            TRACKED.remove(old);
            old.clear();
        }
        // 移除底层 ThreadLocalMap 中的条目
        super.remove();
    }
}
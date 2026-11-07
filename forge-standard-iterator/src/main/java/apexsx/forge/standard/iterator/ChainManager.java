/*
 * Copyright (c) 2026.
 */

package apexsx.forge.standard.iterator;


import apexsx.forge.core.Logger;
import apexsx.forge.core.cc.ObjectStatist;
import apexsx.forge.core.cc.SortedKey;
import apexsx.forge.core.cc.This;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 抽象管理器类，用于管理具有唯一名称的实例对象
 * 支持线程安全的注册和迭代操作
 * <p>
 * 设计特点：
 * - 使用泛型参数<T>指定管理的实例类型
 * - 使用泛型参数<I>实现链式调用模式，允许子类继承并保持正确的返回类型
 * - 利用ConcurrentSkipListMap提供线程安全的存储和自然排序能力
 * - 通过volatile数组缓存实现高效的迭代访问
 * - 支持频率统计排序功能，根据使用频率动态调整实例顺序
 *
 * @param <T> 管理的实例类型，表示此管理器中存储的具体对象类型
 * @param <I> 具体实现的管理器类型，用于支持链式调用模式（Fluent Interface）
 * @author Terraqo
 */
public class ChainManager<T, I extends ChainManager<T, I>> implements This<I> {

    /**
     * 存储管理的实例对象，使用ConcurrentSkipListMap确保线程安全和排序
     * 键为SortedKey，包含名称和顺序号，确保相同名称的对象也能按注册顺序排序
     * <p>
     * 数据结构选择说明：
     * - ConcurrentSkipListMap提供了线程安全的有序映射
     * - SortedKey封装了名称和注册顺序，保证了键的唯一性和排序逻辑
     * - ObjectStatist包装实际实例对象，同时维护使用频率统计
     * - 使用自然排序（Comparator.naturalOrder()）基于SortedKey的比较逻辑
     */
    private final Map<SortedKey<String>, ObjectStatist> instanceRegistry = new ConcurrentSkipListMap<>(Comparator.naturalOrder());

    /**
     * 原子整数用于生成唯一顺序号，保证多线程环境下注册顺序的正确记录
     * 每次注册新实例时递增，作为SortedKey的顺序部分，确保唯一性
     * <p>
     * 设计考虑：
     * - AtomicInteger提供线程安全的自增操作
     * - 避免了显式的同步机制，提高并发性能
     * - 顺序号用于处理同名实例的情况，确保SortedKey的唯一性
     */
    private final AtomicInteger orderCounter = new AtomicInteger(0);

    /**
     * 排序后的实例缓存数组
     * <p>
     * 设计要点：
     * - volatile关键字确保数组引用的可见性，防止指令重排
     * - 存储排序后的ObjectStatist对象，避免每次迭代都进行排序操作
     * - 通过双重检查锁定模式进行懒加载，优化性能
     * - 根据useFrequencyStatisticsOrder参数决定排序策略
     */
    private volatile ObjectStatist[] sortedInstanceCache;

    /**
     * 构造函数，使用可变参数初始化管理器
     * <p>
     * 功能说明：
     * - 接收多个实例对象，批量注册到管理器中
     * - 对每个实例使用默认名称（null）进行注册
     * - 自动分配唯一的顺序号作为内部标识
     *
     * @param instances 要注册的实例数组
     */
    @SafeVarargs
    public ChainManager(T... instances) {
        for (T instance : instances) {
            registerInstance(null, instance); // 使用null名称，系统自动分配唯一标识
        }
    }

    /**
     * 注册一个实例到管理器中，使用默认名称
     * <p>
     * 这是一个便捷方法，相当于调用registerInstance(null, instance)
     * 实例将以自动生成的唯一名称注册到管理器中
     *
     * @param instance 实例对象，不能为null
     * @return 返回当前管理器实例，支持链式调用
     * @throws IllegalArgumentException 如果instance为null
     */
    public final I registerInstance(T instance) {
        return this$().registerInstance(null, instance);
    }

    /**
     * 注册一个实例到管理器中，支持指定名称
     * <p>
     * 注册流程：
     * 1. 验证实例不为null
     * 2. 处理名称情况：
     * - 若名称为null，生成"$序号"格式的唯一名称
     * - 若名称非null，检查名称冲突
     * 3. 创建带有顺序号的SortedKey
     * 4. 将实例包装成ObjectStatist并存入注册表
     * 5. 使缓存失效，触发下次访问时重建
     * <p>
     * 线程安全性：
     * - 使用原子操作保证orderCounter的线程安全
     * - ConcurrentSkipListMap确保put操作的线程安全
     *
     * @param name     实例名称，若为null则自动生成唯一名称
     * @param instance 实例对象，不能为null
     * @return 返回当前管理器实例，支持链式调用
     * @throws IllegalArgumentException 如果instance为null，或name已存在
     */
    public final I registerInstance(String name, T instance) {
        if (instance == null) {
            throw new IllegalArgumentException("Instance cannot be null"); // 防御性检查
        }

        SortedKey<String> key;
        if (name == null) {
            // 自动生成唯一名称，格式为"$序号"
            int count = orderCounter.incrementAndGet(); // 原子递增获取唯一序号
            key = new SortedKey<>("$" + count, count);
        } else {
            // 检查名称是否存在，避免重复注册
            if (instanceRegistry.containsKey(new SortedKey<>(name, -1))) { // 使用-1作为占位符检查名称是否存在
                throw new IllegalArgumentException("Instance name already exists: " + name);
            }
            int count = orderCounter.incrementAndGet(); // 获取顺序号
            key = new SortedKey<>(name, count); // 创建带名称和顺序号的键
        }

        instanceRegistry.put(key, new ObjectStatist(instance)); // 注册实例
        return this$(); // 返回当前实例，支持链式调用
    }

    /**
     * 返回实例集合的迭代器，默认不使用频率统计排序
     * <p>
     * 这是一个便捷方法，相当于调用getInstanceChain(false)
     * 返回的迭代器按照实例注册顺序遍历所有实例
     *
     * @return 实例值的迭代器，按照注册顺序排列
     */
    public final Iterable<T> getInstanceChain() {
        return this$().getInstanceChain(false);
    }

    /**
     * 返回实例集合的迭代器，支持频率统计排序选项
     * <p>
     * 功能说明：
     * - 根据useFrequencyStatisticsOrder参数决定排序方式
     * - 使用ConvertingIterable延迟转换ObjectStatist为T类型
     * - 透明地处理排序缓存的获取和维护
     *
     * @param useFrequencyStatisticsOrder 是否使用频率统计排序
     *                                    true - 按使用频率降序排列
     *                                    false - 按注册顺序排列
     * @return 实例值的迭代器，顺序根据参数决定
     */
    public final Iterable<T> getInstanceChain(boolean useFrequencyStatisticsOrder) {
        // 获取排序后的缓存数组，根据参数决定排序方式
        // 使用方法引用将ObjectStatist转换为实际的T类型实例
        return new ConvertingIterable<>(this.getSortedInstanceCache(useFrequencyStatisticsOrder), ObjectStatist::getAndCastInstance);
    }

    /**
     * 解析方法，尝试对列表中的每个元素应用解析器
     * <p>
     * 工作流程：
     * 1. 获取当前实例的快照（避免并发修改问题）
     * 2. 遍历实例，对每个实例应用resolver
     * 3. 如果resolver返回true，根据useFrequencyStatisticsOrder参数更新统计
     * 4. 在找到第一个匹配项后停止遍历
     * <p>
     * 频率统计排序特性：
     * - 当useFrequencyStatisticsOrder为true时，会增加匹配实例的使用计数
     * - 如果匹配实例的计数超过当前首位，会触发缓存重新排序
     * - 采用优化策略减少不必要的排序操作
     * <p>
     * 异常处理：
     * - 捕获resolver执行过程中的异常，记录日志并继续处理下一个实例
     * - 防止单个实例的问题影响整个解析过程
     *
     * @param resolver 布尔函数接口，用于判断某个对象是否满足条件
     * @return 返回当前管理器实例，支持链式调用
     */
    public final I resolve(Predicate<T> resolver) {
        return this$().resolve(true, resolver);
    }

    /**
     * 解析方法，尝试对列表中的每个元素应用解析器
     * <p>
     * 工作流程：
     * 1. 获取当前实例的快照（避免并发修改问题）
     * 2. 遍历实例，对每个实例应用resolver
     * 3. 如果resolver返回true，根据useFrequencyStatisticsOrder参数更新统计
     * 4. 在找到第一个匹配项后停止遍历
     * <p>
     * 频率统计排序特性：
     * - 当useFrequencyStatisticsOrder为true时，会增加匹配实例的使用计数
     * - 如果匹配实例的计数超过当前首位，会触发缓存重新排序
     * - 采用优化策略减少不必要的排序操作
     * <p>
     * 异常处理：
     * - 捕获resolver执行过程中的异常，记录日志并继续处理下一个实例
     * - 防止单个实例的问题影响整个解析过程
     *
     * @param useFrequencyStatisticsOrder 是否启用频率统计排序
     * @param resolver                    布尔函数接口，用于判断某个对象是否满足条件
     * @return 返回当前管理器实例，支持链式调用
     */
    public final I resolve(boolean useFrequencyStatisticsOrder, Predicate<T> resolver) {
        // 获取当前实例的快照，避免遍历时受到其他线程修改的影响
        ObjectStatist[] current = this.getSortedInstanceCache(useFrequencyStatisticsOrder);

        // 遍历当前快照，避免在遍历过程中被其他线程修改
        for (int i = 0; i < current.length; i++) {
            T instance;
            try {
                // 安全获取实例对象
                instance = current[i].getAndCastInstance();
            } catch (Exception e) {
                // 记录获取实例时的异常，但不中断处理流程
                if (Logger.isEnabledError()) {
                    Logger.error("An error occurred while resolving instance: %s", e.getMessage(), e);
                }
                continue; // 继续处理下一个实例
            }

            boolean test;
            try {
                // 应用解析器测试实例
                test = resolver.test(instance);
            } catch (Exception e) {
                // 记录解析器执行异常，但不中断处理流程
                if (Logger.isEnabledError()) {
                    Logger.error("An error occurred while executing instance at index %d with value %s: %s", i, instance, e.getMessage(), e);
                }
                continue; // 继续处理下一个实例
            }

            if (test) {
                // 如果解析器返回true，说明找到了匹配的实例
                if (useFrequencyStatisticsOrder) {
                    // 增加该对象的使用计数，用于频率统计排序
                    long incrementedCount = current[i].incrementAndGet();

                    // 优化：只有在当前缓存大小与注册表大小一致时才考虑重新排序
                    // 这可以避免在排序期间发生注册导致的缓存不一致问题
                    if (current.length == instanceRegistry.size()) {
                        // 进一步优化：只有在当前项不是第一项且其计数超过当前第一项时才重新排序
                        // 这减少了不必要的排序操作
                        if (i != 0 && incrementedCount > current[0].get()) {
                            synchronized (this) {
                                // 双重检查：再次验证缓存未被其他线程更新
                                ObjectStatist[] latestCache = sortedInstanceCache;
                                // 确认当前缓存仍是最新，或当前项计数已超过最新缓存的首项
                                if (latestCache == current || incrementedCount > latestCache[0].get()) {
                                    // 创建副本并按计数降序排序
                                    ObjectStatist[] temp = Arrays.copyOf(latestCache, latestCache.length);
                                    Arrays.sort(temp, Comparator.comparingLong(o -> -o.get())); // 按使用计数降序排列
                                    // 更新缓存，volatile写入确保所有线程立即看到变更
                                    sortedInstanceCache = temp;
                                }
                            }
                        }
                    }
                }
                break; // 找到匹配项后退出循环，提高效率
            }
        }
        return this$(); // 返回当前实例，支持链式调用
    }

    /**
     * 获取排序后的实例缓存，支持频率统计排序
     * <p>
     * 缓存策略：
     * - 采用懒加载和双重检查锁定模式
     * - 当缓存不存在或大小不匹配时重建缓存
     * - 根据useFrequencyStatisticsOrder参数决定排序方式
     * <p>
     * 线程安全：
     * - 使用synchronized块确保缓存重建的原子性
     * - volatile变量确保缓存更新的可见性
     *
     * @param useFrequencyStatisticsOrder 是否按频率统计排序
     * @return 排序后的ObjectStatist数组
     */
    private ObjectStatist[] getSortedInstanceCache(boolean useFrequencyStatisticsOrder) {
        // 首先读取当前缓存，避免不必要的同步
        ObjectStatist[] current = sortedInstanceCache;
        // 检查缓存是否为空或大小不匹配（表明可能有新实例注册）
        if (current == null || current.length != instanceRegistry.size()) {
            synchronized (this) { // 同步块确保只有一个线程重建缓存
                if (sortedInstanceCache == null || sortedInstanceCache.length != instanceRegistry.size()) {
                    // 从注册表获取所有实例并转换为流
                    Stream<ObjectStatist> stream = instanceRegistry.values().stream();

                    if (useFrequencyStatisticsOrder) {
                        // 按使用频率降序排序
                        stream = stream.sorted(Comparator.comparingLong(o -> -o.get()));
                    }

                    // 转换为数组
                    current = stream.toArray(ObjectStatist[]::new);
                }
                // 更新缓存，volatile写入确保其他线程能看到最新状态
                sortedInstanceCache = current;
            }
        }
        return current; // 返回缓存的实例数组
    }
}

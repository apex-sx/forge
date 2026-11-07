package apexsx.forge.util.algorithm;


import lombok.Getter;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 关系计算器，用于在内存中对两个列表（主表和副表）进行关联操作。
 * <p>
 * 支持 Inner Join（内连接）和 Left Join（左连接）语义。
 * 提供两种连接策略：
 * 1. 基于 Hash Map 的高效连接策略（Hash Join），适用于定义了键提取器的场景，时间复杂度接近 O(N + M)。
 * 2. 基于嵌套循环的通用连接策略（Nested Loop Join），适用于未定义键提取器或需要复杂谓词判断的场景，时间复杂度为 O(N * M)。
 *
 * @param <M> 主表元素类型
 * @param <O> 副表元素类型
 * @param <R> 结果元素类型
 * @author Terraqo
 */
public class RelationComputer<M, O, R> {
    /**
     * 主表数据列表
     */
    private final List<M> mainList;

    /**
     * 副表数据列表
     */
    private final List<O> otherList;

    /**
     * 连接类型标识。
     * true: 内连接（Inner Join），仅保留匹配成功的记录。
     * false: 左连接（Left Join），保留主表所有记录，未匹配的副表字段以 null 填充。
     */
    private final boolean isInnerJoin;

    /**
     * 计算后的结果列表
     */
    @Getter
    private List<R> resultList;

    /**
     * 结果映射器：将主表元素和副表元素合并为结果元素
     */
    private final BiFunction<M, O, R> merger;

    /**
     * 副表关联键提取器：从副表元素中提取用于关联的唯一键
     */
    private Function<O, UniqueKey> otherKeyExtractor;

    /**
     * 主表关联键提取器：从主表元素中提取用于关联的唯一键
     */
    private Function<M, UniqueKey> mainKeyExtractor;

    /**
     * 关联条件过滤器：用于在键匹配基础上进行额外的复杂条件判断。
     * 如果未定义键提取器，则此过滤器将用于嵌套循环中的每一对元素判断。
     */
    private BiPredicate<M, O> relationFilter;

    /**
     * 主表预过滤器：在关联前对主表数据进行过滤，减少参与关联的数据量
     */
    private Predicate<M> mainRelationFilter;

    /**
     * 副表预过滤器：在关联前对副表数据进行过滤，减少参与关联的数据量
     */
    private Predicate<O> otherRelationFilter;

    /**
     * 结果过滤器：对最终生成的结果进行过滤
     */
    private Predicate<R> resultFilter;

    /**
     * 私有构造函数
     *
     * @param mainList    主表数据列表，不能为 null
     * @param otherList   副表数据列表，不能为 null
     * @param merger      结果合并函数，不能为 null
     * @param isInnerJoin 是否为内连接
     */
    private RelationComputer(List<M> mainList, List<O> otherList, BiFunction<M, O, R> merger, boolean isInnerJoin) {
        this.mainList = Objects.requireNonNull(mainList, "The main table data must be not null");
        this.otherList = Objects.requireNonNull(otherList, "The other table data must be not null");
        this.merger = Objects.requireNonNull(merger, "The main and other merger must be not null");
        this.isInnerJoin = isInnerJoin;
    }

    /**
     * 静态工厂方法，用于创建 RelationComputer 实例
     *
     * @param mainList     主表数据列表
     * @param otherList    副表数据列表
     * @param resultMapper 结果合并函数
     * @param isInnerJoin  是否为内连接
     * @param <M>          主表元素类型
     * @param <O>          副表元素类型
     * @param <R>          结果元素类型
     * @return RelationComputer 实例
     */
    public static <M, O, R> RelationComputer<M, O, R> of(List<M> mainList, List<O> otherList, BiFunction<M, O, R> resultMapper, boolean isInnerJoin) {
        return new RelationComputer<>(mainList, otherList, resultMapper, isInnerJoin);
    }

    /**
     * 链式调用方法：将当前计算结果作为新的主表，与新的副表进行关联。
     * <p>
     * 如果当前尚未执行计算，将先触发 {@link #compute()} 方法。
     *
     * @param otherList    新的副表数据列表
     * @param resultMapper 新的结果合并函数
     * @param isInnerJoin  新的连接类型（true为内连接，false为左连接）
     * @param <NO>         新副表元素类型
     * @param <NR>         新结果元素类型
     * @return 新的 RelationComputer 实例
     */
    public <NO, NR> RelationComputer<R, NO, NR> join(List<NO> otherList, BiFunction<R, NO, NR> resultMapper, boolean isInnerJoin) {
        List<R> currentResult = getResultList();
        if (currentResult == null) {
            currentResult = compute();
        }
        if (currentResult == null) {
            throw new IllegalStateException("No result list generated.");
        }
        return new RelationComputer<>(currentResult, otherList, resultMapper, isInnerJoin);
    }

    /**
     * 设置基于键的关联条件
     *
     * @param mainKeyExtractor  主表键提取器
     * @param otherKeyExtractor 副表键提取器
     * @return 当前实例，支持链式调用
     */
    public RelationComputer<M, O, R> on(Function<M, UniqueKey> mainKeyExtractor, Function<O, UniqueKey> otherKeyExtractor) {
        this.mainKeyExtractor = Objects.requireNonNull(mainKeyExtractor);
        this.otherKeyExtractor = Objects.requireNonNull(otherKeyExtractor);
        return this;
    }

    /**
     * 设置基于谓词的关联条件（适用于复杂关联逻辑或无明确键的场景）
     *
     * @param relationFilter 关联条件过滤器
     * @return 当前实例，支持链式调用
     */
    public RelationComputer<M, O, R> on(BiPredicate<M, O> relationFilter) {
        this.relationFilter = Objects.requireNonNull(relationFilter);
        return this;
    }

    /**
     * 设置主表预过滤器
     *
     * @param mainRelationFilter 主表过滤条件
     * @return 当前实例，支持链式调用
     */
    public RelationComputer<M, O, R> filterMain(Predicate<M> mainRelationFilter) {
        this.mainRelationFilter = Objects.requireNonNull(mainRelationFilter);
        return this;
    }

    /**
     * 设置副表预过滤器
     *
     * @param otherRelationFilter 副表过滤条件
     * @return 当前实例，支持链式调用
     */
    public RelationComputer<M, O, R> filterOther(Predicate<O> otherRelationFilter) {
        this.otherRelationFilter = Objects.requireNonNull(otherRelationFilter);
        return this;
    }

    /**
     * 设置结果过滤器
     *
     * @param resultFilter 结果过滤条件
     * @return 当前实例，支持链式调用
     */
    public RelationComputer<M, O, R> filterResult(Predicate<R> resultFilter) {
        this.resultFilter = Objects.requireNonNull(resultFilter);
        return this;
    }

    /**
     * 执行关联计算，并将结果存入 resultList。
     * <p>
     * 计算策略选择逻辑：
     * 1. 如果同时定义了主表和副表的键提取器（mainKeyExtractor 和 otherKeyExtractor），则使用基于 HashMap 的 Hash Join 策略。
     * - 首先构建副表的索引 Map。
     * - 然后遍历主表，通过键快速查找匹配的副表记录。
     * - 时间复杂度接近 O(N + M)，性能较高，推荐大数据量使用。
     * 2. 如果未定义键提取器，则使用嵌套循环连接（Nested Loop Join）策略。
     * - 遍历主表和副表的所有组合，并通过 relationFilter 判断是否匹配。
     * - 时间复杂度为 O(N * M)，适用于数据量较小或关联条件极其复杂无法提取键的场景。
     * <p>
     * 连接类型处理：
     * - 内连接 (isInnerJoin = true): 仅当找到匹配的副表记录且通过所有过滤器时，才将结果加入 resultList。
     * - 左连接 (isInnerJoin = false): 保留主表所有经过预过滤的记录。如果找不到匹配的副表记录，或所有匹配记录都被 relationFilter 过滤掉，
     * 则将主表记录与 null 合并后加入 resultList（前提是结果通过 resultFilter）。
     */
    public List<R> compute() {
        resultList = new ArrayList<>();

        // 策略1: 如果定义了关联键提取器，使用基于 Map 的 Join (Hash Join)，复杂度接近 O(N+M)
        if (mainKeyExtractor != null && otherKeyExtractor != null) {
            // 构建副表的索引 Map: Key -> List<O>
            // 初始化容量为 otherList.size()，负载因子设为 1 以减少哈希冲突并优化内存
            Map<UniqueKey, List<O>> otherMap = new HashMap<>(otherList.size(), 1);

            // 遍历副表，构建索引
            for (O other : otherList) {
                // 应用副表预过滤：如果不符合条件，跳过该元素
                if (otherRelationFilter != null && !otherRelationFilter.test(other)) {
                    continue;
                }

                // 提取关联键
                UniqueKey key = otherKeyExtractor.apply(other);
                if (key != null) {
                    // 将元素添加到对应键的列表中
                    otherMap.computeIfAbsent(key, k -> new ArrayList<>(8)).add(other);
                }
            }

            // 遍历主表进行匹配
            for (M main : mainList) {
                // 应用主表预过滤：如果不符合条件，跳过该元素
                if (mainRelationFilter != null && !mainRelationFilter.test(main)) {
                    continue;
                }

                // 提取主表关联键
                UniqueKey key = mainKeyExtractor.apply(main);

                // 检查副表中是否存在匹配的键
                if (key != null && otherMap.containsKey(key)) {
                    List<O> others = otherMap.get(key);

                    // 标记是否至少有一个匹配项通过所有过滤条件
                    boolean hasValidMatch = false;

                    // 遍历所有匹配的副表记录
                    for (O other : others) {
                        // 如果有额外的关联条件过滤器，则进行二次校验
                        if (relationFilter != null && !relationFilter.test(main, other)) {
                            continue;
                        }

                        // 标记存在有效匹配
                        hasValidMatch = true;

                        // 合并主表和副表数据生成结果
                        R result = merger.apply(main, other);

                        // 应用结果过滤：如果未定义过滤器或过滤通过，则加入结果集
                        if (resultFilter == null || resultFilter.test(result)) {
                            resultList.add(result);
                        }
                    }

                    // 如果是 Left Join (!isInnerJoin) 且没有任何有效匹配（可能是因为 relationFilter 过滤掉了所有匹配，或者列表为空）
                    // 注意：上面的循环中如果 relationFilter 过滤掉所有项，hasValidMatch 将为 false
                    if (!isInnerJoin && !hasValidMatch) {
                        // 生成左连接结果：副表部分为 null
                        R result = merger.apply(main, null);
                        if (resultFilter == null || resultFilter.test(result)) {
                            resultList.add(result);
                        }
                    }
                } else if (!isInnerJoin) {
                    // Left Join 情况：主表有，副表无匹配键
                    // 生成左连接结果：副表部分为 null
                    R result = merger.apply(main, null);
                    if (resultFilter == null || resultFilter.test(result)) {
                        resultList.add(result);
                    }
                }
            }

        } else {
            // 策略2: 嵌套循环连接 (Nested Loop Join)
            // 适用场景: 未定义关联键提取器，或显式依赖 relationFilter 进行复杂条件判断
            // 注意: 时间复杂度为 O(N*M)，数据量大时性能较差

            for (M main : mainList) {
                // 应用主表预过滤
                if (mainRelationFilter != null && !mainRelationFilter.test(main)) {
                    continue;
                }

                // 标记当前主表记录是否在副表中找到匹配
                boolean hasMatch = false;

                // 遍历副表
                for (O other : otherList) {
                    // 应用副表预过滤
                    if (otherRelationFilter != null && !otherRelationFilter.test(other)) {
                        continue;
                    }

                    // 应用关联条件过滤器 (如果存在)
                    // 如果未定义 relationFilter，则默认所有经过预过滤的组合都匹配（笛卡尔积）
                    if (relationFilter != null && !relationFilter.test(main, other)) {
                        continue;
                    }

                    // 匹配成功
                    hasMatch = true;

                    // 合并数据生成结果
                    R result = merger.apply(main, other);

                    // 应用结果过滤
                    if (resultFilter == null || resultFilter.test(result)) {
                        resultList.add(result);
                    }
                }

                // 如果是 Left Join (!isInnerJoin) 且没有匹配到任何副表记录
                if (!isInnerJoin && !hasMatch) {
                    // 生成左连接结果：副表部分为 null
                    R result = merger.apply(main, null);
                    if (resultFilter == null || resultFilter.test(result)) {
                        resultList.add(result);
                    }
                }
            }
        }

        return resultList;
    }
}

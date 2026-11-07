/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */

package apexsx.forge.core;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * TagStore 表示一个标签分类存储对象，采用模块(module) + 类型(type)两个维度进行标识管理
 * <p>
 * 该类为不可直接new的实体，全部实例由工厂方法{@link #of(Class, String, String, String)}统一创建、缓存复用；
 * 底层使用线程安全的ConcurrentHashMap做全局注册表，支持多线程并发读写；
 * 同一个唯一标识(TagKey)全局只会对应一个TagStore实例，重复调用of方法只会更新内部value值，不会新建对象。
 * </p>
 *
 * @author Terraqo
 */
public final class TagStore implements Comparable<TagStore> {
    /**
     * 全局标签存储注册表，线程安全，整个应用共享此静态容器
     * <p>
     * Key：使用该标签的业务Class对象，用于按业务类做第一层分组隔离
     * Value：内层Map，key为{@link TagKey}标签唯一复合键，value为对应的TagStore实例
     * </p>
     * 外层Map：key=业务类Class<?>，value=该类名下全部标签的映射表
     * 内层Map：key=TagKey(useClass+module+type的复合唯一键)，value=TagStore实例
     */
    private static final Map<Class<?>, Map<TagKey, TagStore>> STORE = new ConcurrentHashMap<>();

    /**
     * 标签的复合唯一主键，封装了 useClass、module、type 三个标识维度
     */
    private final TagKey key;

    /**
     * 标签存储的业务字符串值，volatile保证多线程下可见性，允许运行时修改
     */
    private volatile String value;

    /**
     * 私有构造函数，禁止外部直接new实例；只能通过静态工厂方法of创建对象
     *
     * @param key   标签复合唯一键，不可为null
     * @param value 标签初始存储字符串值
     */
    private TagStore(TagKey key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 工厂方法：创建或者获取TagStore标签实例
     * <p>
     * 根据【使用类 + 模块名 + 类型名】组装TagKey，调用重载方法完成实例获取/创建；
     * 如果该组合标识已经存在，则更新已有实例的value，返回原有对象；不存在则新建并注册到全局STORE。
     * </p>
     *
     * @param useClass 使用该标签的业务类，作为分组隔离维度
     * @param module   所属模块唯一标识符，业务模块名称
     * @param type     分类类型唯一标识符，模块下具体标签类型
     * @param value    需要存入标签的字符串值
     * @return 返回注册/更新后的TagStore实例，全局单例复用
     */
    public static TagStore of(Class<?> useClass, String module, String type, String value) {
        return of(TagKey.of(useClass, module, type), value);
    }

    /**
     * 工厂方法重载：基于已经构建好的{@link TagKey}复合键创建或者获取TagStore实例
     * <p>
     * 逻辑说明：
     * 1. 先从全局STORE中获取useClass对应的内层Map，如果不存在则新建ConcurrentHashMap放入外层STORE；
     * 2. 调用Map.compute原子方法：如果key已经存在，则更新旧实例内部value；key不存在则新建TagStore实例存入内层Map；
     * 3. compute是原子操作，避免并发场景下的竞态条件，保证线程安全。
     * </p>
     *
     * @param key   预构建完成的标签复合唯一主键
     * @param value 设置/更新的标签存储字符串
     * @return 对应的TagStore实例，复用已有或者新创建
     */
    public static TagStore of(TagKey key, String value) {
        // 外层Map：获取useClass对应的内层标签Map，不存在则新建ConcurrentHashMap实例
        Map<TagKey, TagStore> innerMap = STORE.computeIfAbsent(key.useClass(), k -> new ConcurrentHashMap<>());

        /*
         * compute为原子更新操作：
         * 入参tagKey：内层Map的key即TagKey；oldStore：map中旧值，null代表key尚未存在
         * 分支1 oldStore != null：实例已存在，调用value()更新内部存储的字符串，返回旧实例
         * 分支2 oldStore == null：无此实例，new创建TagStore对象返回，map会自动存入该新对象
         */
        return innerMap.compute(key, (tagKey, oldStore) -> {
            if (oldStore != null) {
                oldStore.value(value);
                return oldStore;
            }
            return new TagStore(tagKey, value);
        });
    }

    /**
     * 获取全局所有已注册的TagStore标签实例总数量
     * <p>
     * 遍历外层STORE所有value（各个业务类对应的内层Map），累加每个内层Map的size，得到全部标签总数
     * </p>
     *
     * @return int，全部TagStore实例合计数目
     */
    public static int count() {
        return STORE.values().stream()
                .mapToInt(Map::size)
                .sum();
    }

    /**
     * 查询指定业务类useClass下全部注册的TagStore标签集合
     *
     * @param useClass 使用标签的目标业务Class，分组维度
     * @return 返回ArrayList封装的标签列表；当该类没有注册任何标签，返回{@link Collections#emptyList()}空集合
     */
    public static List<TagStore> list(Class<?> useClass) {
        Map<TagKey, TagStore> inner = STORE.get(useClass);
        if (inner == null) {
            return Collections.emptyList();
        }
        // 将map中全部TagStore值复制为ArrayList返回，避免直接返回内部集合，防止外部修改破坏底层存储
        return new ArrayList<>(inner.values());
    }

    /**
     * 查询【指定业务类 + 指定模块】下所有TagStore标签实例列表
     * <p>
     * 在useClass对应的内层Map中过滤，只保留module名称匹配的标签对象
     * </p>
     *
     * @param useClass 使用标签的业务类对象
     * @param module   需要过滤匹配的模块标识符名称
     * @return 过滤后的标签List集合；无匹配项返回空集合
     */
    public static List<TagStore> list(Class<?> useClass, String module) {
        Map<TagKey, TagStore> inner = STORE.get(useClass);
        if (inner == null) {
            return Collections.emptyList();
        }
        return inner.values().stream()
                // 使用Objects.equals做判等，规避module为null时直接调用equals空指针风险
                .filter(e -> Objects.equals(module, e.key().module().name()))
                .collect(Collectors.toList());
    }

    /**
     * 根据三元组(useClass,module,type)精确查询获取TagStore实例
     *
     * @param useClass 使用标签的业务类
     * @param module   模块唯一标识符
     * @param type     类型唯一标识符
     * @return 匹配到返回TagStore实例；不存在则返回null
     */
    public static TagStore get(Class<?> useClass, String module, String type) {
        // 组装查询用的目标复合主键
        TagKey targetKey = TagKey.of(useClass, module, type);
        Map<TagKey, TagStore> inner = STORE.get(useClass);
        if (inner == null) {
            return null;
        }
        return inner.get(targetKey);
    }

    /**
     * 实现Comparable接口，支持TagStore对象之间的排序；直接委托给内部TagKey的compareTo比较逻辑
     *
     * @param o 待比较的另一个TagStore对象
     * @return int 比较结果，小于0、等于0、大于0分别代表小于、等于、大于
     */
    @Override
    public int compareTo(TagStore o) {
        return this.key.compareTo(o.key);
    }

    /**
     * 获取当前标签实例的复合唯一主键{@link TagKey}
     *
     * @return 不可修改的TagKey对象
     */
    public TagKey key() {
        return key;
    }

    /**
     * 获取标签内部存储的字符串value值
     *
     * @return 当前保存的value字符串
     */
    public String value() {
        return value;
    }

    /**
     * 修改更新标签存储的value值；成员变量value使用volatile，多线程修改具备可见性
     *
     * @param value 新的字符串存储值
     */
    public void value(String value) {
        this.value = value;
    }
}

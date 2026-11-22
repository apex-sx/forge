/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author ApexSX
 */
package io.github.apex_sx.forge.core;

import java.lang.annotation.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * SPI扩展注册中心，基于JDK ServiceLoader封装
 * <p>
 * 核心能力：
 * <ol>
 *     <li>扫描SPI接口全部实现类，解析{@link Tag}注解元数据</li>
 *     <li>根据order字段自动排序扩展列表，order相同时保持SPI声明顺序</li>
 *     <li>支持多种查询方式：全量加载、ID查询、实现类类型查询、分类分组查询</li>
 *     <li>元数据全局缓存，避免重复扫描SPI；提供缓存清除接口用于单元测试/热重载</li>
 * </ol>
 * <p>
 * 使用示例：
 * <pre>{@code
 * Extension<MyExtension> registry = Extension.of(MyExtension.class);
 * List<MyExtension> all = registry.loadAll();
 * Optional<MyExtension> byId = registry.loadById("demo");
 * }</pre>
 *
 * @param <S> SPI接口类型
 * @author ApexSX
 */
public final class Extension<S> {
    /**
     * 全局元数据缓存
     * key：SPI接口Class对象
     * value：对应接口的扩展注册器实例
     * <p>ConcurrentHashMap保障多线程安全；computeIfAbsent实现懒加载，只扫描一次SPI</p>
     */
    private static final ConcurrentHashMap<Class<?>, Extension<?>> CACHE = new ConcurrentHashMap<>();
    /**
     * 默认分类名称，未标注{@link Tag}或{@link Tag#classify()}为空白时使用
     */
    private static final String DEFAULT_CLASSIFY = "default";

    /**
     * 清空全部全局SPI元数据缓存
     * <p>
     * 清除后再次调用{@link #of(Class)}会重新扫描SPI并构建新的注册器；
     * 已经被业务代码持有的注册器实例及其缓存的单例对象不受影响，需重新获取才能生效。
     * 注意：与{@link #of(Class)}并发调用时不具备原子性，正在扫描的线程可能在清理完成后
     * 把基于旧状态构建的注册器写回缓存，热重载场景应在无并发访问时调用。
     * 主要用于单元测试、插件热重载场景；业务线上不建议频繁调用。
     */
    public static void clearCache() {
        CACHE.clear();
    }

    /**
     * 清除指定SPI接口的注册缓存，用于热重载
     * <p>
     * 注意：与{@link #of(Class)}并发调用时不具备原子性，正在扫描的线程可能在清理完成后
     * 把旧注册器写回缓存，热重载场景应在无并发访问时调用。
     *
     * @param extensionInterface SPI接口Class
     */
    public static void clearCache(Class<?> extensionInterface) {
        CACHE.remove(extensionInterface);
    }

    /**
     * 获取SPI接口对应的扩展注册器，使用全局缓存，只扫描一次SPI
     * <p>
     * 扫描使用SPI接口自身的类加载器，不依赖线程上下文类加载器；
     * 接口由bootstrap加载器加载时回退到线程上下文类加载器。
     *
     * @param extensionInterface SPI接口Class
     * @param <S>                SPI接口类型
     * @return Extension实例
     * @throws NullPointerException      当extensionInterface为null时抛出
     * @throws IllegalStateException     当实现类重复、id为空白或id重复时抛出
     * @throws ServiceConfigurationError 当实现类缺少public无参构造或无法加载时抛出
     */
    @SuppressWarnings("unchecked")
    public static <S> Extension<S> of(Class<S> extensionInterface) {
        Objects.requireNonNull(extensionInterface, "The extension interface can not be null.");
        return (Extension<S>) CACHE.computeIfAbsent(extensionInterface, key -> {
            // 显式指定接口的类加载器，避免线程上下文类加载器变化影响扫描结果与缓存归属
            ClassLoader classLoader = extensionInterface.getClassLoader();
            ServiceLoader<S> serviceLoader = classLoader == null
                    ? ServiceLoader.load(extensionInterface)
                    : ServiceLoader.load(extensionInterface, classLoader);
            // 1.遍历SPI提供者封装为Entry，按order升序排序；order越小越靠前，order相同时保持SPI声明顺序
            // 实现类缺少public无参构造时，ServiceLoader会在迭代阶段抛出ServiceConfigurationError
            List<Entry<S>> entries = serviceLoader.stream()
                    .map(provider -> new Entry<>(provider, provider.type().getAnnotation(Tag.class)))
                    .sorted(Comparator.comparingInt(Entry::order))
                    .toList();
            // 2.构建Class -> Entry映射，检测重复实现类
            Map<Class<?>, Entry<S>> typeMapping = HashMap.newHashMap(entries.size());
            for (Entry<S> entry : entries) {
                Class<? extends S> type = entry.type();
                if (typeMapping.putIfAbsent(type, entry) != null) {
                    throw new IllegalStateException("Duplicate extension implementation meta: " + type.getName());
                }
            }
            // 3.构建id -> Entry映射；未标注id的实现不参与id查询，id为空白属于配置错误
            Map<String, Entry<S>> idMapping = HashMap.newHashMap(entries.size());
            for (Entry<S> entry : entries) {
                String id = entry.id();
                if (id == null) {
                    continue;
                }
                if (id.isBlank()) {
                    throw new IllegalStateException("The extension id can not be blank, meta: " + entry.type().getName());
                }
                Entry<S> conflict = idMapping.putIfAbsent(id, entry);
                if (conflict != null) {
                    throw new IllegalStateException("Duplicate extension id [" + id + "], conflict meta: "
                            + conflict.type().getName() + " vs " + entry.type().getName());
                }
            }
            // 4.按照classify字段分组，分组内部保持order顺序
            Map<String, List<Entry<S>>> classifyMapping = entries.stream()
                    .collect(Collectors.groupingBy(
                            Entry::classify,
                            Collectors.toUnmodifiableList()
                    ));
            return new Extension<>(
                    entries,
                    Collections.unmodifiableMap(typeMapping),
                    Collections.unmodifiableMap(idMapping),
                    Collections.unmodifiableMap(classifyMapping)
            );
        });
    }

    /**
     * 全部扩展元数据条目，已经按order升序排好序
     */
    private final List<Entry<S>> extensionEntries;
    /**
     * 实现Class -> 扩展条目映射表，用于根据实现类Class查找扩展
     */
    private final Map<Class<?>, Entry<S>> typeMappingTable;
    /**
     * 扩展ID -> 扩展条目映射表，用于根据id查找扩展
     * <p>约束：同一个SPI接口下{@link Tag#id()}不能为空白且不能重复，否则加载时抛出异常</p>
     */
    private final Map<String, Entry<S>> idMappingTable;
    /**
     * classify分类名称 -> 同分类扩展条目列表，分组存储，支持按分类批量获取扩展
     */
    private final Map<String, List<Entry<S>>> classifyMappingTable;

    /**
     * 私有构造器，外部禁止new；使用静态工厂方法{@link #of(Class)}创建实例
     *
     * @param extensionEntries     排序后的扩展元数据列表
     * @param typeMappingTable     Class到Entry映射
     * @param idMappingTable       id到Entry映射
     * @param classifyMappingTable 分类分组映射
     */
    private Extension(List<Entry<S>> extensionEntries,
                      Map<Class<?>, Entry<S>> typeMappingTable,
                      Map<String, Entry<S>> idMappingTable,
                      Map<String, List<Entry<S>>> classifyMappingTable) {
        this.extensionEntries = extensionEntries;
        this.typeMappingTable = typeMappingTable;
        this.idMappingTable = idMappingTable;
        this.classifyMappingTable = classifyMappingTable;
    }

    /**
     * 加载全部扩展实例（保持order排序）
     * <p>
     * 单例扩展多次加载返回同一实例，多例扩展每次加载都创建新实例。
     *
     * @return 所有SPI实现实例列表，顺序与order升序一致
     */
    public List<S> loadAll() {
        return extensionEntries.stream().map(Entry::getInstance).toList();
    }

    /**
     * 根据扩展唯一ID加载实例
     *
     * @param id {@link Tag#id()}的值
     * @return Optional，id为null或找不到对应扩展时返回Optional.empty()
     */
    public Optional<S> loadById(String id) {
        Entry<S> entry = idMappingTable.get(id);
        return entry == null ? Optional.empty() : Optional.of(entry.getInstance());
    }

    /**
     * 根据实现类Class查找扩展实例
     *
     * @param type SPI实现类Class
     * @return Optional，type为null或找不到对应扩展时返回Optional.empty()
     */
    public Optional<S> loadByType(Class<?> type) {
        Entry<S> entry = typeMappingTable.get(type);
        return entry == null ? Optional.empty() : Optional.of(entry.getInstance());
    }

    /**
     * 根据分类classify批量加载该分类全部扩展
     *
     * @param classify {@link Tag#classify()}分类标识
     * @return 该分类所有实例列表；无匹配返回空集合
     */
    public List<S> loadByClassify(String classify) {
        return loadByClassify(classify, null);
    }

    /**
     * 根据分类加载扩展，附加Entry过滤器Predicate
     *
     * @param classify  分类名称
     * @param predicate 扩展Entry过滤条件；传null则不做过滤
     * @return 过滤后的实例集合，无匹配返回空集合（不会返回null）
     */
    public List<S> loadByClassify(String classify, Predicate<Entry<S>> predicate) {
        List<Entry<S>> entries = classifyMappingTable.get(classify);
        if (entries == null) {
            return List.of();
        }
        if (predicate == null) {
            return entries.stream().map(Entry::getInstance).toList();
        }
        return entries.stream().filter(predicate).map(Entry::getInstance).toList();
    }

    /**
     * SPI扩展元数据包装条目
     * <p>
     * 封装单个SPI扩展实现的全部元信息，解析{@link Tag}注解，
     * 封装实例创建逻辑，支持单例/多例两种实例化策略。
     * <p>
     * 由{@link ServiceLoader.Provider}构造，实现类必须存在public无参构造，否则由ServiceLoader抛出
     * {@link ServiceConfigurationError}；
     * 单例模式使用volatile + 双重检查锁实现延迟初始化线程安全。
     *
     * @param <S> SPI接口类型
     */
    public static final class Entry<S> {
        /**
         * JDK SPI提供器，负责生产实现类实例
         */
        private final ServiceLoader.Provider<S> provider;
        /**
         * 扩展唯一ID，来自{@link Tag#id()}；无注解时值为null
         */
        private final String id;
        /**
         * 扩展分类，来自{@link Tag#classify()}；未标注或为空白时取默认值default
         */
        private final String classify;
        /**
         * 排序order，来自{@link Tag#order()}；无注解默认0
         */
        private final int order;
        /**
         * 是否单例，来自{@link Tag#singleton()}；无注解默认true
         */
        private final boolean singleton;
        /**
         * 缓存已经实例化的扩展对象
         * <p>volatile：禁止指令重排序，保障双重检查锁线程安全；仅singleton=true时生效</p>
         */
        private volatile S instance;

        /**
         * 私有构造函数
         *
         * @param provider SPI提供者
         * @param tag      实现类上的注解，为null时使用默认配置
         */
        private Entry(ServiceLoader.Provider<S> provider, Tag tag) {
            this.provider = provider;
            if (tag != null) {
                this.id = tag.id();
                String classify = tag.classify();
                this.classify = classify.isBlank() ? DEFAULT_CLASSIFY : classify;
                this.order = tag.order();
                this.singleton = tag.singleton();
            } else {
                // 未添加Tag注解，使用默认配置
                this.id = null;
                this.classify = DEFAULT_CLASSIFY;
                this.order = 0;
                this.singleton = true;
            }
        }

        /**
         * 获取扩展实例对象
         * <ul>
         * <li>singleton=true：双重检查锁延迟初始化，第一次调用才创建实例，后续返回缓存对象</li>
         * <li>singleton=false：每次调用直接创建全新实例</li>
         * </ul>
         *
         * @return SPI扩展实现实例
         * @throws ServiceConfigurationError 实现类无法实例化时抛出
         */
        public S getInstance() {
            if (!singleton) {
                return provider.get();
            }
            if (instance == null) {
                synchronized (this) {
                    if (instance == null) {
                        instance = provider.get();
                    }
                }
            }
            return instance;
        }

        /**
         * 获取扩展实现Class对象
         *
         * @return 实现类Class，即SPI接口的具体实现类型
         */
        public Class<? extends S> type() {
            return provider.type();
        }

        /**
         * 获取扩展唯一ID
         *
         * @return id，没有注解返回null
         */
        public String id() {
            return id;
        }

        /**
         * 获取扩展分类标识
         *
         * @return classify分类名称
         */
        public String classify() {
            return classify;
        }

        /**
         * 获取排序优先级数值
         *
         * @return order值，越小越靠前
         */
        public int order() {
            return order;
        }

        /**
         * 是否开启单例模式
         *
         * @return true=单例；false=每次新建对象
         */
        public boolean singleton() {
            return singleton;
        }
    }

    /**
     * SPI扩展实现标记注解
     * <p>
     * 标注在SPI接口的实现类上，提供扩展元数据：唯一ID、分类、排序顺序、是否单例。
     * 配合{@link Extension}、{@link Entry}完成扩展加载管理。
     * <p>
     * 约束：
     * <ul>
     *     <li>实现类必须提供public无参构造函数，否则加载时由ServiceLoader抛出{@link ServiceConfigurationError}</li>
     *     <li>注解不会被子类继承，只有实现类自身标注时才会生效</li>
     * </ul>
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Tag {
        /**
         * 扩展唯一ID
         * <p>用于{@link Extension#loadById(String)}根据id查找扩展实现，同一个SPI接口下ID不能重复；
         * 空白字符串属于配置错误，加载时抛出{@link IllegalStateException}。</p>
         *
         * @return 非空字符串唯一标识
         */
        String id();

        /**
         * 扩展分类标识
         * <p>用于分组管理扩展，可通过{@link Extension#loadByClassify(String)}按分类批量加载扩展；
         * 空白字符串按默认分类处理。</p>
         *
         * @return 分类名称，默认值default
         */
        String classify() default "default";

        /**
         * 加载排序优先级
         * <p>扩展列表会按照order值从小到大升序排列；数值越小优先级越高，排在列表前面。</p>
         *
         * @return 排序int值，默认0
         */
        int order() default 0;

        /**
         * 是否为单例模式
         * <ul>
         *     <li>true：全局只创建一个实例，多次调用getInstance返回同一个对象（双重检查锁延迟初始化）</li>
         *     <li>false：每次调用{@link Entry#getInstance()}都会创建全新对象</li>
         * </ul>
         *
         * @return 是否单例，默认为true
         */
        boolean singleton() default true;
    }
}
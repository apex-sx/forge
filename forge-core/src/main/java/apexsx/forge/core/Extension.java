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
 *     <li>根据order字段自动排序扩展列表</li>
 *     <li>支持多种查询方式：全量加载、ID查询、实现类类型查询、分类分组查询</li>
 *     <li>元数据全局缓存，避免重复扫描SPI；提供缓存清除接口用于单元测试/热重载</li>
 * </ol>
 * <p>
 * 使用示例：
 * <pre>{@code
 * ExtensionRegistry<MyExtension> registry = ExtensionRegistry.of(MyExtension.meta);
 * List<MyExtension> all = registry.loadAll();
 * Optional<MyExtension> byId = registry.loadById("demo");
 * }</pre>
 *
 * @param <S> SPI接口类型
 * @author Terraqo
 */
@SuppressWarnings("unchecked")
public final class Extension<S> {

    /**
     * 全局元数据缓存
     * key：SPI接口Class对象
     * value：对应接口的扩展注册器实例
     * <p>ConcurrentHashMap保障多线程安全；computeIfAbsent实现懒加载，只扫描一次SPI</p>
     */
    private static final ConcurrentHashMap<Class<?>, Extension<?>> CACHE = new ConcurrentHashMap<>();

    /**
     * JDK SPI加载器实例，持有扫描到的所有实现提供者
     */
    private final ServiceLoader<S> serviceLoader;

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
     * <p>约束：同一个SPI接口下@ExtensionSpec(id)不能重复，否则collect会抛出重复key异常</p>
     */
    private final Map<String, Entry<S>> idMappingTable;

    /**
     * classify分类名称 -> 同分类扩展条目列表，分组存储，支持按分类批量获取扩展
     */
    private final Map<String, List<Entry<S>>> classifyMappingTable;

    /**
     * 私有构造器，外部禁止new；使用静态工厂方法{@link #of(Class)}创建实例
     *
     * @param serviceLoader        SPI加载器
     * @param extensionEntries     排序后的扩展元数据列表
     * @param typeMappingTable     Class到Entry映射
     * @param idMappingTable       id到Entry映射
     * @param classifyMappingTable 分类分组映射
     */
    private Extension(ServiceLoader<S> serviceLoader,
                      List<Entry<S>> extensionEntries,
                      Map<Class<?>, Entry<S>> typeMappingTable,
                      Map<String, Entry<S>> idMappingTable,
                      Map<String, List<Entry<S>>> classifyMappingTable) {
        this.serviceLoader = serviceLoader;
        this.extensionEntries = extensionEntries;
        this.idMappingTable = idMappingTable;
        this.typeMappingTable = typeMappingTable;
        this.classifyMappingTable = classifyMappingTable;
    }

    /**
     * 获取SPI接口对应的扩展注册器，使用全局缓存，只扫描一次SPI
     *
     * @param extensionInterface SPI接口Class
     * @return ExtensionRegistry实例
     */
    public static <S> Extension<S> of(Class<S> extensionInterface) {
        return (Extension<S>) CACHE.computeIfAbsent(extensionInterface, key -> {
            ServiceLoader<S> serviceLoader = ServiceLoader.load(extensionInterface);
            // 1.遍历SPI提供者封装为ExtensionEntry，按order升序排序；order越小越靠前
            List<Entry<S>> metas = serviceLoader.stream()
                    .map(provider -> {
                        Class<? extends S> extensionClass = provider.type();
                        try {
                            // 校验SPI实现类必须存在无参构造
                            extensionClass.getDeclaredConstructor();
                        } catch (NoSuchMethodException e) {
                            throw new RuntimeException("The extension implementation meta missing no-arg constructor: " + extensionClass.getName(), e);
                        }
                        // 读取类上的ExtensionSpec注解
                        return new Entry<>(provider, extensionClass.getAnnotation(Tag.class));
                    })
                    .sorted(Comparator.comparingInt(Entry::order))
                    .toList();

            // 2.构建Class -> Entry映射，检测重复实现类
            Map<Class<?>, Entry<S>> typeMapping = new HashMap<>();
            for (Entry<S> entry : metas) {
                Class<?> type = entry.type();
                if (typeMapping.containsKey(type)) {
                    throw new IllegalStateException("Duplicate extension implementation meta: " + type.getName());
                }
                typeMapping.put(type, entry);
            }

            // 3.构建id -> Entry映射；过滤id=null，检测id重复
            Map<String, Entry<S>> idMapping = new HashMap<>();
            for (Entry<S> entry : metas) {
                String id = entry.id();
                if (id == null) {
                    continue;
                }
                if (idMapping.containsKey(id)) {
                    throw new IllegalStateException("Duplicate extension id [" + id + "], conflict meta: "
                            + idMapping.get(id).type().getName() + " vs " + entry.type().getName());
                }
                idMapping.put(id, entry);
            }

            // 4.按照classify字段分组，内部集合转为不可变List
            Map<String, List<Entry<S>>> classifyMapping = metas.stream()
                    .collect(Collectors.groupingBy(
                            Entry::classify,
                            Collectors.toUnmodifiableList()
                    ));

            return new Extension<>(
                    serviceLoader,
                    metas,
                    Collections.unmodifiableMap(typeMapping),
                    Collections.unmodifiableMap(idMapping),
                    Collections.unmodifiableMap(classifyMapping)
            );
        });
    }

    /**
     * 清空全部全局SPI元数据缓存
     * <p>
     * 注意：只清除内存中的Registry元数据缓存；已经创建出来的单例实例对象不会被GC，需要业务侧释放引用。
     * 主要用于单元测试、插件热重载场景；业务线上不建议频繁调用。
     */
    public static void clearCache() {
        CACHE.clear();
    }

    /**
     * 清除指定SPI接口的注册缓存，用于热重载
     *
     * @param spiInterface SPI接口class
     */
    public static void clearCache(Class<?> spiInterface) {
        CACHE.remove(spiInterface);
    }

    /**
     * 加载全部扩展实例（保持order排序）
     *
     * @return 所有SPI实现实例列表，顺序与order升序一致
     */
    public List<S> loadAll() {
        return extensionEntries.stream().map(Entry::getInstance).toList();
    }

    /**
     * 根据扩展唯一ID加载实例
     *
     * @param id @ExtensionSpec#id()的值
     * @return Optional，找不到返回Optional.empty()
     */
    public Optional<S> loadById(String id) {
        return Optional.ofNullable(idMappingTable.get(id)).map(Entry::getInstance);
    }

    /**
     * 根据实现类Class查找扩展实例
     *
     * @param type SPI实现类Class
     * @return Optional，找不到返回Optional.empty()
     */
    public Optional<S> loadByType(Class<?> type) {
        return Optional.ofNullable(typeMappingTable.get(type)).map(Entry::getInstance);
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
        Predicate<Entry<S>> actualPredicate = predicate == null ? e -> true : predicate;
        return Optional.ofNullable(classifyMappingTable.get(classify))
                .map(l -> l.stream().filter(actualPredicate).map(Entry::getInstance).toList())
                .orElse(Collections.emptyList());
    }

    /**
     * SPI扩展实现标记注解
     * <p>
     * 标注在SPI接口的实现类上，提供扩展元数据：唯一ID、分类、排序顺序、是否单例。
     * 配合{@link Extension}、{@link Entry}完成扩展加载管理。
     * <p>
     * 约束：实现类必须提供无参构造函数，否则加载时抛出运行时异常。
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface Tag {
        /**
         * 扩展唯一ID
         * <p>用于{@link Extension#loadById(String)}根据id查找扩展实现，同一个SPI接口下ID不能重复。</p>
         *
         * @return 非空字符串唯一标识
         */
        String id();

        /**
         * 扩展分类标识
         * <p>用于分组管理扩展，可通过{@link Extension#loadByClassify(String)}按分类批量加载扩展。</p>
         *
         * @return 分类名称，默认值 default
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

    /**
     * SPI扩展元数据包装条目
     * <p>
     * 封装单个SPI扩展实现的全部元信息，解析{@link Tag}注解，
     * 封装实例创建逻辑，支持单例/多例两种实例化策略。
     * <p>
     * 由ServiceLoader#Provider构造，校验实现类必须有无参构造；
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
         * 扩展分类，来自{@link Tag#classify()}；无注解默认"default"
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
         * @param tag      实现类上的注解，可为null（没有标注注解）
         */
        private Entry(ServiceLoader.Provider<S> provider, Tag tag) {
            this.provider = provider;
            if (tag != null) {
                this.id = tag.id();
                this.classify = tag.classify();
                this.order = tag.order();
                this.singleton = tag.singleton();
            } else {
                // 未添加ExtensionSpec注解，使用默认配置
                this.id = null;
                this.classify = "default";
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
         * @throws RuntimeException 实例化失败抛出运行时异常
         */
        public S getInstance() {
            if (singleton) {
                if (instance == null) {
                    synchronized (this) {
                        if (instance == null) {
                            try {
                                S extension = provider.get();
                                instance = extension;
                                return extension;
                            } catch (Exception e) {
                                throw new RuntimeException("Failed to create extension instance, meta: " + provider.type(), e);
                            }
                        }
                    }
                }
                return instance;
            } else {
                // 非单例，每次新建实例
                return provider.get();
            }
        }

        /**
         * 获取扩展实现Class对象
         *
         * @return 实现类Class
         */
        public Class<?> type() {
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
}
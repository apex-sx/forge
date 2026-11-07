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

import com.github.terraqo.forge.lang.ref.ReferentialThreadLocal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 国际化资源管理器，用于根据当前线程的本地化配置加载对应的资源文件。
 *
 * <p>主要功能包括：
 * <ul>
 *     <li>维护线程级别的Locale隔离和管理（使用软引用存储）</li>
 *     <li>提供基于基础名称和Locale获取ResourceBundle的方法</li>
 *     <li>支持显式设置当前线程的本地化配置</li>
 * </ul>
 *
 * <p>核心特性说明：
 * <ol>
 *     <li>实例缓存：使用WeakHashMap做实例缓存，以baseName为key，避免重复创建管理器；key为弱引用，便于内存回收</li>
 *     <li>线程隔离Locale：ReferentialThreadLocal软引用存储Locale，每个线程拥有独立的本地化配置，不会线程互相污染</li>
 *     <li>多Jar资源合并：内置AggregatedControl，支持多个jar下同名properties资源按__ORDER__排序合并，大序号覆盖小序号</li>
 *     <li>Locale查找优先级：传入参数Locale > 当前线程绑定Locale > JVM系统默认Locale</li>
 *     <li>资源回退：当指定locale资源找不到时，交由JDK ResourceBundle执行语言回退（zh_CN → zh → 根资源）</li>
 * </ol>
 *
 * @author Terraqo
 */
public class International {
    /**
     * 聚合资源包控制类实例。
     * 替换JDK默认ResourceBundle.Control，实现多Jar包同名properties资源合并加载逻辑。
     * 全局单例复用。
     */
    public static final ResourceBundle.Control AGGREGATED_CONTROL = new AggregatedControl();

    /**
     * International实例缓存容器。
     * key：资源baseName字符串；value：International实例
     * 使用WeakHashMap：key为弱引用，当baseName字符串无其他强引用时允许被GC，防止内存泄漏。
     */
    private static final Map<String, International> CACHE = new WeakHashMap<>();

    /**
     * 当前线程的本地化配置，使用软引用存储，默认值为系统默认的本地化配置。
     * 通过 ReferentialThreadLocal 实现线程级别的 Locale 隔离和管理。
     * <p>
     * 软引用特性：当JVM内存紧张时，线程绑定的Locale对象可以被回收，避免ThreadLocal内存泄漏；
     * 每个业务线程可以独立setCurrentLocale，互不干扰。
     */
    protected final ReferentialThreadLocal<Locale> local = ReferentialThreadLocal.soft();

    /**
     * 国际化资源文件的基础名称。
     * 示例：META‑INF/i18n/messages，对应资源文件 messages_zh_CN.properties、messages_en_US.properties。
     * baseName是实例唯一标识，equals/hashCode以此字段做判断。
     */
    protected final String baseName;

    /**
     * 构造一个新的资源管理器，并指定资源文件的基础名称。
     * <p>私有构造，外部不能直接new，统一通过 {@link #of(String)} 获取实例，由CACHE做缓存复用。
     *
     * @param baseName 国际化资源文件的基础名称
     */
    private International(String baseName) {
        this.baseName = baseName;
    }

    /**
     * 根据基础名称创建对应的资源管理器实例。
     * <p>优先从CACHE缓存获取；缓存不存在则新建并存入缓存。
     *
     * @param baseName 国际化资源的基础名称，通常对应资源文件的路径和名称
     * @return 返回与基础名称关联的资源管理器实例
     * @throws IllegalArgumentException baseName为null或空白字符串抛出异常
     */
    public static International of(String baseName) {
        if (baseName == null || baseName.isBlank()) {
            throw new IllegalArgumentException("Resource bundle base name cannot be empty");
        }
        return CACHE.computeIfAbsent(baseName, International::new);
    }

    /**
     * 获取当前实例的资源文件的基础名称。
     *
     * @return 返回资源文件的基础名称
     */
    public String getBaseName() {
        return baseName;
    }

    /**
     * 设置当前线程使用的本地化配置。
     * <p>仅作用于调用该方法的当前线程；传入null不会执行set操作。
     *
     * @param locale 要设置的本地化配置
     */
    public void setCurrentLocale(Locale locale) {
        if (locale != null) {
            local.set(locale);
        }
    }

    /**
     * 使用当前实例的 baseName 和指定的 locale 获取资源文件对象。
     * 内部调用 {@link #findResourceBundle(Locale, String)}，复用当前对象baseName。
     *
     * @param locale 指定的本地化配置
     * @return 返回匹配的 ResourceBundle 对象，如果找不到则返回 null
     */
    public ResourceBundle findResourceBundle(Locale locale) {
        return this.findResourceBundle(locale, baseName);
    }

    /**
     * 获取与当前线程相关的本地化资源配置。
     * 如果传入的 locale 为空，则尝试从线程上下文中获取；
     * 如果仍为空，则使用系统默认的本地化配置。
     * <p>
     * 此方法按照以下顺序查找资源包：
     * 1. 使用传入的 locale 参数（如果非空）
     * 2. 使用线程本地存储的 locale（如果非空）
     * 3. 使用默认的 locale 获取资源包
     *
     * <p>异常处理：捕获MissingResourceException（资源包完全找不到），打印警告日志，返回null。
     *
     * @param locale   请求的本地化配置
     * @param baseName 基础名称，通常是包含语言代码的包路径
     * @return 返回匹配的 ResourceBundle 对象，如果找不到则返回 null
     */
    public ResourceBundle findResourceBundle(Locale locale, String baseName) {
        ResourceBundle resourceBundle = null;
        try {
            // 首先尝试使用传入的 locale 参数获取资源包
            if (locale != null) {
                resourceBundle = ResourceBundle.getBundle(baseName, locale, AGGREGATED_CONTROL);
            }
            // 如果没有传入 locale 或者获取失败，则尝试使用线程本地的 locale
            Locale threadLocale = local.get();
            if (resourceBundle == null && threadLocale != null) {
                resourceBundle = ResourceBundle.getBundle(baseName, threadLocale, AGGREGATED_CONTROL);
            }
            // 如果前面都失败了，则使用默认的 locale 获取资源包
            if (resourceBundle == null) {
                resourceBundle = ResourceBundle.getBundle(baseName, AGGREGATED_CONTROL);
            }
        } catch (MissingResourceException e) {
            // 记录资源包获取失败的日志
            if (Logger.isEnabledWarn()) {
                Logger.warn("Error getting ResourceBundle for baseName: " + baseName, e);
            }
        }
        return resourceBundle;
    }

    /**
     * 相等判断：只比较baseName，baseName相同代表同一个国际化管理器。
     *
     * @param object 待比较对象
     * @return true代表baseName一致
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof International that)) return false;
        return Objects.equals(baseName, that.baseName);
    }

    /**
     * hashCode仅使用baseName字段，与equals保持契约。
     *
     * @return hash值
     */
    @Override
    public int hashCode() {
        return Objects.hash(baseName);
    }

    /**
     * toString直接返回baseName，方便调试打印。
     *
     * @return baseName字符串
     */
    @Override
    public String toString() {
        return baseName;
    }

    /**
     * 聚合资源包提供程序，用于从多个JAR包中收集同名的资源文件并合并为一个ResourceBundle
     *
     * <p>实现要点：
     * <ul>
     *     <li>每个资源文件独立解析，再按 {@code __ORDER__} 升序合并，序号大的覆盖序号小的</li>
     *     <li>使用缓冲字符流和轻量解析器，避免 {@link Properties} 的同步与多次复制开销</li>
     *     <li>仅在 reload 时关闭连接缓存，其余情况复用 JAR 文件缓存</li>
     *     <li>使用 HashMap 存储键值，单键查找为 O(1)</li>
     * </ul>
     *
     * <p>合并规则详解：
     * <ol>
     *     <li>调用ClassLoader.getResources拿到所有jar中同名properties</li>
     *     <li>逐个解析每个properties得到Map；读取时设置超时；reload=true关闭url缓存</li>
     *     <li>按每个properties内__ORDER__数值升序排序；没有该key或解析异常则视作Integer.MAX_VALUE，排末尾</li>
     *     <li>循环putAll合并，后面（order更大）的key会覆盖前面的；最后移除__ORDER__元数据key</li>
     *     <li>构造自定义MapResourceBundle返回；解析不到任何资源返回null，交给JDK做locale回退</li>
     * </ol>
     *
     * @author Terraqo
     */
    private static final class AggregatedControl extends ResourceBundle.Control {
        /**
         * 支持的资源包格式常量，这里只处理properties格式。
         */
        private static final String FORMAT = "java.properties";
        /**
         * 资源文件后缀名。
         */
        private static final String SUFFIX = "properties";
        /**
         * 合并顺序键，值越小越先合并，未配置的资源文件排在最后且优先级最低。
         * <p>解析完成合并后，该key会从最终map移除，不会暴露给业务代码。
         */
        private static final String ORDER_KEY = "__ORDER__";
        /**
         * 复用 JDK 的属性资源包命名规则，避免每次调用 Control#getControl。
         * 用于把baseName+locale转成真实资源文件名。
         */
        private static final ResourceBundle.Control DELEGATE = ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES);
        /**
         * URL连接超时毫秒。
         */
        private static final int CONNECT_TIMEOUT = 5000;
        /**
         * URL读取超时毫秒。
         */
        private static final int READ_TIMEOUT = 10000;
        /**
         * HashMap初始容量默认值，减少扩容开销。
         */
        private static final int DEFAULT_CAPACITY = 64;

        /**
         * 私有构造，外部不能实例化，全局复用AGGREGATED_CONTROL单例。
         */
        private AggregatedControl() {
        }

        /**
         * 返回支持的资源包格式。本实现只支持properties。
         *
         * @param baseName 资源包的基础名称
         * @return 支持的格式列表，这里只支持properties格式
         */
        @Override
        public List<String> getFormats(String baseName) {
            return ResourceBundle.Control.FORMAT_PROPERTIES;
        }

        /**
         * 创建一个新的ResourceBundle实例，核心逻辑：多jar同名properties合并加载。
         *
         * @param baseName 资源包的基础名称
         * @param locale   本地化信息
         * @param format   资源包格式
         * @param loader   类加载器，用来getResources扫描全部jar
         * @param reload   是否重新加载；true时关闭url缓存，强制重读jar资源
         * @return ResourceBundle实例，未找到对应资源时返回null以允许语言回退
         */
        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) {
            // 只处理properties格式，其他格式直接返回null
            if (!FORMAT.equals(format)) {
                return null;
            }
            // 使用jdk delegate完成 baseName+locale → xxx_zh_CN.properties资源文件名转换
            String resourceName = DELEGATE.toResourceName(DELEGATE.toBundleName(baseName, locale), SUFFIX);
            Enumeration<URL> urls;
            try {
                // 获取类加载器下所有jar包中该资源的url集合（多jar同名资源全部拿到）
                urls = loader.getResources(resourceName);
            } catch (IOException e) {
                if (Logger.isEnabledWarn()) {
                    Logger.warn("Unable to locate " + resourceName, e);
                }
                return null;
            }
            // sources保存每一个properties解析出来的map，后续排序合并
            List<Map<String, String>> sources = new ArrayList<>(2);
            Map<String, String> data;
            // 遍历所有jar里找到的同名properties资源
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                try {
                    URLConnection connection = url.openConnection();
                    if (reload) {
                        // 仅在重新加载时禁用缓存，jar连接一旦禁用缓存就会重新打开并解析JAR文件
                        connection.setUseCaches(false);
                    }
                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setReadTimeout(READ_TIMEOUT);
                    // 使用工具类按UTF‑8解析properties输入流，得到键值map
                    try (InputStream stream = connection.getInputStream()) {
                        data = LineProcessor.loadProperties(stream, StandardCharsets.UTF_8, new HashMap<>(DEFAULT_CAPACITY));
                    }
                } catch (IOException e) {
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Unable to load properties from " + url + ", " + e.getMessage(), e);
                    }
                    return null;
                }
                // 非空map才加入待合并列表
                if (!data.isEmpty()) {
                    sources.add(data);
                }
            }
            int size = sources.size();
            if (size == 0) {
                // 返回null，让ResourceBundle按candidateLocales继续回退（zh_CN -> zh -> 根资源包）
                return null;
            }
            // 只有一份资源，直接去掉__ORDER__元key返回，不需要合并
            if (size == 1) {
                Map<String, String> properties = sources.getFirst();
                properties.remove(ORDER_KEY);
                return new MapResourceBundle(properties);
            }
            // 稳定排序，__ORDER__ 相同的资源文件保持 ClassLoader 的枚举顺序
            sources.sort(Comparator.comparingInt(properties -> {
                String order = properties.get(ORDER_KEY);
                if (order == null || order.isBlank()) {
                    // 没有order配置排到最后
                    return Integer.MAX_VALUE;
                }
                try {
                    return Integer.parseInt(order.trim());
                } catch (NumberFormatException e) {
                    if (Logger.isEnabledWarn()) {
                        Logger.warn("Invalid __ORDER__ value: " + order);
                    }
                    // order格式非法同样排末尾
                    return Integer.MAX_VALUE;
                }
            }));
            // 预估总容量，减少合并过程HashMap扩容
            int capacity = 0;
            for (Map<String, String> source : sources) {
                capacity += source.size();
            }
            // 依次putAll：后面循环的source（order更大）会覆盖前面的key，实现高优先级覆盖低优先级
            Map<String, String> merged = new HashMap<>(Math.max(DEFAULT_CAPACITY, capacity + (capacity >> 1)));
            for (Map<String, String> source : sources) {
                merged.putAll(source);
            }
            // 移除元数据key，不对外暴露
            merged.remove(ORDER_KEY);
            return new MapResourceBundle(merged);
        }
    }

    /**
     * 基于 HashMap 的资源包实现，键查找为 O(1)，且不产生额外的中间数组。
     * <p>区别于JDK的PropertyResourceBundle：底层直接使用HashMap，不使用Properties对象；内部map设为不可变，实例构造后只读。
     *
     * @author Terraqo
     */
    private static final class MapResourceBundle extends ResourceBundle {
        /**
         * 国际化键值对，构造后不再修改，不可变map。
         */
        private final Map<String, String> values;
        /**
         * 键集合视图，避免每次 getKeys 重新构建，不可变集合。
         */
        private final Set<String> keys;

        /**
         * 构造函数。传入普通map，内部包装为不可变，防止外部修改。
         *
         * @param values 国际化键值对
         */
        private MapResourceBundle(Map<String, String> values) {
            this.values = Collections.unmodifiableMap(values);
            this.keys = Collections.unmodifiableSet(values.keySet());
        }

        /**
         * 获取指定键对应的国际化内容。ResourceBundle回调方法。
         *
         * @param key 国际化键
         * @return 国际化内容，不存在时返回null
         */
        @Override
        protected Object handleGetObject(String key) {
            return key == null ? null : values.get(key);
        }

        /**
         * 返回所有key的枚举。复用预构建keys集合。
         *
         * @return key枚举
         */
        @Override
        public Enumeration<String> getKeys() {
            return Collections.enumeration(keySet());
        }

        /**
         * 返回键集合，供 ResourceBundle#getKeys 复用。JDK8+ ResourceBundle回调方法。
         *
         * @return 不可变的键集合
         */
        @Override
        protected Set<String> handleKeySet() {
            return keys;
        }
    }
}

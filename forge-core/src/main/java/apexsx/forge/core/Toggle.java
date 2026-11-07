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


import com.github.terraqo.forge.lang.struct.Bounds;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 功能开关类，用于控制功能的开启和关闭
 * 支持国际化描述信息，可以从资源文件中加载对应的描述
 * 提供层级化的开关管理机制，允许子开关依赖于父开关状态
 *
 * @author Terraqo
 */
public final class Toggle {
    /**
     * 存储所有功能开关实例的映射表，使用ConcurrentHashMap保证线程安全
     * 键为Category类型表示开关的唯一标识，值为Toggle实例
     */
    private static final Map<TagKey, Toggle> TOGGLES_LIST = new ConcurrentHashMap<>();
    /**
     * 父功能开关实例，用于实现功能开关的层级关系
     * 当父开关关闭时，即使当前开关开启，也会被视为关闭状态
     */
    private final Toggle parent;
    /**
     * 功能开关的唯一标识符，用于在TOGGLES_LIST中查找对应实例
     */
    private final TagKey uniqueIdentifier;
    /**
     * 功能开关的描述信息，支持国际化显示
     */
    private final InternationalMessage description;
    /**
     * 功能开关的默认状态，用于初始化和恢复操作
     */
    private final boolean defaultStatus;
    /**
     * 当前功能开关的状态，使用AtomicBoolean确保线程安全的操作
     * 注意：toggle字段名称可能会与toggle()方法产生混淆，但在内部使用无碍
     */
    private final AtomicBoolean toggle = new AtomicBoolean(false);

    /**
     * 私有构造函数，初始化功能开关的各个属性
     * 所有参数都必须非空（除了parent可能为null）
     *
     * @param parent           父功能开关实例，可为null表示顶级开关
     * @param uniqueIdentifier 功能开关的唯一标识符
     * @param description      功能开关的描述信息
     * @param defaultStatus    功能开关的默认状态
     */
    private Toggle(Toggle parent, TagKey uniqueIdentifier, InternationalMessage description, boolean defaultStatus) {
        this.parent = parent;
        this.uniqueIdentifier = uniqueIdentifier;
        this.description = description;
        this.defaultStatus = defaultStatus;
        this.toggle.set(defaultStatus);  // 初始化开关状态为默认状态
    }

    /**
     * 创建一个新的 Builder 实例
     *
     * @return 新的 Builder 实例，用于构建Toggle对象
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 获取功能开关实例的总数
     *
     * @return 当前已注册的功能开关实例数量
     */
    public static int getCount() {
        return TOGGLES_LIST.size();
    }

    /**
     * 获取所有功能开关列表，应用指定的转换和过滤条件
     * 此方法返回全部结果，不进行分页限制
     *
     * @param mapper 用于转换Toggle对象的映射函数
     * @param filter 用于过滤功能开关的谓词函数，可为null表示不过滤
     * @return 包含转换后对象的列表
     */
    public static <T> List<T> list(Function<Toggle, T> mapper, Predicate<Toggle> filter) {
        return Toggle.list(0, Integer.MAX_VALUE, mapper, filter);
    }

    /**
     * 分页获取功能开关列表
     * 使用流式API处理数据，延迟计算提高性能
     *
     * @param offset 起始偏移量，从0开始
     * @param count  需要获取的数量
     * @param mapper 用于转换Toggle对象的映射函数
     * @param filter 用于过滤功能开关的谓词函数，可为null表示不过滤
     * @return 包含转换后对象的列表
     * @throws IllegalArgumentException 当offset或count参数无效时抛出异常
     */
    public static <T> List<T> list(int offset, int count, Function<Toggle, T> mapper, Predicate<Toggle> filter) {
        Bounds.validOffset(offset, count, Integer.MAX_VALUE);
        return TOGGLES_LIST.values().stream()
                .filter(filter != null ? filter : t -> true)  // 如果filter为null，则接受所有元素
                .skip(offset)                                // 跳过offset个元素
                .limit(count)                                // 限制返回最多count个元素
                .map(mapper)                                 // 应用转换函数
                .toList();                                   // 收集到不可变列表中
    }

    /**
     * 从全局开关列表中移除指定的开关项
     * 注意：此方法不会自动清理相关资源或断开与其他开关的依赖关系
     *
     * @param toggle 需要被移除的开关对象，该对象必须具有唯一的标识符
     */
    public static void remove(Toggle toggle) {
        TOGGLES_LIST.remove(toggle.uniqueIdentifier);
    }

    /**
     * 检查功能开关是否开启
     * 开关开启需满足两个条件：
     * 1. 父开关为空或父开关处于开启状态
     * 2. 当前开关本身处于开启状态
     *
     * @return 如果功能开关开启返回true，否则返回false
     */
    public boolean isOpen() {
        return (parent == null || parent.isOpen()) && toggle.getAcquire();
    }

    /**
     * 检查功能开关是否关闭
     *
     * @return 如果功能开关关闭返回true，否则返回false
     */
    public boolean isClose() {
        return !this.isOpen();
    }

    /**
     * 切换功能开关状态
     * 使用原子操作确保线程安全
     *
     * @return 当前功能开关实例，支持链式调用
     */
    public Toggle toggle() {
        boolean value = toggle.getAcquire();
        toggle.compareAndSet(value, !value);
        return this;
    }

    /**
     * 将功能开关状态恢复为默认状态
     * 此操作会覆盖当前状态，将其设置为构造时指定的默认状态
     *
     * @return 当前功能开关实例，支持链式调用
     */
    public Toggle resume() {
        toggle.set(defaultStatus);
        return this;
    }

    /**
     * 获取功能开关的唯一标识符
     *
     * @return 功能开关的唯一标识符
     */
    public TagKey getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * 获取功能开关的描述信息
     * 首先尝试从资源文件中加载国际化描述，如果加载失败则返回默认描述
     *
     * @return 功能开关的描述信息，默认使用系统语言环境
     */
    public String getDescription() {
        return description.getLocalizedMessage(Locale.getDefault());
    }

    /**
     * 获取功能开关的描述信息
     * 首先尝试从资源文件中加载指定语言环境的国际化描述，如果加载失败则返回默认描述
     *
     * @param locale 指定的语言环境
     * @return 功能开关的描述信息
     */
    public String getDescription(Locale locale) {
        return description.getLocalizedMessage(locale);
    }

    /**
     * Toggle 构建器，用于简化功能开关的创建过程
     * 提供流畅的API来设置各种属性
     */
    public static final class Builder {
        private Toggle parent;
        private String module;      // 功能开关所属模块名称
        private String type;        // 功能开关类型
        private String value;       // 功能开关的具体值（可选）
        private String defaultDescription;  // 默认描述信息
        private boolean defaultStatus = true;  // 默认状态，默认为开启

        /**
         * 私有构造函数，防止外部直接实例化
         * 通过Toggle.builder()方法获取Builder实例
         */
        private Builder() {
        }

        /**
         * 设置父功能开关
         * 如果设置了父开关，则当前开关的状态依赖于父开关状态
         *
         * @param parent 父功能开关实例
         * @return 当前构建器实例，支持链式调用
         */
        public Builder parent(Toggle parent) {
            this.parent = parent;
            return this;
        }

        /**
         * 设置功能开关所属模块
         *
         * @param module 功能开关所属模块名称
         * @return 当前构建器实例，支持链式调用
         */
        public Builder module(String module) {
            this.module = module;
            return this;
        }

        /**
         * 设置功能开关所属模块
         * 使用Class对象的全限定名为模块名称
         *
         * @param module 功能开关所属模块的Class对象
         * @return 当前构建器实例，支持链式调用
         */
        public Builder module(Class<?> module) {
            this.module = module.getName();
            return this;
        }

        /**
         * 设置功能开关类型
         *
         * @param type 功能开关类型
         * @return 当前构建器实例，支持链式调用
         */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * 设置功能开关值（可选）
         * 用于进一步区分同一类型下的不同开关
         *
         * @param value 功能开关值
         * @return 当前构建器实例，支持链式调用
         */
        public Builder value(String value) {
            this.value = value;
            return this;
        }

        /**
         * 设置功能开关的默认描述信息
         *
         * @param defaultDescription 功能开关的默认描述信息
         * @return 当前构建器实例，支持链式调用
         */
        public Builder defaultDescription(String defaultDescription) {
            this.defaultDescription = defaultDescription;
            return this;
        }

        /**
         * 设置功能开关的默认状态
         *
         * @param defaultStatus 功能开关的默认状态(true为开启，false为关闭)
         * @return 当前构建器实例，支持链式调用
         */
        public Builder defaultStatus(boolean defaultStatus) {
            this.defaultStatus = defaultStatus;
            return this;
        }

        /**
         * 构建功能开关实例
         * 在构建过程中会验证必要的参数，并将新创建的开关注册到全局列表中
         *
         * @return 功能开关实例
         * @throws IllegalArgumentException 当必要参数缺失或验证失败时抛出异常
         */
        public Toggle build() {
            // 验证必需参数
            if (module == null || module.trim().isEmpty()) {
                throw new IllegalArgumentException("The Module cannot be null or empty");
            }

            if (type == null || type.trim().isEmpty()) {
                throw new IllegalArgumentException("The Type cannot be null or empty");
            }

            if (defaultDescription == null) {
                throw new IllegalArgumentException("The default description cannot be null.");
            }

            // 创建分类标识符
            TagStore tagStore = TagStore.of(Toggle.class, module, type, value);
            InternationalMessage description = InternationalMessage.of(tagStore.key(), defaultDescription);
            Toggle toggle = new Toggle(parent, tagStore.key(), description, defaultStatus);
            TOGGLES_LIST.put(tagStore.key(), toggle);
            return toggle;
        }
    }
}

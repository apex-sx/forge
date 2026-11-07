/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.chameleon;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * 变色龙类型转换管理器 (Chameleon Manager)
 * <p>
 * 该管理器负责注册、管理和执行各种类型转换器 ({@link Chameleon})。
 * 它支持基于目标类型的分层注册机制，区分“严格匹配”的转换器和“通用/动态”匹配的转换器，
 * 并提供线程安全的转换操作。
 * </p>
 *
 * @author Terraqo
 */
public final class ChameleonManager {

    /**
     * 默认的类型转换管理器实例，预注册了所有内置的类型转换器
     * 包括基本数据类型之间的转换、字符串转换、日期时间转换等
     */
    public static final ChameleonManager DEFAULT = ChameleonManager.of(manager -> {
        // 注册 ChameleonCoreLibrary 中定义的核心类型转换器
        manager
                // Boolean 转其他类型
                .register(ChameleonCoreLibrary.BOOLEAN__TO__BYTE)
                .register(ChameleonCoreLibrary.BOOLEAN__TO__CHARACTER_01)
                .register(ChameleonCoreLibrary.BOOLEAN__TO__CHARACTER_FT)
                .register(ChameleonCoreLibrary.BOOLEAN__TO__DOUBLE)
                .register(ChameleonCoreLibrary.BOOLEAN__TO__FLOAT)
                .register(ChameleonCoreLibrary.BOOLEAN__TO__SHORT)
                .register(ChameleonCoreLibrary.BOOLEAN__TO__INTEGER)
                .register(ChameleonCoreLibrary.BOOLEAN__TO__LONG)
                // Character 转其他类型
                .register(ChameleonCoreLibrary.CHARACTER__TO__BOOLEAN)
                .register(ChameleonCoreLibrary.CHARACTER__TO__BYTE)
                .register(ChameleonCoreLibrary.CHARACTER__TO__SHORT)
                .register(ChameleonCoreLibrary.CHARACTER__TO__INTEGER)
                .register(ChameleonCoreLibrary.CHARACTER__TO__LONG)
                .register(ChameleonCoreLibrary.CHARACTER__TO__FLOAT)
                .register(ChameleonCoreLibrary.CHARACTER__TO__DOUBLE)
                // Number 转其他类型
                .register(ChameleonCoreLibrary.NUMBER__TO__BOOLEAN)
                .register(ChameleonCoreLibrary.NUMBER__TO__CHARACTER)
                .register(ChameleonCoreLibrary.NUMBER__TO__BYTE)
                .register(ChameleonCoreLibrary.NUMBER__TO__SHORT)
                .register(ChameleonCoreLibrary.NUMBER__TO__INTEGER)
                .register(ChameleonCoreLibrary.NUMBER__TO__LONG)
                .register(ChameleonCoreLibrary.NUMBER__TO__FLOAT)
                .register(ChameleonCoreLibrary.NUMBER__TO__DOUBLE)
                .register(ChameleonCoreLibrary.NUMBER__TO__BIG_INTEGER)
                .register(ChameleonCoreLibrary.NUMBER__TO__BIG_DECIMAL)
                // String 转其他类型
                .register(ChameleonCoreLibrary.STRING__TO__BOOLEAN)
                .register(ChameleonCoreLibrary.STRING__TO__CHARACTER)
                .register(ChameleonCoreLibrary.STRING__TO__BYTE)
                .register(ChameleonCoreLibrary.STRING__TO__SHORT)
                .register(ChameleonCoreLibrary.STRING__TO__INTEGER)
                .register(ChameleonCoreLibrary.STRING__TO__LONG)
                .register(ChameleonCoreLibrary.STRING__TO__FLOAT)
                .register(ChameleonCoreLibrary.STRING__TO__DOUBLE)
                .register(ChameleonCoreLibrary.STRING__TO__BIG_INTEGER)
                .register(ChameleonCoreLibrary.STRING__TO__BIG_DECIMAL)
                .register(ChameleonCoreLibrary.STRING__TO__NUMBER)
                // CharSequence 转其他类型
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__BOOLEAN)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__CHARACTER)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__BYTE)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__SHORT)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__INTEGER)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__LONG)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__FLOAT)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__DOUBLE)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__BIG_INTEGER)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__BIG_DECIMAL)
                .register(ChameleonCoreLibrary.CHAR_SEQUENCE_TO__NUMBER)
                // Date/Time 转其他类型
                .register(ChameleonCoreLibrary.DATE__TO__LONG)
                .register(ChameleonCoreLibrary.DATE__TO__INSTANT)
                .register(ChameleonCoreLibrary.DATE__TO__LOCAL_DATE_TIME)
                .register(ChameleonCoreLibrary.DATE__TO__OFFSET_DATE_TIME)
                .register(ChameleonCoreLibrary.DATE__TO__ZONED_DATE_TIME)
                .register(ChameleonCoreLibrary.INSTANT__TO__LONG)
                .register(ChameleonCoreLibrary.INSTANT__TO__DATE)
                .register(ChameleonCoreLibrary.INSTANT__TO__LOCAL_DATE_TIME)
                .register(ChameleonCoreLibrary.INSTANT__TO__OFFSET_DATE_TIME)
                .register(ChameleonCoreLibrary.INSTANT__TO__ZONED_DATE_TIME)
                .register(ChameleonCoreLibrary.LOCAL_DATE_TIME__TO__LONG)
                .register(ChameleonCoreLibrary.LOCAL_DATE_TIME__TO__DATE)
                .register(ChameleonCoreLibrary.LOCAL_DATE_TIME__TO__INSTANT)
                .register(ChameleonCoreLibrary.LOCAL_DATE_TIME__TO__OFFSET_DATE_TIME)
                .register(ChameleonCoreLibrary.LOCAL_DATE_TIME__TO__ZONED_DATE_TIME)
                .register(ChameleonCoreLibrary.OFFSET_DATE_TIME__TO__LONG)
                .register(ChameleonCoreLibrary.OFFSET_DATE_TIME__TO__DATE)
                .register(ChameleonCoreLibrary.OFFSET_DATE_TIME__TO__INSTANT)
                .register(ChameleonCoreLibrary.OFFSET_DATE_TIME__TO__ZONED_DATE_TIME)
                .register(ChameleonCoreLibrary.OFFSET_DATE_TIME__TO__LOCAL_DATE_TIME)
                .register(ChameleonCoreLibrary.ZONED_DATE_TIME__TO__LONG)
                .register(ChameleonCoreLibrary.ZONED_DATE_TIME__TO__DATE)
                .register(ChameleonCoreLibrary.ZONED_DATE_TIME__TO__INSTANT)
                .register(ChameleonCoreLibrary.ZONED_DATE_TIME__TO__OFFSET_DATE_TIME)
                .register(ChameleonCoreLibrary.ZONED_DATE_TIME__TO__LOCAL_DATE_TIME);
    });

    /**
     * 核心注册容器。
     * <p>
     * Key: 目标类型 (Target Type)，即转换后期望得到的类型。
     * Value: {@link Registry} 实例，包含该目标类型下所有可用的源类型转换器。
     * </p>
     * <p>
     * 使用 {@link ConcurrentHashMap} 保证多线程环境下的读取安全和动态注册安全。
     * </p>
     */
    private final Map<Class<?>, Registry> registryContainer;

    /**
     * 私有构造函数，防止外部直接实例化，强制通过静态工厂方法创建。
     *
     * @param registryContainer 预初始化的注册容器映射表
     */
    private ChameleonManager(Map<Class<?>, Registry> registryContainer) {
        this.registryContainer = registryContainer;
    }

    /**
     * 创建一个新的 {@code ChameleonManager} 实例，并通过消费者函数进行链式配置。
     * <p>
     * 示例用法：
     * <pre>{@code
     * ChameleonManager manager = ChameleonManager.of(m -> {
     *     m.register(new StringToIntConverter());
     *     m.register(new DateToStringConverter());
     * });
     * }</pre>
     *
     * @param consumer 用于配置管理器的消费者函数，可在其中注册转换器
     * @return 配置完成且不可变的 {@code ChameleonManager} 实例
     */
    public static ChameleonManager of(Consumer<ChameleonManager> consumer) {
        // 初始化空的并发映射表
        ChameleonManager chameleonManager = new ChameleonManager(new ConcurrentHashMap<>());
        // 执行用户配置逻辑
        consumer.accept(chameleonManager);
        return chameleonManager;
    }

    /**
     * 基于现有的 {@code ChameleonManager} 创建一个新的副本实例。
     * <p>
     * 此操作是深拷贝注册表结构（Registry），但转换器实例本身是共享引用。
     * 这允许在不影响原始管理器配置的情况下，扩展或修改新的管理器实例。
     *
     * @param manager 现有的管理器实例，若为 null 则创建一个空管理器
     * @return 新的 {@code ChameleonManager} 实例，包含原有实例的所有注册配置
     */
    public static ChameleonManager of(ChameleonManager manager) {
        Map<Class<?>, Registry> map = new ConcurrentHashMap<>();
        if (manager != null) {
            // 遍历现有注册表，为每个目标类型创建新的 Registry 副本
            manager.registryContainer.forEach((targetType, registry) -> map.put(targetType, new Registry(registry)));
        }
        return new ChameleonManager(map);
    }

    /**
     * 创建一个新的、空的 {@code ChameleonManager} 实例。
     *
     * @return 空的 {@code ChameleonManager} 实例
     */
    public static ChameleonManager of() {
        return new ChameleonManager(new ConcurrentHashMap<>());
    }

    /**
     * 注册一个类型转换器。
     * <p>
     * 转换器会根据其支持的目标类型 ({@link Chameleon#supportsTargetType()}) 被归类到相应的注册表中。
     * 根据转换器是否为“严格模式” ({@link Chameleon#isStrict()})，它会被放入精确匹配映射或通用匹配列表。
     *
     * @param converter    要注册的类型转换器实例
     * @param <SourceType> 转换器支持的源类型泛型占位符
     * @param <TargetType> 转换器支持的目标类型泛型占位符
     * @param <Parameter>  转换过程中可能需要的上下文参数类型泛型占位符
     * @return 当前 {@code ChameleonManager} 实例，支持链式调用
     */
    public <SourceType, TargetType, Parameter> ChameleonManager register(Chameleon<SourceType, TargetType, Parameter> converter) {
        if (converter != null) {
            // computeIfAbsent 确保如果目标类型尚未注册，则自动创建一个新的 Registry
            // 然后调用 Registry.register 将转换器加入对应的集合
            registryContainer.computeIfAbsent(converter.supportsTargetType(), Registry::new).register(converter);
        }
        return this;
    }

    /**
     * 执行类型转换。
     * <p>
     * 转换策略遵循以下优先级：
     * 1. 如果源对象为 null，直接返回默认值。
     * 2. 如果目标类型未指定，尝试从默认值推断目标类型。
     * 3. 如果源对象已经是目标类型的实例，直接返回源对象（避免不必要的转换）。
     * 4. 查找针对源对象具体类的“严格/精确”转换器。
     * 5. 如果未找到精确匹配，遍历“通用”转换器列表，通过 {@link Chameleon#isCanConvert(Object, Object...)} 判断是否适用。
     * 6. 如果所有尝试都失败，返回默认值。
     *
     * @param source        待转换的源对象
     * @param targetType    期望转换成的目标类型 Class 对象
     * @param targetDefault 转换失败或无法转换时的默认返回值
     * @param parameter     传递给转换器的上下文参数
     * @param selectors     选择器数组，用于辅助通用转换器判断是否可转换
     * @param <SourceType>  源对象的实际类型
     * @param <TargetType>  目标类型
     * @param <Parameter>   上下文参数类型
     * @return 转换后的目标对象，或者在失败/空值情况下返回 {@code targetDefault}
     * @throws ChameleonException 当转换过程中发生不可恢复的错误时抛出
     */
    @SuppressWarnings("unchecked")
    public <SourceType, TargetType, Parameter> TargetType convert(SourceType source, Class<TargetType> targetType, TargetType targetDefault, Parameter parameter, Object... selectors) throws ChameleonException {
        // 1. 处理源对象为空的情况
        if (source == null) {
            return targetDefault;
        }

        // 2. 处理目标类型缺失的情况，尝试从默认值推断
        if (targetType == null) {
            if (targetDefault == null) {
                throw new ChameleonException("Please specify the target targetType or provide a non-null default value");
            }
            // noinspection unchecked
            targetType = (Class<TargetType>) targetDefault.getClass();
        }

        // 3. 类型兼容性检查：如果源对象本身就是目标类型，无需转换
        if (targetType.isInstance(source)) {
            return (TargetType) source;
        }

        // 4. 获取目标类型对应的注册表
        Registry registry = registryContainer.get(targetType);
        if (registry == null) {
            // 没有为该目标类型注册任何转换器，返回默认值
            return targetDefault;
        }

        // 5. 获取源对象的运行时类
        Class<SourceType> sourceType = (Class<SourceType>) source.getClass();

        // 6. 优先尝试精确匹配（Strict Converters）
        // 从 specifics 映射中查找完全匹配源类型的转换器
        try (Chameleon<SourceType, TargetType, Parameter> found = (Chameleon<SourceType, TargetType, Parameter>) registry.specifics.get(sourceType)) {
            if (found != null) {
                // 设置上下文参数
                found.setContextParameter(parameter);
                // 执行转换，isStrict=true 表示这是精确匹配
                return Chameleon.performConversion(sourceType, source, targetType, targetDefault, found, false, true);
            }
        }

        // 7. 如果没有精确匹配，遍历通用转换器（Generic Converters）
        // 这些转换器通常支持接口、父类或动态判断逻辑
        for (Chameleon<?, ?, ?> chameleons : registry.generics) {
            // 检查转换器是否声称可以处理当前源对象（可能基于 selectors）
            if (chameleons.isCanConvert(source, selectors)) {
                //noinspection unchecked
                try (Chameleon<SourceType, TargetType, Parameter> found = (Chameleon<SourceType, TargetType, Parameter>) chameleons) {
                    found.setContextParameter(parameter);
                    // 执行转换，isStrict=false 表示这是通用匹配
                    return Chameleon.performConversion(sourceType, source, targetType, targetDefault, found, false, false);
                }
            }
        }

        // 8. 所有尝试均失败，返回默认值
        return targetDefault;
    }

    /**
     * 内部记录类 (Record)，用于封装特定目标类型下的转换器注册信息。
     * <p>
     * 每个 {@code Registry} 实例对应一个目标类型，并维护两组转换器：
     * <ul>
     *     <li>{@code specifics}: 精确匹配映射表。Key 为源类型 Class，Value 为转换器。适用于源类型明确且固定的场景。</li>
     *     <li>{@code generics}: 通用转换器列表。适用于源类型不确定、基于接口/父类或需要运行时动态判断的场景。</li>
     * </ul>
     *
     * @param targetType 该注册表所属的目标类型
     * @param specifics  精确类型匹配的转换器映射表 (Thread-safe: ConcurrentHashMap)
     * @param generics   通用转换器列表 (Thread-safe: CopyOnWriteArrayList)
     */
    private record Registry(Class<?> targetType, Map<Class<?>, Chameleon<?, ?, ?>> specifics,
                            List<Chameleon<?, ?, ?>> generics) {

        /**
         * 构造一个新的空注册表，用于指定的目标类型。
         *
         * @param type 目标类型
         */
        private Registry(Class<?> type) {
            this(type, new ConcurrentHashMap<>(), new CopyOnWriteArrayList<>());
        }

        /**
         * 拷贝构造函数，用于深拷贝注册表结构（浅拷贝转换器实例）。
         *
         * @param registry 原始注册表
         */
        private Registry(Registry registry) {
            this(registry.targetType, new ConcurrentHashMap<>(registry.specifics), new CopyOnWriteArrayList<>(registry.generics));
        }

        /**
         * 将转换器注册到当前的注册表中。
         * <p>
         * 根据转换器的 {@link Chameleon#isStrict()} 属性决定存储位置：
         * <ul>
         *     <li>如果是严格模式 (Strict)，放入 {@code specifics} 映射，以源类型为 Key。</li>
         *     <li>如果不是严格模式 (Generic)，添加到 {@code generics} 列表末尾。</li>
         * </ul>
         *
         * @param converter 要注册的转换器实例
         */
        private void register(Chameleon<?, ?, ?> converter) {
            // 获取转换器声明支持的源类型
            Class<?> sourceType = converter.supportsSourceType();
            if (converter.isStrict()) {
                // 严格模式：建立 源类型 -> 转换器 的直接映射，提高查找效率 O(1)
                specifics.put(sourceType, converter);
            } else {
                // 通用模式：加入列表，查找时需要遍历并调用 isCanConvert 进行判断 O(N)
                generics.add(converter);
            }
        }
    }
}

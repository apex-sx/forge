/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation.chameleon;

import io.github.terraqo.foundation.Logger;

import java.io.Closeable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 类型转换器抽象基类，用于定义和执行不同类型之间的转换规则。
 * <p>
 * 该抽象类提供了泛型支持，允许子类指定源类型（SourceType）和目标类型（TargetType）。
 * 它支持两种模式：
 * <ol>
 *   <li><strong>严格模式（Strict Mode）</strong>：在构造函数中明确指定源类型 Class，进行严格的 {@link Class#isInstance(Object)} 类型匹配。</li>
 *   <li><strong>动态模式（Dynamic Mode）</strong>：未指定源类型 Class，系统尝试通过反射解析泛型参数确定源类型，或委托给 {@link #isSupportSource(Object)} 方法进行动态判断。</li>
 * </ol>
 * <p>
 * 此外，该类还支持“提供者/选择器”（Providers）机制，用于在注册了多个同类型转换器时，根据上下文提供的标识符（如字符串、枚举、数字等）精确匹配特定的转换场景。
 *
 * @param <SourceType> 源数据类型
 * @param <TargetType> 目标数据类型
 * @param <Parameter>  线程上下文参数类型，用于在转换过程中传递线程隔离的额外配置信息
 * @author Terraqo
 */
public abstract class Chameleon<SourceType, TargetType, Parameter> implements Closeable {

    /**
     * 源类型的 Class 对象。
     * <ul>
     *   <li>在<strong>严格模式</strong>下，此字段不为 null，用于 {@link Class#isInstance(Object)} 快速类型检查。</li>
     *   <li>在<strong>动态模式</strong>下，此字段可能通过反射解析得到具体 Class，也可能为 null（此时完全依赖 {@link #isSupportSource(Object)}）。</li>
     * </ul>
     */
    private final Class<SourceType> sourceType;

    /**
     * 目标类型的 Class 对象。
     * <p>
     * 此字段永远不为 null，用于验证转换结果类型及提供元数据。
     */
    private final Class<TargetType> targetType;

    /**
     * 标识此转换器是否处于“严格模式”。
     * <ul>
     *   <li>{@code true}：表示构造函数中显式传入了 sourceType，将仅使用该类型进行严格的 instanceof 匹配。</li>
     *   <li>{@code false}：表示未显式指定 sourceType，系统将尝试通过反射解析泛型参数，或委托给 {@link #isSupportSource(Object)} 进行动态判断。</li>
     * </ul>
     */
    private final boolean strict;

    /**
     * 支持的选择器（Providers）数组。
     * <p>
     * 用于在注册了多个同类型转换器时，根据上下文提供的标识符（如字符串、枚举、数字等）定位到具体的转换器实例。
     * 数组中的元素已去重且过滤了 null 值。
     */
    private final Object[] providers;

    /**
     * 动态源类型检查器。
     * <p>
     * 当处于动态模式且无法通过反射解析出具体源类型时，可使用此 Predicate 进行自定义逻辑判断。
     */
    private final Predicate<Object> supportChecker;

    /**
     * 线程局部变量，用于存储当前线程的上下文参数。
     * <p>
     * 仅在构造函数传入了 defaultParameter 时初始化，否则为 null 以节省内存。
     */
    private final ThreadLocal<Parameter> context;

    /**
     * 默认的上下文参数，当线程局部变量中未设置参数时使用。
     */
    private final Parameter defaultParameter;

    /**
     * 构造函数：初始化转换器，不指定源类型（动态模式）。
     * <p>
     * 系统将在后续通过反射解析子类泛型参数来确定源类型，或依赖子类实现的 {@link #isSupportSource(Object)} 方法或传入的 {@code supportChecker}。
     *
     * @param supportChecker 动态源类型检查器，可为 null
     * @param targetType     目标类型的 Class 对象，不能为 null
     * @param providers      可变参数的选择器列表，用于区分不同的转换策略。null 值将被过滤。
     * @throws NullPointerException 当 targetType 为 null 时抛出
     */
    protected Chameleon(Predicate<Object> supportChecker, Class<TargetType> targetType, Object... providers) {
        this(supportChecker, null, targetType, null, providers);
    }

    /**
     * 构造函数：初始化转换器，可指定源类型（严格模式或混合模式）。
     * <p>
     * 如果 {@code sourceType} 不为 null，则启用严格模式，直接使用传入的 Class 进行类型匹配。
     * 如果 {@code sourceType} 为 null，则启用动态模式，尝试通过反射解析泛型信息来确定源类型。
     *
     * @param sourceType 源类型的 Class 对象。若为 null，则触发泛型解析逻辑。
     * @param targetType 目标类型的 Class 对象，不能为 null
     * @param providers  可变参数的选择器列表，用于区分不同的转换策略。null 值将被过滤。
     * @throws NullPointerException 当 targetType 为 null 时抛出
     */
    protected Chameleon(Class<SourceType> sourceType, Class<TargetType> targetType, Object... providers) {
        this(null, sourceType, targetType, null, providers);
    }

    /**
     * 构造函数：初始化转换器，不指定源类型（动态模式），并支持默认上下文参数。
     * <p>
     * 系统将在后续通过反射解析子类泛型参数来确定源类型，或依赖子类实现的 {@link #isSupportSource(Object)} 方法或传入的 {@code supportChecker}。
     *
     * @param defaultParameter 默认的上下文参数，若不为 null 则启用 ThreadLocal 上下文支持
     * @param supportChecker   动态源类型检查器，可为 null
     * @param targetType       目标类型的 Class 对象，不能为 null
     * @param providers        可变参数的选择器列表，用于区分不同的转换策略。null 值将被过滤。
     * @throws NullPointerException 当 targetType 为 null 时抛出
     */
    protected Chameleon(Parameter defaultParameter, Predicate<Object> supportChecker, Class<TargetType> targetType, Object... providers) {
        this(supportChecker, null, targetType, defaultParameter, providers);
    }

    /**
     * 构造函数：初始化转换器，可指定源类型（严格模式或混合模式），并支持默认上下文参数。
     * <p>
     * 如果 {@code sourceType} 不为 null，则启用严格模式，直接使用传入的 Class 进行类型匹配。
     * 如果 {@code sourceType} 为 null，则启用动态模式，尝试通过反射解析泛型信息来确定源类型。
     *
     * @param defaultParameter 默认的上下文参数，若不为 null 则启用 ThreadLocal 上下文支持
     * @param sourceType       源类型的 Class 对象。若为 null，则触发泛型解析逻辑。
     * @param targetType       目标类型的 Class 对象，不能为 null
     * @param providers        可变参数的选择器列表，用于区分不同的转换策略。null 值将被过滤。
     * @throws NullPointerException 当 targetType 为 null 时抛出
     */
    protected Chameleon(Parameter defaultParameter, Class<SourceType> sourceType, Class<TargetType> targetType, Object... providers) {
        this(null, sourceType, targetType, defaultParameter, providers);
    }

    /**
     * 私有主构造函数：初始化转换器所有字段。
     * <p>
     * 处理逻辑如下：
     * <ol>
     *   <li>验证并赋值目标类型。</li>
     *   <li>过滤并赋值 Providers 选择器。</li>
     *   <li>判断模式：
     *     <ul>
     *       <li>若 {@code sourceType} 非空，设为严格模式。</li>
     *       <li>若 {@code sourceType} 为空，设为动态模式，并尝试通过反射遍历继承链解析泛型参数以确定源类型。</li>
     *     </ul>
     *   </li>
     *   <li>赋值动态检查器。</li>
     *   <li>初始化上下文参数支持（ThreadLocal）。</li>
     * </ol>
     *
     * @param supportChecker   动态源类型检查器
     * @param sourceType       源类型的 Class 对象。若为 null，则触发泛型解析逻辑。
     * @param targetType       目标类型的 Class 对象，不能为 null
     * @param defaultParameter 默认的上下文参数
     * @param providers        可变参数的选择器列表
     */
    private Chameleon(Predicate<Object> supportChecker, Class<SourceType> sourceType, Class<TargetType> targetType, Parameter defaultParameter, Object... providers) {
        // 1. 确保目标类型不为空
        this.targetType = Objects.requireNonNull(targetType, "Target type must not be null");

        // 2. 初始化选择器数组，过滤掉 null 元素并去重
        this.providers = providers == null ? new Object[0] : Arrays.stream(providers).filter(Objects::nonNull).distinct().toArray();

        if (sourceType != null) {
            // 3a. 严格模式：直接使用传入的源类型
            this.strict = true;
            this.sourceType = sourceType;
        } else {
            // 3b. 动态模式：尝试通过反射解析泛型参数以确定源类型
            this.strict = false;
            this.sourceType = resolveSourceTypeViaReflection();
        }

        // 4. 赋值动态检查器
        this.supportChecker = supportChecker;

        // 5. 初始化上下文参数支持
        if (defaultParameter == null) {
            this.context = null;
            this.defaultParameter = null;
        } else {
            this.context = new ThreadLocal<>();
            this.defaultParameter = defaultParameter;
        }
    }

    /**
     * 通过反射解析泛型源类型。
     * <p>
     * 遍历从当前类到 {@link Chameleon} 的继承链，提取泛型参数。
     * 支持处理数组类型泛型（如 {@code List<String[]>}）和类型变量绑定。
     *
     * @return 解析出的源类型 Class，若解析失败则返回 null
     */
    @SuppressWarnings("unchecked")
    private Class<SourceType> resolveSourceTypeViaReflection() {
        int arrayDimensions = 0; // 记录数组维度
        int typeArgIndex = 0;    // 当前处理的泛型参数索引（对应 Chameleon<SourceType, TargetType, Parameter> 中的 SourceType 位置，通常为0）
        Class<?> resolvedType = null;

        // 构建从当前类到 Chameleon 的继承链列表（自底向上）
        LinkedList<Class<?>> inheritanceChain = new LinkedList<>();
        for (Class<?> currentClass = getClass(); currentClass != Chameleon.class; currentClass = currentClass.getSuperclass()) {
            inheritanceChain.addFirst(currentClass);
        }

        // 遍历继承链，解析泛型信息
        for (Class<?> currentClass : inheritanceChain) {
            // 获取当前类直接父类的泛型超类信息
            Type genericSuperclass = currentClass.getGenericSuperclass();
            if (!(genericSuperclass instanceof ParameterizedType)) {
                continue;
            }

            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();

            // 边界检查：确保索引有效
            if (typeArgIndex >= actualTypeArguments.length) {
                break;
            }

            Type type = actualTypeArguments[typeArgIndex];

            // 处理数组类型泛型（如 List<String[]>），提取组件类型并记录维度
            while (type instanceof GenericArrayType) {
                arrayDimensions++;
                type = ((GenericArrayType) type).getGenericComponentType();
            }

            if (type instanceof TypeVariable) {
                // 如果类型是类型变量（如 T），需要查找其在当前类定义中的实际绑定
                TypeVariable<? extends Class<?>>[] typeParameters = currentClass.getTypeParameters();
                boolean foundBinding = false;

                for (int i = 0; i < typeParameters.length; i++) {
                    if (typeParameters[i].equals(type)) {
                        // 更新索引以匹配父类中的参数位置，以便在下一轮循环中正确获取父类的实际类型参数
                        typeArgIndex = i;

                        // 获取类型变量的上界（通常第一个上界是具体的 Class）
                        Type bound = typeParameters[i].getBounds()[0];
                        resolvedType = (Class<?>) (bound instanceof ParameterizedType ? ((ParameterizedType) bound).getRawType() : bound);
                        foundBinding = true;
                        break;
                    }
                }

                if (!foundBinding) {
                    // 如果无法在当前类解析类型变量，可能需要进一步向上查找，此处暂保持 resolvedType 为 null 或继续循环
                    break;
                }
            } else {
                // 类型已具体化（如 String, Integer 等），直接提取 Class
                resolvedType = (Class<?>) (type instanceof ParameterizedType ? ((ParameterizedType) type).getRawType() : type);
                break; // 找到具体类型，结束解析
            }
        }

        // 如果解析出的类型是数组，重构数组 Class 对象
        if (arrayDimensions > 0 && resolvedType != null) {
            resolvedType = Array.newInstance(resolvedType, new int[arrayDimensions]).getClass();
        }

        // 赋值解析后的源类型（可能为 null，如果解析失败则依赖 isSupportSource）
        return (Class<SourceType>) resolvedType;
    }

    /**
     * 静态工具方法：执行类型转换的核心逻辑。
     * <p>
     * 该方法封装了转换前的验证、异常处理以及日志记录逻辑，供外部框架调用。
     *
     * @param sourceType                源类型的 Class 对象，用于日志记录
     * @param source                    待转换的源对象实例
     * @param targetType                期望的目标类型 Class 对象
     * @param targetDefault             转换失败时的默认返回值
     * @param foundChameleon            已选定的类型转换器实例
     * @param isThrowConverterException 是否在发生转换异常时重新抛出异常。若为 false，则记录警告日志并返回默认值。
     * @param isCheckValue              是否执行严格的源类型检查。若为 true，调用 {@link #convert(Object, Object)}；否则调用 {@link #convertUnchecked(Object, Object)}。
     * @param <SourceType>              源类型泛型
     * @param <TargetType>              目标类型泛型
     * @param <Parameter>               上下文参数泛型
     * @return 转换后的目标对象。如果转换失败且未抛出异常，则返回 {@code targetDefault}。
     * @throws ChameleonException 当转换器不支持目标类型，或 {@code isThrowConverterException} 为 true 且转换发生错误时抛出。
     */
    public static <SourceType, TargetType, Parameter> TargetType performConversion(
            Class<SourceType> sourceType, SourceType source,
            Class<TargetType> targetType, TargetType targetDefault,
            Chameleon<SourceType, TargetType, Parameter> foundChameleon,
            boolean isThrowConverterException, boolean isCheckValue) {

        TargetType target = null;

        // 1. 验证转换器是否真正支持请求的目标类型，防止注册错误
        if (!foundChameleon.supportsTargetType().equals(targetType)) {
            throw new ChameleonException(
                    "The converter does not support the target type [" + targetType.getSimpleName() +
                            "]. Expected: [" + foundChameleon.supportsTargetType().getSimpleName() +
                            "]. Possibly due to an error in the target type when registering the converter."
            );
        }

        try {
            // 2. 执行转换
            if (isCheckValue) {
                // 带源类型检查的转换
                target = foundChameleon.convert(source, targetDefault);
            } else {
                // 不带源类型检查的转换（信任调用者已确认类型）
                target = foundChameleon.convertUnchecked(source, targetDefault);
            }
        } catch (ChameleonException e) {
            // 3. 异常处理
            if (isThrowConverterException) {
                throw e;
            } else {
                // 记录警告日志，但不中断流程
                if (Logger.isEnabledWarn()) {
                    Logger.warn(
                            "Failed to convert source type [%s] to target type [%s] using converter [%s]. Reason: %s",
                            sourceType != null ? sourceType.getSimpleName() : "Unknown",
                            targetType.getSimpleName(),
                            foundChameleon.getClass().getSimpleName(),
                            e.getMessage()
                    );
                }
            }
        }

        // 4. 返回结果：如果转换结果为 null，则返回默认值；否则强制 casting 后返回
        return target != null ? targetType.cast(target) : targetDefault;
    }

    /**
     * 初始化并设置当前线程的上下文参数。
     *
     * @param parameter 要设置的上下文参数
     * @return 设置的参数值
     */
    public final Parameter initContextParameter(Parameter parameter) {
        this.setContextParameter(parameter);
        return parameter;
    }

    /**
     * 设置当前线程的上下文参数。
     * <p>
     * 如果未启用上下文支持（即 defaultParameter 为 null），此方法不执行任何操作。
     *
     * @param parameter 要设置的上下文参数
     */
    public final void setContextParameter(Parameter parameter) {
        if (context != null) {
            context.set(parameter);
        }
    }

    /**
     * 获取当前线程的上下文参数。
     * <p>
     * 如果当前线程未设置参数，则返回构造函数中指定的默认参数。
     * 如果未启用上下文支持，则抛出异常。
     * </p>
     *
     * @return 当前上下文参数或默认参数
     * @throws ChameleonException 如果未启用上下文支持
     */
    public final Parameter getContextParameter() {
        if (context != null) {
            Parameter parameter = context.get();
            if (parameter == null) {
                parameter = defaultParameter;
            }
            return parameter;
        }
        throw new ChameleonException("No context parameter found.");
    }

    /**
     * 获取此转换器支持的源类型 Class 对象。
     * <p>
     * <strong>注意：</strong>
     * <ul>
     *   <li>如果返回非 null，框架将优先使用 {@link Class#isInstance(Object)} 进行快速类型匹配。</li>
     *   <li>如果返回 null，框架将调用 {@link #isCanConvert(Object, Object...)} 进而委托给 {@link #isSupportSource(Object)} 进行动态判断。</li>
     * </ul>
     *
     * @return 源类型的 Class 对象，可能为 null（在动态模式下且反射解析失败时）
     */
    public final Class<SourceType> supportsSourceType() {
        return sourceType;
    }

    /**
     * 获取此转换器支持的目标类型 Class 对象。
     *
     * @return 目标类型的 Class 对象，保证不为 null
     */
    public final Class<TargetType> supportsTargetType() {
        return targetType;
    }

    /**
     * 获取此转换器的转换模式。
     * <p>
     * <ul>
     *   <li>{@code true}：严格模式。表示在构造函数中显式传入了源类型，将仅使用该类型进行严格的 {@link Class#isInstance(Object)} 匹配。</li>
     *   <li>{@code false}：动态模式。表示未显式指定源类型，系统将尝试通过反射解析泛型参数，或委托给 {@link #isSupportSource(Object)} 进行动态判断。</li>
     * </ul>
     *
     * @return 如果处于严格模式返回 {@code true}，否则返回 {@code false}
     */
    public final boolean isStrict() {
        return strict;
    }

    /**
     * 判断指定的源对象和提供者标识是否支持由此转换器进行转换。
     * <p>
     * 此方法是转换前的核心校验入口，结合了类型检查和提供者匹配。
     *
     * @param source    待检查的源对象
     * @param providers 可选的提供者标识数组，用于在多策略转换器中进行细分匹配。
     * @return 如果源对象类型匹配且提供者标识兼容，返回 {@code true}；否则返回 {@code false}。
     */
    public final boolean isCanConvert(Object source, Object... providers) {
        // 1. 类型检查
        if (strict) {
            // 严格模式：必须指定了 sourceType 且源对象是其实例
            if (sourceType == null) {
                throw new Error("Internal Error: The source type cannot be null in strict mode.");
            }
            if (!sourceType.isInstance(source)) {
                return false;
            }
        } else {
            // 动态模式
            if (sourceType != null) {
                // 如果反射解析到了 sourceType，先进行 instanceof 检查以提高性能
                if (!sourceType.isInstance(source)) {
                    return false;
                }
            } else {
                // 如果没有解析到 sourceType，委托给子类实现的动态判断方法
                if (!isSupportSource(source)) {
                    return false;
                }
            }
        }

        // 2. 提供者（Provider）匹配检查
        return this.isSupportProvider(providers);
    }

    /**
     * 动态判断源对象是否支持转换。
     * <p>
     * 当 {@link #supportsSourceType()} 返回 null 时，此方法将被调用来执行自定义的类型判断逻辑。
     * 优先使用构造函数传入的 {@code supportChecker}，若未提供且子类未重写此方法，则抛出异常。
     *
     * @param source 待检查的源对象
     * @return 如果支持转换返回 {@code true}，否则返回 {@code false}
     * @throws ChameleonException 如果子类未实现此方法且未提供 supportChecker
     */
    public boolean isSupportSource(Object source) {
        if (supportChecker != null) {
            return supportChecker.test(source);
        }
        throw new ChameleonException(
                "Method 'isSupportSource' must be implemented in the subclass when sourceType is not explicitly defined and no supportChecker is provided."
        );
    }

    /**
     * 检查给定的提供者标识是否与当前转换器支持的标识相匹配。
     * <p>
     * 匹配规则：
     * <ul>
     *   <li>如果传入的 {@code providers} 为空或全为 null，视为匹配所有（返回 true）。</li>
     *   <li>如果当前转换器未设置任何 providers，视为接受所有请求（返回 true）。</li>
     *   <li>对于 String 类型的标识，执行忽略大小写的比较。</li>
     *   <li>对于其他类型，使用 {@link Objects#equals(Object, Object)} 进行比较。</li>
     *   <li>只要传入的任意一个标识与支持的任意一个标识匹配，即返回 true。</li>
     * </ul>
     *
     * @param providers 要检查的提供者标识数组
     * @return 如果包含任意一个匹配的标识则返回 {@code true}，否则返回 {@code false}
     */
    public boolean isSupportProvider(Object... providers) {
        // 如果没有传入任何筛选条件，默认匹配
        if (providers == null || providers.length == 0) {
            return true;
        }

        // 过滤掉传入参数中的 null 值
        Object[] filteredTargets = Arrays.stream(providers).filter(Objects::nonNull).toArray();

        // 如果过滤后没有有效目标，或者当前转换器没有设置任何支持的 providers，则视为匹配
        if (filteredTargets.length == 0 || this.providers.length == 0) {
            return true;
        }

        // 双重循环比对：传入的标识 vs 支持的标识
        for (Object target : filteredTargets) {
            for (Object supported : this.providers) {
                // 字符串特殊处理：忽略大小写
                if (supported instanceof String supportedStr && target instanceof String targetStr) {
                    if (supportedStr.equalsIgnoreCase(targetStr)) {
                        return true;
                    }
                }
                // 其他类型：标准 equals 比较
                else if (Objects.equals(target, supported)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 将源对象转换为目标类型对象（带类型检查）。
     * <p>
     * 此方法会先调用 {@link #isCanConvert(Object, Object...)} 验证源对象类型，
     * 如果类型不匹配，将直接返回默认值而不执行转换逻辑。
     *
     * @param source 源对象
     * @return 转换后的目标对象，如果转换失败或源对象为 null，则返回 null
     * @throws ChameleonException 转换过程中发生非预期错误时抛出
     */
    public final TargetType convert(SourceType source) throws ChameleonException {
        return this.convert(source, null);
    }

    /**
     * 将源对象转换为目标类型对象（带类型检查及默认值）。
     * <p>
     * 此方法会先调用 {@link #isCanConvert(Object, Object...)} 验证源对象类型。
     * 注意：此处不传入外部 providers，仅依赖构造函数中固化的 providers 或默认匹配逻辑。
     *
     * @param source       源对象
     * @param defaultValue 默认值，当源对象为 null、类型不匹配或转换结果为 null 时返回此值
     * @return 转换后的目标对象，或者 {@code defaultValue}
     * @throws ChameleonException 转换逻辑内部抛出异常时被捕获并重新包装抛出
     */
    public final TargetType convert(SourceType source, TargetType defaultValue) throws ChameleonException {
        try {
            TargetType result = null;

            // 1. 前置检查：源对象非空且类型兼容
            if (source != null) {
                // 调用无参 isCanConvert，意味着忽略外部传入的 provider 筛选，仅依赖内部状态
                if (this.isCanConvert(source)) {
                    // 2. 执行具体转换逻辑（包含预处理）
                    result = this.pretreatment(source);
                }
            }

            // 3. 返回结果：优先返回转换结果，若为 null 则返回默认值
            if (result != null) {
                return result;
            }
        } catch (Throwable e) {
            // 4. 异常包装：添加详细的上下文信息
            throw new ChameleonException(
                    "Failed to convert [" + supportsSourceType() + "] to [" + supportsTargetType() + "] with value '" + source + "'!",
                    e
            );
        }

        return defaultValue;
    }

    /**
     * 将源对象转换为目标类型对象（跳过类型检查）。
     * <p>
     * 此方法直接执行转换逻辑，不验证源对象类型。适用于调用者已经确保类型安全的场景，性能略高。
     *
     * @param source 源对象
     * @return 转换后的目标对象，如果转换失败或结果为 null，则返回 null
     * @throws ChameleonException 转换过程中发生非预期错误时抛出
     */
    public final TargetType convertUnchecked(SourceType source) throws ChameleonException {
        return this.convertUnchecked(source, null);
    }

    /**
     * 将源对象转换为目标类型对象（跳过类型检查及默认值）。
     * <p>
     * 此方法直接执行转换逻辑，不验证源对象类型。
     *
     * @param source       源对象
     * @param defaultValue 默认值，当转换结果为 null 时返回此值
     * @return 转换后的目标对象，或者 {@code defaultValue}
     * @throws ChameleonException 转换逻辑内部抛出异常时被捕获并重新包装抛出
     */
    public final TargetType convertUnchecked(SourceType source, TargetType defaultValue) throws ChameleonException {
        try {
            // 直接执行转换（包含预处理），不进行 isCanConvert 检查
            TargetType result = this.pretreatment(source);

            if (result != null) {
                return result;
            }
        } catch (Throwable e) {
            // 异常包装
            throw new ChameleonException(
                    "Failed to convert [" + supportsSourceType() + "] to [" + supportsTargetType() + "] with value '" + source + "'!",
                    e
            );
        }

        return defaultValue;
    }

    /**
     * 预处理并执行核心转换逻辑。
     * <p>
     * 此方法会对输入的字符序列进行首尾空白字符修剪处理，然后再进行类型转换。
     * 对于空字符串或全空白字符的字符串，会直接返回空值处理结果。
     * 如果源对象不是 {@link CharSequence}，则直接委托给 {@link #handleObject(Object)}。
     *
     * @param source 源对象
     * @return 转换后的目标对象，可能为 null
     * @throws Throwable 转换过程中发生错误时抛出
     */
    private TargetType pretreatment(SourceType source) throws Throwable {
        if (source instanceof CharSequence sequence) {
            int temp = sequence.length();
            // 空字符串直接处理
            if (temp == 0) {
                return handleString("");
            }
            // 初始化起始位置为0，结束位置为输入序列的最后一个字符索引
            int s = 0, e = temp - 1;
            // 使用位运算标记需要处理的方向：0b01表示处理开头，0b10表示处理结尾
            for (int tag = 0b10 | 0b01, i; tag != 0 && s <= e; ) {
                // 如果需要处理开头且满足移除条件，则移动起始位置
                if ((tag & 0b01) == 0b01) {
                    // 获取指定索引处的 code point
                    temp = Character.codePointAt(sequence, s);
                    // 如果该 code point 满足修剪条件，则返回其占用的字符数（1或2）
                    i = isTrimCodePoint(source, s, temp) ? Character.charCount(temp) : 0;
                    if (i > 0) {
                        // 如果需要跳过，则更新起始位置
                        s += i;
                    } else {
                        // 否则清除处理开头的标记（将最低位设为0）
                        tag = tag & 0b10;
                    }
                }
                // 如果需要处理结尾（tag的第二位为1）
                if ((tag & 0b10) == 0b10) {
                    // 获取结束位置处的 code point
                    temp = Character.codePointBefore(sequence, e);
                    // 如果该 code point 满足修剪条件，则返回其占用的字符数（1或2）
                    i = isTrimCodePoint(source, e, temp) ? Character.charCount(temp) : 0;
                    if (i > 0) {
                        // 如果需要跳过，则更新结束位置
                        e -= i;
                    } else {
                        // 否则清除处理结尾的标记（将第二位设为0）
                        tag = tag & 0b01;
                    }
                }
            }
            // 如果起始位置大于结束位置，说明整个字符串都是空白字符，返回空值处理结果
            if (s > e) {
                return handleString("");
            }
            // 如果修剪后的字符串长度等于原字符串长度，说明没有需要修剪的字符，直接转换整个字符串
            if (e - s + 1 == sequence.length()) {
                return handleString(source.toString());
            }

            // 否则，提取修剪后的子序列并转换
            return handleString(sequence.subSequence(s, e + 1).toString());
        } else {
            return handleObject(source);
        }
    }

    /**
     * 判断指定位置的代码点是否应该被修剪掉。
     * <p>
     * 默认实现判断是否为空白字符或空格字符。子类可重写此方法以自定义修剪规则。
     *
     * @param source    源字符序列
     * @param index     代码点在源中的索引位置
     * @param codePoint 需要判断的代码点
     * @return 如果应该被修剪返回 true，否则返回 false
     */
    protected boolean isTrimCodePoint(SourceType source, int index, int codePoint) {
        return Character.isWhitespace(codePoint) || Character.isSpaceChar(codePoint);
    }

    /**
     * 核心转换逻辑抽象方法（非字符串对象）。
     * <p>
     * 子类必须实现此方法以定义具体的类型转换算法。
     * 此方法不应包含类型检查逻辑（由 {@link #convert} 处理），应专注于数据转换。
     * 当源对象不是 {@link CharSequence} 时，{@link #pretreatment(Object)} 会调用此方法。
     *
     * @param source 源对象，保证不为 null（在 converts 调用路径下）
     * @return 转换后的目标对象，可以为 null
     * @throws Throwable 转换过程中发生的任何异常
     */
    protected TargetType handleObject(SourceType source) throws Throwable {
        return null;
    }

    /**
     * 核心转换逻辑抽象方法（字符串对象）。
     * <p>
     * 子类应重写此方法以定义字符串到目标类型的转换逻辑。
     * 当源对象是 {@link CharSequence} 时，{@link #pretreatment(Object)} 会在修剪空白后调用此方法。
     *
     * @param source 修剪后的字符串
     * @return 转换后的目标对象
     * @throws Throwable 转换过程中发生错误时抛出
     */
    protected TargetType handleString(String source) throws Throwable {
        return null;
    }

    /**
     * 返回类型转换器的字符串表示形式，包含类名及源/目标类型信息。
     *
     * @return 格式为 "TypeConverter[ClassName(SourceType->TargetType)]" 的字符串
     */
    @Override
    public String toString() {
        Class<SourceType> srcType = supportsSourceType();
        Class<TargetType> tgtType = supportsTargetType();

        String sourceName = (srcType == null) ? "Unknown" : srcType.getSimpleName();
        String targetName = tgtType.getSimpleName();

        return "TypeConverter[" + getClass().getName() + "(" + sourceName + "->" + targetName + ")]";
    }

    /**
     * 清除当前线程的上下文参数。
     * <p>
     * 实现 {@link Closeable} 接口，支持 try-with-resources 语句自动清理资源，防止内存泄漏。
     * </p>
     */
    @Override
    public void close() {
        if (context != null) {
            context.remove();
        }
    }
}

/*
 * Copyright (c) 2025-2026.
 */

package terraqo.cheetah.converter;

import terraqo.cheetah.Logger;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 * 类型转换器抽象类，用于定义不同类型之间的转换规则
 *
 * @param <SourceType> 源类型
 * @param <TargetType> 目标类型
 * @author Terraqo
 */
public abstract class AbstractTypeConverter<SourceType, TargetType> {
    /**
     * 源类型的Class对象
     */
    private final Class<SourceType> sourceType;
    /**
     * 目标类型的Class对象
     */
    private final Class<TargetType> targetType;
    /**
     * 标识此转换器是否为特定源类型的转换器
     * 如果为true，表示指定了明确的源类型，将使用该类型进行严格匹配
     * 如果为false，表示未指定源类型，将通过supportsSource方法进行动态判断
     */
    private final boolean isSpecific;
    /**
     * 支持的选择器数组，用于在多个转换器中定位到想要转换的源对象
     */
    private final Object[] supportedSelectors;

    /**
     * 构造函数，初始化目标类型和支持的选择器
     *
     * @param targetType         目标类型的Class对象
     * @param supportedSelectors 支持的选择器列表
     * @throws NullPointerException 当targetType为null时抛出
     */
    public AbstractTypeConverter(Class<TargetType> targetType, Object... supportedSelectors) {
        this(null, targetType, supportedSelectors);
    }

    /**
     * 构造函数，初始化目标类型和支持的选择器
     *
     * @param sourceType         源类型的Class对象，可以为null
     * @param targetType         目标类型的Class对象
     * @param supportedSelectors 支持的选择器列表
     * @throws NullPointerException 当targetType为null时抛出
     */
    public AbstractTypeConverter(Class<SourceType> sourceType, Class<TargetType> targetType, Object... supportedSelectors) {
        this.isSpecific = sourceType != null;
        this.targetType = Objects.requireNonNull(targetType);
        this.supportedSelectors = Arrays.stream(supportedSelectors).filter(Objects::nonNull).distinct().toArray();
        if (sourceType != null) {
            this.sourceType = sourceType;
        } else {
            // 获取父类的泛型参数
            int arr = 0, idx = 0;
            Class<?> foundType = null;
            LinkedList<Class<?>> classes = new LinkedList<>();
            for (Class<?> cur = getClass(); cur != AbstractTypeConverter.class; cur = cur.getSuperclass())
                classes.addFirst(cur);
            for (Class<?> cur : classes) {
                Type type = ((ParameterizedType) cur.getGenericSuperclass()).getActualTypeArguments()[idx];
                // 找到数组类型
                for (; type instanceof GenericArrayType; arr++) {
                    type = ((GenericArrayType) type).getGenericComponentType();
                }
                if (type instanceof TypeVariable) {
                    // 查找同一泛型确定索引位置
                    TypeVariable<? extends Class<?>>[] tvs = cur.getTypeParameters();
                    for (int i = 0; i < tvs.length; i++) {
                        if (tvs[i] != type) continue;
                        idx = i;
                        type = tvs[i].getBounds()[0];
                        foundType = (Class<?>) (type instanceof ParameterizedType ? ((ParameterizedType) type).getRawType() : type);
                        break;
                    }
                } else {
                    // 确定泛型类型，结束查找
                    foundType = (Class<?>) (type instanceof ParameterizedType ? ((ParameterizedType) type).getRawType() : type);
                    break;
                }
            }
            if (arr != 0) {
                foundType = Array.newInstance(foundType, new int[arr]).getClass();
            }
            //noinspection ReassignedVariable,unchecked
            this.sourceType = (Class<SourceType>) foundType;
        }
    }

    /**
     * 执行类型转换操作
     *
     * @param <SourceType>              源类型泛型参数
     * @param <TargetType>              目标类型泛型参数
     * @param sourceType                源类型Class对象
     * @param source                    源对象实例
     * @param targetType                目标类型Class对象
     * @param targetDefault             目标类型的默认值
     * @param foundTypeConverter        找到的类型转换器
     * @param isThrowConverterException 是否在转换异常时抛出异常
     * @return 转换后的目标对象，如果转换失败则返回默认值
     */
    public static <SourceType, TargetType> TargetType performConversion(Class<SourceType> sourceType, SourceType source, Class<TargetType> targetType, TargetType targetDefault,
                                                                        AbstractTypeConverter<SourceType, TargetType> foundTypeConverter, boolean isThrowConverterException) {
        TargetType target = null;
        // 验证转换器是否支持目标类型
        if (foundTypeConverter.supportsTargetType() != targetType) {
            throw new TypeConverterException("The converter does not support the target type, possibly due to an error in the target type when registering the converter");
        }
        try {
            // 执行类型转换操作
            target = foundTypeConverter.converts(source, targetDefault);
        } catch (TypeConverterException e) {
            if (isThrowConverterException) {
                throw e;
            } else {
                // 记录转换失败的日志信息
                if (Logger.isEnabledWarn()) {
                    Logger.warn("Failed to convert source type [%s] to target type [%s] using converter [%s]", sourceType, targetType, foundTypeConverter, e);
                }
            }
        }
        // 返回转换结果，如果转换失败则返回默认值
        return target != null ? targetType.cast(target) : targetDefault;
    }

    /**
     * 获取源类型Class对象
     * 如果返回null，则使用 {@link #isCanConvert(Object, Object[])}或{@link #isSupportsSource(Object)} 进行判断，
     * 如果不为null，则使用本函数返回的Class对象进行类型匹配判断
     *
     * @return 源类型的Class对象
     */
    public final Class<SourceType> supportsSourceType() {
        return sourceType;
    }

    /**
     * 获取目标类型Class对象
     *
     * @return 目标类型的Class对象，不能为空
     */
    public final Class<TargetType> supportsTargetType() {
        return targetType;
    }

    /**
     * 判断此转换器是否为特定源类型的转换器
     *
     * @return 如果是特定源类型的转换器返回true，否则返回false
     */
    public final boolean isSpecific() {
        return isSpecific;
    }

    /**
     * 判断指定的源对象是否支持转换
     * 当{@link #supportsSourceType()}返回null时，会调用此方法进行判断
     *
     * @param source    源对象
     * @param selectors 用于选择在多个转换器中定为到想要转换的源对象，对象可以是任何类型，枚举，字符串，数字等
     * @return 如果支持转换返回true，否则返回false
     */
    public final boolean isCanConvert(Object source, Object... selectors) {
        if (!this.isSupportsSource(source)) {
            return false;
        }
        return this.isContainsSelector(selectors);
    }

    /**
     * 判断指定的源对象是否支持转换
     * 当{@link #supportsSourceType()}返回null时，会调用此方法进行判断
     *
     * @param source 源对象
     * @return 如果支持转换返回true，否则返回false
     */
    public boolean isSupportsSource(Object source) {
        if (isSpecific()) {
            throw new TypeConverterException("Please implement the method in the subclass");
        } else {
            return supportsSourceType().isInstance(source);
        }
    }

    /**
     * 检查当前对象是否包含指定的选择器
     *
     * @param selectors 要检查的选择器数组，可以为null
     * @return 如果包含任意一个指定的选择器则返回true，否则返回false
     */
    public final boolean isContainsSelector(Object... selectors) {
        // 处理输入参数，如果为null则转换为空数组，并过滤掉null元素
        selectors = selectors == null ? new Object[0] : Arrays.stream(selectors).filter(Objects::nonNull).toArray();
        if (supportedSelectors.length == 0) {
            return selectors.length == 0;
        }
        // 遍历所有传入的选择器
        for (Object selector : selectors) {
            // 将当前选择器与所有支持的选择器进行比较
            for (Object supportedSelector : supportedSelectors) {
                // 如果支持的选择器是字符串类型，进行忽略大小写的字符串比较
                if (supportedSelector instanceof String) {
                    if (supportedSelector.toString().equalsIgnoreCase(selector.toString())) {
                        return true;
                    }
                } else if (Objects.equals(selector, supportedSelector)) {
                    // 对于非字符串类型，使用Objects.equals进行比较
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将源对象转换为目标类型对象
     * 使用默认值为null的转换方法
     *
     * @param source 源对象
     * @return 转换后的目标对象，可能为null
     * @throws TypeConverterException 转换过程中发生错误时抛出
     */
    public final TargetType converts(SourceType source) throws TypeConverterException {
        return this.converts(source, null);
    }

    /**
     * 将源对象转换为目标类型对象
     *
     * @param source       源对象
     * @param defaultValue 默认值，当转换失败时返回此值
     * @return 转换后的目标对象，可能为null
     * @throws TypeConverterException 转换过程中发生错误时抛出
     */
    public final TargetType converts(SourceType source, TargetType defaultValue) throws TypeConverterException {
        try {
            TargetType result = null;
            // 检查源对象是否为null
            if (source != null) {
                // 验证源对象类型是否支持转换
                if (isSpecific() || isSupportsSource(source)) {
                    // 执行具体的类型转换逻辑
                    result = this.convertsUnchecked(source);
                }
            }
            // 如果转换成功且结果不为null，则返回转换结果
            if (result != null) {
                return result;
            }
        } catch (Throwable e) {
            // 捕获转换异常并重新抛出，添加上下文信息
            throw new TypeConverterException("Failed to convert [" + supportsSourceType() + "] to [" + supportsTargetType() + "] with '" + source + "'!", e);
        }
        // 转换失败或结果为null时返回默认值
        return defaultValue;
    }

    /**
     * 将源对象转换为目标类型对象，不进行源对象类型检查
     *
     * @param source 源对象
     * @return 转换后的目标对象，可能为null
     * @throws Throwable 转换过程中发生错误时抛出
     */
    public abstract TargetType convertsUnchecked(SourceType source) throws Throwable;

    /**
     * 返回类型转换器的字符串表示形式
     *
     * @return 类型转换器的字符串表示
     */
    @Override
    public String toString() {
        Class<SourceType> sourceType = supportsSourceType();
        Class<TargetType> targetType = supportsTargetType();
        if (sourceType == null || targetType == null) {
            return "TypeConverter[" + getClass().getName() + "]";
        } else {
            return "TypeConverter[" + getClass().getName() + "(" + sourceType.getSimpleName() + "->" + targetType.getSimpleName() + ")]";
        }
    }

}

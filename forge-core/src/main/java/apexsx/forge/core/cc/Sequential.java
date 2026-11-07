/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package apexsx.forge.core.cc;

import io.github.terraqo.foundation.util.iteration.ArrayIterator;

import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

/**
 * 序列列表
 * <p>
 * 提供链式调用的通用执行逻辑，支持按不同策略遍历委托对象并调用指定方法，
 * 避免子类重复编写遍历逻辑。
 *
 * @author potatoxf
 */
@SuppressWarnings("unchecked")
public abstract class Sequential<T> implements Iterable<T>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.publicLookup();
    private final Object[] values;

    /**
     * 链式调用模式
     */
    public enum InvokeMode {
        /**
         * 返回第一个非空结果，遍历所有委托对象调用方法，返回第一个非 null 的结果
         */
        FIRSTNonNull,
        /**
         * 返回第一个成功结果，遍历所有委托对象调用方法，跳过抛出异常的对象，返回第一个未抛出异常的结果
         */
        FIRST_SUCCESS
    }

    /**
     * 使用首个元素和其余元素构造序列
     *
     * @param first  首个元素，不能为 null
     * @param others 其余元素
     */
    @SafeVarargs
    public Sequential(T first, T... others) {
        values = new Object[others.length + 1];
        values[0] = first;
        System.arraycopy(others, 0, values, 1, others.length);
    }

    /**
     * 使用集合构造序列
     *
     * @param delegates 委托对象集合，不能为空
     */
    public Sequential(Collection<T> delegates) {
        Objects.requireNonNull(delegates, "delegates must not be null");
        Object[] others = delegates.toArray();
        values = Arrays.copyOf(others, others.length + 1);
    }

    // ======================== 方法句柄创建 ========================

    /**
     * 创建用于在委托对象上调用接口方法的方法句柄
     *
     * @param delegateInterface 委托接口类
     * @param methodName        方法名
     * @return 方法句柄
     * @throws NoSuchMethodException    如果方法不存在
     * @throws IllegalAccessException   如果方法不可访问
     */
    public static MethodHandle createHandle(Class<?> delegateInterface, String methodName)
            throws NoSuchMethodException, IllegalAccessException {
        return LOOKUP.findVirtual(delegateInterface, methodName,
                MethodType.methodType(Object.class, Object[].class));
    }

    /**
     * 创建用于在委托对象上调用接口方法的方法句柄（支持重载方法消歧）
     *
     * @param delegateInterface 委托接口类
     * @param methodName        方法名
     * @param parameterTypes    参数类型列表，用于定位重载方法
     * @return 方法句柄
     * @throws NoSuchMethodException    如果方法不存在
     * @throws IllegalAccessException   如果方法不可访问
     */
    public static MethodHandle createHandle(Class<?> delegateInterface, String methodName,
                                            Class<?>... parameterTypes)
            throws NoSuchMethodException, IllegalAccessException {
        Class<?>[] fullParamTypes = new Class<?>[parameterTypes.length + 1];
        fullParamTypes[0] = Object[].class;
        System.arraycopy(parameterTypes, 0, fullParamTypes, 1, parameterTypes.length);
        return LOOKUP.findVirtual(delegateInterface, methodName,
                MethodType.methodType(Object.class, fullParamTypes));
    }

    // ======================== 链式调用执行 ========================

    /**
     * 按指定模式对委托对象链式调用方法
     * <p>
     * 遍历所有委托对象，使用 {@link MethodHandle} 调用指定方法，
     * 根据调用模式决定返回策略。
     *
     * @param mode              调用模式
     * @param delegateInterface 委托接口类
     * @param methodName        方法名
     * @param args              方法参数
     * @return 按模式决定的返回值，如果所有委托对象均未产生有效结果则返回 null
     * @throws RuntimeException 如果方法句柄创建或调用过程中发生受检异常，将包装为运行时异常抛出
     */
    protected final Object doit(InvokeMode mode, Class<?> delegateInterface,
                                String methodName, Object... args) {
        try {
            MethodHandle handle = createHandle(delegateInterface, methodName);
            return executeChainCall(handle, mode, args);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException("Failed to create method handle for " +
                    delegateInterface.getSimpleName() + "." + methodName, e);
        }
    }

    /**
     * 按指定模式对委托对象链式调用方法（使用预创建的方法句柄）
     * <p>
     * 适用于需要缓存方法句柄以提升性能的场景。
     *
     * @param mode   调用模式
     * @param handle 预创建的方法句柄
     * @param args   方法参数
     * @return 按模式决定的返回值，如果所有委托对象均未产生有效结果则返回 null
     */
    protected final Object doit(InvokeMode mode, MethodHandle handle, Object... args) {
        return executeChainCall(handle, mode, args);
    }

    /**
     * 按指定模式对委托对象链式调用方法（使用预创建的方法句柄并指定返回类型）
     *
     * @param <R>    期望的返回类型
     * @param mode   调用模式
     * @param handle 预创建的方法句柄
     * @param args   方法参数
     * @return 按模式决定的返回值，如果所有委托对象均未产生有效结果则返回 null
     */
    protected final <R> R doitTyped(InvokeMode mode, MethodHandle handle, Object... args) {
        return (R) executeChainCall(handle, mode, args);
    }

    /**
     * 执行链式调用的核心逻辑
     *
     * @param handle 方法句柄
     * @param mode   调用模式
     * @param args   方法参数
     * @return 执行结果
     */
    private Object executeChainCall(MethodHandle handle, InvokeMode mode, Object[] args) {
        for (Object delegate : values) {
            try {
                Object result = handle.invoke(delegate, args);
                switch (mode) {
                    case FIRSTNonNull:
                        if (result != null) {
                            return result;
                        }
                        break;
                    case FIRST_SUCCESS:
                        return result != null ? result : Boolean.TRUE;
                    default:
                        throw new IllegalStateException("Unknown invoke mode: " + mode);
                }
            } catch (Throwable e) {
                if (mode == InvokeMode.FIRST_SUCCESS) {
                    continue;
                }
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                if (e instanceof Error) {
                    throw (Error) e;
                }
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    // ======================== 排序与工具方法 ========================

    /**
     * 对委托对象序列进行排序
     *
     * @param comparator 比较器
     */
    public void sort(Comparator<? super T> comparator) {
        Arrays.sort(values, (a, b) -> comparator.compare((T) a, (T) b));
    }

    /**
     * 反转委托对象序列的顺序
     */
    public void reverse() {
        int len = values.length;
        for (int i = 0, mid = len / 2; i < mid; i++) {
            Object tmp = values[i];
            int j = len - 1 - i;
            values[i] = values[j];
            values[j] = tmp;
        }
    }

    /**
     * 获取委托对象数量
     *
     * @return 委托对象数量
     */
    public final int size() {
        return values.length;
    }

    /**
     * 获取指定位置的委托对象
     *
     * @param index 索引
     * @return 指定位置的委托对象
     */
    public final T get(int index) {
        return (T) values[index];
    }

    /**
     * 将委托对象序列转为新数组
     *
     * @return 包含所有委托对象的新数组
     */
    public final T[] toArray() {
        return (T[]) Arrays.copyOf(values, values.length);
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>((T[]) values);
    }
}

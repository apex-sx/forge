/*
 * Copyright (c) 2026 Your Company Name. All rights reserved.
 *
 * Author: Terraqo
 */

package io.github.terraqo.foundation;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 用于执行前置条件检查的工具类。
 * <p>
 * 此类提供静态方法来验证方法参数并检测潜在的循环依赖
 * （例如接口默认方法中的无限递归）。
 * 它旨在作为防御性编程的辅助工具使用。
 * </p>
 *
 * @author Terraqo
 */
public final class Precondition {

    /**
     * 防止实例化此工具类。
     *
     * @throws UnsupportedOperationException 始终抛出此异常以指示此类无法被实例化
     */
    private Precondition() {
        throw new UnsupportedOperationException("The class cannot be instantiated");
    }

    /**
     * 确保涉及调用方法参数的表达式的真实性。
     *
     * @param condition 需要检查的布尔条件
     * @throws IllegalArgumentException 如果 {@code condition} 为 false
     */
    public static void checkArgument(boolean condition) {
        Precondition.check(condition, null, null);
    }

    /**
     * 通过分析当前线程的堆栈跟踪来检查循环依赖。
     * <p>
     * 此方法主要用于防止接口默认实现中可能意外调用自身而导致的无限递归。
     * 它通过比较当前堆栈帧与前一个堆栈帧的类名和方法名来进行检测。
     * </p>
     *
     * @param message 一个 {@link Supplier}，用于在检测到循环依赖时提供异常消息。
     *                如果为 {@code null}，则将使用默认消息。
     * @throws UnsupportedOperationException 如果检测到循环依赖
     */
    public static void checkCircularDependency(Supplier<String> message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length >= 4) {
            // stackTrace[0] 是 getStackTrace
            // stackTrace[1] 是 checkCircularDependency
            // stackTrace[2] 是调用 checkCircularDependency 的当前方法
            // stackTrace[3] 是调用当前方法的前一个方法
            StackTraceElement current = stackTrace[2];
            StackTraceElement previous = stackTrace[3];

            // 注意：接口方法的堆栈跟踪行为可能因实现而异。
            // 此检查主要用于防止默认实现调用自身时产生的无限递归。
            String name = current.getClassName();
            if (Objects.equals(name, previous.getClassName())) {
                name = current.getMethodName();
                if (Objects.equals(name, previous.getMethodName())) {
                    // 针对接口上下文的简化检查
                    if (message != null) {
                        throw new UnsupportedOperationException(message.get());
                    } else {
                        throw new UnsupportedOperationException("Circular dependency detected");
                    }
                }
            }
        }
    }

    /**
     * 内部辅助方法，用于评估条件，如果条件为 false 则抛出 {@link IllegalArgumentException}。
     *
     * @param condition 需要评估的布尔条件
     * @param e         异常的根本原因，可以为 {@code null}
     * @param format    异常消息的格式化字符串，可以为 {@code null}
     * @param args      替换到格式化字符串中的参数
     * @throws IllegalArgumentException 如果 {@code condition} 为 false
     */
    private static void check(boolean condition, Throwable e, String format, Object... args) {
        if (!condition) {
            if (format == null) {
                throw new IllegalArgumentException(e);
            } else {
                String message = String.format(format, args);
                throw new IllegalArgumentException(message, e);
            }
        }
    }
}

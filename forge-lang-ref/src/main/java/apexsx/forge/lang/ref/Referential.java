/*
 * Copyright (c) 2026 版权所有。
 *
 * 未经版权所有者事先书面许可，任何人不得以任何方式复制、传播、修改本文件及相关代码。
 * 本代码按"现状"提供，不提供任何明示或暗示的担保，包括但不限于适销性、特定用途适用性的保证。
 * 在法律允许范围内，版权方不对因使用本代码产生的任何损失承担责任。
 *
 * @author Terraqo
 */
package apexsx.forge.lang.ref;

import java.lang.ref.Reference;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 关于引用的扩展
 * 该接口定义了引用的通用行为，扩展了 Supplier 接口，
 * 提供了获取、设置、清除引用值以及比较引用等基础方法。
 *
 * @param <T> 引用对象的类型
 * @author Terraqo
 */
public interface Referential<T> extends Supplier<T> {
    /**
     * 获取引用类型
     *
     * @return 引用类型 {@link ReferentialType}
     */
    ReferentialType type();

    /**
     * 获取引用的值
     * 实现了 Supplier 接口的 get 方法
     *
     * @return 返回引用的值
     */
    T get();

    /**
     * 设置引用的值，并使用对象默认的哈希码
     * 内部调用 {@link #set(Object, int)}，哈希码通过 {@link Objects#hashCode(Object)} 计算
     *
     * @param referent 引用的值
     */
    default void setWithDefaultHashCode(T referent) {
        // 调用 set 方法，传入引用值及其默认哈希码
        set(referent, Objects.hashCode(referent));
    }

    /**
     * 设置引用的值，并使用对象的身份哈希码（基于内存地址）
     * 内部调用 {@link #set(Object, int)}，哈希码通过 {@link System#identityHashCode(Object)} 计算
     *
     * @param referent 引用的值
     */
    default void setWithIdentityHashCode(T referent) {
        // 调用 set 方法，传入引用值及其身份哈希码
        set(referent, System.identityHashCode(referent));
    }

    /**
     * 设置引用的值及其对应的哈希码
     * 默认实现为抛出 UnsupportedOperationException 异常，子类需重写此方法以支持该操作
     *
     * @param referent 引用的值
     * @param hashCode 引用的值的哈希码
     */
    default void set(T referent, int hashCode) {
        // 默认不支持设置操作，强制子类重写
        throw new UnsupportedOperationException();
    }

    /**
     * 清除引用，释放引用的对象
     * 默认实现为空，子类可以重写此方法以实现特定的清理逻辑
     */
    default void clear() {
        // 默认空实现
    }

    /**
     * 比较引用值是否相等，主要用于辅助实现 {@link Object#equals(Object)}
     * 支持与 {@link Reference} 或 {@link Referential} 类型的对象进行比较
     *
     * @param other 另一个引用 {@link Referential} 或 {@link Reference}
     * @return 如果引用的值相等返回 true，否则返回 false
     */
    default boolean equalsReference(Object other) {
        // 如果是同一个对象，直接返回 true
        if (this == other) return true;
        // 如果比较对象为 null，直接返回 false
        if (other == null) return false;
        Object otherReferent;
        // 判断其他对象是否为 java.lang.ref.Reference 类型，并获取其引用值
        if (other instanceof Reference) {
            otherReferent = ((Reference<?>) other).get();
        }
        // 判断其他对象是否为 Referential 类型，并获取其引用值
        else if (other instanceof Referential) {
            otherReferent = ((Referential<?>) other).get();
        }
        // 类型不匹配，直接返回 false
        else {
            return false;
        }
        // 比较当前引用的值与另一个引用的值是否相等
        return Objects.equals(this.get(), otherReferent);
    }

    /**
     * 获取引用值的哈希码，主要用于辅助实现 {@link Object#hashCode()}
     *
     * @return 返回引用值的哈希码
     */
    default int hashCodeReference() {
        // 基于引用的值计算哈希码
        return Objects.hash(this.get());
    }

    /**
     * 获取引用值的字符串表示，主要用于辅助实现 {@link Object#toString()}
     *
     * @return 返回引用值的字符串表示
     */
    default String toStringReference() {
        // 将引用的值转换为字符串
        return String.valueOf(this.get());
    }
}

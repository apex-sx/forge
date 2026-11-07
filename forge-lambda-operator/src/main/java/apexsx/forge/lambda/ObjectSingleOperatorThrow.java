package apexsx.forge.lambda;

/**
 * 这是一个通用的 lambda 函数接口，用于对 1 个 R 类型参数进行操作。。支持抛出异常。
 * 该接口扩展自 {@link ObjectSingleOperator}，增加了异常处理能力。
 *
 * @param <T> 第 1 个参数类型。
 * @param <E> 异常类型，必须是 {@link Exception} 的子类
 * @author zhitron
 */
@FunctionalInterface
public interface ObjectSingleOperatorThrow<T, R, E extends Exception> extends ObjectSingleOperator<T, R> {

    /**
     * 一个空实现的实例，它总是返回 null 值。
     */
    ObjectSingleOperatorThrow<?, ?, ?> EMPTY = (v1) -> null;

    /**
     * 返回一个空实现的实例，它总是返回 null 值。
     *
     * @return 获取一个空的函数式接口实例。
     */
    @SuppressWarnings("unchecked")
    static <T, R, E extends Exception> ObjectSingleOperatorThrow<T, R, E> empty() {
        return (ObjectSingleOperatorThrow<T, R, E>) EMPTY;
    }

    /**
     * 创建一个始终返回指定常量值的函数式接口。
     *
     * @param value 常量值。
     * @return 返回指定常量值的函数式接口。
     */
    static <T, R, E extends Exception> ObjectSingleOperatorThrow<T, R, E> constant(R value) {
        return (v1) -> value;
    }

    /**
     * 对给定的 1 个 OBJECT 参数执行某种操作并返回结果。
     *
     * @param v1 类型为 T 的第 1 个参数。
     * @return 操作后的 R 结果。
     * @throws E 抛出执行过程中的异常
     */
    R applyThrow(T v1) throws E;

    /**
     * 默认方法实现，用于应用某些操作或逻辑
     * 该方法旨在封装对 {@link #applyThrow} 方法的调用，并处理可能抛出的异常
     *
     * @param v1 类型为 T 的第 1 个参数。
     * @return 操作后的 R 结果。
     */
    @Override
    default R apply(T v1) {
        try {
            return this.applyThrow(v1);
        } catch (Exception e) {
            throw new RuntimeException("Exception for 'applyThrow'", e);
        }
    }
}

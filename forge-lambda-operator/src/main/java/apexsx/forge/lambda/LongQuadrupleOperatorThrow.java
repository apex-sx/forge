package apexsx.forge.lambda;

/**
 * 这是一个通用的 lambda 函数接口，用于对 4 个 long 类型参数进行操作。。支持抛出异常。
 * 该接口扩展自 {@link LongQuadrupleOperator}，增加了异常处理能力。
 *
 * @param <E> 异常类型，必须是 {@link Exception} 的子类
 * @author zhitron
 */
@FunctionalInterface
public interface LongQuadrupleOperatorThrow<E extends Exception> extends LongQuadrupleOperator {

    /**
     * 一个空实现的实例，它总是返回 0L 值。
     */
    LongQuadrupleOperatorThrow<?> EMPTY = (v1, v2, v3, v4) -> 0L;

    /**
     * 返回一个空实现的实例，它总是返回 0L 值。
     *
     * @return 获取一个空的函数式接口实例。
     */
    @SuppressWarnings("unchecked")
    static <E extends Exception> LongQuadrupleOperatorThrow<E> empty() {
        return (LongQuadrupleOperatorThrow<E>) EMPTY;
    }

    /**
     * 创建一个始终返回指定常量值的函数式接口。
     *
     * @param value 常量值。
     * @return 返回指定常量值的函数式接口。
     */
    static <E extends Exception> LongQuadrupleOperatorThrow<E> constant(long value) {
        return (v1, v2, v3, v4) -> value;
    }

    /**
     * 对给定的 4 个 LONG 参数执行某种操作并返回结果。
     *
     * @param v1 类型为 long 的第 1 个参数。
     * @param v2 类型为 long 的第 2 个参数。
     * @param v3 类型为 long 的第 3 个参数。
     * @param v4 类型为 long 的第 4 个参数。
     * @return 操作后的 long 结果。
     * @throws E 抛出执行过程中的异常
     */
    long applyThrow(long v1, long v2, long v3, long v4) throws E;

    /**
     * 默认方法实现，用于应用某些操作或逻辑
     * 该方法旨在封装对 {@link #applyThrow} 方法的调用，并处理可能抛出的异常
     *
     * @param v1 类型为 long 的第 1 个参数。
     * @param v2 类型为 long 的第 2 个参数。
     * @param v3 类型为 long 的第 3 个参数。
     * @param v4 类型为 long 的第 4 个参数。
     * @return 操作后的 long 结果。
     */
    @Override
    default long apply(long v1, long v2, long v3, long v4) {
        try {
            return this.applyThrow(v1, v2, v3, v4);
        } catch (Exception e) {
            throw new RuntimeException("Exception for 'applyThrow'", e);
        }
    }
}

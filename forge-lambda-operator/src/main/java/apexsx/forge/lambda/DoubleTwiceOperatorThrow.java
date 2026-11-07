package apexsx.forge.lambda;

/**
 * 这是一个通用的 lambda 函数接口，用于对 2 个 double 类型参数进行操作。。支持抛出异常。
 * 该接口扩展自 {@link DoubleTwiceOperator}，增加了异常处理能力。
 *
 * @param <E> 异常类型，必须是 {@link Exception} 的子类
 * @author zhitron
 */
@FunctionalInterface
public interface DoubleTwiceOperatorThrow<E extends Exception> extends DoubleTwiceOperator {

    /**
     * 一个空实现的实例，它总是返回 0.0D 值。
     */
    DoubleTwiceOperatorThrow<?> EMPTY = (v1, v2) -> 0.0D;

    /**
     * 返回一个空实现的实例，它总是返回 0.0D 值。
     *
     * @return 获取一个空的函数式接口实例。
     */
    @SuppressWarnings("unchecked")
    static <E extends Exception> DoubleTwiceOperatorThrow<E> empty() {
        return (DoubleTwiceOperatorThrow<E>) EMPTY;
    }

    /**
     * 创建一个始终返回指定常量值的函数式接口。
     *
     * @param value 常量值。
     * @return 返回指定常量值的函数式接口。
     */
    static <E extends Exception> DoubleTwiceOperatorThrow<E> constant(double value) {
        return (v1, v2) -> value;
    }

    /**
     * 对给定的 2 个 DOUBLE 参数执行某种操作并返回结果。
     *
     * @param v1 类型为 double 的第 1 个参数。
     * @param v2 类型为 double 的第 2 个参数。
     * @return 操作后的 double 结果。
     * @throws E 抛出执行过程中的异常
     */
    double applyThrow(double v1, double v2) throws E;

    /**
     * 默认方法实现，用于应用某些操作或逻辑
     * 该方法旨在封装对 {@link #applyThrow} 方法的调用，并处理可能抛出的异常
     *
     * @param v1 类型为 double 的第 1 个参数。
     * @param v2 类型为 double 的第 2 个参数。
     * @return 操作后的 double 结果。
     */
    @Override
    default double apply(double v1, double v2) {
        try {
            return this.applyThrow(v1, v2);
        } catch (Exception e) {
            throw new RuntimeException("Exception for 'applyThrow'", e);
        }
    }
}

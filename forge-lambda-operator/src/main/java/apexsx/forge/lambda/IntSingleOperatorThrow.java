package apexsx.forge.lambda;

/**
 * 这是一个通用的 lambda 函数接口，用于对 1 个 int 类型参数进行操作。。支持抛出异常。
 * 该接口扩展自 {@link IntSingleOperator}，增加了异常处理能力。
 *
 * @param <E> 异常类型，必须是 {@link Exception} 的子类
 * @author zhitron
 */
@FunctionalInterface
public interface IntSingleOperatorThrow<E extends Exception> extends IntSingleOperator {

    /**
     * 一个空实现的实例，它总是返回 0 值。
     */
    IntSingleOperatorThrow<?> EMPTY = (v1) -> 0;

    /**
     * 返回一个空实现的实例，它总是返回 0 值。
     *
     * @return 获取一个空的函数式接口实例。
     */
    @SuppressWarnings("unchecked")
    static <E extends Exception> IntSingleOperatorThrow<E> empty() {
        return (IntSingleOperatorThrow<E>) EMPTY;
    }

    /**
     * 创建一个始终返回指定常量值的函数式接口。
     *
     * @param value 常量值。
     * @return 返回指定常量值的函数式接口。
     */
    static <E extends Exception> IntSingleOperatorThrow<E> constant(int value) {
        return (v1) -> value;
    }

    /**
     * 对给定的 1 个 INT 参数执行某种操作并返回结果。
     *
     * @param v1 类型为 int 的第 1 个参数。
     * @return 操作后的 int 结果。
     * @throws E 抛出执行过程中的异常
     */
    int applyThrow(int v1) throws E;

    /**
     * 默认方法实现，用于应用某些操作或逻辑
     * 该方法旨在封装对 {@link #applyThrow} 方法的调用，并处理可能抛出的异常
     *
     * @param v1 类型为 int 的第 1 个参数。
     * @return 操作后的 int 结果。
     */
    @Override
    default int apply(int v1) {
        try {
            return this.applyThrow(v1);
        } catch (Exception e) {
            throw new RuntimeException("Exception for 'applyThrow'", e);
        }
    }
}

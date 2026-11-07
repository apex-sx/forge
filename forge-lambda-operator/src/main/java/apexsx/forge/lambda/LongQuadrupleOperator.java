package apexsx.forge.lambda;

/**
 * 这是一个通用的 lambda 函数接口，用于对 4 个 long 类型参数进行操作。。
 *
 * @author zhitron
 */
@FunctionalInterface
public interface LongQuadrupleOperator {

    /**
     * 一个空实现的实例，它总是返回 0L 值。
     */
    LongQuadrupleOperator EMPTY = (v1, v2, v3, v4) -> 0L;

    /**
     * 返回一个空实现的实例，它总是返回 0L 值。
     *
     * @return 获取一个空的函数式接口实例。
     */
    static LongQuadrupleOperator empty() {
        return EMPTY;
    }

    /**
     * 创建一个始终返回指定常量值的函数式接口。
     *
     * @param value 常量值。
     * @return 返回指定常量值的函数式接口。
     */
    static LongQuadrupleOperator constant(long value) {
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
     */
    long apply(long v1, long v2, long v3, long v4);
}

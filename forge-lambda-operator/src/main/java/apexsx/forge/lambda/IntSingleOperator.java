package apexsx.forge.lambda;

/**
 * 这是一个通用的 lambda 函数接口，用于对 1 个 int 类型参数进行操作。。
 *
 * @author zhitron
 */
@FunctionalInterface
public interface IntSingleOperator {

    /**
     * 一个空实现的实例，它总是返回 0 值。
     */
    IntSingleOperator EMPTY = (v1) -> 0;

    /**
     * 返回一个空实现的实例，它总是返回 0 值。
     *
     * @return 获取一个空的函数式接口实例。
     */
    static IntSingleOperator empty() {
        return EMPTY;
    }

    /**
     * 创建一个始终返回指定常量值的函数式接口。
     *
     * @param value 常量值。
     * @return 返回指定常量值的函数式接口。
     */
    static IntSingleOperator constant(int value) {
        return (v1) -> value;
    }

    /**
     * 对给定的 1 个 INT 参数执行某种操作并返回结果。
     *
     * @param v1 类型为 int 的第 1 个参数。
     * @return 操作后的 int 结果。
     */
    int apply(int v1);
}

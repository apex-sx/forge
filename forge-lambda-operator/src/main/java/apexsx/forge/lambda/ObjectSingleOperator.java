package apexsx.forge.lambda;

/**
 * 这是一个通用的 lambda 函数接口，用于对 1 个 R 类型参数进行操作。。
 *
 * @param <T> 第 1 个参数类型。
 * @author zhitron
 */
@FunctionalInterface
public interface ObjectSingleOperator<T, R> {

    /**
     * 一个空实现的实例，它总是返回 null 值。
     */
    ObjectSingleOperator<?, ?> EMPTY = (v1) -> null;

    /**
     * 返回一个空实现的实例，它总是返回 null 值。
     *
     * @return 获取一个空的函数式接口实例。
     */
    @SuppressWarnings("unchecked")
    static <T, R> ObjectSingleOperator<T, R> empty() {
        return (ObjectSingleOperator<T, R>) EMPTY;
    }

    /**
     * 创建一个始终返回指定常量值的函数式接口。
     *
     * @param value 常量值。
     * @return 返回指定常量值的函数式接口。
     */
    static <T, R> ObjectSingleOperator<T, R> constant(R value) {
        return (v1) -> value;
    }

    /**
     * 对给定的 1 个 OBJECT 参数执行某种操作并返回结果。
     *
     * @param v1 类型为 T 的第 1 个参数。
     * @return 操作后的 R 结果。
     */
    R apply(T v1);
}

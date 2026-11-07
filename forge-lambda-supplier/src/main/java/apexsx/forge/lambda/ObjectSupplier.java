package apexsx.forge.lambda;

import java.util.function.Supplier;

/**
 * 一个函数式接口，用于表示一个提供 Object 值的供应方。
 * 该接口适用于需要延迟计算或按需提供 Object 值的场景。
 *
 * @param <R> 供应结果的类型
 * @author zhitron
 */
public interface ObjectSupplier<R> extends Supplier<R> {

    /**
     * 获取一个 Object 值。
     *
     * @return 提供的 Object 值
     */
    R get();
}

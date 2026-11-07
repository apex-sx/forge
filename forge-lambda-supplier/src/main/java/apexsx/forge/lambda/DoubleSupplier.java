package apexsx.forge.lambda;

/**
 * 一个函数式接口，用于表示一个提供 double 值的供应方。
 * 该接口适用于需要延迟计算或按需提供 double 值的场景。
 *
 * @author zhitron
 */
public interface DoubleSupplier {

    /**
     * 获取一个 double 值。
     *
     * @return 提供的 double 值
     */
    double getAsDouble();
}
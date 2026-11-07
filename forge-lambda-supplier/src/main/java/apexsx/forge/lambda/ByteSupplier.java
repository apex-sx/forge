package apexsx.forge.lambda;

/**
 * 一个函数式接口，用于表示一个提供 byte 值的供应方。
 * 该接口适用于需要延迟计算或按需提供 byte 值的场景。
 *
 * @author zhitron
 */
public interface ByteSupplier {

    /**
     * 获取一个 byte 值。
     *
     * @return 提供的 byte 值
     */
    byte getAsByte();
}
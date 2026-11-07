package apexsx.forge.lambda;

/**
 * 一个扩展自 {@link FloatSupplier} 的函数式接口，允许在其方法中抛出指定类型的异常。
 * 此接口适用于需要延迟计算或按需提供 float 值，并且可能抛出受检异常的场景。
 *
 * @param <E> 接口方法可以抛出的异常类型，必须是 {@link Throwable} 的子类型
 * @author zhitron
 */
public interface FloatSupplierThrow<E extends Exception> extends FloatSupplier {

    /**
     * 获取一个 float 值，并可能抛出指定类型的异常。
     *
     * @return 提供的 float 值
     * @throws E 当操作失败时抛出的异常
     */
    float getAsFloatThrow() throws E;

    /**
     * 默认实现，调用 {@link #getAsFloatThrow()} 方法并捕获其中抛出的异常，将其包装为运行时异常后抛出。
     * 如果发生异常，将抛出一个新的 {@link RuntimeException}，其中包含原始异常的信息。
     *
     * @return 提供的 float 值
     * @throws RuntimeException 包装了 {@link #getAsFloatThrow()} 方法中抛出的异常
     */
    @Override
    default float getAsFloat() {
        try {
            return getAsFloatThrow();
        } catch (Exception e) {
            throw new RuntimeException("Exception for 'getAsFloatThrow'", e);
        }
    }
}
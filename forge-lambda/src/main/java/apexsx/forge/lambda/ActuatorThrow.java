package apexsx.forge.lambda;

/**
 * 执行器接口，用于定义可执行的操作。
 *
 * @author zhitron
 */
@FunctionalInterface
public interface ActuatorThrow<E extends Exception> extends Actuator {
    /**
     * 定义需要抛出异常的执行方法
     *
     * @throws E 异常类型，由接口实现时指定
     */
    void executeThrow() throws E;

    /**
     * 默认执行方法，对executeThrow进行封装处理
     * 如果executeThrow抛出异常，则将其包装成RuntimeException抛出
     */
    @Override
    default void execute() {
        try {
            executeThrow();
        } catch (Exception e) {
            throw new RuntimeException("The lambda function execution appears exception", e);
        }
    }
}
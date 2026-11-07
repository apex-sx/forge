package apexsx.forge.lambda;

/**
 * 执行器接口，用于定义可执行的操作。
 *
 * @author zhitron
 */
@FunctionalInterface
public interface Actuator {
    /**
     * 执行具体操作
     */
    void execute();
}
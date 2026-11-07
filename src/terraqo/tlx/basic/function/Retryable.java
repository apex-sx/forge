package pxf.toolkit.basic.function;

import pxf.toolkit.basic.exception.RetryException;
import pxf.toolkit.basic.exception.TemporaryException;
import pxf.toolkit.basic.util.Sleep;

/**
 * 可重新尝试的
 *
 * @author potatoxf
 * @date 2021/3/28
 */
public interface Retryable {

  /**
   * 执行动作
   *
   * @throws TemporaryException for an error condition that can be temporary - i.e. retrying later
   *     could be successful
   * @throws Throwable for all other error conditions
   */
  void attempt() throws Throwable;
  /**
   * 执行
   *
   * @param interval 间隔
   * @param timeout 超时
   * @throws RetryException 如果重新尝试失败抛出此异常
   */
  default void execute(long interval, long timeout) throws RetryException {
    execute(0L, interval, timeout);
  }

  /**
   * 执行
   *
   * @param delay 延迟
   * @param interval 间隔
   * @param timeout 超时
   * @throws RetryException 如果重新尝试失败抛出此异常
   */
  default void execute(long delay, long interval, long timeout) throws RetryException {
    long start = System.currentTimeMillis();
    if (delay > 0L) {
      Sleep.fixedMillisecond(delay);
    }
    while (true) {
      try {
        attempt();
        return;
      } catch (Throwable e) {
        if (System.currentTimeMillis() - start < timeout) {
          Sleep.fixedMillisecond(interval);
        } else {
          throw new RetryException(e.getCause());
        }
      }
    }
  }
}

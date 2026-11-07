package apexsx.forge.util.concurrent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 可重入自旋锁
 *
 * @author potatoxf
 * @date 2021/3/14
 */
public class ReentrantSpinLock {

  /** 自旋锁 */
  private final transient AtomicBoolean spinLock = new AtomicBoolean(false);

  private volatile Thread currentThread = null;

  /**
   * 加锁操作
   *
   * <p>当锁被占用的话就循环等待
   *
   * <p>{@code lock()}和 {@code unlock()}应该成对出现
   */
  @SuppressWarnings("StatementWithEmptyBody")
  public final void lock() {
    Thread ct = Thread.currentThread();
    if (!ct.equals(currentThread)) {
      while (!spinLock.compareAndSet(false, true)) {
        // NOT TO DO
      }
      currentThread = ct;
    }
  }

  /**
   * 解锁操作
   *
   * <p>当锁未被加锁的话就循环等待
   *
   * <p>{@code lock()}和 {@code unlock()}应该成对出现
   */
  @SuppressWarnings("StatementWithEmptyBody")
  public final void unlock() {
    Thread ct = Thread.currentThread();
    if (ct.equals(currentThread)) {
      while (!spinLock.compareAndSet(true, false)) {
        // NOT TO DO
      }
      currentThread = null;
    }
  }
}

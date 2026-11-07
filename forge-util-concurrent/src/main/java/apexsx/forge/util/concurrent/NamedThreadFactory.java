package apexsx.forge.util.concurrent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 命名线程工厂
 *
 * @author potatoxf
 * @date 2021/4/20
 */
public class NamedThreadFactory implements ThreadFactory {

  private static final AtomicInteger TOTAL = new AtomicInteger(0);
  private static final Map<String, AtomicInteger> NAMED_TOTAL = new ConcurrentHashMap<>();

  private final String baseName;
  private final boolean daemon;

  public NamedThreadFactory(String baseName) {
    this(baseName, true);
  }

  public NamedThreadFactory(String baseName, boolean daemon) {
    this.baseName = baseName;
    this.daemon = daemon;
  }

  public static int totalThread() {
    return TOTAL.get();
  }

  public static int totalThread(String baseName) {
    AtomicInteger total = NAMED_TOTAL.get(baseName);
    if (total == null) {
      return 0;
    }
    return total.get();
  }

  private static int getNameNextIndex(String baseName) {
    AtomicInteger atomicInteger = NAMED_TOTAL.get(baseName);
    if (atomicInteger == null) {
      atomicInteger = new AtomicInteger(0);
      NAMED_TOTAL.put(baseName, atomicInteger);
    }
    return atomicInteger.incrementAndGet();
  }

  @Override
  public Thread newThread( Runnable runnable) {
    int totalIndex = TOTAL.incrementAndGet();
    int namedIndex = getNameNextIndex(baseName);
    Thread thread =
        new Thread(runnable, "THREAD[" + totalIndex + "]-" + baseName + "-" + namedIndex);
    thread.setDaemon(daemon);
    return thread;
  }
}

package pxf.toolkit.basic.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该工具类主要用于零零散散的操不常用的操作
 *
 * @author potatoxf
 * @date 2020/12/26
 */
public final class Op {

  private static final Logger LOG = LoggerFactory.getLogger(Op.class);

  private Op() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  public static void setAccessible(@Nullable AccessibleObject accessibleObject) {
    if (accessibleObject == null) {
      return;
    }
    try {
      //
      // XXX Default access superclass workaround
      //
      // When a public class has a default access superclass
      // with public methods, these methods are accessible.
      // Calling them from compiled code works fine.
      //
      // Unfortunately, using reflection to invoke these methods
      // seems to (wrongly) to prevent access even when the method
      // modifer is public.
      //
      // The following workaround solves the problem but will only
      // work from sufficiently privilages code.
      //
      // Better workarounds would be greatfully accepted.
      //
      accessibleObject.setAccessible(true);
    } catch (final SecurityException se) {
      boolean vulnerableJVM = false;
      try {
        final String specVersion = System.getProperty("java.specification.version");
        if (specVersion.charAt(0) == '1'
            && (specVersion.charAt(2) == '0'
                || specVersion.charAt(2) == '1'
                || specVersion.charAt(2) == '2'
                || specVersion.charAt(2) == '3')) {
          vulnerableJVM = true;
        }
      } catch (final SecurityException e) {
        vulnerableJVM = true;
      }
      if (vulnerableJVM) {
        LOG.warn(
            "Current Security Manager restricts use of workarounds for reflection bugs in pre-1.4 JVMs.");
      }
      LOG.debug(
          "Cannot setAccessible on method. Therefore cannot use jvm access bug workaround.", se);
    }
  }

  /**
   * If we're running on JDK 1.4 or later, initialize the cause for the given throwable.
   *
   * @param throwable The throwable.
   * @param cause The cause of the throwable.
   * @return true if the cause was initialized, otherwise false.
   * @since 1.8.0
   */
  public static boolean initCause(Throwable throwable, Throwable cause) {
    Method method = Get.initCauseMethod();
    if (method != null && cause != null) {
      try {
        method.invoke(throwable, cause);
        return true;
      } catch (Throwable e) {
        // can't initialize cause
        return false;
      }
    }
    return false;
  }

  /**
   * 深度克隆对象
   *
   * @param object 对象
   * @param <T> 对象类型
   * @return 返回新的克隆对象，如果返回 {@code null}则表示克隆失败
   */
  @Nonnull
  @SuppressWarnings("unchecked")
  public static <T> T deepClone(@Nonnull T object) {
    Assert.noNull(object, "]cloned object");
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(object);
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      ois.close();
      return (T) ois.readObject();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(
          String.format(
              "Deep cloning error, the cloned object must implement Serializable interface, current type [%s]",
              object.getClass()),
          e);
    } catch (IOException e) {
      throw new RuntimeException("Deep cloning error, IO Stream Exception", e);
    }
  }

  /**
   * 关闭资源
   *
   * @param closeables 可关闭资源
   */
  public static void close(Closeable... closeables) {
    if (Is.empty(closeables)) {
      return;
    }
    for (Closeable closeable : closeables) {
      if (Is.nvl(closeable)) {
        continue;
      }
      try {
        closeable.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 随机休眠
   *
   * @param maxMillis 最大毫秒数
   * @return 返回随机休眠时间
   */
  public static int randomSleep(final int maxMillis) {
    return randomSleep(maxMillis, 0);
  }

  /**
   * 随机休眠
   *
   * @param maxMillisecond 最大毫秒数
   * @param minMillisecond 最小毫秒数
   */
  public static int randomSleep(final int maxMillisecond, final int minMillisecond) {
    if (minMillisecond > maxMillisecond) {
      throw new IllegalArgumentException("The max millisecond less then min millisecond");
    }
    int minMillis = Math.max(minMillisecond, 0);
    int maxMillis = Math.max(maxMillisecond, 0);
    int sleepTime;
    if (maxMillis == minMillis) {
      sleepTime = minMillis;
    } else {
      sleepTime = new Random().nextInt(Math.abs(maxMillisecond - minMillis)) + minMillis;
    }
    sleep(sleepTime);
    return sleepTime;
  }

  /**
   * 休眠
   *
   * @param millis 毫秒数
   */
  public static void sleep(final long millis) {
    try {
      if (LOG.isTraceEnabled()) {
        Thread thread = Thread.currentThread();
        LOG.trace("The [" + thread + "] going to ready to rest " + millis + " millisecond");
      }
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      if (LOG.isErrorEnabled()) {
        Thread thread = Thread.currentThread();
        LOG.error(thread + " was interrupted when it is resting", e);
      }
    }
  }

  /**
   * 开启线程
   *
   * @param threads 线程
   */
  public static void startThread(@Nullable final Thread... threads) {
    if (null == threads || threads.length == 0) {
      return;
    }
    for (Thread thread : threads) {
      if (null == thread) {
        continue;
      }
      try {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Start thread [" + thread + "]");
        }
        thread.start();
      } catch (Exception e) {
        if (LOG.isErrorEnabled()) {
          LOG.error("Error start thread [" + thread + "]", e);
        }
      }
    }
  }

  /**
   * join线程
   *
   * @param threads 线程
   */
  public static void joinThread(@Nullable final Thread... threads) {
    if (null == threads || threads.length == 0) {
      return;
    }
    for (Thread thread : threads) {
      if (null == thread) {
        continue;
      }
      try {
        thread.join();
        if (LOG.isInfoEnabled()) {
          LOG.info(String.format("Join thread [%s]", thread));
        }
      } catch (Exception e) {
        if (LOG.isErrorEnabled()) {
          LOG.error(String.format("Error join thread [%s]", thread), e);
        }
      }
    }
  }

  /**
   * join线程
   *
   * @param waitMillis 等待毫秒数
   * @param threads 线程
   */
  public static void joinThread(final long waitMillis, @Nullable final Thread... threads) {
    if (null == threads || threads.length == 0) {
      return;
    }
    for (Thread thread : threads) {
      if (null == thread) {
        continue;
      }
      try {
        thread.join(waitMillis);
        if (LOG.isInfoEnabled()) {
          LOG.info(String.format("Join thread [%s] after %d millisecond", thread, waitMillis));
        }
      } catch (Exception e) {
        if (LOG.isErrorEnabled()) {
          LOG.error(
              String.format("Error join thread [%s] after %d millisecond", thread, waitMillis), e);
        }
      }
    }
  }

  public static void startAndWaitTerminated(@Nonnull final Thread... threads)
      throws InterruptedException {
    for (Thread t : threads) {
      if (t == null) {
        continue;
      }
      t.start();
      if (LOG.isDebugEnabled()) {
        LOG.debug("The " + t + " Starting...");
      }
    }
    for (Thread t : threads) {
      if (t == null) {
        continue;
      }
      t.join();
      if (LOG.isDebugEnabled()) {
        LOG.debug("The " + t + " Joining...");
      }
    }
  }

  public static void startAndWaitTerminated(@Nonnull final Iterable<Thread> threads)
      throws InterruptedException {
    for (Thread t : threads) {
      if (t == null) {
        continue;
      }
      t.start();
      if (LOG.isDebugEnabled()) {
        LOG.debug("The " + t + " Starting...");
      }
    }
    for (Thread t : threads) {
      if (t == null) {
        continue;
      }
      t.join();
      if (LOG.isDebugEnabled()) {
        LOG.debug("The " + t + " Starting...");
      }
    }
  }
  // ---------------------------------------------------------------------------
}

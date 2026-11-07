package pxf.toolkit.basic.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭操作
 *
 * @author potatoxf
 * @date 2021/3/5
 */
public final class Close {

  private Close() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 关闭可关闭对象
   *
   * @param closeable 可关闭对象
   */
  public static void closeable(Closeable closeable) {
    try {
      closeable.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 关闭可关闭对象
   *
   * @param closeables 一系列可关闭对象
   */
  public static void closeables(Closeable... closeables) {
    for (Closeable closeable : closeables) {
      Close.closeable(closeable);
    }
  }

  /**
   * 静默关闭可关闭对象
   *
   * @param closeable 可关闭对象
   */
  public static void closeableSilently(Closeable closeable) {
    try {
      closeable.close();
    } catch (IOException ignored) {
    }
  }

  /**
   * 静默关闭可关闭对象
   *
   * @param closeables 一系列可关闭对象
   */
  public static void closeablesSilently(Closeable... closeables) {
    for (Closeable closeable : closeables) {
      Close.closeableSilently(closeable);
    }
  }
}

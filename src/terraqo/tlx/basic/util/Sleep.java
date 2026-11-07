package pxf.toolkit.basic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 睡眠操作
 *
 * @author potatoxf
 * @date 2021/3/28
 */
public final class Sleep {

  private static final Logger LOG = LoggerFactory.getLogger(Sleep.class);

  private Sleep() throws IllegalAccessException {
    throw new IllegalAccessException(
        "The instance creation is not allowed,because this is static method utils class");
  }

  /**
   * 睡眠固定毫秒数
   *
   * @param millis 毫秒
   */
  public static void fixedMillisecond(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      if (LOG.isErrorEnabled()) {
        LOG.error("Sleep Exception", e);
      }
    }
  }
}
